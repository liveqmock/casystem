package ca.core.db;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import ca.core.logic.ActionResultInfo;
import ca.core.logic.DBSessionUtil;
import ca.core.logic.DateUtil;
import ca.core.logic.IDUtil;
import ca.core.logic.JsonSQLUtil;
import ca.core.logic.LogUtil;
import org.hibernate.Session;

public class InspectOperator
{
	public static final int InspectStatusPass = 1;
	private static Object lockInspect = new Object();     //生成验证码时用到的锁
	
	public InspectOperator() {}
	
	public void InspectAdd(JSONObject input, JSONObject output)
	{
		Inspect nInspect = new Inspect();
		nInspect.setInspectCode(input.getString("InspectCode"));
		nInspect.setProductName(input.getString("ProductName"));
		nInspect.setTypeId(input.getLong("TypeID"));
		if(input.has("InDistributors"))
			nInspect.setInDistributors(input.getString("InDistributors"));
		else
			nInspect.setInDistributors("");
		if(input.has("InAgentor"))
			nInspect.setInAgentor(input.getString("InAgentor"));
		else
			nInspect.setInAgentor("");
        if(input.has("InspectInCode"))
            nInspect.setInspectInCode(input.getString("InspectInCode"));
        if(input.has("InExportPort"))
            nInspect.setInExportPort(input.getString("InExportPort"));
        if(input.has("TagApplyCount"))
            nInspect.setTagApplyCount(input.getInt("TagApplyCount"));
		nInspect.setCategory(input.getInt("Category"));
		nInspect.setHsCode(input.getString("HSCode"));
		if(input.has("InspectInfo"))
			nInspect.setInspectInfo(input.getString("InspectInfo"));
		else
			nInspect.setInspectInfo("");
		nInspect.setProductCount(input.getString("ProductCount"));
		nInspect.setProductCountUnit(input.getString("ProductCountUnit"));
		nInspect.setProductWeight(input.getString("ProductWeight"));
		nInspect.setProductWeightUnit(input.getString("ProductWeightUnit"));
		nInspect.setProductValue(input.getString("ProductValue"));
		nInspect.setProductValueUnit(input.getString("ProductValueUnit"));
		nInspect.setProductPackage(input.getString("ProductPackage"));
		nInspect.setProductPackageUnit(input.getString("ProductPackageUnit"));
		if(input.has("IntoDate"))
            nInspect.setIntoData(DateUtil.convertString2Date(input.getString("IntoDate"), DateUtil.DEFAULT_DATE_FORMAT));
        else
            nInspect.setIntoData(new Date());
        if(input.has("Importer"))
            nInspect.setImporter(input.getString("Importer"));
        if(input.has("ImportGate"))
            nInspect.setImportGate(input.getString("ImportGate"));
		if(input.has("InspectDate"))
            nInspect.setInspectDate(DateUtil.convertString2Date(input.getString("InspectDate"), DateUtil.DEFAULT_DATE_FORMAT));
		if(input.has("ProductShipDate"))
            nInspect.setProductShipDate(DateUtil.convertString2Date(input.getString("ProductShipDate"), DateUtil.DEFAULT_DATE_FORMAT));
		nInspect.setInspectStatus(input.getInt("InspectStatus"));
		if (input.has("CertificateFile")) {
			nInspect.setCertificateFile(input.getString("CertificateFile"));
		} else {
			nInspect.setCertificateFile("");
		}
		if (input.has("Comment")) {
			nInspect.setComment(input.getString("Comment"));
		}
        if (input.has("Urgent")) {
            nInspect.setUrgent(input.getString("Urgent"));
        }
		if(input.has("InspectRemain"))
            nInspect.setInspectRemain(input.getInt("InspectRemain"));
		nInspect.setVendorID(input.getLong("VendorID"));

		String msg1 = LogUtil.MsgDBInsert;
		msg1 += nInspect.getClass().getName();
		LogUtil.info(LogUtil.LogInspect, LogUtil.InspectNone, this.getClass().getName(), msg1);
		
		//TODO: 		
		if (!DBSessionUtil.save(nInspect))
		{
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorDBNewInspect);
			return;
		}
		
		if(input.getInt("Category") == 2 && input.has("batchIds")) {
			//出口报检后，更新批次信息表中的报检批次号
			VPBatchOperator batchOp = new VPBatchOperator();
			JSONArray batchIdsArr = JSONArray.fromObject(input.getString("batchIds"));
			for(int i=0;i<batchIdsArr.size();i++) {
				JSONObject bInput = new JSONObject();
				JSONObject bOutput = new JSONObject();
				bInput.put("TypeID", nInspect.getTypeId());
				bInput.put("InspectCode", nInspect.getInspectCode());
				bInput.put("InspectID", nInspect.getInspectId());
				bInput.put("BatchID", batchIdsArr.get(i));
				batchOp.VPBatchModify(bInput, bOutput);
			}
			//出口报检后，更新商品认证表中的报检批次号
            TagOperator tagop = new TagOperator();
			for(int i=0;i<batchIdsArr.size();i++) {
				JSONObject bInput = new JSONObject();
				JSONObject bOutput = new JSONObject();
				bInput.put("TypeID", nInspect.getTypeId());
				bInput.put("BatchID", batchIdsArr.get(i));
				batchOp.VPBatchQueryById(bInput, bOutput);
				long startID = bOutput.getLong("StartID");
				long endID = bOutput.getLong("EndID");
				JSONObject bInput1 = new JSONObject();
				JSONObject bOutput1 = new JSONObject();
				bInput1.put("TypeID", nInspect.getTypeId());
				bInput1.put("InspectCode", nInspect.getInspectCode());
				bInput1.put("InspectID", nInspect.getInspectId());
				bInput1.put("StartID", startID);
				bInput1.put("EndID", endID);
				tagop.TagUpdate(bInput1, bOutput1);
			}
		}
		
		//生成输出JSON
		output.put("Result", ActionResultInfo.Success);
        output.put("InspectID",nInspect.getInspectId());
	}

	public void InspectQuery(JSONObject input, JSONObject output)
	{
		String sql;
		
		//根据参数在数据库中查找用户
		if (input.isEmpty())
		{
			sql = "select * from " + "inspect where InspectStatus>=0";
		}
		else
		{			
			//1. 准备查找SQL语句。准备查询
			sql = "select * from " + "inspect" + " where ";
            boolean bFirst = false;
            if(input.has("ProductName")) {
                bFirst = false;
                sql += " productname like '%" + input.getString("ProductName") + "%'";
                input.remove("ProductName");
            } else {
                bFirst = true;
            }
			sql += JsonSQLUtil.JSON2SQLWhere(bFirst, input);
		}
		
		String msg0 = LogUtil.MsgDBSQL;
		msg0 += sql;
		/* FIXME */
		LogUtil.info(LogUtil.LogInspect, LogUtil.InspectNone, this.getClass().getName(), msg0);
		
		@SuppressWarnings("unchecked")
		List<Inspect> mylist = DBSessionUtil.query(sql, Inspect.class);
		if (mylist == null)
		{
			output.put("Result", ActionResultInfo.Fail);
			/* FIXME */
			output.put("FailInfo", ActionResultInfo.ErrorDBQueryInspect);
			return;
		}

		output.put("Result", ActionResultInfo.Success);
		JSONArray ja = new JSONArray();
		for(Inspect tInspect : mylist)
		{
			JSONObject jo = new JSONObject();
			jo.put("InspectID", tInspect.getInspectId());
			jo.put("InspectCode", tInspect.getInspectCode());
			jo.put("ProductName", tInspect.getProductName());
			jo.put("TypeID", tInspect.getTypeId());
			jo.put("Category", tInspect.getCategory());
			jo.put("HSCode", tInspect.getHsCode());
			jo.put("InspectInfo", tInspect.getInspectInfo() == null ? "" : tInspect.getInspectInfo());
			jo.put("InDistributors", tInspect.getInDistributors() == null ? "" : tInspect.getInDistributors());
			jo.put("InAgentor", tInspect.getInAgentor() == null ? "" : tInspect.getInAgentor());
			jo.put("ProductCount", tInspect.getProductCount() == null ? "0" : tInspect.getProductCount());
			jo.put("ProductCountUnit", tInspect.getProductCountUnit());
			jo.put("ProductWeight", tInspect.getProductWeight());
			jo.put("ProductWeightUnit", tInspect.getProductWeightUnit());
			jo.put("ProductValue", tInspect.getProductValue());
			jo.put("ProductValueUnit", tInspect.getProductValueUnit());
			jo.put("ProductPackage", tInspect.getProductPackage());
			jo.put("ProductPackageUnit", tInspect.getProductPackageUnit());
			jo.put("IntoDate", DateUtil.convertDate2String(tInspect.getIntoData(), DateUtil.DATE_FORMAT));
			jo.put("InspectDate", DateUtil.convertDate2String(tInspect.getInspectDate(), DateUtil.DATE_FORMAT));
			jo.put("ProductShipDate", DateUtil.convertDate2String(tInspect.getProductShipDate(), DateUtil.DATE_FORMAT));
			jo.put("InspectStatus", tInspect.getInspectStatus());
			jo.put("InspectRemain", tInspect.getInspectRemain() == null ? 0 : tInspect.getInspectRemain());
			jo.put("CertificateFile", tInspect.getCertificateFile() == null ? "" : tInspect.getCertificateFile());
			jo.put("Comment", tInspect.getComment() == null ? "" : tInspect.getComment());
			jo.put("Urgent", tInspect.getUrgent() == null ? "" : tInspect.getUrgent());
			jo.put("InExportPort", tInspect.getInExportPort() == null ? "" : tInspect.getInExportPort());
			jo.put("InspectInCode", tInspect.getInspectInCode() == null ? "" : tInspect.getInspectInCode());
			jo.put("Importer", tInspect.getImporter() == null ? "" : tInspect.getImporter());
			jo.put("ImportGate", tInspect.getImportGate() == null ? "" : tInspect.getImportGate());
			jo.put("VendorID", tInspect.getVendorID());
			jo.put("TagApplyCount", tInspect.getTagApplyCount() == null ? 0 : tInspect.getTagApplyCount());
			ja.add(jo);
		}
		output.put("Inspects", ja);
	}	

	public void InspectModify(JSONObject input, JSONObject output) throws IOException
	{
        int InspectID = input.getInt("InspectID");
		int InspectStatus = 0;
		int userID = input.getInt("UserID");
		input.remove("UserID");
		if (input.has("InspectStatus"))
		{
			InspectStatus = input.getInt("InspectStatus");
		}
		input.remove("InspectID");
        String batchIds = null;
        if(input.has("batchIds")) {
            batchIds = input.getString("batchIds");
            input.remove("batchIds");
        }
        String sql = "select * from " + "inspect" + " where InspectId = " + InspectID;
        @SuppressWarnings("unchecked")
        List<Inspect> mylist = DBSessionUtil.query(sql, Inspect.class);
        if (mylist == null)
        {
            output.put("Result", ActionResultInfo.Fail);

            output.put("FailInfo", ActionResultInfo.ErrorDBQueryVPType);
            return;
        }

        Inspect tmpInspect = (Inspect)mylist.get(0);
		/* FIXME Optimize */
		synchronized(lockInspect)
		{
			if (InspectStatus == InspectStatusPass)
			{
				JSONObject jInspect = new JSONObject();
				/* 出口商品 若已审核过，则不再重新生成防伪码 */
				if (tmpInspect.getInspectStatus() != InspectStatusPass && tmpInspect.getCategory() == 1)
				{
					jInspect.put("TypeID", tmpInspect.getTypeId());
					jInspect.put("ProductCount", tmpInspect.getProductCount());
					jInspect.put("TagApplyCount", tmpInspect.getTagApplyCount());
					jInspect.put("InspectCode", tmpInspect.getInspectCode());
					jInspect.put("InspectID", tmpInspect.getInspectId());
					jInspect.put("VendorID", tmpInspect.getVendorID());
					jInspect.put("UserID", userID);
					
					TagOperator to = new TagOperator();
					to.TagGenerate(jInspect, output);
					
					/* output fail info */
					if (output.getInt("Result") == ActionResultInfo.Fail)
					{
						return;
					}
				}
			}
	
			sql = "update " + "inspect" + " set " + JsonSQLUtil.JSON2SQLSet(input);
			sql += " where InspectID=" + InspectID;
			
			String msg = LogUtil.MsgDBSQL;
			msg += sql;
			LogUtil.info(LogUtil.LogInspect, LogUtil.TypeIDNone, this.getClass().getName(), msg);
			if (!DBSessionUtil.update(sql))
			{
				output.put("Result", ActionResultInfo.Fail);
				output.put("FailInfo", ActionResultInfo.ErrorDBModifyInspect);
				return;
			}
			
			//更新认证码的报检批号
			if (InspectStatus >= TagAuthorize.TagOK || InspectStatus == TagAuthorize.TagInProof)
			{
				String authTable = IDUtil.getProductAuthTable(tmpInspect.getTypeId());
				
				sql = "update " + authTable + " set Status = " + InspectStatus;
				sql += " where InspectID =" + tmpInspect.getInspectId();
				if (!DBSessionUtil.update(sql))
				{
					output.put("Result", ActionResultInfo.Fail);
					output.put("FailInfo", ActionResultInfo.ErrorModifyAuth);
					return;
				}
				
				String batchTable = IDUtil.getBatchTable(tmpInspect.getTypeId());
				
				sql = "update " + batchTable + " set batchstatus = " + InspectStatus + " where inspectid = " +  tmpInspect.getInspectId();
				if (!DBSessionUtil.update(sql))
				{
					output.put("Result", ActionResultInfo.Fail);
					output.put("FailInfo", ActionResultInfo.ErrorModifyAuth);
					return;
				}
			}
		}

        if(tmpInspect.getCategory() == 2 && batchIds != null) {
            //出口报检后，更新批次信息表中的报检批次号
            VPBatchOperator batchOp = new VPBatchOperator();
            JSONObject tInput = new JSONObject();
            JSONObject tOutput = new JSONObject();
            tInput.put("TypeID",tmpInspect.getTypeId());
            tInput.put("InspectID",tmpInspect.getInspectId());
            batchOp.modifyBatchForTemp(tInput,tOutput);
            JSONArray batchIdsArr = JSONArray.fromObject(batchIds);
            for(int i=0;i<batchIdsArr.size();i++) {
                JSONObject bInput = new JSONObject();
                JSONObject bOutput = new JSONObject();
                bInput.put("TypeID", tmpInspect.getTypeId());
                bInput.put("InspectCode", tmpInspect.getInspectCode());
                bInput.put("InspectID", tmpInspect.getInspectId());
                bInput.put("BatchID", batchIdsArr.get(i));
                batchOp.VPBatchModify(bInput, bOutput);
            }
            //出口报检后，更新商品认证表中的报检批次号
            TagOperator tagop = new TagOperator();
            JSONObject tInput_ = new JSONObject();
            JSONObject tOutput_ = new JSONObject();
            tInput_.put("TypeID",tmpInspect.getTypeId());
            tInput_.put("InspectID",tmpInspect.getInspectId());
            tagop.TagUpdateTemp(tInput_,tOutput_);
            for(int i=0;i<batchIdsArr.size();i++) {
                JSONObject bInput = new JSONObject();
                JSONObject bOutput = new JSONObject();
                bInput.put("TypeID", tmpInspect.getTypeId());
                bInput.put("BatchID", batchIdsArr.get(i));
                batchOp.VPBatchQueryById(bInput, bOutput);
                long startID = bOutput.getLong("StartID");
                long endID = bOutput.getLong("EndID");
                JSONObject bInput1 = new JSONObject();
                JSONObject bOutput1 = new JSONObject();
                bInput1.put("TypeID", tmpInspect.getTypeId());
                bInput1.put("InspectCode", tmpInspect.getInspectCode());
                bInput1.put("InspectID", tmpInspect.getInspectId());
                bInput1.put("StartID", startID);
                bInput1.put("EndID", endID);
                tagop.TagUpdate(bInput1, bOutput1);
            }
        }

		output.put("Result", ActionResultInfo.Success);
	}

    public void InspectQueryOut(JSONObject input, JSONObject output) {
        //To change body of created methods use File | Settings | File Templates.
        String sql=" ";
       if(1==input.getInt("flag")){
         sql = "select * from inspect where InspectStatus in(1,3) and InspectRemain>=1";
        if(input.has("Category")) {
            sql=sql+" and Category="+input.getInt("Category");
        }
        if(input.has("VendorID")) {
            sql=sql+" and VendorID="+input.getLong("VendorID");
        }
        if(input.has("InspectCode")) {
            sql=sql+" and InspectCode='"+input.getString("InspectCode")+"'";
        }
      }else{
           sql = "select * from inspect where 1=1 ";
           if(input.has("Category")) {
               sql=sql+" and Category="+input.getInt("Category");
           }
           if(input.has("VendorID")) {
               sql=sql+" and VendorID="+input.getLong("VendorID");
           }
           if(input.has("InspectCode")) {
               sql=sql+" and InspectCode='"+input.getString("InspectCode")+"'";
           }
        }
        String msg0 = LogUtil.MsgDBSQL;
        msg0 += sql;
        /* FIXME */
        LogUtil.info(LogUtil.LogInspect, LogUtil.InspectNone, this.getClass().getName(), msg0);

        @SuppressWarnings("unchecked")
        List<Inspect> mylist = DBSessionUtil.query(sql, Inspect.class);
        if (mylist == null)
        {
            output.put("Result", ActionResultInfo.Fail);
            /* FIXME */
            output.put("FailInfo", ActionResultInfo.ErrorDBQueryInspect);
            return;
        }

        output.put("Result", ActionResultInfo.Success);
        JSONArray ja = new JSONArray();
        for(Inspect tInspect : mylist)
        {
            JSONObject jo = new JSONObject();
            jo.put("InspectID", tInspect.getInspectId());
            jo.put("InspectCode", tInspect.getInspectCode());
            jo.put("ProductName", tInspect.getProductName());
            jo.put("TypeID", tInspect.getTypeId());
            jo.put("Category", tInspect.getCategory());
            jo.put("HSCode", tInspect.getHsCode());
            jo.put("InspectInfo", tInspect.getInspectInfo() == null ? "" : tInspect.getInspectInfo());
            jo.put("InDistributors", tInspect.getInDistributors() == null ? "" : tInspect.getInDistributors());
            jo.put("InAgentor", tInspect.getInAgentor() == null ? "" : tInspect.getInAgentor());
            jo.put("ProductCount", tInspect.getProductCount());
            jo.put("ProductCountUnit", tInspect.getProductCountUnit());
            jo.put("ProductWeight", tInspect.getProductWeight());
            jo.put("ProductWeightUnit", tInspect.getProductWeightUnit());
            jo.put("ProductValue", tInspect.getProductValue());
            jo.put("ProductValueUnit", tInspect.getProductValueUnit());
            jo.put("ProductPackage", tInspect.getProductPackage());
            jo.put("ProductPackageUnit", tInspect.getProductPackageUnit());
            jo.put("IntoDate", DateUtil.convertDate2String(tInspect.getIntoData(), DateUtil.DATE_FORMAT));
            jo.put("InspectDate", DateUtil.convertDate2String(tInspect.getInspectDate(), DateUtil.DATE_FORMAT));
            jo.put("ProductShipDate", DateUtil.convertDate2String(tInspect.getProductShipDate(), DateUtil.DATE_FORMAT));
            jo.put("InspectStatus", tInspect.getInspectStatus());
            jo.put("InspectRemain", tInspect.getInspectRemain());
            jo.put("CertificateFile", tInspect.getCertificateFile() == null ? "" : tInspect.getCertificateFile());
            jo.put("Comment", tInspect.getComment() == null ? "" : tInspect.getComment());
            jo.put("Urgent", tInspect.getUrgent() == null ? "" : tInspect.getUrgent());
            jo.put("VendorID", tInspect.getVendorID());
            jo.put("InExportPort", tInspect.getInExportPort() == null ? "" : tInspect.getInExportPort());
            jo.put("InspectInCode", tInspect.getInspectInCode() == null ? "" : tInspect.getInspectInCode());
            jo.put("Importer", tInspect.getImporter() == null ? "" : tInspect.getImporter());
            jo.put("ImportGate", tInspect.getImportGate() == null ? "" : tInspect.getImportGate());
            jo.put("TagApplyCount", tInspect.getTagApplyCount() == null ? 0 : tInspect.getTagApplyCount());
            ja.add(jo);
        }
        output.put("Inspects", ja);
    }

    public void InspectQueryOutnew(JSONObject input, JSONObject output) {
        //To change body of created methods use File | Settings | File Templates.
        String sql ="";
        if(input.has("TypeID")){
        sql = "SELECT InspectCode,TypeID,SUM(ProductCount) AS ProductCount,SUM(InspectRemain) AS InspectRemain " +
                " FROM inspect WHERE InspectStatus IN(1,3) AND InspectRemain>=1 AND Category=1 " ;
        sql=sql+" and TypeID="+input.getLong("TypeID");
        if(input.has("Category")) {
            sql=sql+" and Category="+input.getInt("Category");
        }
        if(input.has("VendorID")) {
            sql=sql+" and VendorID="+input.getLong("VendorID");
        }
        sql +=  " GROUP BY InspectCode,TypeID ";
        }else {
            sql = "SELECT InspectCode,SUM(ProductCount) AS ProductCount,SUM(InspectRemain) AS InspectRemain " +
                    " FROM inspect WHERE InspectStatus IN(1,3)  AND Category=1 " ;
            if(input.has("Category")) {
                sql=sql+" and Category="+input.getInt("Category");
            }
            if(input.has("VendorID")) {
                sql=sql+" and VendorID="+input.getLong("VendorID");
            }
            sql +=  " GROUP BY InspectCode ";
        }
        String msg0 = LogUtil.MsgDBSQL;
        msg0 += sql;
        /* FIXME */
        LogUtil.info(LogUtil.LogInspect, LogUtil.InspectNone, this.getClass().getName(), msg0);

        JSONArray ja = new JSONArray();
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        Session session=null;
        Long fun=null;
        try {
            session = DBSessionUtil.getDBSession();
            pStmt=session.connection().prepareStatement(sql);
            rs = pStmt.executeQuery();
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount();
            while(rs.next()){
                JSONObject jo = new JSONObject();
                jo.put("InspectCode", rs.getString("InspectCode"));
                jo.put("ProductCount", rs.getLong("ProductCount"));
                jo.put("InspectRemain",rs.getLong("InspectRemain"));
                ja.add(jo);
            }
            output.put("Result", ActionResultInfo.Success);
            DBSessionUtil.closeDBSession(session);
            rs.close();
            pStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            output.put("Result", ActionResultInfo.Fail);
            /* FIXME */
            output.put("FailInfo", ActionResultInfo.ErrorDBQueryInspect);
            return;
        } finally {
            output.put("Inspects", ja);
        }
    }

    public void InspectDelete(JSONObject input, JSONObject output) {
        Integer inspectID = input.getInt("InspectID");
        long typeID = input.getLong("TypeID");
        String authTable = IDUtil.getProductAuthTable(typeID);
        String batchTable = IDUtil.getBatchTable(typeID);
        String sql1 = "update " + batchTable + " set inspectid=0,inspectcode='0' where inspectid=" + inspectID;
        String sql2 = "update " + authTable + " set inspectid=0,inspectcode='0' where inspectid=" + inspectID;
        String sql3 = "delete from inspect where inspectid=" + inspectID;
        if (!DBSessionUtil.update(sql1))
        {
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorDBDeleteTmpPA);
            return;
        }
        if (!DBSessionUtil.update(sql2))
        {
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorDBDeleteTmpPA);
            return;
        }
        if (!DBSessionUtil.update(sql3))
        {
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorDBDeleteTmpPA);
            return;
        }
        output.put("Result", ActionResultInfo.Success);
    }
}
