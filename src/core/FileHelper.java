package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileHelper {
	
	//---------------------------------------------------------------------------
	// getExtension
	//---------------------------------------------------------------------------
	public static String getExtension(String s) {
		ConsoleHelper.PrintMessage("getExtension");
		String separator = System.getProperty("file.separator");
		String filename;

		// Remove the path upto the filename.
		int lastSeparatorIndex = s.lastIndexOf(separator);
		if (lastSeparatorIndex == -1) {
			filename = s;
		} else {
			filename = s.substring(lastSeparatorIndex + 1);
		}

		// Remove the extension.
		int extensionIndex = filename.lastIndexOf(".");
		if (extensionIndex == -1) {
			return "";
		}

		return filename.substring(extensionIndex + 1);
	}
	
	
	//---------------------------------------------------------------------------
	// GetFileFromAbsoluteOrRelativeFilePath
	//---------------------------------------------------------------------------
	// the idea here is an absolute path will contain a colon, and a relative path
	// will not.
	public static File GetFileFromAbsoluteOrRelativeFilePath(String filePath) {
		
		File file;
		
		if (filePath.contains(":")) {
			file = new File(filePath);
		} else {
			file = new File(System.getProperty("user.dir"), filePath);
		}
		
		return file;
	}
	
	
	//---------------------------------------------------------------------------
	// GetFilenameFromFileChooser
	//---------------------------------------------------------------------------
	public static String GetFilenameFromFileChooser(String[] fileExtensions, String fileDecription) {
		return GetFilenameFromFileChooser(fileExtensions, fileDecription, "");
	}
		
			
	//---------------------------------------------------------------------------
	// GetFilenameFromFileChooser
	//---------------------------------------------------------------------------
	public static String GetFilenameFromFileChooser(String[] fileExtensions, String fileDescription, String startingDirectory) {
		ConsoleHelper.PrintMessage("GetFilenameFromFileChooser");

		JFileChooser chooser = new JFileChooser();
		//File dataDir = new File(startingDirectory, "\\");
		File dataDir = new File(startingDirectory, File.separator);
		chooser.setSelectedFile(dataDir);
		FileNameExtensionFilter fileFilter = 
				new FileNameExtensionFilter(
						fileDescription,
						fileExtensions
						);
		chooser.setFileFilter(fileFilter);
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			ConsoleHelper.PrintMessage("You chose to open this file: " + chooser.getSelectedFile().getName());
			return chooser.getSelectedFile().toString();
		}
		return null;
	}
		
	//---------------------------------------------------------------------------
	// GetFileNameFromFilePath
	//---------------------------------------------------------------------------
	synchronized public static String GetFileNameFromFilePath(String filePath) {
		ConsoleHelper.PrintMessage("GetFileNameFromFilePath");
		
		File file = new File(filePath);
		return file.getName();
	}
	
	//---------------------------------------------------------------------------
	// GetPathFromFilePath //still needs to be tested
	//---------------------------------------------------------------------------
	synchronized public static String GetPathFromFilePath(String filePath) {
		ConsoleHelper.PrintMessage("GetPathFromFilePath");	
		
		return filePath.substring(0, filePath.lastIndexOf(File.separator));
	}
	
	
	//---------------------------------------------------------------------------
	// remove file extension
	//---------------------------------------------------------------------------
	synchronized public static String removeExtension(String filename) {
		ConsoleHelper.PrintMessage("removeExtension");

		int extensionIndex = filename.lastIndexOf(".");
		if (extensionIndex == -1) {
			return filename;
		}

		return filename.substring(0, extensionIndex);
	}
	
	
	//---------------------------------------------------------------------------
	// CopyFileUsingStream
	//from https://www.journaldev.com/861/java-copy-file
	//---------------------------------------------------------------------------
	public static void CopyFileUsingStream(File sourceFile, File destinationFile) throws IOException {
	    InputStream inputStream = null;
	    OutputStream outputStream = null;
	    try {
	        inputStream = new FileInputStream(sourceFile);
	        outputStream = new FileOutputStream(destinationFile);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = inputStream.read(buffer)) > 0) {
	            outputStream.write(buffer, 0, length);
	        }
	    } finally {
	        inputStream.close();
	        if (outputStream != null) {
	        	outputStream.close();
	        }
	    }
	}
	
	public static void DeleteAllFilesInDirectory(String dir) {
		File dirFile = new File(dir);
		for (File file: dirFile.listFiles()) {
		    if (!file.isDirectory()) { 
		        file.delete();
		    }
		}
	}
}
