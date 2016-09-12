package com.bluecoatcloud.threatpulse.utils;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class EvaluateXPath {

	private XPathFactory factory;
	private XPath xPath;

	public EvaluateXPath() {
		factory = XPathFactory.newInstance(); 
		xPath = factory.newXPath();
	}

	public String validateXPath(String XPath, String xmlResponse) 
	throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {	
		XPathExpression expr = xPath.compile(XPath);
		return expr.evaluate(getDocument(XPath, xmlResponse));		
	}
	
	public NodeList getNodeList(String XPath, String xmlResponse) throws XPathExpressionException, SAXException, IOException, ParserConfigurationException {
		XPathExpression expr = xPath.compile(XPath);
		Object result = expr.evaluate(getDocument(XPath, xmlResponse), XPathConstants.NODESET);
		NodeList nodes = (NodeList) result;
		return nodes;
	}
	
	private Document getDocument(String XPath, String xmlResponse) throws SAXException, IOException, XPathExpressionException, ParserConfigurationException {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true); // never forget this!
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(xmlResponse)));
		return doc;
	}
}
