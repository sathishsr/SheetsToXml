package com.marketSimplified.labelGenerator;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringEscapeUtils;

import com.google.api.client.auth.oauth2.Credential;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.ServiceException;

/**
 * @author sathishsr
 *
 */
public class SpreadSheetToXml {

	private static URL listFeedUrl;

	private static StringBuffer constants = new StringBuffer();
	private static StringBuffer stringsXML = new StringBuffer();
	private static StringBuffer colorsXML = new StringBuffer();
	// static StringBuffer configjson = new StringBuffer();
	private static StringBuffer stringsplist = new StringBuffer();
	private static StringBuffer macrosList = new StringBuffer();
	private static StringBuffer colorsplist = new StringBuffer();

	private static StringBuffer win8XML = new StringBuffer();
	private static StringBuffer win81XML = new StringBuffer();
	private static StringBuffer labelconfig = new StringBuffer();
	private static StringBuffer duplicateKeys = new StringBuffer();
	private static ArrayList<String> keyList = new ArrayList<String>();
	private static final String QUOTE = "\"";

	private static String KEY_COLUMN_NAME = "key";
	private static String VALUE_COLUMN_NAME = "value";
	private static String DYNAMIC_COLUMN_NAME = "isdynamicyesno";
	private static String BBVALUES_COLUMN_NAME = "bbvalues";
	private static String CONSTANTS_PACKAGE = "com.msf.kmb";

	private static String STRINGS_XML_PATH = "strings.xml";
	private static String STRINGS_PLIST_PATH = "Localizable.strings";
	private static String MACROS_PATH = "Localizable.h";

	private static String COLORS_XML_PATH = "colors.xml";
	private static String COLORS_PLIST_PATH = "appmacros.h";

	private static String CONSTANTS_PATH = "LabelConfig.java";
	private static String LABEL_PROPS_PATH = "labelconfig.properties";
	private static String WINDOWS8_RES_PATH = "Proj.en-IN.resx";
	private static String WINDOWS81_RES_PATH = "Proj.en-IN.resw";

	private static int languagePosition;
	private static boolean addedNewLine = false;
	private static boolean isColor;

	private static int getLanguageFolderCount;

	private static String languageFolder;

	private static HashMap<String, String> stringsXMLMap = new HashMap<String, String>();

	/**
	 *
	 * @param credential
	 * @throws IOException
	 * @throws ServiceException
	 */
	static void printDocuments(Credential credential) throws IOException, ServiceException {
		// Instantiate and authorize a new SpreadsheetService object.

		SpreadsheetService service = new SpreadsheetService("LabelGenerator");
		service.setOAuth2Credentials(credential);
		// Send a request to the Documents List API to retrieve document
		// entries.
		URL SPREADSHEET_FEED_URL = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full");
		// Make a request to the API and get all spreadsheets.
		SpreadsheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL, SpreadsheetFeed.class);
		List<com.google.gdata.data.spreadsheet.SpreadsheetEntry> spreadsheets = feed.getEntries();
		if (spreadsheets.isEmpty()) {
			System.out.println("No SpreadSheets available.");
		}

		// com.google.gdata.data.spreadsheet.SpreadsheetEntry spreadsheet =
		// spreadsheets.get(0);
		// System.out.println(spreadsheet.getTitle().getPlainText());
		// Get the first worksheet of the first spreadsheet.

		Preferences.loadConfig();
		STRINGS_XML_PATH = "";
		STRINGS_PLIST_PATH = "";
		COLORS_PLIST_PATH = "";
		COLORS_XML_PATH = "";
		MACROS_PATH="";

		isColor = Preferences.getSheetType().equalsIgnoreCase("COLOR");

		if (isColor) {
			if (Preferences.getOsType().equalsIgnoreCase("ios")) {
				COLORS_PLIST_PATH = COLORS_PLIST_PATH + Preferences.getCOLORS_PLIST_PATH();
				MACROS_PATH=MACROS_PATH+Preferences.getMACROS_PATH();
			} else {
				COLORS_XML_PATH = COLORS_XML_PATH + Preferences.getCOLORS_XML_PATH();
			}
		}

		getLanguageFolderCount = (Preferences.getOsType().equalsIgnoreCase("ios"))
				? Preferences.getIosLanguageFolders().length : Preferences.getAndLanguageFolders().length;

		for (int langaugeCount = 0; langaugeCount < getLanguageFolderCount; langaugeCount++) {

			resetValues();
			languagePosition = langaugeCount;
			if (Preferences.getOsType().equalsIgnoreCase("ios")) {
				STRINGS_PLIST_PATH = STRINGS_PLIST_PATH + Preferences.getIosLanguageFolders()[langaugeCount]
						+ Preferences.getIosExtension();
			} else {
				STRINGS_XML_PATH = STRINGS_XML_PATH + Preferences.getAndLanguageFolders()[langaugeCount]
						+ Preferences.getAndroidExtenstion();
			}

			languageFolder = Preferences.getAndLanguageFolders()[langaugeCount];

			for (com.google.gdata.data.spreadsheet.SpreadsheetEntry spreadsheet : spreadsheets) {

				if (Preferences.getSheetname().equals(spreadsheet.getTitle().getPlainText())) {

					System.out.println("SpreadSheet Title Name:::" + spreadsheet.getTitle().getPlainText().toString());

					WorksheetFeed worksheetFeed = service.getFeed(spreadsheet.getWorksheetFeedUrl(),
							WorksheetFeed.class);
					List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
					WorksheetEntry worksheet = worksheets.get(0);

					/*
					 * if (spreadsheet.getWorksheets().size() >= 1) {
					 * listFeedUrl =
					 * spreadsheet.getWorksheets().get(0).getListFeedUrl(); }
					 */

					for (int sheet = 0; sheet < spreadsheet.getWorksheets().size(); sheet++) {
						listFeedUrl = spreadsheet.getWorksheets().get(sheet).getListFeedUrl();

						// Fetch the cell feed of the worksheet.
						URL cellFeedUrl = worksheet.getCellFeedUrl();
						CellFeed cellFeed = service.getFeed(cellFeedUrl, CellFeed.class);
						System.out.println("SpreadSheet Sheet Name:::"
								+ spreadsheet.getWorksheets().get(sheet).getTitle().getPlainText().toString());
						// If isColor = false, move on to localization
						// generator.
						if (!isColor) {
							// Iterate through each cell, printing its value.
							for (CellEntry cell : cellFeed.getEntries()) {
								// Print the cell's address in A1 notation
								System.out.print(cell.getTitle().getPlainText() + "\t");
								// Print the cell's address in R1C1 notation
								System.out.print(cell.getId().substring(cell.getId().lastIndexOf('/') + 1) + "\t");
								// Print the cell's formula or text value
								System.out.print(cell.getCell().getInputValue() + "\t");
								// Print the cell's calculated value if the
								// cell's
								// value
								// is
								// numeric
								// Prints empty string if cell's value is not
								// numeric
								System.out.print(cell.getCell().getNumericValue() + "\t");
								// Print the cell's displayed value (useful if
								// the
								// cell
								// has
								// a
								// formula)
								System.out.println(cell.getCell().getValue() + "\t");
							}
							processAllEntries(service, isColor);
							writeToFile();
							
							// Move on to Color generator.
						} else {
							if (spreadsheet.getWorksheets().get(sheet).getTitle().getPlainText()
									.equalsIgnoreCase("Colors")) {
								System.out.println("Colors");
								processAllEntries(service, isColor);
								 writeToFile();
								 return;
							}
							
						}
					}

				}
			}
		}

	}

	private static void resetValues() {
		keyList.clear();
		stringsXML.delete(0, stringsXML.length());
		colorsXML.delete(0, colorsXML.length());
		stringsplist.delete(0, stringsplist.length());
		macrosList.delete(0, stringsplist.length());
		colorsplist.delete(0, colorsplist.length());
		duplicateKeys.delete(0, duplicateKeys.length());
		STRINGS_XML_PATH = Preferences.getStringsXmlPath();
		STRINGS_PLIST_PATH = Preferences.getLocalizablePath();
		MACROS_PATH=Preferences.getMACROS_PATH();

	}

	/**
	 *
	 * @param service
	 * @throws IOException
	 * @throws ServiceException
	 */
	private static void processAllEntries(SpreadsheetService service, boolean isColor)
			throws IOException, ServiceException {
		ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);

		if (!isColor) {
			constants.append("package ").append(CONSTANTS_PACKAGE).append(";\n\npublic interface LabelConfig {\n\n");
			stringsXML.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + "<resources>\n");

			win8XML.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + "<root>\n"
					+ "<xsd:schema id=\"root\" xmlns=\"\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:msdata=\"urn:schemas-microsoft-com:xml-msdata\">\n"
					+ "<xsd:import namespace=\"http://www.w3.org/XML/1998/namespace\" />\n"
					+ "<xsd:element name=\"root\" msdata:IsDataSet=\"true\">\n" + "<xsd:complexType>\n"
					+ "<xsd:choice maxOccurs=\"unbounded\">\n" + "<xsd:element name=\"metadata\">\n"
					+ "<xsd:complexType>\n" + "<xsd:sequence>\n"
					+ "<xsd:element name=\"value\" type=\"xsd:string\" minOccurs=\"0\" />\n" + " </xsd:sequence>\n"
					+ "<xsd:attribute name=\"name\" use=\"required\" type=\"xsd:string\" />\n"
					+ "<xsd:attribute name=\"type\" type=\"xsd:string\" />\n"
					+ "<xsd:attribute name=\"mimetype\" type=\"xsd:string\" />\n"
					+ "<xsd:attribute ref=\"xml:space\" />\n" + "  </xsd:complexType>\n" + "</xsd:element>\n"
					+ "<xsd:element name=\"assembly\">\n" + "<xsd:complexType>\n"
					+ "<xsd:attribute name=\"alias\" type=\"xsd:string\" />\n"
					+ "<xsd:attribute name=\"name\" type=\"xsd:string\" />\n" + " </xsd:complexType>\n"
					+ " </xsd:element>\n" + "<xsd:element name=\"data\">\n" + "<xsd:complexType>\n" + "<xsd:sequence>\n"
					+ "<xsd:element name=\"value\" type=\"xsd:string\" minOccurs=\"0\" msdata:Ordinal=\"1\" />\n"
					+ "<xsd:element name=\"comment\" type=\"xsd:string\" minOccurs=\"0\" msdata:Ordinal=\"2\" />\n"
					+ " </xsd:sequence>\n"
					+ "<xsd:attribute name=\"name\" type=\"xsd:string\" use=\"required\" msdata:Ordinal=\"1\" />\n"
					+ "<xsd:attribute name=\"type\" type=\"xsd:string\" msdata:Ordinal=\"3\" />\n"
					+ "<xsd:attribute name=\"mimetype\" type=\"xsd:string\" msdata:Ordinal=\"4\" />\n"
					+ "<xsd:attribute ref=\"xml:space\" />\n" + " </xsd:complexType>\n" + "</xsd:element>\n"
					+ "<xsd:element name=\"resheader\">\n" + "<xsd:complexType>\n" + "<xsd:sequence>\n"
					+ "<xsd:element name=\"value\" type=\"xsd:string\" minOccurs=\"0\" msdata:Ordinal=\"1\" />\n"
					+ "</xsd:sequence>\n" + "<xsd:attribute name=\"name\" type=\"xsd:string\" use=\"required\" />\n"
					+ " </xsd:complexType>\n" + " </xsd:element>\n" + " </xsd:choice>\n" + "  </xsd:complexType>\n"
					+ " </xsd:element>\n" + "</xsd:schema>\n" + "<resheader name=\"resmimetype\">\n"
					+ "  <value>text/microsoft-resx</value>\n" + " </resheader>\n" + "<resheader name=\"version\">\n"
					+ " <value>2.0</value>\n" + "</resheader>\n" + " <resheader name=\"reader\">\n"
					+ " <value>System.Resources.ResXResourceReader, System.Windows.Forms, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089</value>\n"
					+ "</resheader>\n" + "<resheader name=\"writer\">\n"
					+ " <value>System.Resources.ResXResourceWriter, System.Windows.Forms, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089</value>\n"
					+ "  </resheader>\n");

			win81XML.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + "<root>\n"
					+ "<xsd:schema id=\"root\" xmlns=\"\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:msdata=\"urn:schemas-microsoft-com:xml-msdata\">\n"
					+ "<xsd:import namespace=\"http://www.w3.org/XML/1998/namespace\" />\n"
					+ "<xsd:element name=\"root\" msdata:IsDataSet=\"true\">\n" + "<xsd:complexType>\n"
					+ "<xsd:choice maxOccurs=\"unbounded\">\n" + "<xsd:element name=\"metadata\">\n"
					+ "<xsd:complexType>\n" + "<xsd:sequence>\n"
					+ "<xsd:element name=\"value\" type=\"xsd:string\" minOccurs=\"0\" />\n" + " </xsd:sequence>\n"
					+ "<xsd:attribute name=\"name\" use=\"required\" type=\"xsd:string\" />\n"
					+ "<xsd:attribute name=\"type\" type=\"xsd:string\" />\n"
					+ "<xsd:attribute name=\"mimetype\" type=\"xsd:string\" />\n"
					+ "<xsd:attribute ref=\"xml:space\" />\n" + "  </xsd:complexType>\n" + "</xsd:element>\n"
					+ "<xsd:element name=\"assembly\">\n" + "<xsd:complexType>\n"
					+ "<xsd:attribute name=\"alias\" type=\"xsd:string\" />\n"
					+ "<xsd:attribute name=\"name\" type=\"xsd:string\" />\n" + " </xsd:complexType>\n"
					+ " </xsd:element>\n" + "<xsd:element name=\"data\">\n" + "<xsd:complexType>\n" + "<xsd:sequence>\n"
					+ "<xsd:element name=\"value\" type=\"xsd:string\" minOccurs=\"0\" msdata:Ordinal=\"1\" />\n"
					+ "<xsd:element name=\"comment\" type=\"xsd:string\" minOccurs=\"0\" msdata:Ordinal=\"2\" />\n"
					+ " </xsd:sequence>\n"
					+ "<xsd:attribute name=\"name\" type=\"xsd:string\" use=\"required\" msdata:Ordinal=\"1\" />\n"
					+ "<xsd:attribute name=\"type\" type=\"xsd:string\" msdata:Ordinal=\"3\" />\n"
					+ "<xsd:attribute name=\"mimetype\" type=\"xsd:string\" msdata:Ordinal=\"4\" />\n"
					+ "<xsd:attribute ref=\"xml:space\" />\n" + " </xsd:complexType>\n" + "</xsd:element>\n"
					+ "<xsd:element name=\"resheader\">\n" + "<xsd:complexType>\n" + "<xsd:sequence>\n"
					+ "<xsd:element name=\"value\" type=\"xsd:string\" minOccurs=\"0\" msdata:Ordinal=\"1\" />\n"
					+ "</xsd:sequence>\n" + "<xsd:attribute name=\"name\" type=\"xsd:string\" use=\"required\" />\n"
					+ " </xsd:complexType>\n" + " </xsd:element>\n" + " </xsd:choice>\n" + "  </xsd:complexType>\n"
					+ " </xsd:element>\n" + "</xsd:schema>\n" + "<resheader name=\"resmimetype\">\n"
					+ "  <value>text/microsoft-resx</value>\n" + " </resheader>\n" + "<resheader name=\"version\">\n"
					+ " <value>2.0</value>\n" + "</resheader>\n" + " <resheader name=\"reader\">\n"
					+ " <value>System.Resources.ResXResourceReader, System.Windows.Forms, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089</value>\n"
					+ "</resheader>\n" + "<resheader name=\"writer\">\n"
					+ " <value>System.Resources.ResXResourceWriter, System.Windows.Forms, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089</value>\n"
					+ "  </resheader>\n");

			System.out.println("\nIterating through " + listFeed.getTotalResults() + " labels...");
			for (ListEntry entry : listFeed.getEntries()) {

				processEachEntry(entry);
			}
			constants.append("\n\n}");
			win8XML.append("</root>");
			win81XML.append("</root>");
			System.out.println("\nProcessed all data...");

		} else {
			// For populating colors

			// #ifndef Header_h
			// #define Header_h
			//
			// #define UIColorFromRGB(rgbValue) [UIColor \
			// colorWithRed:((float)((rgbValue & 0xFF0000) >> 16))/255.0 \
			// green:((float)((rgbValue & 0xFF00) >> 8))/255.0 \
			// blue:((float)(rgbValue & 0xFF))/255.0 alpha:1.0]

			colorsplist.append(
					"#ifndef Header_h\n#define Header_h\n#define UIColorFromRGB(rgbValue) [UIColor colorWithRed:((float)((rgbValue & 0xFF0000) >> 16))/255.0 green:((float)((rgbValue &0xFF00) >> 8))/255.0 blue:((float)(rgbValue & 0xFF))/255.0 alpha:1.0]\n");

			colorsXML.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + "<resources>\n");
			System.out.println("\nIterating through " + listFeed.getTotalResults() + " labels...");
			for (ListEntry entry : listFeed.getEntries()) {

				processEachEntry(entry);
			}
			System.out.println("\nProcessed all data...");
		}

	}

	/**
	 *
	 * @param entry
	 */
	private static void processEachEntry(ListEntry entry) {

		win8XML.append("");
		win81XML.append("");

		String key = null;
		String value = null;
		String dynamic = null;
		String bbValue = null;
		for (String tag : entry.getCustomElements().getTags()) {

			if (tag.equals(KEY_COLUMN_NAME)) {
				key = entry.getCustomElements().getValue(tag);
				// System.out.println("Key: "+key);
			}
			if (Preferences.getValueName().length > 0) {

				if (key != null && Preferences.getValueName()[languagePosition].toLowerCase()
						.equalsIgnoreCase(tag.toLowerCase())) {
					value = entry.getCustomElements().getValue(tag);
					// System.out.println("Value:
					// "+entry.getCustomElements().getValue(tag));
				}

			}

			if (value != null && tag.equals(DYNAMIC_COLUMN_NAME)) {
				dynamic = entry.getCustomElements().getValue(tag);
			}
			if (key != null && tag.equals(BBVALUES_COLUMN_NAME)) {
				bbValue = entry.getCustomElements().getValue(tag);
				;
			}
		}

		if (key != null && key.trim().length() != 0) {
			boolean isdynamic = "Yes".equalsIgnoreCase(dynamic);
			key = key.trim();
			key = key.replace(" ", "");
			if (key.contains(" ")) {
				System.out.println(
						"The following Key contain white-space " + key + "\n\nPress any key to exit the tool..");
				getUserInput();
				System.exit(1);
			}
			if (keyList.contains(key)) {
				duplicateKeys.append(key).append("\n");
			} else
				keyList.add(key);
			if (value != null) {
				value = value.trim();
				if (!isColor) {
					if (Preferences.isConversion().equalsIgnoreCase("YES")) {
						// Replaces &
						value = (value.contains("&")) ? value.replace("&", "&amp;") : value;

						// Replaces '
						if (value.contains("'") && !value.contains("\\'"))
							value = value.replace("'", "\\&#39;");
						// Replaces ""
						if (value.contains("\"") && !value.contains("\\'"))
							value = value.replace("\"", "&#34;");
						// Replaces ©
						if (value.contains("©"))
							value = value.replace("©", "&#169;");
						// Replaces ™
						if (value.contains("™"))
							value = value.replace("™", "&#8482;");
						// Replaces ...
						if (value.contains("..."))
							value = value.replace("...", "&#8230;");
						// Replaces Ⓡ
						if (value.contains("Ⓡ"))
							value = value.replace("Ⓡ", "&#174;");
						// Replaces ₹
						if (value.contains("₹"))
							value = value.replace("₹", "\u20b9");
						// Replace % to %% for Arabic and Spanish
						// if (languageFolder.equalsIgnoreCase("values-ar") ||
						// languageFolder.equalsIgnoreCase("values-es"))
						// value = value.replace("%", "%%");

					} else {
						// Replaces ""
						if (value.contains("\"") && !value.contains("\\'"))
							value = value.replaceAll("\"", Matcher.quoteReplacement("\\\""));
					}
					if (!isdynamic) {
						value = changeSpecial(value);
						if (languageFolder.equalsIgnoreCase("values-ar")
								|| languageFolder.equalsIgnoreCase("values-es"))
							stringsXML.append("<string name=\"").append(key).append("\" formatted=\"").append("false")
									.append("\">").append(value).append("</string>\n");
						else
							stringsXML.append("<string name=\"").append(key).append("\">").append(value)
									.append("</string>\n");
						stringsplist.append(QUOTE).append(key).append(QUOTE).append("=").append(QUOTE).append(value)
								.append(QUOTE).append(";\n");
						
						//#define INFO_ID_SELECT_BANK_WITH_APPSTORE_REDIRECTION @"ELG0044"
						
						macrosList.append("#define ").append(key).append(" ").append("@").append("\"")
						.append(key).append("\"").append("\n");

					} else {
						// configjson.append("{\"key\":\"").append(key)
						// .append("\",\"value\":\"").append(value)
						// .append("\"},\n");
					}

					win8XML.append("<data name=").append(QUOTE).append(key).append(QUOTE).append(" xml:space=")
							.append(QUOTE).append("preserve").append(QUOTE).append(">\n").append("<value>")
							.append(value).append("</value>\n").append("</data>\n");

					win81XML.append("<data name=").append(QUOTE).append(key).append(".Text").append(QUOTE)
							.append(" xml:space=").append(QUOTE).append("preserve").append(QUOTE).append(">\n")
							.append("<value>").append(value).append("</value>\n").append("</data>\n");

					String temp = bbValue != null ? bbValue.trim() : value;
					if (key.length() != 0 && temp != null) {
						labelconfig.append(key).append(" = ").append(temp).append("\n");
					}
					if (key.length() != 0) {
						constants.append("String ").append(key).append("=").append(QUOTE).append(key).append(QUOTE)
								.append(";\n");
					}
				}

				else {
					// Color update
					value = changeSpecial(value);
					colorsXML.append("<color name=\"").append(key).append("\">").append(value).append("</color>\n");
					// #define PLACEHOLDER_COLOR UIColorFromRGB(0x777777)
					colorsplist.append("#define ").append(key).append(" ").append("UIColorFromRGB(0x")
							.append(value.replace("#", "")).append(")").append("\n");

				}
				addedNewLine = false;
			} else if (!addedNewLine) {
				addedNewLine = true;
				stringsXML.append("\n");
				colorsXML.append("\n");
				// configjson.append("\n");
				stringsplist.append("\n");
				macrosList.append("\n");
				colorsplist.append("\n");
				constants.append("\n");
				labelconfig.append("\n");
				win8XML.append("\n");
				win81XML.append("\n");
			}
		}
	}

	/**
	 *
	 * @param value
	 * @return String
	 */
	private static String changeSpecial(String value) {
		for (String key : stringsXMLMap.keySet()) {

			value = value.replace(key, stringsXMLMap.get(key));
		}

		return value;
	}

	/**
	 * Get user Inputs
	 */
	private static void getUserInput() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			reader.readLine();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	/**
	 *
	 * @throws IOException
	 */
	public static void writeToFile() throws IOException {
		// OS Type Android
		System.out.println("COLOR++++++++++++++++++++++++++++++++++++++++++++++++++++"+isColor);
		if (Preferences.getOsType().toLowerCase().equalsIgnoreCase("Android")) {
			if (!isColor) {
				stringsXML.append("\n\n</resources>");
				writeToFile(STRINGS_XML_PATH, stringsXML.toString());
				System.out.println("\nProcessed file " + STRINGS_XML_PATH);
			} else {
				colorsXML.append("\n\n</resources>");
				writeToFile(COLORS_XML_PATH, colorsXML.toString());
				System.out.println("\nProcessed file " + COLORS_XML_PATH);
			}

			// OS Type iOS
		} else if (Preferences.getOsType().toLowerCase().equalsIgnoreCase("iOS")) {
			// writeToFile(CONFIG_JSON_PATH, configjson.toString());
			// System.out.println("\nProcessed file " + CONFIG_JSON_PATH);
			if (!isColor) {
				writeToFile(STRINGS_PLIST_PATH, stringsplist.toString());
				writeToFile(MACROS_PATH, macrosList.toString());
				System.out.println("\nProcessed file " + STRINGS_PLIST_PATH);
			} else {
				writeToFile(COLORS_PLIST_PATH, colorsplist.toString());
				System.out.println("\nProcessed file " + COLORS_PLIST_PATH);
			}
		} else {
			// // Android Path
			// stringsXML.append("\n\n</resources>");
			// writeToFile(STRINGS_XML_PATH, stringsXML.toString());
			// System.out.println("\nProcessed file " + STRINGS_XML_PATH);
			// // iOS Path
			// writeToFile(STRINGS_PLIST_PATH, stringsplist.toString());
			// System.out.println("\nProcessed file " + STRINGS_PLIST_PATH);
			//
			if (!isColor) {
				stringsXML.append("\n\n</resources>");
				writeToFile(STRINGS_XML_PATH, stringsXML.toString());
				System.out.println("\nProcessed file " + STRINGS_XML_PATH);
				// iOS Path
				writeToFile(STRINGS_PLIST_PATH, stringsplist.toString());
				writeToFile(MACROS_PATH, macrosList.toString());
				System.out.println("\nProcessed file " + STRINGS_PLIST_PATH);
			} else {
				colorsXML.append("\n\n</resources>");
				writeToFile(COLORS_XML_PATH, colorsXML.toString());
				System.out.println("\nProcessed file " + COLORS_XML_PATH);

				writeToFile(COLORS_PLIST_PATH, colorsplist.toString());
				System.out.println("\nProcessed file " + COLORS_PLIST_PATH);
			}
		}
		if (!isColor) {
			writeToFile(CONSTANTS_PATH, constants.toString());
			System.out.println("\nProcessed file " + CONSTANTS_PATH);
			writeToFile(LABEL_PROPS_PATH, labelconfig.toString());
			System.out.println("\nProcessed file " + LABEL_PROPS_PATH);
			writeToFile(WINDOWS8_RES_PATH, win8XML.toString());
			System.out.println("\nProcessed file " + WINDOWS8_RES_PATH);

			writeToFile(WINDOWS81_RES_PATH, win81XML.toString());
			System.out.println("\nProcessed file " + WINDOWS81_RES_PATH);

		}
		if (duplicateKeys.length() > 0) {
			System.out.println("\nWARNING  DUPLICATE KEYS FOUND :\n" + duplicateKeys);
		}
	}

	/**
	 *
	 * @param filePath
	 * @param contents
	 * @throws IOException
	 */
	private static void writeToFile(String filePath, String contents) throws IOException {
		FileWriter pw = new FileWriter(filePath);
		pw.write(contents);
		pw.flush();
		pw.close();
	}
}