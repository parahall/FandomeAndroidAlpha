package com.fandome.infra;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatesHelper {
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	public static boolean isTimedOut(Date dateTime, int timeOut){
		return (new Date().getTime()-dateTime.getTime())>timeOut;
	}
	
	public static Date getDate(String dateStr) throws ParseException{
		return dateFormat.parse(dateStr);
	}
	
	public static String getNowAsString(){
		return getDate(new Date());
	}
	
	public static String getDate(Date date){
		return dateFormat.format(date);
	}
}
