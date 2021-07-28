package com.aliencat.communication.netty.https;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;

public class SSLContextFactory {

    public static SSLContext getContext() {
        FileInputStream fis = null;
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLSv1");
            KeyStore ks = KeyStore.getInstance("JKS");
            char[] password = "123456".toCharArray();
            fis = new FileInputStream("F:\\大象分享视频\\itjoin.jks");
            ks.load(fis, password);
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(ks, "123456".toCharArray());
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
            return sslContext;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
