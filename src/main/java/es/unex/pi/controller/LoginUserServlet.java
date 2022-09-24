package es.unex.pi.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.util.logging.Logger;

import es.unex.pi.dao.JDBCUserDAOImpl;
import es.unex.pi.dao.UserDAO;
import es.unex.pi.model.User;

@WebServlet("/LoginUserServlet.do")
/**
 * Servlet implementation class LoginUserServlet
 */
public class LoginUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(HttpServlet.class.getName());  

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginUserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("The request was made using GET ");
		// Al pasarle como parámetro 'false', en el caso de no existir la sesión devuelve 'null'
		HttpSession session = request.getSession(false);
		if(session != null) {
			response.sendRedirect("ListHostsServlet.do");
		}
		else {
			RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/login.html");
			view.forward(request, response);
		}		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		logger.info("Handling POST");

		// Inicio sesion
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		if (username != null && password != null) {
			
			// Obtenemos la conexión realizada en el listener con la BD
			Connection conn = (Connection) getServletContext().getAttribute("dbConn");
			UserDAO userDAO = new JDBCUserDAOImpl();
			userDAO.setConnection(conn);
			
			// Obtenemos el usuario de la BD
			User user = userDAO.get(username);
			
			if (user != null) {
				logger.info("Validating the password from "+user.getUsername() +" a new user who wants to log on");
				
				// Validamos si la contraseña introducida coincide con la de su username en la BD
				if (user.getPassword().equals(password)) {
					logger.info("Password is correct");

					// Guardamos en la sesión la información relacionada con el usuario
					HttpSession session = request.getSession();
					session.setAttribute("user", user);
					response.sendRedirect("pages/index.html");
				}
				else {
					doGet(request,response);
				}
			}
			else {
				doGet(request,response);
			}
		}
		else {
			doGet(request,response);
		}
	}
}
