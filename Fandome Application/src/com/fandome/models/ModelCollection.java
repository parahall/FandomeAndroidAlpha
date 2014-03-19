package com.fandome.models;

import java.util.List;

import com.fandome.intefaces.IKey;


public class ModelCollection<T> implements IKey{
	
	private List<T> list;
	private String key;
	
	public ModelCollection(){
		
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	
}
