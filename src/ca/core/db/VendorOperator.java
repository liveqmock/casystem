package ca.core.db;

import java.util.List;

import ca.core.logic.ActionResult;
import ca.core.logic.ActionResultInfo;
import ca.core.logic.DBSessionUtil;
import ca.core.logic.DESUtil;
import ca.core.logic.IDUtil;
import ca.core.logic.JsonSQLUtil;
import ca.core.logic.LogUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class VendorOperator
{
	private static final int VendorStatusOk    = 1;
	@SuppressWarnings("unused")
	private static final int VendorStatusForbidden = 0;
	
	public VendorOperator() {}
	
	/*
	 * 1. 根据input创建Vendors
	 * 2. 根据AuthID在授权方表中找到对应的条目
	 * 3. 根据AuthID、授权方的厂家数量，生成VendorID
	 * 4. 根据授权方的母密钥，VendorID，计算Vnedors的母密钥
	 * 5. 将Vendors插入厂家信息表中
	 * 6. 将授权方中的VendorCount加1
	 */
	public void addVendor(Vendors dbVendor, ActionResult actionResult)
	{
		// 1. 根据input创建Vendors
		dbVendor.setTypeCount(0);
		dbVendor.setVendorStatus(VendorOperator.VendorStatusOk);
		// 2. 根据AuthID在授权方表中找到对应的条目
		// 从AuthId = 0 的条目中获取当前的厂家数目，0 为虚拟的授权方
		//Authorize authVirtual = AuthOperator.AuthQuery(0);
		Authorize auth = AuthOperator.AuthQuery(dbVendor.getAuthId());
		// old：3. 根据AuthID、授权方的厂家数量，生成VendorID
		// delete new：3： AuthId 不再在Id中体现，只要根据VendorCount来生成厂家ID
		long vendorId = IDUtil.getVendorId(auth.getAuthId(), auth.getVendorCount());
		dbVendor.setVendorId(vendorId);
		// 4. 根据授权方的母密钥，VendorID，计算Vnedors的母密钥 VendorKey
		String key = auth.getAuthKey();
		String strVendorID = Long.toHexString(dbVendor.getVendorId());
		dbVendor.setVendorKey(DESUtil.fadeInDES(key, strVendorID));

		String msg1 = LogUtil.MsgDBInsert;
		msg1 += dbVendor.getVendorName();
		LogUtil.info(LogUtil.LogTypeVendor, LogUtil.TypeIDNone, this.getClass().getName(), msg1);
		
		// 5. 将Vendors插入厂家信息表中
		if (!DBSessionUtil.save(dbVendor))
		{
			actionResult.setResult(ActionResultInfo.Fail);
			actionResult.setFailInfo(ActionResultInfo.ErrorDBNewVendor);
			return;
		}
		
		// FIXME    getVendorCount  可能会累加出错，用同一个授权账户操作时     shangyh
		// 6. 将授权VendorCount加1
		auth.setVendorCount(auth.getVendorCount() + 1);
		
		String msg = LogUtil.MsgDBUpdate;
		msg += auth.getClass().getName();
		LogUtil.info(LogUtil.LogTypeVendor, LogUtil.TypeIDNone, this.getClass().getName(), msg);
		
		if (!DBSessionUtil.update(auth))
		{
			actionResult.setResult(ActionResultInfo.Fail);
			actionResult.setFailInfo(ActionResultInfo.ErrorDBModifyAuth);
			return;
		}
		
		//返回成功和VendorID信息
		actionResult.setResult(ActionResultInfo.Success);
		actionResult.setData(dbVendor.getVendorId());
	}
	
	public static Vendors VendorQuery(long vendorId)
	{
		String sql = "select * from vendors where VendorID=" + vendorId;
		
		String msg0 = LogUtil.MsgDBSQL;
		msg0 += sql;
		LogUtil.info(LogUtil.LogTypeVendor, LogUtil.TypeIDNone, "VendorOperator Class: VendorQuery Method", msg0);
		
		@SuppressWarnings("unchecked")
		List<Vendors> mylist = DBSessionUtil.query(sql, Vendors.class);		
		
		if (mylist == null || mylist.size() < 1)
		{
			return null;
		}
		Vendors vendor = mylist.get(0);
		return vendor;
	}

	public void VendorQuery(JSONObject input, JSONObject output)
	{
		String sql;
		
		//根据参数在数据库中查找用户
		if (input.isEmpty())
		{
			sql = "select * from vendors";
		}
		else
		{
			sql = "select * from vendors where " + JsonSQLUtil.JSON2SQLWhere(true, input);
		}
		try
		{			
			
			String msg0 = LogUtil.MsgDBSQL;
			msg0 += sql;
			LogUtil.info(LogUtil.LogTypeVendor, LogUtil.TypeIDNone, this.getClass().getName(), msg0);
			
			@SuppressWarnings("unchecked")
			List<Vendors> mylist = DBSessionUtil.query(sql, Vendors.class);
			if (mylist == null)
			{
				output.put("Result", ActionResultInfo.Fail);
				output.put("FailInfo", ActionResultInfo.ErrorDBQueryVendor);
				return;
			}

			//生成输出JSON
			output.put("Result", ActionResultInfo.Success);
			JSONArray ja = new JSONArray();
			for(Vendors vendor : mylist)
			{
				JSONObject jo = new JSONObject();
				jo.put("VendorID", vendor.getVendorId());
				jo.put("VendorName", vendor.getVendorName());
				jo.put("VendorAddress", vendor.getVendorAddress());
				jo.put("VendorPerson", vendor.getVendorPerson());
				jo.put("VendorURL", vendor.getVendorURL());
				jo.put("VendorPhone", vendor.getVendorPhone());
				jo.put("VendorFox", vendor.getVendorFox());
				jo.put("VendorEmail", vendor.getVendorEmail());
				jo.put("VendorStatus", vendor.getVendorStatus());
				jo.put("AuthID", vendor.getAuthId());
				jo.put("FatherID", vendor.getFatherId());
				jo.put("VendorRecord", vendor.getVendorRecord());
				jo.put("TypeCount", vendor.getTypeCount());
				jo.put("Certification", vendor.getCertification() == null ? "" : vendor.getCertification());
				jo.put("Comment", vendor.getComment() == null ? "" : vendor.getComment());
				ja.add(jo);
			}
			output.put("Vendors", ja);
		}
		catch (Exception e)
		{
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorQueryVendor);
		}
	}	

	public void VendorModify(JSONObject input, ActionResult actionResult)
	{
		long vendorID = input.getLong("VendorID");
		input.remove("VendorID");
		String sql = "update vendors set " + JsonSQLUtil.JSON2SQLSet(input);
		sql += " where VendorID=" + vendorID;
		
		String msg = LogUtil.MsgDBSQL;
		msg += sql;
		LogUtil.info(LogUtil.LogTypeVendor, LogUtil.TypeIDNone, this.getClass().getName(), msg);
		if (!DBSessionUtil.update(sql))
		{
			actionResult.setResult(ActionResultInfo.Fail);
			actionResult.setFailInfo(ActionResultInfo.ErrorDBModifyVendor);
			return;
		}

		actionResult.setResult(ActionResultInfo.Success);
		actionResult.setData(null);
	}

   public boolean isVendorNameExists(Vendors vendors){
       String  sql = "select * from vendors where VendorName='" + vendors.getVendorName() + "' AND VendorId!="+vendors.getVendorId();

       String msg0 = LogUtil.MsgDBSQL;
       msg0 += sql;
       LogUtil.info(LogUtil.LogTypeUser, LogUtil.TypeIDNone, this.getClass().getName(), msg0);
       List<Vendors> mylist = DBSessionUtil.query(sql, Vendors.class);
       if(mylist!=null && mylist.size()!=0){
          return true;
       }
       return false;
   }
    public boolean isVendorRecordExists(Vendors vendors){
        String sql = "select * from vendors where VendorRecord='" + vendors.getVendorRecord() + "'  AND VendorId!="+vendors.getVendorId();
        String msg0 = LogUtil.MsgDBSQL;
        msg0 += sql;
        LogUtil.info(LogUtil.LogTypeUser, LogUtil.TypeIDNone, this.getClass().getName(), msg0);
        List<Vendors> mylist = DBSessionUtil.query(sql, Vendors.class);
        if(mylist!=null && mylist.size()!=0){
            return true;
        }
        return false;
    }
}
