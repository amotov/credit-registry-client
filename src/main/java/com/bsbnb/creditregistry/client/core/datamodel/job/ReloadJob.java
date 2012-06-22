package com.bsbnb.creditregistry.client.core.datamodel.job;

import com.bsbnb.creditregistry.client.core.DocumentAndTabManager;
import com.bsbnb.creditregistry.client.core.datamodel.Document;
import com.bsbnb.creditregistry.client.util.Encoding;
import com.bsbnb.creditregistry.client.util.EncodingUtil;
import com.bsbnb.creditregistry.client.util.MimeTypesUtil;
import static org.apache.commons.io.FileUtils.readFileToString;

import java.io.IOException;

import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Shell;

public class ReloadJob implements Runnable {

    private final DocumentAndTabManager documentAndTabManager;
    private final CTabItem tabToReload;
    private final Shell shell;

    public ReloadJob(final DocumentAndTabManager documentAndTabManager, final CTabItem tabToReload, final Shell shell) {
        this.documentAndTabManager = documentAndTabManager;
        this.tabToReload = tabToReload;
        this.shell = shell;
    }

    public void run() {
        final Document document = documentAndTabManager.getDocument(tabToReload);
        if (document != null) {
            // TODO: move here logic for document reload.
            try {
                final String mimeType = MimeTypesUtil.getContentType(document.getPath().getAbsolutePath());
                final String encoding = EncodingUtil.getFileEncoding(document.getPath().getAbsolutePath());
                final String content = readFileToString(document.getPath());
                final Document newDocument = new Document(document.getPath(), content, document.isTemporary(), encoding == null ? Encoding.UTF8 : encoding, mimeType);
                shell.getDisplay().asyncExec(new Runnable() {

                    public void run() {
                        documentAndTabManager.reload(document, newDocument);
                    }
                });
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }
}
