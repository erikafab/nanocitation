package it.unipd.dei.nanop.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.nanopub.MalformedNanopubException;
import org.nanopub.Nanopub;
import org.nanopub.NanopubImpl;
import org.openrdf.OpenRDFException;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;

import it.unipd.dei.nanocitation.metadata.AuthorsNpExtractor;
import it.unipd.dei.nanocitation.metadata.DerefAndEnrich;
import it.unipd.dei.nanocitation.metadata.types.MetadataContainer;
import it.unipd.dei.nanocitation.metadata.types.Person;
import it.unipd.dei.nanocitation.util.NoSupportedNpException;

public class prova
{
	
	public static void main(String[] args)
			throws MalformedURLException, MalformedNanopubException, OpenRDFException, IOException
	{
		
		Nanopub np = new NanopubImpl(
				new URL("http://rdf.disgenet.org/nanopub-server/RA4v808oJhl24_1NuqMxUff1ADPZ8dUTiqoRg2d5k6d-M"));
		
		System.out.println("Creation Time");
		GregorianCalendar gc = (GregorianCalendar) np.getCreationTime();
		if (gc != null)
			System.out.println(
					gc.get(Calendar.YEAR) + "-" + gc.get(Calendar.MONTH) + "-" + gc.get(Calendar.DAY_OF_MONTH));
		
		System.out.println("Triple count");
		System.out.println(np.getTripleCount());
		
		System.out.println("Creators");
		for (URI u : np.getCreators())
		{
			System.out.println(u.stringValue());
			System.out.println("____________");
		}
		
		System.out.println("Authors");
		for (URI u : np.getAuthors())
		{
			System.out.println(u.stringValue());
		}
		
		System.out.println("Head: ");
		for (Statement statement : np.getHead())
		{
			System.out.println(
					statement.getSubject() + "     " + statement.getPredicate() + "     " + statement.getObject());
			System.out.println("____________");
			
		}
		
		System.out.println("Pub Info: ");
		for (Statement statement : np.getPubinfo())
		{
			System.out.println(
					statement.getSubject() + "     " + statement.getPredicate() + "     " + statement.getObject());
			System.out.println("____________");
			
		}
		
		System.out.println("Assertions: ");
		for (Statement statement : np.getAssertion())
		{
			System.out.println(
					statement.getSubject() + "     " + statement.getPredicate() + "     " + statement.getObject());
			System.out.println("____________");
		}
		
		System.out.println("Provenance: ");
		for (Statement statement : np.getProvenance())
		{
			System.out.println(
					statement.getSubject() + "     " + statement.getPredicate() + "     " + statement.getObject());
			System.out.println("____________");
		}
		
		MetadataContainer ret = new MetadataContainer();
		
		AuthorsNpExtractor ae = new AuthorsNpExtractor();
		ae.getCreator(np);
		ae.getNpAuthors(np);
		
		System.out.println("Np auth");
		for (Person p : ae.getAuthList())
			System.out.println("Person " + p.toString());
		
		System.out.println("Np creat");
		for (Person p : ae.getCreatorList())
			System.out.println("Person " + p.toString());
		
		ret.setNpauthors(ae.getAuthList());
		ret.setCreators(ae.getCreatorList());
		
		URI npUri = np.getUri();
		
		ret.setIdentifier(npUri.toString());
		
		// LOGGER.info("\n\n\nText Snippet:\n");
		// LOGGER.info(textsnippet);
		
		DerefAndEnrich ed = new DerefAndEnrich("RA4v808oJhl24_1NuqMxUff1ADPZ8dUTiqoRg2d5k6d-M", Paths.get("resources/sio.owl").toFile());
		
		try
		{
			MetadataContainer meta = ed.getMeta();
			
			
			System.out.println(meta.toString());
		} catch (NoSupportedNpException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	@SuppressWarnings("unused")
	private static void print(String f)
	{
		File file = new File(f);
		
		try (BufferedReader br = new BufferedReader(new FileReader(file)))
		{
			String line = null;
			while ((line = br.readLine()) != null)
			{
				System.out.println(line);
			}
		} catch (FileNotFoundException e)
		{
			return;
			
		} catch (IOException e)
		{
			return;
		}
	}
	
}
