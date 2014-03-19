package com.fandome.receivers;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.fandome.infra.Keys;
import com.fandome.infra.ListHelper;
import com.fandome.infra.MemoryCache;
import com.fandome.models.ArticleModel;
import com.fandome.services.ResourcesDownloadService;

public class ResourceDownloadReceiver extends BroadcastReceiver {
	public static final String ResourceManagerResult = "ResourceManagerResult"; 
	
	private Callback callback;
	
	public ResourceDownloadReceiver(Callback callback){
		this.callback = callback;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("ResourceManagerResult")) {
			String operation = intent.getExtras().getString(ResourcesDownloadService.OperationKeyword);
			if(operation.equals(ResourcesDownloadService.Operation.GalleryImagesList)){
				String imagesList = intent.getExtras().getString(Keys.Images.ImagesList);
				callback.onImagesList(ListHelper.fromStringToList(imagesList));
			}
			else if(operation.equals(ResourcesDownloadService.Operation.ArticlesList)){
				List<ArticleModel> articles = (List<ArticleModel>)MemoryCache.getInstance().get(Keys.Articles.ArticleListKey);
				callback.onArticlesList(articles);
			}
		}
	}
	
	public IntentFilter getIntentFilter(){
		return new IntentFilter(ResourceManagerResult);
	}
	
	
	public interface Callback{
		void onImagesList(List<String> imagesList);
		void onArticlesList(List<ArticleModel> articles);
	}
}
