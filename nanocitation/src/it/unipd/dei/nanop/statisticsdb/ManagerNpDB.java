/**
 * 
 */
package it.unipd.dei.nanop.statisticsdb;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.unipd.dei.nanocitation.metadata.types.MetadataContainer;
import it.unipd.dei.nanocitation.metadata.types.Person;
import it.unipd.dei.nanocitation.text_snippet.ExtractTextSnippetInfo;


/**
 * @author erika
 *
 */
public class ManagerNpDB
{

	/**
	 * path and name of the configuration file
	 */
	private static final String	PROPFILE				= "res/config.properties";

	private static final String	POSTGRESQLCLASSDRIVER	= "org.postgresql.Driver";

	private static final Logger	LOGGER					= LoggerFactory.getLogger(ExtractTextSnippetInfo.class);

	private String				connectDBUrl;
	private String				user;
	private String				password;

	private static String		sqlInsertPerson			= "INSERT INTO person(given_name, family_name, num, orcid_id) VALUES (?, ?, ?, ?)";
	private static String		sqlInsertAuthored		= "INSERT INTO authored (np_id, given_name, family_name, num) VALUES (?, ?, ?, ?)";
	private static String		sqlInsertCreator		= "INSERT INTO creator (np_id, given_name, family_name, num) VALUES (?, ?, ?, ?)";
	private static String		sqlInsertEvidAuth		= "INSERT INTO evidauthored (np_id, given_name, family_name, num) VALUES (?, ?, ?, ?)";

	/**
	 * Instance of {@code Connection} to connect with the database.
	 */
	private Connection			c;

	/**
	 * 
	 */
	public ManagerNpDB()
	{
		Properties prop = new Properties();

		try (InputStream input = new FileInputStream(PROPFILE))
		{
			prop.load(input);
			connectDBUrl = prop.getProperty("connectDBUrl");
			user = prop.getProperty("user");
			password = prop.getProperty("password");
		} catch (IOException e)
		{
			LOGGER.warn("Error in constructor\n" + e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * 
	 */
	public void openManager()
	{
		try
		{
			Class.forName(POSTGRESQLCLASSDRIVER);
			c = DriverManager.getConnection(connectDBUrl, user, password);
		} catch (Exception e)
		{
			LOGGER.warn("Error in openManager\n" + e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * Close database connection.
	 */
	public void closeManager()
	{
		try
		{
			c.close();
		} catch (SQLException e)
		{
			LOGGER.warn("Error in closeManager\n" + e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * @param npdhr
	 */
	public void insertNewNp(MetadataContainer npdhr)
	{

		String sql = "INSERT INTO nanopub( np_id, evid_id, prov_evidence, evid_description, generated_by, prov_db, prov_date, numauth, numevidauth, numcreator) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement pstmt = c.prepareStatement(sql))
		{

			String charnull = "\u0000";

			pstmt.setString(1, npdhr.getIdentifier());
			pstmt.setString(2, npdhr.getEvidenceId().replaceAll(charnull, ""));
			pstmt.setString(3, npdhr.getProvenanceEvidence().replaceAll(charnull, ""));
			//pstmt.setString(4, npdhr.getPredicateDescription().replaceAll(charnull, ""));
			pstmt.setString(5, npdhr.getProvenanceGeneratedBy().replaceAll(charnull, ""));
			pstmt.setString(6, npdhr.getDatabase().replaceAll(charnull, ""));
			pstmt.setString(7, npdhr.getDateDb().replaceAll(charnull, ""));

			pstmt.setInt(8, npdhr.getNumberOfNpAuthors());
			pstmt.setInt(9, npdhr.getNumberOfAuthors());
			pstmt.setInt(10, npdhr.getNumberOfCreators());

			pstmt.executeUpdate();

		} catch (SQLException e)
		{

			// LOGGER.log(Level.WARNING, "Error in insertNewNp\n" + e.getClass().getName() +
			// ": " + e.getMessage());

		}

		insertNpAuthorsInfo(npdhr.getIdentifier(), (ArrayList<Person>) npdhr.getNpauthors());
		insertNpCreatorsInfo(npdhr.getIdentifier(), (ArrayList<Person>) npdhr.getCreators());
		insertEvidenceAuthorsInfo(npdhr.getIdentifier(), (ArrayList<Person>) npdhr.getAuthors());

	}

	/**
	 * @param npId
	 * @param npauth
	 */
	public void insertNpAuthorsInfo(String npId, List<Person> npauth)
	{

		for (Person p : npauth)
		{

			try (PreparedStatement pstmt1 = c.prepareStatement(sqlInsertPerson))
			{

				pstmt1.setString(1, p.getName());
				pstmt1.setString(2, p.getSurname());
				pstmt1.setInt(3, 1);
				pstmt1.setString(4, p.getOrcidId());

				pstmt1.executeUpdate();

			} catch (SQLException e)
			{
				// LOGGER.log(Level.WARNING, "Error in insertNpAuthorsInfo\n" +
				// e.getClass().getName() + ": " + e.getMessage());
			}

			try (PreparedStatement pstmt1 = c.prepareStatement(sqlInsertAuthored))
			{
				pstmt1.setString(1, npId);
				pstmt1.setString(2, p.getName());
				pstmt1.setString(3, p.getSurname());
				pstmt1.setInt(4, 1);

				pstmt1.executeUpdate();

			} catch (SQLException e)
			{
				// LOGGER.log(Level.WARNING, "Error in insertNpAuthorsInfo\n" +
				// e.getClass().getName() + ": " + e.getMessage());
			}
		}
	}

	/**
	 * @param npId
	 * @param cre
	 */
	public void insertNpCreatorsInfo(String npId, List<Person> cre)
	{
		for (Person p : cre)
		{

			try (PreparedStatement pstmt1 = c.prepareStatement(sqlInsertPerson))
			{

				pstmt1.setString(1, p.getName());
				pstmt1.setString(2, p.getSurname());
				pstmt1.setInt(3, 1);
				pstmt1.setString(4, p.getOrcidId());

				pstmt1.executeUpdate();

			} catch (SQLException e)
			{
				// LOGGER.log(Level.WARNING, "Error in insertNpCreatorsInfo\n" +
				// e.getClass().getName() + ": " + e.getMessage());
			}

			try (PreparedStatement pstmt1 = c.prepareStatement(sqlInsertCreator))
			{
				pstmt1.setString(1, npId);
				pstmt1.setString(2, p.getName());
				pstmt1.setString(3, p.getSurname());
				pstmt1.setInt(4, 1);

				pstmt1.executeUpdate();

			} catch (SQLException e)
			{
				// LOGGER.log(Level.WARNING, "Error in insertNpCreatorsInfo\n" +
				// e.getClass().getName() + ": " + e.getMessage());
			}
		}
	}

	/**
	 * @param npId
	 * @param evidauth
	 */
	public void insertEvidenceAuthorsInfo(String npId, List<Person> evidauth)
	{
		for (Person p : evidauth)
		{

			try (PreparedStatement pstmt1 = c.prepareStatement(sqlInsertPerson))
			{

				pstmt1.setString(1, p.getName());
				pstmt1.setString(2, p.getSurname());
				pstmt1.setInt(3, 1);
				pstmt1.setString(4, p.getOrcidId());

				pstmt1.executeUpdate();

			} catch (SQLException e)
			{
				// LOGGER.log(Level.WARNING, "Error in insertEvidenceAuthorsInfo\n" +
				// e.getClass().getName() + ": " + e.getMessage());
			}

			try (PreparedStatement pstmt1 = c.prepareStatement(sqlInsertEvidAuth))
			{
				pstmt1.setString(1, npId);
				pstmt1.setString(2, p.getName());
				pstmt1.setString(3, p.getSurname());
				pstmt1.setInt(4, 1);

				pstmt1.executeUpdate();

			} catch (SQLException e)
			{
				// LOGGER.log(Level.WARNING, "Error in insertEvidenceAuthorsInfo\n" +
				// e.getClass().getName() + ": " + e.getMessage());
			}
		}

	}

	/**
	 * @param from
	 * @param to
	 * @return
	 */
	public List<Integer> getNumberInsertedFromTo(int from, int to)
	{

		ResultSet			rs	= null;
		ArrayList<Integer>	ret	= null;
		String				sql	= "SELECT substring(substring(np_id, 'NP[0-9]{6}\\.'), '[0-9]{6}')::integer as n, np_id, evid_id, prov_evidence, evid_description, generated_by, prov_db, prov_date, numauth, numevidauth, numcreator\n"
				+ "	FROM public.nanopub where substring(substring(np_id, 'NP[0-9]{6}\\.'), '[0-9]{6}')::integer >= "
				+ from + " \n" + "	and substring(substring(np_id, 'NP[0-9]{6}\\.'), '[0-9]{6}')::integer <= " + to
				+ "\n" + "	order by substring(substring(np_id, 'NP[0-9]{6}\\.'), '[0-9]{6}')::integer asc;";

		try (Statement pstmt1 = c.createStatement())
		{
			rs = pstmt1.executeQuery(sql);
			ret = resultSetToArray(rs);
		} catch (SQLException e)
		{
			LOGGER.warn("Error in getNumberInsertedFromTo\n" + e.getClass().getName() + ": " + e.getMessage());
		}

		return ret;
	}

	/**
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private ArrayList<Integer> resultSetToArray(ResultSet rs) throws SQLException
	{
		ArrayList<Integer> ret = new ArrayList<>();
		while (rs.next())
		{
			ret.add(rs.getInt("n"));
		}
		return ret;
	}

	public List<String> checkPersonAlreadyInDB(String orcid)
	{
		ArrayList<String>	ret	= new ArrayList<>();

		ResultSet			rs	= null;
		String				sql	= "SELECT given_name, family_name, num FROM public.person where orcid_id = '" + orcid
				+ "';";

		try (Statement pstmt1 = c.createStatement())
		{
			rs = pstmt1.executeQuery(sql);
			if (rs.next())
			{
				ret.add(rs.getString("given_name"));
				ret.add(rs.getString("family_name"));
				ret.add(rs.getString("num"));
			}

		} catch (SQLException e)
		{
			LOGGER.warn("Error in getNumberInsertedFromTo\n" + e.getClass().getName() + ": " + e.getMessage());
		}

		return ret;
	}

}
