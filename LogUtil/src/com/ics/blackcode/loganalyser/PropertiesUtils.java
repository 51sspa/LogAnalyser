/**
 * 
 */
package com.ics.blackcode.loganalyser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author zhuxiaowen
 *
 */
public class PropertiesUtils
{
	private static final Logger logger = Logger.getLogger(Properties.class);
	//配置文件地址
	private static final String configFile = "log.conf";
	//配置信息
	private Properties config;
	
	private static PropertiesUtils propertiesUtils;
	
	public static synchronized PropertiesUtils getIntances()
	{
		if(propertiesUtils == null)
		{
			propertiesUtils = new PropertiesUtils();
			propertiesUtils.init();
		}
		
		return propertiesUtils;
	}

	private void init()
	{
		config = new Properties();
		
		try(InputStream conf = PropertiesUtils.class.getClassLoader().getResourceAsStream(configFile))
		{
			config.load(conf);
			
		} catch (IOException e)
		{
			logger.error("error on load config : ", e);
		}
	}
	
	public final String getConfig(String key)
	{
		String result = config.getProperty(key);
		return result == null ? null : result.trim();
	}
}
