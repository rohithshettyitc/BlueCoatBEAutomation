package com.bluecoatcloud.threatpulse.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

//import org.apache.commons.httpclient.HttpMethod;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.params.ConnRouteParamBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;




//import com.aptimus.altcloud.utils.SeattleAuthClient;

public class RestTestClient {

    private static RestTestClient theClient;
    private DefaultHttpClient httpClient;

    public enum HttpMethodType {
	GET, POST, PUT, DELETE
    }

    public RestTestClient() {
	// httpClient = new DefaultHttpClient();
	wrapClient();
    }

    @SuppressWarnings("deprecation")
    private void wrapClient() {
	httpClient = new DefaultHttpClient();
	try {
	    SSLContext ctx = SSLContext.getInstance("TLS");
	    X509TrustManager tm = new X509TrustManager() {
		public void checkClientTrusted(X509Certificate[] xcs,
			String string) throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] xcs,
			String string) throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
		    return null;
		}
	    };
	    ctx.init(null, new TrustManager[] { tm }, null);
	    SSLSocketFactory ssf = new SSLSocketFactory(ctx);
	    ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

	    ClientConnectionManager ccm = httpClient.getConnectionManager();
	    SchemeRegistry sr = ccm.getSchemeRegistry();
	    sr.register(new Scheme("https", ssf, 443));
	    sr.register(new Scheme("http", PlainSocketFactory
		    .getSocketFactory(), 80));
	    //HttpHost proxy = new HttpHost("127.0.0.1", 8888);
	   // httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
	} catch (Exception ex) {
	    ex.printStackTrace();
	}

    }

    public HttpResponse executeRequest(HttpMethodType requestType,
	    java.net.URI uri, List<NameValuePair> headers, HttpEntity entity,
	    List<NameValuePair> matrixParams, String username, String password, String profileId, String courseId,
            boolean useExistingConnection)
            throws ClientProtocolException, UnsupportedEncodingException,
	    IOException, InterruptedException, GeneralSecurityException, Exception {

	/*if (matrixParams == null)
	    matrixParams = new ArrayList<NameValuePair>();

	String authCookie = SeattleAuthClient.getCookie(username, password,
		useExistingConnection);

	if (authCookie != "") {

	    matrixParams.add(new BasicNameValuePair("cookie", SeattleAuthClient.getCookie(
			username, password, useExistingConnection)));
	    matrixParams.add(new BasicNameValuePair(
		    CommonConstants.HEADER_X_REQUESTED_WITH, authCookie
			    .substring(0, authCookie.indexOf('=') + 1)));
	}*/
	// just set it to get, dumb compiler doesn't trust me

      //  matrixParams.add(new BasicNameValuePair("x-apollogroup-edu-vlp-context", DiscussionSuiteUtils.generateVLPConextHeader(courseId)));
	HttpUriRequest request = new HttpGet();

	switch (requestType) {
	case GET:
	    request = new HttpGet(uri);
	    break;
	case POST:
	    request = new HttpPost(uri);
	    if (entity != null) {
		((HttpPost) request).setEntity(entity);
	    }
	    break;
	case PUT:
	    request = new HttpPut(uri);

	    if (entity != null) {
		((HttpPut) request).setEntity(entity);
	    }

	    break;
	case DELETE:
	    request = new HttpDelete(uri);
	    break;

	}

	if (matrixParams == null) {
	    matrixParams = new ArrayList<NameValuePair>();
	}

	Iterator<NameValuePair> iterator = matrixParams.iterator();
	while (iterator.hasNext()) {
	    NameValuePair header = iterator.next();
	    request.setHeader(header.getName(), header.getValue());
	}

	if (headers != null) {
	    setHeaders(request, headers);
	}

	HttpResponse resp = httpClient.execute(request); 
	resetHttpConnections();
	return resp;
    }
    
    public HttpResponse executeOauthRequest(HttpMethodType requestType,
	    java.net.URI uri, List<NameValuePair> headers, HttpEntity entity,
	    List<NameValuePair> matrixParams, String username, String password, String profileId, String courseId, String tenantId,
            String oauthKey, String oauthPassword)
            throws ClientProtocolException, UnsupportedEncodingException,
	    IOException, InterruptedException, GeneralSecurityException, Exception {

	if (matrixParams == null)
	    matrixParams = new ArrayList<NameValuePair>();


	
	// just set it to get, dumb compiler doesn't trust me

        //matrixParams.add(new BasicNameValuePair("x-apollogroup-edu-vlp-context", DiscussionSuiteUtils.generateVLPConextHeader(courseId)));
	HttpUriRequest request = new HttpGet();

	switch (requestType) {
	case GET:
	    request = new HttpGet(uri);
	    break;
	case POST:
	    request = new HttpPost(uri);
	    if (entity != null) {
		((HttpPost) request).setEntity(entity);
	    }
	    break;
	case PUT:
	    request = new HttpPut(uri);

	    if (entity != null) {
		((HttpPut) request).setEntity(entity);
	    }

	    break;
	case DELETE:
	    request = new HttpDelete(uri);
	    break;

	}

	/*if (matrixParams == null) {
	    matrixParams = new ArrayList<NameValuePair>();
	}
	HashMap<String, String> parameters = new HashMap<String, String>();*/
	


	/*String oauthHeader = SeattleAuthClient.getOAuthHeader(new URL(uri.toURL().toString().replace("https", "http")), RestAPI.valueOf(requestType.toString()), null);
	Iterator<NameValuePair> iterator = matrixParams.iterator();
	while (iterator.hasNext()) {
	    NameValuePair header = iterator.next();
	    request.setHeader(header.getName(), header.getValue());
	}*/
	
	//request.setHeader("Authorization", oauthHeader);
	//request.setHeader("Content-Type", "application/sparql-query");

	/*headers.add(new BasicNameValuePair("Authorization", oauthHeader));
	if (headers != null) {
	    setHeaders(request, headers);
	}*/

	HttpResponse resp = httpClient.execute(request);
	resetHttpConnections();
	return resp;
    }
    
    /*public HttpResponse executeRequestWithAdditionalHeaders(HttpMethodType requestType,
	    java.net.URI uri, List<NameValuePair> headers, HttpEntity entity,
	    List<NameValuePair> matrixParams, String username, String password,
	    String testEnvironment, boolean useExistingConnection)
	    throws ClientProtocolException, UnsupportedEncodingException,
	    IOException, InterruptedException, GeneralSecurityException {

	if (matrixParams == null)
	    matrixParams = new ArrayList<NameValuePair>();

	String authCookie = SeattleAuthClient.getCookie(username, password,
		useExistingConnection);

	if (authCookie != "") {

	    matrixParams.add(new BasicNameValuePair("cookie", SeattleAuthClient.getCookie(
			username, password, useExistingConnection)));
	    matrixParams.add(new BasicNameValuePair(
		    CommonConstants.HEADER_X_REQUESTED_WITH, authCookie
			    .substring(0, authCookie.indexOf('=') + 1)));
	}

	
	String cookie = "";
	ArrayList<NameValuePair> cookiesToRemove = new ArrayList<NameValuePair>();
	for (NameValuePair cookiePair : matrixParams)
	{
	    
	    if (cookiePair.getName().equals("cookie")) {
		cookie+=cookiePair.getValue() + ";";
		cookiesToRemove.add(cookiePair);
	    }
	}
	
	matrixParams.removeAll(cookiesToRemove);

	matrixParams.add(new BasicNameValuePair("cookie", cookie));
	
	// just set it to get, dumb compiler doesn't trust me

	HttpUriRequest request = new HttpGet();

	switch (requestType) {
	case GET:
	    request = new HttpGet(uri);
	    break;
	case POST:
	    request = new HttpPost(uri);
	    if (entity != null) {
		((HttpPost) request).setEntity(entity);
	    }
	    if (matrixParams != null && matrixParams.size() > 0) {
		((HttpPost) request).setEntity(new UrlEncodedFormEntity(
			matrixParams, "UTF-8"));
	    }
	    break;
	case PUT:
	    request = new HttpPut(uri);

	    if (entity != null) {
		((HttpPut) request).setEntity(entity);
	    }
	    if (matrixParams != null && matrixParams.size() > 0) {
		((HttpPut) request).setEntity(new UrlEncodedFormEntity(
			matrixParams, "UTF-8"));
	    }
	    break;
	case DELETE:
	    request = new HttpDelete(uri);
	    break;

	}

	if (matrixParams == null) {
	    matrixParams = new ArrayList<NameValuePair>();
	}

	Iterator<NameValuePair> iterator = matrixParams.iterator();
	while (iterator.hasNext()) {
	    NameValuePair header = iterator.next();
	    request.setHeader(header.getName(), header.getValue());
	}

	if (headers != null) {
	    setHeaders(request, headers);
	}

	HttpResponse resp = httpClient.execute(request);
	resetHttpConnections();
	return resp;
    }*/

    private void setHeaders(HttpUriRequest request, List<NameValuePair> headers) {
	Iterator<NameValuePair> iterator = headers.iterator();

	while (iterator.hasNext()) {
	    NameValuePair header = iterator.next();
	    request.setHeader(header.getName(), header.getValue());
	}
    }

    public HttpResponse executeRestRequest(HttpMethodType requestType,
	    java.net.URI uri, List<NameValuePair> headers, HttpEntity entity,
	    List<NameValuePair> matrixParams, String username, String password, String profileId, String courseId,
	    boolean useExistingConnection)
	    throws ClientProtocolException, IOException, InterruptedException, GeneralSecurityException, Exception {
	return executeRequest(requestType, uri, headers, entity, matrixParams,
		username, password, profileId, courseId, useExistingConnection);
    }

    public HttpResponse executeRestRequest(HttpMethodType requestType,
	    java.net.URI uri, List<NameValuePair> headers, String json,
	    List<NameValuePair> matrixParams, String username, String password,String profileId, String courseId,
	    String testEnv, boolean useExistingConnection)
	    throws ClientProtocolException, IOException, InterruptedException, GeneralSecurityException, Exception {
	return executeRequest(requestType, uri, headers,
		new StringEntity(json), matrixParams, username, password, profileId, courseId,
		useExistingConnection);
    }

    public void resetHttpConnections() {

	try {
	    wrapClient();
	} catch (Exception e) {
	}
    }
    
    public HttpResponse executeRequest(HttpUriRequest request)
    	throws Exception
    {
	return httpClient.execute(request);
    }
    
    public HttpResponse executeBasicRequestNoAuth(HttpMethodType requestType, java.net.URI uri, HttpEntity entity)
    	throws Exception
    {


	
	// just set it to get, dumb compiler doesn't trust me

	HttpUriRequest request = new HttpGet();

	switch (requestType) {
	case GET:
	    request = new HttpGet(uri);
	    break;
	case POST:
	    request = new HttpPost(uri);
	    if (entity != null) {
		((HttpPost) request).setEntity(entity);
	    }
	    break;
	case PUT:
	    request = new HttpPut(uri);

	    if (entity != null) {
		((HttpPut) request).setEntity(entity);
	    }

	    break;
	case DELETE:
	    request = new HttpDelete(uri);
	    break;

	}


	HttpResponse resp = httpClient.execute(request);
	resetHttpConnections();
	return resp;
    }
}
