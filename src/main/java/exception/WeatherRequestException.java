package main.java.exception;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class WeatherRequestException extends IOException {
    public WeatherRequestException(URL requestUrl) {
        super("Could not create weather request for: \"" +
              Objects.requireNonNull(requestUrl, "requestUrl cannot be null").toString() + "\"");
    }
}
