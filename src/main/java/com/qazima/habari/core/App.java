package com.qazima.habari.core;

import com.qazima.habari.core.configuration.ConfigurationManager;

public class App {
    public static void main(String[] args) {
        ConfigurationManager.getInstance().loadConfiguration(args[0]);
        ConfigurationManager.getInstance().configure();
        ConfigurationManager.getInstance().start();
    }
}
