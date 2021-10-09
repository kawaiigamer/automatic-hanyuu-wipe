package hanyuu.net.wipe.boards;

import config.Config;
import hanyuu.chan.Chan;
import hanyuu.managers.ThreadManager;
import hanyuu.net.proxy.HttpProxy;
import hanyuu.net.wipe.AbstractWipe;
import hanyuu.net.wipe.Action;
import hanyuu.net.wipe.Wipe;
import org.apache.http.entity.mime.content.ContentBody;
import utils.Utils;

import java.io.InputStream;

public class FutabaWipe extends Wipe {
    public FutabaWipe(HttpProxy proxy, ThreadManager tm, Chan chan) {
        super(proxy, tm, chan);
    }

    public void makeMimesAndHeaders(String url) {
        if (!this.work)
            return;
        if (onZeroPage())
            return;
        switch (Config.workMode) {
            case WipeBoard:
                this.me.addPart(this.chan.FieldThreadNumber, (ContentBody) Utils.sb(0));
                this.me.addPart("resto", (ContentBody) Utils.sb(this.thread));
                this.ui.logInfo(this.wipeThread.getName() + " posting to " + this.chan.ChanHost + Config.board);
                break;
            case Force:
                this.thread = getNextThread(getThreads());
                this.ui.logInfo(this.wipeThread.getName() + " posting to " + this.chan.ChanHost + Config.board + "res/" + this.thread + ".html");
                break;
            default:
                this.ui.logInfo(this.wipeThread.getName() + " posting to " + this.chan.ChanHost + Config.board + "res/" + this.thread + ".html");
                this.me.addPart(this.chan.FieldThreadNumber, (ContentBody) Utils.sb(this.thread));
                break;
        }
        this.me.addPart(this.chan.task, (ContentBody) Utils.sb(this.chan.post));
        this.me.addPart("MAX_FILE_SIZE", (ContentBody) Utils.sb(2097152));
        this.ocr.recognizeCaptcha((AbstractWipe) this);
        this.me.addPart(this.chan.FieldCaptcha, (ContentBody) Utils.sb(this.txtCaptcha));
        this.me.addPart("mode", (ContentBody) Utils.sb("regist"));
    }

    public InputStream getCaptchaStream() {
        this.lastAction = Action.RequestCaptcha;
        return null;
    }
}
