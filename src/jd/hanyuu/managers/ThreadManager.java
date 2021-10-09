package hanyuu.managers;

import config.Config;
import hanyuu.chan.Chan;
import hanyuu.chan.ChanManager;
import hanyuu.ext.ScriptManager;
import hanyuu.managers.pictures.ImgManager;
import hanyuu.net.proxy.HttpProxy;
import hanyuu.net.proxy.ProxyManager;
import hanyuu.net.wipe.AbstractWipe;
import hanyuu.net.wipe.Action;
import hanyuu.net.wipe.boards.FutabaWipe;
import hanyuu.net.wipe.boards.HanabiraWipe;
import hanyuu.net.wipe.boards.KusabaWipe;
import hanyuu.net.wipe.boards.WakabaWipe;
import hanyuu.parser.HornedParser;
import utils.Constants;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;

public class ThreadManager implements Runnable, Constants {
    private static ThreadManager _instance = null;

    private boolean work = false;

    private ProxyManager pmr;

    private ImgManager im;

    private UIManager uim;

    private Thread ManagerThread;

    private ChanManager cm;

    private CopyPasteManager cpm;

    private boolean console;

    private HornedParser parser;

    private MessageManager mm;

    private ScriptManager scripts;

    private ArrayList<AbstractWipe> notReady = new ArrayList<>();

    private ArrayList<AbstractWipe> running = new ArrayList<>();

    public static ThreadManager getInstance() {
        return _instance;
    }

    public boolean isWork() {
        return this.work;
    }

    public ThreadManager(boolean console) {
        _instance = this;
        this.ManagerThread = new Thread(this, "Thread Manager");
        this.console = console;
        this.ManagerThread.start();
    }

    public synchronized void handleError(AbstractWipe wipe) {
        if (!Config.smartErrorHandler)
            return;
        this.uim.getUI().logError("Called handleError() method in thread: [ " + wipe.toString() + " ]" + " Last action is: [ " + wipe.getLastAction() + " ] error is: " + wipe.getException().toString());
        wipe.incErrors();
        if (wipe.getErrors() == Config.smartErrorCount) {
            if (Config.smartErrorAction == 0) {
                wipe.setError(true);
                destroyThread(wipe, "Too many errors, see logs for details.");
                return;
            }
            if (Config.smartErrorAction == 1) {
                this.uim.getUI().logInfo("Too many errors in thread [ " + wipe.toString() + " ], restarting...");
                reStart(wipe);
            }
        }
        switch (wipe.getLastAction()) {
            case Wakaba:
            case Kusaba:
                wipe.getOCR().recognizeCaptcha(wipe);
                break;
            case Hanabira:
                wipe.getThreads();
                break;
            case Futaba:
            case null:
                wipe.post();
                break;
        }
    }

    public void run() {
        this.ManagerThread.setPriority(10);
        try {
            System.setOut(new PrintStream(System.out, true, Config.EncodingConsole));
            System.setErr(new PrintStream(System.err, true, Config.EncodingConsole));
            this.pmr = new ProxyManager();
            this.cpm = new CopyPasteManager();
            this.cm = new ChanManager();
            this.cm.createAndLoad();
            Config.useGui = !this.console;
            this.scripts = new ScriptManager(this);
            this.im = new ImgManager(Config.path, this);
            this.uim = new UIManager(this);
            this.parser = new HornedParser();
            this.mm = new MessageManager(this);
            if (this.console)
                StartStopManage();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public MessageManager getMM() {
        return this.mm;
    }

    public HornedParser getParser() {
        return this.parser;
    }

    public ProxyManager getProxyManager() {
        return this.pmr;
    }

    public void deleteAllMessages() {
    }

    public ScriptManager getScripts() {
        return this.scripts;
    }

    public ImgManager getImgManager() {
        return this.im;
    }

    public void reBuildImgManager() {
        this.im = new ImgManager(Config.path, this);
    }

    public UIManager getUIManager() {
        return this.uim;
    }

    public ChanManager getChanManager() {
        return this.cm;
    }

    public CopyPasteManager getCopyPasteManager() {
        return this.cpm;
    }

    public void sleepWipe(long time, AbstractWipe wipe) {
        try {
            if (wipe.isSleeping())
                return;
            wipe.setLastAction(Action.Sleeping);
            wipe.setSleeping(true);
            synchronized (wipe) {
                wipe.wait(time);
            }
            wipe.setSleeping(false);
        } catch (Exception e) {
            wipe.setSleeping(false);
            wipe.setException(e);
            handleError(wipe);
        }
    }

    public void StartStopManage() {
        try {
            if (!(new File(Config.path)).exists() && !Config.BokuNoFile) {
                this.uim.getUI().showMessage("File or folder " + Config.path + " not found", 0);
                this.uim.getUI().logError("File or folder " + Config.path + " not found");
                return;
            }
            if (this.work) {
                this.work = false;
                this.uim.getUI().SwitchStartStop();
                for (int i = 0; i < this.running.size(); ) {
                    destroyThread(this.running.get(i), "Stopped.");
                    i++;
                }
            } else {
                HttpProxy[] proxys;
                if (Config.chanName.contains("NoName")) {
                    this.uim.getUI().logError("No chan selected");
                    return;
                }
                this.work = true;
                this.uim.getUI().SwitchStartStop();
                int threads = this.uim.getUI().getThreads();
                if (Config.noProxy) {
                    proxys = new HttpProxy[1];
                    proxys[0] = new HttpProxy("no proxy", -1);
                    threads = 1;
                } else {
                    proxys = (HttpProxy[]) this.pmr.toArray((Object[]) new HttpProxy[0]);
                }
                for (int i = 0; i < threads; i++)
                    runWipe(proxys[i]);
                this.uim.getUI().setTitle("Рогатулечка. Чан: " + Config.chanName + ", Доска: " + Config.board + ". Потоков: " + this.running.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.work = false;
            this.uim.getUI().SwitchStartStop();
            this.uim.getUI().showMessage("Ошибка при запуске.\n" + e.toString(), 0);
        }
    }

    public boolean isReady() {
        return this.notReady.isEmpty();
    }

    public void removeFromNotReady(AbstractWipe wipe) {
        this.notReady.remove(wipe);
        if (this.running.contains(wipe))
            this.running.remove(wipe);
        this.running.add(wipe);
        this.uim.getUI().logInfo("Ready threads: " + this.running.size());
    }

    public void runThreads() {
        for (AbstractWipe w : this.running) {
            this.uim.getUI().logInfo("Notifyng thread [ " + w.toString() + " ] ...");
            w.myNotify();
        }
        this.running.clear();
    }

    public void runWipe(HttpProxy p) {
        WakabaWipe wakabaWipe;
        KusabaWipe kusabaWipe;
        HanabiraWipe hanabiraWipe;
        FutabaWipe futabaWipe;
        AbstractWipe ww = null;
        Chan chan = this.cm.getByName(Config.chanName);
        if (chan == null || chan.ChanType == null) {
            this.uim.getUI().logError("unknown Chan: " + Config.chanName + " type: " + chan.ChanType.name());
            this.uim.getUI().SwitchStartStop();
            return;
        }
        switch (chan.ChanType) {
            case Wakaba:
                wakabaWipe = new WakabaWipe(p, this, chan);
                break;
            case Kusaba:
                kusabaWipe = new KusabaWipe(p, this, chan);
                break;
            case Hanabira:
                hanabiraWipe = new HanabiraWipe(p, this, chan);
                break;
            case Futaba:
                futabaWipe = new FutabaWipe(p, this, chan);
                break;
        }
        if (futabaWipe != null) {
            if (Config.waitForNotReady) {
                this.notReady.add(futabaWipe);
            } else {
                this.running.add(futabaWipe);
            }
        } else {
            this.uim.getUI().logError("Unknown Chan: " + Config.chanName + " type: " + chan.ChanType.name());
            this.uim.getUI().SwitchStartStop();
            return;
        }
    }

    public synchronized int getAllSuccessful() {
        int i = 0;
        for (int l = 0; l < this.running.size(); l++)
            i += ((AbstractWipe) this.running.get(l)).getSuccessful();
        return i;
    }

    public synchronized int getAllFailed() {
        int i = 0;
        for (int l = 0; l < this.running.size(); l++)
            i += ((AbstractWipe) this.running.get(l)).getFailed();
        return i;
    }

    public int size() {
        return this.running.size();
    }

    public void reStart(AbstractWipe ww) {
        WakabaWipe wakabaWipe;
        KusabaWipe kusabaWipe;
        HanabiraWipe hanabiraWipe;
        FutabaWipe futabaWipe;
        this.running.remove(ww);
        switch ((ww.getChan()).ChanType) {
            case Wakaba:
                wakabaWipe = new WakabaWipe(ww.getProxy(), this, ww.getChan());
                break;
            case Kusaba:
                kusabaWipe = new KusabaWipe(wakabaWipe.getProxy(), this, wakabaWipe.getChan());
                break;
            case Hanabira:
                hanabiraWipe = new HanabiraWipe(kusabaWipe.getProxy(), this, kusabaWipe.getChan());
                break;
            case Futaba:
                futabaWipe = new FutabaWipe(hanabiraWipe.getProxy(), this, hanabiraWipe.getChan());
                break;
        }
        this.running.add(futabaWipe);
    }

    public void destroyThread(AbstractWipe ww, String reason) {
        try {
            synchronized (lock) {
                if (ww == null)
                    return;
                ww.myNotify();
                ww.stop();
                if (!this.running.contains(ww) || !this.notReady.contains(ww))
                    return;
                if (!this.running.remove(ww))
                    this.notReady.remove(ww);
                this.work = !this.running.isEmpty();
                this.uim.getUI().SwitchStartStop();
                this.uim.getTray().switchState();
                if ((Config.useTmp || Config.randomPicGenerate) && ww.getFile().exists())
                    ww.getFile().delete();
                if (this.work)
                    this.uim.getUI().logInfo("Threads count is: " + this.running.size());
                this.uim.getUI().setTitle("Рогатулечка. Чан: " + Config.chanName + ", Доска: " + Config.board + ". Потоков: " + this.running.size());
                if (!this.work)
                    this.uim.getUI().setTitle("Рогатулечка. Чан: " + Config.chanName + ", Доска: " + Config.board + ".");
                if (ww.isError()) {
                    this.uim.getUI().logError("Thread [" + ww.getThread().getName() + "] stoped by ThreadManager.\nReason: " + reason);
                    if (Config.useGui)
                        this.uim.getTray().trayPrintError("Ханюша", "Поток " + ww.getThread().getName() + "\nЗавершён причина: " + reason);
                } else {
                    if (reason != null && reason.isEmpty()) {
                        this.uim.getUI().logInfo("Thread [" + ww + "] stoped.");
                    } else {
                        this.uim.getUI().logInfo("Thread [" + ww + "] stoped.\nReason: " + reason);
                    }
                    if (reason != null && reason.isEmpty()) {
                        this.uim.getTray().trayPrint("Ханюша", "Поток " + ww + " завершён.");
                    } else {
                        this.uim.getTray().trayPrint("Ханюша", "Поток " + ww + " завершён.\nПричина: " + reason);
                    }
                }
                ww = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
