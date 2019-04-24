/**
 * 
 */
package it.unipd.dei.nanocitation.logger;

import java.io.File;
import java.nio.file.Path;

import org.apache.log4j.Logger;


/**
 * Logger stub.
 * 
 * @author erika
 *
 */
public class LoggerCustom
{
	public final String INFO = "INFO";
	public final String DEBUG = "DEBUG";
	public final String WARN = "WARN";
	public final String ERROR = "ERROR";
	public final String TRACE = "TRACE";
	public final String FATAL = "FATAL";
	
	
	// da inserire possibilità di scrivere file di log -> path
	public static File logFile;
	public static Path pathToLog;
	
	private Logger logger;
	
	/**
	 * Retrieve a logger named according to the value of the name parameter.
	 * 
	 * @param name
	 *            The name of the logger to retrieve
	 */
	public LoggerCustom(String name)
	{
		logger = Logger.getLogger(name);
		
	}
	
	/**
	 * Log a message object with the level passed as parameter.
	 * 
	 * @param logType
	 *            log level.
	 * @param msg
	 *            message the message object to log.
	 */
	public void log(String logType, String msg)
	{
		switch (logType)
		{
			case INFO:
				infoMsg(msg);
				break;
			case DEBUG:
				debugMsg(msg);
				break;
			case WARN:
				warnMsg(msg);
				break;
			case ERROR:
				errorMsg(msg);
				break;
			case TRACE:
				traceMsg(msg);
				break;
			case FATAL:
				fatalMsg(msg);
				break;
		}
	}
	
	/**
	 * Log a message object with the level passed as parameter, including the
	 * stack trace of the {@link Throwable} <code>tr</code> passed as parameter
	 * 
	 * @param logType
	 *            log level.
	 * @param msg
	 *            message the message object to log.
	 * @param tr
	 *            the exception to log, including its stack trace.
	 */
	public void log(String logType, String msg, Throwable tr)
	{
		switch (logType)
		{
			case INFO:
				infoMsg(msg, tr);
				break;
			case DEBUG:
				debugMsg(msg, tr);
				break;
			case WARN:
				warnMsg(msg, tr);
				break;
			case ERROR:
				errorMsg(msg, tr);
				break;
			case TRACE:
				traceMsg(msg, tr);
				break;
			case FATAL:
				fatalMsg(msg, tr);
				break;
		}
	}
	
	/**
	 * Log a message object with the level <code>INFO</code>.
	 * 
	 * @param msg
	 *            message the message object to log.
	 */
	public void infoMsg(String msg)
	{
		logger.info(msg);
	}
	
	/**
	 * Log a message object with the level <code>INFO</code> including the stack
	 * trace of the {@link Throwable} <code>tr</code> passed as parameter.
	 * 
	 * @param msg
	 *            message the message object to log.
	 * @param tr
	 *            the exception to log, including its stack trace.
	 */
	public void infoMsg(String msg, Throwable tr)
	{
		logger.info(msg, tr);
	}
	
	/**
	 * Log a message object with the level <code>DEBUG</code>.
	 * 
	 * @param msg
	 *            message the message object to log.
	 */
	public void debugMsg(String msg)
	{
		logger.debug(msg);
	}
	
	/**
	 * Log a message object with the level <code>DEBUG</code> including the
	 * stack trace of the {@link Throwable} <code>tr</code> passed as parameter.
	 * 
	 * @param msg
	 *            message the message object to log.
	 * @param tr
	 *            the exception to log, including its stack trace.
	 */
	public void debugMsg(String msg, Throwable tr)
	{
		logger.debug(msg, tr);
	}
	
	/**
	 * Log a message object with the level <code>WARN</code>.
	 * 
	 * @param msg
	 *            message the message object to log.
	 */
	public void warnMsg(String msg)
	{
		logger.warn(msg);
	}
	
	/**
	 * Log a message object with the level <code>WARN</code> including the stack
	 * trace of the {@link Throwable} <code>tr</code> passed as parameter.
	 * 
	 * @param msg
	 *            message the message object to log.
	 * @param tr
	 *            the exception to log, including its stack trace.
	 */
	public void warnMsg(String msg, Throwable tr)
	{
		logger.info(msg, tr);
	}
	
	/**
	 * Log a message object with the level <code>ERROR</code>.
	 * 
	 * @param msg
	 *            message the message object to log.
	 */
	public void errorMsg(String msg)
	{
		logger.error(msg);
	}
	
	/**
	 * Log a message object with the level <code>ERROR</code> including the
	 * stack trace of the {@link Throwable} <code>tr</code> passed as parameter.
	 * 
	 * @param msg
	 *            message the message object to log.
	 * @param tr
	 *            the exception to log, including its stack trace.
	 */
	public void errorMsg(String msg, Throwable tr)
	{
		logger.error(msg, tr);
	}
	
	/**
	 * Log a message object with the level <code>TRACE</code>.
	 * 
	 * @param msg
	 *            message the message object to log.
	 */
	public void traceMsg(String msg)
	{
		logger.trace(msg);
	}
	
	/**
	 * Log a message object with the level <code>TRACE</code> including the
	 * stack trace of the {@link Throwable} <code>tr</code> passed as parameter.
	 * 
	 * @param msg
	 *            message the message object to log
	 * @param tr
	 *            the exception to log, including its stack trace.
	 */
	public void traceMsg(String msg, Throwable tr)
	{
		logger.trace(msg, tr);
	}
	
	/**
	 * Log a message object with the level <code>FATAL</code>.
	 * 
	 * @param msg
	 *            message the message object to log.
	 */
	public void fatalMsg(String msg)
	{
		logger.fatal(msg);
	}
	
	/**
	 * Log a message object with the level <code>FATAL</code> including the
	 * stack trace of the {@link Throwable} <code>tr</code> passed as parameter.
	 * 
	 * @param msg
	 *            message the message object to log.
	 * @param tr
	 *            the exception to log, including its stack trace.
	 */
	public void fatalMsg(String msg, Throwable tr)
	{
		logger.fatal(msg, tr);
	}
	
}
