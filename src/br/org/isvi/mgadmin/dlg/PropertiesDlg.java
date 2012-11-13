package br.org.isvi.mgadmin.dlg;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import br.org.isvi.mgadmin.model.PropertiesVO;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class PropertiesDlg extends Dialog {

	private PropertiesVO properties;
	private Button btnchkSaveSession;
	private Button btnInformOfUpdates;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public PropertiesDlg(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		
		Composite composite = new Composite(container, SWT.BORDER);
		GridData gd_composite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite.widthHint = 426;
		gd_composite.heightHint = 206;
		composite.setLayoutData(gd_composite);
		
		btnchkSaveSession = new Button(composite, SWT.CHECK);
		btnchkSaveSession.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				properties.setSaveSession(btnchkSaveSession.getSelection());
			}
		});
		btnchkSaveSession.setBounds(10, 37, 406, 18);
		btnchkSaveSession.setText("Save Session");
		
		btnInformOfUpdates = new Button(composite, SWT.CHECK);
		btnInformOfUpdates.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				properties.setSaveSession(btnInformOfUpdates.getSelection());
			}
		});
		btnInformOfUpdates.setBounds(10, 61, 406, 18);
		btnInformOfUpdates.setText("Inform of Updates");

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
		return new Point(450, 335);
	}

	public PropertiesVO getProperties() {
		return properties;
	}

	public void setProperties(PropertiesVO properties) {
		this.properties = properties;
	}
}
