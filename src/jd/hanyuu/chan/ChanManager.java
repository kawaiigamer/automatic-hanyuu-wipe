package hanyuu.chan;

import config.Config;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import utils.Constants;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

public final class ChanManager extends ArrayList<Chan> implements Constants {
    private final String ChanList = "./ini/chans.xml";

    private ChanType type;

    public ChanManager() {
        if (!(new File("./ini/chans.xml")).exists()) {
            System.out.println("ChanManager, file ./ini/chans.xml not found.");
            System.exit(-1);
        }
        try {
            loadChans();
        } catch (Exception e) {
            System.err.println("Error while loading chans!");
        }
    }

    public void createAndLoad() {
        try {
            Chan defaultChan = getByName(Config.chanName);
            int http = defaultChan.ChanHost.indexOf("//");
            String name = "";
            if (http > 0) {
                name = defaultChan.ChanHost.substring(http + 2);
            } else {
                name = defaultChan.ChanHost;
            }
            Config.ConfigFile = "./ini/chans/" + name + ".ini";
            Config.setFile(Config.ConfigFile);
            File f = new File(Config.ConfigFile);
            if (!f.exists()) {
                f.createNewFile();
                Config.store();
            } else {
                Config.load();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadChans() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringComments(true);
            Document doc = factory.newDocumentBuilder().parse(new File("./ini/chans.xml"));
            NodeList list = doc.getElementsByTagName("chan");
            for (int i = 0; i < list.getLength(); i++) {
                Chan chan = new Chan();
                Node chanNode = list.item(i);
                NodeList values = chanNode.getChildNodes();
                for (int n = 0; n < values.getLength(); n++) {
                    Node value = values.item(n);
                    NamedNodeMap attrs = value.getAttributes();
                    String name = value.getNodeName();
                    if (attrs != null) {
                        String sValue = attrs.getNamedItem("value").getNodeValue();
                        if (name.contains("ChanName"))
                            chan.ChanName = sValue;
                        if (name.contains("ChanType"))
                            chan.ChanType = ChanType.valueOf(sValue);
                        if (name.contains("ChanHost"))
                            chan.ChanHost = sValue;
                        if (name.contains("ChanPosts"))
                            chan.ChanPosts = sValue;
                        if (name.contains("ScriptPath"))
                            chan.ScriptPath = sValue.replace("[%host]", chan.ChanHost);
                        if (name.contains("CaptchaPath"))
                            chan.CaptchaPath = sValue.replace("[%host]", chan.ChanHost);
                        if (name.contains("post"))
                            chan.post = sValue;
                        if (name.contains("task"))
                            chan.task = sValue;
                        if (name.contains("FieldEmail"))
                            chan.FieldEmail = sValue;
                        if (name.contains("FieldName"))
                            chan.FieldName = sValue;
                        if (name.contains("FieldCaptcha"))
                            chan.FieldCaptcha = sValue;
                        if (name.contains("FieldPassword"))
                            chan.FieldPassword = sValue;
                        if (name.contains("retName"))
                            chan.retName = sValue;
                        if (name.contains("retMode"))
                            chan.retMode = sValue;
                        if (name.contains("FieldFile"))
                            chan.FieldFile = sValue;
                        if (name.contains("FieldMessage"))
                            chan.FieldMessage = sValue;
                        if (name.contains("FieldTheme"))
                            chan.FieldTheme = sValue;
                        if (name.contains("CaptchaType"))
                            chan.CaptchaType = sValue;
                        if (name.contains("FieldThreadNumber"))
                            chan.FieldThreadNumber = sValue;
                        if (name.contains("MessageBan"))
                            chan.MessageBan = Pattern.compile(sValue);
                        if (name.contains("MessageInvalidCaptcha"))
                            chan.MessageInvalidCaptcha = Pattern.compile(sValue);
                        if (name.contains("MessageError"))
                            chan.MessageError = Pattern.compile(sValue);
                        if (name.contains("MessageSuccessful"))
                            chan.MessageSuccessful = Pattern.compile(sValue);
                        if (name.contains("BoardReplayTag"))
                            chan.BoardReplayTag = sValue;
                        if (name.contains("BoardReplayClass"))
                            chan.BoardReplayClass = sValue;
                        if (name.contains("RequestDeletePost"))
                            chan.RequestDeletePost = sValue;
                        if (name.contains("MessageIncorrectPassword"))
                            chan.MessageIncorrectPassword = Pattern.compile(sValue);
                        if (name.contains("FieldBoardName"))
                            chan.FieldBoardName = sValue;
                    }
                }
                add(chan);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public Chan getByName(String name) {
        for (Chan c : this) {
            if (c.ChanName.contains(name))
                return c;
        }
        return null;
    }

    public int getIndexName(String name) {
        for (Chan c : this) {
            if (c.ChanName.contains(name))
                return indexOf(c);
        }
        return -1;
    }
}
