package main.java.exception;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public class ConfigException extends IOException {
    public enum Type {
        READ,
        READ_DIRECTORY,
        WRITE,
        PARSE
    }

    public ConfigException(Path filePath, Type type) {
        super(makeExceptionPrefix(type) + "\"" +
              Objects.requireNonNull(filePath, "filePath cannot be null").toString() + "\"");
    }

    private static String makeExceptionPrefix(Type type) {
        Objects.requireNonNull(type, "type cannot by null");
        switch (type) {
            case READ:
                return "Could not load file: ";
            case READ_DIRECTORY:
                return "Could not read directory: ";
            case WRITE:
                return "Could not save file: ";
            case PARSE:
                return "Could not parse file: ";
            default:
                return "";
        }
    }
}
