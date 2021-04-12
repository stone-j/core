package core.helper;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import processing.core.PImage;

public class ColorHelper {
	
	public static Color getOppositeColor(Color color) {
		return new Color (
			255 - color.getRed(),
			255 - color.getGreen(),
			255 - color.getBlue(),
			color.getAlpha()
		);
	}
	
	
	public static Color getOppositeBrightness(Color color) {
		float[] hsbVals = new float[3];
		Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsbVals);
		Color modifiedColor = new Color(Color.HSBtoRGB(
			hsbVals[0],
			hsbVals[1],
			(hsbVals[2] + 0.5f) % 1.0f
		));
		return new Color(
			modifiedColor.getRed(),
			modifiedColor.getGreen(),
			modifiedColor.getBlue(),
			color.getAlpha()
		);
	}
	
		
	public static Color getMostFrequentColorFromArray(Color colors[]) {
		
	    Color maxValue = null;
	    int maxCount = 0;

	    for (int i = 0; i < colors.length; i++) {
	        int count = 0;
	        for (int j = 0; j < colors.length; j++) {
	        	if (!(colors[i]==null) && !(colors[j]==null)) {
	        		if (colors[j].getRGB() == colors[i].getRGB()) count++;
	        	}
	        }
	        if (count > maxCount) {
	            maxCount = count;
	            maxValue = colors[i];
	        }
	    }

	    return maxValue;
	}
	
	
	public static Color getMostFrequentColorFromArrayList(ArrayList<Color> colors) {
		
	    Color maxValue = null;
	    int maxCount = 0;

	    for (Color c : colors) {
	        int count = 0;
	        for (Color c2 : colors) {
	        	if (c.getRGB() == c2.getRGB()) {
	        		count++;
	        	}
	        }
	        if (count > maxCount) {
	            maxCount = count;
	            maxValue = c;
	        }
	    }

	    return maxValue;
	}
	
	public static int[] GetAverageRGBLValues(PImage MyImage) {

		int pixelCount = 0;
		int RGBLValues[] = new int[4];

		Color currentPixelColor;

		RGBLValues[0] = 0;
		RGBLValues[1] = 0;
		RGBLValues[2] = 0;

		for (int i = 0; i < MyImage.width * MyImage.height; i++) {   
			currentPixelColor = ColorHelper.pixeltoColor(MyImage.pixels[i]);
			if (currentPixelColor.getAlpha() == 255) {
				RGBLValues[0] += currentPixelColor.getRed();
				RGBLValues[1] += currentPixelColor.getGreen();
				RGBLValues[2] += currentPixelColor.getBlue();
				//RGBLValues[3] += brightness(MyImage.pixels[i]);
				RGBLValues[3] += (int) (Color.HSBtoRGB(currentPixelColor.getRed(), currentPixelColor.getGreen(), currentPixelColor.getBlue()) * 255.0);

				pixelCount++;
			}
		} 

		if (pixelCount > 0) {
			RGBLValues[0] /= pixelCount;
			RGBLValues[1] /= pixelCount;
			RGBLValues[2] /= pixelCount;
			RGBLValues[3] /= pixelCount;
		}

		//colorMode(RGB, 255);

		return RGBLValues;
	}
	
	
	public static int[] GetAverageAndStdevRGBLValues(PImage MyImage) {

		int pixelCount = 0;
		int RGBLValues[] = new int[8];

		int stdevRed = 0;
		int stdevGreen = 0;
		int stdevBlue = 0;
		int stdevBrightness = 0;

		Color currentPixelColor;
		float[] hsbvals = null; //this is a container for the return array from Color.RGBtoHSB (return values are [hue, sat, lum])

		RGBLValues[0] = 0;
		RGBLValues[1] = 0;
		RGBLValues[2] = 0;  
		RGBLValues[3] = 0;
		RGBLValues[4] = 0;
		RGBLValues[5] = 0; 
		RGBLValues[6] = 0;
		RGBLValues[7] = 0;	

		for (int i = 0; i < MyImage.width * MyImage.height; i++) {    
			currentPixelColor = ColorHelper.pixeltoColor(MyImage.pixels[i]);
			if (currentPixelColor.getAlpha() == 255) {
				RGBLValues[0] += currentPixelColor.getRed();
				RGBLValues[1] += currentPixelColor.getGreen();
				RGBLValues[2] += currentPixelColor.getBlue();
				int brightness = (int)(Color.RGBtoHSB(currentPixelColor.getRed(), currentPixelColor.getGreen(), currentPixelColor.getBlue(), hsbvals)[2] * 255.0);
				RGBLValues[3] += brightness;
				pixelCount++;
			}
		}

		if (pixelCount > 0) {
			RGBLValues[0] /= pixelCount;
			RGBLValues[1] /= pixelCount;
			RGBLValues[2] /= pixelCount;
			RGBLValues[3] /= pixelCount;
		}

		for (int i = 0; i < MyImage.width * MyImage.height; i++) {    
			currentPixelColor = ColorHelper.pixeltoColor(MyImage.pixels[i]);
			if (currentPixelColor.getAlpha() == 255) {
				stdevRed 		+= Math.pow(currentPixelColor.getRed() - RGBLValues[0], 2);
				stdevGreen 		+= Math.pow(currentPixelColor.getGreen() - RGBLValues[1], 2);
				stdevBlue 		+= Math.pow(currentPixelColor.getBlue() - RGBLValues[2], 2);
				int brightness = (int)(Color.RGBtoHSB(currentPixelColor.getRed(), currentPixelColor.getGreen(), currentPixelColor.getBlue(), hsbvals)[2] * 255.0);
				stdevBrightness += Math.pow(brightness - RGBLValues[3], 2);
			}
		}

		if (pixelCount > 0) {
			RGBLValues[4] = (int)Math.sqrt(stdevRed			/ pixelCount);
			RGBLValues[5] = (int)Math.sqrt(stdevGreen		/ pixelCount);
			RGBLValues[6] = (int)Math.sqrt(stdevBlue		/ pixelCount);
			RGBLValues[7] = (int)Math.sqrt(stdevBrightness	/ pixelCount);
		}

		// println("stdevRed = " + Integer.toString(RGBLValues[4]));
		// println("stdevGreen = " + Integer.toString(RGBLValues[5]));
		// println("stdevBlue = " + Integer.toString(RGBLValues[6]));
		// println("stdevLuminance = " + Integer.toString(RGBLValues[7]));

		return RGBLValues;
	}
	
	
	/*
	This method removes the outliers of each color COMPONENT (i.e. r, g, b, a) rather than
	the outlying colors in total. A more elaborate process would be needed to determine which colors
	are the outliers by comparing the distance between the value of each component to the value of
	each component of every other Color in the array. This would be more costly, but more accurate.
	*/
	public static Color getAverageColorFromColorArray(Color[] color) {
		
		List<Integer> redList = new ArrayList<Integer>();
		List<Integer> greenList = new ArrayList<Integer>();
		List<Integer> blueList = new ArrayList<Integer>();
		List<Integer> alphaList = new ArrayList<Integer>();
		
		int redBucket = 0;
		int greenBucket = 0;
		int blueBucket = 0;
		int alphaBucket = 0;
		
		//this is to remove the top 1/5th and bottom 1/5th of total samples (outliers)
		int numberOfItemsToRemove = (int)Math.floor(color.length / 5);
		
		for (int i = 0; i < color.length; i++) {
			
			redList.add(color[i].getRed());
			greenList.add(color[i].getGreen());
			blueList.add(color[i].getBlue());
			alphaList.add(color[i].getAlpha());
		}
		
		Collections.sort(redList);
		Collections.sort(greenList);
		Collections.sort(blueList);
		Collections.sort(alphaList);
		
		//remove outliers from sorted lists (first and last indexes are excluded)
		for (int i = numberOfItemsToRemove; i < color.length - numberOfItemsToRemove; i++) {
			
			redBucket += redList.get(i);
			greenBucket += greenList.get(i);
			blueBucket += blueList.get(i);
			alphaBucket += alphaList.get(i);
		}
		
		return new Color(
			redBucket / (color.length - (numberOfItemsToRemove * 2)),
			greenBucket / (color.length - (numberOfItemsToRemove * 2)),
			blueBucket / (color.length - (numberOfItemsToRemove * 2)),
			alphaBucket / (color.length - (numberOfItemsToRemove * 2))
		);
	}


	//---------------------------------------------------------------------------
	// pixeltoColor
	//---------------------------------------------------------------------------
	public static Color pixeltoColor(int myPixel) {

		int a = (myPixel >> 24) & 0xFF;
		int r = (myPixel >> 16) & 0xFF;  // Faster way of getting red(argb)
		int g = (myPixel >> 8) & 0xFF;   // Faster way of getting green(argb)
		int b = myPixel & 0xFF;          // Faster way of getting blue(argb)

		return new Color(r,g,b,a);
	}

	//---------------------------------------------------------------------------
	// colorToProcessingColor
	//---------------------------------------------------------------------------
	public static int colorToProcessingColor(Color color) {
		return
				(color.getRed  () 	<< 16) +
				(color.getGreen() 	<<  8) +
				(color.getBlue ()		 ) +
				(color.getAlpha() 	<< 24);
	}


	//---------------------------------------------------------------------------
	// rgbaToProcessingColor
	//---------------------------------------------------------------------------
	public static int rgbaToProcessingColor(int red, int green, int blue, int alpha) {
		return (red << 16) + (green << 8) + (blue) + (alpha << 24);
	}

	//---------------------------------------------------------------------------
	// singleValueToGrayscaleProcessingColor
	//---------------------------------------------------------------------------
	public static int singleValueToGrayscaleProcessingColor(int value) {
		return rgbaToProcessingColor(value, value, value, 255);
	}
	
	//---------------------------------------------------------------------------
	// GetTextColorForBGColor
	//---------------------------------------------------------------------------
	public static Color GetTextColorForBGColor(Color bgColor, boolean getSubduedColor) {
		if (bgColor.getRed() + bgColor.getGreen() + bgColor.getBlue() < (100 * 3)) {
			if (getSubduedColor) {
				Color darkerWhite = Color.WHITE;
				for(int i = 0; i < 1; i++) {
					darkerWhite = darkerWhite.darker();
				}
				return darkerWhite;
			} else {
				return Color.WHITE;
			}
		} else {
			if (getSubduedColor) {
				Color lighterBlack = Color.BLACK;
				for(int i = 0; i < 11; i++) {
					lighterBlack = lighterBlack.brighter();
				}
				return lighterBlack;
			} else {
				return Color.BLACK;
			}
		}
	}
	
	//---------------------------------------------------------------------------
	// GetTextColorForBGColor
	//---------------------------------------------------------------------------
	public static Color GetTextColorForBGColor(Color bgColor) {
		return GetTextColorForBGColor(bgColor, false);
	}
}
