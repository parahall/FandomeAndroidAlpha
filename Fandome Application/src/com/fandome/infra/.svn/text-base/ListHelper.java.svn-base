package com.fandome.infra;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

public class ListHelper {
	public static <T> ArrayList<T> convert(T[] array){
		ArrayList<T> list = new ArrayList<T>();
		for (int i = 0; i < array.length; i++) {
			list.add(array[i]);
		}
		return list;
	}
	
	public static String fromListToString(List<String> list){
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for (String string : list) {
			if(!isFirst)
				sb.append(",");
			sb.append(string);
			isFirst = false;
		}
		return sb.toString();
	}
	
	public static List<String> fromStringToList(String str){
		List<String> result = new ArrayList<String>();
		String[] array = str.split(",");
		for (String string : array) {
			result.add(string);
		}
		return result;
	}
	
	public static <T> boolean contain(T[] array, T object){
		for (T t : array) {
			if(t.equals(object))
				return true;
		}
		return false;
	}
	
	public static int[] convert(JSONArray intArray){
		int arraySize = intArray.length();
		int[] array = new int[arraySize];
		for (int i = 0; i < intArray.length(); i++) {
			try {
				array[i] = intArray.getInt(i);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return array;
	}
}
