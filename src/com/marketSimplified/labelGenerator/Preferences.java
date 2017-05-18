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
	private static String CHARACTER_CONVERSION = "";
	private static String ANDROID_LANGUAGE_FOLDER = "";
	private static String ANDROID_STRING_EXTENSION = "";
	private static String OS_TYPE = "";
	private static String IOS_EXTENSION = "";
	private static String IOS_LANGUAGE_FOLDER = "";
	private static String SHEET_TYPE = "";
	private static String COLORS_XML_PATH = "";
	private static String COLORS_PLIST_PATH = "";
	private static String MACROS_PATH="";

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
		CHARACTER_CONVERSION = p.getProperty("CHARACTER_CONVERSION");
		ANDROID_LANGUAGE_FOLDER = p.getProperty("ANDROID_LANGUAGE_FOLDER");
		ANDROID_STRING_EXTENSION = p.getProperty("ANDROID_STRING_EXTENSION");
		IOS_LANGUAGE_FOLDER = p.getProperty("IOS_LANGUAGE_FOLDER");
		IOS_EXTENSION = p.getProperty("IOS_EXTENSION");
		OS_TYPE = p.getProperty("OS_TYPE");
		SHEET_TYPE = p.getProperty("SHEET_TYPE");
		COLORS_PLIST_PATH = p.getProperty("COLORS_PLIST_PATH");
		COLORS_XML_PATH = p.getProperty("COLORS_XML_PATH");
		MACROS_PATH = p.getProperty("MACROS_PATH");
	}

	public static String getSheetname() {
		return Preferences.SPREADSHEET_NAME;
	}

	public static String[] getValueName() {
		String[] valueNames = null;
		if (Preferences.VALUE_COLUMN_NAME.contains("~")) {
			valueNames = Preferences.VALUE_COLUMN_NAME.split("~");
		} else {
			valueNames = new String[1];
			valueNames[0] = Preferences.VALUE_COLUMN_NAME;
		}
		return valueNames;
	}

	public static String isConversion() {
		return Preferences.CHARACTER_CONVERSION;
	}

	public static String[] getAndLanguageFolders() {
		String[] languageFolders = null;
		if (Preferences.ANDROID_LANGUAGE_FOLDER.contains("~")) {
			languageFolders = Preferences.ANDROID_LANGUAGE_FOLDER.split("~");
		} else {
			languageFolders = new String[1];
			languageFolders[0] = Preferences.ANDROID_LANGUAGE_FOLDER;
		}
		return languageFolders;
	}

	public static String getAndroidExtenstion() {
		return Preferences.ANDROID_STRING_EXTENSION;
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
		String xmlPath = p.getProperty("STRINGS_XML_PATH");
		return xmlPath;
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
		String plistPath = p.getProperty("STRINGS_PLIST_PATH");
		return plistPath;
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
			prop.setProperty("ANDROID_LANGUAGE_FOLDER", ANDROID_LANGUAGE_FOLDER);
			prop.setProperty("ANDROID_STRING_EXTENSION", ANDROID_STRING_EXTENSION);
			prop.setProperty("IOS_LANGUAGE_FOLDER", IOS_LANGUAGE_FOLDER);
			prop.setProperty("IOS_EXTENSION", IOS_EXTENSION);
			prop.setProperty("OS_TYPE", OS_TYPE);
			prop.setProperty("SHEET_TYPE", SHEET_TYPE);
			prop.setProperty("COLORS_XML_PATH", COLORS_XML_PATH);
			prop.setProperty("COLORS_PLIST_PATH", COLORS_PLIST_PATH);
			prop.setProperty("MACROS_PATH", MACROS_PATH);
			
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

	public static String getOsType() {
		return Preferences.OS_TYPE;
	}

	public static String getSheetType() {
		return Preferences.SHEET_TYPE;
	}

	public static void setSheetType(String sheetType) {
		SHEET_TYPE = sheetType;
	}

	public static void setOS_TYPE(String oS_TYPE) {
		OS_TYPE = oS_TYPE;
	}

	public static String getIosExtension() {
		return Preferences.IOS_EXTENSION;
	}

	public static void setIOS_EXTENSION(String iOS_EXTENSION) {
		IOS_EXTENSION = iOS_EXTENSION;
	}

	public static String[] getIosLanguageFolders() {
		String[] languageFolders = null;
		if (Preferences.IOS_LANGUAGE_FOLDER.contains("~")) {
			languageFolders = Preferences.IOS_LANGUAGE_FOLDER.split("~");
		} else {
			languageFolders = new String[1];
			languageFolders[0] = Preferences.IOS_LANGUAGE_FOLDER;
		}
		return languageFolders;
	}

	public static void setIOS_LANGUAGE_FOLDER(String iOS_LANGUAGE_FOLDER) {
		IOS_LANGUAGE_FOLDER = iOS_LANGUAGE_FOLDER;
	}

	public static String getCOLORS_XML_PATH() {
		return COLORS_XML_PATH;
	}

	public static void setCOLORS_XML_PATH(String cOLORS_XML_PATH) {
		COLORS_XML_PATH = cOLORS_XML_PATH;
	}

	public static String getCOLORS_PLIST_PATH() {
		return COLORS_PLIST_PATH;
	}

	public static void setCOLORS_PLIST_PATH(String cOLORS_PLIST_PATH) {
		COLORS_PLIST_PATH = cOLORS_PLIST_PATH;
	}

	public static String getMACROS_PATH() {
		return MACROS_PATH;
	}

	public static void setMACROS_PATH(String mACROS_PATH) {
		MACROS_PATH = mACROS_PATH;
	}
}
