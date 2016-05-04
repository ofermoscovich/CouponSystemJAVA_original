package beans.DBDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import beans.Customer;
import beans.DAO.CustomerDAO;
import main.ConnectionPool;
import main.CouponException;

/**
 * @author Ofer Moscovich
 * DBDAO: All SQL's related to Customer entity. 
 **/
public class CustomerDBDAO implements CustomerDAO {

	private ConnectionPool pool;

	/**
	 * Constructor
	 * @throws CouponException 
	 * Getting Access to Connection Pool.
	 */
	public CustomerDBDAO() throws CouponException{
		pool = ConnectionPool.getInstance();
	}

	/**
	 * Create New Customer.
	 * DB will generate new Id.
	 */
	@Override
	public void createCustomer(Customer customer) throws CouponException {
		Connection connection = pool.getConnection();
		try {
			String sql = "INSERT INTO app.Customer (customer_name, password) VALUES (?,?)";
			PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			pstmt.setString(1, customer.getCustName());
			pstmt.setString(2, customer.getPassword());
	        
			pstmt.executeUpdate();
			
			ResultSet rs = pstmt.getGeneratedKeys();
			rs.next();
			
			long key = rs.getLong(1);
			customer.setId(key);
			
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new CouponException("DB ERROR! Duplicated Customer Name: Create new Customer Failed!");
		} catch (SQLException e) {
			throw new CouponException("DB ERROR! Create new Customer Failed.");
		} catch (Exception e) {
			throw new CouponException("APP ERROR! Create new Customer Failed.");
		} finally {
			pool.returnConnection(connection);
		}
	}

	/**
	 * Remove Customer.
	 * Will remove customers from Customer and CustomerCoupon tables.
	 */
	@Override
	public void removeCustomer(long custId) throws CouponException {
		Connection connection = pool.getConnection();
		try {
			connection.setAutoCommit(false);

			String sql = "DELETE FROM app.Customer WHERE id = ?";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, custId);
			pstmt.executeUpdate();
			
			sql = "DELETE FROM app.CustomerCoupon WHERE customer_id = ?";
			pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, custId);
			pstmt.executeUpdate();
			
			connection.commit();
			
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new CouponException("DB ERROR! Remove Customer Failed. RollBack Failed!");
			}
			throw new CouponException("DB ERROR! Remove Customer Failed.");
		} catch (Exception e) {
			throw new CouponException("APP ERROR! Remove Customer Failed.");
		} finally {
			pool.returnConnection(connection);
		}
	}

	/**
	 * Update Customer
	 * Only password will be update.
	 */
	@Override
	public void updateCustomer(Customer customer) throws CouponException {
		Connection connection = pool.getConnection();
		try {
			String sql = "UPDATE app.Customer SET password = ? WHERE id = ?";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, customer.getPassword());
			pstmt.setLong(2, customer.getId());
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			throw new CouponException("DB ERROR! Update Customer Failed.");
		} catch (Exception e) {
			throw new CouponException("APP ERROR! Update Customer Failed.");
		} finally {
			pool.returnConnection(connection);
		}
	}

	/* 
	 * Get Customer By customer ID.
	 * @return Customer collection.
	 */
	@Override
	public Customer getCustomer(long custId) throws CouponException {
		Customer customer = new Customer();
		Connection connection = pool.getConnection();
		try {
			String sql = "SELECT id, customer_name, password FROM app.Customer WHERE id = ?";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, custId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()){
				customer.setId(rs.getLong("id"));
				customer.setCustName(rs.getString("customer_name"));
				customer.setPassword(rs.getString("password"));
			} 
			return customer;
			
		} catch (SQLException e) {
			throw new CouponException("DB ERROR! Getting Customer Failed.");
		} catch (Exception e) {
			throw new CouponException("APP ERROR! Getting Customer Failed.");
		} finally {
			pool.returnConnection(connection);
		}
	}

	/**
	 * Get All Customers in customer table (for administrator usage).
	 * @return Customer collection.
	 */
	@Override
	public Set<Customer> getAllCustomers() throws CouponException {
		Set<Customer> customerList = new HashSet<>();
		Customer customer;
		Connection connection = pool.getConnection();
		String sql = "SELECT id, customer_name, password FROM app.Customer ";
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				customer = new Customer();
				customer.setId(rs.getLong("id"));
				customer.setCustName(rs.getString("customer_name"));
				customer.setPassword(rs.getString("password"));
				customerList.add(customer);				
			} 
			return customerList;
			
		} catch (SQLException e) {
			throw new CouponException("DB ERROR! Getting All Customers Failed.");
		} catch (Exception e) {
			throw new CouponException("APP ERROR! Getting All Customers Failed.");
		} finally {
			pool.returnConnection(connection);
		}
	}
	
	/**
	 * Check if Customer name exists in all Customer table.
	 * @return boolean - true - customer name exists.
	 * 					false - customer name not exist.
	 */
	@Override
	public boolean isCustomerNameExists(String custName) throws CouponException {
		Connection connection = pool.getConnection();
		try {
			String sql = "SELECT id FROM Customer WHERE customer_name = ? "; 
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, custName);
			ResultSet rs = pstmt.executeQuery(); 
			if (rs.next()) {
				return true;
			} 
			return false;
				
		} catch (SQLException e) {
			throw new CouponException("DB ERROR! Failed to checking if Customer name already exists.");
		} catch (Exception e) {
			throw new CouponException("APP ERROR! Failed to checking if Customer name already exists.");
		} finally {
			pool.returnConnection(connection);
		}
	}

	/**
	 * Login - Checking Customer
	 * @return boolean - true - known Customer
	 * 					false - unknown Customer
	 */
	@Override
	public Customer login(String custName, String password) throws CouponException {
		Connection connection = pool.getConnection();
		Customer customer = null;
		try {
			String sql = "SELECT id, customer_name, password FROM app.Customer WHERE customer_name = ? and password = ?";
			PreparedStatement pstmt = connection.prepareStatement(sql);
		
			pstmt.setString(1, custName);
			pstmt.setString(2, password);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				long customerId = rs.getLong("id");
				if (customerId > 0) { 
					customer = new Customer();
					customer.setId(customerId);
					customer.setCustName(custName);
					customer.setPassword(password);
					return customer;
				}
			}
			return null;
			
		} catch (SQLException e) {
			throw new CouponException("DB ERROR! Login Failed.");
		} catch (Exception e) {
			throw new CouponException("APP ERROR! Login Failed.");
		} finally {
			pool.returnConnection(connection);
		}
	}
}