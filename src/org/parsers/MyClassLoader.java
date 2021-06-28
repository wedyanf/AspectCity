package org.parsers;


import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.util.ClassPath;
import org.apache.bcel.util.SyntheticRepository;
import org.apache.bcel.Repository;

public class MyClassLoader
   extends ClassLoader {

   private String mDirectory;
   SyntheticRepository ss;
   JavaClass jclas;
   Class clas;
   private java.lang.ClassLoader classloader;
   
   public MyClassLoader(String pDirectory) {
       super();
       mDirectory = pDirectory;
       ss = SyntheticRepository.getInstance(new ClassPath(mDirectory));
   }
   public MyClassLoader(){
	   
   }
   protected Class loadClass(String name, boolean resolve) throws java.lang.ClassNotFoundException {
       clas=findLoadedClass(name);
       if(clas!=null) {
           return clas;
       }
       if(name.startsWith("java")) {
           return getParent().loadClass(name);
       }
       clas=findClass(name);
       if(clas==null) {
           return null;
       }
       if(resolve) {
           resolveClass(clas);
       }
       return clas;
   }

   /** Defines the class.
    * @param className Guess what.
    * @return The loaded class.
    */
   protected Class findClass(String className) {
     jclas=classWithTracing(className);
       if(jclas==null) {
           return null;
       }
       byte[] b = jclas.getBytes();
       return defineClass(className, b, 0, b.length);
   }
	public Class getClass(JavaClass jclas, String className) {
		   Class outClass;
	       if(jclas==null) {
	           return null;
	       }
	       byte[] b = jclas.getBytes();
	       outClass=defineClass(className, b, 0, b.length);
	       return outClass;
	   }

   public JavaClass classWithTracing(String className)
   {
       JavaClass clazz;
       try {
      	clazz= ss.loadClass(className);
      	       
       } catch (Exception e) {
           clazz=null;
       }
       if(clazz==null) {
           return null;
       }
       return clazz;
   }
   public JavaClass loadJavaClass(String classname){
	   return classWithTracing(classname);
   }
   
 
  
}