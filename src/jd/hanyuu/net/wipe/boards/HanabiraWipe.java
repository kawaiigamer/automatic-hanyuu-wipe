package hanyuu.net.wipe.boards;

import config.Config;
import hanyuu.chan.Chan;
import hanyuu.managers.ThreadManager;
import hanyuu.net.proxy.HttpProxy;
import hanyuu.net.wipe.AbstractWipe;
import hanyuu.net.wipe.Action;
import hanyuu.net.wipe.Wipe;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import utils.Utils;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

public class HanabiraWipe extends Wipe {
    public HanabiraWipe(HttpProxy proxy, ThreadManager tm, Chan chan) {
        super(proxy, tm, chan);
    }

    public InputStream getCaptchaStream() {
        if (!this.work)
            return null;
        this.lastAction = Action.RequestCaptcha;
        this.txtCaptcha = "";
        String url = this.chan.ChanHost + "/" + this.chan.CaptchaPath + Config.board + String.valueOf(random.nextInt(2147483647)) + ".png";
        this.headers = new ArrayList();
        this.ui.logInfo("Request CAPTCHA from " + this.chan.ChanHost + " via " + this.proxy);
        switch (Config.workMode) {
            case WipeBoard:
                this.headers.add("Referer," + this.chan.ChanHost + Config.board);
                return this.httpClient.getInputStream(url, false);
        }
        this.headers.add("Referer," + this.chan.ChanHost + Config.board + "res/" + this.thread + ".xhtml");
        return this.httpClient.getInputStream(url, false);
    }

    public void makeMimesAndHeaders(String url) {
        this.thread = Config.thread;
        url = "http://dobrochan.ru" + Config.board + "post/new.xhtml".replace("//", "");
        switch (Config.workMode) {
            case WipeBoard:
                this.headers.add("Referer," + this.chan.ChanHost + Config.board);
                this.me.addPart("thread_id", (ContentBody) Utils.sb(0));
                this.ui.logInfo(this.wipeThread.getName() + " posting to " + this.chan.ChanHost + Config.board);
                break;
            case Force:
                this.thread = getNextThread(getThreads());
                this.me.addPart("thread_id", (ContentBody) Utils.sb(this.thread));
                this.headers.add("Referer," + this.chan.ChanHost + Config.board + this.thread + ".xhtml");
                this.ui.logInfo(this.wipeThread.getName() + " posting to " + this.chan.ChanHost + Config.board + "res/" + this.thread + ".xhtml");
                break;
            default:
                this.me.addPart("thread_id", (ContentBody) Utils.sb(this.thread));
                this.headers.add("Referer," + this.chan.ChanHost + Config.board + this.thread + ".xhtml");
                this.ui.logInfo(this.wipeThread.getName() + " posting to " + this.chan.ChanHost + Config.board + "res/" + this.thread + ".xhtml");
                break;
        }
        this.ui.logInfo("Request CAPTCHA from " + this.chan.ChanHost + " via " + this.proxy);
        this.me.addPart("task", (ContentBody) Utils.sb("post"));
        this.me.addPart("name", (ContentBody) Utils.sb(Config.name));
        this.me.addPart("subject", (ContentBody) Utils.sb(Config.theme));
        this.me.addPart("message", (ContentBody) Utils.sb(this.msg));
        this.me.addPart("password", (ContentBody) Utils.sb(Config.password));
        this.me.addPart("post_files_count", (ContentBody) Utils.sb("1"));
        this.me.addPart("file_1_rating", (ContentBody) Utils.sb("SFW"));
        FileBody fileBody = new FileBody((File) this.picture, "image/" + this.picture.format);
        this.me.addPart("file_1", (ContentBody) fileBody);
        this.me.addPart("goto", (ContentBody) Utils.sb("board"));
        this.me.addPart("board", (ContentBody) Utils.sb(Config.board));
        if (!this.work)
            this.tm.destroyThread((AbstractWipe) this, " stopped.");
        this.httpClient.addCookie("wakabastyle=Futaba");
        this.httpClient.addCookie("settings=1287054997050");
    }
}
