package hanyuu.managers;

import config.Config;
import hanyuu.chan.Chan;
import hanyuu.ext.ScriptContainer;
import hanyuu.ext.interfaces.OCR;
import hanyuu.net.proxy.HttpProxy;
import ui.cli.CLI;
import ui.cli.CTray;
import ui.gui.GUI;
import ui.gui.Tray;
import ui.interfaces.ITray;
import ui.interfaces.UI;

public class UIManager {
    private UI ui;

    private ITray tray;

    private ThreadManager tm;

    public UIManager(ThreadManager tm) {
        this.tm = tm;
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() throws Exception {
        if (Config.useGui) {
            System.out.println("Starting GUI");
            this.ui = (UI) new GUI(this.tm);
            this.tray = (ITray) new Tray(this.tm);
            this.tray.setUI(this.ui);
            this.tray.addIcon();
            for (Chan ca : this.tm.getChanManager())
                this.ui.addChan(ca.ChanName);
            for (HttpProxy p : this.tm.getProxyManager())
                this.ui.addProxy(p);
            for (ScriptContainer s : this.tm.getScripts().getScripts())
                this.ui.addScript(s);
            for (OCR ocr : this.tm.getScripts().getOCRs().values())
                this.ui.addOCR(ocr);
            this.ui.reSelectChan();
            this.ui.reSelectOCR();
            this.ui.setVisible(true);
        } else {
            this.ui = (UI) new CLI(this.tm);
            this.tray = (ITray) new CTray();
        }
        this.ui.logInfo("Loaded " + this.tm.getProxyManager().size() + " proxys");
        this.ui.logInfo("Loaded: " + this.tm.getCopyPasteManager().size() + " copypaste blocks");
        System.gc();
    }

    public UI getUI() {
        return this.ui;
    }

    public ITray getTray() {
        return this.tray;
    }
}
