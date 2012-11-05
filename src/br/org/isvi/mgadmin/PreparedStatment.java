package br.org.isvi.mgadmin;

import java.util.HashMap;

import com.mongodb.DBCollection;

public class PreparedStatment {
	enum Type {
		find,
		update,
		insert,
		remove
	};
	
	public DBCollection collection;
	public HashMap<String, Object> params = new HashMap<String, Object>();
	public Type type;
}
