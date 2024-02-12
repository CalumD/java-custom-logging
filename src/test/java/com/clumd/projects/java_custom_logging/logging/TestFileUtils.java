package com.clumd.projects.java_custom_logging.logging;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class TestFileUtils {

    public static List<String> getFileAsStrings(final String file) throws IOException {
        ArrayList<String> ret = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                ret.add(reader.readLine() + System.lineSeparator());
            }
        }

        return ret;
    }

    public static void makeContainingDirs(String path) throws IOException {
        makeAllDirs(new File(path).getParentFile().getCanonicalPath());
    }

    public static void makeAllDirs(String path) throws IOException {
        Files.createDirectories(new File(path).toPath());
    }

    public static void deleteDirectoryIfExists(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        file = new File(path).getCanonicalFile();
        deleteIfExists(file.getCanonicalPath());
    }

    private static void deleteIfExists(String path) throws IOException {
        File file = new File(path);

        if (!file.exists()) {
            return;
        }

        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (!Files.isSymbolicLink(f.toPath())) {
                    deleteIfExists(f.getCanonicalPath());
                }
            }
        }
        try {
            Files.delete(file.toPath());
        } catch (Exception e) {
            throw new IOException("Failed to delete: {" + path + "}", e);
        }
    }
}
