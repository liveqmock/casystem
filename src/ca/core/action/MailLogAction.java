package ca.core.action;

import ca.core.db.MailLogOperator;
import ca.core.logic.ActionResultInfo;
import ca.core.logic.JsonHttpSession;
import ca.core.logic.LogUtil;
import net.sf.json.JSONObject;

import java.io.IOException;

/**
 * Created by Administrator on 2014/6/18.
 */
public class MailLogAction {
    public String modify() {
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
            LogUtil.debug(LogUtil.LogMailLog, LogUtil.LogMailLog, this.getClass().getName(), msg);

            MailLogOperator lo = new MailLogOperator();
            lo.modify(input, output);
        } catch (Exception e) {
            e.printStackTrace();
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorModifyMailLog);
        }
        try
        {
            String msg = LogUtil.MsgOutput;
            msg += output.toString();
            LogUtil.debug(LogUtil.LogMailLog, LogUtil.LogMailLog, this.getClass().getName(), msg);
            JsonHttpSession.outResult(output);
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
        return null;
    }
    public String query() {
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
            LogUtil.debug(LogUtil.LogMailLog, LogUtil.LogMailLog, this.getClass().getName(), msg);

            MailLogOperator lo = new MailLogOperator();
            lo.query(input, output);
        } catch (Exception e) {
            e.printStackTrace();
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorQueryMailLog);
        }
        try
        {
            String msg = LogUtil.MsgOutput;
            msg += output.toString();
            LogUtil.debug(LogUtil.LogMailLog, LogUtil.LogMailLog, this.getClass().getName(), msg);
            JsonHttpSession.outResult(output);
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
        return null;
    }
    public String query_group() {
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
            LogUtil.debug(LogUtil.LogMailLog, LogUtil.LogMailLog, this.getClass().getName(), msg);

            MailLogOperator lo = new MailLogOperator();
            lo.query_group(input, output);
        } catch (Exception e) {
            e.printStackTrace();
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorQueryMailLog);
        }
        try
        {
            String msg = LogUtil.MsgOutput;
            msg += output.toString();
            LogUtil.debug(LogUtil.LogMailLog, LogUtil.LogMailLog, this.getClass().getName(), msg);
            JsonHttpSession.outResult(output);
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
        return null;
    }
}
