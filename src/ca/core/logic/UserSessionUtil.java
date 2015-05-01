package ca.core.logic;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import ca.core.db.SessionOperator;

public class UserSessionUtil {

	
	public static Integer checkSession (JSONObject output)
	{
		// 0. 用户是否登录
		Integer userID;
		try
		{
//			HttpSession httpsession = ServletActionContext.getRequest().getSession();
//			userID = (Integer) httpsession.getAttribute("UserID");
			String token = ServletActionContext.getRequest().getHeader(SessionOperator.HeaderTokenName);
			if (token == null)
			{
				output.put("Result", ActionResultInfo.Fail);
				output.put("FailInfo", ActionResultInfo.ErrorUserOffline);
				return -1;
			}

			SessionOperator sOperator = new SessionOperator();
			if (!sOperator.sessionUpdate(token))
			{
				output.put("Result", ActionResultInfo.Fail);
				output.put("FailInfo", ActionResultInfo.ErrorUserOffline);
				return -1;
			}

			userID = sOperator.SessionUserId(token);

		}
		catch (Exception e)
		{
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorUserOffline);
			return -1;
		}
		return userID;
	}

}
