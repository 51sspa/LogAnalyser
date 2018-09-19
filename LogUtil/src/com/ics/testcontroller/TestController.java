package com.ics.testcontroller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.ics.util.JsonUtil;

@Controller
@RequestMapping("/test")
public class TestController
{
	@Resource(name = "JsonUtil")
	private JsonUtil jsonUtil = null;

	@RequestMapping("/sayHello")
	public void hello(HttpServletRequest request, HttpServletResponse response)
	{
		JSONObject json = jsonUtil.getParamters(request);
		String fileName = json.getString("fileName");
		String processName = json.getString("processName");

		System.out.println("接收到参数：fileName = " + fileName + "  processName = " + processName);

		JSONObject obj = new JSONObject();
		obj.put("success", true);
		obj.put("msg", "我已收到请求..");

		jsonUtil.out(response, obj.toJSONString());
	}
}
