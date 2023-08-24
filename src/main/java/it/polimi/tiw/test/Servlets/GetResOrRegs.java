package it.polimi.tiw.test.Servlets;

import java.io.IOException;
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
import javax.servlet.RequestDispatcher;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;


@WebServlet("/GetResOrRegs")
public class GetResOrRegs extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection db= null;
  
    public GetResOrRegs() {
        super();
    }
    
    public void init() throws ServletException {
    	db=ConnectionHandler.getConnection(getServletContext());
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u=(User) request.getSession().getAttribute("user");
		CourseDAO c= new CourseDAO(db);
		String e= request.getParameter("exc");
		Timestamp dt=Timestamp.valueOf(request.getParameter("exd"));
		int o=Integer.parseInt(request.getParameter("o"));
		RegistrationDAO reg= new RegistrationDAO(db);
		switch(u.getStatus()) {
		case STUDENT->{
			try {
				if(!c.checksStudent(e, u.getMatricola())){
					String path = getServletContext().getContextPath() + "/GoToHome";
					response.sendRedirect(path);
					return;
				}
			} catch (SQLException e2) {
				response.sendError(HttpServletResponse.SC_BAD_GATEWAY, " 1failed to connect to the database");
				return;
			} 
			Registration esito=new Registration();
			try {
				esito= reg.getStudentRegistration(u.getMatricola(),e,dt);
			} catch (SQLException e1) {
				response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "2failed to connect to the database");
				return;
			}
			if(esito.getEvalStatus()==EvalStatus.PUBLISHED||esito.getEvalStatus()==EvalStatus.REFUSED||esito.getEvalStatus()==EvalStatus.VERBALIZED) {
				String path1="/WEB-INF/Esito.jsp";
				request.setAttribute("esito", esito);
				request.setAttribute("exc", e);
				RequestDispatcher d1= request.getRequestDispatcher(path1);
				d1.forward(request, response);
				return;
			}else {
				String path2="/WEB-INF/Esito.jsp";
				RequestDispatcher d1= request.getRequestDispatcher(path2);
				d1.forward(request, response);
				return;
			}
		}
		case TEACHER->{
			try {
				if(!c.checksProfessor(e, u.getId())){
					String path = getServletContext().getContextPath() + "/GoToHome";
					response.sendRedirect(path);
					return;
				}
			} catch (SQLException e2) {
				response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "3failed to connect to the database");
				return;
			} 
			ArrayList <Registration> iscr=new ArrayList<>();
			try {
				iscr= reg.getExamRegistrations(e, dt, o);
			} catch (SQLException e1) {
				response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "4failed to connect to the database");
				return;
			}
			String path2="/WEB-INF/Iscritti.jsp";
			request.setAttribute("regs", iscr);
			request.setAttribute("order", o);
			request.setAttribute("cc", e);
			request.setAttribute("date", dt);			
			RequestDispatcher d2= request.getRequestDispatcher(path2);
			d2.forward(request, response);
			return;
		}
	}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
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
