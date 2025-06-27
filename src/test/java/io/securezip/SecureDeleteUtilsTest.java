package io.securezip;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;

class SecureDeleteUtilsTest {

    @Test
    void testSecureDelete() throws Exception {
        String filePath = createSampleFile("delete_test.txt", "Delete me securely!");

        File file = new File(filePath);
        assertTrue(file.exists());

        SecureDeleteUtils.secureDelete(filePath);

        assertFalse(file.exists());
    }

    private String createSampleFile(String fileName, String content) throws Exception {
        String filePath = System.getProperty("java.io.tmpdir") + "/" + fileName;
        File file = new File(filePath);
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(content);
        }
        return filePath;
    }
}
