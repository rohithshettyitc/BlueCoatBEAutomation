package com.bluecoatcloud.threatpulse.constants;

public class CommonConstants {


	//use this to turn on or off debug 

	public static final String PERF_LOGGING_ON = "false";
	public static final String DEBUG_ON = "false";

	//selenium server info for apolloasertions
	public static final String KEY_SELENIUM_SERVER = "seleniumServer";
	public static final String KEY_SELENIUM_PORT = "seleniumPort";

	//Default configuration File to provide setup  information 
	public static final String CONFIG_LOCATION = "\\src\\test\\java\\com\\bluecoatcloud\\threatpulse\\configuration\\";
	public static final String DATA_LOCATION = "\\src\\test\\resources\\com\\aptimus\\altcloud\\TestData\\";
	public static final String DATA_COMMON = "common";

	//public static final String JKS_LOCATION = "\\src\\test\\java\\edu\\apollogrp\\platform\\properties\\";
	public static final String SSO_PROP_FILENAME = "sso.properties";

	//system property
	public static final String USER_DIR = "user.dir";
	public static final String CONFIGURATION_FILE_DEFAULT = "configuration.xml";

	//Testing Environment - QA, DEV or PROD
	public static final String KEY_TEST_ENVIRONMENT = "testEnv";
	public static final String KEY_HEADER_CONNECTION = "connectionHeader";


	public static final String HEADER_ACCEPT = "Accept" ;
	public static final String HEADER_COOKIE = "Cookie" ;
	public static final String HEADER_CONNECTION = "Connection";
	public static final String HEADER_AUTHORIZATION = "Authorization";
	public static final String HEADER_CONTENT_TYPE = "Content-type";
	public static final String HEADER_X_REQUESTED_WITH = "X-Requested-With";
	public static final String HEADER_SECURITY_XTEST = "x-test";
	public static final String HEADER_ACCEPT_CHARSET = "Accept-Charset";
	public static final String HEADER_APPLICATION_OCTET_STREAM = "application/octet-stream";
	public static final String HEADER_ETAG = "Etag";
	public static final String HEADER_IF_NONE_MATCH = "If-None-Match";
	public static final String HEADER_IF_MODIFIED_SINCE = "If-Modified-Since";
	public static final String HEADER_ACCEPT_AAPLICATION_JSON = "Application/json";
	public static final String HEADER_ACCEPT_AAPLICATION_XML = "Application/xml";
	public static final String CHAR_SET = "UTF-8";

	//QA Aptimus Configurations
	public static final String TEST_ENVIRONMENT_QA_BLUECOAT = "qa.portalbluecoat";
	public static final String TEST_ENVIRONMENT_DEV_BLUECOAT = "dev.portalbluecoat";
	public static final String KEY_NETWORK_PROTOCOL_QA_BLUECOAT = "qa.networkprotocol";
	public static final String KEY_HOST_QA_BLUECOAT= "qa.host";
	public static final String KEY_PORT_QA_BLUECOAT = "qa.port";


	public static final int REQUEST_GET = 1;
	public static final int REQUEST_POST = 2;
	public static final int REQUEST_PUT = 3;
	public static final int REQUEST_DELETE = 4;

	public static final String BASE_VERSION_PLACEHOLDER = "#version#";
	public static final String TENANTID_PLACEHOLDER = "#tenantId#";
	public static final String HOST_PLACEHOLDER = "#host#";
	public static final String PORT_PLACEHOLDER = "#port#";
	public static final String QUERY_PARAM_DELIMETER_PLACEHOLDER = "#?#";
	public static final String URL_PLACEHOLDER = "#url#";
	public static final String QUERY_PARAM_DELIMETER = "&";
	public static final String BASE_CONTEXT_NAME_PLACEHOLDER = "#contextName#";
	public static final String X_APOLLOGROUP_EDU_VLP_CONTEXT_PLACEHOLDER = "vlpContext.header";
	public static final String PERMANENT_SENT_FAILURE_PLACEHOLDER = "permanent.sent.failure.url";

	public static final String DB_HOST = "dbhost";
	public static final String DB_PORT = "dbport";
	public static final String DB_USERNAME = "dbusername";
	public static final String DB_PASSWORD = "dbpassword";
}
