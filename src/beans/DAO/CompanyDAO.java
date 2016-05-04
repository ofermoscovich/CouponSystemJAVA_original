package beans.DAO;

import java.util.Set;
import beans.Company;
import main.CouponException;

/**
 * @author Ofer Moscovich
 * Abstract methods for Company entity
 */
public interface CompanyDAO {
	
	public void createCompany(Company company) throws CouponException;
	public void removeCompany(long compId) throws CouponException;
	public void updateCompany(Company company) throws CouponException;
	public Company getCompany(long compId) throws CouponException;
	public Set<Company> getAllCompanies() throws CouponException;
	public Company login(String compName,String password) throws CouponException;
	public boolean isCompanyNameExists(String compName) throws CouponException;

}
