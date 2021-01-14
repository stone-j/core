package core;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConsoleHelper {

	public static enum MessageLevel {
		NONE,
		CRITICAL,
		DEBUG,
		MESSAGE //MESSAGE is a special type that will always be shown if MessageLevel is set to MESSAGE
	}

	//Change this value to NONE when going to production. It will disable all println statements.
	private static MessageLevel systemMessageLevel = MessageLevel.DEBUG; //NONE, CRITICAL, DEBUG

	
	//---------------------------------------------------------------------------
	// PrintMessage
	//---------------------------------------------------------------------------
	synchronized public static void PrintMessage(String message, MessageLevel myMessageLevel) {
		if (myMessageLevel.ordinal() <= systemMessageLevel.ordinal() || myMessageLevel.ordinal() == MessageLevel.MESSAGE.ordinal()) {
			String timeStamp = new SimpleDateFormat("yyyy-MM-dd  KK:mm.ss[.SSS]").format(new Date());
			System.out.println(timeStamp + " " + message);
		}
	}

	//---------------------------------------------------------------------------
	// PrintMessage
	//---------------------------------------------------------------------------
	synchronized public static void PrintMessage (String message) {
		PrintMessage(message, MessageLevel.DEBUG);
	}

	//---------------------------------------------------------------------------
	// PrintMessage
	//---------------------------------------------------------------------------
	synchronized public static void PrintMessage (int message) {
		PrintMessage(Integer.toString(message), MessageLevel.DEBUG);
	}


	//---------------------------------------------------------------------------
	// printStackTrace
	//---------------------------------------------------------------------------
	public static void printStackTrace(Throwable t) {
		t.printStackTrace();
	}
}
