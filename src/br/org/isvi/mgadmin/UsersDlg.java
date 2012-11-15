package br.org.isvi.mgadmin;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class UsersDlg extends TitleAreaDialog {

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public UsersDlg(Shell parentShell) {
		super(parentShell);
		setHelpAvailable(false);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage("Add or edit users");
		setTitleImage(SWTResourceManager.getImage(UsersDlg.class, "/br/org/isvi/mgadmin/images/users_48.png"));
		setTitle("Users management");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Tree tree = new Tree(container, SWT.BORDER);
		tree.setHeaderVisible(true);
		tree.setBounds(10, 10, 430, 434);
		
		TreeColumn trclmnUserName = new TreeColumn(tree, SWT.NONE);
		trclmnUserName.setWidth(277);
		trclmnUserName.setText("Username");
		
		TreeColumn trclmnReadOnly = new TreeColumn(tree, SWT.NONE);
		trclmnReadOnly.setWidth(135);
		trclmnReadOnly.setText("Read Only");
		
		Menu menu = new Menu(tree);
		tree.setMenu(menu);
		
		MenuItem mntmAddUser = new MenuItem(menu, SWT.NONE);
		mntmAddUser.setText("Add User");
		
		MenuItem mntmEditSelected = new MenuItem(menu, SWT.NONE);
		mntmEditSelected.setText("Edit Selected");
		
		MenuItem mntmRemoveSelected = new MenuItem(menu, SWT.NONE);
		mntmRemoveSelected.setText("Remove Selected");

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
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 584);
	}
}
