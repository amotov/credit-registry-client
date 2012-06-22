package com.bsbnb.creditregistry.client.core.datamodel.job;

import com.bsbnb.creditregistry.client.core.DocumentAndTabManager;
import com.bsbnb.creditregistry.client.core.datamodel.Document;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Shell;

public class SaveFileAsJob implements Runnable {

    private final DocumentAndTabManager documentAndTabManager;
    private final Document document;
    private final String newPath;
    private final CTabItem cTabItem;
    private final Shell shell;

    public SaveFileAsJob(final DocumentAndTabManager documentAndTabManager, final CTabItem cTabItem, final Document document,
            final String newPath, final Shell shell) {
        this.documentAndTabManager = documentAndTabManager;
        this.cTabItem = cTabItem;
        this.document = document;
        this.newPath = newPath;
        this.shell = shell;
    }

    public void run() {
        shell.getDisplay().asyncExec(new Runnable() {

            public void run() {
                documentAndTabManager.saveAs(cTabItem, document, newPath);
            }
        });
    }
    
}
