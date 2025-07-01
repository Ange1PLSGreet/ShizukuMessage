package org.cookiebyte.dev.announce.gui;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public interface GuiObjectInterface {


    /**
     * 获取 gui-property 下的元素
     * @param name 元素名称
     */
    public NodeList GetObject(String name);

    /**
     * 获取 gui-property 下的元素的属性
     * @param name 元素名称
     * @param attr 属性名称
     */
    public String GetObjectAttr(String name, String attr);

    public String GetPropertyName();

    public String GetWindowTitle();

    public String GetNormalServerStatus();

    public String GetErrorServerStatus();

    public String GetDeviceWidth();

    public String GetDeviceHeight();
}
