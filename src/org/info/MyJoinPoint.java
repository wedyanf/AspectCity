package org.info;

import org.aspectj.lang.reflect.AdviceKind;

public class MyJoinPoint
{
	org.aspectj.lang.JoinPoint aJoinPoint;
	//add the following members to the Join Point
	String AdviceID;
	String AdviceSerialNumber;
	AdviceKind advicekind;
	int lineNumber; //in bytecode
	String aspectName;  //declaring aspect
	String methodName="";    //advises method shortname
	String clasName;		//class where the joinpoint resides
	
	public MyJoinPoint(String AdviceID, String AdviceSerialNumber, 
			  		    String AspectName){
		this.AdviceID = AdviceID;
		this.AdviceSerialNumber = AdviceSerialNumber;
		this.aspectName=AspectName;
	}
	
	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getClassName(){
		return clasName;
	}
	public void setClassName(String clasName){
		this.clasName=clasName;
	}
	
	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getAspectName()
	{
	  return aspectName;	
	}
	public void setAspectName(String AspectName)
	{
	this.aspectName=AspectName;	
	}
	 public void setAdviceID(String ID) {
		 AdviceID =ID;
	 }
	 public String  getAdviceID(){
		 return AdviceID;
	 }
	 public void setAdviceSerialNumber(String sn){
		 AdviceSerialNumber = sn;
	 }
	 public String getAdviceSerialNumber(){
		return AdviceSerialNumber; 
	 }
	 public void setAdviceKind(AdviceKind ak)	 {
		 advicekind=ak;
	 }
	 public AdviceKind getAdviceKind(){
		 return advicekind;}
	 
	 public Object[] getArgs() {
		 //get arguments passed to the join point
		 return aJoinPoint.getArgs();
	 }
	 
	 public  org.aspectj.lang.Signature 	getSignature() {
		 // Returns the signature at the join point.
		 return aJoinPoint.getSignature();
	 }
	 public org.aspectj.lang.JoinPoint.StaticPart 	getStaticPart() {
		 return aJoinPoint.getStaticPart();
	 }
}

