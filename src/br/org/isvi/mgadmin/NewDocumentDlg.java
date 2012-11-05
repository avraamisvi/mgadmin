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

public class NewDocumentDlg extends TitleAreaDialog {

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public NewDocumentDlg(Shell parentShell) {
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
		setTitleImage(SWTResourceManager.getImage(NewDocumentDlg.class, "/br/org/isvi/mgadmin/images/document_48.png"));
		setTitle("Creates a new Document");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Tree tree = new Tree(container, SWT.BORDER);
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		tree.setBounds(10, 10, 430, 339);
		
		TreeColumn trclmnField = new TreeColumn(tree, SWT.NONE);
		trclmnField.setWidth(100);
		trclmnField.setText("Field");
		
		TreeColumn trclmnValue = new TreeColumn(tree, SWT.NONE);
		trclmnValue.setWidth(300);
		trclmnValue.setText("Value");

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
		return new Point(450, 489);
	}
}
