package br.org.isvi.mgadmin.util.undoredo.operations;

public class CommandsToolController {
	
	public void textModified(Document doc, String text, int caret) {
		if(caret-1 >= 0) {
			doc.appendChar(text.charAt(caret-1), caret);
		}
	}
	
}