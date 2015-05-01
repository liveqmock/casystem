package ca.core.action;

import ca.core.db.APPFormatOperator;
import ca.core.logic.ActionResultInfo;
import ca.core.logic.JsonHttpSession;
import ca.core.logic.LogUtil;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONObject;

import java.io.IOException;

/**
 * Created by Administrator on 2014/6/14.
 */
public class APPFormatAction  extends ActionSupport {
    public String add() {
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
            LogUtil.debug(LogUtil.LogAPPFormat, LogUtil.LogAPPFormat, this.getClass().getName(), msg);

            APPFormatOperator lo = new APPFormatOperator();
            lo.add(input, output);
        } catch (Exception e) {
            e.printStackTrace();
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorAddAPPFormat);
        }
        try
        {
            String msg = LogUtil.MsgOutput;
            msg += output.toString();
            LogUtil.debug(LogUtil.LogAPPFormat, LogUtil.LogAPPFormat, this.getClass().getName(), msg);
            JsonHttpSession.outResult(output);
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
        return null;
    }

    public String delete() {
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
            LogUtil.debug(LogUtil.LogAPPFormat, LogUtil.LogAPPFormat, this.getClass().getName(), msg);

            APPFormatOperator lo = new APPFormatOperator();
            lo.delete(input, output);
        } catch (Exception e) {
            e.printStackTrace();
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorDelAPPFormat);
        }
        try
        {
            String msg = LogUtil.MsgOutput;
            msg += output.toString();
            LogUtil.debug(LogUtil.LogAPPFormat, LogUtil.LogAPPFormat, this.getClass().getName(), msg);
            JsonHttpSession.outResult(output);
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
        return null;
    }
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
            LogUtil.debug(LogUtil.LogAPPFormat, LogUtil.LogAPPFormat, this.getClass().getName(), msg);

            APPFormatOperator lo = new APPFormatOperator();
            lo.modify(input, output);
        } catch (Exception e) {
            e.printStackTrace();
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorModifyAPPFormat);
        }
        try
        {
            String msg = LogUtil.MsgOutput;
            msg += output.toString();
            LogUtil.debug(LogUtil.LogAPPFormat, LogUtil.LogAPPFormat, this.getClass().getName(), msg);
            JsonHttpSession.outResult(output);
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
        return null;
    }
    public String merge() {
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
            LogUtil.debug(LogUtil.LogAPPFormat, LogUtil.LogAPPFormat, this.getClass().getName(), msg);

            APPFormatOperator lo = new APPFormatOperator();
            lo.merge(input, output);
        } catch (Exception e) {
            e.printStackTrace();
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorModifyAPPFormat);
        }
        try
        {
            String msg = LogUtil.MsgOutput;
            msg += output.toString();
            LogUtil.debug(LogUtil.LogAPPFormat, LogUtil.LogAPPFormat, this.getClass().getName(), msg);
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
            LogUtil.debug(LogUtil.LogAPPFormat, LogUtil.LogAPPFormat, this.getClass().getName(), msg);

            APPFormatOperator lo = new APPFormatOperator();
            lo.query(input, output);
        } catch (Exception e) {
            e.printStackTrace();
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorQueryAPPFormat);
        }
        try
        {
            String msg = LogUtil.MsgOutput;
            msg += output.toString();
            LogUtil.debug(LogUtil.LogAPPFormat, LogUtil.LogAPPFormat, this.getClass().getName(), msg);
            JsonHttpSession.outResult(output);
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
        return null;
    }
}
