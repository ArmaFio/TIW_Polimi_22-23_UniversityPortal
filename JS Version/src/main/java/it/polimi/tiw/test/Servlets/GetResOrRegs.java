package it.polimi.tiw.test.Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.tiw.test.Beans.EvalStatus;
import it.polimi.tiw.test.Beans.Registration;
import it.polimi.tiw.test.Beans.User;
import it.polimi.tiw.test.DAO.CourseDAO;
import it.polimi.tiw.test.DAO.RegistrationDAO;
import it.polimi.tiw.test.Utils.ConnectionHandler;
import javax.servlet.RequestDispatcher;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;


@WebServlet("/GetResOrRegs")
@MultipartConfig
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
		User u = (User) request.getSession().getAttribute("user");
		CourseDAO c = new CourseDAO(db);
		Gson gson = new GsonBuilder()
				.setDateFormat("yyyy MMM dd").create();
		String e = request.getParameter("exc");
		Date dt = Date.valueOf(request.getParameter("exd"));
		RegistrationDAO reg = new RegistrationDAO(db);
		switch (u.getStatus()) {
			case STUDENT -> {
				try {
					if (!c.checksStudent(e, u.getMatricola())) {
						response.sendError(HttpServletResponse.SC_BAD_REQUEST);
						return;
					}
				} catch (SQLException e2) {
					response.sendError(HttpServletResponse.SC_BAD_GATEWAY, " 1failed to connect to the database");
					return;
				}
				Registration esito = new Registration();
				try {
					esito = reg.getStudentRegistration(u.getMatricola(), e, dt);
					esito.setEmail(u.getEmail());
					esito.setGC(u.getCorsoLaurea());
					esito.setName(u.getName());
					esito.setSurname(u.getSurname());
				} catch (SQLException e1) {
					response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "2failed to connect to the database");
					return;
				}
				if (esito.getEvalStatus() == EvalStatus.PUBLISHED || esito.getEvalStatus() == EvalStatus.REFUSED || esito.getEvalStatus() == EvalStatus.VERBALIZED) {
					response.setStatus(HttpServletResponse.SC_OK);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					String json = gson.toJson(esito);
					response.getWriter().write(json);
				} else {
					response.setStatus(HttpServletResponse.SC_NO_CONTENT);
					return;
				}

			}
			case TEACHER -> {
				try {
					if (!c.checksProfessor(e, u.getId())) {
						response.sendError(HttpServletResponse.SC_BAD_REQUEST);
						return;
					}
				} catch (SQLException e2) {
					response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "3failed to connect to the database");
					return;
				}
				ArrayList<Registration> iscr = new ArrayList<>();
				try {
					iscr = reg.getExamRegistrations(e, dt);
				} catch (SQLException e1) {
					response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "4failed to connect to the database");
					return;
				}
				if (!iscr.isEmpty()) {
					response.setStatus(HttpServletResponse.SC_OK);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					String json = gson.toJson(iscr);
					response.getWriter().write(json);

				} else
					response.setStatus(HttpServletResponse.SC_NO_CONTENT);
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
