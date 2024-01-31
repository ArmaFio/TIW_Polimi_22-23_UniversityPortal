package it.polimi.tiw.test.Beans;

public class User {
	private String id;
	private int matricola;
	private UserStatus status;
	private String name;
	private String surname;
	private String email;
	private String corsoLaurea;
	
	public User() {
		super();
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCorsoLaurea() {
		return corsoLaurea;
	}

	public void setCorsoLaurea(String corsoLaurea) {
		this.corsoLaurea = corsoLaurea;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status=status;
	}
	
	public int getMatricola() {
		return matricola;
	}	
	
	public void setMatricola(int matricola) {
		this.matricola=matricola;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isStudent() {
		return status == UserStatus.STUDENT;
	}
	
	public boolean isTeacher() {
		return status == UserStatus.TEACHER;
	}
	
}