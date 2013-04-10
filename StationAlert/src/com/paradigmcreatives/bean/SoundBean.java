package com.paradigmcreatives.bean;

public class SoundBean {
	
	private String name;
	private String uri;
	private String path;
	
	public SoundBean(String name, String uri, String path) {
		this.name = name;
		this.uri = uri;
		this.path = path;
	}
	
	
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUri() {
		return uri;
	}
	
	public void setUri(String uri) {
		this.uri = uri;
	}



	public String getPath() {
		return path;
	}



	public void setPath(String path) {
		this.path = path;
	}

}
