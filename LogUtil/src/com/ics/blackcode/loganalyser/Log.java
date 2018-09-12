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
	
	private String pid;
	private String tid;
	private Date date;
	private String level;
	private String logger;
	private String location;
	private String log;
	public String getPid()
	{
		return pid;
	}
	public void setPid(String pid)
	{
		this.pid = pid;
	}
	public String getTid()
	{
		return tid;
	}
	public void setTid(String tid)
	{
		this.tid = tid;
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
	public String getLocation()
	{
		return location;
	}
	public void setLocation(String location)
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
