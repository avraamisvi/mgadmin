package br.org.isvi.mgadmin.data.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tree {

	Map<String, Object> data = new HashMap<>();
	List<TreeItem> items = new ArrayList<>();;
	
	public Tree(TreeItem item) {
		this.add(item);
	}

	public void add(TreeItem item) {
		items.add(item);
	}

	public int count() {
		return items.size();
	}
	
	public TreeItem get(int index) {
		return items.get(index);
	}
	
	public void setData(String key, Object value) {
		data.put(key, value);
	}
	
	public Object getData(String key) {
		return data.get(key);
	}

	public List<TreeItem> getItems() {
		return items;
	}
	
}
