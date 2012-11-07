package br.org.isvi.mgadmin;

import java.util.HashMap;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.wb.swt.SWTResourceManager;

import br.org.isvi.mgadmin.util.ErrorsDlg;
import br.org.isvi.mgadmin.util.NameValueDlg;
import br.org.isvi.mgadmin.util.TreeUtil;
import br.org.isvi.mgadmin.util.TreeUtil.ConfigEditorConditional;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class DocumentDlg extends TitleAreaDialog {
	private HashMap<String, Object> params;
	private DBObject rootObj;
	private StyledText styledTextAdvanced;
	private Button buttonOk;
	private Tree treeUserMode;
    private ScriptEngineManager engineManager;
    private ScriptEngine engineJavascript;	
    private Button btnErrors;
    private String errors;
	
    enum TypeItem {
    	array, document, root_, editable, array_value
    }
    
    
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public DocumentDlg(Shell parentShell) {
		super(parentShell);
		setHelpAvailable(false);
		
		engineManager = new ScriptEngineManager();
	    engineJavascript = engineManager.getEngineByName("JavaScript");		
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage("Fill the fields and press ok to create a new Document");
		setTitleImage(SWTResourceManager.getImage(DocumentDlg.class, "/br/org/isvi/mgadmin/images/document_48.png"));
		setTitle("Creates a new Document");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		CTabFolder tabFolder = new CTabFolder(container, SWT.BORDER);
		tabFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {		
				
				if(rootObj != null)
					styledTextAdvanced.setText(rootObj.toString());
			}
		});
		tabFolder.setBounds(10, 10, 430, 332);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		CTabItem tbtmUserMode = new CTabItem(tabFolder, SWT.NONE);
		tbtmUserMode.setText("User mode");
		
		treeUserMode = new Tree(tabFolder, SWT.BORDER);
		treeUserMode.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
				HashMap<String, Object> p = new HashMap<String, Object>();
				
				TreeItem item = treeUserMode.getSelection()[0];
				TypeItem tp = (TypeItem) item.getData("type");
				
				switch(tp) {
					case array_value:
						p.put("name-enabled", Boolean.FALSE);
						p.put("value-enabled", Boolean.TRUE);
						break;
					case array:
					case document:
						p.put("name-enabled", Boolean.TRUE);
						p.put("value-enabled", Boolean.FALSE);
						break;
					case editable:
						p.put("name-enabled", Boolean.TRUE);
						p.put("value-enabled", Boolean.TRUE);					
						break;
					case root_:
						return;					
				}				
				
				NameValueDlg dlg = new NameValueDlg(getShell());
				
				p.put("name", item.getText(0));
				p.put("value", item.getText(1));			
				
				dlg.setParams(p);
				if(dlg.open() <= 0) {
					
					DBObject obj = (DBObject) item.getData("obj");
					DBObject pObj = (DBObject) item.getParentItem().getData("obj");
					
					switch(tp) {
						case array_value:
							obj.put(item.getText(0),  p.get("value")!=null?p.get("value").toString():"null");
							break;
						case array:
						case document: 
							{
								if(p.get("name") != null) {
									pObj.removeField(item.getText(0));
									pObj.put(p.get("name").toString(),  obj);
								}
							}
							break;
						case editable:
							{
								if(p.get("name") != null) {
									Object v = obj.get(item.getText(0));
									obj.removeField(item.getText(0));
									obj.put(p.get("name").toString(),  v);
									obj.put(p.get("name").toString(),  p.get("value")!=null?p.get("value").toString():"null");
								} else {
									obj.put(item.getText(0),  p.get("value")!=null?p.get("value").toString():"null");
								}
							}	
							break;
						default:
							break;
					}
					
					rootObj = (DBObject) treeUserMode.getItem(0).getData("obj");
					
					treeUserMode.setItemCount(0);
					createItem(rootObj, treeUserMode);
				}
			}
		});
		treeUserMode.setLinesVisible(true);
		treeUserMode.setHeaderVisible(true);
		tbtmUserMode.setControl(treeUserMode);
		
		TreeColumn treeColumn = new TreeColumn(treeUserMode, SWT.NONE);
		treeColumn.setWidth(100);
		treeColumn.setText("Field");
		
		TreeColumn treeColumn_1 = new TreeColumn(treeUserMode, SWT.NONE);
		treeColumn_1.setWidth(300);
		treeColumn_1.setText("Value");
		
		Menu menuUserMode = new Menu(treeUserMode);
		treeUserMode.setMenu(menuUserMode);
		
		MenuItem mnItAddDocument = new MenuItem(menuUserMode, SWT.NONE);
		mnItAddDocument.setText("Add Document");
		
		MenuItem mntmAddArray = new MenuItem(menuUserMode, SWT.NONE);
		mntmAddArray.setText("Add Array");
		
		MenuItem mntmAddValue = new MenuItem(menuUserMode, SWT.NONE);
		mntmAddValue.setText("Add Value");
		
		MenuItem userModeMenuSep = new MenuItem(menuUserMode, SWT.SEPARATOR);
		
		MenuItem mntmDelete = new MenuItem(menuUserMode, SWT.NONE);
		mntmDelete.setText("Delete");
		
		CTabItem tbtmAdvancedMode = new CTabItem(tabFolder, SWT.NONE);
		tbtmAdvancedMode.setText("Advanced mode");
		
		styledTextAdvanced = new StyledText(tabFolder, SWT.BORDER);
		styledTextAdvanced.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				try {
					engineJavascript.put(engineJavascript.FILENAME, "Document");
					engineJavascript.eval("doc="+styledTextAdvanced.getText());
					
					rootObj = (DBObject) JSON.parse(styledTextAdvanced.getText());
					
					params.put("dbobj", rootObj);
					
					treeUserMode.setItemCount(0);
					createItem(rootObj, treeUserMode);
					disableErrors(true);			
				} 
				catch(ScriptException e) {
					//System.out.println(e.getMessage());
					errors = e.getMessage();
					disableErrors(false);
				} catch(Exception e) {
					System.out.println(e.getMessage());
					errors = "Unknown Error!";
				}
			}
		});
		tbtmAdvancedMode.setControl(styledTextAdvanced);
		
		btnErrors = new Button(container, SWT.FLAT);
		btnErrors.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ErrorsDlg errorsDlg = new ErrorsDlg(getShell());
				errorsDlg.setErrorsText(errors);
				errorsDlg.open();
			}
		});
		btnErrors.setEnabled(false);
		btnErrors.setToolTipText("Errors");
		btnErrors.setImage(SWTResourceManager.getImage(DocumentDlg.class, "/br/org/isvi/mgadmin/images/document_error_16_ativado.png"));
		btnErrors.setBounds(10, 348, 39, 37);

		getShell().addListener(SWT.Show, new Listener() {
			
			@Override
			public void handleEvent(Event e) {
				configDialog();
			}
		});
		
		return area;
	}

	
	
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		buttonOk = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		buttonOk.setEnabled(false);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 551);
	}
	
	private void configDialog() {
		if(rootObj != null) {
			createItem(rootObj, treeUserMode);
			styledTextAdvanced.setText(rootObj.toString());
		}
		
		
	}
	
	public void setParams(HashMap<String, Object> params) {
		rootObj = (DBObject) params.get("dbobj");
		
		this.params = params;
	}	
	
	private void createItem(DBObject obj, Tree tree) {		
		TreeItem item = new TreeItem (tree, SWT.NONE);
		item.setText (0, "Document");
		item.setText (1, obj.toString());
		item.setData ("obj", obj);
		item.setData("type", TypeItem.root_);
		
		for(String k : obj.keySet()) {
			
			TreeItem fld = new TreeItem (item, SWT.NONE);
			
			fld.setText (0, k);
			
			Object value = obj.get(k);
			
			if(value instanceof DBObject) {
				DBObject val = (DBObject) value;
				fld.setText (1, val.toString());
				createItem(val, fld);
			} else {
				fld.setText (1, value.toString());
				fld.setData("type", TypeItem.editable);
				fld.setData ("obj", obj);
			}
		}
	}
	
	private void createItem(DBObject obj, TreeItem par) {
		TreeItem item = new TreeItem (par, SWT.NONE);
		par.setData ("obj", obj);
		item.setData ("obj", obj);
		
		if(obj instanceof BasicDBList) {
			item.setText (0, "Array");
			par.setData("type", TypeItem.array);
			item.setData("type", TypeItem.root_);
		} else {
			item.setText (0, "Document");
			par.setData("type", TypeItem.document);
			item.setData("type", TypeItem.root_);
		}
		
		item.setText (1, obj.toString());
		
		for(String k : obj.keySet()) {
			
			TreeItem fld = new TreeItem (item, SWT.NONE);
			
			fld.setText (0, k);
			
			Object value = obj.get(k);
			
			if(value instanceof DBObject) {
				DBObject val = (DBObject) value;
				createItem(val, fld);
			} else {
				fld.setData("type", TypeItem.array_value);
				fld.setText (1, value.toString());
				fld.setData ("obj", obj);
			}
		}
	}
	
	private void disableErrors(boolean t) {
		buttonOk.setEnabled(t);
		btnErrors.setEnabled(!t);
	}
}
