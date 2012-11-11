package br.org.isvi.mgadmin.util.undoredo.operations;

import java.util.ArrayList;
import java.util.List;

public class AppendTextCmd implements Command{
	
	Document doc;
	List<CharItem> charsBuffer = new ArrayList<CharItem>();
	
	public AppendTextCmd(Document doc, List<CharItem> charsBuffer) {
		this.doc = doc;
		this.charsBuffer = charsBuffer;
	}
	
	@Override
	public void execute() {
		System.out.println("AppendTextCmd:======INSERT======");
		int c = 0;
				
		for(CharItem chi : charsBuffer) {
			c = getStart(chi.caret);
			doc.text.insert(c, chi.char_);
		}
		
	}

	@Override
	public boolean isUndoable() {
		return true;
	}

	@Override
	public void undo() {

		System.out.println("AppendTextCmd:======UNDO======");
		
		int cont = 0;
		for(CharItem chi : charsBuffer) {
			int c = getStart(chi.caret-cont);
			doc.text.delete(c, c+1);
			this.doc.setCaret(c);
			cont++;
			System.out.println("CH: " + chi.char_ + " car:" + c + " cont:" + cont);
		}
	}
	
	public int getStart(int caret) {
		int pos = caret-1;
		String doctxt = doc.getText();
		
		if(pos < 0) {pos = 0;}
		else
		if(pos > 0 && pos > doctxt.length()) {pos=doctxt.length();} 
		
		return pos;
	}
}
