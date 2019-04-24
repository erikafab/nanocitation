package it.unipd.dei.nanocitation.metadata.types;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DescriptionTopic")
public class DescriptionTopic
{
	@XmlElement(name = "subject")
	private String subject;
	@XmlElementWrapper(name="assertion")
	@XmlList
	private ArrayList<String> assertion;
	
	public String getSubject()
	{
		return subject;
	}
	public void setSubject(String subject)
	{
		this.subject = subject;
	}
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
		
		if(subject!=null)
			bld.append("(" + subject + ") ");
		
		String a;
		for(int i=0; i<assertion.size(); ++i)
		{
			a = assertion.get(i);
			bld.append(a);
			if(i<assertion.size()-1)
				bld.append("; ");
		}
		return bld.toString();
	}

}
