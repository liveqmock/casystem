package ca.core.action;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.Action;

import net.sf.json.JSONObject;

import ca.core.db.TagOperator;
import ca.core.logic.ActionResultInfo;
import ca.core.logic.JsonHttpSession;
import ca.core.logic.LogUtil;
import ca.core.logic.UserSessionUtil;

public class TagDownload
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
			
			// Check UserSession
			if (UserSessionUtil.checkSession(output) < 0)
			{
				JsonHttpSession.outResult(output);
				return null;
			}
			
			String msg = LogUtil.MsgInput;
			msg += input.toString();
			LogUtil.debug(LogUtil.LogTypeTag, LogUtil.TypeIDNone, this.getClass().getName(), msg);
			
			TagOperator to = new TagOperator();
			to.TagDownload(input, output);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorConfirmTag);
		}
		// 
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
    public String tagDownload(){
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

            TagOperator to = new TagOperator();
            to.TagDownload2(input, output);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorConfirmTag);
        }
        //
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
