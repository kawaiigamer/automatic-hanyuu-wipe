import hanyuu.ext.interfaces.OCR;
import hanyuu.net.wipe.AbstractWipe;
import hanyuu.net.wipe.Action;
import ui.interfaces.UI;

public class HandsOCR implements OCR {
    private UI ui;

    private AbstractWipe w;

    public void recognizeCaptcha(AbstractWipe wipe) {
        try {
            wipe.setLastAction(Action.Recognition);
            this.ui = wipe.getUI();
            this.w = wipe;
            this.ui.setCaptcha(wipe);
            synchronized (wipe) {
                wipe.wait();
            }
        } catch (Exception e) {
            wipe.setException(e);
            wipe.getThreadManager().handleError(wipe);
        }
    }

    public void onError() {
        this.ui.updateThreadStatus();
        this.ui.logError("Неверно введена капча. [" + this.w.toString() + "]");
    }

    public void onSuccess() {
        this.ui.updateThreadStatus();
    }

    public String toString() {
        return getInfo();
    }

    public void onLoad() {
        System.out.println("Loaded hands OCR.");
    }

    public String getInfo() {
        return "Hands OCR.";
    }
}
