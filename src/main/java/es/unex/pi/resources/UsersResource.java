package es.unex.pi.resources;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import es.unex.pi.dao.JDBCUserDAOImpl;
import es.unex.pi.dao.UserDAO;
import es.unex.pi.model.User;

@Path("/users")
public class UsersResource {
	private static final Logger logger = Logger.getLogger(HttpServlet.class.getName());

	  @Context
	  ServletContext sc;
	  @Context
	  UriInfo uriInfo;
  
	  
	  @GET
	  @Produces(MediaType.APPLICATION_JSON)
	  public User getUserJSON(@Context HttpServletRequest request) {
		Connection conn = (Connection) sc.getAttribute("dbConn");
		UserDAO userDao = new JDBCUserDAOImpl();
		userDao.setConnection(conn);
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
				
		User returnUser =userDao.get(user.getUsername());
		
		return returnUser; 
	  }
	  
	  
	  @PUT
	  @Produces(MediaType.APPLICATION_JSON)
	  public Response put(User userUpdate, @Context HttpServletRequest request) {
		
		Connection conn = (Connection) sc.getAttribute("dbConn");
		UserDAO userDao = new JDBCUserDAOImpl();
		userDao.setConnection(conn);
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
				
		Response response = null;
		
		if(user != null) {
			Map<String, String> messages = new HashMap<String, String>();
			if(userUpdate.validate(messages)) {
				userUpdate.setId(user.getId());
				userDao.save(userUpdate);
				session.removeAttribute("user");
				session.setAttribute("user", userUpdate);
				
			}
		}
		else {
			  throw new WebApplicationException(Response.Status.NOT_FOUND);			
		}		
		return response; 
	  }
	  
	  
	  @DELETE
	  @Produces(MediaType.APPLICATION_JSON)
	  public Response delete(@Context HttpServletRequest request) {
		
		Connection conn = (Connection) sc.getAttribute("dbConn");
		UserDAO userDao = new JDBCUserDAOImpl();
		userDao.setConnection(conn);
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
	
		if(user != null) {
				userDao.delete(user.getId());
				session.invalidate();
				return Response.noContent().build(); //204 no content 
		}
		else {
			  throw new WebApplicationException(Response.Status.NOT_FOUND);			
		}		
	  }
	  
} 
