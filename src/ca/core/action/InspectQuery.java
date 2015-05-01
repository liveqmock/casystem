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
public class InspectQuery extends ActionSupport
{	
	public String execute()
	{		
		JSONObject output = new JSONObject();
		try
		{
			JSONObject input = JsonHttpSession.getJsonInput("json");
			
			// Check UserSession
			if (UserSessionUtil.checkSession(output) < 0)
			{
				JsonHttpSession.outResult(output);
				return null;
			}
			
			
			String msg = LogUtil.MsgInput;
			msg += input.toString();
			LogUtil.debug(LogUtil.LogTypeTag, LogUtil.TypeIDNone, this.getClass().getName(), msg);
			//必须包含flag参数，flag=1出区核销查询未出区的报检单  flag=2局方查询报检单信息 flag=3企业查询报检单信息
			InspectOperator inspectOp = new InspectOperator();

            if(input.has("flag")){
                if(10==input.getInt("flag")){
                     inspectOp.InspectQueryOutnew(input, output);
                } else {
                     inspectOp.InspectQueryOut(input, output);
                }
            }else {
			   inspectOp.InspectQuery(input, output);
            }

		}
		catch (Exception e)
		{
			e.printStackTrace();
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorQueryBatch);
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
