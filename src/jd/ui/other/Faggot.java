package ui.other;

import hanyuu.managers.ThreadManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Faggot extends JWindow {
    private static Faggot _instance = null;

    private static String path = "/ui/res/faggot.jpg";

    private final Image _image;

    private final int _imageWidth;

    private final int _imageHeight;

    private JLabel hanyuu;

    public Faggot() {
        this._image = Toolkit.getDefaultToolkit().getImage(getClass().getResource(path));
        this._imageWidth = 400;
        this._imageHeight = 300;
        this.hanyuu = new JLabel();
        _instance = this;
    }

    public static Faggot getinstance() {
        if (_instance == null)
            new Faggot();
        return _instance;
    }

    public void display() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - this._imageWidth) / 2;
        int y = (screenSize.height - this._imageHeight) / 2;
        this.hanyuu.setIcon(new ImageIcon(this._image));
        setBounds(x, y, this._imageWidth, this._imageHeight);
        MouseListener mouseListener = new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                Faggot.this.closeIt();
                ThreadManager.getInstance().getUIManager().getUI().setVisible(true);
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }
        };
        add(this.hanyuu);
        this.hanyuu.addMouseListener(mouseListener);
        setVisible(true);
    }

    public void closeIt() {
        setVisible(false);
        dispose();
    }
}
