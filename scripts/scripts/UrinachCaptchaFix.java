/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import hanyuu.ext.interfaces.WipeExtension;
import java.util.HashMap;
import org.apache.http.HttpHost;
import org.apache.http.Header;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.HttpResponse;
import hanyuu.net.wipe.AbstractWipe;
import hanyuu.net.HTTPClient;
import java.io.BufferedReader;
import java.util.regex.Pattern;
import hanyuu.net.wipe.Wipe;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import org.apache.http.HttpVersion;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.params.HttpParams;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLSocketFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.TrustManager;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import hanyuu.net.proxy.HttpProxy;
import hanyuu.ext.ScriptContainer;
import ui.interfaces.UI;
import utils.Utils;

/**
 *
 * @author Hanyuu
 */
public class UrinachCaptchaFix implements WipeExtension {

    private HashMap<AbstractWipe, CapData> threads=new HashMap<AbstractWipe, CapData>();
    
    private class CapData{
        
        public boolean cap_request = false;
        public AbstractWipe wipe = null;
        public HTTPClient client = null;
        public String key="";
        
        public CapData(AbstractWipe wipe){
            this.wipe=wipe;
        }
    }

    /**Метод будет вызван после загрузки скрипта в Ханю*/
    public void onLoad() {
        System.out.println("Loaded Urinach Captcha Fix  script.");
    }

    /**Метод будет вызван при запуске потока*/
    public void onRunWipe(AbstractWipe w) {


    }

    /**Метод будет вызван при остновке потока*/
    public void onStopWipe(AbstractWipe w) {
    }

    /**Эти методы позволяют модифицировть данные от потока Wipe для Get запроса через скрипт, если не нужно то не изменяйте или верните null<br/>
     *   Страрайтесь что бы скрипты не запороли друг друга.
     * Хедеры: headers.add("Name,Value");
     */
    public Object[] onGetRequest(AbstractWipe w, String url, HttpHost host, ArrayList<String> headers, boolean saveCookies) {
        CapData data=threads.get(w);
        if(data==null)
            data=new CapData(w);
        data.cap_request = url.contains("/MocheCaptcha");
        threads.put(w, data);
        return null;
    }

    /**Эти методы позволяют модифицировть данные от потока Wipe для Post запроса через скрипт, если не нужно то не изменяйте или верните null<br/>
     *  Страрайтесь что бы скрипты не запороли друг друга.
     */
    public Object[] onPostRequest(AbstractWipe w, String url, HttpHost host, MultipartEntity me, ArrayList<String> headers) {
        CapData data=threads.get(w);
        if(url.contains("2ch.so"))
            me.addPart("recaptcha_challenge_field", Utils.sb(data.key));
        return null;
    }

    /**Срабатывает после получения ответа на Get запрос и предоставляет ответ для обработки и/или модификации.*/
    public HttpResponse onGetAnswer(AbstractWipe w, HttpResponse hr) {
        CapData data=threads.get(w);
        if (data.cap_request && hr.getStatusLine().getStatusCode() == 404) {
            BufferedReader br = data.wipe.getHttpClient().getBufferedReader("http://api.recaptcha.net/challenge?k=6LdOEMMSAAAAAIGhmYodlkflEb2C-xgPjyATLnxx&lang=en");
            try {
                br.readLine();
                br.readLine();
                br.readLine();
                data.key=br.readLine();
                data.key=data.key.replaceAll("challenge : '", "").replaceAll("',", "").trim();
                HttpGet get = new HttpGet("http://www.google.com/recaptcha/api/image?c="+data.key);
                return data.wipe.getHttpClient().createClient().execute(get);
            } catch (Exception e) {
               data.wipe.setException(e);
               data.wipe.getThreadManager().handleError(data.wipe);
            }
        }
        return hr;
    }

    /**Срабатывает после получения отверта на Post запрос и предоставляет ответ для обработки и/или модификации.*/
    public HttpResponse onPostAnswer(AbstractWipe w, HttpResponse hr) {
        return hr;
    }

    /**Возвращает описание скрипта*/
    public String getInfo() {
        return "Urinach Captcha Fix .";
    }

    private static void p(Object o) {
        System.out.println(o);
    }
}
