package config;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Config extends ConfigAbstract {
    public static boolean useGui;

    public static boolean BokuNoFile;

    public static boolean onePic;

    public static boolean checkOnLoad;

    public static boolean contentCheck;

    public static boolean noProxy;

    public static boolean silentBump;

    public static boolean emptySeparator;

    public static boolean mySeparator;

    public static boolean randomizePaste;

    public static boolean msgCount;

    public static boolean picsNotEdit;

    public static boolean picsPuck;

    public static boolean useTmp;

    public static boolean colorRndRnd;

    public static boolean pasteOnPic;

    public static boolean pasteNoMsg;

    public static boolean dontEditPixels;

    public static boolean randomBytes;

    public static boolean dontTrayMsg;

    public static boolean emulateWipe;

    public static boolean randomPicGenerate;

    public static boolean checker = false;

    public static boolean parser;

    public static boolean reverseCaptcha;

    public static boolean smartErrorHandler;

    public static boolean waitForNotReady;

    public static ArrayList<String[]> replacements = (ArrayList) new ArrayList<>();

    public static String path;

    public static String separator;

    public static String name;

    public static String keyForServices;

    public static String chanName;

    public static String password;

    public static String board;

    public static String thread;

    public static String theme;

    public static String msg;

    public static String userAgent;

    public static String FontName;

    public static String EncodingConsole;

    public static String syte;

    public static String syteKey;

    public static String tr;

    public static String email;

    public static double scaleW;

    public static double scaleH;

    public static int timeout;

    public static int fontSize;

    public static int page;

    public static int startX;

    public static int startY;

    public static int deltaY;

    public static int picsTxtR;

    public static int picsTxtG;

    public static int picsTxtB;

    public static int pixelEdit;

    public static int smartErrorCount;

    public static int smartErrorAction;

    public static String ocrMode;

    public static WorkMode workMode;

    public static int rndCount;

    public static int msgCountInt;

    public static int maxFileSize;

    public static int colorRnd;

    public static int threads;

    public static String ConfigFile = "./ini/config.ini";

    public static void load() {
        loadParams(ConfigFile);
    }

    public static void loadParams(String file) {
        Load(file);
        try {
            tr = getString("replacements", "");
            buildReplacements();
            emulateWipe = getBoolean("emulateWipe", "false");
            useGui = getBoolean("useGui", "true");
            randomBytes = getBoolean("randomBytes", "false");
            useTmp = getBoolean("useTmp", "true");
            smartErrorHandler = getBoolean("smartErrorHandler", "true");
            colorRndRnd = getBoolean("colorRndRnd", "true");
            emptySeparator = getBoolean("emptySeparator", "false");
            mySeparator = getBoolean("mySeparator", "true");
            randomizePaste = getBoolean("randomizePaste", "false");
            BokuNoFile = getBoolean("BokuNoFile", "false");
            checkOnLoad = getBoolean("checkOnLoad", "false");
            email = getString("email", "");
            onePic = getBoolean("onePic", "false");
            noProxy = getBoolean("noProxy", "false");
            contentCheck = getBoolean("contentCheck", "false");
            silentBump = getBoolean("silentBump", "false");
            msgCount = getBoolean("msgCount", "false");
            picsNotEdit = getBoolean("picsNotEdit", "false");
            picsPuck = getBoolean("picsPuck", "false");
            pasteOnPic = getBoolean("pasteOnPic", "false");
            pasteNoMsg = getBoolean("pasteNoMsg", "false");
            dontEditPixels = getBoolean("dontEditPixels", "false");
            dontTrayMsg = getBoolean("dontTrayMsg", "false");
            randomPicGenerate = getBoolean("randomPicGenerate", "false");
            parser = getBoolean("parser", "false");
            reverseCaptcha = getBoolean("reverseCaptcha", "false");
            waitForNotReady = getBoolean("waitForNotReady", "false");
            path = getString("path", "/hame/wipe/");
            keyForServices = getString("keyForServices", "");
            chanName = getString("chanName", "");
            name = getString("name", "Ханю");
            password = getString("password", "password");
            theme = getString("theme", "Hauu~");
            FontName = getString("FontName", "Tahoma");
            thread = getString("thread", "");
            board = getString("board", "/b/");
            msg = getString("msg", "Хаууу~ауу");
            userAgent = getString("userAgent", "");
            separator = getString("separator", "-=-=-=-");
            EncodingConsole = getString("EncodingConsole", "utf-8");
            syte = getString("syte", "http://google.com/");
            syteKey = getString("syteKey", "google");
            rndCount = getInt("rndCount", "0");
            timeout = getInt("timeOut", "0");
            threads = getInt("threads", "1");
            ocrMode = getString("ocrMode", "");
            workMode = WorkMode.valueOf(getString("workMode", "WipeBoard"));
            picsTxtR = getInt("picsTxtR", "0");
            picsTxtG = getInt("picsTxtG", "0");
            picsTxtB = getInt("picsTxtB", "0");
            scaleW = Double.valueOf(getString("scaleW", "1.0")).doubleValue();
            scaleH = Double.valueOf(getString("scaleH", "1.0")).doubleValue();
            startX = getInt("startX", "0");
            startY = getInt("startY", "0");
            deltaY = getInt("deltaY", "0");
            smartErrorCount = getInt("smartErrorCount", "1");
            smartErrorAction = getInt("smartErrorAction", "0");
            fontSize = getInt("fontSize", "14");
            page = getInt("page", "0");
            msgCountInt = getInt("msgCountInt", "0");
            maxFileSize = getInt("maxFileSize", "10000");
            colorRnd = getInt("colorRnd", "0");
            pixelEdit = getInt("pixelEdit", "0");
        } catch (Exception e) {
            System.out.println("error loading configs");
            e.printStackTrace();
        }
    }

    public static void buildReplacements() {
        replacements.clear();
        if (tr != null) {
            StringTokenizer st = new StringTokenizer(tr, ";");
            while (st.hasMoreTokens()) {
                StringTokenizer str = new StringTokenizer(st.nextToken(), ":");
                String[] r = {str.nextToken(), str.nextToken()};
                replacements.add(r);
            }
        }
    }
}
