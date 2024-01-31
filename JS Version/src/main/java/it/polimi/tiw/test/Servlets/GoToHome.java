package it.polimi.tiw.test.Servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.tiw.test.Beans.Course;
import it.polimi.tiw.test.Beans.User;
import it.polimi.tiw.test.DAO.CourseDAO;
import it.polimi.tiw.test.Utils.ConnectionHandler;

@WebServlet("/GoToHome")
@MultipartConfig
public class GoToHome extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Connection db=null;
    
    public GoToHome(){
    	super();
    }
    
    public void init() throws ServletException {
        db=ConnectionHandler.getConnection(getServletContext());
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u= (User)request.getSession().getAttribute("user");
		Gson gson = new GsonBuilder()
				.setDateFormat("yyyy MMM dd").create();
		CourseDAO c= new CourseDAO(db);
		List<Course> list = null;
		switch(u.getStatus()) {
		case STUDENT-> {
			try{
				list =c.find_courses_by_student(u.getMatricola());
				request.setAttribute("Courses", list);
			}catch(SQLException e) {
				response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "5 failed to connect to the 2 database");
				return;
			}
		}
		case TEACHER ->{
			try {
				list=c.find_courses_by_teacher(u.getId());
				request.setAttribute("Courses", list);
			}catch (SQLException e
					) {
				response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "6 failed to connect to the database");
				return;
			}
		}
		}
		if(!list.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			String json = gson.toJson(list);
			response.getWriter().write(json);
		}
		else
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
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
