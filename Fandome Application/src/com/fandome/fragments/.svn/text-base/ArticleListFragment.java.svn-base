package com.fandome.fragments;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.fandome.application.ArticleActivity;
import com.fandome.application.R;
import com.fandome.infra.GeneralUtility;
import com.fandome.infra.Keys;
import com.fandome.listadapters.ArticleListAdapter;
import com.fandome.models.ArticleModel;
import com.fandome.receivers.ResourceDownloadReceiver;
import com.fandome.services.ResourcesDownloadService;

public class ArticleListFragment extends BaseFragment implements ResourceDownloadReceiver.Callback{
	private ListView list;
	private ProgressDialog progressDialog;
	private ArticleListAdapter arrayAdapter;
	private ResourceDownloadReceiver resourceDownloadReceiver;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
 
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        resourceDownloadReceiver = new ResourceDownloadReceiver(this);
        int imageSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
        arrayAdapter = new ArticleListAdapter(getActivity(), imageSize);
		list.setAdapter(arrayAdapter);
    	list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent intent = new Intent(getActivity(), ArticleActivity.class);
				intent.putExtra(Keys.Articles.ArticleClicked, position);
				startActivity(intent);
			}
		});
    	progressDialog = GeneralUtility.getCancelableProgressDialog("Articles", "Please wait while articles are loaded", getActivity());
    }
    
    public void onResume(){
		super.onResume();
		getActivity().registerReceiver(resourceDownloadReceiver, resourceDownloadReceiver.getIntentFilter());	
		Intent intent = new Intent(getActivity(), ResourcesDownloadService.class);
		intent.setAction(ResourcesDownloadService.Operation.ArticlesList);
		getActivity().startService(intent);
	}
	
	public void onPause(){
		super.onPause();
		getActivity().unregisterReceiver(resourceDownloadReceiver);
	}
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        
    	View view = inflater.inflate(R.layout.articles_layout, container, false);
    	list = (ListView)view.findViewById(R.id.articlesList);
		return view;
    }

	public void onImagesList(List<String> imagesList) {
		// TODO Auto-generated method stub
		
	}

	public void onArticlesList(List<ArticleModel> articles) {
		if(articles != null){
	    	for (ArticleModel articleModel : articles) {
				arrayAdapter.add(articleModel);
			}
	    	arrayAdapter.notifyDataSetChanged();
    	}
		if(progressDialog != null && progressDialog.isShowing())
			progressDialog.dismiss();
	}
    
	
}
