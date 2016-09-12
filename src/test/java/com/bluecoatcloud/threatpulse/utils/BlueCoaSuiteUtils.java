package com.bluecoatcloud.threatpulse.utils;

import java.util.Random;


public class BlueCoaSuiteUtils {

	
	public static Random r = new Random(System.currentTimeMillis());

	
	public static final String getdirectProvider="https://portal.qa3.bluecoatcloud.com/djn/directprovider";
	public static final String getdirectProvider_negativeCheck ="https://portal.qa3.bluecoatcloud.com/djn/directprovider/";
	
	private static String scheme;
	private static String host;
	private static int port;
	private static String baseVersion;
	private static String messageApiNewVersion;
	private static String tenantId;


	public static void configSuiteUtils(String scheme, String host, int port,
			String baseVersion, String messageApiNewVersion,String tenantId) {
		BlueCoaSuiteUtils.scheme = scheme;
		BlueCoaSuiteUtils.host = host;
		BlueCoaSuiteUtils.port = port;
		BlueCoaSuiteUtils.baseVersion = baseVersion;
		BlueCoaSuiteUtils.messageApiNewVersion = messageApiNewVersion;
		BlueCoaSuiteUtils.tenantId = tenantId;
	}
	
	public static String getHost() {
		return host;
	}

	public static String getScheme() {
		return scheme;
	}

	public static void setScheme(String scheme) {
		BlueCoaSuiteUtils.scheme = scheme;
	}

	public static void setHost(String host) {
		BlueCoaSuiteUtils.host = host;
	}

	public static int getPort() {
		return port;
	}

	public static void setPort(int port) {
		BlueCoaSuiteUtils.port = port;
	}

	public static String getBaseVersion() {
		return baseVersion;
	}

	public static void setBaseVersion(String baseVersion) {
		BlueCoaSuiteUtils.baseVersion = baseVersion;
	}
	public static String getMessageApiNewVersion() {
		return messageApiNewVersion;
	}

	public static void setMessageApiNewVersion(String messageApiNewVersion) {
		BlueCoaSuiteUtils.messageApiNewVersion = messageApiNewVersion;
	}


	public static String getTenantId() {
		return tenantId;
	}

	public static void setTenantId(String tenantId) {
		BlueCoaSuiteUtils.tenantId = tenantId;
	}
	
	public static String generateDirectProviderUrl() {
		return  String.format(getdirectProvider);
	}
	
	public static String generateDirectProviderUrl_negativeCheck() {
		return  String.format(getdirectProvider_negativeCheck);
	}
	
	public static String generateHostString() {
		return getScheme() + "://" + getHost()
				+ (getPort() != 0 ? ":" + getPort() : "");
	}
}
