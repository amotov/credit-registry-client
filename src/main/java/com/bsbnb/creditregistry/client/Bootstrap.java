package com.bsbnb.creditregistry.client;

import com.bsbnb.creditregistry.client.core.DocumentAndTabManager;
import com.bsbnb.creditregistry.client.core.TextEditor;
import com.bsbnb.creditregistry.client.core.WindowAndTabManager;
import com.bsbnb.creditregistry.client.language.LanguageUtil;
import com.bsbnb.creditregistry.client.util.LocaleUtil;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Shell;

/**
 *
 * @author Admin
 */
public class Bootstrap {

    public static void load() {
        final Display display = new Display();
        final Shell shell = new Shell(display);
        
        /*ClassLoader cl = Bootstrap.class.getClassLoader();
        final Image image = new Image(display, ClassLoader.getSystemResourceAsStream("images/credit.png"));
        shell.setImage(image);*/
        
        shell.setText(LanguageUtil.get(LocaleUtil.getDefault(), "application-text"));
        
        /*shell.addDisposeListener(new DisposeListener() {
        public void widgetDisposed(DisposeEvent arg0) {
            image.dispose();
        }
        });*/
        
        SashForm sashForm = new SashForm(shell, SWT.VERTICAL);
        sashForm.setLayout(new FillLayout());

        Composite topComposite = new Composite(sashForm, SWT.NONE);
        topComposite.setLayout(new FillLayout());
        
        Composite bottomComposite = new Composite(sashForm, SWT.NONE);
        bottomComposite.setLayout(new FillLayout());
        //bottomComposite.setVisible(false);
    
        final DocumentAndTabManager documentAndTabManager = new DocumentAndTabManager(topComposite);
        final WindowAndTabManager windowAndTabManager = new WindowAndTabManager(bottomComposite);
        
        final ExecutorService executorService = Executors.newFixedThreadPool(100);
        final TextEditor textEditor = new TextEditor(shell, documentAndTabManager, windowAndTabManager, executorService);
        textEditor.init();
        textEditor.run();
    }
    
}
