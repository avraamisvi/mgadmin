package br.org.isvi.mgadmin;

import java.util.HashMap;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

public class NewDatabaseOldDlg extends Dialog {
	private Text textDbName;
	private HashMap<String, Object> params;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public NewDatabaseOldDlg(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.TITLE | SWT.APPLICATION_MODAL);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 2;
		new Label(container, SWT.NONE);
		
		Label lblDatabaseName = new Label(container, SWT.NONE);
		lblDatabaseName.setText("Database Name");
		new Label(container, SWT.NONE);
		
		textDbName = new Text(container, SWT.BORDER);
		textDbName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				params.put("dbname", textDbName.getText());
			}
		});
		textDbName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		return container;
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
		return new Point(450, 184);
	}

	public void setParams(HashMap<String, Object> params) {
		this.params = params;
	}
}
