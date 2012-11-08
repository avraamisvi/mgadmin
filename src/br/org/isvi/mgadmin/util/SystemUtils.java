package br.org.isvi.mgadmin.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;

public class SystemUtils {

	public static boolean isUndoKey(Event e) {
		
		boolean ret = false;
		
		if (SWT.getPlatform().equals("cocoa")) {
			ret = (((e.stateMask & SWT.COMMAND) == SWT.COMMAND) && (e.keyCode == 'z')); 
		} else {
			ret = (((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == 'z'));
		}
		
		return ret;
	}
	
	public static boolean isRedoKey(Event e) {
		
		boolean ret = false;
		
		if (SWT.getPlatform().equals("cocoa")) {
			ret = (((e.stateMask & SWT.COMMAND) == SWT.COMMAND) && ((e.stateMask & SWT.SHIFT) == SWT.SHIFT) && (e.keyCode == 'z'));
		} else {
			ret = (((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == 'y'));
		}
		
		return ret;
	}

	public void getUndoKey() {

	}	
}
