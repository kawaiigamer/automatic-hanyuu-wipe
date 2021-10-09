package hanyuu.ext.interfaces;

import hanyuu.net.wipe.AbstractWipe;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.entity.mime.MultipartEntity;

import java.util.ArrayList;

public interface WipeExtension extends SimpleScript {
    void onRunWipe(AbstractWipe paramAbstractWipe);

    void onStopWipe(AbstractWipe paramAbstractWipe);

    Object[] onGetRequest(AbstractWipe paramAbstractWipe, String paramString, HttpHost paramHttpHost, ArrayList<String> paramArrayList, boolean paramBoolean);

    Object[] onPostRequest(AbstractWipe paramAbstractWipe, String paramString, HttpHost paramHttpHost, MultipartEntity paramMultipartEntity, ArrayList<String> paramArrayList);

    HttpResponse onGetAnswer(AbstractWipe paramAbstractWipe, HttpResponse paramHttpResponse);

    HttpResponse onPostAnswer(AbstractWipe paramAbstractWipe, HttpResponse paramHttpResponse);
}
