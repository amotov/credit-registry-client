package com.bsbnb.creditregistry.client.core;

import com.bsbnb.creditregistry.client.core.datamodel.CustomIdentifier;
import com.bsbnb.creditregistry.client.core.datamodel.Document;
import com.bsbnb.creditregistry.client.core.datamodel.ParseMessage;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 *
 * @author Alexandr.Motov
 */
public class ErrorWindow implements Window {

    private Table table = null;

    public ErrorWindow(CTabFolder cTabFolder, CTabItem cTabItem) {
        table = new Table(cTabFolder, SWT.NONE);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        table.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        cTabItem.setControl(table);
    }

    @Override
    public void fill(Document document) {
        table.setRedraw(false);
 
        createTableColumns(document.getCustomIdentifiers());
        
        for (Iterator iterator = document.getParseMessages().iterator(); iterator.hasNext();) {
            ParseMessage parseMessage = (ParseMessage) iterator.next();
            
            TableItem item = new TableItem(table, SWT.NONE);
            
            Map<CustomIdentifier, Object> values = parseMessage.getCustomValues();
            int i = 0;
            for (CustomIdentifier customIdentifier: document.getCustomIdentifiers()) {
                if (values.containsKey(customIdentifier)) {
                	Object value = values.get(customIdentifier);
                    item.setText(i, (value == null ? "" : value.toString()));
                }
                i++;
            }

            item.setText(i, parseMessage.getSeverity());
            item.setText(i + 1, new Integer(parseMessage.getLineNumber()).toString());
            item.setText(i + 2, new Integer(parseMessage.getColumnNumber()).toString());
            item.setText(i + 3, parseMessage.getMessage());
        }
        table.setRedraw(true);
    }

    private void createTableColumns(Collection<CustomIdentifier> customIdentifiers) {
        // Удаляем текущие столбцы (на случай если были изменены пользовательские при запуске проверки)
        while (table.getColumnCount() > 0) {
            table.getColumns()[0].dispose();
        }

        // Создаем пользовательские столбцы
        TableColumn[] column = new TableColumn[customIdentifiers.size() + 4];
        int i = 0;
        for (CustomIdentifier customIdentifier : customIdentifiers) {
            column[i] = new TableColumn(table, SWT.LEFT);
            column[i].setText(customIdentifier.getName());
            column[i].setWidth(100);
            i++;
        }

        // Создаем стандартные столбцы
        column[i] = new TableColumn(table, SWT.LEFT);
        column[i].setText("Критичность");
        column[i].setWidth(100);

        column[i + 1] = new TableColumn(table, SWT.RIGHT);
        column[i + 1].setText("Номер строки");
        column[i + 1].setWidth(100);

        column[i + 2] = new TableColumn(table, SWT.RIGHT);
        column[i + 2].setText("Позиция в строке");
        column[i + 2].setWidth(100);

        column[i + 3] = new TableColumn(table, SWT.LEFT);
        column[i + 3].setText("Сообщение");
        column[i + 3].setWidth(500);
    }
    
    @Override
    public void clear() {
        table.setRedraw(false);
        table.removeAll();
        table.setRedraw(true);
    }
    
    
    
}
