import config.Config;
import hanyuu.ext.interfaces.WipeExtension;
import hanyuu.managers.ThreadManager;
import hanyuu.net.wipe.AbstractWipe;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.entity.mime.MultipartEntity;
import utils.Constants;
import utils.Utils;

import java.util.ArrayList;

public class Macros implements WipeExtension {
    public void onLoad() {
        System.out.println("Loaded macros script.");
    }

    public void onRunWipe(AbstractWipe w) {
        String tmp = Config.msg;
        ThreadManager tm = w.getThreadManager();
        tmp = tmp.replaceAll("%rint%", String.valueOf(Constants.random.nextInt()));
        tmp = tmp.replaceAll("%rchars%", tm.getCopyPasteManager().getRandomCirilicString());
        if (Config.randomizePaste) {
            tmp = tmp.replaceAll("%paste%", tm.getCopyPasteManager().randomize(tm.getCopyPasteManager().getCopyPaste()));
        } else {
            tmp = tmp.replaceAll("%paste%", tm.getCopyPasteManager().getCopyPaste());
        }
        tmp = tmp.replaceAll("%time%", Utils.getTime(":"));
        if (Config.replacements.size() > 0)
            for (String[] s : Config.replacements)
                tmp = tmp.replaceAll(s[0], s[1]);
        w.setMsg(tmp);
    }

    public void onStopWipe(AbstractWipe par0) {
    }

    public Object[] onGetRequest(AbstractWipe w, String url, HttpHost host, ArrayList<String> headers, boolean saveCookies) {
        Object[] o = new Object[3];
        o[0] = url;
        o[1] = host;
        o[2] = headers;
        o[2] = Boolean.valueOf(true);
        return o;
    }

    public Object[] onPostRequest(AbstractWipe w, String url, HttpHost host, MultipartEntity me, ArrayList<String> headers) {
        return null;
    }

    public HttpResponse onGetAnswer(AbstractWipe w, HttpResponse hr) {
        return hr;
    }

    public HttpResponse onPostAnswer(AbstractWipe w, HttpResponse hr) {
        return hr;
    }

    public String getInfo() {
        return "Macros script.";
    }
}
