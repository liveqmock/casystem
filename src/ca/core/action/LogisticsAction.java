package ca.core.action;

import java.io.IOException;

import org.json.JSONArray;

import net.sf.json.JSONObject;
import ca.core.db.LogisticsOperator;
import ca.core.logic.ActionResultInfo;
import ca.core.logic.JsonHttpSession;
import ca.core.logic.LogUtil;
import ca.core.logic.UserSessionUtil;

import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class LogisticsAction extends ActionSupport {
	
	public String logisticsAdd() {
		JSONObject output = new JSONObject();
		try {
			JSONObject input = JsonHttpSession.getJsonInput("json");
			// Check UserSession
//			if (UserSessionUtil.checkSession(output) < 0)
//			{
//				JsonHttpSession.outResult(output);
//				return null;
//			}
			String msg = LogUtil.MsgInput;
			msg += input.toString();
			System.out.println("msg=1="+msg);
			LogUtil.debug(LogUtil.LogLogistics, LogUtil.TypeIDNone, this.getClass().getName(), msg);
			
			LogisticsOperator lo = new LogisticsOperator();
			
			lo.addSingle(input, output);
			
		} catch (Exception e) {
			e.printStackTrace();
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorModifyBatch);
		}
		
		try 
		{
			
			String msg = LogUtil.MsgOutput;
			msg += output.toString();
			LogUtil.debug(LogUtil.LogLogistics, LogUtil.LogLogistics, this.getClass().getName(), msg);
			
			JsonHttpSession.outResult(output);
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		return null;
	}
	
	public String logisticsQry() {
		JSONObject output = new JSONObject();
		try {
			JSONObject input = JsonHttpSession.getJsonInput("json");
			// Check UserSession
//			if (UserSessionUtil.checkSession(output) < 0)
//			{
//				JsonHttpSession.outResult(output);
//				return null;
//			}
			String msg = LogUtil.MsgInput;
			msg += input.toString();
			LogUtil.debug(LogUtil.LogLogistics, LogUtil.TypeIDNone, this.getClass().getName(), msg);
			
			LogisticsOperator lo = new LogisticsOperator();
			lo.query(input, output);
		} catch (Exception e) {
			e.printStackTrace();
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorModifyBatch);
		}
		
		try 
		{
			
			String msg = LogUtil.MsgOutput;
			msg += output.toString();
			LogUtil.debug(LogUtil.LogLogistics, LogUtil.LogLogistics, this.getClass().getName(), msg);
			
			JsonHttpSession.outResult(output);
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		return null;
	}

    public String logisticsQrybyinspect() {
        JSONObject output = new JSONObject();
        try {
            JSONObject input = JsonHttpSession.getJsonInput("json");
            // Check UserSession
//			if (UserSessionUtil.checkSession(output) < 0)
//			{
//				JsonHttpSession.outResult(output);
//				return null;
//			}
            String msg = LogUtil.MsgInput;
            msg += input.toString();
            LogUtil.debug(LogUtil.LogLogistics, LogUtil.TypeIDNone, this.getClass().getName(), msg);

            LogisticsOperator lo = new LogisticsOperator();
            lo.querybyinspect(input, output);
        } catch (Exception e) {
            e.printStackTrace();
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorModifyBatch);
        }

        try
        {

            String msg = LogUtil.MsgOutput;
            msg += output.toString();
            LogUtil.debug(LogUtil.LogLogistics, LogUtil.LogLogistics, this.getClass().getName(), msg);

            JsonHttpSession.outResult(output);
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
        return null;
    }

    public String logisticsMod() {
		JSONObject output = new JSONObject();
		try {
			JSONObject input = JsonHttpSession.getJsonInput("json");
			//System.out.println("logisticsMod==input="+input);
			// Check UserSession
//			if (UserSessionUtil.checkSession(output) < 0)
//			{
//				JsonHttpSession.outResult(output);
//				return null;
//			}
			String msg = LogUtil.MsgInput;
			msg += input.toString();
			LogUtil.debug(LogUtil.LogLogistics, LogUtil.TypeIDNone, this.getClass().getName(), msg);
			
			LogisticsOperator lo = new LogisticsOperator();
			lo.modifySingle(input, output);
		} catch (Exception e) {
			e.printStackTrace();
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorModifyBatch);
		}
		
		try 
		{
			
			String msg = LogUtil.MsgOutput;
			msg += output.toString();
			LogUtil.debug(LogUtil.LogLogistics, LogUtil.LogLogistics, this.getClass().getName(), msg);
			
			JsonHttpSession.outResult(output);
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		return null;
	}
	public String vptypeQrybyinspect(){
        JSONObject output = new JSONObject();
        try {
            JSONObject input = JsonHttpSession.getJsonInput("json");
            // Check UserSession
//			if (UserSessionUtil.checkSession(output) < 0)
//			{
//				JsonHttpSession.outResult(output);
//				return null;
//			}
            String msg = LogUtil.MsgInput;
            msg += input.toString();
            LogUtil.debug(LogUtil.LogLogistics, LogUtil.TypeIDNone, this.getClass().getName(), msg);

            LogisticsOperator lo = new LogisticsOperator();
            lo.vtypequerybyinspect(input, output);
        } catch (Exception e) {
            e.printStackTrace();
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorModifyBatch);
        }

        try
        {

            String msg = LogUtil.MsgOutput;
            msg += output.toString();
            LogUtil.debug(LogUtil.LogLogistics, LogUtil.LogLogistics, this.getClass().getName(), msg);

            JsonHttpSession.outResult(output);
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
        return null;
    }
}
