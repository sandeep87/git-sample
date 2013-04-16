package com.paradigmcreatives.listcustomdemo;

public class City {
	private String name;
	private String urlWiki;
	private String image;

	public City(String name, String urlWiki, String image) {
		super();
		this.name = name;
		this.urlWiki = urlWiki;
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String nameText) {
		name = nameText;
	}

	public String getUrlwiki() {
		return urlWiki;
	}

	public void setUrlwiki(String urlWiki) {
		this.urlWiki = urlWiki;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
