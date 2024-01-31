package it.polimi.tiw.test.Beans;

import java.sql.Date;

public class Exam{
	private String courseCode;
	private Date date;
	

	public Exam() {
		super();
	}

	public String getCourseCode() {
		return courseCode;
	}
	
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
}




