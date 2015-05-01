package ca.core.action;

import net.sf.json.JSONObject;
import ca.core.db.VendorOperator;
import ca.core.db.Vendors;
import ca.core.logic.ActionResult;
import ca.core.logic.ActionResultInfo;
import ca.core.logic.JsonHttpSession;
import ca.core.logic.LogUtil;
import ca.core.logic.UserSessionUtil;

import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class VendorAdd extends ActionSupport
{
	public String execute()
	{
		ActionResult actionResult = new ActionResult();
		try
		{
			JSONObject output = new JSONObject();
			
			// Check UserSession
			if (UserSessionUtil.checkSession(output) < 0)
			{
				JsonHttpSession.outResult(output);
				return null;
			}
			
			Vendors dbVendor = (Vendors)JsonHttpSession.getInput("json", Vendors.class);

			String msg = LogUtil.MsgInput;
			msg += dbVendor.getClass().getName();
			LogUtil.debug(LogUtil.LogTypeVendor, LogUtil.TypeIDNone, this.getClass().getName(), msg);
			
			VendorOperator vo = new VendorOperator();
			vo.addVendor(dbVendor, actionResult);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			actionResult.setResult(ActionResultInfo.Fail);
			actionResult.setFailInfo(ActionResultInfo.ErrorAddVendor);
		}
		
		String msg = LogUtil.MsgOutput;
		msg += JsonHttpSession.result2Json(actionResult, "VendorID").toString();
		LogUtil.debug(LogUtil.LogTypeVendor, LogUtil.TypeIDNone, this.getClass().getName(), msg);
		
		// 
		JsonHttpSession.outResult(actionResult, "VendorID");
		return null;
	}
}
