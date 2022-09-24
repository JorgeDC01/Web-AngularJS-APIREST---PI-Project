package es.unex.pi.dao;

import java.sql.Connection;
import java.util.List;

import es.unex.pi.model.HostingCategories;
import es.unex.pi.model.UserHosting;

public interface UserHostingDAO {

	/**
	 * set the database connection in this DAO.
	 * 
	 * @param conn
	 *            database connection.
	 */
	public void setConnection(Connection conn);
	
	/**
	 * Gets all the users and hosting related to them from the database.
	 * 
	 * @return List of all the hosting and the users related to them from the database.
	 */
	
	public List<UserHosting> getAll();
	
	/**
	 *Gets all the UserHosting that are related to a hosting.
	 * 
	 * @param idh
	 *            Category identifier
	 * 
	 * @return List of all the UserHosting related to a hosting.
	 */
	public List<UserHosting> getAllByHost(long idh);
	
	/**
	 * Gets all the UserHosting that contains an specific user.
	 * 
	 * @param idh
	 *            User Identifier
	 * 
	 * @return List of all the UserHosting that contains an specific user
	 */
	public List<UserHosting> getAllByUser(long idu);
	
	/**
	 * Gets a UserHosting from the DB using idh and idu.
	 * 
	 * @param idh 
	 *            hosting identifier.
	 *            
	 * @param idu
	 *            User Identifier
	 * 
	 * @return UserHosting with that idh and idu.
	 */
	
	public UserHosting get(long idh,long idu);
	
	/**
	 * Adds an UserHosting to the database.
	 * 
	 * @param UserHosting
	 *            UserHosting object with the details of the relation between the hosting and the user.
	 * 
	 * @return hosting identifier or -1 in case the operation failed.
	 */
	
	public boolean add(UserHosting userHosting);
	
	/**
	 * Deletes an UserHosting from the database.
	 * 
	 * @param idh
	 *            Hosting identifier.
	 *            
	 * @param idu
	 *            User Identifier
	 * 
	 * @return True if the operation was made and False if the operation failed.
	 */
	
	public boolean delete(long idh, long idu);
}
