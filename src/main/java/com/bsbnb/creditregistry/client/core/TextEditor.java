package com.bsbnb.creditregistry.client.core;

import com.bsbnb.creditregistry.client.core.listeners.*;
import com.bsbnb.creditregistry.client.language.LanguageUtil;
import com.bsbnb.creditregistry.client.util.LocaleUtil;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.eclipse.jface.action.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;

public class TextEditor extends ApplicationWindow {

    class EncodingAction extends Action {

        StatusLineManager statman;
        short triggercount = 0;

        public EncodingAction(StatusLineManager sm) {
            super("&Trigger@Ctrl+T", AS_PUSH_BUTTON);
            statman = sm;
            setToolTipText("Trigger the Action");
            //setImageDescriptor(ImageDescriptor.createFromFile(this.getClass(), "eclipse.gif"));
        }

        public void run() {
            triggercount++;
            statman.setMessage("The status action has fired. Count: " + triggercount);
        }
    }
    
    class OpenAction extends Action {

        StatusLineManager statman;
        short triggercount = 0;

        public OpenAction(StatusLineManager sm) {
            super("&Open@Ctrl+O", AS_PUSH_BUTTON);
            statman = sm;
            setToolTipText("Open file");
            //setImageDescriptor(ImageDescriptor.createFromFile(this.getClass(), "eclipse.gif"));
        }

        public void run() {
            triggercount++;
            statman.setMessage("Opened. Count: " + triggercount);
        }
    }
    
    StatusLineManager encodingSlm = new StatusLineManager();
    StatusLineManager openSlm = new StatusLineManager();
    
    EncodingAction encodingAction = new EncodingAction(encodingSlm);
    OpenAction openAction = new OpenAction(openSlm);
    ActionContributionItem aci = new ActionContributionItem(encodingAction);
    private DocumentAndTabManager documentAndTabManager;
    private WindowAndTabManager windowAndTabManager;
    private ExecutorService executorService;

    public TextEditor() {
        super(null);

        executorService = Executors.newFixedThreadPool(100);

        addStatusLine();
        addMenuBar();
        addToolBar(SWT.FLAT | SWT.WRAP);
    }

    @Override
    protected Control createContents(Composite parent) {
        getShell().setText(LanguageUtil.get(LocaleUtil.getDefault(), "application-text"));

        parent.setSize(600, 400);

        SashForm sashForm = new SashForm(parent, SWT.VERTICAL);
        sashForm.setLayout(new FillLayout());

        Composite topComposite = new Composite(sashForm, SWT.NONE);
        topComposite.setLayout(new FillLayout());

        Composite bottomComposite = new Composite(sashForm, SWT.NONE);
        bottomComposite.setLayout(new FillLayout());

        documentAndTabManager = new DocumentAndTabManager(topComposite);
        windowAndTabManager = new WindowAndTabManager(bottomComposite);

        return parent;
    }

    public void init() {
        getShell().setLayout(new FillLayout());

        getShell().setMenuBar(createAndSetUpMenu());
    }

    private Menu createAndSetUpMenu() {
        final Menu menu = new Menu(getShell(), SWT.BAR);

        // Create all the items in the bar menu
        final MenuItem fileItem = new MenuItem(menu, SWT.CASCADE);
        fileItem.setText(LanguageUtil.get(LocaleUtil.getDefault(), "menu-item-file-caption"));
        final MenuItem xmlItem = new MenuItem(menu, SWT.CASCADE);
        xmlItem.setText(LanguageUtil.get(LocaleUtil.getDefault(), "menu-item-xml-caption"));
        final MenuItem windowItem = new MenuItem(menu, SWT.CASCADE);
        windowItem.setText(LanguageUtil.get(LocaleUtil.getDefault(), "menu-item-window-caption"));
        final MenuItem helpItem = new MenuItem(menu, SWT.CASCADE);
        helpItem.setText(LanguageUtil.get(LocaleUtil.getDefault(), "menu-item-help-caption"));


        createAndSetUpFileMenu(menu, fileItem);
        createAndSetUpXmlMenu(menu, xmlItem);
        createAndSetUpWindowMenu(menu, windowItem);
        createAndSetUpHelpMenu(menu, helpItem);
        return menu;
    }

    private void createAndSetUpFileMenu(Menu menu, MenuItem fileItem) {
        final Menu fileMenu = new Menu(menu);
        fileItem.setMenu(fileMenu);
        setUpFileMenu(fileMenu);
    }

    private void createAndSetUpHelpMenu(Menu menu, MenuItem helpItem) {
        final Menu helpMenu = new Menu(menu);
        helpItem.setMenu(helpMenu);
        final MenuItem about = new MenuItem(helpMenu, SWT.NONE);
        about.setText("About");
        about.addSelectionListener(new HelpMenuAboutSelectionListener(getShell()));
    }

    private void createAndSetUpXmlMenu(Menu menu, MenuItem xmlItem) {
        final Menu xmlMenu = new Menu(menu);
        xmlItem.setMenu(xmlMenu);
        final MenuItem validate = new MenuItem(xmlMenu, SWT.NONE);
        validate.setText("Validate");
        validate.addSelectionListener(new XmlMenuValidateSelectionListener(getShell(), documentAndTabManager, windowAndTabManager, executorService));
    }

    private void createAndSetUpWindowMenu(Menu menu, MenuItem windowItem) {
        final Menu windowMenu = new Menu(menu);
        windowItem.setMenu(windowMenu);

        final Menu xmlMenu = new Menu(getShell(), SWT.DROP_DOWN);
        final MenuItem xml = new MenuItem(windowMenu, SWT.CASCADE);
        xml.setText("XML");
        xml.setMenu(xmlMenu);

        final MenuItem validate = new MenuItem(xmlMenu, SWT.NONE);
        validate.setText("Error stack");
        //validate.addSelectionListener(new XmlMenuValidateSelectionListener(shell, documentAndTabManager, executorService));
    }

    private void setUpFileMenu(Menu fileMenu) {
        setUpFileMenuNewAction(fileMenu);
        setUpFileMenuOpenAction(fileMenu);

        new MenuItem(fileMenu, SWT.SEPARATOR);

        setUpFileMenuReloadAction(fileMenu);

        new MenuItem(fileMenu, SWT.SEPARATOR);

        setUpFileMenuSaveAction(fileMenu);
        setUpFileMenuSaveAsAction(fileMenu);

        new MenuItem(fileMenu, SWT.SEPARATOR);

        setUpFileMenuExitAction(fileMenu);
    }

    private void setUpFileMenuReloadAction(Menu fileMenu) {
        MenuItem exitItem = new MenuItem(fileMenu, SWT.NONE);
        exitItem.setText("&Reload");
        exitItem.addSelectionListener(new FileMenuReloadSelectionListener(getShell(), documentAndTabManager, executorService));
    }

    private void setUpFileMenuExitAction(Menu fileMenu) {
        MenuItem exitItem = new MenuItem(fileMenu, SWT.NONE);
        exitItem.setText("&Exit");
        exitItem.addSelectionListener(new FileMenuExitSelectionListener(getShell()));
    }

    private void setUpFileMenuSaveAsAction(Menu fileMenu) {
        MenuItem saveAsItem = new MenuItem(fileMenu, SWT.NONE);
        saveAsItem.addSelectionListener(new FileMenuSaveAsSelectionListener(getShell(), documentAndTabManager, executorService));
        saveAsItem.setText("Sa&ve As...");
    }

    private void setUpFileMenuSaveAction(Menu fileMenu) {
        MenuItem saveItem = new MenuItem(fileMenu, SWT.NONE);
        saveItem.setText("&Save");
        saveItem.addSelectionListener(new FileMenuSaveSelectionListener(getShell(), documentAndTabManager, executorService));
    }

    private void setUpFileMenuOpenAction(Menu fileMenu) {
        MenuItem openItem = new MenuItem(fileMenu, SWT.NONE);
        openItem.setText("&Open...");
        openItem.addSelectionListener(new FileMenuOpenSelectionListener(getShell(), documentAndTabManager, executorService));
    }

    private void setUpFileMenuNewAction(Menu fileMenu) {
        MenuItem newItem = new MenuItem(fileMenu, SWT.NONE);
        newItem.setText("&New");
        newItem.addSelectionListener(new FileMenuNewSelectionListener(getShell(), documentAndTabManager, executorService));
    }

    protected MenuManager createMenuManager() {
        MenuManager menuManager = new MenuManager(null);
        MenuManager fileMenu = new MenuManager("File");
        MenuManager xmlMenu = new MenuManager("XML");
        MenuManager windowMenu = new MenuManager("Window");
        MenuManager helpMenu = new MenuManager("Window");
        
        menuManager.add(fileMenu);
        menuManager.add(xmlMenu);
        menuManager.add(windowMenu);
        menuManager.add(helpMenu);
        
        fileMenu.add(openAction);
        fileMenu.add(encodingAction);
        
        
        
        return menuManager;
    }

    protected ToolBarManager createToolBarManager(int style) {
        ToolBarManager toolBarManager = new ToolBarManager(style);
        toolBarManager.add(encodingAction);
        
        return toolBarManager;
    }

    protected StatusLineManager createStatusLineManager() {
        return encodingSlm;
    }
}
