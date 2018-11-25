package com.ics.blackcode.datachace;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.ics.blackcode.loganalyser.Log;
import com.ics.blackcode.loganalyser.LogReader;
import com.ics.blackcode.loganalyser.LogReaderUtil;
import com.ics.util.JsonUtil;

@Controller
@RequestMapping("/dataFactory")
public class DataFactory {
	
	private static final Logger logger = Logger.getLogger(DataFactory.class);
	
	/** 以下变量保存上一次查询条件   */
	/** 标识日志文件  */
	private static String logIndex = "0";
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
	/** 最大返回行数 */
	private static int maxReturnNum = 1000;
	
	//读取行数默认第一行
	private int startScroolIndex = 0;
	
	/** 缓存文件列表  */
	private List<JSONObject> logFileList = new ArrayList<JSONObject>();
	
	public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
	
	@Resource(name = "JsonUtil")
	private JsonUtil jsonUtil = null;
	

	/**
	 * 这个方法是点击查询按钮时调用
	 * 根据条件获取json数据 默认符合条件的数据最多返回 maxReturnNum行
	 * @author tianwenchao 2018-09-12
	 * @param logIndex 日志文件ID标识唯一
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
		logger.info("begin to getLogInfosByCon。");
		JSONObject paramJson = jsonUtil.getParamters(request);	
		logIndex = paramJson.getString("logIndex");
		startTime = paramJson.getString("startTime");
		endTime = paramJson.getString("endTime");
		prossNo = paramJson.getString("prossNo");
		level = paramJson.getString("level").toUpperCase();
		context = paramJson.getString("context");	
		logger.info("getLogInfosByCon startTime："+startTime+" endTime:"+ endTime +" prossNo:"+ prossNo +" level:"+ level + " pre:"+ pre + " context:"+ context);		
				
		List<JSONObject> jsonList = new ArrayList<JSONObject>();

		LogReader reader =LogReaderUtil.getReader(getLogFileObject(logIndex));
		
		jsonList = getResultList(jsonList,reader,0,maxReturnNum);
				
		JSONObject resultObj = new JSONObject();
		resultObj.put("data", jsonList);	
		resultObj.put("count", jsonList.size());
		resultObj.put("logIndex", logIndex);
		resultObj.put("result", "success");

		jsonUtil.out(response, resultObj.toJSONString());	
		logger.info("end to getLogInfosByCon。");
	}
	
	
	/**
	 * 这个方法是滚动条滑动时调用
	 * 返回json数据
	 * @author tianwenchao 2018-09-16
	 * @param logIndex 日志文件标识，读取哪个日志文件
	 * @param  startIndex  开始位置行数
	 * @param  rowCount  返回日志行数
	 * @return JSONObject  
	 */
	@RequestMapping("/getLogInfosByScroll")
	public void getLogInfosByScroll(HttpServletRequest request, HttpServletResponse response){
		logger.info("begin to getLogInfosByScroll。");
		JSONObject paramJson = jsonUtil.getParamters(request);
		logIndex = paramJson.getString("logIndex");
		if("1".equals(logIndex))
		{
			if(null == paramJson.getString("startIndex")){
				startScroolIndex = 0;
			}else{
				startScroolIndex = Integer.parseInt(paramJson.getString("startIndex"));
			}
			startScroolIndex += startScroolIndex;
		}
		logger.info("getLogInfosByScroll startIndex is " + startScroolIndex);
		//返回行数默认1000行
		int rowcount = 1000;
		if(null == paramJson.getString("rowCount")){
			rowcount = 1000;
		}else{
			rowcount = Integer.parseInt(paramJson.getString("rowCount"));
		}
		logger.info("getLogInfosByScroll rowcount is " + rowcount);	

		LogReader reader =LogReaderUtil.getReader(getLogFileObject(logIndex));
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		jsonList = getResultList(jsonList,reader,startScroolIndex,rowcount);
				
		JSONObject resultObj = new JSONObject();
		resultObj.put("data", jsonList);
		resultObj.put("count", jsonList.size());
		resultObj.put("logIndex", logIndex);
		resultObj.put("result", "success");

		jsonUtil.out(response, resultObj.toJSONString());		
		logger.info("end to getLogInfosByScroll。");
	}
	
	/**
	 * 
	 * @return
	 */
	private List<JSONObject> getResultList(List<JSONObject> resultList,LogReader reader,int startIndex, int returnNum){
		List<JSONObject> tempList = new ArrayList<JSONObject>();
		List<Log> logs = reader.getLogList(startIndex, returnNum);
		JSONObject temp;
		for(Log aLog : logs){	
			temp = JSONObject.parseObject(JSONObject.toJSONString(aLog));
			temp.put("time", sdf.format(new Date((long) temp.get("date"))));
			tempList.add(temp);			
		}
				
		resultList.addAll(dataFilter(tempList, returnNum));
		logger.info("jsonList size: "+resultList.size());
		if(resultList.size()>=returnNum || logs.size()<returnNum){
			return resultList;
		}else
		{
			getResultList(tempList,reader, startIndex, returnNum);
		}
		return resultList;
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
				
				if(null!=startTime && !"".equals(startTime) && null!=tempJson.getString("time")){				
					if(sdf.parse(tempJson.getString("time")).before(sdf.parse(startTime))){
						continue;
					}							
				}
				if(null!=endTime && !"".equals(endTime)  && null!=tempJson.getString("time")){				
					if(sdf.parse(tempJson.getString("time")).after(sdf.parse(endTime))){
						continue;
					}					
				}
				if(null!=prossNo && !"".equals(prossNo) && null!=tempJson.getString("pid")){
					
					if(!prossNo.equals(tempJson.getString("pid"))){
						continue;
					}							
				}
				if(null!=pre && !"".equals(pre) && null!=tempJson.getString("pre")){
					
					if(!pre.equals(tempJson.getString("pre"))){
						continue;
					}							
				}
				if(null!=level && !"".equals(level) && null!=tempJson.getString("level")){
					
					if(!level.equals(tempJson.getString("level").trim())){
						continue;
					}							
				}
				if(null!=context && !"".equals(context) && null!=tempJson.getString("log")){
					
					if(tempJson.getString("log").indexOf(context)<0){
						continue;
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
			logger.error(e);
		}	
		
		return returnList;
	}
	
	/**
	 * 根据日志标识返回日志文件全路径
	 * @author tianwenchao 2018-10-07 
	 * @param index 日志标识
	 * @return String
	 */
	private String getLogFileObject(String index){
		for(JSONObject fileObj : logFileList){
			if(index.equals(fileObj.getString("logIndex"))){
				return fileObj.getString("logPath");
			}
		}
		return "";
	}
		
	/**
	 * 获取log日志文件数组,目录为 class同级logs目录下文件
	 * 如果目录不存在返回null
	 * @author tianwenchao 2018-09-19
	 * @return 日志文件信息
	 */
	@RequestMapping("/getlogFiles")
	public void getlogFiles(HttpServletRequest request, HttpServletResponse response){	
		File f = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().replace("/WEB-INF/classes", "")+"/data/");	
		logger.info("begin to getlogFiles。logpath is:"+f.getPath());
		JSONObject resultObj = new JSONObject();
		if(f.exists() && f.isDirectory()){
			List<JSONObject> jsonList = new ArrayList<JSONObject>();
			JSONObject fileObj;
			int idx = 0;
			for(File afile : f.listFiles()){
				fileObj = new JSONObject();
				idx++;
                fileObj.put("logIndex", idx);
				fileObj.put("logPath", afile.getPath());
				fileObj.put("logName", afile.getName());
				jsonList.add(fileObj);
			}			
			resultObj.put("data", jsonList);
			resultObj.put("result", "success");
			//在返回结果同时，将日志文件列表缓存
			logFileList = jsonList;
		}else{
			resultObj.put("data", null);
			resultObj.put("result", "failed");
		}
		
		jsonUtil.out(response, resultObj.toJSONString());
		logger.info("end to getlogFiles。jsonList is:"+resultObj.toJSONString());
	}
	
	 /**
     * 为了确保前后台属性显示一直，日志显示模版从后台读取
     * @author guofeipeng 2018-10-7
     * @return 日志文件显示模版
     */
    @RequestMapping("/getlogTmplate")
    public void getlogTmplate(HttpServletRequest request, HttpServletResponse response)
    {  
        // TODO:请注意,前后台日志模版需要保持一致(后台解析属性一旦变更，这里的模版需要同步更新)
        // "<span class='log_text_aaa'>#AAA#:</span>" 
        // 这里的aaa表示后台日志的一个属性;AAA为属性名全大写,请保持风格一致,否则可能无法正常显示
        String logIndex = request.getParameter("logIndex");
        
        StringBuffer bf = new StringBuffer();
        //一行的span元素，用时间字符串为id
        bf.append("<span class='log_text' id='#TIME#'>");
        //独立的一个日志子属性
        bf.append("<span class='log_text_location'>#LOCATION#:</span> ");
        bf.append("<span class='log_text_time'>#TIME#</span> ");
        bf.append("<span class='log_text_pid'>#PID#</span> ");
        bf.append("<span class='log_text_level'>#LEVEL#</span> ");
        bf.append("<span class='log_text_tname'>#TNAME#</span> ");
        bf.append("<span class='log_text_content'>#CONTENT#</span> ");
        //单行结束+换行
        bf.append("</span><br/>");
        
        jsonUtil.out(response, bf.toString());   
    }
	
}
