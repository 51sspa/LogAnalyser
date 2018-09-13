package com.tians.datachace;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class DataFactory {

	public static void main(String [] aa){
		try {
			
		
			System.out.println("【startTime】:"+new Date().getTime());	
			
			getLogInfosByCon("", null, null, null, null, null, null);
			
			System.out.println("【endTime】"+new Date().getTime());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 根据条件获取json数据
	 * @author tianwenchao 2018-09-12
	 * @param logFileID 日志文件ID标识唯一
	 * @param startTime 过滤开始时间
	 * @param endTime 过滤结束时间
	 * @param prossNo 进程号
	 * @param level 日志级别
	 * @param pre 日志前缀 ： [org.jboss.seam.Component]
	 * @param context 内容关键字 模糊匹配
	 * @return JSONObject  
	 */
	public static JSONObject getLogInfosByCon(String logFileID,String startTime,String endTime,String prossNo,String level,String pre,String context){
		
		JSONObject fileJson = new JSONObject();
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		for(int i=0;i<100000;i++){
			jsonList.add(getJSONObject("2018-08-26 12:08:57,70"+i,"10"+i,"INFO","[org.jboss.seam.Component]","(main) Component class should be serializable: fixFieldManager"));
		}
		try {
			fileJson.put("count", jsonList.size());
			fileJson.put("index", 1);
			fileJson.put("data", jsonList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println("jsonList.size:"+jsonList.size());
		return fileJson;
	}
	
	/**
	 * 生成json对象
	 * @author tianwenchao 2018-09-12
	 * @param logTime
	 * @param prossNo
	 * @param level
	 * @param pre
	 * @param context
	 * @return JSONObject 
	 */
	private static JSONObject getJSONObject(String logTime,String prossNo,String level,String pre,String context){
		try {
			JSONObject aa1 = new JSONObject();
			aa1.put("context",  context);
			aa1.put("pre",  pre);
			aa1.put("time",  logTime);
			aa1.put("level",  level);
			aa1.put("prossNo",  prossNo);			
			return aa1;
		} catch (JSONException e) {
			e.printStackTrace();
			return new JSONObject();
		}
	}
	
	
}
