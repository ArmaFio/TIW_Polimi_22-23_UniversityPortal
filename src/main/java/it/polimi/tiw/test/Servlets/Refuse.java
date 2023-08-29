package it.polimi.tiw.test.Servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.tiw.test.Beans.EvalStatus;
import it.polimi.tiw.test.Beans.Registration;
import it.polimi.tiw.test.Beans.User;
import it.polimi.tiw.test.DAO.CourseDAO;
import it.polimi.tiw.test.DAO.RegistrationDAO;
import it.polimi.tiw.test.Utils.ConnectionHandler;

@WebServlet("/Refuse")
public class Refuse extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Connection db;
    
    public Refuse() {
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
		RegistrationDAO d= new RegistrationDAO(db);
		Registration r=new Registration();
		try {
			if(!c.checksStudent(e, u.getMatricola())){
				String path = getServletContext().getContextPath() + "/GoToHome";
				response.sendRedirect(path);
				return;
			}
		} catch (SQLException e2) {
			response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "failed to connect to the database");
			return;
		} 
		try {
			r=d.getStudentRegistration(u.getMatricola(), e, dt);
		}catch (SQLException e0) {
			response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "55Impossible to Connect to the database");
			return;
		}
		if(r.getEvalStatus()==EvalStatus.PUBLISHED) {
			try {
				d.RefuseVote(u.getMatricola(), dt, e);
			}catch(SQLException e1) {
				response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "66Impossible to Connect to the database");
				return;
			}
			String path="/GetResOrRegs";
			RequestDispatcher req= request.getRequestDispatcher(path);
			req.forward(request, response);
			return;
		}else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Not Allowed");
			return;
		}
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
