package cz.maku.mommons.utils;

import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public final class Files {

    public static void download(URL url, File file) throws IOException {
        FileUtils.copyURLToFile(url, file);
    }

    public static boolean delete(File directory) {
        if (!directory.isDirectory()) return false;
        for (File file : Objects.requireNonNull(directory.listFiles() == null ? new File[]{} : directory.listFiles())) {
            delete(file);
        }
        return directory.delete();
    }

}
