package com.qazima.habari.core.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qazima.habari.core.content.ContentManager;
import com.qazima.habari.plugin.core.Plugin;
import lombok.Getter;
import org.apache.log4j.LogManager;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class ConfigurationManager {
    @Getter
    private final ServiceLoader<Plugin> serviceLoader = ServiceLoader.load(Plugin.class);
    @Getter
    private final List<ContentManager> contentManagers = new ArrayList<>();
    @Getter
    private Parameter parameter;

    private ConfigurationManager() {
    }

    public static ConfigurationManager getInstance() {
        return InnerCM.INSTANCE;
    }

    public List<Configuration> getConfigurations() {
        return parameter.getConfigurations();
    }

    public void loadConfiguration(String configurationFileName) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            parameter = mapper.readValue(new File(configurationFileName), Parameter.class);
        } catch (IOException e) {
            LogManager.getLogger(ConfigurationManager.class).fatal(e);
        }
    }

    public void configure() {
        try {
            parameter.configureServers();
            contentManagers.addAll(parameter.configureContentManagers());
        } catch (IOException | UnrecoverableKeyException | CertificateException | KeyStoreException |
                 NoSuchAlgorithmException | KeyManagementException e) {
            LogManager.getLogger(ConfigurationManager.class).fatal(e);
        }
    }

    public void start() {
        parameter.startServers();
    }

    private static class InnerCM {
        private static final ConfigurationManager INSTANCE = new ConfigurationManager();
    }
}
