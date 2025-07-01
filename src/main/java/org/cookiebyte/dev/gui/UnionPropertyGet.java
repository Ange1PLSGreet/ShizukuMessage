package org.cookiebyte.dev.gui;

import org.cookiebyte.dev.announce.gui.GuiObjectInterface;
import org.cookiebyte.dev.announce.log.UnionLogInterface;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

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
            return nodeList.item(0).getAttributes().getNamedItem(attr).getNodeValue();
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
     * Get Title
     * @return String
     */
    public String GetTitleMain(){
        try{
            UnionPropertyGet unionPropertyGet = new UnionPropertyGet();
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
