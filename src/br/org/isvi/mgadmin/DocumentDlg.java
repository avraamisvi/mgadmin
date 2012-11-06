package br.org.isvi.mgadmin;

import java.util.HashMap;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class DocumentDlg extends TitleAreaDialog {
	private HashMap<String, Object> params;
	private DBObject obj;
	private StyledText styledTextAdvanced;
	private Button buttonOk;
	private Tree treeUserMode;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public DocumentDlg(Shell parentShell) {
		super(parentShell);
		setHelpAvailable(false);
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
				
//				System.out.println(e.widget); TODO AKI
				
				if(obj != null)
					styledTextAdvanced.setText(obj.toString());
			}
		});
		tabFolder.setBounds(10, 10, 430, 339);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		CTabItem tbtmUserMode = new CTabItem(tabFolder, SWT.NONE);
		tbtmUserMode.setText("User mode");
		
		treeUserMode = new Tree(tabFolder, SWT.BORDER);
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
					obj = (DBObject) JSON.parse(styledTextAdvanced.getText());
					
					params.put("dbobj", obj);
					
					buttonOk.setEnabled(true);					
				} catch(Exception e) {
					System.out.println(e.getMessage());
					buttonOk.setEnabled(false);
				}
			}
		});
		tbtmAdvancedMode.setControl(styledTextAdvanced);

		this.configTree();
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
		return new Point(450, 489);
	}
	
	private void configTree() {
		if(obj != null) {
			createItem(obj, treeUserMode);			
		}		
	}
	
	public void setParams(HashMap<String, Object> params) {
		obj = (DBObject) params.get("dbobj");
		
		this.params = params;
	}	
	
	private void createItem(DBObject obj, Tree tree) {		
		TreeItem item = new TreeItem (tree, SWT.NONE);
		item.setText (0, "Document");
		item.setText (1, obj.toString());
		
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
			}
		}
	}
	
	private void createItem(DBObject obj, TreeItem par) {
		TreeItem item = new TreeItem (par, SWT.NONE);
		
		if(obj instanceof BasicDBList)
			item.setText (0, "Array");
		else
			item.setText (0, "Document");
		
		item.setText (1, obj.toString());
		
		for(String k : obj.keySet()) {
			
			TreeItem fld = new TreeItem (item, SWT.NONE);
			
			fld.setText (0, k);
			
			Object value = obj.get(k);
			
			if(value instanceof DBObject) {
				DBObject val = (DBObject) value;
				createItem(val, fld);
			} else {
				fld.setText (1, value.toString());
			}
		}
	}	
}
