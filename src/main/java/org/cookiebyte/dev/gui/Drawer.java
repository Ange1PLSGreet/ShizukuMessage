package org.cookiebyte.dev.gui;

import com.formdev.flatlaf.FlatLightLaf;
import org.cookiebyte.dev.announce.log.UnionLogInterface;
import org.cookiebyte.dev.gui.desktop.GuiMain;

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
            // 设置按钮面板为透明
            buttonPanel.setOpaque(false);
            for (int i = 0; i < underButtonList.size(); i += 2) {
                imagePath = underButtonList.get(i);
                buttonText = underButtonList.get(i + 1);
                AddButton(buttonPanel);
            }
            // 确保 frame 不为 null 再添加组件
            if (Frame.frame != null) {
                // 获取背景标签
                JLabel backgroundLabel = (JLabel) Frame.frame.getContentPane();
                backgroundLabel.setLayout(new BorderLayout());
                backgroundLabel.add(buttonPanel, BorderLayout.SOUTH);
                Frame.frame.revalidate();
                Frame.frame.repaint();
            } else {
                log.error("Error: frame is null");
            }
        } else {
            log.error("Error: underButtonList is null or empty");
        }
    }

    protected void DrawBackGround(){

        imagePath = GuiMain.unionPropertyGet.GetObjectAttr("background", "image");
        if (imagePath == null || imagePath.isEmpty()) {
            log.error("Background image path is null or empty");
            return;
        }
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        LoadImg(screenSize.width, screenSize.height);

        if (icon != null && Frame.frame != null) {
            JLabel backgroundLabel = new JLabel(icon);
            backgroundLabel.setLayout(new BorderLayout());
            Frame.frame.setContentPane(backgroundLabel);
            Frame.frame.revalidate();
            Frame.frame.repaint();
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
            LoadImg(unionPropertyGet.GetUnderButtonWidth(), unionPropertyGet.GetUnderButtonHeight());
            button = new JButton(buttonText, icon);
            buttonPanel.add(button);
            log.info("Under Button added: " + buttonText + " with image " + imagePath);
        } catch (Exception e) {
            log.error("Failed to load image: " + imagePath + ", error: " + e.getMessage());
        }
    }

    public void LoadImg(int width, int height){
        if (imagePath != null && imagePath.toLowerCase().endsWith(".svg")) {
            icon = loadSvgIconInterface.LoadSvgIcon(imagePath, width, height);
        } else {
            log.error("Unsupported image format: " + imagePath);
        }
    }
}
