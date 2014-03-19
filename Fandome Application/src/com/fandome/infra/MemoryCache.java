package com.fandome.infra;

import java.util.Date;
import java.util.HashMap;

import android.util.Log;

public class MemoryCache {
	private static MemoryCache instance = new MemoryCache();

	private MemoryCache() {
	}

	public static MemoryCache getInstance() {
		return instance;
	}
	
	private HashMap<String, Object> cache = new HashMap<String, Object>();
	private HashMap<String, Date> cacheTime = new HashMap<String, Date>();
	
	public void add(String key, Object value){
		cache.put(key, value);
		cacheTime.put(key, new Date());
		Log.d("MemoryCache", String.format("add %s key to memory cache", key));
	}
	
	public boolean contain(String key){
		return get(key) != null;
	}
	
	public Object get(String key){
		return get(key, -1);
	}
	
	public Object get(String key, int cacheTimeOut){
		if(cacheTimeOut>=0){
			Date time = getTime(key);
			if(time == null || DatesHelper.isTimedOut(time, cacheTimeOut))
				return null;
		}
		
		Log.d("MemoryCache", String.format("Seek %s key on cache", key));
		Object object = cache.get(key);
		Log.d("MemoryCache", String.format("key %s %s found", key, object != null ? "was" : "wasn't"));
		return object;
	}
	
	
	public <T> T getAs(String key, Class<T> returnType){
		return getAs(key, returnType, -1);
	}
	
	public <T> T getAs(String key, Class<T> returnType, int cacheTimeOut){
		Object value = get(key, cacheTimeOut);
		if(value == null || value.getClass()==returnType.getClass())
			return null;
		
		return returnType.cast(value);
	}
	
	public Date getTime(String key){
		return cacheTime.get(key);
	}

	public void remove(String key) {
		if(cache.containsKey(key)){
			cache.remove(key);
			cacheTime.remove(key);
			Log.d("MemoryCache", String.format("remove %s key from memory cache", key));
		}
		else
			Log.d("MemoryCache", String.format("Remove - the key %s wasn't found", key));
	}
}
