package com.bsbnb.creditregistry.client.core.datamodel.job;

import com.bsbnb.creditregistry.client.core.DocumentAndTabManager;
import com.bsbnb.creditregistry.client.core.Window;
import com.bsbnb.creditregistry.client.core.WindowAndTabManager;
import com.bsbnb.creditregistry.client.core.datamodel.Document;
import com.bsbnb.creditregistry.client.util.XMLUtil;
import com.bsbnb.creditregistry.client.util.XMLUtil.XMLParserHandler;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Shell;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

public class ValidateJob implements Runnable {

    private final DocumentAndTabManager documentAndTabManager;
    private final WindowAndTabManager windowAndTabManager;
    private final CTabItem tabToValidate;
    private final Shell shell;
    private final Document parsedDocument;

    public ValidateJob(final DocumentAndTabManager documentAndTabManager, final WindowAndTabManager windowAndTabManager, 
            CTabItem tabToValidate, final Document parsedDocument, final Shell shell) {
        this.documentAndTabManager = documentAndTabManager;
        this.windowAndTabManager = windowAndTabManager;
        this.tabToValidate = tabToValidate;
        this.parsedDocument = parsedDocument;
        this.shell = shell;
    }

    public void run() {
        final Document document = documentAndTabManager.getDocument(tabToValidate);
        if (document != null) {
            // TODO: move here logic for validate xml.
            /*final CTabItem currentTab = documentAndTabManager.getCurrentTabItem();
            final Document newDocument = new Document(document.getPath(), ((Text)currentTab.getControl()).getText(), 
                    document.isTemporary(), document.getMimeType(), document.getEncoding());*/
            
            XMLParserHandler handler = new XMLParserHandler(parsedDocument.getCustomIdentifiers());
            try {
                XMLUtil.validate(new ByteArrayInputStream(parsedDocument.getContent().getBytes(document.getEncoding())), handler);
            } catch (SAXNotSupportedException ex) {
                
            } catch (ParserConfigurationException ex) {
            	
            } catch (SAXNotRecognizedException ex) {
            	
            } catch (SAXException ex) {
            	
            } catch (IOException ex) {
                
            } finally {
                parsedDocument.setParseMessages(handler.getParseMessages());
            }
            
            shell.getDisplay().asyncExec(new Runnable() {

                public void run() {
                    documentAndTabManager.reload(document, parsedDocument);
                    CTabItem cTabItem = windowAndTabManager.addOrOpenExisting(/*newDocument*/);
                    Window window = windowAndTabManager.getWindow(cTabItem);
                    window.clear();
                    window.fill(parsedDocument);
                }
            });
        }
    }
}
