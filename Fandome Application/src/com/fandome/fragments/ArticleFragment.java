package com.fandome.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.fandome.application.ApplicationManager;
import com.fandome.application.R;
import com.fandome.communication.FacebookManager;
import com.fandome.communication.MyFandomeManager;
import com.fandome.infra.Keys;
import com.fandome.infra.MemoryCache;
import com.fandome.intefaces.IFinishNotify;
import com.fandome.models.ArticleModel;
import com.fandome.views.ImageViewWithProcessIndicator;

public class ArticleFragment extends BaseFragment {
	private TextView text;
	private TextView title;
	private ImageViewWithProcessIndicator image;
	private ArticleModel article;
	private ImageFetcher imageFetcher;

	private final String ARTICLE_RELATION = "ArticlesList";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		imageFetcher = new ImageFetcher(getActivity(), ApplicationManager.getResourceManager().getDimensionPixelSize(R.dimen.image_thumbnail_size));
		if (savedInstanceState != null) {
			article = MemoryCache.getInstance().getAs(
					savedInstanceState.getString(Keys.Articles.ArticleID),
					ArticleModel.class);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.single_article, container, false);
		title = (TextView) view.findViewById(R.id.title);
		text = (TextView) view.findViewById(R.id.articleText);
		image = (ImageViewWithProcessIndicator) view
				.findViewById(R.id.articleImage);

		if (article != null) {
			title.setText(article.getTitle());
			text.setText(article.getText());
			imageFetcher.loadImage(article.getImageUrl(), image);
		}

		return view;
	}

	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (article != null) {
			MemoryCache.getInstance().add(article.getId(), article);
			outState.putString(Keys.Articles.ArticleID, article.getId());
			
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public ArticleModel getArticle() {
		return article;
	}

	public void setArticle(ArticleModel article) {
		this.article = article;
	}
	
	
	public void onShareClicked(View v){
		publishArticle(getActivity());
	}
	
	public boolean onOptionsItemSelected(Activity activity, MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.share:
			publishArticle(activity);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	private void publishArticle(Activity activity) {
		FacebookManager instance = FacebookManager.getInstance();
		instance.facebookConnect(activity);
		
		instance.postArticle(activity, getArticle(), new IFinishNotify<String>() {
			
			public void onFinished(String result) {
				
			}
			
			public void onError(Exception e) {
				
			}
		});
	}
	
	public void onMyFandomeBtnClicked(boolean isChecked){
		MyFandomeManager instance = MyFandomeManager.getInstance();
		if (isChecked) {
			instance.addArticleId(article.getId());
		} else {
			instance.remove(article.getId(),ARTICLE_RELATION);
		}
	}
	
	public boolean getMyFandomeState(){
		return MyFandomeManager.getInstance().contain(article.getId());
	}
    	
	
}
