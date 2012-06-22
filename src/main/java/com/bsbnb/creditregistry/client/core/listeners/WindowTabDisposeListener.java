package com.bsbnb.creditregistry.client.core.listeners;

import com.bsbnb.creditregistry.client.core.DocumentAndTabManager;
import com.bsbnb.creditregistry.client.core.WindowAndTabManager;
import org.apache.log4j.Logger;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

public class WindowTabDisposeListener implements DisposeListener {
        private static final Logger LOGGER = Logger.getLogger(WindowTabDisposeListener.class);
        private WindowAndTabManager windowAndTabManager;

        public WindowTabDisposeListener(final WindowAndTabManager windowAndTabManager) {
                this.windowAndTabManager = windowAndTabManager;
        }

        public void widgetDisposed(final DisposeEvent disposeEvent) {
                if (LOGGER.isDebugEnabled())
                        LOGGER.debug("Disposing tab" + disposeEvent);
                final CTabItem tab = (CTabItem) disposeEvent.getSource();
                windowAndTabManager.removeWindowByTab(tab);
                if (LOGGER.isDebugEnabled())
                        LOGGER.debug("All related tabs should be removed." + disposeEvent);
        }
}
