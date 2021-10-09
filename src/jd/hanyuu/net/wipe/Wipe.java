package hanyuu.net.wipe;

import config.Config;
import config.WorkMode;
import hanyuu.chan.Chan;
import hanyuu.ext.ScriptContainer;
import hanyuu.managers.ThreadManager;
import hanyuu.net.HTTPClient;
import hanyuu.net.proxy.HttpProxy;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import utils.Constants;
import utils.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

public abstract class Wipe extends AbstractWipe implements Runnable, Constants {
    public Wipe(HttpProxy proxy, ThreadManager tm, Chan chan) {
        this.chan = chan;
        this.proxy = proxy;
        this.tm = tm;
        this.ui = tm.getUIManager().getUI();
        this.tray = tm.getUIManager().getTray();
        this.ocr = tm.getScripts().getOCRbyName(Config.ocrMode);
        this.httpClient = new HTTPClient(proxy, this);
        this.parser = tm.getParser();
        if (chan.ChanHost != null && chan.ScriptPath != null)
            this.url = chan.ScriptPath.replace("[%b]", Config.board);
        this.msg = Config.msg;
        this.thread = Config.thread;
        this.wipeThread = new Thread(this, proxy.getHost());
        this.wipeThread.start();
    }

    protected boolean onZeroPage() {
        if (Config.workMode != WorkMode.OnZeroPage) {
            this.onZeroPage = false;
        } else if (getThreads().contains(Config.thread)) {
            this.ui.logInfo("Thread " + this.thread + " on zero page.");
            this.onZeroPage = true;
        } else {
            this.onZeroPage = false;
        }
        return this.onZeroPage;
    }

    public boolean checkPost(String s) {
        if (!this.work)
            return true;
        if (this.picture != null && this.picture.exists() && (Config.useTmp || Config.randomPicGenerate))
            this.picture.delete();
        if (Config.parser) {
            this.lastPost = this.parser.getMyPost(this.httpClient.getInputStream(this.chan.ChanHost + Config.board), this);
            if (this.lastPost == null || this.lastPost.isEmpty()) {
                if (!isError(s)) {
                    this.failed++;
                    this.error = true;
                    this.ui.logError("Неудалось найти сообщение, причина неизвестна. Ниже приведён ответ от борды:\n" + s);
                }
                count();
                return false;
            }
            onSuccess();
            return true;
        }
        if (isError(s)) {
            count();
            return false;
        }
        if (this.chan.MessageSuccessful != null && this.chan.MessageSuccessful.matcher(s).find()) {
            onSuccess();
            return true;
        }
        onSuccess();
        return true;
    }

    public void onSuccess() {
        this.mustSleep = true;
        this.successful++;
        this.tm.getImgManager().setUsedFile((File) this.picture);
        this.tray.trayPrint("Успешно " + Config.chanName, "Уже " + this.tm.getAllSuccessful() + " постов");
        this.ui.logInfo("Post " + (Config.parser ? ("(ID: " + this.lastPost + ")") : "") + " successful " + this.tm.getAllSuccessful() + " Proxy is: " + this.proxy.getHost() + " Threads count " + this.tm.size());
        this.ocr.onSuccess();
        count();
        if (Config.msgCount && this.tm.getAllSuccessful() >= Config.msgCountInt) {
            this.ui.logInfo("Поток " + this.wipeThread.getName() + " завершён. Все сообщения (" + Config.msgCountInt + ") отправленны.");
            destroy("All massage sended.");
        }
    }

    private boolean isError(String msg) {
        boolean isError = false;
        if (this.chan.MessageBan != null && this.chan.MessageBan.matcher(msg).find()) {
            this.ui.logError(this.wipeThread.getName() + " banned!");
            this.tm.getProxyManager().delete(this.proxy, this.ui);
            this.tm.destroyThread(this, "Banned!");
            isError = true;
        }
        if (this.chan.MessageInvalidCaptcha != null && this.chan.MessageInvalidCaptcha.matcher(msg).find()) {
            this.ui.logError("Неправельная капча.");
            this.ocr.onError();
            isError = true;
        }
        if (this.chan.MessageError != null && this.chan.MessageError.matcher(msg).find() && !isError) {
            if (Config.emulateWipe) {
                destroy("Эмуляция вайпа удачна.");
                return false;
            }
            int index = msg.indexOf(this.chan.MessageError.toString());
            if (index > 0)
                this.ui.logError(msg.substring(index));
            isError = true;
        }
        if (isError) {
            this.failed++;
            this.tray.trayPrintError("Неудачно", "Всего " + this.tm.getAllFailed() + " неудачных постов");
        }
        this.mustSleep = !isError;
        return isError;
    }

    protected String getNextThread(ArrayList<String> ThreadArray) {
        if (!this.work)
            return null;
        String threadNumber = "";
        try {
            threadNumber = this.parsedThreads.get(random.nextInt(this.parsedThreads.size()));
        } catch (IllegalArgumentException iae) {
            destroy("Bad proxy. " + iae.toString());
        }
        if (this.sendedThreads.contains(threadNumber))
            getNextThread(ThreadArray);
        this.sendedThreads.add(threadNumber);
        return threadNumber;
    }

    public void run() {
        while (this.work && this.ready) {
            System.gc();
            for (ScriptContainer s : this.tm.getScripts().getScripts()) {
                if (s.use)
                    s.script.onRunWipe(this);
            }
            if (!Config.BokuNoFile)
                this.tm.getImgManager().reImg(this);
            post();
            if (Config.timeout > 0 && (this.mustSleep || (Config.workMode == WorkMode.OnZeroPage && this.onZeroPage)) && this.work) {
                this.ui.logInfo("Sleeping " + Config.timeout + " ms." + " Thread: " + this.wipeThread.getName());
                this.tm.sleepWipe(Config.timeout, this);
                this.mustSleep = false;
            }
            if (Config.workMode != WorkMode.OnZeroPage && (Config.useTmp || Config.randomPicGenerate) && this.picture != null && this.picture.exists())
                this.picture.delete();
            if (Config.workMode != WorkMode.OnZeroPage && !Config.onePic && !Config.BokuNoFile)
                this.picture = this.tm.getImgManager().getFile(this);
        }
    }

    public void post() {
        this.lastAction = Action.Posting;
        try {
            if (!this.work)
                return;
            this.thread = Config.thread;
            if (onZeroPage())
                return;
            this.headers = new ArrayList<>();
            this.me = new MultipartEntity();
            makeMimesAndHeaders(this.url);
            this.ocr.recognizeCaptcha(this);
            this.me.addPart(this.chan.FieldCaptcha, (ContentBody) Utils.sb(this.txtCaptcha));
            if (Config.pasteNoMsg && Config.pasteOnPic && !Config.BokuNoFile) {
                this.me.addPart(this.chan.FieldMessage, (ContentBody) Utils.sb(""));
            } else {
                this.me.addPart(this.chan.FieldMessage, (ContentBody) Utils.sb(this.msg));
            }
            this.me.addPart(this.chan.FieldEmail, (ContentBody) Utils.sb(Config.email));
            this.me.addPart(this.chan.FieldName, (ContentBody) Utils.sb(Config.name));
            this.me.addPart(this.chan.FieldTheme, (ContentBody) Utils.sb(Config.theme));
            this.me.addPart(this.chan.FieldPassword, (ContentBody) Utils.sb(Config.password));
            if (this.picture == null || Config.BokuNoFile) {
                this.me.addPart(this.chan.FieldFile, (ContentBody) Utils.sb(""));
            } else {
                FileBody fileBody = new FileBody((File) this.picture, "image/" + this.picture.format);
                this.me.addPart(this.chan.FieldFile, (ContentBody) fileBody);
            }
            if (!this.work)
                return;
            if (Config.waitForNotReady && !this.tm.isReady())
                waitForNotReady();
            BufferedReader br = this.httpClient.postBufferedReader(this.url, this.me, this.headers);
            String s2 = "";
            String s;
            while ((s = br.readLine()) != null)
                s2 = s2 + s;
            if (checkPost(s2) && Config.silentBump) {
                this.ui.logInfo("Deleting post, number: " + this.lastPost);
                delete(new String[]{this.lastPost});
            }
        } catch (Exception e) {
            setException(e);
            this.tm.handleError(this);
        }
    }

    public void delete(String[] messages) {
        if (this.chan.RequestDeletePost == null || this.chan.RequestDeletePost.isEmpty()) {
            this.ui.showMessage("Удаление не возможно, не указан запрос на удаление.", 0);
            this.ui.logError("Cannot delete post, please configure chan " + this.chan.ChanName + " in you chans.xml");
            return;
        }
        try {
            for (String m : messages) {
                final String request = this.chan.RequestDeletePost.replace("[%id]", m).replace("[%p]", Config.password).replace("[%b]", Config.board.replace("/", ""));
                System.out.println(request);
                Runnable r = new Runnable() {
                    public void run() {
                        Utils.print(Wipe.this.httpClient.postBufferedReader(Wipe.this.chan.ScriptPath.replace("[%b]", Config.board), null, request, Wipe.this.headers));
                    }
                };
                Utils.startThread(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.tm.handleError(this);
        }
    }

    public ArrayList<String> getThreads() {
        this.lastAction = Action.RequestThreads;
        try {
            String threadsUrl;
            if (Config.workMode != WorkMode.OnZeroPage) {
                if (this.sendedThreads.size() < this.parsedThreads.size())
                    return this.parsedThreads;
                this.sendedThreads.clear();
            }
            this.headers = new ArrayList<>();
            if (Config.page == 0 || Config.workMode == WorkMode.OnZeroPage) {
                threadsUrl = this.chan.ChanPosts.replace("[%host]", this.chan.ChanHost).replace("[%b]", Config.board);
            } else {
                threadsUrl = this.chan.ChanPosts.replace("[%host]", this.chan.ChanHost).replace("[%b]", Config.board) + Config.page + ".html";
            }
            this.parsedThreads.clear();
            this.ui.logInfo("Request threads array from " + this.chan.ChanHost + " via " + this.proxy);
            this.headers.add("Referer, " + this.chan.ChanHost + Config.board);
            this.parsedThreads = this.parser.getThreads(this.httpClient.getInputStream(threadsUrl, null, this.headers, true), this);
            this.ui.logInfo(this.parsedThreads.size() + " threas parsed, thread: " + toString());
            return this.parsedThreads;
        } catch (Exception e) {
            setException(e);
            this.tm.handleError(this);
            return null;
        }
    }

    public abstract void makeMimesAndHeaders(String paramString);

    public abstract InputStream getCaptchaStream();
}
