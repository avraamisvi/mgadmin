package br.org.isvi.mgadmin;

import java.util.ArrayList;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;

public class Query {
	
	private DBCollection collection;
	
	public Query(DBCollection collection) {
		this.collection = collection;
	}

	public void setCollection(DBCollection collection) {
		this.collection = collection;
	}
	
	public WriteResult update(String ref, String sets) {
		
		DBObject qry = (DBObject) JSON.parse(ref);
		DBObject st = (DBObject) JSON.parse(sets);
		
		return collection.update(qry, st, false, true);
	}
	
	public WriteResult insert(String ref) {
		DBObject qry = (DBObject) JSON.parse(ref);
		return collection.insert(qry);
	}	
	
	public DBCursor find(String ref, ArrayList<String> keys) {
		
		DBCursor ret = null;
		
		DBObject qry = (DBObject) JSON.parse(ref);
		DBObject kys = new BasicDBObject();		
		
		if(keys != null) {
			
			for(String k : keys) {
				kys.put(k, Boolean.TRUE);
			}
			
			ret = collection.find(qry, kys);
		} else {
			
			ret = collection.find(qry);			
		}
		
		return ret;
	}

	public WriteResult remove(String ref) {
		DBObject qry = (DBObject) JSON.parse(ref);
		return collection.remove(qry);		
	}
}
