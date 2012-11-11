package br.org.isvi.mgadmin;

import java.util.ResourceBundle;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CaretEvent;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;

import br.org.isvi.mgadmin.cocoa.CocoaUIEnhancer;
import br.org.isvi.mgadmin.controllers.CommandEditorController;
import br.org.isvi.mgadmin.controllers.MainTreeMenuController;
import br.org.isvi.mgadmin.controllers.QueryControler;
import br.org.isvi.mgadmin.controllers.QueryProcessor;
import br.org.isvi.mgadmin.controllers.SystemMainController;
import br.org.isvi.mgadmin.controllers.SystemMainController.TipoItens;
import br.org.isvi.mgadmin.model.PreparedStatment;
import br.org.isvi.mgadmin.util.undoredo.operations.Document;

public class MainWindow {

	protected Shell shell;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Tree treeMain;
	private QueryControler queryControler = new QueryControler();
	private Text txtUsing;
	private Tree treeResultado;
	private TabItem tbtmLog;
	private StyledText stlTxtLog;
	private Display display;
	private StyledText styledTextProperties;
	private Menu menuMainTree;
	private Composite compositeQueryComposer;
	private Menu menuBar;
	
	public StyledText stlTxtQueryComposer;
	public SystemMainController systemMainController;
	public Document documentOperations = new Document("");
	public CommandEditorController commandEditorController;
	
	private MenuItem mntmEdit;
	private MenuItem mntmUndo;
	private MenuItem mntmRedo;
	private MenuItem mntmCopy;
	private MenuItem mntmCut;
	private MenuItem mntmPaste;
	private MenuItem mntmProperties; 
	
	public MainWindow() {
		commandEditorController = new CommandEditorController(this);
	}
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Display.setAppName("MgAdmin");
			Display.getDefault();
			MainWindow window = new MainWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setSearchEnabled(boolean en) {
		compositeQueryComposer.setEnabled(en);
	}
	
	@Deprecated
	public void startRealm() {
		Display display = Display.getDefault();
		Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {
			@Override
			public void run() {
				open();				
			}
		});
	}
	
	/**
	 * Open the window.
	 */
	public void open() {
		display = Display.getDefault();
		
		if (SWT.getPlatform().equals("cocoa")) {
			new CocoaUIEnhancer().earlyStartup();
		}
		
		//Filter for main menu
//		display.addFilter(SWT.KeyDown, new Listener() {
//			@Override
//			public void handleEvent(Event e) {
//				commandEditorController.filterShortCuts(e);
//			}
//		});
		
		createContents();
		shell.open();
		shell.layout();
		
		systemMainController = new SystemMainController(this);
		systemMainController.getServers(treeMain);
		
		menuMainTree = new Menu(treeMain);
		treeMain.setMenu(menuMainTree);
		
		MenuItem mntmOpen = new MenuItem(menuMainTree, SWT.NONE);
		mntmOpen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem item = (TreeItem) treeMain.getData("item_selected");
				try {
					systemMainController.openItem(item);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		mntmOpen.setText(ResourceBundle.getBundle("br.org.isvi.mgadmin.message.mainwindow").getString("MainWindow.mntmOpen.text")); //$NON-NLS-1$ //$NON-NLS-2$
		
		MenuItem mntmAdd = new MenuItem(menuMainTree, SWT.NONE);
		mntmAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem item = (TreeItem) treeMain.getData("item_selected");
				if(item == null) {
					systemMainController.openNewConnectionDlg(shell);
				} else if(item.getData("tipo").equals(TipoItens.server)) {
					try {
						systemMainController.openNewDatabaseDlg(item, shell);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				} else if(item.getData("tipo").equals(TipoItens.collections)) {
					try {
						systemMainController.openNewCollectionDlg(item, shell);
					} catch (Exception e1) {
						e1.printStackTrace();
					}					
				}  else if(item.getData("tipo").equals(TipoItens.collection)) {
					try {
						systemMainController.openNewDocumentDlg(item, shell);
					} catch (Exception e1) {
						e1.printStackTrace();
					}					
				}
				
				
			}
		});
		mntmAdd.setText(ResourceBundle.getBundle("br.org.isvi.mgadmin.message.mainwindow").getString("MainWindow.mntmAdd.text")); //$NON-NLS-1$ //$NON-NLS-2$
		
		MenuItem mntmRemove = new MenuItem(menuMainTree, SWT.NONE);
		mntmRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem item = (TreeItem) treeMain.getData("item_selected");
				if(item == null) {
					
				} else if(item.getData("tipo").equals(TipoItens.server)) {
					systemMainController.removeConnectionItem(item, shell);
				} else if(item.getData("tipo").equals(TipoItens.collections)) {
									
				} else if(item.getData("tipo").equals(TipoItens.collection)) {
					systemMainController.removeCollectionItem(item, shell);
				}	
			}
		});
		mntmRemove.setText(ResourceBundle.getBundle("br.org.isvi.mgadmin.message.mainwindow").getString("MainWindow.mntmRemove.text")); //$NON-NLS-1$ //$NON-NLS-2$
		
		MenuItem mntmClose = new MenuItem(menuMainTree, SWT.NONE);
		mntmClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem item = (TreeItem) treeMain.getData("item_selected");
				if(item == null) {
					
				} else if(item.getData("tipo").equals(TipoItens.server)) {
					systemMainController.closeServer(item);
				} else if(item.getData("tipo").equals(TipoItens.collections)) {
					systemMainController.closeDataBase(item);
				} else if(item.getData("tipo").equals(TipoItens.collection)) {
					systemMainController.unSetUsing(item);
				}				
			}
		});
		mntmClose.setText(ResourceBundle.getBundle("br.org.isvi.mgadmin.message.mainwindow").getString("MainWindow.mntmClose.text")); //$NON-NLS-1$ //$NON-NLS-2$
		
		menuBar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menuBar);
		
		MenuItem mntmFile = new MenuItem(menuBar, SWT.CASCADE);
		mntmFile.setText(ResourceBundle.getBundle("br.org.isvi.mgadmin.message.mainwindow").getString("MainWindow.mntmFile.text")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-2$
		
		Menu menu_1 = new Menu(mntmFile);
		mntmFile.setMenu(menu_1);
		
		MenuItem mntmAddServer = new MenuItem(menu_1, SWT.NONE);
		mntmAddServer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				systemMainController.openNewConnectionDlg(shell);
			}
		});
		mntmAddServer.setText(ResourceBundle.getBundle("br.org.isvi.mgadmin.message.mainwindow").getString("MainWindow.mntmAddServer.text")); //$NON-NLS-1$ //$NON-NLS-2$
		
		mntmEdit = new MenuItem(menuBar, SWT.CASCADE);
		mntmEdit.setText(ResourceBundle.getBundle("br.org.isvi.mgadmin.message.mainwindow").getString("MainWindow.mntmEdit.text")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-2$
		
		Menu menu_2 = new Menu(mntmEdit);
		mntmEdit.setMenu(menu_2);
		
		mntmUndo = new MenuItem(menu_2, SWT.NONE);
		mntmUndo.setText(ResourceBundle.getBundle("br.org.isvi.mgadmin.message.mainwindow").getString("MainWindow.mntmUndo.text")); //$NON-NLS-1$ //$NON-NLS-2$
		
		mntmRedo = new MenuItem(menu_2, SWT.NONE);
		mntmRedo.setText(ResourceBundle.getBundle("br.org.isvi.mgadmin.message.mainwindow").getString("MainWindow.mntmRedo.text")); //$NON-NLS-1$ //$NON-NLS-2$
		
		MenuItem separatorMenuUndoRedo = new MenuItem(menu_2, SWT.SEPARATOR);
		separatorMenuUndoRedo.setText(ResourceBundle.getBundle("br.org.isvi.mgadmin.message.mainwindow").getString("MainWindow.mntmNewSubmenu.text")); //$NON-NLS-1$ //$NON-NLS-2$
		
		mntmCopy = new MenuItem(menu_2, SWT.NONE);
		mntmCopy.setText(ResourceBundle.getBundle("br.org.isvi.mgadmin.message.mainwindow").getString("MainWindow.mntmCopy.text")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-2$
		
		mntmCut = new MenuItem(menu_2, SWT.NONE);
		mntmCut.setText(ResourceBundle.getBundle("br.org.isvi.mgadmin.message.mainwindow").getString("MainWindow.mntmCut.text")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-2$
		
		mntmPaste = new MenuItem(menu_2, SWT.NONE);
		mntmPaste.setText(ResourceBundle.getBundle("br.org.isvi.mgadmin.message.mainwindow").getString("MainWindow.mntmPaste.text")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-2$
		
		MenuItem menuItem = new MenuItem(menu_2, SWT.SEPARATOR);
		menuItem.setText(ResourceBundle.getBundle("br.org.isvi.mgadmin.message.mainwindow").getString("MainWindow.other.text")); //$NON-NLS-1$ //$NON-NLS-2$
		
		mntmProperties = new MenuItem(menu_2, SWT.NONE);
		mntmProperties.setText(ResourceBundle.getBundle("br.org.isvi.mgadmin.message.mainwindow").getString("MainWindow.mntmProperties.text")); //$NON-NLS-1$ //$NON-NLS-2$
		
		MenuItem mntmHelp = new MenuItem(menuBar, SWT.CASCADE);
		mntmHelp.setText(ResourceBundle.getBundle("br.org.isvi.mgadmin.message.mainwindow").getString("MainWindow.mntmHelp.text_1")); //$NON-NLS-1$ //$NON-NLS-2$
		
		Menu menu_3 = new Menu(mntmHelp);
		mntmHelp.setMenu(menu_3);
		
		Display.setAppName("MgAdmin");
		Display.setAppVersion("0.1");
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	public void setUsingText(String using) {
		txtUsing.setText(using);
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(740, 635);
		shell.setText(ResourceBundle.getBundle("br.org.isvi.mgadmin.message.mainwindow").getString("MainWindow.shell.text")); //$NON-NLS-1$ //$NON-NLS-2$
		shell.setLayout(new FormLayout());
		
		SashForm sashForm = new SashForm(shell, SWT.NONE);
		FormData fd_sashForm = new FormData();
		fd_sashForm.bottom = new FormAttachment(100, -11);
		fd_sashForm.left = new FormAttachment(0);
		fd_sashForm.right = new FormAttachment(100);
		sashForm.setLayoutData(fd_sashForm);
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(sashForm, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		treeMain = formToolkit.createTree(scrolledComposite, SWT.NONE);
		treeMain.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
				Point point = new Point (e.x, e.y);
				TreeItem item = treeMain.getItem (point);
				if (item != null) {
					try {
						systemMainController.openItem(item);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			@Override
			public void mouseDown(MouseEvent e) {
				Point point = new Point (e.x, e.y);
				TreeItem item = treeMain.getItem (point);
				
				treeMain.setData("item_selected", item);
				
				new MainTreeMenuController().configureMenuItem(item, menuMainTree);
				
				if (item != null) {
					try {
						systemMainController.fillProperties(styledTextProperties, item);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} 
			}
		});
		
		treeMain.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.character == SWT.CR) {
					TreeItem item = treeMain.getSelection()[0];
					if (item != null) {
						try {
							systemMainController.openItem(item);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}					
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		});
		
		formToolkit.paintBordersFor(treeMain);
		scrolledComposite.setContent(treeMain);
		scrolledComposite.setMinSize(treeMain.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		ScrolledComposite scrolledComposite_1 = new ScrolledComposite(sashForm, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite_1.setExpandHorizontal(true);
		scrolledComposite_1.setExpandVertical(true);
		
		SashForm sashMainDireita = new SashForm(scrolledComposite_1, SWT.VERTICAL);
		formToolkit.adapt(sashMainDireita);
		formToolkit.paintBordersFor(sashMainDireita);
		
		TabFolder tabDadosGerais = new TabFolder(sashMainDireita, SWT.NONE);
		formToolkit.adapt(tabDadosGerais);
		formToolkit.paintBordersFor(tabDadosGerais);
		
		TabItem tbtmPropriedades = new TabItem(tabDadosGerais, SWT.NONE);
		tbtmPropriedades.setText(ResourceBundle.getBundle("br.org.isvi.mgadmin.message.mainwindow").getString("MainWindow.tbtmPropriedades.text")); //$NON-NLS-1$ //$NON-NLS-2$
		
		Composite composite = new Composite(tabDadosGerais, SWT.NONE);
		tbtmPropriedades.setControl(composite);
		formToolkit.paintBordersFor(composite);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		styledTextProperties = new StyledText(composite, SWT.BORDER | SWT.READ_ONLY);
		formToolkit.adapt(styledTextProperties);
		formToolkit.paintBordersFor(styledTextProperties);
		
		TabItem tbtmPesquisas = new TabItem(tabDadosGerais, SWT.NONE);
		tbtmPesquisas.setText(ResourceBundle.getBundle("br.org.isvi.mgadmin.message.mainwindow").getString("MainWindow.tbtmPesquisas.text")); //$NON-NLS-1$ //$NON-NLS-2$
		
		SashForm sashPesquisas = new SashForm(tabDadosGerais, SWT.VERTICAL);
		tbtmPesquisas.setControl(sashPesquisas);
		formToolkit.paintBordersFor(sashPesquisas);
		
		compositeQueryComposer = new Composite(sashPesquisas, SWT.NONE);
		compositeQueryComposer.setEnabled(false);
		compositeQueryComposer.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		formToolkit.adapt(compositeQueryComposer);
		formToolkit.paintBordersFor(compositeQueryComposer);
		compositeQueryComposer.setLayout(new FormLayout());
		
		Label lblUsing = new Label(compositeQueryComposer, SWT.NONE);
		FormData fd_lblUsing = new FormData();
		fd_lblUsing.top = new FormAttachment(0, 10);
		fd_lblUsing.left = new FormAttachment(0, 10);
		lblUsing.setLayoutData(fd_lblUsing);
		formToolkit.adapt(lblUsing, true, true);
		lblUsing.setText(ResourceBundle.getBundle("br.org.isvi.mgadmin.message.mainwindow").getString("MainWindow.lblUsing.text")); //$NON-NLS-1$ //$NON-NLS-2$
		
		txtUsing = new Text(compositeQueryComposer, SWT.BORDER);
		txtUsing.setEditable(false);
		FormData fd_txtUsing = new FormData();
		fd_txtUsing.right = new FormAttachment(lblUsing, 277, SWT.RIGHT);
		fd_txtUsing.top = new FormAttachment(0, 5);
		fd_txtUsing.left = new FormAttachment(lblUsing, 6);
		txtUsing.setLayoutData(fd_txtUsing);
		formToolkit.adapt(txtUsing, true, true);
		
		stlTxtQueryComposer = new StyledText(compositeQueryComposer, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
//		stlTxtQueryComposer.addModifyListener(new ModifyListener() {
//			public void modifyText(ModifyEvent e) {
////				commandEditorController.textModified(documentOperations, stlTxtQueryComposer.getText(), stlTxtQueryComposer.getCaretOffset());
//			}
//		});
		
//		stlTxtQueryComposer.addCaretListener(new CaretListener() {
//			
//			@Override
//			public void caretMoved(CaretEvent e) {
//				int x = stlTxtQueryComposer.getSelectionRange().x;
//				int l = stlTxtQueryComposer.getSelectionRange().y;
//				
//				commandEditorController.setSelection(x, x+l);
//			}
//		});
		
		stlTxtQueryComposer.addExtendedModifyListener(new ExtendedModifyListener() {
			
			@Override
			public void modifyText(ExtendedModifyEvent e) {
//				System.out.println("e.replacedText: " + e.replacedText);
//				System.out.println("text: " + stlTxtQueryComposer.getText());
				commandEditorController.saveState(stlTxtQueryComposer);
			}
		});
		
		stlTxtQueryComposer.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				commandEditorController.filterShortCuts(e);
			}
		});
		
		stlTxtQueryComposer.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				enableMenuQueryComposer(false);
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {
				enableMenuQueryComposer(true);
			}
		});
		
		fd_txtUsing.bottom = new FormAttachment(stlTxtQueryComposer, -6);
		FormData fd_stlTxtQueryComposer = new FormData();
		fd_stlTxtQueryComposer.right = new FormAttachment(100, -10);
		fd_stlTxtQueryComposer.left = new FormAttachment(0, 10);
		fd_stlTxtQueryComposer.bottom = new FormAttachment(100, -10);
		fd_stlTxtQueryComposer.top = new FormAttachment(lblUsing, 6);
		stlTxtQueryComposer.setLayoutData(fd_stlTxtQueryComposer);
		formToolkit.adapt(stlTxtQueryComposer);
		formToolkit.paintBordersFor(stlTxtQueryComposer);
		
		Button btnRun = new Button(compositeQueryComposer, SWT.NONE);
		btnRun.setImage(SWTResourceManager.getImage(MainWindow.class, "/br/org/isvi/mgadmin/images/play_16.png"));
		btnRun.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					PreparedStatment stm = new PreparedStatment();
					QueryProcessor proc = new QueryProcessor();
					try {
						proc.processCommand(stlTxtQueryComposer.getText(), MainWindow.this, stm);
					} catch (Exception e1) {
						
						if(proc.error != null)
							stlTxtLog.append(proc.error);
						else
							stlTxtLog.append("Unknown error!");
						return;
					}
					queryControler.process(stm);
					
					queryControler.setLogResult(stlTxtLog);
					
					treeResultado.setItemCount(0);
					
					queryControler.fetch(treeResultado);
					systemMainController.refreshMainTreeInfo();
				} catch(Exception ex1) {
					ex1.printStackTrace();
					stlTxtLog.setText("");
					stlTxtLog.append("Unknown error!");
				}
				
			}
		});
		FormData fd_btnRun = new FormData();
		fd_btnRun.bottom = new FormAttachment(stlTxtQueryComposer);
		fd_btnRun.left = new FormAttachment(txtUsing, 6);
		btnRun.setLayoutData(fd_btnRun);
		formToolkit.adapt(btnRun, true, true);
		btnRun.setText(ResourceBundle.getBundle("br.org.isvi.mgadmin.message.mainwindow").getString("MainWindow.btnRun.text")); //$NON-NLS-1$ //$NON-NLS-2$
		
		Composite compQueryResult = new Composite(sashPesquisas, SWT.BORDER);
		formToolkit.adapt(compQueryResult);
		formToolkit.paintBordersFor(compQueryResult);
		compQueryResult.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		TabFolder tabQueryResult = new TabFolder(compQueryResult, SWT.NONE);
		formToolkit.adapt(tabQueryResult);
		formToolkit.paintBordersFor(tabQueryResult);
		
		tbtmLog = new TabItem(tabQueryResult, SWT.NONE);
		tbtmLog.setText(ResourceBundle.getBundle("br.org.isvi.mgadmin.message.mainwindow").getString("MainWindow.tbtmLog.text")); //$NON-NLS-1$ //$NON-NLS-2$
		
		stlTxtLog = new StyledText(tabQueryResult, SWT.BORDER | SWT.READ_ONLY);
		tbtmLog.setControl(stlTxtLog);
		formToolkit.paintBordersFor(stlTxtLog);
		
		TabItem tbtmResult = new TabItem(tabQueryResult, SWT.NONE);
		tbtmResult.setText(ResourceBundle.getBundle("br.org.isvi.mgadmin.message.mainwindow").getString("MainWindow.tbtmResult.text")); //$NON-NLS-1$ //$NON-NLS-2$
		
		treeResultado = new Tree(tabQueryResult, SWT.BORDER);
		treeResultado.setLinesVisible(true);
		treeResultado.setHeaderVisible(true);
		tbtmResult.setControl(treeResultado);
		formToolkit.paintBordersFor(treeResultado);
		
		TreeColumn tColField = new TreeColumn(treeResultado, SWT.LEFT);
		tColField.setWidth(129);
		tColField.setText(ResourceBundle.getBundle("br.org.isvi.mgadmin.message.mainwindow").getString("MainWindow.tColField.text")); //$NON-NLS-1$ //$NON-NLS-2$
		
		TreeColumn tColValue = new TreeColumn(treeResultado, SWT.LEFT);
		tColValue.setWidth(223);
		tColValue.setText(ResourceBundle.getBundle("br.org.isvi.mgadmin.message.mainwindow").getString("MainWindow.tColValue.text")); //$NON-NLS-1$ //$NON-NLS-2$
		
		Menu menu = new Menu(treeResultado);
		treeResultado.setMenu(menu);
		
		MenuItem mntmEditSelectedItem = new MenuItem(menu, SWT.NONE);
		mntmEditSelectedItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					TreeItem items[] = treeResultado.getSelection();
					if(items != null && items.length > 0) {
						TreeItem item = items[0];					
						
						String data = item.getText(1);
						
							systemMainController.openEditDocumentDlg(data, item, shell);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		mntmEditSelectedItem.setText(ResourceBundle.getBundle("br.org.isvi.mgadmin.message.mainwindow").getString("MainWindow.mntmEditSelectedItem.text")); //$NON-NLS-1$ //$NON-NLS-2$
		
		MenuItem mntmExclude = new MenuItem(menu, SWT.NONE);
		mntmExclude.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					TreeItem items[] = treeResultado.getSelection();
					if(items != null && items.length > 0) {
						if(MessageDialog.openConfirm(shell, "Warning", "Are you sure you want to exclude this item?")) {
							TreeItem item = items[0];					
							String data = item.getText(1);
							systemMainController.removeDocument(data, item, shell);
							item.dispose();
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}				
			}
		});
		mntmExclude.setText(ResourceBundle.getBundle("br.org.isvi.mgadmin.message.mainwindow").getString("MainWindow.mntmExclude.text")); //$NON-NLS-1$ //$NON-NLS-2$
		sashPesquisas.setWeights(new int[] {163, 107});
		sashMainDireita.setWeights(new int[] {1});
		scrolledComposite_1.setContent(sashMainDireita);
		scrolledComposite_1.setMinSize(sashMainDireita.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		ToolBar toolBar = new ToolBar(shell, SWT.FLAT | SWT.RIGHT);
		fd_sashForm.top = new FormAttachment(0, 49);
		FormData fd_toolBar = new FormData();
		fd_toolBar.bottom = new FormAttachment(sashForm, -6);
		sashForm.setWeights(new int[] {160, 577});
		fd_toolBar.left = new FormAttachment(0);
		fd_toolBar.top = new FormAttachment(0);
		fd_toolBar.right = new FormAttachment(100);
		toolBar.setLayoutData(fd_toolBar);
		formToolkit.adapt(toolBar);
		formToolkit.paintBordersFor(toolBar);
		
		ToolItem tltmBtnconnect = new ToolItem(toolBar, SWT.NONE);
		tltmBtnconnect.setToolTipText(ResourceBundle.getBundle("br.org.isvi.mgadmin.message.mainwindow").getString("MainWindow.tltmBtnconnect.toolTipText")); //$NON-NLS-1$ //$NON-NLS-2$
		tltmBtnconnect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				systemMainController.openNewConnectionDlg(shell);
			}
		});
		tltmBtnconnect.setDisabledImage(SWTResourceManager.getImage(MainWindow.class, "/br/org/isvi/mgadmin/images/connect-icon.png"));
		tltmBtnconnect.setImage(SWTResourceManager.getImage(MainWindow.class, "/br/org/isvi/mgadmin/images/connect-icon32.png"));
		
		ToolItem tltmBtnRefresh = new ToolItem(toolBar, SWT.NONE);
		tltmBtnRefresh.setToolTipText(ResourceBundle.getBundle("br.org.isvi.mgadmin.message.mainwindow").getString("MainWindow.tltmBtnRefresh.toolTipText")); //$NON-NLS-1$ //$NON-NLS-2$
		tltmBtnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				systemMainController.refreshMainTreeInfo();
			}
		});
		tltmBtnRefresh.setImage(SWTResourceManager.getImage(MainWindow.class, "/br/org/isvi/mgadmin/images/Sign-Refresh-icon32.png"));
	}
	
	private void enableMenuQueryComposer(boolean en) {
		mntmEdit.setEnabled(en);      
		mntmUndo.setEnabled(en);      
		mntmRedo.setEnabled(en);
		mntmCopy.setEnabled(en);
		mntmCut.setEnabled(en);
		mntmPaste.setEnabled(en);
//		mntmProperties.setEnabled(en);
	}
}
