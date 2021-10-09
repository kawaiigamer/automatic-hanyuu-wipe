import config.Config;
import hanyuu.ext.interfaces.OCR;
import hanyuu.net.wipe.AbstractWipe;
import hanyuu.net.wipe.Action;
import org.apache.xmlrpc.DefaultTypeFactory;
import org.apache.xmlrpc.client.*;
import ui.interfaces.UI;
import utils.CaptchaUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class CaptchaBotOCR implements OCR {
    private UI ui;

    private AbstractWipe w;

    private String id;

    public void recognizeCaptcha(AbstractWipe w) {
        this.w = w;
        this.ui = w.getUI();
        w.setLastAction(Action.Recognition);
        try {
            XmlRpcClient xmlRpcClient = new XmlRpcClient();
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL("http://captchabot.com/rpc/xml.php"));
            config.setEnabledForExtensions(true);
            XmlRpcTransportFactory factory = xmlRpcClient.getTransportFactory();
            if (!Config.noProxy)
                ((XmlRpcSun15HttpTransportFactory) factory).setProxy(w.getProxy().getHost(), w.getProxy().getPort());
            xmlRpcClient.setConfig((XmlRpcClientConfig) config);
            ArrayList<Object> params = new ArrayList();
            DefaultTypeFactory dtf = new DefaultTypeFactory();
            params.add(dtf.createBase64(CaptchaUtils.getScaledCaptchaBase64(Config.scaleW, Config.scaleH, w)));
            params.add("system_key");
            params.add(Config.keyForServices);
            params.add(dtf.createInteger("0"));
            params.add(dtf.createInteger("1"));
            params.add(dtf.createInteger("0"));
            params.add(dtf.createInteger("0"));
            params.add(dtf.createInteger("0"));
            params.add(dtf.createInteger("0"));
            String result = xmlRpcClient.execute("ocr_server::analyze", params).toString();
            try {
                int er = Integer.valueOf(result).intValue();
                this.ui.logError("Ошибка " + er + " во время распознавания, код не получен." + this);
            } catch (Exception e) {
                StringTokenizer st = new StringTokenizer(result, ",");
                this.id = st.nextToken().replace("{id=", "");
                w.setCaptcha(st.nextToken().replace("text=", "").replace("}", "").trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onError() {
        try {
            this.ui.updateThreadStatus(this.w);
            this.ui.logInfo("Captcha invalid,(" + this.w.getCaptcha() + ") reporting to CB " + this.w);
            XmlRpcClient client = new XmlRpcClient();
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL("http://captchabot.com/rpc/xml.php"));
            config.setEnabledForExtensions(true);
            XmlRpcTransportFactory factory = client.getTransportFactory();
            if (!Config.noProxy)
                ((XmlRpcSun15HttpTransportFactory) factory).setProxy(this.w.getProxy().getHost(), this.w.getProxy().getPort());
            client.setConfig((XmlRpcClientConfig) config);
            ArrayList<String> params = new ArrayList();
            DefaultTypeFactory dtf = new DefaultTypeFactory();
            params.add("no");
            params.add(dtf.createInteger(this.id));
            client.execute("ocr_server::ver", params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSuccess() {
        this.ui.updateThreadStatus(this.w);
    }

    public String toString() {
        return getInfo();
    }

    public void onLoad() {
        System.out.println("Loaded CaptchaBot OCR.");
    }

    public String getInfo() {
        return "CaptchaBot OCR.";
    }
}
