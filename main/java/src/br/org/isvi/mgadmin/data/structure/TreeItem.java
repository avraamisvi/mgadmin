package br.org.isvi.mgadmin.data.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeItem {
	
	Map<Integer, String> text = new HashMap<>();

	Map<String, Object> data = new HashMap<>();
	List<TreeItem> items = new ArrayList<>();;
	
	public TreeItem(Tree tree) {
		tree.add(this);
	}
	
	public TreeItem(TreeItem par) {
		par.add(this);
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
	
	public void setText(int index, String text) {
		this.text.put(index, text);
	}
	
	public String getText() {
		return text.get(0);
	}
	
	public String getText(int index) {
		return text.get(index);
	}
	
	public void setData(String key, Object value) {
		data.put(key, value);
	}
	
	public Object getData(String key) {
		return data.get(key);
	}

	public void setText(String text) {
		setText(0, text);
	}

	public List<TreeItem> getItems() {
		return items;
	}
}
