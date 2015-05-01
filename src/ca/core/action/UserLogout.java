package ca.core.action;

import ca.core.db.Users;
import ca.core.db.UsersOperator;
import ca.core.logic.ActionResult;
import ca.core.logic.ActionResultInfo;
import ca.core.logic.JsonHttpSession;
import ca.core.logic.LogUtil;

import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class UserLogout extends ActionSupport
{
	public String execute()
	{
		ActionResult actionResult = new ActionResult();
		try
		{
			Users user = (Users) JsonHttpSession.getInput("json", Users.class);
			UsersOperator uo = new UsersOperator();
			uo.userLogout(user, actionResult);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			actionResult.setResult(ActionResultInfo.Fail);
			actionResult.setFailInfo(ActionResultInfo.ErrorLogout);
		}
		
		String msg = LogUtil.MsgOutput;
		msg += JsonHttpSession.result2Json(actionResult, "User").toString();
		LogUtil.debug(LogUtil.LogTypeUser, LogUtil.TypeIDNone, this.getClass().getName(), msg);
		
		// 
		JsonHttpSession.outResult(actionResult, "User");
		return null;
	}
}
