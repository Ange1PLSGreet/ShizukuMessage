package org.cookiebyte.dev.gui;

import org.cookiebyte.dev.announce.gui.GuiObjectInterface;
import org.cookiebyte.dev.announce.log.UnionLogInterface;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.File;
import java.util.List;

public class UnionPropertyGet implements UnionLogInterface, GuiObjectInterface {

    public String path = "src/main/resources/gui_property.xml";

    public File xml = new File(path);

    public Document docObj = null;

    public UnionPropertyGet() {
        // Initialize
        try{
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            docObj = dBuilder.parse(xml);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
        }
    }

    /**
     * 获取 gui-property
     */
    private Element GetGuiProperty() {
        return docObj.getDocumentElement();
    }

    @Override
    public NodeList GetObject(String name){
        return GetGuiProperty().getElementsByTagName(name);
    }

    @Override
    public String GetObjectAttr(String name, String attr){
        NodeList nodeList = GetObject(name);
        if (nodeList.getLength() > 0 && nodeList.item(0).getAttributes() != null) {
            // 检查 getNamedItem 的返回值是否为 null
            org.w3c.dom.Node namedItem = nodeList.item(0).getAttributes().getNamedItem(attr);
            if (namedItem != null) {
                return namedItem.getNodeValue();
            }
        }
        return null;
    }

    @Override
    public String GetPropertyName() {
        return GetObjectAttr("property-name", "name");
    }

    @Override
    public String GetWindowTitle() {
        return GetObjectAttr("title", "title");
    }

    @Override
    public String GetNormalServerStatus() {
        return GetObjectAttr("normal", "status");
    }

    @Override
    public String GetErrorServerStatus() {
        return GetObjectAttr("error", "status");
    }

    @Deprecated
    @Override
    public String GetDeviceWidth() {
        return GetObjectAttr("width", "width");
    }

    @Deprecated
    @Override
    public String GetDeviceHeight() {
        return GetObjectAttr("height", "height");
    }

    /**
     * Get UnderButton Width Height
     */
    public int GetUnderButtonWidth(){
        try{
            String width = GetObjectAttr("under-button", "width");
            if (width!= null) {
                return Integer.parseInt(width);
            } else{
                log.error("Error: width is null");
            }
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
        }
        return 1;
    }

    /**
     * Get Hagp
     */
    public int GetUnderButtonHgap(){
        try{
            String hgap = GetObjectAttr("under-button", "hgap");
            if (hgap!= null) {
                return Integer.parseInt(hgap);
            } else{
                log.error("Error: hgap is null");
            }
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
        }
        return 1;
    }

    /**
     * Get Vgap
     */
    public int GetUnderButtonVgap(){
        try{
            String vgap = GetObjectAttr("under-button", "vgap");
            if (vgap!= null) {
                return Integer.parseInt(vgap);
            } else{
                log.error("Error: vgap is null");
            }
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
        }
        return 1;
    }

    /**
     * Get Layout
     */
    public int GetLayout(){
        String layout = GetObjectAttr("under-button", "layout");
        if (layout!= null) {
            int layoutType = Integer.parseInt(layout);
            switch (layoutType){
                case 0:
                    return FlowLayout.LEFT;
                case 1:
                    return FlowLayout.CENTER;
                case 2:
                    return FlowLayout.RIGHT;
                case 3:
                    return FlowLayout.LEADING;
                case 4:
                    return FlowLayout.TRAILING;
            }
        }
        return 1;
    }

    /**
     * Get UnderButton Height
     */
    public int GetUnderButtonHeight() {
        try {
            String height = GetObjectAttr("under-button", "height");
            if (height != null) {
                return Integer.parseInt(height);
            } else {
                log.error("Error: height is null");
            }
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
        }
        return 1;
    }

    /**
     * Get Under Button Object
     * @return underButton A list containing multiple button image paths and texts
     */
    public List<String> GetUnderButton(){
        // Initialize the underButton list
        List<String> underButton = new java.util.ArrayList<>();
        // Get all under-btn elements
        NodeList underBtnNodes = GetObject("under-btn");
        for (int i = 0; i < underBtnNodes.getLength(); i++) {
            org.w3c.dom.Node underBtnNode = underBtnNodes.item(i);
            if (underBtnNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                org.w3c.dom.Element underBtnElement = (org.w3c.dom.Element) underBtnNode;
                String imgPath = underBtnElement.getAttribute("img");
                String btnText = underBtnElement.getAttribute("text");
                if (imgPath == null || imgPath.isEmpty() || btnText == null) {
                    log.error("Error: imgPath or btnText is null or empty for under-btn at index " + i);
                } else {
                    // Add elements to the list
                    underButton.add(imgPath);
                    underButton.add(btnText);
                }
            }
        }
        return underButton;
    }

    /**
     * Get Title
     * @return String
     */
    public String GetTitleMain(){
        try{
            String title = GetWindowTitle();
            if (title!= null) {
                return title;
            } else{
                log.error("Error: title is null");
            }
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
        }
        return null;
    }
}
