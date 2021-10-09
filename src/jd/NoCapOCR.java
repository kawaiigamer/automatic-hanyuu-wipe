import hanyuu.ext.interfaces.OCR;
import hanyuu.net.wipe.AbstractWipe;
import ui.interfaces.UI;

public class NoCapOCR implements OCR {
    private UI ui;

    private AbstractWipe w;

    public void recognizeCaptcha(AbstractWipe w) {
        this.w = w;
        this.ui = w.getUI();
        w.setCaptcha(" ");
    }

    public void onError() {
        this.ui.updateThreadStatus(this.w);
    }

    public void onSuccess() {
        this.ui.updateThreadStatus(this.w);
    }

    public String toString() {
        return "OCR for no captcha. lol.";
    }

    public void onLoad() {
        System.out.println("Loaded OCR for no captcha. lol.");
    }

    public String getInfo() {
        return "OCR for no captcha. lol.";
    }
}
