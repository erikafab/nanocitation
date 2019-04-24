/**
 * 
 */
package it.unipd.dei.nanop.threads;


import java.util.ArrayList;

import it.unipd.dei.nanop.statisticsdb.ManagerNpDB;


/**
 * @author erika
 *
 */
public class FinalThread extends Thread
{
	private int		id;
	private int		startNum, endNum;
	private String	pathNpDir;

	private long	startTime;
	private long	endTime;

	public FinalThread(int id, int startNum, int endNum, String pathNpDir)
	{
		this.startNum = startNum;
		this.endNum = endNum;
		this.id = id;
		this.pathNpDir = pathNpDir;
	}

	@Override
	public void run()
	{
		// start to count time to run
		startTime = System.currentTimeMillis();

		ManagerNpDB mdb = new ManagerNpDB();
		mdb.openManager();

		ArrayList<Integer>	ris		= (ArrayList<Integer>) mdb.getNumberInsertedFromTo(startNum, endNum);

		int					numRows	= ris.size();

		if (numRows >= endNum - startNum)
		{
			System.out.println("Everything's ok");
			return;

		}
		int	firstGap	= startNum;
		int	lastGap		= startNum;
		int	current		= startNum + 1;
		int	i			= 0;

		for (i = 0; i < ris.size(); ++i)
		{
			// System.out.println("In while");
			int temp = (int) ris.get(i);
			if (temp != current)
			{
				lastGap = temp;
				System.out.println("firstGap " + firstGap + " lastGap " + lastGap);
				ExtractingThread thread = new ExtractingThread(firstGap, firstGap, lastGap, pathNpDir);
				thread.start();
			}
			current = temp + 1;
			firstGap = temp;
		}
		int temp = (int) ris.get(ris.size() - 1);
		if (temp != endNum)
		{
			ExtractingThread thread = new ExtractingThread(temp, temp, endNum, pathNpDir);
			thread.start();
			System.out.println("final");
		}

		mdb.closeManager();
		endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out
				.println("Thread " + id + " Total running time = " + Math.round(totalTime / (1000d * 60)) + " minutes");
	}

}
