package io.securezip;

public class SecureZip {

    public static void compressAndEncrypt(String sourcePath, String outputZip, String password) throws Exception {
        String tempZip = outputZip + ".tmp";
        CompressionUtils.zipFolder(sourcePath, tempZip);
        EncryptionUtils.encryptFile(tempZip, outputZip, password);
        FileUtils.deleteFile(tempZip);
    }

    public static void decryptAndExtract(String encryptedZip, String outputFolder, String password) throws Exception {
        String tempZip = encryptedZip + ".tmp";
        EncryptionUtils.decryptFile(encryptedZip, tempZip, password);
        CompressionUtils.unzipFolder(tempZip, outputFolder);
        FileUtils.deleteFile(tempZip);
    }
}
