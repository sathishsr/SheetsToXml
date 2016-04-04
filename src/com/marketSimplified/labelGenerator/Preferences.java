package com.marketSimplified.labelGenerator;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;

/**
 * @author sathishsr
 *
 */
public class Preferences {

	private static String CONFIG_FILE = "config.properties";
	private static String USERNAME = "";
	private static String PASSWORD = "";
	private static String SPREADSHEET_NAME = "";
	private static String STRINGS_XML_PATH = "";
	private static String STRINGS_PLIST_PATH = "";
	private static String CONSTANTS_PATH = "";
	private static String CONFIG_JSON_PATH = "";
	private static String LABEL_PROPS_PATH = "";
	private static String WINDOWS8_RES_PATH = "";
	private static String WINDOWS81_RES_PATH = "";
	private static String KEY_COLUMN_NAME = "";
	private static String VALUE_COLUMN_NAME = "";
	private static String DYNAMIC_COLUMN_NAME = "";
	private static String BBVALUES_COLUMN_NAME = "";
	private static String CONSTANTS_PACKAGE = "";
	private static String CHARACTER_CONVERSION="";
	/**
	 * Load the data from config.properties
	 */
	public static void loadConfig() {
		Properties p = new Properties();
		try {
			p.load(new FileReader(CONFIG_FILE));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in reading config file " + e.getMessage());

			System.exit(1);
		}

		USERNAME = p.getProperty("USERNAME");
		PASSWORD = p.getProperty("PASSWORD");
		SPREADSHEET_NAME = p.getProperty("SPREADSHEET_NAME");
		STRINGS_XML_PATH = p.getProperty("STRINGS_XML_PATH");
		STRINGS_PLIST_PATH = p.getProperty("STRINGS_PLIST_PATH");
		CONSTANTS_PATH = p.getProperty("CONSTANTS_PATH");
		CONFIG_JSON_PATH = p.getProperty("CONFIG_JSON_PATH");
		LABEL_PROPS_PATH = p.getProperty("LABEL_PROPS_PATH");
		WINDOWS8_RES_PATH = p.getProperty("WINDOWS8_RES_PATH");
		WINDOWS81_RES_PATH = p.getProperty("WINDOWS81_RES_PATH");
		KEY_COLUMN_NAME = p.getProperty("KEY_COLUMN_NAME");
		VALUE_COLUMN_NAME = p.getProperty("VALUE_COLUMN_NAME");
		DYNAMIC_COLUMN_NAME = p.getProperty("DYNAMIC_COLUMN_NAME");
		BBVALUES_COLUMN_NAME = p.getProperty("BBVALUES_COLUMN_NAME");
		CONSTANTS_PACKAGE = p.getProperty("CONSTANTS_PACKAGE");
		CHARACTER_CONVERSION=p.getProperty("CHARACTER_CONVERSION");
	}
	
	public static String getSheetname(){
		return Preferences.SPREADSHEET_NAME;
	}
	
	public static String isConversion(){
		return Preferences.CHARACTER_CONVERSION;
	}

	public static String getToken() {
		Properties p = new Properties();
		try {
			p.load(new FileReader(CONFIG_FILE));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in reading config file " + e.getMessage());

			System.exit(1);
		}
		String refreshToken = p.getProperty("REFRESH_TOKEN");
		return refreshToken;
	}
	
	public static String getStringsXmlPath() {
		Properties p = new Properties();
		try {
			p.load(new FileReader(CONFIG_FILE));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in reading config file " + e.getMessage());

			System.exit(1);
		}
		String refreshToken = p.getProperty("STRINGS_XML_PATH");
		return refreshToken;
	}
	public static String getLocalizablePath() {
		Properties p = new Properties();
		try {
			p.load(new FileReader(CONFIG_FILE));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in reading config file " + e.getMessage());

			System.exit(1);
		}
		String refreshToken = p.getProperty("STRINGS_PLIST_PATH");
		return refreshToken;
	}
	

	/**
	 * 
	 * @param response
	 */
	public static void saveProperties(GoogleTokenResponse response) {
		Properties prop = new Properties();
		OutputStream output = null;

		try {

			output = new FileOutputStream("config.properties");

			// set the properties value
			prop.setProperty("USERNAME", USERNAME);
			prop.setProperty("PASSWORD", PASSWORD);
			prop.setProperty("SPREADSHEET_NAME", SPREADSHEET_NAME);
			prop.setProperty("STRINGS_XML_PATH", STRINGS_XML_PATH);
			prop.setProperty("STRINGS_PLIST_PATH", STRINGS_PLIST_PATH);
			prop.setProperty("CONSTANTS_PATH", CONSTANTS_PATH);
			prop.setProperty("CONFIG_JSON_PATH", CONFIG_JSON_PATH);
			prop.setProperty("LABEL_PROPS_PATH", LABEL_PROPS_PATH);
			prop.setProperty("WINDOWS8_RES_PATH", WINDOWS8_RES_PATH);
			prop.setProperty("WINDOWS81_RES_PATH", WINDOWS81_RES_PATH);
			prop.setProperty("KEY_COLUMN_NAME", KEY_COLUMN_NAME);
			prop.setProperty("VALUE_COLUMN_NAME", VALUE_COLUMN_NAME);
			prop.setProperty("DYNAMIC_COLUMN_NAME", DYNAMIC_COLUMN_NAME);
			prop.setProperty("BBVALUES_COLUMN_NAME", BBVALUES_COLUMN_NAME);
			prop.setProperty("CONSTANTS_PACKAGE", CONSTANTS_PACKAGE);

			/**************/
			prop.setProperty("ACCESS_TOKEN", response.getAccessToken());
			prop.setProperty("REFRESH_TOKEN", response.getRefreshToken());

			// save properties to project root folder
			prop.store(output, null);

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}
}
