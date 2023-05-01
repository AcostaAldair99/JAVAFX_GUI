package models;

import java.math.BigInteger;

public class Sinoidales {
	private BigInteger id_sinoidales;
	private String first_Name;
	private String second_Name;
    private BigInteger id_professor;
    private String area;
    private int disponibility;
    private int isActive;
    
    
	public BigInteger getId_sinoidales() {
		return id_sinoidales;
	}
	public void setId_sinoidales(BigInteger id_sinoidales) {
		this.id_sinoidales = id_sinoidales;
	}
	public String getFirst_Name() {
		return first_Name;
	}
	public void setFirst_Name(String first_Name) {
		this.first_Name = first_Name;
	}
	
	public String getSecond_Name() {
		return second_Name;
	}
	public void setSecond_Name(String second_Name) {
		this.second_Name = second_Name;
	}
	public BigInteger getId_professor() {
		return id_professor;
	}
	public void setId_professor(BigInteger id_professor) {
		this.id_professor = id_professor;
	}

	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}

	public int getDisponibility() {
		return disponibility;
	}
	public void setDisponibility(int disponibility) {
		this.disponibility = disponibility;
	}
	public int getIsActive() {
		return isActive;
	}
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	@Override
	public String toString() {
		return "Sinoidales [id_sinoidales=" + id_sinoidales + ", first_Name=" + first_Name + ", second_Name="
				+ second_Name + ", id_professor=" + id_professor + ", area=" + area + ", disponibility=" + disponibility
				+ ", isActive=" + isActive + "]";
	}

	
	
}
