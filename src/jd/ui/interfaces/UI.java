package ui.interfaces;

import hanyuu.ext.ScriptContainer;
import hanyuu.ext.interfaces.OCR;
import hanyuu.net.proxy.HttpProxy;
import hanyuu.net.wipe.AbstractWipe;

public interface UI {
    void SwitchStartStop();

    int getThreads();

    void logInfo(String paramString);

    void logError(String paramString);

    void setVisible(boolean paramBoolean);

    void setTitle(String paramString);

    void addChan(String paramString);

    void reSelectChan();

    void setCaptcha(AbstractWipe paramAbstractWipe);

    void updateThreadStatus();

    void updateThreadStatus(AbstractWipe paramAbstractWipe);

    void removeThread(AbstractWipe paramAbstractWipe);

    void setStatus(AbstractWipe paramAbstractWipe);

    void setUpState(boolean paramBoolean);

    void setSuccessful(int paramInt);

    void setFailed(int paramInt);

    void seWindowtState(int paramInt);

    void addScript(ScriptContainer paramScriptContainer);

    void addProxy(HttpProxy paramHttpProxy);

    void removeProxy(HttpProxy paramHttpProxy);

    void setProxyProgress(int paramInt);

    void addOCR(OCR paramOCR);

    void reSelectOCR();

    void showMessage(String paramString, int paramInt);
}
