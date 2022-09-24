package es.unex.pi.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import es.unex.pi.model.UserHosting;

public class JDBCUserHostingDAOImpl implements UserHostingDAO {

	private Connection conn;
	private static final Logger logger = Logger.getLogger(JDBCUserHostingDAOImpl.class.getName());
	
	@Override
	public void setConnection(Connection conn) {
		this.conn = conn;
	}

	
	@Override
	public List<UserHosting> getAll() {
		
		if (conn == null) return null;
		
		ArrayList<UserHosting> UserHostingList = new ArrayList<UserHosting>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM UserHosting");
						
			while ( rs.next() ) {
				UserHosting UserHosting = new UserHosting();
				UserHosting.setIdh(rs.getInt("idh"));
				UserHosting.setIdu(rs.getInt("idu"));
						
				UserHostingList.add(UserHosting);
				logger.info("fetching all UserHosting: "+UserHosting.getIdh()+" "+UserHosting.getIdu());
					
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return UserHostingList;
	}

	
	@Override
	public List<UserHosting> getAllByHost(long idh) {
		
		if (conn == null) return null;
		
		ArrayList<UserHosting> UserHostingList = new ArrayList<UserHosting>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM UserHosting WHERE idh="+idh);
						
			while ( rs.next() ) {
				UserHosting UserHosting = new UserHosting();
				UserHosting.setIdh(rs.getInt("idh"));
				UserHosting.setIdu(rs.getInt("idu"));
						
				UserHostingList.add(UserHosting);
				logger.info("fetching all UserHosting by idh: "+UserHosting.getIdu()+" "+UserHosting.getIdh());
					
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return UserHostingList;
	}

	
	@Override
	public List<UserHosting> getAllByUser(long idu) {
		
		if (conn == null) return null;
		
		ArrayList<UserHosting> UserHostingList = new ArrayList<UserHosting>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM UserHosting WHERE idu="+idu);
						
			while ( rs.next() ) {
				UserHosting UserHosting = new UserHosting();
				UserHosting.setIdh(rs.getInt("idh"));
				UserHosting.setIdu(rs.getInt("idu"));
						
				UserHostingList.add(UserHosting);
				logger.info("fetching all UserHosting by idu: "+UserHosting.getIdh()+" "+UserHosting.getIdu());
					
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return UserHostingList;
	}

	
	@Override
	public UserHosting get(long idh, long idu) {
		
		if (conn == null) return null;
		
		UserHosting UserHosting = null;		
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM UserHosting WHERE Idh="+idh+" AND idu="+idu);			 
			if (!rs.next()) return null;
			UserHosting= new UserHosting();
			UserHosting.setIdh(rs.getInt("idh"));
			UserHosting.setIdu(rs.getInt("idu"));

			logger.info("fetching UserHosting by idh: "+UserHosting.getIdh()+"  and idu: "+UserHosting.getIdu());
		
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return UserHosting;
	}

	
	@Override
	public boolean add(UserHosting userHosting) {
		boolean done = false;
		if (conn != null){
			
			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("INSERT INTO UserHosting (idu,idh) VALUES('"+
									userHosting.getIdu()+"','"+
									userHosting.getIdh()+"')");
						
				logger.info("creating HostingCategories:("+userHosting.getIdu()+" "+userHosting.getIdh());
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;
	}

	@Override
	public boolean delete(long idh, long idu) {
		boolean done = false;
		if (conn != null){

			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("DELETE FROM UserHosting WHERE idu ="+idu+" AND idh="+idh);
				logger.info("deleting UserHosting: "+idu+" , idh="+idh);
				done= true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return done;
	}

}
