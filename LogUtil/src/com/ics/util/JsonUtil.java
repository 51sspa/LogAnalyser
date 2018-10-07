package com.ics.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;

@Repository(value = "JsonUtil")
public class JsonUtil
{
	/**
	 * 返回信息
	 * 
	 * @param response
	 *            响应
	 * @param msg
	 *            信息
	 */
	public void out(HttpServletResponse response, String msg)
	{
		if (null == response)
		{
			return;
		}
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter write = null;
		try
		{
			write = response.getWriter();
			write.write(msg);
		} catch (IOException e)
		{
			System.out.println(e.getMessage());
		} finally
		{
			if (null != write)
			{
				write.flush();
				write.close();
			}
		}
	}

	/**
	 * 获取请求参数
	 * 
	 * @param request
	 * @return
	 */
	public JSONObject getParamters(HttpServletRequest request)
	{
		JSONObject json = null;
		BufferedReader br = null;
		try
		{
			br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream(), "utf-8"));
			StringBuffer sb = new StringBuffer();
			String temp;
			while ((temp = br.readLine()) != null)
			{
				sb.append(temp);
			}

			json = JSONObject.parseObject(sb.toString());

		} catch (Exception e)
		{
			System.out.println(e.getMessage());
		} finally
		{
			if (br != null)
			{
				try
				{
					br.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}

		return json;
	}
}
