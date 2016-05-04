package activities;

import beans.ClientType;
import clients.*;
import main.ConnectionPool;
import main.CouponException;

/** 
 * @author Ofer Moscovich 
 * Main System: Authorization system - return the properly facade for the login user. 
 * Also will start and shutdown the Deletion Thread.  
 **/
public class CouponSystem {
	
	/**
	 * SINGLETON 
	 **/
	private static CouponSystem instance;
	DailyCouponExpirationTask DailyTask;
	Thread thread;
	
	/**
	 * Thread timer - 1000*3600*24 is every 24 hours
	 * 100 Milliseconds works for testing.
	 */
	private static final int DAY = 1000*3600*24;
	private static final int SLEEPTIME = 1*DAY; 
	
	
	/**
	 * Constructor: Start Daily Coupon Expiration Task thread.
	 * @throws CouponException
 	 **/
	public CouponSystem() throws CouponException {
		// Activate the daily Coupons Deletion Demon (Thread)
		DailyTask = new DailyCouponExpirationTask(SLEEPTIME);
		thread = new Thread(DailyTask);
		thread.start();
	}
	
	/**
	 * SINGLETON Setting
	 */
	public static CouponSystem getInstance() throws CouponException {
		if (instance==null)instance = new CouponSystem();
		return instance;
	}

	/**
	 * The login method enable access to the system	     		
	 * 															
	 * @param user String - user using the system.				
	 * @param pass String - password to the system.				
	 * @param clientType ClientType - Customer or Company or Admin
	 * 			to return Facade.								
	 * 															
	 * @return CouponClientFacadean - object Facade gives access to a package of actions.	
	 * @throws CouponException 										
	 **/
	public CouponClientFacade login(String user,String pass,ClientType clientType) throws CouponException {

		CouponClientFacade couponClientFacade = null;
		
		switch (clientType) {
		case ADMINFACADE:
			couponClientFacade = new AdminFacade();
			break;
		case COMPANYFACADE:
			couponClientFacade = new CompanyFacade();
			break;
		case CUSTOMERFACADE:
			couponClientFacade = new CustomerFacade();
			break;
		default:
			couponClientFacade = null;
		} 
		
		if (couponClientFacade != null) {
			couponClientFacade = couponClientFacade.login(user,pass);
			if (couponClientFacade != null) {
				return couponClientFacade;
			} else {
				throw new CouponException("STOP! Login Falied! Invalid User or Password!");
			}
		} else {
			throw new CouponException("STOP! Login Falied! Invalid User Type!");
		}
	}
	
	/**
	 * Shutdown system.                          
	 * Close all Connection Pool connections.	
	 * Stop daily coupon expiration task deletion Thread. 
	 **/
	public void shutdown() throws CouponException{

		try {
			ConnectionPool connectionPool = ConnectionPool.getInstance();
			connectionPool.closeAllConnections();
		} catch (CouponException e) {
			throw new CouponException("ERROR! Properly Shut Down Application Failed!");
		}
		DailyTask.stopTask();
	}
}
