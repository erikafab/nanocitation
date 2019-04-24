///**
// * 
// */
//package it.unipd.dei.nanop.util;
//
//import java.io.File;
//import java.io.IOException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Set;
//
//import org.nanopub.MalformedNanopubException;
//import org.nanopub.Nanopub;
//import org.nanopub.NanopubImpl;
//import org.openrdf.OpenRDFException;
//import org.openrdf.model.Statement;
//
//import it.unipd.dei.nanocitation.landing_page.LandingPageCreator;
//import it.unipd.dei.nanocitation.logger.LoggerCustom;
//import it.unipd.dei.nanocitation.metadata.types.Assertion;
//import it.unipd.dei.nanocitation.metadata.types.MetadataContainer;
//import it.unipd.dei.nanocitation.metadata.types.Person;
//import it.unipd.dei.nanocitation.text_snippet.ExtractTextSnippetInfo;
//import it.unipd.dei.nanocitation.util.AssertionInfo;
//import it.unipd.dei.nanocitation.util.NoSupportedNpException;
//import it.unipd.dei.nanocitation.util.Sio;
//import it.unipd.dei.nanocitation.web.model.LandingPage;
//
///**
// * This class consists exclusively of a main and methods that creates a text
// * snippet from a given nanopublication.
// * 
// * @author erika
// *
// */
//public class AutomaticTextSnippet
//{
//	
//	public static final String NPURLSERVERDISGENET = "http://rdf.disgenet.org/nanopub-server/";
//	private static final String LANDINGPAGEURL = "http://nanocitation.dei.unipd.it/landingpage/";
//	private static final String URILASTPARTREGEX = "[A-Za-z0-9._#-]+";
//	private static final String DISGENETURIREGEX = "http://rdf.disgenet.org/resource/nanopub/" + URILASTPARTREGEX;
//	private static final String PROTEINATLAS = "http://www.proteinatlas.org" + "[A-Za-z0-9._#-//]+";
//	
//	private static final int NUMMAXCREATORINTEXTSNIPPET = 1;
//	private static final int NUMMAXNPAUTHORSINTEXTSNIPPET = 1;
//	private static final int NUMMAXEVIDAUTHORSINTEXTSNIPPET = 1;
//	
//	private static final boolean TEST = false;
//	
//	// private static final Logger LOGGER =
//	// LoggerFactory.getLogger(AutomaticTextSnippet.class);
//	private final LoggerCustom LOGGER = new LoggerCustom(this.getClass().getName());
//	
//	private static final int CREATORFLAG = 0;
//	private static final int AUTHORNPFLAG = 1;
//	private static final int AUTHORFLAG = 2;
//	
//	private String npId;
//	
//	private MetadataContainer nd;
//	
//	public LandingPage landingPageConfigurator(String npId, File file) throws IOException, NoSupportedNpException
//	{
//		Sio sio = new Sio(file);
//		
//		// normalize the id if it is in the form of a URIs (such as
//		// http://rdf.disgenet.org/resource/nanopub/NP311194.RAtszEYhmGBt_wP7rvpUCkRpyGS2bFylq1QtKYoSyXaa4
//		// or an id which contains an additional prefix (such as
//		// NP311194.RAtszEYhmGBt_wP7rvpUCkRpyGS2bFylq1QtKYoSyXaa4)
//		// get the last part of the given (in the example
//		// RAtszEYhmGBt_wP7rvpUCkRpyGS2bFylq1QtKYoSyXaa4)
//		// LOGGER.info("Id required: " + npId);
//		
//		// here check the nanopublication to be supported by the tool--> check nanopublication to have an uri which is Disgenet one or proteinatlas
//		
//		this.npId = getDisgenetID(npId);
//		String url = NPURLSERVERDISGENET + this.npId;
//		
//		try
//		{
//			Nanopub np = new NanopubImpl(new URL(url));
//			
//			// check np to be a DisGeNET one
//			// checkNpPresenceInDisgenet(np.getUri());
//			
//			// System.out.println("-------->Head");
//			// Set<Statement> s = np.getHead();
//			// for (Statement u : s)
//			// {
//			// System.out.println(u.getSubject() + " " + u.getPredicate() + " "
//			// + u.getObject());
//			// System.out.println("__________");
//			// }
//			//
//			// s = np.getAssertion();
//			// System.out.println("-------->Assertion");
//			// for (Statement u : s)
//			// {
//			// System.out.println(u.getSubject() + " " + u.getPredicate() + " "
//			// + u.getObject());
//			// System.out.println("__________");
//			// }
//			// System.out.println("-------->Pubinfo");
//			// s = np.getPubinfo();
//			// for (Statement u : s)
//			// {
//			// System.out.println(u.getSubject() + " " + u.getPredicate() + " "
//			// + u.getObject());
//			// System.out.println("__________");
//			// }
//			// System.out.println("-------->Provenance");
//			// s = np.getProvenance();
//			// for (Statement u : s)
//			// {
//			// System.out.println(u.getSubject() + " " + u.getPredicate() + " "
//			// + u.getObject());
//			// System.out.println("__________");
//			// }
//			
//			ExtractTextSnippetInfo ets = new ExtractTextSnippetInfo(sio);
//			
//			String identifier = ets.getIdentifier(new URL(url));
//			if(!(identifier.contains("rdf.disgenet.org") || identifier.contains("proteinatlas")))
//				throw new NoSupportedNpException("Nanopublication not supported");
//			
//			nd = ets.getInfo(new URL(url));
//			
//			
//			
//			nd.setUrlId(url);
//			// LOGGER.info(nd.toString());
//			
//			Set<Statement> ass = np.getAssertion();
//			AssertionInfo assInfo = new AssertionInfo(sio);
//			ArrayList<Assertion> asser = (ArrayList<Assertion>) assInfo.getAssertionsHumReadable(ass);
//			
//			String textsnippet = generateTextSnippet(nd, asser, LANDINGPAGEURL);
//			
//			// LOGGER.info("\n\n\nText Snippet:\n");
//			// LOGGER.info(textsnippet);
//			
//			LandingPageCreator lpc = new LandingPageCreator(nd, textsnippet);
//			LandingPage lp = lpc.createPage(LANDINGPAGEURL);
//			return lp;
//			
//		} catch (MalformedNanopubException | OpenRDFException ex)
//		{
//			LOGGER.warnMsg("Error in main\n" + ex.getClass().getName() + ": " + ex.getMessage());
//			throw new IOException();
//		}
//		
//	}
//	
//	private String getDisgenetID(String givenNpId)
//	{	
//		if (givenNpId.matches(URILASTPARTREGEX))
//		{
//			int firstIndex = givenNpId.lastIndexOf('.') + 1;
//			return givenNpId.substring(firstIndex);
//		}
//		//if (givenNpId.matches(DISGENETURIREGEX))
//		else
//		{
//			int firstIndex = givenNpId.lastIndexOf('/') > givenNpId.lastIndexOf('.') ? givenNpId.lastIndexOf('/') + 1
//					: givenNpId.lastIndexOf('.') + 1;
//			return givenNpId.substring(firstIndex);
//		}
//		//else
//		//	return givenNpId;
//		
//	}
//	
//	// @SuppressWarnings("unused")
//	// private void checkNpPresenceInDisgenet(URI uri) throws
//	// noDisgenetNpException
//	// {
//	//
//	// if (!uri.toString().contains("rdf.disgenet.org/resource/nanopub"))
//	// throw new noDisgenetNpException();
//	// }
//	//
//	/**
//	 * @param nd
//	 * @param asser
//	 * @return
//	 */
//	private String generateTextSnippet(MetadataContainer nd, ArrayList<Assertion> asser, String urlLandingPage)
//	{
//		StringBuilder bld = new StringBuilder();
//		
//		// LOGGER.info(nd.toString());
//		
//		/*
//		 * Generate Text Snippet
//		 */
//		
//		/*
//		 * Creators
//		 */
//		bld.append(personInfoTS(nd, CREATORFLAG));
//		
//		/*
//		 * Authors
//		 */
//		bld.append(personInfoTS(nd, AUTHORNPFLAG));
//		
//		/*
//		 * Creation date
//		 */
//		bld.append(nd.getTimeCreation());
//		bld.append(", ");
//		
//		/*
//		 * Right holder
//		 */
//		bld.append(nd.getRightHolder());
//		bld.append(", ");
//		
//		/*
//		 * subject
//		 */
//		bld.append(nd.getSubject());
//		bld.append(": ");
//		
//		/*
//		 * Assertion Info
//		 */
//		
//		bld.append(getAssertionInfoForTextSnippet(asser));
//		bld.append(", ");
//		
//		/*
//		 * Platform
//		 */
//		
//		if (nd.getPlatform() != null)
//		{
//			bld.append(nd.getPlatform().contains("disgenet") ? "DisGeNET" : nd.getPlatform());
//			bld.append(", ");
//		}
//		
//		/*
//		 * Version
//		 */
//		bld.append(nd.getVersion() != null ? nd.getVersion() : "");
//		bld.append(", ");
//		
//		/*
//		 * Authors
//		 */
//		bld.append(personInfoTS(nd, AUTHORFLAG));
//		
//		/*
//		 * Landing Page URL
//		 */
//		bld.append(urlLandingPage + npId);
//		// bld.append(", ");
//		
//		/*
//		 * URL Data Subset or Single NP
//		 */
//		// bld.append(nd.getIdentifier());
//		
//		return bld.toString();
//		
//	}
//	
//	/**
//	 * @param asser
//	 * @return
//	 */
//	public Object getAssertionInfoForTextSnippet(ArrayList<Assertion> asser)
//	{
//		String ret = "";
//		// GDA associations id type gda, gene-name type gene, disease-name type
//		// disease,
//		// gda id refers to gda
//		if (asser.size() == 5)
//			ret = stringFrom5Assertions(asser);
//		
//		// GDA associations
//		else if (asser.size() == 3)
//		{
//			ret = stringFrom3Assertions(asser);
//		}
//		
//		return ret;
//	}
//	
//	/**
//	 * @param asser
//	 * @return
//	 */
//	private String stringFrom5Assertions(ArrayList<Assertion> asser)
//	{
//		StringBuilder assInfoTS = new StringBuilder();
//		
//		for (Assertion a : asser)
//			// for now... only if GDA association
//			if (a.getSubject().contains("GDA id") && a.getPredicate().equals("type"))
//			{
//				
//				assInfoTS.append("(" + a.getObject() + ") ");
//				String gene = "";
//				String disease = "";
//				
//				for (Assertion as : asser)
//				{
//					if (as.getObject().equals("Gene") && as.getPredicate().equals("type"))
//						gene = as.getSubject();
//					else if (as.getObject().equals("Disease") && as.getPredicate().equals("type"))
//						disease = as.getSubject();
//				}
//				
//				assInfoTS.append(gene + " - " + disease);
//				return assInfoTS.toString();
//				
//			}
//		return assInfoTS.toString();
//	}
//	
//	/**
//	 * @param asser
//	 * @return
//	 */
//	private String stringFrom3Assertions(ArrayList<Assertion> asser)
//	{
//		
//		StringBuilder assInfoTS = new StringBuilder();
//		
//		/// TODO modificare
//		String nptype = "";
//		for (Assertion a : asser)
//		{
//			if (a.getSubject().contains("GDA id"))
//			{
//				nptype = "disg";
//				break;
//			}
//			else if (a.getSubject().contains("proteinatlas"))
//			{
//				nptype = "proteinatlas";
//				break;
//			}
//		}
//		
//		if (nptype == "dis")
//		
//		{
//			for (Assertion a : asser)
//			{
//				if (a.getSubject().contains("GDA id") && a.getPredicate().equals("type"))
//				{
//					assInfoTS.append("(" + a.getObject() + ") ");
//					String gene = "";
//					String disease = "";
//					String gdaId = a.getSubject();
//					
//					for (Assertion as : asser)
//					{
//						if (as.getSubject().equals(gdaId) && as.getPredicate().equals("refers to") && gene.equals(""))
//							gene = as.getObject();
//						else if (as.getSubject().equals(gdaId) && as.getPredicate().equals("refers to")
//								&& disease.equals(""))
//							disease = as.getObject();
//					}
//					
//					assInfoTS.append(gene + " - " + disease);
//					return assInfoTS.toString();
//					
//				}
//			}
//		}
//		else
//		{
//			for (Assertion a : asser)
//			{
//				assInfoTS.append( a.getObject() != null ? "(" + a.getObject() + ") " : "");
//				String sub = a.getSubject();
//				String pred = a.getPredicate();
//				String ob = a.getObject();
//				String type = a.getSubject();
//				
//				assInfoTS.append(sub + " " + pred + " " + ob);
//			}
//		}
//		return assInfoTS.toString();
//		
//	}
//	
//	/**
//	 * @param nd
//	 * @param flagCreatorsOrNpAuthorsOrAuthors
//	 * @return
//	 */
//	private String personInfoTS(MetadataContainer nd, int flagCreatorsOrNpAuthorsOrAuthors)
//	{
//		int maxNum;
//		int numPerson;
//		ArrayList<Person> person;
//		switch (flagCreatorsOrNpAuthorsOrAuthors)
//		{
//			case CREATORFLAG:
//				maxNum = NUMMAXCREATORINTEXTSNIPPET;
//				numPerson = nd.getNumberOfCreators();
//				person = (ArrayList<Person>) nd.getCreators();
//				break;
//			case AUTHORNPFLAG:
//				maxNum = NUMMAXNPAUTHORSINTEXTSNIPPET;
//				numPerson = nd.getNumberOfNpAuthors();
//				person = (ArrayList<Person>) nd.getNpauthors();
//				break;
//			case AUTHORFLAG:
//				maxNum = NUMMAXEVIDAUTHORSINTEXTSNIPPET;
//				numPerson = nd.getNumberOfAuthors();
//				person = (ArrayList<Person>) nd.getAuthors();
//				break;
//			default:
//				maxNum = 0;
//				numPerson = 0;
//				person = new ArrayList<>();
//		}
//		
//		StringBuilder bld = new StringBuilder();
//		Person p;
//		for (int i = 0; i < maxNum && i < numPerson; ++i)
//		{
//			p = person.get(i);
//			bld.append(p.getNameAndSurnameCapitalized());
//			
//			if (i == maxNum - 1 && i < numPerson - 1)
//			{
//				bld.append(" et al.");
//				bld.append("(" + numPerson + ")");
//			}
//			bld.append(", ");
//			
//		}
//		
//		return bld.toString();
//		
//	}
//}