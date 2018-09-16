/**
 * 
 */
package com.ics.blackcode.loganalyser;

import java.util.Date;

/**
 * @author zhuxiaowen
 *
 */
public class Log {
	//进程ID
	private long pid;
	//线程ID
	private long tid;
	//线程名称
	private String tname;
	//打印时间
	private Date date;
	//日志级别
	private String level;
	//日志打印类、文件...
	private String logger;
	//日志打印类、文件...的打印行
	private long location;
	//纯日志内容，客户调用日志类时所传递的内容
	private String content;
	//日志内容，打印工具所打印的完整的日志内容
	private String log;
	
	
	public long getPid()
	{
		return pid;
	}
	public void setPid(long pid)
	{
		this.pid = pid;
	}
	public long getTid()
	{
		return tid;
	}
	public void setTid(long tid)
	{
		this.tid = tid;
	}
	public String getTname()
	{
		return tname;
	}
	public void setTname(String tname)
	{
		this.tname = tname;
	}
	public Date getDate()
	{
		return date;
	}
	public void setDate(Date date)
	{
		this.date = date;
	}
	public String getLevel()
	{
		return level;
	}
	public void setLevel(String level)
	{
		this.level = level;
	}
	public String getLogger()
	{
		return logger;
	}
	public void setLogger(String logger)
	{
		this.logger = logger;
	}
	public long getLocation()
	{
		return location;
	}
	public void setLocation(long location)
	{
		this.location = location;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String pureContent)
	{
		this.content = pureContent;
	}
	public String getLog()
	{
		return log;
	}
	public void setLog(String log)
	{
		this.log = log;
	}
	
	

}
