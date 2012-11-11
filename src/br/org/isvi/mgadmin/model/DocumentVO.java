package br.org.isvi.mgadmin.model;

import com.mongodb.DBObject;

public class DocumentVO {
	public DBObject dbobj;

	public DocumentVO(DBObject dbobj) {
		super();
		this.dbobj = dbobj;
	}
	
	public DocumentVO() {
	}
}
