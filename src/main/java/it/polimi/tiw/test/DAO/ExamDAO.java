package it.polimi.tiw.test.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import it.polimi.tiw.test.Beans.Exam;

import java.sql.ResultSet;

public class ExamDAO{
	private Connection db;
	
	public ExamDAO(Connection connection) {
		db=connection;
	}
	
	public ArrayList<Exam> FindExams(String idc) throws SQLException {
		ArrayList<Exam> exams= new ArrayList<>();
		String prep="SELECT Data FROM appelli WHERE CodiceCorso = ? ORDER BY Data DESC";
		try(PreparedStatement query= db.prepareStatement(prep);){
			query.setString(1, idc);
			try(ResultSet result=query.executeQuery();){
				if(!result.isBeforeFirst())
					return exams;
				else {
					while(result.next()) {
						Exam a= new Exam();
						a.setCourseCode(idc);
						a.setDate(result.getTimestamp("Data"));
						exams.add(a);
					}
					return exams;
				}
			}
		}
	}
	
	public ArrayList<Exam> FindExams(String idc, int matr) throws SQLException {
		ArrayList<Exam> exams= new ArrayList<>();
		String prep="SELECT Data FROM iscrizioniappelli WHERE CodiceCorso = ? and Matricola=? ORDER BY Data DESC";
		try(PreparedStatement query= db.prepareStatement(prep);){
			query.setString(1, idc);
			query.setInt(2, matr);
			try(ResultSet result=query.executeQuery();){
				if(!result.isBeforeFirst())
					return exams;
				else {
					result.next();
					while(!result.isAfterLast()) {
						Exam a= new Exam();
						a.setCourseCode(idc);
						a.setDate(result.getTimestamp("Data"));
						exams.add(a);
						result.next();
					}
					return exams;
				}
			}
		}
	}
}