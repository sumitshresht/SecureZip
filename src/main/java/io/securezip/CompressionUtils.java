package io.securezip;

import java.io.*;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.*;

public class CompressionUtils {

    // -------------------- ✅ Compress a Folder --------------------
    public static void zipFolder(String sourceDir, String outputFile) throws IOException {
        zipFolderWithProgress(sourceDir, outputFile, null);
    }

    public static void zipFolderWithProgress(String sourceDir, String outputFile, ProgressListener listener) throws IOException {
        File folder = new File(sourceDir);
        if (!folder.exists()) {
            throw new FileNotFoundException("Source folder not found: " + sourceDir);
        }

        long totalSize = calculateTotalSize(folder);
        long[] processed = {0};

        try (FileOutputStream fos = new FileOutputStream(outputFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            zipFile(folder, folder.getName(), zos, listener, processed, totalSize);
        }
    }

    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zos, ProgressListener listener, long[] processed, long total) throws IOException {
        if (fileToZip.isHidden()) return;

        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zos.putNextEntry(new ZipEntry(fileName));
                zos.closeEntry();
            } else {
                zos.putNextEntry(new ZipEntry(fileName + "/"));
                zos.closeEntry();
            }

            File[] children = fileToZip.listFiles();
            if (children != null) {
                for (File childFile : children) {
                    zipFile(childFile, fileName + "/" + childFile.getName(), zos, listener, processed, total);
                }
            }
            return;
        }

        try (FileInputStream fis = new FileInputStream(fileToZip)) {
            ZipEntry zipEntry = new ZipEntry(fileName);
            zos.putNextEntry(zipEntry);

            byte[] buffer = new byte[4096];
            int length;
            while ((length = fis.read(buffer)) >= 0) {
                zos.write(buffer, 0, length);
                processed[0] += length;
                updateProgress(listener, processed[0], total);
            }
        }
    }

    // -------------------- ✅ Compress Multiple Files --------------------
    public static void zipFiles(List<String> filePaths, String outputFile) throws IOException {
        zipFilesWithProgress(filePaths, outputFile, null);
    }

    public static void zipFilesWithProgress(List<String> filePaths, String outputFile, ProgressListener listener) throws IOException {
        long totalSize = 0;
        for (String path : filePaths) {
            File file = new File(path);
            if (!file.exists() || file.isDirectory()) {
                throw new FileNotFoundException("Invalid file: " + path);
            }
            totalSize += file.length();
        }

        long[] processed = {0};

        try (FileOutputStream fos = new FileOutputStream(outputFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            for (String path : filePaths) {
                File file = new File(path);
                try (FileInputStream fis = new FileInputStream(file)) {
                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zos.putNextEntry(zipEntry);

                    byte[] buffer = new byte[4096];
                    int length;
                    while ((length = fis.read(buffer)) >= 0) {
                        zos.write(buffer, 0, length);
                        processed[0] += length;
                        updateProgress(listener, processed[0], totalSize);
                    }
                }
            }
        }
    }

    // -------------------- ✅ Extract ZIP --------------------
    public static void unzipFolder(String zipFilePath, String destDir) throws IOException {
        unzipFolderWithProgress(zipFilePath, destDir, null);
    }

    public static void unzipFolderWithProgress(String zipFilePath, String destDir, ProgressListener listener) throws IOException {
        File destDirFile = new File(destDir);
        if (!destDirFile.exists()) destDirFile.mkdirs();

        long totalSize = calculateZipTotalSize(zipFilePath);
        long[] processed = {0};

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                File newFile = newFile(destDirFile, zipEntry);
                if (zipEntry.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    File parent = newFile.getParentFile();
                    if (!parent.exists()) parent.mkdirs();

                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        byte[] buffer = new byte[4096];
                        int length;
                        while ((length = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                            processed[0] += length;
                            updateProgress(listener, processed[0], totalSize);
                        }
                    }
                }
                zis.closeEntry();
                zipEntry = zis.getNextEntry();
            }
        }
    }

    // -------------------- ✅ Utilities --------------------
    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    private static void updateProgress(ProgressListener listener, long processed, long total) {
        if (listener != null && total > 0) {
            int percent = (int) ((processed * 100) / total);
            listener.onProgress(percent);
        }
    }

    private static long calculateTotalSize(File file) {
        if (file.isFile()) {
            return file.length();
        }

        long total = 0;
        File[] children = file.listFiles();
        if (children != null) {
            for (File child : children) {
                total += calculateTotalSize(child);
            }
        }
        return total;
    }

    private static long calculateZipTotalSize(String zipFilePath) throws IOException {
        long total = 0;
        try (ZipFile zipFile = new ZipFile(zipFilePath)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                total += entry.getSize() > 0 ? entry.getSize() : 0;
            }
        }
        return total;
    }
}
