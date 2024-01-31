package it.polimi.tiw.test.Servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;

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

@WebServlet("/Insert")
public class Insert extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Connection db;
    
    public Insert() {
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
		String loginpath = getServletContext().getContextPath() + "/LoginPage.html";
	    CourseDAO c= new CourseDAO(db);
		String vote= request.getParameter("vote");
		String [] available= new String [] {"18","19","20","21","22","23","24","25","26","27","28","29","30","30L","Rimandato","Riprovato","Assente"};
		String e= request.getParameter("exc");
		Date dt= Date.valueOf(request.getParameter("exd"));
		int m=Integer.parseInt(request.getParameter("matr"));
		RegistrationDAO r = new RegistrationDAO(db);
		Registration reg=new Registration();
		try {
			if(!c.checksProfessor(e, u.getId())){
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Not Allowed");
				return;
			}
		} catch (SQLException e2) {
			response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "failed to connect to the database");
			return;
		} 
		try {
			reg=r.getExamRegistration(e, dt, m);
		}catch (SQLException e0) {
			response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Impossible to Connect to the database");
			return;
		}
		if(reg.getEvalStatus()==EvalStatus.NOT_INSERTED||reg.getEvalStatus()==EvalStatus.INSERTED) {
			boolean incorrect = true;
			for(int i = 0; i < available.length&& incorrect; i++) {
				if(vote.equals(available[i])) incorrect = false;
			}
			if(incorrect) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect param values");
				return;
			} else {
				int value;
				if(vote.equals("Assente"))
					value=-3;
				else if(vote.equals("Rimandato"))
					value=-2;
				else if(vote.equals("Riprovato"))
					value=-1;
				else if(vote.equals("30L"))
					value=31;
				else
					value=Integer.parseInt(vote);
				try {
					r.InsertVote(m, e, dt, value);
				} catch (SQLException e1) {
					response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "failed to connect to the database");
					return;
				};
				response.sendRedirect(getServletContext().getContextPath()+"/GetResOrRegs?exd="+dt.toString()+"&exc="+e+"&o=0");
				return;
			}
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
