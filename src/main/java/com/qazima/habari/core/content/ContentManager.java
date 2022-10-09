package com.qazima.habari.core.content;

import com.qazima.habari.core.configuration.Configuration;
import com.qazima.habari.core.configuration.Server;

public class ContentManager {
    public ContentManager(Configuration configuration) {
        for (Server server :
                configuration.getServers()) {
            if(server.getUri() != null && server.getUri().length() > 0) {
                server.getListener().createContext(server.getUri(), new UriContext(configuration));
            }

            if(server.getMetadataUri() != null && server.getMetadataUri().length() > 0) {
                server.getListener().createContext(server.getMetadataUri(), new MetadataContext(configuration));
            }

            if(server.getConfigurationUri() != null && server.getConfigurationUri().length() > 0) {
                server.getListener().createContext(server.getConfigurationUri(), new ConfigurationContext(configuration));
            }
        }
    }
}
