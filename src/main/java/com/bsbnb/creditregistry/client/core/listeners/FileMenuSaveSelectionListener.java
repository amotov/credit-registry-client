package com.bsbnb.creditregistry.client.core.listeners;

import com.bsbnb.creditregistry.client.core.DocumentAndTabManager;
import com.bsbnb.creditregistry.client.core.datamodel.Document;
import com.bsbnb.creditregistry.client.core.datamodel.job.SaveFileAsJob;
import com.bsbnb.creditregistry.client.core.datamodel.job.SaveFileJob;
import java.util.concurrent.ExecutorService;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class FileMenuSaveSelectionListener extends SelectionAdapter {
        private static final Logger LOGGER = Logger.getLogger(FileMenuSaveSelectionListener.class);
        private final DocumentAndTabManager documentAndTabManager;
        private final Shell shell;
        private final ExecutorService executorService;

        public FileMenuSaveSelectionListener(final Shell shell, final DocumentAndTabManager documentAndTabManager,
                        final ExecutorService executorService) {
                this.documentAndTabManager = documentAndTabManager;
                this.shell = shell;
                this.executorService = executorService;
        }

        @Override
        public void widgetSelected(final SelectionEvent selectionEvent) {
                final CTabItem currentTab = documentAndTabManager.getCurrentTabItem();
                if (currentTab != null) {
                        final Document document = documentAndTabManager.getDocument(currentTab);
                        if (document != null) {
                                final Document documentToSave = new Document(document.getPath(),
                                                ((Text) currentTab.getControl()).getText(), document.isTemporary(), document.getEncoding(), document.getMimeType());
                                if (document.isTemporary()) {
                                        if (LOGGER.isDebugEnabled())
                                                LOGGER.debug("Submitting save as job...");
                                        selectWhereToSaveAndSumbitFileSaveAsJob(documentToSave);
                                        if (LOGGER.isDebugEnabled())
                                                LOGGER.debug("Submitted save as job.");
                                } else {
                                        if (LOGGER.isDebugEnabled())
                                                LOGGER.debug("Submitting save job...");
                                        executorService.execute(new SaveFileJob(documentAndTabManager, currentTab, documentToSave, shell));
                                        if (LOGGER.isDebugEnabled())
                                                LOGGER.debug("Submitted save job.");
                                }
                        }
                } else {
                        if (LOGGER.isDebugEnabled())
                                LOGGER.debug("current tab is null");
                }
        }

        private void selectWhereToSaveAndSumbitFileSaveAsJob(final Document document) {
                final FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
                fileDialog.setText("Please select which file to save.");
                final String newPath = fileDialog.open();
                final CTabItem currentTab = documentAndTabManager.getCurrentTabItem();
                if (newPath != null) {
                        final Document documentToSave = new Document(document.getPath(),
                                        ((Text) currentTab.getControl()).getText(), document.isTemporary(), document.getEncoding(), document.getMimeType());
                        if (LOGGER.isDebugEnabled())
                                LOGGER.debug("Submitting save as job...");
                        shell.getDisplay().asyncExec(
                                        new SaveFileAsJob(documentAndTabManager, currentTab, documentToSave, newPath, shell));
                        if (LOGGER.isDebugEnabled())
                                LOGGER.debug("Submitted save as job.");
                } else {
                        if (LOGGER.isDebugEnabled())
                                LOGGER.debug("No file selected");
                }
        }
}
