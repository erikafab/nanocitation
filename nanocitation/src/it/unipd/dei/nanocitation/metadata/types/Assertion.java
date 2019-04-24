/**
 * 
 */
package it.unipd.dei.nanocitation.metadata.types;


import java.net.URI;


/**
 * @author erika
 *
 */
public class Assertion
{

	private URI		subjectURI;
	private String	Subject;
	private URI		predicateURI;
	private String	Predicate;
	private URI		objectURI;
	private String	Object;
	private String	typeAss;

	public URI getSubjectURI()
	{
		return subjectURI;
	}

	public void setSubjectURI(URI subjectURI)
	{
		this.subjectURI = subjectURI;
	}

	public String getSubject()
	{
		return Subject;
	}

	public void setSubject(String subject)
	{
		Subject = subject;
	}

	public URI getPredicateURI()
	{
		return predicateURI;
	}

	public void setPredicateURI(URI predicateURI)
	{
		this.predicateURI = predicateURI;
	}

	public String getPredicate()
	{
		return Predicate;
	}

	public void setPredicate(String predicate)
	{
		Predicate = predicate;
	}

	public URI getObjectURI()
	{
		return objectURI;
	}

	public void setObjectURI(URI objectURI)
	{
		this.objectURI = objectURI;
	}

	public String getObject()
	{
		return Object;
	}

	public void setObject(String object)
	{
		Object = object;
	}

	public String getTypeAss()
	{
		return typeAss;
	}

	public void setTypeAss(String typeAss)
	{
		this.typeAss = typeAss;
	}

	@Override
	public String toString() {
		return "Assertion [subjectURI=" + subjectURI + ", Subject=" + Subject + ", predicateURI=" + predicateURI
				+ ", Predicate=" + Predicate + ", objectURI=" + objectURI + ", Object=" + Object + ", typeAss="
				+ typeAss + "]";
	}

	
	
}
