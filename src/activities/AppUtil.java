/**
 * Application General Utility
 */
package activities;

//import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import main.CouponException;

/**
 * @author Ofer Moscovich
 **/
public class AppUtil {

	/**
	 * Adding days to current date. No hours return. 
	 * @param daysAfter int - number of days after/before to calculate
	 * @return Timestamp - after calculation today date with number of days.
	 * @throws CouponException
	 */
	public Timestamp toDate(int daysAfter) throws CouponException {
		try {
			Calendar calNow = Calendar.getInstance();  
			if (daysAfter > 0) { 
				calNow.add(Calendar.DATE, daysAfter);
			} 
		    Date newDate = calNow.getTime();
		    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		    Timestamp ts = Timestamp.valueOf(format.format(newDate) + " 00:00:00");
		    return ts;
		} catch (Exception e) {
			throw new CouponException("APP ERROR: Date Conversion Failed." + e);
		}
	}	

	/**
	 * Date Converting from String type to Timestamp.
	 * @param dateStr string - an input Date in String type.)
	 * @return Timestamp - date with " 00:00:00" hours format.
	 * @throws CouponException
	 */
	
	public Timestamp toDate (String dateStr) throws CouponException {
		try {
			return Timestamp.valueOf(dateStr + " 00:00:00");
		} catch (Exception e) {
			throw new CouponException("APP ERROR: Date Format Conversion Failed.");
		} 

	}
	
	/**
	 * Date Convert from String type to Timestamp.
	 * @param date (Date) - in date format.
	 * @return Timestamp - date with " 00:00:00" hours format.
	 * @throws CouponException
	 */
	
	public Timestamp toDate (Date date) throws CouponException {
		try {
			return Timestamp.valueOf(date.toString() + " 00:00:00");
		} catch (Exception e) {
			throw new CouponException("APP ERROR: Date Format Conversion Failed.");
		} 

	}

	/**
	 * Get Today Date. 
	 * @return Timestamp - date with " 00:00:00" hours format.
	 * @throws CouponException
	 */
	public Timestamp today () throws CouponException {
		return toDate(0);	 
	}
}
