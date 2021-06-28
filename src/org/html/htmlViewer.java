package org.html;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
public class htmlViewer {
public static void main(String []args){
	String inDir="d:\\aop2\\temp";
	 createHtmlJP(inDir+"\\jp.xml");
	 createHtmlClasses(inDir+"\\coverageInfo.xml");
	 createHtmlAdvices(inDir+"\\advices.xml");
}
public static void createHtmlAdvices(String filepath){
try{
		
		TransformerFactory factory = TransformerFactory.newInstance();
		StreamSource stylesheet = new StreamSource("style\\"+"advices.xsl");
		Transformer transformer = factory.newTransformer(stylesheet);
		StreamSource source = new StreamSource(filepath);
		String OutFilepath = filepath.substring(0, filepath.lastIndexOf("."));
		OutFilepath = OutFilepath+".html";
		StreamResult result = 
		    new StreamResult(new FileOutputStream(OutFilepath));
		transformer.transform(source, result);	
	}
	  catch (Exception e) {
		   System.out.println("error: " + e.getMessage());
	}
	
}
public static void createHtmlJP(String filepath){
try{
		
		TransformerFactory factory = TransformerFactory.newInstance();
		StreamSource stylesheet = new StreamSource("style\\"+"jp.xsl");
		Transformer transformer = factory.newTransformer(stylesheet);
		StreamSource source = new StreamSource(filepath);
		String OutFilepath = filepath.substring(0, filepath.lastIndexOf("."));
		OutFilepath = OutFilepath+".html";
		StreamResult result = 
		    new StreamResult(new FileOutputStream(OutFilepath));
		transformer.transform(source, result);	
	}
	  catch (Exception e) {
		   System.out.println("error: " + e.getMessage());
	}
}
public static void createHtmlClasses(String filepath){
	try{
			
		TransformerFactory factory = TransformerFactory.newInstance();
		StreamSource stylesheet = new StreamSource("style\\"+"coverageInfo.xsl");
		Transformer transformer = factory.newTransformer(stylesheet);
		StreamSource source = new StreamSource(filepath);
		String OutFilepath = filepath.substring(0, filepath.lastIndexOf("."));
		OutFilepath = OutFilepath+".html";
		StreamResult result = 
				
		    new StreamResult(new FileOutputStream(OutFilepath));
		transformer.transform(source, result);	
	 }catch (Exception e) {
		   System.out.println("error: " + e.getMessage());
	 }
 }
}
