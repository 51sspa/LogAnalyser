package com.tians.datachace;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class DataFactory {
	
	/** 以下变量保存上一次查询条件   */
	/** 标识日志文件  */
	private static String logFileID = "111";
	/** 查询日志开始时间  */
	private static String startTime = "";
	/** 查询日志结束时间  */
	private static String endTime = "";
	/** 查询日志进程号  */
	private static String prossNo = "";
	/** 日志级别  */
	private static String level = "";
	/** 前缀标识  */
	private static String pre = "";
	/** 内容关键字  */
	private static String context = "";
	
	public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");

	public DataFactory(){}
	

	/**
	 * 这个方法是点击查询按钮时调用
	 * 根据条件获取json数据 默认符合条件的数据最多返回一万行
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
	public JSONObject getLogInfosByCon(String aLogFileID,String aStartTime,String aEndTime,String aProssNo,String aLevel,String aPre,String aContext){
		
		logFileID = aLogFileID;
		startTime = aStartTime;
		endTime = aEndTime;
		prossNo = aProssNo;
		level = aLevel;
		pre = aPre;
		context = aContext;
				
		JSONObject fileJson = new JSONObject();
		
		/** 此处为构造数据，后面替换成小文的App.readLog 方法 */
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
		
		dataFilter(jsonList, 10000);
		
		System.out.println("jsonList.size:"+jsonList.size());
		return fileJson;
	}
	
	
	/**
	 * 这个方法是滚动条滑动时调用
	 * 返回json数据
	 * @author tianwenchao 2018-09-16
	 * @param aLogFileID 日志文件标识，读取哪个日志文件
	 * @param  startindex  开始位置行数
	 * @param  rowcount  返回日志行数
	 * @return JSONObject  
	 */
	public JSONObject getLogInfosByScroll(String aLogFileID,long startindex,int rowcount){
		
		logFileID = aLogFileID;	
				
		JSONObject fileJson = new JSONObject();
		/** 此处为构造数据，后面替换成小文的App.readLog 方法 */
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		for(int i=0;i<1000;i++){
			jsonList.add(getJSONObject("2018-08-26 12:08:57,70"+i,"10"+i,"INFO","[org.jboss.seam.Component]","(main) Component class should be serializable: fixFieldManager"));
		}
		try {
			fileJson.put("count", jsonList.size());
			fileJson.put("index", 1);
			fileJson.put("data", jsonList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		dataFilter(jsonList, 1000);
		
		System.out.println("jsonList.size:"+jsonList.size());
		return fileJson;
	}
	
	
	
	/***
	 * 根据原始数据集合过滤所需日志
	 * @param sourceList 原始数据集合（从后台获取到的）
	 * @param maxLimit 最大返回行数限制
	 * @return
	 */
	private List<JSONObject> dataFilter(List<JSONObject> sourceList,int maxLimit){
		List<JSONObject> returnList = new ArrayList<JSONObject>(maxLimit);
		if(null == sourceList || 0== sourceList.size()){
			return returnList;
		}
				
		int jsonNum = 0;
		try {
			//所有过滤均采用反向判断，即：有一项不符合条件就跳出循环，寻找下一条
			for(JSONObject tempJson : sourceList){		
				
				if(null!=startTime && !"".equals(startTime)){				
					if(sdf.parse(tempJson.getString("time")).before(sdf.parse(startTime))){
						break;
					}							
				}
				if(null!=endTime && !"".equals(endTime)){				
					if(sdf.parse(tempJson.getString("time")).after(sdf.parse(endTime))){
						break;
					}					
				}
				if(null!=prossNo && !"".equals(prossNo)){
					
					if(!prossNo.equals(tempJson.getString("prossNo"))){
						break;
					}							
				}
				if(null!=pre && !"".equals(pre)){
					
					if(!pre.equals(tempJson.getString("pre"))){
						break;
					}							
				}
				if(null!=level && !"".equals(level)){
					
					if(!level.equals(tempJson.getString("level"))){
						break;
					}							
				}
				if(null!=context && !"".equals(context)){
					
					if(!tempJson.getString("context").matches(context)){
						break;
					}							
				}
									
				returnList.add(tempJson);
				jsonNum++;
				if(jsonNum==maxLimit){
					return returnList;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();			
		}	
		
		return returnList;
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
