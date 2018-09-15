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
	
	private long pid;
	private long tid;
	private String tname;
	private Date date;
	private String level;
	private String logger;
	private long location;
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
	public String getLog()
	{
		return log;
	}
	public void setLog(String log)
	{
		this.log = log;
	}
	
	

}
