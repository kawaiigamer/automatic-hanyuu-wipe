package hanyuu.net.proxy;

import org.apache.http.HttpHost;

public class HttpProxy {
    private String host;

    private int port;

    private String login;

    private String password;

    private HttpHost ProxyHost;

    public HttpProxy(String host, int port) {
        this.host = host;
        this.port = port;
        this.ProxyHost = new HttpHost(this.host, this.port, "http");
    }

    public HttpProxy(String host, int port, String login, String password) {
        this.host = host;
        this.port = port;
        this.login = login;
        this.password = password;
        this.ProxyHost = new HttpHost(this.host, this.port, "http");
    }

    public boolean isAuth() {
        return (this.login != null);
    }

    public String toSipleString() {
        return this.host + ":" + this.port;
    }

    public String getHost() {
        return this.host;
    }

    public String getLogin() {
        return this.login;
    }

    public String getPassword() {
        return this.password;
    }

    public int getPort() {
        return this.port;
    }

    public HttpHost getProxy() {
        return this.ProxyHost;
    }

    public String toString() {
        if (isAuth())
            return this.host + ":" + this.port + ":" + this.login + ":" + this.password;
        return this.host + ":" + this.port;
    }
}
