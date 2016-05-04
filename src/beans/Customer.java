package beans;

import javax.xml.bind.annotation.XmlRootElement;

import main.CouponException;

/**
 * @author Ofer Moscovich
 * Customer bean
 **/
@XmlRootElement
//JAX-RS supports an automatic mapping from JAXB annotated class to XML and JSON
public class Customer {

	private long id = 0;
	private String custName = null;
	private String password;


	/**
	 * Constructor-1
	 * @param id long 
	 * @param name String - customer name
	 * @param password String 
	 * @throws CouponException 
	 * With ID (for known customer - to get customer)
	 **/
	public Customer(long id, String name, String password) throws CouponException {
		this(name,password);
		this.id = id;
	}
	
	/**
	 * Constructor-2
	 * @param name String  - customer name
	 * @param password String 
	 * @throws CouponException 
	 * Without ID and coupons (for new customer)
	 **/
	public Customer(String name, String password) throws CouponException {
		this.custName = name;
		this.password = password;
	}
	
	/**
	 * Constructor-3
	 * @param id long 
	 * @param password String 
	 * @throws CouponException 
	 * customer name = null
	 * Without ID and coupons (for update without name)
	 **/
	public Customer(long id, String password) throws CouponException {
		this.custName = null;
		this.password = password;
		this.id = id;
	}
	
	/**
	 * Constructor-3
	 * @throws CouponException
	 **/
	public Customer() throws CouponException {

	}
		
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", custName=" + custName + ", pass=" + password + "]\n";
	}

}
