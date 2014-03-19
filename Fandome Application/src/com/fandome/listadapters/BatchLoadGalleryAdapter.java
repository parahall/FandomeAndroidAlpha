package com.fandome.listadapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public class BatchLoadGalleryAdapter extends GalleryAdapter implements OnScrollListener{
	public BatchLoadGalleryAdapter(Context ctx, int imageHeight, int imageWifth) {
		super(ctx, imageHeight, imageWifth);
	}

	private int batchSize = 5;
	private int imagesLoadedMarker = 0;
	private List<String> imageList = new ArrayList<String>();
	private int maxNumberOfBatches;
	private int firstItem = -1;
	private int startingShowImagesIndex;
	private int endingShowImagesIndex;
	
	public void setImageList(List<String> images) {
		imageList = images;
		maxNumberOfBatches = (int)Math.ceil(imageList.size() / (double)batchSize);
		startFetchImages();
		manageLoadedImages(0);
	}
	
	private boolean isNeedLoadNewBatch(int pos) {
		int numberOfLoadedBatch = (int)Math.ceil(imagesLoadedMarker / (double)batchSize);
		int batchGroupOfImageInPos = (pos / batchSize)+1;
		return numberOfLoadedBatch <= batchGroupOfImageInPos 
				&& numberOfLoadedBatch < maxBatches();
	}
	
	private int maxBatches() {
		return maxNumberOfBatches;
	}

	private void startFetchImages(){
		loadNextImageBatch();
	}
	
	private void loadNextImageBatch() {
		int loadSizeInCurrentBatch = Math.min(imageList.size() - imagesLoadedMarker, batchSize);
		int firstImageIndex = imagesLoadedMarker;
		int lastImageIndex = imagesLoadedMarker + loadSizeInCurrentBatch;
		imagesLoadedMarker += loadSizeInCurrentBatch;
		for (int i = firstImageIndex; i < lastImageIndex; i++) {
			addNewImage(imageList.get(i));
		}
	}
	
	
	private void manageLoadedImages(int currentItem){
		if(imageList.size()==0)
			return;
		startingShowImagesIndex = Math.max(0, currentItem-batchSize-1);
		endingShowImagesIndex = Math.min(images.size()-1, currentItem+batchSize-1);
		notifyCommandForItem(firstItem);
		for (int i = firstItem; i < images.size(); i++) {
			notifyCommandForItem(i);
		}
		for (int i = 0; i < firstItem; i++) {
			notifyCommandForItem(i);
		}
	}
	
	private void notifyCommandForItem(int itemIndex){
//		DrawableDetails item = images.get(itemIndex);
//		if(itemIndex>=startingShowImagesIndex 
//				&& itemIndex<=endingShowImagesIndex)
//			item.beReady();
//		else
//			item.freeImage();
	}
	
	@Override
	public Object getItem(int pos) {
		if(isNeedLoadNewBatch(pos))
			loadNextImageBatch();
		return super.getItem(pos);
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
			manageLoadedImages(firstItem);
		}
	}
	
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if(firstItem != firstVisibleItem){
			firstItem = firstVisibleItem;
		}
	}

		
}
