package com.qazima.habari.core.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qazima.habari.core.content.ContentManager;
import lombok.Getter;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Parameter {
    @Getter
    @JsonProperty("configurations")
    private final List<Configuration> configurations = new ArrayList<>();

    public void synchronizeServersConfiguration() {
        for (Configuration configuration : getConfigurations()) {
            configuration.synchronizeConnectionsTypes();

            for (Server server : configuration.getServers()) {
                if(server.getDeleteAllowed().isEmpty()) {
                    server.setDeleteAllowed(Optional.of(configuration.isDeleteAllowed()));
                }
                if(server.getGetAllowed().isEmpty()) {
                    server.setGetAllowed(Optional.of(configuration.isGetAllowed()));
                }
                if(server.getPostAllowed().isEmpty()) {
                    server.setPostAllowed(Optional.of(configuration.isPostAllowed()));
                }
                if(server.getPutAllowed().isEmpty()) {
                    server.setPutAllowed(Optional.of(configuration.isPutAllowed()));
                }
            }
        }
    }

    public void configureServers() throws IOException, UnrecoverableKeyException, CertificateException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        for (Configuration configuration : getConfigurations()) {
            configuration.configureServers();
        }
    }

    public List<ContentManager> configureContentManagers() {
        List<ContentManager> contentManagers = new ArrayList<>();
        for (Configuration configuration : getConfigurations()) {
            contentManagers.add(new ContentManager(configuration));
        }
        return contentManagers;
    }

    public void startServers() {
        for (Configuration configuration : getConfigurations()) {
            configuration.startServers();
        }
    }
}
