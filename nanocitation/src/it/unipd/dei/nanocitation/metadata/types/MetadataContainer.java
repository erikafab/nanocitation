/**
 * 
 */
package it.unipd.dei.nanocitation.metadata.types;

import java.util.ArrayList;
import java.util.List;

/**
 * @author erika
 *
 */
public class MetadataContainer
{
	
	private String identifier;
	private String urlId;
	private String shortId;
	
	// Public Info
	private String timeCreation;
	private String subject;
	private String subjectUrl;
	private String rightHolder;
	private String rightHolderUrl;
	private String rightUrl;
	private String platform;
	private String platformUrl;
	private String version;
	private String assertionType; // GDA or other
	
	private ArrayList<Person> authors;
	private ArrayList<Person> npauthors;
	private ArrayList<Person> creators;
	
	private int numberOfAuthors;
	private int numberOfNpAuthors;
	private int numberOfCreators;
	
	// Assertion
	private ArrayList<String> predicateDescription;
	
	// Provenance
	private String evidenceId;
	private String evidenceIdUrl;
	// CURATED PREDICTED or LITERATURE
	private String provenanceEvidence;
	private String provenanceEvidenceUrl;
	
	private String provenenceEvidenceTypeUrl;
	private String provenenceEvidenceComment;
	private String provenenceEvidenceLabel;
	
	// MANUAL or AUTOMATIC
	private String provenanceGeneratedBy;
	private String provenanceGeneratedByUrl;
	
	// wasDerivedFrom
	// if integrated data from expert curated databases
	// ctd_human, gwascat, rgd, gad, lhgdn, BEFREE
	private String database;
	private String databaseUrl;
	// importedOn
	private String dateDb;
	
	
	//usedData on provenance graph: this property refers to a source data item that has been use during the creation of a data item
	private ArrayList<String> usedDataProv;
	
	private ArrayList<String> usedDataUrlProv;
	
	private ArrayList<DescriptionTopic> content;
	private ArrayList<Assertion> assertion;
	
	

	/**
	 * 
	 */
	public MetadataContainer()
	{
		authors = new ArrayList<>();
		npauthors = new ArrayList<>();
		creators = new ArrayList<>();
		numberOfAuthors = 0;
		numberOfNpAuthors = 0;
		numberOfCreators = 0;
		
		identifier = "";
		urlId = "";
		
		timeCreation = "";
		subject = "";
		subjectUrl = "";
		rightHolder = "";
		rightHolderUrl = "";
		rightUrl = "";
		platform = "";
		platformUrl = "";
		version = "";
		assertionType = "";
		
		authors = new ArrayList<>();
		npauthors = new ArrayList<>();
		creators = new ArrayList<>();
		
		numberOfAuthors = 0;
		numberOfNpAuthors = 0;
		numberOfCreators = 0;
		
		predicateDescription = new ArrayList<>();
		
		evidenceId = "";
		evidenceIdUrl = "";
		provenanceEvidence = "";
		provenanceEvidenceUrl = "";
		
		provenenceEvidenceTypeUrl = "";
		provenenceEvidenceComment = "";
		provenenceEvidenceLabel = "";
		
		provenanceGeneratedBy = "";
		provenanceGeneratedByUrl = "";
		
		database = "";
		databaseUrl = "";
		dateDb = "";
		
		usedDataProv = new ArrayList<>();
		usedDataUrlProv = new ArrayList<>();
		
		content = new ArrayList<>();
		setAssertion(new ArrayList<>());
	}
	
	/**
	 * @return the platform
	 */
	public String getPlatform()
	{
		return platform;
	}
	
	/**
	 * @param platform
	 *            the platform to set
	 */
	public void setPlatform(String platform)
	{
		this.platform = platform;
	}
	
	/**
	 * @return
	 */
	public String getTimeCreation()
	{
		return timeCreation;
	}
	
	/**
	 * @param timeCreation
	 */
	public void setTimeCreation(String timeCreation)
	{
		this.timeCreation = timeCreation;
	}
	
	/**
	 * @return
	 */
	public String getSubject()
	{
		return subject;
	}
	
	/**
	 * @param subject
	 */
	public void setSubject(String subject)
	{
		this.subject = subject;
	}
	
	/**
	 * @return
	 */
	public String getRightHolder()
	{
		return rightHolder;
	}
	
	/**
	 * @param rightHolder
	 */
	public void setRightHolder(String rightHolder)
	{
		this.rightHolder = rightHolder;
	}
	
	/**
	 * @return
	 */
	public String getVersion()
	{
		return version;
	}
	
	/**
	 * @param version
	 */
	public void setVersion(String version)
	{
		this.version = version;
	}
	
	/**
	 * @return
	 */
	public int getNumberOfAuthors()
	{
		return numberOfAuthors;
	}
	
	/**
	 * @param numberOfAuthors
	 */
	public void setNumberOfAuthors(int numberOfAuthors)
	{
		this.numberOfAuthors = numberOfAuthors;
	}
	
	/**
	 * @return
	 */
	public int getNumberOfNpAuthors()
	{
		return numberOfNpAuthors;
	}
	
	/**
	 * @param numberOfNpAuthors
	 */
	public void setNumberOfNpAuthors(int numberOfNpAuthors)
	{
		this.numberOfNpAuthors = numberOfNpAuthors;
	}
	
	/**
	 * @return
	 */
	public int getNumberOfCreators()
	{
		return numberOfCreators;
	}
	
	/**
	 * @param numberOfCreators
	 */
	public void setNumberOfCreators(int numberOfCreators)
	{
		this.numberOfCreators = numberOfCreators;
	}
	
	/**
	 * @return the identifier
	 */
	public String getIdentifier()
	{
		return identifier;
	}
	
	/**
	 * @param identifier
	 *            the identifier to set
	 */
	public void setIdentifier(String identifier)
	{
		this.identifier = identifier;
	}
	
	/**
	 * @return the urlId
	 */
	public String getUrlId()
	{
		return urlId;
	}
	
	/**
	 * @param urlId
	 *            the urlId to set
	 */
	public void setUrlId(String urlId)
	{
		this.urlId = urlId;
	}
	
	/**
	 * @return the authors
	 */
	public List<Person> getAuthors()
	{
		return authors;
	}
	
	/**
	 * @param authors
	 *            the authors to set
	 */
	public void setAuthors(List<Person> authors)
	{
		this.authors = (ArrayList<Person>) authors;
		numberOfAuthors = authors.size();
	}
	
	/**
	 * @return the npauthors
	 */
	public List<Person> getNpauthors()
	{
		return npauthors;
	}
	
	/**
	 * @param npauthors
	 *            the npauthors to set
	 */
	public void setNpauthors(List<Person> npauthors)
	{
		this.npauthors = (ArrayList<Person>) npauthors;
		numberOfNpAuthors = npauthors.size();
	}
	
	/**
	 * @return the creators
	 */
	public List<Person> getCreators()
	{
		return creators;
	}
	
	/**
	 * @param creators
	 *            the creators to set
	 */
	public void setCreators(List<Person> creators)
	{
		this.creators = (ArrayList<Person>) creators;
		numberOfCreators = creators.size();
	}
	
	public void addAuthor(Person p)
	{
		authors.add(p);
		numberOfAuthors++;
	}
	
	public void addNpAuthor(Person p)
	{
		npauthors.add(p);
		numberOfNpAuthors++;
	}
	
	public void addCreator(Person p)
	{
		creators.add(p);
		numberOfCreators++;
	}
	
	/**
	 * @return the evidenceId
	 */
	public String getEvidenceId()
	{
		return evidenceId;
	}
	
	/**
	 * @param evidenceId
	 *            the evidenceId to set
	 */
	public void setEvidenceId(String evidenceId)
	{
		this.evidenceId = evidenceId;
	}
	
	/**
	 * @return the provenanceEvidence
	 */
	public String getProvenanceEvidence()
	{
		return provenanceEvidence;
	}
	
	/**
	 * @param provenanceEvidence
	 *            the provenanceEvidence to set
	 */
	public void setProvenanceEvidence(String provenanceEvidence)
	{
		// if (!(provenanceEvidence.equals("CURATED") ||
		// provenanceEvidence.equals("PREDICTED")
		// || provenanceEvidence.equals("LITERATURE")))
		// return;
		this.provenanceEvidence = provenanceEvidence;
	}
	
	/**
	 * @return the provenanceGeneratedBy
	 */
	public String getProvenanceGeneratedBy()
	{
		return provenanceGeneratedBy;
	}
	
	/**
	 * @param provenanceGeneratedBy
	 *            the provenanceGeneratedBy to set
	 */
	public void setProvenanceGeneratedBy(String provenanceGeneratedBy)
	{
		
//		if (!(provenanceGeneratedBy.equals("MANUAL ASSERTION") || provenanceGeneratedBy.equals("AUTOMATIC ASSERTION")))
//			return;
		this.provenanceGeneratedBy = provenanceGeneratedBy;
	}
	
	/**
	 * @return the database
	 */
	public String getDatabase()
	{
		return database;
	}
	
	/**
	 * @param database
	 *            the database to set
	 */
	public void setDatabase(String database)
	{
		this.database = database;
	}
	
	/**
	 * @return the dateDb
	 */
	public String getDateDb()
	{
		return dateDb;
	}
	
	/**
	 * @param dateDb
	 *            the dateDb to set
	 */
	public void setDateDb(String dateDb)
	{
		this.dateDb = dateDb;
	}
	
	/**
	 * @return
	 */
	public ArrayList<String> getPredicateDescription()
	{
		return predicateDescription;
	}
	
	/**
	 * @param predicateDescription
	 */
	public void setPredicateDescription(String description)
	{
		predicateDescription.add(description);
	}
	
	/**
	 * @return the assertionType
	 */
	public String getAssertionType()
	{
		return assertionType;
	}
	
	/**
	 * @param assertionType
	 *            the assertionType to set
	 */
	public void setAssertionType(String assertionType)
	{
		this.assertionType = assertionType;
	}
	
	/**
	 * @return the rightUrl
	 */
	public String getRightUrl()
	{
		return rightUrl;
	}
	
	/**
	 * @param rightUrl
	 *            the rightUrl to set
	 */
	public void setRightUrl(String rightUrl)
	{
		this.rightUrl = rightUrl;
	}
	
	/**
	 * @return the subjectUrl
	 */
	public String getSubjectUrl()
	{
		return subjectUrl;
	}
	
	/**
	 * @param subjectUrl
	 *            the subjectUrl to set
	 */
	public void setSubjectUrl(String subjectUrl)
	{
		this.subjectUrl = subjectUrl;
	}
	
	/**
	 * @return the rightHolderUrl
	 */
	public String getRightHolderUrl()
	{
		return rightHolderUrl;
	}
	
	/**
	 * @param rightHolderUrl
	 *            the rightHolderUrl to set
	 */
	public void setRightHolderUrl(String rightHolderUrl)
	{
		this.rightHolderUrl = rightHolderUrl;
	}
	
	/**
	 * @return the platformUrl
	 */
	public String getPlatformUrl()
	{
		return platformUrl;
	}
	
	/**
	 * @param platformUrl
	 *            the platformUrl to set
	 */
	public void setPlatformUrl(String platformUrl)
	{
		this.platformUrl = platformUrl;
	}
	
	/**
	 * @return the evidenceIdUrl
	 */
	public String getEvidenceIdUrl()
	{
		return evidenceIdUrl;
	}
	
	/**
	 * @param evidenceIdUrl
	 *            the evidenceIdUrl to set
	 */
	public void setEvidenceIdUrl(String evidenceIdUrl)
	{
		this.evidenceIdUrl = evidenceIdUrl;
	}
	
	/**
	 * @return the provenanceEvidenceUrl
	 */
	public String getProvenanceEvidenceUrl()
	{
		return provenanceEvidenceUrl;
	}
	
	/**
	 * @param provenanceEvidenceUrl
	 *            the provenanceEvidenceUrl to set
	 */
	public void setProvenanceEvidenceUrl(String provenanceEvidenceUrl)
	{
		this.provenanceEvidenceUrl = provenanceEvidenceUrl;
	}
	
	/**
	 * @return the databaseUrl
	 */
	public String getDatabaseUrl()
	{
		return databaseUrl;
	}
	
	/**
	 * @param databaseUrl
	 *            the databaseUrl to set
	 */
	public void setDatabaseUrl(String databaseUrl)
	{
		this.databaseUrl = databaseUrl;
	}
	
	/**
	 * @return the provenanceGeneratedByUrl
	 */
	public String getProvenanceGeneratedByUrl()
	{
		return provenanceGeneratedByUrl;
	}
	
	/**
	 * @param provenanceGeneratedByUrl
	 *            the provenanceGeneratedByUrl to set
	 */
	public void setProvenanceGeneratedByUrl(String provenanceGeneratedByUrl)
	{
		this.provenanceGeneratedByUrl = provenanceGeneratedByUrl;
	}
	
	
	public ArrayList<String> getUsedDataProv()
	{
		return usedDataProv;
	}

	public void setUsedDataProv(String usedDataProvS)
	{
		usedDataProv.add(usedDataProvS);
	}

	public ArrayList<String> getUsedDataUrlProv()
	{
		return usedDataUrlProv;
	}

	public void setUsedDataUrlProv(String usedDataUrlProvS)
	{
		usedDataUrlProv.add(usedDataUrlProvS);
	}
	
	
	public String getProvenenceEvidenceTypeUrl()
	{
		return provenenceEvidenceTypeUrl;
	}
	
	public void setProvenenceEvidenceTypeUrl(String provenenceEvidenceTypeUrl)
	{
		this.provenenceEvidenceTypeUrl = provenenceEvidenceTypeUrl;
	}
	
	public String getProvenenceEvidenceComment()
	{
		return provenenceEvidenceComment;
	}
	
	public void setProvenenceEvidenceComment(String provenenceEvidenceComment)
	{
		this.provenenceEvidenceComment = provenenceEvidenceComment;
	}
	
	public String getProvenenceEvidenceLabel()
	{
		return provenenceEvidenceLabel;
	}
	
	public void setProvenenceEvidenceLabel(String provenenceEvidenceLabel)
	{
		this.provenenceEvidenceLabel = provenenceEvidenceLabel;
	}
	
	public ArrayList<DescriptionTopic> getContent()
	{
		return content;
	}

	public void setContent(ArrayList<DescriptionTopic> content)
	{
		this.content = content;
	}
	
	public void addDescriptionTopic(DescriptionTopic dt)
	{
		this.content.add(dt);
	}
	

	public String toString()
	{
		StringBuilder authorsString = new StringBuilder();
		StringBuilder authorsNpString = new StringBuilder();
		StringBuilder creatorsString = new StringBuilder();
		StringBuilder usedDataProvS = new StringBuilder();
		StringBuilder desctopic = new StringBuilder();
		StringBuilder asser = new StringBuilder();
		
		for (Person p : authors)
			authorsString.append(p.toString() + '\n');
		for (Person p : npauthors)
			authorsNpString.append(p.toString() + '\n');
		for (Person p : creators)
			creatorsString.append(p.toString() + '\n');
		
		for (String s : usedDataProv)
			creatorsString.append(s+ '\n');
		for (DescriptionTopic d : content)
			desctopic.append(d.toString()+ '\n');
		
		for(Assertion a : assertion )
			asser.append(a.toString()+ '\n');
		
		return "NpDisgenetHumReadable\n " + "[identifier = " + identifier + ",\n urlId = " + urlId + ",\n timeCreation = " + timeCreation
				+ ",\n subject = " + subject + ",\n rightHolder = " + rightHolder + ",\n platform = " + platform
				+ ",\n version = " + version + ",\n assertionType = " + assertionType + ",\n numberOfAuthors = " + numberOfAuthors + "\n numberOfNpAuthors="
				+ numberOfNpAuthors + "\n numberOfCreators=" + numberOfCreators + "\nauthors = \n" + authorsString
				+ "np-authors = \n" + authorsNpString + "creators = \n" + creatorsString + "\n\n predicateDescription="
				+ predicateDescription + "\n provenenceEvidenceComment = " + provenenceEvidenceComment +  "\n evidenceId = " + evidenceId + "\n provenanceEvidence = " + provenanceEvidence
				+ "\n provenanceGeneratedBy=" + provenanceGeneratedBy + "\n database = " + database + "\n dateDb = "
				+ dateDb + "\n usedDataProv = " + usedDataProvS + "\n\n Description Topic" + desctopic.toString()+ "\n\n Assertion list" + asser.toString()+ "]";
	}

	public String getPredicateDescriptionString()
	{
		StringBuilder bld = new StringBuilder();
		for(String s: this.predicateDescription)
			bld.append(s+"\n");
		
		return bld.toString();
	}

	public String getShortId()
	{
		return shortId;
	}

	public void setShortId(String shortId)
	{
		this.shortId = shortId;
	}

	public ArrayList<Assertion> getAssertion()
	{
		return assertion;
	}

	public void setAssertion(ArrayList<Assertion> assertion)
	{
		this.assertion = assertion;
	}
	
	
}
