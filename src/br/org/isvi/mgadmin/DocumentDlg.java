//package br.org.isvi.mgadmin;
//
//import javax.script.ScriptEngine;
//import javax.script.ScriptEngineManager;
//import javax.script.ScriptException;
//
//import org.eclipse.jface.dialogs.IDialogConstants;
//import org.eclipse.jface.dialogs.TitleAreaDialog;
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.custom.CTabFolder;
//import org.eclipse.swt.custom.CTabItem;
//import org.eclipse.swt.custom.StyledText;
//import org.eclipse.swt.events.ModifyEvent;
//import org.eclipse.swt.events.ModifyListener;
//import org.eclipse.swt.events.MouseAdapter;
//import org.eclipse.swt.events.MouseEvent;
//import org.eclipse.swt.events.SelectionAdapter;
//import org.eclipse.swt.events.SelectionEvent;
//import org.eclipse.swt.graphics.Point;
//import org.eclipse.swt.layout.GridData;
//import org.eclipse.swt.widgets.Button;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Control;
//import org.eclipse.swt.widgets.Display;
//import org.eclipse.swt.widgets.Event;
//import org.eclipse.swt.widgets.Listener;
//import org.eclipse.swt.widgets.Menu;
//import org.eclipse.swt.widgets.MenuItem;
//import org.eclipse.swt.widgets.Shell;
//import org.eclipse.swt.widgets.Tree;
//import org.eclipse.swt.widgets.TreeColumn;
//import org.eclipse.swt.widgets.TreeItem;
//import org.eclipse.wb.swt.SWTResourceManager;
//
//import br.org.isvi.mgadmin.dlg.ErrorsDlg;
//import br.org.isvi.mgadmin.dlg.NameValueDlg;
//import br.org.isvi.mgadmin.model.DocumentVO;
//import br.org.isvi.mgadmin.model.NameValueVO;
//
//import com.mongodb.BasicDBList;
//import com.mongodb.BasicDBObject;
//import com.mongodb.DBObject;
//import com.mongodb.util.JSON;
//
//public class DocumentDlg extends TitleAreaDialog {
//	private DocumentVO params;
//	private DBObject rootObj;
//	private StyledText styledTextAdvanced;
//	private Button buttonOk;
//	private Tree treeUserMode;
//    private ScriptEngineManager engineManager;
//    private ScriptEngine engineJavascript;	
//    private Button btnErrors;
//    private String errors;
//	
//    enum TypeItem {
//    	array, document, root_, editable, array_value
//    }
//    
//    
//	/**
//	 * Create the dialog.
//	 * @param parentShell
//	 */
//	public DocumentDlg(Shell parentShell) {
//		super(parentShell);
//		setHelpAvailable(false);
//		
//		engineManager = new ScriptEngineManager();
//	    engineJavascript = engineManager.getEngineByName("JavaScript");		
//	}
//
//	/**
//	 * Create contents of the dialog.
//	 * @param parent
//	 */
//	@Override
//	protected Control createDialogArea(Composite parent) {
//		setMessage("Fill the fields and press ok to create a new Document");
//		setTitleImage(SWTResourceManager.getImage(DocumentDlg.class, "/br/org/isvi/mgadmin/images/document_48.png"));
//		setTitle("Creates a new Document");
//		Composite area = (Composite) super.createDialogArea(parent);
//		Composite container = new Composite(area, SWT.NONE);
//		container.setLayoutData(new GridData(GridData.FILL_BOTH));
//		
//		CTabFolder tabFolder = new CTabFolder(container, SWT.BORDER);
//		tabFolder.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {		
//				
//				if(rootObj != null)
//					styledTextAdvanced.setText(rootObj.toString());
//			}
//		});
//		tabFolder.setBounds(10, 10, 430, 332);
//		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
//		
//		CTabItem tbtmUserMode = new CTabItem(tabFolder, SWT.NONE);
//		tbtmUserMode.setText("User mode");
//		
//		treeUserMode = new Tree(tabFolder, SWT.BORDER);
//		treeUserMode.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseDoubleClick(MouseEvent e) {
//				editSelectedItem();
//			}
//		});
//		treeUserMode.setLinesVisible(true);
//		treeUserMode.setHeaderVisible(true);
//		tbtmUserMode.setControl(treeUserMode);
//		
//		TreeColumn treeColumn = new TreeColumn(treeUserMode, SWT.NONE);
//		treeColumn.setWidth(100);
//		treeColumn.setText("Field");
//		
//		TreeColumn treeColumn_1 = new TreeColumn(treeUserMode, SWT.NONE);
//		treeColumn_1.setWidth(300);
//		treeColumn_1.setText("Value");
//		
//		Menu menuUserMode = new Menu(treeUserMode);
//		treeUserMode.setMenu(menuUserMode);
//		
//		MenuItem mnItAddDocument = new MenuItem(menuUserMode, SWT.NONE);
//		mnItAddDocument.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				insertItem(TypeItem.document);
//			}
//		});
//		mnItAddDocument.setText("Add Document");
//		
//		MenuItem mntmAddArray = new MenuItem(menuUserMode, SWT.NONE);
//		mntmAddArray.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				insertItem(TypeItem.array);
//			}
//		});
//		mntmAddArray.setText("Add Array");
//		
//		MenuItem mntmAddValue = new MenuItem(menuUserMode, SWT.NONE);
//		mntmAddValue.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				insertItem(TypeItem.editable);
//			}
//		});
//		mntmAddValue.setText("Add Value");
//		
//		MenuItem userModeMenuSep = new MenuItem(menuUserMode, SWT.SEPARATOR);
//		
//		MenuItem mntmDelete = new MenuItem(menuUserMode, SWT.NONE);
//		mntmDelete.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				deleteSelectedItem();
//			}
//		});
//		mntmDelete.setText("Delete");
//		
//		CTabItem tbtmAdvancedMode = new CTabItem(tabFolder, SWT.NONE);
//		tbtmAdvancedMode.setText("Advanced mode");
//		
//		styledTextAdvanced = new StyledText(tabFolder, SWT.BORDER);
//		styledTextAdvanced.addModifyListener(new ModifyListener() {
//			public void modifyText(ModifyEvent arg0) {
//				try {
//					engineJavascript.put(engineJavascript.FILENAME, "Document");
//					engineJavascript.eval("doc="+styledTextAdvanced.getText());
//					
//					rootObj = (DBObject) JSON.parse(styledTextAdvanced.getText());
//					
//					params.dbobj = rootObj;
//					
//					treeUserMode.setItemCount(0);
//					createItem(rootObj, treeUserMode);
//					disableErrors(true);			
//				} 
//				catch(ScriptException e) {
//					//System.out.println(e.getMessage());
//					errors = e.getMessage();
//					disableErrors(false);
//				} catch(Exception e) {
//					System.out.println(e.getMessage());
//					errors = "Unknown Error!";
//				}
//			}
//		});
//		tbtmAdvancedMode.setControl(styledTextAdvanced);
//		
//		btnErrors = new Button(container, SWT.FLAT);
//		btnErrors.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				ErrorsDlg errorsDlg = new ErrorsDlg(getShell());
//				errorsDlg.setErrorsText(errors);
//				errorsDlg.open();
//			}
//		});
//		btnErrors.setEnabled(false);
//		btnErrors.setToolTipText("Errors");
//		btnErrors.setImage(SWTResourceManager.getImage(DocumentDlg.class, "/br/org/isvi/mgadmin/images/document_error_16_ativado.png"));
//		btnErrors.setBounds(10, 348, 39, 37);
//
//		getShell().addListener(SWT.Show, new Listener() {
//			
//			@Override
//			public void handleEvent(Event e) {
//				configDialog();
//			}
//		});
//		
//		return area;
//	}
//
//	
//	
//	/**
//	 * Create contents of the button bar.
//	 * @param parent
//	 */
//	@Override
//	protected void createButtonsForButtonBar(Composite parent) {
//		buttonOk = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
//				true);
//		buttonOk.setEnabled(false);
//		createButton(parent, IDialogConstants.CANCEL_ID,
//				IDialogConstants.CANCEL_LABEL, false);
//	}
//
//	/**
//	 * Return the initial size of the dialog.
//	 */
//	@Override
//	protected Point getInitialSize() {
//		return new Point(450, 551);
//	}
//	
//	private void configDialog() {
//		if(rootObj != null) {
//			createItem(rootObj, treeUserMode);
//			styledTextAdvanced.setText(rootObj.toString());
//		}
//	}
//	
//	public void setParams(DocumentVO params) {
//		rootObj = params.dbobj;
//		
//		this.params = params;
//	}	
//	
//	private void createItem(DBObject obj, Tree tree) {
//		tree.setItemCount(0);
//		TreeItem item = new TreeItem (tree, SWT.NONE);
//		item.setExpanded(true);
//		item.setText (0, "Document");
//		item.setText (1, obj.toString());
//		item.setData ("obj", obj);
//		item.setData("type", TypeItem.root_);
//		
//		for(String k : obj.keySet()) {
//			
//			TreeItem fld = new TreeItem (item, SWT.NONE);
//			fld.setExpanded(true);
//			
//			fld.setText (0, k);
//			
//			Object value = obj.get(k);
//			
//			if(value instanceof DBObject) {
//				DBObject val = (DBObject) value;
//				fld.setText (1, val.toString());
//				createItem(val, fld);
//			} else {
//				fld.setText (1, value.toString());
//				fld.setData("type", TypeItem.editable);
//				fld.setData ("obj", obj);
//			}
//		}
//		
//		for(TreeItem itm : treeUserMode.getItems()) {
//			
//			itm.setExpanded(true);
//			
//			if(itm.getItemCount() > 0) {
//				extandAll(itm);
//			}
//		}
//		
//		disableErrors(true);//everything is ok		
//	}
//	
//	private void createItem(DBObject obj, TreeItem par) {
//		TreeItem item = new TreeItem (par, SWT.NONE);
//		item.setExpanded(true);
//		par.setData ("obj", obj);
//		item.setData ("obj", obj);
//		
//		if(obj instanceof BasicDBList) {
//			item.setText (0, "Array");
//			par.setData("type", TypeItem.array);
//			item.setData("type", TypeItem.root_);
//		} else {
//			item.setText (0, "Document");
//			par.setData("type", TypeItem.document);
//			item.setData("type", TypeItem.root_);
//		}
//		
//		item.setText (1, obj.toString());
//		
//		for(String k : obj.keySet()) {
//			
//			TreeItem fld = new TreeItem (item, SWT.NONE);
//			fld.setExpanded(true);
//			
//			fld.setText (0, k);
//			
//			Object value = obj.get(k);
//			
//			if(value instanceof DBObject) {
//				DBObject val = (DBObject) value;
//				createItem(val, fld);
//			} else {
//				fld.setData("type", TypeItem.array_value);
//				fld.setText (1, value.toString());
//				fld.setData ("obj", obj);
//			}
//		}
//	}
//	
//	private boolean canAddItem() {
//		return true;
//	}
//	
//	private void insertItem(TypeItem typeInsert) {
//		NameValueVO nameValue = new NameValueVO();
//		
//		NameValueDlg dlg = new NameValueDlg(DocumentDlg.this.getShell());
//		
//		TreeItem itemSelect = null;
//		
//		if(treeUserMode.getItemCount() <= 0) {
//			rootObj = new BasicDBObject();
//			createItem(rootObj, treeUserMode);
//			itemSelect = treeUserMode.getItem(0);
//		} else {			
//			itemSelect = treeUserMode.getSelection()[0];
//		}
//		
//		TypeItem tp = (TypeItem) itemSelect.getData("type");
//		DBObject obj =  (DBObject) itemSelect.getData("obj");
//		
//		boolean inserir = true;
//		
//		if(!tp.equals(TypeItem.document) && !tp.equals(TypeItem.array)) {
//			nameValue = new NameValueVO();
//			
//			nameValue.name = "";
//			nameValue.value = "";			
//			nameValue.nameEnabled = !tp.equals(TypeItem.array);
//			nameValue.valueEnabled = !tp.equals(TypeItem.array) && !tp.equals(TypeItem.document);
//			
//			dlg.setParams(nameValue);
//			inserir = dlg.open() >= 0;
//		}
//				
//		if(inserir) {
//			
//			if(rootObj == null) {
//				rootObj = new BasicDBObject();
//			}
//			
//			if(treeUserMode.getSelection() == null) {
//				rootObj.put(nameValue.name, new BasicDBList());
//			} else {
//				
//				Object value = null;
//				
//				/*Generate value*/
//				switch(typeInsert) {
//				case array:
//					value = new BasicDBList();
//					break;
//				case root_:
//				case document:
//					value = new BasicDBObject();
//					break;
//				case array_value:
//				case editable:
//					value = nameValue.value;
//					break;
//				}			
//				
//				/*Insert value*/
//				switch(tp) {
//				case array: 
//				case document:
//					return;
//				case root_: {
//						if(obj instanceof BasicDBList) {
//							((BasicDBList) obj).add(value);
//						} else {
//							obj.put(nameValue.name, value);
//						}
//					}
//					break;
//				case array_value: {
//						BasicDBList lst = (BasicDBList) obj;
//						int indexAr = Integer.parseInt(itemSelect.getText(0));
//						lst.set(indexAr, value);
//					}
//					break;
//				case editable:
//					obj.put(itemSelect.getText(0), value);
//					break;
//				}				
//				
//			}
//			
//			createItem(rootObj, treeUserMode);
//		}		
//	}
//	
//	public void extandAll(TreeItem root) {
//		for(TreeItem itm : root.getItems()) {
//			
//			itm.setExpanded(true);
//			
//			if(itm.getItemCount() > 0) {
//				extandAll(itm);
//			}
//		}		
//	}
//	
//	private void deleteSelectedItem () {
//		TreeItem item = treeUserMode.getSelection()[0];
//		TypeItem tp = (TypeItem) item.getData("type");
//		DBObject obj = (DBObject) item.getData("obj");
//		DBObject pObj = (DBObject) item.getParentItem().getData("obj");
//		
//		switch(tp) {
//			case array_value:
//				obj.removeField(item.getText(0));
//				break;
//			case root_:
//				return;
//			case array:
//			case document:
//				{
//					pObj.removeField(item.getText(0));
//				}
//				break;
//			case editable:
//				{
//					obj.removeField(item.getText(0));
//				}	
//				break;
//			default:
//				break;
//		}
//		
//		createItem(pObj, treeUserMode);
//	}
//	
//	private void editSelectedItem() {
//		
//		NameValueVO p = new NameValueVO();
//		
//		TreeItem item = treeUserMode.getSelection()[0];
//		TypeItem tp = (TypeItem) item.getData("type");
//		
//		switch(tp) {
//			case array_value:
//				p.nameEnabled = Boolean.FALSE;
//				p.valueEnabled = Boolean.TRUE;
//				break;
//			case array:
//			case document:
//				p.nameEnabled = Boolean.TRUE;
//				p.valueEnabled = Boolean.FALSE;
//				break;
//			case editable:
//				p.nameEnabled = Boolean.TRUE;
//				p.valueEnabled = Boolean.TRUE;					
//				break;
//			case root_:
//				return;					
//		}				
//		
//		NameValueDlg dlg = new NameValueDlg(getShell());
//		
//		p.name = item.getText(0);
//		p.value = item.getText(1);			
//		
//		dlg.setParams(p);
//		if(dlg.open() <= 0) {
//			
//			DBObject obj = (DBObject) item.getData("obj");
//			DBObject pObj = (DBObject) item.getParentItem().getData("obj");
//			
//			switch(tp) {
//				case array_value:
//					obj.put(item.getText(0),  p.value !=null?p.value.toString():"null");
//					break;
//				case array:
//				case document: 
//					{
//						if(p.name != null) {
//							pObj.removeField(item.getText(0));
//							pObj.put(p.name,  obj);
//						}
//					}
//					break;
//				case editable:
//					{
//						if(p.name != null) {
//							Object v = obj.get(item.getText(0));
//							obj.removeField(item.getText(0));
//							obj.put(p.name,  v);
//							obj.put(p.name,  p.value!=null?p.value.toString():"null");
//						} else {
//							obj.put(item.getText(0),  p.value!=null?p.value.toString():"null");
//						}
//					}	
//					break;
//				default:
//					break;
//			}
//			
//			rootObj = (DBObject) treeUserMode.getItem(0).getData("obj");
//			
//			treeUserMode.setItemCount(0);
//			createItem(rootObj, treeUserMode);
//		}
//	}
//	
//	private void disableErrors(boolean t) {
//		buttonOk.setEnabled(t);
//		btnErrors.setEnabled(!t);
//	}
//}
