package ca.core.action;

import ca.core.db.ReportOperator;
import ca.core.logic.ActionResultInfo;
import ca.core.logic.JsonHttpSession;
import ca.core.logic.LogUtil;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONObject;

import java.io.IOException;

/**
 * Created by Administrator on 2014/7/2.
 */
public class ReportAction extends ActionSupport {
    public String query_rpt() {
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
            LogUtil.debug(LogUtil.LogReportLog, LogUtil.LogReportLog, this.getClass().getName(), msg);

            ReportOperator lo = new ReportOperator();

            if(input.containsKey("flag")){
                int flag=input.getInt("flag");
                switch(flag)
                {
                    case 1:
                        lo.query_producttype_rpt(input, output);
                        break;
                    case 2:
                        lo.query_inspect_rpt(input, output);
                        break;
                    case 3:
                        lo.query_logisticcode_rpt(input, output);
                        break;
                    case 4:
                        lo.query_logisticid_rpt(input, output);
                        break;
                    default:
                        output.put("Result", ActionResultInfo.Fail);
                        output.put("FailInfo", ActionResultInfo.ErrorQueryReportErrorParameter);
                        break;
                }
            }else {
                output.put("Result", ActionResultInfo.Fail);
                output.put("FailInfo", ActionResultInfo.ErrorQueryReportNoParameter);
            }

        } catch (Exception e) {
            e.printStackTrace();
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorQueryReport);
        }
        try
        {
            String msg = LogUtil.MsgOutput;
            msg += output.toString();
            LogUtil.debug(LogUtil.LogReportLog, LogUtil.LogReportLog, this.getClass().getName(), msg);
            JsonHttpSession.outResult(output);
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
        return null;
    }

    public String query_producttype_rpt() {
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
            LogUtil.debug(LogUtil.LogReportLog, LogUtil.LogReportLog, this.getClass().getName(), msg);

            ReportOperator lo = new ReportOperator();
            lo.query_producttype_rpt(input, output);
        } catch (Exception e) {
            e.printStackTrace();
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorQueryReport);
        }
        try
        {
            String msg = LogUtil.MsgOutput;
            msg += output.toString();
            LogUtil.debug(LogUtil.LogReportLog, LogUtil.LogReportLog, this.getClass().getName(), msg);
            JsonHttpSession.outResult(output);
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
        return null;
    }

    public String query_inspect_rpt() {
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
            LogUtil.debug(LogUtil.LogReportLog, LogUtil.LogReportLog, this.getClass().getName(), msg);

            ReportOperator lo = new ReportOperator();
            lo.query_producttype_rpt(input, output);
        } catch (Exception e) {
            e.printStackTrace();
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorQueryReport);
        }
        try
        {
            String msg = LogUtil.MsgOutput;
            msg += output.toString();
            LogUtil.debug(LogUtil.LogReportLog, LogUtil.LogReportLog, this.getClass().getName(), msg);
            JsonHttpSession.outResult(output);
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
        return null;
    }

    public String query_logisticcode_rpt() {
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
            LogUtil.debug(LogUtil.LogReportLog, LogUtil.LogReportLog, this.getClass().getName(), msg);

            ReportOperator lo = new ReportOperator();
            lo.query_producttype_rpt(input, output);
        } catch (Exception e) {
            e.printStackTrace();
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorQueryReport);
        }
        try
        {
            String msg = LogUtil.MsgOutput;
            msg += output.toString();
            LogUtil.debug(LogUtil.LogReportLog, LogUtil.LogReportLog, this.getClass().getName(), msg);
            JsonHttpSession.outResult(output);
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
        return null;
    }

    public String query_logisticid_rpt() {
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
            LogUtil.debug(LogUtil.LogReportLog, LogUtil.LogReportLog, this.getClass().getName(), msg);

            ReportOperator lo = new ReportOperator();
            lo.query_producttype_rpt(input, output);
        } catch (Exception e) {
            e.printStackTrace();
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorQueryReport);
        }
        try
        {
            String msg = LogUtil.MsgOutput;
            msg += output.toString();
            LogUtil.debug(LogUtil.LogReportLog, LogUtil.LogReportLog, this.getClass().getName(), msg);
            JsonHttpSession.outResult(output);
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
        return null;
    }
}
