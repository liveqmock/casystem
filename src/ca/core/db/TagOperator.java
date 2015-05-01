package ca.core.db;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ca.core.logic.ActionResultInfo;
import ca.core.logic.DBSessionUtil;
import ca.core.logic.DESUtil;
import ca.core.logic.DateUtil;
import ca.core.logic.IDUtil;
import ca.core.logic.JsonSQLUtil;
import ca.core.logic.LogUtil;

@SuppressWarnings("unused")
public class TagOperator
{	
	public static final int TagConfirmNormal = 1;      // 正常标签确认
	public static final int TagConfirmDelete = 2;      // 删除临时表中所有内容
	private static final int TagApplyMaxCount = 20000; // 最大申请数量
	
	private static Object lockVPType = new Object();     //确认时用到的锁
	
	public TagOperator() {}
	
	public void tagQuery(JSONObject input, JSONObject output)
	{
		String authCode = input.getString("TagID");
		authCode = authCode.toLowerCase();
		// 1. 将authCode分段解释，如厂家商品类型ID，即TypeID， 商品编号，认证码
		long typeID = IDUtil.getTypeId(authCode);
		String authTable = "pa" + Long.toHexString(typeID);
		// 3. 根据商品编号，在商品认证表中搜索相应的条目
		long productId = IDUtil.getProductId(authCode);
		String sql = "select * from " + authTable + " where ProductID=" + productId;

		String msg0 = LogUtil.MsgDBSQL;
		msg0 += sql;
		LogUtil.info(LogUtil.LogTypeTag, typeID, this.getClass().getName(), msg0);
		
		@SuppressWarnings("unchecked")
		List<Productauth> authlist = DBSessionUtil.query(sql, Productauth.class);
		if (authlist == null || authlist.size() != 1)
		{
			TagAuthorize.returnTagUnknown(output);
			return;
		}
		// 4. 对认证码进行认证
		Productauth auth = authlist.get(0);
		
		// 5. 输出商品信息
		output.put("Result", ActionResultInfo.Success);
		JSONObject jo = new JSONObject();
		
		jo.put("BatchID", auth.getBatchId());
		jo.put("TypeID", typeID);
        jo.put("InspectCode",auth.getInspectCode());
        jo.put("InspectID",auth.getInspectID());
        jo.put("LogisticsID",auth.getLogisticsId());
		jo.put("Count", auth.getCounter());
		jo.put("Status", auth.getStatus());
		output.put("TagInfo", jo);
	}
	
	/*
	 * CA防伪标签购买
	 * 1. 根据TypeID从数据库中获取厂家商品类型相应条目；
	 * 2. 将相应条目的TagsBought进行相应的调整，重新入库
	 * 3. 在CA防伪标签交易表中增加一个交易记录条目
	 * 
	 * @param input
	 *   包含：TypeID, ProductCount, Amount, UserID
	 * @param output
	 *   输出JSON，包含：result，FailInfo
	 */
	public void TagBuy(JSONObject input, JSONObject output)
	{
		long typeid = input.getLong("TypeID");

		//1. 根据TypeID从数据库中获取厂家商品类型相应条目；
		String sql = "select * from producttype where TypeID=" + typeid;

		String msg0 = LogUtil.MsgDBSQL;
		msg0 += sql;
		LogUtil.info(LogUtil.LogTypeTag, typeid, this.getClass().getName(), msg0);
		
		@SuppressWarnings("unchecked")
		List<Producttype> mylist = DBSessionUtil.query(sql, Producttype.class);
		if (mylist == null || mylist.size() < 1)
		{
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorDBQueryVPType);
			return;
		}
		
		//  FIXME getTagsBought 可能会累加错误，引发原因可能是授权方（包括同一个）并发为同一个厂家授权     shangyh
		//2. 将相应条目的TagsBought进行相应的调整，重新入库
		Producttype dbtype = mylist.get(0);
		dbtype.setTagsBought(dbtype.getTagsBought() + input.getInt("ProductCount"));
		
		
		String msg = LogUtil.MsgDBUpdate;
		msg += dbtype.getClass().getName();
		LogUtil.info(LogUtil.LogTypeTag, typeid, this.getClass().getName(), msg);
		
		if (!DBSessionUtil.update(dbtype))
		{
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorDBModifyVPType);
			return;
		}
		
		//3. 在CA防伪标签交易表中增加一个交易记录条目
		Tagdeals mydeal = new Tagdeals();
		mydeal.setProductCount(input.getInt("ProductCount"));
		if (input.has("Amount"))
		{
			mydeal.setAmount(input.getDouble("Amount"));
		}
		else
		{
			mydeal.setAmount(0);
		}
		mydeal.setDealsType(0);
		mydeal.setTypeId(typeid);
		mydeal.setUserId(input.getInt("UserID"));
		Date now = new Date(System.currentTimeMillis());
		mydeal.setDealsTime(now);

		String msg1 = LogUtil.MsgDBInsert;
		msg1 += mydeal.getClass().getName();
		LogUtil.info(LogUtil.LogTypeTag, typeid, this.getClass().getName(), msg1);
		
		if (!DBSessionUtil.save(mydeal))
		{
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorDBNewTagDeal);
			return;
		}
		
		output.put("Result", ActionResultInfo.Success);
	}
	
	/*
	 * CA防伪标签交易记录查询
	 * 1. 在CA防伪标签交易表中进行搜索
	 * 2. 将搜索结果放入JSON，返回
	 * 
	 * 
	 * @param input
	 *   CA防伪标签交易记录表中相应的条目局部信息
	 * @param output
	 *   CA防伪标签交易记录表中搜索的条目数组
	 */
	public void TagDealQuery(JSONObject input, JSONObject output)
	{
		boolean bFirst = true;
		
		//1. 准备查找SQL语句。准备查询
		String sql = "select * from tagdeals where ";
		
		if (input.has("DateStart"))
		{
			String strDate = input.getString("DateStart");
			sql += "DealsTime > '" + strDate + "'";
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
			sql += "DealsTime < '" + strDate + "'";
			bFirst = false;
			input.remove("DateEnd");
		}
		if (input.has("ProductCount"))
		{
			if (!bFirst)
			{
				sql += " and ";
			}
			sql += "ProductCount > " + input.getString("ProductCount");
			bFirst = false;
			input.remove("ProductCount");
		}
		sql += JsonSQLUtil.JSON2SQLWhere(bFirst, input);

		String msg0 = LogUtil.MsgDBSQL;
		msg0 += sql;
		LogUtil.info(LogUtil.LogTypeTag, LogUtil.TypeIDNone, this.getClass().getName(), msg0);
		
		@SuppressWarnings("unchecked")
		List<Tagdeals> mylist = DBSessionUtil.query(sql, Tagdeals.class);
		if (mylist == null)
		{
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorDBQueryTagDEal);
			return;
		}
		//2. 生成输出JSON
		output.put("Result", ActionResultInfo.Success);
		JSONArray ja = new JSONArray();
		for(Tagdeals tdeal : mylist)
		{
			JSONObject jo = new JSONObject();
			jo.put("DealsID", tdeal.getDealsId());
			jo.put("TypeID", tdeal.getTypeId());
			jo.put("ProductCount", tdeal.getProductCount());
			jo.put("DealsTime", DateUtil.convertDate2String(tdeal.getDealsTime(), DateUtil.DATE_FORMAT));
			jo.put("DealsType", tdeal.getDealsType());
			jo.put("UserID", tdeal.getUserId());
			jo.put("Amount", tdeal.getAmount());
			ja.add(jo);
		}
		output.put("Deals", ja);
	}
	
	/*
	 * CA防伪标签使用申请
	 * 1. 确认用户已登录，获取用户ID
	 * 2. 根据TypeID从数据库中获取厂家商品类型相应条目；特别是商品类型的母密钥
	 * 3. 根据TypeID，SessionID创建一个临时表
	 * 4. 如果表不存在，新建临时表，如果表存在则获取最大ProductID
	 * 5. 在临时表中创建每个商品认证信息，并输出到文件
	 * 
	 * @param input
	 *   包含：TypeID, ProductCount
	 * @param output
	 *   输出JSON，包含：result，FailInfo， tagURL
	 */
	public void TagApply(JSONObject input, JSONObject output) throws IOException
	{
		long productID;
		Producttype vpType = null;
	
		long typeID = input.getLong("TypeID");
		int tagCount = input.getInt("ProductCount");
		int deviceID = input.getInt("DeviceID");
		
		if (tagCount <= 0)
		{
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorApplyZeroTag);
			return;
		}
		if (deviceID > IDUtil.DeviceMaxId)
		{
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorLackofBalance);
			return;
		}
		if (tagCount > TagOperator.TagApplyMaxCount)
		{
			tagCount = TagOperator.TagApplyMaxCount;
		}

		
		// 1. 确认用户已登录，获取用户ID
		int userID;
		try
		{
//			HttpSession httpsession = ServletActionContext.getRequest().getSession();
//			userID = (Integer) httpsession.getAttribute("UserID");
			String token = ServletActionContext.getRequest().getHeader(SessionOperator.HeaderTokenName);
			if (token == null)
			{
				output.put("Result", ActionResultInfo.Fail);
				output.put("FailInfo", ActionResultInfo.ErrorUserOffline);
				return;
			}

			SessionOperator sOperator = new SessionOperator();
			if (!sOperator.sessionUpdate(token))
			{
				output.put("Result", ActionResultInfo.Fail);
				output.put("FailInfo", ActionResultInfo.ErrorUserOffline);
				return;
			}

			userID = sOperator.SessionUserId(token);
		}
		catch (Exception e)
		{
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorUserOffline);
			return;
		}
		// 加锁，防止两个线程用并发，导致TagsApplied计数存在问题
		synchronized(lockVPType)
		{
			// 2. 根据TypeID从数据库中获取厂家商品类型相应条目：商品类型的母密钥,ProductID,DeviceCount等
			vpType = VPTypeOperator.VPTypeQuery(typeID);
			if (vpType == null)
			{
				output.put("Result", ActionResultInfo.Fail);
				output.put("FailInfo", ActionResultInfo.ErrorVPType);
				return;
			}
			if ((vpType.getTagsBought() - vpType.getTagsUsed() - vpType.getTagsApplied()) < tagCount)
			{
				output.put("Result", ActionResultInfo.Fail);
				output.put("FailInfo", ActionResultInfo.ErrorLackofBalance);
				return;
			}
			long deviceCount =  vpType.getDeviceCount(deviceID);
			productID = IDUtil.getProductId(deviceID, deviceCount);
			
			long newTagsApplied = vpType.getTagsApplied() + tagCount;
			vpType.setTagsApplied(newTagsApplied);
	
			String msg = LogUtil.MsgDBUpdate;
			msg += vpType.getClass().getName();
			LogUtil.info(LogUtil.LogTypeTag, typeID, this.getClass().getName(), msg);
	
			if (!DBSessionUtil.update(vpType))
			{
				output.put("Result", ActionResultInfo.Fail);
				output.put("FailInfo", ActionResultInfo.ErrorDBModifyVPType);
				return;
			}
		}
		// 3. 根据TypeID，SessionID创建一个临时表
		String tmpTable = IDUtil.getTempPaTable(typeID, userID, deviceID);
		// 4. 如果表不存在，新建临时表，如果表存在则获取最大ProductID
		if (!DBSessionUtil.isExistTable(tmpTable))
		{
			String sql =
					"create table " + tmpTable + 
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
			
			String msg = LogUtil.MsgDBSQL;
			msg += sql;
			LogUtil.info(LogUtil.LogTypeTag, typeID, this.getClass().getName(), msg);
			if (!DBSessionUtil.update(sql))
			{
				output.put("Result", ActionResultInfo.Fail);
				output.put("FailInfo", ActionResultInfo.ErrorDBCreateTmpPA);
				return;
			}
		}
		else
		{
			String sql = "select max(" + tmpTable + ".ProductID) from " + tmpTable;
			BigInteger tmp = (BigInteger)DBSessionUtil.getUniqueResult(sql);
			if (tmp != null) productID = tmp.longValue() + 1;
		}
		// 5. 在临时表中创建每个商品认证信息，并输出到文件
		String strDir = ServletActionContext.getServletContext().getRealPath("/");
		strDir +=  "/" + tmpTable + ".ca";
		FileOutputStream fos = new FileOutputStream(strDir);
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		BufferedWriter bw = new BufferedWriter(osw);
		Session session = DBSessionUtil.getDBSession();
		try
		{
			Transaction tx = session.beginTransaction();
			for (int tagIndex = 0; tagIndex < tagCount; tagIndex++,productID++)
			{
				String sql = "insert into " + tmpTable +  
						" (ProductID, ProductKey, Random, AuthCode, Counter, BatchID, InspectCode,InspectID, LogisticsID, Status, LinkInfoID,SaleFlag,CoatCode) values (";
				sql += productID + ", ";
				// ProductKey
				String typeKey = vpType.getTypeKey();
				String strProductKey = DESUtil.fadeInDES(typeKey, Long.toHexString(productID));
				if (strProductKey.length() >= 32)
				{
					strProductKey = String.valueOf(strProductKey.toCharArray(), 16, 16);
				}
				sql += "'" + strProductKey + "', ";
				// 随机数
				Random random = new Random();
				String strRandom = Long.toHexString(random.nextLong());
				sql += "'" + strRandom + "', ";
				// 认证码
				String strAuthCode = DESUtil.fadeInDES(strProductKey, strRandom);
				if (strAuthCode.length() > 16)
				{
					strAuthCode = String.valueOf(strAuthCode.toCharArray(), 0, 16);
				}
                // 二次认证码
                String coatCode = DESUtil.fadeInDES(strAuthCode, strRandom);
                if(coatCode.length() > 8){
                    coatCode = String.valueOf(coatCode.toCharArray(), 0, 8);
                }
				sql += "'" + strAuthCode + "', ";
				// 认证计数初始化为0
				sql += "0, ";
				// 商品批次ID初始化为0, 等确认时再进行赋值
				sql += "'0', ";
				//报批id
				sql += "'0', ";
				sql += "0, ";
				//物流id
				sql += "'0', ";
				// 状态初始化为0
				sql += "0, ";
				// 关联信息id
				sql += "0,";
                //未出售
                sql += "0,";
                //二次校验码
				sql += "'" + coatCode + "')";

				SQLQuery query = session.createSQLQuery(sql);
				query.executeUpdate();
				
				// 输出到文件
				if (strAuthCode.length() > 8)
				{
					strAuthCode = String.valueOf(strAuthCode.toCharArray(), 0, 8);
				}
				if(input.has("TagType")){
                    int tagType = input.getInt("TagType");
                    switch (tagType){
                        case 1: //C码
                            bw.write(IDUtil.getProductId(typeID, productID) + strAuthCode + "," + String.format("%010d",productID));
                            break;
                        case 2: //涂层标签
                            bw.write(IDUtil.getProductId(typeID, productID) + strAuthCode + "," + String.format("%010d",productID) + "," + coatCode.substring(4));
                            break;
                        case 3: //芯片
                            bw.write(IDUtil.getProductId(typeID, productID) + strAuthCode + "," + String.format("%010d",productID));
                            break;
                    }
                } else {
                    bw.write(IDUtil.getProductId(typeID, productID) + strAuthCode);
                }
				bw.newLine();
				if (tagIndex % 50 == 0)
				{
					session.flush();
					session.clear();
					bw.flush();
				}
			}
			session.flush();
			session.clear();
			tx.commit();
			DBSessionUtil.closeDBSession(session);
			bw.flush();
			bw.close();
			
			String msg = "标签申请， 临时表中增加标签起始ID：" + productID + " 数量：" + tagCount;
			LogUtil.info(LogUtil.LogTypeTag, typeID, this.getClass().getName(), msg);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			DBSessionUtil.closeDBSession(session);
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorDBNewTmpPA);
			return;
		}
		output.put("Result", ActionResultInfo.Success);
		output.put("tagURL", tmpTable + ".ca");
	}
	
	
	
	/*
	 * CA防伪标签生成
	 * 1. 确认用户已登录，获取用户ID
	 * 2. 根据TypeID从数据库中获取厂家商品类型相应条目；特别是商品类型的母密钥
	 * 3. 根据TypeID，SessionID创建一个临时表
	 * 4. 如果表不存在，新建临时表，如果表存在则获取最大ProductID
	 * 5. 在表中创建每个商品认证信息
	 * 
	 * @param input
	 *   包含：TypeID, ProductCount
	 * @param output
	 *   输出JSON，包含：result，FailInfo， tagURL
	 */
	public void TagGenerate(JSONObject input, JSONObject output) throws IOException
	{
		long productID;
		Producttype vpType = null;
		String sql = "";
	
		long typeID = input.getLong("TypeID");
		int tagCount = input.getInt("ProductCount");
        int tagApplyCount = input.getInt("TagApplyCount");
		String inspectCode = input.getString("InspectCode");
		Integer inspectID = input.getInt("InspectID");
		int userID = input.getInt("UserID");
		/* FIXME default is device0 */
		int deviceID = 0;
		
		if (tagCount <= 0)
		{
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorApplyZeroTag);
			return;
		}

		if (tagCount > TagOperator.TagApplyMaxCount)
		{
			tagCount = TagOperator.TagApplyMaxCount;
		}

		synchronized(lockVPType)
		{
			// 2. 根据TypeID从数据库中获取厂家商品类型相应条目：商品类型的母密钥,ProductID,DeviceCount等
			vpType = VPTypeOperator.VPTypeQuery(typeID);
			if (vpType == null)
			{
				output.put("Result", ActionResultInfo.Fail);
				output.put("FailInfo", ActionResultInfo.ErrorVPType);
				return;
			}
			if ((vpType.getTagsBought() - vpType.getTagsUsed()) < tagCount)
			{
				output.put("Result", ActionResultInfo.Fail);
				output.put("FailInfo", ActionResultInfo.ErrorLackofBalance);
				return;
			}
			long deviceCount =  vpType.getDeviceCount(deviceID);
			productID = IDUtil.getProductId(deviceID, deviceCount);
//
//			long newTagsUsed = vpType.getTagsUsed() + tagApplyCount;
//			vpType.setTagsUsed(newTagsUsed);
//			vpType.setBatchCount(vpType.getBatchCount() + 1);
//			vpType.setProductCount(vpType.getProductCount() + tagCount);
//			vpType.setRemain(vpType.getRemain() + tagCount);
//
//			String msg = LogUtil.MsgDBUpdate;
//			msg += vpType.getClass().getName();
//			LogUtil.info(LogUtil.LogTypeTag, typeID, this.getClass().getName(), msg);
//
//			if (!DBSessionUtil.update(vpType))
//			{
//				output.put("Result", ActionResultInfo.Fail);
//				output.put("FailInfo", ActionResultInfo.ErrorApplyTag);
//				return;
//			}
//
		}
		
		// 7. 根据TypeID获得数据库中"CA防伪标签交易表"
		Date now = new Date(System.currentTimeMillis());
		Tagdeals mydeal = new Tagdeals();
		mydeal.setProductCount(tagCount);
		mydeal.setAmount(0);
		mydeal.setDealsType(1);
		mydeal.setTypeId(typeID);
		/* no User info */
		mydeal.setUserId(input.getInt("VendorID"));
		mydeal.setDealsTime(now);

		String msg3 = LogUtil.MsgDBInsert;
		msg3 += mydeal.getClass().getName();
		LogUtil.info(LogUtil.LogTypeTag, typeID, this.getClass().getName(), msg3);
		
		if (!DBSessionUtil.save(mydeal))
		{
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorDBNewTagDeal);
			return;
		}
		
		// 3. 根据TypeID生成商品验证表
		String authTable = IDUtil.getProductAuthTable(typeID);
		
		// 4. 创建商品类型时已经新建，是否需要检测？ ？？如果表不存在，新建商品表，如果表存在则获取最大ProductID,
		if (!DBSessionUtil.isExistTable(authTable))
		{
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
			
			String msg = LogUtil.MsgDBSQL;
			msg += sql;
			LogUtil.info(LogUtil.LogTypeTag, typeID, this.getClass().getName(), msg);
			if (!DBSessionUtil.update(sql))
			{
				output.put("Result", ActionResultInfo.Fail);
				output.put("FailInfo", ActionResultInfo.ErrorDBCreateTmpPA);
				return;
			}
		}
		else
		{
			sql = "select max(" + authTable + ".ProductID) from " + authTable;
			BigInteger tmp = (BigInteger)DBSessionUtil.getUniqueResult(sql);
			if (tmp != null) productID = tmp.longValue() + 1;
		}

        String batchTable = IDUtil.getBatchTable(typeID);
        sql = "update " + batchTable + " set StartID=" + productID + ",EndID=" + (productID + tagApplyCount - 1) + " where TypeID=" + typeID + " and InspectID='" + inspectID + "'";
        String msg = LogUtil.MsgDBSQL;
        msg += sql;
        LogUtil.info(LogUtil.LogTypeTag, typeID, this.getClass().getName(), msg);
        if (!DBSessionUtil.update(sql))
        {
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorDBNewBatch);
            return;
        }

		Session session = DBSessionUtil.getDBSession();
		try
		{
			Transaction tx = session.beginTransaction();
			for (int tagIndex = 0; tagIndex < tagApplyCount; tagIndex++,productID++)
			{
				sql = "insert into " + authTable +  
					  " (ProductID, ProductKey, Random, AuthCode, Counter, BatchID, InspectCode,InspectID, LogisticsID, Status, LinkInfoID,SaleFlag,CoatCode) values (";
				sql += productID + ", ";
				// ProductKey
				String typeKey = vpType.getTypeKey();
				String strProductKey = DESUtil.fadeInDES(typeKey, Long.toHexString(productID));
				if (strProductKey.length() >= 32)
				{
					strProductKey = String.valueOf(strProductKey.toCharArray(), 16, 16);
				}
				sql += "'" + strProductKey + "', ";
				// 随机数
				Random random = new Random();
				String strRandom = Long.toHexString(random.nextLong());
				sql += "'" + strRandom + "', ";
				// 认证码
				String strAuthCode = DESUtil.fadeInDES(strProductKey, strRandom);
				if (strAuthCode.length() > 16)
				{
					strAuthCode = String.valueOf(strAuthCode.toCharArray(), 0, 16);
				}
                //二次认证码
                String coatCode = DESUtil.fadeInDES(strAuthCode, strRandom);
                if (coatCode.length() > 8)
                {
                    coatCode = String.valueOf(coatCode.toCharArray(), 0, 8);
                }
				sql += "'" + strAuthCode + "', ";
				// 认证计数初始化为0
				sql += "0, ";
				// 商品批次ID初始化为0, 等确认时再进行赋值
				sql += vpType.getBatchCount() + ", ";
				//报批id
				sql += "'" + inspectCode + "', ";
				sql += inspectID + ", ";
				//物流id
				sql += "'0', ";
				// 状态初始化为0
				sql += "0, ";
				// 关联信息id
				sql += "0,";
                //未出售
                sql += "0,";
                //二次校验码
                sql += "'" + coatCode + "')";
				SQLQuery query = session.createSQLQuery(sql);
				query.executeUpdate();

				if (tagIndex % 50 == 0)
				{
					session.flush();
					session.clear();
				}
			}
			session.flush();
			session.clear();
			tx.commit();
			DBSessionUtil.closeDBSession(session);
			
			msg = "标签生成，增加标签起始ID：" + productID + " 数量：" + tagCount;
			LogUtil.info(LogUtil.LogTypeTag, typeID, this.getClass().getName(), msg);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			DBSessionUtil.closeDBSession(session);
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorDBNewTmpPA);
			return;
		}
		output.put("Result", ActionResultInfo.Success);
	}
	
	/*
	 * CA防伪标签使用确认
	 * 0. 用户是否登录
	 * 1. 获取临时认证表名，并查找所有表项
	 * 2. 根据TypeID从数据库中获取厂家商品类型相应条目
	 * 3. 重新计算TagCount, startID；
	 * 4. 将vpType的TagsUsed和ProductCount进行相应的调整，重新入库
	 * 6. 根据TypeID获得数据中"商品批次信息表"， 在该表中增加一个批次条目
	 * 7. 根据TypeID在"CA防伪标签交易表"中增加一个交易记录条目
	 * 8. 将临时表中的数据加入“商品认证表”
	 * 9. 删除临时表，或删除表中内容
	 * 
	 * @param input
	 *   包含：TypeID, StartProductID， ProductCount
	 * @param output
	 *   输出JSON，包含：result，FailInfo
	 */
	@SuppressWarnings("unchecked")
	public void TagConfirm(JSONObject input, JSONObject output)
	{
		long tmpTableSize = 0;
		Producttype vpType = null;
		long typeID = input.getLong("TypeID");
		int followAction = input.getInt("FollowAction");
		String strStartID = input.getString("StartProductID");
		strStartID = strStartID.toLowerCase();
		int tagCount = input.getInt("ProductCount");
		int deviceID = input.getInt("DeviceID");
		long startID = IDUtil.getProductId(strStartID);
		
		// 0. 用户是否登录
		int userID;
		try
		{
//			HttpSession httpsession = ServletActionContext.getRequest().getSession();
//			userID = (Integer) httpsession.getAttribute("UserID");
			String token = ServletActionContext.getRequest().getHeader(SessionOperator.HeaderTokenName);
			if (token == null)
			{
				output.put("Result", ActionResultInfo.Fail);
				output.put("FailInfo", ActionResultInfo.ErrorUserOffline);
				return;
			}

			SessionOperator sOperator = new SessionOperator();
			if (!sOperator.sessionUpdate(token))
			{
				output.put("Result", ActionResultInfo.Fail);
				output.put("FailInfo", ActionResultInfo.ErrorUserOffline);
				return;
			}

			userID = sOperator.SessionUserId(token);

		}
		catch (Exception e)
		{
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorUserOffline);
			return;
		}
		
		// 1. 获取临时认证表名，并查找所有表项
		String tmpTable = IDUtil.getTempPaTable(typeID, userID, deviceID);
		String sql = "select count(*) from " + tmpTable;		
		String msg = LogUtil.MsgDBSQL;
		msg += sql;
		LogUtil.info(LogUtil.LogTypeTag, typeID, this.getClass().getName(), msg);

		BigInteger tmp = (BigInteger)DBSessionUtil.getUniqueResult(sql);
		if (tmp != null)   tmpTableSize = tmp.longValue();
		if (tmp == null || tmpTableSize == 0)
		{
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorNoAppliedTag);
			return;
		}
		// 加锁，防止两个线程用并发，导致TagsUsed，ProductCount BatchCount计数存在问题
		synchronized(lockVPType)
		{
			// 2. 根据TypeID从数据库中获取厂家商品类型相应条目
			vpType = VPTypeOperator.VPTypeQuery(typeID);
			if (vpType == null)
			{
				output.put("Result", ActionResultInfo.Fail);
				output.put("FailInfo", ActionResultInfo.ErrorVPType);
				return;
			}
			long deviceCount = vpType.getDeviceCount(deviceID);
			// 3. 重新计算TagCount, startID
			deviceCount = IDUtil.getProductId(deviceID, deviceCount);
			if (deviceCount > startID)
			{
				tagCount = (tagCount - (int)(deviceCount - startID));
				startID = deviceCount;
			}
			if ((tagCount + startID - deviceCount) > tmpTableSize)
			{
				tagCount = (int)(tmpTableSize + (deviceCount - startID));
			}
			if (tagCount < 0)
			{
				tagCount = 0;
			}
			
			// 4. 将vpType的TagsUsed和ProductCount进行相应的调整，重新入库
			long newTagsApplied = vpType.getTagsApplied() - tagCount;
			if (followAction == TagOperator.TagConfirmDelete)
			{
				newTagsApplied = vpType.getTagsApplied() - tmpTableSize;
			}
			vpType.setTagsApplied(newTagsApplied);
			if (tagCount != 0)
			{
				vpType.setTagsUsed(vpType.getTagsUsed() + tagCount);
				vpType.setProductCount(vpType.getProductCount() + tagCount);
				vpType.setDeviceCount(deviceID, (startID + tagCount - 1) & IDUtil.ProductIDMask);
				vpType.setProductCount(vpType.getProductCount() + tagCount);
				vpType.setRemain(vpType.getRemain() + tagCount);
				vpType.setBatchCount(vpType.getBatchCount() + 1);
			}
	
			msg = LogUtil.MsgDBUpdate;
			msg += vpType.getClass().getName();
			LogUtil.info(LogUtil.LogTypeTag, typeID, this.getClass().getName(), msg);
			
			if (!DBSessionUtil.update(vpType))
			{
				output.put("Result", ActionResultInfo.Fail);
				output.put("FailInfo", ActionResultInfo.ErrorDBModifyVPType);
				return;
			}
		}
		// 5. 如果确认个数为0，判断是否清理标签申请
		if (tagCount > 0)
		{
			Date now = new Date(System.currentTimeMillis());
			// 6. 根据TypeID获得数据中"商品批次信息表"， 在该表中增加一个批次条目
			String batchTable = IDUtil.getBatchTable(typeID);
			sql = "insert into " + batchTable +  
				  " (BatchID, TypeID, ProductName, VendorID, VendorBatchID, ProductDate, ExpireDate, " +
				  "BatchStatus, ProductCount, StartID, EndID, InspectCode,InspectID, UserID, AuthID) values (";
			sql += vpType.getBatchCount() + ", ";           // BatchID
			sql += vpType.getTypeId() + ", ";           // TypeID
			sql += "'" + vpType.getProductName() + "', ";   // 商品名称
			sql += vpType.getVendorId() + ",";   // 企业编号
			sql += "'', ";   // 企业关联的生产批次
			sql += "'" + DateUtil.convertDate2String(now, DateUtil.DATE_FORMAT) + "', ";          // 生产日期
			Date expireDate = now;
			int shelfUnit = Integer.parseInt(vpType.getShelfLifeUnit(),10);
			Calendar ca = Calendar.getInstance();
			switch(shelfUnit) {
			case 1 :
				ca.add(Calendar.YEAR, Integer.parseInt(vpType.getShelfLife(), 10));
				break;
			case 2 :
				ca.add(Calendar.MONTH, Integer.parseInt(vpType.getShelfLife(), 10));
				break;
			case 3 :
				ca.add(Calendar.DATE, Integer.parseInt(vpType.getShelfLife(), 10));
				break;
			}
			sql += "'" + DateUtil.convertDate2String(ca.getTime(), DateUtil.DATE_FORMAT) + "', "; // 过期时间
			sql += TagAuthorize.TagInvalid + ", ";   // 状态为激活
			sql += tagCount + ",";
			sql += startID + ",";
			sql += (startID + tagCount - 1) + ",";
			sql += "'0',";
			sql += "0,";
			sql += userID + ", ";
			sql += vpType.getAuthId() + ")";
	
			msg = LogUtil.MsgDBSQL;
			msg += sql;
			LogUtil.info(LogUtil.LogTypeTag, typeID, this.getClass().getName(), msg);
			if (!DBSessionUtil.update(sql))
			{
				output.put("Result", ActionResultInfo.Fail);
				output.put("FailInfo", ActionResultInfo.ErrorDBNewBatch);
				return;
			}
			
			// 7. 根据TypeID获得数据库中"CA防伪标签交易表"
			Tagdeals mydeal = new Tagdeals();
			mydeal.setProductCount(tagCount);
			mydeal.setAmount(0);
			mydeal.setDealsType(1);
			mydeal.setTypeId(typeID);
			mydeal.setUserId(userID);
			mydeal.setDealsTime(now);
	
			String msg3 = LogUtil.MsgDBInsert;
			msg3 += mydeal.getClass().getName();
			LogUtil.info(LogUtil.LogTypeTag, typeID, this.getClass().getName(), msg3);
			
			if (!DBSessionUtil.save(mydeal))
			{
				output.put("Result", ActionResultInfo.Fail);
				output.put("FailInfo", ActionResultInfo.ErrorDBNewTagDeal);
				return;
			}
			
			// 8. 将临时表中的数据加入“商品认证表”
			Session session = DBSessionUtil.getDBSession();
			try
			{
				int recCount = 0;
				Transaction tx = session.beginTransaction();
				String authTable = IDUtil.getProductAuthTable(typeID);
				// 从临时表中查找出范围内的认证项
				sql = "select * from " + tmpTable + 
					  " where ProductID >= " + startID +
				      " and ProductID < " + (tagCount + startID);
				msg = LogUtil.MsgDBSQL;
				msg += sql;
				LogUtil.info(LogUtil.LogTypeTag, typeID, this.getClass().getName(), msg);
				List<Productauth> listTempAuth = DBSessionUtil.query(sql, Productauth.class);
				if (listTempAuth == null || listTempAuth.size() == 0)
				{
					output.put("Result", ActionResultInfo.Fail);
					output.put("FailInfo", ActionResultInfo.ErrorNoAppliedTag);
					return;
				}

				msg = "标签确认， 标签起始ID：" + startID + " 数量：" + tagCount;
				LogUtil.info(LogUtil.LogTypeTag, typeID, this.getClass().getName(), msg);
				for(Productauth auth : listTempAuth)
				{
					//插入新的认证项
					sql  = "insert into " + authTable +  
						   " (ProductID, ProductKey, Random, AuthCode, Counter, BatchID, " +
						   "InspectCode, InspectID, LogisticsID, Status, LinkInfoID,SaleFlag,CoatCode) values (";
					sql += auth.getProductId() + ", ";
					sql += "'" + auth.getProductKey() + "', ";
					sql += "'" + auth.getRandom() + "', ";
					sql += "'" + auth.getAuthCode() + "', ";
					sql += auth.getCounter() + ", ";
					sql += vpType.getBatchCount() + ", ";
					sql += "'" + auth.getInspectCode() + "', ";
					sql += "" + auth.getInspectID() + ", ";
					sql += auth.getLogisticsId() + ", ";
					sql += auth.getStatus() + ", ";
					sql += auth.getLinkInfoId() + ", ";
					sql += auth.getSaleFlag() + ", ";
					sql += "'" + auth.getCoatCode() + "') ";
					SQLQuery query = session.createSQLQuery(sql);
					query.executeUpdate();
					recCount++;
					if (recCount % 50 == 0)
					{
						session.flush();
						session.clear();
					}
				}
				session.flush();
				session.clear();
				tx.commit();
				DBSessionUtil.closeDBSession(session);
				//从临时数据表中删除新的认证项
				if (followAction == TagOperator.TagConfirmNormal)
				{
					sql = "delete from " + tmpTable + " where ProductID >= " + startID + " and ProductID < " + (tagCount + startID);	
					msg = LogUtil.MsgDBSQL;
					msg += sql;
					LogUtil.info(LogUtil.LogTypeTag, typeID, this.getClass().getName(), msg);
					DBSessionUtil.update(sql);
				}
				
			}
			catch (Exception e)
			{
				e.printStackTrace();
				DBSessionUtil.closeDBSession(session);
				output.put("Result", ActionResultInfo.Fail);
				output.put("FailInfo", ActionResultInfo.ErrorDBTmpPA2PA);
				return;
			}
		}
		// 9. 删除临时表
		if (followAction == TagOperator.TagConfirmDelete)
		{
			sql = "drop table " + tmpTable;
	
			String msg1 = LogUtil.MsgDBSQL;
			msg1 += sql;
			LogUtil.info(LogUtil.LogTypeTag, typeID, this.getClass().getName(), msg1);
			
			if (!DBSessionUtil.update(sql))
			{
				output.put("Result", ActionResultInfo.Fail);
				output.put("FailInfo", ActionResultInfo.ErrorDBDeleteTmpPA);
				return;
			}
		}
		
		output.put("Result", ActionResultInfo.Success);
		output.put("StartTagID", startID);
		output.put("TagCount", tagCount);
	}
	
	public void TagUpdate(JSONObject input, JSONObject output) {
		long typeID = input.getLong("TypeID");
		String inspectCode = input.getString("InspectCode");
		Integer inspectID = input.getInt("InspectID");
		long startID = input.getLong("StartID");
		long endID = input.getLong("EndID");
		
		String authTable = IDUtil.getProductAuthTable(typeID);
		String sql = "update " + authTable + " set inspectcode = '" + inspectCode + "',inspectid=" + inspectID + " where productid between " + startID + " and " + endID;
		if (!DBSessionUtil.update(sql))
		{
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorDBDeleteTmpPA);
			return;
		}
		output.put("Result", ActionResultInfo.Success);
	}

    public void TagUpdateTemp(JSONObject input,JSONObject output){
        long typeID = input.getLong("TypeID");
        Integer inspectID = input.getInt("InspectID");

        String authTable = IDUtil.getProductAuthTable(typeID);
        String sql = "update " + authTable + " set inspectcode = '0',inspectid=0 where inspectid= " + inspectID;
        if (!DBSessionUtil.update(sql))
        {
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorDBDeleteTmpPA);
            return;
        }
        output.put("Result", ActionResultInfo.Success);
    }
	
	/*
	 * CA防伪标签下载
	 * 1. 确认用户已登录，获取用户ID
	 * 2. 根据TypeID从数据库中获取厂家商品类型相应条目；特别是商品类型的母密钥
	 * 3. 根据TypeID，SessionID创建一个临时表
	 * 4. 如果表不存在，新建临时表，如果表存在则获取最大ProductID
	 * 5. 在临时表中创建每个商品认证信息，并输出到文件
	 * 
	 * @param input
	 *   包含：TypeID, ProductCount
	 * @param output
	 *   输出JSON，包含：result，FailInfo， tagURL
	 */
	public void TagDownload(JSONObject input, JSONObject output) throws IOException
	{
		long productID;
		Producttype vpType = null;
	
		long typeID = input.getLong("TypeID");
		Integer inspectID = input.getInt("InspectID");

		
		// 3. 根据TypeID获取
		String tmpTable = IDUtil.getProductAuthTable(typeID);
		
//		String sql = "select count(*) from " + tmpTable;		
//		String msg = LogUtil.MsgDBSQL;
//		msg += sql;
//		LogUtil.info(LogUtil.LogTypeTag, typeID, this.getClass().getName(), msg);
//
//		BigInteger tmp = (BigInteger)DBSessionUtil.getUniqueResult(sql);
//		long tmpTableSize;
//		if (tmp != null)   tmpTableSize = tmp.longValue();
//		if (tmp == null || tmpTableSize == 0)
//		{
//			output.put("Result", ActionResultInfo.Fail);
//			output.put("FailInfo", ActionResultInfo.ErrorNoAppliedTag);
//			return;
//		}

		// 5. 在临时表中创建每个商品认证信息，并输出到文件
		String strDir = ServletActionContext.getServletContext().getRealPath("/");
		strDir +=  "/" + tmpTable + ".ca";
		FileOutputStream fos = new FileOutputStream(strDir);
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		BufferedWriter bw = new BufferedWriter(osw);
		Session session = DBSessionUtil.getDBSession();	
		try
		{
			String sql;
			String msg;
			
			int recCount = 0;
			Transaction tx = session.beginTransaction();
			String authTable = IDUtil.getProductAuthTable(typeID);
			// 从认证表中查找出范围内的认证项
			sql = "select * from " + tmpTable + 
				  " where InspectID = " + inspectID + "";
			msg = LogUtil.MsgDBSQL;
			msg += sql;
			LogUtil.info(LogUtil.LogTypeTag, typeID, this.getClass().getName(), msg);
			@SuppressWarnings("unchecked")
			List<Productauth> listTempAuth = DBSessionUtil.query(sql, Productauth.class);
			if (listTempAuth == null || listTempAuth.size() == 0)
			{
				output.put("Result", ActionResultInfo.Fail);
				output.put("FailInfo", ActionResultInfo.ErrorNoAppliedTag);
				return;
			}

			msg = "标签下载， ";
			LogUtil.info(LogUtil.LogTypeTag, typeID, this.getClass().getName(), msg);
			
			for(Productauth auth : listTempAuth)
			{
				recCount++;
				
				String strAuthCode = auth.getAuthCode();
				if (strAuthCode.length() > 8)
				{
					strAuthCode = String.valueOf(strAuthCode.toCharArray(), 0, 8);
				}
				bw.write(IDUtil.getProductId(typeID, auth.getProductId()) + strAuthCode);
				bw.newLine();
				if (recCount % 50 == 0)
				{
					session.flush();
					session.clear();
					bw.flush();
				}
			}
			session.flush();
			session.clear();
			tx.commit();
			DBSessionUtil.closeDBSession(session);
			bw.flush();
			bw.close();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			DBSessionUtil.closeDBSession(session);
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorDBTmpPA2PA);
			return;
		}
		output.put("Result", ActionResultInfo.Success);
		output.put("tagURL", tmpTable + ".ca");
		
	}

    /*
	 * CA防伪标签下载
	 * 1. 确认用户已登录，获取用户ID
	 * 2. 根据TypeID从数据库中获取厂家商品类型相应条目；特别是商品类型的母密钥
	 * 3. 根据TypeID，SessionID创建一个临时表
	 * 4. 如果表不存在，新建临时表，如果表存在则获取最大ProductID
	 * 5. 在临时表中创建每个商品认证信息，并输出到文件
	 *
	 * @param input
	 *   包含：TypeID, ProductCount
	 * @param output
	 *   输出JSON，包含：result，FailInfo， tagURL
	 */
    public void TagDownload2(JSONObject input, JSONObject output) throws IOException
    {
        long productID;
        Producttype vpType = null;

        long typeID = input.getLong("TypeID");
        Integer inspectID = input.getInt("InspectID");
        int tagType = input.getInt("TagType");

        // 3. 根据TypeID获取
        String tmpTable = IDUtil.getProductAuthTable(typeID);

        // 5. 在临时表中创建每个商品认证信息，并输出到文件
        String strDir = ServletActionContext.getServletContext().getRealPath("/");
        strDir +=  "/" + tmpTable + ".ca";
        FileOutputStream fos = new FileOutputStream(strDir);
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        BufferedWriter bw = new BufferedWriter(osw);
        Session session = DBSessionUtil.getDBSession();
        try
        {
            String sql;
            String msg;

            int recCount = 0;
            Transaction tx = session.beginTransaction();
            String authTable = IDUtil.getProductAuthTable(typeID);
            // 从认证表中查找出范围内的认证项
            sql = "select * from " + tmpTable +
                    " where InspectID = " + inspectID + "";
            msg = LogUtil.MsgDBSQL;
            msg += sql;
            LogUtil.info(LogUtil.LogTypeTag, typeID, this.getClass().getName(), msg);
            @SuppressWarnings("unchecked")
            List<Productauth> listTempAuth = DBSessionUtil.query(sql, Productauth.class);
            if (listTempAuth == null || listTempAuth.size() == 0)
            {
                output.put("Result", ActionResultInfo.Fail);
                output.put("FailInfo", ActionResultInfo.ErrorNoAppliedTag);
                return;
            }

            msg = "标签下载， ";
            LogUtil.info(LogUtil.LogTypeTag, typeID, this.getClass().getName(), msg);

            for(Productauth auth : listTempAuth)
            {
                recCount++;

                String strAuthCode = auth.getAuthCode();
                if (strAuthCode.length() > 8)
                {
                    strAuthCode = String.valueOf(strAuthCode.toCharArray(), 0, 8);
                }
                switch (tagType){
                    case 1: //C码
                        bw.write(IDUtil.getProductId(typeID, auth.getProductId()) + strAuthCode + "," + String.format("%010d",auth.getProductId()));
                        break;
                    case 2: //涂层标签
                        bw.write(IDUtil.getProductId(typeID, auth.getProductId()) + strAuthCode + "," + String.format("%010d",auth.getProductId()) + "," + auth.getCoatCode().substring(4));
                        break;
                    case 3: //芯片
                        bw.write(IDUtil.getProductId(typeID, auth.getProductId()) + strAuthCode + "," + String.format("%010d",auth.getProductId()));
                        break;
                }

                bw.newLine();
                if (recCount % 50 == 0)
                {
                    session.flush();
                    session.clear();
                    bw.flush();
                }
            }
            session.flush();
            session.clear();
            tx.commit();
            DBSessionUtil.closeDBSession(session);
            bw.flush();
            bw.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            DBSessionUtil.closeDBSession(session);
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorDBTmpPA2PA);
            return;
        }
        output.put("Result", ActionResultInfo.Success);
        output.put("tagURL", tmpTable + ".ca");

    }
}
