package org.xml;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import org.info.*;


public class XmlWriter {
	private static final Logger logger = Logger.getLogger(XmlWriter.class.getName());
	
    private FileWriter writer;      // underlying writer
    private Stack stack;        // of xml entity names
    private StringBuffer attrs; // current attribute string
    private boolean empty;      // is the current node empty
    private boolean closed;     // is the current node closed...

    /**
     * Create an XmlWriter on top of an existing java.io.Writer.
     */
    public XmlWriter(FileWriter writer) {
        this.writer = writer;
        this.closed = true;
        this.stack = new Stack();
    }
    /**
     * Create an XmlWriter on top of an existing java.io.Writer.
     */
    public XmlWriter(String filepath) throws IOException {
  	  try{
  		this.writer = new FileWriter(filepath);
        this.closed = true;
        this.stack = new Stack();  
  	  }
  	  catch (IOException e) {		 
  		  logger.log(Level.INFO, e.getMessage());	   
	      throw e;
  	  }
 }
        
  
    
    /**
     * Begin to output an entity. 
     *
     * @param String name of entity.
     */
    public XmlWriter writeEntity(String name) throws WritingException {
        try {
            closeOpeningTag();
            this.closed = false;
            this.writer.write("<");
            this.writer.write(name);
            stack.add(name);
            this.empty = true;
            return this;
        } catch (IOException ioe) {
            throw new XmlWritingException(ioe);
        }
    }
 // close off the opening tag
    private void closeOpeningTag() throws IOException {
        if (!this.closed) {
            writeAttributes();
            this.closed = true;
            this.writer.write(">");
            //this.writer.write("\n");
        }
    }
    // write out all current attributes
    private void writeAttributes() throws IOException {
        if (this.attrs != null) {
            this.writer.write(this.attrs.toString());
            this.attrs.setLength(0);
            this.empty = false;
        }
    }

    /**
     * Write an attribute out for the current entity. 
     * Any xml characters in the value are escaped.
     * Currently it does not actually throw the exception, but 
     * the api is set that way for future changes.
     *
     * @param String name of attribute.
     * @param String value of attribute.
     */
    public XmlWriter writeAttribute(String attr, String value) throws WritingException {

        // maintain api
        if (false) throw new XmlWritingException();

        if (this.attrs == null) {
            this.attrs = new StringBuffer();
        }
        this.attrs.append(" ");
        this.attrs.append(attr);
        this.attrs.append("=\"");
        this.attrs.append(escapeXml(value));
        this.attrs.append("\"");
        return this;
    }

    /**
     * End the current entity. This will throw an exception 
     * if it is called when there is not a currently open 
     * entity.
     */
    public XmlWriter endEntity() throws WritingException {
        try {
            if(this.stack.empty()) {
                throw new XmlWritingException("Called endEntity too many times. ");
            }
            String name = (String)this.stack.pop();
            if (name != null) {
                if (this.empty) {
                    writeAttributes();
                    this.writer.write("/>");
                //    this.writer.write("\n");
                } else {
                    this.writer.write("</");
                    this.writer.write(name);
                    this.writer.write(">");
              //      this.writer.write("\n");
                }
                this.empty = false;
            }
            return this;
        } catch (IOException ioe) {
            throw new XmlWritingException(ioe);
        }
    }
    /**
     * Close this writer. It does not close the underlying 
     * writer, but does throw an exception if there are 
     * as yet unclosed tags.
     * @throws Exception 
     */
    public void close() throws Exception {
        if(!this.stack.empty()) {
            throw new XmlWritingException("Tags are not all closed. "+
                "Possibly, "+this.stack.pop()+" is unclosed. ");
        }
        try
        {
        writer.close();
        }
        catch (Exception e){
        	  logger.log(Level.INFO, e.getMessage());	   
    	      throw e;
        }
    }

    /**
     * Output body text. Any xml characters are escaped. 
     */
    public XmlWriter writeText(String text) throws WritingException {
        try {
            closeOpeningTag();
            this.empty = false;
            this.writer.write(escapeXml(text));
            return this;
        } catch (IOException ioe) {
            throw new XmlWritingException(ioe);
        }
    }
    // Static functions lifted from generationjava helper classes
    // to make the jar smaller.
    
    // from XmlW
    static public String escapeXml(String str) {
        str = replaceString(str,"&","&amp;");
        str = replaceString(str,"<","&lt;");
        str = replaceString(str,">","&gt;");
        str = replaceString(str,"\"","&quot;");
        str = replaceString(str,"'","&apos;");
        return str;
    }  

    // from StringW
    static public String replaceString(String text, String repl, String with) {
        return replaceString(text, repl, with, -1);
    }  
    /**
     * Replace a string with another string inside a larger string, for
     * the first n values of the search string.
     *
     * @param text String to do search and replace in
     * @param repl String to search for
     * @param with String to replace with
     * @param n    int    values to replace
     *
     * @return String with n values replacEd
     */
    static public String replaceString(String text, String repl, String with, int max) {
        if(text == null) {
            return null;
        }
 
        StringBuffer buffer = new StringBuffer(text.length());
        int start = 0;
        int end = 0;
        while( (end = text.indexOf(repl, start)) != -1 ) {
            buffer.append(text.substring(start, end)).append(with);
            start = end + repl.length();
 
            if(--max == 0) {
                break;
            }
        }
        buffer.append(text.substring(start));
 
        return buffer.toString();
    }              
    public void writeAdvicesMapToXML(Map <String, MyAdvice> advices)
    {
    	
      int mapsize = advices.size();
  	  Iterator keyValuePairs1 = advices.entrySet().iterator();
  	  try{
    	   this.writeEntity("advices");
    	   this.writeAttribute("count", String.valueOf(mapsize));
       	  for (int i = 0; i < mapsize; i++) {
    	    Map.Entry entry = (Map.Entry) keyValuePairs1.next();
    		Object key = entry.getKey();
    		Object value = entry.getValue();
    		MyAdvice myadvice = (MyAdvice) value;
    		
    	    this.writeEntity("advice");
            this.writeAttribute("key", (String) key);
            writeEntity("ID");
            writeText(myadvice.ID);
            endEntity();
            
            writeEntity("SN");
            writeText(myadvice.SerialNumber);
            endEntity();
            
            writeEntity("AspectName");
            writeText(myadvice.getDeclaringAspectName());
            endEntity();
                            
            writeEntity("AdviceKind");
            writeText(myadvice.getKind().toString());
            endEntity();
     
            endEntity();
          } //for
    	  endEntity();
    	   this.close();
    	}catch (Exception e) {
			 System.out.println("error: " + e.getMessage());
			
		}
    }
  
    public void writeInfo(Map <String, ClassInfo> 
    allClasses, Map <String, AspectInfo> allAspects){
    	try{
    	    this.writeEntity("ParsingInfo");
    	      	       
    		writeAspectsInfo(allAspects, allAspects.size());
       	    writeclassInfo( allClasses);
        	endEntity();
        	this.close();
        	
    	}catch (Exception e) {
      		 System.out.println("error: " + e.getMessage());
        }
    	
    }
    private void writeAspectsInfo(Map <String, AspectInfo>allAspects,
    		int numAspectJP){
    	int mapsize = allAspects.size();
    	Iterator keyValuePairs1 = allAspects.entrySet().iterator();
    	try{
    		this.writeEntity("Aspects");
    	    writeEntity("numAspects");
    	    this.writeText(String.valueOf(mapsize));
    	    endEntity();
    	    
    	    this.writeEntity("numJoinPoints");
    	    this.writeText(Integer.toString(numAspectJP));
    	    endEntity();
    	    
    	    for (int i = 0; i < mapsize; i++) {
    	    	Map.Entry entry = (Map.Entry) keyValuePairs1.next();
    	    	Object key = entry.getKey();
    	    	Object value = entry.getValue();
    	    	AspectInfo aspect = (AspectInfo) value;
    	      	this.writeEntity("aspect");
                int numJP = aspect.numAdvicesJP();
                int numAdvices = aspect.numAdvices();
                writeEntity("Name");
                writeText((String) key);
                endEntity();
                
                writeEntity("numAdvices");
                writeText(Integer.toString(numAdvices));
                endEntity();
                
                writeEntity("numJoinPoints");
                writeText(Integer.toString(numJP));
                endEntity();
                
                Map <String, MyAdvice> AspectAdvices;
                AspectAdvices = aspect.getAdvices();
            	Iterator keyValuePairs2;
            	keyValuePairs2= AspectAdvices.entrySet().iterator();
            	this.writeEntity("advices");
            	//loop on the advices in the aspect
            	for (int j=0; j<numAdvices; j++){
            		Map.Entry entryJP = (Map.Entry) keyValuePairs2.next();
            		Object keyJP = entryJP.getKey();
            		Object valueJP = entryJP.getValue();
            		MyAdvice advice = (MyAdvice) valueJP;
            		this.writeEntity("advice");
            		
                    this.writeAttribute("key", (String) keyJP);
                    writeEntity("AdviceID");
                    writeText(advice.getID());
                    endEntity();
                    
                    writeEntity("SN");
                    writeText(advice.getSerialNumber());
                    endEntity();
                    
                    
                    writeEntity("AdviceKind");
                    writeText(advice.getAdvicekind());
                    endEntity();

                    writeEntity("numJP");
                    writeText( Integer.toString( advice.getNumJP() ));
                    endEntity();

                    //write the join points info of the advice
                    Map <String, MyJoinPoint> AdviceJoinPoints;
                    AdviceJoinPoints = advice.getJoinpoints();
                	Iterator keyValuePairs3;
                	keyValuePairs3= AdviceJoinPoints.entrySet().iterator();
                	this.writeEntity("joinpoints");
                	numJP=advice.getNumJP();
                	for (int k=0; k<numJP; k++)
                    {
                		entryJP = (Map.Entry) keyValuePairs3.next();
                		Object keyAJP = entryJP.getKey();
                		Object valueAJP = entryJP.getValue();
                		MyJoinPoint jp = (MyJoinPoint) valueAJP;
                		this.writeEntity("jp");
                        this.writeAttribute("JPkey", (String) keyAJP);
                        
                        writeEntity("ClassName");
                        writeText(jp.getClassName());
                        endEntity();

                        endEntity();  //jp
                    }         
                	endEntity();
                    endEntity();  //advice
            		
            	}
            	endEntity();  //advices
                endEntity();  //aspect
    	    } //loop on aspects
    		endEntity();  //aspects
       	    
       	}catch (Exception e) {
   		 System.out.println("error: " + e.getMessage());
       	}
    }
    
    private void writeclassInfo(Map <String, ClassInfo> 
    allClasses)
    {
    	int mapsize = allClasses.size();
    	Iterator keyValuePairs1 = allClasses.entrySet().iterator();
    	try{
    		this.writeEntity("Classes");
    	    writeEntity("Count");
    	    this.writeText(String.valueOf(mapsize));
    	    endEntity();
    	    
    	    /*this.writeEntity("numJP");
    	    this.writeText(String.valueOf(ClassesTotalnumJP));
    	    endEntity();
    	    */
    	    for (int i = 0; i < mapsize; i++) {
    	    	Map.Entry entry = (Map.Entry) keyValuePairs1.next();
    	    	Object key = entry.getKey();
    	    	Object value = entry.getValue();
    	    	ClassInfo clas = (ClassInfo) value;
    	      	this.writeEntity("clas");
                int numJP = clas.numJP();
                
                writeEntity("Name");
                writeText((String) key);
                endEntity();
                
                writeEntity("numJP");
                writeText(Integer.toString(numJP));
                endEntity();
                
                
                Map <String, MyJoinPoint> ClassJoinPoints;
                ClassJoinPoints = clas.getJPS();
            	Iterator keyValuePairs2;
            	keyValuePairs2= ClassJoinPoints.entrySet().iterator();
            	this.writeEntity("joinpoints");
            	this.writeAttribute("count", Integer.toString(numJP));
            	for (int j=0; j<numJP; j++)
                {
            		Map.Entry entryJP = (Map.Entry) keyValuePairs2.next();
            		Object keyJP = entryJP.getKey();
            		Object valueJP = entryJP.getValue();
            		MyJoinPoint jp = (MyJoinPoint) valueJP;
            		this.writeEntity("jp");
                    this.writeAttribute("JPkey", (String) keyJP);
                    writeEntity("AdviceID");
                    writeText(jp.getAdviceID());
                    endEntity();
                    
                    writeEntity("SN");
                    writeText(jp.getAdviceSerialNumber());
                    endEntity();
                    
                    writeEntity("DeclaringName");
                    writeText(jp.getAspectName());
                    endEntity();
                    
                    writeEntity("AdvisedMethod");
                    writeText(jp.getMethodName());
                    endEntity();
                    
                    writeEntity("AdvisedClass");
                    writeText(jp.getClassName());
                    endEntity();
                    
                    writeEntity("AdviceKind");
                    writeText(jp.getAdviceKind().toString());
                    endEntity();


                     endEntity();  //jp
                 } //class join points for loop
                endEntity();  //join points
                endEntity();  //class
    	  } //big for
    	    endEntity();  //classes
        	}catch (Exception e) {
		 System.out.println("error: " + e.getMessage());
		
	}
    }
   
    public void writeJPMapToXML(Map <String, MyJoinPoint> joinpoints) throws Exception
    {
    	
        int mapsize = joinpoints.size();
    	Iterator keyValuePairs1 = joinpoints.entrySet().iterator();
    	try{
    		/*
    		this.writeEntity("?xml");
    		this.writeAttribute("version", "1.0");
    		this.writeAttribute("encoding","UTF-8");
    		this.endEntity();
    		*/
    	    this.writeEntity("joinpoints");
    	    this.writeAttribute("count", String.valueOf(mapsize)); 	
    	  for (int i = 0; i < mapsize; i++) {
    	    Map.Entry entry = (Map.Entry) keyValuePairs1.next();
    		Object key = entry.getKey();
    		Object value = entry.getValue();
    		MyJoinPoint jp = (MyJoinPoint) value;
    		
    	    this.writeEntity("jp");
            this.writeAttribute("JPkey", (String) key);
            writeEntity("AdviceID");
            writeText(jp.getAdviceID());
            endEntity();
            
            writeEntity("AdviceSerialNumber");
            writeText(jp.getAdviceSerialNumber());
            endEntity();
            
            writeEntity("DeclaringAspect");
            writeText(jp.getAspectName());
            endEntity();
            
            writeEntity("AdvisedMethod");
            writeText(jp.getMethodName());
            endEntity();
            
            writeEntity("LineNumberBytecode");
            writeText(Integer.toString(jp.getLineNumber()));
            endEntity();
                        
            writeEntity("AdvisedClass");
            writeText(jp.getClassName());
            endEntity();
            
            writeEntity("AdviceKind");
            writeText(jp.getAdviceKind().toString());
            endEntity();

            
            endEntity();
          } //for
    	  endEntity();
    	   this.close();
    	}catch (Exception e) {
    		  logger.log(Level.INFO, e.getMessage());	   
    	      throw e;			
		}
    }
        
}
