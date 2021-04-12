package core.swingComponent;

import java.awt.Dimension;

import javax.swing.JComboBox;

public class JComboBoxMaker {
	
	static final int defaultWidth = 280;
	static final int defaultHeight = 26;
	
	public static JComboBox<String> makeJComboBox(String[] labels) {
		return makeJComboBox(labels, defaultWidth, defaultHeight);
	}
	
	public static JComboBox<String> makeJComboBox(String[] labels, int width, int height) {
		
		JComboBox<String> jComboBox = new JComboBox<String>(labels);
		
		int maxStringLength = 0;
		
		//find the longest string in the labels collection,
		//and set the width of the box according to its length
		for (String label : labels) {
			int stringLength = label.length();
			if (stringLength > maxStringLength) {
				maxStringLength = stringLength;
				jComboBox.setPrototypeDisplayValue(label);
			}
		}
		
		jComboBox.setMaximumSize(new Dimension(width, height));
		jComboBox.setMinimumSize(new Dimension(width, height));
		jComboBox.setPreferredSize(new Dimension(width, height));
		jComboBox.setBorder(new BorderMaker(BorderMaker.NONE, 0, 0));
		
		return jComboBox;
	}
}
