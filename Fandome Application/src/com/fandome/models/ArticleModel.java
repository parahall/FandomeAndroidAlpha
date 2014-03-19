package com.fandome.models;

import java.io.Serializable;


public class ArticleModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2476393018116978862L;
	private String text;
	private String description;
	private String id;
	private String imageUrl;
	private String title;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription(){
		return description;
	}
	

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}
