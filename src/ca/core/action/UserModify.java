package ca.core.action;

import net.sf.json.JSONObject;
import ca.core.db.UsersOperator;
import ca.core.logic.ActionResult;
import ca.core.logic.ActionResultInfo;
import ca.core.logic.JsonHttpSession;
import ca.core.logic.LogUtil;
import ca.core.logic.UserSessionUtil;

import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class UserModify extends ActionSupport
{
	
	public String execute()
	{
		ActionResult actionResult = new ActionResult();
		try
		{
			String input = JsonHttpSession.getJsonInput();
			JSONObject output = new JSONObject();
			
			// Check UserSession
			if (UserSessionUtil.checkSession(output) < 0)
			{
				JsonHttpSession.outResult(output);
				return null;
			}
			
			String msg = LogUtil.MsgInput;
			msg += input.toString();
			LogUtil.debug(LogUtil.LogTypeUser, LogUtil.TypeIDNone, this.getClass().getName(), msg);
			
			UsersOperator uo = new UsersOperator();
			uo.modifyUser(input, actionResult);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			actionResult.setResult(ActionResultInfo.Fail);
			actionResult.setFailInfo(ActionResultInfo.ErrorModifyUser);
		}
		
		String msg = LogUtil.MsgOutput;
		msg += JsonHttpSession.result2Json(actionResult, "User").toString();
		LogUtil.debug(LogUtil.LogTypeUser, LogUtil.TypeIDNone, this.getClass().getName(), msg);
		
		// 
		JsonHttpSession.outResult(actionResult, "User");
		return null;
	}
}
