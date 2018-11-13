package main.java;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;

public class Resources {
    private static FileSystem FILE_SYSTEM;
    static {
        try {
            final PathMatcher EXTENSION_MATCHER = FileSystems.getDefault().getPathMatcher("glob:*.jar");
            URI jarUri = Resources.class.getProtectionDomain().getCodeSource().getLocation().toURI();
            Path jarPath = Paths.get(jarUri);
            if (EXTENSION_MATCHER.matches(jarPath.getFileName()))
                FILE_SYSTEM = FileSystems.newFileSystem(jarPath, ClassLoader.getSystemClassLoader());
            else
                FILE_SYSTEM = null;
        }
        catch (IOException | URISyntaxException exc) {
            exc.printStackTrace();
            FILE_SYSTEM = null;
            // will never occur
        }
    }

    private static Path getPathFromUri(URI uri) {
        if (FILE_SYSTEM == null)
            return Paths.get(uri);

        String jarPath = uri.toString().replaceAll(".*\\.jar!", "");
        return FILE_SYSTEM.getPath(jarPath);
    }

    public static Path getPath(String name) {
        URL url = Resources.class.getResource(name);
        try {
            return getPathFromUri(url.toURI());
        }
        catch (URISyntaxException exc) {
            exc.printStackTrace();
            return null;
        }
    }

    public static Path getRelativePath(String value) {
        return Paths.get(System.getProperty("user.dir") + File.separator + value);
    }

    public static InputStream getResourceAsStream(String name) {
        return Resources.class.getResourceAsStream(name);
    }
}
