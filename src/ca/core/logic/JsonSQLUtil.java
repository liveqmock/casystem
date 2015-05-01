package ca.core.logic;

import java.util.Iterator;

import net.sf.json.JSONObject;

public class JsonSQLUtil {

	/*
	 * 将input中包含的key:value转换成SQL Where子句
	 * 不支持多级JSON。
	 */
	public static String JSON2SQLWhere(boolean bfirst, JSONObject input)
	{
		String sql = " ";
		@SuppressWarnings("unchecked")
		Iterator<String> keys = input.keys();  
		Object o;  
		String key;
		while(keys.hasNext())
		{
			if (!bfirst)
			{
				sql += " and ";
			}
			key = keys.next();
			o = input.get(key);
			if(o instanceof String)
			{
				sql += key + "='" + o + "'";
			}
			else // Integer, Long, Double
			{
				if("InspectRemain".toLowerCase().equals(key.toLowerCase())){
				     sql += key +  ">=" + o+" ";
				}else{
					 sql += key +  "=" + o+" ";
				}
			}
			bfirst = false;
		}
		return sql;
	}
	
	/*
	 * 将input中包含的key:value转换成SQL SET子句
	 * 不支持多级JSON。
	 */
	public static String JSON2SQLSet(JSONObject input)
	{
		String sql = " ";
		@SuppressWarnings("unchecked")
		Iterator<String> keys = input.keys();  
		Object o;  
		String key;
		boolean bfirst = true;
		
		while(keys.hasNext())
		{
			if (!bfirst)
			{
				sql += " , ";
			}
			key = keys.next();
			o = input.get(key);
			if(o instanceof String)
			{
				sql += key + "='" + o + "'"; 
			}
			else //if (o instanceof Integer/Long/Double)
			{
				sql += key +  "=" + o;
			}
			bfirst = false;
		}
		return sql;
	}
}
