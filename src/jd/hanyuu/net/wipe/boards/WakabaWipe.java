package hanyuu.net.wipe.boards;

import config.Config;
import hanyuu.chan.Chan;
import hanyuu.managers.ThreadManager;
import hanyuu.net.proxy.HttpProxy;
import hanyuu.net.wipe.Action;
import hanyuu.net.wipe.Wipe;
import org.apache.http.entity.mime.content.ContentBody;
import utils.Utils;

import java.io.InputStream;
import java.util.ArrayList;

public class WakabaWipe extends Wipe {
    public WakabaWipe(HttpProxy proxy, ThreadManager tm, Chan chan) {
        super(proxy, tm, chan);
    }

    public InputStream getCaptchaStream() {
        this.lastAction = Action.RequestCaptcha;
        if (!this.work)
            return null;
        this.headers = new ArrayList();
        switch (Config.workMode) {
            case WipeBoard:
                this.headers.add("Referer," + this.chan.ChanHost + Config.board);
                url = this.chan.CaptchaPath.replace("[%b]", Config.board).replace("[%r]", String.valueOf(Math.floor(Math.random() * 1000.0D))).replace("[%t]", "mainpage").replace(".0", "").replace("[%host]", this.chan.ChanHost);
                this.txtCaptcha = "";
                this.ui.logInfo("Request CAPTCHA from " + this.chan.ChanHost + " via " + this.proxy.toSipleString());
                this.headers.add("Cookie,wakabastyle=photon");
                return this.httpClient.getInputStream(url, null, this.headers, true);
        }
        this.headers.add("Referer," + this.chan.ChanHost + Config.board + "res/" + this.thread + ".html");
        String url = this.chan.CaptchaPath.replace("[%b]", Config.board).replace("[%r]", String.valueOf(Math.floor(Math.random() * 1000.0D))).replace("[%t]", "res" + this.thread).replace(".0", "").replace("[%host]", this.chan.ChanHost);
        this.txtCaptcha = "";
        this.ui.logInfo("Request CAPTCHA from " + this.chan.ChanHost + " via " + this.proxy.toSipleString());
        this.headers.add("Cookie,wakabastyle=photon");
        return this.httpClient.getInputStream(url, null, this.headers, true);
    }

    public void makeMimesAndHeaders(String url) {
        switch (Config.workMode) {
            case WipeBoard:
                this.me.addPart(this.chan.FieldThreadNumber, (ContentBody) Utils.sb(""));
                this.me.addPart(this.chan.retName, (ContentBody) Utils.sb("board"));
                this.headers.add("Referer," + this.chan.ChanHost + Config.board);
                this.headers.add("Cookie,wakabastyle=photon; email=; password=" + Config.password + "; name=; " + this.chan.retName + "=board;");
                this.ui.logInfo(this.wipeThread.getName() + " posting to " + this.chan.ChanHost + Config.board);
                break;
            case Force:
                this.thread = getNextThread(getThreads());
                this.me.addPart(this.chan.FieldThreadNumber, (ContentBody) Utils.sb(this.thread));
                this.me.addPart(this.chan.retName, (ContentBody) Utils.sb(this.chan.retName));
                this.headers.add("Referer," + this.chan.ChanHost + Config.board + "res/" + this.thread + ".html");
                this.ui.logInfo(this.wipeThread.getName() + " posting to " + this.chan.ChanHost + Config.board + "res/" + this.thread + ".html");
                break;
            default:
                this.me.addPart(this.chan.retName, (ContentBody) Utils.sb(this.chan.retName));
                this.headers.add("Referer," + this.chan.ChanHost + Config.board + "res/" + this.thread + ".html");
                this.headers.add("Cookie, wakabastyle=photon; email=; password=" + Config.password + "; name=; " + this.chan.retName + "=" + this.chan.retMode + ";");
                this.me.addPart(this.chan.FieldThreadNumber, (ContentBody) Utils.sb(this.thread));
                this.ui.logInfo(this.wipeThread.getName() + " posting to " + this.chan.ChanHost + Config.board + "res/" + this.thread + ".html");
                break;
        }
        this.me.addPart(this.chan.task, (ContentBody) Utils.sb(this.chan.post));
        if (Config.BokuNoFile)
            this.me.addPart("nofile", (ContentBody) Utils.sb(1));
        this.me.addPart("name", (ContentBody) Utils.sb(""));
        this.me.addPart("link", (ContentBody) Utils.sb(""));
    }
}
