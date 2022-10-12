package com.qazima.habari.core.content;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qazima.habari.core.configuration.Configuration;
import com.qazima.habari.plugin.core.Content;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.http.HttpStatus;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ConfigurationContext implements HttpHandler {
    private final Configuration configuration;

    public ConfigurationContext(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestUri = httpExchange.getRequestURI().getPath();
        //List<Plugin> plugins = configuration.getConnections().stream().filter(item -> Pattern.compile(item.getConfiguration().getUri()).matcher(requestUri).matches()).toList();
        Content content = new Content();
        int contentResult = HttpStatus.SC_NOT_FOUND;
        ObjectMapper objectMapper = new ObjectMapper();
        String pluginsJson = objectMapper.writeValueAsString(configuration.getConnections());
        byte[] response = pluginsJson.getBytes(StandardCharsets.UTF_8);
        httpExchange.sendResponseHeaders(contentResult, response.length);
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(response);
        outputStream.close();
    }
}
