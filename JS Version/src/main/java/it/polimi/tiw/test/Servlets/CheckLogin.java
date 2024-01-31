package it.polimi.tiw.test.Servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.tiw.test.Beans.User;
import it.polimi.tiw.test.DAO.UserDAO;
import it.polimi.tiw.test.Utils.ConnectionHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


@WebServlet("/CheckLogin")
@MultipartConfig
public class CheckLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection db = null;
	
   
    public CheckLogin() {
        super();
    }
    
    public void init() throws ServletException{
    	db = ConnectionHandler.getConnection(getServletContext());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id=request.getParameter("id");
		String pw=request.getParameter("password");
		Gson gson = new GsonBuilder()
				   .setDateFormat("yyyy MMM dd").create();
		if (id == null || pw == null || id.isEmpty() || pw.isEmpty() ) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write(gson.toJson("Error: Null credential(s)"));
			return;
		}
		UserDAO user= new UserDAO(db);
		User u= new User();
		try {
			u=user.checkLogin(id, pw);
		}catch(SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write(gson.toJson("Internal server error, retry later"));
			return;
		}
		String path = getServletContext().getContextPath();
		if(u==null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write(gson.toJson("Invalid Credentials, retry"));
		}
		else {
			request.getSession().setAttribute("user", u);
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			String json = gson.toJson(u);
			response.getWriter().write(json);
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
