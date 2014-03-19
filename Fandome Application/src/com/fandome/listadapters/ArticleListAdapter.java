package com.fandome.listadapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.bitmapfun.util.ImageFetcher;
import com.fandome.application.R;
import com.fandome.models.ArticleModel;
import com.fandome.views.ImageViewWithProcessIndicator;

public class ArticleListAdapter extends BaseAdapter{

	private List<ArticleModel> articles;
	private LayoutInflater layoutInflater;
	private ImageFetcher imageFetcher;
	
    public ArticleListAdapter(Context context, int imageSize) {
        super();
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        articles = new ArrayList<ArticleModel>();
        imageFetcher = new ImageFetcher(context, imageSize);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;

        if(v == null){
	        v = layoutInflater.inflate(R.layout.listview_single_article_layout, null);
        }
        
        ArticleModel article = (ArticleModel)getItem(position);
        
        TextView description;
        final ImageViewWithProcessIndicator imageView;
        TextView title;
        
        title = (TextView)v.findViewById(R.id.title1);
    	description = (TextView)v.findViewById(R.id.articleText);
    	imageView = (ImageViewWithProcessIndicator)v.findViewById(R.id.articleImage);
    	
        if(article != null){
        	title.setText(article.getTitle());
        	description.setText(article.getDescription());
        	imageFetcher.loadImage(article.getImageUrl(), imageView);
        }
        
        return v;           
    }


	public int getCount() {
		return articles.size();
	}

	public Object getItem(int arg0) {
		return articles.get(arg0);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public void add(ArticleModel articleModel) {
		if(articleModel == null)
			return;
		boolean isExist = false;
		for (ArticleModel article : articles) {
			if(article.getId().equals(articleModel.getId())){
				isExist = true;
				break;
			}
		}
		if(!isExist)
			articles.add(articleModel);
	}
}
