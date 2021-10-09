import hanyuu.ext.interfaces.WipeExtension;
import hanyuu.net.HTTPClient;
import hanyuu.net.wipe.AbstractWipe;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import utils.Utils;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;

public class UrinachCaptchaFix implements WipeExtension {
    private HashMap<AbstractWipe, CapData> threads = new HashMap<>();

    private class CapData {
        public boolean cap_request = false;

        public AbstractWipe wipe = null;

        public HTTPClient client = null;

        public String key = "";

        public CapData(AbstractWipe wipe) {
            this.wipe = wipe;
        }
    }

    public void onLoad() {
        System.out.println("Loaded Urinach Captcha Fix  script.");
    }

    public void onRunWipe(AbstractWipe w) {
    }

    public void onStopWipe(AbstractWipe w) {
    }

    public Object[] onGetRequest(AbstractWipe w, String url, HttpHost host, ArrayList<String> headers, boolean saveCookies) {
        CapData data = this.threads.get(w);
        if (data == null)
            data = new CapData(w);
        data.cap_request = url.contains("/MocheCaptcha");
        this.threads.put(w, data);
        return null;
    }

    public Object[] onPostRequest(AbstractWipe w, String url, HttpHost host, MultipartEntity me, ArrayList<String> headers) {
        CapData data = this.threads.get(w);
        if (url.contains("2ch.so"))
            me.addPart("recaptcha_challenge_field", (ContentBody) Utils.sb(data.key));
        return null;
    }

    public HttpResponse onGetAnswer(AbstractWipe w, HttpResponse hr) {
        CapData data = this.threads.get(w);
        if (data.cap_request && hr.getStatusLine().getStatusCode() == 404) {
            BufferedReader br = data.wipe.getHttpClient().getBufferedReader("http://api.recaptcha.net/challenge?k=6LdOEMMSAAAAAIGhmYodlkflEb2C-xgPjyATLnxx&lang=en");
            try {
                br.readLine();
                br.readLine();
                br.readLine();
                data.key = br.readLine();
                data.key = data.key.replaceAll("challenge : '", "").replaceAll("',", "").trim();
                HttpGet get = new HttpGet("http://www.google.com/recaptcha/api/image?c=" + data.key);
                return data.wipe.getHttpClient().createClient().execute((HttpUriRequest) get);
            } catch (Exception e) {
                data.wipe.setException(e);
                data.wipe.getThreadManager().handleError(data.wipe);
            }
        }
        return hr;
    }

    public HttpResponse onPostAnswer(AbstractWipe w, HttpResponse hr) {
        return hr;
    }

    public String getInfo() {
        return "Urinach Captcha Fix .";
    }

    private static void p(Object o) {
        System.out.println(o);
    }
}
