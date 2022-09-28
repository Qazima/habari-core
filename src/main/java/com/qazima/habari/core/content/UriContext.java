package com.qazima.habari.core.content;

import com.qazima.habari.core.configuration.Configuration;
import com.qazima.habari.plugin.core.Content;
import com.qazima.habari.plugin.core.Plugin;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.http.HttpStatus;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.regex.Pattern;

public class UriContext implements HttpHandler {
    private final Configuration configuration;

    public UriContext(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestUri = exchange.getRequestURI().getPath();
        List<Plugin> plugins = configuration.getConnections().stream().filter(item -> Pattern.compile(item.getUri()).matcher(requestUri).matches()).toList();
        Content content = new Content();
        int contentResult = HttpStatus.SC_NOT_FOUND;
        for (Plugin plugin : plugins) {
            contentResult = plugin.process(exchange, content);
        }
        byte[] response = content.getBody();
        exchange.sendResponseHeaders(contentResult, response.length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response);
        outputStream.close();
    }
}
