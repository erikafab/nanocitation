/**
 * 
 */
package it.unipd.dei.nanop.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author erika
 *
 */
public class NanoParser
{

	@SuppressWarnings("unused")
	private File				file;
	private String				fileName;
	private String				pathName;
	private String				fileInString;
	@SuppressWarnings("unused")
	private ArrayList<String>	npArray		= new ArrayList<String>(); // NOTA: non ha
																		 // senso
																		 // tenere
																		 // questo
	private ArrayList<Integer>	npIntArray	= new ArrayList<Integer>();

	private final String		TEMPPATH	= "out/temp";
	// private final String PATHNPFILE =
	// "/Users/erika/Documents/Università/NpDISGeNET";

	public int					numNp;

	/**
	 * @throws IOException
	 * 
	 */
	public NanoParser(File file, String pathName, String fileName)
	{
		this.file = file;
		this.fileName = fileName;
		this.pathName = pathName;
		numNp = 0;

		// try
		// {
		// fileInString = file2string();
		// } catch (IOException e)
		// {
		// e.printStackTrace();
		// return;
		// }
		// set npArray
		// divideNp();
	}

	/**
	 * @return
	 */
	@SuppressWarnings("unused")
	private void divideNp()
	{
		int current = 0;

		while (current >= 0)
		{
			current = fileInString.indexOf("prefix", current);
			// System.out.println(current);
			if (current != 0 && current != -1)
			{
				// System.out.println(fileInString.charAt(current));

				int i = current;
				while (true)
				{
					if (i == 1)
					{
						npIntArray.add(0);
						break;
					}
					--i;
					char c = fileInString.charAt(i);
					if (c != '\n' && c != '\t' && c != '\r' && c != ' ' && c != '@')
					{
						if (c == '}')
						{
							npIntArray.add(current - 1);
							// System.out.println("Trovato");
						}
						break;
					} else
						--i;
				}
			}
			if (current != -1)
			{
				current += 6;
			}
		}

		numNp = npIntArray.size();
		System.out.println("NP trovate: " + numNp);

	}

	public String nNanopubInFile(int n)
	{
		String			ret					= "";
		String			line				= "";
		// String prevLine = null;
		Pattern			pSpace				= Pattern.compile("[ \t\n\f\r]");
		Pattern			pComment			= Pattern.compile("#.");
		Pattern			pPrefix				= Pattern.compile("[ \t\n\f\r]*@prefix.*");

		boolean			alreadyFindPrefix	= false;
		boolean			target				= false;
		int				count				= 0;

		FileInputStream	inputStream			= null;
		Scanner			sc					= null;
		try
		{
			inputStream = new FileInputStream(pathName + "/" + fileName);
			sc = new Scanner(inputStream, "UTF-8");
			while (sc.hasNextLine())
			{
				line = sc.nextLine();

				Matcher	mSpace		= pSpace.matcher(line);
				Matcher	mComment	= pComment.matcher(line);
				if (mSpace.matches() || mComment.matches())
					continue;

				Matcher mPrefix = pPrefix.matcher(line);
				if (mPrefix.matches() && !alreadyFindPrefix) // trovato @prefix
															 // per
															 // prima volta
															 // dopo
															 // body
				{

					alreadyFindPrefix = true;
					count++;
					if (count == n)
						target = true;

					else if (count > n)
						return ret;
				} else if (!mPrefix.matches())
					alreadyFindPrefix = false;

				if (target)
					ret = ret + "\n" + line;

			}
			if (ret.equals(""))
				return null;
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} finally
		{
			sc.close();
		}

		return ret;
	}

	public File extractFirst()
	{
		return extractOne(1);
	}

	public File extractOne(int position) // position parte da 1
	{

		File ret = null;
		try
		{
			Files.createDirectories(Paths.get(TEMPPATH));

			ret = new File(
					TEMPPATH + "/" + fileName.substring(0, fileName.lastIndexOf('.')) + "_" + position + ".trig");
			ret.createNewFile();
			FileWriter	fw	= new FileWriter(ret);

			String		np	= nNanopubInFile(position);
			if (np == null)
			{
				fw.close();
				return null;
			}
			fw.write(np);
			fw.close();

		} catch (IOException e)
		{
			e.printStackTrace();
		}

		return ret;
	}

	public File extractFirstOld()
	{
		if (numNp < 0)
			return null;
		return extractOne(1);
	}

	// to remove
	public File extractOneOld(int position) // position parte da 1
	{
		/*
		 * for (int i = 1; i<npIntArray.size();++i) { System.out.println(i +
		 * "  :  "+ npIntArray.get(i)); }
		 * 
		 * try { System.in.read(); } catch (IOException e1) {
		 * e1.printStackTrace(); }
		 */

		if (position > numNp)
			return null;
		File ret = null;
		try
		{
			Files.createDirectories(Paths.get(TEMPPATH));
			// System.out.println(TEMPPATH + "/" + fileName.substring(0,
			// fileName.lastIndexOf('.')) + "_" + position+".trig");
			ret = new File(
					TEMPPATH + "/" + fileName.substring(0, fileName.lastIndexOf('.')) + "_" + position + ".trig");
			ret.createNewFile();
			FileWriter fw = new FileWriter(ret);
			// System.out.println(ret.toString());

			System.out.println(position);
			int firstIntChar = npIntArray.get(position - 1);

			// System.out.println("Primo carattere "+firstIntChar);
			// System.out.println("Ultimo carattere
			// "+(npIntArray.get(position)-1));
			if (npIntArray.size() != position)
				fw.write(fileInString.substring(firstIntChar, npIntArray.get(position) - 1));
			else
				fw.write(fileInString.substring(firstIntChar));

			fw.close();

		} catch (IOException e)
		{
			e.printStackTrace();
		}

		return ret;
	}

	@SuppressWarnings("unused")
	private String file2string() throws IOException
	{

		return new String(Files.readAllBytes(Paths.get(pathName + "/" + fileName)));
	}

	public void npSetToFile(int from, int to)
	{

		// controllare from < to e from > 0

		Pattern			pSpace				= Pattern.compile("[ \t\n\f\r]");
		Pattern			pComment			= Pattern.compile("#.");
		Pattern			pPrefix				= Pattern.compile("[ \t\n\f\r]*@prefix.*");

		String			ret					= "";
		String			line				= "";

		Scanner			sc					= null;
		FileInputStream	inputStream			= null;

		boolean			alreadyFindPrefix	= false;
		boolean			target				= false;
		int				count				= 0;
		int				n					= from;

		try
		{
			inputStream = new FileInputStream(pathName + "/" + fileName);
			sc = new Scanner(inputStream, "UTF-8");
			while (sc.hasNextLine())
			{
				line = sc.nextLine();
				// System.out.println("Parsing numero "+ count);

				Matcher	mSpace		= pSpace.matcher(line);
				Matcher	mComment	= pComment.matcher(line);
				if (mSpace.matches() || mComment.matches())
					continue;

				Matcher mPrefix = pPrefix.matcher(line);
				if (mPrefix.matches() && !alreadyFindPrefix)
				{
					// System.out.println("dentro if prefix trovato e not
					// already");
					alreadyFindPrefix = true;

					// System.out.println("count "+count);
					if (count == n)
						target = true;

					else if (count > n)
					{
						System.out.println("Writing file" + count);
						File	fil;

						String	s	= getDirPath(n);

						fil = new File(s);

						fil.createNewFile();
						FileWriter fw = new FileWriter(fil);

						fw.write(ret);
						fw.close();

						++n;
						if (n >= to)
							return;
						alreadyFindPrefix = false;
						target = false;
						ret = line;
						--count;
					}
					++count;
				} else if (!mPrefix.matches())
					alreadyFindPrefix = false;

				if (target)
					ret = ret + "\n" + line;

			}
			if (!ret.equals(""))
			{

				System.out.println("fine");
				System.out.println("Writing file" + count);

				// Files.createDirectories(Paths.get(TEMPPATH));
				File	fil;

				String	s	= getDirPath(n);

				fil = new File(s);

				fil.createNewFile();
				FileWriter fw = new FileWriter(fil);

				fw.write(ret);
				fw.close();
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			sc.close();
		}

	}

	public void npSetToFile(int from)
	{

		// controllare from < to e from > 0

		Pattern			pSpace				= Pattern.compile("[ \t\n\f\r]");
		Pattern			pComment			= Pattern.compile("#.");
		Pattern			pPrefix				= Pattern.compile("[ \t\n\f\r]*@prefix.*");

		String			ret					= "";
		String			line				= "";

		Scanner			sc					= null;
		FileInputStream	inputStream			= null;

		boolean			alreadyFindPrefix	= false;
		boolean			target				= false;
		int				count				= 0;
		int				n					= from;

		try
		{
			inputStream = new FileInputStream(pathName + "/" + fileName);
			sc = new Scanner(inputStream, "UTF-8");
			while (sc.hasNextLine())
			{
				line = sc.nextLine();
				// System.out.println("Parsing numero "+ count);

				Matcher	mSpace		= pSpace.matcher(line);
				Matcher	mComment	= pComment.matcher(line);
				if (mSpace.matches() || mComment.matches())
					continue;

				Matcher mPrefix = pPrefix.matcher(line);
				if (mPrefix.matches() && !alreadyFindPrefix)
				{
					// System.out.println("dentro if prefix trovato e not
					// already");
					alreadyFindPrefix = true;

					// System.out.println("count "+count);
					if (count == n)
						target = true;

					else if (count > n)
					{
						System.out.println("Writing file" + count);
						// Files.createDirectories(Paths.get(TEMPPATH));
						File	fil;

						String	s	= getDirPath(n);

						fil = new File(s);

						fil.createNewFile();
						FileWriter fw = new FileWriter(fil);

						fw.write(ret);
						fw.close();

						++n;

						alreadyFindPrefix = false;
						target = false;
						ret = line;
						--count;
					}
					++count;
				} else if (!mPrefix.matches())
					alreadyFindPrefix = false;

				if (target)
					ret = ret + "\n" + line;

			}
			if (!ret.equals(""))
			{

				System.out.println("fine");
				System.out.println("Writing file" + count);

				// Files.createDirectories(Paths.get(TEMPPATH));

				File	fil;

				String	s	= getDirPath(n);

				fil = new File(s);

				fil.createNewFile();
				FileWriter fw = new FileWriter(fil);

				fw.write(ret);
				fw.close();
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			sc.close();
		}

	}

	/**
	 * @param n
	 * @return
	 */
	private String getDirPath(int n)
	{
		n++; // 0-->1
		String ret = pathName;
		if (n > 1000000)
		{
			File dir = new File(pathName + "/1000000");
			dir.mkdirs();
			ret = ret + "/1000000/nanopublications_v4.0.0.0_" + n + ".trig";
		} else if (n > 900000)
		{
			File dir = new File(pathName + "/900000");
			dir.mkdirs();
			ret = ret + "/900000/nanopublications_v4.0.0.0_" + n + ".trig";
		} else if (n > 800000)
		{
			File dir = new File(pathName + "/800000");
			dir.mkdirs();
			ret = ret + "/800000/nanopublications_v4.0.0.0_" + n + ".trig";
		} else if (n > 700000)
		{
			File dir = new File(pathName + "/700000");
			dir.mkdirs();
			ret = ret + "/700000/nanopublications_v4.0.0.0_" + n + ".trig";
		} else if (n > 600000)
		{
			File dir = new File(pathName + "/600000");
			dir.mkdirs();
			ret = ret + "/600000/nanopublications_v4.0.0.0_" + n + ".trig";
		} else if (n > 500000)
		{
			File dir = new File(pathName + "/500000");
			dir.mkdirs();
			ret = ret + "/500000/nanopublications_v4.0.0.0_" + n + ".trig";
		} else if (n > 400000)
		{
			File dir = new File(pathName + "/400000");
			dir.mkdirs();
			ret = ret + "/400000/nanopublications_v4.0.0.0_" + n + ".trig";
		} else if (n > 300000)
		{
			File dir = new File(pathName + "/300000");
			dir.mkdirs();
			ret = ret + "/300000/nanopublications_v4.0.0.0_" + n + ".trig";
		} else if (n > 200000)
		{
			File dir = new File(pathName + "/200000");
			dir.mkdirs();
			ret = ret + "/200000/nanopublications_v4.0.0.0_" + n + ".trig";
		} else if (n > 100000)
		{
			File dir = new File(pathName + "/100000");
			dir.mkdirs();
			ret = ret + "/100000/nanopublications_v4.0.0.0_" + n + ".trig";
		} else
		{
			File dir = new File(pathName + "/1000");
			dir.mkdirs();
			ret = ret + "/1000/nanopublications_v4.0.0.0_" + n + ".trig";
		}
		return ret;
	}
}
