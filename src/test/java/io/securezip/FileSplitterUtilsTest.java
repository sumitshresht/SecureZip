package io.securezip;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

class FileSplitterUtilsTest {

    @Test
    void testSplitAndMerge() throws Exception {
        String originalFile = createSampleFile("split_test.txt", "This is a test file with some repeated text. ".repeat(500));
        
        // ✅ Split into 1MB parts
        List<String> parts = FileSplitterUtils.splitFile(originalFile, 1);
        assertTrue(parts.size() > 0);

        // ✅ Merge back
        String mergedFile = originalFile + "_merged.txt";
        FileSplitterUtils.mergeFiles(parts, mergedFile);

        // ✅ Check hash equality
        String originalHash = HashUtils.getSHA256(originalFile);
        String mergedHash = HashUtils.getSHA256(mergedFile);
        assertEquals(originalHash, mergedHash);
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
