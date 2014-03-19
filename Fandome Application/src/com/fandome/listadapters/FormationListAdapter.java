package com.fandome.listadapters;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fandome.models.GameDetailsModel;
import com.fandome.views.TeamFormationView;

public class FormationListAdapter extends BaseAdapter{
	private Context context;
	private List<GameDetailsModel> gameDetails;
	
    public FormationListAdapter(Context context, List<GameDetailsModel> gameDetails) {
        super();
        this.context = context;
        this.gameDetails = gameDetails;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;

        if(v == null)
        {
            TeamFormationView teamFormationView = new TeamFormationView(context);
            
            GameDetailsModel gameDetails = (GameDetailsModel)getItem(position);
            
            if(gameDetails != null){
            	teamFormationView.setGameDetails(gameDetails);
            }
            
            v = teamFormationView;
        }
        
        return v;           
    }

	public void onFinished(Void result) {
		notifyDataSetChanged();
	}

	public int getCount() {
		return gameDetails.size();
	}

	public Object getItem(int arg0) {
		return gameDetails.get(arg0);
	}

	public long getItemId(int arg0) {
		return arg0;
	}
}
