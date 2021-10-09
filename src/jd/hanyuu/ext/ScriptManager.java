package hanyuu.ext;

import com.sun.tools.javac.Main;
import hanyuu.ext.interfaces.OCR;
import hanyuu.ext.interfaces.SimpleScript;
import hanyuu.ext.interfaces.WipeExtension;
import hanyuu.managers.ThreadManager;
import utils.Constants;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;

public class ScriptManager implements Constants {
    private static final File scripts_path = new File("./scripts/");

    private ThreadManager tm;

    private ArrayList<File> files = new ArrayList<>();

    private ArrayList<ScriptContainer> scripts = new ArrayList<>();

    private HashMap<String, OCR> ocrs = new HashMap<>();

    public ScriptManager(ThreadManager tm) {
        this.tm = tm;
        try {
            initFiles(scripts_path);
            for (File f : this.files) {
                File ff = null;
                String[] sfiles = new String[this.files.size() + 2];
                sfiles[0] = "-encoding";
                sfiles[1] = "UTF-8";
                for (int i = 0; i < this.files.size(); i++) {
                    sfiles[i + 2] = f.getAbsolutePath();
                    f.setLastModified(System.currentTimeMillis());
                    ff = new File(sfiles[i + 2].replace(".java", ".class"));
                    if (ff.exists())
                        ff.delete();
                }
                Main.compile(sfiles);
                String path = f.getPath().replace(f.getName(), "");
                URLClassLoader ucl = new URLClassLoader(new URL[]{(new File(path)).toURI().toURL()});
                SimpleScript script = (SimpleScript) ucl.loadClass(f.getName().replace(".java", "")).newInstance();
                if (ff != null && ff.exists())
                    ff.delete();
                if (script instanceof OCR) {
                    this.ocrs.put(script.getInfo(), (OCR) script);
                } else if (script instanceof WipeExtension) {
                    ScriptContainer sc = new ScriptContainer((WipeExtension) script, f);
                    this.scripts.add(sc);
                }
                script.onLoad();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading scripts.");
        }
    }

    public ArrayList<ScriptContainer> getScripts() {
        return this.scripts;
    }

    public HashMap<String, OCR> getOCRs() {
        return this.ocrs;
    }

    public void addOCR(OCR ocr) {
        this.ocrs.put(ocr.toString(), ocr);
    }

    public OCR getOCRbyName(String name) {
        return this.ocrs.get(name);
    }

    public void reLoad(ScriptContainer s) {
        try {
            File script_class = new File(s.path.getPath().replace(".java", ".class"));
            if (script_class.exists())
                script_class.delete();
            String[] sfiles = new String[3];
            sfiles[0] = "-encoding";
            sfiles[1] = "UTF-8";
            sfiles[2] = s.path.getPath();
            Main.compile(sfiles);
            this.scripts.remove(s);
            String path = s.path.getPath().replace(s.path.getName(), "");
            URLClassLoader ucl = new URLClassLoader(new URL[]{(new File(path)).toURI().toURL()});
            Class<?> c = ucl.loadClass(s.path.getName().replace(".java", ""));
            SimpleScript script_loaded = (SimpleScript) c.newInstance();
            if (script_class.exists())
                script_class.delete();
            if (script_loaded instanceof OCR) {
                this.ocrs.put(script_loaded.getInfo(), (OCR) script_loaded);
            } else if (script_loaded instanceof WipeExtension) {
                ScriptContainer sc = new ScriptContainer((WipeExtension) script_loaded, s.path);
                this.tm.getUIManager().getUI().addScript(sc);
                this.scripts.add(sc);
            }
            script_loaded.onLoad();
        } catch (Exception e) {
            e.printStackTrace();
            this.tm.getUIManager().getUI().logError("Ошибка загрузки скрипта: " + s + "\n" + e.toString());
        }
    }

    public void load(File f) {
        try {
            File script_class = new File(f.getPath().replace(".java", ".class"));
            if (script_class.exists())
                script_class.delete();
            String[] sfiles = new String[3];
            sfiles[0] = "-encoding";
            sfiles[1] = "utf8";
            sfiles[2] = f.getPath();
            Main.compile(sfiles);
            String path = f.getPath().replace(f.getName(), "");
            URLClassLoader ucl = new URLClassLoader(new URL[]{(new File(path)).toURI().toURL()});
            Class<?> c = ucl.loadClass(f.getName().replace(".java", ""));
            SimpleScript script_loaded = (SimpleScript) c.newInstance();
            if (script_class.exists())
                script_class.delete();
            for (ScriptContainer sc : this.scripts) {
                if (sc.script.getInfo().contains(script_loaded.getInfo())) {
                    this.tm.getUIManager().getUI().showMessage("Этот скрипт уже загружен.", 0);
                    return;
                }
            }
            if (script_loaded instanceof OCR) {
                this.ocrs.put(script_loaded.getInfo(), (OCR) script_loaded);
            } else if (script_loaded instanceof WipeExtension) {
                ScriptContainer sc = new ScriptContainer((WipeExtension) script_loaded, script_class);
                this.tm.getUIManager().getUI().addScript(sc);
                this.scripts.add(sc);
            }
            script_loaded.onLoad();
        } catch (Exception e) {
            e.printStackTrace();
            this.tm.getUIManager().getUI().logError("Ошибка загрузки скрипта: " + f.getName() + "\n" + e.toString());
        }
    }

    private void initFiles(File dir) {
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                initFiles(f);
            } else if (f.getName().endsWith(".java")) {
                this.files.add(f);
            }
        }
    }
}
