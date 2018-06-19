package br.upe.basileiro.models;

import java.sql.Timestamp;
import java.util.Date;

/* This object will be marshall and sent between storm bolts  */
public class UnitData {
	
	private String message;
	private Date timestamp;
	private String classification;
	private String type;
	
	public UnitData(String message, Timestamp timestamp, String classification, String type) {
		this.setMessage(message);
		this.setTimestamp(timestamp);
		this.setClassification(classification);
		this.setType(type);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
