package it.polimi.tiw.test.Servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.tiw.test.Beans.Exam;
import it.polimi.tiw.test.Beans.User;
import it.polimi.tiw.test.DAO.CourseDAO;
import it.polimi.tiw.test.DAO.ExamDAO;
import it.polimi.tiw.test.Utils.ConnectionHandler;

@WebServlet("/GetExams")
@MultipartConfig
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
		Gson gson = new GsonBuilder()
				.setDateFormat("yyyy MMM dd").create();
		String CourseId= (String) request.getParameter("corso");
		ArrayList <Exam> Exams=new ArrayList<>();
		ExamDAO ex= new ExamDAO(db);
		ArrayList <String> dates= new ArrayList<>();
		switch(u.getStatus()) {
		case STUDENT->{
			try {
				if(!c.checksStudent(CourseId, u.getMatricola())){
					response.sendError(HttpServletResponse.SC_BAD_REQUEST);
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
					response.sendError(HttpServletResponse.SC_BAD_REQUEST);
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
		for(Exam e: Exams)
			dates.add(e.getDate().toString());
		if(!Exams.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			String json = gson.toJson(dates);
			response.getWriter().write(json);
		}
		else
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
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
