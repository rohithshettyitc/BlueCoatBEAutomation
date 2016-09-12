package com.bluecoatcloud.threatpulse.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.logging.Logger;

//import static org.testng.Assert.assertTrue;



import javax.naming.AuthenticationException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;



import com.bluecoatcloud.threatpulse.utils.BlueCoaSuiteUtils;
//import com.bluecoatcloud.threatpulse.reporting.Reporter;
import com.bluecoatcloud.threatpulse.utils.RestTestClient;
import com.bluecoatcloud.threatpulse.clients.AbstractBlueCoatTest;
import com.bluecoatcloud.threatpulse.constants.BLueCoatConstants;
import com.bluecoatcloud.threatpulse.dto.DirectProvider;
import com.bluecoatcloud.threatpulse.helpers.TestCaseResult;

public class BlueCoatThreatPulseTest extends AbstractBlueCoatTest {

	protected RestTestClient restClient;
	protected String bluecoatAPIPayload;

	protected String userName;
	protected String passWord;
	private HashMap<String, TestCaseResult> resultSummary;
	protected DirectProvider directProvider;

	@BeforeClass(alwaysRun = true)
	public void setUp() throws IOException, AuthenticationException {

		restClient = new RestTestClient();
		directProvider = new DirectProvider();
		userName = BLueCoatConstants.USERNAME;
		passWord = BLueCoatConstants.PASSWORD;

		resultSummary = new HashMap<String ,TestCaseResult>();

		InputStream inStream1 = null;
		String jdataPath = System.getProperty("user.dir") +
				org.apache.commons.io.FilenameUtils.separatorsToSystem(BLueCoatConstants.BLUECOATCLOUD_DATA_LOCATION);

		if(System.getProperty(BLueCoatConstants.KEY_JOB_SERVICE_API_PAYLOAD) == null){
			String filepath1 = jdataPath + BLueCoatConstants.KEY_JOB_SERVICE_API_PAYLOAD;
			inStream1 = new FileInputStream(filepath1);
			bluecoatAPIPayload = IOUtils.toString(inStream1);
		}
	}

	public static String getAuthParam(String username, String password) throws IOException {
		String authParam = "Basic "+new String(Base64.encodeBase64((username + ":" + password).getBytes()));
		//Logger.info("AuthParam for "+username +" and "+password+" is "+ authParam);
		return authParam;
	}

	public Object[][] getTimeInMilliseconds() throws Exception {

		List<Integer> dateInputList = new ArrayList<Integer>();
		dateInputList.add(1); //"Today"
		dateInputList.add(2); //"Yesterday"
		dateInputList.add(3); //"CurrentWeek"
		dateInputList.add(4); //"PreviousWeek"
		dateInputList.add(5); //"CurentMonth"
		dateInputList.add(6); //"PreviousMonth"
		dateInputList.add(7); //"Custom"
		dateInputList.add(8); //"AllDates"

		String path = "src/test/resources/com/bluecoatcloud/threatpulse/";
		File dir = new File(path);
		String files[] = dir.list();
		InputStream stream = null;
		String jsonData = null;
		String file = files[0];
		System.out.println("Direct Provider.." +file);

		Object[][] inputPayload = new Object[6][2];

		for (int i = 0; i < dateInputList.size(); i++) {

			switch (dateInputList.get(i)) {
			case 1: 
				stream = new FileInputStream(path+"directProvider_today_Json");
				jsonData = IOUtils.toString(stream);
				long currentTimeInMilliseconds = System.currentTimeMillis();
				String todayDate = Long.toString(currentTimeInMilliseconds);
				Date dateStart = new Date(currentTimeInMilliseconds);
				System.out.println("Today start "+dateStart.toString());
				System.out.println("dateStart "+currentTimeInMilliseconds);

				Calendar c = Calendar.getInstance(); 
				c.setTime(dateStart); 
				c.add(Calendar.DATE, 1);
				Date dateEnd = c.getTime();
				long todayDateEnd = dateEnd.getTime();
				String todayEnd = Long.toString(todayDateEnd);
				System.out.println("Today End "+dateEnd.toString()); 
				System.out.println("dateEnd "+todayDate);

				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
				String dateSelected = "Between " + sdf.format(dateStart)+ " and " +sdf.format(dateEnd);
				org.testng.Reporter.log(dateSelected); 

				jsonData = jsonData.replace("#dateStart#", todayDate).replace("#dateEnd#", todayEnd);
				inputPayload[0][0] = dateSelected;
				inputPayload[0][1] = jsonData;
				break;

			case 2:
				stream = new FileInputStream(path+"directProvider_yesterday_Json");
				jsonData = IOUtils.toString(stream);
				long currentTimeInMilliseconds1 = System.currentTimeMillis();
				String yesterdayDate = Long.toString(currentTimeInMilliseconds1);
				Date yesterdaydateStart = new Date(currentTimeInMilliseconds1);
				System.out.println("Yesterday start "+yesterdayDate.toString());
				//System.out.println("dateStart "+currentTimeInMilliseconds1);

				Calendar c2 = Calendar.getInstance(); 
				c2.setTime(yesterdaydateStart); 
				c2.add(Calendar.DATE, -1);
				Date yesterdaydateEnd = c2.getTime();
				long yesterdayDateEnd = yesterdaydateEnd.getTime();
				String yesterdayEnd = Long.toString(yesterdayDateEnd);
				System.out.println("Yesterday End "+yesterdaydateEnd.toString()); 
				//System.out.println("dateEnd "+yesterdayEnd);

				SimpleDateFormat sdf_yesterday = new SimpleDateFormat("MM/dd/yy");
				String yesterdaydateSelected = "Between " + sdf_yesterday.format(yesterdayDateEnd)+ " and " +sdf_yesterday.format(yesterdaydateStart);
				org.testng.Reporter.log(yesterdaydateSelected); 

				jsonData = jsonData.replace("#dateStart#", yesterdayEnd).replace("#dateEnd#", yesterdayDate);
				inputPayload[1][0] = yesterdaydateSelected;
				inputPayload[1][1] = jsonData;
				break;

			case 3:
				stream = new FileInputStream(path+"directProvider_currentWeek_Json");
				jsonData = IOUtils.toString(stream);
				long currentTimeInMilliseconds3 = System.currentTimeMillis();
				//String currentWeekDate = Long.toString(currentTimeInMilliseconds3);
				Date currenTweekdateStart = new Date(currentTimeInMilliseconds3);
				Calendar cal = Calendar.getInstance();
				cal.setTime(currenTweekdateStart);
				cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
				Date startDate = cal.getTime();
				long startdateMilisecond = startDate.getTime();
				String currenweekstart = Long.toString(startdateMilisecond);
				System.out.println("Currrent Week start "+startDate.toString());

				cal.add(Calendar.DATE, 7);
				Date endDate = cal.getTime();
				long endDateMillisecond = endDate.getTime();
				String currenweekend = Long.toString(endDateMillisecond);
				System.out.println("Current week Start Date:"+startDate+"Current week End Date:"+endDate);

				SimpleDateFormat sdf_currentweek = new SimpleDateFormat("MM/dd/yy");
				String currentweekdateSelected = "Between " + sdf_currentweek.format(startdateMilisecond)+ " and " +sdf_currentweek.format(endDateMillisecond);
				org.testng.Reporter.log(currentweekdateSelected); 

				jsonData = jsonData.replace("#dateStart#", currenweekend).replace("#dateEnd#", currenweekstart);
				inputPayload[2][0] = currentweekdateSelected;
				inputPayload[2][1] = jsonData;
				break;

			case 4:
				stream = new FileInputStream(path+"directProvider_previousWeek_Json");
				jsonData = IOUtils.toString(stream);
				long currentTimeInMilliseconds4 = System.currentTimeMillis();
				//String currentWeekDate = Long.toString(currentTimeInMilliseconds3);
				Date previousweekdateStart = new Date(currentTimeInMilliseconds4);
				Calendar cal4 = Calendar.getInstance();
				cal4.setTime(previousweekdateStart);
				cal4.set(Calendar.DAY_OF_WEEK, cal4.getFirstDayOfWeek());
				Date previousweekstartDate = cal4.getTime();
				long previousweekstartdateMilisecond = previousweekstartDate.getTime();
				String previousweekstart = Long.toString(previousweekstartdateMilisecond);
				System.out.println("Previous Week start "+previousweekstart.toString());

				cal4.add(Calendar.DATE, -7);
				Date previousweeekendDate = cal4.getTime();
				long previousweekendDateMillisecond = previousweeekendDate.getTime();
				String previousweekend = Long.toString(previousweekendDateMillisecond);
				System.out.println("Previous week Start Date:"+previousweekstart+"Previous week End Date:"+previousweekend);

				SimpleDateFormat sdf_previousweek = new SimpleDateFormat("MM/dd/yy");
				String previousweekdateSelected = "Between " + sdf_previousweek.format(previousweekendDateMillisecond)+ " and " +sdf_previousweek.format(previousweekstartdateMilisecond);
				org.testng.Reporter.log(previousweekdateSelected); 

				jsonData = jsonData.replace("#dateStart#", previousweekend).replace("#dateEnd#", previousweekstart);
				inputPayload[3][0] = previousweekdateSelected;
				inputPayload[3][1] = jsonData;	
				break;
			case 5:
				stream = new FileInputStream(path+"directProvider_currentMonth_Json");
				jsonData = IOUtils.toString(stream);
				long currentTimeInMilliseconds5 = System.currentTimeMillis();
				//String currentWeekDate = Long.toString(currentTimeInMilliseconds3);
				Date currentmonthdateStart = new Date(currentTimeInMilliseconds5);
				Calendar cal5 = Calendar.getInstance();
				cal5.setTime(currentmonthdateStart);
				cal5.set(Calendar.DAY_OF_MONTH, cal5.getActualMinimum(Calendar.DAY_OF_MONTH));
				cal5.set(Calendar.HOUR_OF_DAY, 0);
				cal5.set(Calendar.MINUTE, 0);
				cal5.set(Calendar.SECOND, 0);
				cal5.set(Calendar.MILLISECOND, 0);
				Date currentmonthstartDate = cal5.getTime();
				long currentmonthstartdateMilisecond = currentmonthstartDate.getTime();
				String currentmonthstart = Long.toString(currentmonthstartdateMilisecond);
				System.out.println("Current month start date "+currentmonthstartDate.toString());

				cal5.set(Calendar.DAY_OF_MONTH, cal5.getActualMaximum((Calendar.DAY_OF_MONTH))); 
				cal5.set(Calendar.HOUR_OF_DAY, 23);
				cal5.set(Calendar.MINUTE, 59);
				cal5.set(Calendar.SECOND, 59);
				cal5.set(Calendar.MILLISECOND, 999);
				Date currentmonthendDate = cal5.getTime();
				long currentmonthenddateMilisecond = currentmonthendDate.getTime();
				String currentmonthend = Long.toString(currentmonthenddateMilisecond);
				System.out.println("Current month end date "+currentmonthendDate.toString());

				System.out.println("Current month Start Date:"+currentmonthstart+"Current month End Date:"+currentmonthend);

				SimpleDateFormat sdf_currentmonth = new SimpleDateFormat("MM/dd/yy");
				String currentmonthdateSelected = "Between " + sdf_currentmonth.format(currentmonthstartdateMilisecond)+ " and " +sdf_currentmonth.format(currentmonthenddateMilisecond);
				org.testng.Reporter.log(currentmonthdateSelected); 

				jsonData = jsonData.replace("#dateStart#", currentmonthstart).replace("#dateEnd#", currentmonthend);
				inputPayload[4][0] = currentmonthdateSelected;
				inputPayload[4][1] = jsonData;
				break;

			case 6:
				stream = new FileInputStream(path+"directProvider_previousMonth_Json");
				jsonData = IOUtils.toString(stream);
				Calendar aCalendar = Calendar.getInstance();
				aCalendar.set(Calendar.DATE, 1);
				aCalendar.add(Calendar.DAY_OF_MONTH, -1);
				Date lastDateOfPreviousMonth = aCalendar.getTime();
				long previoustmonthstartdateMilisecond = lastDateOfPreviousMonth.getTime();
				String previousmonthend = Long.toString(previoustmonthstartdateMilisecond);
				System.out.println("Previous month start date "+lastDateOfPreviousMonth.toString());
				aCalendar.set(Calendar.DATE, 1);
				Date firstDateOfPreviousMonth = aCalendar.getTime();
				long previoustmonthenddateMilisecond = firstDateOfPreviousMonth.getTime();
				String previousmonthenddate = Long.toString(previoustmonthenddateMilisecond);
				System.out.println("Previous month end date "+firstDateOfPreviousMonth.toString());

				System.out.println("Previous month Start Date:"+firstDateOfPreviousMonth+"Current month End Date:"+lastDateOfPreviousMonth);

				SimpleDateFormat sdf_previousmonth = new SimpleDateFormat("MM/dd/yy");
				String previousmonthdateSelected = "Between " + sdf_previousmonth.format(previoustmonthenddateMilisecond)+ " and " +sdf_previousmonth.format(previoustmonthstartdateMilisecond);
				org.testng.Reporter.log(previousmonthdateSelected); 

				jsonData = jsonData.replace("#dateStart#", previousmonthenddate).replace("#dateEnd#", previousmonthend);
				inputPayload[5][0] = previousmonthdateSelected;
				inputPayload[5][1] = jsonData;

				break;

			default:
				break;
			}	
		}
		return inputPayload;
	}

	public HashMap<String, Long> convertDateFormat(){

		long currentTimeInMilliseconds = System.currentTimeMillis();	
		Date date = new Date(currentTimeInMilliseconds);
		System.out.println("Today start "+date.toString());
		System.out.println("dateStart "+currentTimeInMilliseconds);

		HashMap<String, Long> hashmapMilliseconds = new HashMap<String, Long>();

		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		c.add(Calendar.DATE, 1);
		date = c.getTime();
		hashmapMilliseconds.put("TodaydateStart", currentTimeInMilliseconds);
		hashmapMilliseconds.put("TodaydateEnd",   date.getTime());
		System.out.println("Today End "+date.toString()); 
		System.out.println("dateEnd "+date.getTime());

		return hashmapMilliseconds;
	}


	/*
	 *  To get pay load for all date formats
	 */
	@DataProvider(name = "directProvider_Paylaod")
	public Object[][] getOtherActivity() throws Exception
	{
		Object[][] inputData = getTimeInMilliseconds();
		return inputData;		
	}

	public synchronized void differentScenarioReporter(ITestResult result, String dateSelected){
		TestCaseResult testCaseResult = new TestCaseResult();
		testCaseResult.setStatus(String.valueOf(result.getStatus()));
		testCaseResult.setTestCase(result.getMethod().getDescription());
		testCaseResult.setResponseBody(String.valueOf(result.getAttribute("ResponseBody")));
		testCaseResult.setResponseCode(String.valueOf(result.getAttribute("ResponseCode")));		
		testCaseResult.setUrl(String.valueOf(result.getAttribute("Url")));		
		testCaseResult.setResponseTime(String.valueOf(result.getAttribute("ResponseTime"))+" Milliseconds");
		testCaseResult.setRequestMethod(String.valueOf(result.getAttribute("requestMethod")));
		testCaseResult.setRequestBody(String.valueOf(result.getAttribute("RequestParameter")));
		resultSummary.put(testCaseResult.getTestCase()+":"+dateSelected,testCaseResult);
	}

	@Test(description = "BlueCoatCloud djn directprovider API", dataProvider="directProvider_Paylaod") 
	public void bluecoatcloud_DirectProvider_tests(String dateSelected, String payLoad) throws Exception {

		ITestResult result = org.testng.Reporter.getCurrentTestResult();
		long startTime = System.currentTimeMillis();
		HttpResponse apiResponse = blueCoatCloud_DirectProvider(restClient, payLoad/*, cookie*/);
		String getResponseBody = getResponseBody(apiResponse);
		long endTime = System.currentTimeMillis();	
		result.setAttribute("Url", BlueCoaSuiteUtils.generateDirectProviderUrl());
		result.setAttribute("ResponseBody", getResponseBody);
		result.setAttribute("ResponseCode", String.valueOf(apiResponse.getStatusLine().getStatusCode()));
		result.setAttribute("ResponseTime", String.valueOf(endTime-startTime));
		result.setAttribute("requestMethod", "POST");
		result.setAttribute("RequestParameter", dateSelected); 
		
		
		differentScenarioReporter(result, dateSelected);
		/*Assert.assertTrue(apiResponse.getStatusLine().getStatusCode() == 200,
				"No response from API.");*/

		directProvider = unmarshall(getResponseBody, DirectProvider.class);
		
		Assert.assertFalse(directProvider.getResult().isEmpty()); 

		System.out.println("Response.." + getResponseBody);
		org.testng.Reporter.log("Response from API.. " +getResponseBody); 
	}

	/*
	 * To generate customized HTML report.
	 */
	@AfterClass(alwaysRun = true)
	public synchronized void generateReport() throws Exception {
		FileWriter writer = new FileWriter("BlueCoat_Backend_Regression_Report.html");
		String htmlReport = "";
		boolean anyFailure = false;
		int serialNo	=	1;

		String loginURL = "https://portal.qa3.bluecoatcloud.com/login.jsp";

		htmlReport = "<html>" +"<head><style type=\"text/css\"> table { margin: 1em; border-collapse: collapse; } .BreakWord { word-break: break-all; } .instruction {border-width:2px; border-style:solid; padding: 10px} tr.fail{background: #FF0000} tr.pass{background: #FFFFFF} td {word-wrap:break-word; padding: .3em; border: 1px #ccc solid;overflow: hidden;} th { padding: .3em; border: 1px #ccc solid; } thead { background: #fc9; } </style></head>" +"<body>";
		htmlReport+= "<p> BLUE Coat Threat Pulse Url : <a href=" + loginURL+" >" + loginURL +  " </a> </p>";
		htmlReport = htmlReport + "<table> " +"<thead> " +"<tr>" +"<th> S.No </th>" +"<th>Service Call</th> " +"<th>Url</th>"+"<th>Request Parameter</th>"+"<th>ResponseTime</th>" +"<th>Response Code</th>" +"<th>Response Body" +"</tr>" +"</thead>" +"<tbody>";

		System.out.println("Forming Report HTML---");
		System.out.println("Total Test  "+resultSummary.size());
		int failcount=0;

		for(Map.Entry<String, TestCaseResult> entry : resultSummary.entrySet()){

			TestCaseResult result = entry.getValue();
			if(result.getResponseCode().equals("200")) { 
				anyFailure = false;
				htmlReport = htmlReport + "<tr class=\"pass\"><td>"+ serialNo++ +" </td>" +"<td>"+result.getTestCase();				
				htmlReport = htmlReport +"</td><td class=\"BreakWord\" width=\"60%\" >"+result.getRequestMethod()+" "+result.getUrl()+"<td>"+result.getRequestBody()+"</td>" +"<td>"+result.getResponseTime()+"</td>" +"<td>"+result.getResponseCode()+"</td>" +"<td>"+result.getResponseBody()+"</td>" +"</tr>";
			}
			else {		
				failcount++;
				anyFailure = true;
				htmlReport = htmlReport + "<tr class=\"fail\"><td>"+ serialNo++ +" </td>" +"<td>"+result.getTestCase();				
				htmlReport = htmlReport +"</td><td class=\"BreakWord\" width=\"40%\" >"+result.getRequestMethod()+" "+result.getUrl()+"<td>"+result.getRequestBody()+"</td>" +"<td>"+result.getResponseTime()+"</td>" +"<td>"+result.getResponseCode()+"</td>" +"<td>"+result.getResponseBody()+"</td>" +"</tr>";		
			}	
		}
		
		System.out.println("Total failures :"+failcount);
		System.out.println(anyFailure); 

		htmlReport += "</tbody>" +"</table><br/>";
		htmlReport += "<div class=\"instruction\"><h3>Instructions to run backend sanity</h3>" +"<ul><li>Go to ";

		htmlReport+="Hudson Build Plan</a></li>" +"<li>Click on Build Now, Will be available on left side</li>" +"<li>Wait untill you will see the results. If there are any failure, you will get an email</li></div>";
		htmlReport += "</body> </html>";
		writer.write(htmlReport);
		writer.flush();
		writer.close();

		System.out.println("report generated successfully");
	}
	
	@Test(description = "BlueCoatCloud djn directprovider API") 
	public void bluecoatcloud_DirectProvider_withoutPayload_tests() throws Exception {
		
		//String payLoad = "";
		String dateSelected = "";
		ITestResult result = org.testng.Reporter.getCurrentTestResult();
		long startTime = System.currentTimeMillis();
		HttpResponse apiResponse = blueCoatCloud_DirectProvider_negativeCheck(restClient, bluecoatAPIPayload/*, cookie*/);
		String getResponseBody = getResponseBody(apiResponse);
		long endTime = System.currentTimeMillis();	
		result.setAttribute("Url", BlueCoaSuiteUtils.generateDirectProviderUrl_negativeCheck());
		result.setAttribute("ResponseBody", getResponseBody);
		result.setAttribute("ResponseCode", String.valueOf(apiResponse.getStatusLine().getStatusCode()));
		result.setAttribute("ResponseTime", String.valueOf(endTime-startTime));
		result.setAttribute("requestMethod", "POST");
		result.setAttribute("RequestParameter", ""); 
		
		
		differentScenarioReporter(result, dateSelected);
		Assert.assertTrue(apiResponse.getStatusLine().getStatusCode() == 200,
				"No response from API.");


		System.out.println("Response.." + getResponseBody);
		org.testng.Reporter.log("Response from API.. " +getResponseBody);
	}
}
