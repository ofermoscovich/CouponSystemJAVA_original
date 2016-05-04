
package clients;

import java.sql.Timestamp;
//import java.util.Calendar;
import java.util.Set;
import beans.Company;
import beans.Coupon;
import beans.CouponType;
import beans.DAO.CompanyDAO;
import beans.DAO.CouponDAO;
import beans.DBDAO.CompanyDBDAO;
import beans.DBDAO.CouponDBDAO;
import main.CouponException;
import activities.AppUtil;
/**
 * @author Ofer Moscovich
 * Company Facade
 */
public class CompanyFacade extends Clients implements CouponClientFacade {

	private CompanyDAO companyDAO;
	private CouponDAO couponDAO ;
	private AppUtil appUtil;
	private long companyId = 0;
	private Company company;
	
	/**
	 * Constructor
	 * @throws CouponException 
	 */
	public CompanyFacade() throws CouponException {
		companyDAO = new CompanyDBDAO();
		couponDAO = new CouponDBDAO();
		appUtil = new AppUtil();
	}

	/**
	 * First Company Login check
	 * @param name String 
	 * @param Password String 
	 * @return CouponClientFacade - the specific Company facade (this).
	 * @throws CouponException
	 */
	@Override
	public CouponClientFacade login(String name, String Password) throws CouponException  {
		Company company = new Company();
		company = companyDAO.login(name, Password);
		if (company != null) {
			// initiate companyId to remember facade.
			this.companyId = company.getId();
			this.company = company;
			return this;
		} else {
			return null;
		}
	}
	
	/**
	 *  Add new coupon with unique title to the company id.
	 *  Dates will be validated.
	 *  Start date must be equal or greater than today
	 *  End date must be greater than Start date.
	 *  @param coupon Coupon 
	 *  @throws CouponException
	 */
	public void createCoupon(Coupon coupon) throws CouponException {
		if (coupon != null) {
			String CoupTitle = coupon.getTitle();
			if (CoupTitle != null) {
				Timestamp startDate = coupon.getStartDate();
				Timestamp endDate = coupon.getEndDate();
				if (startDate.getTime() <= endDate.getTime()) {
					if (startDate.getTime() >= appUtil.toDate(0).getTime()) {//ts.getTime()) { //new Timestamp(System.currentTimeMillis()).getTime()) {
						if (!couponDAO.isCouponTitleExists(CoupTitle)) {
							couponDAO.createCoupon(companyId, coupon);
						} else {
							throw new CouponException("STOP! Coupon Title is Already Exists! Create New Coupon is Canceled!"); 
						}
					} else {
						throw new CouponException("STOP! Coupon Start Date Cannot Be In The Past! Create New Coupon is Canceled!"); 
					}
				} else {
					throw new CouponException("STOP! Coupon Start Date Cannot Be Greater then End Date! Create New Coupon is Canceled!"); 
				}
			} else {
				throw new CouponException("STOP! No Coupons Information! Create New Coupon is Canceled!"); 
			}
		} else {
			throw new CouponException("STOP! Coupon Information Not Exist! Create New Coupon is Failed!"); 
		}
	}
	
	/**
	 * Delete all coupons from related to company id include those 
	 * how bought by customers (customerCoupon).
	 * @param coupId long 
	 * @throws CouponException
	 * 	Coupon must be related to the company.
	 */
	public void removeCoupon(long coupId) throws CouponException {
		if (coupId > 0) {
			if (couponDAO.isCouponExistsForCompany(companyId, coupId)) {
				couponDAO.removeCoupon(coupId);
			} else {
				throw new CouponException("STOP! Coupon Not Exist for Company! Remove Coupon is Canceled!");
			}
		} else {
			throw new CouponException("STOP! No Coupon Was Chosen! Remove Coupon is Canceled!");
		}
	}

	/**
	 * Update Coupon - only end date and price.
	 * @param coupon Coupon 
	 * @throws CouponException
	 *  Dates will be validated.
	 *  Start date must be greater than today
	 *  End date must be greater than Start date.
	 *  Coupon must be related to the company.
	 *  Price cannot be nothing.
	 */
	public void updateCoupon(Coupon coupon) throws CouponException {
		if (coupon != null) {
			long couponId = coupon.getId();
			if (couponDAO.isCouponExistsForCompany(companyId, couponId)) {
				Double CoupPrice = coupon.getPrice();
				if (CoupPrice > 0) {
					Timestamp startDate = couponDAO.getCoupon(couponId).getStartDate();
					Timestamp endDate = coupon.getEndDate();
					if (startDate.getTime() <= endDate.getTime()) {
						//if (startDate.getTime() >= new Timestamp(System.currentTimeMillis()).getTime()) {
							couponDAO.updateCoupon(companyId, coupon);
						//} else {
						//	throw new CouponException("STOP! Coupon Start Date Cannot Be In The Past! Update Coupon is Canceled!"); 
						//}
					} else {
						throw new CouponException("STOP! Coupon Start Date Cannot Be Greater then End Date! Update Coupon is Canceled!"); 
					}
				} else {
					throw new CouponException("STOP! Invalid Price For Coupon! Update Coupon is Canceled!"); 
				}
			} else {
				throw new CouponException("STOP! Coupon Not Exist for Company! Update Coupon is Canceled!");
			}
		} else {
			throw new CouponException("STOP! Coupon Information Not Exist! Update Coupon is Failed!"); 
		}
	}
	
	/**
	 * View specific company by id
	 * @return Company
	 * @throws CouponException
	 */
	public Company getCompany() throws CouponException {
		return companyDAO.getCompany(companyId);
	}
	
	/**
	 * View specific coupon
	 * @param coupId long 
	 * @return Coupon
	 * @throws CouponException
	 */
	public Coupon getCoupon(long coupId) throws CouponException {
		return couponDAO.getCoupon(coupId);
	}	
	
	/**
	 * Get all company coupons.
	 * @return Coupons collection.
	 * @throws CouponException
	 * false - not expired coupons.
	 */
	public Set<Coupon> getCoupons() throws CouponException {
		return couponDAO.getCoupons(companyId,0,0,false);
	}

	/**
	 * View all coupons related to specific company by type.
	 * @param coupType CouponType 
	 * @return Coupons collection.
	 * @throws CouponException
	 */
	public Set<Coupon> getCouponsByType(CouponType coupType) throws CouponException {
		return couponDAO.getCouponsByType(companyId, coupType);
	}

	/**
	 * View all coupons related to specific company By Max Coupon Price
	 * @param price double 
	 * @return Coupons collection.
	 * @throws CouponException
	 */
	public Set<Coupon> getCouponsByMaxCouponPrice(double price) throws CouponException {
		return couponDAO.getCouponsByMaxCouponPrice(companyId,price);
	}

	/**
	 * View all coupons related to specific company by Max Coupon Date
	 * @param maxCouponDate Timestamp 
	 * @return Coupons collection.
	 * @throws CouponException
	 */
	public Set<Coupon> getCouponsByMaxCouponDate(Timestamp maxCouponDate)  throws CouponException{
		return couponDAO.getCouponsByMaxCouponDate(companyId, maxCouponDate);		
	}
	
	/**
	 * @return the companyId
	 * Set in login()
	 */
	public long getCompanyId() {
		return companyId;
	}
	
	/**
	 * @return company
	 * Set in login()
	 */
	public Company getCompanyInstance() {
		return company;
	}
}
