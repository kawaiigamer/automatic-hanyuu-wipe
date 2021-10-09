
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import hanyuu.ext.interfaces.OCR;
import hanyuu.net.wipe.AbstractWipe;
import hanyuu.net.wipe.Action;
import ui.interfaces.UI;

/**
 *
 * @author Hanyuu Furude
 */
public class HandsOCR implements OCR {

    private UI ui;
    private AbstractWipe w;
    @SuppressWarnings({"WaitWhileNotSynced", "CallToThreadDumpStack"})
    public void recognizeCaptcha(AbstractWipe wipe) {
        try {
            wipe.setLastAction(Action.Recognition);
            ui=wipe.getUI();
            this.w=wipe;
            ui.setCaptcha(wipe);
            synchronized (wipe) {
                wipe.wait();
            }
            
        } catch (Exception e) {
            wipe.setException(e);
            wipe.getThreadManager().handleError(wipe);
        }
    }

    public void onError() 
    {
        ui.updateThreadStatus();
        ui.logError("Неверно введена капча. ["+w.toString()+"]");
    }

    public void onSuccess()
    {
        ui.updateThreadStatus();
    }

    @Override
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
