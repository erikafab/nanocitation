package it.unipd.dei.nanocitation.metadata.types;

public class Organisation
{
	private String orgName;

	public String getOrgName()
	{
		return orgName;
	}

	public void setOrgName(String orgName)
	{
		this.orgName = orgName;
	}

	@Override
	public String toString()
	{
		return "Organisation [orgName=" + orgName + "]";
	}
	
	
	
}
