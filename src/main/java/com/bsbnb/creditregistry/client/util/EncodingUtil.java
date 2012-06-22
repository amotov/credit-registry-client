package com.bsbnb.creditregistry.client.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.mozilla.universalchardet.UniversalDetector;

/**
 *
 * @author Alexandr.Motov
 */
public class EncodingUtil {
    
    public static String getFileEncoding(String FileNameFull) {
        UniversalDetector detector = new UniversalDetector(null);
        FileInputStream fis = null;
        
        try {
            fis = new FileInputStream(FileNameFull);
        } catch (FileNotFoundException e) {
        }
        
        byte[] buf = new byte[4096];
        int nread;
        
        try {
            while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
                detector.handleData(buf, 0, nread);
            }
        } catch (IOException e) {
        }
        
        detector.dataEnd();

        String encoding = detector.getDetectedCharset();
        return encoding;
    }
    
}
