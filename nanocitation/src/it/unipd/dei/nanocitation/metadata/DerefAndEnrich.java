package it.unipd.dei.nanocitation.metadata;

import java.io.File;
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

import it.unipd.dei.nanocitation.logger.LoggerCustom;
import it.unipd.dei.nanocitation.metadata.types.Assertion;
import it.unipd.dei.nanocitation.metadata.types.DescriptionTopic;
import it.unipd.dei.nanocitation.metadata.types.MetadataContainer;
import it.unipd.dei.nanocitation.metadata.types.Person;
import it.unipd.dei.nanocitation.util.NoSupportedNpException;
import it.unipd.dei.nanocitation.util.Sio;

public class DerefAndEnrich
{
	public static final String NPURLSERVERDISGENET = "http://rdf.disgenet.org/nanopub-server/";
	//private static final String LANDINGPAGEURL = "http://ims-ws.dei.unipd.it/nanocitation/landingpage/";
	private static final String URILASTPARTREGEX = "[A-Za-z0-9._#-]+";
	private static final String DISGENETURIREGEX = "http://rdf.disgenet.org/resource/nanopub/" + URILASTPARTREGEX;
	private static final String PROTEINATLAS = "http://www.proteinatlas.org" + "[A-Za-z0-9._#-//]+";
	
	
	private final LoggerCustom LOGGER = new LoggerCustom(this.getClass().getName());
	
	private String npId;
	private Sio sio;
	
	public DerefAndEnrich(String npId, File file)
	{
		sio = new Sio(file);
		this.npId = getNanoID(npId);
		
	}
	
	public MetadataContainer getMeta() throws IOException, NoSupportedNpException
	{
		MetadataContainer meta = new MetadataContainer();
		
		String url = NPURLSERVERDISGENET + this.npId;
		
		try
		{
			Nanopub np = new NanopubImpl(new URL(url));
			
			URI npUri = np.getUri();
			meta.setShortId(this.npId);
			meta.setIdentifier(npUri.toString());
			meta.setUrlId(url);
			
			// check if nanopub is supported
			checkSupported(npUri.toString());
			
			// get creation date
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			meta.setTimeCreation(
					(np.getCreationTime() != null ? dateFormat.format(np.getCreationTime().getTime()) : ""));
			
			// get and store info in head
			getHead(np, meta);
			
			// get and store info in pub info
			getPubInfo(np, meta);
			
			// get and store provenance info
			getProvenenceInfo(np, meta);
			
			
			// Enrichment: authors
			AuthorsNpExtractor anpe = new AuthorsNpExtractor();
			ArrayList<Person> npaut = anpe.getNpAuthors(np);
			ArrayList<Person> creat = (ArrayList<Person>) anpe.getCreator(np);
			
			meta.setNpauthors(npaut);
			meta.setCreators(creat);
			
			//System.out.println(meta.toString());
			
			// get and store assertion 
			Set<Statement> ass = np.getAssertion();
			AssertionManager assInfo = new AssertionManager(sio);
			ArrayList<Assertion> asser = (ArrayList<Assertion>) assInfo.getAssertions(ass);
			
			
//			System.out.println("___________________________\n\nAssertions\n");
//			for(Assertion a: asser)
//			{
//				System.out.println("Assertion_____"+ a.toString());
//			}
			
			meta.setAssertion(asser);
			
			ArrayList<DescriptionTopic> dt = assInfo.getAssertionContentFormatted(meta, npUri.toString(), asser);
			
			meta.setContent(dt);
			
//			System.out.println("___________________________\nDescription topic\n");
//			for(DescriptionTopic t: dt)
//			{
//				System.out.println("Assertion_____"+ t.toString());
//			}
			

			
		} catch (MalformedNanopubException | OpenRDFException ex)
		{
			LOGGER.warnMsg("Error in getMeta\n" + ex.getClass().getName() + ": " + ex.getMessage());
			throw new IOException();
		}
		
		return meta;
	}
	
	

	private void checkSupported(String id) throws NoSupportedNpException
	{
		if (!(id.matches(DISGENETURIREGEX) || id.matches(PROTEINATLAS)))
		{
			throw new NoSupportedNpException("Nanopublication not supported yet");
		}
		
	}
	
	private String getNanoID(String givenNpId)
	{
		if (givenNpId.matches(URILASTPARTREGEX))
		{
			int firstIndex = givenNpId.lastIndexOf('.') + 1;
			return givenNpId.substring(firstIndex);
		}
		else
		{
			int firstIndex = givenNpId.lastIndexOf('/') > givenNpId.lastIndexOf('.') ? givenNpId.lastIndexOf('/') + 1
					: givenNpId.lastIndexOf('.') + 1;
			return givenNpId.substring(firstIndex);
		}
		
	}
	
	
	private void getHead(Nanopub np, MetadataContainer meta)
	{
		Set<Statement> head = np.getHead();
		for (Statement u : head)
		{
			String predicate = u.getPredicate().toString();
			String object = u.getObject().toString();
			if (predicate.equals("http://www.w3.org/2000/01/rdf-schema#comment"))
			{
				if (object.toString().contains("/XMLSchema#string"))
					meta.setProvenenceEvidenceComment(
							u.getObject().toString().substring(1, u.getObject().toString().indexOf("^^") - 1));
			}
			
		}
		
	}
	
	/**
	 * @param np
	 * @param meta
	 */
	private void getPubInfo(Nanopub np, MetadataContainer meta)
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
				meta.setTimeCreation(creationTime);
			}
			else if (predicate.contains("dc/terms/rightsHolder"))
			{
				String rightHold = "";
				int firstCharacter = getFirstIndexFromStatementURL(u);
				
				if (firstCharacter != -1)
					rightHold = object.substring(firstCharacter);
				meta.setRightHolder(rightHold);
				meta.setRightUrl(object);
				
			}
			else if (predicate.contains("dc/terms/rights"))
			{
				meta.setRightUrl(object);
			}
			else if (predicate.contains("dc/terms/subject"))
			{
				int firstCharacter = getFirstIndexFromStatementURL(u);
				String sub = "";
				
				if (firstCharacter != -1 && object.substring(firstCharacter).contains("SIO_"))
					sub = sio.sioToReadable(object);
				
				meta.setSubject(sub);
				meta.setSubjectUrl(object);
			}
			else if (predicate.contains("provenance"))
			{
				String plat = "";
				int firstCharacter = getFirstIndexFromStatementURL(u);
				if (firstCharacter != -1)
					plat = object.substring(firstCharacter);
				meta.setPlatform(plat);
				meta.setPlatformUrl(object);
			}
			else if (predicate.contains("version"))
			{
				String version = "";
				Pattern pattern = Pattern.compile("\"[\\d.v]*\"");
				Matcher matcher = pattern.matcher(object);
				if (matcher.find())
					version = matcher.group().substring(1, matcher.group().length() - 1);
				
				meta.setVersion(version);
			}
			
		}
		
	}
	
	/**
	 * @param np
	 * @param meta
	 */
	private void getProvenenceInfo(Nanopub np, MetadataContainer meta)
	{
		Set<Statement> prov = np.getProvenance();
		
		AuthorsExtractor authEx;
		for (Statement u : prov)
		{
			String predicate = u.getPredicate().toString();
			String object = u.getObject().toString();
			
			
			if (predicate.equals("http://semanticscience.org/resource/SIO_000772")) // "has
																					// evidence"
			{
				
				meta.setEvidenceId(u.getObject().toString());
				meta.setEvidenceIdUrl(object);
				if (object.contains("pubmed"))
				{
					authEx = new AuthorsExtractor(u.getObject().toString());
					try
					{
						// extract from PubMed
						authEx.extrNumberAuthors();
					} catch (JSONException e)
					{
						System.err.println(meta.getIdentifier() + " evidence id " + meta.getEvidenceId());
					}
					
					meta.setAuthors(authEx.getAuthList());
					
				}
			}
			
			else if (predicate.equals("http://purl.org/ontology/wi/core#evidence"))
			{
				if (u.getObject().toString().contains("/XMLSchema#string"))
					meta.setProvenanceEvidence(
							u.getObject().toString().substring(1, u.getObject().toString().indexOf("^^") - 1));
				else
				{
					int firstCharacter = getFirstIndexFromStatementURL(u);
					
					meta.setProvenanceEvidence(object.substring(firstCharacter));
					meta.setProvenanceEvidenceUrl(object);
				}
				
			}
			else if (predicate.equals("http://www.w3.org/2000/01/rdf-schema#comment"))
			{
				String[] p = object.split("\"");
				if (p.length > 1 && p[1] != null)
					meta.setProvenenceEvidenceComment(p[1]);
			}
			else if (predicate.equals("http://www.w3.org/2000/01/rdf-schema#label"))
			{
				String[] p = object.split("\"");
				if (p.length > 1 && p[1] != null)
					meta.setProvenenceEvidenceLabel(p[1]);
			}
			else if (predicate.contains("rdf-syntax-ns#type"))
			{
				meta.setProvenenceEvidenceTypeUrl(object);
				
				int firstCharacter = getFirstIndexFromStatementURL(u);
				
				meta.setAssertionType(object.substring(firstCharacter));
				
			}
			else if (predicate.equals("http://www.w3.org/ns/prov#wasDerivedFrom"))
			{
				int firstCharacter = getFirstIndexFromStatementURL(u);
				
				meta.setDatabase(object.substring(firstCharacter));
				meta.setDatabaseUrl(object);
				
			}
			
			else if (predicate.equals("http://www.w3.org/ns/prov#wasGeneratedBy"))
			{
				
				int firstCharacter = getFirstIndexFromStatementURL(u);
				
				
				String provString = (meta.getProvenanceGeneratedBy() != "" ? meta.getProvenanceGeneratedBy() + " " : "");
				if (object.substring(firstCharacter).equals("ECO_0000218"))
					provString += "MANUAL ASSERTION";
				else if (object.substring(firstCharacter).equals("ECO_0000203"))
					provString += "AUTOMATIC ASSERTION";
				else
					provString += spaceAfterCapLetters(object.substring(firstCharacter));
				meta.setProvenanceGeneratedBy(provString);
				meta.setProvenanceGeneratedByUrl(object);
				
			}
			
			else if (predicate.equals("http://purl.org/pav/importedOn"))
			{
				String importedTime = "";
				Pattern pattern = Pattern.compile("\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])\"");
				Matcher matcher = pattern.matcher(object);
				if (matcher.find())
					importedTime = matcher.group().substring(0, matcher.group().length() - 1);
				meta.setDateDb(importedTime);
				
			}
			else if (predicate.equals("http://purl.org/dc/terms/description"))
			{
				String[] p = object.split("\"");
				if (p.length > 1 && p[1] != null)
					meta.setPredicateDescription(p[1]);
				// meta.setPredicateDescription(object);
				
			}
			else if (predicate.equals("http://purl.org/net/provenance/ns#usedData"))
			{
				meta.setUsedDataProv(object);
				if (!object.contains("\""))
					meta.setUsedDataUrlProv(object);
				
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
	
	@SuppressWarnings("unused")
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
	
}
