package com.tians.datachace;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class DataFactory {
	
	/** ���±���������һ�β�ѯ����   */
	/** ��ʶ��־�ļ�  */
	private static String logFileID = "111";
	/** ��ѯ��־��ʼʱ��  */
	private static String startTime = "";
	/** ��ѯ��־����ʱ��  */
	private static String endTime = "";
	/** ��ѯ��־���̺�  */
	private static String prossNo = "";
	/** ��־����  */
	private static String level = "";
	/** ǰ׺��ʶ  */
	private static String pre = "";
	/** ���ݹؼ���  */
	private static String context = "";
	
	public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");

	public DataFactory(){}
	

	/**
	 * ��������ǵ����ѯ��ťʱ����
	 * ����������ȡjson���� Ĭ�Ϸ���������������෵��һ����
	 * @author tianwenchao 2018-09-12
	 * @param logFileID ��־�ļ�ID��ʶΨһ
	 * @param startTime ���˿�ʼʱ��
	 * @param endTime ���˽���ʱ��
	 * @param prossNo ���̺�
	 * @param level ��־����
	 * @param pre ��־ǰ׺ �� [org.jboss.seam.Component]
	 * @param context ���ݹؼ��� ģ��ƥ��
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
		
		/** �˴�Ϊ�������ݣ������滻��С�ĵ�App.readLog ���� */
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
	 * ��������ǹ���������ʱ����
	 * ����json����
	 * @author tianwenchao 2018-09-16
	 * @param aLogFileID ��־�ļ���ʶ����ȡ�ĸ���־�ļ�
	 * @param  startindex  ��ʼλ������
	 * @param  rowcount  ������־����
	 * @return JSONObject  
	 */
	public JSONObject getLogInfosByScroll(String aLogFileID,long startindex,int rowcount){
		
		logFileID = aLogFileID;	
				
		JSONObject fileJson = new JSONObject();
		/** �˴�Ϊ�������ݣ������滻��С�ĵ�App.readLog ���� */
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
	 * ����ԭʼ���ݼ��Ϲ���������־
	 * @param sourceList ԭʼ���ݼ��ϣ��Ӻ�̨��ȡ���ģ�
	 * @param maxLimit ��󷵻���������
	 * @return
	 */
	private List<JSONObject> dataFilter(List<JSONObject> sourceList,int maxLimit){
		List<JSONObject> returnList = new ArrayList<JSONObject>(maxLimit);
		if(null == sourceList || 0== sourceList.size()){
			return returnList;
		}
				
		int jsonNum = 0;
		try {
			//���й��˾����÷����жϣ�������һ���������������ѭ����Ѱ����һ��
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
	 * ����json����
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
