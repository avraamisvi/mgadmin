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
		
		//Cascade
		MenuItem[] itemsCascade = items[1].getMenu().getItems();
		itemsCascade[0].setText("Add");
		itemsCascade[0].setEnabled(false);
		
		itemsCascade[1].setText("Add Server");
		itemsCascade[1].setEnabled(true);
		//Cascade
		
		items[2].setText("Remove");
		items[2].setEnabled(false);
		
		items[3].setText("Close");
		items[3].setEnabled(false);
	}	
	
	public void configureServersPopup(Menu menu) {
		MenuItem[] items = menu.getItems();
		
		items[0].setText("Open");
		items[0].setEnabled(true);
		
		//Cascade
		MenuItem[] itemsCascade = items[1].getMenu().getItems();
		itemsCascade[0].setText("Add Auth");
		itemsCascade[0].setEnabled(true);
		
		itemsCascade[1].setText("Add Database");
		itemsCascade[1].setEnabled(true);
		//Cascade		
		
		items[2].setText("Remove");
		items[2].setEnabled(true);
		
		items[3].setText("Close");
		items[3].setEnabled(true);
	}
	
	public void configureDatabasePopup(Menu menu, Boolean aberto) {
		MenuItem[] items = menu.getItems();
		
		items[0].setText("Open");
		items[0].setEnabled(true);
		
//		items[1].setText("Add Collection");
//		items[1].setEnabled(aberto);
		
		//Cascade
		MenuItem[] itemsCascade = items[1].getMenu().getItems();
		itemsCascade[0].setText("Add User");
		itemsCascade[0].setEnabled(aberto);//FIXME
		
		itemsCascade[1].setText("Add Collection");
		itemsCascade[1].setEnabled(aberto);
		//Cascade			
		
		items[2].setText("Remove");
		items[2].setEnabled(true);
		
		items[3].setText("Close");
		items[3].setEnabled(aberto);		
	}
	
	public void configureCollectionPopup(Menu menu) {
		MenuItem[] items = menu.getItems();
		
		items[0].setText("Set Using");
		items[0].setEnabled(true);
		
//		items[1].setText("Add Item");
//		items[1].setEnabled(true);
		
		//Cascade
		MenuItem[] itemsCascade = items[1].getMenu().getItems();
		itemsCascade[0].setText("Add Index");
		itemsCascade[0].setEnabled(true);//FIXME
		
		itemsCascade[1].setText("Add Item");
		itemsCascade[1].setEnabled(true);
		//Cascade		
		
		items[2].setText("Remove");
		items[2].setEnabled(true);
		
		items[3].setText("Remove Using");
		items[3].setEnabled(true);			
	}
	
	public void configureUsersPopup(Menu menu) {
		MenuItem[] items = menu.getItems();
		
		items[0].setText("Set Using");
		items[0].setEnabled(false);
		
//		items[1].setText("Add User");
//		items[1].setEnabled(true);
				
		//Cascade
		MenuItem[] itemsCascade = items[1].getMenu().getItems();
		itemsCascade[0].setText("Add Index");
		itemsCascade[0].setEnabled(false);//FIXME
		
		itemsCascade[1].setText("Edit");
		itemsCascade[1].setEnabled(true);
		//Cascade		
		
		items[2].setText("Remove");
		items[2].setEnabled(false);
		
		items[3].setText("Remove Using");
		items[3].setEnabled(false);			
	}	
	
	public void configureIndexesPopup(Menu menu) {
		MenuItem[] items = menu.getItems();
		
		items[0].setText("Set Using");
		items[0].setEnabled(false);
		
//		items[1].setText("Add User");
//		items[1].setEnabled(false);
		
		//Cascade
		MenuItem[] itemsCascade = items[1].getMenu().getItems();
		itemsCascade[0].setText("Add");
		itemsCascade[0].setEnabled(false);
		
		itemsCascade[1].setText("Edit");
		itemsCascade[1].setEnabled(true);
		//Cascade		
		
		items[2].setText("Remove");
		items[2].setEnabled(false);
		
		items[3].setText("Remove Using");
		items[3].setEnabled(false);			
	}		
	
	public void configureAdminPopup(Menu menu, Boolean aberto) {
		MenuItem[] items = menu.getItems();
		
		items[0].setText("Open");
		items[0].setEnabled(true);
		
		//Cascade
		MenuItem[] itemsCascade = items[1].getMenu().getItems();
		itemsCascade[0].setText("Add");
		itemsCascade[0].setEnabled(false);
		
		itemsCascade[1].setText("Edit");
		itemsCascade[1].setEnabled(false);
		//Cascade	
		
		items[2].setText("Remove");
		items[2].setEnabled(false);
		
		items[3].setText("Close");
		items[3].setEnabled(aberto);		
	}
	
	public void configureMenuItem(TreeItem item, Menu menuMainTree) {
		if(item == null) {
			configureDefaultPopup(menuMainTree);
			return;
		}
		
		if(item.getData("tipo").equals(TipoItens.server)) {
			configureServersPopup(menuMainTree);
		} else if(item.getData("tipo").equals(TipoItens.collection)) {
			if(item.getData("nome").equals("system.indexes")) {
				configureIndexesPopup(menuMainTree);
			} else if(item.getData("nome").equals("system.users")) {
				configureUsersPopup(menuMainTree);
			} else {						
				configureCollectionPopup(menuMainTree);
			}
		} else if(item.getData("tipo").equals(TipoItens.collections)) {
			
			if(item.getData("nome").equals("admin")) {
				configureAdminPopup(menuMainTree, (Boolean)item.getData("aberto"));
			} else {
				configureDatabasePopup(menuMainTree, (Boolean)item.getData("aberto"));
			}
			
		}	
	}
}
