package com.qazima.habari.core.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class AvailableConnection {
    @Getter
    @Setter
    @JsonProperty("className")
    private String className;
    @Getter
    @Setter
    @JsonProperty("connectionType")
    private String connectionType;
    @Getter
    @Setter
    @JsonProperty("url")
    private String url;
}
