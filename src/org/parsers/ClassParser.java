package org.parsers;

import org.apache.bcel.generic.GETSTATIC;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.aspectj.lang.reflect.AdviceKind;
import org.info.MyJoinPoint;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

public class ClassParser {
	static {
		soot.options.Options.v().set_keep_line_number(true);
		soot.options.Options.v().set_whole_program(true);
		soot.options.Options.v().setPhaseOption("cg","verbose:true");
	}

	/*
	 * Parse the class bytecode for the join points. attach each join point to its  
	 * advice object.
	 */
	org.apache.bcel.generic.ConstantPoolGen cp;
	org.apache.bcel.generic.ClassGen cg;
	int out;
	int printlnString;
	int updatecounter;
	int reporter;

	public Map <String, MyJoinPoint> MapJoinPoints = new HashMap();
	JavaClass jclas;
	public ClassParser(){
		MapJoinPoints = new java.util.HashMap <String, MyJoinPoint>();
	}

	/*
	 * Parse methods advised with an Around advice
	 */

	public  org.apache.bcel.classfile.Method 
	parseAroundMethods(org.apache.bcel.classfile.Method m){

		String MethodName  = m.getName();

		org.apache.bcel.generic.MethodGen mg = 
				new org.apache.bcel.generic.MethodGen(m,jclas.getClassName(),cp);
		org.apache.bcel.generic.InstructionList il = 
				mg.getInstructionList();
		org.apache.bcel.generic.InstructionHandle[] ihs = 
				il.getInstructionHandles();

		//get the second argument of the method

		String aspectName = mg.getArgumentType(2).toString();
		MyJoinPoint jp = new MyJoinPoint("NAN","NAN", aspectName);
		jp.setClassName(cg.getClassName());
		jp.setAdviceKind(AdviceKind.AROUND);	  

		String key =  cg.getClassName()+"$"+mg.getName()+"$"+aspectName;
		MapJoinPoints.put(key, jp);

		System.out.println(mg.getSignature());


		InstructionList patch  = new InstructionList();
		patch.append(new GETSTATIC(out));
		patch.append(new PUSH(cp, "Executing join point: "+mg.getName()));
		patch.append(new INVOKEVIRTUAL(printlnString));

		patch.append(new PUSH(cp,key));
		patch.append(new INVOKESTATIC(updatecounter));

		if(MethodName.equals("<init>")) { // First let the super or other constructor be called
			for(int j=1; j < ihs.length; j++) {
				if(ihs[j].getInstruction() instanceof INVOKESPECIAL) {
					il.append(ihs[j], patch);  // Should check: method name == "<init>"
					break;
				}
			}
		}
		else
			il.insert(ihs[0], patch);

		mg.setInstructionList(il);
		il.setPositions();
		mg.setMaxStack();
		mg.setMaxLocals();
		mg.removeLineNumbers();    
		return mg.getMethod();
	}


	/*
	 *
	 * Parse a method for join points, //determine if the method is adviSed
	 */
	public  void     parseMethod(org.apache.bcel.classfile.Method m)
	{

		org.apache.bcel.generic.MethodGen mg = 
				new org.apache.bcel.generic.MethodGen(m,jclas.getClassName(),cp);


		org.apache.bcel.generic.InstructionList il = 
				mg.getInstructionList();
		org.apache.bcel.generic.InstructionHandle[] ihs = 
				il.getInstructionHandles();
		MyJoinPoint jp;
		String methodName = m.getName();
		System.out.println("================  method name ============= "+ m.getName());
			
		
		if (methodName.contains("aroundBody")||m.isSynthetic()) {
			String []tok = methodName.split("_");
			methodName =tok[0];
			methodName =handleAround(m, methodName);
		}
		else
			methodName =handleMethodArguments(m);

		if (methodName.contains("<init>")) {
			String str = cg.getJavaClass().getPackageName()+".";
//			System.out.println("================  class name ============= "+ cg.getJavaClass().getPackageName());
			String str2 = cg.getClassName().replace(str, "");
			methodName = methodName.replace("<init>", str2);
		}

        System.out.println("================  method name after ============= "+ methodName);
		for (int j=0; j<ihs.length; j++)
		{   
			org.apache.bcel.generic.InstructionHandle ih = ihs[j];
			org.apache.bcel.generic.Instruction instr = 
					ih.getInstruction();

			if (instr instanceof org.apache.bcel.generic.INVOKEVIRTUAL)
			{
				//potential join point
				org.apache.bcel.generic.INVOKEVIRTUAL call =
						(org.apache.bcel.generic.INVOKEVIRTUAL) instr;
				String calledMethodName = call.getMethodName(cp);
				//  	System.out.println(CalledMethodName);
				if (calledMethodName.startsWith("ajc")){
					//a join point, parse the name
					String []tokens = calledMethodName.split("\\$");
					if (tokens.length==5){		    
						/*  //example: ajc$before$kettle_Validator$3$74183793
						 * tokens[0]: ajc
						 * tokens[1]: advice type
						 * tokens[2]: Declaring aspect
						 * tokens[4]: adviceID
						 * tokens[3]: AdviceSerialNumber
						 */
						String strDeclaringAspect = tokens[2];
						strDeclaringAspect = strDeclaringAspect.replaceAll("_", ".");
						jp = new MyJoinPoint(tokens[4],tokens[3],
								strDeclaringAspect);

					    
						if (tokens[1].equalsIgnoreCase(AspectParser.AFTER))
							jp.setAdviceKind(AdviceKind.AFTER);
						if (tokens[1].equalsIgnoreCase(AspectParser.BEFORE))
							jp.setAdviceKind(AdviceKind.BEFORE);
						if (tokens[1].equalsIgnoreCase(AspectParser.AFTER_RETURNING))
							jp.setAdviceKind(AdviceKind.AFTER_RETURNING);
						if (tokens[1].equalsIgnoreCase(AspectParser.AFTER_THROWING))
							jp.setAdviceKind(AdviceKind.AFTER_THROWING);
						if (tokens[1].equalsIgnoreCase(AspectParser.AROUND))
							jp.setAdviceKind(AdviceKind.AROUND);

						// System.out.println(m);
						//System.out.println(cg.getClassName()+"$"+mg.getName()+"$"+ih.getPosition());

						/*
						 * calledmethedName is the advice name. mg.getname is the advised method name
						 */
						jp.setClassName(cg.getClassName());
						jp.setMethodName(methodName)	   	  ;
						jp.setLineNumber(ih.getPosition());
						// String key =  cg.getClassName()+"$"+mg.getName()+"$"+ih.getPosition()+"$"+calledMethodName;
						String key =  cg.getClassName()+"$"+methodName+"$"+calledMethodName;

						MapJoinPoints.put(key, jp);



					}   } 
			}
		}
	}
	
	private String handleAround(Method m, String methodName) {
		
		org.apache.bcel.generic.Type[] t = m.getArgumentTypes();
		String str ="(";
		for (int k=1;k<t.length;k++) {
			if (k==1)
			str+=t[k];
			else
				str+=", "+t[k];    						
		}
		str =methodName +str+")";
		return str;
	}
	
	private String handleMethodArguments(Method m) {
		String methodName =m.getName();
		if (methodName.contains("aroundBody")||m.isSynthetic()) {
			String []tok = methodName.split("_");
			methodName =tok[0];
		}
		org.apache.bcel.generic.Type[] t = m.getArgumentTypes();
		String str ="(";
		for (int k=0;k<t.length;k++) {
			if (k==0)
			str+=t[k];
			else
				str+=", "+t[k];    						
		}
			
			//System.out.println(str+")");
			str =methodName +str+")";
			return str;
	
	}
	private boolean isOverloaded(Method[] m, int j) {
		for (int i=0; i<m.length;i++) {
			if (j==i) continue;
			if (m[i].getName().compareTo(m[j].getName())==0)
				return true;
		}
		
	  return false;	
	}
	private void handleConstructors(Method method) {
		
		
	}

	boolean openClass(){
		try{
			//org.apache.bcel.classfile.ClassParser p = 
			//new org.apache.bcel.classfile.ClassParser(classFile);
			//org.apache.bcel.classfile.JavaClass jc = p.parse()
			cg =  new org.apache.bcel.generic.ClassGen(jclas);
			cp=   new org.apache.bcel.generic.ConstantPoolGen(
					jclas.getConstantPool());
		}
		catch (Exception e) {
			System.out.println("error: " + e.getMessage());
			return false;
		}
		return true;
	}
	
	public JavaClass parse(JavaClass javaclass,	String inDir){
		/*
		 * given the list of advices, parse the class for join points, 
		 * add the found join points for each advice.
		 */
		//read the constants Pool
		try {
			this.jclas = javaclass; 
			if (jclas==null) return jclas;
			if (!openClass()) return jclas;
			//changeClassName(jclas);
			org.apache.bcel.classfile.JavaClass jc1;
			org.apache.bcel.classfile.Method []m= jclas.getMethods();
			out = cp.addFieldref("java.lang.System", "out", "Ljava/io/PrintStream;");
			printlnString = cp.addMethodref("java.io.PrintStream", "println",
					"(Ljava/lang/String;)V");

			  
			updatecounter = cp.addMethodref("parClass.Reporter", "updateCounter",
					"(Ljava/lang/String;)V");

			for (int i=0;i<m.length; i++){
				//System.out.println(m[i]);
    			if (!m[i].isAbstract()){
    				parseMethod(m[i]);

				}
			}
			jc1 = cg.getJavaClass();
			jc1.setConstantPool(cp.getFinalConstantPool());
			return jc1;
		}
		catch (Exception e) {
			System.out.println("error: " + e.getMessage());
			return jclas;
		}
	}
/*
	  public static void main(String []args){
		  sootParser();
	  }
private static void sootParser() {
	
	for (Iterator it = Scene.v().getApplicationClasses().iterator(); it.hasNext();) {
				SootClass c = (SootClass) it.next();
				DisplayMethodNames(c);}
	

	
	//Scene.v().setSootClassPath("c:\\AOP\\KettleV2\\bin\\");
	System.out.println(Scene.v().getSootClassPath());
	System.out.println("**********************" +Scene.v().getApplicationClasses().size());
    Scene.v().setSootClassPath("c:\\AOP\\KettleV2\\bin\\kettle");
    Scene.v().addBasicClass("c:\\AOP\\KettleV2\\bin\\kettle\\Kettle.class");
    
    System.out.println("**********************" +Scene.v().getApplicationClasses().size());
	SootClass c = Scene.v().loadClassAndSupport("kettle.Kettle");
	c.setApplicationClass();
	
	for (Iterator<SootClass> im=Scene.v().getApplicationClasses().iterator(); im.hasNext();){
		SootClass ss = (SootClass) im.next();
		System.out.println("**********************" +ss.getName());
	}
	
//    SootClass c = Scene.v().loadClassAndSupport("kettle.Kettle");
//c.setApplicationClass();
//DisplayMethodNames(c);

}
private static void DisplayMethodNames(SootClass c){
		
		for (Iterator im=c.methodIterator(); im.hasNext();){
			SootMethod sm = (SootMethod) im.next();
			System.out.println("**********************" +sm);
			
		}	
   }
*/

		
}

