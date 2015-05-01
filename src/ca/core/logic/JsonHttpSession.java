package ca.core.logic;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;

public class JsonHttpSession {
	
	@SuppressWarnings("rawtypes")
	public static Object getInput(String father, Class clazz)
	{
		ActionContext context = ActionContext.getContext();
		Map parameters = context.getParameters();
		
		//以JSON格式输入的数据获取
		String[] params1 = (String[]) parameters.get(father);
		String strjson = new String(params1[0]);
		
		return FromJsonUtil.getDTO(strjson, clazz);
	}
	
	@SuppressWarnings("rawtypes")
	public static String getJsonInput() throws UnsupportedEncodingException
	{
		ActionContext context = ActionContext.getContext();
		Map parameters = context.getParameters();
		String[] params = (String[]) parameters.get("json");
		String strJson = new String(params[0]);
		return strJson;
	}
	
	public static JSONObject getJsonInput(String father) throws UnsupportedEncodingException
	{
		ActionContext context = ActionContext.getContext();
		@SuppressWarnings("rawtypes")
		Map parameters = context.getParameters();
		// 
		String[] params = (String[]) parameters.get(father);
		String strJson = new String(params[0]);
		JSONObject input = JSONObject.fromObject(strJson);		
		return input;
	}
	
	public static void outResult(JSONObject output) throws UnsupportedEncodingException, IOException
	{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.getOutputStream().write((output.toString().getBytes("utf-8")));
        response.getOutputStream().close();
	}
	
	public static void outResult(ActionResult result, String strData)
	{
		JSONObject output = new JSONObject();
		output.put("Result", result.getResult());
		output.put("FailInfo", result.getFailInfo());
		Object obj = result.getData();
	    if (obj == null) { 
	    } else if (obj instanceof String ||
	    		obj instanceof Integer || 
	    		obj instanceof Float  || 
	    		obj instanceof Boolean || 
	    		obj instanceof Short || 
	    		obj instanceof Double || 
	    		obj instanceof Long || 
	    		obj instanceof BigDecimal || 
	    		obj instanceof BigInteger || 
	    		obj instanceof Byte) {
	    	output.put(strData, result.getData());
	    }
	    else {
	    	String strResult = ToJsonUtil.objectTojson(result.getData());
	    	output.put(strData, strResult);
	    }
		
		try {
			
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.getOutputStream().write((output.toString().getBytes("utf-8")));
	        response.getOutputStream().close();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
	}

	public static JSONObject result2Json(ActionResult result, String strData)
	{
		JSONObject output = new JSONObject();
		output.put("Result", result.getResult());
		output.put("FailInfo", result.getFailInfo());
		Object obj = result.getData();
	    if (obj == null) { 
	    } else if (obj instanceof String ||
	    		obj instanceof Integer || 
	    		obj instanceof Float  || 
	    		obj instanceof Boolean || 
	    		obj instanceof Short || 
	    		obj instanceof Double || 
	    		obj instanceof Long || 
	    		obj instanceof BigDecimal || 
	    		obj instanceof BigInteger || 
	    		obj instanceof Byte) {
	    	output.put(strData, result.getData());
	    }
	    else {
	    	String strResult = ToJsonUtil.objectTojson(result.getData());
	    	output.put(strData, strResult);
	    }
	    return output;
	}
}
