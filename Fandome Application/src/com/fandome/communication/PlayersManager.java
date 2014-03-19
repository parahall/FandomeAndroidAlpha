package com.fandome.communication;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;

import com.fandome.infra.ParseCache;
import com.fandome.models.PlayerModel;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class PlayersManager {

	private static PlayersManager instance = new PlayersManager();

	private PlayersManager() {
	}

	public static PlayersManager getInstance() {
		return instance;
	}
	
	public List<PlayerModel> map(List<ParseObject> players) {
		return map(players, false);
	}
	
	public List<PlayerModel> map(List<ParseObject> players, boolean isHome) {
		List<PlayerModel> result = new ArrayList<PlayerModel>();
		if(players != null){
			for (ParseObject article : players) {
				result.add(map(article, isHome));
			}
		}
		return result;
	}
	public PlayerModel map(ParseObject player) {
		return map(player, false);
	}
	public PlayerModel map(ParseObject player, boolean isHome) {
		PlayerModel result = new PlayerModel();
		result.setId(player.getObjectId());
		result.setFirstName(player.getString("firstName"));
		result.setLastName(player.getString("lastName"));
		result.setBirthdate(player.getString("birthdate"));
		result.setPosition(player.getString("position"));
		result.setImageUrl(player.getString("imageURL"));
		result.setNumber(player.getInt("playerNumb"));
		result.setGoals(player.getInt("goalsNumb"));
		result.setGames(player.getInt("gamesPlayed"));
		result.setAssists(player.getInt("assists"));
		result.setRedCards(player.getInt("redCards"));
		result.setIsHome(isHome);
		return result;
	}

	public void getAll(final ICallback callback) {
		new AsyncTask<Void, Void, Result<List<PlayerModel>>>() {

			@Override
			protected Result<List<PlayerModel>> doInBackground(Void... params) {
				ParseQuery query = getQuery();
				ParseCache.setCachePolicy(query);

				List<PlayerModel> palyers = null;
				try {
					List<ParseObject> palyersResult = query.find();
					palyers = map(palyersResult);
				} catch (ParseException e) {
					return new Result<List<PlayerModel>>(e);
				}

				return new Result<List<PlayerModel>>(palyers);
			}

			protected void onPostExecute(Result<List<PlayerModel>> result) {
				if(result.getError() != null)
					callback.onError(result.getError());
				else
					callback.onpalyersArrived(result.getResult());
			}

		}.execute();
	}

	public void get(String id, final ICallback callback) {
		new AsyncTask<String, Void, Result<PlayerModel>>() {

			@Override
			protected Result<PlayerModel> doInBackground(String... params) {
				ParseQuery query = getQuery();
				ParseCache.setCachePolicy(query);

				PlayerModel palyer = null;
				try {
					ParseObject palyerResult = query.get(params[0]);
					palyer = map(palyerResult);
				} catch (ParseException e) {
					return new Result<PlayerModel>(e);
				}

				return new Result<PlayerModel>(palyer);
			}

			protected void onPostExecute(Result<PlayerModel> result) {
				if(result.getError() != null)
					callback.onError(result.getError());
				else
					callback.onpalyerArrived(result.getResult());
			}

		}.execute();
	}

	private ParseQuery getQuery() {
		ParseQuery query = new ParseQuery("SoccerPlayer");
		ParseCache.setCachePolicy(query);
		return query;
	}

	public interface ICallback {
		void onpalyersArrived(List<PlayerModel> palyers);

		void onpalyerArrived(PlayerModel palyer);
		void onError(Exception e);
	}

	
	
}
