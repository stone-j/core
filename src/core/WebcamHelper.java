package core;

import java.awt.Dimension;
import java.util.List;
import java.util.logging.Logger;

import com.github.sarxos.webcam.Webcam;

public class WebcamHelper {
	
	public static Webcam getWebcam(String webcamName, int webcamWidth, int webcamHeight, ExceptionLogger exceptionLogger) {
		
		Webcam webcam = null;
		
		List<Webcam> webcams = Webcam.getWebcams();
		for (Webcam w : webcams) {
			ConsoleHelper.PrintMessage("webcam name = " + w.getName());
			if (w.getName().contains(webcamName)) {
				webcam = w;
			}
		}
		
		if (webcam == null) {
			exceptionLogger.logMessage("No webcams found with the name \"" + webcamName + "\"");
			System.exit(0);
		}
		
		webcam.setCustomViewSizes(new Dimension(webcamWidth, webcamHeight));
		webcam.setViewSize(webcam.getCustomViewSizes()[0]);
		
		ConsoleHelper.PrintMessage("Webcam resolutions: ");
		for (Dimension d : webcam.getViewSizes()) {
			ConsoleHelper.PrintMessage(d.toString());
		}
		
		return webcam;
	}
}