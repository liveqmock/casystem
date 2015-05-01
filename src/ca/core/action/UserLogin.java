package ca.core.action;

import net.sf.json.JSONObject;

import ca.core.db.UsersOperator;
import ca.core.logic.ActionResult;
import ca.core.logic.ActionResultInfo;
import ca.core.logic.JsonHttpSession;
import ca.core.logic.LogUtil;

import com.opensymphony.xwork2.ActionSupport;

import java.io.IOException;

@SuppressWarnings("serial")
public class UserLogin extends ActionSupport
{
    public String execute()
    {
        ActionResult actionResult = new ActionResult();
        try
        {
            JSONObject input = JsonHttpSession.getJsonInput("json");

            String msg = LogUtil.MsgInput;
            msg += input.toString();
            LogUtil.debug(LogUtil.LogTypeUser, LogUtil.TypeIDNone, this.getClass().getName(), msg);

            //调用数据处理类
            UsersOperator uo = new UsersOperator();
            uo.userLogin(input, actionResult);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            actionResult.setResult(ActionResultInfo.Fail);
            actionResult.setFailInfo(ActionResultInfo.ErrorLogin);
        }

        String msg = LogUtil.MsgOutput;
        msg += JsonHttpSession.result2Json(actionResult, "User").toString();
        LogUtil.debug(LogUtil.LogTypeUser, LogUtil.TypeIDNone, this.getClass().getName(), msg);

        // 输出结果
        JsonHttpSession.outResult(actionResult, "User");
        return null;
    }
    public String initinf()
    {
        JSONObject output = new JSONObject();
        try
        {
            JSONObject input = JsonHttpSession.getJsonInput("json");

            String msg = LogUtil.MsgInput;
            msg += input.toString();
            LogUtil.debug(LogUtil.LogTypeUser, LogUtil.TypeIDNone, this.getClass().getName(), msg);

            //调用数据处理类
            UsersOperator uo = new UsersOperator();
            uo.initinf(input, output);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorModifyBatch);
        }
        try
        {
            String msg = LogUtil.MsgOutput;
            msg += output.toString();
            LogUtil.debug(LogUtil.LogTypeUser, LogUtil.TypeIDNone, this.getClass().getName(), msg);

            JsonHttpSession.outResult(output);
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
        return null;
    }

}
