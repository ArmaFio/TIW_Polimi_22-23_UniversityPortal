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

@WebServlet("/Modify")
public class Modify extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection db;
  
    public Modify() {
        super();
        
    }
    

	public void init() throws ServletException {
		db=ConnectionHandler.getConnection(getServletContext());
	}
	

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u=(User) request.getSession().getAttribute("user");
		CourseDAO c=new CourseDAO(db);
		String e= request.getParameter("exc");
		Date dt= Date.valueOf(request.getParameter("exd"));
		RegistrationDAO mod= new RegistrationDAO(db);
		int s= Integer.parseInt(request.getParameter("matr"));
		try {
			if(!c.checksProfessor(e, u.getId())){
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Not Allowed");
				return;
			}
		} catch (SQLException e2) {
			response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "2failed to connect to the database");
			return;
		}
		Registration esito=new Registration();
		try {
			esito= mod.getExamRegistration(e,dt,s);
		} catch (SQLException e1) {
			response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "1failed to connect to the database");
			return;
		}
		if(esito.getEvalStatus()==EvalStatus.NOT_INSERTED||esito.getEvalStatus()==EvalStatus.INSERTED) {
			String path="/WEB-INF/Modifica.jsp";
			request.setAttribute("line", esito);
			request.setAttribute("exc", e);
			request.setAttribute("exd", dt);
			request.setAttribute("matr", s);
			RequestDispatcher rd=request.getRequestDispatcher(path);
			rd.forward(request, response);
			return;
		}else {
			String path="/GetResOrRegs";
			RequestDispatcher rd=request.getRequestDispatcher(path);
			rd.forward(request, response);
			return;
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

}
