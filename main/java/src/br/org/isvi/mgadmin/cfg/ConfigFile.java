package br.org.isvi.mgadmin.cfg;

import java.util.ArrayList;

public class ConfigFile {

	ArrayList<Server> servers;

	public ConfigFile() {
		super();
		servers = new ArrayList<Server>();
	}

	public ArrayList<Server> getServers() {
		return servers;
	}

	public void setServers(ArrayList<Server> servers) {
		this.servers = servers;
	}
}
