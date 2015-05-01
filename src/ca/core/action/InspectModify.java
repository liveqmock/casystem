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
public class InspectModify extends ActionSupport
{
    public String delete(){
        JSONObject output = new JSONObject();
        try
        {
            JSONObject input = JsonHttpSession.getJsonInput("json");

            int ret = UserSessionUtil.checkSession(output);
            // Check UserSession
            if (ret < 0)
            {
                JsonHttpSession.outResult(output);
                return null;
            }
            input.put("UserID", ret);
            String msg = LogUtil.MsgInput;
            msg += input.toString();
            LogUtil.debug(LogUtil.LogTypeTag, LogUtil.TypeIDNone, this.getClass().getName(), msg);

            InspectOperator inspectOp = new InspectOperator();
            inspectOp.InspectDelete(input, output);
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
            LogUtil.debug(LogUtil.LogTypeTag, LogUtil.TypeIDNone, this.getClass().getName(), msg);

            JsonHttpSession.outResult(output);
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
        return null;
    }

	public String execute()
	{
		JSONObject output = new JSONObject();
		try
		{
			JSONObject input = JsonHttpSession.getJsonInput("json");
			
			int ret = UserSessionUtil.checkSession(output);
			// Check UserSession
			if (ret < 0)
			{
				JsonHttpSession.outResult(output);
				return null;
			}
			input.put("UserID", ret);
			String msg = LogUtil.MsgInput;
			msg += input.toString();
			LogUtil.debug(LogUtil.LogTypeTag, LogUtil.TypeIDNone, this.getClass().getName(), msg);
			
			InspectOperator inspectOp = new InspectOperator();
			inspectOp.InspectModify(input, output);
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
			LogUtil.debug(LogUtil.LogTypeTag, LogUtil.TypeIDNone, this.getClass().getName(), msg);
			
			JsonHttpSession.outResult(output);
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		return null;
	}
}
