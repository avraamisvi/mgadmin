package br.org.isvi.mgadmin.model;

public class NameValueVO {
	public String name;
	public Object value;
	public boolean valueEnabled;
	public boolean nameEnabled;
	
	public NameValueVO(String name, Object value, boolean valueEnabled,
			boolean nameEnabled) {
		super();
		this.name = name;
		this.value = value;
		this.valueEnabled = valueEnabled;
		this.nameEnabled = nameEnabled;
	}
	
	public NameValueVO() {	
		super();
		this.name = "";
		this.value = "";
		this.valueEnabled = true;
		this.nameEnabled = true;		
	}
}
