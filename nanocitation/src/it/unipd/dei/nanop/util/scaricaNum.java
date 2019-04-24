package it.unipd.dei.nanop.util;

import java.io.File;
import java.io.IOException;

import org.nanopub.MalformedNanopubException;
import org.nanopub.Nanopub;
import org.nanopub.NanopubImpl;
import org.openrdf.OpenRDFException;

// scarica numero triple, numero nanopub, numero curators, numero manual
public class scaricaNum
{
	
	static String pathName = "npDISGeNET";
	
	public static void main(String[] args)
	{
		
		int triplecount = 0;
		for (int i = 1; i <= 1414902; ++i)
		{
			String fileName = getDirPath(i);
			
			try
			{
				System.out.println("NP num " + i);
				File f = new File(fileName);
				Nanopub np = new NanopubImpl(f);
				triplecount += np.getTripleCount();
				
				System.out.println("numero triple " + triplecount);
				
			} catch (MalformedNanopubException | OpenRDFException | IOException e)
			{
				e.printStackTrace();
			}
			
		}
	}
	
	private static String getDirPath(int n)
	{
		String ret = pathName;
		if (n > 1000000)
		{
			File dir = new File(pathName + "/1000000");
			dir.mkdirs();
			ret = ret + "/1000000/nanopublications_v4.0.0.0_" + n + ".trig";
		}
		else if (n > 900000)
		{
			File dir = new File(pathName + "/900000");
			dir.mkdirs();
			ret = ret + "/900000/nanopublications_v4.0.0.0_" + n + ".trig";
		}
		else if (n > 800000)
		{
			File dir = new File(pathName + "/800000");
			dir.mkdirs();
			ret = ret + "/800000/nanopublications_v4.0.0.0_" + n + ".trig";
		}
		else if (n > 700000)
		{
			File dir = new File(pathName + "/700000");
			dir.mkdirs();
			ret = ret + "/700000/nanopublications_v4.0.0.0_" + n + ".trig";
		}
		else if (n > 600000)
		{
			File dir = new File(pathName + "/600000");
			dir.mkdirs();
			ret = ret + "/600000/nanopublications_v4.0.0.0_" + n + ".trig";
		}
		else if (n > 500000)
		{
			File dir = new File(pathName + "/500000");
			dir.mkdirs();
			ret = ret + "/500000/nanopublications_v4.0.0.0_" + n + ".trig";
		}
		else if (n > 400000)
		{
			File dir = new File(pathName + "/400000");
			dir.mkdirs();
			ret = ret + "/400000/nanopublications_v4.0.0.0_" + n + ".trig";
		}
		else if (n > 300000)
		{
			File dir = new File(pathName + "/300000");
			dir.mkdirs();
			ret = ret + "/300000/nanopublications_v4.0.0.0_" + n + ".trig";
		}
		else if (n > 200000)
		{
			File dir = new File(pathName + "/200000");
			dir.mkdirs();
			ret = ret + "/200000/nanopublications_v4.0.0.0_" + n + ".trig";
		}
		else if (n > 100000)
		{
			File dir = new File(pathName + "/100000");
			dir.mkdirs();
			ret = ret + "/100000/nanopublications_v4.0.0.0_" + n + ".trig";
		}
		else
		{
			File dir = new File(pathName + "/1000");
			dir.mkdirs();
			ret = ret + "/1000/nanopublications_v4.0.0.0_" + n + ".trig";
		}
		return ret;
	}
}
