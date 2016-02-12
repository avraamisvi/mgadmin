package br.org.isvi.mgadmin.dlg;

import java.util.HashMap;

//import org.eclipse.jface.dialogs.Dialog;
//import org.eclipse.jface.dialogs.IDialogConstants;
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.graphics.Point;
//import org.eclipse.swt.layout.GridData;
//import org.eclipse.swt.layout.GridLayout;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Control;
//import org.eclipse.swt.widgets.Event;
//import org.eclipse.swt.widgets.Label;
//import org.eclipse.swt.widgets.Listener;
//import org.eclipse.swt.widgets.Shell;
//import org.eclipse.swt.widgets.Text;
//import org.eclipse.swt.events.ModifyListener;
//import org.eclipse.swt.events.ModifyEvent;

import br.org.isvi.mgadmin.model.NameValueVO;
@Deprecated
public class NameValueDlg {
//	extends Dialog {

//	private Text textName;
//	private Text textValue;
//	private NameValueVO params = new NameValueVO();
//
//	/**
//	 * Create the dialog.
//	 * @param parentShell
//	 */
//	public NameValueDlg(Shell parentShell) {
//		super(parentShell);
//		setShellStyle(SWT.APPLICATION_MODAL);
//	}
//
//	/**
//	 * Create contents of the dialog.
//	 * @param parent
//	 */
//	@Override
//	protected Control createDialogArea(Composite parent) {
//		Composite container = (Composite) super.createDialogArea(parent);
//		GridLayout gridLayout = (GridLayout) container.getLayout();
//		gridLayout.numColumns = 2;
//		new Label(container, SWT.NONE);
//		new Label(container, SWT.NONE);
//		
//		Label lblName = new Label(container, SWT.NONE);
//		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
//		lblName.setText("Name");
//		
//		textName = new Text(container, SWT.BORDER);
//		textName.addModifyListener(new ModifyListener() {
//			public void modifyText(ModifyEvent arg0) {
//				params.name = textName.getText();
//			}
//		});
//		textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		
//		Label lblValue = new Label(container, SWT.NONE);
//		lblValue.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
//		lblValue.setText("Value");
//		
//		textValue = new Text(container, SWT.BORDER);
//		textValue.addModifyListener(new ModifyListener() {
//			public void modifyText(ModifyEvent arg0) {
//				params.value = textValue.getText();
//			}
//		});
//		textValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//
//		getShell().addListener(SWT.Show, new Listener() {
//			
//			@Override
//			public void handleEvent(Event e) {
//				try {
//					String name = params.name;
//					String val = params.value.toString();
//					
//					boolean n = params.nameEnabled;
//					boolean v = params.valueEnabled;
//					
//					textName.setText(name);
//					textName.setEnabled(n);
//					
//					textValue.setText(val);
//					textValue.setEnabled(v);
//					
//				} catch(Exception ex) {
////					ex.printStackTrace();
//				}
//			}
//		});
//
//		return container;
//	}
//
//	/**
//	 * Create contents of the button bar.
//	 * @param parent
//	 */
//	@Override
//	protected void createButtonsForButtonBar(Composite parent) {
//		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
//				true);
//		createButton(parent, IDialogConstants.CANCEL_ID,
//				IDialogConstants.CANCEL_LABEL, false);
//	}
//
//	/**
//	 * Return the initial size of the dialog.
//	 */
//	@Override
//	protected Point getInitialSize() {
//		return new Point(450, 177);
//	}
//
//	public void setParams(NameValueVO params) {
//		this.params = params;
//	}
}
