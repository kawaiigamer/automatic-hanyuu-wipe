import config.Config;
import hanyuu.ext.interfaces.OCR;
import hanyuu.net.wipe.AbstractWipe;
import hanyuu.net.wipe.Action;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import ui.interfaces.UI;
import utils.CaptchaUtils;
import utils.Utils;

import java.io.BufferedReader;

public class AntiGateOCR implements OCR {
    private UI ui;

    private AbstractWipe wipe;

    private String id;

    private String postToAC = "http://antigate.com/in.php";

    private String urlGetC = "http://antigate.com/res.php?key=" + Config.keyForServices + "&action=get&id=";

    private String report = "http://antigate.com/res.php?key=" + Config.keyForServices + "&action=reportbad&id=";

    public void recognizeCaptcha(AbstractWipe w) {
        this.ui = w.getUI();
        this.wipe = w;
        w.setCaptcha(AntiCaptchaComOCR(CaptchaUtils.getScaledCaptchaBase64(Config.scaleW, Config.scaleH, w)));
    }

    public void onError() {
        try {
            this.ui.updateThreadStatus(this.wipe);
            this.ui.logError("Invalid CAPTCHA. Reporting... [" + this.wipe.toString() + "]");
            this.ui.logInfo(this.wipe.getHttpClient().getBufferedReader(this.report + this.id).readLine());
        } catch (Exception e) {
            e.printStackTrace();
            this.ui.logError(e.toString());
        }
    }

    public void onSuccess() {
        this.ui.updateThreadStatus(this.wipe);
    }

    public String toString() {
        return getInfo();
    }

    public void onLoad() {
        System.out.println("Loaded antigate OCR.");
    }

    public String getInfo() {
        return "Antigate OCR.";
    }

    public String AntiCaptchaComOCR(String base64Captcha) {
        try {
            this.wipe.setLastAction(Action.Recognition);
            MultipartEntity me = new MultipartEntity();
            if (base64Captcha == null || base64Captcha.length() < 5) {
                this.wipe.destroy("No captcha img, may be Iptables?:3");
                this.wipe.getThreadManager().getProxyManager().delete(this.wipe.getProxy(), this.ui);
            }
            me.addPart("method", (ContentBody) Utils.sb("base64"));
            me.addPart("key", (ContentBody) Utils.sb(Config.keyForServices));
            me.addPart("body", (ContentBody) Utils.sb(base64Captcha));
            me.addPart("ext", (ContentBody) Utils.sb((this.wipe.getChan()).CaptchaType));
            me.addPart("phrase", (ContentBody) Utils.sb("0"));
            me.addPart("regsense", (ContentBody) Utils.sb("0"));
            me.addPart("numeric", (ContentBody) Utils.sb("2"));
            me.addPart("calc", (ContentBody) Utils.sb("0"));
            me.addPart("min_len", (ContentBody) Utils.sb("2"));
            me.addPart("max_len", (ContentBody) Utils.sb("20"));
            BufferedReader br = this.wipe.getHttpClient().postBufferedReader(this.postToAC, me);
            String s = br.readLine();
            byte iter = 0;
            if (s == null)
                AntiCaptchaComOCR(CaptchaUtils.getScaledCaptchaBase64(Config.scaleW, Config.scaleH, this.wipe));
            if (s.contains("OK")) {
                this.id = s.substring(3);
                while (this.wipe.isWork()) {
                    BufferedReader br2 = this.wipe.getHttpClient().getBufferedReader(this.urlGetC + this.id);
                    String c = br2.readLine();
                    if (c.contains("CAPCHA_NOT_READY")) {
                        iter = (byte) (iter + 1);
                        if (iter == 45) {
                            this.ui.logError("CAPTCHA not recognize, getting new..");
                            AntiCaptchaComOCR(CaptchaUtils.getScaledCaptchaBase64(Config.scaleW, Config.scaleH, this.wipe));
                        }
                        this.wipe.getThreadManager().sleepWipe(400L, this.wipe);
                        continue;
                    }
                    if (c.contains("OK"))
                        return c.substring(3).toLowerCase().trim();
                }
            }
            if (s.contains("ERROR_IMAGE_IS_NOT")) {
                AntiCaptchaComOCR(CaptchaUtils.getScaledCaptchaBase64(Config.scaleW, Config.scaleH, this.wipe));
            } else if (s.contains("ERROR_NO_SLOT_AVAILABLE")) {
                this.wipe.getThreadManager().sleepWipe(400L, this.wipe);
                AntiCaptchaComOCR(CaptchaUtils.getScaledCaptchaBase64(Config.scaleW, Config.scaleH, this.wipe));
            } else if (s.contains("ERROR_NO_SUCH_CAPCHA_ID")) {
                AntiCaptchaComOCR(CaptchaUtils.getScaledCaptchaBase64(Config.scaleW, Config.scaleH, this.wipe));
            } else if (s.isEmpty() || !s.contains("ERROR") || !s.contains("OK")) {
                this.ui.logError(s);
                this.wipe.setError(true);
                this.wipe.getThreadManager().destroyThread(this.wipe, " answer from AC is bad.");
            }
        } catch (Exception e) {
            this.wipe.setException(e);
            this.wipe.getThreadManager().handleError(this.wipe);
        }
        return "";
    }
}
