package com.fandome.communication;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;

import com.fandome.infra.Keys;
import com.fandome.infra.MemoryCache;
import com.fandome.infra.ParseCache;
import com.fandome.models.TeamModel;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class TeamsManager {
	private static TeamsManager instance = new TeamsManager();

	private TeamsManager() {
	}

	public static TeamsManager getInstance() {
		return instance;
	}
	
	public List<TeamModel> map(List<ParseObject> teams) {
		List<TeamModel> result = new ArrayList<TeamModel>();
		if(teams != null){
			for (ParseObject team : teams) {
				TeamModel mapedTeam = map(team);
				result.add(mapedTeam);
			}
		}
		return result;
	}

	public TeamModel map(ParseObject team) {
		TeamModel result = new TeamModel();
		result.setId(team.getObjectId());
		result.setDraws(team.getInt("draw"));
		result.setWins(team.getInt("win"));
		result.setRank(team.getInt("rank"));
		result.setGames(team.getInt("played"));
		result.setLoses(team.getInt("lose"));
		result.setGoalDiff(team.getInt("gameDifference"));
		
		result.setName(team.getString("teamName"));
		result.setNickname(team.getString("nickName"));
		result.setLogoUrl(team.getString("logoUrl"));
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public void getTeams(final ICallback callback){
		Object memoryCache = MemoryCache.getInstance().get(Keys.Teams.TeamsListKey);
		if(memoryCache != null){
			callback.onTeamsArrived((ArrayList<TeamModel>)memoryCache);
			return;
		}
		
		new AsyncTask<Void, Void, Result<List<TeamModel>>>(){
			@Override
			protected Result<List<TeamModel>> doInBackground(Void... params) {				
				ParseQuery query = new ParseQuery("Team");
				ParseCache.setCachePolicy(query);
				List<TeamModel> teamList = null;
				try {
					List<ParseObject> teamsFromServer = query.find();
					teamList = map(teamsFromServer);
					
				} catch (ParseException e) {
					return new Result<List<TeamModel>>(e);
				}
		        
		        MemoryCache.getInstance().add(Keys.Teams.TeamsListKey, teamList);
		        for (TeamModel teamModel : teamList) {
		        	MemoryCache.getInstance().add(teamModel.getId(), teamModel);
				}
		        
				return new Result<List<TeamModel>>(teamList);
			}
			
			protected void onPostExecute(Result<List<TeamModel>> result) {
				if(result.getError() != null)
		        	callback.onError(result.getError());
		        else
		        	callback.onTeamsArrived(result.getResult());
		     }
		}.execute();
	}
	
	public void getTeam(String teamId, final ICallback callback){
		TeamModel memoryCache = MemoryCache.getInstance().getAs(teamId, TeamModel.class);
		if(memoryCache != null){
			callback.onTeamArrived(memoryCache);
			return;
		}
		
		new AsyncTask<String, Void, Result<TeamModel>>(){
			@Override
			protected Result<TeamModel> doInBackground(String... params) {				
				ParseQuery query = new ParseQuery("Team");
				TeamModel team = null;
				try {
					team  = map(query.get(params[0]));
				} catch (ParseException e) {
					return new Result<TeamModel>(e);
				}
		        
		        MemoryCache.getInstance().add(params[0], team);
				return new Result<TeamModel>(team);
			}
			
			protected void onPostExecute(Result<TeamModel> result) {
				if(result.getError() != null)
		        	callback.onError(result.getError());
		        else
		        	callback.onTeamArrived(result.getResult());
		     }
		}.execute();
	}
	
	public interface ICallback{
		void onTeamsArrived(List<TeamModel> team);
		void onTeamArrived(TeamModel team);
		void onError(Exception e);
	}
}
