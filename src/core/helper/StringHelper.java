package core.helper;

import java.awt.Color;
import java.awt.Graphics;

public class StringHelper {
	//---------------------------------------------------------------------------
	//trim
	//---------------------------------------------------------------------------
	static public String trim(String str) {
		if (str == null) {
			return null;
		}
		return str.replace('\u00A0', ' ').trim();
	}
	
	
	//---------------------------------------------------------------------------
	//trim
	//---------------------------------------------------------------------------
	static public String[] trim(String[] array) {
		if (array == null) {
			return null;
		}
		String[] outgoing = new String[array.length];
		for (int i = 0; i < array.length; i++) {
			if (array[i] != null) {
				outgoing[i] = trim(array[i]);
			}
		}
		return outgoing;
	}
	
	//---------------------------------------------------------------------------
	//split
	//---------------------------------------------------------------------------
	static public String[] split(String value, char delim) {
		// do this so that the exception occurs inside the user's
		// program, rather than appearing to be a bug inside split()
		if (value == null) return null;
		//return split(what, String.valueOf(delim));  // huh

		char chars[] = value.toCharArray();
		int splitCount = 0; //1;
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == delim) splitCount++;
		}
		// make sure that there is something in the input string
		//if (chars.length > 0) {
		// if the last char is a delimeter, get rid of it..
		//if (chars[chars.length-1] == delim) splitCount--;
		// on second thought, i don't agree with this, will disable
		//}
		if (splitCount == 0) {
			String splits[] = new String[1];
			splits[0] = value;
			return splits;
		}
		//int pieceCount = splitCount + 1;
		String splits[] = new String[splitCount + 1];
		int splitIndex = 0;
		int startIndex = 0;
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == delim) {
				splits[splitIndex++] =
						new String(chars, startIndex, i-startIndex);
				startIndex = i + 1;
			}
		}
		//if (startIndex != chars.length) {
		splits[splitIndex] =
				new String(chars, startIndex, chars.length-startIndex);
		//}
		return splits;
	}
	
	
	//---------------------------------------------------------------------------
	//getRightString
	//---------------------------------------------------------------------------
	public static String getRightString(String string, int length) {
	    int stringlength=string.length();

	    if(stringlength<=length){
	        return string;
	    }

	    return string.substring((stringlength-length));
	}
	
	
	//---------------------------------------------------------------------------
	//getLeftString
	//---------------------------------------------------------------------------
	public static String getLeftString(String string, int length) {
	    int stringlength=string.length();

	    if(stringlength<=length){
	        return string;
	    }

	    return string.substring(0, length);
	}
	
	
	//---------------------------------------------------------------------------
	//removeXML
	// https://stackoverflow.com/questions/240546/remove-html-tags-from-a-string
	//---------------------------------------------------------------------------
	public static String removeXML(String string) {

		return string.replaceAll("\\<[^>]*>","");
	}
	
	
	//---------------------------------------------------------------------------
	//drawString (improves on Graphics.drawString by adding drop-shadows/borders)
	//---------------------------------------------------------------------------
	public static void drawString(Graphics g, String s, int x, int y, boolean drawFontBorder) {
		
		Color fontColor = g.getColor();

		if(drawFontBorder) {
			
			Color fontColorBorder1 = ColorHelper.GetTextColorForBGColor(fontColor);
			//Color fontColorBorder2 = ColorHelper.GetTextColorForBGColor(fontColorBorder1);
			
//			g.setColor(fontColorBorder1);
//			g.drawString(s, x + 2, y + 2);
//			g.drawString(s, x + 2, y - 2);
//			g.drawString(s, x - 2, y + 2);
//			g.drawString(s, x - 2, y - 2);
//			g.drawString(s, x + 2, y + 0);
//			g.drawString(s, x - 2, y + 0);
//			g.drawString(s, x + 0, y + 2);
//			g.drawString(s, x + 0, y - 2);
			g.setColor(fontColorBorder1);
			g.drawString(s, x + 1, y + 1);
			g.drawString(s, x + 1, y - 1);
			g.drawString(s, x - 1, y + 1);
			g.drawString(s, x - 1, y - 1);
			g.drawString(s, x + 1, y + 0);
			g.drawString(s, x - 1, y + 0);
			g.drawString(s, x + 0, y + 1);
			g.drawString(s, x + 0, y - 1);
		}
		
		g.setColor(fontColor);
		g.drawString(s, x, y);	
	}

}
