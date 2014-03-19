package com.fandome.infra;

import android.util.Log;

public class Stopwatch {
	private static final String Tag = "Performance";
	private long startTime;
	private long stopTime;
	
	public static Stopwatch startNew(){
		Stopwatch result = new Stopwatch();
		result.restart();
		return result;
	}
	
	public Stopwatch restart(){
		this.startTime = System.currentTimeMillis();
		return this;
	}
	
	public long stop(String message){
		this.stopTime = System.currentTimeMillis();
		Log.d(Tag, message + " " + (stopTime - startTime) + " ms");
		return getTime();
	}
	
	public long stopAndRestart(String message){
		long result = stop(message);
		restart();
		return result;
	}
	
	public long getTime(){
		return stopTime - startTime;
	}
}
