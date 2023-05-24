package models;

import java.math.BigInteger;

public class Telephone {
	private BigInteger id_register;
	private BigInteger id_sinoidal;
	private String telephone;
	
	
	
	
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




	public String getTelephone() {
		return telephone;
	}




	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}




	@Override
	public String toString() {
		return "Telephone [id_register=" + id_register + ", id_sinoidal=" + id_sinoidal + ", telephone=" + telephone
				+ "]";
	}
	
	
}
