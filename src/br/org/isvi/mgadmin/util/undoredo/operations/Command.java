package br.org.isvi.mgadmin.util.undoredo.operations;

public interface Command {
	void execute();
	boolean isUndoable();
	void undo();
}
