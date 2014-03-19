package com.fandome.listadapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.android.bitmapfun.util.ImageFetcher;
import com.fandome.application.R;
import com.fandome.views.ImageViewWithProcessIndicator;

public class GalleryAdapter extends BaseAdapter {
	protected List<String> images = new ArrayList<String>();
	private LayoutInflater layoutInflater;
	private int imageHeight;
	private ImageFetcher imageFetcher;
	
	protected Context context;

    public GalleryAdapter(Context ctx, int imageHeight, int imageWidth) {
        context = ctx;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imageHeight = imageHeight;
        imageFetcher = new ImageFetcher(context, imageWidth, imageHeight);
    }
    
	public int getCount() {
		return images.size();
	}

	public Object getItem(int pos) {
		return images.get(pos);
	}

	public long getItemId(int position) {
		return 0;
	}
	
	

	public View getView(int position, View convertView, ViewGroup parent) {
		final ImageViewWithProcessIndicator result;
        if (convertView == null) {
        	result = (ImageViewWithProcessIndicator)layoutInflater.inflate(R.layout.thumbnails_game_images, null);
        	result.showImage();
        	result.setLayoutParams(new GridView.LayoutParams(LayoutParams.MATCH_PARENT, imageHeight));
        	result.getImageView().setScaleType(ImageView.ScaleType.CENTER_CROP);
        	result.setPadding(8, 8, 8, 8);
        }
        else{
        	result = (ImageViewWithProcessIndicator)convertView;
        }
        String imageId = (String)getItem(position);
        imageFetcher.loadImage(imageId, result);
        	
        return result;
	}
	
	public void addNewImage(String imageId){
		if(!images.contains(imageId)){
			images.add(0, imageId);
			notifyDataSetChanged();
		}
	}
		
	

}
