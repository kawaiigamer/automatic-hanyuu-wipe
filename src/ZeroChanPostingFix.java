/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import hanyuu.net.wipe.AbstractWipe;
import java.util.ArrayList;
import org.apache.http.HttpHost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.HttpResponse;
import hanyuu.ext.interfaces.WipeExtension;
import java.util.HashMap;
import config.Config;
import java.io.BufferedReader;
import utils.Utils;
import config.WorkMode;
/**
 *
 * @author Hanyuu
 */
public class ZeroChanPostingFix implements WipeExtension {

    private HashMap<AbstractWipe, String> keys = new HashMap<AbstractWipe, String>();

    /**Метод будет вызван после загрузки скрипта в Ханю*/
    public void onLoad() {
        System.out.println("Loaded 0chan posting /b/ Fix  script.");
    }

    /**Метод будет вызван при запуске потока*/
    public void onRunWipe(AbstractWipe w) {
    }

    /**Метод будет вызван при остновке потока*/
    public void onStopWipe(AbstractWipe w) {
    }

    /**Эти методы позволяют модифицировть данные от потока Wipe для Get запроса через скрипт, если не нужно то не изменяйте или верните null<br/>
    Страрайтесь что бы скрипты не запороли друг друга.
     */
    public Object[] onGetRequest(AbstractWipe w, String url, HttpHost host, ArrayList<String> headers, boolean saveCookies) {
        return null;
    }

    /**Эти методы позволяют модифицировть данные от потока Wipe для Post запроса через скрипт, если не нужно то не изменяйте или верните null*/
    public Object[] onPostRequest(AbstractWipe w, String url, HttpHost host, MultipartEntity me, ArrayList<String> headers) {
        if (url.contains("0chan.hk/board.php?dir=/b/")) {
            BufferedReader br = null;

            if (Config.workMode==WorkMode.WipeBoard) {
                br = w.getHttpClient().getBufferedReader("http://0chan.hk" + Config.board);
            } else {
                br = w.getHttpClient().getBufferedReader("http://0chan.hk" + Config.board + "res/" + w.getActiveThread() + ".html");
            }
            
            String key = findeMM(br);
            me.addPart("mm", Utils.sb(key));
            String rm = String.valueOf(rm(key + w.getMsg() + Config.password));

            w.getHttpClient().addCookie("mm2=1");
            w.getHttpClient().addCookie("mm=" + rm);
            return new Object[]{url, host, me, headers};
        }
        return null;
    }

    private String findeMM(BufferedReader br) {
        try {
            String tmp = br.readLine();
            while ((tmp = br.readLine()) != null) {
                //p(tmp);
                if (tmp.contains("\"mm\"")) {
                    return tmp.substring(tmp.indexOf("value=\"") + 7).replace("\" /><tr>", "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**Срабатывает после получения ответа на Get запрос и предоставляет ответ для обработки и/или модификации.*/
    public HttpResponse onGetAnswer(AbstractWipe w, HttpResponse hr) {
        return null;
    }

    /**Срабатывает после получения ответа на Post запрос и предоставляет ответ для обработки и/или модификации.*/
    public HttpResponse onPostAnswer(AbstractWipe w, HttpResponse hr) {
        return null;
    }

    public String getInfo() {
        return "0chan posting /b/ fix.";
    }

    private static long rm(String f) {
        f = f.replaceAll("\n", "");
        int a = f.length();
        long e = 2 ^ a;
        int d = 0, c;
        while (a >= 4) {
            c = (((int) f.charAt((int) d) & 255)) | (((int) f.charAt((int) ++d) & 255) << 8) | (((int) f.charAt((int) ++d) & 255) << 16) | (((int) f.charAt((int) ++d) & 255) << 24);
            c = (((c & 65535) * 1540483477) + ((((c >>> 16) * 1540483477) & 65535) << 16));
            c ^= c >>> 24;
            c = (((c & 65535) * 1540483477) + ((((c >>> 16) * 1540483477) & 65535) << 16));
            e = (((e & 65535) * 1540483477) + ((((e >>> 16) * 1540483477) & 65535) << 16)) ^ c;
            a -= 4;
            ++d;

        }
        switch (a) {
            case 3:
                e ^= (f.charAt(d + 2) & 255) << 16;
            case 2:
                e ^= (f.charAt(d + 1) & 255) << 8;
            case 1:
                e ^= (f.charAt(d) & 255);
                e = (((e & 65535) * 1540483477) + ((((e >>> 16) * 1540483477) & 65535) << 16));
        }
        e ^= (int) e >>> 13;
        e = (((e & 65535) * 1540483477) + ((((e >>> 16) * 1540483477) & 65535) << 16));
        e ^= (int) e >>> 15;
        return Long.parseLong(Integer.toBinaryString((int) e), 2);
    }

    private static void p(Object o) {
        System.out.println(o);
    }
}
