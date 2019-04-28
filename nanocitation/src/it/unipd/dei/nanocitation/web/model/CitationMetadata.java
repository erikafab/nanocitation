package it.unipd.dei.nanocitation.web.model;


import java.util.ArrayList;
import java.util.LinkedList;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import it.unipd.dei.nanocitation.metadata.types.MetadataContainer;


//@XmlType(name = "", propOrder =
//{ "identifier", "creatorList", "contributorList", "creationDate", "rightsHolder", "contentList", "platform", "version",
//		"evidenceReference", "evidenceAuthorList", "landingpageUrl" })
 @JacksonXmlRootElement(namespace =
 "http://nanocitation.dei.unipd.it/citationmetadata", localName =
 "citationMetadata")
public class CitationMetadata
{

	private static final String				LANDINGPAGEURL	= "http://nanocitation.dei.unipd.it/landingpage/";

	private String							identifier;
	private LinkedList<Agent>				creatorList;
	private LinkedList<Agent>				contributorList;

	private String							creationDate;
	private String							rightsHolder;
	private LinkedList<DescriptionTopic>	contentList;
	private String							platform;
	private String							version;

	private String							evidenceReference;

	private LinkedList<Person>				evidenceAuthorList;

	private String							landingpageUrl;

	public CitationMetadata(MetadataContainer metaCont)
	{

		this.identifier = metaCont.getIdentifier();
		this.creatorList = new LinkedList<>();

		for (it.unipd.dei.nanocitation.metadata.types.Person p : metaCont.getCreators())
			this.creatorList.add(new Agent(p));

		this.contributorList = new LinkedList<>();
		for (it.unipd.dei.nanocitation.metadata.types.Person p : metaCont.getNpauthors())
			this.contributorList.add(new Agent(p));

		this.creationDate = metaCont.getTimeCreation();
		this.rightsHolder = metaCont.getRightHolder();

		this.contentList = new LinkedList<>();

		for (it.unipd.dei.nanocitation.metadata.types.DescriptionTopic dt : metaCont.getContent())
			this.contentList.add(new DescriptionTopic(dt));

		this.platform = metaCont.getPlatform();
		this.version = metaCont.getVersion();

		this.evidenceReference = metaCont.getEvidenceId();
		this.evidenceAuthorList = new LinkedList<>();
		for (it.unipd.dei.nanocitation.metadata.types.Person p : metaCont.getAuthors())
			this.evidenceAuthorList.add(new Person(p));

		this.landingpageUrl = LANDINGPAGEURL + metaCont.getShortId();
	}

	/**
	 * @return the identifier
	 */
	 @JacksonXmlElementWrapper(namespace =
	 "http://nanocitation.dei.unipd.it/citationmetadata/id", localName =
	 "identifier")
//	@XmlElement(name = "identifier")
	public String getIdentifier()
	{
		return identifier;
	}

	/**
	 * @param identifier
	 *                   the identifier to set
	 */
	public void setIdentifier(String identifier)
	{
		this.identifier = identifier;
	}

	/**
	 * @return the creatorList
	 */
	 @JacksonXmlElementWrapper(namespace =
	 "http://nanocitation.dei.unipd.it/creators", localName = "creators")
//	@XmlElementWrapper(name = "creators")
//	@XmlList
	public LinkedList<Agent> getCreator()
	{
		return creatorList;
	}

	/**
	 * @param creatorList
	 *                    the creatorList to set
	 */
	public void setCreator(LinkedList<Agent> creator)
	{
		this.creatorList = creator;
	}

	/**
	 * @return the contributorList
	 */
	 @JacksonXmlElementWrapper(namespace =
	 "http://nanocitation.dei.unipd.it/contributors", localName = "contributors")
//	@XmlElementWrapper(name = "contributors")
//	@XmlList
	public LinkedList<Agent> getContributor()
	{
		return contributorList;
	}

	/**
	 * @param contributorList
	 *                        the contributorList to set
	 */
	public void setContributor(LinkedList<Agent> contributor)
	{
		this.contributorList = contributor;
	}

	/**
	 * @return the creationDate
	 */
//	@XmlElement(name = "creationDate")
	 @JacksonXmlProperty(namespace =
	 "http://nanocitation.dei.unipd.it/citationmetadata/creation_date", localName
	 = "creationDate")
	public String getCreationDate()
	{
		return creationDate;
	}

	/**
	 * @param creationDate
	 *                     the creationDate to set
	 */
	public void setCreationDate(String creationDate)
	{
		this.creationDate = creationDate;
	}

	/**
	 * @return the rightsHolder
	 */
	 @JacksonXmlProperty(namespace =
	 "http://nanocitation.dei.unipd.it/citationmetadata/rights_holder", localName
	 = "rightsHolder")
//	@XmlElement(name = "rightsHolder")
	public String getRightsHolder()
	{
		return rightsHolder;
	}

	/**
	 * @param rightsHolder
	 *                     the rightsHolder to set
	 */
	public void setRightsHolder(String rightsHolder)
	{
		this.rightsHolder = rightsHolder;
	}

	/**
	 * @return the contentList
	 */
	 @JacksonXmlElementWrapper(namespace =
	 "http://nanocitation.dei.unipd.it/content", localName = "content")
//	@XmlElementWrapper(name = "DescriptionTopic")
//	@XmlList
	public LinkedList<DescriptionTopic> getContent()
	{
		return contentList;
	}

	/**
	 * @param contentList
	 *                    the contentList to set
	 */
	public void setContent(LinkedList<DescriptionTopic> contentList)
	{
		this.contentList = contentList;
	}

	/**
	 * @return the platform
	 */
	 @JacksonXmlProperty(namespace =
	 "http://nanocitation.dei.unipd.it/citationmetadata/platform", localName =
	 "platform")
//	@XmlElement(name = "platform")
	public String getPlatform()
	{
		return platform;
	}

	/**
	 * @param platform
	 *                 the platform to set
	 */
	public void setPlatform(String platform)
	{
		this.platform = platform;
	}

	/**
	 * @return the version
	 */
	 @JacksonXmlProperty(namespace =
	 "http://nanocitation.dei.unipd.it/citationmetadata/version", localName =
	 "version")
//	@XmlElement(name = "version")
	public String getVersion()
	{
		return version;
	}

	/**
	 * @param version
	 *                the version to set
	 */
	public void setVersion(String version)
	{
		this.version = version;
	}

	/**
	 * @return the evidenceReference
	 */
	 @JacksonXmlProperty(namespace =
	 "http://nanocitation.dei.unipd.it/citationmetadata/evidence_reference",
	 localName = "evidenceRefernce")
//	@XmlElement(name = "evidenceReference")
	public String getEvidenceReference()
	{
		return evidenceReference;
	}

	/**
	 * @param evidenceReference
	 *                          the evidenceReference to set
	 */
	public void setEvidenceReference(String evidenceReference)
	{
		this.evidenceReference = evidenceReference;
	}

	/**
	 * @return the evidenceAuthorList
	 */
	 @JacksonXmlElementWrapper(namespace =
	 "http://nanocitation.dei.unipd.it/evidence_authors", localName =
	 "evidenceAuthors")
//	@XmlElementWrapper(name = "evidenceAuthors")
//	@XmlList
	public LinkedList<Person> getEvidenceAuthor()
	{
		return evidenceAuthorList;
	}

	/**
	 * @param evidenceAuthorsList
	 *                            the evidenceAuthorsList to set
	 */
	public void setEvidenceAuthor(LinkedList<Person> evidenceAuthor)
	{
		this.evidenceAuthorList = evidenceAuthor;
	}

	/**
	 * @return the landingpageUrl
	 */
	 @JacksonXmlProperty(namespace =
	 "http://nanocitation.dei.unipd.it/citationmetadata/landing_page_url",
	 localName = "landingpageUrl")
//	@XmlElement(name = "landingpageUrl")
	public String getLandingpageUrl()
	{
		return landingpageUrl;
	}

	/**
	 * @param landingpageUrl
	 *                       the landingpageUrl to set
	 */
	public void setLandingpageUrl(String landingpageUrl)
	{
		this.landingpageUrl = landingpageUrl;
	}

	@JacksonXmlRootElement(namespace =
			 "http://nanocitation.dei.unipd.it/agent", localName = "agent")
//	@XmlRootElement(name = "Agent")
	public class Agent
	{
		

		private Person			pers;

		private Organisation	org;

		public Agent(it.unipd.dei.nanocitation.metadata.types.Person p)
		{
			this.pers = new Person(p);
		}

		/**
		 * @return the pers
		 */
		 @JacksonXmlProperty(localName = "pers")
//		@XmlElement(name = "pers")
		public Person getPers()
		{
			return pers;
		}

		/**
		 * @param pers
		 *             the pers to set
		 */
		public void setPers(Person pers)
		{
			this.pers = pers;
		}

		/**
		 * @return the org
		 */
//		@XmlElement(name = "org")
		 @JacksonXmlProperty(localName = "org")
		public Organisation getOrg()
		{
			return org;
		}

		/**
		 * @param org
		 *            the org to set
		 */
		public void setOrg(Organisation org)
		{
			this.org = org;
		}

	}

	 @JacksonXmlRootElement(localName = "organisation")
//	@XmlRootElement(name = "Organisation")
	public class Organisation
	{
		//
		private String orgName;

		/**
		 * @return the orgName
		 */
		 @JacksonXmlProperty(localName = "orgName")
//		@XmlElement(name = "orgName")
		public String getOrgName()
		{
			return orgName;
		}

		/**
		 * @param orgName
		 *                the orgName to set
		 */
		public void setOrgName(String orgName)
		{
			this.orgName = orgName;
		}

	}

	 @JacksonXmlRootElement(localName = "person")
//	@XmlRootElement(name = "Person")
	public class Person
	{
		//
		private String	given_name;
		//
		private String	family_name;
		//
		private String	personID;

		public Person(it.unipd.dei.nanocitation.metadata.types.Person p)
		{
			this.given_name = p.getName();
			this.family_name = p.getSurname();
			if (p.getOrcidId() != null)
				this.personID = p.getOrcidId();
			else if (p.getUrlId() != null)
				this.personID = p.getUrlId();
		}

		 @JacksonXmlProperty(localName = "given_name")
//		@XmlElement(name = "given_name")
		public String getGiven_name()
		{
			return given_name;
		}

		public void setGiven_name(String given_name)
		{
			this.given_name = given_name;
		}

		 @JacksonXmlProperty(localName = "family_name")
//		@XmlElement(name = "family_name")
		public String getFamily_name()
		{
			return family_name;
		}

		public void setFamily_name(String family_name)
		{
			this.family_name = family_name;
		}

		 @JacksonXmlProperty(localName = "personID")
//		@XmlElement(name = "personID")
		public String getPersonID()
		{
			return personID;
		}

		public void setPersonID(String personID)
		{
			this.personID = personID;
		}

	}

	 @JacksonXmlRootElement(localName = "descriptionTopic")
//	@XmlRootElement(name = "DescriptionTopic")
	public class DescriptionTopic
	{

		//
		private String				subject;
		// 
		private ArrayList<String>	assertion	= new ArrayList<>();

		public DescriptionTopic(it.unipd.dei.nanocitation.metadata.types.DescriptionTopic dt)
		{
			this.subject = dt.getSubject();
			for (String s : dt.getAssertion())
				this.assertion.add(s);
		}

		 @JacksonXmlProperty(localName = "subject")
//		@XmlElement(name = "subject")
		public String getSubject()
		{
			return subject;
		}

		public void setSubject(String subject)
		{
			this.subject = subject;
		}

		// @XmlElementWrapper(name = "assertion")
		// @XmlList
		@JacksonXmlElementWrapper(localName = "assertion")
		public ArrayList<String> getAssertion()
		{
			return assertion;
		}

		public void setAssertion(ArrayList<String> assertion)
		{
			this.assertion = assertion;
		}

		public void add(String a)
		{
			this.assertion.add(a);
		}

		@Override
		public String toString()
		{
			StringBuilder bld = new StringBuilder();

			if (subject != null)
				bld.append("(" + subject + ") ");

			String a;
			for (int i = 0; i < assertion.size(); ++i)
			{
				a = assertion.get(i);
				bld.append(a);
				if (i < assertion.size() - 1)
					bld.append("; ");
			}
			return bld.toString();
		}

	}
}
