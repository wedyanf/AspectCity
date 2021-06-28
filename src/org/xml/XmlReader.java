package org.xml;

import java.io.File;
import java.util.Map;
import org.aspectj.lang.reflect.AdviceKind;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Attr;

import org.info.*;
import org.parsers.AspectParser;

public class XmlReader {
	
	public Map <String, MyJoinPoint> readJPMaptoXML(String filename){
        Map <String, MyJoinPoint> joinpoints;
        joinpoints = new java.util.HashMap <String, MyJoinPoint>();
    	try {
    		  File file = new File(filename);
    		  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    		  DocumentBuilder db = dbf.newDocumentBuilder();
    		  Document doc = db.parse(file);
    		  doc.getDocumentElement().normalize();
    		  //System.out.println("Root element " + doc.getDocumentElement().getNodeName());
    		  NodeList nodeLst = doc.getElementsByTagName("jp");
    		  //System.out.println("Information of all joinpoints");
    		  for (int s = 0; s < nodeLst.getLength(); s++) {
   			    Node fstNode = nodeLst.item(s);	    
    			if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
    			  Element fstElmnt = (Element) fstNode;
    			  Attr key =fstElmnt.getAttributeNode("JPkey");
    
    			  NodeList ElmntLst = fstElmnt.getElementsByTagName("AdviceID");
    			  Element AdviceIDElmnt = (Element) ElmntLst.item(0);
    			  NodeList ID = AdviceIDElmnt.getChildNodes();
    			  String strID= ((Node) ID.item(0)).getNodeValue();
    			  
    			  ElmntLst = fstElmnt.getElementsByTagName("SN");
    			  Element SNElmnt = (Element) ElmntLst.item(0);
    			  NodeList SN = SNElmnt.getChildNodes();
    			  String strSN=((Node) SN.item(0)).getNodeValue();
    			  
    			  ElmntLst = fstElmnt.getElementsByTagName("AspectName");
    			  Element AspectNameElmnt = (Element) ElmntLst.item(0);
    			  NodeList AspectName = AspectNameElmnt.getChildNodes();
    			  String strAspectName=((Node) AspectName.item(0)).getNodeValue();
    			  
    			  ElmntLst = fstElmnt.getElementsByTagName("ClassName");
    			  Element ClassNameElmnt = (Element) ElmntLst.item(0);
    			  NodeList ClassName = ClassNameElmnt.getChildNodes();
    			  String strClassName =((Node) ClassName.item(0)).getNodeValue();
    			  
    			  ElmntLst = fstElmnt.getElementsByTagName("AdviceKind");
    			  Element AdviceKindElmnt = (Element) ElmntLst.item(0);
    			  NodeList AdviceKind = AdviceKindElmnt.getChildNodes();
    			  String strAdviceKind= ((Node) AdviceKind.item(0)).getNodeValue();

    			  ElmntLst = fstElmnt.getElementsByTagName("Counter");
    			  Element CounterElmnt = (Element) ElmntLst.item(0);
    			  NodeList Counter = CounterElmnt.getChildNodes();
    			  String strCounter= ((Node) Counter.item(0)).getNodeValue();
    			  
    			  MyJoinPoint jp = new MyJoinPoint(strID, strSN,strAspectName);
	    		  jp.setClassName(strClassName);
	    		  jp.setAdviceKind(getAK(strAdviceKind));
	    		  joinpoints.put(key.getValue(), jp);
       		     }
    		  }
       	}catch (Exception e) {
			 System.out.println("error: " + e.getMessage());
			}
    	  return joinpoints;
    }
	org.aspectj.lang.reflect.AdviceKind getAK(String ak){
		org.aspectj.lang.reflect.AdviceKind result=org.aspectj.lang.reflect.AdviceKind.AFTER;
		if (ak.equalsIgnoreCase(AspectParser.AFTER))
			result= org.aspectj.lang.reflect.AdviceKind.AFTER;
		if (ak.equalsIgnoreCase(AspectParser.AFTER_RETURNING))
			result= org.aspectj.lang.reflect.AdviceKind.AFTER_RETURNING;
		if (ak.equalsIgnoreCase(AspectParser.AFTER_THROWING))
			result= org.aspectj.lang.reflect.AdviceKind.AFTER_THROWING;
		if (ak.equalsIgnoreCase(AspectParser.BEFORE))
			result= org.aspectj.lang.reflect.AdviceKind.BEFORE;
		if (ak.equalsIgnoreCase(AspectParser.AROUND))
			result= org.aspectj.lang.reflect.AdviceKind.AROUND;
		return result;
	}
	public Map <String, MyAdvice> readAdvicesMaptoXML(String filename){
        Map <String, MyAdvice> advices;
        advices = new java.util.HashMap <String, MyAdvice>();
    	try {
    		  File file = new File(filename);
    		  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    		  DocumentBuilder db = dbf.newDocumentBuilder();
    		  Document doc = db.parse(file);
    		  doc.getDocumentElement().normalize();
    		  //System.out.println("Root element " + doc.getDocumentElement().getNodeName());
    		  NodeList nodeLst = doc.getElementsByTagName("advice");
    		  
    		  for (int s = 0; s < nodeLst.getLength(); s++) {
   			    Node fstNode = nodeLst.item(s);	    
    			if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
    			  Element fstElmnt = (Element) fstNode;
    			  Attr key =fstElmnt.getAttributeNode("key");
    			//  System.out.println(key.getValue());
    			  NodeList ElmntLst = fstElmnt.getElementsByTagName("ID");    			    
    			  Element AdviceIDElmnt = (Element) ElmntLst.item(0);
    			  NodeList ID = AdviceIDElmnt.getChildNodes();
    			  String strID= ((Node) ID.item(0)).getNodeValue();

    			  ElmntLst = fstElmnt.getElementsByTagName("SN");
    			  Element SNElmnt = (Element) ElmntLst.item(0);
    			  NodeList SN = SNElmnt.getChildNodes();
    			  String strSN= ((Node) SN.item(0)).getNodeValue();
    			  
    			  ElmntLst = fstElmnt.getElementsByTagName("AspectName");
    			  Element AspectNameElmnt = (Element) ElmntLst.item(0);
    			  NodeList AspectName = AspectNameElmnt.getChildNodes();
    			  String strAspectName=((Node) AspectName.item(0)).getNodeValue();
    			  
    			  ElmntLst = fstElmnt.getElementsByTagName("AdviceKind");
    			  Element AdviceKindElmnt = (Element) ElmntLst.item(0);
    			  NodeList AdviceKind = AdviceKindElmnt.getChildNodes();
    			  String strAdviceKind= ((Node) AdviceKind.item(0)).getNodeValue();
     			  MyAdvice myadvice = new MyAdvice(strID, strSN);
    	    	  myadvice.setDeclaringAspectName(strAspectName);
    	    	  myadvice.setKind(getAK(strAdviceKind));
    	    	  advices.put(key.getValue(), myadvice);
    		     }
    		  }
    		  
    	}catch (Exception e) {
			 System.out.println("error: " + e.getMessage());
			
		}
    	  return advices;
    }

}
