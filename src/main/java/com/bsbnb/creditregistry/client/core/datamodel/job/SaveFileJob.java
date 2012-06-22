package com.bsbnb.creditregistry.client.core.datamodel.job;

import com.bsbnb.creditregistry.client.core.DocumentAndTabManager;
import com.bsbnb.creditregistry.client.core.datamodel.Document;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Shell;

public class SaveFileJob implements Runnable {

    private final DocumentAndTabManager documentAndTabManager;
    private final Document document;
    private final CTabItem cTabItem;
    private final Shell shell;

    public SaveFileJob(final DocumentAndTabManager documentAndTabManager, final CTabItem cTabItem, final Document document, final Shell shell) {
        this.documentAndTabManager = documentAndTabManager;
        this.cTabItem = cTabItem;
        this.document = document;
        this.shell = shell;
    }

    public void run() {
        shell.getDisplay().asyncExec(new Runnable() {

            public void run() {
                documentAndTabManager.save(cTabItem, document);
            }
        });
    }
}
