package it.polimi.tiw.test.Servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.tiw.test.Beans.User;
import it.polimi.tiw.test.DAO.CourseDAO;
import it.polimi.tiw.test.DAO.RegistrationDAO;
import it.polimi.tiw.test.Utils.*;

@WebServlet("/Publish")
@MultipartConfig
public class Publish extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection db;
       
    public Publish() {
        super();
    }
    
    public void init() throws ServletException {
    	db=ConnectionHandler.getConnection(getServletContext());
    }
	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u=(User) request.getSession().getAttribute("user");
		CourseDAO c=new CourseDAO(db);
		String e= request.getParameter("exc");
		Date dt=Date.valueOf(request.getParameter("exd"));
		RegistrationDAO publish= new RegistrationDAO(db);
		try {
			if(!c.checksProfessor(e, u.getId())){
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
		} catch (SQLException e2) {
			response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "failed to connect to the database");
			return;
		} 
		try {
			publish.PublishVotes(e,dt);
		}catch(SQLException e1) {
			response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "impossible to connect to the database");
			return;
		}
		response.setStatus(HttpServletResponse.SC_OK);
		return;
	} 
	
	public void destroy() {
		try {
			if (db != null) {
				db.close();
			}
		} catch (SQLException sqle) {
		}
	}

}
