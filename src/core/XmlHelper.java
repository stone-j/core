package core;

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

import processing.data.XML;

public class XmlHelper {

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

	static public BufferedReader createReader(InputStream input) {
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
	public String GetAbsoluteFilePathStringFromXml(String nodePath, XML[] xml) {
		ConsoleHelper.PrintMessage("GetAbsoluteFilePathStringFromXml");

		String myString = GetDataFromXml(nodePath, xml);

		if (!myString.contains(":")) {
			// myString = sketchPath("") + myString;
			myString = System.getProperty("user.dir") + myString;
		}

		ConsoleHelper.PrintMessage("node = " + myString);
		return myString;
	}

	// ---------------------------------------------------------------------------
	// GetDataFromXml
	// ---------------------------------------------------------------------------
	public static String GetDataFromXml(String nodePath, XML[] xml) {
		ConsoleHelper.PrintMessage("GetDataFromXml: " + nodePath);

		// ConsoleHelper.PrintMessage("XML dump = " + xml[0].toString());

		String[] nodeName = nodePath.split("\\.");

		// drill into the xml nodes (replacing "node" with the next node down on each
		// iteration) until we reach the final node
		for (int i = 0; i < nodeName.length; i++) {
			ConsoleHelper.PrintMessage("Traversing node " + nodeName[i]);
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
		ConsoleHelper.PrintMessage("GetXMLFromFile");

		ConsoleHelper.PrintMessage("xmlFilePath = " + xmlFilePath);
		ConsoleHelper.PrintMessage("fallbackXmlFilePath = " + fallbackXmlFilePath);

		File xmlFile = FileHelper.GetFileFromAbsoluteOrRelativeFilePath(xmlFilePath);
		XML[] xml = new XML[1];
		
		//if xmlFilePath whiffs, look to fallbackXmlFilePath
		if (!xmlFile.exists() || xmlFile.isDirectory()) {
			if (!fallbackXmlFilePath.equals("")) {
				ConsoleHelper.PrintMessage("No valid XML file found for file: " + xmlFilePath);
				ConsoleHelper.PrintMessage("Looking for fallbackXmlFilePath ...");
				return GetXMLFromFile(fallbackXmlFilePath, "");
			} else {
				ConsoleHelper.PrintMessage("No valid XML file found for file: " + xmlFilePath);
			}
		} else {
			ConsoleHelper.PrintMessage("Found a valid file!");
			xml[0] = loadXML(xmlFile.toString(), null);
		}

		return xml;
	}

	// ---------------------------------------------------------------------------
	// GetBooleanFromXml
	// ---------------------------------------------------------------------------
	public boolean GetBooleanFromXml(String nodePath, XML[] xml) {
		ConsoleHelper.PrintMessage("GetBooleanFromXml");

		return (Integer.parseInt(GetDataFromXml(nodePath, xml)) == 0 ? false : true);
	}

	// ---------------------------------------------------------------------------
	// GetIntFromXml
	// ---------------------------------------------------------------------------
	public int GetIntFromXml(String nodePath, XML[] xml) {
		ConsoleHelper.PrintMessage("GetIntFromXml");

		return Integer.parseInt(GetDataFromXml(nodePath, xml));
	}

	// ---------------------------------------------------------------------------
	// GetFloatFromXml
	// ---------------------------------------------------------------------------
	public float GetFloatFromXml(String nodePath, XML[] xml) {
		ConsoleHelper.PrintMessage("GetFloatFromXml");

		return Float.parseFloat(GetDataFromXml(nodePath, xml));
	}

	// ---------------------------------------------------------------------------
	// AlterXML (Overload)
	// SEE: https://www.mkyong.com/java/how-to-modify-xml-file-in-java-dom-parser/
	// ---------------------------------------------------------------------------
	public void AlterXML(String nodeName, String nodeValue, String xmlFilePath) {
		ConsoleHelper.PrintMessage("AlterXML (Overload)");

		String[][] myXmlData = new String[1][3];
		myXmlData[0] = new String[] { nodeName, nodeValue, "0" };

		AlterXML(myXmlData, xmlFilePath);
	}

	// ---------------------------------------------------------------------------
	// AlterXML
	// SEE: https://www.mkyong.com/java/how-to-modify-xml-file-in-java-dom-parser/
	// ---------------------------------------------------------------------------
	public void AlterXML(String[][] xmlData, String xmlFilePath) {
		ConsoleHelper.PrintMessage("AlterXML");

		try {
			File xmlFile;

			// the idea here is an absolute path will contain a colon, and a relative path
			// will not.
			if (xmlFilePath.contains(":")) {
				xmlFile = new File(xmlFilePath);
			} else {
				xmlFile = new File(System.getProperty("user.dir"), xmlFilePath);
			}

			String filepath = xmlFile.getAbsolutePath();

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);

			// for every xml tag/value we're trying to update
			for (int i = 0; i < xmlData.length; i++) {
				String[] nodePath = xmlData[i][0].split("\\.");
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
