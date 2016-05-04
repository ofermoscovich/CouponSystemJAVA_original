package beans.DAO;

import java.sql.Timestamp;
import java.util.Set;

import beans.Coupon;
import beans.CouponType;
import main.CouponException;

/**
 * @author Ofer Moscovich
 * Abstract methods for Coupon entity 
 */
public interface CouponDAO {
	
	/**
	 *  General and basic activities
	 */
	public void createCoupon(long compId, Coupon coupon) throws CouponException ;
	public void removeCoupon(long coupId) throws CouponException ;
	public void updateCoupon(long compId, Coupon coupon) throws CouponException ;
	public Coupon getCoupon(long coupId) throws CouponException ;
	public Set<Coupon> getCoupons(long compId,long custId,long coupId, boolean isExpiered) throws CouponException;

	/**
	 *  Company activities
	 */
	public Set<Coupon> getCouponsByType(long compId, CouponType coupType) throws CouponException;
	public Set<Coupon> getCouponsByMaxCouponPrice(long compId, double price) throws CouponException;
	public Set<Coupon> getCouponsByMaxCouponDate(long compId, Timestamp maxCouponDate) throws CouponException;
	public boolean isCouponExistsForCompany(long compId, long coupId) throws CouponException;
	public boolean isCouponTitleExists(String coupTitle) throws CouponException;

	/**
	 *  Customer activities
	 */
	public void purchaseCoupon(long custId, long coupId) throws CouponException ;
	public Set<Coupon> getCouponsByType(CouponType coupType) throws CouponException;
	public Set<Coupon> getAllPurchasedCoupons(long custId, CouponType type, double price) throws CouponException ;
	public Set<Coupon> getAllPurchasedCoupons(long custId) throws CouponException;
	public Set<Coupon> getAllPurchasedCoupons() throws CouponException;
	public Set<Coupon> getAllPurchasedCouponsByType(long custId, CouponType type) throws CouponException ;
	public Set<Coupon> getAllPurchasedCouponsByPrice(long custId,double price) throws CouponException ;
	public boolean isCouponPurchasedByCustomer(long custId,long coupId) throws CouponException;
	/**
	 *  Daily Task (Thread)
	 */
	public void removeExpiredCoupons() throws CouponException;

}
