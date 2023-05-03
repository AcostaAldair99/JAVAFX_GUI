package models;

import java.math.BigInteger;

public class Actas_Sinoidales {
	 private BigInteger id_actas_fk;
     private BigInteger id_sinoidales_fk;
     private String date_fk;
     private BigInteger id_ceremony;
     private BigInteger id_register;
     private int signed;
     
     
     
	public int getSigned() {
		return signed;
	}
	public void setSigned(int signed) {
		this.signed = signed;
	}
	public BigInteger getId_actas_fk() {
		return id_actas_fk;
	}
	public void setId_actas_fk(BigInteger id_actas_fk) {
		this.id_actas_fk = id_actas_fk;
	}
	public BigInteger getId_sinoidales_fk() {
		return id_sinoidales_fk;
	}
	public void setId_sinoidales_fk(BigInteger id_sinoidales_fk) {
		this.id_sinoidales_fk = id_sinoidales_fk;
	}
	public String getDate_fk() {
		return date_fk;
	}
	public void setDate_fk(String date_fk) {
		this.date_fk = date_fk;
	}
	public BigInteger getId_ceremony() {
		return id_ceremony;
	}
	public void setId_ceremony(BigInteger id_ceremony) {
		this.id_ceremony = id_ceremony;
	}
	public BigInteger getId_register() {
		return id_register;
	}
	public void setId_register(BigInteger id_register) {
		this.id_register = id_register;
	}
	@Override
	public String toString() {
		return "Actas_Sinoidales [id_actas_fk=" + id_actas_fk + ", id_sinoidales_fk=" + id_sinoidales_fk + ", date_fk="
				+ date_fk + ", id_ceremony=" + id_ceremony + ", id_register=" + id_register+", signed= "+signed;
	}
     
     
     
     
}
