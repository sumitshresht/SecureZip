package io.securezip;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import java.io.File;
import java.util.List;

public class SecureZipUtils {

    // ✅ Create Password-Protected ZIP from Files or Folders
    public static void createPasswordProtectedZip(String zipFilePath, List<String> filePaths, String password) throws ZipException {
        ZipFile zipFile = new ZipFile(zipFilePath, password.toCharArray());

        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(CompressionMethod.DEFLATE);
        parameters.setCompressionLevel(CompressionLevel.NORMAL);
        parameters.setEncryptFiles(true);
        parameters.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);

        for (String path : filePaths) {
            File file = new File(path);
            if (file.isDirectory()) {
                zipFile.addFolder(file, parameters);
            } else {
                zipFile.addFile(file, parameters);
            }
        }
    }

    // ✅ Extract Password-Protected ZIP
    public static void extractPasswordProtectedZip(String zipFilePath, String destination, String password) throws ZipException {
        ZipFile zipFile = new ZipFile(zipFilePath);
        if (zipFile.isEncrypted()) {
            zipFile.setPassword(password.toCharArray());
        }
        zipFile.extractAll(destination);
    }

    // ✅ Check if ZIP is Password Protected
    public static boolean isZipEncrypted(String zipFilePath) throws ZipException {
        ZipFile zipFile = new ZipFile(zipFilePath);
        return zipFile.isEncrypted();
    }
}
