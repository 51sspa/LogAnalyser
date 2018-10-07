package com.ics.blackcode.loganalyser;

import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * Hello world!
 *
 */
public class App implements Runnable 
{
	private static final Logger logger = Logger.getLogger(App.class);
	
	
    public App(int exhauseSeconds, int lines, Logger logger)
	{
		this.exhausted = exhauseSeconds;
		this.lines = lines;
		this.desLogger = logger;
	}


	public static void main( String[] args )
    {
//    	produceLog(6*60,new int[] {50000, 30000, 40000});
//    	checkPattern();
    	checkReadLog();
    	
        
    }


	private static void checkReadLog()
	{
		String logFile = "D:\\harold\\env\\eclipse\\workspace\\loganalyser\\src\\main\\resource\\logNoneOutPut1.log";
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
        String input2 = "[FATAL]:[2018-10-07 22:11:35:116] [Thread-0] [com.ics.blackcode.loganalyser.App:120]this is fatal message : 7019";
    	String logPattern2 = "^\\[(?<level>[\\w ]{5})\\]:\\[(?<date>\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}:\\d{3})\\] \\[(?<tname>[\\w\\-]+)\\] \\[(?<logger>.+):(?<location>\\d+)\\](?<content>.*)$";
    	logger.info(Pattern.matches(logPattern2, input2));		
	}


	private static List<Log> readLog(String logFile, String logPattern, long offset, int limit)
	{

		LogReader reader = LogReaderUtil.getReader(logFile);
		return reader.getLogList(offset, limit);
	}


	private static void produceLog(int exhauseSeconds, int[] loggerLines)
	{
		for(int i =0; i<3; )
		{
			App app = new App(exhauseSeconds, loggerLines[i], Logger.getLogger("file" + (++i)));
			new Thread((Runnable) app).start();
		}
	}

	private int lines;
	private Logger desLogger;
	private int exhausted;
	private Random random = new Random();
	
	private void produce() throws InterruptedException
	{
		float speed = (float)(this.exhausted * 100) / lines;
		int preLeft = 0;
		for(int i=0; i< this.lines; i++)
		{
			int sleep = random.nextInt(10);
			int sleepTime = (int)((sleep + preLeft) * speed);
			Thread.sleep(sleepTime);
			preLeft = 10 - sleep;
			if(i%5 == 0)
			{
				this.desLogger .debug("this is debug message : " + i);
			}
			else if(i%5 == 1)
			{
				desLogger.info("this is info message : " + i);
			}
			else if(i%5 == 2)
			{
				desLogger.warn("this is warn message : " + i);
			}
			else if(i%5 == 3)
			{
				
				try
				{
					throw new Exception("Test exception : " + i);
				}
				catch(Exception e)
				{
					desLogger.error("this is error message : " + i + "\n error message is \n just say it is produced in sequece : " + i, e);
				}
			}
			else if(i%5 == 4)
			{
				desLogger.fatal("this is fatal message : " + i);
			}
		}
		
	
	}


	@Override
	public void run()
	{
		try
		{
			this.produce();
		} catch (InterruptedException e)
		{
			logger.error(e);
		}
		
	}
}
