package it.polimi.tiw.test.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.test.Beans.Course;

import java.sql.ResultSet;

public class CourseDAO{
	private Connection db;
	
	public CourseDAO(Connection conn) {
		db=conn;
	}
	
	public List<Course> find_courses_by_teacher(String docente) throws SQLException{
		List<Course> courses= new ArrayList<>();
		String prep= "SELECT CodiceCorso,NomeCorso FROM corsi WHERE Docente=? ORDER BY NomeCorso DESC";
		try(PreparedStatement query= db.prepareStatement(prep);){
			query.setString(1,docente);
			try(ResultSet result= query.executeQuery();){
				if(!result.isBeforeFirst())
					return courses;
				else {
					while(result.next()) {
						Course c= new Course();
						c.setCourseId(result.getString("CodiceCorso"));
						c.setCourseName(result.getString("NomeCorso"));
						c.setTeacher(docente);
						courses.add(c);
					}
					return courses;
				}
			}
		}
		
	}
	
	
	public ArrayList<Course> find_courses_by_student (int studente) throws SQLException{
		ArrayList<Course> Courses= new ArrayList<>();
		String prep="SELECT CodiceCorso, NomeCorso, Docente FROM corsi , iscrizionicorsi WHERE Matricola=? and Corso=CodiceCorso ORDER BY NomeCorso DESC";
		try(PreparedStatement query= db.prepareStatement(prep);){
			query.setInt(1, studente);
			try(ResultSet result= query.executeQuery();){
				if(!result.isBeforeFirst())
					return null;
				else {
					while(result.next()) {
						Course c= new Course();
						c.setCourseId(result.getString("CodiceCorso"));
						c.setCourseName(result.getString("NomeCorso"));
						c.setTeacher(result.getString("Docente"));
						Courses.add(c);
					}
					return Courses;
				}
			}
		}
	}
	
	public boolean checksProfessor(String courseid, String teacherId) throws SQLException{
		
		boolean exit = false;
		
		String query = "SELECT * from corsi WHERE CodiceCorso = ?";
		try (PreparedStatement pstatement = db.prepareStatement(query);) {
			pstatement.setString(1, courseid);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					if(result.getString("Docente").equals(teacherId)) exit = true;
				}
			}
		}
		
		return exit;
		
	}
	
	public boolean checksStudent(String courseid, int studMatr) throws SQLException{
		
		boolean exit = false;
		
		String query = "SELECT * FROM iscrizionicorsi WHERE Corso = ?";
		try (PreparedStatement pstatement = db.prepareStatement(query);) {
			pstatement.setString(1, courseid);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					if(result.getInt("Matricola")==studMatr) exit = true;
				}
			}
		}
		
		return exit;
		
	}
		
}
