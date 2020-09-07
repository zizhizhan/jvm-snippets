package com.zizhizhan.legacies.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class WorkingDirectory {

    public static File getWorkingDirectory(String name) {
        File pwd = new File(System.getProperty("user.dir"));
        List<File> targetFiles = new ArrayList<>();
        scan(pwd, f -> f.isDirectory() && f.getName().equals(name), targetFiles);
        return targetFiles.size() > 0 ? targetFiles.get(0) : pwd;
    }

    private static void scan(final File file, Predicate<File> predicate, List<File> targetFiles) {
        if (file == null || predicate == null) {
            return;
        }
        if (predicate.test(file)) {
            targetFiles.add(file);
        } else {
            if (file.isDirectory()){
                for (final File child : Objects.requireNonNull(file.listFiles())) {
                    scan(child, predicate, targetFiles);
                }
            }
        }
    }

}
