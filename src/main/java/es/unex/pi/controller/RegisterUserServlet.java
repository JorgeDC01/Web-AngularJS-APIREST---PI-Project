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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import es.unex.pi.dao.JDBCUserDAOImpl;
import es.unex.pi.dao.UserDAO;
import es.unex.pi.model.Hosting;
import es.unex.pi.model.User;

@WebServlet("/RegisterUserServlet.do")

/**
 * Servlet implementation class RegisterUserServlet
 */
public class RegisterUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(HttpServlet.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegisterUserServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		logger.info("The request was made using GET ");
		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/RegisterUser.jsp");
		view.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		logger.info("Handling POST");

		Map<String, String> messages = new HashMap<String, String>();

		User user = new User();
		user.setUsername(request.getParameter("username"));
		user.setEmail(request.getParameter("email"));
		user.setPassword(request.getParameter("password"));

		logger.info("User username: " + user.getUsername());

		// Validamos los campos introducidos por el usuario que quiere registrarse

		if (user.validate(messages)) {

			logger.info("Data validated: " + user.getUsername());
			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			response.sendRedirect("CheckRegisterUserServlet.do");
		} else {
			// Si la contraseña no cumple con los requisitos mínimos
			logger.info("Data not validated: " + user.getUsername());
			request.setAttribute("error", messages);
			doGet(request, response);
			
			// Ejemplo traza
			for (String clave: messages.keySet()) {
			    String m = messages.get(clave);
			    logger.info("Clave: " + clave + ", valor: " + m);
			}
		}
	
	}

}
