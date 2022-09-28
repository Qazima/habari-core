package com.qazima.habari.core.configuration;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import lombok.Getter;
import lombok.Setter;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

public class Server {
    @Getter
    private final List<String> certificatesUrls = new ArrayList<>();
    private HttpServer httpServer;
    private HttpsServer httpsServer;
    @Getter
    @Setter
    private String configurationUri = "/configuration";
    @Getter
    @Setter
    private String host = "127.0.0.1";
    @Getter
    @Setter
    private String keyFile = "";
    @Getter
    @Setter
    private String keyManagerProtocol = "";
    @Getter
    @Setter
    private String keyPassword = "";
    @Getter
    @Setter
    private String metadataUri = "/metadata";
    @Getter
    @Setter
    private int port = 9000;
    @Getter
    @Setter
    private boolean secured = false;
    @Getter
    @Setter
    private String sslProtocol = "";
    @Getter
    @Setter
    private String storePassword = "";
    @Getter
    @Setter
    private String storeProtocol = "";
    @Getter
    @Setter
    private String trustManagerProtocol = "";
    @Getter
    @Setter
    private String uri = "/";

    public void configureListener() throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        if (isSecured()) {
            FileInputStream fileInputStream = new FileInputStream(getKeyFile());
            KeyStore keyStore = KeyStore.getInstance(getStoreProtocol());
            keyStore.load(fileInputStream, getStorePassword().toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(getKeyManagerProtocol());
            keyManagerFactory.init(keyStore, getKeyPassword().toCharArray());
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(getTrustManagerProtocol());
            trustManagerFactory.init(keyStore);
            httpsServer = HttpsServer.create(new InetSocketAddress(getHost(), getPort()), 0);
            SSLContext sslContext = SSLContext.getInstance(getSslProtocol());
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
            httpsServer.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
                public void configure(HttpsParameters httpsParameters) {
                    try {
                        SSLContext context = SSLContext.getDefault();
                        SSLEngine engine = context.createSSLEngine();
                        httpsParameters.setNeedClientAuth(false);
                        httpsParameters.setCipherSuites(engine.getEnabledCipherSuites());
                        httpsParameters.setProtocols(engine.getEnabledProtocols());
                        SSLParameters parameters = context.getDefaultSSLParameters();
                        httpsParameters.setSSLParameters(parameters);
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } else {
            httpServer = HttpServer.create(new InetSocketAddress(getHost(), getPort()), 0);
        }
    }

    public HttpServer getListener() {
        return isSecured() ? httpsServer : httpServer;
    }

    public void startListener() {
        getListener().setExecutor(null);
        getListener().start();
    }
}
