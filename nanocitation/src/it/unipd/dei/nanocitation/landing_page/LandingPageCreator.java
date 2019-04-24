package it.unipd.dei.nanocitation.landing_page;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.nanopub.Nanopub;

import it.unipd.dei.nanocitation.metadata.types.Assertion;
import it.unipd.dei.nanocitation.metadata.types.MetadataContainer;
import it.unipd.dei.nanocitation.metadata.types.Person;
import it.unipd.dei.nanocitation.web.model.LandingPage;

/**
 * This is a Class dedicated to create from a Nano Publication Id the model to
 * return for a relative view which will visualize it
 * 
 * @author erika
 *
 */
public class LandingPageCreator
{
	private static final Logger LOGGER = Logger.getLogger(LandingPageCreator.class.getName());
	
	private MetadataContainer nd;
	private String textsnippet;
	
	public LandingPageCreator(MetadataContainer nd, String textsnippet)
	{
		this.nd = nd;
		this.textsnippet = textsnippet;
	}
	
	public LandingPage createPage()
	{
		LandingPage lp = new LandingPage();
		lp.setTextSnippet(textsnippet);
		lp.setUrlNP(getHtmlStringUrlNP());
		lp.setTimeCreation(getHtmlTimeCreation());
		lp.setRightHolder(getHtmlRightHolder());
		lp.setPlatform(getHtmlPlatform());
		lp.setVersion("<b>Version</b>: " + nd.getVersion());
		lp.setCreators(getHtmlStringCreators());
		lp.setAuthors(getHtmlStringAuthors());
		lp.setAssertion(getHtmlAssertion());
		String pubmed = null;
		if (nd.getEvidenceId().contains("pubmed"))
			pubmed = nd.getEvidenceId().substring(nd.getEvidenceId().lastIndexOf('/') + 1);
		lp.setEvidauthors(getHtmlStringAuthorsEvid(pubmed));
		lp.setProvenance(getHtmlProvenance());
		
		lp.setIdNP(nd.getIdentifier());
		
		return lp;
	}
	
	private String getHtmlStringUrlNP()
	{
		return "<b>Link to the Nanopublication</b>: <a target=\"_blank\" rel=\"noopener noreferrer\" href=\""
				+ nd.getUrlId() + "\">" + nd.getIdentifier() + "</a>";
	}
	
	private String getHtmlTimeCreation()
	{
		String ret = "";
		if (nd.getTimeCreation() != null)
			ret = "<b>Creation Date</b>: " + nd.getTimeCreation();
		return ret;
	}
	
	private String getHtmlRightHolder()
	{
		String ret = "";
		if (nd.getTimeCreation() != null)
			ret = "<b>Right Holder</b>: " + nd.getRightHolder();
		return ret;
	}
	
	private String getHtmlPlatform()
	{
		String ret = "";
		String plat = "";
		if (nd.getPlatform() != null)
			plat = nd.getPlatform().contains("disgenet") ? "DisGeNET" : nd.getPlatform();
		
		ret = "<b>Platform</b>: <a target=\"_blank\" rel=\"noopener noreferrer\" href=\"" + nd.getPlatformUrl() + "\">"
				+ plat + "</a>";
		return ret;
	}
	
	private String getHtmlStringCreators()
	{
		StringBuilder bld = new StringBuilder();
		
		for (Person p : nd.getCreators())
		{
			if (p.getUrlId() != null && p.getNameAndSurnameCapitalized() != "")
			{
				bld.append("<a target=\"_blank\" rel=\"noopener noreferrer\" href=\"" + p.getUrlId() + "\">"
						+ p.getNameAndSurnameCapitalized() + "</a>");
				bld.append("<br>");
			}
			else
			{
				bld.append(p.getNameAndSurnameCapitalized());
				bld.append("<br>");
			}
			
		}
		
		return bld.toString();
	}
	
	private String getHtmlStringAuthors()
	{
		StringBuilder bld = new StringBuilder();
		
		for (Person p : nd.getNpauthors())
		{
			if (p.getUrlId() != null)
			{
				bld.append("<a target=\"_blank\" rel=\"noopener noreferrer\" href=\"" + p.getUrlId() + "\">"
						+ p.getNameAndSurnameCapitalized() + "</a>");
				bld.append("<br>");
			}
			else
			{
				bld.append(p.getNameAndSurnameCapitalized());
				bld.append("<br>");
			}
			
		}
		
		return bld.toString();
	}
	
	private String getHtmlAssertion()
	{
		StringBuilder bld = new StringBuilder();
		
		bld.append((nd.getProvenenceEvidenceComment() != null && !nd.getProvenenceEvidenceComment().equals(""))
				? "<p><b> Provenence Evidence Comment:</b> </p>" + nd.getProvenenceEvidenceComment() + "<br><br>"
				: "");
		bld.append((nd.getPredicateDescription() != null)
				? "<p><b> Assertion Description:</b> </p>" + nd.getPredicateDescriptionString() + "<br><br>"
				: "");
		if (nd.getPredicateDescription() == null && nd.getProvenenceEvidenceComment() != null)
			bld.append("<p><b> Assertion Description:</b> </p>" + nd.getProvenenceEvidenceComment() + "<br><br>");
		
		bld.append("<p><b> Assertion:</b> </p>" + nd.getContent().toString());
		
		bld.append("<br><br>");
		
		bld.append("<p><b> Reference links:</b> </p>");
		
		ArrayList<URI> alreadyPrint = new ArrayList<>();
		for (Assertion a : nd.getAssertion())
		{
			if (!a.getSubject().contains("GDA id") && a.getSubjectURI() != null
					&& !alreadyPrint.contains(a.getSubjectURI()))
			{
				bld.append("<p>");
				bld.append("<a target=\"_blank\" rel=\"noopener noreferrer\" href=\"" + a.getSubjectURI() + "\">"
						+ a.getSubject() + "</a>");
				
				bld.append("</p>");
				alreadyPrint.add(a.getSubjectURI());
				
			}
			else if (a.getObject() != null && !a.getObject().contains("GDA id")
					
					&& !alreadyPrint.contains(a.getObjectURI()))
			{
				bld.append("<p>");
				bld.append("<a target=\"_blank\" rel=\"noopener noreferrer\" href=\"" + a.getObjectURI() + "\">"
						+ a.getObject() + "</a>");
				bld.append("</p>");
				alreadyPrint.add(a.getObjectURI());
			}
			
		}
		
		bld.append("</p>");
		return bld.toString();
	}
	
	private String getHtmlProvenance()
	{
		StringBuilder bld = new StringBuilder();
		
		bld.append("<p><b>Assertion Generated by</b>: ");
		bld.append("<a target=\"_blank\" rel=\"noopener noreferrer\" href=\"" + nd.getProvenanceGeneratedByUrl() + "\">"
				+ nd.getProvenanceGeneratedBy().toLowerCase() + "</a>");
		
		bld.append("<br>");
		bld.append("<a target=\"_blank\" rel=\"noopener noreferrer\" href=\"" + nd.getProvenenceEvidenceTypeUrl()
				+ "\">" + nd.getProvenenceEvidenceLabel() + "</a>");
		bld.append("<br>");
		bld.append("<b>Assertion Generation Description</b>: " + nd.getProvenenceEvidenceComment());
		
		bld.append("<br><br>");
		bld.append("<b>Evidence Source</b>: ");
		bld.append("<a target=\"_blank\" rel=\"noopener noreferrer\" href=\"" + nd.getEvidenceId() + "\">"
				+ nd.getEvidenceId() + "</a>");
		
		if (nd.getEvidenceId().contains("pubmed"))
			bld.append(getInfoFromPubmed(nd.getEvidenceId()));
		bld.append("</p>");
		return bld.toString();
	}
	
	private String getInfoFromPubmed(String evidenceId)
	{
		StringBuilder bld = new StringBuilder();
		if (evidenceId.lastIndexOf('/') == evidenceId.length() - 1)
			return "";
		
		try (InputStream in = new URL("https://www.ncbi.nlm.nih.gov/pubmed/"
				+ evidenceId.substring(evidenceId.lastIndexOf('/') + 1) + "?report=abstract&format=text").openStream())
		{
			String txt = IOUtils.toString(in, StandardCharsets.UTF_8);
			String[] txtSplit = txt.split("\n\n");
			
			String abst = "";
			for (String t : txtSplit)
			{
				if (abst.length() < t.length())
					abst = t;
			}
			
			// bld.append("<p>Abstract:\n" + txtSplit[txtSplit.length - 3] +
			// "</p>");
			bld.append("<p>Abstract:\n" + abst + "</p>");
			
		} catch (IOException e)
		{
			LOGGER.log(Level.WARNING, "Error in getInfoFromPubmed\n" + e.getClass().getName() + ": " + e.getMessage());
		}
		
		return bld.toString();
		
	}
	
	private String getHtmlStringAuthorsEvid(String pubmed)
	{
		StringBuilder bld = new StringBuilder();
		
		for (Person p : nd.getAuthors())
		{
			if (p.getOrcidId() != null)
			{
				bld.append("<a target=\"_blank\" rel=\"noopener noreferrer\" href=\"" + p.getUrlId() + "\">"
						+ p.getNameAndSurnameCapitalized() + "</a>");
				bld.append("<br>");
			}
			else
			{
				if (pubmed != null)
				{
					String a = "https://www.ncbi.nlm.nih.gov/pubmed/?term=" + p.getSurname() + "%20" + p.getName()
							+ "%5BAuthor%5D&cauthor_uid=" + pubmed;
					bld.append("<a target=\"_blank\" rel=\"noopener noreferrer\" href=\"" + a + "\">"
							+ p.getNameAndSurnameCapitalized() + "</a>");
				}
				else
					bld.append(p.getNameAndSurnameCapitalized());
				bld.append("<br>");
			}
			
		}
		
		return bld.toString();
	}
	
}
