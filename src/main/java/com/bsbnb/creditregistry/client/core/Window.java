package com.bsbnb.creditregistry.client.core;

import com.bsbnb.creditregistry.client.core.datamodel.Document;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.TabItem;

/**
 *
 * @author Alexandr.Motov
 */
public interface Window {
    
    public void fill(Document document);
    
    public void clear();
    
}
