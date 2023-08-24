package it.polimi.tiw.test.Beans;

import java.sql.Timestamp;

public class Exam{
	private String courseCode;
	private Timestamp date;
	

	public Exam() {
		super();
	}

	public String getCourseCode() {
		return courseCode;
	}
	
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp timestamp) {
		this.date = timestamp;
	}
	
}




