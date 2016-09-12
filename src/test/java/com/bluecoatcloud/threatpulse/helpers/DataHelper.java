package com.bluecoatcloud.threatpulse.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONObject;
import org.testng.Reporter;

import com.bluecoatcloud.threatpulse.constants.CommonConstants;
import com.bluecoatcloud.threatpulse.dataProvider.TestEnvironment;

/**
 * 
 * @author arohatgi
 *
 */
public class DataHelper {
	
		protected JSONObject jsonLearningUnit;
		protected JSONObject jsonLearningActivity;
		protected JSONObject jsonOutCome;
		protected JSONObject jsonPreReqs;
			
		/**
		 * default Constructor
		 */
		public DataHelper(){			
		}
		

		 /**
		  * 
		  */
		  public static Object[][] createDataForDataProvider(String fileName) throws IOException{
			  System.out.println("data helper test called-----------");
		     	//Map<String, Map<String, String>>  dataFromFile = DataHelper.getDataFromXSL("C:\\Work\\Documents\\Amita\\Dashboards\\EventLogging_backup.xls");
		    	Map<String, Map<String, String>>  dataFromFile = DataHelper.getDataFromXSL(fileName);
			    
		     	Integer totalRowNumber = Integer.valueOf(dataFromFile.size());
		        Set<String> rowKeySet = dataFromFile.keySet();
		        Iterator<String> iter = rowKeySet.iterator();
		        Map<String, String> columnData = dataFromFile.get(iter.next());
		      	Integer totalColumnNumber = Integer.valueOf(columnData.size());
		    	Object[][] memberData = new Object[totalRowNumber][totalColumnNumber];
		        Set<Entry<String,Map<String,String>>> entrySet = dataFromFile.entrySet();
		        Iterator<Entry<String,Map<String,String>>> entryIterator = entrySet.iterator();	
		        int rownum = 0;
		     	String[] columnArray = null;
		        while(entryIterator.hasNext()){
		        	columnArray = new String[totalColumnNumber];
		         	Entry<String,Map<String,String>> anEntry =  entryIterator.next();
		          	Map<String, String> columnValues = anEntry.getValue();
		          	Set<String> columnKeys = columnValues.keySet();
		          	Iterator<String> keyIterator = columnKeys.iterator();
		          	int i = 0;
		          	while (keyIterator.hasNext()){
		          		String key = keyIterator.next();
		          		columnArray[i] = key+"="+columnValues.get(key);
		          		i++;
		          	}
		        	//Object[][] retData = new Object[][] { new Object[] { new Integer(42)}};
		        	//Object[][] retData = new Object[][] { new Object[] { new Integer(42)}, new Object[] { new Integer(45)} };
		        	
		        	memberData[rownum] = new Object[][]{ {columnArray}};
		            
		          	//memberData[rownum][rownum] = columnArray;
		          	rownum = rownum+1;
		    	}
		    	
		    	return memberData;
		    }
		
	    /**
         * 
         * @param key
         * @return
         * @throws IOException
         */
        public static Map<String, Map<String,String>> getDataFromXSL(String fileName) throws IOException{
          	Map<String, Map<String,String>> returnMap = new HashMap<String, Map<String,String>>();
          	InputStream inStream = new FileInputStream(fileName);
            Map<String, String> memberProperties = null; 
         	Workbook wb = new HSSFWorkbook(inStream);
    	 	Sheet sheet1 = wb.getSheetAt(0);
    	 	List<String> columnNameList = new ArrayList<String>();
           	int columnIndex = 0;
        	String cellValue = null;
        	int memberCounter = 0;
           	for (int rowIndex = 0; rowIndex < sheet1.getLastRowNum(); rowIndex++) {
           		memberProperties = new HashMap<String,String>();
           		columnIndex = 0;
           		memberCounter = memberCounter+rowIndex;
           		Row row = sheet1.getRow(rowIndex);
         		Iterator<Cell> cit = row.cellIterator();
           		while (cit.hasNext()){
           			Cell cell = cit.next();
           			if (rowIndex == 0){
				/*		Reporter.log("Column number: " + cell.getColumnIndex());
						Reporter.log("Column Title: " + cell.getStringCellValue());
				*/		columnNameList.add(cell.getStringCellValue());
	        		}else{
	        			switch (cell.getCellType()){
		        			case Cell.CELL_TYPE_STRING:
								cellValue = cell.getRichStringCellValue().getString();
								break;
		        			case Cell.CELL_TYPE_NUMERIC:
								if(DateUtil.isCellDateFormatted(cell)) {
									cellValue = cell.getDateCellValue().toString();
								} else {
									cellValue = String.valueOf((int)cell.getNumericCellValue());
								}
								break;
	        			}		
	         /*			Reporter.log("Column number: " + cell.getColumnIndex());
	        			Reporter.log("Column Title: " + columnNameList.get(columnIndex));
		   				Reporter.log("Column Value: " + cellValue);
		   	*/		
		   				memberProperties.put(columnNameList.get(columnIndex), cellValue);
        			}
                     columnIndex = columnIndex+1;	
   			     }
    			returnMap.put(String.valueOf(rowIndex), memberProperties);
    	          
           	}
        	return returnMap;
        	     
        }

     
			
        /**
         * 
         * @param key
         * @return
         * @throws IOException
         */
        public static String getDataFromXSL(String fileName, String key) throws IOException{
          	String returnVal = null;
            //String fileName = System.getProperty(NewSyllabusConstants.KEY_CDG_JSON_FILE);
        	InputStream inStream = new FileInputStream(fileName);
       	    Workbook wb = new HSSFWorkbook(inStream);

    		Properties prop = getProperties();
        	Sheet sheet1 = wb.getSheetAt(0);
        	boolean found = false;
       
         	for (Row row : sheet1) {
        		Iterator<Cell> cit = row.cellIterator();
        		if (cit.hasNext() && !found) {
        			Cell cell = cit.next();
        			if (cell.getRichStringCellValue() != null) {
        				if (TestEnvironment.getTestEnvironmentObject().isDebugLoggingOn()){
		        			Reporter.log("looking for key : " + key, true);
		        			Reporter.log("Value for the current cell " + cell.getRichStringCellValue().getString(), true);
        			     }
			      			if (key.equalsIgnoreCase(cell.getRichStringCellValue().getString())){
			      		if (TestEnvironment.getTestEnvironmentObject().isDebugLoggingOn()){
		        			Reporter.log("Found the key: " + key, true);
			      		}
		        			found = true;
		        			if (cit.hasNext()){
		        				cell = cit.next();
		        		    	switch (cell.getCellType()){
		        					case Cell.CELL_TYPE_STRING:
		        						returnVal = cell.getRichStringCellValue().getString();
		        						break;
		        					case Cell.CELL_TYPE_NUMERIC:
		        						if(DateUtil.isCellDateFormatted(cell)) {
		        							returnVal = cell.getDateCellValue().toString();
		        						} else {
		        							returnVal = String.valueOf(cell.getNumericCellValue());
		        						}
		        					    break;
		        					case Cell.CELL_TYPE_BOOLEAN:
		        						returnVal = String.valueOf(cell.getBooleanCellValue());
		        					    break;
		        			     } 
		        			}
		        			if (TestEnvironment.getTestEnvironmentObject().isDebugLoggingOn()){
		    		            Reporter.log("Return Value :" + returnVal, true);
		        			}
			        	    return returnVal;    
	        		     }
	        		}
           	}
     
        }
       	return returnVal;
     }
        
   /**
    * 
    * @param name
    * @return
    */
     public Object getObject(String name){
        	Class<?> cl = null;
        	Object obj = null;
			try {
				cl = Class.forName(name);
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
        	try {
				 obj = cl.newInstance();
			} catch (InstantiationException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}
        	String value  = "";
        	/*BeanInfo info = Introspector.getBeanInfo(newInstance.class);
        
     	   
        	
            for ( PropertyDescriptor pd : info.getPropertyDescriptors() )
                Reporter.log( pd.getName() );*/
            
            //read xsl and set the properties on the attributes using reflection
          
            if (cl != null){
            	Field[]  field = cl.getDeclaredFields();
               //read the attributes from the .xsl file
                for (int i = 0; i < field.length; i++){
            	Field aField = field[i];
              	if (aField != null && aField.getName().length() > 0 && aField.getType().getSimpleName().equalsIgnoreCase("String")){
              		try {
						value = getDataFromXSL("", aField.getName());
					} catch (IOException e) {
							e.printStackTrace();
					}
              	
              	if (value != null && value.length() > 0){
              		try {
              			
              			aField.setAccessible(true);
						aField.set(obj, value);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
                  }
              	}
              }
        
	        }
			return obj;
     	
        }
  	
	 	/**
	  	 * This is responsible to read the data files from resources dir, load the properties from the it 
	  	 * and return them.
	  	 * 
	  	 * @param String testEnv
	  	 * @return Properties
	  	 * @throws IOException
	  	 */
	  	 public static Properties getDataProperties(String testEnv) throws IOException {
	    	String fileLocaion = System.getProperty("user.dir") + org.apache.commons.io.FilenameUtils.separatorsToSystem(CommonConstants.DATA_LOCATION); 
	  		Properties prop = getProperties(fileLocaion, testEnv);
	   	    return prop;
	 	  }
	  	 
	  	 
	 	/**
	   	 * 
	     * @param dir
	     * @return
	     * @throws IOException
	     */
	   	private static Properties getProperties(String dataLocation, String testEnv) throws IOException{
	    	  	Properties aProperty = new Properties();
	    	    
	    		File dir = new File(dataLocation);
	     		 if (dir.isDirectory()){
	      		   String[] dirList = dir.list();
	     		   for (String dirName : dirList){
	     			  File dataDir = new File(dataLocation+dirName);
	     			  File[] dataFileLists = dataDir.listFiles();
	     			 //read one file at a time and add to properties
		     		  for ( File file : dataFileLists ){
		     			   String fileName = file.getAbsolutePath();
		     			   if (fileName.endsWith(testEnv.toLowerCase() + ".xml") || fileName.endsWith(testEnv.toUpperCase() + ".xml")  || (fileName.endsWith(CommonConstants.DATA_COMMON + ".xml")) ){
		     			   InputStream	thePropFile = new FileInputStream(fileName);
		     				if (thePropFile != null){

		     	      	   		Properties newProp = new Properties();
		     	   	     	    newProp.loadFromXML(thePropFile);
		     		     	    Set<Entry<Object,Object>> propEntrySet = newProp.entrySet();
		     		     	    if (propEntrySet != null && !propEntrySet.isEmpty()){
		     			     	    Iterator<Entry<Object,Object>> propIter = propEntrySet.iterator();
		     			      	    while(propIter.hasNext()){
		     			     	    	Entry<Object,Object> theEntry = propIter.next();
		     			     	    	aProperty.put(theEntry.getKey(), theEntry.getValue());
		     			     	    }
		     		     	    }
		     				}
		     			   }
		     		   }
	     		   }
	   		 }
	   		 return aProperty;
	   	 }
	  	 
    	/**
     	 * This is responsible to read the configuration file, load the properties from the it 
     	 * and return them
     	 * @return Properties
     	 * @throws IOException
     	 */
     	 public static Properties getProperties() throws IOException {
       		String fileLocaion = System.getProperty("user.dir") + org.apache.commons.io.FilenameUtils.separatorsToSystem(CommonConstants.CONFIG_LOCATION); 
     		Properties prop = getProperties(fileLocaion);
      	    return prop;
    	  }
     	 
     	/**
     	 * 
     	 * @param dir
     	 * @return
     	 * @throws IOException
     	 */
    	private static Properties getProperties(String dataLocation) throws IOException{
    	  	Properties aProperty = new Properties();
    	    
    		File dir = new File(dataLocation);
     		 if (dir.isDirectory()){
     		    File[] congifFileLists = dir.listFiles();
     		    //read one file at a time and add to properties
     		   for ( File file : congifFileLists ){
     			   String fileName = file.getAbsolutePath();
     			   if (fileName.endsWith(".xml")){
     			   InputStream	thePropFile = new FileInputStream(fileName);
     				if (thePropFile != null){

     	      	   		Properties newProp = new Properties();
     	   	     	    newProp.loadFromXML(thePropFile);
     		     	    Set<Entry<Object,Object>> propEntrySet = newProp.entrySet();
     		     	    if (propEntrySet != null && !propEntrySet.isEmpty()){
     			     	    Iterator<Entry<Object,Object>> propIter = propEntrySet.iterator();
     			      	    while(propIter.hasNext()){
     			     	    	Entry<Object,Object> theEntry = propIter.next();
     			     	    	aProperty.put(theEntry.getKey(), theEntry.getValue());
     			     	    }
     		     	    }
     				}
     			   }
     		   }
     		    
     		 }
     		 return aProperty;
     	 }
    	
    /**
     * 	
     * @param file
     * @return
     * @throws IOException
     */
	public static List<String> readTextFile(File file) throws IOException{
 			
 		    List<String> xssString = null;
 			
 			FileUtils.readLines(file);
 			
 			return xssString;
 			
 			
 	}
	
	/**
	 * For testing
	 * @param argv
	 */
	public static void main (String [] argv){
		DataHelper dataHelper  = new DataHelper();
		try {
			//dataHelper.getDataFromXSL("C:\\Work\\Documents\\Amita\\Dashboards\\EventLogging.xls");
			DataHelper.getDataProperties("SJQA".toLowerCase());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
          
}
