package core.logging;

import java.io.File;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

//SEE https://medium.com/yohan-liyanage/know-the-jvm-series-1-the-uncaught-exception-handler-beb3ea1edb14
public class ExceptionLogger implements UncaughtExceptionHandler {
	
	//https://stackoverflow.com/questions/15758685/how-to-write-logs-in-text-file-when-using-java-util-logging-logger
	Logger logger;
	FileHandler fileHandler;
	
	public ExceptionLogger(boolean useAppData, String appDataFolderName) {

		logger = Logger.getLogger("MyLog");
	    
	    try {  
	    	if (useAppData) {
	    		fileHandler = new FileHandler(System.getenv("APPDATA") + File.separator + appDataFolderName + File.separator + "log" + File.separator + "log.txt");  
	    	} else {
	    		fileHandler = new FileHandler(System.getProperty("user.dir") + File.separator + "log" + File.separator + "log.txt");
	    	}
	        logger.addHandler(fileHandler);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fileHandler.setFormatter(formatter);  
	
	    } 
	    catch (SecurityException e) { e.printStackTrace(); }
	    catch (IOException e) { e.printStackTrace(); }
	    
		Thread.currentThread().setUncaughtExceptionHandler(this);
	}
	
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		logStackTrace(e);
	}
	
	public void logMessage(String message) {
		logger.severe(message);
	}
	
	public void logStackTrace(Throwable e) {
		logger.severe(getStackTrace(e));
	}
	
	public static String getStackTrace(Throwable e) {
		String err = e.toString();
		
		for (StackTraceElement ste : e.getStackTrace()) {
			err = err + System.getProperty("line.separator") + ste.toString();
		}
		return err;
	}
}
