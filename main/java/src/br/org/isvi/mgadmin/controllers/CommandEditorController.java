package br.org.isvi.mgadmin.controllers;

import java.util.Stack;

import br.org.isvi.mgadmin.RemoteGui;

public class CommandEditorController {

	private RemoteGui mainWindow;
//	private Stack<State> undoStack = new Stack<CommandEditorController.State>(); 
//	private Stack<State> redoStack = new Stack<CommandEditorController.State>();
//	private boolean undoRedo = false;
//	private int limiteStack = 500; 
//	private State stateOld;
	
	public CommandEditorController(RemoteGui mainWindow) {
		super();
		this.mainWindow = mainWindow;
	}

//	public void saveState(StyledText text) {
//		if(undoRedo) {
//			undoRedo = false;
//			return;
//		}
//		
//		redoStack = new Stack<CommandEditorController.State>();
//		
//		State st = new State();
//		st.text = text.getText();
//		st.caret = text.getCaretOffset();
//		
//		if(stateOld != null)
//			st.dif = Math.abs(stateOld.text.length()-st.text.length());
//		else
//			st.dif = st.text.length();
//			
//		stateOld = st;
//		
//		verifyLimite(undoStack);
//		
//		undoStack.add(st);
//	}
//	
//	public void filterShortCuts(KeyEvent e) {
//		
//		/*if(SystemUtils.isRedoKey(e)) {
//			redo();
//		} 
//		else if(SystemUtils.isUndoKey(e)) {
//			undo();
//		}*/		
//	}
//	
//	private void applyStyle() {
//		
//	}
//	
//	private void verifyLimite(Stack<State> st) {
//		if(st.size() >= limiteStack) {
//			st.pop();
//		}
//	}	
//	
//	public void undo() {
//		try {
//			
//			if(undoStack.size() > 0) {
//				undoRedo = true;
//				
//				State st = null;
//				
//				int cnt = 0;
//				while(undoStack.size() > 0) {
//					if(cnt > 5)
//						break;
//					
//					st = undoStack.pop();
//					redoStack.push(st);
//					cnt += st.dif;
//				}
//				
////				mainWindow.stlTxtQueryComposer.setText(st.text);
////				mainWindow.stlTxtQueryComposer.setCaretOffset(st.caret);
//
//			}
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void redo() {
//		try {
//				if(redoStack.size() > 0) {
//					undoRedo = true;
//					State st = null;
//					int cnt = 0;
//					
//					while(redoStack.size() > 0) {
//						if(cnt > 5)
//							break;
//						
//						st = redoStack.pop();
//						undoStack.push(st);
//						cnt += st.dif;
//					}					
//					
////					mainWindow.stlTxtQueryComposer.setText(st.text);
////					mainWindow.stlTxtQueryComposer.setCaretOffset(st.caret);					
//				}
//		}catch(Exception e) {
//		e.printStackTrace();	
//		}
//	}	
//	
//	class State {
//		int caret;
//		String text;
//		int dif = 0;
//	}
		
}
