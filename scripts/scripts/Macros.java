import hanyuu.ext.interfaces.WipeExtension;
import hanyuu.net.wipe.AbstractWipe;
import hanyuu.managers.ThreadManager;
import config.Config;
import utils.Constants;
import utils.Utils;
import java.util.ArrayList;
import org.apache.http.HttpHost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.HttpResponse;
import hanyuu.net.wipe.AbstractWipe;
/**
 *
 * @author Hanyuu Furude
 * Преобразует макросы в соответствующий текст.
 * Ну ещё это пример скрипта для Ханюши. 
 */
public class Macros implements WipeExtension {

    /**Метод будет вызван после загрузки скрипта в Ханю*/
    public void onLoad() {
        System.out.println("Loaded macros script.");
    }

    /**Метод будет вызван при запуске потока*/
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
        if (Config.replacements.size() > 0) {
            for (String[] s : Config.replacements) {
                tmp = tmp.replaceAll(s[0], s[1]);
            }
        }

        w.setMsg(tmp);
    }

    /**Метод будет вызван при остновке потока*/
    public void onStopWipe(AbstractWipe par0) {
    }

    /**Эти методы позволяют модифицировть данные от потока Wipe для Get запроса через скрипт, если не нужно то не изменяйте или верните null<br/>
     *   Страрайтесь что бы скрипты не запороли друг друга.
     * Хедеры: headers.add("Name,Value");
     */
    public Object[] onGetRequest(AbstractWipe w, String url, HttpHost host, ArrayList<String> headers, boolean saveCookies) {

        Object[] o = new Object[3];
        o[0] = url;
        o[1] = host;
        o[2] = headers;
        o[2] = true;
        //System.out.println("Geting from: " + url);
        return o;
    }

    /**Эти методы позволяют модифицировть данные от потока Wipe для Post запроса через скрипт, если не нужно то не изменяйте или верните null<br/>
     *  Страрайтесь что бы скрипты не запороли друг друга.
     */
    public Object[] onPostRequest(AbstractWipe w, String url, HttpHost host, MultipartEntity me, ArrayList<String> headers) {

        /*Object[] o = new Object[3];
        o[0] = url;
        o[1] = host;
        o[2] = me;
        o[2] = headers;*/
        return null;
    }
    /**Срабатывает после получения ответа на Get запрос и предоставляет ответ для обработки и/или модификации.*/
    public HttpResponse onGetAnswer(AbstractWipe w, HttpResponse hr)
    {
        return hr;
    }
    /**Срабатывает после получения отверта на Post запрос и предоставляет ответ для обработки и/или модификации.*/
    public HttpResponse onPostAnswer(AbstractWipe w, HttpResponse hr)
    {
        return hr;
    }
    /**Возвращает описание скрипта*/
    public String getInfo() {
        return "Macros script.";
    }
}