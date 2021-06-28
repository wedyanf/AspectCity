package org.info;

import java.util.Map;

import org.aspectj.lang.reflect.Advice;
import org.aspectj.lang.reflect.AdviceKind;
import org.aspectj.lang.reflect.AjType;
import org.aspectj.lang.reflect.PointcutExpression;

/*
/*
 *   @author: Reema Freihat
	 @author: Fadi Wedyan
 * The class provides information about each advice, along with a list of joinpoints the advice pointcut matches
 * Runtime representation of an advice declaration inside an aspect 
 * Last Update: May 21, 2019
 *  * Last modification; May 23, 2019
 */

public class MyAdvice
{
	String key; 	//advicekind$ID$SN
	
	Advice anAdvice;
	public String ID;
	public String SerialNumber;
	AdviceKind ak;
	String aspectName;
	private Map <String, MyJoinPoint>  joinpoints;
	
	public MyAdvice(String ID, String SerialNumber){
		this.SerialNumber=SerialNumber;
		this.ID=ID;
		joinpoints = new java.util.HashMap <String,  MyJoinPoint> ();
		
	}
	public String getSerialNumber() {
		return SerialNumber;
	}
	
	public void setKind(AdviceKind ak) {
		this.ak=ak;
	}
	public AdviceKind 	getKind() {
		return this.ak;
	}
	
	public void setDeclaringAspectName(String declaring_aspect){
		//the declaring aspect
		this.aspectName=declaring_aspect;
	}
	public String getDeclaringAspectName(){
		//the declaring aspect
		
		return this.aspectName;
	}

	public String getID() {
		return ID;
	}
	
	public int getNumJP() {
		return joinpoints.size();
	}
	public Map <String, MyJoinPoint> getJoinpoints(){
		return joinpoints;
	}
    public String getAdvicekind()
    { return getKind().toString();}
    

	/*//encapsulate the advice methods
	public AjType getDeclaringType(){
		//the declaring aspect
		return anAdvice.getDeclaringType();
	}
	public AjType<?>[] 	getParameterTypes() {
		return anAdvice.getParameterTypes();
	}
	
	public AdviceKind 	getKind() {
		return anAdvice.getKind();
	}
	public String getName() { 
		return anAdvice.getName();
	}
	public PointcutExpression 	getPointcutExpression() {
		return anAdvice.getPointcutExpression();
	}
	public void setAdvice(Advice a) {
		anAdvice =a;
	}
	public Advice getAdvice(){
		return anAdvice;
	}
*/	
	
}

/*
public class AdviceCoverageInfo {
	String key; 	//advicekind$ID$SN
	MyAdvice advice;
	int numJP; 

    public Map <String, MyJoinPoint> AdviceJoinPoints;
    
    public AdviceCoverageInfo(String key, MyAdvice advice){
    	this.advice=advice; 
    	AdviceJoinPoints=new java.util.HashMap  <String, MyJoinPoint>();
    }
    public String adviceID()
    { return advice.ID;}
    public String adviceSN(){
    	return advice.SerialNumber;
    }
    public String Advicekind()
    { return advice.getKind().toString();}
    public void addJP(String key, MyJoinPoint jp){
    	AdviceJoinPoints.put(key, jp);
    }
    public int numJP(){
    	return AdviceJoinPoints.size();
    }
    public Map <String, MyJoinPoint> getJPS(){
    	return AdviceJoinPoints;
    }
    
}
*/