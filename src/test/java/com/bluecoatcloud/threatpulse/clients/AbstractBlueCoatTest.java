package com.bluecoatcloud.threatpulse.clients;


import java.net.URI;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

import com.bluecoatcloud.threatpulse.clients.CommonTest;
import com.bluecoatcloud.threatpulse.clients.BlueCoatTestBase;
import com.bluecoatcloud.threatpulse.utils.BlueCoaSuiteUtils;
import com.bluecoatcloud.threatpulse.utils.RestTestClient;


public class AbstractBlueCoatTest extends CommonTest {
	
	WebDriver driver;
	HttpResponse resonse;
	URI uri;
	
	

	/*
	 * Back end API construction
	 */
	public HttpResponse blueCoatCloud_DirectProvider(RestTestClient restClient, String payLoad/*, String cookieId*/) throws Exception {
		ArrayList<NameValuePair> headers = BlueCoatTestBase
				.getHeader(new BasicNameValuePair("content-type",
						getContentTypeHeader()));
		headers.add(new BasicNameValuePair("Accept", "application/json"));
		headers.add(new BasicNameValuePair("Authorization", "Basic UmFnaHVuYW5kYW4uZGl4aXRAaXRjaW5mb3RlY2guY29tOkl0Y2luZm90ZWNoQDEyMw=="));
		headers.add(new BasicNameValuePair("Referer", "https://portal.qa3.bluecoatcloud.com/"));
		headers.add(new BasicNameValuePair("Host", "portal.qa3.bluecoatcloud.com"));

		//This should be made as dynamic, once we get info from developer on generating X-CSRF-Token
		headers.add(new BasicNameValuePair("X-CSRF-Token", "gsk1tcg8n4kle62qtrnav8k6km"));
		headers.add(new BasicNameValuePair("Cookie", "JSESSIONID=70CEBA99723650FEBF46758569DE4857"));
		
		/*if(payLoad.equals("")){ 
			
			uri = new URI(BlueCoaSuiteUtils.generateDirectProviderUrl_negativeCheck());
			
		}
		else {
			uri = new URI(BlueCoaSuiteUtils.generateDirectProviderUrl());
		}*/
		
		uri = new URI(BlueCoaSuiteUtils.generateDirectProviderUrl());
		String msg = "djn API reuest"; 
		System.out.println(" Request: "+uri.toString());
		Reporter.log(msg);
		Reporter.log("POST Request: "+uri.toString());

		HttpEntity entity = new StringEntity(payLoad);
		return restClient.executeRequest(RestTestClient.HttpMethodType.POST, uri, headers, entity, null, 
				null, null, null, null, true);
	}
	
	public HttpResponse blueCoatCloud_DirectProvider_negativeCheck(RestTestClient restClient, String payLoad/*, String cookieId*/) throws Exception {
		ArrayList<NameValuePair> headers = BlueCoatTestBase
				.getHeader(new BasicNameValuePair("content-type",
						getContentTypeHeader()));
		headers.add(new BasicNameValuePair("Accept", "application/json"));
		headers.add(new BasicNameValuePair("Authorization", "Basic UmFnaHVuYW5kYW4uZGl4aXRAaXRjaW5mb3RlY2guY29tOkl0Y2luZm90ZWNoQDEyMw=="));
		headers.add(new BasicNameValuePair("Referer", "https://portal.qa3.bluecoatcloud.com/"));
		headers.add(new BasicNameValuePair("Host", "portal.qa3.bluecoatcloud.com"));

		//This should be made as dynamic, once we get info from developer on generating X-CSRF-Token
		//headers.add(new BasicNameValuePair("X-CSRF-Token", "75o0lmide4drovke6rvdmir570"));
		headers.add(new BasicNameValuePair("Cookie", "JSESSIONID=70CEBA99723650FEBF46758569DE4857"));
		
		uri = new URI(BlueCoaSuiteUtils.generateDirectProviderUrl_negativeCheck());
		
		//uri = new URI(BlueCoaSuiteUtils.generateDirectProviderUrl());
		String msg = "djn API reuest"; 
		System.out.println(" Request: "+uri.toString());
		Reporter.log(msg);
		Reporter.log("POST Request: "+uri.toString());

		HttpEntity entity = new StringEntity(payLoad);
		return restClient.executeRequest(RestTestClient.HttpMethodType.POST, uri, headers, entity, null, 
				null, null, null, null, true);
	}
}
