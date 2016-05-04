package main;

import java.util.HashSet;
import java.util.Set;
import activities.CouponSystem;
import activities.AppUtil;
import beans.*;
import clients.*;

public class Test {
 
	/**
	 * @param args
	 * Main method to run local presentation test.
	 * To create new DB, run first CreateTestDB.main(). CreateTestDB.main() call to this Test.main().
	 */
	
	public static void main(String[] args) {
		
		Set<Company> companies = new HashSet<Company>();
		Set<Customer> customers = new HashSet<Customer>();
		Set<Coupon> coupons = new HashSet<Coupon>();
		CouponClientFacade facade;

		try {
			/**
			 * CouponSystem return the related facade for login user. 
			 * AppUtil is a general application utility for dates etc. 
			 */
			CouponSystem couponSystem = CouponSystem.getInstance();
			AppUtil appUtil = new AppUtil();
			String line = "---------------------------------------------------------------\n";
			
			/*****************************
			 * Administrator Facade test *
			 *****************************/			
			
			// Login as Admin
			facade = couponSystem.login("admin","1234",ClientType.ADMINFACADE);
			if (facade instanceof AdminFacade) {

				System.out.println("========  Login(admin, 1234, AdminFacade) ========\n");
				
				// Create new 5 companies.
				Company company01 = new Company("PIZZAHUT","1001","pizzaHut@PizzaHut.com"); 
				Company company02 = new Company("HOLMESPLACE","1002","holmesplaceþ@gmail.com"); 
				Company company03 = new Company("BUG","1003","bug@bug.com"); 
				Company company04 = new Company("ISSTA","1004","issta@issta.com"); 
				Company company05 = new Company("ILANS","1005","ilans@yahoo.com"); 
			    
				((AdminFacade) facade).createCompany(company01); // Add new company PIZZAHUT    - gets id=1
				((AdminFacade) facade).createCompany(company02); // Add new company HOLMESPLACE - gets id=2
				((AdminFacade) facade).createCompany(company03); // Add new company BUG		    - gets id=3
				((AdminFacade) facade).createCompany(company04); // Add new company ISSTA       - gets id=4
				((AdminFacade) facade).createCompany(company05); // Add new company ILANS	    - gets id=5
				
				// Show all companies (After creating new companies - before update).
				companies = ((AdminFacade) facade).getAllCompanies();
				System.out.println("After creating new companies - Show all new companies\n" + line + companies.toString());				

				// Update Companies (Id=5,2,3).
				// Update company RIKUSHET with email
				Company company06 = new Company(5,"1005","support@ilans.com");
				((AdminFacade) facade).updateCompany(company06);  

				// Show One updated company id=5
				Company company07 = ((AdminFacade) facade).getCompany(5);
				System.out.println("Show One updated company\n" + company07.toString());
			
				Company company08 = new Company(2,"9998","holm@holm-newþ.com");
				((AdminFacade) facade).updateCompany(company08);  // Update company HOLMESPLACEþ with password

				// Show One updated company
				Company company09 = ((AdminFacade) facade).getCompany(2);
				System.out.println("Show One updated company\n" + company09.toString());				
				
				Company company10 = new Company(3,"9999","info@bug-il.com");
				((AdminFacade) facade).updateCompany(company10);  // Update company BUG with password + email

				// Show One updated company
				Company company11 = ((AdminFacade) facade).getCompany(3);
				System.out.println("Show One updated company\n" + company11.toString());
				
				// Show all companies (after update).
				companies = ((AdminFacade) facade).getAllCompanies();
				System.out.println("Show all companies (after update)\n" + companies.toString());
			
				// Add new 5 customers
				Customer customer01 = new Customer("Ofer Moscovich","2001"); 
				Customer customer02 = new Customer("Shiran Goldringer","2002"); 
				Customer customer03 = new Customer("Hadar","2003");
				Customer customer04 = new Customer("Karin","2004"); 
				Customer customer05 = new Customer("Gili","2005"); 
			    
				((AdminFacade) facade).createCustomer(customer01); // Add new customer Ofer -  gets id=1
				((AdminFacade) facade).createCustomer(customer02); // Add new customer Shiran -gets id=2
				((AdminFacade) facade).createCustomer(customer03); // Add new customer Hadar - gets id=3
				((AdminFacade) facade).createCustomer(customer04); // Add new customer Karin - gets id=4
				((AdminFacade) facade).createCustomer(customer05); // Add new customer Gili -  gets id=5

				// Show all customers (After creating new customers - before update).
				customers = ((AdminFacade) facade).getAllCustomers();
				System.out.println("After creating new customers - Show all customers (before update)\n" + customers.toString());
			
				// Update 2 Customers (id=3,2).
				Customer customer06 = new Customer(3,"2100");
				((AdminFacade) facade).updateCustomer(customer06); // Update customer Gili with password. 

				// Show One updated customer.
				Customer customer07 = ((AdminFacade) facade).getCustomer(3);
				System.out.println("Show One updated customer\n" + customer07.toString());
				
				Customer customer08 = new Customer(2,"2101"); // update customer id = 2.
				((AdminFacade) facade).updateCustomer(customer08);  // Update customer Shiran with password.

				// Show One updated customer
				Customer customer09 = ((AdminFacade) facade).getCustomer(2);
				System.out.println("Show One updated customer\n" + customer09.toString());
				
				// Show all customers (after update).
				customers = ((AdminFacade) facade).getAllCustomers();
				System.out.println("Show all customers (after update)\n" + customers.toString());

			}
			/*****************************************************************
			 * Company Facade TEST: id: 1, user: "PIZZAHUT", password:"1001" *
			 *****************************************************************/
			facade = couponSystem.login("PIZZAHUT","1001",ClientType.COMPANYFACADE);
			if (facade instanceof CompanyFacade) {
				System.out.println("========  Login(PIZZAHUT, 1001, CompanyFacade) ========\n");

				// Add new 10 coupons
				// toDate() can get string date or number of days after/before today.
				Coupon coupon01 = new Coupon("Deal-01", appUtil.toDate(  0),appUtil.toDate(  0), 5, CouponType.FOOD, "Best Deal",  69.90, "http//www.pizzahatCoupon.co.il/?1"); 
				Coupon coupon02 = new Coupon("Deal-02", appUtil.toDate(  1),appUtil.toDate( 90), 4, CouponType.RESTURANTS, "Best Deal",  99.90, "http//www.pizzahatCoupon.co.il/?2"); 
				Coupon coupon03 = new Coupon("Deal-03", appUtil.toDate( 61),appUtil.toDate(120), 10, CouponType.FOOD, "Best Deal", 50.90, "http//www.pizzahatCoupon.co.il/?3"); 
			    
				((CompanyFacade) facade).createCoupon(coupon01); // Add new 1-PIZZAHUT Coupon - gets id=1
				((CompanyFacade) facade).createCoupon(coupon02); // Add new 1-PIZZAHUT Coupon - gets id=2
				((CompanyFacade) facade).createCoupon(coupon03); // Add new 1-PIZZAHUT Coupon - gets id=3

				// Show all new PIZZAHUT coupons (before update).
				coupons = ((CompanyFacade) facade).getCoupons();
				System.out.println("After creating 3 new PIZZAHUT coupons - Show all company coupons (before update)\n" + coupons.toString());
				
				// Update Coupons
				Coupon coupon04 = new Coupon(3, appUtil.toDate("2017-01-01"), 200.90);
				((CompanyFacade) facade).updateCoupon(coupon04); // Update company RIKUSHET with email using same last companyId. 

				Coupon coupon05 = ((CompanyFacade) facade).getCoupon(3);
				System.out.println("Show One PIZZAHUT updated coupon (3)\n" + coupon05.toString());
			
				// getCouponsByType(CouponType.FOOD)
				coupons = ((CompanyFacade) facade).getCouponsByType(CouponType.FOOD);
				System.out.println("View all PIZZAHUT company coupons by Type FOOD\n" + coupons.toString());
			
				// getCouponsByType(CouponType.FOOD)
				coupons = ((CompanyFacade) facade).getCouponsByType(CouponType.RESTURANTS);
				System.out.println("View all PIZZAHUT company coupons by Type RESTURANTS\n" + coupons.toString());

				// getCouponsByMaxCouponPrice(100 nis)
				coupons = ((CompanyFacade) facade).getCouponsByMaxCouponPrice(100.00);
				System.out.println("View all PIZZAHUT company Coupons by Max Price 100 nis:\n" + coupons.toString());

				// getCouponsByMaxCouponDate(100 days from today)
				coupons = ((CompanyFacade) facade).getCouponsByMaxCouponDate(appUtil.toDate(100));
				System.out.println("View all PIZZAHUT company Coupons by Max 100 days from today (Expiration Date= " + appUtil.toDate(100).toString() + "):\n" + coupons.toString());

			}
			
			/*********************************************************************
			 * Company Facade TEST: id: 2, user: "HOLMESPLACE", password: "þ * "9998
			 *********************************************************************/
			facade = couponSystem.login("HOLMESPLACE","9998",ClientType.COMPANYFACADE);
			if (facade instanceof CompanyFacade) {
				System.out.println("========  Login(HOLMESPLACE, 9998, CompanyFacade) ========\n");

				// Add new 10 coupons
				Coupon coupon01 = new Coupon("Supper big Deal-01",appUtil.toDate(0),appUtil.toDate(60),  6, CouponType.SPORTS, "Deal Of The year-1",  120.90, "http//www.holmesplace.co.il/?1"); 
				Coupon coupon02 = new Coupon("Supper big Deal-02",appUtil.toDate(30),appUtil.toDate(90), 6, CouponType.SPORTS, "Deal Of The year-1",  130.90, "http//www.holmesplace.co.il/?2"); 
				Coupon coupon03 = new Coupon("Supper big Deal-03",appUtil.toDate(60),appUtil.toDate(120), 6, CouponType.SPORTS, "Deal Of The year-1",  140.90, "http//www.holmesplace.co.il/?3"); 
				Coupon coupon04 = new Coupon("Supper big Deal-04",appUtil.toDate(90),appUtil.toDate(150), 6, CouponType.SPORTS, "Deal Of The year-1",  29.90, "http//www.holmesplace.co.il/?4"); 
				Coupon coupon05 = new Coupon("Supper big Deal-05",appUtil.toDate(120),appUtil.toDate(180),  6, CouponType.SPORTS, "Deal Of The year-1",  39.90, "http//www.holmesplace.co.il/?5"); 
				Coupon coupon06 = new Coupon("Supper big Deal-06",appUtil.toDate(0),appUtil.toDate(60), 6, CouponType.HEALTH, "Deal Of The year-1",  49.90, "http//www.holmesplace.co.il/?6"); 
				Coupon coupon07 = new Coupon("Supper big Deal-07",appUtil.toDate(30),appUtil.toDate(90), 6, CouponType.HEALTH, "Deal Of The year-1",  59.90, "http//www.holmesplace.co.il/?7"); 
				Coupon coupon08 = new Coupon("Supper big Deal-08",appUtil.toDate(60),appUtil.toDate(120), 6, CouponType.HEALTH, "Deal Of The year-1",  69.90, "http//www.holmesplace.co.il/?8"); 
				Coupon coupon09 = new Coupon("Supper big Deal-09",appUtil.toDate(90),appUtil.toDate(150),  6, CouponType.HEALTH, "Deal Of The year-1",  79.90, "http//www.holmesplace.co.il/?9"); 
				Coupon coupon10 = new Coupon("Supper big Deal-10",appUtil.toDate(120),appUtil.toDate(180), 6, CouponType.HEALTH, "Deal Of The year-1",  89.90, "http//www.holmesplace.co.il/?10"); 

			
			    
				((CompanyFacade) facade).createCoupon(coupon01); // Add new 2-HOLMESPLACE Coupon - gets id=4
				((CompanyFacade) facade).createCoupon(coupon02); // Add new 2-HOLMESPLACE Coupon - gets id=5
				((CompanyFacade) facade).createCoupon(coupon03); // Add new 2-HOLMESPLACE Coupon - gets id=6
				((CompanyFacade) facade).createCoupon(coupon04); // Add new 2-HOLMESPLACE Coupon - gets id=7
				((CompanyFacade) facade).createCoupon(coupon05); // Add new 2-HOLMESPLACE Coupon - gets id=8
				((CompanyFacade) facade).createCoupon(coupon06); // Add new 2-HOLMESPLACE Coupon - gets id=9
				((CompanyFacade) facade).createCoupon(coupon07); // Add new 2-HOLMESPLACE Coupon - gets id=10
				((CompanyFacade) facade).createCoupon(coupon08); // Add new 2-HOLMESPLACE Coupon - gets id=11
				((CompanyFacade) facade).createCoupon(coupon09); // Add new 2-HOLMESPLACE Coupon - gets id=12
				((CompanyFacade) facade).createCoupon(coupon10); // Add new 2-HOLMESPLACE Coupon - gets id=13

				// Show all new HOLMESPLACE coupons.
				coupons = ((CompanyFacade) facade).getCoupons();
				System.out.println("After creating 10 new HOLMESPLACE coupons - Show all company coupons\n" + coupons.toString());
				
				// getCouponsByType(CouponType.SPORTS)
				coupons = ((CompanyFacade) facade).getCouponsByType(CouponType.SPORTS);
				System.out.println("View all HOLMESPLACE company coupons by Type SPORTS\n" + coupons.toString());

				// getCouponsByType(CouponType.HEALTH)
				coupons = ((CompanyFacade) facade).getCouponsByType(CouponType.HEALTH);
				System.out.println("View all HOLMESPLACE company coupons by Type HEALTH\n" + coupons.toString());

				// getCouponsByMaxCouponPrice(100 nis)
				coupons = ((CompanyFacade) facade).getCouponsByMaxCouponPrice(100.00);
				System.out.println("View all HOLMESPLACE company Coupons by Max Price 100 nis:\n" + coupons.toString());

				// getCouponsByMaxCouponDate(100 days from today)
				coupons = ((CompanyFacade) facade).getCouponsByMaxCouponDate(appUtil.toDate(100));
				System.out.println("View all HOLMESPLACE company Coupons by Max 100 days from today (Expiration Date= " + appUtil.toDate(100).toString() + "):\n" + coupons.toString());
				
			}
			
			/*************************************************************
			 * Company Facade TEST: user: id: 3, "BUG", password: "9999" *
			 *************************************************************/
			facade = couponSystem.login("BUG","9999",ClientType.COMPANYFACADE);
			if (facade instanceof CompanyFacade) {
				System.out.println("========  Login(BUG, 9999, CompanyFacade) ========\n");

				// Add new 10 coupons
				Coupon coupon01 = new Coupon("Only Deal-01",appUtil.toDate(0),appUtil.toDate(60),  2, CouponType.ELECTRICITY, "Deal Of The year-1",  30.00, "http//www.bug.co.il/?1"); 
				Coupon coupon02 = new Coupon("Only Deal-02",appUtil.toDate(30),appUtil.toDate(90), 2, CouponType.ELECTRICITY, "Deal Of The year-1",  40.00, "http//www.bug.co.il/?2"); 
				Coupon coupon03 = new Coupon("Only Deal-03",appUtil.toDate(60),appUtil.toDate(120), 2, CouponType.ELECTRICITY, "Deal Of The year-1",  50.00, "http//www.bug.co.il/?3"); 
				Coupon coupon04 = new Coupon("Only Deal-04",appUtil.toDate(90),appUtil.toDate(150), 2, CouponType.ELECTRICITY, "Deal Of The year-1",  60.00, "http//www.bug.co.il/?4"); 
				Coupon coupon05 = new Coupon("Only Deal-05",appUtil.toDate(120),appUtil.toDate(180),  2, CouponType.ELECTRICITY, "Deal Of The year-1",  70.00, "http//www.bug.co.il/?5"); 
				Coupon coupon06 = new Coupon("Only Deal-06",appUtil.toDate(0),appUtil.toDate(60), 2, CouponType.ELECTRICITY, "Deal Of The year-1",  90.00, "http//www.bug.co.il/?6"); 
				Coupon coupon07 = new Coupon("Only Deal-07",appUtil.toDate(30),appUtil.toDate(90), 3, CouponType.ELECTRICITY, "Deal Of The year-1",  90.00, "http//www.bug.co.il/?7"); 
				Coupon coupon08 = new Coupon("Only Deal-08",appUtil.toDate(60),appUtil.toDate(120), 3, CouponType.ELECTRICITY, "Deal Of The year-1",  100.00, "http//www.bug.co.il/?8"); 
				Coupon coupon09 = new Coupon("Only Deal-09",appUtil.toDate(90),appUtil.toDate(150),  3, CouponType.ELECTRICITY, "Deal Of The year-1",  110.00, "http//www.bug.co.il/?9"); 
				Coupon coupon10 = new Coupon("Only Deal-10",appUtil.toDate(120),appUtil.toDate(180), 2, CouponType.ELECTRICITY, "Deal Of The year-1",  120.00, "http//www.bug.co.il/?10"); 
			    
				((CompanyFacade) facade).createCoupon(coupon01); // Add new 3-BUG Coupon - gets id=14
				((CompanyFacade) facade).createCoupon(coupon02); // Add new 3-BUG Coupon - gets id=15
				((CompanyFacade) facade).createCoupon(coupon03); // Add new 3-BUG Coupon - gets id=16
				((CompanyFacade) facade).createCoupon(coupon04); // Add new 3-BUG Coupon - gets id=17
				((CompanyFacade) facade).createCoupon(coupon05); // Add new 3-BUG Coupon - gets id=18
				((CompanyFacade) facade).createCoupon(coupon06); // Add new 3-BUG Coupon - gets id=19
				((CompanyFacade) facade).createCoupon(coupon07); // Add new 3-BUG Coupon - gets id=20
				((CompanyFacade) facade).createCoupon(coupon08); // Add new 3-BUG Coupon - gets id=21
				((CompanyFacade) facade).createCoupon(coupon09); // Add new 3-BUG Coupon - gets id=22
				((CompanyFacade) facade).createCoupon(coupon10); // Add new 3-BUG Coupon - gets id=23

				// Show all new BUG coupons.
				coupons = ((CompanyFacade) facade).getCoupons();
				System.out.println("After creating 10 new BUG coupons - Show all company coupons\n" + coupons.toString());
				
				// getCouponsByType(CouponType.ELECTRICITY)
				coupons = ((CompanyFacade) facade).getCouponsByType(CouponType.ELECTRICITY);
				System.out.println("View all BUG company coupons by Type ELECTRICITY\n" + coupons.toString());

				// getCouponsByMaxCouponPrice(100 nis)
				coupons = ((CompanyFacade) facade).getCouponsByMaxCouponPrice(100.00);
				System.out.println("View all BUG company Coupons by Max Price 100 nis:\n" + coupons.toString());

				// getCouponsByMaxCouponDate(100 days from today)
				coupons = ((CompanyFacade) facade).getCouponsByMaxCouponDate(appUtil.toDate(100));
				System.out.println("View all BUG company Coupons by Max 100 days from today (Expiration Date= " + appUtil.toDate(100).toString() + "):\n" + coupons.toString());
				
			}
			
			
			/***************************************************************
			 * Company Facade TEST: id: 4, user: "ISSTA", password: "1004" *
			 ***************************************************************/
			facade = couponSystem.login("ISSTA","1004",ClientType.COMPANYFACADE);
			if (facade instanceof CompanyFacade) {
				System.out.println("========  Login(ISSTA, 1004, CompanyFacade) ========\n");

				// Add new 10 coupons
				Coupon coupon01 = new Coupon("Year deal-01",appUtil.toDate(0),appUtil.toDate(60),  2, CouponType.CAMPING, "Deal Of The year-1",  35.00, "http//www.ista.co.il/?1"); 
				Coupon coupon02 = new Coupon("Year deal-02",appUtil.toDate(30),appUtil.toDate(90), 3, CouponType.CAMPING, "Deal Of The year-1",  45.00, "http//www.ista.co.il/?2"); 
				Coupon coupon03 = new Coupon("Year deal-03",appUtil.toDate(60),appUtil.toDate(120), 3, CouponType.CAMPING, "Deal Of The year-1",  55.00, "http//www.ista.co.il/?3"); 
				Coupon coupon04 = new Coupon("Year deal-04",appUtil.toDate(90),appUtil.toDate(150), 3, CouponType.CAMPING, "Deal Of The year-1",  65.00, "http//www.ista.co.il/?4"); 
				Coupon coupon05 = new Coupon("Year deal-05",appUtil.toDate(120),appUtil.toDate(180),  3, CouponType.CAMPING, "Deal Of The year-1",  75.00, "http//www.ista.co.il/?5"); 
				Coupon coupon06 = new Coupon("Year deal-06",appUtil.toDate(0),appUtil.toDate(60), 2, CouponType.TRAVELING, "Deal Of The year-1",  85.00, "http//www.ista.co.il/?6"); 
				Coupon coupon07 = new Coupon("Year deal-07",appUtil.toDate(30),appUtil.toDate(90), 3, CouponType.TRAVELING, "Deal Of The year-1",  95.00, "http//www.ista.co.il/?7"); 
				Coupon coupon08 = new Coupon("Year deal-08",appUtil.toDate(60),appUtil.toDate(120), 3, CouponType.TRAVELING, "Deal Of The year-1",  105.00, "http//www.ista.co.il/?8"); 
				Coupon coupon09 = new Coupon("Year deal-09",appUtil.toDate(90),appUtil.toDate(150),  3, CouponType.TRAVELING, "Deal Of The year-1",  110.00, "http//www.ista.co.il/?9"); 
				Coupon coupon10 = new Coupon("Year deal-10",appUtil.toDate(120),appUtil.toDate(180), 3, CouponType.TRAVELING, "Deal Of The year-1",  115.00, "http//www.ista.co.il/?10"); 
			    
				((CompanyFacade) facade).createCoupon(coupon01); // Add new 4-ISSTA Coupon - gets id=24
				((CompanyFacade) facade).createCoupon(coupon02); // Add new 4-ISSTA Coupon - gets id=25
				((CompanyFacade) facade).createCoupon(coupon03); // Add new 4-ISSTA Coupon - gets id=26
				((CompanyFacade) facade).createCoupon(coupon04); // Add new 4-ISSTA Coupon - gets id=27
				((CompanyFacade) facade).createCoupon(coupon05); // Add new 4-ISSTA Coupon - gets id=28
				((CompanyFacade) facade).createCoupon(coupon06); // Add new 4-ISSTA Coupon - gets id=29
				((CompanyFacade) facade).createCoupon(coupon07); // Add new 4-ISSTA Coupon - gets id=30
				((CompanyFacade) facade).createCoupon(coupon08); // Add new 4-ISSTA Coupon - gets id=31
				((CompanyFacade) facade).createCoupon(coupon09); // Add new 4-ISSTA Coupon - gets id=32
				((CompanyFacade) facade).createCoupon(coupon10); // Add new 4-ISSTA Coupon - gets id=33

				// Show all new ISSTA coupons.
				coupons = ((CompanyFacade) facade).getCoupons();
				System.out.println("After creating 10 new ISSTA coupons - Show all company coupons\n" + coupons.toString());
				 
				// getCouponsByType(CouponType.CAMPING)
				coupons = ((CompanyFacade) facade).getCouponsByType(CouponType.CAMPING);
				System.out.println("View all ISSTA company coupons by Type CAMPING\n" + coupons.toString());

				// getCouponsByType(CouponType.TRAVELING)
				coupons = ((CompanyFacade) facade).getCouponsByType(CouponType.TRAVELING);
				System.out.println("View all ISSTA company coupons by Type TRAVELING\n" + coupons.toString());

				// getCouponsByMaxCouponPrice(100 nis)
				coupons = ((CompanyFacade) facade).getCouponsByMaxCouponPrice(100.00);
				System.out.println("View all ISSTA company Coupons by Max Price 100 nis:\n" + coupons.toString());

				// getCouponsByMaxCouponDate(100 days from today)
				coupons = ((CompanyFacade) facade).getCouponsByMaxCouponDate(appUtil.toDate(100));
				System.out.println("View all ISSTA company Coupons by Max 100 days from today (Expiration Date= " + appUtil.toDate(100).toString() + "):\n" + coupons.toString());
				
			}
			
			/***************************************************************
			 * Company Facade TEST: id: 4, user: "ILANS", password: "1005" *
			 ***************************************************************/
			facade = couponSystem.login("ILANS","1005",ClientType.COMPANYFACADE);
			if (facade instanceof CompanyFacade) {
				System.out.println("========  Login(ILANS, 1005, CompanyFacade) ========\n");

				// Add new 10 coupons
				Coupon coupon01 = new Coupon("Great Year deal-01",appUtil.toDate(0),appUtil.toDate(60),  5, CouponType.FOOD, "Deal Of The year-1",  58.00, "http//www.ilans.co.il/?1"); 
				Coupon coupon02 = new Coupon("Great Year deal-02",appUtil.toDate(30),appUtil.toDate(90), 5, CouponType.FOOD, "Deal Of The year-1",  66.00, "http//www.ilans.co.il/?2"); 
				Coupon coupon03 = new Coupon("Great Year deal-03",appUtil.toDate(60),appUtil.toDate(120), 5, CouponType.FOOD, "Deal Of The year-1",  77.00, "http//www.ilans.co.il/?3"); 
				Coupon coupon04 = new Coupon("Great Year deal-04",appUtil.toDate(90),appUtil.toDate(150), 5, CouponType.FOOD, "Deal Of The year-1",  88.00, "http//www.ilans.co.il/?4"); 
				Coupon coupon05 = new Coupon("Great Year deal-05",appUtil.toDate(120),appUtil.toDate(180),  5, CouponType.RESTURANTS, "Deal Of The year-1",  74.00, "http//www.ilans.co.il/?5"); 
				Coupon coupon06 = new Coupon("Great Year deal-06",appUtil.toDate(0),appUtil.toDate(60), 5, CouponType.RESTURANTS, "Deal Of The year-1",  25.00, "http//www.ilans.co.il/?6"); 
				Coupon coupon07 = new Coupon("Great Year deal-07",appUtil.toDate(30),appUtil.toDate(90), 5, CouponType.RESTURANTS, "Deal Of The year-1",  33.00, "http//www.ilans.co.il/?7"); 
				Coupon coupon08 = new Coupon("Great Year deal-08",appUtil.toDate(60),appUtil.toDate(120), 5, CouponType.RESTURANTS, "Deal Of The year-1",  119.00, "http//www.ilans.co.il/?8"); 
				Coupon coupon09 = new Coupon("Great Year deal-09",appUtil.toDate(90),appUtil.toDate(150),  5, CouponType.RESTURANTS, "Deal Of The year-1",  108.00, "http//www.ilans.co.il/?9"); 
				Coupon coupon10 = new Coupon("Great Year deal-10",appUtil.toDate(120),appUtil.toDate(180), 5, CouponType.RESTURANTS, "Deal Of The year-1",  116.00, "http//www.ilans.co.il/?10"); 
			    
				((CompanyFacade) facade).createCoupon(coupon01); // Add new 5-ILANS Coupon - gets id=34
				((CompanyFacade) facade).createCoupon(coupon02); // Add new 5-ILANS Coupon - gets id=35
				((CompanyFacade) facade).createCoupon(coupon03); // Add new 5-ILANS Coupon - gets id=36
				((CompanyFacade) facade).createCoupon(coupon04); // Add new 5-ILANS Coupon - gets id=37
				((CompanyFacade) facade).createCoupon(coupon05); // Add new 5-ILANS Coupon - gets id=38
				((CompanyFacade) facade).createCoupon(coupon06); // Add new 5-ILANS Coupon - gets id=39
				((CompanyFacade) facade).createCoupon(coupon07); // Add new 5-ILANS Coupon - gets id=40
				((CompanyFacade) facade).createCoupon(coupon08); // Add new 5-ILANS Coupon - gets id=41
				((CompanyFacade) facade).createCoupon(coupon09); // Add new 5-ILANS Coupon - gets id=42
				((CompanyFacade) facade).createCoupon(coupon10); // Add new 5-ILANS Coupon - gets id=43

				// Show all new ILANS coupons.
				coupons = ((CompanyFacade) facade).getCoupons();
				System.out.println("After creating 10 new ILANS coupons - Show all company coupons\n" + coupons.toString());
				
				// getCouponsByType(CouponType.FOOD)
				coupons = ((CompanyFacade) facade).getCouponsByType(CouponType.FOOD);
				System.out.println("View all ILANS company coupons by Type FOOD\n" + coupons.toString());

				// getCouponsByType(CouponType.RESTURANTS)
				coupons = ((CompanyFacade) facade).getCouponsByType(CouponType.RESTURANTS);
				System.out.println("View all ILANS company coupons by Type RESTURANTS\n" + coupons.toString());

				// getCouponsByMaxCouponPrice(100 nis)
				coupons = ((CompanyFacade) facade).getCouponsByMaxCouponPrice(100.00);
				System.out.println("View all ILANS company Coupons by Max Price 100 nis:\n" + coupons.toString());

				// getCouponsByMaxCouponDate(100 days from today)
				coupons = ((CompanyFacade) facade).getCouponsByMaxCouponDate(appUtil.toDate(100));
				System.out.println("View all ILANS company Coupons by Max 100 days from today (Expiration Date= " + appUtil.toDate(100).toString() + "):\n" + coupons.toString());
				
			}			
			
		
			/************************
			 * Customer Facade TEST *
			 ************************/			
		
			
			
			/**************************************************************************
			 * Customer Facade TEST: id: 1,  user: "Ofer Moscovich", password: "2001" *
			 **************************************************************************/			
			facade = couponSystem.login("Ofer Moscovich","2001",ClientType.CUSTOMERFACADE);
			if (facade instanceof CustomerFacade) {
				System.out.println("========  Login(Ofer Moscovich, 2001, CustomerFacade) ========\n");
	
				// getCouponsByType(CouponType.RESTURANTS)
				coupons = ((CustomerFacade) facade).getCouponsByType(CouponType.RESTURANTS);
				System.out.println("List of All RESTURANTS Coupons type: \n" + coupons.toString());

				// getCouponsByType(CouponType.FOOD)
				coupons = ((CustomerFacade) facade).getCouponsByType(CouponType.FOOD);
				System.out.println("List of All FOOD Coupons type: \n" + coupons.toString());

				// Purchase Coupons
				((CustomerFacade) facade).purchaseCoupon(2);  // Add new 1-PIZZAHUT Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(41); // Add new 5-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(42); // Add new 5-ILANS Coupon to customer 			
				((CustomerFacade) facade).purchaseCoupon(43); // Add new 5-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(37); // Add new 5-ILANS Coupon to customer				
				((CustomerFacade) facade).purchaseCoupon(36); // Add new 5-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(34); // Add new 5-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(35); // Add new 5-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(38); // Add new 5-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(3);  // Add new 1-PIZZAHUT Coupon to customer 				
			
				// CustomerFacade Reports 
				
				coupons = ((CustomerFacade) facade).getAllPurchasedCoupons();
				System.out.println("List of All Ofer's Purchased Coupons: \n" + coupons.toString());

				coupons = ((CustomerFacade) facade).getAllPurchasedCouponsByType (CouponType.RESTURANTS);
				System.out.println("List of All Ofer's Purchased Coupons by RESTURANTS type: \n" + coupons.toString());

				coupons = ((CustomerFacade) facade).getAllPurchasedCouponsByType (CouponType.FOOD);
				System.out.println("List of All Ofer's Purchased Coupons by FOOD type: \n" + coupons.toString());

				coupons = ((CustomerFacade) facade).getAllPurchasedCouponsByPrice (80); 
				System.out.println("List of All Ofer's Purchased Coupons by max price of 80 nis: \n" + coupons.toString());
				
			}


			/*****************************************************************************
			 * Customer Facade TEST: id: 2,  user: "Shiran Goldringer", password: "2002" *
			 *****************************************************************************/			
			facade = couponSystem.login("Shiran Goldringer","2101",ClientType.CUSTOMERFACADE);
			if (facade instanceof CustomerFacade) {
				System.out.println("========  Login(Shiran Goldringer, 2101, CustomerFacade) ========\n");

				// getCouponsByType(CouponType.ELECTRICITY)
				coupons = ((CustomerFacade) facade).getCouponsByType(CouponType.ELECTRICITY);
				System.out.println("List of All ELECTRICITY Coupons type: \n" + coupons.toString());

				// getCouponsByType(CouponType.FOOD)
				coupons = ((CustomerFacade) facade).getCouponsByType(CouponType.FOOD);
				System.out.println("List of All FOOD Coupons type: \n" + coupons.toString());

				// getCouponsByType(CouponType.SPORTS)
				coupons = ((CustomerFacade) facade).getCouponsByType(CouponType.SPORTS);
				System.out.println("List of All SPORTS Coupons type: \n" + coupons.toString());

				// getCouponsByType(CouponType.HEALTH)
				coupons = ((CustomerFacade) facade).getCouponsByType(CouponType.HEALTH);
				System.out.println("List of All HEALTH Coupons type: \n" + coupons.toString());
				
				// Purchase Coupons
				((CustomerFacade) facade).purchaseCoupon(3);  // Add new 1-PIZZAHUT Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(35); // Add new 5-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(37); // Add new 5-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(14); // Add new 5-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(18); // Add new 5-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(20); // Add new 5-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(21); // Add new 5-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(23); // Add new 5-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(5); // Add new 2-HOLMESPLACE Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(9);  // Add new 2-HOLMESPLACE Coupon to customer 				
			
				// CustomerFacade Reports 
				
				coupons = ((CustomerFacade) facade).getAllPurchasedCoupons();
				System.out.println("List of All Shiran's Purchased Coupons: \n" + coupons.toString());

				coupons = ((CustomerFacade) facade).getAllPurchasedCouponsByType (CouponType.FOOD);
				System.out.println("List of All Shiran's Purchased Coupons by FOOD type: \n" + coupons.toString());

				coupons = ((CustomerFacade) facade).getAllPurchasedCouponsByType (CouponType.SPORTS);
				System.out.println("List of All Shiran's Purchased Coupons by SPORTS type: \n" + coupons.toString());

				coupons = ((CustomerFacade) facade).getAllPurchasedCouponsByType (CouponType.HEALTH);
				System.out.println("List of All Shiran's Purchased Coupons by HEALTH type: \n" + coupons.toString());

				coupons = ((CustomerFacade) facade).getAllPurchasedCouponsByPrice (80); 
				System.out.println("List of All Shiran's Purchased Coupons by max price of 80 nis: \n" + coupons.toString());
				
			}
			
			/*****************************************************************
			 * Customer Facade TEST: id: 3,  user: "Hadar", password: "2100" *
			 *****************************************************************/			
			facade = couponSystem.login("Hadar","2100",ClientType.CUSTOMERFACADE);
			if (facade instanceof CustomerFacade) {
				System.out.println("========  Login(Hadar, 2100, CustomerFacade) ========\n");

			// Purchase Coupons
				((CustomerFacade) facade).purchaseCoupon(2);  // Add new 1-PIZZAHUT Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(41); // Add new 5-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(42); // Add new 5-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(43); // Add new 5-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(37); // Add new 5-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(36); // Add new 5-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(34); // Add new 5-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(35); // Add new 5-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(38); // Add new 5-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(3);  // Add new 1-PIZZAHUT Coupon to customer 				
		
				// CustomerFacade Reports 
				
				coupons = ((CustomerFacade) facade).getAllPurchasedCoupons();
				System.out.println("List of All Hadar's Purchased Coupons: \n" + coupons.toString());

				coupons = ((CustomerFacade) facade).getAllPurchasedCouponsByType (CouponType.RESTURANTS);
				System.out.println("List of All Hadar's Purchased Coupons by RESTURANTS type: \n" + coupons.toString());

				coupons = ((CustomerFacade) facade).getAllPurchasedCouponsByType (CouponType.FOOD);
				System.out.println("List of All Hadar's Purchased Coupons by FOOD type: \n" + coupons.toString());

				coupons = ((CustomerFacade) facade).getAllPurchasedCouponsByPrice (60); 
				System.out.println("List of All Hadar's Purchased Coupons by max price of 80 nis: \n" + coupons.toString());
				
			}
			
			/*****************************************************************
			 * Customer Facade TEST: id: 4,  user: "Karin", password: "2004" *
			 *****************************************************************/			
			facade = couponSystem.login("Karin","2004",ClientType.CUSTOMERFACADE);
			if (facade instanceof CustomerFacade) {
				System.out.println("========  Login(Karin, 2004, CustomerFacade) ========\n");

				// Purchase Coupons
				((CustomerFacade) facade).purchaseCoupon(2);  // Add new 1-PIZZAHUT Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(41); // Add new 2-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(42); // Add new 2-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(43); // Add new 2-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(37); // Add new 2-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(36); // Add new 2-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(34); // Add new 2-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(35); // Add new 2-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(38); // Add new 1-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(3);  // Add new 1-PIZZAHUT Coupon to customer 				
			
				// CustomerFacade Reports 
				
				coupons = ((CustomerFacade) facade).getAllPurchasedCoupons();
				System.out.println("List of All Karin's Purchased Coupons: \n" + coupons.toString());

				coupons = ((CustomerFacade) facade).getAllPurchasedCouponsByType (CouponType.RESTURANTS);
				System.out.println("List of All Karin's Purchased Coupons by RESTURANTS type: \n" + coupons.toString());

				coupons = ((CustomerFacade) facade).getAllPurchasedCouponsByType (CouponType.FOOD);
				System.out.println("List of All Karin's Purchased Coupons by FOOD type: \n" + coupons.toString());

				coupons = ((CustomerFacade) facade).getAllPurchasedCouponsByPrice (80); 
				System.out.println("List of All Karin's Purchased Coupons by max price of 80 nis: \n" + coupons.toString());
				
			}
		
			
			/***************************************************************
			 * Customer Facade TEST: id: 5, user: "Gili", password: "2005" *
			 ***************************************************************/			
			facade = couponSystem.login("Gili","2005",ClientType.CUSTOMERFACADE);
			if (facade instanceof CustomerFacade) {
				System.out.println("========  Login(Gili, 2005, CustomerFacade) ========\n");
	
				// Purchase Coupons
				((CustomerFacade) facade).purchaseCoupon(2);  // Add new 1-PIZZAHUT Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(41); // Add new 2-ILANS Coupon to customer 			
				((CustomerFacade) facade).purchaseCoupon(42); // Add new 2-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(43); // Add new 2-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(37); // Add new 2-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(36); // Add new 2-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(34); // Add new 2-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(35); // Add new 2-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(38); // Add new 1-ILANS Coupon to customer 				
				((CustomerFacade) facade).purchaseCoupon(3);  // Add new 1-PIZZAHUT Coupon to customer 				
			
				// CustomerFacade Reports 
				
				coupons = ((CustomerFacade) facade).getAllPurchasedCoupons();
				System.out.println("List of All Gili's Purchased Coupons: \n" + coupons.toString());

				coupons = ((CustomerFacade) facade).getAllPurchasedCouponsByType (CouponType.RESTURANTS);
				System.out.println("List of All Gili's Purchased Coupons by RESTURANTS type: \n" + coupons.toString());

				coupons = ((CustomerFacade) facade).getAllPurchasedCouponsByType (CouponType.FOOD);
				System.out.println("List of All Gili's Purchased Coupons by FOOD type: \n" + coupons.toString());

				coupons = ((CustomerFacade) facade).getAllPurchasedCouponsByPrice (80); 
				System.out.println("List of All Gili's Purchased Coupons by max price of 80 nis: \n" + coupons.toString());

			}

			/*****************************************
			 * Company Facade test - DELETE (Coupon) *
			 *****************************************/			
			
			// Login as Company ILANS
			facade = couponSystem.login("ILANS","1005",ClientType.COMPANYFACADE);
			if (facade instanceof CompanyFacade) {
				System.out.println("========  Login(ILANS, 1005, CompanyFacade) ========\n");
		
				// Show 5-ILANS company coupons 38 (before removing coupon 38).
				Coupon coupon = ((CompanyFacade) facade).getCoupon(38);
				System.out.println("View company coupons 38 (before removing coupon 38)\n" + coupon.toString());

				// Remove Coupon 38 from company 4-ISSTA
				 ((CompanyFacade) facade).removeCoupon(38); // companyId+couponId
				System.out.println("coupons 38 Deleted.\n");
				 
				 
				// Show 5-ILANS company coupons 38 (before removing coupon 38).
				coupon = ((CompanyFacade) facade).getCoupon(38);
				System.out.println("View company coupons 38 (after removing coupon 38)\n" + coupon.toString());
			}

			/*****************************
			 * Administrator Facade test *
			 *****************************/			
			
			// Login as Admin
			facade = couponSystem.login("admin","1234",ClientType.ADMINFACADE);
			if (facade instanceof AdminFacade) {
				System.out.println("========  Login(admin, 1234, AdminFacade) ========\n");

				/**
				 *  Remove Customer.
				 */
				
				// Show all customers (before removing customer 1-Ofer).
				customers = ((AdminFacade) facade).getAllCustomers();
				System.out.println("View all customers (before removing customer 1-Ofer)\n" + customers.toString());
			}
			/*************************
			 * Customer Facade check *
			 *************************/			
			
			// Login as Ofer Customer
			facade = couponSystem.login("Ofer Moscovich","2001",ClientType.CUSTOMERFACADE);
			if (facade instanceof CustomerFacade) {
				System.out.println("========  Login(admin, 1234, AdminFacade) ========\n");

				/**
				 *  View all Ofer (Customer) Coupons Purchased.
				 */

				coupons = ((CustomerFacade) facade).getAllPurchasedCoupons();
				System.out.println("View all customer 1-Ofer purchased Coupons (before removing customer 1-Ofer)\n" + coupons.toString());
			}
			/*****************************
			 * Administrator Facade test *
			 *****************************/			
			
			// Login as Admin
			facade = couponSystem.login("admin","1234",ClientType.ADMINFACADE);
			if (facade instanceof AdminFacade) {
				System.out.println("========  Login(admin, 1234, AdminFacade) ========\n");
			
				// Remove customer 1-Ofer
				((AdminFacade) facade).removeCustomer(1);
				System.out.println("========  DELETE Customer 1-Ofer ========\n");


				// Show all customers (after removing customer 1-Ofer).
				Customer customer = ((AdminFacade) facade).getCustomer(1);
				System.out.println("View customer 1-Ofer (after removing customer 1-Ofer)\n" + customer.toString());

								
				/**
				 *  Remove Company.
				 */
				
				// Show all companies (after removing customer 5-ILANS).
				companies = ((AdminFacade) facade).getAllCompanies();
				System.out.println("View all companies (before removing company 5-ILANS)\n" + companies.toString());
				
				// Remove Company 5-ILANS
				((AdminFacade) facade).removeCompany(5);				
				System.out.println("========  DELETE Company 5-ILANS ========\n");

				// Show all companies (after removing customer 5-ILANS).
				companies = ((AdminFacade) facade).getAllCompanies();
				System.out.println("View all companies (after removing company 5-ILANS)\n" + companies.toString());
				
			}		

			System.out.println("========  shut down system ========\n");

			/**
			 *  End of program - Close thread all connections, thread
			 */
			couponSystem.shutdown();	
			System.out.println("Disconnected!");
			
		} catch (Exception e) {
			System.out.println("Coupon System Error Message: " + e.getMessage());
		}	
	}
}

