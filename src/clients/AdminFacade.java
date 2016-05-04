package clients;

import java.util.Set;

import beans.Company;
import beans.Coupon;
import beans.Customer;
import beans.DAO.*;
import beans.DBDAO.*;
import main.CouponException;

/**
 * @author Ofer Moscovich
 * Administrator Facade
 */
public class AdminFacade extends Clients implements CouponClientFacade { 

	private static final String ADMINUSERNAME = "admin";
	private static final String ADMINPASSWORD = "1234";
	private CompanyDAO companyDAO;
	private CustomerDAO customerDAO;
	private CouponDAO couponDAO;

	
	/**
	 * Constructor
	 * @throws CouponException
	 */
	public AdminFacade() throws CouponException {
		this.companyDAO = new CompanyDBDAO();
		this.customerDAO = new CustomerDBDAO();
		this.couponDAO = new CouponDBDAO();
	}
	
	/**
	 * First Administrator Login check
	 * @param name String - company name
	 * @param password String 
	 * @return CouponClientFacade - the specific admin facade (this).
	 * @throws CouponException
	 */
	@Override
	public CouponClientFacade login(String name, String password) throws CouponException {
		if (name.equals(ADMINUSERNAME) && password.equals(ADMINPASSWORD)) {
			return this;
		} else {
			return null;
		}
	}

	/**
	 * Add Company with unique name.
	 * @param company Company 
	 * @throws CouponException
	 * Company must include unique name, filled password.
	 */
	public void createCompany(Company company) throws CouponException {
		if (company != null) {
			String compName = company.getCompName();
			if (compName != null) {
				if (company.getPassword() != null) {
					if (!companyDAO.isCompanyNameExists(compName)) {
						companyDAO.createCompany(company);
					} else {
						throw new CouponException("STOP! Company Name Already Exists! Create New Company Canceled!"); 
					}
				} else {
					throw new CouponException("STOP! Password Requiered! Create New Company Canceled!"); 
				}
			} else {
				throw new CouponException("STOP! Company Name Requiered! Create New Company Canceled!"); 
			}
		} else {
			throw new CouponException("STOP! Company Information Not Exist! Create New Company Failed!"); 
		}	
	}
	
	/**
	 * Delete Company with all related coupons (CompanyCoupon and CustomerCoupon tables).
	 * @param compId long - companyId
	 * @throws CouponException
	 */
	public void removeCompany(long compId) throws CouponException {
		if (compId > 0) {
			companyDAO.removeCompany(compId);
		} else {
			throw new CouponException("STOP! No Company was Chosen! Remove Company is Canceled!"); 
		}
	}
	
	/**
	 * Update Company (without company name)
	 * @param company Company 
	 * @throws CouponException
	 * Company must include same unique name and filled password.
	 * Include double check to eliminate updating the company name, since 
	 *  the Company object does not pass the name anyway and the sql Update
	 *  query does not include the company name.
	 */
	public void updateCompany(Company company) throws CouponException {
		if (company != null) {
			String compName = company.getCompName();
			if (compName != null || compName != companyDAO.getCompany(company.getId()).getCompName()) {
				if (company.getPassword() != null) {
						companyDAO.updateCompany(company);
				} else {
					throw new CouponException("STOP! Password Requiered! Update Company Canceled!"); 
				}
			} else {
				throw new CouponException("STOP! Company Name Cannot be Changed or Empty! Update Company Canceled!"); 
			}
		} else {
			throw new CouponException("STOP! Company Information Not Exists! Update Company Canceled!"); 
		}	
	}
	
	/**
	 * View specific company by id // show specific company detail 
	 * @param compId long - company Id
	 * @return Company company
	 * @throws CouponException
	 */
	public Company getCompany(long compId) throws CouponException{
		if (compId > 0) {
			return companyDAO.getCompany(compId);
		} else {
			throw new CouponException("STOP! No Company was Chosen! Getting Company Failed!"); 
		}
	}

	/**
	 * View all companies
	 * @return Company collection 
	 * @throws CouponException
	 */
	public Set<Company> getAllCompanies() throws CouponException {
		return companyDAO.getAllCompanies();
	}
		
	/**
	 * Add new customer with unique name and filled password.
	 * @param customer Customer 
	 * @throws CouponException
	 * Customer Name and password are required.
	 * Customer name must be unique.
	 */
	public void createCustomer(Customer customer) throws CouponException {
		if (customer != null) {
			String custName = customer.getCustName();
			if (custName != null) {
				if (customer.getPassword() != null) {
					if (!customerDAO.isCustomerNameExists(custName)) {
						customerDAO.createCustomer(customer);
					} else {
						throw new CouponException("STOP! Customer Already Exists! Create New Customer Canceled!"); 
					}
				} else {
					throw new CouponException("STOP! Password Requiered! Create New Customer Canceled!"); 
				}
			} else {
				throw new CouponException("STOP! Customer Name Requiered! Create New Customer Canceled!"); 
			}
		} else {
			throw new CouponException("STOP! Customer Information Not Exists! Create New Customer Canceled!"); 
		}	
	}
	
	/**
	 * Delete customer with all his current and history coupons
	 * @param custId long 
	 * @throws CouponException
	 */
	public void removeCustomer(long custId) throws CouponException {
		if (custId > 0) {
			customerDAO.removeCustomer(custId);
		} else {
			throw new CouponException("STOP! No Customer was Chosen! Remove Customer Canceled!"); 
		}
	}
	
	/**
	 * Update customer without the name.
	 * @param customer Customer 
	 * @throws CouponException
	 * Customer Name and password are required.
	 * Customer name must be unique.
	 */
	public void updateCustomer(Customer customer) throws CouponException {
		if (customer != null) {
			String custName = customer.getCustName();
			if (custName != null || custName != customerDAO.getCustomer(customer.getId()).getCustName()) {
				if (customer.getPassword() != null) {
					customerDAO.updateCustomer(customer);
				} else {
					throw new CouponException("STOP! Password Requiered! Update Customer Canceled!"); 
				}
			} else {
				throw new CouponException("STOP! Customer Name Cannot be Changed or Empty! Update Customer Canceled!"); 
			}
		} else {
			throw new CouponException("STOP! Customer Information Not Exists! Update Customer Canceled!"); 
		}	
	}
	
	/**
	 * View list of all customers
	 * @return customer collection
	 * @throws CouponException
	 */
	public Set<Customer> getAllCustomers() throws CouponException {
		return customerDAO.getAllCustomers();
	}

	/**
	 * View specific customer detail
	 * @param custId long 
	 * @return Customer 
	 * @throws CouponException
	 */
	public Customer getCustomer(long custId) throws CouponException {
		if (custId > 0) {
			return customerDAO.getCustomer(custId);
		} else {
			throw new CouponException("STOP! No Customer was Chosen! View Customer Canceled!"); 
		}
		
	}
	
	/**
	 * View one coupon
	 * @param coupId long 
	 * @return Coupon
	 * @throws CouponException
	 */
	public Coupon getCoupon(long coupId) throws CouponException {
		return couponDAO.getCoupon(coupId);
	}
	
	/**
	 * View all valid coupons in coupon table
	 * @return Coupon collection
	 * @throws CouponException
	 * false = not expired
	 * This Coupon list important to administrator for support.
	 */
	public Set<Coupon> getCoupons() throws CouponException {
		return couponDAO.getCoupons(0,0,0,false);
	}
	
	/**
	 * View All Coupons in the system.
	 * @return Coupon collection
	 * @throws CouponException
	 * True = also expired
	 * This Coupon list important to administrator for support. 
	 * For Administrator or support. (Not In Use Yet)
	 * If company Id, customerId and couponId are all 0 than all companies will be pooled.
	 * true - Include expired coupons - all coupons in Coupon table (for administrator only) 
	 */
	public Set<Coupon> getAllCoupons() throws CouponException {
		return couponDAO.getCoupons(0, 0, 0, true);
	}
}
