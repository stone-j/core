package core;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class FontHelper {
	
	public static Font loadFont(String fontFileName, int fontSize, ExceptionLogger exceptionLogger) {	
	
		//initialize the font to a default
		Font font = new Font("", Font.PLAIN, fontSize);
		
		//Attempt to load the custom font
		try {
		    //create the font to use. Specify the size!
			ConsoleHelper.PrintMessage("Attempting to load font file: " + System.getProperty("user.dir") + File.separator + "fonts" + File.separator + fontFileName);
			font = Font.createFont(Font.TRUETYPE_FONT, new File(System.getProperty("user.dir") + File.separator + "fonts" + File.separator + fontFileName)).deriveFont((float)fontSize);
		    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    //register the font
		    ge.registerFont(font);
		} 
		catch(IOException e) {
			exceptionLogger.logStackTrace(e);
		}
		catch(FontFormatException e) {
			exceptionLogger.logStackTrace(e);
		}
		
		return font;
	}
}
