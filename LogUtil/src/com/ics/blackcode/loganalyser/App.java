package com.ics.blackcode.loganalyser;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * Hello world!
 *
 */
public class App 
{
	private static final Logger logger = Logger.getLogger(App.class);
	
	
    public static void main( String[] args )
    {
//    	produceLog(500);
//    	checkPattern();
    	checkReadLog();
    	
        
    }


	private static void checkReadLog()
	{
		String logFile = "D:\\harold\\env\\eclipse\\workspace\\loganalyser\\src\\main\\resource\\log.log";
		String logPattern = "^(?<date>\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})  \\[ (?<tname>\\w+):\\d+ \\] (?<logger>[\\w\\.]+):(?<location>\\d+) - \\[ (?<level>\\w+) \\] (?<content>.*)$";
        
    	long offset = 217;
    	int limit = 10;
    	
        List<Log> logs = readLog(logFile, logPattern, offset, limit);
        
        logger.info("fetched size is : " + logs.size());
        for(Log log : logs)
        {
        	logger.info(" log content line : "  + log.getContent());
        }		
	}


	private static void checkPattern()
	{
		String input = "2018-09-14 23:08:38  [ main:32 ] com.ics.blackcode.loganalyser.App:74 - [ FATAL ]  this is fatal message : 5";
    	String logPattern = "^(?<date>\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})  \\[ (?<tname>\\w+):\\d+ \\] (?<logger>[\\w\\.]+):(?<location>\\d+) - \\[ (?<level>\\w+) \\] (?<content>.*)$";
        logger.info(Pattern.matches(logPattern, input));		
	}


	private static List<Log> readLog(String logFile, String logPattern, long offset, int limit)
	{

		LogReader reader = new LogReader(logFile, logPattern);
		return reader.getLogList(offset, limit);
	}


	private static void produceLog(int lines)
	{
		for(int i=0; i< lines; i++)
		{
			if(i%5 == 0)
			{
				logger.debug("this is debug message : " + i);
			}
			else if(i%5 == 1)
			{
				logger.info("this is info message : " + i);
			}
			else if(i%5 == 2)
			{
				logger.warn("this is warn message : " + i);
			}
			else if(i%5 == 3)
			{
				
				try
				{
					throw new Exception("Test exception : " + i);
				}
				catch(Exception e)
				{
					logger.error("this is error message : " + i + "\n error message is \n just say it is produced in sequece : " + i, e);
				}
			}
			else if(i%5 == 4)
			{
				logger.fatal("this is fatal message : " + i);
			}
		}
		
	}
}
