package core.swingComponent;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;

import core.logging.ConsoleHelper;

public class LookAndFeelHelper {
	
	public ConsoleHelper consoleHelper = new ConsoleHelper();
	
	public void setLookAndFeel(String lookAndFeelName) {
	
		listAllLookAndFeels();
		
		try {
	        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			    consoleHelper.PrintMessage(info.getName());
			}
			
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				consoleHelper.PrintMessage(info.getName());
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
	
	
	public void listAllLookAndFeels() {		
        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		    consoleHelper.PrintMessage(info.getName());
		}
	}
}
