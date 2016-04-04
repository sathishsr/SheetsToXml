package com.sathishSR.sheetsToXml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.gdata.util.ServiceException;

/**
 * @author sathishsr
 *
 */
public class LableGenerator {
	// Retrieve the CLIENT_ID and CLIENT_SECRET from an APIs Console project:
	// https://code.google.com/apis/console
	static String CLIENT_ID = "1066872312302-sjpicrp9loglgavrg5l7r1u6pq115d8v.apps.googleusercontent.com";
	static String CLIENT_SECRET = "vqlHRn73LIC3j6vqmE_XpHK-";
	// Change the REDIRECT_URI value to your registered redirect URI for web
	// applications.
	static String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
	// Add other requested scopes.
	static List<String> SCOPES = Arrays.asList("https://spreadsheets.google.com/feeds");
	// Set Access Type
	static String ACCESS_TYPE = "offline";
	// Set Approval Prompt
	static String APPROVAL_PROMPT = "force";

	static String RESPONSE_TOKEN;

	/**
	 * main function to execute the LableGenerator.java class
	 * 
	 * @param args
	 * @throws IOException
	 * @throws ServiceException
	 */
	public static void main(String args[]) throws IOException, ServiceException {

		Credential credential = getCredentials();
		SpreadSheetToXml.printDocuments(credential);
		SpreadSheetToXml.writeToFile();
		System.out.println("\nLabel Generated successfully. Press any key to close this window");
		// LabelGenerator.getUserInput();
	}

	/**
	 * Retrieve OAuth 2.0 credentials.
	 * 
	 * @return OAuth 2.0 Credential instance.
	 */
	static Credential getCredentials() throws IOException {
		HttpTransport transport = new NetHttpTransport();
		JacksonFactory jsonFactory = new JacksonFactory();

		// Step 1: Authorize -->
		String authorizationUrl = new GoogleAuthorizationCodeRequestUrl(CLIENT_ID, REDIRECT_URI, SCOPES)
				.setAccessType(ACCESS_TYPE).setApprovalPrompt(APPROVAL_PROMPT).build();

		// Point or redirect your user to the authorizationUrl.
		System.out.println("Go to the following link in your browser:");
		System.out.println(authorizationUrl);

		// Read the authorization code from the standard input stream.
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Authorizing...");
		String code = "4/COKUJ8z7FoO53V7pt2JTkK8f13FAKWqrKNmWd5HJCVA";// in.readLine();
		// End of Step 1 <--

		//Load the config files.
		Preferences.loadConfig();

		if (Preferences.getToken()!=null) {

			System.out.println("Authorization success.");
			RESPONSE_TOKEN =Preferences.getToken();
		} else {
			System.out.println("Authorization Failed.");
			System.out.println("Creating new Access Token...");
			GoogleTokenResponse response = new GoogleAuthorizationCodeTokenRequest(transport, jsonFactory, CLIENT_ID,
					CLIENT_SECRET, code, REDIRECT_URI).execute();
			RESPONSE_TOKEN = response.getRefreshToken();
			
			Preferences.saveProperties(response);
			System.out.println("Success");
		}
		// Step 2: Exchange -->

		// End of Step 2 <--

		// Build a new GoogleCredential instance and return it.
		return new GoogleCredential.Builder().setClientSecrets(CLIENT_ID, CLIENT_SECRET).setJsonFactory(jsonFactory)
				.setTransport(transport).build()
				/* .setAccessToken(response.getAccessToken()) */ .setRefreshToken(RESPONSE_TOKEN);

	}

}
