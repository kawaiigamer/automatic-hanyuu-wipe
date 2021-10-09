
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import hanyuu.ext.interfaces.OCR;
import hanyuu.net.wipe.AbstractWipe;
import ui.interfaces.UI;
/**
 *
 * @author Hanyuu Furude
 */
public class NoCapOCR implements OCR
{
    private UI ui;
    private AbstractWipe w;
    public void recognizeCaptcha(AbstractWipe w)
    {
        this.w=w;
        this.ui=w.getUI();
        w.setCaptcha(" ");
    }
    public void onError()
    {
        ui.updateThreadStatus(w);
    }
    public void onSuccess()
    {
        ui.updateThreadStatus(w);
    }
    @Override
    public String toString()
    {
        return "OCR for no captcha. lol.";
    }
    public void onLoad()
    {
        System.out.println("Loaded OCR for no captcha. lol.");
    }

    public String getInfo()
    {
        return "OCR for no captcha. lol.";
    }
}
