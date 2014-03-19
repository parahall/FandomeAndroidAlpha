package com.fandome.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;

public class AlignmentListView extends ListView {
	private final String LOG_TAG = "AlignmentListView";
//	private ViewPager viewPager;
	
//	public void setViewPager(ViewPager viewPager){
//		this.viewPager = viewPager;
//	}
	
	private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
		
		public void onPageSelected(int arg0) {
			
		}
		
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}
		
		public void onPageScrollStateChanged(int arg0) {
			
		}
	};
	
	public AlignmentListView(Context context) {
		this(context, null);
	}
	
	public AlignmentListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public AlignmentListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
    public boolean onTouchEvent(MotionEvent event) {
//		return viewPager.onTouchEvent(event);
		// prevent taps
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			Log.d(LOG_TAG, "ActionDown");
			if(event.getHistorySize() == 0
				|| (event.getHistoricalX(0) == event.getX() 
				&& event.getHistoricalY(0) == event.getY())){
				Log.d(LOG_TAG, "Recognized as Click");
				return true;
			}
		}
		
		
		int top = getChildAt(0).getTop();
		if(event.getAction() == MotionEvent.ACTION_UP){
			int childCount = getChildCount();
			if(childCount == 0){
				return true;
			}
			else if(childCount == 1){
				smoothScrollTo(0);
				return true;
			}
			
			int firstItemDistance = Math.abs(top);
			int secondItemDistance = firstItemDistance;
			secondItemDistance = getChildAt(1).getTop();
			
			if(firstItemDistance < secondItemDistance) 
				smoothScrollTo(0);
			 else
				 smoothScrollTo(1);
			return true;
		}
		
		int topPtc = top / getChildAt(0).getHeight();
		pageChangeListener.onPageScrollStateChanged(ViewPager.SCROLL_STATE_DRAGGING);
		pageChangeListener.onPageScrolled(getFirstVisiblePosition(), topPtc, top);
		
		
		boolean superResult = super.onTouchEvent(event);
		Log.d(LOG_TAG, "Touch event pass to super " + superResult);
		return superResult;
	}
	
	private void smoothScrollTo(int index){
		//smoothScrollToPosition(index);
		int distance = getChildAt(index).getTop(); 
		pageChangeListener.onPageScrollStateChanged(ViewPager.SCROLL_STATE_SETTLING);
		smoothScrollBy(distance, 300);
		pageChangeListener.onPageScrollStateChanged(ViewPager.SCROLL_STATE_IDLE);
		pageChangeListener.onPageSelected(getFirstVisiblePosition());
	}
	
	public void setOnPageChangeListener(ViewPager.OnPageChangeListener pageChangeListener){
		this.pageChangeListener = pageChangeListener;
	}
	
}
