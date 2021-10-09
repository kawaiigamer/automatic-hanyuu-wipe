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

public class KusabaWipe extends Wipe {
    public KusabaWipe(HttpProxy proxy, ThreadManager tm, Chan chan) {
        super(proxy, tm, chan);
    }

    public void makeMimesAndHeaders(String url) {
        if (!this.work)
            return;
        this.thread = Config.thread;
        switch (Config.workMode) {
            case WipeBoard:
                this.headers.add("Referer," + this.chan.ChanHost + Config.board);
                this.ui.logInfo(this.wipeThread.getName() + " posting to " + this.chan.ChanHost + Config.board);
                break;
            case Force:
                this.thread = getNextThread(getThreads());
                this.headers.add("Referer," + this.chan.ChanHost + Config.board + "res/" + this.thread + ".html");
                this.ui.logInfo(this.wipeThread.getName() + " posting to " + this.chan.ChanHost + Config.board + "res/" + this.thread + ".html");
                break;
            default:
                this.headers.add("Referer," + this.chan.ChanHost + Config.board + "res/" + this.thread + ".html");
                this.ui.logInfo(this.wipeThread.getName() + " posting to " + this.chan.ChanHost + Config.board + "res/" + this.thread + ".html");
                break;
        }
        this.me.addPart(this.chan.FieldBoardName, (ContentBody) Utils.sb(Config.board.replace("/", "")));
        switch (Config.workMode) {
            case WipeBoard:
                this.me.addPart(this.chan.FieldThreadNumber, (ContentBody) Utils.sb("0"));
                return;
        }
        this.me.addPart(this.chan.FieldThreadNumber, (ContentBody) Utils.sb(this.thread));
    }

    public InputStream getCaptchaStream() {
        if (!this.work)
            return null;
        this.lastAction = Action.RequestCaptcha;
        this.txtCaptcha = "";
        this.headers = new ArrayList();
        String url = this.chan.CaptchaPath + "?0." + String.valueOf(random.nextInt(2147483647));
        this.ui.logInfo("Request CAPTCHA from " + this.chan.ChanHost + " via " + this.proxy);
        switch (Config.workMode) {
            case WipeBoard:
                this.headers.add("Referer," + this.chan.ChanHost + Config.board);
                return this.httpClient.getInputStream(url, null, this.headers, true);
        }
        this.headers.add("Referer," + this.chan.ChanHost + Config.board + "res/" + this.thread + ".html");
        return this.httpClient.getInputStream(url, null, this.headers, true);
    }
}
