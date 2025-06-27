package io.securezip;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileSplitterUtils {

    // ✅ Split a file into parts of specified size (in MB)
    public static List<String> splitFile(String filePath, int partSizeMB) throws IOException {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            throw new FileNotFoundException("File not found: " + filePath);
        }

        List<String> partFileNames = new ArrayList<>();

        int partSize = partSizeMB * 1024 * 1024; // Convert MB to bytes
        byte[] buffer = new byte[4096];

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            int partCounter = 1;
            int bytesAmount;

            while ((bytesAmount = bis.read(buffer)) > 0) {
                File newFile = new File(file.getParent(), file.getName() + ".part" + partCounter);
                try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(newFile))) {
                    int written = 0;
                    while (written < partSize && bytesAmount > 0) {
                        bos.write(buffer, 0, bytesAmount);
                        written += bytesAmount;
                        if (written < partSize) {
                            bytesAmount = bis.read(buffer);
                        } else {
                            break;
                        }
                    }
                }
                partFileNames.add(newFile.getAbsolutePath());
                partCounter++;
            }
        }

        return partFileNames;
    }

    // ✅ Merge parts back into a single file
    public static void mergeFiles(List<String> partFiles, String outputFilePath) throws IOException {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFilePath))) {
            for (String partFile : partFiles) {
                File file = new File(partFile);
                try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
                    byte[] buffer = new byte[4096];
                    int bytesAmount;
                    while ((bytesAmount = bis.read(buffer)) != -1) {
                        bos.write(buffer, 0, bytesAmount);
                    }
                }
            }
        }
    }
}
