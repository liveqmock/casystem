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
public class CTagCheck extends ActionSupport
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
			taga.CTagAuthorize_part(authCode, output);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorCheckTag);
		}
		// 
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

    public String tcodeAuth(){
        JSONObject output = new JSONObject();
        try
        {
            ActionContext context = ActionContext.getContext();
            @SuppressWarnings("rawtypes")
            Map parameters = context.getParameters();
            String[] params1 = (String[]) parameters.get("authCode");
            String authCode = new String(params1[0]);
            String[] params2 = (String[]) parameters.get("coatCode");
            String coatCode = new String(params2[0]);
            //
            TagAuthorize taga = new TagAuthorize();
            taga.CoatCodeAuthorize(authCode, coatCode,output);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorCheckTag);
        }
        //
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

