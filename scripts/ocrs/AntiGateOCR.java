
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import hanyuu.ext.interfaces.OCR;
import ui.interfaces.UI;
import hanyuu.net.wipe.AbstractWipe;
import org.apache.http.entity.mime.MultipartEntity;

import java.io.BufferedReader;
import utils.CaptchaUtils;
import config.Config;
import utils.Utils;
import hanyuu.net.wipe.Action;

/**
 *
 * @author Hanyuu Furude
 */
public class AntiGateOCR implements OCR {

    private UI ui;
    private AbstractWipe wipe;
    private String id;
    private String postToAC = "http://antigate.com/in.php";
    private String urlGetC = "http://antigate.com/res.php?key=" + Config.keyForServices + "&action=get&id=";
    private String report = "http://antigate.com/res.php?key=" + Config.keyForServices + "&action=reportbad&id=";

    public void recognizeCaptcha(AbstractWipe w) {
        ui = w.getUI();
        this.wipe = w;
        w.setCaptcha(AntiCaptchaComOCR(CaptchaUtils.getScaledCaptchaBase64(Config.scaleW, Config.scaleH, w)));
    }

    @SuppressWarnings("CallToThreadDumpStack")
    public void onError() {
        try {
            ui.updateThreadStatus(wipe);
            ui.logError("Invalid CAPTCHA. Reporting... [" + wipe.toString() + "]");
            ui.logInfo(wipe.getHttpClient().getBufferedReader(report + id).readLine());
        } catch (Exception e) {
            e.printStackTrace();
            ui.logError(e.toString());
        }
    }

    public void onSuccess() 
    {
        ui.updateThreadStatus(wipe);
    }

    @Override
    public String toString() {
        return getInfo();
    }

    public void onLoad() {
        System.out.println("Loaded antigate OCR.");
    }

    public String getInfo() {
        return "Antigate OCR.";
    }

    @SuppressWarnings("CallToThreadDumpStack")
    public String AntiCaptchaComOCR(String base64Captcha) {
        try {
            wipe.setLastAction(Action.Recognition);
            MultipartEntity me = new MultipartEntity();
            if (base64Captcha == null || base64Captcha.length() < 5) {
                wipe.destroy("No captcha img, may be Iptables?:3");
                wipe.getThreadManager().getProxyManager().delete(wipe.getProxy(), ui);
            }
            me.addPart("method", Utils.sb("base64"));
            me.addPart("key", Utils.sb(Config.keyForServices));
            me.addPart("body", Utils.sb(base64Captcha));
            me.addPart("ext", Utils.sb(wipe.getChan().CaptchaType));
            me.addPart("phrase", Utils.sb("0"));
            me.addPart("regsense", Utils.sb("0"));
            me.addPart("numeric", Utils.sb("2"));
            me.addPart("calc", Utils.sb("0"));
            me.addPart("min_len", Utils.sb("2"));
            me.addPart("max_len", Utils.sb("20"));

            BufferedReader br = wipe.getHttpClient().postBufferedReader(postToAC, me);
            String s = br.readLine();
            byte iter = 0;
            if (s == null) {
                AntiCaptchaComOCR(CaptchaUtils.getScaledCaptchaBase64(Config.scaleW, Config.scaleH, wipe));
            }
            if (s.contains("OK")) {
                id = s.substring(3);
                while (wipe.isWork()) {
                    BufferedReader br2 = wipe.getHttpClient().getBufferedReader(urlGetC + id);
                    String c = br2.readLine();

                    if (c.contains("CAPCHA_NOT_READY")) {
                        iter++;
                        if (iter == 45) {
                            ui.logError("CAPTCHA not recognize, getting new..");
                            AntiCaptchaComOCR(CaptchaUtils.getScaledCaptchaBase64(Config.scaleW, Config.scaleH, wipe));
                        }
                        wipe.getThreadManager().sleepWipe(400, wipe);//Столько козявок было в носу:3
                    } else if (c.contains("OK")) {
                        return c.substring(3).toLowerCase().trim();
                    }

                }
            }
            if (s.contains("ERROR_IMAGE_IS_NOT")) {
                AntiCaptchaComOCR(CaptchaUtils.getScaledCaptchaBase64(Config.scaleW, Config.scaleH, wipe));
            } else if (s.contains("ERROR_NO_SLOT_AVAILABLE")) {
                wipe.getThreadManager().sleepWipe(400, wipe);
                AntiCaptchaComOCR(CaptchaUtils.getScaledCaptchaBase64(Config.scaleW, Config.scaleH, wipe));
            } else if (s.contains("ERROR_NO_SUCH_CAPCHA_ID")) {
                AntiCaptchaComOCR(CaptchaUtils.getScaledCaptchaBase64(Config.scaleW, Config.scaleH, wipe));
            } else if (s.isEmpty() || !s.contains("ERROR") || !s.contains("OK")) {
                ui.logError(s);
                wipe.setError(true);
                wipe.getThreadManager().destroyThread(wipe, " answer from AC is bad.");
            }
        } catch (Exception e) {
            wipe.setException(e);
            wipe.getThreadManager().handleError(wipe);
        }
        return "";
    }
}
