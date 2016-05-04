package beans;


import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlRootElement;

import main.CouponException;

/**
 * @author Ofer Moscovich
 * Coupon bean
 */
@XmlRootElement
//JAX-RS supports an automatic mapping from JAXB annotated class to XML and JSON
public class Coupon {

	private long id = 0;
	private String title;
	private Timestamp startDate;
	private Timestamp endDate;	
	private long amount;
	private CouponType type;
	private String message;
	private double price;
	private String image;
	// from Company table
	private String company_name = null;
	private String email = null;
	//from Customer table
	private String customer_name = null;


	/**
	 * Constructor-1
	 * @param id long 
	 * @param title String 
	 * @param startDate Timestamp 
	 * @param endDate Timestamp 
	 * @param amount long 
	 * @param type CouponType 
	 * @param message String 
	 * @param price double 
	 * @param image String 
	 * @param company_name String from Company table
	 * @param email String - company email from Company table
	 * @param customer_name String from Customer table
	 * @throws CouponException
	 **/
	public Coupon(long id, String title, Timestamp startDate, 
				  Timestamp endDate, long amount,CouponType type, 
				  String message, double price, String image,
				  String company_name, String email, 
				  String customer_name) throws CouponException {
		
		this(id,title, startDate, endDate, amount, type, message, 
			 price, image, company_name, email);
		this.customer_name = customer_name;
	}
	
	/**
	 * Constructor-2
	 * @param id long 
	 * @param title String 
	 * @param startDate Timestamp 
	 * @param endDate Timestamp 
	 * @param amount long 
	 * @param type CouponType 
	 * @param message String 
	 * @param price double 
	 * @param image String 
	 * @param company_name String from Company table
	 * @param email String - company email from Company table
	 * @throws CouponException 
	 **/
	public Coupon(long id, String title, Timestamp startDate, 
				  Timestamp endDate, long amount, CouponType type, 
				  String message, double price, String image,
				  String company_name, String email) throws CouponException {
		
		this(id,title, startDate, endDate, amount, type, message, 
			 price, image);
		this.company_name = company_name;
		this.email = email;
	}

	/**
	 * Constructor-3
	 * @param id long 
	 * @param title String 
	 * @param startDate String 
	 * @param endDate String 
	 * @param amount long 
	 * @param type CouponType 
	 * @param message String 
	 * @param price double 
	 * @param image String 
	 * @throws CouponException
	 **/
	public Coupon(long id, String title, Timestamp startDate, Timestamp endDate, long amount, 
					CouponType type, String message, double price, String image) throws CouponException {
		
		this(title, startDate, endDate, amount, type, message, price, image);
		this.id = id;
	}
	
	/**
	 * Constructor-4
	 * @param title String 
	 * @param startDate Timestamp 
	 * @param endDate Timestamp 
	 * @param amount long 
	 * @param type CouponType 
	 * @param message String 
	 * @param price double 
	 * @param image String 
	 * @throws CouponException
	 **/
	public Coupon(String title, Timestamp startDate, 
			      Timestamp endDate, long amount, CouponType type, 
			      String message,double price, String image) throws CouponException {
		// id = 0
		this(0,endDate, price);
		this.title = title;
		this.startDate = startDate;
		this.amount = amount;
		this.type = type;
		this.message = message;
		this.image = image;
	}

	/**
	 * Constructor-5
	 * @param id long 
	 * @param endDate Timestamp 
	 * @param price double 
	 * @throws CouponException
	 * CTCR - without other not updatble fields (for update Coupon).
	 **/
	public Coupon(long id, Timestamp endDate, double price) throws CouponException {

		this.id = id;
		this.endDate = endDate;
		this.price = price;
	}	
	
	/**
	 * Constructor-6
	 * @throws CouponException
	 */
	public Coupon() throws CouponException{
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public CouponType getType() {
		return type;
	}

	public void setType(CouponType type) {
		this.type = type;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	
	// Company email
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	@Override
	public String toString() {
		return "Coupon [id=" + id + ", title=" + title + 
			   ", startDate=" + startDate + ", endDate=" + endDate + 
			   ", amount=" + amount + ", type=" + type + 
			   ", message=" + message + ", price=" + price + 
			   ", image=" + image + ", company_name=" + company_name + 
			   ", email=" + email + ", customer_name=" + customer_name + "]\n";
	}
}
