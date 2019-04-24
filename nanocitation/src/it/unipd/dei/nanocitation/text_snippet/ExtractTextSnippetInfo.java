/**
 * 
 */
package it.unipd.dei.nanocitation.text_snippet;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.nanopub.MalformedNanopubException;
import org.nanopub.Nanopub;
import org.nanopub.NanopubImpl;
import org.openrdf.OpenRDFException;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.unipd.dei.nanocitation.metadata.AuthorsExtractor;
import it.unipd.dei.nanocitation.metadata.AuthorsNpExtractor;
import it.unipd.dei.nanocitation.metadata.types.MetadataContainer;
import it.unipd.dei.nanocitation.metadata.types.Person;
import it.unipd.dei.nanocitation.util.Sio;

/**
 * @author erika
 *
 */
public class ExtractTextSnippetInfo
{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExtractTextSnippetInfo.class);
	
	private Sio sio;
	
	public ExtractTextSnippetInfo(Sio sio)
	{
		this.sio = sio;
	}
	
	/**
	 * @param fileTemp
	 * @return
	 */
	public MetadataContainer getInfo(URL file)
	{
		
		if (file == null)
			return null;
		
		MetadataContainer ret = new MetadataContainer();
		
		try
		{
			Nanopub np = new NanopubImpl(file);
			
			printStatements(np.getHead(), "-------------------Head: ");
			printStatements(np.getPubinfo(), "Pub info: ");
			printStatements(np.getProvenance(), "Provenance: ");
			printStatements(np.getAssertion(), "Assertion: ");
			
			// np identificator
			URI npUri = np.getUri();
			
			ret.setIdentifier(npUri.toString());
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			ret.setTimeCreation(
					(np.getCreationTime() != null ? dateFormat.format(np.getCreationTime().getTime()) : ""));
			
			getHead(np, ret);
			
			getPubInfoTS(np, ret);
			
			getProvenenceInfoTS(np, ret);
			
			AuthorsNpExtractor anpe = new AuthorsNpExtractor();
			ArrayList<Person> npaut = anpe.getNpAuthors(np);
			ArrayList<Person> creat = (ArrayList<Person>) anpe.getCreator(np);
			
			ret.setNpauthors(npaut);
			ret.setCreators(creat);
			
		} catch (MalformedNanopubException | OpenRDFException | IOException e)
		{
			LOGGER.warn("Error in getInfo " + e.getClass().getName() + ": " + e.getMessage());
		}
		
		System.out.println("RISULTATO: \n" + ret.toString());
		return ret;
	}
	
	private void getHead(Nanopub np, MetadataContainer ret)
	{
		Set<Statement> head = np.getHead();
		for (Statement u : head)
		{
			String predicate = u.getPredicate().toString();
			String object = u.getObject().toString();
			if (predicate.equals("http://www.w3.org/2000/01/rdf-schema#comment"))
			{
				if (object.toString().contains("/XMLSchema#string"))
					ret.setProvenenceEvidenceComment(
							u.getObject().toString().substring(1, u.getObject().toString().indexOf("^^") - 1));
			}
			
		}
		
	}
	
	/**
	 * @param np
	 * @param ret
	 */
	private void getPubInfoTS(Nanopub np, MetadataContainer ret)
	{
		Set<Statement> pub = np.getPubinfo();
		for (Statement u : pub)
		{
			String predicate = u.getPredicate().toString();
			String object = u.getObject().toString();
			
			if (predicate.contains("http://purl.org/dc/terms/created"))
			{
				String creationTime = "";
				Pattern pattern = Pattern.compile("\\d{4}\\-(0?[1-9]|1[012])\\-\\d{2}");
				Matcher matcher = pattern.matcher(object);
				if (matcher.find())
					creationTime = matcher.group();
				ret.setTimeCreation(creationTime);
			}
			else if (predicate.contains("dc/terms/rightsHolder"))
			{
				String rightHold = "";
				int firstCharacter = getFirstIndexFromStatementURL(u);
				
				if (firstCharacter != -1)
					rightHold = object.substring(firstCharacter);
				ret.setRightHolder(rightHold);
				ret.setRightUrl(object);
				
			}
			else if (predicate.contains("dc/terms/rights"))
			{
				ret.setRightUrl(object);
			}
			else if (predicate.contains("dc/terms/subject"))
			{
				int firstCharacter = getFirstIndexFromStatementURL(u);
				String sub = "";
				
				if (firstCharacter != -1 && object.substring(firstCharacter).contains("SIO_"))
					sub = sio.sioToReadable(object);
				
				ret.setSubject(sub);
				ret.setSubjectUrl(object);
			}
			else if (predicate.contains("provenance"))
			{
				String plat = "";
				int firstCharacter = getFirstIndexFromStatementURL(u);
				if (firstCharacter != -1)
					plat = object.substring(firstCharacter);
				ret.setPlatform(plat);
				ret.setPlatformUrl(object);
			}
			else if (predicate.contains("version"))
			{
				String version = "";
				Pattern pattern = Pattern.compile("\"[\\d.v]*\"");
				Matcher matcher = pattern.matcher(object);
				if (matcher.find())
					version = matcher.group().substring(1, matcher.group().length() - 1);
				
				ret.setVersion(version);
			}
			
		}
		
	}
	
	/**
	 * @param np
	 * @param ret
	 */
	private void getProvenenceInfoTS(Nanopub np, MetadataContainer ret)
	{
		Set<Statement> prov = np.getProvenance();
		
		AuthorsExtractor authEx;
		for (Statement u : prov)
		{
			String predicate = u.getPredicate().toString();
			String object = u.getObject().toString();
			
			// LOGGER.info("ASSPROV___\n" + u.getSubject() + " \n" + predicate +
			// " \n" + object);
			
			if (predicate.equals("http://semanticscience.org/resource/SIO_000772")) // "has
																					// evidence"
			{
				
				ret.setEvidenceId(u.getObject().toString());
				ret.setEvidenceIdUrl(object);
				if (object.contains("pubmed"))
				{
					authEx = new AuthorsExtractor(u.getObject().toString());
					try
					{
						// extract from PubMed
						authEx.extrNumberAuthors();
					} catch (JSONException e)
					{
						System.err.println(ret.getIdentifier() + " evidence id " + ret.getEvidenceId());
					}
					
					ret.setAuthors(authEx.getAuthList());
					
				}
			}
			
			else if (predicate.equals("http://purl.org/ontology/wi/core#evidence"))
			{
				if (u.getObject().toString().contains("/XMLSchema#string"))
					ret.setProvenanceEvidence(
							u.getObject().toString().substring(1, u.getObject().toString().indexOf("^^") - 1));
				else
				{
					int firstCharacter = getFirstIndexFromStatementURL(u);
					
					ret.setProvenanceEvidence(object.substring(firstCharacter));
					ret.setProvenanceEvidenceUrl(object);
				}
				
			}
			else if (predicate.equals("http://www.w3.org/2000/01/rdf-schema#comment"))
			{
				String[] p = object.split("\"");
				if (p.length > 1 && p[1] != null)
					ret.setProvenenceEvidenceComment(p[1]);
			}
			else if (predicate.equals("http://www.w3.org/2000/01/rdf-schema#label"))
			{
				String[] p = object.split("\"");
				if (p.length > 1 && p[1] != null)
					ret.setProvenenceEvidenceLabel(p[1]);
			}
			else if (predicate.contains("rdf-syntax-ns#type"))
			{
				ret.setProvenenceEvidenceTypeUrl(object);
				
				int firstCharacter = getFirstIndexFromStatementURL(u);
				
				ret.setAssertionType(object.substring(firstCharacter));
				
			}
			else if (predicate.equals("http://www.w3.org/ns/prov#wasDerivedFrom"))
			{
				int firstCharacter = getFirstIndexFromStatementURL(u);
				
				ret.setDatabase(object.substring(firstCharacter));
				ret.setDatabaseUrl(object);
				
			}
			
			else if (predicate.equals("http://www.w3.org/ns/prov#wasGeneratedBy"))
			{
				
				int firstCharacter = getFirstIndexFromStatementURL(u);
				
				System.out.println("first char " + firstCharacter);
				
				String provString = (ret.getProvenanceGeneratedBy() != "" ? ret.getProvenanceGeneratedBy() + " " : "");
				if (object.substring(firstCharacter).equals("ECO_0000218"))
					provString += "MANUAL ASSERTION";
				else if (object.substring(firstCharacter).equals("ECO_0000203"))
					provString += "AUTOMATIC ASSERTION";
				else
					provString += spaceAfterCapLetters(object.substring(firstCharacter));
				System.out.println("provString " + provString);
				ret.setProvenanceGeneratedBy(provString);
				ret.setProvenanceGeneratedByUrl(object);
				
			}
			
			else if (predicate.equals("http://purl.org/pav/importedOn"))
			{
				String importedTime = "";
				Pattern pattern = Pattern.compile("\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])\"");
				Matcher matcher = pattern.matcher(object);
				if (matcher.find())
					importedTime = matcher.group().substring(0, matcher.group().length() - 1);
				ret.setDateDb(importedTime);
				
			}
			else if (predicate.equals("http://purl.org/dc/terms/description"))
			{
				String[] p = object.split("\"");
				if (p.length > 1 && p[1] != null)
					ret.setPredicateDescription(p[1]);
				// ret.setPredicateDescription(object);
				
			}
			else if (predicate.equals("http://purl.org/net/provenance/ns#usedData"))
			{
				ret.setUsedDataProv(object);
				if (!object.contains("\""))
					ret.setUsedDataUrlProv(object);
				
			}
		}
		
	}
	
	private String spaceAfterCapLetters(String provString)
	{
		return provString.replaceAll("([A-Z])", " $1");
	}
	
	/**
	 * @param u
	 * @return
	 */
	private static int getFirstIndexFromStatementURL(Statement u)
	{
		return u.getObject().toString().lastIndexOf('/') > u.getObject().toString().lastIndexOf('#')
				? u.getObject().toString().lastIndexOf('/') + 1
				: u.getObject().toString().lastIndexOf('#') + 1;
	}
	
	private void printStatements(Set<Statement> set, String msg)
	{
		System.out.println(msg);
		for (Statement statement : set)
		{
			System.out.println(
					statement.getSubject() + "     " + statement.getPredicate() + "     " + statement.getObject());
			System.out.println("____________");
		}
	}
	
	public String getIdentifier(URL file)
	{
		if (file == null)
			return null;
		
		MetadataContainer ret = new MetadataContainer();
		
		Nanopub np;
		try
		{
			np = new NanopubImpl(file);
			
			URI npUri = np.getUri();
			return npUri.toString();
		} catch (MalformedNanopubException | OpenRDFException | IOException e)
		{
			
			e.printStackTrace();
		}
		return "";
		
	}
}
