package ui.cli;

import config.Config;
import hanyuu.ext.ScriptContainer;
import hanyuu.ext.interfaces.OCR;
import hanyuu.managers.ThreadManager;
import hanyuu.net.proxy.HttpProxy;
import hanyuu.net.wipe.AbstractWipe;
import ui.interfaces.UI;
import utils.Utils;

public class CLI implements UI {
    private ThreadManager tm;

    public CLI(ThreadManager tm) {
        this.tm = tm;
        System.out.println("Auto Hanyuu\nRunning in console mode.");
    }

    public void SwitchStartStop() {
    }

    public void reSelectChan() {
    }

    public void removeThread(AbstractWipe par1) {
    }

    public void addChan(String chan) {
    }

    public void setCaptcha(AbstractWipe w) {
    }

    public void setStatus(AbstractWipe s) {
    }

    public void setUpState(boolean b) {
    }

    public void setVisible(boolean b) {
    }

    public void setTitle(String s) {
    }

    public void seWindowtState(int s) {
    }

    public void updateThreadStatus() {
    }

    public void updateThreadStatus(AbstractWipe w) {
    }

    public void addScript(ScriptContainer s) {
    }

    public void showMessage(String msg, int type) {
    }

    public void removeProxy(HttpProxy p) {
    }

    public void setProxyProgress(int i) {
    }

    public void addOCR(OCR ocr) {
    }

    public void addProxy(HttpProxy p) {
    }

    public void reSelectOCR() {
    }

    public int getThreads() {
        if (this.tm.getProxyManager().size() < Config.threads) {
            System.out.println("Threads could not be more than proxys.");
            System.exit(0);
        }
        if (Config.threads == -1)
            return this.tm.getProxyManager().size();
        if (Config.threads < 1 && Config.threads != -1) {
            System.out.println("Thread could not be less then one.");
            System.exit(0);
        }
        return Config.threads;
    }

    public void setFailed(int i) {
        System.out.println("[" + Utils.getTime(":") + "] Failed: " + i);
    }

    public void setSuccessful(int i) {
        System.out.println("[" + Utils.getTime(":") + "] Successful: " + i);
    }

    public void logInfo(String s) {
        System.out.println("[" + Utils.getTime(":") + "] " + s);
    }

    public void logError(String s) {
        System.err.println("[" + Utils.getTime(":") + "] " + s);
    }
}
