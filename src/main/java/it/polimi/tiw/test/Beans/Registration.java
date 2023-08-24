package it.polimi.tiw.test.Beans;

import java.sql.Timestamp;

public class Registration {
    private Timestamp date;
	private int matr;
	private String gc;
	private String email;
	private String name;
	private String surname;
	private String teacherName;
	private String teacherSurname;
	private String course;
    private String vote;
	private EvalStatus status;
	

	public Registration() {
		super();
	}



	public String getGc() {
		return gc;
	}



	public void setGC(String gC) {
		this.gc = gC;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getSurname() {
		return surname;
	}



	public void setSurname(String surname) {
		this.surname = surname;
	}



	public String getTeacherName() {
		return teacherName;
	}



	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}



	public String getTeacherSurname() {
		return teacherSurname;
	}



	public void setTeacherSurname(String teacherSurname) {
		this.teacherSurname = teacherSurname;
	}



	public String getCourse() {
		return course;
	}



	public void setCourse(String course) {
		this.course = course;
	}
	
	
	
	public int getMatr() {
		return matr;
	}



	public void setMatr(int matr) {
		this.matr = matr;
	}

	
	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public String getVote() {
		return vote;
	}
	
	public void setVote(String vote) {
		this.vote = vote;
	}
	
	public String getStatus() {
		switch(status) {
		case NOT_INSERTED:
			return "NON INSERITO";
		case INSERTED:
			return "INSERITO";
		case PUBLISHED:
			return "PUBBLICATO";
		case REFUSED:
			return "RIFIUTATO";
		case VERBALIZED:
			return "VERBALIZZATO";
		default:
		return "NON INSERITO";
		}
	}
	
	public EvalStatus getEvalStatus(){
		return status;
	}
	
	public void setStatus(EvalStatus status) {
		this.status = status;
	}
	
	
}
