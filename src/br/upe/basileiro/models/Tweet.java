package br.upe.basileiro.models;

import java.io.Serializable;
import java.util.Date;

public class Tweet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;
	private int favoriteCount;
	private int retweetCount;
	private Date date;
	private String classification;
	
	public Tweet(String message, int favoriteCount, int retweetCount, Date date) {
		this.setMessage(message);
		this.setFavoriteCount(favoriteCount);
		this.setRetweetCount(retweetCount);
		this.setDate(date);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getFavoriteCount() {
		return favoriteCount;
	}

	public void setFavoriteCount(int favoriteCount) {
		this.favoriteCount = favoriteCount;
	}

	public int getRetweetCount() {
		return retweetCount;
	}

	public void setRetweetCount(int retweetCount) {
		this.retweetCount = retweetCount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}
	
}
