/**
 * 
 */
package it.unipd.dei.nanop.threads;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import org.json.JSONException;
import org.nanopub.MalformedNanopubException;
import org.nanopub.Nanopub;
import org.nanopub.NanopubImpl;
import org.openrdf.OpenRDFException;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.unipd.dei.nanocitation.metadata.AuthorsExtractor;
import it.unipd.dei.nanocitation.metadata.types.MetadataContainer;
import it.unipd.dei.nanocitation.metadata.types.Person;
import it.unipd.dei.nanocitation.text_snippet.ExtractTextSnippetInfo;
import it.unipd.dei.nanocitation.util.AuthorsNpExtractor;
import it.unipd.dei.nanop.statisticsdb.ManagerNpDB;


/**
 * @author erika
 *
 */
public class ExtractingThread extends Thread
{
	private int					id;
	private int					startNum;
	private int					endNum;
	private String				pathNpDir;

	private static final Logger	LOGGER	= LoggerFactory.getLogger(ExtractTextSnippetInfo.class);

	/**
	 * @param id
	 * @param startNum
	 * @param endNum
	 * @param pathNpDir
	 */
	public ExtractingThread(int id, int startNum, int endNum, String pathNpDir)
	{
		this.startNum = startNum;
		this.endNum = endNum;
		this.id = id;
		this.pathNpDir = pathNpDir;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run()
	{
		long	startTime;
		long	endTime;

		// start to count time to run
		startTime = System.currentTimeMillis();

		ManagerNpDB manDB = new ManagerNpDB();

		for (int i = startNum; i <= endNum; ++i)
		{

			System.out.println("[Thread " + id + "---------- Processing in file number " + i + " ----------");

			File fileTemp = new File(getDirPath(i));
			if (!fileTemp.exists())
				return;

			MetadataContainer ass = getInfo(fileTemp);

			if (ass == null)
				break;

			manDB.openManager();
			manDB.insertNewNp(ass);
			manDB.closeManager();

		}

		endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out
				.println("Thread " + id + " Total running time = " + Math.round(totalTime / (1000d * 60)) + " minutes");
	}

	/**
	 * @param fileTemp
	 * @return
	 */
	public MetadataContainer getInfo(File fileTemp)
	{

		if (fileTemp == null)
			return null;

		MetadataContainer ret = new MetadataContainer();

		try
		{
			Nanopub	np		= new NanopubImpl(fileTemp);

			// np identificator
			URI		npUri	= np.getUri();
			ret.setIdentifier(npUri.toString());

			Set<Statement>		prov	= np.getProvenance();

			AuthorsExtractor	authEx;
			for (Statement u : prov)
			{
				if (u.getPredicate().toString().equals("http://semanticscience.org/resource/SIO_000772")) // SIO_000772
																											 // =
																											 // hasevidence
				{

					ret.setEvidenceId(u.getObject().toString());
					if (u.getObject().toString().contains("pubmed"))
					{

						authEx = new AuthorsExtractor(u.getObject().toString());
						try
						{
							// extract from PubMed
							authEx.extrNumberAuthors();
						} catch (JSONException e)
						{
							System.err.println(ret.getIdentifier() + " evidence id " + ret.getEvidenceId());
						}

						ret.setAuthors(authEx.getAuthList());

					}
				}

				// controllare il tipo di evidence della nanopublication
				else if (u.getPredicate().toString().equals("http://purl.org/ontology/wi/core#evidence"))
				{
					int firstCharacter = u.getObject().toString().lastIndexOf('/') > u.getObject().toString()
							.lastIndexOf('#') ? u.getObject().toString().lastIndexOf('/') + 1
									: u.getObject().toString().lastIndexOf('#') + 1;

					ret.setProvenanceEvidence(u.getObject().toString().substring(firstCharacter));

				} else if (u.getPredicate().toString().equals("http://www.w3.org/ns/prov#wasDerivedFrom"))
				{
					int firstCharacter = u.getObject().toString().lastIndexOf('/') > u.getObject().toString()
							.lastIndexOf('#') ? u.getObject().toString().lastIndexOf('/') + 1
									: u.getObject().toString().lastIndexOf('#') + 1;

					ret.setDatabase(u.getObject().toString().substring(firstCharacter));

				}

				else if (u.getPredicate().toString().equals("http://www.w3.org/ns/prov#wasGeneratedBy"))
				{
					int		firstCharacter	= u.getObject().toString().lastIndexOf('/') > u.getObject().toString()
							.lastIndexOf('#') ? u.getObject().toString().lastIndexOf('/') + 1
									: u.getObject().toString().lastIndexOf('#') + 1;
					String	provString;
					if (u.getObject().toString().substring(firstCharacter).equals("ECO_0000218"))
						provString = "Manual Assertion";
					else if (u.getObject().toString().substring(firstCharacter).equals("ECO_0000203"))
						provString = "Automatic Assertion";
					else
						provString = u.getObject().toString().substring(firstCharacter);
					ret.setProvenanceGeneratedBy(provString);

				}

				else if (u.getPredicate().toString().equals("http://purl.org/pav/importedOn"))
				{
					int	firstCharacter	= u.getObject().toString().indexOf('\"') + 1;
					int	lastCharacter	= u.getObject().toString().lastIndexOf('\"');

					ret.setDateDb(u.getObject().toString().substring(firstCharacter, lastCharacter));

				} else if (u.getPredicate().toString().equals("http://purl.org/dc/terms/description"))
				{

					ret.setPredicateDescription(u.getObject().toString());

				}
			}

			AuthorsNpExtractor	anpe	= new AuthorsNpExtractor();
			ArrayList<Person>	npaut	= (ArrayList<Person>) anpe.getNpAuthors(np);
			ArrayList<Person>	creat	= (ArrayList<Person>) anpe.getCreator(np);

			ret.setNpauthors(npaut);
			ret.setCreators(creat);

		} catch (MalformedNanopubException | OpenRDFException | IOException e)
		{
			LOGGER.warn("Error in getInfo\n" + e.getClass().getName() + ": " + e.getMessage());
		}

		return ret;
	}

	/**
	 * @param n
	 * @return
	 */
	private String getDirPath(int n)
	{
		String	trig	= ".trig";
		String	ret		= pathNpDir;
		if (n > 1000000)
		{
			File dir = new File(pathNpDir + "/1000000");
			dir.mkdirs();
			ret = ret + "/1000000/nanopublications_v4.0.0.0_" + n + trig;
		} else if (n > 900000)
		{
			File dir = new File(pathNpDir + "/900000");
			dir.mkdirs();
			ret = ret + "/900000/nanopublications_v4.0.0.0_" + n + trig;
		} else if (n > 800000)
		{
			File dir = new File(pathNpDir + "/800000");
			dir.mkdirs();
			ret = ret + "/800000/nanopublications_v4.0.0.0_" + n + trig;
		} else if (n > 700000)
		{
			File dir = new File(pathNpDir + "/700000");
			dir.mkdirs();
			ret = ret + "/700000/nanopublications_v4.0.0.0_" + n + trig;
		} else if (n > 600000)
		{
			File dir = new File(pathNpDir + "/600000");
			dir.mkdirs();
			ret = ret + "/600000/nanopublications_v4.0.0.0_" + n + trig;
		} else if (n > 500000)
		{
			File dir = new File(pathNpDir + "/500000");
			dir.mkdirs();
			ret = ret + "/500000/nanopublications_v4.0.0.0_" + n + trig;
		} else if (n > 400000)
		{
			File dir = new File(pathNpDir + "/400000");
			dir.mkdirs();
			ret = ret + "/400000/nanopublications_v4.0.0.0_" + n + trig;
		} else if (n > 300000)
		{
			File dir = new File(pathNpDir + "/300000");
			dir.mkdirs();
			ret = ret + "/300000/nanopublications_v4.0.0.0_" + n + trig;
		} else if (n > 200000)
		{
			File dir = new File(pathNpDir + "/200000");
			dir.mkdirs();
			ret = ret + "/200000/nanopublications_v4.0.0.0_" + n + trig;
		} else if (n > 100000)
		{
			File dir = new File(pathNpDir + "/100000");
			dir.mkdirs();
			ret = ret + "/100000/nanopublications_v4.0.0.0_" + n + trig;
		} else
		{
			File dir = new File(pathNpDir + "/1000");
			dir.mkdirs();
			ret = ret + "/1000/nanopublications_v4.0.0.0_" + n + trig;
		}
		return ret;
	}

}
