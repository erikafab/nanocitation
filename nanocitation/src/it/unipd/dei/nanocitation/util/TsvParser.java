package it.unipd.dei.nanocitation.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TsvParser
{
	
	public TsvParser()
	{
		super();
	}
	
	public Map<String, String> parseTsvFile(File file)
	{
		Map<String, String> ret = new HashMap<String, String>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(file)))
		{
			String[] label;
			String[] value;
			
			String line;
			if ((line = br.readLine()) != null)
				label = line.split("\t");
			else
				return ret;
			if ((line = br.readLine()) != null)
				value = line.split("\t");
			else
				return ret;
			
			String lab;
			String val;
			for (int i = 0; i < label.length; ++i)
			{
				lab = label[i];
				if (i < value.length)
					val = value[i];
				else
					val = "";
				ret.put(lab, val);
			}
			
		} catch (FileNotFoundException e)
		{
			
		} catch (IOException e)
		{
			
		}
		
		file.delete();
		
		return ret;
	}
	
	private void printStringArray(String[] s)
	{
		for (String st : s)
			System.out.println(st);
		
	}
	
	public Map<String, String> parseTsvUrl(InputStream openStream)
	{
		Map<String, String> ret = new HashMap<String, String>();
		Reader in = new InputStreamReader(openStream);
		
		try (BufferedReader br = new BufferedReader(in))
		{
			String[] label;
			String[] value;
			
			String line;
			if ((line = br.readLine()) != null)
				label = line.split("\t");
			else
				return ret;
			if ((line = br.readLine()) != null)
				value = line.split("\t");
			else
				return ret;
			
			String lab;
			String val;
			for (int i = 0; i < label.length; ++i)
			{
				lab = label[i];
				if (i < value.length)
					val = value[i];
				else
					val = "";
				ret.put(lab, val);
			}
			
		} catch (FileNotFoundException e)
		{
			
		} catch (IOException e)
		{
			
		}
		return ret;
	}
	
}
