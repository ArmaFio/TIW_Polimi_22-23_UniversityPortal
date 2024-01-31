package it.polimi.tiw.test.Servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.tiw.test.DAO.CourseDAO;
import it.polimi.tiw.test.DAO.RegistrationDAO;
import it.polimi.tiw.test.Utils.*;
import it.polimi.tiw.test.Beans.*;



@WebServlet("/Verbalize")
@MultipartConfig
public class Verbalize extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Connection db; 

    public Verbalize() {
        super();
    }

	public void init() throws ServletException {
		db=ConnectionHandler.getConnection(getServletContext());	
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u= (User) request.getSession().getAttribute("user");
		RegistrationDAO verb= new RegistrationDAO(db);
		String e= request.getParameter("exc");
		Date dt= Date.valueOf(request.getParameter("exd"));
		Verbal v= new Verbal();
		ArrayList<Registration> Verbalinfo= new ArrayList<>();
		CourseDAO c= new CourseDAO(db);
		Gson gson = new GsonBuilder()
				.setDateFormat("yyyy MMM dd").create();
		try {
			if(!c.checksProfessor(e, u.getId())){
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
		} catch (SQLException e2) {
			response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "failed to connect to the database");
		} 
		try {
			Verbalinfo= verb.getExamRegistrations(e, dt);
		}catch(SQLException e2) {
			response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "1Impossible to connect to the database");
			return;
		}
		Iterator<Registration> i=Verbalinfo.iterator();
		while(i.hasNext()){
			Registration curr= i.next();
			if(curr.getEvalStatus()!=EvalStatus.PUBLISHED&&curr.getEvalStatus()!=EvalStatus.REFUSED)
				i.remove();
		}
		if(Verbalinfo.size()>0) {
			try {
				v=verb.VerbalizeVotes(e,dt);
			} catch(SQLException e1) {
				response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "2Impossible to connect to the database");
				return;
			}
			try {
				verb.takeVerbalized(v);
			} catch (SQLException ex) {
				response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "2Impossible to connect to the database");
				return;
			}
			if(v.getRegistrations().isEmpty())
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error");
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			String json = gson.toJson(v);
			response.getWriter().write(json);
			return;
		}else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
