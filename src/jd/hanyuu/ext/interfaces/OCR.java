package hanyuu.ext.interfaces;

import hanyuu.net.wipe.AbstractWipe;

public interface OCR extends SimpleScript {
    void recognizeCaptcha(AbstractWipe paramAbstractWipe);

    void onError();

    void onSuccess();

    String toString();
}
