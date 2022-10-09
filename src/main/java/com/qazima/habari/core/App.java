package com.qazima.habari.core;

import com.qazima.habari.core.configuration.ConfigurationManager;

public class App {
    public static void main(String[] args) {
        ConfigurationManager.getInstance().loadConfiguration(args[0]);
        if (ConfigurationManager.getInstance().getParameter() != null) {
            ConfigurationManager.getInstance().configure();
            ConfigurationManager.getInstance().start();
        }
    }
}
