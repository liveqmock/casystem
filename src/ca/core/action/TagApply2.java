package ca.core.action;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import ca.core.db.TagOperator;
import ca.core.logic.ActionResultInfo;
import ca.core.logic.JsonHttpSession;
import ca.core.logic.LogUtil;

import com.opensymphony.xwork2.Action;

public class TagApply2 
{	
	public InputStream getTargetFile()
	{
		String filePath;
		
		try
		{
			HttpSession httpsession = ServletActionContext.getRequest().getSession();
			filePath = (String) httpsession.getAttribute("tagURL");
		}
		catch (Exception e)
		{
			filePath = "";
			e.printStackTrace();
		}
		return ServletActionContext.getServletContext().getResourceAsStream("/" + filePath);
	}
	
	public String execute()
	{
		JSONObject output = new JSONObject();
		try
		{
			JSONObject input = JsonHttpSession.getJsonInput("json");
			
			String msg = LogUtil.MsgInput;
			msg += input.toString();
			LogUtil.debug(LogUtil.LogTypeTag, LogUtil.TypeIDNone, this.getClass().getName(), msg);
			
			TagOperator to = new TagOperator();
			to.TagApply(input, output);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorApplyTag);
		}

		if (output.getInt("Result") == ActionResultInfo.Fail)
		{
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

		// 修改Http会话属性
		HttpSession httpsession = ServletActionContext.getRequest().getSession();
		httpsession.setAttribute("tagURL", output.getString("tagURL"));
		return Action.SUCCESS;
	}

    public String tagApply3(){
        JSONObject output = new JSONObject();
        try
        {
            JSONObject input = JsonHttpSession.getJsonInput("json");

            String msg = LogUtil.MsgInput;
            msg += input.toString();
            LogUtil.debug(LogUtil.LogTypeTag, LogUtil.TypeIDNone, this.getClass().getName(), msg);

            TagOperator to = new TagOperator();
            to.TagApply(input, output);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorApplyTag);
        }

        if (output.getInt("Result") == ActionResultInfo.Fail)
        {
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

        // 修改Http会话属性
        HttpSession httpsession = ServletActionContext.getRequest().getSession();
        httpsession.setAttribute("tagURL", output.getString("tagURL"));
        return Action.SUCCESS;
    }
}
