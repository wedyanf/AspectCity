package org.info;

import java.util.Iterator;
import java.util.Map;

/*
 * Rename class into ClassJoinPoints
 * Date May 25, 2019
 */

public class ClassInfo {
	String className;
	
    public Map <String, MyJoinPoint> ClassJoinPoints;
    
    
    public ClassInfo(String className){
    	this.className=className;
    	ClassJoinPoints=new java.util.HashMap  <String, MyJoinPoint>();
    }
    public void addJP(String key, MyJoinPoint jp){
    	ClassJoinPoints.put(key, jp);
    }
    
    public int numJP(){
    	return ClassJoinPoints.size();
    }
    
    public Map <String, MyJoinPoint> getJPS(){
    	return ClassJoinPoints;
    }
}
