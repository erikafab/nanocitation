package it.unipd.dei.nanocitation.metadata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.openrdf.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import it.unipd.dei.nanocitation.util.AssertionInfo;
import it.unipd.dei.nanocitation.util.Sio;
import it.unipd.dei.nanocitation.util.TsvParser;
import it.unipd.dei.nanocitation.metadata.types.Assertion;
import it.unipd.dei.nanocitation.metadata.types.DescriptionTopic;
import it.unipd.dei.nanocitation.metadata.types.MetadataContainer;;

public class AssertionManager
{
	
	private Sio sio;
	private static final Logger LOGGER = LoggerFactory.getLogger(AssertionInfo.class);
	
	public AssertionManager(Sio sio)
	{
		this.sio = sio;
	}
	
	public ArrayList<Assertion> getAssertions(Set<Statement> ass)
	{
		ArrayList<Assertion> ret = new ArrayList<>();
		Assertion a;
		
		for (Statement u : ass)
		{
			a = getOneAssertion(u);
			ret.add(a);
			
		}
		return ret;
	}
	
	private Assertion getOneAssertion(Statement u)
	{
		Assertion ret = new Assertion();
		
		// LOGGER.info("------> Assertion = ");
		// LOGGER.info("Subject " + getValue(u.getSubject().toString()));
		// LOGGER.info("Predicate " + getValue(u.getPredicate().toString()));
		// LOGGER.info("Object " + getValue(u.getObject().toString()));
		
		setValueAndURI(ret, u, 1);
		setValueAndURI(ret, u, 2);
		setValueAndURI(ret, u, 3);
		
		return ret;
	}
	
	private void setValueAndURI(Assertion ret, Statement u, int type)
	{
		switch (type)
		{
			case 1: // subject
				ret.setSubject(getValue(u.getSubject().toString()));
				try
				{
					ret.setSubjectURI(URI.create(u.getSubject().toString()));
				} catch (IllegalArgumentException e)
				{
				}
				break;
			case 2: // predicate
				ret.setPredicate(getValue(u.getPredicate().toString()));
				try
				{
					ret.setPredicateURI(URI.create(u.getPredicate().toString()));
				} catch (IllegalArgumentException e)
				{
				}
				break;
			case 3: // object
				ret.setObject(getValue(u.getObject().toString()));
				try
				{
					ret.setObjectURI(URI.create(u.getObject().toString()));
				} catch (IllegalArgumentException e)
				{
				}
				break;
			
		}
		
	}
	
	private String getValue(String u)
	{
		String ret = "";
		
		if (u.contains("/resource/SIO_"))
			ret = sio.sioToReadable(u);
		else if (u.contains("linkedlifedata.com"))
			ret = linkedlifedataToReadable(u);
		else if (u.contains("ncicb.nci"))
			ret = ncitIdToReadable(u.substring(u.lastIndexOf('#') + 1));
		else if (u.contains("proteinatlas.org"))
			ret = proteinatlasToReadable(u);
		else if (u.contains("^^<http://www.w3.org/2001/XMLSchema#string>"))
			ret = u.substring(1, u.indexOf("^^") - 1);
		else if (u.contains("www.w3.org") && u.contains("/rdf-schema#"))
			ret = rdfSyntaxToReadable(u.substring(u.lastIndexOf('#') + 1));
		else if (u.contains("www.w3.org") && u.contains("rdf-syntax-ns#"))
			ret = rdfSyntaxToReadable(u.substring(u.lastIndexOf('#') + 1));
		
		else if (u.contains("rdf.disgenet.org/resource/gda/"))
			ret = "GDA id " + u.substring(u.lastIndexOf('/') + 1);
		else if (u.contains("rdf.disgenet.org/gene-disease-association"))
			ret = "GDA id " + u.substring(u.lastIndexOf('#') + 1);
		else if (u.contains("identifiers.org/ncbigene"))
			ret = identifiersGeneToReadable(u);
		else if (u.contains("/purl.obolibrary.org/obo/caloha.obo#"))
			ret = obocalohaToReadable(rdfSyntaxToReadable(u));
		else if (u.contains("purl.obolibrary.org/obo/"))
			ret = oboToReadable(u);
		else if (u.contains("http://ontology.neuinfo.org/") && u.contains("#"))
			ret = u.substring(u.lastIndexOf('#') + 1);
		else
			ret = u;
		
		return ret;
	}
	
	private String oboToReadable(String u)
	{
		String url = "http://www.ontobee.org/ontology/RO?iri=" + u;
		
		File obofile = getFileFromUrl(url, "");
		
		if (obofile != null)
		{
			try (BufferedReader br = new BufferedReader(new FileReader(obofile)))
			{
				String s = "";
				String line;
				while ((line = br.readLine()) != null)
				{
					s += line;
				}
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader(s));
				Document doc = builder.parse(is);
				
				XPathFactory xpathfactory = XPathFactory.newInstance();
				XPath xpath = xpathfactory.newXPath();
				
				XPathExpression expr = xpath.compile("//ObjectProperty[@about='" + u + "']/label/text()");
				Object result = expr.evaluate(doc, XPathConstants.NODESET);
				NodeList nodes = (NodeList) result;
				
				return nodes.item(0).getNodeValue();
				
			} catch (FileNotFoundException e)
			{
				return "";
			} catch (IOException e)
			{
				return "";
			} catch (ParserConfigurationException e)
			{
				return "";
			} catch (SAXException e)
			{
				return "";
			} catch (XPathExpressionException e)
			{
				return "";
			}
		}
		
		return "";
	}
	
	private String obocalohaToReadable(String term)
	{
		JSONObject data = null;
		String ret = "";
		try
		{
			HttpURLConnection conn = (HttpURLConnection) (new URL("https://api.nextprot.org/term/" + term))
					.openConnection();
			
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			
			conn.setRequestMethod("GET");
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String out = "";
			for (String line; (line = reader.readLine()) != null;)
			{
				out = out + line;
			}
			reader.close();
			
			data = new JSONObject(out);
			
			if (!data.has("cvTerm"))
				return "";
			
			data = data.getJSONObject("cvTerm");
			
			if (data.has("name"))
				ret = data.getString("name");
			
			/*
			 * if(data.has("description") ret =
			 * data.getString("description").toString();
			 */
			
		} catch (ProtocolException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return ret;
	}
	
	/**
	 * @param ncitId
	 * @return
	 */
	private String ncitIdToReadable(String ncitId)
	{
		String ret = null;
		switch (ncitId)
		{
			case "C16612":
				ret = "Gene";
				break;
			case "C7057":
				ret = "Disease";
				break;
			case "C18279":
				ret = "SNP";
				break;
			
			case "C47902":
				ret = "PubmedArticle";
				break;
			case "C25338":
				ret = "Score";
				break;
			case "C43568":
				ret = "HGNC Gene Symbol";
				break;
			case "C17021":
				ret = "Protein";
				break;
			case "C20633":
				ret = "Pathway";
				break;
			default:
				ret = "";
				
		}
		return ret;
	}
	
	/**
	 * @param synt
	 * @return
	 */
	private String rdfSyntaxToReadable(String synt)
	{
		return synt.substring(synt.lastIndexOf('#') + 1);
	}
	
	private String getLastPartOfUrl(String url)
	{
		return url.substring(url.lastIndexOf('/') + 1);
	}
	
	/**
	 * @param url
	 * @return
	 */
	private String llIdToReadable(String url)
	{
		String ret = null;
		String urlJsonGet = url + ".json";
		
		try (InputStream in = new URL(urlJsonGet).openStream())
		{
			// String s = "assertion/";
			// Files.createDirectories(Paths.get(OUTDIRPATH + s));
			// Files.copy(in, Paths.get(OUTDIRPATH + s +
			// urlJsonGet.substring(urlJsonGet.lastIndexOf('/') + 1)),
			// StandardCopyOption.REPLACE_EXISTING);
			// ret = in.toString();
			ret = IOUtils.toString(in, StandardCharsets.UTF_8);
			// Files.delete(Paths.get(OUTDIRPATH + s +
			// urlJsonGet.substring(urlJsonGet.lastIndexOf('/') + 1)));
		} catch (IOException e)
		{
			LOGGER.error("Error in llIdToReadable\n" + e.getClass().getName() + ": " + e.getMessage());
		}
		return ret;
	}
	
	private String linkedlifedataToReadable(String url)
	{
		String ret = "";
		
		String json = llIdToReadable(url);
		if (json != null)
			ret = readJsonLinkedLifeDataValue(json, url);
		return ret;
	}
	
	private String proteinatlasToReadable(String u)
	{
		String ret = "";
		TsvParser tsvpars = new TsvParser();
		//
		
		File proteinInfoTsv = getFileFromUrl(u, ".tsv");
		
		if (proteinInfoTsv != null)
		{
			Map<String, String> pro = tsvpars.parseTsvFile(proteinInfoTsv);
			
			String gen = pro.get("Gene description");
			
			
			ret = gen != null ? gen : "";
			
		}
		
		if (ret == "")
			ret = getLastPartOfUrl(u);
		return ret;
	}
	
	/**
	 * @param url
	 * @return
	 */
	private String identifiersGeneToReadable(String url)
	{
		String ret = null;
		URL obj;
		try
		{
			obj = new URL(url);
			
			HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
			conn.setReadTimeout(5000);
			conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
			conn.addRequestProperty("User-Agent", "Mozilla");
			conn.addRequestProperty("Referer", "google.com");
			
			boolean redirect = false;
			
			int status = conn.getResponseCode();
			if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM
					|| status == HttpURLConnection.HTTP_SEE_OTHER)
				redirect = true;
			
			// identifiers.org redirection
			if (redirect)
			{
				url = conn.getHeaderField("Location"); // +
														// (url.contains("ncbi")
														// ?
														// "?report=tabular&format=text"
														// : "");
				
				if (url.contains("gene"))
				{
					
					org.jsoup.nodes.Document doc;
					
					doc = Jsoup.connect(url).get();
					String title = doc.title().substring(0, doc.title().lastIndexOf('[') - 1);
					
					return title;
					// String[] parts = title.split("\\[");
					// return parts[0];
					
				}
				
			}
			
		} catch (IOException e)
		{
			LOGGER.error("Error in identifiersGeneToReadable\n" + e.getClass().getName() + ": " + e.getMessage());
		}
		return ret;
	}
	
	private String readJsonLinkedLifeDataValue(String json, String url)
	{
		String ret = null;
		JSONObject rootObject;
		try
		{
			rootObject = new JSONObject(json);
			
			JSONArray rows = rootObject.getJSONObject(url)
					.getJSONArray("http://www.w3.org/2004/02/skos/core#exactMatch");
			JSONObject element = rows.getJSONObject(0);
			String name = element.getString("value");
			name = name.substring(name.lastIndexOf("mesh/") + 5);
			name = URLDecoder.decode(name, "UTF-8");
			return name;
			
		} catch (JSONException e)
		{
			
			org.jsoup.nodes.Document doc;
			try
			{
				doc = Jsoup.connect(url).get();
				ret = doc.title().substring(Integer.max(doc.title().indexOf("CONCEPT ") + 8, 0));
				ret = URLDecoder.decode(ret, "UTF-8");
				return ret;
			} catch (IOException ex)
			{
				LOGGER.error(
						"Error in readJsonLinkedLifeDataValue\n" + ex.getClass().getName() + ": " + ex.getMessage());
			}
			
		} catch (UnsupportedEncodingException e)
		{
			LOGGER.error("Error in readJsonLinkedLifeDataValue\n" + e.getClass().getName() + ": " + e.getMessage());
		}
		
		return ret;
		
	}
	
	@SuppressWarnings("resource")
	private File getFileFromUrl(String u, String extension)
	{
		
		InputStream is = null;
		FileOutputStream fos = null;
		
		String tempDir = System.getProperty("java.io.tmpdir");
		String outputPath = tempDir + "/" + u.substring(u.lastIndexOf('/') + 1);
		
		try
		{
			URL url = new URL(u + extension);
			URLConnection urlConn = url.openConnection();
			
			is = urlConn.getInputStream();
			fos = new FileOutputStream(outputPath);
			
			byte[] buffer = new byte[4096];
			int length;
			
			while ((length = is.read(buffer)) > 0)
			{
				fos.write(buffer, 0, length);
			}
			return new File(outputPath);
		} catch (IOException e)
		{
		}
		
		return null;
		
	}
	
	protected ArrayList<DescriptionTopic> getAssertionContentFormatted(MetadataContainer meta, String npId,
			ArrayList<Assertion> asser)
	{
		ArrayList<DescriptionTopic> dt = new ArrayList<>();
		DescriptionTopic tmp;
		if (npId.contains("disgenet"))
		{
			if (asser.size() == 5)
			{
				tmp = stringFrom5Assertions(asser);
				dt.add(tmp);
			}
			// GDA associations
			else if (asser.size() == 3)
			{
				tmp = stringFrom3Assertions(asser);
				dt.add(tmp);
			}
		}
		else if (npId.contains("proteinatlas"))
		{
			if (meta.getAssertionType().equals("IHCEvidence"))
			{
				tmp = stringFromIHCAssert(asser);
				dt.add(tmp);
			}
		}
		
		return dt;
	}
	
	private DescriptionTopic stringFromIHCAssert(ArrayList<Assertion> asser)
	{
		DescriptionTopic dt = new DescriptionTopic();
		dt.setSubject("IHCEvidence");
		ArrayList<String> assHumRe = new ArrayList<String>();
		
		String expression = "";
		String protein = "";
		String tissue = "";
		String tissueName = "";
		for (Assertion a : asser)
		{
			if (a.getPredicate() != null && a.getPredicate().contains("nlx"))
			{
				expression = a.getObject();
				protein = a.getSubject();
				
				for (Assertion as : asser)
				{
					if (as.getPredicate() != null && as.getPredicate().contains("occurs in"))
					{
						tissue = as.getObject();
					}
					else if (!tissue.equals("") && as.getPredicate().contains("type") && as.getObject() != null)
					{
						tissueName = as.getObject();
						break;
					}
				}
			}
			
		}
		assHumRe.add(protein + " with "+ expression +" expression in " + tissueName + " (" + tissue + ")");
		dt.setAssertion(assHumRe);
		return dt;
	}
	
	private DescriptionTopic stringFrom5Assertions(ArrayList<Assertion> asser)
	{
		DescriptionTopic dt = new DescriptionTopic();
		
		for (Assertion a : asser)
			if (a.getSubject().contains("GDA id") && a.getPredicate().contains("type"))
			{
				dt.setSubject(a.getObject());
				String gene = "";
				String disease = "";
				
				for (Assertion as : asser)
				{
					if (as.getObject().equals("Gene") && as.getPredicate().contains("type"))
						gene = as.getSubject();
					else if (as.getObject().equals("Disease") && as.getPredicate().contains("type"))
						disease = as.getSubject();
				}
				
				ArrayList<String> assHumRe = new ArrayList<String>();
				assHumRe.add(gene + " - " + disease);
				dt.setAssertion(assHumRe);
				
				return dt;
				
			}
		return dt;
	}
	
	/**
	 * @param asser
	 * @return
	 */
	private DescriptionTopic stringFrom3Assertions(ArrayList<Assertion> asser)
	{
		
		DescriptionTopic dt = new DescriptionTopic();
		
		for (Assertion a : asser)
		{
			if (a.getSubject().contains("GDA id") && a.getPredicate().contains("type"))
			{
				dt.setSubject(a.getObject());
				String gene = "";
				String disease = "";
				String gdaId = a.getSubject();
				
				for (Assertion as : asser)
				{
					if (as.getSubject().equals(gdaId) && as.getPredicate().equals("refers to") && gene.equals(""))
						gene = as.getObject();
					else if (as.getSubject().equals(gdaId) && as.getPredicate().equals("refers to")
							&& disease.equals(""))
						disease = as.getObject();
				}
				
				ArrayList<String> assHumRe = new ArrayList<String>();
				assHumRe.add(gene + " - " + disease);
				dt.setAssertion(assHumRe);
				
				return dt;
			}
		}
		
		return dt;
		
	}
}
