/**
 * 
 */
package com.ics.blackcode.loganalyser;

/**
 * 用于获取日志读取类的帮助类
 * @author zhuxiaowen
 *
 */
public class LogReaderUtil
{

	private static final String LOG_PATTERN_KEY = "LOG_PATTERN";
	private static final String LOG_DATE_PATTERN = "LOG_DATE_PATTERN";
	private static final String LOG_DATE_LOCALE = "LOG_DATE_LOCALE";

	/**
	 * 根据配置文件中的日志模式，日期模式配置获取日志读取类
	 * @param filePath 日志文件路径
	 * @return log reader
	 */
	public static LogReader getReader(String filePath)
	{
		return new LogReader(filePath
								, PropertiesUtils.getIntances().getConfig(LOG_PATTERN_KEY)
								, PropertiesUtils.getIntances().getConfig(LOG_DATE_PATTERN)
								, PropertiesUtils.getIntances().getConfig(LOG_DATE_LOCALE));
	}
}
