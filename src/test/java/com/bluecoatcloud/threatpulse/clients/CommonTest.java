package com.bluecoatcloud.threatpulse.clients;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;










import com.bluecoatcloud.threatpulse.constants.CommonConstants;
import com.bluecoatcloud.threatpulse.dataProvider.TestEnvironment;
import com.bluecoatcloud.threatpulse.helpers.DataHelper;
//import com.aptimus.altcloud.reporting.PerformanceData;
import com.bluecoatcloud.threatpulse.utils.EvaluateXPath;
import com.bluecoatcloud.threatpulse.utils.JsonToXMLMapper;
//import edu.apollogrp.utils.OAuthHandler;
//import edu.apollogrp.utils.Utility;
//import org.json.JSONObject;
//import org.openqa.selenium.firefox.FirefoxDriver;
//import org.testng.Assert;
import com.gargoylesoftware.htmlunit.WebClient;

public abstract class CommonTest {

	protected static EvaluateXPath xPath;
	private JsonToXMLMapper jsonToXML;
	private String dataExchangeFormat = "json";
	//private static ArrayList<NameValuePair> httpHeaders;
	public enum AuthType {OAUTH,  APOLLOASSERTIONS, OAUTHANDAPOLLOASSERTIONS};		
	public enum RestAPI {PUT, GET, POST, DELETE};
	protected enum Role {STUDENT, FACULTY, ADMIN, IDD};
	protected static AuthType authType;
	//protected static OAuthHandler oauthHandler;
	protected static Properties prop;
	public List<NameValuePair> tenantHeaders;
	protected  Map<String,String> mapCookie  = new HashMap<String,String>();
	String cookie;
	String X_CSRF_Token;


	//Headers
	private String connectionHeader;
	private String acceptHeader;
	private String contentTypeHeader;
	private String securityXTest;
	private String acceptCharset;
	//protected String cookie;

	//ApolloAsserions cookie
	private String authCookie;
	private String host;
	private int port;
	private String seleniumServer;
	private int seleniumPort;


	public static String testEnv;
	private String scheme;
	private String fragment;
	private List<NameValuePair> headers;
	protected List<NameValuePair> matrixParams;
	//private org.apache.http.HttpEntity requestEntity;

	private String baseVersion;
	private String messageApiNewVersion;

	private String tenantId;
	private String baseContextName;
	private String consumerKey;
	private String secret;

	protected int activityTestSleepIntervalMS;

	//Performance reporter related
	protected boolean isDebugOn;
	private boolean isPerfLoggingOn;
	com.bluecoatcloud.threatpulse.reporting.Reporter perfReporter;

	//Cache Testing
	protected boolean verifyCache = false;

	/**
	 * 
	 * @throws IOException
	 */
	public CommonTest(){

	}

	/**
	 * Builds the header with the information provided in testsuite file
	 * This adds 
	 * <uL>
	 * <li>ConnectionHeader
	 * <li>Connection
	 * <li>Content-type
	 * <li>Accept
	 * <li>x-test
	 * </ul>
	 * 
	 * 
	 */
	public  List<NameValuePair> buildHeaders(){

		//retrieve the header information from the configuration file
		connectionHeader = prop.getProperty(CommonConstants.KEY_HEADER_CONNECTION);
		//acceptHeader = prop.getProperty(CommonConstants.KEY_HEADER_ACCEPT);
		//contentTypeHeader = prop.getProperty(CommonConstants.KEY_HEADER_CONTENT_TYPE);

		//adding Authentication header
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair(CommonConstants.HEADER_CONNECTION, connectionHeader));
		headers.add(new BasicNameValuePair(CommonConstants.HEADER_CONTENT_TYPE, contentTypeHeader));
		headers.add(new BasicNameValuePair(CommonConstants.HEADER_ACCEPT, acceptHeader));
		headers.add(new BasicNameValuePair(CommonConstants.HEADER_SECURITY_XTEST, securityXTest));
		headers.add(new BasicNameValuePair(CommonConstants.HEADER_ACCEPT_CHARSET, acceptCharset));

		return headers;
	}


	/**
	 * clear headers
	 */
	public void clearHeader() {
		headers.clear();
	}

	/**
	 * Overwrites the headers provided in the given theHeaders
	 *	This wil overwrite teh following 
	 * <uL>
	 * <li>Connection
	 * <li>Content-type
	 * <li>Accept
	 * <li>x-test
	 * </ul>
	 * 
	 */
	public  List<NameValuePair> buildHeaders(Map <String, String> theHeaders){

		Set<Entry<String,String>> headersEntrySet = theHeaders.entrySet();	
		Iterator<Entry<String,String>> iter =  headersEntrySet.iterator();
		List<NameValuePair> retHeaders;
		if (headers != null){
			retHeaders = getHeaders();
		} else 
			retHeaders =  new ArrayList<NameValuePair>();

		while (iter.hasNext()){
			Entry<String, String> anEntry =  iter.next();
			if (anEntry.getKey().equalsIgnoreCase(CommonConstants.HEADER_CONNECTION)){
				removeKeyValue(CommonConstants.HEADER_CONNECTION, retHeaders);
				retHeaders.add(new BasicNameValuePair(CommonConstants.HEADER_CONNECTION, anEntry.getValue()));
			}else if (anEntry.getKey().equalsIgnoreCase(CommonConstants.HEADER_CONTENT_TYPE)){
				removeKeyValue(CommonConstants.HEADER_CONTENT_TYPE, retHeaders);
				retHeaders.add(new BasicNameValuePair(CommonConstants.HEADER_CONTENT_TYPE, anEntry.getValue()));
			}else if (anEntry.getKey().equalsIgnoreCase(CommonConstants.HEADER_ACCEPT)){
				removeKeyValue(CommonConstants.HEADER_ACCEPT, retHeaders);
				retHeaders.add(new BasicNameValuePair(CommonConstants.HEADER_ACCEPT, anEntry.getValue()));
			} else if (anEntry.getKey().equalsIgnoreCase(CommonConstants.HEADER_SECURITY_XTEST)){
				removeKeyValue(CommonConstants.HEADER_SECURITY_XTEST, retHeaders);
				retHeaders.add(new BasicNameValuePair(CommonConstants.HEADER_SECURITY_XTEST, anEntry.getValue()));
			} else if (anEntry.getKey().equalsIgnoreCase(CommonConstants.HEADER_ACCEPT_CHARSET)){
				removeKeyValue(CommonConstants.HEADER_ACCEPT_CHARSET, retHeaders);
				retHeaders.add(new BasicNameValuePair(CommonConstants.HEADER_ACCEPT_CHARSET, anEntry.getValue()));
			}

		}
		return retHeaders;
	}


	/**
	 * This is an overloaded api that builds the auth header given the auth type, username, password, url and the test method type 
	 * @param theUri
	 * @param theRestMethodType
	 * @param theUserName
	 * @param thePassword
	 * @param isExpired
	 * @param additionalParameters
	 * @param postParameters
	 * @throws MalformedURLException
	 * @throws Exception
	 */
	/*public void buildAuth(AuthType authType, URI theUri,  RestAPI theRestMethodType, String theUserName, String thePassword, boolean isExpired,  Map<String,String> additionalParameters, Map<String,String> postParameters) throws MalformedURLException, Exception{
		String authHeader  = null;
		String xRequestedWith = null;

		switch (authType){
		case APOLLOASSERTIONS:
			authCookie =  getCookie(theUserName, thePassword);
			if (isDebugOn){
				Reporter.log(CommonConstants.HEADER_COOKIE + ": " + authCookie, true);
			}
			removeKeyValue(CommonConstants.HEADER_COOKIE, headers);

			if (authCookie != null){
				xRequestedWith = authCookie.replace("APOLLOASSERTION=", "");
				headers.add(new BasicNameValuePair(CommonConstants.HEADER_COOKIE, authCookie));
				headers.add(new BasicNameValuePair(CommonConstants.HEADER_X_REQUESTED_WITH, xRequestedWith));
			}
			break;
		case OAUTH:
			removeKeyValue(CommonConstants.HEADER_COOKIE, headers);
			authHeader = oauthHandler.getAuthHeader(this, theUri.toURL(), theRestMethodType, additionalParameters, postParameters);
			//authHeader = oauthHandler.getAuthHeader(this, theUri.toURL(), theRestMethodType, "SyllabusKey", "devsecretSyllabusKey1234", additionalParameters, postParameters);
			removeKeyValue(CommonConstants.HEADER_AUTHORIZATION, headers);
			if (authHeader!=null){
				headers.add(new BasicNameValuePair(CommonConstants.HEADER_AUTHORIZATION, authHeader));
			}
			if (isDebugOn){
				Reporter.log(CommonConstants.HEADER_AUTHORIZATION + ": " + authHeader, true);
			}
			break;
		case OAUTHANDAPOLLOASSERTIONS:
			authHeader = oauthHandler.getAuthHeader(this, theUri.toURL(), theRestMethodType, additionalParameters, postParameters);
			removeKeyValue(CommonConstants.HEADER_AUTHORIZATION, headers);
			if (authHeader!=null){
				headers.add(new BasicNameValuePair(CommonConstants.HEADER_AUTHORIZATION, authHeader));
			}
			if (isDebugOn){
				Reporter.log(CommonConstants.HEADER_AUTHORIZATION + ": " + authHeader, true);
			}
			//add apolloasserions also
			if (isExpired){
				authCookie =  getCookie(theUserName, thePassword);
			}  
			removeKeyValue(CommonConstants.HEADER_COOKIE, headers);
			if (authCookie != null){
				headers.add(new BasicNameValuePair(CommonConstants.HEADER_COOKIE, authCookie));
				xRequestedWith = authCookie.replace("APOLLOASSERTION=", "");
				headers.add(new BasicNameValuePair(CommonConstants.HEADER_X_REQUESTED_WITH, xRequestedWith));
			}
			if (isDebugOn){
				Reporter.log(CommonConstants.HEADER_COOKIE + ": " + authCookie, true);
			}
			break;
		}

	}*/
	/**
	 * 	
	 * @param theKey
	 * @param theData
	 */
	protected void removeKeyValue(String theKey, List<NameValuePair> theData){
		if (theData != null){
			Iterator<NameValuePair> iter = theData.iterator();
			while (iter.hasNext()){
				NameValuePair data = (NameValuePair)iter.next();
				if (data.getName().equalsIgnoreCase(theKey)){
					iter.remove();
				}
			}
		}
	}


	/**
	 * 
	 * @thro?aExchangeFormat;
		httpHeaders.add(new BasicNameValuePair("Accept", "application/"+dataExchangeFormat));
	}

	/**
	 * 
	 * @param <T>
	 * @param json
	 * @param klass
	 * @return
	 */
	protected static <T> T unmarshallJSON(final String json, final Class<T> klass) {
		final ObjectMapper mapper = new ObjectMapper();
		final AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
		// make deserializer use JAXB annotations (only)
		mapper.getDeserializationConfig().setAnnotationIntrospector(
				introspector);
		// make serializer use JAXB annotations (only)
		mapper.getSerializationConfig().setAnnotationIntrospector(introspector);
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		//mapper.getDeserializationConfig().set(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(SerializationConfig.Feature.WRITE_NULL_PROPERTIES, false);
		try {
			return mapper.readValue(json, klass);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 
	 * @param object
	 * @return
	 * @throws IOException
	 */
	public static String marshallJSON(Object object) throws IOException {

		ObjectMapper mapper = new ObjectMapper();
		AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
		//mapper.enableDefaultTyping();
		// make deserializer use JAXB annotations (only)
		mapper.getDeserializationConfig().setAnnotationIntrospector(introspector);
		// make serializer use JAXB annotations (only)
		mapper.getSerializationConfig().setAnnotationIntrospector(introspector);


		StringWriter writer = new StringWriter();

		mapper.writeValue(writer, object);

		return writer.toString();
	}


	/**
	 * 
	 * @param <T>
	 * @param <T>
	 * @param xml
	 * @param klass
	 * @return
	 * @throws JAXBException 
	 */
	protected static <T> Object unmarshallXML(final String xml, final Class<T> klass) throws JAXBException {
		StringBuffer xmlStr = new StringBuffer(xml);
		String packageName = klass.getPackage().getName();
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance (packageName);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new StringReader(xmlStr.toString())));
		} catch (JAXBException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 
	 * @param <T>
	 * @param is
	 * @param klass
	 * @return
	 * @throws Exception
	 */
	public static <T> T unmarshallJSON(InputStream is, Class<T> klass)
			throws Exception {
		try {

			ObjectMapper objectMapper = new ObjectMapper();
			AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
			objectMapper.getDeserializationConfig().setAnnotationIntrospector(
					introspector);
			objectMapper.getSerializationConfig().setAnnotationIntrospector(
					introspector);
			objectMapper.getDeserializationConfig()
			.set(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			objectMapper.configure(SerializationConfig.Feature.WRITE_NULL_PROPERTIES, false);
			return objectMapper.readValue(is, klass);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 
	 * @param object
	 * @return
	 * @throws JAXBException 
	 */
	protected String marshallXML(final Object object) throws JAXBException {
		StringWriter writer = new StringWriter();
		String packageName = object.getClass().getPackage().getName();
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(packageName);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(new JAXBElement(new QName(object.getClass().getName()), object.getClass(), object), writer);
			return writer.toString();
		} catch (JAXBException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 
	 * @param <T>
	 * @param data
	 * @param klass
	 * @return
	 * @throws JAXBException 
	 */
	public <T> T unmarshall(String data, final Class<T> klass) throws JAXBException {
		if(dataExchangeFormat.equals("xml"))
			return (T) unmarshallXML(data, klass);
		// Add more data exchange formats
		return unmarshallJSON(data, klass);		
	}

	/**
	 * 
	 * @param object
	 * @return
	 * @throws JAXBException 
	 */
	public String marshall(final Object object) throws JAXBException {
		if(dataExchangeFormat.equals("xml"))
			return marshallXML(object);
		try {
			return marshallJSON(object);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Map<String, Object> getJsonHashMapFromHttpResponse(HttpResponse response,
			Class filterView, Class objectClass) throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.DEFAULT_VIEW_INCLUSION,
				false);
		mapper.getSerializationConfig().setSerializationView(filterView);
		return  mapper.readValue(response.getEntity().getContent(),
				new TypeReference<Map<String, Object>>() {
		});
		//		return mapper.readValue(response.getEntity().getContent(),
		//				new TypeReference<List>() {
		//				});
	}

	/**
	 * 
	 * @param response
	 * @return
	 */
	public static String getResponseBody(HttpResponse response) {
		try {
			return EntityUtils.toString(response.getEntity());

		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 
	 * @param response
	 * @return
	 */
	public int getResponseStatusCode(HttpResponse response) {
		return response.getStatusLine().getStatusCode();

	}

	
	@SuppressWarnings("static-access")
	public String getCookie(String username, String password)  {

		// Create a new instance of the Firefox driver
		WebDriver driver = new FirefoxDriver();

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

		URL url;
		HttpURLConnection connection;
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
				//System.out.println(loadedCookie.getName()+":"+ loadedCookie.getValue());
				if (loadedCookie.getName().equalsIgnoreCase("JSESSIONID")){
					cookie = "JSESSIONID="+ loadedCookie.getValue();
					mapCookie.put(username, cookie);
					//System.out.println( "cookie: " +  cookie);
					//return cookie;
				}
				/*if (loadedCookie.getName().equalsIgnoreCase("X-CSRF-Token")){
					X_CSRF_Token = ":"+ loadedCookie.getValue();
					mapCookie.put("X_CSRF_Token_Key", X_CSRF_Token);
					System.out.println( "X_CSRF_Token: " +  X_CSRF_Token);
					//return cookie;
				}*/
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		finally{
			//Close the browser
			driver.manage().deleteAllCookies();
			driver.quit();
		}

		return cookie;
	}



	@BeforeClass(alwaysRun = true)
	@Parameters({"testEnv", "applicationRequestContentType", "applicationResponseContentType", "authType", "seleniumServer", "seleniumPort", 
		"tenantId", "baseVersion", "messageApiNewVersion", "securityXTestHeader","activityTestSleepIntervalMS","baseContextName", "acceptCharset", "debug", "perfLog", "verifyCache", "consumerKey", "secret"})
	public  void initialize (@Optional("") String theTestEnv, @Optional("")String theAppRequestContentType, @Optional("")String theApplicationResponseContentType,@Optional("OAUTH")String theAuthType, @Optional("") String theSeleniumServer,
			@Optional("") String theSeleniumPort, @Optional() String theTenantId, @Optional("") String theBaseVersion,@Optional("") String theMessageApiNewVersion, @Optional("") String theSecurityXTestHeader,
			@Optional("")String theActivityTestSleepIntervalMS, @Optional("")String theBaseContextName, @Optional("")String theAcceptCharset,@Optional("")String theDebug, @Optional("")String thePerfLog, @Optional("") boolean verifyCache, @Optional("") String theConsumerKey, @Optional("") String theSecret) throws IOException{

		prop = DataHelper.getProperties();
		mapCookie=new HashMap<String, String>();
		testEnv = theTestEnv;
		if (testEnv != null){
			if (testEnv.equalsIgnoreCase(CommonConstants.TEST_ENVIRONMENT_QA_BLUECOAT)){
				host = prop.getProperty(CommonConstants.KEY_HOST_QA_BLUECOAT);
				if (prop.getProperty(CommonConstants.KEY_PORT_QA_BLUECOAT) != null && prop.getProperty(CommonConstants.KEY_PORT_QA_BLUECOAT).length() > 0) {
					port = Integer.parseInt(prop.getProperty(CommonConstants.KEY_PORT_QA_BLUECOAT));
				}
				scheme = prop.getProperty(CommonConstants.KEY_NETWORK_PROTOCOL_QA_BLUECOAT);

			}
			/*else if (testEnv.equalsIgnoreCase(CommonConstants.TEST_ENVIRONMENT_DEV_BLUECOAT)){
				host = prop.getProperty(CommonConstants.KEY_HOST_QA_BLUECOAT);
				if (prop.getProperty(CommonConstants.KEY_PORT_QA_BLUECOAT) != null && prop.getProperty(CommonConstants.KEY_PORT_QA_BLUECOAT).length() > 0) {
					port = Integer.parseInt(prop.getProperty(CommonConstants.KEY_PORT_QA_BLUECOAT));
				}
				scheme = prop.getProperty(CommonConstants.KEY_NETWORK_PROTOCOL_QA_BLUECOAT);
			}*/
			
		}

		xPath = new EvaluateXPath();
		//httpHeaders = new ArrayList<NameValuePair>();
		matrixParams = new ArrayList<NameValuePair>();
		//oauthHandler =  new OAuthHandler();
		authType = AuthType.valueOf(theAuthType);
		seleniumServer = theSeleniumServer;
		tenantId = theTenantId;
		consumerKey = theConsumerKey;
		secret = theSecret;

		if (theSeleniumPort != null && !theSeleniumPort.isEmpty()){
			seleniumPort = Integer.parseInt(theSeleniumPort);
		}
		contentTypeHeader = theAppRequestContentType;
		acceptHeader = theApplicationResponseContentType;
		if (acceptHeader.equalsIgnoreCase(CommonConstants.HEADER_ACCEPT_AAPLICATION_XML))
			dataExchangeFormat = "xml";
		else if (acceptHeader.equalsIgnoreCase(CommonConstants.HEADER_ACCEPT_AAPLICATION_JSON))
			dataExchangeFormat = "json";
		securityXTest = theSecurityXTestHeader;
		acceptCharset = theAcceptCharset;
		headers = buildHeaders();
		//utf-8
		baseVersion = theBaseVersion;
		messageApiNewVersion=theMessageApiNewVersion;
		baseContextName = theBaseContextName;

		if(theActivityTestSleepIntervalMS == null || theActivityTestSleepIntervalMS.equals("")){
			theActivityTestSleepIntervalMS = "0";
		}
		activityTestSleepIntervalMS = Integer.parseInt(theActivityTestSleepIntervalMS);

		//set the perflogging on or off
		if (thePerfLog != null && !thePerfLog.isEmpty()){
			isPerfLoggingOn =  Boolean.valueOf(thePerfLog);
		}else {
			isPerfLoggingOn = Boolean.valueOf(prop.getProperty(CommonConstants.PERF_LOGGING_ON));
		}
		//set the debug logging on or off
		if (theDebug != null && !theDebug.isEmpty()){
			isDebugOn = Boolean.valueOf(theDebug);
		}
		else {
			isDebugOn = Boolean.valueOf(prop.getProperty(CommonConstants.DEBUG_ON));
		}
		//initialize the reporter
		TestEnvironment testEnvironment = TestEnvironment.getTestEnvironmentObject();
		String klassName = this.getClass().getName();
		if (thePerfLog != null && !thePerfLog.isEmpty()){
			perfReporter = new com.bluecoatcloud.threatpulse.reporting.Reporter(klassName.substring(this.getClass().getName().lastIndexOf(".")+1, klassName.length()));
			//testEnvironment.setReporter(perfReporter);;
			testEnvironment.setDebugLoggingOn(isDebugOn);
			testEnvironment.setPerformanceLoggingOn(isPerfLoggingOn);
		}

		//Cache Testing
		this.verifyCache = verifyCache;
	}


	/**
	 * 
	 * @return
	 */
	public JsonToXMLMapper getJsonToXML() {
		return jsonToXML;
	}

	/**
	 * 
	 * @param jsonToXML
	 */
	public void setJsonToXML(JsonToXMLMapper theJsonToXML) {
		this.jsonToXML = theJsonToXML;
	}

	/**
	 * 
	 * @return
	 */
	public String getDataExchangeFormat() {
		return dataExchangeFormat;
	}

	/**
	 * 
	 * @param theDataExchangeFormat
	 */
	public void setDataExchangeFormat(String theDataExchangeFormat) {
		this.dataExchangeFormat = theDataExchangeFormat;
	}

	/**
	 * 
	 * @return
	 */
	public String getConnectionHeader() {
		return connectionHeader;
	}

	public void setConnectionHeader(String theConnectionHeader) {
		this.connectionHeader = theConnectionHeader;
	}

	/**
	 * 
	 * @return
	 */
	public String getAcceptHeader() {
		return acceptHeader;
	}

	/**
	 * 
	 * @param theAcceptHeader
	 */
	public void setAcceptHeader(String theAcceptHeader) {
		this.acceptHeader = theAcceptHeader;
	}

	/**
	 * 
	 * @return
	 */
	public String getContentTypeHeader() {
		return contentTypeHeader;
	}

	/**
	 * 
	 * @param contentTypeHeader
	 */
	public void setContentTypeHeader(String theContentTypeHeader) {
		this.contentTypeHeader = theContentTypeHeader;
	}

	/**
	 * 
	 * @return
	 */
	public String getAuthCookie() {
		return authCookie;
	}

	/**
	 * 
	 * @param theAuthCookie
	 */
	public void setAuthCookie(String theAuthCookie) {
		this.authCookie = theAuthCookie;
	}

	/**
	 * 
	 * @return
	 */
	public String getHost() {
		return host;
	}

	/**
	 * 
	 * @param theHost
	 */
	public void setHost(String theHost) {
		this.host = theHost;
	}

	/**
	 * 
	 * @return
	 */
	public int getPort() {
		return port;
	}

	/**
	 * 
	 * @param thePort
	 */
	public void setPort(int thePort) {
		this.port = thePort;
	}

	/**
	 * 
	 * @return
	 */
	public String getSeleniumServer() {
		return seleniumServer;
	}

	/**
	 * 
	 * @param theSeleniumServer
	 */
	public void setSeleniumServer(String theSeleniumServer) {
		this.seleniumServer = theSeleniumServer;
	}

	/**
	 * 
	 * @return
	 */
	public int getSeleniumPort() {
		return seleniumPort;
	}

	/**
	 * 
	 * @param theSeleniumPort
	 */
	public void setSeleniumPort(int theSeleniumPort) {
		this.seleniumPort = theSeleniumPort;
	}

	/**
	 * 
	 * @return
	 */
	public String getTestEnv() {
		return testEnv;
	}

	/**
	 * 
	 * @param theTestEnv
	 */
	public void setTestEnv(String theTestEnv) {
		this.testEnv = theTestEnv;
	}


	/**
	 * 
	 * @return
	 */
	public String getScheme() {
		return scheme;
	}

	/**
	 * 
	 * @param theScheme
	 */
	public void setScheme(String theScheme) {
		this.scheme = theScheme;
	}

	/**
	 * 
	 * @return
	 */
	public String getFragment() {
		return fragment;
	}

	/**
	 * 
	 * @param theFragment
	 */
	public void setFragment(String theFragment) {
		this.fragment = theFragment;
	}

	/**
	 * 
	 * @return
	 */
	public List<NameValuePair> getHeaders() {
		return headers;
	}

	/**
	 * 
	 * @param theHeaders
	 */
	public void setHeaders(List<NameValuePair> theHeaders) {
		this.headers = theHeaders;
	}

	/**
	 * 
	 * @return
	 */
	public List<NameValuePair> getMatrixParams() {
		return matrixParams;
	}

	/**
	 * 
	 * @param theMatrixParams
	 */
	public void setMatrixParams(List<NameValuePair> theMatrixParams) {
		this.matrixParams = theMatrixParams;
	}


	public static AuthType getAuthType() {
		return authType;
	}

	public static void setAuthType(AuthType authType) {
		CommonTest.authType = authType;
	}

	public String getBaseVersion() {
		return baseVersion;
	}

	public void setBaseVersion(String theBaseVersion) {
		this.baseVersion = theBaseVersion;
	}

	public String getMessageApiNewVersion() {
		return messageApiNewVersion;
	}

	public void setMessageApiNewVersion(String messageApiNewVersion) {
		this.messageApiNewVersion = messageApiNewVersion;
	}


	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String theTenantId) {
		this.tenantId = theTenantId;
	}

	public String getSecurityXTest() {
		return securityXTest;
	}

	public void setSecurityXTest(String theSecurityXTest) {
		this.securityXTest = theSecurityXTest;
	}

	public boolean isVerifyCache() {
		return verifyCache;
	}

	public void setVerifyCache(boolean verifyCache) {
		this.verifyCache = verifyCache;
	}

	public int getActivityTestSleepIntervalMS() {
		return activityTestSleepIntervalMS;
	}

	public void setActivityTestSleepIntervalMS(String theActivityTestSleepIntervalMS) {
		if(theActivityTestSleepIntervalMS == null || theActivityTestSleepIntervalMS.equals("")){
			theActivityTestSleepIntervalMS = "0";
		}
		this.activityTestSleepIntervalMS = Integer.parseInt(theActivityTestSleepIntervalMS);
	}

	public String getBaseContextName() {
		return baseContextName;
	}

	public void setBaseContextInfo(String theBaseContextName) {
		this.baseContextName = theBaseContextName;
	}

	/**
	 * 
	 * @param stringToParse
	 * @return
	 */
	public Map<String, String> parse(String stringToParse){

		Map<String, String> parsedData = new HashMap<String, String>();
		String[] parsedArray = stringToParse.split("!");
		for ( String part : parsedArray){
			String key = part.substring(0, part.indexOf("="));
			String value = part.substring((part.indexOf("="))+1, part.length());
			parsedData.put(key, value);
		}
		return parsedData;
	}

	public boolean isDebugOn() {
		return isDebugOn;
	}


	public boolean isPerfLoggingOn() {
		return isPerfLoggingOn;
	}

	@AfterTest(alwaysRun = true)
	public void generatePerformanceReport(){
		//perfReporter.generateReport();
	}


	public String getConsumerKey() {
		return consumerKey;
	}

	public String getSecret() {
		return secret;
	}

	public String getresponse(String jsonMembership) {

		System.out.println();
		if (jsonMembership.indexOf("[") != -1
				&& jsonMembership.lastIndexOf("]") != -1)
			jsonMembership = jsonMembership.substring(
					jsonMembership.indexOf("[") + 1,
					jsonMembership.lastIndexOf("]"));

		System.out.println("jsonMembership.." + jsonMembership);
		return jsonMembership;

	}

	/*public HttpResponse getResponse(int requestType, String url,
			List<NameValuePair> headers) throws Exception {
		StringBuilder buffer = new StringBuilder();
		buffer.append(url);
		URI uri = new URI(buffer.toString());
		PlatformTestClient platformTestClient = new PlatformTestClient(
				"Platform");
		HttpResponse httpResponse = platformTestClient.executeRestRequest(
				requestType, uri, headers, null, null);
		return httpResponse;
	}*/

	/*public String getRelativeUrlWithReplaceParamsForPlatformUrl(String url,
			String context, String profileId) {

		System.out.println("profileId.." + profileId);
		if (url.contains("{contextId}"))
			url = url.replace("{contextId}", getCourseOfferingId());
		return replaceParams(url, profileId);
	}*/

	public String replaceParams(String url, String profileId) {
		if (url.contains("{tenantId}"))
			url = url.replace("{tenantId}", getTenantId());
		/*if (url.contains("{profileId}"))
			url = url.replace("{profileId}", profileId);
		if (url.contains("{coId}"))
			url = url.replace("{coId}", getCourseOfferingId());*/
		/*if (url.contains("{courseCode}"))
			url = url.replace("{courseCode}", getCourseCode());*/
		//System.out.println("PlatFormServiceGateway url:" + url);
		return url;
	}

}
