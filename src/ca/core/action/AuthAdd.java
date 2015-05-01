package ca.core.action;

import net.sf.json.JSONObject;

import ca.core.db.AuthOperator;
import ca.core.logic.ActionResult;
import ca.core.logic.ActionResultInfo;
import ca.core.logic.JsonHttpSession;
import ca.core.logic.LogUtil;
import ca.core.logic.UserSessionUtil;

import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class AuthAdd extends ActionSupport{
	
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
			LogUtil.debug(LogUtil.LogTypeAuth, LogUtil.TypeIDNone, this.getClass().getName(), msg);
			//调用数据处理类
			AuthOperator ao = new AuthOperator();
			ao.AuthAdd(input, actionResult);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			actionResult.setResult(ActionResultInfo.Fail);
			actionResult.setFailInfo(ActionResultInfo.ErrorAddAuth);
		}
		
		String msg = LogUtil.MsgOutput;
		msg += JsonHttpSession.result2Json(actionResult, "Auth").toString();
		LogUtil.debug(LogUtil.LogTypeAuth, LogUtil.TypeIDNone, this.getClass().getName(), msg);
		
		// 输出结果
		JsonHttpSession.outResult(actionResult, "Auth");
		return null;
	}
}
