package it.polimi.tiw.test.Beans;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Verbal{
	private int id;
    private Timestamp dateTime;
	private ArrayList<Registration> registrations;
    
	
    public Verbal() {
    	super();
    }
    


	public Timestamp getDateTime() {
		return dateTime;
	}



	public void setDateTime(Timestamp dateTime) {
		this.dateTime = dateTime;
	}



	public ArrayList<Registration> getRegistrations() {
		return registrations;
	}



	public void setRegistrations(ArrayList<Registration> registrations) {
		this.registrations = registrations;
	}



	public int getId() {
		return id;
	}
	
	public void setId(int i) {
		this.id = i;
	}



	
}