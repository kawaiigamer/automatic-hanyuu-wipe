package hanyuu.net.wipe;

import config.Config;
import hanyuu.chan.Chan;
import hanyuu.ext.ScriptContainer;
import hanyuu.ext.interfaces.OCR;
import hanyuu.managers.ThreadManager;
import hanyuu.managers.pictures.Pic;
import hanyuu.net.HTTPClient;
import hanyuu.net.proxy.HttpProxy;
import hanyuu.parser.HornedParser;
import org.apache.http.entity.mime.MultipartEntity;
import ui.interfaces.ITray;
import ui.interfaces.UI;

import javax.swing.*;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

public abstract class AbstractWipe {
    protected HTTPClient httpClient;

    protected Pic picture;

    protected HttpProxy proxy;

    protected String txtCaptcha;

    protected UI ui;

    protected Thread wipeThread;

    protected boolean work = true;

    protected ImageIcon captcha = null;

    protected String status;

    protected ITray tray;

    protected Chan chan;

    protected boolean ready = true;

    protected boolean mustSleep = false;

    protected boolean error = false;

    protected String cookies;

    protected String msg;

    protected String url;

    protected ThreadManager tm;

    protected String WipeUserAgent = Config.userAgent;

    protected int successful = 0;

    protected int failed = 0;

    protected String thread;

    protected ArrayList<String> headers;

    protected ArrayList<String> sendedThreads = new ArrayList<>();

    protected ArrayList<String> parsedThreads = new ArrayList<>();

    protected boolean onZeroPage = false;

    protected byte errors = 0;

    protected boolean isSleeping;

    protected String lastPost;

    protected HornedParser parser;

    protected OCR ocr;

    protected MultipartEntity me;

    protected Action lastAction;

    protected Exception exception;

    protected void count() {
        this.tray.reIcon(this.tm.getAllSuccessful(), this.tm.getAllFailed());
        this.ui.setSuccessful(this.tm.getAllSuccessful());
        this.ui.setFailed(this.tm.getAllFailed());
    }

    public void destroy(String reason) {
        this.tm.destroyThread(this, reason);
    }

    public String getCaptcha() {
        return this.txtCaptcha;
    }

    public ImageIcon getCaptchaImg() {
        return this.captcha;
    }

    public Chan getChan() {
        return this.chan;
    }

    public void setLastAction(Action action) {
        this.lastAction = action;
    }

    public Action getLastAction() {
        return this.lastAction;
    }

    public OCR getOCR() {
        return this.ocr;
    }

    public void setOCR(OCR ocr) {
        this.ocr = ocr;
    }

    public int getFailed() {
        return this.failed;
    }

    public Pic getFile() {
        return this.picture;
    }

    public HTTPClient getHttpClient() {
        return this.httpClient;
    }

    public String getMsg() {
        return this.msg;
    }

    public HttpProxy getProxy() {
        return this.proxy;
    }

    public String getStatus() {
        return this.status;
    }

    public int getSuccessful() {
        return this.successful;
    }

    public Thread getThread() {
        return this.wipeThread;
    }

    public ThreadManager getThreadManager() {
        return this.tm;
    }

    public UI getUI() {
        return this.ui;
    }

    public String getUserAgent() {
        return this.WipeUserAgent;
    }

    public boolean isError() {
        return this.error;
    }

    public boolean isWork() {
        return this.work;
    }

    public synchronized void myNotify() {
        this.ready = true;
        notify();
    }

    public void waitForNotReady() {
        try {
            this.ready = false;
            synchronized (this) {
                this.ui.logInfo("Thread [ " + toString() + " ] waiting...");
                while (!this.ready)
                    wait();
                this.ui.logInfo("Notifyng thread [ " + toString() + " ] ...");
            }
        } catch (Exception e) {
            waitForNotReady();
        }
    }

    public void setCaptcha(String captcha) {
        this.txtCaptcha = captcha;
        if (Config.waitForNotReady)
            this.tm.removeFromNotReady(this);
    }

    public void setCaptchaImg(ImageIcon img) {
        this.captcha = img;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public void setFile(File file) {
        this.picture = new Pic(file);
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setProxy(HttpProxy p) {
        this.proxy = p;
    }

    public void setReadyCaptcha(boolean ready) {
        this.ready = ready;
    }

    public void setStatus(String st) {
        this.status = st;
    }

    public void setUserAgent(String agent) {
        this.WipeUserAgent = agent;
    }

    public Exception getException() {
        return this.exception;
    }

    public void setSleeping(boolean sleep) {
        this.isSleeping = sleep;
    }

    public boolean isSleeping() {
        return this.isSleeping;
    }

    public void setException(Exception e) {
        e.printStackTrace();
        this.exception = e;
    }

    public String toString() {
        return this.proxy.getHost();
    }

    public synchronized void stop() {
        if (!this.work)
            return;
        this.work = false;
        this.captcha = null;
        if ((Config.useTmp || Config.randomPicGenerate) && this.picture != null && this.picture.exists())
            this.picture.delete();
        this.ui.removeThread(this);
        for (ScriptContainer s : this.tm.getScripts().getScripts()) {
            if (s.use)
                s.script.onStopWipe(this);
        }
        this.wipeThread.setPriority(1);
    }

    public byte getErrors() {
        return this.errors;
    }

    public void incErrors() {
        this.errors = (byte) (this.errors + 1);
    }

    public void requestDelet(String[] msgs) {
        this.lastAction = Action.DeletingPost;
        delete(msgs);
    }

    public abstract void delete(String[] paramArrayOfString);

    public abstract void post();

    public abstract InputStream getCaptchaStream();

    public abstract ArrayList<String> getThreads();
}
