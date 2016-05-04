package beans.DBDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import beans.Company;
import beans.DAO.CompanyDAO;
import main.ConnectionPool;
import main.CouponException;

/**
 * @author Ofer Moscovich
 * DBDAO: All SQL's related to Company entity. 
 */
public class CompanyDBDAO implements CompanyDAO {

	private ConnectionPool pool;
	
	/**
	 * Constructor
	 * @throws CouponException
	 * No Arguments.
	 * Getting Access to Connection Pool.
	 */
	public CompanyDBDAO() throws CouponException {
		pool = ConnectionPool.getInstance();
	}

	/**
	 * Create new Company.
	 * throws CouponException.
	 */
	@Override
	public void createCompany(Company company) throws CouponException {
		Connection connection = pool.getConnection();
		try {
			String sql = "INSERT INTO app.Company (company_name, password, email) VALUES (?,?,?)";
			PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			pstmt.setString(1, company.getCompName());
			pstmt.setString(2, company.getPassword());
			pstmt.setString(3, company.getEmail());
			
			pstmt.executeUpdate();
			
			ResultSet rs = pstmt.getGeneratedKeys();
			rs.next();
			
			long key = rs.getLong(1);
			company.setId(key);
			
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new CouponException("DB ERROR! Duplicated Company Name: Create Company Failed!");
		} catch (SQLException e) {
			throw new CouponException("DB ERROR! Create New Company Failed.");
		} catch (Exception e) {
			throw new CouponException("APP ERROR! Create New Company Failed.");
		} finally {
			pool.returnConnection(connection);
		}
	}

	/**
	 * Remove Company.
	 * throws CouponException.
	 */
	@Override
	public void removeCompany(long compId) throws CouponException {
		Connection connection = pool.getConnection();
		try {
			connection.setAutoCommit(false);

			String sql = "DELETE FROM app.CustomerCoupon custcoup WHERE custcoup.coupon_id IN (SELECT compcoup.coupon_id FROM app.CompanyCoupon compcoup WHERE compcoup.company_id = ?)";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, compId);
			pstmt.executeUpdate();
			
			sql = "DELETE FROM app.CompanyCoupon WHERE company_id = ?";
			pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, compId);
			pstmt.executeUpdate();
			
			sql = "DELETE FROM app.Coupon coup WHERE coup.id IN (SELECT compcoup.coupon_id FROM app.CompanyCoupon compcoup WHERE compcoup.company_id = ?)";
			pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, compId);
			pstmt.executeUpdate();
			
			sql = "DELETE FROM app.Company WHERE id = ?";
			pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, compId);
			pstmt.executeUpdate();
			
			connection.commit();
			
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new CouponException("DB ERROR! Remove Company Failed. RollBack Failed!");
			}
			throw new CouponException("DB ERROR! Remove Company Failed.");
		} catch (Exception e) {
			throw new CouponException("APP ERROR! Remove Company Failed.");
		} finally {
			pool.returnConnection(connection);
		}
	}

	/**
	 * Update Company.
	 * throws CouponException.
	 */
	@Override
	public void updateCompany(Company company) throws CouponException {
		Connection connection = pool.getConnection();
		try {
			String sql = "UPDATE app.Company SET password = ?, email = ? WHERE id = ?";

			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, company.getPassword());
			pstmt.setString(2, company.getEmail());
			pstmt.setLong(3, company.getId()); 

			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			throw new CouponException("DB ERROR! Update Company Failed.");
		} catch (Exception e) {
			throw new CouponException("APP ERROR! Update Company Failed.");
		} finally {
			pool.returnConnection(connection);
		}
	}

	/** 
	 * Get Company By Company ID
	 * return Company.
	 * throws CouponException.
	 */
	@Override
	public Company getCompany(long compId) throws CouponException {
		Company company = new Company();
		Connection connection = pool.getConnection();
		try {
			String sql = "SELECT id, company_name, password, email FROM app.Company WHERE id = ?";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, compId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()){
				company.setId(rs.getLong("id"));
				company.setCompName(rs.getString("company_name"));
				company.setPassword(rs.getString("password"));
				company.setEmail(rs.getString("email"));
			} 
			return company;
			
		} catch (SQLException e) {
			throw new CouponException("DB ERROR! Getting Company Failed.");
		} catch (Exception e) {
			throw new CouponException("APP ERROR! Getting Company Failed.");
		} finally {
			pool.returnConnection(connection);
		}
	}

	/** 
	 * Get All Companies List with order by company name.
	 * return Company collection.
	 * throws CouponException.
	 */
	@Override
	public Set<Company> getAllCompanies() throws CouponException {
		Set<Company> companyList = new HashSet<Company>();
		Company company;
		Connection connection = pool.getConnection();
		String sql = "SELECT id, company_name, password, email FROM app.Company ";

		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				company = new Company();
				company.setId(rs.getLong("id"));
				company.setCompName(rs.getString("company_name"));
				company.setPassword(rs.getString("password"));
				company.setEmail(rs.getString("email"));
				companyList.add(company);				
			} 
			return companyList;
			
		} catch (SQLException e) {
			throw new CouponException("DB ERROR! Getting All Companies Failed.");
		} catch (Exception e) {
			throw new CouponException("APP ERROR! Getting All Companies Failed.");
		} finally {
			pool.returnConnection(connection);
		}
	}

	/**
	 * Check if Company Name Exists (For Creating New Company)
	 * return boolean - true - Company name exists.
	 * 					false - Company name not  exists.
	 * throws CouponException.
	 */
	@Override
	public boolean isCompanyNameExists(String compName) throws CouponException {
		Connection connection = pool.getConnection();
		try {
			String sql = "SELECT id FROM Company WHERE company_name = ? "; 
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, compName);
			ResultSet rs = pstmt.executeQuery(); 
			if (rs.next()) {
				return true;
			} 
			return false;
				
		} catch (SQLException e) {
			throw new CouponException("DB ERROR! Failed to checking if Company name already exists.");
		} catch (Exception e) {
			throw new CouponException("APP ERROR! Failed to checking if Company name already exists.");
		} finally {
			pool.returnConnection(connection);
		}
	}
	
	/**
	 * Login - Checking Company
	 * return boolean - true - Company name exists.
	 * 					false - Company name not exists.
	 * throws CouponException.
	 */
	@Override
	public Company login(String compName, String password) throws CouponException {
		Connection connection = pool.getConnection();
		Company company = null;
		try {
			String sql = "SELECT id, company_name, password, email FROM app.Company WHERE company_name = ? AND password = ?";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, compName);
			pstmt.setString(2, password);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				long companyId = rs.getLong("id");
				if (companyId > 0) {
					company = new Company();
					company.setId(companyId);
					company.setCompName(compName);
					company.setPassword(password);
					company.setEmail(rs.getString("email"));
					return company;
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
