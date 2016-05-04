package beans.DBDAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import beans.Coupon;
import beans.CouponType;
import beans.DAO.CouponDAO;
import main.ConnectionPool;
import main.CouponException;

/**
 * @author Ofer Moscovich
 * DBDAO: All SQL's related to Coupon entity. 
 */
public class CouponDBDAO implements CouponDAO {

	private ConnectionPool pool;
	
	/**
	 * Common Coupon SQL Select Query 
	 */

	private static final String sqlSELECT_coupon = "SELECT coup.id, title, start_date, end_date, amount, type, message, price, image ";
	private static final String sqlSELECT_company = ", comp.company_name, comp.email ";
	private static final String sqlSELECT_company_empty = ", '' company_name, '' email ";
	private static final String sqlSELECT_customer = ", cust.customer_name ";
	private static final String sqlSELECT_customer_empty = ", '' customer_name ";
	private static final String sqlFROM_coupon  = "FROM app.Coupon coup ";
	private static final String sqlFROM_companyCoupon  = "INNER JOIN app.CompanyCoupon compcoup ON coup.id = compcoup.coupon_id ";
	private static final String sqlFROM_customerCoupon  = "INNER JOIN app.CustomerCoupon custcoup ON coup.id = custcoup.coupon_id ";
	private static final String sqlFROM_company  = "INNER JOIN app.Company comp ON compcoup.company_id = comp.id ";
	
	/**
	 * CTCR
	 * @throws CouponException
	 * No Arguments.
	 * Getting Access to Connection Pool.
	 */
	public CouponDBDAO() throws CouponException {
		pool = ConnectionPool.getInstance();
	}

	/**
	 * Create New Coupon.
	 * Company can add new coupon with unique name.
	 * DB will generate new coupon Id.
	 * throws CouponException.
	 */
	@Override
	public void createCoupon(long compId, Coupon coupon) throws CouponException {
		Connection connection = pool.getConnection();
		try {
			connection.setAutoCommit(false);
			String sql = "INSERT INTO app.Coupon (title, start_date, end_date, amount, type, message, price, image) VALUES (?,?,?,?,?,?,?,?)";
			PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			pstmt.setString(1, coupon.getTitle());
			pstmt.setDate(2, new Date(coupon.getStartDate().getTime()));
			pstmt.setDate(3, new Date(coupon.getEndDate().getTime()));
			pstmt.setLong(4, coupon.getAmount());
			pstmt.setString(5, coupon.getType().toString());
			pstmt.setString(6, coupon.getMessage());
			pstmt.setDouble(7, coupon.getPrice());
			pstmt.setString(8, coupon.getImage());

			pstmt.executeUpdate();
			
			ResultSet rs = pstmt.getGeneratedKeys();
			rs.next();
			
			long key = rs.getLong(1);
			coupon.setId(key);
			
			sql = "INSERT INTO app.CompanyCoupon (company_id, coupon_id) VALUES (?,?)";
			pstmt = connection.prepareStatement(sql);

			pstmt.setLong(1, compId);
			pstmt.setLong(2, key);
		
			pstmt.executeUpdate();
			
			connection.commit();
			
		} catch (SQLException e) {
			throw new CouponException("DB ERROR! Create New Coupon Failed.");
		} catch (Exception e) {
			throw new CouponException("APP ERROR! Create New Coupon Failed.");
		} finally {
			pool.returnConnection(connection);
		}
	}

	/**
	 * Remove Coupon.
	 * Company delete all related coupons, include bought by customers 
	 * Will remove Coupons from CustomerCoupon, CustomerCoupon tables. 
	 * throws CouponException.
	 */
	@Override
	public void removeCoupon(long coupId) throws CouponException {
		Connection connection = pool.getConnection();
		try {
			connection.setAutoCommit(false);
			String sql = "DELETE FROM app.CompanyCoupon WHERE coupon_id = ?";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, coupId);
			pstmt.executeUpdate();
			
			sql = "DELETE FROM app.CustomerCoupon WHERE coupon_id = ?";
			pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, coupId);
			pstmt.executeUpdate();
			
			sql = "DELETE FROM app.Coupon WHERE id = ?";
			pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, coupId);
			pstmt.executeUpdate();
			
			connection.commit();
			
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new CouponException("DB ERROR! Remove Coupon Failed. RollBack Transaction Failed!");
			}
			throw new CouponException("DB ERROR! Remove Coupon Failed.");
		} catch (Exception e) {
			throw new CouponException("APP ERROR! Remove Coupon Failed.");
		} finally {
			pool.returnConnection(connection);
		}
	}
	
	/**
	 * Remove Expired Coupons.
	 *  Daily delete all expired coupons (by expiration end_date) include
	 *   bought by customers (Coupon, CustomerCoupon, CustomerCoupon tables).
	 *  1. First will pool the expired coupons
	 *  2. Will delete them from all tables using removeCoupon(coupID) method.
 	 *  throws CouponException.
	 */
	@Override
	public void removeExpiredCoupons() throws CouponException {
		Connection connection = pool.getConnection();
		try {
			String sql = "SELECT id FROM app.Coupon WHERE end_date < CURRENT_DATE ";
			Statement pstmt = connection.createStatement();
			ResultSet rs = pstmt.executeQuery(sql); 
			while (rs.next()) {
				this.removeCoupon(rs.getLong("id"));			
			} 
		} catch (SQLException e) {
			throw new CouponException("DB ERROR! Remove Expired Coupon Failed.");
		} catch (Exception e) {
			throw new CouponException("APP ERROR! Remove Expired Coupon Failed.");
		} finally {
			pool.returnConnection(connection);
		}
	}

	/**
	 * Update Coupon: only end_date and price
	 * throws CouponException.
	 */
	@Override
	public void updateCoupon(long compId, Coupon coupon) throws CouponException {
		Connection connection = pool.getConnection();
		try {
			String sql = "UPDATE app.Coupon SET end_date = ?, price = ? WHERE id = ?";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setDate(1, new Date(coupon.getEndDate().getTime()));
			pstmt.setDouble(2, coupon.getPrice());
			pstmt.setLong(3, coupon.getId()); 

			pstmt.executeUpdate();
	
		} catch (SQLException e) {
			throw new CouponException("DB ERROR! Update Coupon Failed.");
		} catch (Exception e) {
			throw new CouponException("APP ERROR! Update Coupon Failed.");
		} finally {
			pool.returnConnection(connection);
		}
	}

	/**
	 * Get One Coupon (by coupon ID)
	 * return Coupon collection.
	 * throws CouponException.
	 */
	@Override
	public Coupon getCoupon(long coupId) throws CouponException {
		try {
			Set<Coupon> coupons = new HashSet<Coupon>();
			coupons = getCoupons(0, 0, coupId, false);
			Iterator<Coupon> it = coupons.iterator();
			while(it.hasNext()) { 
				return it.next();
			}
			
			Coupon coupon = new Coupon();
			return coupon;
		} catch (Exception e) {
			throw new CouponException("APP ERROR! Getting Coupon Failed.");
		} 
	}
	
	/**
	 * Get Coupons
	 * If coupon ID exists (not 0) than will get specific Coupon.
	 * If company ID exists (not 0) than will get all Coupons for specific Company. Called from CompanyFacade.
	 * If customer ID exists (not 0) than will get all Coupons for specific Customer. (Not Called Yet)
	 * If all 3 not exists the will get all Coupons (for administrator or support).
	 * @param compId
	 * @param custId
	 * @param coupId
	 * @param isExpiered - True  = view not expired coupons.
	 * 					   False = view expired coupons.
	 * @return Coupon collection.
	 * @throws CouponException
	 */
	@Override
	public Set<Coupon> getCoupons(long compId, long custId, long coupId, boolean isExpiered) throws CouponException {
		long arg = 0;
		Set<Coupon> couponList = new HashSet<Coupon>();
		Coupon coupon;
		Connection connection = pool.getConnection();
		ResultSet rs = null;
		try {
			// General SQL query build
			String sqlFROM  = sqlFROM_coupon + sqlFROM_companyCoupon + sqlFROM_company;
			String sql = sqlSELECT_coupon + sqlSELECT_company + sqlSELECT_customer_empty + sqlFROM;
			String sqlWHERE = "";
			String sqlAND = "";
			if (!isExpiered) { 
				sqlAND = " AND ";
				sqlWHERE = " end_date >= CURRENT_DATE AND amount > 0 ";
			}
			// One specific Coupon
			if (coupId > 0) {
				sql += " WHERE coup.id = ? " + sqlAND + sqlWHERE;
				arg = coupId;
			// Company Coupons	
			} else if (compId > 0 && custId == 0){
				sql += " WHERE compcoup.company_id = ? " + sqlAND + sqlWHERE;
				arg = compId;
			// Customer Coupons		
			} else if (custId > 0 && compId == 0){
				sql = sqlSELECT_coupon + sqlSELECT_company + sqlSELECT_customer + sqlFROM + sqlFROM_customerCoupon;
				sql += " WHERE custcoup.customer_id = ?" + sqlAND + sqlWHERE;
				arg = custId;
			// All Coupons (for administrator or support)			
			} else if (coupId == 0 && custId == 0 && compId == 0){
				Statement stmt = connection.createStatement();
				//sql += " WHERE " + sqlWHERE;
				rs = stmt.executeQuery(sql);
			// No option - not a valid option
			} else {
				return null;
			}

			if (coupId > 0 || custId > 0 || compId > 0) {
				PreparedStatement stmt = connection.prepareStatement(sql);
				stmt.setLong(1, arg);
				rs = stmt.executeQuery();
			}
			while (rs.next()) {
				coupon = new Coupon();
				toCoupon(coupon, rs, connection);
				couponList.add(coupon);				
			} 
		return couponList;
			
		} catch (SQLException e) {
			throw new CouponException("DB ERROR! Getting Coupon/s Failed.");
		} catch (Exception e) {
			throw new CouponException("APP ERROR! Getting Coupon/s Failed.");
		} finally {
			pool.returnConnection(connection);
		}
	}	
	

	/**********************
	 * Company Activities *
	 **********************/

	/**
	 * Get Coupons By Coupon Type.
	 * return Coupon collection.
	 * throws CouponException.
	 **/
	@Override
	public Set<Coupon> getCouponsByType(long compId,CouponType coupType) throws CouponException {
		Set<Coupon> couponList = new HashSet<Coupon>();
		Coupon coupon;
		Connection connection = pool.getConnection();
		String sql = null;
		try {
			// Get All Company Coupons by type (for Company)
			if (compId > 0) {
				sql = sqlSELECT_coupon + 
					  sqlSELECT_company_empty + 
					  sqlSELECT_customer_empty + 
					  sqlFROM_coupon + 
					  sqlFROM_companyCoupon + 
					  "WHERE type = ? AND compcoup.company_id = ? ";
			// Get All Available Coupons to buy, by type (for Customer) - not expired, no 0 amount for coupons left
			} else {
				sql = sqlSELECT_coupon + 
					  sqlSELECT_company + 
					  sqlSELECT_customer_empty + 
					  sqlFROM_coupon + 
					  sqlFROM_companyCoupon +
					  sqlFROM_company +
					  "WHERE end_date >= CURRENT_DATE AND amount > 0 AND type = ? ";
			}
			
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, coupType.toString());
			if (compId > 0) pstmt.setLong(2, compId); // for specific Company
			
			ResultSet rs = pstmt.executeQuery(); 
			while (rs.next()) {
				coupon = new Coupon();
				toCoupon(coupon, rs, connection);
				couponList.add(coupon);				
			} 
			return couponList;
				
		} catch (SQLException e) {
			throw new CouponException("DB ERROR! Getting Coupons by Type Failed.");
		} catch (Exception e) {
			throw new CouponException("APP ERROR! Getting Coupons by Type Failed.");
		} finally {
			pool.returnConnection(connection);
		}
	}

	/**
	 * Get Coupons By Max Coupon Price.
	 * return Coupon collection.
	 * throws CouponException.
	 */
	@Override
	public Set<Coupon> getCouponsByMaxCouponPrice(long compId, double price) throws CouponException {
		Set<Coupon> couponList = new HashSet<Coupon>();
		Coupon coupon;
		Connection connection = pool.getConnection();
		try {
			String sql = sqlSELECT_coupon + 
						 sqlSELECT_company + 
						 sqlSELECT_customer_empty +
						 sqlFROM_coupon + 
						 sqlFROM_companyCoupon +  
						 sqlFROM_company +
						 "WHERE comp.id = ? AND coup.price <= ?";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, compId);
			pstmt.setDouble(2, price);
			ResultSet rs = pstmt.executeQuery(); 
			while (rs.next()) {
				coupon = new Coupon();
				toCoupon(coupon, rs, connection);
				couponList.add(coupon);				
			} 
			return couponList;
				
		} catch (SQLException e) {
			throw new CouponException("DB ERROR! Getting Coupons by Max Coupon Price is Failed.");
		} catch (Exception e) {
			throw new CouponException("APP ERROR! Getting Coupons by Max Coupon Price is Failed.");
		} finally {
			pool.returnConnection(connection);
		}
	}

	/**
	 * Get Coupons By Max Coupon Date.
	 * return Coupon collection.
	 * throws CouponException.
	 */
	@Override
	public Set<Coupon> getCouponsByMaxCouponDate(long compId, Timestamp maxCouponDate) throws CouponException {
		Set<Coupon> couponList = new HashSet<Coupon>();
		Coupon coupon;
		Connection connection = pool.getConnection();
		try {
			String sql = sqlSELECT_coupon + 
						 sqlSELECT_company + 
						 sqlSELECT_customer_empty +
						 sqlFROM_coupon + 
						 sqlFROM_companyCoupon +  
						 sqlFROM_company + 
						 "WHERE comp.id = ? AND end_date < ? ";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, compId);
			pstmt.setDate(2, new Date(maxCouponDate.getTime()));
			ResultSet rs = pstmt.executeQuery(); 
			while (rs.next()) {
				coupon = new Coupon();
				toCoupon(coupon, rs, connection);
				couponList.add(coupon);				
			} 
			return couponList;
				
		} catch (SQLException e) {
			throw new CouponException("DB ERROR! Getting Coupons by Max Date is Failed.");
		} catch (Exception e) {
			throw new CouponException("APP ERROR! Getting Coupons by Max Date is Failed.");
		} finally {
			pool.returnConnection(connection);
		}
	}
	
	/************************
	 *  Customer Activities *
	 ************************/
	
	/**
	 * Purchase Coupon. (by Customer)
	 * Will insert new entry to CustomerCoupon table.
	 * Will decrease 1 from amount of coupon stock in Coupon table.
	 * throws CouponException.
	 */
	@Override
	public void purchaseCoupon(long custId, long coupId) throws CouponException {
		Connection connection = pool.getConnection();

		try {
			connection.setAutoCommit(false);
			String sql = "INSERT INTO app.CustomerCoupon (customer_id,coupon_id) VALUES (?,?)";
			PreparedStatement pstmt = connection.prepareStatement(sql);

			pstmt.setLong(1, custId);
			pstmt.setLong(2, coupId);

			pstmt.executeUpdate();
			
			sql = "UPDATE app.Coupon SET amount = amount - 1 WHERE id = ?";
			pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, coupId);
		
			pstmt.executeUpdate();
			
			connection.commit();
			
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new CouponException("DB ERROR! Purchase Coupon is Failed. RollBack Failed!");
			}
			throw new CouponException("DB ERROR! Purchase Coupon is Failed.");
		} catch (Exception e) {
			throw new CouponException("APP ERROR! Purchase Coupon is Failed.");
		} finally {
			pool.returnConnection(connection);
		}
	}

	/**
	 * Get Customer Coupons By Coupon Type.
	 * For Customer use.
	 * return Coupon collection.
	 * throws CouponException.
	 **/	
	@Override
	public Set<Coupon> getCouponsByType(CouponType coupType) throws CouponException {
		return getCouponsByType(0, coupType);
	}

	/**
	 * Get All Purchased Coupons (for administrator)
	 *  or by customer 
	 *  or by type 
	 *  or by max price.
	 *
	 * (for Customer use).
	 * return Coupon collection.
	 * throws CouponException.
	 **/		
	@Override
	public Set<Coupon> getAllPurchasedCoupons(long custId, CouponType type, double price) throws CouponException {
		Set<Coupon> couponList = new HashSet<Coupon>();
		Coupon coupon;
		Connection connection = pool.getConnection();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String sqlWHERE  = "WHERE custcoup.customer_id = ? ";
		try {
			String sql = sqlSELECT_coupon + 
						 sqlSELECT_company +
						 sqlSELECT_customer_empty + 
						 sqlFROM_coupon +
						 sqlFROM_companyCoupon +
						 sqlFROM_customerCoupon +
						 sqlFROM_company +
						 sqlWHERE;
			// All customer Purchased Coupons history include not expired (for customer)
			if (custId > 0 && type == null && price == 0.0) {
				pstmt = connection.prepareStatement(sql);
				pstmt.setLong(1, custId);
			// All customer Purchased Coupons history, by type, include not expired(for customer)		
			} else if (custId > 0 && type != null && price == 0.0){
				sql += "AND type = ? ";
				pstmt = connection.prepareStatement(sql);
				pstmt.setLong(1, custId);
				pstmt.setString(2, type.toString());
			// All customer Purchased Coupons history, by price, include not expired(for customer)		
			} else if (custId > 0 && type == null && price > 0.0){
				sql += "AND price <= ? ";
				pstmt = connection.prepareStatement(sql);
				pstmt.setLong(1, custId);
				pstmt.setDouble(2, price);
			// All Purchased Coupons history include not expired(for administrator)	
			} else if (custId == 0 && type == null && price == 0.0){
				sql = sqlSELECT_coupon + 
					  sqlSELECT_company +
					  sqlSELECT_customer + 
					  sqlFROM_coupon +
					  sqlFROM_companyCoupon +
					  sqlFROM_customerCoupon +
					  sqlFROM_company;
				Statement cstmt = connection.createStatement();
				rs = cstmt.executeQuery(sql);
				// No option - not a valid option
			} else {
				return null;
			}

			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				coupon = new Coupon();
				toCoupon(coupon, rs, connection);
				couponList.add(coupon);				
			} 
		return couponList;
			
		} catch (SQLException e) {
			throw new CouponException("DB ERROR! Getting All Purchased Coupons is Failed.");
		} catch (Exception e) {
			throw new CouponException("APP ERROR! Getting All Purchased Coupons is Failed.");
		} finally {
			pool.returnConnection(connection);
		}
	}

	/**
	 * Get All Purchased Coupons for specific customer. 
	 *
	 * (for Customer use).
	 * return Coupon collection.
	 * throws CouponException.
	 **/	
	@Override
	public Set<Coupon> getAllPurchasedCoupons(long custId) throws CouponException {
		return getAllPurchasedCoupons(custId, null, 0.0);		
	}
	
	/**
	 * Get All Purchased Coupons (for administrator). (not in use yet) 
	 * (for Customer use).
	 * return Coupon collection.
	 * throws CouponException.
	 **/
	@Override
	public Set<Coupon> getAllPurchasedCoupons() throws CouponException {
		return getAllPurchasedCoupons(0, null, 0.0);		
	}

	/**
	 * Get All Purchased Coupons for specific customer and coupon type. 
	 * (for Customer use).
	 * return Coupon collection.
	 * throws CouponException.
	 **/	
	@Override
	public Set<Coupon> getAllPurchasedCouponsByType(long custId, CouponType type) throws CouponException {
		return getAllPurchasedCoupons(custId, type, 0);	
	}

	/**
	 * Get All Purchased Coupons for specific customer and price. 
	 * (for Customer use).
	 * return Coupon collection.
	 * throws CouponException.
	 **/	
	@Override
	public Set<Coupon> getAllPurchasedCouponsByPrice(long custId, double price) throws CouponException {
		return getAllPurchasedCoupons(custId, null, price);	
	}

	/**
	 * Check if specific Coupon was purchased By same customer.
	 * Customer cannot buy same Coupon twice. 
	 * (for Customer use).
	 * return boolean.
	 * 		true - if Coupon purchased by Customer.
	 * 		false - if Coupon was not purchased by Customer.
	 * throws CouponException. 
	 **/	
	@Override
	public boolean isCouponPurchasedByCustomer(long custId, long coup_id) throws CouponException {
		Connection connection = pool.getConnection();
		try {
			String sql = "SELECT coupon_id FROM CustomerCoupon WHERE customer_id = ? AND coupon_id = ? "; 
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, custId);
			pstmt.setLong(2, coup_id);
			ResultSet rs = pstmt.executeQuery(); 
			if (rs.next()) {
				return true;
			} 
			return false;
				
		} catch (SQLException e) {
			throw new CouponException("DB ERROR! Checking If Coupon Already Exists For Company is Failed.");
		} catch (Exception e) {
			throw new CouponException("APP ERROR! Checking If Coupon Already Exists For Company is Failed.");
		} finally {
			pool.returnConnection(connection);
		}
	}

	/**
	 * Check if specific Coupon related to specific company.
	 * Company cannot update or delete non exist Coupon or other Companies Coupons. 
	 *
	 * (for Company use).
	 * @return boolean.
	 * 		true - Coupon exists for specific Company.
	 * 		false - Coupon does not exist for specific Company.
	 * throws CouponException.
	 */
	@Override
	public boolean isCouponExistsForCompany(long compId, long coupId) throws CouponException {
		Connection connection = pool.getConnection();
		try {
			String sql = "SELECT coupon_id FROM CompanyCoupon WHERE company_id = ? AND coupon_id = ? "; 
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, compId);
			pstmt.setLong(2, coupId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return true;
			} 
			return false;
				
		} catch (SQLException e) {
			throw new CouponException("DB ERROR! Checking if Coupon Exists For The Company is Failed.");
		} catch (Exception e) {
			throw new CouponException("APP ERROR! Checking if Coupon Exists For The Company is Failed.");
		} finally {
			pool.returnConnection(connection);
		}
	}

	
	/**
	 * Check if specific Coupon related to specific company.
	 * Company cannot update or delete non exist Coupon or other Companies Coupons. 
	 *
	 * (for Company use).
	 * @return boolean.
	 * 		true - Coupon title exists.
	 * 		false - Coupon title does not exist..
	 *
	 */
	@Override
	public boolean isCouponTitleExists(String coupTitle) throws CouponException {
		Connection connection = pool.getConnection();
		try {
			String sql = "SELECT id FROM Coupon WHERE title = ? "; 
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, coupTitle);
			ResultSet rs = pstmt.executeQuery(); 
			if (rs.next()) {
				return true;
			} 
			return false;
				
		} catch (SQLException e) {
			throw new CouponException("DB ERROR! Checking if Coupon Title Exists Failed.");
		} catch (Exception e) {
			throw new CouponException("APP ERROR! Checking if Coupon Title Exists Failed.");
		} finally {
			pool.returnConnection(connection);
		}
	}
	
	
	/**
	 * Coupon set with Result set.
	 * This method is a general Coupon set from result set
	 * 	include Company & Customer information such as
	 *  company_name & email, customer_name.  
	 *
	 * (for Customer use).
	 * @return Coupon
	 * @throws CouponException.
	 **/	
	private Coupon toCoupon(Coupon coupon, ResultSet rs, Connection connection) throws CouponException {
		try {
			coupon.setId(rs.getLong("id"));
			coupon.setTitle(rs.getString("title"));
			Date date = rs.getDate("start_date");
			coupon.setStartDate(new Timestamp(date.getTime()));
			date = rs.getDate("end_date");
			coupon.setEndDate(new Timestamp(date.getTime()));
			coupon.setAmount(rs.getLong("amount"));
			coupon.setType(CouponType.valueOf(rs.getString("type")));
			coupon.setMessage(rs.getString("message"));
			coupon.setPrice(rs.getDouble("price"));
			coupon.setImage(rs.getString("image"));
			coupon.setCompany_name(rs.getString("company_name"));
			coupon.setEmail(rs.getString("email"));
			coupon.setCustomer_name(rs.getString("customer_name"));

			return coupon;
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new CouponException("DB ERROR! Coupons Information Set Failed. RollBack Failed!");
			}
			throw new CouponException("DB ERROR! Coupons Information Set is Failed.");
		} catch (Exception e) {
			throw new CouponException("APP ERROR! Coupons Information Set is Failed.");
		} finally {
			pool.returnConnection(connection);
		}		
	}
}
