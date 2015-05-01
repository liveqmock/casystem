package ca.core.db;

import java.util.List;

import ca.core.logic.*;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class VPTypeOperator
{
	@SuppressWarnings("unused")
	private static final int VPTagTypeCCode = 1;
	@SuppressWarnings("unused")
	private static final int VPTagTypeMF1 = 2;
	@SuppressWarnings("unused")
	private static final int VPTagTypeCPUCard = 3;
	
	public VPTypeOperator() {}

	/*
	 * 1. 根据input创建Producttype
	 * 2. 根据VendorID从厂家信息表中获取Vendors
	 * 3. 根据VendorID, Vendors.typeCount获得TypeID
	 * 4. 根据VendorID, Vendors的母密钥计算Producttype的密钥
	 * 5. 将Producttype插入厂家商品类型表
	 * 6. 更新Vendors中相应条目的TypeCount
	 * 7. 创建"商品认证表"和"商品批次信息表"
	 */
	public void VPTypeAdd(JSONObject input, ActionResult actionResult)
	{
		// 1. 根据input创建Producttype
		Producttype mytype = new Producttype();
		mytype.setProductName(input.getString("ProductName"));
		if (input.has("CategoryID")) {
			mytype.setCategoryId(input.getInt("CategoryID"));
		}
		else
		{
			mytype.setCategoryId(0);
		}
		
		if (input.has("Remain")) {
			mytype.setRemain(input.getInt("Remain"));
		}
		else
		{
			mytype.setRemain(0);
		}
		
		
		mytype.setVendorId(input.getLong("VendorID"));
		mytype.setTagType(input.getInt("TagType"));
		// new 
		mytype.setCategory(input.getInt("Category"));
		mytype.setOriginCountry(input.getString("OriginCountry"));
		mytype.setOriginRegion(input.getString("OriginRegion"));
		//mytype.setInDistributors(input.getString("InDistributors"));
		mytype.setOutDistributors(input.getString("OutDistributors"));
		mytype.setNetContent(input.getString("NetContent"));
		mytype.setNetContentUnit(input.getString("NetContentUnit"));
		mytype.setHsCode(input.getString("HSCode"));
		mytype.setCnProofID(input.getString("CNProofID"));
        if(input.has("ApplyStatus")){
            mytype.setApplyStatus(input.getString("ApplyStatus"));
        }else{
            mytype.setApplyStatus("0");
        }
		mytype.setLabelProof(input.getString("LabelProof"));
		if(input.has("cnProofSample"))
			mytype.setCnProofSample(input.getString("cnProofSample"));
		else
			mytype.setCnProofSample("");
		if(input.has("manageReport"))
			mytype.setManageReport(input.getString("manageReport"));
		else
			mytype.setManageReport("");
		mytype.setBrand(input.getString("Brand"));
		mytype.setTagsBought(4000000000L);
		mytype.setTagsApplied(0);
		mytype.setTagsUsed(0);
		mytype.setProductCount(0);
		mytype.setBatchCount(0);
		mytype.setShelfLife(input.getString("ShelfLife"));
		mytype.setShelfLifeUnit(input.getString("ShelfLifeUnit"));
		if (input.has("TypeInfo"))
		{
			mytype.setTypeInfo(input.getString("TypeInfo"));
		}
		else
		{
			mytype.setTypeInfo("");
		}
        if(input.has("Importer"))
            mytype.setImporter(input.getString("Importer"));
        else
            mytype.setImporter("");
		// 2. 根据VendorID从厂家信息表中获取Vendors
		Vendors vendor = VendorOperator.VendorQuery(mytype.getVendorId());
		// 3. 根据VendorID, Vendors.typeCount获得TypeID
		if (vendor == null || vendor.getTypeCount() == null)
		{
			//返回失败信息
			actionResult.setResult(ActionResultInfo.Success);
			actionResult.setFailInfo(ActionResultInfo.ErrorNoVendor);
			actionResult.setData(null);
			return;
		}
		
		mytype.setAuthId(vendor.getAuthId());
		long typeId = IDUtil.getTypeId(vendor.getVendorId(), vendor.getTypeCount());
		mytype.setTypeId(typeId);
		// 4. 根据VendorID, Vendors的母密钥计算Producttype的密钥
		String key = vendor.getVendorKey();
		String strTypeID = Long.toHexString(mytype.getTypeId());
		mytype.setTypeKey(DESUtil.fadeInDES(key, strTypeID));
		if (mytype.getTypeKey().length() > 16)
		{
			mytype.setTypeKey(String.valueOf(mytype.getTypeKey().toCharArray(), 0, 16));
		}
        mytype.setCreateDate(DateUtil.getCurrentDate());
		String msg1 = LogUtil.MsgDBInsert;
		msg1 += mytype.getClass().getName();
		LogUtil.info(LogUtil.LogTypeVPType, LogUtil.TypeIDNone, this.getClass().getName(), msg1);
		
		// 5. 将Producttype插入厂家商品类型表
		if (!DBSessionUtil.save(mytype))
		{
			actionResult.setResult(ActionResultInfo.Fail);
			actionResult.setFailInfo(ActionResultInfo.ErrorDBNewVPType);
			return;
		}
		
		// FIXME    TypeCount  可能会累加出错，对同个厂商并发增加产品类型时   shangyh
		// 6. 更新Vendors中相应条目的TypeCount
		vendor.setTypeCount(vendor.getTypeCount() + 1);		
		if (!DBSessionUtil.update(vendor))
		{
			actionResult.setResult(ActionResultInfo.Fail);
			actionResult.setFailInfo(ActionResultInfo.ErrorDBModifyVendor);
			return;
		}
		
		actionResult.setResult(ActionResultInfo.Success);
		actionResult.setData(mytype.getTypeId());
	}
	
	public static Producttype VPTypeQuery(long typeID)
	{
		String sql = "select * from producttype where TypeID=" + typeID;
				
		String msg0 = LogUtil.MsgDBSQL;
		msg0 += sql;
		LogUtil.info(LogUtil.LogTypeVPType, LogUtil.TypeIDNone, "VPTypeOperator Class: VPTypeQuery Method", msg0);
		
		@SuppressWarnings("unchecked")
		List<Producttype> mylist = DBSessionUtil.query(sql, Producttype.class);
		
		if (mylist == null || mylist.size() < 1)
		{
			return null;
		}
		return mylist.get(0);
	}

	public void VPTypeQuery(JSONObject input, JSONObject output)
	{
		String sql,totalSql;
        String productName = "";
        if(input.has("ProductName")){
            productName = input.getString("ProductName");
            input.remove("ProductName");
        }
//        int StartPage = input.getInt("StartPage");
//        int EndPage = input.getInt("EndPage");
//        input.remove("StartPage");
//        input.remove("EndPage");
		//根据参数在数据库中查找用户
		if (input.isEmpty()){
            sql = "select * from producttype where ApplyStatus>=0 ";
            totalSql = "select count(1) from producttype where ApplyStatus>=0 ";
		}else{
            if(!productName.equals(""))   {
			    sql = "select * from producttype where productname like '%" + productName + "%' and " + JsonSQLUtil.JSON2SQLWhere(true, input);
                totalSql = "select count(1) from producttype where productname like '%" + productName + "%' and " + JsonSQLUtil.JSON2SQLWhere(true, input);
            }else {
                if(input.has("VendorID")){
                    sql = "select * from producttype where " + JsonSQLUtil.JSON2SQLWhere(true, input);
                    totalSql = "select count(1) from producttype where " + JsonSQLUtil.JSON2SQLWhere(true, input);
                }else{
                    if(input.has("ApplyStatus")){
                        sql = "select * from producttype where " + JsonSQLUtil.JSON2SQLWhere(true, input);
                        totalSql = "select count(1) from producttype where " + JsonSQLUtil.JSON2SQLWhere(true, input);
                    }else{
                        sql = "select * from producttype where ApplyStatus>=0 and " + JsonSQLUtil.JSON2SQLWhere(true, input);
                        totalSql = "select count(1) from producttype where ApplyStatus>=0 and " + JsonSQLUtil.JSON2SQLWhere(true, input);
                    }
                }
            }
		}
//        sql += " limit " + StartPage + "," + EndPage;
		String msg0 = LogUtil.MsgDBSQL;
		msg0 += sql;
		LogUtil.info(LogUtil.LogTypeVPType, LogUtil.TypeIDNone, this.getClass().getName(), msg0);
		@SuppressWarnings("unchecked")
		List<Producttype> mylist = DBSessionUtil.query(sql, Producttype.class);
		if (mylist == null)
		{
			output.put("Result", ActionResultInfo.Success);
			output.put("FailInfo", ActionResultInfo.ErrorDBQueryVPType);
			return;
		}
		//生成输出JSON
		output.put("Result", ActionResultInfo.Success);
		JSONArray ja = new JSONArray();
		for(Producttype vptype : mylist)
		{
			JSONObject jo = new JSONObject();
			jo.put("TypeID", vptype.getTypeId());
			jo.put("ProductName", vptype.getProductName());
			jo.put("ApplyStatus", vptype.getApplyStatus());
			jo.put("Category", vptype.getCategory());
			jo.put("OriginCountry", vptype.getOriginCountry());
			jo.put("OriginRegion", vptype.getOriginRegion());
			jo.put("OutDistributors", vptype.getOutDistributors());
			jo.put("NetContent", vptype.getNetContent());
			jo.put("NetContentUnit", vptype.getNetContentUnit());
			jo.put("LabelProof", vptype.getLabelProof());
			jo.put("Brand", vptype.getBrand());
			jo.put("Remain", vptype.getRemain());
			if (vptype.getCategoryId() == null)
			{
				jo.put("CategoryID", 0);
			}
			else
			{
				jo.put("CategoryID", vptype.getCategoryId());
			}
			jo.put("VendorID", vptype.getVendorId());
			jo.put("AuthID", vptype.getAuthId());
			jo.put("ProductCount", vptype.getProductCount());
			jo.put("TagsBought", vptype.getTagsBought());
			jo.put("TagsApplied", vptype.getTagsApplied());
			jo.put("TagsUsed", vptype.getTagsUsed());
			jo.put("TagType", vptype.getTagType());
			jo.put("BatchCount", vptype.getBatchCount());
			jo.put("ShelfLife", vptype.getShelfLife());
			jo.put("ShelfLifeUnit", vptype.getShelfLifeUnit());
			if (vptype.getTypeInfo() == null)
				jo.put("TypeInfo", "null");
			else 				
				jo.put("TypeInfo", vptype.getTypeInfo());
			jo.put("HSCode", vptype.getHsCode());
			jo.put("CNProofID", vptype.getCnProofID());
			jo.put("ManageReport", vptype.getManageReport());
			jo.put("CNProofSample", vptype.getCnProofSample());
            if (vptype.getImporter() == null)
                jo.put("Importer", "null");
            else
                jo.put("Importer", vptype.getImporter());

            if (vptype.getRemark() == null)
                jo.put("Remark", "null");
            else
                jo.put("Remark", vptype.getRemark());

			ja.add(jo);
		}
		output.put("VPType", ja);
	}

	public void VPTypeModify(JSONObject input, ActionResult actionResult)
	{
		long typeId = input.getLong("TypeID");
		input.remove("TypeID");
		String sql = "update producttype set " + JsonSQLUtil.JSON2SQLSet(input);
		sql += " where TypeID=" + typeId;
		
		String msg = LogUtil.MsgDBSQL;
		msg += sql;
		LogUtil.info(LogUtil.LogTypeVPType, LogUtil.TypeIDNone, this.getClass().getName(), msg);
		if (!DBSessionUtil.update(sql))
		{
			actionResult.setResult(ActionResultInfo.Fail);
			actionResult.setFailInfo(ActionResultInfo.ErrorDBModifyVPType);
			return;
		}

		actionResult.setResult(ActionResultInfo.Success);
		actionResult.setData(null);
	}
	
	public void apply(JSONObject input, ActionResult actionResult) {
		long typeId = input.getLong("TypeID");
		input.remove("TypeID");
		int applyStatus = input.getInt("ApplyStatus");
		String sql = "update producttype set " + JsonSQLUtil.JSON2SQLSet(input);
		sql += " where TypeID=" + typeId;
		
		String msg = LogUtil.MsgDBSQL;
		msg += sql;
		LogUtil.info(LogUtil.LogTypeVPType, LogUtil.TypeIDNone, this.getClass().getName(), msg);
		if (!DBSessionUtil.update(sql))
		{
			actionResult.setResult(ActionResultInfo.Fail);
			actionResult.setFailInfo(ActionResultInfo.ErrorDBModifyVPType);
			return;
		}
		
		//审批通过
		if(applyStatus == 1) {
			// 7. 创建新的"商品认证表"
			String authTable = IDUtil.getProductAuthTable(typeId);
			sql = "create table " + authTable + 
					     " (" + 
					     "ProductID BIGINT UNSIGNED," +
					     "ProductKey VARCHAR(16)," +
					     "Random VARCHAR(16)," +
					     "AuthCode VARCHAR(60)," +
					     "Counter INT UNSIGNED," +
					     "BatchID INT UNSIGNED," +
					     "InspectCode VARCHAR(15)," +
                         "InspectID INT UNSIGNED," +
					     "LogisticsID VARCHAR(128)," +
					     "Status INT," +
					     "LinkInfoID INT," +
                         "SaleDate DATETIME," +
                         "SaleFlag INT UNSIGNED," +
                         "CoatCode VARCHAR(20)" +
					     ")ENGINE=InnoDB DEFAULT CHARACTER SET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT";
			
			msg = LogUtil.MsgDBSQL;
			msg += sql;
			LogUtil.info(LogUtil.LogTypeVPType, LogUtil.TypeIDNone, this.getClass().getName(), msg);

			if (!DBSessionUtil.update(sql))
			{
				actionResult.setResult(ActionResultInfo.Fail);
				actionResult.setFailInfo(ActionResultInfo.ErrorDBCreatePA);
				return;
			}
			
			// 8. 创建新的"商品批次信息表"
			String batchTable = IDUtil.getBatchTable(typeId);
			sql = "create table " + batchTable + 
				     " (" + 
				     "BatchID INT UNSIGNED," +
				     "TypeID BIGINT UNSIGNED," +
				     "ProductName VARCHAR(50)," +
				     "VendorID BIGINT UNSIGNED," +
				     "VendorBatchID VARCHAR(20)," +
				     "ProductDate DATETIME," +
				     "ExpireDate DATETIME," +
				     "BatchStatus INTEGER," +
				     "ProductCount INT UNSIGNED," +
				     "StartID BIGINT UNSIGNED, " +
				     "EndID BIGINT UNSIGNED," +
				     "InspectCode VARCHAR(15)," +
                     "InspectID INT UNSIGNED," +
				     "BatchKey VARCHAR(16)," +
				     "UserID INT UNSIGNED," +
				     "ProductSourceReport VARCHAR(50)," +
				     "ThirdReport VARCHAR(50)," +
				     "VendorReport VARCHAR(50)," +
				     "BatchInfo VARCHAR(50)," +
				     "AuthID INT UNSIGNED" +
				     ")ENGINE=InnoDB DEFAULT CHARACTER SET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT";
			
			msg = LogUtil.MsgDBSQL;
			msg += sql;
			LogUtil.info(LogUtil.LogTypeVPType, LogUtil.TypeIDNone, this.getClass().getName(), msg);
			if (!DBSessionUtil.update(sql))
			{
				actionResult.setResult(ActionResultInfo.Fail);
				actionResult.setFailInfo(ActionResultInfo.ErrorDBCreateVPBatch);
				return;
			}
		}
		
		actionResult.setResult(ActionResultInfo.Success);
		actionResult.setData(null);
	}

    public void VPTypeDel(JSONObject input, ActionResult actionResult) {
        if (input.has("TypeID")&&input.has("VendorID")&&input.has("ApplyStatus")){
            String sql = "DELETE FROM producttype WHERE ApplyStatus='"+input.getString("ApplyStatus")+"' AND VendorID="+input.getLong("VendorID")+"  AND TypeID="+input.getLong("TypeID");

            String msg = LogUtil.MsgDBSQL;
            msg += sql;
            LogUtil.info(LogUtil.LogTypeVPType, LogUtil.TypeIDNone, this.getClass().getName(), msg);
            if (!DBSessionUtil.update(sql))
            {
                actionResult.setResult(ActionResultInfo.Fail);
                actionResult.setFailInfo(ActionResultInfo.ErrorDBModifyVPType);
                return;
            }
            actionResult.setResult(ActionResultInfo.Success);
            actionResult.setData(null);
            return;
        }else {
            actionResult.setResult(ActionResultInfo.Fail);
            actionResult.setFailInfo(ActionResultInfo.ErrorDBModifyVPType);
            return;
        }

    }
}
