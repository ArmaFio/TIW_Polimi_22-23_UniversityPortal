package it.polimi.tiw.test.Servlets;

import java.io.IOException;
import java.io.Serial;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.tiw.test.Beans.User;
import it.polimi.tiw.test.DAO.UserDAO;
import it.polimi.tiw.test.Utils.ConnectionHandler;


@WebServlet("/CheckLogin")
public class CheckLogin extends HttpServlet {
	@Serial
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
		String matr=request.getParameter("matricola");
		String pw=request.getParameter("password");
		String loginpath = getServletContext().getContextPath() + "/LoginPage.html";

		if (matr == null || matr.isEmpty() || pw == null || pw.isEmpty()) {
			response.sendRedirect(loginpath);
			return;
		}

		UserDAO user= new UserDAO(db);
		User u= new User();
		try {
			u=user.checkLogin(matr, pw);
		}catch(SQLException e) {
			response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "failed to connect to the database "+e.getMessage());
			return;
		}
		String path = getServletContext().getContextPath();
		if(u==null)
			path = getServletContext().getContextPath() + "/LoginPage.html";
		else {
			request.getSession().setAttribute("user", u);		
			path=path+"/GoToHome";
		}
		response.sendRedirect(path);
		return;
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
