package ca.core.db;

import java.util.List;

import ca.core.logic.ActionResultInfo;
import ca.core.logic.DBSessionUtil;
import ca.core.logic.DateUtil;
import ca.core.logic.JsonSQLUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class LogOperator {

	public void logQuery(JSONObject input, JSONObject output) 
	{
		String sql;
		
		//根据参数在数据库中查找用户
		if (input.isEmpty())
		{
			sql = "select * from loginfo";
		}
		else
		{
			boolean bFirst = true;
			//1. 准备查找SQL语句。准备查询
			sql = "select * from loginfo where ";
			
			if (input.has("TimeStart"))
			{
				String strDate = input.getString("TimeStart");
				sql += "LogTime > '" + strDate + "'";
				bFirst = false;
				input.remove("TimeStart");
			}
			if (input.has("TimeEnd"))
			{
				if (!bFirst)
				{
					sql += " and ";
				}
				String strDate = input.getString("TimeEnd");
				sql += "LogTime < '" + strDate + "'";
				bFirst = false;
				input.remove("TimeEnd");
			}
			
			sql += JsonSQLUtil.JSON2SQLWhere(bFirst, input);
		}
		@SuppressWarnings("unchecked")
		List<Loginfo> mylist = DBSessionUtil.query(sql, Loginfo.class);
		if (mylist == null)
		{
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorLogQuery);
			return;
		}

		output.put("Result", ActionResultInfo.Success);
		JSONArray ja = new JSONArray();
		for(Loginfo logInfo : mylist)
		{
			JSONObject jo = new JSONObject();
			jo.put("LogID", logInfo.getLogId());
			jo.put("LogType", logInfo.getLogType());
			jo.put("UserID", logInfo.getUserId());
			jo.put("ClassName", logInfo.getClassName());
			jo.put("Level", logInfo.getLevel());
			jo.put("TypeID", logInfo.getTypeId());
			jo.put("LogTime", DateUtil.convertDate2String(logInfo.getLogTime(), DateUtil.DATE_FORMAT));
			jo.put("LogInfo", logInfo.getLogInfo());
			ja.add(jo);
		}
		output.put("Log", ja);
	}

}