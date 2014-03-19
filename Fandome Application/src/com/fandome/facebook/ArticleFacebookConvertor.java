package com.fandome.facebook;

import android.os.Bundle;

import com.fandome.models.ArticleModel;

public class ArticleFacebookConvertor extends BaseFacebookObjectConvertor<ArticleModel> {

	public ArticleFacebookConvertor() {
		super(ArticleModel.class);
	}

	@Override
	protected Bundle convertImp(ArticleModel article) {
		Bundle result = new Bundle();
		result.putString("name", article.getTitle());
		result.putString("caption", "");
		result.putString("description", article.getDescription());
		result.putString("link", "http://Sport5.co.il");
		result.putString("picture", article.getImageUrl());
		return result;
	}

}
