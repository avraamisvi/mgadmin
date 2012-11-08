package br.org.isvi.mgadmin.controllers;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import br.org.isvi.mgadmin.model.PreparedStatment;
import br.org.isvi.mgadmin.model.Query;

import com.mongodb.BasicDBList;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

public class QueryControler {

	DBCursor cursor;
	WriteResult res;
	PreparedStatment stm;
	
	public void process(PreparedStatment stm) {
		
		this.stm = stm;
		this.cursor = null;
		this.res = null;		
		
		switch(stm.type) {
			case find:
				this.find(stm);
				break;
			case insert:
				this.insert(stm);
				break;
			case remove:
				this.remove(stm);
				break;
			case update:
				this.update(stm);
				break;				
		}
	}
	
	private void remove(PreparedStatment stm) {
		Query q = new Query(stm.collection);
		String ref = (String) stm.params.get("ref");
		
		res = q.remove(ref);
	}

	void insert(PreparedStatment stm) {
		Query q = new Query(stm.collection);
		String ref = (String) stm.params.get("ref");
		
		res = q.insert(ref);
	}

	void update(PreparedStatment stm) {
		Query q = new Query(stm.collection);
		String ref = (String) stm.params.get("ref");
		String sets = (String) stm.params.get("sets");
		
		res = q.update(ref, sets);
	}
	
	void find(PreparedStatment stm) {
		Query q = new Query(stm.collection);
		String ref = (String) stm.params.get("ref");
		ArrayList<String> keys = (ArrayList<String>) stm.params.get("keys");
		
		cursor = q.find(ref, keys);
	}
	
	public void next(Tree tree, int qtd) {
		try 
	 	{
			if(cursor == null)
				return;
			
            for(int i = 0; i < qtd; i++) {
            	if(!cursor.hasNext()){
            		break;
            	}
                DBObject obj = cursor.next();
                
                createItem(obj, tree);
            }
        } finally {
            cursor.close();
        }
	}
	
	public void fetch(Tree tree) {
		if(cursor == null)
			return;
		
		try 
	 	{
            while(cursor.hasNext()) {
                DBObject obj = cursor.next();
                createItem(obj, tree);
            }
        } finally {
            cursor.close();
        }
	}	
	
	public void createItem(DBObject obj, Tree tree) {		
		TreeItem item = new TreeItem (tree, SWT.NONE);
		item.setText (0, "Document");
		item.setText (1, obj.toString());
		item.setData("db", stm.collection);
		
		for(String k : obj.keySet()) {
			
			TreeItem fld = new TreeItem (item, SWT.NONE);
			
			fld.setText (0, k);
			
			Object value = obj.get(k);
			
			if(value instanceof DBObject) {
				DBObject val = (DBObject) value;
				fld.setText (1, val.toString());
				createItem(val, fld);
			} else {
				fld.setText (1, value.toString());
			}
		}
	}
	
	public void createItem(DBObject obj, TreeItem par) {
		TreeItem item = new TreeItem (par, SWT.NONE);
		item.setData("db", stm.collection);
		
		if(obj instanceof BasicDBList)
			item.setText (0, "Array");
		else
			item.setText (0, "Document");
		
		item.setText (1, obj.toString());
		
		for(String k : obj.keySet()) {
			
			TreeItem fld = new TreeItem (item, SWT.NONE);
			
			fld.setText (0, k);
			
			Object value = obj.get(k);
			
			if(value instanceof DBObject) {
				DBObject val = (DBObject) value;
				createItem(val, fld);
			} else {
				fld.setText (1, value.toString());
			}
		}
	}

	public void setLogResult(StyledText stlTxtLog) {
		
		stlTxtLog.setText("");
		
		if(res != null) {
			//stlTxtLog.append("Operation realized with success!");
			if(res.getError()!=null)
				stlTxtLog.append("Error:" + res.getError());
			else
				stlTxtLog.append("Affected documents: " + res.getN());
		} else if(cursor != null) {
			stlTxtLog.append("Total documents retrived:" + cursor.count());
		}
	}
}
