package br.org.isvi.mgadmin.model;

public class PropertiesVO {
	boolean saveSession;
	boolean informUpdates;
	
	public boolean isSaveSession() {
		return saveSession;
	}
	public void setSaveSession(boolean saveSession) {
		this.saveSession = saveSession;
	}
	public boolean isInformUpdates() {
		return informUpdates;
	}
	public void setInformUpdates(boolean informUpdates) {
		this.informUpdates = informUpdates;
	}
}
