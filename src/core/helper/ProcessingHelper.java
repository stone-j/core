package core.helper;

import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import core.logging.ConsoleHelper;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import processing.core.PConstants;
import processing.data.Table;

public class ProcessingHelper {
	
	public ConsoleHelper consoleHelper = new ConsoleHelper();
	
	//---------------------------------------------------------------------------
	//loadImage
	//---------------------------------------------------------------------------
	public PImage loadImage(String filename) {
		return this.loadImage(filename, null);
	}


	//---------------------------------------------------------------------------
	//loadImage
	//---------------------------------------------------------------------------
	public PImage loadImage(String filename, String extension) { //, Object params) {

		if (extension == null) {
			String lower = filename.toLowerCase();
			int dot = filename.lastIndexOf('.');
			if (dot == -1) {
				extension = "unknown";  // no extension found

			} else {
				extension = lower.substring(dot + 1);

				// check for, and strip any parameters on the url, i.e.
				// filename.jpg?blah=blah&something=that
				int question = extension.indexOf('?');
				if (question != -1) {
					extension = extension.substring(0, question);
				}
			}
		}

		// just in case. them users will try anything!
		extension = extension.toLowerCase();

		if (extension.equals("tga")) {
			try {
				PImage image = loadImageTGA(filename);
				//	        if (params != null) {
				//	          image.setParams(g, params);
				//	        }
				return image;
			} catch (IOException e) {
				consoleHelper.printStackTrace(e);
				return null;
			}
		}

//		if (extension.equals("tif") || extension.equals("tiff")) {
//			byte bytes[] = loadBytes(filename);
//			PImage image =  (bytes == null) ? null : PImage.loadTIFF(bytes);
//			//	      if (params != null) {
//			//	        image.setParams(g, params);
//			//	      }
//			return image;
//		}

		// For jpeg, gif, and png, load them using createImage(),
		// because the javax.imageio code was found to be much slower.
		// http://dev.processing.org/bugs/show_bug.cgi?id=392
		try {
			if (extension.equals("jpg") || extension.equals("jpeg") ||
					extension.equals("gif") || extension.equals("png") ||
					extension.equals("unknown")) {
				byte bytes[] = loadBytes(filename);
				if (bytes == null) {
					return null;
				} else {
					//Image awtImage = Toolkit.getDefaultToolkit().createImage(bytes);
					Image awtImage = new ImageIcon(bytes).getImage();

					if (awtImage instanceof BufferedImage) {
						BufferedImage buffImage = (BufferedImage) awtImage;
						int space = buffImage.getColorModel().getColorSpace().getType();
						if (space == ColorSpace.TYPE_CMYK) {
							System.err.println(filename + " is a CMYK image, " +
									"only RGB images are supported.");
							return null;
							/*
	              // wishful thinking, appears to not be supported
	              // https://community.oracle.com/thread/1272045?start=0&tstart=0
	              BufferedImage destImage =
	                new BufferedImage(buffImage.getWidth(),
	                                  buffImage.getHeight(),
	                                  BufferedImage.TYPE_3BYTE_BGR);
	              ColorConvertOp op = new ColorConvertOp(null);
	              op.filter(buffImage, destImage);
	              image = new PImage(destImage);
							 */
						}
					}

					PImage image = new PImage(awtImage);
					if (image.width == -1) {
						System.err.println("The file " + filename +
								" contains bad image data, or may not be an image.");
					}

					//format should always be ARGB, so this does not seem to be needed -JS
					//					// if it's a .gif image, test to see if it has transparency
					//					if (extension.equals("gif") || extension.equals("png") ||
					//							extension.equals("unknown")) {
					//						image.checkAlpha();
					//					}

					//	          if (params != null) {
					//	            image.setParams(g, params);
					//	          }
					//removed dependency on PApplet -JS
					//image.parent = this;
					return image;
				}
			}
		} catch (Exception e) {
			// show error, but move on to the stuff below, see if it'll work
			consoleHelper.printStackTrace(e);
		}

		String[] loadImageFormats = ImageIO.getReaderFormatNames();

		if (loadImageFormats != null) {
			for (int i = 0; i < loadImageFormats.length; i++) {
				if (extension.equals(loadImageFormats[i])) {
					return this.loadImageIO(filename);
					//	          PImage image = loadImageIO(filename);
					//	          if (params != null) {
					//	            image.setParams(g, params);
					//	          }
					//	          return image;
				}
			}
		}

		// failed, could not load image after all those attempts
		System.err.println("Could not find a method to load " + filename);
		return null;
	}


	//---------------------------------------------------------------------------
	//loadImage
	//---------------------------------------------------------------------------
	protected PImage loadImageTGA(String filename) throws IOException {
		InputStream is = this.createInput(filename);
		if (is == null) return null;

		byte header[] = new byte[18];
		int offset = 0;
		do {
			int count = is.read(header, offset, header.length - offset);
			if (count == -1) return null;
			offset += count;
		} while (offset < 18);

		/*
	      header[2] image type code
	      2  (0x02) - Uncompressed, RGB images.
	      3  (0x03) - Uncompressed, black and white images.
	      10 (0x0A) - Run-length encoded RGB images.
	      11 (0x0B) - Compressed, black and white images. (grayscale?)
	      header[16] is the bit depth (8, 24, 32)
	      header[17] image descriptor (packed bits)
	      0x20 is 32 = origin upper-left
	      0x28 is 32 + 8 = origin upper-left + 32 bits
	        7  6  5  4  3  2  1  0
	      128 64 32 16  8  4  2  1
		 */

		int format = 0;

		if (((header[2] == 3) || (header[2] == 11)) &&  // B&W, plus RLE or not
				(header[16] == 8) &&  // 8 bits
				((header[17] == 0x8) || (header[17] == 0x28))) {  // origin, 32 bit
			format = PConstants.ALPHA;

		} else if (((header[2] == 2) || (header[2] == 10)) &&  // RGB, RLE or not
				(header[16] == 24) &&  // 24 bits
				((header[17] == 0x20) || (header[17] == 0))) {  // origin
			format = PConstants.RGB;

		} else if (((header[2] == 2) || (header[2] == 10)) &&
				(header[16] == 32) &&
				((header[17] == 0x8) || (header[17] == 0x28))) {  // origin, 32
			format = PConstants.ARGB;
		}

		if (format == 0) {
			System.err.println("Unknown .tga file format for " + filename);
			//" (" + header[2] + " " +
			//(header[16] & 0xff) + " " +
			//hex(header[17], 2) + ")");
			return null;
		}

		int w = ((header[13] & 0xff) << 8) + (header[12] & 0xff);
		int h = ((header[15] & 0xff) << 8) + (header[14] & 0xff);
		PImage outgoing = createImage(w, h, format);

		// where "reversed" means upper-left corner (normal for most of
		// the modernized world, but "reversed" for the tga spec)
		//boolean reversed = (header[17] & 0x20) != 0;
		// https://github.com/processing/processing/issues/1682
		boolean reversed = (header[17] & 0x20) == 0;

		if ((header[2] == 2) || (header[2] == 3)) {  // not RLE encoded
			if (reversed) {
				int index = (h-1) * w;
				switch (format) {
				case PConstants.ALPHA:
					for (int y = h-1; y >= 0; y--) {
						for (int x = 0; x < w; x++) {
							outgoing.pixels[index + x] = is.read();
						}
						index -= w;
					}
					break;
				case PConstants.RGB:
					for (int y = h-1; y >= 0; y--) {
						for (int x = 0; x < w; x++) {
							outgoing.pixels[index + x] =
									is.read() | (is.read() << 8) | (is.read() << 16) |
									0xff000000;
						}
						index -= w;
					}
					break;
				case PConstants.ARGB:
					for (int y = h-1; y >= 0; y--) {
						for (int x = 0; x < w; x++) {
							outgoing.pixels[index + x] =
									is.read() | (is.read() << 8) | (is.read() << 16) |
									(is.read() << 24);
						}
						index -= w;
					}
				}
			} else {  // not reversed
				int count = w * h;
				switch (format) {
				case PConstants.ALPHA:
					for (int i = 0; i < count; i++) {
						outgoing.pixels[i] = is.read();
					}
					break;
				case PConstants.RGB:
					for (int i = 0; i < count; i++) {
						outgoing.pixels[i] =
								is.read() | (is.read() << 8) | (is.read() << 16) |
								0xff000000;
					}
					break;
				case PConstants.ARGB:
					for (int i = 0; i < count; i++) {
						outgoing.pixels[i] =
								is.read() | (is.read() << 8) | (is.read() << 16) |
								(is.read() << 24);
					}
					break;
				}
			}

		} else {  // header[2] is 10 or 11
			int index = 0;
			int px[] = outgoing.pixels;

			while (index < px.length) {
				int num = is.read();
				boolean isRLE = (num & 0x80) != 0;
				if (isRLE) {
					num -= 127;  // (num & 0x7F) + 1
					int pixel = 0;
					switch (format) {
					case PConstants.ALPHA:
						pixel = is.read();
						break;
					case PConstants.RGB:
						pixel = 0xFF000000 |
						is.read() | (is.read() << 8) | (is.read() << 16);
						//(is.read() << 16) | (is.read() << 8) | is.read();
						break;
					case PConstants.ARGB:
						pixel = is.read() |
						(is.read() << 8) | (is.read() << 16) | (is.read() << 24);
						break;
					}
					for (int i = 0; i < num; i++) {
						px[index++] = pixel;
						if (index == px.length) break;
					}
				} else {  // write up to 127 bytes as uncompressed
					num += 1;
					switch (format) {
					case PConstants.ALPHA:
						for (int i = 0; i < num; i++) {
							px[index++] = is.read();
						}
						break;
					case PConstants.RGB:
						for (int i = 0; i < num; i++) {
							px[index++] = 0xFF000000 |
									is.read() | (is.read() << 8) | (is.read() << 16);
							//(is.read() << 16) | (is.read() << 8) | is.read();
						}
						break;
					case PConstants.ARGB:
						for (int i = 0; i < num; i++) {
							px[index++] = is.read() | //(is.read() << 24) |
									(is.read() << 8) | (is.read() << 16) | (is.read() << 24);
							//(is.read() << 16) | (is.read() << 8) | is.read();
						}
						break;
					}
				}
			}

			if (!reversed) {
				int[] temp = new int[w];
				for (int y = 0; y < h/2; y++) {
					int z = (h-1) - y;
					System.arraycopy(px, y*w, temp, 0, w);
					System.arraycopy(px, z*w, px, y*w, w);
					System.arraycopy(temp, 0, px, z*w, w);
				}
			}
		}
		is.close();
		return outgoing;
	}


	public static PImage createImage(int w, int h, int format) {
		PImage image = new PImage(w, h, format);
		//removed dependency on PApplet -JS
		//image.parent = this;  // make save() work
		return image;
	}
		
		
	public static ArrayList<PVector> RotationCorrection (ArrayList<PVector> myPVector) {

		ArrayList<PVector> myPVector_Rotated = new ArrayList<PVector>();

		//determine if the card's orientation warrants a rotational correction
		if (
			//PVector.dist(myPVector.get(0), myPVector.get(1)) +
			//PVector.dist(myPVector.get(2), myPVector.get(3)) >
			////----------is greater than ?------------------
			//PVector.dist(myPVector.get(1), myPVector.get(2)) +
			//PVector.dist(myPVector.get(3), myPVector.get(0))
			
			myPVector.get(0).x + myPVector.get(0).y < 
			myPVector.get(1).x + myPVector.get(1).y
		) {
			//System.out.println("I NEED rotation correction");
			myPVector_Rotated.add(myPVector.get(3));
			myPVector_Rotated.add(myPVector.get(0));
			myPVector_Rotated.add(myPVector.get(1));
			myPVector_Rotated.add(myPVector.get(2));
		} else {
			//System.out.println("I do NOT need rotation correction");
			myPVector_Rotated = myPVector;
		}

		return myPVector_Rotated;
	}
		
		
	//---------------------------------------------------------------------------
	//loadBytes
	//---------------------------------------------------------------------------
	public static byte[] loadBytes(InputStream input) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buffer = new byte[4096];

			int bytesRead = input.read(buffer);
			while (bytesRead != -1) {
				out.write(buffer, 0, bytesRead);
				bytesRead = input.read(buffer);
			}
			out.flush();
			return out.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	//---------------------------------------------------------------------------
	//loadBytes
	//---------------------------------------------------------------------------
	public byte[] loadBytes(String filename) {
		String lower = filename.toLowerCase();

		InputStream is = this.createInput(filename);
		if (is != null) {
			byte[] outgoing = loadBytes(is);
			try {
				is.close();
			} catch (IOException e) {
				consoleHelper.printStackTrace(e);  // shouldn't happen
			}
			return outgoing;
		}

		System.err.println("The file \"" + filename + "\" " +
				"is missing or inaccessible, make sure " +
				"the URL is valid or that the file has been " +
				"added to your sketch and is readable.");
		return null;
	}


	//---------------------------------------------------------------------------
	//createInput
	//---------------------------------------------------------------------------
	public InputStream createInput(String filename) {
		InputStream input = createInputRaw(filename);
		return new BufferedInputStream(input);
	}
	
	
	//---------------------------------------------------------------------------
	//createInputRaw
	//---------------------------------------------------------------------------
	public InputStream createInputRaw(String filename) {

		// First check whether this looks like a URL
		if (filename.contains(":")) {  // at least smells like URL
			try {
				URL url = new URL(filename);
				URLConnection conn = url.openConnection();

				if (conn instanceof HttpURLConnection) {
					HttpURLConnection httpConn = (HttpURLConnection) conn;
					// Will not handle a protocol change (see below)
					httpConn.setInstanceFollowRedirects(true);
					int response = httpConn.getResponseCode();
					// Default won't follow HTTP -> HTTPS redirects for security reasons
					// http://stackoverflow.com/a/1884427
					if (response >= 300 && response < 400) {
						String newLocation = httpConn.getHeaderField("Location");
						return createInputRaw(newLocation);
					}
					return conn.getInputStream();
				} else if (conn instanceof JarURLConnection) {
					return url.openStream();
				}
			} catch (MalformedURLException mfue) {
				// not a url, that's fine

			} catch (FileNotFoundException fnfe) {
				// Added in 0119 b/c Java 1.5 throws FNFE when URL not available.
				// http://dev.processing.org/bugs/show_bug.cgi?id=403

			} catch (IOException e) {
				// changed for 0117, shouldn't be throwing exception
				consoleHelper.printStackTrace(e);
				//System.err.println("Error downloading from URL " + filename);
				return null;
				//throw new RuntimeException("Error downloading from URL " + filename);
			}
		}

		InputStream stream = null;

		try {
			// attempt to load from a local file, used when running as
			// an application, or as a signed applet
			try {  // first try to catch any security exceptions
				try {
					stream = new FileInputStream(filename);
					if (stream != null) return stream;
				} catch (IOException e1) { }

			} catch (SecurityException se) { }  // online, whups

		} catch (Exception e) {
			consoleHelper.printStackTrace(e);
		}

		return null;
	}
		
		
	//---------------------------------------------------------------------------
	//loadImageIO
	//---------------------------------------------------------------------------
	protected PImage loadImageIO(String filename) {
		InputStream stream = createInput(filename);
		if (stream == null) {
			System.err.println("The image " + filename + " could not be found.");
			return null;
		}

		try {
			BufferedImage bi = ImageIO.read(stream);
			PImage outgoing = new PImage(bi.getWidth(), bi.getHeight());
			//removed dependency on PApplet -JS
			//outgoing.parent = this;

			bi.getRGB(0, 0, outgoing.width, outgoing.height,
					outgoing.pixels, 0, outgoing.width);

			//removed, since all images should be ARGB -JS
			//			// check the alpha for this image
			//			// was gonna call getType() on the image to see if RGB or ARGB,
			//			// but it's not actually useful, since gif images will come through
			//			// as TYPE_BYTE_INDEXED, which means it'll still have to check for
			//			// the transparency. also, would have to iterate through all the other
			//			// types and guess whether alpha was in there, so.. just gonna stick
			//			// with the old method.
			//			outgoing.checkAlpha();

			stream.close();
			// return the image
			return outgoing;

		} catch (Exception e) {
			consoleHelper.printStackTrace(e);
			return null;
		}
	}
	
	
	//---------------------------------------------------------------------------
	// loadTable
	//---------------------------------------------------------------------------
	public Table loadTable(String filename, String options) {
		try {
			String optionStr = Table.extensionOptions(true, filename, options);
			String[] optionList = StringHelper.trim(StringHelper.split(optionStr, ','));

			Table dictionary = null;
			for (String opt : optionList) {
				if (opt.startsWith("dictionary=")) {
					dictionary = loadTable(opt.substring(opt.indexOf('=') + 1), "tsv");
					return dictionary.typedParse(createInput(filename), optionStr);
				}
			}
			InputStream input = createInput(filename);
			if (input == null) {
				System.err.println(filename + " does not exist or could not be read");
				return null;
			}
			return new Table(input, optionStr);

		} catch (IOException e) {
			consoleHelper.printStackTrace(e);
			return null;
		}
	}
	
	
	//---------------------------------------------------------------------------
	//createGraphics
	//---------------------------------------------------------------------------
	synchronized public PGraphics createGraphics(int w, int h, String renderer, String path) {
		return makeGraphics(w, h, renderer, path, false);
	}
	
	
	//---------------------------------------------------------------------------
	//makeGraphics
	//---------------------------------------------------------------------------
	synchronized protected PGraphics makeGraphics(int w, int h, String renderer, String path, boolean primary) {
		//String openglError = external ?
		//// This first one should no longer be possible
		//"Before using OpenGL, first select " +
		//"Import Library > OpenGL from the Sketch menu." :
		//// Welcome to Java programming! The training wheels are off.
		//"The Java classpath and native library path is not " +
		//"properly set for using the OpenGL library.";

		try {
			Class<?> rendererClass =
					Thread.currentThread().getContextClassLoader().loadClass(renderer);

			Constructor<?> constructor = rendererClass.getConstructor(new Class[] { });
			PGraphics pg = (PGraphics) constructor.newInstance();

			//pg.setParent(this);
			pg.setPrimary(primary);
			if (path != null) {
				//pg.setPath(savePath(path));
				pg.setPath(path);
			}
			//pg.setQuality(sketchQuality());
			//if (!primary) {
			//surface.initImage(pg, w, h);
			//}
			pg.setSize(w, h);

			// everything worked, return it
			return pg;

		} catch (InvocationTargetException ite) {
			String msg = ite.getTargetException().getMessage();
			if ((msg != null) &&
					(msg.indexOf("no jogl in java.library.path") != -1)) {
				// Is this true anymore, since the JARs contain the native libs?
				throw new RuntimeException("The jogl library folder needs to be " +
						"specified with -Djava.library.path=/path/to/jogl");

			} else {
				consoleHelper.PrintMessage(ite.getTargetException().toString());
				Throwable target = ite.getTargetException();
				throw new RuntimeException(target.getMessage());
			}

		} catch (ClassNotFoundException cnfe) {
		} catch (Exception e) {
			if ((e instanceof IllegalArgumentException) ||
					(e instanceof NoSuchMethodException) ||
					(e instanceof IllegalAccessException)) {
				if (e.getMessage().contains("cannot be <= 0")) {
					// IllegalArgumentException will be thrown if w/h is <= 0
					// http://code.google.com/p/processing/issues/detail?id=983
					throw new RuntimeException(e);

				} else {
					consoleHelper.PrintMessage(e.toString());
					String msg = renderer + " needs to be updated " +
							"for the current release of Processing.";
					throw new RuntimeException(msg);
				}
			} else {
				consoleHelper.PrintMessage(e.toString());
				throw new RuntimeException(e.getMessage());
			}
		}
		return null;
	}
}
