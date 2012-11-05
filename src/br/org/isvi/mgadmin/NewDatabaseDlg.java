package br.org.isvi.mgadmin;

import java.util.HashMap;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.custom.CTabItem;

public class NewDatabaseDlg extends TitleAreaDialog {
	
	private HashMap<String, Object> params;
	private Text textName;
	private Text textOwner;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public NewDatabaseDlg(Shell parentShell) {
		super(parentShell);
		setHelpAvailable(false);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitleImage(SWTResourceManager.getImage(NewDatabaseDlg.class, "/br/org/isvi/mgadmin/images/database_48.png"));
		setMessage("Fill the fields and press ok to create a new database");
		setTitle("Creates a New Database");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		CTabFolder tabFolder = new CTabFolder(container, SWT.BORDER);
		tabFolder.setBorderVisible(false);
		tabFolder.setBounds(10, 10, 430, 427);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		CTabItem tabProperties = new CTabItem(tabFolder, SWT.NONE);
		tabProperties.setText("Properties");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tabProperties.setControl(composite);
		composite.setLayout(new FormLayout());
		
		Label label = new Label(composite, SWT.NONE);
		label.setText("Name");
		FormData fd_label = new FormData();
		label.setLayoutData(fd_label);
		
		textName = new Text(composite, SWT.BORDER);
		textName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				params.put("dbname", textName.getText());
			}
		});
		fd_label.top = new FormAttachment(textName, 3, SWT.TOP);
		fd_label.right = new FormAttachment(textName, -6);
		FormData fd_textName = new FormData();
		fd_textName.top = new FormAttachment(0, 10);
		fd_textName.right = new FormAttachment(100, -26);
		fd_textName.left = new FormAttachment(0, 51);
		textName.setLayoutData(fd_textName);
		
		textOwner = new Text(composite, SWT.BORDER);
		FormData fd_textOwner = new FormData();
		fd_textOwner.top = new FormAttachment(textName, 6);
		fd_textOwner.right = new FormAttachment(100, -26);
		fd_textOwner.left = new FormAttachment(textName, 0, SWT.LEFT);
		textOwner.setLayoutData(fd_textOwner);
		
		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setText("Owner");
		FormData fd_label_1 = new FormData();
		fd_label_1.top = new FormAttachment(label, 11);
		fd_label_1.left = new FormAttachment(label, 0, SWT.LEFT);
		label_1.setLayoutData(fd_label_1);
		
		CTabItem tabItemPrivileges = new CTabItem(tabFolder, SWT.NONE);
		tabItemPrivileges.setText("Privileges");
		
		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tabItemPrivileges.setControl(composite_1);

		return area;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 589);
	}
	
	public void setParams(HashMap<String, Object> params) {
		this.params = params;
	}	
}
