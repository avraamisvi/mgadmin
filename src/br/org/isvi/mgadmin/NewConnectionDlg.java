//package br.org.isvi.mgadmin;
//
//import java.util.HashMap;
//
//import org.eclipse.jface.dialogs.IDialogConstants;
//import org.eclipse.jface.dialogs.TitleAreaDialog;
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.events.ModifyEvent;
//import org.eclipse.swt.events.ModifyListener;
//import org.eclipse.swt.graphics.Point;
//import org.eclipse.swt.layout.GridData;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Control;
//import org.eclipse.swt.widgets.Display;
//import org.eclipse.swt.widgets.Label;
//import org.eclipse.swt.widgets.Shell;
//import org.eclipse.swt.widgets.Text;
//import org.eclipse.ui.forms.widgets.FormToolkit;
//import org.eclipse.wb.swt.SWTResourceManager;
//import org.eclipse.swt.events.FocusAdapter;
//import org.eclipse.swt.events.FocusEvent;
//
//public class NewConnectionDlg extends TitleAreaDialog {
//	private Text textUser;
//	private Text textPass;
//	private Text textHost;
//	private Text textName;
//	private HashMap<String, Object> params;
//	private Text textPort;
//	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
////	public Port port = new Port();
//	
//	/**
//	 * Create the dialog.
//	 * @param parentShell
//	 */
//	public NewConnectionDlg(Shell parentShell) {
//		super(parentShell);
//		setHelpAvailable(false);
//	}
//
//	/**
//	 * Create contents of the dialog.
//	 * @param parent
//	 */
//	@Override
//	protected Control createDialogArea(Composite parent) {
//		setMessage("Fill the fields and press ok to create a new connection.");
//		setTitleImage(SWTResourceManager.getImage(NewConnectionDlg.class, "/br/org/isvi/mgadmin/images/connect-icon.png"));
//		setTitle("Creates a New Connection");
//		Composite area = (Composite) super.createDialogArea(parent);
//		Composite container = new Composite(area, SWT.NONE);
//		container.setLayoutData(new GridData(GridData.FILL_BOTH));
//		
//		Label lblName = new Label(container, SWT.NONE);
//		lblName.setBounds(10, 12, 59, 14);
//		lblName.setText("Name");
//		
//		textName = new Text(container, SWT.BORDER);
//		textName.addModifyListener(new ModifyListener() {
//			public void modifyText(ModifyEvent arg0) {
//				params.put("name", textName.getText());
//			}
//		});
//		textName.setToolTipText("Name of connection");
//		textName.setBounds(75, 10, 354, 19);
//		
//		Label lblHost = new Label(container, SWT.NONE);
//		lblHost.setBounds(10, 38, 59, 14);
//		lblHost.setText("Host");
//		
//		textHost = new Text(container, SWT.BORDER);
//		textHost.addModifyListener(new ModifyListener() {
//			public void modifyText(ModifyEvent arg0) {
//				params.put("host", textHost.getText());
//			}
//		});
//		textHost.setToolTipText("Host address");
//		textHost.setBounds(75, 35, 354, 19);
//		
//		Label lblUsername = new Label(container, SWT.NONE);
//		lblUsername.setBounds(10, 89, 59, 14);
//		lblUsername.setText("Username");
//		
//		textUser = new Text(container, SWT.BORDER);
//		textUser.addModifyListener(new ModifyListener() {
//			public void modifyText(ModifyEvent arg0) {
//				params.put("user", textUser.getText());
//			}
//		});
//		textUser.setToolTipText("Default user name");
//		textUser.setBounds(75, 86, 354, 19);
//		
//		textPass = new Text(container, SWT.BORDER | SWT.PASSWORD);
//		textPass.addModifyListener(new ModifyListener() {
//			public void modifyText(ModifyEvent arg0) {
//				params.put("pass", textPass.getText());
//			}
//		});
//		textPass.setToolTipText("Password");
//		textPass.setBounds(75, 111, 287, 19);
//		
//		Label lblPassword = new Label(container, SWT.NONE);
//		lblPassword.setBounds(10, 116, 59, 14);
//		lblPassword.setText("Password");
//		
//		textPort = new Text(container, SWT.BORDER);
//		textPort.addFocusListener(new FocusAdapter() {
//			@Override
//			public void focusLost(FocusEvent e) {
//				if(textPort.getText() == null || textPort.getText().isEmpty()) {
//					params.put("port", "27017");
//					textPort.setText("27017");					
//				}
//			}
//		});
//		textPort.setText("27017");
//		textPort.addModifyListener(new ModifyListener() {
//			public void modifyText(ModifyEvent arg0) {
//				
//				if(textPort.getText() == null || textPort.getText().isEmpty())
//					return;
//				
//				try {
//					Integer.parseInt(textPort.getText());
//					params.put("port", textPort.getText());
//				} catch (NumberFormatException e) {
//					params.put("port", "27017");
//					textPort.setText("27017");
//				}
//			}
//		});
//		textPort.setBounds(75, 61, 153, 19);
//		
//		Label lblPort = new Label(container, SWT.NONE);
//		lblPort.setBounds(10, 66, 59, 14);
//		lblPort.setText("Port");
//
//		return area;
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
//		return new Point(450, 301);
//	}
//
//	public void setParams(HashMap<String, Object> params) {
//		this.params = params;
//		params.put("port", "27017");
//	}
//	
////	class Port {
////		public int port;
////
////		public int getPort() {
////			return port;
////		}
////
////		public void setPort(int port) {
////			this.port = port;
////		}
////	}
//
//}
