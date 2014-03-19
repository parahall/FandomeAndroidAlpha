package com.fandome.infra;

import com.parse.ParseQuery;
import com.parse.ParseQuery.CachePolicy;

public class ParseCache {
	public static final int HourInMiliseconds = 60*60*1000;
	
	public static void setCachePolicy(ParseQuery query){
		query.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);
		
		query.setMaxCacheAge(HourInMiliseconds);
	}
	
}
