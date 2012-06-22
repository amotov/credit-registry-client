package com.bsbnb.creditregistry.client.core.datamodel.job;

import com.bsbnb.creditregistry.client.core.DocumentAndTabManager;
import com.bsbnb.creditregistry.client.core.datamodel.Document;
import com.bsbnb.creditregistry.client.util.Encoding;
import com.bsbnb.creditregistry.client.util.EncodingUtil;
import com.bsbnb.creditregistry.client.util.MimeTypesUtil;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.eclipse.swt.widgets.Shell;

public class OpenFileJob implements Runnable {

    private final String pathToFile;
    private final DocumentAndTabManager documentAndTabManager;
    private final Shell shell;

    public OpenFileJob(final String pathToFile, final DocumentAndTabManager documentAndTabManager, final Shell shell) {
        this.pathToFile = pathToFile;
        this.documentAndTabManager = documentAndTabManager;
        this.shell = shell;
    }

    public void run() {
        final File fileToOpen = new File(pathToFile);
        try {
            final String encoding = EncodingUtil.getFileEncoding(pathToFile);
            final String mimeType = MimeTypesUtil.getContentType(pathToFile);
            final String content = FileUtils.readFileToString(fileToOpen, encoding);
            final Document document = new Document(fileToOpen, content, false, encoding == null ? Encoding.UTF8 : encoding, mimeType);
            
            shell.getDisplay().asyncExec(new Runnable() {

                public void run() {
                    documentAndTabManager.addOrOpenExisting(document);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
