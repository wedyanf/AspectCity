package org;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.html.htmlViewer;
import org.parsers.MainParser;

public class Main {
	/*
	 * AspectsCity Main Class
	 * @author: Reema Freihat
	 * @author: Fadi Wedyan
	 * @param args[0], folder of the program bytecode 
	 * @param  args[1], output folder, where xml and hmtl files are saved
	 * @version 1.0
	 * @since May 20, 2019
	 */
	 private static final Logger logger = Logger.getLogger(MainParser.class.getName());
	
	  public static void main(String []args){
		  /*if (args.length  !=2)
		  {	  
		     System.out.println("Usage: Main inDirectory outDirectory"); 
			  System.exit(0);
		  }*/
		  //String inDir = args[0];
		  //String outDir = args[1];
		  
		  String inDir = "c:\\AOP\\KettleV2\\bin";
		  String outDir = "c:\\v";
		  
		  MainParser mainparser= new MainParser(inDir, outDir);
		 // String outputFilename="ParsingInfo.xml";
		  try {
			mainparser.parse();
			mainparser.saveAdvices();
			mainparser.saveJoinPoints();
			mainparser.saveInfo();
		} catch (Exception e) {
			  logger.log(Level.INFO, e.getMessage(), e);			   
				
		}
		 // mainparser.saveAdvices(outDir);
		 // mainparser.saveJoinPoints(outDir);

		    htmlViewer.createHtmlJP(outDir+"\\"+MainParser.jpXML);
		    htmlViewer.createHtmlAdvices(outDir+"\\"+MainParser.advicesXML);
		   
		   //mainparser.createAspectsInfo();
		   //mainparser.createClassInfo();

		//   Reporter.computeAdvicesCoverage();
		  // Reporter.computeCoverage();
		   //mainparser.saveInfo(outDir);
		   //htmlViewer.createHtmlClasses(outDir+"\\"+outputFilename);
		   
		  }

}
