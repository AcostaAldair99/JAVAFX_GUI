package models;

import java.math.BigInteger;

public class Actas {
	
	private String name_Student;
	private String lastName_Student;
	private BigInteger id_actas;
	private BigInteger id_Student;
	private int id_Folder_fk;
	private String degree;
	private int degree_plan;
	private String date_limit_fk;
	private BigInteger id_ceremony_fk;
	private int signatures;

	
	
	
	

	public String getName_Student() {
		return name_Student;
	}






	public void setName_Student(String name_Student) {
		this.name_Student = name_Student;
	}






	public String getLastName_Student() {
		return lastName_Student;
	}






	public void setLastName_Student(String lastName_Student) {
		this.lastName_Student = lastName_Student;
	}






	public BigInteger getId_actas() {
		return id_actas;
	}






	public void setId_actas(BigInteger id_actas) {
		this.id_actas = id_actas;
	}






	public BigInteger getId_Student() {
		return id_Student;
	}






	public void setId_Student(BigInteger id_Student) {
		this.id_Student = id_Student;
	}






	public int getId_Folder_fk() {
		return id_Folder_fk;
	}






	public void setId_Folder_fk(int id_Folder_fk) {
		this.id_Folder_fk = id_Folder_fk;
	}






	public String getDegree() {
		return degree;
	}






	public void setDegree(String degree) {
		this.degree = degree;
	}






	public int getDegree_plan() {
		return degree_plan;
	}






	public void setDegree_plan(int degree_plan) {
		this.degree_plan = degree_plan;
	}






	public String getDate_limit_fk() {
		return date_limit_fk;
	}






	public void setDate_limit_fk(String date_limit_fk) {
		this.date_limit_fk = date_limit_fk;
	}






	public BigInteger getId_ceremony_fk() {
		return id_ceremony_fk;
	}






	public void setId_ceremony_fk(BigInteger id_ceremony_fk) {
		this.id_ceremony_fk = id_ceremony_fk;
	}






	public int getSignatures() {
		return signatures;
	}






	public void setSignatures(int signatures) {
		this.signatures = signatures;
	}






	@Override
	public String toString() {
		return "Actas [name_Student=" + name_Student + ", lastName_Student=" + lastName_Student + ", id_actas="
				+ id_actas + ", id_Student=" + id_Student + ", id_Folder_fk=" + id_Folder_fk + ", degree=" + degree
				+ ", degree_plan=" + degree_plan + ", date_limit_fk=" + date_limit_fk + ", id_ceremony_fk="
				+ id_ceremony_fk + ", signatures=" + signatures + "]";
	}
}
