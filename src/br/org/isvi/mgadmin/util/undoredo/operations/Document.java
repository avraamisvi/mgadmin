package br.org.isvi.mgadmin.util.undoredo.operations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Document {

	public boolean undoRedo = false;
	private int caret = 0;
	
	StringBuilder text;
	CommandManager manager = new CommandManager();
	
	List<CharItem> charsBuffer = new ArrayList<CharItem>();
	
	public static int MAX_BUFFER = 5;
	
	public Document(String text) {
		this.text = new StringBuilder(text);
	}
	
	public String getText() {
		return text.toString();
	}
	
	public void delete(int start, int end) {
		
		if(charsBuffer.size() > 0) {
			manager.execute(new AppendTextCmd(this, charsBuffer));
			charsBuffer = new ArrayList<CharItem>();
		}		
		
		System.out.println("delete: " + start + " text:" + text.substring(start, end));
		manager.execute(new DeleteTextCmd(this, text.substring(start, end), start));
	}

	public void delete(int start) {
		
		if(charsBuffer.size() > 0) {
			manager.execute(new AppendTextCmd(this, charsBuffer));
			charsBuffer = new ArrayList<CharItem>();
		}		
		
		String char_ = ""+text.charAt(start);
		
		manager.execute(new DeleteTextCmd(this, char_, start));
		System.out.println("delete: " + start + " text:" + char_);
	}	
	
	public void appendChar(char ch, int caret) {
		
		if(Character.isSupplementaryCodePoint(ch) || Character.isIdentifierIgnorable(ch) || undoRedo)
			return;
		
		CharItem chi = new CharItem();
		chi.char_ = ch; 
		chi.caret = caret; 
		
		System.out.println("appendChar: code:" + (int)ch + " car:" + caret + " char:" + ch);
		
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
		
		if(charsBuffer.size() > 0) {
			manager.execute(new AppendTextCmd(this, charsBuffer));
			charsBuffer = new ArrayList<CharItem>();
		}
		
		this.undoRedo = true;
		manager.undo();	
	}
	
	public void redo() {
		this.undoRedo = true;
		manager.redo();
	}
	
	public int getCaret() {
		return caret;
	}
	
	public void setCaret(int c) {
		caret = c;
	}
}
