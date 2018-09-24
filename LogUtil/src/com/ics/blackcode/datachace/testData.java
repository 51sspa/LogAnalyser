package com.ics.blackcode.datachace;

import java.io.File;
import java.util.Date;

public class testData {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//System.out.println("【startTime】:"+new Date().getTime());	
		
		//DataFactory.getLogInfosByCon("", null, null, null, null, null, null);
		
		//System.out.println("【endTime】"+new Date().getTime());
		DataFactory dd = new DataFactory();
		File[] fs = dd.getlogFiles();
		if(null==fs){
			System.out.println("无法找到日志文件");
		}else{
			for(File aa : fs){
				System.out.println(aa.getName()+" : "+aa.getPath());
			}
		}
	}

}
