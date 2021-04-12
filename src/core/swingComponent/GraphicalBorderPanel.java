package core.swingComponent;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import core.logging.ConsoleHelper;

public class GraphicalBorderPanel extends JPanel {
	
	public final static String FRAME06 = "frame06";
	public final static String PLAIN_PURPLE = "plain_purple";
	
	public ConsoleHelper consoleHelper = new ConsoleHelper();
	
	JPanel centralJPanel;
	String borderFolder;
	
	BufferedImage top_left;
	BufferedImage top;
	BufferedImage top_right;
	BufferedImage right;
	BufferedImage bot_right;
	BufferedImage bot;
	BufferedImage bot_left;
	BufferedImage left;
	
	public boolean useAppData;
	public String appDataFolderName;
	
	private String userFolder;
	
	//constructor
	public GraphicalBorderPanel(String myBorderFolder, boolean myUseAppData, String myAppDataFolderName) {
		super();
		borderFolder = myBorderFolder;
		consoleHelper.PrintMessage("If you invoke the GraphicalBorderPanel constructor with only argument [String myBorderFolder], you must call buildPanel(JPanel myCentralJPanel) later to avoid a null pointer exception.");
		this.useAppData = myUseAppData;
		this.appDataFolderName = myAppDataFolderName;
		if (useAppData) {
			this.userFolder = System.getenv("APPDATA") + File.separator + appDataFolderName;
		} else {
			this.userFolder = System.getProperty("user.dir");
		}
	}
	
	//constructor
	public GraphicalBorderPanel(JPanel myCentralJPanel, String myBorderFolder, boolean myUseAppData, String myAppDataFolderName) {
		super();
		borderFolder = myBorderFolder;
		centralJPanel = myCentralJPanel;
		this.useAppData = myUseAppData;
		this.appDataFolderName = myAppDataFolderName;
		buildPanel();
	}
	
	
	public void buildPanel(JPanel myCentralJPanel) {
		centralJPanel = myCentralJPanel;
		buildPanel();
	}
		
	public void buildPanel() {
		
		this.setLayout(new BorderLayout());
		
		JPanel masterPanel = new JPanel();
		masterPanel.setLayout(new BorderLayout());	
		
		JPanelPaintable panel_top 	= new JPanelPaintable(centralJPanel.getBackground());
		JPanelPaintable panel_bot 	= new JPanelPaintable(centralJPanel.getBackground());
		JPanelPaintable panel_left 	= new JPanelPaintable(centralJPanel.getBackground());		
		JPanelPaintable panel_right = new JPanelPaintable(centralJPanel.getBackground());
		
		top_left 	= null;
		top 		= null;
		top_right 	= null;
		right 		= null;
		bot_right 	= null;
		bot 		= null;
		bot_left 	= null;
		left 		= null;
		try {
			top_left 	= ImageIO.read(new File(userFolder + File.separator + "images" + File.separator + "borders" + File.separator + borderFolder + File.separator + "top_left.png"));
			top 		= ImageIO.read(new File(userFolder + File.separator + "images" + File.separator + "borders" + File.separator + borderFolder + File.separator + "top.png"));
			top_right 	= ImageIO.read(new File(userFolder + File.separator + "images" + File.separator + "borders" + File.separator + borderFolder + File.separator + "top_right.png"));
			right 		= ImageIO.read(new File(userFolder + File.separator + "images" + File.separator + "borders" + File.separator + borderFolder + File.separator + "right.png"));
			bot_right 	= ImageIO.read(new File(userFolder + File.separator + "images" + File.separator + "borders" + File.separator + borderFolder + File.separator + "bot_right.png"));
			bot 		= ImageIO.read(new File(userFolder + File.separator + "images" + File.separator + "borders" + File.separator + borderFolder + File.separator + "bot.png"));
			bot_left 	= ImageIO.read(new File(userFolder + File.separator + "images" + File.separator + "borders" + File.separator + borderFolder + File.separator + "bot_left.png"));
			left 		= ImageIO.read(new File(userFolder + File.separator + "images" + File.separator + "borders" + File.separator + borderFolder + File.separator + "left.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
		
		//have to create these nested single panels to propagate the transparency
		//not sure why, but this hack works to retain transparency
		JPanelPaintable panel_left_child 	= new JPanelPaintable(left, JPanelPaintable.TILED);
		JPanelPaintable panel_right_child 	= new JPanelPaintable(right, JPanelPaintable.TILED);
		
		panel_left	.add(panel_left_child);
		panel_right	.add(panel_right_child);
		
		panel_top.setLayout(new BorderLayout());
		panel_bot.setLayout(new BorderLayout());
		
		JPanelPaintable panel_top_left 		= new JPanelPaintable(top_left	, JPanelPaintable.TILED);
		JPanelPaintable panel_top_middle 	= new JPanelPaintable(top		, JPanelPaintable.TILED);
		JPanelPaintable panel_top_right 	= new JPanelPaintable(top_right	, JPanelPaintable.TILED);
		JPanelPaintable panel_bot_left 		= new JPanelPaintable(bot_left	, JPanelPaintable.TILED);
		JPanelPaintable panel_bot_middle 	= new JPanelPaintable(bot		, JPanelPaintable.TILED);
		JPanelPaintable panel_bot_right 	= new JPanelPaintable(bot_right	, JPanelPaintable.TILED);
		
		panel_top.add(panel_top_left	, BorderLayout.LINE_START);
		panel_top.add(panel_top_middle	, BorderLayout.CENTER);
		panel_top.add(panel_top_right	, BorderLayout.LINE_END);
		panel_bot.add(panel_bot_left	, BorderLayout.LINE_START);
		panel_bot.add(panel_bot_middle	, BorderLayout.CENTER);
		panel_bot.add(panel_bot_right	, BorderLayout.LINE_END);
		
		masterPanel.add(panel_top		, BorderLayout.PAGE_START);
		masterPanel.add(panel_bot		, BorderLayout.PAGE_END);
		masterPanel.add(centralJPanel	, BorderLayout.CENTER);
		masterPanel.add(panel_left		, BorderLayout.LINE_START);
		masterPanel.add(panel_right		, BorderLayout.LINE_END);
				
		this.add(masterPanel);
		//this.pack();
		this.setVisible(true);
	}
}

