package ca.core.action;

import java.io.IOException;

import net.sf.json.JSONObject;
import ca.core.db.LogOperator;
import ca.core.logic.ActionResultInfo;
import ca.core.logic.JsonHttpSession;

public class LogQuery
{	
	public String execute()
	{
		JSONObject output = new JSONObject();
		try
		{
			JSONObject input = JsonHttpSession.getJsonInput("json");
			//调用数据处理类 
			LogOperator lo = new LogOperator();
			lo.logQuery(input, output);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorLogQuery);
		}
		// 输出结果
		try 
		{
			JsonHttpSession.outResult(output);
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		return null;
	}
}
