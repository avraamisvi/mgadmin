package br.org.isvi.mgadmin.messages;

public class Message implements MessageConstants {
	String name;
	
	public Message() {
		super();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}