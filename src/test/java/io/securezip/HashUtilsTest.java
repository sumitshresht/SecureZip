package io.securezip;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;

class HashUtilsTest {

    @Test
    void testSHA256() throws Exception {
        String filePath = createSampleFile("test_sha.txt", "Hello World");
        String hash = HashUtils.getSHA256(filePath);
        assertNotNull(hash);
        assertEquals(64, hash.length()); // SHA-256 hash should be 64 chars
    }

    @Test
    void testMD5() throws Exception {
        String filePath = createSampleFile("test_md5.txt", "Hash me!");
        String hash = HashUtils.getMD5(filePath);
        assertNotNull(hash);
        assertEquals(32, hash.length()); // MD5 hash should be 32 chars
    }

    // ✅ Helper to create a small file for testing
    private String createSampleFile(String fileName, String content) throws Exception {
        String filePath = System.getProperty("java.io.tmpdir") + "/" + fileName;
        File file = new File(filePath);
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(content);
        }
        return filePath;
    }
}
