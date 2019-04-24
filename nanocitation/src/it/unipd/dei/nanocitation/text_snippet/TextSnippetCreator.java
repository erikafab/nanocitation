package it.unipd.dei.nanocitation.text_snippet;

import java.util.ArrayList;

import it.unipd.dei.nanocitation.metadata.types.MetadataContainer;
import it.unipd.dei.nanocitation.metadata.types.Person;

public class TextSnippetCreator
{
	private static final int CREATORFLAG = 0;
	private static final int AUTHORNPFLAG = 1;
	private static final int AUTHORFLAG = 2;
	
	
	private static final int NUMMAXCREATORINTEXTSNIPPET = 1;
	private static final int NUMMAXNPAUTHORSINTEXTSNIPPET = 1;
	private static final int NUMMAXEVIDAUTHORSINTEXTSNIPPET = 1;
	
	private static final String LANDINGPAGEURL = "http://nanocitation.dei.unipd.it/landingpage/";
	
	
	private MetadataContainer meta;
	
	public TextSnippetCreator(MetadataContainer meta)
	{
		this.meta = meta;
	}
	
	public String generateTextSnippet()
	{
StringBuilder bld = new StringBuilder();
		
		// LOGGER.info(meta.toString());
		
		/*
		 * Generate Text Snippet
		 */
		
		/*
		 * Creators
		 */
		bld.append(personInfoTS(meta, CREATORFLAG));
		
		/*
		 * Authors
		 */
		bld.append(personInfoTS(meta, AUTHORNPFLAG));
		
		/*
		 * Creation date
		 */
		bld.append(meta.getTimeCreation());
		bld.append(", ");
		
		/*
		 * Right holder
		 */
		bld.append(meta.getRightHolder());
		bld.append(", ");
		
		/*
		 * subject
		 */
		bld.append(meta.getSubject());
		bld.append(": ");
		
		/*
		 * Assertion Info
		 */
		
		bld.append(meta.getContent().toString());
		bld.append(", ");
		
		/*
		 * Platform
		 */
		
		if (meta.getPlatform() != null)
		{
			bld.append(meta.getPlatform().contains("disgenet") ? "DisGeNET" : meta.getPlatform());
			bld.append(", ");
		}
		
		/*
		 * Version
		 */
		bld.append(meta.getVersion() != null ? meta.getVersion() : "");
		bld.append(", ");
		
		/*
		 * Authors
		 */
		bld.append(personInfoTS(meta, AUTHORFLAG));
		
		/*
		 * Landing Page URL
		 */
		bld.append(LANDINGPAGEURL + meta.getShortId());
		// bld.append(", ");
		
		/*
		 * URL Data Subset or Single NP
		 */
		// bld.append(meta.getIdentifier());
		
		return bld.toString();
	}
	
	
	/**
	 * @param nd
	 * @param flagCreatorsOrNpAuthorsOrAuthors
	 * @return
	 */
	private String personInfoTS(MetadataContainer nd, int flagCreatorsOrNpAuthorsOrAuthors)
	{
		int maxNum;
		int numPerson;
		ArrayList<Person> person;
		switch (flagCreatorsOrNpAuthorsOrAuthors)
		{
			case CREATORFLAG:
				maxNum = NUMMAXCREATORINTEXTSNIPPET;
				numPerson = nd.getNumberOfCreators();
				person = (ArrayList<Person>) nd.getCreators();
				break;
			case AUTHORNPFLAG:
				maxNum = NUMMAXNPAUTHORSINTEXTSNIPPET;
				numPerson = nd.getNumberOfNpAuthors();
				person = (ArrayList<Person>) nd.getNpauthors();
				break;
			case AUTHORFLAG:
				maxNum = NUMMAXEVIDAUTHORSINTEXTSNIPPET;
				numPerson = nd.getNumberOfAuthors();
				person = (ArrayList<Person>) nd.getAuthors();
				break;
			default:
				maxNum = 0;
				numPerson = 0;
				person = new ArrayList<>();
		}
		
		StringBuilder bld = new StringBuilder();
		Person p;
		for (int i = 0; i < maxNum && i < numPerson; ++i)
		{
			p = person.get(i);
			bld.append(p.getNameAndSurnameCapitalized());
			
			if (i == maxNum - 1 && i < numPerson - 1)
			{
				bld.append(" et al.");
				bld.append("(" + numPerson + ")");
			}
			bld.append(", ");
			
		}
		
		return bld.toString();
		
	}
}
