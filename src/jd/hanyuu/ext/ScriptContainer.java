package hanyuu.ext;

import hanyuu.ext.interfaces.WipeExtension;

import java.io.File;

public class ScriptContainer {
    public boolean use = true;

    public WipeExtension script;

    public File path;

    public ScriptContainer(WipeExtension script, File path) {
        this.script = script;
        this.path = path;
    }

    public String toString() {
        return this.script.getInfo();
    }
}
