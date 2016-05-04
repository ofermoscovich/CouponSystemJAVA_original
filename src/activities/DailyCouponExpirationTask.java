/**
 * 
 */
package activities;

import beans.DAO.CouponDAO;
import beans.DBDAO.CouponDBDAO;
import main.CouponException;

/**
 * @author Ofer Moscovich
 * Daily Thread: Check and delete expired coupons in coupon table and join 
 *  (Coupon, CompanyCoupon, CustomerCoupon)
 */
public class DailyCouponExpirationTask implements Runnable {
	
	private CouponDAO coupDAO;
	private int sleepTime;
	private boolean quit = false;
		
	/**
	 * Constructor
	 * @param sleepTime - sleep time days.
	 **/
	public DailyCouponExpirationTask(int sleepTime) throws CouponException {
		coupDAO = new CouponDBDAO();
		this.sleepTime = sleepTime;
	}

	/**
	 * The Thread run action - delete expired Coupons.
	 */
	@Override
	public void run() {
		while (!this.quit) {
			try {
				coupDAO.removeExpiredCoupons();
				
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				System.out.println("Interrupted!");
			} catch (CouponException e) {
				// No need
			} 
		}
	}

	/**
	 * stop task (attribute quit set true will stop deletion Thread)
	 */
	public void stopTask() {
		this.quit = true;
	}
	
}
