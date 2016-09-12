package com.bluecoatcloud.threatpulse.reporting;

public class PerformanceData {
	    public String getMethodType() {
		return methodType;
	}
	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}
		String methodType;
		String method;
		String ComponentName;
		String responseTime;
		
		public String getMethod() {
			return method;
		}
		public void setMethod(String method) {
			this.method = method;
		}
		public String getComponentName() {
			return ComponentName;
		}
		public void setComponentName(String componentName) {
			ComponentName = componentName;
		}
		public String getResponseTime() {
			return responseTime;
		}
		public void setResponseTime(String responseTime) {
			this.responseTime = responseTime;
		}
}
