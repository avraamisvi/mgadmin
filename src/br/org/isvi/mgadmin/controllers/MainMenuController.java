package br.org.isvi.mgadmin.controllers;

import org.eclipse.swt.widgets.Event;

import br.org.isvi.mgadmin.MainWindow;
import br.org.isvi.mgadmin.util.SystemUtils;

public class MainMenuController {

	private MainWindow mainWindow;
	
	public MainMenuController(MainWindow mainWindow) {
		super();
		this.mainWindow = mainWindow;
	}

	public void filterShortCuts(Event e) {

		if(SystemUtils.isRedoKey(e)){
			mainWindow.documentOperations.redo();
			
			mainWindow.stlTxtQueryComposer.setText(mainWindow.documentOperations.getText());
			mainWindow.stlTxtQueryComposer.setCaretOffset(mainWindow.documentOperations.getCaret());			
		} else
		if(SystemUtils.isUndoKey(e)) {
			mainWindow.documentOperations.undo();
			
			mainWindow.stlTxtQueryComposer.setText(mainWindow.documentOperations.getText());
			mainWindow.stlTxtQueryComposer.setCaretOffset(mainWindow.documentOperations.getCaret());
		}
	}
}
