package com.qazima.habari.core.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qazima.habari.core.content.ConfigurationContext;
import com.qazima.habari.plugin.core.Plugin;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

public class Configuration {
    @Getter
    @JsonProperty("connections")
    private final List<Plugin> connections = new ArrayList<>();
    @Getter
    @JsonProperty("servers")
    private final List<Server> servers = new ArrayList<>();
    @Getter
    @Setter
    @JsonProperty("defaultPageSize")
    private int defaultPageSize = 50;
    @Getter
    @Setter
    @JsonProperty("allowDelete")
    private boolean deleteAllowed = false;
    @Getter
    @Setter
    @JsonProperty("allowGet")
    private boolean getAllowed = false;
    @Getter
    @Setter
    @JsonProperty("allowPost")
    private boolean postAllowed = false;
    @Getter
    @Setter
    @JsonProperty("allowPut")
    private boolean putAllowed = false;

    public void synchronizeConnectionsTypes() {
        for (Plugin plugin : getConnections()){
            plugin.setConnectionType(plugin.getClass().getTypeName());
        }
    }

    public void configureServers() throws IOException, UnrecoverableKeyException, CertificateException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        for (Server server : getServers()) {
            server.configureListener();
        }
    }

    public void startServers() {
        for (Server server : getServers()) {
            server.startListener();
            server.getListener().createContext(server.getUri(), new ConfigurationContext(this));
        }
    }
}
