package com.ics.blackcode.loganalyser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


/**
 * 日志读取类 - 用于读取指定日志中指定偏移的若干行日志
 * 非线程安全，若要多线程调用，请实例化多个
 * @author zhuxiaowen
 *
 */
public class LogReader {

	private static final Logger logger = Logger.getLogger(LogReader.class);
	private RandomAccessFile sourcee;
	private LogPattern pattern;

	public LogReader(String filePath, String logPattern)
	{
		this(filePath, logPattern, null, null);
	}

	public LogReader(String filePath, String logPattern, String datePattern)
	{
		this(filePath, logPattern, datePattern, null);
	}
	
	public LogReader(String filePath, String logPattern, String datePattern, String dateLocale)
	{
		try {
			this.sourcee = new RandomAccessFile(filePath, "r");
		} catch (FileNotFoundException e) {
			logger.error("can not found specified file : " + filePath);
			throw new RuntimeException("can not found specified file : " + filePath);
		}
		
		this.pattern = new LogPattern(logPattern, datePattern, dateLocale);
	}
	
	public List<Log>getLogList(long offset, int limit)
	{
		
		try {
			this.sourcee.seek(offset);
			List<Log> result = new ArrayList<>(limit);
			
			Log pre = null;
			Log current = null;
			StringBuilder preContent = new StringBuilder();
			
			for(int i = 0; i < limit; i++)
			{
				current = this.readLine(preContent);
				if(pre !=null && preContent.length() > 0)
				{
					pre.setLog(preContent.insert(0, pre.getLog()).toString());
					preContent.setLength(0);//clear buffer
				}
				
				if(current != null)
				{
					pre = current;
					result.add(current);
				}
				else
				{
					logger.error("may be log is less than expect, terrminate in read line of : " + i);
					break;
				}
			}
			
			return result;
		} catch (IOException e) {
			logger.error("exception on getLogList :", e);
			throw new RuntimeException(e.getMessage());
		}
		
	}
	
	/**
	 * 读取下一行日志,递归至下一行行首
	 * @return [0] 当前文件指针处内容至新行开始处胡上一条的内容 [1] 新读取的一行日志
	 * @throws IOException 
	 */
	private Log readLine(StringBuilder preLineContent) throws IOException
	{
		String phyLine = sourcee.readLine();
		if(phyLine == null)
		{
			return null;
		}
		else
		{
			if(this.pattern.match(phyLine))
			{
				return this.pattern.getLog();
			}
			else
			{
				preLineContent.append(phyLine);
				return readLine(preLineContent);
			}
		}

	}

	public long getLogFileSize()
	{
		try {
			return this.sourcee.length();
		} catch (IOException e) {
			logger.error("can not get file length!", e);
		}
		
		return 0;
	}
}
