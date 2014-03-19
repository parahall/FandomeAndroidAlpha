package com.fandome.communication;

import java.util.ArrayList;
import java.util.List;

import com.fandome.infra.ParseCache;
import com.fandome.models.ArticleModel;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ArticlesManager {
	private static ArticlesManager instance = new ArticlesManager();
	
	private ArticlesManager()
	{
		
	}
	
	public static ArticlesManager getInstance(){
		return instance;
	}
	
	private List<ArticleModel> map(List<ParseObject> articles) {
		List<ArticleModel> result = new ArrayList<ArticleModel>();
		if(articles != null){
			for (ParseObject article : articles) {
				result.add(map(article));
			}
		}
		return result;
	}

	private ArticleModel map(ParseObject article) {
		ArticleModel result = new ArticleModel();
		result.setId(article.getObjectId());
		result.setTitle(article.getString("Title"));
		result.setText(article.getString("Text"));
		String description = article.getString("description");
		if(description.length()>180){
			description = description.substring(0, 180)+"...";
		}
		result.setDescription(description);
		result.setImageUrl(article.getString("imageLink"));
		return result;
	}
	
	
	public List<ArticleModel> getArticles() throws ParseException {
		ParseQuery query = new ParseQuery("Article");
		query.addDescendingOrder("createdAt");
		ParseCache.setCachePolicy(query);
		List<ArticleModel> articleList = map(query.find());
		return articleList;
	}
	
}
