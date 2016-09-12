package com.bluecoatcloud.threatpulse.tests;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.testng.Reporter;

import com.bluecoatcloud.threatpulse.constants.BLueCoatConstants;
import com.bluecoatcloud.threatpulse.utils.RestTestClient;
import com.gargoylesoftware.htmlunit.WebClient;
import com.bluecoatcloud.threatpulse.clients.AbstractBlueCoatTest;
import com.bluecoatcloud.threatpulse.clients.BlueCoatTestBase;

public class OpenBrowserApp {

	protected  Map<String,String> mapCookie = new HashMap<String,String>();
	String cookie;
	protected RestTestClient restClient  = new RestTestClient();
	protected String bluecoatAPIPayload;
	HttpResponse apiResponse;
	WebDriver driver;
	URL url;
	HttpURLConnection connection;

	@SuppressWarnings("static-access")
	public  HttpResponse browser(String username, String password) throws Exception {
		
		//  Wait For Page To Load
		// Put a Implicit wait, this means that any search for elements on the page
		//could take the time the implicit wait is set for before throwing exception 
		//driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		
		final String authUser = username;
		final String authPassword = password;
		Authenticator.setDefault(
				new Authenticator() {
					public PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(
								authUser, authPassword.toCharArray());
					}
				}
				);

		try {
			url = new URL("https://portal.qa3.bluecoatcloud.com/login.jsp");
			connection = (java.net.HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setFollowRedirects(true);

			System.getProperties().put("http.proxyHost", "199.116.170.172");
			System.getProperties().put("http.proxyPort", "8080");
			System.setProperty("http.proxyUser", authUser);
			System.setProperty("http.proxyPassword", authPassword);
			System.getProperties().put("http.proxySet", "true");
			
			Map<String, List<String>> headerFieldsMap = connection.getHeaderFields();
			System.out.println("Printing All Response Header for URL: " + connection.toString());
			
			for (Map.Entry<String, List<String>> entry : headerFieldsMap.entrySet()) {
				System.out.println(entry.getKey() + " : " + entry.getValue());
			}
			
			System.out.println("\nGet Response Header By Key ...\n");
			List<String> contentLength = headerFieldsMap.get("X-CSRF-Token");
			
			if (contentLength == null) {
				System.out.println("'Content-Length' doesn't present in Header!");
			} else {
				for (String header : contentLength) {
					System.out.println("X-CSRF-Token: " + header);
				}
			}
			



			/*driver= new HtmlUnitDriver() {
				protected WebClient modifyWebClient(final WebClient client) {
					try {
						client.setUseInsecureSSL(true);
					} catch (GeneralSecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return client;
				}
			};*/
			
			// Create a new instance of the Firefox driver
			 driver = new FirefoxDriver();

			InputStream inStream1 = null;
			String jdataPath = System.getProperty("user.dir") +
					org.apache.commons.io.FilenameUtils.separatorsToSystem(BLueCoatConstants.BLUECOATCLOUD_DATA_LOCATION);

			if(System.getProperty(BLueCoatConstants.KEY_JOB_SERVICE_API_PAYLOAD) == null){
				String filepath1 = jdataPath + BLueCoatConstants.KEY_JOB_SERVICE_API_PAYLOAD;
				inStream1 = new FileInputStream(filepath1);
				bluecoatAPIPayload = IOUtils.toString(inStream1);
			}

			// Navigate to URL
			driver.get("https://portal.qa3.bluecoatcloud.com/login.jsp"); // Maximize the window.
			driver.manage().window().maximize();

			WebElement element1 = driver.findElement(By.xpath("//input[@name='username']"));

			//Enter user name
			element1.sendKeys(username);

			WebElement element2 = driver.findElement(By.xpath("//input[@name='password']")); 

			// Enter password
			element2.sendKeys(password);

			WebElement element3 =  driver.findElement(By.xpath("//a [@id='button-1037']")); 

			element3.click();

			Set<org.openqa.selenium.Cookie> allCookies = driver.manage().getCookies();

			for (org.openqa.selenium.Cookie loadedCookie : allCookies) {
				System.out.println(loadedCookie.getName()+":"+ loadedCookie.getValue());
				if (loadedCookie.getName().equalsIgnoreCase("JSESSIONID")){
					cookie = "JSESSIONID="+ loadedCookie.getValue();
				}
				//cookie = "JSESSIONID="+loadedCookie.getValue();
				mapCookie.put(loadedCookie.getName(), cookie);
				System.out.println( "cookie: " +  cookie);
				//return cookie;
				/*}*/
			}
			
			System.out.println(driver.manage().logs().toString());
			

			apiResponse = blueCoatCloud_DirectProvider(restClient, bluecoatAPIPayload, cookie);
		} catch (Exception e) {

			e.printStackTrace();
		}

		finally{
			//Close the browser
			driver.manage().deleteAllCookies();
			driver.quit();
		}

		return apiResponse;
	}

	@SuppressWarnings("static-access")
	public HttpResponse blueCoatCloud_DirectProvider(RestTestClient restClient, String payLoad, String cookieId) throws Exception {

		ArrayList<NameValuePair> headers = BlueCoatTestBase
				.getHeader(new BasicNameValuePair("content-type",
						"application/json"));
		headers.add(new BasicNameValuePair("Accept", "application/json"));
		headers.add(new BasicNameValuePair("Authorization", "Basic UmFnaHVuYW5kYW4uZGl4aXRAaXRjaW5mb3RlY2guY29tOkl0Y2luZm90ZWNoQDEyMw=="));
		headers.add(new BasicNameValuePair("Referer", "https://portal.qa3.bluecoatcloud.com/"));
		headers.add(new BasicNameValuePair("Host", "portal.qa3.bluecoatcloud.com"));

		headers.add(new BasicNameValuePair("X-CSRF-Token", "qe1fm7g96vhookoo6759v8qn4g"));
		headers.add(new BasicNameValuePair("Cookie", cookieId));

		final String authUser = BLueCoatConstants.USERNAME;
		final String authPassword = BLueCoatConstants.PASSWORD;
		Authenticator.setDefault(
				new Authenticator() {
					public PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(
								authUser, authPassword.toCharArray());
					}
				}
				);

		URL url;
		HttpURLConnection connection;
		url = new URL("https://portal.qa3.bluecoatcloud.com/login.jsp");
		connection = (java.net.HttpURLConnection)url.openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		connection.setFollowRedirects(true);

		System.getProperties().put("http.proxyHost", "199.116.170.172");
		System.getProperties().put("http.proxyPort", "8080");
		System.setProperty("http.proxyUser", authUser);
		System.setProperty("http.proxyPassword", authPassword);
		System.getProperties().put("http.proxySet", "true");



		URI uri = new URI("https://portal.qa3.bluecoatcloud.com/djn/directprovider");

		String msg = "djn API reuest"; 
		System.out.println(" Request: "+uri.toString());
		Reporter.log(msg);
		Reporter.log("POST Request: "+uri.toString());

		HttpEntity entity = new StringEntity(payLoad);

		return restClient.executeRequest(RestTestClient.HttpMethodType.POST, uri, headers, entity, null, 
				null, null, null, null, true);
	}

	public static void main(String[] args) throws Exception {

		OpenBrowserApp test = new OpenBrowserApp();
		test.browser("Raghunandan.dixit@itcinfotech.com", "Itcinfotech@123");
		//String getResponseBody = getResponseBody(apiResponse);

	}

}
