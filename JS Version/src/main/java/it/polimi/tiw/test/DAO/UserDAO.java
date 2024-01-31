package it.polimi.tiw.test.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import it.polimi.tiw.test.Beans.User;
import it.polimi.tiw.test.Beans.UserStatus;

public class UserDAO {
	private Connection db;
	
	public UserDAO (Connection connection) {
		db=connection;
	}
	
	public User checkLogin(String id, String pw) throws SQLException {
		String prep= "SELECT Status, Nome, Cognome, Mail, CorsoLaurea, Matricola FROM utenti WHERE ID = ? AND Password = ?";
		try(PreparedStatement query = db.prepareStatement(prep);){
			query.setString(1,id);
			query.setString(2,pw);
			try(ResultSet result = query.executeQuery();){
				if (!result.isBeforeFirst())
					return null;
				else{
					User u = new User();
					result.next();
				    u.setStatus(UserStatus.getStatusFromInt(result.getInt("Status")));
				    u.setId(id);
					u.setMatricola(result.getInt("Matricola"));
				    u.setName(result.getString("Nome"));
				    u.setSurname(result.getString("Cognome"));
				    u.setEmail(result.getString("Mail"));
				    u.setCorsoLaurea(result.getString("CorsoLaurea"));
				    return u;
				}
			}
				
		}
	}
}
				
			