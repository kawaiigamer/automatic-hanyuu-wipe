package hanyuu.managers.pictures;

import hanyuu.managers.ThreadManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Pic extends File {
    public int w_i;

    public int h_i;

    public String w_s;

    public String h_s;

    public BufferedImage bi;

    public String format;

    public Pic(String name) {
        super(name);
        createBufferedImage(this);
    }

    public Pic(File f) {
        super(f.getAbsolutePath());
        this.format = getName().substring(getName().indexOf(".") + 1).toLowerCase();
        createBufferedImage(f);
    }

    private void createBufferedImage(File f) {
        try {
            this.bi = ImageIO.read(f);
            if (this.bi == null)
                createBufferedImage(ThreadManager.getInstance().getImgManager().getFile());
            this.w_i = this.bi.getWidth();
            this.h_i = this.bi.getHeight();
            this.w_s = String.valueOf(this.w_i);
            this.h_s = String.valueOf(this.h_i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
