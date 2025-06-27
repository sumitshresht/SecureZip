package io.securezip;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipInspector {

    public static void listContents(String zipFilePath) throws IOException {
        File zipFile = new File(zipFilePath);
        if (!zipFile.exists()) {
            throw new IOException("ZIP file does not exist: " + zipFilePath);
        }

        try (ZipFile zf = new ZipFile(zipFile)) {
            System.out.println("Contents of ZIP file: " + zipFilePath);
            System.out.println("-------------------------------------------------");

            Enumeration<? extends ZipEntry> entries = zf.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();

                String name = entry.getName();
                long size = entry.getSize();
                boolean isDir = entry.isDirectory();

                System.out.printf("%s\t%s\t%s\n",
                        isDir ? "[DIR]" : "[FILE]",
                        name,
                        isDir ? "" : readableFileSize(size)
                );
            }

            System.out.println("-------------------------------------------------");
        }
    }

    private static String readableFileSize(long size) {
        if (size <= 0) return "0 B";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return String.format("%.1f %s", size / Math.pow(1024, digitGroups), units[digitGroups]);
    }
}
