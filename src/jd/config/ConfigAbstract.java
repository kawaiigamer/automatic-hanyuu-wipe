package config;

import utils.Constants;

import java.io.*;
import java.util.Properties;

public class ConfigAbstract implements Constants {
    private static final Properties p = new Properties();

    private static String _file;

    public static void setFile(String file) {
        _file = file;
    }

    public static void Load(String file) {
        _file = file;
        File f = new File(_file);
        try {
            if (!f.exists()) {
                System.out.println("Файл конфига не нaйден!!");
                f.createNewFile();
            } else {
                BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                p.load(r);
                r.close();
            }
        } catch (Exception e) {
            System.out.println("error while loading");
            e.printStackTrace();
        }
    }

    public static boolean getBoolean(String value, String dvalue) {
        return Boolean.parseBoolean(getString(value, dvalue));
    }

    public static int getInt(String value, String dvalue) {
        return Integer.parseInt(getString(value, dvalue));
    }

    public static long getLong(String value, String dvalue) {
        return Long.parseLong(getString(value, dvalue));
    }

    public static String getString(String value, String dvalue) {
        return p.getProperty(value, dvalue);
    }

    public static void saveConfigParam(String key, String value) {
        p.setProperty(key, value);
        store();
    }

    public static void saveConfigParam(String key, boolean value) {
        saveConfigParam(key, String.valueOf(value));
    }

    public static void saveConfigParam(String key, int value) {
        saveConfigParam(key, String.valueOf(value));
    }

    public static void saveConfigParam(String key, double value) {
        saveConfigParam(key, String.valueOf(value));
    }

    public static void saveConfigParam(String key, long value) {
        saveConfigParam(key, String.valueOf(value));
    }

    public static void store() {
        try {
            BufferedWriter o = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(_file), "UTF-8"));
            p.store(o, "");
            o.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
