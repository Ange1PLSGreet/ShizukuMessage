package org.cookiebyte.dev.gui.linux;


import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;

import org.cookiebyte.dev.announce.log.UnionLogInterface;
import org.cookiebyte.dev.gui.UnionPropertyGet;
import java.awt.*;

public class GuiMain implements UnionLogInterface {

    public static void Initialize(){
        Thread thread = new Thread( () -> {
            SwingUtilities.invokeLater(() -> {
                try {
                    UIManager.setLookAndFeel(new FlatLightLaf());
                } catch (Exception ex) {
                    System.err.println("Failed to initialize LaF");
                }
                // 创建 GuiMain 实例
                UnionPropertyGet guiMain = new UnionPropertyGet();
                // 创建并显示 GUI
                JFrame frame = new JFrame(guiMain.GetTitleMain());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                // 通过实例调用 GetWidth 方法
                frame.setSize(guiMain.GetWidthMain(), guiMain.GetHeightMain());
                frame.setVisible(true);
                log.info("GUI initialized successfully");
            });
        });
        thread.start();
        try{
            thread.join();
        } catch (InterruptedException e) {
            log.error("Interrupted while waiting for GUI initialization: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }


}

