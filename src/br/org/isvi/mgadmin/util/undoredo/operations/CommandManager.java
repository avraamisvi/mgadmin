package br.org.isvi.mgadmin.util.undoredo.operations;

import java.util.Stack;

public class CommandManager {
	Stack<Command> undoStack = new Stack<Command>();
	Stack<Command> redoStack = new Stack<Command>();
	boolean canRedo = false;
	
	int limiteStack = 100;
	
	public void execute(Command cmd) {
		
		if(cmd.isUndoable()) {
			cmd.execute();
			verifyLimite(undoStack);
			undoStack.push(cmd);
			canRedo = false;
		}
	}
	
	public void undo() {
		try {
			Command cmd = undoStack.pop();
			cmd.undo();
			
			verifyLimite(redoStack);
			
			redoStack.push(cmd);
			
			canRedo = true;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redo() {
		try {
			if(canRedo) {
				Command cmd = redoStack.pop();
				cmd.execute();
		
				verifyLimite(undoStack);
				
				undoStack.push(cmd);
				
			}
		}catch(Exception e) {
		e.printStackTrace();	
		}
	}

	private void verifyLimite(Stack<Command> st) {
		if(st.size() >= limiteStack) {
			st.pop();
		}
	}
}
