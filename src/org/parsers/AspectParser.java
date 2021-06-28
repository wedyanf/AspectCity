package org.parsers;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Iterator; 


import org.apache.bcel.util.ClassPath;
import org.apache.bcel.util.SyntheticRepository;
import org.aspectj.lang.reflect.Advice;
import org.aspectj.lang.reflect.AdviceKind;
import org.aspectj.lang.reflect.AjType;
import org.aspectj.lang.reflect.AjTypeSystem;
import org.info.MyAdvice;
//import org.aspectj.weaver.patterns.SignaturePattern;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.FieldGen;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.Type;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.ALOAD;

public class AspectParser {
	private static final Logger logger = Logger.getLogger(MainParser.class.getName());
	
	private java.lang.ClassLoader classloader;
	 int out;
	 int printlnString;
	 int set_ID;
	 int reporter;
	 public Map <String, MyAdvice> advices;
	 Class clas;					//aspect input class
	 JavaClass jclas;				//aspect input class
	 public AjType aspectClass;		//aspect as provided by the reflect library

    //constants
public static final  String BEFORE  = "before";
public static final  String AFTER  = "after";
public static final  String AFTER_RETURNING  = "afterReturning";
public static final  String AFTER_THROWING  = "afterThrowing";
public static final  String AROUND  = "around";

public AspectParser(){
	advices = new java.util.HashMap <String, MyAdvice>();
}


private boolean processAfterBefore(String adviceID, String SN,
		String AspectName, String adviceType, String mName){
	MyAdvice myadvice;
	myadvice = new MyAdvice(adviceID,SN);
	String strDeclaringAspect = AspectName;
	strDeclaringAspect = strDeclaringAspect.replaceAll("_", ".");
	myadvice.setDeclaringAspectName(strDeclaringAspect);


	boolean done =false;
	if (adviceType.equalsIgnoreCase(AFTER)){
		//after advice
	  myadvice.setKind(AdviceKind.AFTER);
	  done=true;
    }else
   	if (adviceType.equalsIgnoreCase(AFTER_RETURNING)){
		//after returning advice
		myadvice.setKind(AdviceKind.AFTER_RETURNING);
		done=true;
	}else
	if (adviceType.equalsIgnoreCase(BEFORE)){
			//before advice
    		myadvice.setKind(AdviceKind.BEFORE);
    		done=true;
	}if (adviceType.equalsIgnoreCase(AFTER_THROWING)){
		myadvice.setKind(AdviceKind.AFTER_THROWING);
		done=true;
	}
	if (done){
		advices.put(mName, myadvice);
		return true;
	}
	else    	
	return false;
}

private void processAround(
		org.apache.bcel.classfile.Method method, String []tokens){
		                     
	MyAdvice myadvice;
	String adviceID = tokens[4];
	String SN = tokens[3];
	String mName=method.getName();
	myadvice = new MyAdvice(adviceID,SN);
	myadvice = new MyAdvice(tokens[4], tokens[3]);
	String strDeclaringAspect = tokens[2];
	strDeclaringAspect = strDeclaringAspect.replaceAll("_", ".");

	myadvice.setDeclaringAspectName(strDeclaringAspect);
	myadvice.setKind(AdviceKind.AROUND);
	advices.put(mName, myadvice);	     
	
}

public void parse( AjType ac,JavaClass javaclas) throws Exception{
	aspectClass=ac;
	MyAdvice myadvice=null;
	this.jclas = javaclas; 
	if (jclas==null) 
		throw new Exception("Can't parse aspect bytecode since the reference is null");
	
	try{
		System.out.println("Parsing Aspect: "+ aspectClass.getName());
      	 org.apache.bcel.classfile.Method []m= jclas.getMethods();
     	
       	for (int i=0;i<m.length; i++)
    	{
   		 String mName=m[i].getName();
    	 if (mName.startsWith("ajc")){
    		String []tokens = mName.split("\\$");
        	if (!(tokens ==null) &&tokens.length ==5)
        	{
        	 if (!(processAfterBefore(tokens[4], tokens[3],tokens[2],tokens[1],mName)))
        	  {		//not before or after advice, check if around
        			if (tokens[1].equalsIgnoreCase(AROUND) && 
        					!tokens[4].endsWith("proceed")){
        				     processAround(m[i], tokens);
        			}    			
        		}
        	} //length=5
          } //if ajc
         } //for
    	
     }catch (Exception e) {
    	 logger.log(Level.INFO, e.getMessage(), e);		
		 throw e;
	  }
}

}
