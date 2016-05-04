package beans;

import javax.xml.bind.annotation.XmlRootElement;

import main.CouponException;

/**
 * @author Ofer Moscovich
 * Company bean
 */
@XmlRootElement
//JAX-RS supports an automatic mapping from JAXB annotated class to XML and JSON
public class Company {

	private long id = 0;
	private String compName = null;
	private String password;
	private String email;

	/**
	 * Constructor-1
	 * 
	 * @param id long - company id (key)
	 * @param password String
	 * @param email String
	 * compName = null
	 * With ID without name (for update without name)
	 **/
	public Company(long id, 
				   String password,
				   String email) throws CouponException{
		this(null,password,email);
		this.compName = null;
		this.id = id;
	}

	/**
	 * Constructor-2
	 * 
	 * @param id long - company id (key)
	 * @param name String
	 * @param password String 
	 * @param email String
	 * @throws CouponException 
	 */
	// With ID without name (To get company by id)
	public Company(long id,
				   String name,
				   String password,
				   String email) throws CouponException{
		this(name,password,email);
		this.id = id;
	}
	
	/**
	 * Constructor-3
	 * 
	 * @param name String 
	 * @param password String 
	 * @param email String 
	 * @throws CouponException
	 * Without ID and coupons (for new company)	
	 **/	
	public Company(String name, 
			       String password, 
			       String email) throws CouponException{
		this.compName = name;
		this.password = password;
		this.email = email;
	}

	/**
	 * Constructor-4
	 * for Collection of companies
	 * @throws CouponException
	 */
	public Company() throws CouponException{
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Company [id=" + id + ", Name=" + compName + 
			   ", pass=" + password + ", email=" + email + "]\n";
	}

}
