package it.polimi.tiw.test.DAO;

import java.sql.Connection;
import it.polimi.tiw.test.Beans.Verbal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class VerbalDAO{
	Connection db;
	
	public VerbalDAO(Connection c){
		db=c;
	}
	
	public Verbal CreateVerbal() throws SQLException{
		Long datetime = System.currentTimeMillis();
	    Timestamp timestamp = new Timestamp(datetime);
	    Verbal v=new Verbal();
		String prep="INSERT INTO verbali (DataOra) VALUES (?)";
		try(PreparedStatement query= db.prepareStatement(prep,Statement.RETURN_GENERATED_KEYS)){
			query.setTimestamp(1, timestamp);
		    query.execute();
			ResultSet result= query.getGeneratedKeys();	
			result.next();
			v.setId(result.getInt(1));
			v.setDateTime(timestamp);
		}
		return v;
				
	}
	
}