package ui;

import javax.swing.*;

public class LookAndFeel {
    public static void initLookAndFeel(String theme) {
        try {
            UIManager.setLookAndFeel(theme);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
    }
}
