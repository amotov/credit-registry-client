package com.bsbnb.creditregistry.client.util;

import java.io.File;
import java.io.InputStream;

import java.util.Set;

public class MimeTypesUtil {

    public static String getContentType(File file) {
        return getMimeTypes().getContentType(file);
    }

    public static String getContentType(File file, String fileName) {
        return getMimeTypes().getContentType(file, fileName);
    }

    public static String getContentType(
            InputStream inputStream, String fileName) {

        return getMimeTypes().getContentType(inputStream, fileName);
    }

    public static String getContentType(String fileName) {
        return getMimeTypes().getContentType(fileName);
    }

    public static Set<String> getExtensions(String contentType) {
        return getMimeTypes().getExtensions(contentType);
    }

    public static MimeTypes getMimeTypes() {
        return _mimeTypes;
    }

    public static void setMimeTypes(MimeTypes mimeTypes) {
        _mimeTypes = mimeTypes;
    }
    private static MimeTypes _mimeTypes;
}