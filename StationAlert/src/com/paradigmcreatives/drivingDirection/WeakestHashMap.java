package com.paradigmcreatives.drivingDirection;

import java.util.HashMap;
import java.util.WeakHashMap;

public class WeakestHashMap extends WeakHashMap<String, String>{
	public String key;
	public String name;
	public WeakestHashMap(String key, String name){
		super();
		this.key = key;
		this.name = name;
	}
	
}
