package com.fandome.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fandome.application.R;
import com.fandome.intefaces.IFinishNotify;

public class ImageViewWithProcessIndicator extends LinearLayout{
	private RelativeLayout loadingPanel;
	private RecyclingImageView imageView;
	private String lastUrl;
	
	
	public ImageViewWithProcessIndicator(Context context) {
		this(context, null);
		
	}
	
	public ImageViewWithProcessIndicator(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public ImageViewWithProcessIndicator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		LayoutInflater li = LayoutInflater.from(context);
		li.inflate(R.layout.image_with_process, this);
		
		if(!isInEditMode()){
			
			loadingPanel = (RelativeLayout)findViewById(R.id.loadingPanel);
			setImageView((RecyclingImageView)findViewById(R.id.lazyImageView1));
			loadingPanel.setVisibility(GONE);
		}
	}
	
//	public void setImageUrl(final String url){
//		if(!url.equals(getLastUrl())){
//			showAsLoading();
//			lazyImageView.setImageByUrl(url, new IFinishNotify<Void>() {
//				
//				public void onFinished(Void result) {
//					setLastUrl(url);
//					showImage();
//				}
//
//				public void onError(Exception e) {
//					throw new RuntimeException(e.getMessage(), e);
//				}
//			});
//		}
//	}
	
	public void setImage(Drawable image){
		getImageView().setImageDrawable(image);
		showImage();
	}
	
	public void showAsLoading(){
		loadingPanel.setVisibility(VISIBLE);
		getImageView().setVisibility(GONE);
	}
	
	public void showImage(){
		loadingPanel.setVisibility(GONE);
		getImageView().setVisibility(VISIBLE);
	}

//	public LazyImageView getLazyImageView() {
//		return lazyImageView;
//	}

	public String getLastUrl() {
		return lastUrl;
	}

	public void setLastUrl(String lastUrl) {
		this.lastUrl = lastUrl;
	}

	public RecyclingImageView getImageView() {
		return imageView;
	}

	public void setImageView(RecyclingImageView imageView) {
		this.imageView = imageView;
	}



	public void onAsyncImageSet() {
		if(callback != null){
			callback.onFinished(getImageView().getDrawable());
		}
	}
	
	private IFinishNotify<Drawable> callback;
	
	public void setOnAsyncImageSet(IFinishNotify<Drawable> callback){
		this.callback = callback;
	}
	
	
}
