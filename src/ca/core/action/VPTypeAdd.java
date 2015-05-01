package ca.core.action;

import net.sf.json.JSONObject;

import ca.core.db.VPTypeOperator;
import ca.core.logic.ActionResult;
import ca.core.logic.ActionResultInfo;
import ca.core.logic.JsonHttpSession;
import ca.core.logic.LogUtil;
import ca.core.logic.UserSessionUtil;

import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class VPTypeAdd extends ActionSupport
{
	public String execute()
	{
		ActionResult actionResult = new ActionResult();
		try
		{
			JSONObject input = JsonHttpSession.getJsonInput("json");
			JSONObject output = new JSONObject();
			
			// Check UserSession
			if (UserSessionUtil.checkSession(output) < 0)
			{
				JsonHttpSession.outResult(output);
				return null;
			}
			
			String msg = LogUtil.MsgInput;
			msg += input.toString();
			LogUtil.debug(LogUtil.LogTypeVPType, LogUtil.TypeIDNone, this.getClass().getName(), msg);
			
			VPTypeOperator vpto = new VPTypeOperator();
			vpto.VPTypeAdd(input, actionResult);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			actionResult.setResult(ActionResultInfo.Fail);
			actionResult.setFailInfo(ActionResultInfo.ErrorAddVPType);
		}
		
		String msg = LogUtil.MsgOutput;
		msg += JsonHttpSession.result2Json(actionResult, "VPType").toString();
		LogUtil.debug(LogUtil.LogTypeVPType, LogUtil.TypeIDNone, this.getClass().getName(), msg);
		
		// 
		JsonHttpSession.outResult(actionResult, "VPType");
		return null;
	}
	
	public String apply() {
		ActionResult actionResult = new ActionResult();
		try
		{
			JSONObject input = JsonHttpSession.getJsonInput("json");
			JSONObject output = new JSONObject();
			
			// Check UserSession
			if (UserSessionUtil.checkSession(output) < 0)
			{
				JsonHttpSession.outResult(output);
				return null;
			}
			
			String msg = LogUtil.MsgInput;
			msg += input.toString();
			LogUtil.debug(LogUtil.LogTypeVPType, LogUtil.TypeIDNone, this.getClass().getName(), msg);
			
			VPTypeOperator vpto = new VPTypeOperator();
			vpto.apply(input, actionResult);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			actionResult.setResult(ActionResultInfo.Fail);
			actionResult.setFailInfo(ActionResultInfo.ErrorAddVPType);
		}
		
		String msg = LogUtil.MsgOutput;
		msg += JsonHttpSession.result2Json(actionResult, "VPType").toString();
		LogUtil.debug(LogUtil.LogTypeVPType, LogUtil.TypeIDNone, this.getClass().getName(), msg);
		
		// 
		JsonHttpSession.outResult(actionResult, "VPType");
		return null;
	}
	
}
