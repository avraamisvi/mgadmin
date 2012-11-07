package br.org.isvi.mgadmin.util;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;

public class ErrorsDlg extends Dialog {

	String errorsText;
	private StyledText styledTextErrors;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ErrorsDlg(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.APPLICATION_MODAL);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		
		styledTextErrors = new StyledText(container, SWT.BORDER | SWT.READ_ONLY);
		styledTextErrors.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		getShell().addListener(SWT.Show, new Listener() {
			
			@Override
			public void handleEvent(Event e) {
				styledTextErrors.setText(errorsText);
			}
		});		
		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Close",
				true);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(498, 300);
	}

	public void setErrorsText(String errors) {
		errorsText = errors;
	}
}
