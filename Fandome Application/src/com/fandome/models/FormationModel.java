package com.fandome.models;

import java.io.Serializable;

public class FormationModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7221288745965242352L;
	private int[] formation;

	public FormationModel(int[] formation) {
		super();
		this.setFormation(formation);
	}

	public int[] getFormation() {
		return formation;
	}

	public void setFormation(int[] formation) {
		this.formation = formation;
	}
}
