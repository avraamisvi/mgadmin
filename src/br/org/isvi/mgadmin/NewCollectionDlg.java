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
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class NewCollectionDlg extends TitleAreaDialog {
	private Text txtName;
	private HashMap<String, Object> params;
	private Spinner spinnerSize;
	private Spinner spinnerMax;
	private Button btnIsCapped;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public NewCollectionDlg(Shell parentShell) {
		super(parentShell);
		setHelpAvailable(false);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		parent.setToolTipText("");
		setMessage("Fill the fields and press ok to create a new collection.");
		setTitleImage(SWTResourceManager.getImage(NewCollectionDlg.class, "/br/org/isvi/mgadmin/images/collection_48.png"));
		setTitle("Creates a New Collection");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblName = new Label(container, SWT.NONE);
		lblName.setBounds(10, 10, 59, 14);
		lblName.setText("Name:");
		
		txtName = new Text(container, SWT.BORDER);
		txtName.setToolTipText("Name of the collection being created");
		txtName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				params.put("name", txtName.getText());
			}
		});
		txtName.setBounds(75, 8, 365, 19);
		
		spinnerSize = new Spinner(container, SWT.BORDER);
		spinnerSize.setToolTipText("Collection size (in bytes)");
		spinnerSize.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				params.put("size", spinnerSize.getText());
			}
		});
		spinnerSize.setBounds(75, 33, 95, 22);
		
		Label lblSize = new Label(container, SWT.NONE);
		lblSize.setBounds(10, 37, 59, 14);
		lblSize.setText("Size:");
		
		Label lblMax = new Label(container, SWT.NONE);
		lblMax.setBounds(10, 61, 59, 14);
		lblMax.setText("Max:");
		
		spinnerMax = new Spinner(container, SWT.BORDER);
		spinnerMax.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				params.put("max", spinnerMax.getText());
			}
		});
		spinnerMax.setToolTipText("Max number of documents");
		spinnerMax.setBounds(75, 61, 95, 22);
		
		btnIsCapped = new Button(container, SWT.CHECK);
		btnIsCapped.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				params.put("capped", btnIsCapped.getSelection());
			}
		});
		btnIsCapped.setToolTipText("If the collection is capped");
		btnIsCapped.setBounds(10, 89, 160, 18);
		btnIsCapped.setText("Is Capped");

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
		return new Point(450, 300);
	}
	
	public void setParams(HashMap<String, Object> params) {
		this.params = params;
		params.put("size", "0");
		params.put("max", "0");
		params.put("capped", false);		
	}	
}
