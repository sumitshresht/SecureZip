package io.securezip;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.security.spec.KeySpec;

public class EncryptionUtils {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String KEY_ALGORITHM = "AES";
    private static final int KEY_SIZE = 256;
    private static final int SALT_LENGTH = 16;
    private static final int IV_LENGTH = 16;
    private static final int ITERATIONS = 65536;

    public static void encryptFile(String inputFile, String outputFile, String password) throws Exception {
        encryptFileWithProgress(inputFile, outputFile, password, null);
    }

    public static void encryptFileWithProgress(String inputFile, String outputFile, String password, ProgressListener listener) throws Exception {
        byte[] salt = generateRandomBytes(SALT_LENGTH);
        byte[] iv = generateRandomBytes(IV_LENGTH);

        SecretKey key = getKeyFromPassword(password, salt);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));

        long totalBytes = new File(inputFile).length();
        long processedBytes = 0;

        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(salt);
            fos.write(iv);

            try (CipherOutputStream cos = new CipherOutputStream(fos, cipher);
                 FileInputStream fis = new FileInputStream(inputFile)) {

                byte[] buffer = new byte[4096];
                int read;
                while ((read = fis.read(buffer)) != -1) {
                    cos.write(buffer, 0, read);
                    processedBytes += read;
                    updateProgress(listener, processedBytes, totalBytes);
                }
            }
        }
    }

    public static void decryptFile(String inputFile, String outputFile, String password) throws Exception {
        decryptFileWithProgress(inputFile, outputFile, password, null);
    }

    public static void decryptFileWithProgress(String inputFile, String outputFile, String password, ProgressListener listener) throws Exception {
        try (FileInputStream fis = new FileInputStream(inputFile)) {
            byte[] salt = new byte[SALT_LENGTH];
            if (fis.read(salt) != SALT_LENGTH) throw new IOException("Failed to read salt");

            byte[] iv = new byte[IV_LENGTH];
            if (fis.read(iv) != IV_LENGTH) throw new IOException("Failed to read IV");

            SecretKey key = getKeyFromPassword(password, salt);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));

            long totalBytes = new File(inputFile).length() - SALT_LENGTH - IV_LENGTH;
            long processedBytes = 0;

            try (CipherInputStream cis = new CipherInputStream(fis, cipher);
                 FileOutputStream fos = new FileOutputStream(outputFile)) {

                byte[] buffer = new byte[4096];
                int read;
                while ((read = cis.read(buffer)) != -1) {
                    fos.write(buffer, 0, read);
                    processedBytes += read;
                    updateProgress(listener, processedBytes, totalBytes);
                }
            }
        }
    }

    public static boolean isPasswordValid(String encryptedFile, String password) {
        try (FileInputStream fis = new FileInputStream(encryptedFile)) {
            byte[] salt = new byte[SALT_LENGTH];
            if (fis.read(salt) != SALT_LENGTH) throw new IOException("Failed to read salt");

            byte[] iv = new byte[IV_LENGTH];
            if (fis.read(iv) != IV_LENGTH) throw new IOException("Failed to read IV");

            SecretKey key = getKeyFromPassword(password, salt);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));

            try (CipherInputStream cis = new CipherInputStream(fis, cipher)) {
                byte[] buffer = new byte[16];
                if (cis.read(buffer) >= 0) {
                    return true;
                }
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private static void updateProgress(ProgressListener listener, long processed, long total) {
        if (listener != null && total > 0) {
            int percent = (int) ((processed * 100) / total);
            listener.onProgress(percent);
        }
    }

    private static SecretKey getKeyFromPassword(String password, byte[] salt) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_SIZE);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), KEY_ALGORITHM);
    }

    private static byte[] generateRandomBytes(int length) {
        byte[] bytes = new byte[length];
        new SecureRandom().nextBytes(bytes);
        return bytes;
    }
}
