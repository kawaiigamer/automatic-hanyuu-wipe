package hanyuu.net;

import hanyuu.ext.ScriptContainer;
import hanyuu.net.proxy.HttpProxy;
import hanyuu.net.wipe.AbstractWipe;
import hanyuu.net.wipe.Wipe;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import ui.interfaces.UI;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;

public class HTTPClient {
    private String proxyHost;

    private String proxyLogin;

    private String proxyPassword;

    private int proxyPort = -1;

    private Wipe wipe = null;

    private String cookies;

    private HttpHost proxy = null;

    private UI ui;

    private DefaultHttpClient lastClient;

    public HTTPClient(HttpProxy proxy, Wipe wipe) {
        this.proxyHost = proxy.getHost();
        this.proxyPort = proxy.getPort();
        this.wipe = wipe;
        this.proxy = proxy.getProxy();
        if (proxy.isAuth()) {
            this.proxyLogin = proxy.getLogin();
            this.proxyPassword = proxy.getPassword();
        }
        this.ui = wipe.getThreadManager().getUIManager().getUI();
    }

    public HTTPClient(HttpProxy proxy) {
        this.proxyHost = proxy.getHost();
        this.proxyPort = proxy.getPort();
        this.proxy = proxy.getProxy();
        if (proxy.isAuth()) {
            this.proxyLogin = proxy.getLogin();
            this.proxyPassword = proxy.getPassword();
        }
    }

    public BufferedReader getBufferedReader(String RequestUrl, HttpHost host, ArrayList<String> headers, boolean saveCookies) {
        try {
            return new BufferedReader(new InputStreamReader(getInputStream(RequestUrl, host, headers, saveCookies), "UTF-8"));
        } catch (Exception e) {
            if (this.wipe != null) {
                this.wipe.setException(e);
                this.wipe.getThreadManager().handleError((AbstractWipe) this.wipe);
            } else {
                e.printStackTrace();
            }
            return null;
        }
    }

    public BufferedReader getBufferedReader(String RequestUrl, boolean saveCookies) {
        return getBufferedReader(RequestUrl, null, null, saveCookies);
    }

    public BufferedReader getBufferedReader(String RequestUrl) {
        return getBufferedReader(RequestUrl, null, null, false);
    }

    public BufferedReader getBufferedReader(String RequestUrl, ArrayList<String> headers) {
        return getBufferedReader(RequestUrl, null, headers, false);
    }

    public InputStream getInputStream(String RequestUrl, boolean saveCookies) {
        return getInputStream(RequestUrl, null, null, saveCookies);
    }

    public InputStream getInputStream(String RequestUrl) {
        return getInputStream(RequestUrl, null, null, false);
    }

    public InputStream getInputStream(String RequestUrl, HttpHost host, ArrayList<String> headers, boolean saveCookies) {
        try {
            HttpResponse hp;
            if (this.wipe != null)
                for (ScriptContainer sc : this.wipe.getThreadManager().getScripts().getScripts()) {
                    if (sc.use) {
                        Object[] o = sc.script.onGetRequest((AbstractWipe) this.wipe, RequestUrl, host, headers, saveCookies);
                        if (o != null && o.length == 4) {
                            RequestUrl = (String) o[0];
                            host = (HttpHost) o[1];
                            headers = (ArrayList<String>) o[2];
                            saveCookies = ((Boolean) o[3]).booleanValue();
                        }
                    }
                }
            HttpGet get = new HttpGet(RequestUrl);
            if (headers != null)
                for (String header : headers) {
                    StringTokenizer st = new StringTokenizer(header, ",");
                    get.addHeader(st.nextToken(), st.nextToken());
                }
            if (host == null) {
                hp = createClient().execute((HttpUriRequest) get);
            } else {
                hp = createClient().execute(host, (HttpRequest) get);
            }
            if (saveCookies)
                saveCookies(hp);
            if (this.wipe != null)
                for (ScriptContainer sc : this.wipe.getThreadManager().getScripts().getScripts()) {
                    if (sc.use) {
                        HttpResponse schp = sc.script.onGetAnswer((AbstractWipe) this.wipe, hp);
                        if (schp != null)
                            hp = schp;
                    }
                }
            InputStream result = null;
            Header encoding = hp.getEntity().getContentEncoding();
            if (encoding != null && encoding.getValue().contains("gzip")) {
                result = new GZIPInputStream(hp.getEntity().getContent());
            } else {
                result = hp.getEntity().getContent();
            }
            if (result == null && this.wipe != null) {
                this.wipe.destroy("Answer from " + RequestUrl + " is null!");
                this.ui.removeThread(null);
                this.wipe.getThreadManager().getProxyManager().delete(this.wipe.getProxy(), this.ui);
            }
            return result;
        } catch (Exception e) {
            if (this.wipe != null) {
                this.wipe.setException(e);
                this.wipe.getThreadManager().handleError((AbstractWipe) this.wipe);
            } else {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void saveCookies(HttpResponse hp) {
        this.cookies = "";
        for (Header hh : hp.getAllHeaders()) {
            if (hh.getName().contains("ookie")) {
                String tmp = hh.getValue();
                if (tmp.endsWith(";")) {
                    this.cookies += tmp;
                } else {
                    this.cookies += tmp + ";";
                }
                tmp = null;
            }
        }
    }

    public void addCookie(String cookie) {
        if (cookie.endsWith(";")) {
            this.cookies += cookie;
        } else {
            this.cookies += cookie + ";";
        }
    }

    public String getCookieByName(String name) {
        StringTokenizer st = new StringTokenizer(this.cookies, ";");
        while (st.hasMoreTokens()) {
            String s = st.nextToken();
            if (s.startsWith(name)) {
                StringTokenizer ct = new StringTokenizer(s, "=");
                ct.nextToken();
                return ct.nextToken();
            }
        }
        return "";
    }

    public String getCookies() {
        return this.cookies;
    }

    public BufferedReader postBufferedReader(String url, HttpHost host, MultipartEntity me) {
        return postBufferedReader(url, host, me, (ArrayList<String>) null);
    }

    public BufferedReader postBufferedReader(String url, MultipartEntity me) {
        return postBufferedReader(url, (HttpHost) null, me, (ArrayList<String>) null);
    }

    public BufferedReader postBufferedReader(String url, MultipartEntity me, ArrayList<String> headers) {
        return postBufferedReader(url, (HttpHost) null, me, headers);
    }

    public BufferedReader postBufferedReader(String url, HttpHost host, MultipartEntity me, ArrayList<String> headers) {
        try {
            return new BufferedReader(new InputStreamReader(post(url, me, headers), "UTF-8"));
        } catch (Exception e) {
            if (this.wipe == null)
                return null;
            this.wipe.setException(e);
            this.wipe.getThreadManager().handleError((AbstractWipe) this.wipe);
            return null;
        }
    }

    public InputStream post(String url, MultipartEntity me) {
        return post(url, (HttpHost) null, me, (ArrayList<String>) null);
    }

    public InputStream post(String url, MultipartEntity me, ArrayList<String> headers) {
        return post(url, (HttpHost) null, me, headers);
    }

    public BufferedReader postBufferedReader(String url, HttpHost host, String value, ArrayList<String> headers) {
        try {
            return new BufferedReader(new InputStreamReader(post(url, host, value, headers), "UTF-8"));
        } catch (Exception e) {
            if (this.wipe == null)
                return null;
            this.wipe.setException(e);
            this.wipe.getThreadManager().handleError((AbstractWipe) this.wipe);
            return null;
        }
    }

    public InputStream post(String url, HttpHost host, String value, ArrayList<String> sheaders) {
        try {
            HttpResponse hp;
            HttpPost post = new HttpPost(url);
            if (sheaders != null)
                for (String header : sheaders) {
                    StringTokenizer st = new StringTokenizer(header, ",");
                    post.addHeader(st.nextToken(), st.nextToken());
                }
            StringEntity postEntity = new StringEntity(value, "UTF-8");
            postEntity.setContentType("application/x-www-form-urlencoded");
            post.setEntity((HttpEntity) postEntity);
            if (host == null) {
                hp = createClient().execute((HttpUriRequest) post);
            } else {
                hp = createClient().execute(host, (HttpRequest) post);
            }
            saveCookies(hp);
            return hp.getEntity().getContent();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public InputStream post(String url, HttpHost host, MultipartEntity me, ArrayList<String> sheaders) {
        try {
            HttpResponse hp;
            if (this.wipe != null)
                for (ScriptContainer sc : this.wipe.getThreadManager().getScripts().getScripts()) {
                    if (sc.use) {
                        Object[] o = sc.script.onPostRequest((AbstractWipe) this.wipe, url, host, me, sheaders);
                        if (o != null && o.length == 4) {
                            url = (String) o[0];
                            host = (HttpHost) o[1];
                            me = (MultipartEntity) o[2];
                            sheaders = (ArrayList<String>) o[3];
                        }
                    }
                }
            HttpPost post = new HttpPost(url);
            if (sheaders != null)
                for (String header : sheaders) {
                    StringTokenizer st = new StringTokenizer(header, ",");
                    post.addHeader(st.nextToken(), st.nextToken());
                }
            if (this.cookies != null) {
                post.addHeader("Cookie", this.cookies);
                this.cookies = "";
            }
            post.setEntity((HttpEntity) me);
            if (host == null) {
                hp = createClient().execute((HttpUriRequest) post);
            } else {
                hp = createClient().execute(host, (HttpRequest) post);
            }
            saveCookies(hp);
            if (this.wipe != null)
                for (ScriptContainer sc : this.wipe.getThreadManager().getScripts().getScripts()) {
                    if (sc.use) {
                        HttpResponse schp = sc.script.onGetAnswer((AbstractWipe) this.wipe, hp);
                        if (schp != null)
                            hp = schp;
                    }
                }
            InputStream result = hp.getEntity().getContent();
            if (result == null && this.wipe != null) {
                this.wipe.destroy("Answer from " + url + " is null!");
                this.ui.removeThread(null);
                this.wipe.getThreadManager().getProxyManager().delete(this.wipe.getProxy(), this.ui);
            }
            return result;
        } catch (Exception e) {
            if (this.wipe != null) {
                this.wipe.setException(e);
                this.wipe.getThreadManager().handleError((AbstractWipe) this.wipe);
            } else {
                e.printStackTrace();
            }
            return null;
        }
    }

    public boolean isViaProxy() {
        return (this.proxyHost != null && this.proxyPort > 0);
    }

    public boolean isProxyAuth() {
        return (this.proxyLogin != null && this.proxyPassword != null);
    }

    public DefaultHttpClient createClient() {
        DefaultHttpClient httpclient = null;
        try {
            if (this.lastClient != null)
                this.lastClient.getConnectionManager().shutdown();
            SchemeRegistry supportedSchemes = new SchemeRegistry();
            supportedSchemes.register(new Scheme("http", (SocketFactory) PlainSocketFactory.getSocketFactory(), 80));
            SSLSocketFactory s = new SSLSocketFactory(createSSLContext());
            supportedSchemes.register(new Scheme("https", (SocketFactory) s, 443));
            BasicHttpParams basicHttpParams = new BasicHttpParams();
            HttpProtocolParams.setVersion((HttpParams) basicHttpParams, (ProtocolVersion) HttpVersion.HTTP_1_1);
            if (this.wipe != null) {
                HttpProtocolParams.setUserAgent((HttpParams) basicHttpParams, this.wipe.getUserAgent());
            } else {
                HttpProtocolParams.setUserAgent((HttpParams) basicHttpParams, "Mozilla/5.0 (Windows; U; Windows NT 6.1; ru; rv:1.9.2) Gecko/20100115 Firefox/3.6");
            }
            HttpProtocolParams.setContentCharset((HttpParams) basicHttpParams, "UTF-8");
            HttpProtocolParams.setUseExpectContinue((HttpParams) basicHttpParams, false);
            ThreadSafeClientConnManager threadSafeClientConnManager = new ThreadSafeClientConnManager((HttpParams) basicHttpParams, supportedSchemes);
            httpclient = new DefaultHttpClient((ClientConnectionManager) threadSafeClientConnManager, (HttpParams) basicHttpParams);
            if (isViaProxy())
                httpclient.getParams().setParameter("http.route.default-proxy", this.proxy);
            if (isProxyAuth())
                httpclient.getCredentialsProvider().setCredentials(new AuthScope(this.proxyHost, this.proxyPort), (Credentials) new UsernamePasswordCredentials(this.proxyLogin, this.proxyPassword));
        } catch (Exception e) {
            if (this.wipe != null) {
                this.wipe.setException(e);
                this.wipe.getThreadManager().handleError((AbstractWipe) this.wipe);
            } else {
                e.printStackTrace();
            }
        }
        this.lastClient = httpclient;
        return this.lastClient;
    }

    private SSLContext createSSLContext() throws KeyManagementException, NoSuchAlgorithmException {
        SSLContext ctx = SSLContext.getInstance("TLS");
        X509TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        ctx.init(null, new TrustManager[]{tm}, null);
        return ctx;
    }
}
