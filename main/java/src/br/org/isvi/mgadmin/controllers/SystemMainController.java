package br.org.isvi.mgadmin.controllers;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import br.org.isvi.mgadmin.RemoteGui;
import br.org.isvi.mgadmin.cfg.Configuration;
import br.org.isvi.mgadmin.cfg.Server;
import br.org.isvi.mgadmin.data.structure.Tree;
import br.org.isvi.mgadmin.data.structure.TreeItem;
import br.org.isvi.mgadmin.model.DocumentVO;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;

public class SystemMainController {
	
	private static final String STATS = "stats";
	private static final String ABERTO = "aberto";
	private static final String NAME = "name";
	private static final String CAPPED = "capped";
	private static final String MAX = "max";
	private static final String SIZE = "size";
	private static final String DBNAME = "dbname";
	private static final String PREFS = "prefs";
	private static final String COUNT = "count";
	private static final String TIPO = "tipo";
	private static final String PASSWORD = "password";
	private static final String USERNAME = "username";
	private static final String CONNECTION = "connection";
	private static final String PORT = "port";
	private static final String NOME = "nome";
	private HashMap<TreeItem, Mongo> servers = new HashMap<TreeItem, Mongo>();
	private Tree mainTree; 
	private TreeItem using = null;
//	private Color oldColor = null;
	private RemoteGui mainWindow;
	private Configuration cfg;
	
	public enum TipoItens {
		server, collections, collection, index, user
	};
	
	public SystemMainController(RemoteGui mainWindow) {
		this.mainWindow = mainWindow;
		this.cfg = new Configuration();
		this.cfg.open();
	}
	
	public Configuration getCfg() {
		return cfg;
	}
	
	public void refreshMainTree() {
		
		List<Server> srvs = cfg.getServers();
		
		if(srvs.isEmpty())
			return;
		
		LOAD: for(Server s : srvs) {
			
//			if(!servers.isEmpty()) {
				for(TreeItem k : mainTree.getItems()) {
					if(k.getText().equalsIgnoreCase(s.getName())) {
						continue LOAD;
					}
				}
//			}
			
			TreeItem item = new TreeItem (mainTree);
			item.setText (s.getName());
			item.setData(TIPO, TipoItens.server);
			item.setData(CONNECTION, s.getHost());
			item.setData(PORT, Integer.parseInt(s.getPort()));//new Integer(27017)
			item.setData(USERNAME, s.getUsername());
			item.setData(PASSWORD, s.getPassword());
			
//			Image xImage = new Image(item.getDisplay(), 
//					ClassLoader.getSystemResourceAsStream("br/org/isvi/mgadmin/images/server.png"));
//			item.setImage(xImage);				
		}		
	}
	
	/**
	 * Refreshes info of maintree items
	 */
	public void refreshMainTreeInfo() {
		
		if(servers.isEmpty()) {
			return;
		}
		
		for(TreeItem it : mainTree.getItems()) {
			refreshServerItens(it);
		}
	}	
	
	/**
	 * Refreshes info of server items
	 */
	private void refreshServerItens(TreeItem parent) {
		Mongo serv = servers.get(parent);
		
		for(TreeItem it : parent.getItems()) {
			DB db = serv.getDB(it.getData(NOME).toString());

			it.setText (it.getData(NOME).toString()  + " (" + db.getCollectionNames().size() + ")");			
			
			it.setData(COUNT, ""+db.getCollectionNames().size());
			it.setData(PREFS, db.getReadPreference().toDocument());
			
			refreshDataBaseItens(it, db);
		}
	}

	/**
	 * Refreshes info of database items
	 */
	private void refreshDataBaseItens(TreeItem parent, DB db) {
		
		for(TreeItem it : parent.getItems()) {
			DBCollection col = db.getCollection(it.getData(NOME).toString());
			
			it.setText (it.getData(NOME).toString() + " (" + col.count() + ")");
			
			it.setData(COUNT, ""+col.count());
			
			it.setData(PREFS, col.getReadPreference().toDocument());			
			it.setData(CAPPED, col.isCapped());				
		}		
	}

	public void getServers(Tree tree) {
		this.mainTree = tree;
		this.refreshMainTree();
	}
	
	public void openServer(TreeItem item) throws Exception {
		
		String host = item.getData(CONNECTION).toString();
		int port = (Integer) item.getData(PORT);
		
		if(!servers.containsKey(host)) {
			Mongo server = new Mongo(host, port);
			servers.put(item, server);
			try {
				
				for(Server serv : cfg.getServers()) {
					if(item.getText().equals(serv.getName())) {
						if(serv.getUsername() != null && !serv.getUsername().isEmpty()) {
							server.getDB("admin").authenticate(serv.getUsername(), serv.getPassword().toCharArray());
						}
						break;
					}
				}
				
				List<String> itens = server.getDatabaseNames();
				this.createDatabaseItem(item, server, itens);
			} catch (Exception err) {
				if(err.getMessage().contains("need to login")) {
					MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error", 
							"Authentication error!");
				}
			}
		}
		
		mainWindow.setSearchEnabled(!servers.isEmpty());
	}

	public void openDatabase(TreeItem item) throws UnknownHostException {
		
		String colls = item.getData(NOME).toString();
		Boolean aberto = (Boolean) item.getData(ABERTO);
		
		if(!aberto) {
			aberto = true;
			
			item.setData(ABERTO, aberto);
			
			Mongo server = servers.get(item.getParentItem());
			
			DB db = server.getDB(colls);
			
			Set<String> itens = db.getCollectionNames();
			
			createCollectionItem(item, db, itens);
		}
	}
	
	public void openItem(TreeItem item) throws Exception {

		if(item.getData(TIPO) == null)
			return;
		
		if(item.getData(TIPO).equals(TipoItens.server)) {
			openServer(item);
		} else if(item.getData(TIPO).equals(TipoItens.collections)) {
			openDatabase(item);
		} else if(item.getData(TIPO).equals(TipoItens.collection)) {
		
			String name = item.getData(NOME).toString();
			
			if(!name.equals("system.indexes") && !name.equals("system.users")) {
				if(using != null) {
//					using.setForeground(oldColor);
				}
				
				using = item;
//				oldColor = using.getForeground();
				using.setForeground(item.getDisplay().getSystemColor(SWT.COLOR_RED));
				
				mainWindow.setUsingText(using.getData(NOME).toString());
			} else if(name.equals("system.users")){
				//this.openUsersDlg();
				openUsers(item);
			}
		}
	}
	
	private void openUsers(TreeItem item) {
		String colls = item.getParentItem().getData(NOME).toString();
		Boolean aberto = (Boolean) item.getData(ABERTO);
		
		if(!aberto) {
			aberto = true;
			
			item.setData(ABERTO, aberto);
			
			Mongo server = servers.get(item.getParentItem().getParentItem());
			
			DB db = server.getDB(colls);
			DBCollection col = db.getCollection("system.users");
			
			createUserItem(item, col);
		}
	}

	private void createUserItem(TreeItem item, DBCollection col) {
		
		DBCursor cur = col.find();
		
		while(cur.hasNext()) {
			
			DBObject o = cur.next();
			
			TreeItem it = new TreeItem (item, SWT.NONE);
			it.setData(TIPO, TipoItens.user);
			it.setData(NOME, o.get("user"));
			
			Boolean readOnly = (Boolean) o.get("readOnly");
			
			it.setText(o.get("user").toString());		
			it.setData("id", o.get("_id"));
			it.setData("readOnly", readOnly);
			it.setData("Password", o.get("pwd"));			
			
			Image xImage = null;
			
			if(readOnly) {
				xImage = new Image(item.getDisplay(), 
						ClassLoader.getSystemResourceAsStream("br/org/isvi/mgadmin/images/user_icon_24.png"));
			} else {
				xImage = new Image(item.getDisplay(), 
					ClassLoader.getSystemResourceAsStream("br/org/isvi/mgadmin/images/admin_icon_24.png"));
			}
			
			it.setImage(xImage);
		}
	}

	public void unSetUsing(TreeItem item) {
		
		if(item != using)
			return;
		
		if(using != null) {
			using.setForeground(oldColor);
		}
		
		using = null;
		mainWindow.setUsingText("");		
	}
	
	
	public void createDatabaseItem(TreeItem item, Mongo server, List<String> itens) {
		
		for(String str : itens) {
			createDataBaseItem(item, str, server);
		}
	}
	
	public void createCollectionItem(TreeItem item, DB db, Set<String> itens) {
		
		for(String str : itens) {
			createCollectionItem(item, db, str);
		}
	}
	
	private void createDataBaseItem(TreeItem parent, String dbname, Mongo server) {
		DB db = server.getDB(dbname);
		TreeItem it = new TreeItem (parent, SWT.NONE);
		it.setData(TIPO, TipoItens.collections);
		it.setData(ABERTO, Boolean.FALSE);
		it.setData(NOME, dbname);
		
		it.setText (dbname  + " (" + db.getCollectionNames().size() + ")");			
		
		it.setData(COUNT, ""+db.getCollectionNames().size());
		it.setData(PREFS, db.getReadPreference().toDBObject());
		
		Image xImage = null;
		
		if(dbname.equals("admin")) {
			xImage = new Image(parent.getDisplay(), 
					ClassLoader.getSystemResourceAsStream("br/org/isvi/mgadmin/images/admin.png"));			
		} else { 
			xImage = new Image(parent.getDisplay(), 
					ClassLoader.getSystemResourceAsStream("br/org/isvi/mgadmin/images/collections.png"));
		}
		
		it.setImage(xImage);
	}
	
	public void createCollectionItem(TreeItem item, DB db, String name) {
		
			TreeItem it = new TreeItem (item, SWT.NONE);
			it.setData(TIPO, TipoItens.collection);
			it.setData(NOME, name);
			it.setData(ABERTO, false);
			
			DBCollection col = db.getCollection(name);	
			
			long count = -1;
			
			try {
				count = col.count();
			} catch (Exception e) {
			}
			
			
			if(count >= 0) {
				it.setText (name + " (" + count + ")");			
				it.setData(COUNT, ""+col.count());
			} else {
				it.setText (name);			
				it.setData(COUNT, "-1");
			}
			
			it.setData(PREFS, col.getReadPreference().toDBObject());
//			it.setData(STATS, col.getStats());
			it.setData(CAPPED, col.isCapped());			
			
			Image xImage = null;
			
			if(name.equals("system.users")) {
				xImage = new Image(item.getDisplay(), 
						ClassLoader.getSystemResourceAsStream("br/org/isvi/mgadmin/images/users.png"));
			} else if(name.equals("system.indexes")) {
				xImage = new Image(item.getDisplay(), 
						ClassLoader.getSystemResourceAsStream("br/org/isvi/mgadmin/images/index.png"));
			} else {
				xImage = new Image(item.getDisplay(), 
					ClassLoader.getSystemResourceAsStream("br/org/isvi/mgadmin/images/collection.png"));
			}
			
			it.setImage(xImage);
	}	
	
	public DBCollection getUsingCollection() {
		
		DBCollection ret = null;
		
		if(using != null) {
			
			String coll = using.getData(NOME).toString();
			String colls = using.getParentItem().getData(NOME).toString();
			
			Mongo server = servers.get(using.getParentItem().getParentItem());
			
			DB db = server.getDB(colls);
			
			ret = db.getCollection(coll);
		}
		
		return ret;		
	}
	
	public DBCollection getDBCollection(String name) {
		
		DBCollection ret = null; 
		String names[] = name.split("\\.");
		
		if(names.length > 2) {
			Mongo server = null;
			String serverName = names[0];
			String colls = names[1];
			String coll = names[2];
					
			for(TreeItem k : servers.keySet()) {
				if(k.getText().equalsIgnoreCase(serverName)) {
					server = servers.get(k);
					break;
				}
			}		
			
			if(server != null) {				
				DB db = server.getDB(colls);
				ret = db.getCollection(coll);
			}
		}
		
		return ret;		
	}	
	
	public void fillProperties(StyledText stl, TreeItem item) {
		
		stl.setText("");
		
		if(item.getData(TIPO) == null)
			return;
		
		if(item.getData(TIPO).equals(TipoItens.server)) {
			StringBuilder build = new StringBuilder();
			
			String desc = item.getText();
			String host = item.getData(CONNECTION).toString();
			String port = item.getData(PORT).toString();
			String user = item.getData(USERNAME)!=null?item.getData(USERNAME).toString():"";
			
			build.append("Description: " + desc + "\n");
			build.append("Hostname: " + host + "\n");
			build.append("port: " + port + "\n");
			build.append("username: " + user + "\n");
			
			stl.append(build.toString());
			
		} else if(item.getData(TIPO).equals(TipoItens.collections)) {
			StringBuilder build = new StringBuilder();
			
			String desc = item.getData(NOME).toString();
			String itens = item.getData(COUNT).toString();
			String pref = item.getData(PREFS).toString();
			
			build.append("Description: " + desc + "\n");
			build.append("Collections: " + itens + "\n");
			build.append("Preferences: " + pref + "\n");
			
			stl.append(build.toString());
		} else if(item.getData(TIPO).equals(TipoItens.collection)) {
			
			StringBuilder build = new StringBuilder();
			
			String desc = item.getData(NOME).toString();
			String itens = item.getData(COUNT).toString();
			String pref = item.getData(PREFS).toString();
			String capped = item.getData(CAPPED).toString();
			
			build.append("Description: " + desc + "\n");
			build.append("Documents: " + itens + "\n");
			build.append("Preferences: " + pref + "\n");		
			build.append("Capped: " + capped + "\n");			
			
			stl.append(build.toString());
		}		
	}
	
	public void openNewDatabaseDlg(TreeItem item, Shell parent) throws Exception{
		
		NewDatabaseDlg dlg = new NewDatabaseDlg(parent);
		HashMap<String, Object> params = new HashMap<String, Object>();
		dlg.setParams(params);
		
		if(dlg.open() < 1) {
			try{
				if(params.get(DBNAME) == null) {
					MessageDialog.openError(parent, "Error", "Database Name is Empty.");
					return;
				}
				createDatabase(item, params.get(DBNAME).toString());
			} catch(Exception e) {
				if(e.getMessage() != null)
					MessageDialog.openError(parent, "Error", e.getMessage());
				else
					MessageDialog.openError(parent, "Error", "Unknown Error!");
			}
		}
	}
	
	public void openNewDocumentDlg(TreeItem item, Shell parent) throws Exception{
		
		DocumentDlg dlg = new DocumentDlg(parent);
		DocumentVO params = new DocumentVO();
		dlg.setParams(params);
		
		if(dlg.open() < 1) {
			DBObject obj = (DBObject) params.dbobj;
			this.getDBCollection(item).insert(obj);
			this.refreshMainTreeInfo();
		}
	}
	
	public void openEditDocumentDlg(String ref, TreeItem item, Shell parent) throws Exception{
		
		DocumentDlg dlg = new DocumentDlg(parent);
		DocumentVO params = new DocumentVO();
		
		DBObject qry = (DBObject) JSON.parse(ref);
		
		params.dbobj = (DBObject) JSON.parse(ref);
		
		dlg.setParams(params);
		
		if(dlg.open() < 1) {
			DBObject obj = (DBObject) params.dbobj;
			this.getDBCollection(item).remove(qry);//better approach?
			this.getDBCollection(item).insert(obj);//better approach?
		}
	}		
	
	public DBCollection getDBCollection(TreeItem item) {
		DBCollection ret = null;
		
		if(item.getData(TIPO) != null) {
			Mongo server = servers.get(item.getParentItem().getParentItem());
			ret = server.getDB(item.getParentItem().getData(NOME).toString()).
					getCollection(item.getData(NOME).toString()); 
		} else {
			ret = (DBCollection) item.getData("db");
		}
		
		return ret;
	}	
	
	public void openNewConnectionDlg(Shell parent) {
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			NewConnectionDlg dlg = new NewConnectionDlg(parent);
			dlg.setParams(params);		
			if(dlg.open() < 1) {
				String name = params.get("name").toString(); 
				String host = params.get("host").toString(); 
				String port = params.get("port").toString();
				String username = params.get("user") !=null?params.get("user").toString():null; 
				String password = params.get("pass") != null?params.get("pass").toString():null;
				
				for(Server s : cfg.getServers()) {
					if(s.getName().equalsIgnoreCase(name)) {
						MessageDialog.openError(parent, "Error", "Connection Name is already being used.");
						return;
					}
				}
				
				this.cfg.addServer(name, host, port, username, password);
				this.cfg.save();
				this.refreshMainTree();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}	
	
	public void createDatabase(TreeItem parent, String dbname) throws Exception {
		Mongo server = servers.get(parent);
		
		for(String n : server.getDatabaseNames()) {
			if(n.equalsIgnoreCase(dbname)) {
				throw new Exception("Database already exists!");
			}
		}
		
		if(server != null) {
			createDataBaseItem(parent, dbname, server);
			this.refreshMainTreeInfo();
		}
	}

	private void createCollection(TreeItem parent, String name, int size, int max, boolean capped) {
		Mongo server = servers.get(parent.getParentItem());
		
		if(server != null) {
			DB db = server.getDB(parent.getData(NOME).toString());
			
			BasicDBObject options = new BasicDBObject();
			
			if(size > 0) {
				options.append(SIZE, size);
			}
			
			if(max > 0) {
				options.append(MAX, max);
			}
			
			if(capped) {
				options.append(CAPPED, capped);
			}
			
			db.createCollection(name, options);//DBCollection col = 
			
			createCollectionItem(parent, db, name);
			
			this.refreshMainTreeInfo();
		}
	}	
	
	public void openNewCollectionDlg(TreeItem item, Shell shell) {
		NewCollectionDlg dlg = new NewCollectionDlg(shell);
		HashMap<String, Object> params = new HashMap<String, Object>();
		dlg.setParams(params);
		
		if(dlg.open() < 1) {
			String name = params.get(NAME).toString();
			int size = Integer.parseInt(params.get(SIZE).toString());
			int max = Integer.parseInt(params.get(MAX).toString());
			boolean capped = (Boolean) params.get(CAPPED);
			
			createCollection(item, name, size, max, capped);
			
		}
	}
	
	public void removeDocument(String data, TreeItem item, Shell shell) {
		DBObject qry = (DBObject) JSON.parse(data);
		this.getDBCollection(item).remove(qry);
		this.refreshMainTreeInfo();
	}	
	
	public void removeCollectionItem(TreeItem item, Shell shell) {
		if(MessageDialog.openConfirm(shell, "Remove Item", "Are you sure you want to remove this item? ( cannot be undone )")) {
			Mongo server = servers.get(item.getParentItem().getParentItem());
			DB db = server.getDB(item.getParentItem().getData(NOME).toString());
			DBCollection col = db.getCollection(item.getData(NOME).toString());			
			col.drop();
			item.dispose();
			this.refreshMainTreeInfo();
		}
	}

	public void removeDatabaseItem(TreeItem item, Shell shell) {
		if(MessageDialog.openConfirm(shell, "Remove Item", "Are you sure you want to remove this item? ( cannot be undone )")) {
			Mongo server = servers.get(item.getParentItem());
			DB db = server.getDB(item.getData(NOME).toString());
			db.dropDatabase();
			item.dispose();
			this.refreshMainTreeInfo();
		}
	}	
	
	public void removeConnectionItem(TreeItem item, Shell shell) {
		if(MessageDialog.openConfirm(shell, "Remove Item", "Are you sure you want to remove this item? ( cannot be undone )")) {
			this.cfg.removeServer(item.getText());
			this.cfg.save();
			item.dispose();
			this.refreshMainTreeInfo();
		}
	}	
	
	public void closeDataBase(TreeItem item) {
		item.setData(ABERTO, false);
		item.setItemCount(0);
	}
	
	public void closeServer(TreeItem item) {
		item.setItemCount(0);
		Mongo s = servers.remove(item);
		s.close();
	}

	public void openUsersDlg() {
		
	}	
	
	public void openNewUserDlg(TreeItem item, Shell shell) {
		
	}
	
	public void openIndexesDlg(TreeItem item, Shell shell) {
		
	}	
}
