package org.cookiebyte.dev.image;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.bridge.*;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.cookiebyte.dev.announce.image.LoadSvgIconInterface;
import org.cookiebyte.dev.announce.log.UnionLogInterface;
import org.w3c.dom.svg.SVGDocument;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LoadSvgIconInterfaceImpl implements LoadSvgIconInterface, UnionLogInterface {

    @Override
    /**
     * 加载 SVG 图片并转换为 Icon
     * @param svgPath SVG 文件路径
     * @param width 图标宽度
     * @param height 图标高度
     * @return Icon 对象
     * @throws  文件读取异常
     */
    public Icon LoadSvgIcon(String svgPath, int width, int height) {
        try{
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
            SVGDocument document = factory.createSVGDocument(new File(svgPath).toURI().toString());

            UserAgent userAgent = new UserAgentAdapter();
            DocumentLoader loader = new DocumentLoader(userAgent);
            BridgeContext ctx = new BridgeContext(userAgent, loader);
            ctx.setDynamicState(BridgeContext.DYNAMIC);
            GVTBuilder builder = new GVTBuilder();
            GraphicsNode root = builder.build(ctx, document);

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();
            // 设置渲染提示
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            g2d.scale((double) width / document.getRootElement().getWidth().getBaseVal().getValue(),
                    (double) height / document.getRootElement().getHeight().getBaseVal().getValue());
            root.paint(g2d);
            g2d.dispose();

            return new ImageIcon(image);
        } catch (IOException e) {
            log.error("Failed to load SVG icon: " + e.getMessage());
            return null;
        }
    }

}