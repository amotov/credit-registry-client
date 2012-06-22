package com.bsbnb.creditregistry.client.util;

import java.io.File;
import java.io.InputStream;

import java.util.Set;

public interface MimeTypes {

	public String getContentType(File file);

	public String getContentType(File file, String fileName);

	public String getContentType(InputStream inputStream, String fileName);

	public String getContentType(String fileName);

	public Set<String> getExtensions(String contentType);

}