package com.qazima.habari.core.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.qazima.habari.plugin.core.deserializer.OptionalBooleanDeserializer;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.LogManager;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Server {
    @Getter
    private final List<String> certificatesUrls = new ArrayList<>();
    private HttpServer httpServer;
    private HttpsServer httpsServer;
    @Getter
    @Setter
    @JsonProperty("configurationUri")
    private String configurationUri = "";
    @Getter
    @Setter
    @JsonProperty("allowDelete")
    @JsonDeserialize(using = OptionalBooleanDeserializer.class)
    private Optional<Boolean> deleteAllowed = Optional.empty();
    @Getter
    @Setter
    @JsonProperty("allowGet")
    @JsonDeserialize(using = OptionalBooleanDeserializer.class)
    private Optional<Boolean> getAllowed = Optional.empty();
    @Getter
    @Setter
    @JsonProperty("host")
    private String host = "127.0.0.1";
    @Getter
    @Setter
    @JsonProperty("keyFile")
    private String keyFile = "";
    @Getter
    @Setter
    @JsonProperty("keyManagerProtocol")
    private String keyManagerProtocol = "";
    @Getter
    @Setter
    @JsonProperty("keyPassword")
    private String keyPassword = "";
    @Getter
    @Setter
    @JsonProperty("metadataUri")
    private String metadataUri = "";
    @Getter
    @Setter
    @JsonProperty("port")
    private int port = 9000;
    @Getter
    @Setter
    @JsonProperty("allowPost")
    @JsonDeserialize(using = OptionalBooleanDeserializer.class)
    private Optional<Boolean> postAllowed = Optional.empty();
    @Getter
    @Setter
    @JsonProperty("allowPut")
    @JsonDeserialize(using = OptionalBooleanDeserializer.class)
    private Optional<Boolean> putAllowed = Optional.empty();
    @Getter
    @Setter
    @JsonProperty("secured")
    private boolean secured = false;
    @Getter
    @Setter
    @JsonProperty("sslProtocol")
    private String sslProtocol = "";
    @Getter
    @Setter
    @JsonProperty("storePassword")
    private String storePassword = "";
    @Getter
    @Setter
    @JsonProperty("storeProtocol")
    private String storeProtocol = "";
    @Getter
    @Setter
    @JsonProperty("trustManagerProtocol")
    private String trustManagerProtocol = "";
    @Getter
    @Setter
    @JsonProperty("uri")
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
        if(getListener() != null) {
            getListener().setExecutor(null);
            getListener().start();
        } else {
            LogManager.getLogger(ConfigurationManager.class).fatal("Configuration for the server http" + (isSecured() ? "s" : "" ) + "://" + getHost() + ":" + getPort() + getUri() + " is not well defined. Please refer the documentation");
        }
    }
}
