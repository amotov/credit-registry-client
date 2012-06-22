package com.bsbnb.creditregistry.client.core.listeners;

import com.bsbnb.creditregistry.client.core.DocumentAndTabManager;
import com.bsbnb.creditregistry.client.core.WindowAndTabManager;
import com.bsbnb.creditregistry.client.core.datamodel.CustomIdentifier;
import com.bsbnb.creditregistry.client.core.datamodel.Document;
import com.bsbnb.creditregistry.client.core.datamodel.job.ValidateJob;
import java.util.concurrent.ExecutorService;
import org.apache.log4j.Logger;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class XmlMenuValidateSelectionListener extends SelectionAdapter {

    private static final Logger LOGGER = Logger.getLogger(XmlMenuValidateSelectionListener.class);
    private final DocumentAndTabManager documentAndTabManager;
    private final WindowAndTabManager windowAndTabManager;
    private final Shell shell;
    private final ExecutorService executorService;

    public XmlMenuValidateSelectionListener(final Shell shell, final DocumentAndTabManager documentAndTabManager,
            final WindowAndTabManager windowAndTabManager, final ExecutorService executorService) {
        this.documentAndTabManager = documentAndTabManager;
        this.windowAndTabManager = windowAndTabManager;
        this.shell = shell;
        this.executorService = executorService;
    }

    @Override
    public void widgetSelected(final SelectionEvent selectionEvent) {
        final CTabItem currentTab = documentAndTabManager.getCurrentTabItem();
        if (currentTab != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Submitting validate job...");
            }
            final Document document = documentAndTabManager.getDocument(currentTab);
            Document parsedDocument = new Document(document.getPath(), ((Text)currentTab.getControl()).getText(), 
                    document.isTemporary(), document.getEncoding(), document.getMimeType());
            
            parsedDocument.addCustomIdentifier(new CustomIdentifier("Номер пакета", "batch/packages/package", "no", CustomIdentifier.TYPE_UNTIL_THE_END));
            
            executorService.execute(new ValidateJob(documentAndTabManager, windowAndTabManager, currentTab, parsedDocument, shell));
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Submitted validate job...");
            }
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Tab not found.");
            }
        }
    }
}
