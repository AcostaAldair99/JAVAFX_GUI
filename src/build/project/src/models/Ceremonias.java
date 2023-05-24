package models;

import java.math.BigInteger;

public class Ceremonias {
	private String date;
	private String cicle;
	private BigInteger id_ceremony;
		
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getCicle() {
		return cicle;
	}
	public void setCicle(String cicle) {
		this.cicle = cicle;
	}
	public BigInteger getId_ceremony() {
		return id_ceremony;
	}
	public void setId_ceremony(BigInteger id_ceremony) {
		this.id_ceremony = id_ceremony;
	}
	@Override
	public String toString() {
		return "Ceremonias [date=" + date + ", cicle=" + cicle + ", id_ceremony=" + id_ceremony + "]";
	}	
	
	
}
