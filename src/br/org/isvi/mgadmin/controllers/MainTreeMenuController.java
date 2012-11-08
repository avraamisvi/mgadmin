package br.org.isvi.mgadmin.controllers;

import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TreeItem;

import br.org.isvi.mgadmin.controllers.SystemMainController.TipoItens;

public class MainTreeMenuController {
	
	public void configureDefaultPopup(Menu menu) {
		MenuItem[] items = menu.getItems();
		
		items[0].setText("Open");
		items[0].setEnabled(false);
		
		items[1].setText("Add Server");
		items[1].setEnabled(true);
		
		items[2].setText("Remove");
		items[2].setEnabled(false);
		
		items[3].setText("Close");
		items[3].setEnabled(false);
	}	
	
	public void configureServersPopup(Menu menu) {
		MenuItem[] items = menu.getItems();
		
		items[0].setText("Open");
		items[0].setEnabled(true);
		
		items[1].setText("Add Database");
		items[1].setEnabled(true);
		
		items[2].setText("Remove");
		items[2].setEnabled(true);
		
		items[3].setText("Close");
		items[3].setEnabled(true);
	}
	
	public void configureDatabasePopup(Menu menu, Boolean aberto) {
		MenuItem[] items = menu.getItems();
		
		items[0].setText("Open");
		items[0].setEnabled(true);
		
		items[1].setText("Add Collection");
		items[1].setEnabled(aberto);
		
		items[2].setText("Remove");
		items[2].setEnabled(true);
		
		items[3].setText("Close");
		items[3].setEnabled(aberto);		
	}
	
	public void configureCollectionPopup(Menu menu) {
		MenuItem[] items = menu.getItems();
		
		items[0].setText("Set Using");
		items[0].setEnabled(true);
		
		items[1].setText("Add Item");
		items[1].setEnabled(true);
		
		items[2].setText("Remove");
		items[2].setEnabled(true);
		
		items[3].setText("Remove Using");
		items[3].setEnabled(true);			
	}
	
	public void configureMenuItem(TreeItem item, Menu menuMainTree) {
		if(item == null) {
			configureDefaultPopup(menuMainTree);
			return;
		}
				
		if(item.getData("tipo").equals(TipoItens.server)) {
			configureServersPopup(menuMainTree);
		} else if(item.getData("tipo").equals(TipoItens.collection)) {
			configureCollectionPopup(menuMainTree);
		} else if(item.getData("tipo").equals(TipoItens.collections)) {
			configureDatabasePopup(menuMainTree, (Boolean)item.getData("aberto"));
		}		
	}
}
