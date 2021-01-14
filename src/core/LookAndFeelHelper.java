package core;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;

public class LookAndFeelHelper {
	
	public static void setLookAndFeel(String lookAndFeelName) {
	
		listAllLookAndFeels();
		
		try {
	        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			    ConsoleHelper.PrintMessage(info.getName());
			}
			
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				ConsoleHelper.PrintMessage(info.getName());
				if (lookAndFeelName.equals(info.getName())) {
			        UIManager.setLookAndFeel(info.getClassName());
			        break;
			    }
			}
	    } 
		catch (ClassNotFoundException ex) {}
		catch (InstantiationException ex) {}
		catch (IllegalAccessException ex) {}
		catch (UnsupportedLookAndFeelException ex) {}
	}
	
	
	public static void listAllLookAndFeels() {		
        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		    ConsoleHelper.PrintMessage(info.getName());
		}
	}
}
