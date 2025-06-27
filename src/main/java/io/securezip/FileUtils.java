package io.securezip;

import java.io.File;

public class FileUtils {
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) file.delete();
    }
}
