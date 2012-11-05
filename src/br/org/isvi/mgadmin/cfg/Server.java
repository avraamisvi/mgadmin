package br.org.isvi.mgadmin.cfg;

public class Server {
	
	String name;
	String host;
	String port;
	String username;
	String password;
	
	public Server() {
	}
	
	public Server(String name, String host, String port, String username,
			String password) {
		super();
		this.name = name;
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
