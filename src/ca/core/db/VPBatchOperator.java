package ca.core.db;

import java.sql.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import ca.core.logic.ActionResultInfo;
import ca.core.logic.DBSessionUtil;
import ca.core.logic.DateUtil;
import ca.core.logic.IDUtil;
import ca.core.logic.JsonSQLUtil;
import ca.core.logic.LogUtil;

public class VPBatchOperator
{
	public VPBatchOperator() {}
	
	/*
	 * 暂时没有使用
	 */
	public void VPBatchAdd(JSONObject input, JSONObject output)
	{
        Productbatch vpbatch = new Productbatch();
        vpbatch.setBatchID(input.getInt("BatchID"));
        vpbatch.setTypeID(input.getLong("TypeID"));
        vpbatch.setProductName(input.getString("ProductName"));
        vpbatch.setVendorID(input.getLong("VendorID"));
        if(input.has("ProductDate"))
            vpbatch.setProductDate(Date.valueOf(input.getString("ProductDate")));
        if(input.has("ExpireDate"))
            vpbatch.setExpireDate(Date.valueOf(input.getString("ExpireDate")));
        vpbatch.setBatchStatus(input.getInt("Status"));
        vpbatch.setProductCount(input.getInt("ProductCount"));
        vpbatch.setInspectCode(input.getString("InspectCode"));
        vpbatch.setInspectID(input.getInt("InspectID"));
        vpbatch.setUserID(input.getInt("UserID"));
        vpbatch.setAuthID(input.getInt("AuthID"));
        vpbatch.setBatchKey("");
        vpbatch.setVendorBatchID(input.has("VendorBatchID") ? input.getString("VendorBatchID") : "");

        String batchTable = IDUtil.getBatchTable(vpbatch.getTypeID());
        String sql = "insert into " + batchTable;
        if(vpbatch.getProductDate() != null)
            sql += " (BatchID, TypeID, ProductName, VendorID, VendorBatchID, ProductDate, ExpireDate, " +
                "BatchStatus, ProductCount, StartID, EndID, InspectCode, InspectID, UserID, AuthID) values (";
        else
            sql += " (BatchID, TypeID, ProductName, VendorID, VendorBatchID, " +
                    "BatchStatus, ProductCount, StartID, EndID, InspectCode, InspectID, UserID, AuthID) values (";
        sql += vpbatch.getBatchID() + ", ";           // BatchID
        sql += vpbatch.getTypeID() + ", ";           // TypeID
        sql += "'" + vpbatch.getProductName() + "', ";   // 商品名称
        sql += vpbatch.getVendorID() + ",";   // 企业编号
        sql += "'" + vpbatch.getVendorBatchID() + "',";   // 企业关联的生产批次
        if(vpbatch.getProductDate() != null) {
            sql += "'" + DateUtil.convertDate2String(vpbatch.getProductDate(), DateUtil.DATE_FORMAT) + "', ";
            sql += "'" + DateUtil.convertDate2String(vpbatch.getExpireDate(), DateUtil.DATE_FORMAT) + "', ";
        }
        sql += TagAuthorize.TagInvalid + ", ";   // 状态为激活
        sql += vpbatch.getProductCount() + ",";
        sql += "0,";
        sql += "0,";
        sql += "'" + vpbatch.getInspectCode() + "',";
        sql += vpbatch.getInspectID() + ",";
        sql += vpbatch.getUserID() + ", ";
        sql += vpbatch.getAuthID() +  ")";

        //TODO batch key
		String msg1 = LogUtil.MsgDBInsert;
		msg1 += vpbatch.getClass().getName();
		LogUtil.info(LogUtil.LogTypeTag, LogUtil.TypeIDNone, this.getClass().getName(), msg1);

        String msg = LogUtil.MsgDBSQL;
        msg += sql;
        LogUtil.info(LogUtil.LogTypeTag, vpbatch.getTypeID(), this.getClass().getName(), msg);
        if (!DBSessionUtil.update(sql))
        {
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorDBNewBatch);
            return;
        }
		output.put("Result", ActionResultInfo.Success);
	}
	
	public void VPBatchQuery(JSONObject input, JSONObject output)
	{
		String sql;
		String batchTable = IDUtil.getBatchTable(input.getLong("TypeID"));
		input.remove("TypeID");
		
		//根据参数在数据库中查找用户
		if (input.isEmpty())
		{
			sql = "select * from " + batchTable;
		}
		else
		{
			if (input.has("Status"))
			{
				int status = input.getInt("Status");
				input.put("BatchStatus", status);
				input.remove("Status");
			}
			
			//1. 准备查找SQL语句。准备查询
			sql = "select * from " + batchTable + " where ";
			boolean bFirst = true;
			
			if (input.has("DateStart"))
			{
				String strDate = input.getString("DateStart");
				sql += "ProductDate >= '" + strDate + "'";
				bFirst = false;
				input.remove("DateStart");
			}
			if (input.has("DateEnd"))
			{
				if (!bFirst)
				{
					sql += " and ";
				}
				String strDate = input.getString("DateEnd");
				sql += "ProductDate <= '" + strDate + "'";
				bFirst = false;
				input.remove("DateEnd");
			}
			
			sql += JsonSQLUtil.JSON2SQLWhere(bFirst, input);
		}
		
		String msg0 = LogUtil.MsgDBSQL;
		msg0 += sql;
		LogUtil.info(LogUtil.LogTypeTag, LogUtil.TypeIDNone, this.getClass().getName(), msg0);
		
		@SuppressWarnings("unchecked")
		List<Productbatch> mylist = DBSessionUtil.query(sql, Productbatch.class);
		if (mylist == null)
		{
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorDBQueryVPBatch);
			return;
		}

		output.put("Result", ActionResultInfo.Success);
		JSONArray ja = new JSONArray();
		for(Productbatch vpbtch : mylist)
		{
			JSONObject jo = new JSONObject();
			jo.put("BatchID", vpbtch.getBatchID());
			jo.put("TypeID", vpbtch.getTypeID());
			jo.put("ProductName", vpbtch.getProductName());
			jo.put("VendorID", vpbtch.getVendorID());
			jo.put("VendorBatchID", vpbtch.getVendorBatchID() == null ? "" : vpbtch.getVendorBatchID());
			jo.put("ProductDate", DateUtil.convertDate2String(vpbtch.getProductDate(), DateUtil.DATE_FORMAT));
			jo.put("ExpireDate", DateUtil.convertDate2String(vpbtch.getExpireDate(), DateUtil.DATE_FORMAT));
			jo.put("BatchStatus", vpbtch.getBatchStatus());
			jo.put("ProductCount", vpbtch.getProductCount());
			jo.put("StartID", vpbtch.getStartID());
			jo.put("EndID", vpbtch.getEndID());
			jo.put("InspectCode", vpbtch.getInspectCode());
			jo.put("InspectID", vpbtch.getInspectID());
			jo.put("UserID", vpbtch.getUserID());
			jo.put("ProductSourceReport", vpbtch.getProductSourceReport());
			jo.put("ThirdReport", vpbtch.getThirdReport());
			jo.put("VendorReport", vpbtch.getVendorReport());
			jo.put("BatchInfo", vpbtch.getBatchInfo() == null ? "" : vpbtch.getBatchInfo());
			jo.put("AuthID", vpbtch.getAuthID());
			ja.add(jo);
		}
		output.put("Batchs", ja);
	}	
	
	public void VPBatchInspectQuery(JSONObject input, JSONObject output){
		String batchTable = IDUtil.getBatchTable(input.getLong("TypeID"));
		input.remove("TypeID");
		
		//当报检批号为-1时，表示出口生产批次还没有报检
		String sql = "select * from " + batchTable + " where " + JsonSQLUtil.JSON2SQLWhere(true, input);
		
		String msg0 = LogUtil.MsgDBSQL;
		msg0 += sql;
		LogUtil.info(LogUtil.LogTypeTag, LogUtil.TypeIDNone, this.getClass().getName(), msg0);
		
		@SuppressWarnings("unchecked")
		List<Productbatch> mylist = DBSessionUtil.query(sql, Productbatch.class);
		if (mylist == null)
		{
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorDBQueryVPBatch);
			return;
		}

		output.put("Result", ActionResultInfo.Success);
		JSONArray ja = new JSONArray();
		for(Productbatch vpbtch : mylist)
		{
			JSONObject jo = new JSONObject();
			jo.put("BatchID", vpbtch.getBatchID());
			jo.put("TypeID", vpbtch.getTypeID());
			jo.put("ProductName", vpbtch.getProductName());
			jo.put("VendorID", vpbtch.getVendorID());
			jo.put("VendorBatchID", vpbtch.getVendorBatchID());
			jo.put("ProductDate", DateUtil.convertDate2String(vpbtch.getProductDate(), DateUtil.DATE_FORMAT));
			jo.put("ExpireDate", DateUtil.convertDate2String(vpbtch.getExpireDate(), DateUtil.DATE_FORMAT));
			jo.put("BatchStatus", vpbtch.getBatchStatus());
			jo.put("ProductCount", vpbtch.getProductCount());
			jo.put("StartID", vpbtch.getStartID());
			jo.put("EndID", vpbtch.getEndID());
			jo.put("InspectCode", vpbtch.getInspectCode());
			jo.put("InspectID", vpbtch.getInspectID());
			jo.put("UserID", vpbtch.getUserID());
			jo.put("ProductSourceReport", vpbtch.getProductSourceReport());
			jo.put("ThirdReport", vpbtch.getThirdReport());
			jo.put("VendorReport", vpbtch.getVendorReport());
			jo.put("BatchInfo", vpbtch.getBatchInfo() == null ? "" : vpbtch.getBatchInfo());
			jo.put("AuthID", vpbtch.getAuthID());
			ja.add(jo);
		}
		output.put("Batchs", ja);
	}
	
	public void VPBatchQueryById(JSONObject input, JSONObject output) {
		String batchTable = IDUtil.getBatchTable(input.getLong("TypeID"));
		input.remove("TypeID");
		
		//当报检批号为-1时，表示出口生产批次还没有报检
		String sql = "select * from " + batchTable + " where " + JsonSQLUtil.JSON2SQLWhere(true, input);
		
		String msg0 = LogUtil.MsgDBSQL;
		msg0 += sql;
		LogUtil.info(LogUtil.LogTypeTag, LogUtil.TypeIDNone, this.getClass().getName(), msg0);
		
		@SuppressWarnings("unchecked")
		List<Productbatch> mylist = DBSessionUtil.query(sql, Productbatch.class);
		if (mylist == null)
		{
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorDBQueryVPBatch);
			return;
		}
		
		Productbatch batchinfo = mylist.get(0);
		output.put("StartID", batchinfo.getStartID());
		output.put("EndID", batchinfo.getEndID());
	}
	
	/*
	 * 暂时没有使用
	 */
	public void VPBatchModify(JSONObject input, JSONObject output){
		String batchTable = IDUtil.getBatchTable(input.getLong("TypeID"));
		input.remove("TypeID");
		int batchID = input.getInt("BatchID");
		input.remove("BatchID");
		String sql = "update " + batchTable + " set " + JsonSQLUtil.JSON2SQLSet(input);
		sql += " where BatchID=" + batchID;
		String msg = LogUtil.MsgDBSQL;
		msg += sql;
		LogUtil.info(LogUtil.LogTypeTag, LogUtil.TypeIDNone, this.getClass().getName(), msg);
		if (!DBSessionUtil.update(sql)){
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorDBModifyVPBatch);
			return;
		}
		output.put("Result", ActionResultInfo.Success);
	}

    public void modifyBatchByInspectId(JSONObject input, JSONObject output){
        long typeID = input.getLong("TypeID");
        String batchTable = IDUtil.getBatchTable(typeID);
        input.remove("TypeID");
        int inspectID = input.getInt("InspectID");
        input.remove("InspectID");
        String sql = "update " + batchTable + " set " + JsonSQLUtil.JSON2SQLSet(input);
        sql += " where InspectID=" + inspectID + " and TypeID=" + typeID;
        String msg = LogUtil.MsgDBSQL;
        msg += sql;
        LogUtil.info(LogUtil.LogTypeTag, LogUtil.TypeIDNone, this.getClass().getName(), msg);
        if (!DBSessionUtil.update(sql)){
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorDBModifyVPBatch);
            return;
        }
        output.put("Result", ActionResultInfo.Success);
    }

    public void modifyBatchForTemp(JSONObject input,JSONObject output){
        long typeID = input.getLong("TypeID");
        String batchTable = IDUtil.getBatchTable(typeID);
        input.remove("TypeID");
        int inspectID = input.getInt("InspectID");
        input.remove("InspectID");
        String sql = "update " + batchTable + " set inspectid=0,inspectcode='0' ";
        sql += " where InspectID=" + inspectID + " and TypeID=" + typeID;
        String msg = LogUtil.MsgDBSQL;
        msg += sql;
        LogUtil.info(LogUtil.LogTypeTag, LogUtil.TypeIDNone, this.getClass().getName(), msg);
        if (!DBSessionUtil.update(sql)){
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorDBModifyVPBatch);
            return;
        }
        output.put("Result", ActionResultInfo.Success);
    }

    public void deleteBatch(JSONObject input, JSONObject output) {
        String batchTable = IDUtil.getBatchTable(input.getLong("TypeID"));
        input.remove("TypeID");
        int InspectID = input.getInt("InspectID");
        input.remove("InspectID");
        String sql = "delete from " + batchTable + " where InspectID=0";
        String msg = LogUtil.MsgDBSQL;
        msg += sql;
        LogUtil.info(LogUtil.LogTypeTag, LogUtil.TypeIDNone, this.getClass().getName(), msg);
        if (!DBSessionUtil.update(sql)){
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorDBModifyVPBatch);
            return;
        }
        output.put("Result", ActionResultInfo.Success);
    }
}
