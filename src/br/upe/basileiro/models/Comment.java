package br.upe.basileiro.models;

import java.io.Serializable;

import com.google.api.client.util.DateTime;

public class Comment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String message;
	private DateTime date;
	private long likeCount;
	private String classification;
	
	public Comment(String message, DateTime date, long likeCount) {
		this.setMessage(message);
		this.setDate(date);
		this.setLikeCount(likeCount);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public DateTime getDate() {
		return date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}

	public long getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(long likeCount) {
		this.likeCount = likeCount;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}
	
}
