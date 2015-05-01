package ca.core.db;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import ca.core.logic.ActionResult;
import ca.core.logic.ActionResultInfo;
import ca.core.logic.DBSessionUtil;
import ca.core.logic.JsonSQLUtil;
import ca.core.logic.LogUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@SuppressWarnings("unused")
public class UsersOperator
{
	private static final int UserStatusOnline    = 1;
	private static final int UserStatusOffline   = 2;
	private static final int UserStatusForbidden = 0;
	private static final int UserModifyActionPassword = 0;
	private static final int SessionTimeout = 2400;     // Seconds
	
	// Constructors
	/** default constructor */
	public UsersOperator() {}
	
	public static boolean isUserOnline()
	{
		try
		{
//			HttpSession httpsession = ServletActionContext.getRequest().getSession();
//			httpsession.getAttribute("UsernName");
//			httpsession.getAttribute("UserID");
			String token = ServletActionContext.getRequest().getHeader(SessionOperator.HeaderTokenName);
			if (token == null)
			{
				return false;
			}

			SessionOperator sOperator = new SessionOperator();
			if (!sOperator.sessionUpdate(token))
			{
				return false;
			}
		}
		catch (Exception e)
		{
			return false;
		}
		return true;
	}
    @SuppressWarnings("unchecked")
    public boolean isUserNameExist(String  UserName)
    {
        String sql = "select * from users where UserName='" + UserName+ "'";

        String msg0 = LogUtil.MsgDBSQL;
        msg0 += sql;
        LogUtil.info(LogUtil.LogTypeUser, LogUtil.TypeIDNone, this.getClass().getName(), msg0);

        List<Users> listUser = DBSessionUtil.query(sql, Users.class);
        if (listUser != null && listUser.size() > 0)
        {
            return true;
        }
        return false;
    }
	@SuppressWarnings("unchecked")
	public boolean isUserNameExist(Users user)
	{
		String sql = "select * from users where UserName='" + user.getUserName() + "'";
		
		String msg0 = LogUtil.MsgDBSQL;
		msg0 += sql;
		LogUtil.info(LogUtil.LogTypeUser, LogUtil.TypeIDNone, this.getClass().getName(), msg0);
		
		List<Users> listUser = DBSessionUtil.query(sql, Users.class);
		if (listUser != null && listUser.size() > 0)
		{
			return true;
		}
		return false;
	}
	
	public void UserAdd(JSONObject input, ActionResult actionResult)
	{
		Users dbUser = new Users();
		dbUser.setUserName(input.getString("UserName"));
		dbUser.setUserPassword(input.getString("UserPassword"));
		dbUser.setUserType(input.getInt("UserType"));
		if (input.has("UserTelephone"))
		{
			dbUser.setUserTelephone(input.getString("UserTelephone"));
		}
		if (input.has("UserMobilephone"))
		{
			dbUser.setUserMobilephone(input.getString("UserMobilephone"));
		}
		dbUser.setUserPermission(input.getString("UserPermission"));
		if (input.has("UserEmail"))
		{
			dbUser.setUserEmail(input.getString("UserEmail"));
		}
		dbUser.setCompanyId(input.getInt("CompanyID"));
		if (input.has("FatherID"))
		{
			dbUser.setFatherId(input.getInt("FatherID"));
		}
		// 用户名已经存在
		if (this.isUserNameExist(dbUser.getUserName()))
		{
			//返回 用户名已存在的错误信息
			actionResult.setResult(ActionResultInfo.Fail);
			actionResult.setFailInfo(ActionResultInfo.ErrorUserNameExist);
			return;
		}
		else
		{
			dbUser.setUserStatus(UsersOperator.UserStatusOffline);

			String msg1 = LogUtil.MsgDBInsert;
			msg1 += dbUser.getClass().getName();
			LogUtil.info(LogUtil.LogTypeUser, LogUtil.TypeIDNone, this.getClass().getName(), msg1);
			
			if (!DBSessionUtil.save(dbUser))
			{
				actionResult.setResult(ActionResultInfo.Fail);
				actionResult.setFailInfo(ActionResultInfo.ErrorDBNewUser);
				return;
			}
			
			//返回成功和用户ID信息	
			actionResult.setResult(ActionResultInfo.Success);
			actionResult.setData(dbUser.getUserId());
		}
	}
	
	@SuppressWarnings("unchecked")
	public void queryUser(String strJson, JSONObject output)
	{
		String sql;
		
		//根据参数在数据库中查找用户
		JSONObject input = JSONObject.fromObject(strJson);
		if (input.isEmpty())
		{
			sql = "select * from users";
		}
		else
		{
			sql = "select * from users where " + JsonSQLUtil.JSON2SQLWhere(true, input);
		}
		
		String msg0 = LogUtil.MsgDBSQL;
		msg0 += sql;
		LogUtil.info(LogUtil.LogTypeUser, LogUtil.TypeIDNone, this.getClass().getName(), msg0);
		
		List<Users> mylist = DBSessionUtil.query(sql, Users.class);
		if (mylist == null)
		{
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorDBQueryUser);
			return;
		}
		

		output.put("Result", ActionResultInfo.Success);
		JSONArray ja = new JSONArray();
		for(Users myuser : mylist)
		{
			JSONObject jo = new JSONObject();
			jo.put("UserID", myuser.getUserId());
			jo.put("Username", myuser.getUserName());
			jo.put("CompanyID", myuser.getCompanyId());
			jo.put("Usertype", myuser.getUserType());
			jo.put("UserTelephone", myuser.getUserTelephone());
			jo.put("UserMobilephone", myuser.getUserMobilephone());
			jo.put("UserPermission", myuser.getUserPermission());
			jo.put("UserEmail", myuser.getUserEmail());
			ja.add(jo);
		}
		output.put("Users", ja);
	}
	
	public void userLogin(JSONObject input, ActionResult actionResult)
	{
		String sql = "select * from users where ";
		
		sql += "UserName='" + input.getString("Username") + "'";
		
		String msg0 = LogUtil.MsgDBSQL;
		msg0 += sql;
		LogUtil.info(LogUtil.LogTypeUser, LogUtil.TypeIDNone, this.getClass().getName(), msg0);
		
		@SuppressWarnings("unchecked")
		List<Users> mylist = DBSessionUtil.query(sql, Users.class);
		if (mylist == null)
		{
			actionResult.setResult(ActionResultInfo.Fail);
			actionResult.setFailInfo(ActionResultInfo.ErrorDBQueryUser);
			return;
		}
		
		if (mylist.size() < 1)
		{
			actionResult.setResult(ActionResultInfo.Fail);
			actionResult.setFailInfo(ActionResultInfo.ErrorLogin);
			return;
		}

		Users myuser = (Users)mylist.get(0);
		if (!myuser.getUserPassword().equals(input.getString("Password")))
		{
			actionResult.setResult(ActionResultInfo.Fail);
			actionResult.setFailInfo(ActionResultInfo.ErrorLogin);
			return;
		}
		else
		{
			// 修改数据库中用户状态
			myuser.setUserStatus(UsersOperator.UserStatusOnline);
			
			String msg = LogUtil.MsgDBUpdate;
			msg += myuser.getClass().getName();
			LogUtil.info(LogUtil.LogTypeUser, LogUtil.TypeIDNone, this.getClass().getName(), msg);
			
			if (!DBSessionUtil.update(myuser))
			{
				actionResult.setResult(ActionResultInfo.Fail);
				actionResult.setFailInfo(ActionResultInfo.ErrorDBModifyUser);	
				return;
			}
			
			// 修改Http会话属性
//			HttpSession httpsession = ServletActionContext.getRequest().getSession();
//			httpsession.setAttribute("UserName", myuser.getUserName());
//			httpsession.setAttribute("UserID", myuser.getUserId());
//			httpsession.setMaxInactiveInterval(UsersOperator.SessionTimeout);
			SessionOperator sOperator = new SessionOperator();
			String token = sOperator.sessionAdd(myuser.getUserId());
			if(token == null)
			{
				actionResult.setResult(ActionResultInfo.Fail);
				actionResult.setFailInfo(sOperator.getFailInfo());
				return;
			}

			ServletActionContext.getResponse().addHeader(SessionOperator.HeaderTokenName, token);


			// 返沪登录成功信息
			actionResult.setResult(ActionResultInfo.Success);
			actionResult.setData(myuser);
		}
	}
	
	public void userLogout(Users user, ActionResult actionResult)
	{
		try
		{
//			HttpSession httpsession = ServletActionContext.getRequest().getSession();
//			httpsession.removeAttribute("UserName");
//			httpsession.removeAttribute("UserID");
			String token = ServletActionContext.getRequest().getHeader(SessionOperator.HeaderTokenName);
			if (token == null)
			{
				actionResult.setResult(ActionResultInfo.Fail);
				actionResult.setFailInfo(ActionResultInfo.ErrorUserOffline);
				return;
			}
			SessionOperator sOperator = new SessionOperator();
			sOperator.sessionDelete(token);
		}
		catch (Exception e)
		{
			actionResult.setResult(ActionResultInfo.Fail);
			actionResult.setFailInfo(ActionResultInfo.ErrorUserOffline);
			return;
		}
		// 修改数据库中用户状态
		String sql = "select * from users where " + "UserID=" + user.getUserId();
		
		String msg0 = LogUtil.MsgDBSQL;
		msg0 += sql;
		LogUtil.info(LogUtil.LogTypeUser, LogUtil.TypeIDNone, this.getClass().getName(), msg0);
		
		@SuppressWarnings("unchecked")
		List<Users> mylist = DBSessionUtil.query(sql, Users.class);
		if (mylist == null || mylist.size() < 1)
		{
			actionResult.setResult(ActionResultInfo.Fail);
			actionResult.setFailInfo(ActionResultInfo.ErrorDBQueryUser);
			return;
		}

		Users dbUser = (Users)mylist.get(0);
		dbUser.setUserStatus(UsersOperator.UserStatusOffline);
		
		String msg = LogUtil.MsgDBUpdate;
		msg += dbUser.getClass().getName();
		LogUtil.info(LogUtil.LogTypeUser, LogUtil.TypeIDNone, this.getClass().getName(), msg);

		if (!DBSessionUtil.update(dbUser))
		{
			actionResult.setResult(ActionResultInfo.Fail);
			actionResult.setFailInfo(ActionResultInfo.ErrorDBModifyUser);	
			return;
		}
		
		// 返回成功结果
		actionResult.setResult(ActionResultInfo.Success);
	}
	
	public void modifyUser(String input, ActionResult actionResult)
	{
		//准备搜索数据库的SQL语句
		JSONObject tmpJson = JSONObject.fromObject(input);
		int strUserId = tmpJson.getInt("UserID");
		tmpJson.remove("UserID");
		if (tmpJson.getInt("Action") == UserModifyActionPassword &&
			tmpJson.has("NewPassword"))
		{
			String password = tmpJson.getString("NewPassword");
			tmpJson.remove("NewPassword");
			tmpJson.put("UserPassword", password);
		}
        tmpJson.remove("Action");
		String sql = "update users set " + JsonSQLUtil.JSON2SQLSet(tmpJson);
		sql += " where UserID=" + strUserId;
		
		String msg = LogUtil.MsgDBSQL;
		msg += sql;
		LogUtil.info(LogUtil.LogTypeUser, LogUtil.TypeIDNone, this.getClass().getName(), msg);
		if (!DBSessionUtil.update(sql))
		{
			actionResult.setResult(ActionResultInfo.Fail);
			actionResult.setFailInfo(ActionResultInfo.ErrorDBModifyUser);
			return;
		}
		//返回成功信息
		actionResult.setResult(ActionResultInfo.Success);		
	}
    public void initinf(JSONObject input,JSONObject output){
        String sql="";
        JSONObject jo = new JSONObject();
        long companyID = input.getLong("CompanyID");
        int userType = input.getInt("UserType");
        if(userType == 0)   {
            //局方
            sql = "select * from authorize limit 1";

        } else {
            //企业
            sql = "SELECT a.* FROM authorize a,vendors b WHERE a.AuthID=b.AuthID AND b.VendorID="+companyID+" LIMIT 1" ;
        }

        String msg0 = LogUtil.MsgDBSQL;
        msg0 += sql;
        LogUtil.info(LogUtil.LogTypeUser, LogUtil.TypeIDNone, this.getClass().getName(), msg0);

        List<Authorize> mylist = DBSessionUtil.query(sql, Authorize.class);
        if (mylist == null)
        {
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorDBQueryUser);
            return;
        }
        output.put("Result", ActionResultInfo.Success);

        for(Authorize myuser : mylist)
        {
            jo.put("AuthID", myuser.getAuthId());
            jo.put("AuthName", myuser.getAuthName());
            jo.put("AuthAddress", myuser.getAuthAddress());
            jo.put("AuthURL", myuser.getAuthUrl());
            jo.put("AuthPhone", myuser.getAuthPhone());
            jo.put("AuthEmail", myuser.getAuthEmail());
            jo.put("AuthKey", myuser.getAuthKey());
            jo.put("AuthStatus", myuser.getAuthStatus());
            jo.put("FatherID", myuser.getFatherId());
            jo.put("VendorCount", myuser.getVendorCount());
        }
        String sql1="";
        String sql2="";
        //SELECT COUNT(*) FROM producttype WHERE ApplyStatus=0 AND VendorID=1058553;
        //SELECT COUNT(*) FROM inspect WHERE InspectStatus=0 AND VendorID=1058553;
        if(userType == 0)   {
        //待备案数
            sql1="SELECT COUNT(*) FROM producttype WHERE ApplyStatus=0";
        //待报捡数
            sql2="SELECT COUNT(*) FROM inspect WHERE InspectStatus=0";

        }else {
            sql1="SELECT COUNT(*) FROM producttype WHERE ApplyStatus=0 AND VendorID="+companyID;
            sql2="SELECT COUNT(*) FROM inspect WHERE InspectStatus=0 AND VendorID="+companyID;
        }
        Long ApplyStatuscnt=DBSessionUtil.queryfun(sql1) ;
        Long InspectStatuscnt=DBSessionUtil.queryfun(sql2) ;
        jo.put("ApplyStatuscnt", ApplyStatuscnt);
        jo.put("InspectStatuscnt", InspectStatuscnt);

        output.put("Authorizeinfo", jo);
    }
}
