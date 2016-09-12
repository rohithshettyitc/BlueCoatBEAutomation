package com.bluecoatcloud.threatpulse.dataProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bluecoatcloud.threatpulse.constants.CommonConstants;
import com.bluecoatcloud.threatpulse.reporting.PerformanceData;

public class TestEnvironment {
	private String testEnv;
	private static TestEnvironment env;
	private com.bluecoatcloud.threatpulse.reporting.Reporter reporter;
    private static List<PerformanceData> perfData;
    private boolean performanceLoggingOn;
    private boolean debugLoggingOn;
    
	/**
	 * 
	 */
	 public static synchronized TestEnvironment getTestEnvironmentObject (String theBrowserType,String theTestEnv, String theTenantId, String theUrl){
		 if (env == null)
			 // it's ok, we can call this constructor
			 env = new TestEnvironment();
		 env.setTestEnv(theTestEnv);
		 return env;
	 }

	 /**
	  * @throws IOException 
	  * 
	  */
	 public static synchronized TestEnvironment getTestEnvironmentObject (Map<String, String> theTestEnvData) throws IOException{
		 if (env == null)
			 env = new TestEnvironment();
		 env.setTestEnv(theTestEnvData.get(CommonConstants.KEY_TEST_ENVIRONMENT));
		 return env;
	 }

	 /**
	  * 
	  * @return
	  */
	 public static synchronized TestEnvironment getTestEnvironmentObject(){
		 if (env == null)
			 // it's ok, we can call this constructor
			 env = new TestEnvironment();
		 return env;
	 }

	 /**
	  * 
	  */
	 private TestEnvironment(){

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

	public void setReporter(com.bluecoatcloud.threatpulse.reporting.Reporter reporter) {
		this.reporter = reporter;
	}

	public com.bluecoatcloud.threatpulse.reporting.Reporter getReporter() {
		return reporter;
	}

	public static void setPerfData(PerformanceData thePerfData) {
		if (perfData == null){
			perfData = new ArrayList<PerformanceData>();
		}  
		perfData.add(thePerfData);
	}

	public List<PerformanceData> getPerfData() {
		return perfData;
	}

	public boolean isDebugLoggingOn() {
		return debugLoggingOn;
	}

	public void setDebugLoggingOn(boolean debugLoggingOn) {
		this.debugLoggingOn = debugLoggingOn;
	}

	public boolean isPerformanceLoggingOn() {
		return performanceLoggingOn;
	}

	public void setPerformanceLoggingOn(boolean performanceLoggingOn) {
		this.performanceLoggingOn = performanceLoggingOn;
	}

	

}
