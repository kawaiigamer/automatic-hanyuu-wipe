package ui.gui;

import config.Config;
import hanyuu.managers.ThreadManager;
import ui.interfaces.ITray;
import ui.interfaces.UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Tray implements ITray {
    private SystemTray tray;

    private MenuItem start;

    private MenuItem stop;

    private final PopupMenu popup = new PopupMenu();

    private UI g;

    private final ThreadManager tm;

    private Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/ui/res/tray.png"));

    private TrayIcon trayIcon = new TrayIcon(this.image, "Ханюша:" + Config.chanName + "\nУспешных постов: 0\nНеудачных постов: 0", this.popup);

    public Tray(ThreadManager t) {
        this.tm = t;
        if (SystemTray.isSupported()) {
            MouseListener mouseListener = new MouseListener() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == 1) {
                        Tray.this.g.setVisible(true);
                        Tray.this.g.seWindowtState(0);
                    }
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
            this.trayIcon.addMouseListener(mouseListener);
            this.tray = SystemTray.getSystemTray();
            this.popup.setFont(new Font("Tahoma", 1, 11));
            ActionListener exitListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            };
            ActionListener openListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Tray.this.g.setVisible(true);
                    Tray.this.g.seWindowtState(0);
                }
            };
            ActionListener StartStopListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Tray.this.g.SwitchStartStop();
                    try {
                        Tray.this.tm.StartStopManage();
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                    Tray.this.switchState();
                    if (Tray.this.tm.isWork()) {
                        Tray.this.trayPrint("Ханюша", "Запущена");
                    } else {
                        Tray.this.trayPrint("Ханюша", "Остановлена");
                    }
                }
            };
            MenuItem exit = new MenuItem("Выйти");
            MenuItem open = new MenuItem("Развернуть");
            this.start = new MenuItem("Запустить");
            this.stop = new MenuItem("Остановить");
            exit.addActionListener(exitListener);
            open.addActionListener(openListener);
            this.start.addActionListener(StartStopListener);
            this.stop.addActionListener(StartStopListener);
            this.popup.add(open);
            this.popup.add(this.start);
            this.popup.add(this.stop);
            this.popup.add(exit);
            this.trayIcon.setImageAutoSize(true);
        } else {
            System.err.println("System tray is currently not supported.");
        }
    }

    public void addIcon() {
        try {
            switchState();
            this.tray.add(this.trayIcon);
        } catch (AWTException e) {
            System.err.println("TrayIcon could not be added.");
        }
    }

    public void setImage() {
        this.trayIcon.setImage(this.image);
    }

    public void trayPrint(String title, String s) {
        if (Config.dontTrayMsg)
            return;
        this.trayIcon.displayMessage(title, s, TrayIcon.MessageType.INFO);
    }

    public void trayPrintError(String title, String s) {
        if (Config.dontTrayMsg)
            return;
        this.trayIcon.displayMessage(title, s, TrayIcon.MessageType.ERROR);
    }

    public void reIcon(int s, int f) {
        this.trayIcon.setToolTip("Ханюша:" + Config.chanName + "\nУспешных постов: " + s + "\nНеудачных постов: " + f);
    }

    public void switchState() {
        this.start.setEnabled(!this.tm.isWork());
        this.stop.setEnabled(this.tm.isWork());
    }

    public void setUI(UI ui) {
        this.g = ui;
    }
}
