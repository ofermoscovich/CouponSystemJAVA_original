package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Import project data information from file - DB driver and DB connection String
 */

public class GetFileParms {
	
	private static final String FILENAME = "system/DriverNameAndDburl";
	
	public static String getDriverName() throws CouponException {
		
		try (Scanner sc = new Scanner(new File(FILENAME));) {
			String driverName = sc.nextLine();
			return driverName;
		} catch (FileNotFoundException e) {
			throw new CouponException("Error: File Not Found!");
		} 
	}

	public static String getDbUrl() throws CouponException {

		try (Scanner sc = new Scanner(new File(FILENAME));) {
			String url = sc.nextLine();
			url = sc.nextLine();
			return url;
		} catch (FileNotFoundException e) {
			throw new CouponException("Error: File Not Found!");
		} 
	}
}
