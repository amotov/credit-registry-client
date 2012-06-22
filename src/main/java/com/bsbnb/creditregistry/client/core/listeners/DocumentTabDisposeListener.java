package com.bsbnb.creditregistry.client.core.listeners;

import com.bsbnb.creditregistry.client.core.DocumentAndTabManager;
import org.apache.log4j.Logger;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

public class DocumentTabDisposeListener implements DisposeListener {
        private static final Logger LOGGER = Logger.getLogger(DocumentTabDisposeListener.class);
        private DocumentAndTabManager documentAndTabManager;

        public DocumentTabDisposeListener(final DocumentAndTabManager documentAndTabManager) {
                this.documentAndTabManager = documentAndTabManager;
        }

        public void widgetDisposed(final DisposeEvent disposeEvent) {
                if (LOGGER.isDebugEnabled())
                        LOGGER.debug("Disposing tab" + disposeEvent);
                final CTabItem tab = (CTabItem) disposeEvent.getSource();
                documentAndTabManager.removeDocumentByTab(tab);
                if (LOGGER.isDebugEnabled())
                        LOGGER.debug("All related tabs should be removed." + disposeEvent);
        }
}
