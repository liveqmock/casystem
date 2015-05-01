package ca.core.action;

import java.io.IOException;

import net.sf.json.JSONObject;

import ca.core.db.InspectOperator;
import ca.core.logic.ActionResultInfo;
import ca.core.logic.JsonHttpSession;
import ca.core.logic.LogUtil;
import ca.core.logic.UserSessionUtil;

import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class InspectAdd extends ActionSupport
{
	public String execute()
	{
		JSONObject output = new JSONObject();
		try
		{
			JSONObject input = JsonHttpSession.getJsonInput("json");
			
			// Check UserSession
			if (UserSessionUtil.checkSession(output) < 0)
			{
				JsonHttpSession.outResult(output);
				return null;
			}
			
			String msg = LogUtil.MsgInput;
			msg += input.toString();
			LogUtil.debug(LogUtil.LogInspect, LogUtil.InspectNone, this.getClass().getName(), msg);
			
			InspectOperator inspectOp = new InspectOperator();
			inspectOp.InspectAdd(input, output);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorModifyBatch);
		}
		// 
		try 
		{
			
			String msg = LogUtil.MsgOutput;
			msg += output.toString();
			LogUtil.debug(LogUtil.LogInspect, LogUtil.InspectNone, this.getClass().getName(), msg);
			
			JsonHttpSession.outResult(output);
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		return null;
	}
}
