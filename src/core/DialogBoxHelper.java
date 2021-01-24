package core;

import javax.swing.JOptionPane;

public class DialogBoxHelper {
	
	//---------------------------------------------------------------------------
	// WarningDialog
	//---------------------------------------------------------------------------
	public static boolean WarningDialog(String message) {
		ConsoleHelper.PrintMessage("WarningDialog");

		//JOptionPane.showMessageDialog(frame, warning, windowTitle);
		JOptionPane.showMessageDialog(null, message);

		return true;
	}


	//---------------------------------------------------------------------------
	// YesNoDialog
	//---------------------------------------------------------------------------
	public static boolean YesNoDialog(String question, String windowTitle) {
		ConsoleHelper.PrintMessage("YesNoDialog");

		int dialogButton = JOptionPane.YES_NO_OPTION;
		int dialogResult = JOptionPane.showConfirmDialog (null, question, windowTitle, dialogButton);
		if(dialogResult == JOptionPane.YES_OPTION) {
			return true;
		}

		return false;
	}
}
