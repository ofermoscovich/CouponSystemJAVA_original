package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * @author Ofer Moscovich
 * Singleton Connect to DB server and DB url using parameters from file.
 * pool of 10 connections + get url string from file.
 **/

public class ConnectionPool {

	private static ConnectionPool instance;
	private static final long maxConnections = 10;
	private static Set<Connection> connections;
	private static String databaseUrl;

	/**
	 * Constructor
	 * Get params for DB server and Derby DB connection.
	 * @throws CouponException
	 */
	private ConnectionPool() throws CouponException {
		connections = new HashSet<Connection>();
	
		try {
			/**
			 * Get driver from file
			 */
//			Class.forName(GetFileParms.getDriverName());
//			databaseUrl = GetFileParms.getDbUrl();

			databaseUrl = "jdbc:derby://localhost:1527/db_coupons;create=true";
			Class.forName("org.apache.derby.jdbc.ClientDriver");

			/**
			 * Get and Set connections in array
			 * Initiate maxConnection Connections 
			 */
			for (int i = 0; i < maxConnections; i++) {
				connections.add(DriverManager.getConnection(databaseUrl));
			}
			
		} catch (Exception e) {
			//closeAllConnections();
			throw new CouponException("Connection Pool Startup Error");
		}
	
	}
	
	/**
	 * @return ConnectionPool
	 * ofer method - SINGLETON instance 
	 */
	public static ConnectionPool getInstance() throws CouponException{
		if (instance==null)instance = new ConnectionPool();
		return instance;
	}

	/**
	 * Methods get from Connection pool
	 * @return Connection
	 * @throws CouponException
	 */
	public synchronized Connection getConnection() throws CouponException{

		while (connections.size()==0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				System.out.println("Interrupted");
			}
		}
		
		Iterator<Connection> iterator = connections.iterator();
		Connection con = iterator.next();
		iterator.remove();
		return con; 
	}			
	
	/**
	 * Methods return connection to Connection pool
	 * @throws CouponException
	 */
	public synchronized void returnConnection(Connection con)throws CouponException{ //throws Exception {
		try {
			con.setAutoCommit(true);
		} catch (SQLException e) {
			throw new CouponException("ERROR! Return Connection Properly Failed!");
		}
		connections.add(con);
		this.notify();
	}

	/**
	 * Close all Connections
	 * @throws CouponException
	 */
	public synchronized void closeAllConnections() throws CouponException{
		
		while (connections.size()==0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				System.out.println("Interrupted");
			}
		}
		
		Iterator<Connection> iterator = connections.iterator();
		while(iterator.hasNext()) {
			try {
				iterator.next().close();
			} catch (SQLException e) {
				throw new CouponException("Connections: Close All Connection: Error!");
			}
		}
	}
}
