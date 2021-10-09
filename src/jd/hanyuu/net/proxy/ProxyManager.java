package hanyuu.net.proxy;

import config.Config;
import hanyuu.managers.ThreadManager;
import hanyuu.net.HTTPClient;
import ui.interfaces.UI;
import utils.Constants;
import utils.Utils;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ProxyManager extends ArrayList<HttpProxy> implements Constants {
    private String proxysFile = "./ini/proxy.ini";

    private final String comment = "#Формат\n#proxy:port\n#или\n#proxy:port:login:password\n";

    private boolean file = false;

    private int pos = 0;

    public int progress = 1;

    private UI ui = null;

    public ProxyManager() {
        if (!(new File(this.proxysFile)).exists()) {
            System.out.println("ProxyManager, file " + this.proxysFile + " not found.");
            System.exit(-1);
        }
        load(this.proxysFile);
    }

    public void addProxy(HttpProxy p) {
        add(p);
        try {
            RandomAccessFile raf = new RandomAccessFile(this.proxysFile, "rw");
            raf.skipBytes((int) raf.length());
            raf.writeBytes(p.toString() + "\n");
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load(String file) {
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String s;
            while ((s = br.readLine()) != null) {
                this.pos++;
                if (s.startsWith("#") || s.isEmpty() || s.length() < 5)
                    continue;
                s.replace(" ", "");
                StringTokenizer st = new StringTokenizer(s, ":");
                HttpProxy p = null;
                String host = st.nextToken();
                int port = Integer.parseInt(st.nextToken());
                if (st.hasMoreTokens()) {
                    String login = st.nextToken();
                    String password = st.nextToken();
                    if (!isInList(host))
                        p = new HttpProxy(host, port, login, password);
                } else if (!isInList(host)) {
                    p = new HttpProxy(host, port);
                }
                if (p != null)
                    add(p);
                if (Config.checkOnLoad)
                    for (int i = 0; i < size(); i++) {
                        System.out.print("[" + i + "]");
                        if (Config.checker)
                            checker();
                    }
            }
            fr.close();
            br.close();
            saveFile();
        } catch (Exception e) {
            System.err.println("Position is: " + this.pos);
            e.printStackTrace();
        }
    }

    private void checker() {
        for (HttpProxy p : this)
            check(p, (UI) null);
    }

    private boolean isInList(String host) {
        for (HttpProxy h : this) {
            if (h.getHost().contains(host)) {
                System.out.println("Warning! Proxy " + host + " duplicated!");
                return true;
            }
        }
        return false;
    }

    public void writeChecked(HttpProxy p) {
        synchronized (lock) {
            try {
                File f = new File("./checked_proxy.txt");
                if (!f.exists())
                    f.createNewFile();
                RandomAccessFile raf = new RandomAccessFile(f, "rw");
                raf.skipBytes((int) raf.length());
                raf.writeBytes(p.toString() + "\n");
                raf.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveFile() {
        try {
            if (this.file)
                Thread.sleep(250L);
            synchronized (lock) {
                File p = new File(this.proxysFile);
                this.file = true;
                p.delete();
                FileWriter fr = new FileWriter(this.proxysFile);
                BufferedWriter bw = new BufferedWriter(fr);
                bw.write("#Формат\n#proxy:port\n#или\n#proxy:port:login:password\n");
                for (HttpProxy proxy : this)
                    bw.write(proxy.toString() + "\n");
                bw.close();
                fr.close();
                this.file = false;
            }
        } catch (Exception e) {
            this.file = false;
            e.printStackTrace();
        }
    }

    public void check(final HttpProxy proxy, UI u) {
        System.out.println("Checking: " + proxy);
        this.ui = u;
        Utils.startThread(new Runnable() {
            public void run() {
                if (Config.emulateWipe) {
                    ThreadManager tm = ThreadManager.getInstance();
                    tm.runWipe(proxy);
                }
                if (Config.contentCheck && ProxyManager.this.simple(proxy)) {
                    if (!ProxyManager.this.full(proxy))
                        ProxyManager.this.delete(proxy, ProxyManager.this.ui);
                } else if (!ProxyManager.this.simple(proxy)) {
                    ProxyManager.this.delete(proxy, ProxyManager.this.ui);
                }
                if (ProxyManager.this.ui != null)
                    ProxyManager.this.ui.setProxyProgress(ProxyManager.this.progress++);
                ProxyManager.this.writeChecked(proxy);
            }
        });
    }

    public void check(String host, int port) {
        HttpProxy p = new HttpProxy(host, port);
        check(p, (UI) null);
    }

    public void delete(HttpProxy proxy, UI ui) {
        synchronized (lock) {
            remove(proxy);
            if (ui != null)
                ui.removeProxy(proxy);
            saveFile();
            System.out.println("deleted " + proxy.getHost());
        }
    }

    private boolean simple(HttpProxy proxy) {
        try {
            Socket s = new Socket();
            s.connect(new InetSocketAddress(proxy.getHost(), proxy.getPort()), 1000);
            boolean result = s.isConnected();
            s.close();
            if (result)
                if (this.ui != null) {
                    this.ui.logInfo(proxy + " работает.");
                } else {
                    System.out.println(proxy + " work");
                }
            return result;
        } catch (Exception e) {
            if (this.ui != null) {
                this.ui.logError(proxy + " не работает.");
            } else {
                System.out.println(proxy + " dont work");
            }
            return false;
        }
    }

    private boolean full(HttpProxy proxy) {
        try {
            HTTPClient http = new HTTPClient(proxy);
            BufferedReader br = http.getBufferedReader(Config.syte);
            String s = "", s2 = "";
            while ((s = br.readLine()) != null)
                s2 = s2 + s;
            if (s2.contains(Config.syteKey)) {
                if (this.ui != null) {
                    this.ui.logInfo(proxy + " ответ от " + Config.syte + " получен.");
                } else {
                    System.out.println(proxy + " all ok, proxy work");
                }
                return true;
            }
            if (this.ui != null) {
                this.ui.logError(proxy + " ответ от " + Config.syte + " не получен.");
            } else {
                System.out.println(proxy + " bad content.");
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            if (this.ui != null) {
                this.ui.logError(proxy + " ответ от " + Config.syte + " не получен.");
            } else {
                System.out.println(proxy + " bad content.");
            }
            return false;
        }
    }
}
