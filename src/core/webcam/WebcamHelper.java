package core.webcam;

import java.awt.Dimension;
import java.util.List;

import com.github.sarxos.webcam.Webcam;

import core.logging.ConsoleHelper;
import core.logging.ExceptionLogger;

public class WebcamHelper {
	
	public ConsoleHelper consoleHelper = new ConsoleHelper();
	
	public Webcam getWebcam(String webcamName, int webcamWidth, int webcamHeight, ExceptionLogger exceptionLogger) {
		
		Webcam webcam = null;
		
		List<Webcam> webcams = Webcam.getWebcams();
		for (Webcam w : webcams) {
			consoleHelper.PrintMessage("webcam name = " + w.getName());
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
		
		consoleHelper.PrintMessage("Webcam resolutions: ");
		for (Dimension d : webcam.getViewSizes()) {
			consoleHelper.PrintMessage(d.toString());
		}
		
		return webcam;
	}
}
