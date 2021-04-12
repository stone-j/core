package core.helper;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;

import core.logging.ConsoleHelper;
import processing.core.PConstants;
import processing.core.PImage;

public class ImageHelper {
	
	public ConsoleHelper consoleHelper = new ConsoleHelper();
	
	//---------------------------------------------------------------------------
	// getScaledImage
	//---------------------------------------------------------------------------
	public static Image getScaledImage(Image srcImg, int w, int h){
	    
		ImageIcon ico = new ImageIcon(srcImg);
	    if (ico.getIconWidth() == w && ico.getIconHeight() == h) {
	    	return srcImg;
	    }
	    
		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();

	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(srcImg, 0, 0, w, h, null);
	    g2.dispose();

	    return resizedImg;
	}
	
	//---------------------------------------------------------------------------
	// resizeImage
	//---------------------------------------------------------------------------
	public PImage resizeImage(PImage image, int scale) {
		consoleHelper.PrintMessage("resizeImage");

		PImage localImage = image.get();
		PImage localCopyImage = image.get();

		int imageHeight = localImage.height;
		int imageWidth = localImage.width;

		localImage.loadPixels();

		consoleHelper.PrintMessage("image dimensions: " + imageWidth + "px W x " + imageHeight + "px H");
		consoleHelper.PrintMessage("scale: " + scale);

		if (scale < 1) {
			return localImage;
		}

		//clear out pixels
		for (int i = 0; i < imageHeight * imageWidth; i++) {
			localImage.pixels[i] = ColorHelper.singleValueToGrayscaleProcessingColor(255);
		}

		localImage.updatePixels();

		//using the built-in resize function alone results in a blurred result, so we do a custom resize below.
		localImage.resize(0, imageHeight * scale);

		localImage.loadPixels();

		//loop through each pixel in the original image
		for (int i = 0; i < imageHeight * imageWidth; i++) {
			//loop for pixel columns
			for (int j = 0; j < scale; j++) {
				//loop for pixel rows
				for (int k = 0; k < scale; k++) {
					//I don't understand what this line is doing, but I think it's beautiful. Somehow this is upscaling the image.
					localImage.pixels[j + k * imageWidth * scale + i * scale * scale - i % imageWidth * scale * (scale - 1)] = localCopyImage.pixels[i];
				}
			}
		}
		localImage.updatePixels();
		return localImage;
	}
		
		
	static synchronized PImage SharpenImage(PImage img) {

		Color currentPixelColor;

		float[][] kernel = {{ -0.5f, -1.0f, -0.5f}, 
				{ -1.0f, 6.0f, -1.0f}, 
				{ -0.5f, -1.0f, -0.5f}};

		img.loadPixels();
		// Create an opaque image of the same size as the original
		PImage edgeImg = img.get();

		// Loop through every pixel in the image.
		for (int y = 1; y < img.height-1; y++) { // Skip top and bottom edges

			for (int x = 1; x < img.width-1; x++) { // Skip left and right edges

				currentPixelColor = ColorHelper.pixeltoColor(img.pixels[y * img.width + x]);

				//coreA refers to the alpha value of the target (center of the matrix) pixel
				float coreA = currentPixelColor.getAlpha();

				float
				sumR = 0,
				sumG = 0,
				sumB = 0;

				for (int ky = -1; ky <= 1; ky++) {

					for (int kx = -1; kx <= 1; kx++) {

						// Calculate the adjacent pixel for this kernel point
						int pos = (y + ky)*img.width + (x + kx);

						currentPixelColor = ColorHelper.pixeltoColor(img.pixels[pos]);
						float valR = currentPixelColor.getRed();
						float valG = currentPixelColor.getGreen();
						float valB = currentPixelColor.getBlue();
						//valA refers to the alpha value of the current kernel matrix pixel
						float valA = currentPixelColor.getAlpha();


						float kernelPlusOne = (ky==0 && kx==0 ? 1.0f : 0.0f);

						//only do the work if both pixels are fully opaque
						if (valA == 255 && coreA == 255) {
							// Multiply adjacent pixels based on the kernel values
							sumR += (kernel[ky+1][kx+1] * 1.0 + kernelPlusOne) * valR;
							sumG += (kernel[ky+1][kx+1] * 1.0 + kernelPlusOne) * valG;
							sumB += (kernel[ky+1][kx+1] * 1.0 + kernelPlusOne) * valB;
						}
					}
				}

				// For this pixel in the new image, set the RGB values
				// based on the sum from the kernel
				edgeImg.pixels[y*img.width + x] = ColorHelper.rgbaToProcessingColor((int)sumR, (int)sumG, (int)sumB, (int)coreA);
			}
		}
		// State that there are changes to edgeImg.pixels[]
		edgeImg.updatePixels();

		return edgeImg;
	}
	
	
	public BufferedImage RotateImage180(BufferedImage img) {
		return
			pImageToBufferedImage(
				RotateImage180(
					bufferedImagetoPImage(img)
				)
			);
	}
		
	//https://stackoverflow.com/questions/29334348/processing-mirror-image-over-x-axis
	public static PImage RotateImage180(PImage img) {
		//PImage imgRotated = createImage(img.width, img.height, RGB);
		PImage imgRotated = img.get();

		for (int i = 0 ; i < img.pixels.length; i++){
			int srcX = i % img.width;
			int dstX = img.width - srcX - 1;
			int srcY = i / img.width; 
			int dstY = img.height - srcY - 1;
			imgRotated.pixels[dstY * imgRotated.width + dstX] = img.pixels[i];
		}

		return imgRotated;
	}
	
	
	public static PImage RotateImage90(PImage img) {
		PImage imgRotated = new PImage(img.height, img.width, PImage.ARGB);

		for (int y = 0; y < img.height; y++) {
			for (int x = 0; x < img.width; x++) {
				int dstX = imgRotated.width - y - 1;
				int dstY = x;
				imgRotated.pixels[dstX + dstY * imgRotated.width] = img.pixels[x + y * img.width];
			}
		}
		
		return imgRotated;
	}
	
	
	public static PImage MakeTransparentRegion(PImage img, int xStart, int yStart, int width, int height) {

		PImage newImg;
		newImg = img.get();
		int xPos;
		int yPos;

		for (int y = 0; y < img.height; y++) {
			for (int x = 0; x < img.width; x++) {
				if
				(
						y >= yStart &&
						x >= xStart &&
						y <  yStart + height &&
						x <  xStart + width
						) {
					newImg.pixels[y * img.width + x] = ColorHelper.colorToProcessingColor(new Color(0, 0, 0, 0));
				}
			}
		}
		return newImg;
	}
	
	
	//---------------------------------------------------------------------------
	// bufferedImagetoPImage
	//---------------------------------------------------------------------------
	public static PImage bufferedImagetoPImage(BufferedImage bimg) {
		try {
			PImage img=new PImage(bimg.getWidth(),bimg.getHeight(),PConstants.ARGB);
			img.pixels = bimg.getRGB(0, 0, img.width, img.height, img.pixels, 0, img.width);
			img.updatePixels();
			return img;
		}
		catch(Exception e) {
			System.err.println("Can't create image from buffer");
			e.printStackTrace();
		}
		return null;
	}
	
	
	//---------------------------------------------------------------------------
	// bufferedImagetoPImage
	//---------------------------------------------------------------------------
	public BufferedImage pImageToBufferedImage(PImage pImage) {
		return (BufferedImage)pImage.getNative();
	}
	
	
	//this method seems to be the only way to get a BufferedImage from file/URL WITH ALPHA
	public BufferedImage getBufferedImageWithAlphaChannelFromURL(String url) {
		
		ImageIcon imageIcon = null;
		
		//different approaches depending on if the "url" is on the web, or local disk
		if (url.contains("http")) {		
		
			try { imageIcon = new ImageIcon(new URL(url)); }
			catch (MalformedURLException e) {
				consoleHelper.printStackTrace(e);
				consoleHelper.PrintMessage("Malformed URL is: " + url);
			}
		} else {
			imageIcon = new ImageIcon(url);
		}
		
		BufferedImage b = imageToBufferedImage(imageIcon.getImage());
		
		return b;
	}
	
	
	public BufferedImage imageToBufferedImage(Image image)
	{
	    BufferedImage newImage = new BufferedImage(
	        image.getWidth(null),
	        image.getHeight(null),
	        BufferedImage.TYPE_INT_ARGB
	    );
	    Graphics2D g = newImage.createGraphics();
	    g.drawImage(image, 0, 0, null);
	    g.dispose();
	    return newImage;
	}
	
	
	public PImage ReplacePortionOfImage(PImage masterImage, PImage imageToPaste, int xPos, int yPos) {

		//imageToPaste.loadPixels();
		
		PImage myImage = masterImage.get();
		
		//safety checks
		//if the pasted image is outside the bounds of the master image, just return the master image
		if(imageToPaste.width + xPos > masterImage.width || imageToPaste.height + yPos > masterImage.height) {
			return myImage;
		}
		
		for (int y = 0 ; y < imageToPaste.height; y++) {
			for (int x = 0 ; x < imageToPaste.width; x++) {
				myImage.pixels[(y + yPos) * masterImage.width + x + xPos] = imageToPaste.pixels[y * imageToPaste.width + x];
			}
		}
		
		//masterImage.loadPixels();

		return myImage;
	}
}
