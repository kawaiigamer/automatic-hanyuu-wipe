package utils;

import config.Config;
import hanyuu.net.wipe.AbstractWipe;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class CaptchaUtils {
    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage)
            return (BufferedImage) image;
        image = (new ImageIcon(image)).getImage();
        boolean hasAlpha = hasAlpha(image);
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            int transparency = 1;
            if (hasAlpha == true)
                transparency = 2;
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
        }
        if (bimage == null) {
            int type = 1;
            if (hasAlpha == true)
                type = 2;
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }
        Graphics g = bimage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bimage;
    }

    public static boolean hasAlpha(Image image) {
        if (image instanceof BufferedImage)
            return ((BufferedImage) image).getColorModel().hasAlpha();
        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
        }
        return pg.getColorModel().hasAlpha();
    }

    public static String getScaledCaptchaBase64(double scaleW, double scaleH, AbstractWipe wipe) {
        return Utils.encodeLines(getScaledCaptchaBytes(scaleW, scaleH, wipe));
    }

    public static byte[] getScaledCaptchaBytes(double scaleW, double scaleH, AbstractWipe wipe) {
        try {
            return IOUtils.toByteArray(getScaledCaptchaStream(scaleW, scaleH, wipe));
        } catch (Exception e) {
            wipe.setException(e);
            wipe.getThreadManager().handleError(wipe);
            return null;
        }
    }

    public static InputStream getScaledCaptchaStream(double scaleW, double scaleH, AbstractWipe wipe) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Image c = ImageIO.read(wipe.getCaptchaStream());
            int w = (int) Math.round(c.getWidth(null) * ((scaleW < 1.0D) ? 1.0D : scaleW));
            int h = (int) Math.round(c.getHeight(null) * ((scaleH < 1.0D) ? 1.0D : scaleH));
            c = c.getScaledInstance(w, h, 1);
            BufferedImage bi = toBufferedImage(c);
            if (Config.reverseCaptcha)
                bi = inverse(bi);
            ImageIO.write(bi, (wipe.getChan()).CaptchaType, out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            wipe.setException(e);
            wipe.getThreadManager().handleError(wipe);
            return null;
        }
    }

    public static BufferedImage inverse(BufferedImage bi) {
        int _w = bi.getWidth();
        int _h = bi.getHeight();
        for (int x = 0; x < _w; x++) {
            for (int y = 0; y < _h; y++)
                bi.setRGB(x, y, bi.getRGB(x, y) ^ 0xFFFFFF);
        }
        return bi;
    }
}
