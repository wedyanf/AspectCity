package org.info;

import java.util.Iterator;
import java.util.Map;

public class AspectInfo {
	String AspectName;
	int numAdvices;
  	int numAdvicesJP=0;
 	
    double coverage;
    public Map <String, MyAdvice> advices;
    public AspectInfo(String aspectName){
    	this.AspectName=aspectName;
    	advices=new java.util.HashMap  <String, MyAdvice>();
    }
    public void create(Map <String, MyAdvice> Advices){
    	/*
    	 * Create the asp
    	 */
    }
    public MyAdvice getAdvice(String key){
    	return  advices.get(key);
    }
    
    public void addAdvice(String key, MyAdvice advice){
    	
    	MyAdvice cachedAdvice =
	    	(MyAdvice) advices.get(key);
	    if (cachedAdvice==null){
	    	//advice belongs to the aspect, add it
	    	advices.put(key, advice);
	    }
	}
    
    public double getCoverage(){
    	return coverage;
    }
   
    public int numAdvicesJP(){
    	return numAdvicesJP;
    }
    public int numAdvices(){
    	return advices.size();
    }
    
    public Map <String, MyAdvice> getAdvices(){
    	return advices;
    }
}
