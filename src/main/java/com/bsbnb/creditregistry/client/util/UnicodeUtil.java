package com.bsbnb.creditregistry.client.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

/**
 *
 * @author Alexandr.Motov
 */
public class UnicodeUtil {

    public static final String utf8Decode(java.io.File file, String charsetName) throws IOException {
        FileInputStream fileInputStream = null;
        
        
        /*try {
            CharsetDecoder utf8Decoder = Charset.forName(charsetName).newDecoder();
            utf8Decoder.onMalformedInput(CodingErrorAction.IGNORE);
            utf8Decoder.onUnmappableCharacter(CodingErrorAction.IGNORE);
            fileInputStream = new FileInputStream(file);
            byte bytes[] = new byte[(int) file.length()];
            fileInputStream.read(bytes);
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            CharBuffer charBuffer = CharBuffer.allocate(bytes.length);
            CoderResult coderResult = utf8Decoder.decode(byteBuffer, charBuffer, true);

            System.out.println("IS_ERROR: " + coderResult.isError());
            System.out.println("IS_OVERFLOW: " + coderResult.isOverflow());
            System.out.println("IS_MALFORMED: " + coderResult.isMalformed());
            System.out.println("IS_UNMAPPABLE: " + coderResult.isUnmappable());
            System.out.println("IS_UNDERFLOW: " + coderResult.isUnderflow());
            
            return charBuffer.toString();
        } finally {
            fileInputStream.close();
        }*/
        return "";
    }
}
