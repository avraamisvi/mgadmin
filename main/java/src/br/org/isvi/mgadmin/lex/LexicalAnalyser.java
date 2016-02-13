package br.org.isvi.mgadmin.lex;

import java.util.ArrayList;
import java.util.List;

public class LexicalAnalyser {
	
	StringBuilder token = new StringBuilder();
	ArrayList<Symbol> sbms = new ArrayList<Symbol>();
	int noTokenLineBreakColchets = 0;
	int noTokenLineBreakChaves = 0;

	public List<Symbol> process(String text) {
		char oldch = 0;
		int line = 0;
		
		for(int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			oldch = ch;
			
			switch(ch) {
				case '\n':
					if(noTokenLineBreakColchets <= 0 && noTokenLineBreakChaves <= 0 ) {
						createSymbol(line, i);
					}
					line++;
					break;
				case '\r':
					break;
				case '[':
					token.append(ch);
					noTokenLineBreakColchets++;
					break;
				case '{':
					token.append(ch);
					noTokenLineBreakChaves++;
					break;
				case ']':
					token.append(ch);
					noTokenLineBreakColchets--;
					if(noTokenLineBreakColchets <= 0 && noTokenLineBreakChaves <= 0 ) {
						noTokenLineBreakColchets = 0;
						createSymbol(line, i);
					}
					break;					
				case '}':
					token.append(ch);
					noTokenLineBreakChaves--;
					if(noTokenLineBreakColchets <= 0 && noTokenLineBreakChaves <= 0 ) {
						noTokenLineBreakChaves = 0;
						createSymbol(line, i);
					}
					break;
				case 32://space
				case 11://tab
					if(noTokenLineBreakColchets <= 0 && noTokenLineBreakChaves <= 0 ) {
						createSymbol(line, i);
					}					
					break;
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
				case 12:
				case 14:
				case 15:
				case 16:
				case 17:
				case 18:
				case 19:
				case 20:
				case 21:
				case 22:
				case 23:
				case 24:
				case 25:
				case 26:
				case 27:
				case 28:
				case 29:
				case 30:
				case 31:
					//NOP
					break;
				default:
					token.append(ch);
					break;
				
			}
		}
		
		return sbms;
	}
	
	private void createSymbol(int line, int col) {
		
		if(token.length() <=0)
			return;
		
		String tk = this.token.toString();
		SymbolType tp = null;
		
		try {
			tp = SymbolType.valueOf(tk.toLowerCase());
		} catch(Exception e) {
			//a galera de java tem que fazer este tipo de coisa idiota nŽ? era s— retornar null
		}
		
		if(tp == null) {
			tp = SymbolType.mongo_data;
		}
		
		Symbol smb = new Symbol(tp, this.token.toString(), line, col);
		token = new StringBuilder();
		
		sbms.add(smb);
	}
	
//	public static void main(String[] args) {
//		String texto = "using database \n\r find {teste:{$gt:2}} keys [teste, abacate]";
//		
//		List<Symbol> sbms = new LexicalAnalyser().process(texto);
//		
//		System.out.println("sb:" + sbms.size());
//	}
}
