package ca.core.db;

import java.util.List;
import java.util.Random;

import ca.core.logic.ActionResult;
import ca.core.logic.ActionResultInfo;
import ca.core.logic.DBSessionUtil;
import ca.core.logic.JsonSQLUtil;
import ca.core.logic.LogUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class AuthOperator
{
	public AuthOperator() {}
	
	/*
	 * 1. 根据input创建Authorize
	 * 2. 随机生成母密钥
	 * 3. 在授权方数据表中插入一个新条目
	 */
	public void AuthAdd(JSONObject input, ActionResult actionResult)
	{
		// 1. 根据input创建Authorize
		Authorize myauth = new Authorize();
		myauth.setAuthName(input.getString("AuthName"));
		myauth.setAuthAddress(input.getString("AuthAddress"));
		myauth.setAuthUrl(input.getString("AuthURL"));
		myauth.setAuthPhone(input.getString("AuthPhone"));
		myauth.setAuthEmail(input.getString("AuthEmail"));
		myauth.setAuthStatus(1);
		myauth.setFatherId(input.getInt("FatherID"));
		myauth.setVendorCount(0);
		//2. 随机生成母密钥AutKey
		Random random = new Random();
		myauth.setAuthKey(Long.toHexString(random.nextLong()));
		
		String msg = LogUtil.MsgDBInsert;
		msg += myauth.getClass().getName();
		LogUtil.info(LogUtil.LogTypeAuth, LogUtil.TypeIDNone, this.getClass().getName(), msg);
		
		// 3. 在授权方数据表中插入一个新条目
		if (!DBSessionUtil.save(myauth))
		{
			actionResult.setResult(ActionResultInfo.Fail);
			actionResult.setFailInfo(ActionResultInfo.ErrorDBNewAuth);
			return;
		}

		//返回成功和AuthID信息	
		actionResult.setResult(ActionResultInfo.Success);
		actionResult.setData(myauth.getAuthId());
	}
	
	@SuppressWarnings("unchecked")
	public static Authorize AuthQuery(int auth)
	{
		String sql = "select * from authorize where AuthID=" + auth;
		
		String msg = LogUtil.MsgDBSQL;
		msg += sql;
		LogUtil.info(LogUtil.LogTypeAuth, LogUtil.TypeIDNone, "AuthOperator Class: AuthQuery Method", msg);

		List<Authorize> mylist = DBSessionUtil.query(sql, Authorize.class);		
		if (null == mylist || mylist.size() < 1)
		{
			return null;
		}
		return mylist.get(0);
	}

	public void AuthQuery(JSONObject input, JSONObject output)
	{
		String sql;
		
		//根据参数在数据库中查找用户
		if (input.isEmpty())
		{
			sql = "select * from authorize";
		}
		else
		{
			sql = "select * from authorize where " + JsonSQLUtil.JSON2SQLWhere(true, input);
		}
		
		String msg = LogUtil.MsgDBSQL;
		msg += sql;
		LogUtil.info(LogUtil.LogTypeAuth, LogUtil.TypeIDNone, "AuthOperator Class: AuthQuery Method", msg);

		@SuppressWarnings("unchecked")
		List<Authorize> mylist = DBSessionUtil.query(sql, Authorize.class);		
		if (null == mylist)
		{
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorDBQueryAuth);
			return;
		}
		
		//生成输出JSON
		output.put("Result", ActionResultInfo.Success);
		JSONArray ja = new JSONArray();
		for(Authorize auth : mylist)
		{
			JSONObject jo = new JSONObject();
			jo.put("AuthID", auth.getAuthId());
			jo.put("AuthName", auth.getAuthName());
			jo.put("AuthAddress", auth.getAuthAddress());
			jo.put("AuthURL", auth.getAuthUrl());
			jo.put("AuthPhone", auth.getAuthPhone());
			jo.put("AuthEmail", auth.getAuthEmail());
			jo.put("AuthStatus", auth.getAuthStatus());
			jo.put("AuthID", auth.getAuthId());
			jo.put("FatherID", auth.getFatherId());
			jo.put("VendorCount", auth.getVendorCount());
			ja.add(jo);
		}
		output.put("Auths", ja);
	}
	
	public void AuthModify(int authid, int vendorcount)
	{
		//准备查找SQL语句。
		String sql = "update authorize set VendorCount" + vendorcount + " where AuthID=" + authid;
		String msg = LogUtil.MsgDBSQL;
		msg += sql;
		LogUtil.info(LogUtil.LogTypeAuth, LogUtil.TypeIDNone, this.getClass().getName(), msg);
		DBSessionUtil.update(sql);
	}

	public void AuthModify(JSONObject input, ActionResult actionResult)
	{
		//准备查找SQL语句。
		int authID = input.getInt("AuthID");
		input.remove("AuthID");
		String sql = "update authorize set " + JsonSQLUtil.JSON2SQLSet(input);
		sql += " where AuthID=" + authID;
		String msg = LogUtil.MsgDBSQL;
		msg += sql;
		LogUtil.info(LogUtil.LogTypeAuth, LogUtil.TypeIDNone, this.getClass().getName(), msg);
		if (!DBSessionUtil.update(sql))
		{
			actionResult.setResult(ActionResultInfo.Fail);
			actionResult.setFailInfo(ActionResultInfo.ErrorDBModifyAuth);
			return;
		}

		//生成输出成功信息
		actionResult.setResult(ActionResultInfo.Success);
		actionResult.setData(null);
	}
}
