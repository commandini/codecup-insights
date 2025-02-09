package service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

public class FileService {
    public static File getResourceFile(String resourceName) throws FileNotFoundException {
        URL resourceUrl = Thread.currentThread().getContextClassLoader().getResource(resourceName);
        if (resourceUrl != null) {
            return new File(resourceUrl.getFile());
        }
        throw new FileNotFoundException(resourceName);
    }

    public static void deleteFilesRecursively(File root) throws IOException {
        if (!root.isDirectory()) {
            return;
        }
        File[] files = root.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteFilesRecursively(file);
                } else {
                    Files.delete(file.toPath());
                }
            }
        }
    }
}
