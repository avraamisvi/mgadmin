package br.org.isvi.mgadmin.controllers;

import org.eclipse.swt.events.KeyEvent;

import br.org.isvi.mgadmin.MainWindow;
import br.org.isvi.mgadmin.util.SystemUtils;
import br.org.isvi.mgadmin.util.undoredo.operations.Document;

public class CommandEditorController {

	private MainWindow mainWindow;
	
	public CommandEditorController(MainWindow mainWindow) {
		super();
		this.mainWindow = mainWindow;
	}

	public void filterShortCuts(KeyEvent e) {

		if(SystemUtils.isRedoKey(e)){
			mainWindow.documentOperations.redo();
			
			mainWindow.stlTxtQueryComposer.setText(mainWindow.documentOperations.getText());
			mainWindow.stlTxtQueryComposer.setCaretOffset(mainWindow.documentOperations.getCaret());			
		} else
		if(SystemUtils.isUndoKey(e)) {
			mainWindow.documentOperations.undo();
			
			mainWindow.stlTxtQueryComposer.setText(mainWindow.documentOperations.getText());
			mainWindow.stlTxtQueryComposer.setCaretOffset(mainWindow.documentOperations.getCaret());
		} else if(SystemUtils.isDeleteKey(e)) {
			
			mainWindow.documentOperations.delete(mainWindow.stlTxtQueryComposer.getSelection().x, 
					mainWindow.stlTxtQueryComposer.getSelectionText());
		}
				
	}
	
	public void textModified(Document doc, String text, int caret) {
		if(caret-1 >= 0) {
			doc.appendChar(text.charAt(caret-1), caret);
		}
	}	
}
