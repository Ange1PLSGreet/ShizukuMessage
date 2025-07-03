package org.cookiebyte.dev.gui.desktop;

import org.cookiebyte.dev.announce.log.UnionLogInterface;
import org.cookiebyte.dev.gui.Drawer;
import org.cookiebyte.dev.gui.UnionPropertyGet;
import org.cookiebyte.dev.image.LoadSvgIconInterfaceImpl;
import org.cookiebyte.dev.announce.gui.GuiMainInterface;
import org.cookiebyte.dev.gui.Frame;

import javax.swing.*;
import java.awt.*;

public class GuiMain extends Drawer implements UnionLogInterface, GuiMainInterface {

    public static final UnionPropertyGet unionPropertyGet = new UnionPropertyGet();

    private static final GuiMain guiMain = new GuiMain();

    public static final LoadSvgIconInterfaceImpl loadSvgIconInterface = new LoadSvgIconInterfaceImpl();

    public int screenWidth;

    public int screenHeight;

    @Override
    public void Initialize() {
        Thread thread = new Thread(() -> {
            SwingUtilities.invokeLater(() -> {
                InitPlatPaf();
                JFrame frame = new JFrame(unionPropertyGet.GetTitleMain());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                screenWidth = screenSize.width / 2;
                screenHeight = screenSize.height / 2;
                frame.setSize(screenWidth, screenHeight);
                frame.setVisible(true);
                log.info("GUI initialized successfully");
                Frame.frame = frame;
                DrawBackGround();
                DrawUnderButtons();
            });
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            log.error("Interrupted while waiting for GUI initialization: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args){
        guiMain.Initialize();
    }
}
