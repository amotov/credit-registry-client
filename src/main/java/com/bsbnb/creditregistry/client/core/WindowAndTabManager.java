package com.bsbnb.creditregistry.client.core;

import com.bsbnb.creditregistry.client.core.datamodel.Document;
import com.bsbnb.creditregistry.client.core.listeners.WindowTabDisposeListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabItem;

public class WindowAndTabManager {

    private static final Logger LOGGER = Logger.getLogger(WindowAndTabManager.class);
    private final CTabFolder cTabFolder;
    private CTabItem currentTabItem;
    private Object currentTabItemLock;
    private final Map<Window, CTabItem> windowsToTabs;

    public Window getWindow(final CTabItem cTabItem) {
        synchronized (windowsToTabs) {
            for (final Map.Entry<Window, CTabItem> windowToTabEntry : windowsToTabs.entrySet()) {
                if (windowToTabEntry.getValue().equals(cTabItem)) {
                    return windowToTabEntry.getKey();
                }
            }
        }
        return null;
    }

    public WindowAndTabManager(Composite composite) {
        cTabFolder = new CTabFolder(composite, SWT.NONE);
        windowsToTabs = new HashMap<Window, CTabItem>();
        currentTabItemLock = new Object();
        cTabFolder.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                final CTabItem tab = ((CTabFolder) selectionEvent.getSource()).getSelection();
                synchronized (currentTabItemLock) {
                    currentTabItem = tab;
                }
            }
        });
    }

    public CTabItem getCurrentTabItem() {
        synchronized (this) {
            return currentTabItem;
        }
    }
    
    public CTabItem addOrOpenExisting() {
        final CTabItem tab;
        synchronized (windowsToTabs) {
            
            CTabItem cTabItem = null;
            for (Window existingWindow: windowsToTabs.keySet()) {
                if (existingWindow instanceof ErrorWindow) {
                    cTabItem = windowsToTabs.get(existingWindow);
                    break;
                }
            }
            if (cTabItem == null) {
                tab = new CTabItem(cTabFolder, SWT.CLOSE);
                tab.addDisposeListener(new WindowTabDisposeListener(this));
                tab.setText("Error stack");
                
                windowsToTabs.put(new ErrorWindow(cTabFolder, tab), tab);
            } else {
                tab = cTabItem;
            }
            
            synchronized (currentTabItemLock) {
                currentTabItem = tab;
                cTabFolder.setSelection(currentTabItem);
            }
            
            /*if (!windowsToTabs.isEmpty())
                composite.setVisible(true);*/
        }
        
        return tab;
    }
    
    public void removeWindowByTab(CTabItem tab) {
        synchronized (windowsToTabs) {
            Iterator<Map.Entry<Window, CTabItem>> iterator = windowsToTabs.entrySet().iterator();
            while (iterator.hasNext()) {
                final Map.Entry<Window, CTabItem> next = iterator.next();
                if (next.getValue().equals(tab)) {
                    iterator.remove();
                }
            }
            /*if (windowsToTabs.isEmpty())
                composite.setVisible(false);*/
        }
    }
    
}
