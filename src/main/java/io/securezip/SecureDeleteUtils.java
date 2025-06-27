package io.securezip;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.security.SecureRandom;

public class SecureDeleteUtils {

    // ✅ Securely delete file by overwriting and then deleting
    public static void secureDelete(String filePath) throws Exception {
        File file = new File(filePath);

        if (!file.exists() || !file.isFile()) {
            throw new FileNotFoundException("File not found: " + filePath);
        }

        long length = file.length();

        try (RandomAccessFile raf = new RandomAccessFile(file, "rws")) {
            SecureRandom random = new SecureRandom();
            byte[] data = new byte[4096];

            long fullWrites = length / data.length;
            int remainder = (int) (length % data.length);

            // 🔄 Overwrite full blocks
            for (long i = 0; i < fullWrites; i++) {
                random.nextBytes(data);
                raf.write(data);
            }

            // 🔄 Overwrite remaining bytes
            if (remainder > 0) {
                byte[] remaining = new byte[remainder];
                random.nextBytes(remaining);
                raf.write(remaining);
            }
        }

        // 🔥 Delete the file
        boolean deleted = file.delete();
        if (!deleted) {
            throw new Exception("Failed to delete file: " + filePath);
        }

        System.out.println("✅ File securely deleted: " + filePath);
    }
}
