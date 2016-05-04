/**
 * 
 */
package beans.DAO;
import java.util.Set;
import beans.Customer;
import main.CouponException;

/**
 * @author Ofer Moscovich
 * Abstract methods for Customer entity 
 */
public interface CustomerDAO {
	
	public void createCustomer(Customer customer) throws CouponException;
	public void removeCustomer(long custId) throws CouponException ;
	public void updateCustomer(Customer customer) throws CouponException ;
	public Customer getCustomer(long custId) throws CouponException ;
	public Set<Customer> getAllCustomers() throws CouponException ;
	public Customer login(String custName,String password) throws CouponException ;
	public boolean isCustomerNameExists(String custName) throws CouponException;

}
