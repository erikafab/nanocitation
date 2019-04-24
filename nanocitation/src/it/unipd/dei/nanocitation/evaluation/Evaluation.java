package it.unipd.dei.nanocitation.evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.nanopub.MalformedNanopubException;
import org.nanopub.Nanopub;
import org.nanopub.NanopubImpl;
import org.openrdf.OpenRDFException;

import it.unipd.dei.nanocitation.metadata.DerefAndEnrich;
import it.unipd.dei.nanocitation.metadata.types.MetadataContainer;
import it.unipd.dei.nanocitation.text_snippet.TextSnippetCreator;
import it.unipd.dei.nanocitation.util.NoSupportedNpException;

public class Evaluation
{
	private static String pathName = "npDISGeNET";
	
	private static int numExperiments = 100;
	
	private static ArrayList<Long> totalTimeForEachExp;
	private static ArrayList<Integer> textSnippetCharLength;
	private static ArrayList<Integer> textSnippetEmptyFields;
	private static ArrayList<Integer> metaEmptyFields;
	
	private static int unconclusionalExp;
	private static ArrayList<String> idUnconclusionalExp;
	
	public static void main(String[] args)
	{
		
		Logger logger = Logger.getLogger("MyLog");
		FileHandler fh;
		try
		{
			
			fh = new FileHandler("log.log");
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
			
			logger.info("Start Logging");
			
		} catch (SecurityException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
				
		totalTimeForEachExp = new ArrayList();
		textSnippetCharLength = new ArrayList();
		textSnippetEmptyFields = new ArrayList();
		metaEmptyFields = new ArrayList();
		
		unconclusionalExp = 0;
		idUnconclusionalExp = new ArrayList();
		
		File sio = new File("sio.owl");
		
		String id = "";
		Random random;
		int randomNumber;
		
		for (int i = 0; i < numExperiments; ++i)
		{
			// get randomly n num from 1414902
			
			random = new Random();
			randomNumber = random.nextInt(1414902 - 1) + 1;
			
			String fileName = getDirPath(randomNumber);
			// get the nanopub id associated to the nth nanopub
			
			File f = new File(fileName);
			try
			{
				Nanopub np = new NanopubImpl(f);
				id = np.getUri().toString();
			} catch (MalformedNanopubException | OpenRDFException | IOException e1)
			{
				e1.printStackTrace();
			}
			
			// start timing
			long startTime = System.nanoTime();
			System.out.println("Start experiment number " + i + " dis nanopub + " + id);
			logger.info("Start experiment number " + i + " dis nanopub + " + id); 
			
			// get metadata + text snippet
			DerefAndEnrich ed = new DerefAndEnrich(id, sio);
			MetadataContainer meta = new MetadataContainer();
			try
			{
				meta = ed.getMeta();
			} catch (IOException | NoSupportedNpException e)
			{
				System.out.println("UnconclusionalExp for " + id);
				idUnconclusionalExp.add(id);
				++unconclusionalExp;
				--i;
				logger.info("UnconclusionalExp for " + id);  
				continue;
				
			}
			// System.out.println(meta.toString());
			
			TextSnippetCreator tscreat = new TextSnippetCreator(meta);
			String textsnippet = tscreat.generateTextSnippet();
			
			// stop timing
			long endTime = System.nanoTime();
			
			System.out.println(textsnippet);
			
			long totalTime = endTime - startTime;
			totalTimeForEachExp.add(totalTime);
			System.out.println("Running time " + totalTime);
			logger.info("Running time " + totalTime); 
			
			int length = textsnippet.length();
			textSnippetCharLength.add(length);
			logger.info("TS length " + length);
			
			
			int emptyTS = countEmptyTextSnippetFields(meta);
			textSnippetEmptyFields.add(emptyTS);
			logger.info("EmptyTS " + emptyTS);
		}
		
		logger.info("Average Num");
		
		logger.info("Text Snippet average length"+averageList(textSnippetCharLength));
		
		logger.info("TS empty fields average"+averageListL(totalTimeForEachExp));
		logger.info("Meta empty fields average"+averageList(metaEmptyFields));
		logger.info("Total time average"+averageList(textSnippetEmptyFields)+" nanosec");
	}
	
	private static double averageList(ArrayList<Integer> a)
	{
		int sum = 0;
		for(Integer t : a)
			sum+=t;
		
		return (double) sum/numExperiments;
	}
	
	private static double averageListL(ArrayList<Long> a)
	{
		long sum = 0;
		for(Long i : a)
			sum+=i;
		
		return (double) sum/numExperiments;
	}
	
	
	
	private static int countEmptyTextSnippetFields(MetadataContainer meta)
	{
		int num = 0;
		if (meta.getCreators().isEmpty())
			++num;
		if (meta.getNpauthors().isEmpty())
			++num;
		if (meta.getTimeCreation().equals(""))
			++num;
		if (meta.getRightHolder().equals(""))
			++num;
		if (meta.getSubject().equals(""))
			++num;
		if (meta.getContent().isEmpty())
			++num;
		if (meta.getPlatform().equals(""))
			++num;
		if (meta.getVersion().equals(""))
			++num;
		if (meta.getAuthors().isEmpty())
			++num;
		
		return num;
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
