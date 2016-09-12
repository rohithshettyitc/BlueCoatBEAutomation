package com.bluecoatcloud.threatpulse.utils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import org.apache.http.client.utils.URIUtils;



 	public class Utility {
 	
 		protected URI uri;
 		//public static String CharType;
 		public static enum CharType {ALPHA, NUM};
 		
		
 		public static URI getURI(String scheme, String host, int port, String path, String queryParams, String fragment) throws URISyntaxException{	
 			return  URIUtils.createURI(scheme, host, port, path, queryParams, fragment);
 		}
	
 		/**
 		 * 
 		 * @param methodName
 		 * @return
 		 */
 		public static String getRestMethodToString(int methodName){
 			
 			switch (methodName){
 				case 1:
 					return "GET";
 				case 2:
 					return "POST";
 				case 3:
 					return "PUT";
 				case 4:
 					return "DELETE";
  			}
 			return "";
 		}
 		
 		/**
 		 * 
 		 * @param methodName
 		 * @return
 		 */
 		/*public static int getEnumToInt(RestAPI  methodName){
 			
 			switch (methodName){
 				case GET:
 					return 1;
 				case POST:
 					return 2;
 				case PUT:
 					return 3;
 				case DELETE:
 					return 4;
  			}
 			return -1;
 		}*/
 		
 	
 	    /**
 	     * 
 	     * @param theDateValue
 	     * @param theLocale
 	     * @return Date
 	     */
 		public static Date convertStringToDate(String theDateValue, Locale theLocale){
 			//DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, theLocale);
 			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
 			Date retDate = null;
 			try {
 				theDateValue = theDateValue.substring(0, theDateValue.lastIndexOf(":")) + "00";
 				retDate = df.parse(theDateValue);
 				//retDate = df.parse("2011-08-15T23:59:00.000-0700");
 				System.out.println("Date after String to date conversion: " + df.format(retDate));
 			}catch (ParseException e) {
 				e.printStackTrace();
 			}
 			return retDate;
 		}
 		
 		  /**
 		 * @param charType
 		 * @param length
 		 * @return
 		 * 
 		 * eg: To generate a random Alphabet of length 3 
 		 * 
 		 * generateRandomAlphaString(ALPHA, int length)
 		 */
 		public static String generateRandomAlphaString(CharType charType, int length) {
 			 Random random = new Random((new Date()).getTime());
 		      char[] numValues = {'0','1','2','3',
 		               '4','5','6','7','8','9'};
 		      char[] charValues = {'a','b','c','d','e','f','g','h',
 		    		  'i','j','k','l','m','n','o','p',
 		    		  'q', 'r','s','t','u','v','w','x','y','z'};
 		      String out = "";
 		      switch (charType) {
 		      case NUM:
				 for (int i=0;i<length;i++) {
	 		          int idx=random.nextInt(numValues.length);
	 		        out += numValues[idx];
	 		      }
	 		      return out.toUpperCase();
			case ALPHA:
				 for (int i=0;i<length;i++) {
	 		          int idx=random.nextInt(charValues.length);
	 		        out += charValues[idx];
	 		      }
	 		      return out.toUpperCase();
			default:
				break;
			}
			return out;
 		}
 		 
 		
 		public static String getUTCdatetimeAsString()
 		{
 		    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 		    sdf.setTimeZone(TimeZone.getTimeZone("PST"));
 		    final String utcTime = sdf.format(new Date());
 		    return utcTime;
 		}
 		
        /**
         * 
         * @param stringToParse
         * @return
         */
 		public static Map<String, String> parse(String stringToParse){
 			 
 			Map<String, String> parsedData = new HashMap<String, String>();
 			String[] parsedArray = stringToParse.split(";");
 			for ( String part : parsedArray){
 				String key = part.substring(0, part.indexOf("="));
 				String value = part.substring((part.indexOf("="))+1, part.length());
 				parsedData.put(key, value);
 			}
 			return parsedData;
 		}
 		
 		/**
 		 * 
 		 * @param url
 		 * @return
 		 * @throws UnsupportedEncodingException 
 		 */
 		public static String encodeUrl(String url) throws UnsupportedEncodingException{
 			return URLEncoder.encode(url, "UTF-8");
 		}
 		
 		/**
 		 * 
 		 * @param url
 		 * @return
 		 * @throws UnsupportedEncodingException 
 		 */
 		public static String decodeUrl(String decodeUrl) throws UnsupportedEncodingException{
 			URLDecoder urlDecoder = new URLDecoder();
  			return urlDecoder.decode(decodeUrl, "UTF-8");
 			
 		}
 				  
}
