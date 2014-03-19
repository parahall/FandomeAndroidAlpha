package com.fandome.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlayerModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -242658904368121308L;
	private String id;
	private String firstName = "";
	private String lastName = "";
	private int birthdate;
	private String imageUrl;
	private String position;
	private int number;
	private int goals;
	private int games;
	private int assists;
	private int redCards;
	private boolean isHome;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		Date date;
		try {
			 date = new SimpleDateFormat("dd/MM/yyyy").parse(birthdate);
			 Date sysDate = new Date();
			 long Age = sysDate.getTime()-date.getTime();
			 this.birthdate = (int) Math.round(Age/(1000*60*60*24*365.25));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getGoals() {
		return goals;
	}

	public void setGoals(int goals) {
		this.goals = goals;
	}

	public int getGames() {
		return games;
	}

	public void setGames(int games) {
		this.games = games;
	}
	
	public String getFullName(){
		return String.format("%s %s", firstName, lastName);
	}

	public boolean IsHome() {
		return isHome;
	}

	public void setIsHome(boolean isHome) {
		this.isHome = isHome;
	}

	public int getAssists() {
		return assists;
	}

	public void setAssists(int assists) {
		this.assists = assists;
	}

	public int getRedCards() {
		return redCards;
	}

	public void setRedCards(int redCards) {
		this.redCards = redCards;
	}
}
