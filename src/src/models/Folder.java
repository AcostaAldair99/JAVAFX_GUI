package models;

import java.math.BigInteger;

public class Folder {
	private BigInteger id_folder;
	private BigInteger id_case;
	
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
	@Override
	public String toString() {
		return "Folder [id_folder=" + id_folder + ", id_case=" + id_case + "]";
	}
	
	
	
}
