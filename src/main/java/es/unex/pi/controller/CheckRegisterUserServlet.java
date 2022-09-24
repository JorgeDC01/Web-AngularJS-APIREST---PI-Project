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
import java.util.Map;
import java.util.logging.Logger;

import es.unex.pi.dao.JDBCUserDAOImpl;
import es.unex.pi.dao.UserDAO;
import es.unex.pi.model.User;

@WebServlet("/CheckRegisterUserServlet.do")

/**
 * Servlet implementation class CheckRegisterUserServlet
 */
public class CheckRegisterUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(HttpServlet.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CheckRegisterUserServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("The request was made using GET ");

		// Obtenemos la sesión junto con los datos introducidos por el usuario al
		// registrarse para enviarselos al jsp. Eliminaremos la sesion porque, en caso de existir, al ir al 
		// index.html y volver a registrarnos, se cargarian los datos de esta sesion.
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		request.setAttribute("user", user);
		
		session.removeAttribute("user");
		session.invalidate();
		
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
				
		User user = new User();
		user.setUsername(request.getParameter("username"));
		user.setEmail(request.getParameter("email"));
		user.setPassword(request.getParameter("password"));
		
		Map<String, String> messages = new HashMap<String, String>();

		// Obtenemos la conexión realizada en el listener con la BD
		Connection conn = (Connection) getServletContext().getAttribute("dbConn");
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(conn);
		
		logger.info("User username checking: " + user.getUsername());
		
		// Obtenemos si existe un usuario registrado en la BD con el mismo nombre o mismo correo electronico
		User userSameName = userDAO.get(user.getUsername());
		User userSameEmail = userDAO.getByEmail(user.getEmail());
		
		
		if (user.validate(messages)) {

			logger.info("Data CheckRegister validated: " + user.getUsername());
			
			if(userSameName == null && userSameEmail == null) {
				
				// Insertamos el nuevo usuario registrado en la BD con el siguiente proceso
				userDAO.add(user);							
				logger.info("DDDDDDDDDDDDDDD");
				response.sendRedirect("LoginUserServlet.do");
			}
			else {
				logger.info("The user already exits at DB");
				response.sendRedirect("RegisterUserServlet.do");

			}
		}
		else {
			
			logger.info("Data CheckRegister cannot be validated: " + user.getUsername());
			response.sendRedirect("RegisterUserServlet.do");
			
			// Ejemplo traza
			for (String clave: messages.keySet()) {
				String m = messages.get(clave);
				logger.info("Clave: " + clave + ", valor: " + m);
			}			
		}
	}
}
