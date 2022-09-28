package com.qazima.habari.core.content;

import com.qazima.habari.core.configuration.Configuration;
import com.qazima.habari.core.configuration.Server;

public class ContentManager {
    public ContentManager(Configuration configuration) {
        for (Server server :
                configuration.getServers()) {
            server.getListener().createContext(server.getUri(), new UriContext(configuration));
            server.getListener().createContext(server.getMetadataUri(), new MetadataContext(configuration));
            server.getListener().createContext(server.getConfigurationUri(), new ConfigurationContext(configuration));
        }
    }
}
