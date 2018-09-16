package com.ics.blackcode.loganalyser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


/**
 * 日志读取类 - 用于读取指定日志中指定偏移的若干行日志
 * 非线程安全，若要多线程调用，请实例化多个,使用完毕调用 close 方法关闭文件资源
 * @author zhuxiaowen
 *
 */
public class LogReader {

	private static final Logger logger = Logger.getLogger(LogReader.class);
	private RandomAccessFile sourcee;
	private LogPattern pattern;
	//未匹配到日志模式时继续搜寻的行数，超出后仍未找到的话就终止返回。
	private static final int DEFAUTL_BREAK_SEARCH_NUM = 500;

	/**
	 * 
	 * @param filePath  要读取的日志文件
	 * @param logPattern 日志文件的命名正则表达式，目前支持的命名参考 {@link LogPattern}
	 * 日志中打印时间的输出格式, 默认为 yyyy-MM-dd hh:mm:ss 参考  LogPattern.DAFAULT_DATE_PATTERN,
	 * 日志中日期的locale, 默认为系统当前Locale
	 */
	public LogReader(String filePath, String logPattern)
	{
		this(filePath, logPattern, null, null);
	}

	/**
	 * 
	 * @param filePath    要读取的日志文件
	 * @param logPattern  日志文件的命名正则表达式，目前支持的命名参考 {@link LogPattern}
	 * @param datePattern 日志中打印时间的输出格式, 默认为 yyyy-MM-dd hh:mm:ss 参考  LogPattern.DAFAULT_DATE_PATTERN,
	 *                    日志中日期的locale, 默认为系统当前Locale
	 */
	public LogReader(String filePath, String logPattern, String datePattern)
	{
		this(filePath, logPattern, datePattern, null);
	}
	
	/**
	 * 
	 * @param filePath    要读取的日志文件
	 * @param logPattern  日志文件的命名正则表达式，目前支持的命名参考 {@link LogPattern}
	 * @param datePattern 日志中打印时间的输出格式 , 默认为 yyyy-MM-dd hh:mm:ss 参考  LogPattern.DAFAULT_DATE_PATTERN
	 * @param dateLocale  日志中日期的locale, 默认为系统当前Locale
	 */
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
			
			for(int i = 0; i <= limit; i++)
			{
				current = this.readLine(preContent, DEFAUTL_BREAK_SEARCH_NUM);
				if(pre !=null && preContent.length() > 0)
				{
					//去掉多行日志最后一行后面的换行符，所返回的日志，多行日志均不会在末尾添加换行符
					preContent.setLength(preContent.length() - System.lineSeparator().length());
					//续接首行后面的换行符
					preContent.insert(0,System.lineSeparator());
					//设置全属性日志
					pre.setLog(pre.getLog() + preContent.toString());
					//设置纯内容日志
					pre.setContent(preContent.insert(0, pre.getContent()).toString());
					//clear buffer
					preContent.setLength(0);
				}
				else if(pre == null)
				{
					preContent.setLength(0);// 清空第一次读取的当前文件指针行的不完全日志
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
			
			//删掉为了读取完整日志行内容而多余读取的一条日志记录
			if(result.size() >= limit)
			{
				result.remove(result.size()-1);
			}
			
			return result;
		} catch (IOException e) {
			logger.error("exception on getLogList :", e);
			throw new RuntimeException(e.getMessage());
		}
		
	}
	
	/**
	 * 读取下一行日志,递归至下一行日志首行尾部
	 * @param maxSearchLines 为了找寻到日志首行，所搜索的非首行的最大行数，
	 * @return Log 匹配到的第一行日志
	 * @throws IOException 
	 */
	private Log readLine(StringBuilder preLineContent, int maxSearchLines) throws IOException
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
				preLineContent.append(phyLine).append(System.lineSeparator());
				//尚未超过最大搜索深度则继续搜索
				return --maxSearchLines > 0 ? readLine(preLineContent, maxSearchLines) : null;
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
	
	/**
	 * 当不再使用{@link LogReader} 对象时，需要调用来关闭所使用的文件资源
	 */
	public void close()
	{
		try
		{
			this.sourcee.close();
		} catch (IOException e)
		{
			logger.error("can not close log reader's file.", e);
		}
	}
	
	
	@Override
	protected void finalize()
	{
		close();
	}
}
