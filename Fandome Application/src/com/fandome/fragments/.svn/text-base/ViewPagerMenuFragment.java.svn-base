package com.fandome.fragments;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.fandome.application.R;
import com.fandome.application.TabsAdapter;

public class ViewPagerMenuFragment extends BaseFragment{

	private ViewPager viewPager; 
	private TabHost tabHost;
	private TabsAdapter tabsAdapter;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        
    	View view = inflater.inflate(R.layout.viewpager_menu, container, false);
    	
    	tabHost = (TabHost)view.findViewById(android.R.id.tabhost);
        viewPager = (ViewPager)view.findViewById(R.id.pager);
    	
    	return view;
	}
 
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        tabHost.setup();
        
        tabsAdapter = new TabsAdapter(getActivity(), tabHost, viewPager);
        tabsAdapter.addTab(tabHost.newTabSpec("News").setIndicator("News"),
        		ArticleListFragment.class, null);
        tabsAdapter.addTab(tabHost.newTabSpec("Teams").setIndicator("Teams"),
        		TeamsFragment.class, null);
        tabsAdapter.addTab(tabHost.newTabSpec("Videos").setIndicator("Videos"),
        		ImagesFragment.class, null);
        
        if (savedInstanceState != null) {
            tabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
    }
}
