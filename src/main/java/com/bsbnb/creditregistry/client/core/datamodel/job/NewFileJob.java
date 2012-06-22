package com.bsbnb.creditregistry.client.core.datamodel.job;

import com.bsbnb.creditregistry.client.core.DocumentAndTabManager;
import com.bsbnb.creditregistry.client.core.datamodel.Document;
import com.bsbnb.creditregistry.client.util.Encoding;
import static java.io.File.createTempFile;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.widgets.Shell;

public class NewFileJob implements Runnable {
        private final DocumentAndTabManager documentAndTabManager;
        private final Shell shell;

        public NewFileJob(final DocumentAndTabManager documentAndTabManager, final Shell shell) {
                this.documentAndTabManager = documentAndTabManager;
                this.shell = shell;
        }

        public void run() {
                try {
                        final File tempFile = createTempFile("swt_simple_editor", "tmp");
                        tempFile.deleteOnExit();
                        shell.getDisplay().asyncExec(new Runnable() {
                                public void run() {
                                        documentAndTabManager.addOrOpenExisting(new Document(tempFile, "", true, Encoding.UTF8, null));
                                }
                        });
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
}
