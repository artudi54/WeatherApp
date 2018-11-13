package main.java.config;

import javafx.scene.image.Image;

import main.java.exception.ConfigException;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WeatherIcons {
    private static final PathMatcher EXTENSION_MATCHER = FileSystems.getDefault().getPathMatcher("glob:*.png");

    private Map<String, Image> iconMap;

    public WeatherIcons(Path iconsDirectory) throws ConfigException {
        Objects.requireNonNull(iconsDirectory, "iconsDirectory cannot be null");
        iconMap = new HashMap<>();

        try(DirectoryStream<Path> directoryStream = Files.newDirectoryStream(iconsDirectory)) {
            for (Path entry : directoryStream)
                if (EXTENSION_MATCHER.matches(entry.getFileName()))
                    iconMap.put(entry.getFileName().toString().replaceAll("\\.png", ""),
                                new Image(entry.toUri().toString()));
        }
        catch (IOException ignore) {
            throw new ConfigException(iconsDirectory, ConfigException.Type.READ_DIRECTORY);
        }
    }

    public Image getIcon(String iconName) {
        return iconMap.get(iconName);
    }
}
