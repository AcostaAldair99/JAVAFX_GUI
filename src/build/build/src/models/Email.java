package models;

import java.math.BigInteger;

public class Email {
	private BigInteger id_register;
	private BigInteger id_sinoidal;
	private String email;
	public BigInteger getId_register() {
		return id_register;
	}
	public void setId_register(BigInteger id_register) {
		this.id_register = id_register;
	}
	public BigInteger getId_sinoidal() {
		return id_sinoidal;
	}
	public void setId_sinoidal(BigInteger id_sinoidal) {
		this.id_sinoidal = id_sinoidal;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String toString() {
		return "Email [id_register=" + id_register + ", id_sinoidal=" + id_sinoidal + ", email="
				+ email + "]";
	}
	
	
	
}
