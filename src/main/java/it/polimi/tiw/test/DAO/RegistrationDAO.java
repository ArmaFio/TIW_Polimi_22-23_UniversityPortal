package it.polimi.tiw.test.DAO;

import java.sql.Connection;

import it.polimi.tiw.test.Beans.EvalStatus;
import it.polimi.tiw.test.Beans.Registration;
import it.polimi.tiw.test.Beans.Verbal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class RegistrationDAO{
	private Connection db;
	
	public RegistrationDAO(Connection c) {
		db=c;
	}
	
	public ArrayList<Registration> getExamRegistrations(String id, Timestamp data, int order) throws SQLException{
		ArrayList<Registration> Regs= new ArrayList<>();
		String prep="SELECT A.Matricola,StatoValutazione,Voto,Mail,CorsoLaurea,Nome,Cognome,NomeCorso FROM iscrizioniappelli as A ,utenti as B, corsi as C WHERE A.Matricola=B.Matricola and A.CodiceCorso=? and A.CodiceCorso=C.CodiceCorso and Data=? ORDER BY "+getOrderBy(order);
		try(PreparedStatement query= db.prepareStatement(prep);){
			query.setString(1, id);
			query.setTimestamp(2, data);	
			try(ResultSet result= query.executeQuery();){
				if(!result.isBeforeFirst())
					return Regs;
				else {
					while(result.next()) {
						Registration a= new Registration();
						a.setCourse(id);
						a.setDate(data);
						a.setStatus(EvalStatus.getEvalStatusFromInt(result.getInt("StatoValutazione")));
						int gr=result.getInt("Voto");
						if(gr>=18&&gr<=30) 
							a.setVote(String.valueOf(gr));
						else if (gr==-1)
							a.setVote("RIPROVATO");
						else if (gr==-2)
							a.setVote("RIMANDATO");
						else if (gr==-3)
							a.setVote("ASSENTE");
						else if (gr==31)
							a.setVote("30L");
						a.setMatr(result.getInt("Matricola"));
						a.setGC(result.getString("CorsoLaurea"));
						a.setEmail(result.getString("Mail"));
						a.setName(result.getString("Nome"));
						a.setSurname(result.getString("Cognome"));
						a.setCourse(result.getString("NomeCorso"));
						Regs.add(a);
					}
					return Regs;
				}
			}
		}
	}
		
	
	public Registration getExamRegistration(String id, Timestamp data, String matr) throws SQLException{
		Registration a= new Registration();
		String prep="SELECT A.Matricola,StatoValutazione,Voto,Mail,CorsoLaurea,Nome,Cognome,NomeCorso FROM iscrizioniappelli as A ,utenti as B, corsi as C WHERE A.Matricola=? and A.Matricola=B.Matricola and A.CodiceCorso=? and A.CodiceCorso=C.CodiceCorso and Data=?";
		try(PreparedStatement query= db.prepareStatement(prep);){
			query.setString(1, matr);
			query.setString(2, id);
			query.setTimestamp(3, data);	
			try(ResultSet result= query.executeQuery();){
				if(!result.isBeforeFirst())
					return a;
				else {
					result.next();
					a.setStatus(EvalStatus.getEvalStatusFromInt(result.getInt("StatoValutazione")));
					a.setMatr(result.getInt("Matricola"));
					a.setGC(result.getString("CorsoLaurea"));
					a.setEmail(result.getString("Mail"));
			        a.setName(result.getString("Nome"));
			        a.setSurname(result.getString("Cognome"));
			        return a;
				}
			}
		}
	}
	
	public Registration getStudentRegistration(int matr, String id, Timestamp data) throws SQLException{
		Registration a= new Registration();
		String prep= "SELECT StatoValutazione,Voto,Nome,Cognome,NomeCorso FROM iscrizioniappelli as A ,utenti as B, corsi as C WHERE  A.CodiceCorso=? and A.CodiceCorso=C.CodiceCorso and C.Docente=B.Matricola and Data=? and A.Matricola=?";
		try(PreparedStatement query= db.prepareStatement(prep);){
			query.setString(1, id);
			query.setTimestamp(2, data);
			query.setInt(3, matr);
			try(ResultSet result= query.executeQuery();){
				if(!result.isBeforeFirst())
					return a;
				else {
					result.next();
					a.setCourse(result.getString("NomeCorso"));
					a.setDate(data);
					a.setStatus(EvalStatus.getEvalStatusFromInt(result.getInt("StatoValutazione")));
					if(a.getEvalStatus()==EvalStatus.PUBLISHED||a.getEvalStatus()==EvalStatus.REFUSED||a.getEvalStatus()==EvalStatus.VERBALIZED) {
						int gr=result.getInt("Voto");
						if(gr>=18&&gr<=30) 
							a.setVote(String.valueOf(gr));
						else if (gr==-1)
							a.setVote("RIPROVATO");
						else if (gr==-2)
							a.setVote("RIMANDATO");
						else if (gr==-3)
							a.setVote("ASSENTE");
						else if (gr==31)
							a.setVote("30L");
					}
					a.setMatr(matr);
					a.setTeacherName(result.getString("Nome"));
					a.setTeacherSurname(result.getString("Cognome"));
				    return a;
				}
			}
		}
	}
	
	public void PublishVotes(String CourseCode, Timestamp data) throws SQLException{
		String prep= "UPDATE iscrizioniappelli SET StatoValutazione= 2 WHERE StatoValutazione=1 and CodiceCorso=? and Data=?";
		try(PreparedStatement query= db.prepareStatement(prep);){
			query.setString(1, CourseCode);
		    query.setTimestamp(2, data);
		    query.execute();
		}
	}
	
	public Verbal VerbalizeVotes(String CourseCode, Timestamp data) throws SQLException{
		db.setAutoCommit(false);
		Verbal v= new Verbal();
		try{
			VerbalDAO vd= new VerbalDAO(db);
			v= vd.CreateVerbal();
			String pr="UPDATE iscrizioniappelli SET Voto= -2 Where StatoValutazione=3 and CodiceCorso=? and Data=?";
			PreparedStatement query1= db.prepareStatement(pr);
			query1.setString(1, CourseCode);
			query1.setTimestamp(2,data);
			query1.execute();
			String prep= "UPDATE iscrizioniappelli SET Verbale=?, StatoValutazione=4 WHERE (StatoValutazione=2 or StatoValutazione=3) and CodiceCorso=? and Data=?";
			PreparedStatement query2= db.prepareStatement(prep);
			query2.setInt(1, v.getId());
			query2.setString(2, CourseCode);
			query2.setTimestamp(3, data);
			query2.execute();
		}
		finally {
			db.commit();
		}
		return v;
	}
	
	public void InsertVote(String matr, String id, Timestamp data, int i) throws SQLException{
		String prep="Update iscrizioniappelli SET Voto=?,StatoValutazione=1 WHERE Matricola=? AND CodiceCorso=? AND Data=? AND (StatoValutazione=0 OR StatoValutazione=1)";
		try(PreparedStatement query= db.prepareStatement(prep);){
			query.setInt(1,i);
			query.setString(2,matr);
			query.setString(3,id);
			query.setTimestamp(4,data);
			query.execute();
		}
	}
	
	public void RefuseVote(int matr, Timestamp data, String id) throws SQLException  {
		String prep="Update iscrizioniappelli SET StatoValutazione=3 WHERE Matricola=? AND CodiceCorso=? AND Data=? AND StatoValutazione=2 AND Voto>=18 AND Voto<=31";
		try(PreparedStatement query= db.prepareStatement(prep);){
			query.setInt(1,matr);
			query.setString(2,id);
			query.setTimestamp(3,data);
			query.execute();
		}
	}
	
	private String getOrderBy(int order) {
		String orderBy = "";
		switch(order) {
			case 1:
				orderBy += "Matricola";
				break;
			case 2:
				orderBy += "Matricola DESC";
				break;
			case 3:
				orderBy += "Cognome";
				break;
			case 4:
				orderBy += "Cognome DESC";
				break;
			case 5:
				orderBy += "Nome ";
				break;
			case 6:
				orderBy += "Nome DESC";
				break;
			case 7:
				orderBy += "Mail ";
				break;
			case 8:
				orderBy += "Mail DESC";
				break;
			case 9:
				orderBy += "CorsoLaurea";
				break;
			case 10:
				orderBy += "CorsoLaurea DESC";
				break;
			case 11:
				orderBy += "Voto";
				break;
			case 12:
				orderBy += "Voto DESC";
				break;
			case 13:
				orderBy += "StatoValutazione";
				break;
			case 14:
				orderBy += "StatoValutazione DESC";
				break;
			default:
				orderBy += "Matricola ";
				break;
		}
		return orderBy;
	}

}
	
	
			
