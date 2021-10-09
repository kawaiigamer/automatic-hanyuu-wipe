package ui.interfaces;

public interface ITray {
    void setUI(UI paramUI);

    void addIcon();

    void trayPrint(String paramString1, String paramString2);

    void reIcon(int paramInt1, int paramInt2);

    void trayPrintError(String paramString1, String paramString2);

    void switchState();
}
