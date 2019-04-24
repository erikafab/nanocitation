package it.unipd.dei.nanocitation.util;


import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * 
 * @author erika
 *
 */
public class Sio
{

	private static final Logger	LOGGER	= LoggerFactory.getLogger(Sio.class);
	private NodeList			nodeListDescription;

	public Sio(File file)
	{
		DocumentBuilderFactory	factory		= DocumentBuilderFactory.newInstance();
		Document				document	= null;
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.parse(file);
			this.nodeListDescription = document.getElementsByTagName("rdf:Description");
		} catch (SAXException | IOException | ParserConfigurationException e)
		{
			LOGGER.error("Error in Sio\n" + e.getClass().getName() + ": " + e.getMessage());
		}

	}

	/**
	 * @param sio
	 * @return
	 */
	public String sioToReadable(String sio)
	{
		String ret = null;
		if (nodeListDescription != null)
			for (int k = 0; k < nodeListDescription.getLength(); ++k)
			{
				if (nodeListDescription.item(k).getNodeType() == Node.ELEMENT_NODE)
				{
					Element el = (Element) nodeListDescription.item(k);
					if (el.getAttribute("rdf:about").equals(sio))
					{
						ret = el.getElementsByTagName("rdfs:label").item(0).getTextContent();
					}
				}
			}

		return ret;
	}
}
