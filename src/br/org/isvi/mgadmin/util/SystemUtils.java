package br.org.isvi.mgadmin.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;

public class SystemUtils {

	public static boolean isUndoKey(KeyEvent e) {
		
		boolean ret = false;
		
		if (SWT.getPlatform().equals("cocoa")) {
			ret = (((e.stateMask & SWT.COMMAND) == SWT.COMMAND) && (e.keyCode == 'z')); 
		} else {
			ret = (((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == 'z'));
		}
		
		return ret;
	}
	
	public static boolean isRedoKey(KeyEvent e) {
		
		boolean ret = false;
		
		if (SWT.getPlatform().equals("cocoa")) {
			ret = (((e.stateMask & SWT.COMMAND) == SWT.COMMAND) && ((e.stateMask & SWT.SHIFT) == SWT.SHIFT) && (e.keyCode == 'z'));
		} else {
			ret = (((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == 'y'));
		}
		
		return ret;
	}
	
	public static boolean isDeleteKey(KeyEvent e) {
//		System.out.println("DEL " + e.keyCode + " == "+ (int)SWT.DEL);
//		System.out.println("BS " + e.keyCode + " == "+ (int)SWT.BS);
		return (e.keyCode == SWT.DEL || e.keyCode == SWT.BS);
	}

	public void getUndoKey() {

	}	
}
