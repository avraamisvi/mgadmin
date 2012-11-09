package br.org.isvi.mgadmin.util.undoredo.operations;


public class DeleteTextCmd implements Command{
	
	Document doc;
	String text;
	int start;
	
	public DeleteTextCmd(Document doc, String text, int start) {
		this.doc = doc;
		this.text = text;
	}
	
	@Override
	public void execute() {
		System.out.println("======DELETE======");	
		System.out.println(" init:" + start + " text:" + text);
		doc.text.delete(start, start+text.length());
	}

	@Override
	public boolean isUndoable() {
		return true;
	}

	@Override
	public void undo() {

		System.out.println("======UNDO DELETE======");
		doc.text.insert(start, doc.text);
	}
}
