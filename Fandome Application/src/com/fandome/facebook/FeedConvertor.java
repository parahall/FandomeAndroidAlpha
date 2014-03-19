package com.fandome.facebook;

import android.os.Bundle;

public class FeedConvertor extends BaseFacebookObjectConvertor<FeedModel> {

	public FeedConvertor() {
		super(FeedModel.class);
	}

	@Override
	protected Bundle convertImp(FeedModel feed) {
		Bundle result = new Bundle();
		result.putString("name", feed.getName());
		result.putString("caption", feed.getCaption());
		result.putString("description", feed.getDescription());
		result.putString("link", feed.getLink());
		result.putString("picture", feed.getImage());
		return result;
	}

}
