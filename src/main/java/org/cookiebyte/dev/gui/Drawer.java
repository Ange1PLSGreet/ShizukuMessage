package org.cookiebyte.dev.gui;

import com.formdev.flatlaf.FlatLightLaf;
import org.cookiebyte.dev.announce.log.UnionLogInterface;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static org.cookiebyte.dev.gui.desktop.GuiMain.loadSvgIconInterface;
import static org.cookiebyte.dev.gui.desktop.GuiMain.unionPropertyGet;

public class Drawer implements UnionLogInterface {

    public Icon icon = null;

    protected String imagePath;

    protected String buttonText;

    public JButton button = new JButton(buttonText, icon);

    /**
     * 只绘制底部按钮
     */
    protected void DrawUnderButtons() {
        List<String> underButtonList = unionPropertyGet.GetUnderButton();
        if (underButtonList != null && !underButtonList.isEmpty()) {
            JPanel buttonPanel = new JPanel(new FlowLayout(unionPropertyGet.GetLayout(), unionPropertyGet.GetUnderButtonHgap(), unionPropertyGet.GetUnderButtonVgap()));
            for (int i = 0; i < underButtonList.size(); i += 2) {
                imagePath = underButtonList.get(i);
                buttonText = underButtonList.get(i + 1);
                AddButton(buttonPanel);
            }
            // 确保 frame 不为 null 再添加组件
            if (Frame.frame != null) {
                Frame.frame.add(buttonPanel, BorderLayout.SOUTH);
                Frame.frame.revalidate();
                Frame.frame.repaint();
            } else {
                log.error("Error: frame is null");
            }
        } else {
            log.error("Error: underButtonList is null or empty");
        }
    }

    public void InitPlatPaf(){
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            log.error("Failed to initialize LaF");
        }
    }

    public void AddButton(JPanel buttonPanel){
        try {
            LoadImg();
            button = new JButton(buttonText, icon);
            buttonPanel.add(button);
            log.info("Under Button added: " + buttonText + " with image " + imagePath);
        } catch (Exception e) {
            log.error("Failed to load image: " + imagePath + ", error: " + e.getMessage());
        }
    }

    public void LoadImg(){
        if (imagePath.toLowerCase().endsWith(".svg")) {
            icon = loadSvgIconInterface.LoadSvgIcon(imagePath, unionPropertyGet.GetUnderButtonWidth(), unionPropertyGet.GetUnderButtonHeight());
        } else {
            log.error("Unsupported image format: " + imagePath);
        }
    }
}
