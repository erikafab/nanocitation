/**
 * 
 */
package it.unipd.dei.nanocitation.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;
import org.nanopub.Nanopub;
import org.nanopub.SimpleCreatorPattern;
import org.openrdf.model.URI;

import it.unipd.dei.nanocitation.metadata.types.Person;
import it.unipd.dei.nanop.statisticsdb.ManagerNpDB;


/**
 * @author erika
 *
 */
@SuppressWarnings("static-access")
public class AuthorsNpExtractor
{

	private ArrayList<Person>	authNpList;
	private ArrayList<Person>	creatorList;

	public AuthorsNpExtractor()
	{
		authNpList = new ArrayList<>();
		creatorList = new ArrayList<>();
	}

	public List<Person> getAuthList()
	{
		return authNpList;
	}

	public List<Person> getCreatorList()
	{
		return creatorList;
	}

	public List<Person> getNpAuthors(Nanopub np)
	{
		SimpleCreatorPattern	scp		= new SimpleCreatorPattern();
		Set<URI>				setAuth	= scp.getAuthors(np);

		for (URI u : setAuth)
		{
			String		orcidId	= u.toString().substring(u.toString().lastIndexOf('/') + 1);

			ManagerNpDB	mdb		= new ManagerNpDB();
			mdb.openManager();
			ArrayList<String> infoAuth = (ArrayList<String>) mdb.checkPersonAlreadyInDB(orcidId);
			mdb.closeManager();
			if (!infoAuth.isEmpty())
			{
				Person a = new Person();
				a.setOrcidId(orcidId);
				a.setName(infoAuth.get(0));
				a.setSurname(infoAuth.get(1));
				authNpList.add(a);

			} else
				orcidToName(orcidId, 1);

		}

		return authNpList;
	}

	public List<Person> getCreator(Nanopub np)
	{
		SimpleCreatorPattern	scp		= new SimpleCreatorPattern();
		Set<URI>				setCrea	= scp.getCreators(np);

		for (URI u : setCrea)
		{
			String		orcidId	= u.toString().substring(u.toString().lastIndexOf('/') + 1);
			// DA CONTROLLARE CHE SIA ORCID!
			ManagerNpDB	mdb		= new ManagerNpDB();
			mdb.openManager();
			ArrayList<String> infoAuth = (ArrayList<String>) mdb.checkPersonAlreadyInDB(orcidId);
			mdb.closeManager();
			if (!infoAuth.isEmpty())
			{
				Person a = new Person();
				a.setOrcidId(orcidId);
				a.setName(infoAuth.get(0));
				a.setSurname(infoAuth.get(1));
				creatorList.add(a);

			} else
				orcidToName(orcidId, 2);

		}

		return creatorList;
	}

	private Person orcidToName(String orcidId, int flag)
	{

		JSONObject data = null;
		try
		{
			HttpURLConnection conn = (HttpURLConnection) (new URL(
					"https://pub.orcid.org/v3.0_rc1/" + orcidId + "/record")).openConnection();

			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);
			conn.setDoInput(true);

			conn.setRequestMethod("GET");

			BufferedReader	reader	= new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String			out		= "";
			for (String line; (line = reader.readLine()) != null;)
			{
				out = out + line;
			}
			reader.close();

			data = new JSONObject(out);

			// System.out.println(data.toString(2));

		} catch (ProtocolException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		data = data.getJSONObject("person").getJSONObject("name");
		// System.out.println(data.toString(3));

		String	name	= data.getJSONObject("given-names").getString("value").toLowerCase();
		String	surname	= data.getJSONObject("family-name").getString("value").toLowerCase();

		Person	a		= new Person();
		a.setOrcidId(orcidId);
		a.setName(name);
		a.setSurname(surname);
		// System.out.println(a.toString());

		if (flag == 1)
			authNpList.add(a);
		else if (flag == 2)
			creatorList.add(a);

		return a;

	}

}
