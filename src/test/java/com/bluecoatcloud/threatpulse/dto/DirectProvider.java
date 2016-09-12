package com.bluecoatcloud.threatpulse.dto;

import java.util.ArrayList;

public class DirectProvider {

	private int tid;

	public int getTid() { return this.tid; }

	public void setTid(int tid) { this.tid = tid; }

	private String action;

	public String getAction() { return this.action; }

	public void setAction(String action) { this.action = action; }

	private String method;

	public String getMethod() { return this.method; }

	public void setMethod(String method) { this.method = method; }

	private ArrayList<Integer> result;

	public ArrayList<Integer> getResult() { return this.result; }

	public void setResult(ArrayList<Integer> result) { this.result = result; }

	private String type;

	public String getType() { return this.type; }

	public void setType(String type) { this.type = type; }


}
