package io.securezip;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class HashUtils {

    // ✅ Calculate SHA-256 hash
    public static String getSHA256(String filePath) throws Exception {
        return calculateHash(filePath, "SHA-256");
    }

    // ✅ Calculate SHA-1 hash
    public static String getSHA1(String filePath) throws Exception {
        return calculateHash(filePath, "SHA-1");
    }

    // ✅ Calculate MD5 hash
    public static String getMD5(String filePath) throws Exception {
        return calculateHash(filePath, "MD5");
    }

    // ✅ Common hash calculation method
    private static String calculateHash(String filePath, String algorithm) throws Exception {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        File file = new File(filePath);

        if (!file.exists() || !file.isFile()) {
            throw new Exception("File not found: " + filePath);
        }

        try (InputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[8192];
            int n;
            while ((n = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, n);
            }
        }

        byte[] hashBytes = digest.digest();
        return bytesToHex(hashBytes);
    }

    // ✅ Convert bytes to HEX string
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
