/**
 * 
 */
package it.unipd.dei.nanocitation.metadata;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.ConnectException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import it.unipd.dei.nanocitation.metadata.types.Person;


/**
 * @author erika
 *
 */
public class AuthorsExtractor
{

	private String				url;
	private ArrayList<Person>	authList;

	/**
	 * @param url
	 */
	public AuthorsExtractor(String url)
	{
		this.url = url;
		authList = new ArrayList<Person>();
	}

	/**
	 * @return
	 */
	public int extrNumberAuthors()
	{
		
		// get authors from url
		// URLReader urlR = new URLReader();
		// get authors from json response
		JSONPubmedAuthQuery jsonPubmed = new JSONPubmedAuthQuery(url.substring(url.lastIndexOf('/') + 1));

		try
		{
			jsonPubmed.authExtractFromPubmedId();

		} catch (JSONException e)
		{
			throw e;
		}
		/*
		 * try { urlR.extr(url); } catch (Exception e) { e.printStackTrace(); }
		 */
		return 0;
	}

	/**
	 * @return the authList
	 */
	public ArrayList<Person> getAuthList()
	{
		return authList;
	}

	private class JSONPubmedAuthQuery
	{

		private String pubmedId;

		public JSONPubmedAuthQuery(String pubmedId)
		{
			this.pubmedId = pubmedId;
		}

		public void authExtractFromPubmedId()
		{
			if(pubmedId.equals(""))
				return;

			// example
			// https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi?db=pubmed&id=18562290&retmode=json
			String		url		= "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi?db=pubmed&id=" + pubmedId
					+ "&retmode=json";

			JSONObject	jsonObj	= null;

			try
			{
				jsonObj = readJsonFromUrl(url);

				JSONArray authsJson = jsonObj.getJSONObject("result").getJSONObject(pubmedId).getJSONArray("authors");
				if (authsJson.length() == 0)
					return;
				for (int k = 0; k < authsJson.length(); ++k)
				{
					JSONObject	temp	= authsJson.getJSONObject(k);

					String		namesur	= temp.getString("name");
					int			last	= namesur.lastIndexOf(" ");
					if (last == -1)
						continue;

					String	surname	= namesur.substring(0, last);
					String	name	= namesur.substring(namesur.lastIndexOf(" ") + 1);

					Person	a		= new Person();
					
					if(name.length() <= 2)
						a.setInitialNameOnly();
					a.setName(name);
					a.setSurname(surname);
					a.setMain(k == 0);
					authList.add(a);
				}

			} catch (JSONException e)
			{
				e.printStackTrace();
				System.err.println(url + "\n" + jsonObj.toString(3));
				throw e;
			} catch (IOException e)
			{
				e.printStackTrace();
			} catch (InterruptedException e)
			{

				e.printStackTrace();
			}

		}

		public JSONObject readJsonFromUrl(String url) throws IOException, JSONException, InterruptedException
		{

			try
			{
				InputStream		is	= new URL(url).openStream();

				BufferedReader	rd	= new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

				StringBuilder	sb	= new StringBuilder();
				int				cp;
				while ((cp = rd.read()) != -1)
				{
					sb.append((char) cp);
				}
				String		jsonText	= sb.toString();

				JSONObject	json		= new JSONObject(jsonText);
				is.close();
				return json;
			} catch (ConnectException e)
			{
				System.err.println("URL error");
				e.printStackTrace();
				Thread.sleep(2000);
				return readJsonFromUrl(url);
			}
		}

	}

	@SuppressWarnings("unused")
	private class URLReader
	{

		// fatto solo per pubmed (da fare switch eventualmente per altri)
		public void extr(String url) throws Exception
		{
			String	inLine;
			String	pubMedPage	= "";
			URL		ncbi		= new URL(
					"https://www.ncbi.nlm.nih.gov/pubmed/" + url.substring(url.lastIndexOf("/") + 1));
			System.out.println("\n\nhttps://www.ncbi.nlm.nih.gov/pubmed/" + url.substring(url.lastIndexOf("/") + 1));

			BufferedReader in = new BufferedReader(new InputStreamReader(ncbi.openStream()));

			while ((inLine = in.readLine()) != null)
				pubMedPage += inLine;
			in.close();

			int firstIndex = pubMedPage.indexOf("<div class=\"auths\">");
			if (firstIndex == -1)
				return;
			int						lastIndex	= pubMedPage.indexOf("</div>", firstIndex);
			
			String					authPortion	= pubMedPage.substring(firstIndex, lastIndex + 6);
			
			DocumentBuilderFactory	factory		= DocumentBuilderFactory.newInstance();
			DocumentBuilder			builder		= factory.newDocumentBuilder();
			Document				document	= builder.parse(new InputSource(new StringReader(authPortion)));
			NodeList				sourceList	= document.getElementsByTagName("a");

			for (int k = 0; k < sourceList.getLength(); ++k)
			{
				if (sourceList.item(k).getNodeType() == Node.ELEMENT_NODE)
				{
					Element	el		= (Element) sourceList.item(k);
					String	temp	= el.getFirstChild().getNodeValue().trim();
					String	surname	= temp.substring(temp.lastIndexOf(" ") + 1);
					String	name	= temp.substring(0, temp.lastIndexOf(" "));

					Person	a		= new Person();
					a.setName(name);
					a.setSurname(surname);
					a.setMain(k == 0 ? true : false);
					System.out.println(a.toString());
					authList.add(a);

				}
			}
		}

	}

}
