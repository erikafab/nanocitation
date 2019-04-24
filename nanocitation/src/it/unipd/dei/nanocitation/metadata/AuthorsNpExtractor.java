/**
 * parser stax
 */
package it.unipd.dei.nanocitation.metadata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;
import org.nanopub.Nanopub;
import org.nanopub.SimpleCreatorPattern;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;

import it.unipd.dei.nanocitation.metadata.types.Person;

/**
 * @author erika
 *
 */
@SuppressWarnings("static-access")
public class AuthorsNpExtractor
{
	
	private ArrayList<Person> authNpList;
	private ArrayList<Person> creatorList;
	
	public AuthorsNpExtractor()
	{
		authNpList = new ArrayList<Person>();
		creatorList = new ArrayList<Person>();
	}
	
	public AuthorsNpExtractor(ArrayList<Person> NpList, int flag)
	{
		if (flag == 1)
		{
			this.authNpList = NpList;
			creatorList = new ArrayList<Person>();
		}
		else if (flag == 2)
		{
			this.creatorList = NpList;
			authNpList = new ArrayList<Person>();
		}
		else
		{
			authNpList = new ArrayList<Person>();
			creatorList = new ArrayList<Person>();
		}
		
	}
	
	public ArrayList<Person> getAuthList()
	{
		return authNpList;
	}
	
	public ArrayList<Person> getCreatorList()
	{
		return creatorList;
	}
	
	public ArrayList<Person> getNpAuthors(Nanopub np)
	{
		
		SimpleCreatorPattern scp = new SimpleCreatorPattern();
		Set<URI> setAuth = scp.getAuthors(np);
		
		if (!setAuth.isEmpty())
			for (URI u : setAuth)
			{
				if (!u.toString().contains("orcid"))
					continue;
				String orcidId = u.toString().substring(u.toString().lastIndexOf("/") + 1);
				
				Person p = orcidToName(orcidId, 1);
				
				p.setUrlId(u.toString());
				addPersonInList(p, 1);
				
			}
		for (Statement statement : np.getPubinfo())
		{
			
			if (statement.getPredicate().toString().contains("purl.org/dc/terms/contributor"))
			{
				String auth = statement.getObject().toString();
				
				if (auth.contains("//orcid.org/"))
				{
					Person p = orcidToName(auth.substring(auth.lastIndexOf("/") + 1), 1);
					p.setUrlId(auth);
					addPersonInList(p, 1);
					
				}
				else if (auth.contains("www.researcherid.com/rid/"))
				{
					
					Person p = new Person();
					p.setUrlId(auth);
					addPersonInList(p, 1);
					
					// Person p = researcherIdToName(auth, 1);
					// if (p == null)
					// continue;
					// p.setUrlId(auth);
					// addPersonInList(p, 1);
					
					// researcherIdToName(auth, 1);
					
				}
			}
			else if (statement.getPredicate().toString().contains("/pav/authoredBy")
					&& statement.getObject().toString().contains("^^"))
			{
				String auth = statement.getObject().toString();
				auth = auth.substring(1, auth.indexOf("^^")-1);


				Person p = new Person();
				p.setName(auth);
				addPersonInList(p, 1);
			}
		}
		
		return authNpList;
	}
	
	public List<Person> getCreator(Nanopub np)
	{
		SimpleCreatorPattern scp = new SimpleCreatorPattern();
		Set<URI> setCrea = scp.getCreators(np);
		
		if (!setCrea.isEmpty())
			for (URI u : setCrea)
			{
				if (!u.toString().contains("orcid"))
					continue;
				String orcidId = u.toString().substring(u.toString().lastIndexOf("/") + 1);
				Person p = orcidToName(orcidId, 2);
				
				p.setUrlId(u.toString());
				addPersonInList(p, 2);
				
			}
		for (Statement statement : np.getPubinfo())
		{
			
			if (statement.getPredicate().toString().contains("/pav/createdBy")
					&& statement.getObject().toString().contains("^^"))
			{
				String creat = statement.getObject().toString();
				creat = creat.substring(1, creat.indexOf("^^")-1);
				
				String[] name = creat.split(" ");
				
				Person p = new Person();
				if (name.length >= 1)
					p.setName(name[0]);
				if (name.length >= 2)
				{
					String sur = "";
					for (int i = 1; i < name.length; i++)
						sur = sur + " " + name[i];
					p.setSurname(sur);
					
				}
				addPersonInList(p, 2);
			}
			
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
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String out = "";
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
		
		String name = data.getJSONObject("given-names").getString("value").toLowerCase();
		String surname = data.getJSONObject("family-name").getString("value").toLowerCase();
		
		Person a = new Person();
		a.setOrcidId(orcidId);
		a.setName(name);
		a.setSurname(surname);
		// System.out.println(a.toString());
		
		return a;
		
	}
	
	@SuppressWarnings("unused")
	private Person researcherIdToName(String url, int i)
	{
		String inLine;
		String pubMedPage = "";
		URL source;
		try
		{
			source = new URL(url);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(source.openStream()));
			
			while ((inLine = in.readLine()) != null)
				pubMedPage += inLine;
			in.close();
			
			String flag = "<div class=\"profileName\">";
			int firstIndex = pubMedPage.indexOf(flag);
			if (firstIndex == -1)
				return null;
			int lastIndex = pubMedPage.indexOf("</div>", firstIndex);
			String authPortion = pubMedPage.substring(firstIndex + flag.length(), lastIndex);
		
			
			authPortion = authPortion.replace("\n", "");
			
			
			String[] s = authPortion.split(",");
			
			if (s.length == 2)
			{
				Person p = new Person();
				p.setName(s[1]);
				p.setSurname(s[0]);
			}
			
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
		
	}
	
	private void addPersonInList(Person a, int flag)
	{
		if (flag == 1)
			authNpList.add(a);
		else if (flag == 2)
			creatorList.add(a);
	}
	
}