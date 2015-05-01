package ca.core.action;

import net.sf.json.JSONObject;

import ca.core.db.VendorOperator;
import ca.core.logic.ActionResult;
import ca.core.logic.ActionResultInfo;
import ca.core.logic.JsonHttpSession;
import ca.core.logic.LogUtil;
import ca.core.logic.UserSessionUtil;

import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class VendorModify extends ActionSupport
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
			LogUtil.debug(LogUtil.LogTypeVendor, LogUtil.TypeIDNone, this.getClass().getName(), msg);
			
			VendorOperator vo = new VendorOperator();
			vo.VendorModify(input, actionResult);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			actionResult.setResult(ActionResultInfo.Fail);
			actionResult.setFailInfo(ActionResultInfo.ErrorModifyVendor);
		}
		
		String msg = LogUtil.MsgOutput;
		msg += JsonHttpSession.result2Json(actionResult, "Vendors").toString();
		LogUtil.debug(LogUtil.LogTypeVendor, LogUtil.TypeIDNone, this.getClass().getName(), msg);
		
		JsonHttpSession.outResult(actionResult, "Vendors");
		return null;
	}
}
