package es.unex.pi.resources;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import es.unex.pi.dao.JDBCHostingDAOImpl;
import es.unex.pi.dao.JDBCUserDAOImpl;
import es.unex.pi.dao.JDBCUserHostingDAOImpl;
import es.unex.pi.dao.UserDAO;
import es.unex.pi.dao.UserHostingDAO;
import es.unex.pi.resources.exceptions.CustomNotFoundException;
import es.unex.pi.util.LikeComparator;
import es.unex.pi.util.Triplet;
import es.unex.pi.dao.HostingCategoriesDAO;
import es.unex.pi.dao.HostingDAO;
import es.unex.pi.dao.JDBCHostingCategoriesDAOImpl;
import es.unex.pi.model.Hosting;
import es.unex.pi.model.HostingCategories;
import es.unex.pi.model.User;
import es.unex.pi.model.UserHosting;
import es.unex.pi.resources.exceptions.CustomBadRequestException;
import es.unex.pi.resources.exceptions.CustomNotFoundException;

@Path("/hosts")
public class HostsResource {
	private static final Logger logger = Logger.getLogger(HttpServlet.class.getName());

	// Con estos dos atributos, podemos acceder al Contexto de la aplicación y a la información 
	// de la URI obtenida de la request.
	@Context
	ServletContext sc;
	@Context
	UriInfo uriInfo;
	
	
	@GET 
	@Path("/code/{code}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Triplet<Hosting, String, List<HostingCategories>>> getHostsJSON(@PathParam("code") long code, 
			@Context HttpServletRequest request) {

		List<Hosting> hostingList = null;
		HttpSession session = request.getSession();
		
		// Obtenemos el minimo de likes del filtrado del Header
		int minLikes = Integer.parseInt(request.getHeader("minLikes"));
		// Obtenemos el id del host a detallar
		long id_hostDetails = Long.parseLong(request.getHeader("id_hostDetails"));
		
		logger.info("Petición getHost con code: "+code+" y mínimo de Likes: "+request.getHeader("minLikes"));
		// Obtenemos la conexión de la BD
		Connection con = (Connection) sc.getAttribute("dbConn");
		HostingDAO hostingDAO = new JDBCHostingDAOImpl();
		hostingDAO.setConnection(con);
		
		// Pasamos la conexion al DAO de los usuarios
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(con);
		
		// Pasamos la conexion al DAO de alojamientos&categorias
		HostingCategoriesDAO HostingsCategoriesDAO = new JDBCHostingCategoriesDAOImpl();
		HostingsCategoriesDAO.setConnection(con);
		
		// Pasamos la conexión al DAO de User&Hosting
		UserHostingDAO userHostingDAO = new JDBCUserHostingDAOImpl();
		userHostingDAO.setConnection(con);
		
		logger.info("CODE: "+code);
		hostingList = getHostsByParameter(code,minLikes,id_hostDetails);
		Iterator<Hosting> itHostingList = hostingList.iterator();	
		
		// Parametro del request que contiene para cada alojamiento: nombre de usuario y categorias
		List<Triplet<Hosting, String, List<HostingCategories>>> HostingsUserList = new ArrayList<Triplet<Hosting, String, List<HostingCategories>>>();
		
		while(itHostingList.hasNext()) {
			
			Hosting Hosting = (Hosting) itHostingList.next();
			User user = userDAO.get(Hosting.getIdu());
			List<HostingCategories> HostingsCategories = HostingsCategoriesDAO.getAllByHosting(Hosting.getId());
						
			
			// Buscamos en la tabla 'UserHosting' si existe un registro donde coincida el id del host y el del usuario
			User user_logged_in = (User) session.getAttribute("user");

			if(userHostingDAO.get(Hosting.getId(),(long) user_logged_in.getId()) != null) {
				Hosting.setIsFav("YES");
			}
			else {
				Hosting.setIsFav("NO");
			}
			
			HostingsUserList.add(new Triplet<Hosting, String, List<HostingCategories>>(Hosting,user.getUsername(),HostingsCategories));
		}
		return HostingsUserList; 
	}
	
	
	@GET 					  
	@Path("/{hostid}")	 
	@Produces(MediaType.APPLICATION_JSON)
	public Triplet<Hosting, String, List<HostingCategories>> getHostJSON(@PathParam("hostid") long hostid, @Context HttpServletRequest request) {

		Hosting host = null;

		Connection con = (Connection) sc.getAttribute("dbConn");
		HostingDAO hostingDAO = new JDBCHostingDAOImpl();
		hostingDAO.setConnection(con);
		
		UserDAO userDao = new JDBCUserDAOImpl();
		userDao.setConnection(con);
		
		HostingCategoriesDAO hostingCategoriesDAO = new JDBCHostingCategoriesDAOImpl();
		hostingCategoriesDAO.setConnection(con);
		
		UserHostingDAO userHostingDAO = new JDBCUserHostingDAOImpl();
		userHostingDAO.setConnection(con);
		
		host = hostingDAO.get(hostid);
		// Si el host no existe, devuelve una 'response' con error 404 de recurso 'host' no encontrado
		if(host != null) {
			List<HostingCategories> categories = hostingCategoriesDAO.getAllByHosting(host.getId());
			User user = userDao.get(host.getIdu());
			
			HttpSession session = request.getSession();
			User user_logged = (User) session.getAttribute("user");
			if(userHostingDAO.get(host.getId(),(long) user_logged.getId()) != null) {
				host.setIsFav("YES");
			}
			else {
				host.setIsFav("NO");
			}
			
			Triplet<Hosting, String, List<HostingCategories>> HostingUserCat = new Triplet<Hosting, String, List<HostingCategories>>(host,user.getUsername(),categories);
			
			return HostingUserCat;
		}
		else {
			throw new CustomNotFoundException("Host ("+ hostid + ") no ha sido encontrado");
		}
	}
	
	
	@POST	  	  
	@Consumes(MediaType.APPLICATION_JSON)
	public Response post(Triplet<Hosting, String, List<HostingCategories>> newHost, @Context HttpServletRequest request) throws Exception {	

		HostingDAO hostingDAO = null;
		User user= null;

		Connection con = (Connection) sc.getAttribute("dbConn");
		
		hostingDAO = new JDBCHostingDAOImpl();
		hostingDAO.setConnection(con);

		HostingCategoriesDAO hostingCategoriesDAO = new JDBCHostingCategoriesDAOImpl();
		hostingCategoriesDAO.setConnection(con);
		
		HttpSession session = request.getSession();
		user = (User) session.getAttribute("user");

		Response res;

		if (user.getId() != newHost.getFirst().getIdu())
			throw new CustomBadRequestException("Errors in parameters");

		long id = hostingDAO.add(newHost.getFirst());
		
		// Insertamos las categorias
		Iterator<HostingCategories> itHostingCategoriesNewList = newHost.getThird().iterator();
		
		while(itHostingCategoriesNewList.hasNext()) {
			HostingCategories host_cat_aux = itHostingCategoriesNewList.next();
			host_cat_aux.setIdh(id);
			hostingCategoriesDAO.add(host_cat_aux);
		}
		
		res = Response 								// Status code 201
				.created(
						uriInfo.getAbsolutePathBuilder()
						.path(Long.toString(id))
						.build())
				.contentLocation(					//Content Location: /hosts/newid
						uriInfo.getAbsolutePathBuilder()
						.path(Long.toString(id))
						.build())
				.build();

		return res; 
	}
	
	
	//POST Recibe parametros via formulario
	@POST	  	 
	@Consumes("application/x-www-form-urlencoded")
	public Response post(MultivaluedMap<String, String> formParams,
			@Context HttpServletRequest request, @FormParam("services") List<String> servicesList) {	

		HostingDAO hostingDAO = null;
		User user = null;

		Connection con = (Connection) sc.getAttribute("dbConn");
		hostingDAO = new JDBCHostingDAOImpl();
		hostingDAO.setConnection(con);

		HttpSession session = request.getSession();
		user = (User) session.getAttribute("user");

		Response res;
		Hosting host = new Hosting();
		
		host.setTitle(formParams.getFirst("title"));
		host.setTelephone(formParams.getFirst("telephone"));
		host.setPrice(Integer.parseInt(formParams.getFirst("price")));
		host.setLocation(formParams.getFirst("located"));
		host.setDescription(formParams.getFirst("descrip"));
		host.setContactEmail(formParams.getFirst("email"));
		host.setRedSocial(formParams.getFirst("twitter"));
		host.setIdu((int) user.getId());
		host.setLikes(0);
		
		// Disponibilidad
		List<String> list = formParams.get("available");
		
		if(list.isEmpty()) {
			host.setAvailable(1);
		}
		else {
			host.setAvailable(0);
		}
		
		// Establecemos los servicios		
		String aux = null;

		if(servicesList.isEmpty()) {
			aux = " ";
		}
		else {

			Iterator<String> itServicesList = servicesList.iterator();
			while(itServicesList.hasNext()) {
				aux = aux + itServicesList.next() + ",";
			}
		}
		
		host.setServices(aux);
		
		long id_host = hostingDAO.add(host);
		
		// Obtenemos el conjunto de valores (0,1,2,3) correspondientes a las categorias marcadas
		List<String> categoriesList = formParams.get("categories");
		
		if(!categoriesList.isEmpty()) {
			boolean insertCategories = true;
			HostingCategoriesDAO h_c_DAO = new JDBCHostingCategoriesDAOImpl();
			h_c_DAO.setConnection(con);
			
			for(int i = 0; i < categoriesList.size() && insertCategories; i++) {
				
				// Creamos el objeto que unifica los id del 'host' y la categoria
				HostingCategories hostingCategories = new HostingCategories();
				
				hostingCategories.setIdct((Long.parseLong(categoriesList.get(i))));
				hostingCategories.setIdh(id_host);
				
				if(!h_c_DAO.add(hostingCategories)) {
					insertCategories = false;
				}
			}
		}
		
		res = Response //return 201 and Location: /orders/newid
				.created(
						uriInfo.getAbsolutePathBuilder()
						.path(Long.toString(id_host))
						.build())
				.contentLocation(
						uriInfo.getAbsolutePathBuilder()
						.path(Long.toString(id_host))
						.build())
				.build();
		return res;  
	}
	
	
	@PUT
	@Path("/like/{hostid}") 
	public Response likeNotLikeHost(@PathParam("hostid") long hostid,
			@Context HttpServletRequest request) throws Exception{

		HostingDAO hostingDAO = null;
		User user= null;

		Connection con = (Connection) sc.getAttribute("dbConn");
		hostingDAO = new JDBCHostingDAOImpl();
		hostingDAO.setConnection(con);
		
		UserHostingDAO userHostingDAO = new JDBCUserHostingDAOImpl();
		userHostingDAO.setConnection(con);
		
		HttpSession session = request.getSession();
		user = (User) session.getAttribute("user");
		
		Response response = null;

		Hosting host = hostingDAO.get(hostid);
		
		if (host != null) {

			if (host.getId() != hostid) {
				throw new CustomBadRequestException("Error en el id");
			} 
			else {
				
				UserHosting userHosting = userHostingDAO.get(host.getId(), (long) user.getId());
				// Si no hay ningun like registrado, entonces se inserta y se incrementa en uno los likes del host
				if (userHosting == null) {

					userHosting = new UserHosting();
					userHosting.setIdh(hostid);
					userHosting.setIdu((long) user.getId());
					userHostingDAO.add(userHosting);

					host.setLikes(host.getLikes() + 1);
				} else { // El usuario le ha dado Like anteriormente. Se tiene que eliminar el Like

					userHostingDAO.delete(userHosting.getIdh(), userHosting.getIdu());
					host.setLikes(host.getLikes() - 1);
				}
				hostingDAO.save(host);
			}
		}
		else throw new WebApplicationException(Response.Status.NOT_FOUND);			

		return response;
	}
	
	@PUT
	@Path("/{hostid}") 
	@Consumes(MediaType.APPLICATION_JSON)
	public Response put(Triplet<Hosting, String, List<HostingCategories>> hostingUpdate,
			@PathParam("hostid") long hostid,
			@Context HttpServletRequest request) throws Exception{

		HostingDAO hostingDAO = null;
		User user= null;

		Connection con = (Connection) sc.getAttribute("dbConn");
		hostingDAO = new JDBCHostingDAOImpl();
		hostingDAO.setConnection(con);
		
		HostingCategoriesDAO hostingCategoriesDAO = new JDBCHostingCategoriesDAOImpl();
		hostingCategoriesDAO.setConnection(con);
		
		HttpSession session = request.getSession();
		user = (User) session.getAttribute("user");
		Response response = null;


		Hosting host = hostingDAO.get(hostingUpdate.getFirst().getId());
		if (host != null) {
			
			if (host.getId() != hostid) {
				throw new CustomBadRequestException("Error en el id");
			}
			else {

				if(host.getIdu() != user.getId()) {
					throw new CustomBadRequestException("Error: no eres propietario del alojamiento");
				}
				else {
					
					// Primero insertamos las categorias, obteniendo todas las tuplas de la tabla HostingCategories y se borran
					List<HostingCategories> hostingCategoriesList = hostingCategoriesDAO.getAllByHosting(host.getId());
					Iterator<HostingCategories> itHostingCategoriesList = hostingCategoriesList.iterator();
					
					while(itHostingCategoriesList.hasNext()) {
						HostingCategories host_cat_aux = itHostingCategoriesList.next();
						hostingCategoriesDAO.delete(host_cat_aux.getIdh(), host_cat_aux.getIdct());
					}
					
					// Recorremos la lista de HostingCategories del Triplet e insertamos en la BD
					Iterator<HostingCategories> itHostingCategoriesUpdateList = hostingUpdate.getThird().iterator();
					
					while(itHostingCategoriesUpdateList.hasNext()) {
						HostingCategories host_catUpdate_aux = itHostingCategoriesUpdateList.next();
						hostingCategoriesDAO.add(host_catUpdate_aux);
					}
										
					hostingDAO.save(hostingUpdate.getFirst());
				}
			}
		}
		else throw new WebApplicationException(Response.Status.NOT_FOUND);			

		return response;
	}
	
	@DELETE
	@Path("/{hostid}") 	  
	public Response deleteHost(@PathParam("hostid") long hostid,
			@Context HttpServletRequest request) {

		HostingDAO hostingDAO = null;
		User user= null;

		Connection con = (Connection) sc.getAttribute("dbConn");
		hostingDAO = new JDBCHostingDAOImpl();
		hostingDAO.setConnection(con);

		HostingCategoriesDAO hostingCategoriesDAO = new JDBCHostingCategoriesDAOImpl();
		hostingCategoriesDAO.setConnection(con);
		
		UserHostingDAO userHostingDAO = new JDBCUserHostingDAOImpl();
		userHostingDAO.setConnection(con);
		
		HttpSession session = request.getSession();
		user = (User) session.getAttribute("user");

		Hosting host = hostingDAO.get(hostid);
		if ((host != null) && (host.getIdu() == user.getId())){
			
			List<HostingCategories> hostingCategoriesList = hostingCategoriesDAO.getAllByHosting(hostid);
			Iterator<HostingCategories> itHostingCategoriesList = hostingCategoriesList.iterator();
			
			// Eliminamos todas las tuplas de la tabla HostingCategories relacionadas con el alojamiento a borrar
			while(itHostingCategoriesList.hasNext()) {
				logger.info("AAAAAAAA: BORRANDO CATEGORIA");
				HostingCategories hostingCategories = itHostingCategoriesList.next();
				if(!hostingCategoriesDAO.delete(hostingCategories.getIdh(), hostingCategories.getIdct())) {
					logger.info("Error al borrar la tupla <Host,category> de la BD");
				}
			}
			
			List<UserHosting> userHostingList = userHostingDAO.getAllByHost(hostid);
			Iterator<UserHosting> itUserHostingList = userHostingList.iterator();

			// Eliminamos todas las tuplas de la tabla UserHosting relacionadas con los Likes del alojamiento
			while(itUserHostingList.hasNext()) {
				logger.info("BBBBBBBB: BORRANDO LIKE");
				UserHosting userHosting = itUserHostingList.next();
				if(!userHostingDAO.delete(userHosting.getIdh(), userHosting.getIdu())) {
					logger.info("Error al borrar la tupla <Host,User> de la BD");
				}
			}
			
			hostingDAO.delete(hostid);
			return Response.noContent().build(); //204 sin contenido
		}
		else throw new CustomBadRequestException("Error en el id del host");		

	}
	
	/*
	 * This method return the hosting depending on the code given in the request.
	 * 
	 * @param request The request. int code: 1: Available hosting. 2: Reserved
	 * hosting. 3: Hosting Ordered by likes. 4: Hosting Ordered with a MIN.Likes.
	 * 
	 * @return code Int
	 */
	protected List<Hosting> getHostsByParameter(long code, int minLikes, long id_hostDetails) {

		// Obtenemos la conexión
		Connection con = (Connection) sc.getAttribute("dbConn");
		HostingDAO hostingDAO = new JDBCHostingDAOImpl();
		hostingDAO.setConnection(con);

		// Pasamos la conexion al DAO de alojamientos&categorias
		HostingCategoriesDAO HostingsCategoriesDAO = new JDBCHostingCategoriesDAOImpl();
		HostingsCategoriesDAO.setConnection(con);
		
		// Establecemos el código para filtrar los Hosting
		int codeInt;

		codeInt = (int) code;

		List<Hosting> hostingList = new ArrayList<Hosting>();
		switch (codeInt) {
		case 0:
			hostingList = hostingDAO.getAll();
			break;
		case 1:
			hostingList = hostingDAO.getAll();
			Iterator<Hosting> itHostingList = hostingList.iterator();

			while (itHostingList.hasNext()) {
				Hosting aux = itHostingList.next();
				if (aux.getAvailable() == 1) {
					itHostingList.remove();
				}
			}
			break;
		case 2:
			hostingList = hostingDAO.getAll();
			Iterator<Hosting> itHostingReservedList = hostingList.iterator();

			while (itHostingReservedList.hasNext()) {
				Hosting aux = itHostingReservedList.next();
				if (aux.getAvailable() == 0) {
					itHostingReservedList.remove();
				}
			}
			break;
		case 3:
			hostingList = hostingDAO.getAll();
			hostingList.sort(new LikeComparator());
			break;
		
		case 4: 
			hostingList = hostingDAO.getAllByMinLikes(minLikes);
			hostingList.sort(new LikeComparator()); 
			break;
		
		case 5:
			Hosting host = hostingDAO.get(id_hostDetails);
			// Obtenemos los servicios separados por comas en un vector
			String service = host.getServices();
			String servicesVector[] = service.split(",");

			/* Primero se realizará la búsqueda por localidad similar */
			hostingList.addAll(hostingDAO.getAllByLocation(host.getLocation()));

			/* En segundo lugar se realizará la búsqueda por precio */
			hostingList.addAll(hostingDAO.getAllByPrice(host.getPrice()));

			/* En tercer lugar se realizará la búsqueda por servicios */
			if (servicesVector.length != 0) {
				List<Hosting> aux = hostingDAO.getAll();
				Iterator<Hosting> itAux = aux.iterator();

				// Para cada host de la BD
				while (itAux.hasNext()) {
					Hosting hostAux = itAux.next();
					String serviceAux = hostAux.getServices();

					boolean encontrado = false;

					// Para cada servicio del host aux
					for (int i = 0; i < servicesVector.length && !encontrado; i++) {
						if (serviceAux.contains(servicesVector[i])) {
							encontrado = true;
							hostingList.add(hostAux);
						}
					}

				}
			}

			/* En cuarto lugar se realizará la búsqueda por categorías */
			List<HostingCategories> hostingCategoriesList = HostingsCategoriesDAO.getAllByHosting(id_hostDetails);
			List<HostingCategories> hostingCategoriesRelatedList = new ArrayList<>();

			Iterator<HostingCategories> itHostingCategoriesList = hostingCategoriesList.iterator();

			while (itHostingCategoriesList.hasNext()) {
				HostingCategories aux = itHostingCategoriesList.next();
				// Por cada categoria del Host detallado, se desean buscar el resto de
				// alojamientos que la compartan
				hostingCategoriesRelatedList.addAll(HostingsCategoriesDAO.getAllByCategory(aux.getIdct()));

				Iterator<HostingCategories> itHostingCategoriesRelatedList = hostingCategoriesRelatedList.iterator();
				while (itHostingCategoriesRelatedList.hasNext()) {
					hostingList.add(hostingDAO.get(itHostingCategoriesRelatedList.next().getIdh()));
				}
			}

			// Se eliminan todos los host que coincidan con aquel sobre el que se desea
			// realizar busqueda de relacionados
			Iterator<Hosting> itList = hostingList.iterator();

			while (itList.hasNext()) {
				if (itList.next().getId() == host.getId()) {
					itList.remove();
				}
			}

			// Hay que eliminar duplicados con respecto el ID
			Set<Hosting> set = new LinkedHashSet<>();
			set.addAll(hostingList);
			hostingList.clear();
			hostingList.addAll(set);
			break;
		}
		return hostingList;
	}
}

