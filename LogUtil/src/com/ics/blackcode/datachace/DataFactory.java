package com.ics.blackcode.datachace;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.ics.util.JsonUtil;

@Controller
@RequestMapping("/dataFactory")
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
	
	@Resource(name = "JsonUtil")
	private JsonUtil jsonUtil = null;
	

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
	@RequestMapping("/getLogInfosByCon")
	public void getLogInfosByCon(HttpServletRequest request, HttpServletResponse response){
		
		JSONObject paramJson = jsonUtil.getParamters(request);
		logFileID = paramJson.getString("logFileID");
		startTime = paramJson.getString("startTime");
		endTime = paramJson.getString("endTime");
		prossNo = paramJson.getString("prossNo");
		level = paramJson.getString("level");
		pre = paramJson.getString("pre");
		context = paramJson.getString("context");	
				
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
		
		JSONObject resultObj = new JSONObject();
		resultObj.put("data", fileJson);
		resultObj.put("result", "success");

		jsonUtil.out(response, resultObj.toJSONString());		
	}
	
	
	/**
	 * 这个方法是滚动条滑动时调用
	 * 返回json数据
	 * @author tianwenchao 2018-09-16
	 * @param logFileID 日志文件标识，读取哪个日志文件
	 * @param  startindex  开始位置行数
	 * @param  rowcount  返回日志行数
	 * @return JSONObject  
	 */
	@RequestMapping("/getLogInfosByScroll")
	public void getLogInfosByScroll(HttpServletRequest request, HttpServletResponse response){
		
		JSONObject paramJson = jsonUtil.getParamters(request);
		logFileID = paramJson.getString("logFileID");
		int startindex = Integer.parseInt(paramJson.getString("startTime"));
		int rowcount = Integer.parseInt(paramJson.getString("rowcount"));
		
				
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
		JSONObject resultObj = new JSONObject();
		resultObj.put("data", fileJson);
		resultObj.put("result", "success");

		jsonUtil.out(response, resultObj.toJSONString());		
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
	
	/**
	 * 获取log日志文件数组,目录为 class同级logs目录下文件
	 * 如果目录不存在返回null
	 * @author tianwenchao 2018-09-19
	 * @return 日志文件信息
	 */
	@RequestMapping("/getlogFiles")
	public void getlogFiles(HttpServletRequest request, HttpServletResponse response){	
		File f = new File(this.getClass().getResource("/").getPath()+"/logs/");	
		System.out.println("logpath is:"+f.getPath());
		JSONObject resultObj = new JSONObject();
		if(f.exists() && f.isDirectory()){
			JSONObject fileObj = new JSONObject();
			for(File afile : f.listFiles()){
				fileObj.put("filepath", afile.getPath());
				fileObj.put("filename", afile.getPath());
			}			
			resultObj.put("data", fileObj);
			resultObj.put("result", "success");
		}else{
			resultObj.put("data", null);
			resultObj.put("result", "failed");
		}
		
		jsonUtil.out(response, resultObj.toJSONString());
	}
	
	
}
