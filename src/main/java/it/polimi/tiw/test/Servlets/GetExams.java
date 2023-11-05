package it.polimi.tiw.test.Servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import it.polimi.tiw.test.Beans.Exam;
import it.polimi.tiw.test.Beans.User;
import it.polimi.tiw.test.DAO.CourseDAO;
import it.polimi.tiw.test.DAO.ExamDAO;
import it.polimi.tiw.test.Utils.ConnectionHandler;

@WebServlet("/GetExams")
public class GetExams extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Connection db=null;

    public GetExams() {
        super();
    }

	public void init() throws ServletException{
		db=ConnectionHandler.getConnection(getServletContext());
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u=(User) request.getSession().getAttribute("user");
		CourseDAO c=new CourseDAO(db);
		String CourseId= (String) request.getParameter("corso");
		ArrayList <Exam> Exams=new ArrayList<>();
		ArrayList <String> Dates= new ArrayList<>();
		ExamDAO ex= new ExamDAO(db);
		switch(u.getStatus()) {
		case STUDENT->{
			try {
				if(!c.checksStudent(CourseId, u.getMatricola())){
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Not Allowed");
					return;
				}
			} catch (SQLException e2) {
				response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "1 failed to connect to the database");
				return;
			} 
			try {
				Exams= ex.FindExams(CourseId, u.getMatricola());
			}catch (SQLException e) {
				response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "2 failed to connect to the database");
				return;
			}
		}
		case TEACHER->{
			try {
				if(!c.checksProfessor(CourseId, u.getId())){
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Not Allowed");
					return;
				}
			} catch (SQLException e2) {
				response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "3 failed to connect to the database");
				return;
			} 
			try {
				Exams= ex.FindExams(CourseId);
			}catch (SQLException e) {
				response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "4 failed to connect to the database");
				return;
			}
		}
		}
		for(Exam e: Exams){
			Dates.add(e.getDate().toString());
		}
	    String path="/GoToHome";
        request.setAttribute("Exams", Dates);
		request.setAttribute("Exc", CourseId);
        RequestDispatcher req= request.getRequestDispatcher(path);
        req.forward(request, response);
        return;
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
