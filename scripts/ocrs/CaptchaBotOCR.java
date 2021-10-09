/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import hanyuu.ext.interfaces.OCR;
import ui.interfaces.UI;
import hanyuu.net.wipe.AbstractWipe;
import config.Config;
import org.apache.xmlrpc.DefaultTypeFactory;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcSun15HttpTransportFactory;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcTransportFactory;

import utils.CaptchaUtils;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;
import hanyuu.net.wipe.Action;

/**
 *
 * @author Hanyuu Furude
 * This is very derty code.
 * Please refactor this, sambody.
 */
@SuppressWarnings("unchecked")
public class CaptchaBotOCR implements OCR {

    private UI ui;
    private AbstractWipe w;
    private String id;

    @SuppressWarnings("CallToThreadDumpStack")
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

            if (!Config.noProxy) {
                ((XmlRpcSun15HttpTransportFactory) factory).setProxy(w.getProxy().getHost(), w.getProxy().getPort());
            }
            xmlRpcClient.setConfig(config);
            ArrayList params = new ArrayList<Object>();
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
                int er = Integer.valueOf(result);
                ui.logError("Ошибка " + er + " во время распознавания, код не получен." + this);
            } catch (Exception e) {
                StringTokenizer st = new StringTokenizer(result, ",");
                id = st.nextToken().replace("{id=", "");
                w.setCaptcha(st.nextToken().replace("text=", "").replace("}", "").trim());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("CallToThreadDumpStack")
    public void onError() {
        try {
            ui.updateThreadStatus(w);
            ui.logInfo("Captcha invalid,(" + w.getCaptcha() + ") reporting to CB " + w);
            XmlRpcClient client = new XmlRpcClient();
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL("http://captchabot.com/rpc/xml.php"));
            config.setEnabledForExtensions(true);
            XmlRpcTransportFactory factory = client.getTransportFactory();
            if (!Config.noProxy) {
                ((XmlRpcSun15HttpTransportFactory) factory).setProxy(w.getProxy().getHost(), w.getProxy().getPort());
            }
            client.setConfig(config);
            ArrayList params = new ArrayList<Object>();
            DefaultTypeFactory dtf = new DefaultTypeFactory();
            params.add("no");
            params.add(dtf.createInteger(id));
            client.execute("ocr_server::ver", params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSuccess() 
    {
        ui.updateThreadStatus(w);
    }

    @Override
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
