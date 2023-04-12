package models;

import java.math.BigInteger;

public class Folder {
	private BigInteger id_folder;
	private BigInteger id_case;
	private int actas_num;
	
	public BigInteger getId_folder() {
		return id_folder;
	}
	public void setId_folder(BigInteger id_folder) {
		this.id_folder = id_folder;
	}
	public BigInteger getId_case() {
		return id_case;
	}
	public void setId_case(BigInteger id_case) {
		this.id_case = id_case;
	}
	
	public int getActas_num() {
		return actas_num;
	}
	public void setActas_num(int actas_num) {
		this.actas_num = actas_num;
	}
	@Override
	public String toString() {
		return "Folder [id_folder=" + id_folder + ", id_case=" + id_case +"num_actas="+actas_num+ "]";
	}
	
	
	
}
