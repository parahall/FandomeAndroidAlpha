package com.fandome.communication;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;

import com.fandome.infra.ListHelper;
import com.fandome.infra.ParseCache;
import com.fandome.models.FormationModel;
import com.fandome.models.GameDetailsModel;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class GameDetailsManager {
	private static GameDetailsManager instance = new GameDetailsManager();

	private GameDetailsManager() {
	}

	public static GameDetailsManager getInstance() {
		return instance;
	}
	
	public List<GameDetailsModel> map(List<ParseObject> gameDetails) throws ParseException {
		List<GameDetailsModel> result = new ArrayList<GameDetailsModel>();
		if (gameDetails != null) {
			for (ParseObject gameDetail : gameDetails) {
				result.add(map(gameDetail));
			}
		}
		return result;
	}
	public GameDetailsModel map(ParseObject gameDetail) throws ParseException {
		return map(gameDetail, false);
	}
	public GameDetailsModel map(ParseObject gameDetail, boolean isHome) throws ParseException {
		GameDetailsModel result = new GameDetailsModel();
		result.setId(gameDetail.getObjectId());
		result.setIsHome(isHome);
		result.setFormation(new FormationModel(ListHelper.convert(gameDetail.getJSONArray("formation"))));
		result.setLineup(PlayersManager.getInstance().map(getLineup(gameDetail), isHome));
		
		return result;
	}

	private List<ParseObject> getLineup(ParseObject gameDetail) throws ParseException{
		List<ParseObject> result = new ArrayList<ParseObject>();
		ParseQuery query = gameDetail.getRelation("lineup").getQuery();
		query.orderByAscending("lineupPosition");
		ParseCache.setCachePolicy(query);
		result = query.find();
		return result;
	}

	public void getAll(final ICallback callback) {
				
		new AsyncTask<Void, Void, Result<List<GameDetailsModel>>>(){

			@Override
			protected Result<List<GameDetailsModel>> doInBackground(Void... params) {
				ParseQuery query = getQuery();
				ParseCache.setCachePolicy(query);

				List<GameDetailsModel> gameDetails = null;
				try {
					List<ParseObject> gameDetailsResult = query.find();
					gameDetails = map(gameDetailsResult);
				} catch (ParseException e) {
					return new Result<List<GameDetailsModel>>(e);
				}
				
				return new Result<List<GameDetailsModel>>(gameDetails);
			}
			
			protected void onPostExecute(Result<List<GameDetailsModel>> result) {
				if(result.getError() != null)
					callback.onError(result.getError());
				else
					callback.ongameDetailsArrived(result.getResult());
		     }
			
		}.execute();
		
	}

	public void get(String id, final ICallback callback) {
		new AsyncTask<String, Void, Result<GameDetailsModel>>(){

			@Override
			protected Result<GameDetailsModel> doInBackground(String... params) {
				ParseQuery query = getQuery();
				ParseCache.setCachePolicy(query);

				GameDetailsModel gameDetails = null;
				try {
					ParseObject gameDetailResult = query.get(params[0]);
					gameDetails = map(gameDetailResult);
				} catch (ParseException e) {
					return new Result<GameDetailsModel>(e);
				}
				
				return new Result<GameDetailsModel>(gameDetails);
			}
			
			protected void onPostExecute(Result<GameDetailsModel> result) {
				if(result.getError() != null)
					callback.onError(result.getError());
				else
					callback.ongameDetailArrived(result.getResult());
		     }
			
		}.execute();
	}

	private ParseQuery getQuery() {
		ParseQuery query = new ParseQuery("GameDetails");
		ParseCache.setCachePolicy(query);
		return query;
	}

	public interface ICallback {
		void ongameDetailsArrived(List<GameDetailsModel> gameDetails);

		void ongameDetailArrived(GameDetailsModel gameDetail);
		void onError(Exception e);
	}
}
