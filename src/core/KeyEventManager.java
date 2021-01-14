package core;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

public class KeyEventManager {
	
	//default constructor
	public KeyEventManager() {
		init();
	}
	
	public void KeyCodeAction(int keyCode) {
		ConsoleHelper.PrintMessage(keyCode);
	}
	
	private void init() {

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
	
	        @Override
	        public boolean dispatchKeyEvent(KeyEvent ke) {
	            switch (ke.getID()) {
	                case KeyEvent.KEY_PRESSED:
	                	KeyCodeAction(ke.getKeyCode());
	                    break;
	            }
	
	            return false;
	    	}
		});
	}
}
