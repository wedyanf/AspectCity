package org.parsers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Iterator; 

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.util.ClassPath;
import org.apache.bcel.util.SyntheticRepository;
import org.aspectj.lang.reflect.*;
import org.html.htmlViewer;
import org.xml.XmlWriter;

import org.info.*;

public class MainParser {
/*
 * Phase one, parse the aspects and construct a list of advises
 * Phase two, parse classes and find the join points, attach 
 * join points to advises
 */

  private static final Logger logger = Logger.getLogger(MainParser.class.getName());
	
  public static Map <String, MyAdvice> allAdvices;
  public static Map <String, MyJoinPoint> allJoinPoints;
  public static Map <String, ClassInfo> allClasses =
			new java.util.HashMap <String,ClassInfo>();
  public static Map <String, AspectInfo> allAspects =
			new java.util.HashMap <String,AspectInfo>();
	
  private java.lang.ClassLoader classloader;
  private Class clas;
  AspectParser ap;
  ClassParser cp; 
  private String inDir = "c:\\AOP\\KettleV2\\bin";
  private String outDir = "c:\\v";
  
  public MainParser(String inDir, String outDir) {
	  ap = new AspectParser();
	  cp = new ClassParser();
	  this.inDir =inDir;
	  this.outDir =outDir;
		
  }
  /*
   * Default xml files names
   */
  public static final  String jpXML = "joinpoints.xml";
  public static final  String aspectsXML ="aspects.xml";
  public static final  String advicesXML ="advices.xml";
  public static final  String classesXML ="classes.xml";
  
  public static void main(String []args){
  /*if (args.length  !=1)
  {	  
	  System.exit(0);
  }*/
 
	  
	  
 // MainParser mainparser= new MainParser(inDir, outDir);
  //String outputFilename="ParsingInfo.xml";
  try {
	//mainparser.parse(inDir, outDir);
	//mainparser.saveAdvices(outDir);
	//mainparser.saveJoinPoints(outDir);
	  
} catch (Exception e) {
	  logger.log(Level.INFO, e.getMessage(), e);
}


   //htmlViewer.createHtmlJP(outDir+"\\jp.xml");
   //htmlViewer.createHtmlAdvices(outDir+"\\advices.xml");
   
   //mainparser.createAspectsInfo();
   //mainparser.createClassInfo();

//   Reporter.computeAdvicesCoverage();
  // Reporter.computeCoverage();
   //mainparser.saveInfo(outDir);
   //htmlViewer.createHtmlClasses(outDir+"\\"+outputFilename);
   
  }
  
  	
public  void saveInfo() throws IOException{
			  
			  XmlWriter xmlwriter = new XmlWriter(outDir+"\\"+"ParsingInfo.xml");
			  xmlwriter.writeInfo(allClasses, allAspects);
		}
  public  void createAspectsInfo(){
		
		int mapsize = allAdvices.size();
		//the key is the class name
		System.out.println("number of advices = "+mapsize+"-------------------------------------");
		Iterator keyValuePairs1 = allAdvices.entrySet().iterator();
		for (int i = 0; i < mapsize; i++){
			Map.Entry entry = (Map.Entry) keyValuePairs1.next();
		    Object key = entry.getKey();
		    Object value = entry.getValue();
		    MyAdvice advice = (MyAdvice) value;
		    
		    String tempAdviceID=advice.ID;
		    String tempAdviceSN =advice.SerialNumber;
		    String tempAspectName=advice.getDeclaringAspectName();
		    AdviceKind advicekind =advice.getKind();
		    AspectInfo cachedAspect =
		    	(AspectInfo) allAspects.get(tempAspectName);
		    
		    if (cachedAspect==null){
		    	//new advice for new Aspect, add to allAspects
		    	cachedAspect = new AspectInfo(tempAspectName);
		    	String Akey;
		    	Akey=advicekind+"$"+tempAdviceID+"$"+tempAdviceSN;
		    	cachedAspect.addAdvice(Akey, advice);
		       	allAspects.put(tempAspectName, cachedAspect);
		    }
		    else{
		     //add an advice to existing aspect	
		    	String Akey;
		    	Akey=advicekind+"$"+tempAdviceID+"$"+tempAdviceSN;
		    	cachedAspect.addAdvice(Akey, advice);
		       	allAspects.put(tempAspectName, cachedAspect);
		    }
		}
	}
	
  public  void createClassInfo(){
	/*
	 * Organize joinpoints into classes
	 */
	
		//get the class names that has join points 
		int mapsize = allJoinPoints.size();
		//the key is the class name
		
		Iterator keyValuePairs1 = allJoinPoints.entrySet().iterator();
		for (int i = 0; i < mapsize; i++){
			Map.Entry entry = (Map.Entry) keyValuePairs1.next();
		    Object key = entry.getKey();
		    Object value = entry.getValue();
		    MyJoinPoint jp = (MyJoinPoint) value;
		    String tempClassName = jp.getClassName();
		    		    
		    ClassInfo cachedClass =
		    	(ClassInfo) allClasses.get(tempClassName);
		    if (cachedClass==null){
		    	//new class, add to allClasses
		    	cachedClass = new ClassInfo(tempClassName);
		    	cachedClass.addJP((String) key, jp);
		    	allClasses.put(tempClassName, cachedClass);
		    }else{
		    	//update existing class
		    	cachedClass.addJP((String) key, jp);
		    	allClasses.put(tempClassName, cachedClass);
		    }
		 }  //for loop
		
	}
 
  /*
   * @author Fadi Wedyan
   * @author Reema Feihat
   * Assume that dir is a directory.  List its contents, including the contents of sub-directories at all levels.
   */
  void listContents( File inDir, File outDir ) throws Exception{
      
	  try{
 		  
		   String[] files;  // The files in the input directory
		   files = inDir.list();
		  
		   MyClassLoader jclassloader = new MyClassLoader(inDir.getPath());
		   for (int i = 0; i < files.length; i++) {
		       File f_in, f_out;  // One of the files in the directory.
		       f_in = new File(inDir, files[i]);
		       f_out = new File(outDir, files[i]);
		       if ( f_in.isDirectory() ) {
		              // Call listContents() recursively to
		              // list the contents of the directory, f.
		    	   listContents(f_in, f_out);
		       }
		       else {
		    	   // a file
		           String filename = files[i];
		           if (filename.endsWith(".class")&& !filename.contains("$"))
		           {
		        	   processClass(jclassloader, files[i], 
		        			   inDir.getPath(), outDir.getPath());
		           }
		      }
		   }
	  }
	  catch (IOException e) {
		  logger.log(Level.INFO, e.getMessage(), e);
		   
		  throw e;
	} 
   } // end listContents()

void processClass(MyClassLoader jclassloader, String filename, 
				  String inDir, String outDir) throws Exception{
//	Class clas;				//class bytecode for the aspect 
	JavaClass jclas;		//javaclass for the aspect
	AjType aspectClass;		//aspect as provided by the reflect library
	try{
		filename = filename.substring(0, filename.lastIndexOf("."));
		jclas = jclassloader.loadJavaClass(filename);
		if (!(jclas==null)) {System.out.println("loading java class: "+jclas.getClassName());}
		
		//filename=filename.replace(".", "/");
		clas = classloader.loadClass(jclas.getClassName());
		if (!(clas==null)){
			System.out.println("loading class "+clas.getName());	
		}
		//String oldClassName = jclas.getClassName();
		String newClassName = filename+".class";
		aspectClass = AjTypeSystem.getAjType(clas);
	  	if (aspectClass.isAspect()){
	  		ap.parse(aspectClass, jclas);
	  		//jclas.dump(outDir+"\\"+newClassName);
	  	} //else
	  	if (jclas.isClass() )
	  	{ System.out.println("Parsing Class: "+ jclas.getClassName());
	  	  try{
	  		 jclas= cp.parse(jclas, inDir);
	  		 jclas.dump(outDir+"\\"+newClassName);
	     	 System.out.println("Class: "+ jclas.getClassName()+" Parsed Successfully");	 
	  	   } catch (Exception e) {
				   System.out.println("error: " + e.getStackTrace());
			}  
	  	 }	
	}
	catch (Exception e) {
		logger.log(Level.INFO, e.getMessage(), e);
		   
		  throw e;
	}
	
}

/*
 * @author Fadi Wedyan
 * @since May, 2008
 * This method is the main entry to the parsing process
 * Last upodate: May 2019
 * @param inDir where the location of the program bytecode
 * @param ouDir output xml files location 
 */
public void parse() throws Exception{
		 		 	
	 File in_directory = new File(inDir);
	 File out_directory = new File(outDir);
	 try{
	 URL url = in_directory.toURL();
	 URL[] urls = new URL[]{url};
	 classloader = new URLClassLoader(urls);
	 }
	 catch (Exception e) {
		 logger.log(Level.INFO, e.getMessage(), e);
		   
		  throw e;
		 }
     listContents(in_directory, out_directory);
     allJoinPoints = cp.MapJoinPoints;
	 allAdvices = ap.advices ;
	 createAspectsInfo();
	 createClassInfo();

    }


 
public void saveAdvices() throws IOException{
	  XmlWriter xmlwriter = new XmlWriter(outDir+"\\"+advicesXML);
	  xmlwriter.writeAdvicesMapToXML(allAdvices);
	  
	 /* int mapsize = allAdvices.size();
	  Iterator keyValuePairs1 = allAdvices.entrySet().iterator();
	  System.out.println("==============================");
	  System.out.println("Advices list");
	  for (int i=0;i<mapsize;i++){
		Map.Entry entry = (Map.Entry) keyValuePairs1.next();
		Object key = entry.getKey();
		Object value = entry.getValue();
		MyAdvice myadvice = (MyAdvice) value;
		System.out.println("==============================");
		System.out.println("Advice key ="+key.toString());
  		System.out.println("Advice Byte Name:"+myadvice.ID);
  		System.out.println("Advice Serial Number:"+myadvice.SerialNumber );
  		System.out.println("Declaring aspect : "+myadvice.getDeclaringAspectName());
  		System.out.println("Advice kind : "+myadvice.getKind());
  	}*/
  }
public void saveJoinPoints() throws Exception{
	  
	  XmlWriter xmlwriter = new XmlWriter(outDir+"\\"+ jpXML);
	  xmlwriter.writeJPMapToXML(allJoinPoints);
	  int mapsize = allJoinPoints.size();
	  Iterator keyValuePairs1 = allJoinPoints.entrySet().iterator();
	  System.out.print("============================= ");
	 System.out.println("number of JP = "+mapsize);
	 
	 /*for (int i = 0; i < mapsize; i++)
	  {
	    Map.Entry entry = (Map.Entry) keyValuePairs1.next();
	    Object key = entry.getKey();
	    Object value = entry.getValue();
	    MyJoinPoint jp = (MyJoinPoint) value;
	    System.out.println("==============================");
		System.out.println("JP key ="+key.toString());
		System.out.println("In Class "+jp.getClassName() );
	    System.out.println("advice ID "+jp.getAdviceID());
	    System.out.println("advice SN "+jp.getAdviceSerialNumber());
	    System.out.println("advice Aspect "+jp.getAspectName());
	    System.out.println("advice kind "+jp.getAdviceKind());
	  }
	*/ 
  }

}
