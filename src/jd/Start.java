import config.Config;
import hanyuu.managers.ThreadManager;
import hanyuu.net.proxy.ProxyManager;

import java.io.File;

public class Start {
    public static void main(String[] args) throws Exception {
        Config.load();
        if (args.length != 0 && args[0].contains("-check")) {
            Config.checkOnLoad = true;
            Config.checker = true;
            File f = new File("./checked_proxy.txt");
            f.delete();
            f.createNewFile();
            ProxyManager p = new ProxyManager();
            System.out.println("Proxy count is: " + p.size());
        } else {
            new ThreadManager((args.length != 0 && args[0].contains("-console")));
        }
    }
}
