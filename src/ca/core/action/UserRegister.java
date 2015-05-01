package ca.core.action;

import ca.core.db.Users;
import ca.core.db.VendorOperator;
import ca.core.db.Vendors;
import net.sf.json.JSONObject;

import ca.core.db.UsersOperator;
import ca.core.logic.ActionResult;
import ca.core.logic.ActionResultInfo;
import ca.core.logic.JsonHttpSession;
import ca.core.logic.LogUtil;
import ca.core.logic.UserSessionUtil;

import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class UserRegister extends ActionSupport
{
	
	public String execute()
	{
		ActionResult actionResult = new ActionResult();
		try
		{
			JSONObject input = JsonHttpSession.getJsonInput("json");
			JSONObject output = new JSONObject();
			
			// Check UserSession
			if (UserSessionUtil.checkSession(output) < 0)
			{
				JsonHttpSession.outResult(output);
				return null;
			}
			
			String msg = LogUtil.MsgInput;
			msg += input.toString();
			LogUtil.debug(LogUtil.LogTypeUser, LogUtil.TypeIDNone, this.getClass().getName(), msg);
			
			UsersOperator uo = new UsersOperator();
			uo.UserAdd(input, actionResult);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			actionResult.setResult(ActionResultInfo.Fail);
			actionResult.setFailInfo(ActionResultInfo.ErrorRegister);
		}
		
		String msg = LogUtil.MsgOutput;
		msg += JsonHttpSession.result2Json(actionResult, "UserID").toString();
		LogUtil.debug(LogUtil.LogTypeUser, LogUtil.TypeIDNone, this.getClass().getName(), msg);
		
		// 
		JsonHttpSession.outResult(actionResult, "UserID");
		return null;
	}

    public String checkUser(){
        ActionResult actionResult = new ActionResult();
        try
        {
            JSONObject input = JsonHttpSession.getJsonInput("json");
            JSONObject output = new JSONObject();

            // Check UserSession
            if (UserSessionUtil.checkSession(output) < 0)
            {
                JsonHttpSession.outResult(output);
                return null;
            }

            String msg = LogUtil.MsgInput;
            msg += input.toString();
            LogUtil.debug(LogUtil.LogTypeUser, LogUtil.TypeIDNone, this.getClass().getName(), msg);

            UsersOperator uo = new UsersOperator();
            VendorOperator vendorOperator=new VendorOperator();
            Users user=new Users();
            Vendors vendors=new  Vendors();
            if(input.has("UserName"))  {
                user.setUserName(input.getString("UserName"));
            }
            if(input.has("VendorID")){
                vendors.setVendorId(input.getLong("VendorID"));
            }
            if(input.has("VendorName")){
                vendors.setVendorName(input.getString("VendorName"));
            }
            if(input.has("VendorRecord")){
                vendors.setVendorRecord(input.getString("VendorRecord"));
            }

            boolean r = uo.isUserNameExist(user);
            if(r)    {
                actionResult.setResult(0);
                actionResult.setFailInfo("User exist!");
                return null;
            }
            else {
                r=vendorOperator.isVendorNameExists(vendors);
                if(r)    {
                    actionResult.setResult(1);
                    actionResult.setFailInfo("VendorName exist!");
                    return null;
                }
                else {
                    r=vendorOperator.isVendorRecordExists(vendors);
                    if(r)    {
                        actionResult.setResult(2);
                        actionResult.setFailInfo("VendorRecord exist!");
                        return null;
                    }
                    else {

                        actionResult.setResult(3);
                        actionResult.setFailInfo("VendorName not exist! VendorRecord not exist! User not exist!");
                        return null;
                    }
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            actionResult.setResult(4);
            actionResult.setFailInfo(ActionResultInfo.ErrorRegister);
        }finally {
            String msg = LogUtil.MsgOutput;
            msg += JsonHttpSession.result2Json(actionResult, "Users").toString();
            LogUtil.debug(LogUtil.LogTypeUser, LogUtil.TypeIDNone, this.getClass().getName(), msg);

            JsonHttpSession.outResult(actionResult, "Users");
            return null;
        }
    }
}
