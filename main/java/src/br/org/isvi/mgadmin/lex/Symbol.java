package br.org.isvi.mgadmin.lex;

public class Symbol {

	public SymbolType type;
	public String value;
	public int line;
	public int col;
	
	public Symbol(SymbolType type, String value, int line, int col) {
		super();
		this.type = type;
		this.value = value;
		this.line = line;
		this.col = col;
	}
	
}
