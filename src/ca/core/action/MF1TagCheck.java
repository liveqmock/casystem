package ca.core.action;

import java.io.IOException;
import java.util.Map;

import net.sf.json.JSONObject;

import ca.core.db.TagAuthorize;
import ca.core.logic.ActionResultInfo;
import ca.core.logic.JsonHttpSession;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class MF1TagCheck extends ActionSupport
{
	public String execute()
	{			
		JSONObject output = new JSONObject();
		try
		{
			ActionContext context = ActionContext.getContext();
			@SuppressWarnings("rawtypes")
			Map parameters = context.getParameters();
			String[] params1 = (String[]) parameters.get("authCode");
			String authCode = new String(params1[0]);
			// 
			TagAuthorize taga = new TagAuthorize();
			taga.MF1TagAuthorize(authCode, output);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorCheckTag);
		}
		// 
		try {
			JsonHttpSession.outResult(output);
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		return null;
	}
}

