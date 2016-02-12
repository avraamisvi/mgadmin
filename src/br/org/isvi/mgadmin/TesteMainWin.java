//package br.org.isvi.mgadmin;
//
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.graphics.Point;
//import org.eclipse.swt.graphics.Rectangle;
//import org.eclipse.swt.widgets.Display;
//import org.eclipse.swt.widgets.Event;
//import org.eclipse.swt.widgets.Listener;
//import org.eclipse.swt.widgets.Menu;
//import org.eclipse.swt.widgets.MenuItem;
//import org.eclipse.swt.widgets.Shell;
//import org.eclipse.swt.widgets.ToolBar;
//import org.eclipse.swt.widgets.ToolItem;
//
//public class TesteMainWin {
//
//	protected Shell shell;
//
//	/**
//	 * Launch the application.
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		try {
//			TesteMainWin window = new TesteMainWin();
//			window.open();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Open the window.
//	 */
//	public void open() {
//		Display display = Display.getDefault();
//		createContents();
//		shell.open();
//		shell.layout();
//		while (!shell.isDisposed()) {
//			if (!display.readAndDispatch()) {
//				display.sleep();
//			}
//		}
//	}
//
//	/**
//	 * Create contents of the window.
//	 */
//	protected void createContents() {
//		shell = new Shell();
//		shell.setSize(450, 300);
//		shell.setText("SWT Application");
//		
//		final ToolBar toolBar = new ToolBar(shell, SWT.FLAT | SWT.RIGHT);
//		toolBar.setBounds(0, 0, 450, 21);
//		
//		final ToolItem tltmTeste = new ToolItem(toolBar, SWT.DROP_DOWN);
//		tltmTeste.setText("teste");
//		
//		final Menu menu = new Menu (shell, SWT.POP_UP);
//		for (int i=0; i<8; i++) {
//			MenuItem item = new MenuItem (menu, SWT.PUSH);
//			item.setText ("Item " + i);
//		}
//
//		tltmTeste.addListener (SWT.Selection, new Listener () {
//			public void handleEvent (Event event) {
//				if (event.detail == SWT.ARROW) {
//					Rectangle rect = tltmTeste.getBounds ();
//					Point pt = new Point (rect.x, rect.y + rect.height);
//					pt = toolBar.toDisplay (pt);
//					menu.setLocation (pt.x, pt.y);
//					menu.setVisible (true);
//				}
//			}
//		});
//	}
//}
