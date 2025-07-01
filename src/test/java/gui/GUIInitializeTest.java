package gui;

import org.cookiebyte.dev.gui.desktop.GuiMain;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertNotNull;

public class GUIInitializeTest {

    @Test
    public void InitTest(){
        GuiMain guiMain = new GuiMain();
        GuiMain.Initialize();
        assertNotNull(guiMain);
    }
}
