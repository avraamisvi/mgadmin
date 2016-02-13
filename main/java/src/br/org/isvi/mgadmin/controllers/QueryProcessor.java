package br.org.isvi.mgadmin.controllers;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.DBCollection;

import br.org.isvi.mgadmin.RemoteGui;
import br.org.isvi.mgadmin.lex.LexicalAnalyser;
import br.org.isvi.mgadmin.lex.Symbol;
import br.org.isvi.mgadmin.lex.SymbolType;
import br.org.isvi.mgadmin.model.PreparedStatment;
import br.org.isvi.mgadmin.model.PreparedStatment.Type;

public class QueryProcessor {
	
	public String error;
	private RemoteGui window;
	private DBCollection collection;
	private PreparedStatment command;
	
	public void processCommand(String cmd, RemoteGui window, PreparedStatment command) throws Exception {
	
		this.window = window;
		this.command = command;
		
		LexicalAnalyser lex = new LexicalAnalyser();
		
		List<Symbol> comds = lex.process(cmd);
				
		verificarSintaxe(comds);
		
		processSymbol(0, comds);
		
		command.collection = collection;
	}
	
	public int processSymbol(int idx, List<Symbol> comds) throws Exception {
		
		Symbol sb = getSymbol(idx, comds);
		
		switch(sb.type) {
			case find:
				processFind(idx, comds);
				break;
			case remove:
				processRemove(idx, comds);
				break;				
			case insert:
				processInsert(idx, comds);
				break;	
			case update:
				processUpdate(idx, comds);
				break;					
			case using:
				processUsing(idx, comds);
				break;				
		}
		
		return idx;
	}
	
	private void processRemove(int idx, List<Symbol> comds) {
		command.type = Type.remove;
		
		idx++;
		Symbol query = getSymbol(idx, comds);
		
		if(collection == null) {
			collection = window.systemMainController.getUsingCollection();
			command.collection = collection;
		}
		
		command.params.put("ref", query.value);
	}

	private void processUpdate(int idx, List<Symbol> comds) {
		command.type = Type.update;
		
		idx++;
		Symbol sets = getSymbol(idx, comds);
		
		idx++;
		Symbol on = getSymbol(idx, comds);
		
		idx++;
		Symbol query = getSymbol(idx, comds);
		
		if(collection == null) {
			collection = window.systemMainController.getUsingCollection();
			command.collection = collection;
		}
		
		command.params.put("sets", sets.value);
		command.params.put("ref", query.value);
	}

	public void processUsing(int idx, List<Symbol> comds) throws Exception {
		
		idx++;
		Symbol database = getSymbol(idx, comds);
		
		collection = window.systemMainController.getDBCollection(database.value);
		
		if(collection == null)
			gerarErro(" '<server>.<database>.<collection>' expected, found " + database.value, database.line, database.col);
		
		processSymbol(idx+1, comds);
	}	
	
	public int processInsert(int idx, List<Symbol> comds) throws Exception {
		
		command.type = Type.insert;
		
		idx++;
		Symbol query = getSymbol(idx, comds);
		
		if(!query.type.equals(SymbolType.mongo_data)) {
			gerarErro(" Character '{' expected, found " + query.value, query.line, query.col);
		}	
		
		if(collection == null) {
			collection = window.systemMainController.getUsingCollection();
			command.collection = collection;
		}
		
		command.params.put("ref", query.value);
		
		return idx;
	}	
	
	public int processFind(int idx, List<Symbol> comds) throws Exception {
		
		command.type = Type.find;
		
		idx++;
		Symbol query = getSymbol(idx, comds);
		
		if(query == null || !query.type.equals(SymbolType.mongo_data)) {
			gerarErro(" Character '{' expected, found " + query.value, query.line, query.col);
		}

		idx++;
		Symbol keysCmd = getSymbol(idx, comds);
		
		Symbol keys = null;
		
		if(keysCmd != null) {
			if(!keysCmd.type.equals(SymbolType.keys)) {
				gerarErro(" reserved word 'keys' expected, found " + keysCmd.value, query.line, query.col);				
			} else {
				idx++;
				keys = getSymbol(idx, comds);
				
				if(!keys.type.equals(SymbolType.mongo_data)) {
					gerarErro(" Character '[' expected, found " + keys.value, query.line, query.col);
				}
			}
		}
		
		if(keys != null) {
			
			char en = keys.value.charAt(keys.value.length()-1);
			
			if(en != ']') {
				gerarErro(" Character ']' expected, found " + en, keys.line, keys.col+keys.value.length());
			}
			
			String ch = keys.value.substring(1, keys.value.length()-1);
			String []chs = ch.split(",");
			
			ArrayList<String> ks = new ArrayList<String>();
			for(String k : chs) {
				ks.add(k);
			}
			
			command.params.put("keys", ks);
			
		} 
		
		if(collection == null) {
			collection = window.systemMainController.getUsingCollection();
			command.collection = collection;
		}
		
		command.params.put("ref", query.value);
		
		return idx;
	}
	
	private Symbol getSymbol(int idx, List<Symbol> comds) {
		
		if(comds.size() <= idx) {
			return null;
		}
		
		return comds.get(idx);
	}
	
	public void gerarErro(String msg, int lin, int col) throws Exception {
		this.error = "Error at line " + lin + " and column " + col + " : " + msg;
		throw new Exception(this.error);
	}
	
	private void verificarSintaxe(List<Symbol> comds) throws Exception {
		
	}
}
