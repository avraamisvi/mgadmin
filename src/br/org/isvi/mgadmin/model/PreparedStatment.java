package br.org.isvi.mgadmin.model;

import java.util.HashMap;

import com.mongodb.DBCollection;

public class PreparedStatment {
	public enum Type {
		find,
		update,
		insert,
		remove
	};
	
	public DBCollection collection;
	public HashMap<String, Object> params = new HashMap<String, Object>();
	public Type type;
}
