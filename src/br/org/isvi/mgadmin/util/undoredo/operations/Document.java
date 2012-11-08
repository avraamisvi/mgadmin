package br.org.isvi.mgadmin.util.undoredo.operations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Document {

	StringBuilder text;
	CommandManager manager = new CommandManager();
	int caret = 0;
	
	List<CharItem> charsBuffer = new ArrayList<CharItem>();
	
	public static int MAX_BUFFER = 5;
	
	public Document(String text) {
		this.text = new StringBuilder(text);
	}
	
	public String getText() {
		return text.toString();
	}
	
	public void appendChar(char ch, int caret) {
		CharItem chi = new CharItem();
		chi.char_ = ch; 
		chi.caret = caret; 
		
		System.out.println("CH: " + ch + " car:" + caret);
		
		if(!charsBuffer.isEmpty()) {
			
			for(CharItem ct : charsBuffer) {
				if(ct.caret >= caret) {
					ct.caret++;
				}
			}
		}

		charsBuffer.add(chi);
		
		Collections.sort(charsBuffer, new Comparator<CharItem>() {
			@Override
			public int compare(CharItem o1, CharItem o2) {
				
		       if (o1.caret < o2.caret) {
		            return -1;
		        }
		       
		        if (o1.caret > o2.caret) {
		            return 1;
		        }
			     
		        return 0;
			}
		});		
		
		if(charsBuffer.size() >= MAX_BUFFER) {
			manager.execute(new AppendTextCmd(this, charsBuffer));
			charsBuffer = new ArrayList<CharItem>();
		}		
	}
	
	public void undo() {
		manager.undo();
	}
	
	public void redo() {
		manager.redo();
	}

	public int getCaret() {
		return caret;
	}
}
