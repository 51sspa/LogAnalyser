/**
 * 
 */
package com.ics.blackcode.loganalyser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * 日志模式 - 代表所解析日志的模式
 * 由命名正则表达式构成
 * 目前支持的模式有 进程号:pid,线程号:tid,线程名:tname,日期:date,级别:level,打印类:logger,打印行号:location
 * 
 * 日志模式 examples:  
 * 2018-09-14 23:08:38  [ main:298 ] com.ics.blackcode.loganalyser.App:69 - [ ERROR ]  this is error message : 496
 * ^(?<date>\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2})  \[ (?<tname>\w+):\d+ \] (?<logger>[\w\.]+):(?<location>\d+) - \[ (?<level>\w+) \] .*$
 * 
 * @author zhuxiaowen
 *
 */
public class LogPattern {

	private static final Logger logger = Logger.getLogger(LogPattern.class);
	private static final String DAFAULT_DATE_PATTERN = "yyyy-MM-dd hh:mm:ss";
	private static List<SubPattern> paternNames;
	//当前的模式
	private Pattern pattern;
	//上一次mathc方法的mather
	private Matcher matcher;
	//标记上一次math方法是否匹配模式
	private boolean isMatched;
	//标记当前日志模式拥有的子模式
	private boolean[] patternTest;
	private final SimpleDateFormat dateParser;
	

	
	public LogPattern(String logPattern)
	{
		this(logPattern, (String)null, (Locale)null);
	}
	
	public LogPattern(String logPattern, String datePattern, String locale)
	{
		this(logPattern, datePattern, StringUtils.isEmpty(locale) ? (Locale)null : new Locale(locale));
	}
		
	public LogPattern(String logPattern, String datePattern, Locale locale) {
		this.pattern = Pattern.compile(logPattern);
		String realDatePattern = StringUtils.isEmpty(datePattern) ? DAFAULT_DATE_PATTERN : datePattern;
		Locale realLocale = locale == null ? Locale.getDefault() : locale;
		this.dateParser = new SimpleDateFormat(realDatePattern, realLocale);
		initSubPatterns();
		findSections(logPattern);
	}

	//初始化系统支持胡子模式
	private void initSubPatterns()
	{
		paternNames = new ArrayList<>();
		paternNames.add(new SubPattern(0, "pid"));
		paternNames.add(new SubPattern(1, "tid"));
		paternNames.add(new SubPattern(2, "date"));
		paternNames.add(new SubPattern(3, "level"));
		paternNames.add(new SubPattern(4, "logger"));
		paternNames.add(new SubPattern(5, "location"));
		paternNames.add(new SubPattern(6, "tname"));
	}
	
	/**
	 * 识别当前模式有哪些字段
	 * @param logPattern
	 */
	private final void findSections(String logPattern)
	{
		this.patternTest = new boolean [paternNames.size()];
		for(SubPattern subPattern : paternNames)
		{
			this.patternTest[subPattern.innerIndex] = logPattern.indexOf("?<" + subPattern.name + '>') != -1;
		}
	}
	

	/**
	 * 测试指定字符串是否符合日志模式
	 * @param phyLine
	 * @return
	 */
	public boolean match(String phyLine) {
		this.matcher = this.pattern.matcher(phyLine);
		this.isMatched = this.matcher.find();
		return this.isMatched;
	}

	public Log getLog() {
		if(!this.isMatched) throw new RuntimeException("not matched on last call match method!");
		Log log = new Log();
		for(SubPattern subPattern : paternNames)
		{
			if(this.patternTest[subPattern.innerIndex])
			{
				subPattern.setValue(log, this.matcher.group(subPattern.name));
			}
		}
		log.setLog(this.matcher.group());
		return log;
	}

	/**
	 * 当前系统支持的子模式，及其命名
	 * @author zhuxiaowen
	 *
	 */
	class SubPattern
	{
		protected String name;
		protected int    innerIndex;
		

		public SubPattern(int innerIndex, String name)
		{
			this.innerIndex = innerIndex;
			this.name = name;
		}
		
		public void setValue(Log log, String value)
		{
			try
			{
				if("date".equals(this.name))
				{
					log.setDate(LogPattern.this.dateParser.parse(value));
				}
				else
				{
					BeanUtils.setProperty(log, this.name, value);
				}
				
			} catch (Exception e)
			{
				logger.error("can not set property for log."+this.name, e);
			} 
		}
	}
}
