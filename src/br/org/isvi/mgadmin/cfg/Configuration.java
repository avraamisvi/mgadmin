package br.org.isvi.mgadmin.cfg;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Configuration {

	final String FILE_NAME = "mgadmin.cfg";
	ConfigFile cfg = new ConfigFile();
	
	public void open() {
		try {
			File file;
			file = new File(FILE_NAME);
			
			if(!file.exists()) {
					file.createNewFile();
			}
			
			ObjectMapper mapper = new ObjectMapper();
			try {
				cfg = mapper.readValue(file, ConfigFile.class);
			} catch (Exception e) {
				e.printStackTrace();
				cfg = new ConfigFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addServer(String name, String host, String port, String username, String password) {
		cfg.servers.add(new Server(name, host, port, username, password));
	}
	
	public void removeServer(String name) {
		int i = 0;
		Server []svs = cfg.servers.toArray(new Server[0]);
		
		for(i = 0; i < svs.length; i++) {
			Server s = svs[i];
			if(s.getName().equalsIgnoreCase(name)) {
				cfg.servers.remove(i);
				break;
			}
		}
	}
	
	public List<Server> getServers() {
		return cfg.servers;
	}
	
	public void save() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(new File(FILE_NAME), cfg);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
