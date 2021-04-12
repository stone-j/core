package core.helper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import core.logging.ConsoleHelper;
import processing.data.XML;

public class XmlHelper {
	
	public ConsoleHelper consoleHelper;
	boolean useAppData;
	String appDataFolderName;

	
//	public XmlHelper() {
//		consoleHelper = new ConsoleHelper();
//	}
	
	public XmlHelper(boolean myUseAppData, String myAppDataFolderName) {
		consoleHelper = new ConsoleHelper();
		this.useAppData = myUseAppData;
		this.appDataFolderName = myAppDataFolderName;		
	}
		
	
	public XML loadXML(String filename, String options) {
		try {
			BufferedReader reader = createReader(filename);
			if (reader != null) {
				return new XML(reader, options);
			}
			return null;

			// can't use catch-all exception, since it might catch the
			// RuntimeException about the incorrect case sensitivity
		} catch (IOException e) {
			throw new RuntimeException(e);

		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);

		} catch (SAXException e) {
			throw new RuntimeException(e);
		}
	}

	public BufferedReader createReader(InputStream input) {
		InputStreamReader isr = new InputStreamReader(input, StandardCharsets.UTF_8);

		BufferedReader reader = new BufferedReader(isr);
		return reader;
	}

	public BufferedReader createReader(String filename) {
		InputStream is = createInput(filename);
		return createReader(is);
	}

	public InputStream createInput(String filename) {
		InputStream input = null;
		try {
			input = new FileInputStream(filename);
		} catch (IOException e) {
		}
		return new BufferedInputStream(input);
	}

	public InputStream createInputRaw(String filename) {
		try {
			return new FileInputStream(filename);
		} catch (IOException e1) {
		}

		return null;
	}

		
	// ---------------------------------------------------------------------------
	// GetAbsoluteFilePathStringFromXml
	// ---------------------------------------------------------------------------
	//added this underload (opposite of overload) for backward compatibility of
	// function calls before "useAppData" was an option
//	public String GetAbsoluteFilePathStringFromXml(String nodePath, XML[] xml) {
//		return GetAbsoluteFilePathStringFromXml(nodePath, xml, false, "");
//	}
	
	
	// ---------------------------------------------------------------------------
	// GetAbsoluteFilePathStringFromXml
	// ---------------------------------------------------------------------------
	public String GetAbsoluteFilePathStringFromXml(String nodePath, XML[] xml, boolean useAppData, String appDataFolderName) {
		consoleHelper.PrintMessage("GetAbsoluteFilePathStringFromXml");

		String myString = GetDataFromXml(nodePath, xml);

		if (!myString.contains(":")) {
			// myString = sketchPath("") + myString;
			if (useAppData) {
				myString = System.getenv("APPDATA") + File.separator + appDataFolderName +  myString;
			} else {
				myString = System.getProperty("user.dir") + myString;
			}
		}

		consoleHelper.PrintMessage("node = " + myString);
		return myString;
	}

	// ---------------------------------------------------------------------------
	// GetDataFromXml
	// ---------------------------------------------------------------------------
	public String GetDataFromXml(String nodePath, XML[] xml) {
		consoleHelper.PrintMessage("GetDataFromXml: " + nodePath);

		// ConsoleHelper.PrintMessage("XML dump = " + xml[0].toString());

		//String[] nodeName = nodePath.split("\\.");
		String[] nodeName = nodePath.split(File.separator + ".");

		// drill into the xml nodes (replacing "node" with the next node down on each
		// iteration) until we reach the final node
		for (int i = 0; i < nodeName.length; i++) {
			consoleHelper.PrintMessage("Traversing node " + nodeName[i]);
			xml = xml[0].getChildren(nodeName[i]);
		}

		return xml[0].getContent();
	}

	// ---------------------------------------------------------------------------
	// GetXMLFromFile
	// ---------------------------------------------------------------------------
	public XML[] GetXMLFromFile(String xmlFilePath) {
		return GetXMLFromFile(xmlFilePath, "");
	}

	// ---------------------------------------------------------------------------
	// GetXMLFromFile
	// ---------------------------------------------------------------------------
	public XML[] GetXMLFromFile(String xmlFilePath, String fallbackXmlFilePath) {
		
		FileHelper fileHelper = new FileHelper(useAppData, appDataFolderName);
		
		consoleHelper.PrintMessage("GetXMLFromFile");

		consoleHelper.PrintMessage("xmlFilePath = " + xmlFilePath);
		consoleHelper.PrintMessage("fallbackXmlFilePath = " + fallbackXmlFilePath);

		File xmlFile = fileHelper.GetFileFromAbsoluteOrRelativeFilePath(xmlFilePath, useAppData, appDataFolderName);
		XML[] xml = new XML[1];
		
		//if xmlFilePath whiffs, look to fallbackXmlFilePath
		if (!xmlFile.exists() || xmlFile.isDirectory()) {
			if (!fallbackXmlFilePath.equals("")) {
				consoleHelper.PrintMessage("No valid XML file found for file: " + xmlFilePath);
				consoleHelper.PrintMessage("Looking for fallbackXmlFilePath ...");
				return GetXMLFromFile(fallbackXmlFilePath, "");
			} else {
				consoleHelper.PrintMessage("No valid XML file found for file: " + xmlFilePath);
			}
		} else {
			consoleHelper.PrintMessage("Found a valid file!");
			xml[0] = loadXML(xmlFile.toString(), null);
		}

		return xml;
	}

	// ---------------------------------------------------------------------------
	// GetBooleanFromXml
	// ---------------------------------------------------------------------------
	public boolean GetBooleanFromXml(String nodePath, XML[] xml) {
		consoleHelper.PrintMessage("GetBooleanFromXml");

		return (Integer.parseInt(GetDataFromXml(nodePath, xml)) == 0 ? false : true);
	}

	// ---------------------------------------------------------------------------
	// GetIntFromXml
	// ---------------------------------------------------------------------------
	public int GetIntFromXml(String nodePath, XML[] xml) {
		consoleHelper.PrintMessage("GetIntFromXml");

		return Integer.parseInt(GetDataFromXml(nodePath, xml));
	}

	// ---------------------------------------------------------------------------
	// GetFloatFromXml
	// ---------------------------------------------------------------------------
	public float GetFloatFromXml(String nodePath, XML[] xml) {
		consoleHelper.PrintMessage("GetFloatFromXml");

		return Float.parseFloat(GetDataFromXml(nodePath, xml));
	}

	// ---------------------------------------------------------------------------
	// AlterXML (Overload)
	// SEE: https://www.mkyong.com/java/how-to-modify-xml-file-in-java-dom-parser/
	// ---------------------------------------------------------------------------
	public void AlterXML(String nodeName, String nodeValue, String xmlFilePath, boolean useAppData, String appDataFolderName) {
		consoleHelper.PrintMessage("AlterXML (Overload)");

		String[][] myXmlData = new String[1][3];
		myXmlData[0] = new String[] { nodeName, nodeValue, "0" };

		AlterXML(myXmlData, xmlFilePath, useAppData, appDataFolderName);
	}

	// ---------------------------------------------------------------------------
	// AlterXML
	// SEE: https://www.mkyong.com/java/how-to-modify-xml-file-in-java-dom-parser/
	// ---------------------------------------------------------------------------
	public void AlterXML(String[][] xmlData, String xmlFilePath, boolean useAppData, String appDataFolderName) {
		consoleHelper.PrintMessage("AlterXML");

		try {
			File xmlFile;

			// the idea here is an absolute path will contain a colon, and a relative path
			// will not.
			if (xmlFilePath.contains(":")) {
				xmlFile = new File(xmlFilePath);
			} else {
				if (useAppData)	{
					xmlFile = new File(System.getenv("APPDATA") + File.separator + appDataFolderName, xmlFilePath);
				} else {
					xmlFile = new File(System.getProperty("user.dir"), xmlFilePath);
				}
			}

			String filepath = xmlFile.getAbsolutePath();

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);

			// for every xml tag/value we're trying to update
			for (int i = 0; i < xmlData.length; i++) {
				//String[] nodePath = xmlData[i][0].split("\\.");
				String[] nodePath = xmlData[i][0].split(File.separator + ".");
				String finalNodeName = nodePath[nodePath.length - 1];

				NodeList nodeList = doc.getElementsByTagName(finalNodeName);

				// for each node in the XML that matches the tag name we're targeting
				for (int j = 0; j < nodeList.getLength(); j++) {
					boolean correctNode = true;
					Node currentNode = nodeList.item(j);
					Node parentNode = currentNode;

					// start with deepest node name and work out from there
					// traverse the node tree from deepest (target) node to top ancestor
					for (int k = nodePath.length - 2; k >= 0; k--) {
						if (parentNode.getParentNode().getNodeName().equals(nodePath[k])) {
							parentNode = currentNode.getParentNode();
						} else {
							correctNode = false;
						}
					}

					// if the full node tree matches our targeted XML, then we have a single match.
					// update the value.
					if (correctNode) {
						currentNode.setTextContent(xmlData[i][1]);
					}
				}
			}

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filepath));
			transformer.transform(source, result);

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (SAXException sae) {
			sae.printStackTrace();
		}
	}
}
