/**
 * 
 */
package clients;

import java.util.Set;
import activities.AppUtil;
import beans.Coupon;
import beans.CouponType;
import beans.Customer;
import beans.DAO.CouponDAO;
import beans.DAO.CustomerDAO;
import beans.DBDAO.CouponDBDAO;
import beans.DBDAO.CustomerDBDAO;
import main.CouponException;


/**
 * @author Ofer Moscovich
 * Customer Facade
 */
public class CustomerFacade extends Clients implements CouponClientFacade {

	private CustomerDAO customerDAO;
	private CouponDAO couponDAO;
	private AppUtil appUtil;
	private long customerId = 0;
	private Customer customer;

	/**
	 * Constructor
	 * @throws CouponException 
	 */
	public CustomerFacade() throws CouponException {
	
		customerDAO = new CustomerDBDAO();
		couponDAO = new CouponDBDAO();
		// Application utility
		appUtil = new AppUtil();
	}
	
	/**
	 * First Customer Login check
	 * @param name String 
	 * @param Password String 
	 * @return CouponClientFacade - the specific customer Facade (this).
	 * @throws CouponException
	 **/
	@Override
	public CouponClientFacade login(String name, String Password) throws CouponException {
		Customer customer = new Customer();
		customer = customerDAO.login(name, Password);
		if (customer != null) {
			// initiate customerId to remember in facade.
			this.customerId = customer.getId();
			this.customer = customer;
			return this; 
		} else {
			return null;
		}
	}

	/**
	 * Buy Coupon by customer method.
	 * @param coupId long 
	 * @throws CouponException
	 * Will update amount of coupons left in coupon table
	 * Coupon cannot be purchased more than once by same customer.
	 * Coupon cannot be purchased if expired.
	 * Coupon cannot be purchased if out of stock.
	 */
	public void purchaseCoupon(long coupId) throws CouponException {
		Coupon coupon = new Coupon();
		coupon = couponDAO.getCoupon(coupId);
		if (coupon != null) {
			if (coupon.getAmount() > 0) {
				if (coupon.getEndDate().getTime() >= appUtil.today().getTime()) {
					if (!couponDAO.isCouponPurchasedByCustomer(customerId,coupId)) {
						couponDAO.purchaseCoupon(customerId, coupId);
					} else {
						throw new CouponException("STOP! Coupon Was Already Purchased by Customer! Purchase Canceled!"); 
					}
				} else {
					throw new CouponException("STOP! Coupon Expired! Purchase Canceled!"); 
				}
			} else {
				throw new CouponException("STOP! No More Coupons In Stock! Purchase Canceled!"); 
			}
		} else {
			throw new CouponException("STOP! Coupon Information Not Exist! Purchase Failed!"); 
		}
	}

	/**
	 * View one customer coupon - No specific company (company id = 0)
	 * @param coupId long coupon Id
	 * @return Coupon
	 * @throws CouponException
	 */
	public Coupon getCoupon(long coupId) throws CouponException {
		return couponDAO.getCoupon(coupId);
	}
	
	/**
	 * View all customer coupons - No specific company (company id = 0)
	 * @return Coupon collection
	 * @throws CouponException
	 */
	public Set<Coupon> getCoupons() throws CouponException {
		return couponDAO.getCoupons(0,0,0,false);
	}
	
	/**
	 * View all customer coupons by type - No specific company (company id = 0)
	 * @param coupType CouponType 
	 * @return Coupon collection
	 * @throws CouponException
	 */
	public Set<Coupon> getCouponsByType(CouponType coupType) throws CouponException {
		return couponDAO.getCouponsByType(0,coupType);
	}
	

	/**
	 * View all customer coupon purchases history (not by type or price)
	 * @return Coupon collection
	 * @throws CouponException
	 */
	public Set<Coupon> getAllPurchasedCoupons() throws CouponException{
		return couponDAO.getAllPurchasedCoupons(customerId);
	}
	
	/**
	 * View all customer coupon purchases history by Coupon Type
	 * @param type CouponType 
	 * @return Coupon collection
	 * @throws CouponException
	 */
	public Set<Coupon> getAllPurchasedCouponsByType (CouponType type) throws CouponException {
		return couponDAO.getAllPurchasedCouponsByType (customerId,type);
	}
	
	/**
	 * View all customer coupon purchases history be specific argument - max price requested
	 * @param price double 
	 * @return Coupon collection
	 * @throws CouponException
	 */
	public Set<Coupon> getAllPurchasedCouponsByPrice (long price) throws CouponException {
		return couponDAO.getAllPurchasedCouponsByPrice (customerId,price);
	}
	
	/**
	 * @return the customerId
	 */
	public long getCustomerId() {
		return customerId;
	}
	
	/**
	 * @return Customer
	 */
	public Customer getCustomerInstance() {
		return customer;
	}
}
