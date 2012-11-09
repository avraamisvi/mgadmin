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
		System.out.println("======INSERT======");
		
		for(CharItem chi : charsBuffer) {
			int c = getStart(chi.caret);
			doc.text.insert(c, chi.char_);
		}
		
	}

	@Override
	public boolean isUndoable() {
		return true;
	}

	@Override
	public void undo() {

		System.out.println("======UNDO======");
		
		int cont = 0;
		for(CharItem chi : charsBuffer) {
			int c = getStart(chi.caret-cont);
			doc.text.delete(c, c+1);
			this.doc.caret = c;
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
	
//	public String getText() {
//		StringBuilder bdl = new StringBuilder();
//		
//		Collections.sort(charsBuffer, new Comparator<CharItem>() {
//			@Override
//			public int compare(CharItem o1, CharItem o2) {
//				
//		       if (o1.caret < o2.caret) {
//		            return -1;
//		        }
//		       
//		        if (o1.caret > o2.caret) {
//		            return 1;
//		        }
//			     
//		        return 0;
//			}
//		});
//		
//		for(CharItem chi : charsBuffer) {
//			bdl.append(chi.c);
//		}
//	}
}
