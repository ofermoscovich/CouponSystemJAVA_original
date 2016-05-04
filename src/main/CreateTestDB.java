package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Ofer Moscovich
 * Building New Database For Testing Only using the following
 */
public class CreateTestDB {
	
	public static void main(String[] args) {

		String sql;
		boolean first_try_success = true;//false;

		if (first_try_success == true) {
			/**
			 * Get driver & URL from file and Connect to DB
			 * 
			 * driver = "org.apache.derby.jdbc.ClientDriver"
			 * url = "jdbc:derby://localhost:1527/db_coupons"
			 * 
			 */
		
			try (Connection con = DriverManager.getConnection(GetFileParms.getDbUrl());
					Statement stmt = con.createStatement();					
					) {
				System.out.println("JDBC: Connected...");
				con.setAutoCommit(false);

				/***************
				 * DROP tables *
				 ***************/	
				
				sql = "DROP TABLE app.CustomerCoupon";
				stmt.executeUpdate(sql);
				System.out.println("JDBC: " + sql);
				
				sql = "DROP TABLE app.CompanyCoupon";
				stmt.executeUpdate(sql);
				System.out.println("JDBC: " + sql);
				
				sql = "DROP TABLE app.Coupon";
				stmt.executeUpdate(sql);
				System.out.println("JDBC: " + sql);
				
				sql = "DROP TABLE app.Company";
				stmt.executeUpdate(sql);
				System.out.println("JDBC: " + sql);
				
				sql = "DROP TABLE app.Customer";
				stmt.executeUpdate(sql);
				System.out.println("JDBC: " + sql);

				con.commit();
				
				first_try_success = true;
				
				con.setAutoCommit(false);

			//if (first_try_success = true) {
					/************************
					 * Create COMPANY table *
					 ************************/					

				sql = "CREATE TABLE app.Company (" + 
						"id INT GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1)," + 
						"company_name VARCHAR(20) UNIQUE NOT NULL," + 
						"password VARCHAR(20) NOT NULL," +  
						"email VARCHAR(40)," + 
						"PRIMARY KEY (id)" + 
						")";
				stmt.executeUpdate(sql);
				System.out.println("JDBC: " + sql);			
		
					/*************************
					 * Create CUSTOMER table *
					 *************************/	
				
				sql = "CREATE TABLE app.Customer (" + 
						"id INT GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +  
						"customer_name VARCHAR(20) UNIQUE NOT NULL," + 
						"password VARCHAR(20) NOT NULL," + 
						"PRIMARY KEY (id)" + 
						")";
				stmt.executeUpdate(sql);
				System.out.println("JDBC: " + sql);
				
					/***********************
					 * Create COUPON table *
					 ***********************/	
					
				sql = "CREATE TABLE app.Coupon (" + 
						"id INT GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," + 
						"title VARCHAR(20) UNIQUE NOT NULL," +  
						"start_date DATE," +  
						"end_date DATE," +  
						"amount INT," +  
						"type VARCHAR(20)," +  
						"message VARCHAR(20)," +  
						"price DOUBLE," +  
						"image VARCHAR(40)," + 
						"PRIMARY KEY (id)" + 
						")";
				stmt.executeUpdate(sql);
				System.out.println("JDBC: " + sql);
				
				
					/*******************************
					 * Create COMPANY-COUPON table *
					 *******************************/	
			
				sql = "CREATE TABLE app.CompanyCoupon (" + 
						"company_id INT NOT NULL," +  
						"coupon_id INT NOT NULL," + 
						"PRIMARY KEY (company_id,coupon_id)" + 
						")";
				stmt.executeUpdate(sql);
				System.out.println("JDBC: " + sql);
					
					/********************************
					 * Create CUSTOMER-COUPON table *
					 ********************************/	
				
				sql = "CREATE TABLE app.CustomerCoupon (" + 
						"customer_id INT NOT NULL," +  
						"coupon_id INT NOT NULL ," + 
						"PRIMARY KEY (customer_id,coupon_id)" + 
						")";
				stmt.executeUpdate(sql);
				System.out.println("JDBC: " + sql);

				con.commit();
				con.close();
				
				System.out.println("JDBC: DB Is Ready For Test.");
			} catch (SQLException e) {
				System.out.println("ERROR: JDBC: Connection or SQL Falied! " + 
							e.getErrorCode() + e.getMessage() + e.getSQLState() +
							e.getLocalizedMessage());
			} catch (Exception e) {
				System.out.println("ERROR: JDBC: " + e); 
			} finally {

			}
		}
	System.out.println("JDBC: Disconnected...\n\n");
	// Run Tests
	Test.main(null);
	}
}


	

