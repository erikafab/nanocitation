/**
 * 
 */
package it.unipd.dei.nanocitation.metadata.types;

import org.apache.commons.lang.WordUtils;

/**
 * @author erika
 *
 */
public class Person
{
	
	private String given_name;
	private String family_name;
	private String orcidId;
	private String urlId;
	private boolean mainAuthor;
	
	private boolean initialNameOnly = false;
	
	/**
	 * 
	 */
	public Person()
	{
		super();
	}
	
	/**
	 * @return the given_name
	 */
	public String getName()
	{
		return given_name;
	}
	
	/**
	 * @param given_name
	 *            the given_name to set
	 */
	public void setName(String given_name)
	{
		this.given_name = given_name;
	}
	
	/**
	 * @return the family_name
	 */
	public String getSurname()
	{
		return family_name;
	}
	
	/**
	 * @param family_name
	 *            the family_name to set
	 */
	public void setSurname(String family_name)
	{
		this.family_name = family_name;
	}
	
	/**
	 * @return the orcidId
	 */
	public String getOrcidId()
	{
		return orcidId;
	}
	
	/**
	 * @param orcidId
	 *            the orcidId to set
	 */
	public void setOrcidId(String orcidId)
	{
		this.orcidId = orcidId;
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
	 * @return the main
	 */
	public boolean isMainAuthor()
	{
		return mainAuthor;
	}
	
	/**
	 * @param main
	 *            the main to set
	 */
	public void setMain(boolean main)
	{
		this.mainAuthor = main;
	}
	
	public String getNameAndSurnameCapitalized()
	{
		
		if (this.getName() == null && this.getSurname() == null && this.getUrlId() != null)
			return this.getUrlId();
		
		StringBuilder bld = new StringBuilder();
		if (this.getName() != null)
			bld.append(WordUtils.capitalize(this.getName())+" ");
		if (this.getSurname() != null)
			bld.append(WordUtils.capitalize(this.getSurname()));
			
		//
		// String[] temp = this.getName().split("\\s");
		// for (String t : temp)
		// {
		// if (this.initialNameOnly)
		// {
		// bld.append(t.toUpperCase());
		// bld.append(" ");
		// continue;
		// }
		// bld.append(t.substring(0, 1).toUpperCase() +
		// t.substring(1).toLowerCase());
		// bld.append(" ");
		// }
		//
		// if (this.getSurname() == null)
		// return bld.toString();
		// temp = this.getSurname().split("\\s");
		//
		// for (int i = 0; i < temp.length; ++i)
		// {
		// bld.append(temp[i].substring(0, 1).toUpperCase() +
		// temp[i].substring(1).toLowerCase());
		// if (i < temp.length - 1)
		// bld.append(" ");
		// }
		return bld.toString();
	}
	
	public boolean isInitialNameOnly()
	{
		return initialNameOnly;
	}
	
	public void setInitialNameOnly()
	{
		this.initialNameOnly = true;
	}
	
	@Override
	public String toString()
	{
		return "Person [given_name=" + given_name + ", family_name=" + family_name + ", orcidId=" + orcidId + ", urlId=" + urlId + "]";
	}
	
}
