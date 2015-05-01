package ca.core.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.List;
import java.util.ArrayList;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import ca.core.logic.ActionResultInfo;
import ca.core.logic.DBSessionUtil;
import ca.core.logic.DateUtil;
import ca.core.logic.HexString;
import ca.core.logic.JsonSQLUtil;
import ca.core.logic.LogUtil;
import org.hibernate.Session;

public class LogisticsOperator {

	public LogisticsOperator() {
	}

	@SuppressWarnings("unchecked")
	public void add(JSONObject input, JSONObject output) {

		Logistics logistics = new Logistics();
		//logistics.setLogisticsID(HexString.getUuid());
		logistics.setTypeID(input.getLong("TypeID"));
		logistics.setInspectID(input.getLong("InspectID"));
		logistics.setInspectCode(input.getString("InspectCode"));
		logistics.setProductCountUnit(input.getString("ProductCountUnit"));
		logistics.setVendorID(input.getLong("VendorID"));
		logistics.setCategory(input.getInt("Category"));
		logistics.setPassGateID(input.getString("PassGateID"));
        logistics.setPassInspectID(input.getString("PassInspectID"));
		logistics.setOutDate(DateUtil.formatStringDate(
				input.getString("OutDate"), DateUtil.DEFAULT_DATE_FORMAT,
				DateUtil.DATE_YEAR_MONTH_DATE));
		
		String msg1 = LogUtil.MsgDBInsert;
		msg1 += logistics.getClass().getName();
		LogUtil.info(LogUtil.LogLogistics, LogUtil.LogLogistics, this
				.getClass().getName(), msg1);
		int cnt_i=input.getInt("cnt_i");
		@SuppressWarnings("rawtypes")
		List errflag =new ArrayList();		
		for(int i=0;i<=cnt_i;i++){
			logistics.setReceiveAddress("");
			logistics.setReceiveCompany("");
			logistics.setReceivePhone("");
			if(input.has("ReceiveAddress"+i)||input.has("ReceiveCompany"+i)|| input.has("ReceivePhone"+i)){
				logistics.setOutCount(input.getLong("OutCount"+i));
				logistics.setProductStartID(input.getLong("ProductStartID"+i));
				logistics.setProductEndID(input.getLong("ProductEndID"+i));
				logistics.setReceiveAddress(input.getString("ReceiveAddress"+i));
				logistics.setReceiveCompany(input.getString("ReceiveCompany"+i));
				logistics.setReceivePhone(input.getString("ReceivePhone"+i));
				if (!DBSessionUtil.save(logistics)) {
					errflag.add(false);
				}
				//更新报建表，商品备案信息表，认证表
				// update inspect set InspectRemain=InspectRemain-1 where VendorID= and TypeID= and InspectID= and Category=;
				// update producttype set Remain=Remain-1 where VendorID= and TypeID=  and Category=;
				// update pa set LogisticsID where InspectID= and ProductID>=1 and ProductID<=2;//获取不了物流编号
				String sql;
				sql="update inspect set InspectRemain=InspectRemain-"+logistics.getOutCount()+" where VendorID="+logistics.getVendorID()+" and TypeID="+logistics.getTypeID()+" and InspectID="+logistics.getInspectID()+" and Category="+logistics.getCategory();
				System.out.println("LogisticsOperator==sql"+sql);
				if (!DBSessionUtil.update(sql)) {
					errflag.add(false);
				}
				sql="update producttype set Remain=Remain-"+logistics.getOutCount()+" where VendorID="+logistics.getVendorID()+" and TypeID="+logistics.getTypeID()+"  and Category="+logistics.getCategory();
				System.out.println("LogisticsOperator==sql"+sql);
				if (!DBSessionUtil.update(sql)) {
					errflag.add(false);
				}
				if(logistics.getProductStartID()>=0 && logistics.getProductEndID()>=1){
				String patypeid="pa"+Long.toHexString(logistics.getTypeID());
				sql="update "+patypeid+" set LogisticsID="+logistics.getLogisticsID()+" where  InspectID="+logistics.getInspectID()+" and ProductID>="+logistics.getProductStartID() +" and ProductID<="+logistics.getProductEndID();
				System.out.println("LogisticsOperator==sql"+sql);
				if (!DBSessionUtil.update(sql)) {
					errflag.add(false);
				}
			   }
			}
		}
		
		// TODO:
		if (!errflag.isEmpty()) {
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorLogisticsAdd);
			return;
		}
		output.put("Result", ActionResultInfo.Success);
	}

    public void addSingle(JSONObject input, JSONObject output) {

        Logistics logistics = new Logistics();
        //logistics.setLogisticsID(HexString.getUuid());
        logistics.setTypeID(input.getLong("TypeID"));
        logistics.setInspectID(input.getLong("InspectID"));
        logistics.setInspectCode(input.getString("InspectCode"));
        logistics.setProductCountUnit(input.getString("ProductCountUnit"));
        logistics.setVendorID(input.getLong("VendorID"));
        logistics.setCategory(input.getInt("Category"));
        logistics.setPassGateID(input.getString("PassGateID"));
        if(input.has("PassInspectID")){
        logistics.setPassInspectID(input.getString("PassInspectID"));
        }
        logistics.setOutDate(DateUtil.formatStringDate(
                input.getString("OutDate"), DateUtil.DEFAULT_DATE_FORMAT,
                DateUtil.DATE_YEAR_MONTH_DATE));
        List errflag =new ArrayList();
        logistics.setOutCount(input.getLong("OutCount"));
        if(input.has("ProductStartID")){
        logistics.setProductStartID(input.getLong("ProductStartID"));
        }
        if(input.has("ProductEndID")){
        logistics.setProductEndID(input.getLong("ProductEndID"));
        }

        logistics.setReceiveAddress(input.getString("ReceiveAddress"));
        logistics.setReceiveCompany(input.getString("ReceiveCompany"));
        logistics.setReceivePhone(input.getString("ReceivePhone"));

        String msg1 = LogUtil.MsgDBInsert;
        msg1 += logistics.getClass().getName();
        LogUtil.info(LogUtil.LogLogistics, LogUtil.LogLogistics, this
                .getClass().getName(), msg1);

        if (!DBSessionUtil.save(logistics)) {
            errflag.add(false);
        }
        //更新报建表，商品备案信息表，认证表
        // update inspect set InspectRemain=InspectRemain-1 where VendorID= and TypeID= and InspectID= and Category=;
        // update producttype set Remain=Remain-1 where VendorID= and TypeID=  and Category=;
        // update pa set LogisticsID where InspectID= and ProductID>=1 and ProductID<=2;//获取不了物流编号
        String sql;
        sql="update inspect set InspectRemain=InspectRemain-"+logistics.getOutCount()+" where VendorID="+logistics.getVendorID()+" and TypeID="+logistics.getTypeID()+" and InspectID="+logistics.getInspectID()+" and Category="+logistics.getCategory();
        System.out.println("LogisticsOperator==sql="+sql);
        if (!DBSessionUtil.update(sql)) {
            errflag.add(false);
        }
        /*
        String inspectsql=" SELECT InspectID,InspectRemain FROM inspect WHERE InspectRemain>=1 AND InspectCode='"+logistics.getInspectCode()+"' AND VendorID="+logistics.getVendorID()+" AND TypeID="+logistics.getTypeID();
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        Session session=null;
        try {
            session = DBSessionUtil.getDBSession();
            pStmt=session.connection().prepareStatement(inspectsql);
            rs = pStmt.executeQuery();
            ResultSetMetaData md = rs.getMetaData();
            Long outcnt=logistics.getOutCount();
            Long remain=0l;
            boolean  flag=rs.next();
            while (flag){
                remain=rs.getLong("InspectRemain");
                if(remain<outcnt && outcnt>0)  {
                //按照InspectCode循环报检单
                sql="update inspect set InspectRemain=InspectRemain-"+logistics.getOutCount()+" where VendorID="+logistics.getVendorID()+" and TypeID="+logistics.getTypeID()+" and InspectID="+rs.getLong("InspectID")+" and Category="+logistics.getCategory();
                System.out.println("LogisticsOperator==sql="+sql);
                   if (!DBSessionUtil.update(sql)) {
                       errflag.add(false);
                    }
                }
                outcnt-= remain;
                flag=rs.next();
                if(outcnt<=0){
                    flag=false;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            errflag.add(false);
        } finally {
            try{
                 rs.close();
                 pStmt.close();
                DBSessionUtil.closeDBSession(session);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
         */

        sql="update producttype set Remain=Remain-"+logistics.getOutCount()+" where VendorID="+logistics.getVendorID()+" and TypeID="+logistics.getTypeID()+"  and Category="+logistics.getCategory();
        System.out.println("LogisticsOperator==sql"+sql);
        if (!DBSessionUtil.update(sql)) {
            errflag.add(false);
        }
        if(logistics.getProductStartID()>=0 && logistics.getProductEndID()>=1){
            String patypeid="pa"+Long.toHexString(logistics.getTypeID());
            sql="update "+patypeid+" set LogisticsID="+logistics.getLogisticsID()+" where  InspectID="+logistics.getInspectID()+" and ProductID>="+logistics.getProductStartID() +" and ProductID<="+logistics.getProductEndID();
            System.out.println("LogisticsOperator==sql"+sql);
            if(DBSessionUtil.isExistTable(patypeid)){
              if (!DBSessionUtil.update(sql)) {
                  errflag.add(false);
              }
            }
        }
        // TODO:
        if (!errflag.isEmpty()) {
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorLogisticsAdd);
            return;
        }
        output.put("Result", ActionResultInfo.Success);
    }

    public void query(JSONObject input, JSONObject output) {
        String sql="select * from logistics where 1=1 ";
        if (input.isEmpty()) {
            output.put("Result", ActionResultInfo.Fail);
            /* FIXME */
            output.put("FailInfo", ActionResultInfo.ErrorDBQueryLogistics);
            return;
        } else {

            if(input.containsKey("Category")){
                int Category=input.getInt("Category") ;
                sql +=  " and Category = " + Category ;
            }
            if(input.containsKey("VendorID")){
                long VendorID=input.getLong("VendorID") ;
                sql +=  " and VendorID = " + VendorID ;
            }
            if(input.containsKey("PassGateID")){
                String PassGateID=input.getString("PassGateID") ;
                sql +=  " and PassGateID = '" + PassGateID +"'";
            }
            if(input.containsKey("TypeID")){
                long TypeID=input.getLong("TypeID") ;
                sql +=  " and TypeID = " + TypeID;
            }
            if(input.containsKey("InspectCode")){
                String InspectCode=input.getString("InspectCode") ;
                sql +=  " and InspectCode = '" + InspectCode+"'";
            }
            if(input.containsKey("InspectID")){
                long InspectID=input.getLong("InspectID") ;
                sql +=  " and InspectID = " + InspectID;
            }
        }

        String msg0 = LogUtil.MsgDBSQL;
        msg0 += sql;
        /* FIXME */
        LogUtil.info(LogUtil.LogLogistics, LogUtil.LogLogistics, this
                .getClass().getName(), msg0);

        @SuppressWarnings("unchecked")
        List<Logistics> mylist = DBSessionUtil.query(sql, Logistics.class);
        if (mylist == null) {
            output.put("Result", ActionResultInfo.Fail);
            /* FIXME */
            output.put("FailInfo", ActionResultInfo.ErrorDBQueryLogistics);
            return;
        }

        output.put("Result", ActionResultInfo.Success);
        JSONArray ja = new JSONArray();
        for (Logistics logistics : mylist) {
            JSONObject jo = new JSONObject();
            jo.put("LogisticsID", logistics.getLogisticsID());
            jo.put("TypeID", logistics.getTypeID());
            jo.put("InspectID", logistics.getInspectID());
            jo.put("InspectCode", logistics.getInspectCode());
            jo.put("Category", logistics.getCategory());
            jo.put("VendorID", logistics.getVendorID());
            jo.put("TypeID", logistics.getTypeID());
            jo.put("OutDate",
                    DateUtil.formatStringDate(logistics.getOutDate(),
                            DateUtil.DATE_YEAR_MONTH_DATE,
                            DateUtil.DEFAULT_DATE_FORMAT));
            jo.put("OutCount", logistics.getOutCount());
            jo.put("ProductCountUnit", logistics.getProductCountUnit());
            jo.put("ProductStartID", logistics.getProductStartID());
            jo.put("ProductEndID", logistics.getProductEndID());
            jo.put("PassGateID", logistics.getPassGateID());
            jo.put("PassInspectID", logistics.getPassInspectID());
            jo.put("ReceiveCompany", logistics.getReceiveCompany());
            jo.put("ReceiveAddress", logistics.getReceiveAddress());
            jo.put("ReceivePhone", logistics.getReceivePhone());
            ja.add(jo);
            System.out.println("queryBycategoryVendorID===jo=="+jo);
        }

        output.put("logisticses", ja);

	}

	@SuppressWarnings("unchecked")
	public void modify(JSONObject input, JSONObject output) {
  		@SuppressWarnings("rawtypes")
		List errflag =new ArrayList();
		int cnt_i=input.getInt("cnt_i");
		for(int i=0;i<=cnt_i;i++){
			String sql = "update " + "logistics " + " set ";
			long logisticsID = input.getLong("LogisticsID"+i);
			if(input.has("ReceiveAddress"+i)||input.has("ReceiveCompany"+i)|| input.has("ReceivePhone"+i)){
				if(input.has("ReceiveAddress"+i)){
					sql=sql+" ReceiveAddress='"+input.getString("ReceiveAddress"+i)+"',";
				}				
				if(input.has("ReceiveCompany"+i)){
					sql=sql+" ReceiveCompany='"+input.getString("ReceiveCompany"+i)+"',";
				}
				if(input.has("ReceivePhone"+i)){
					sql=sql+" ReceivePhone='"+input.getString("ReceivePhone"+i)+"'";
				}
				
				sql += " where LogisticsID=" + logisticsID;
				String msg = LogUtil.MsgDBSQL;
				msg += sql;
				LogUtil.info(LogUtil.LogLogistics, LogUtil.LogLogistics, this.getClass()
						.getName(), msg);
				System.out.println("modify==sql="+sql);
				if (!DBSessionUtil.update(sql)) {
					errflag.add(false);
				}
			}
		}
	
		if (!errflag.isEmpty()) {
			output.put("Result", ActionResultInfo.Fail);
			output.put("FailInfo", ActionResultInfo.ErrorDBModifyLogistics);
			return;
		}

		output.put("Result", ActionResultInfo.Success);

	}

    @SuppressWarnings("unchecked")
    public void modifySingle(JSONObject input, JSONObject output) {
        if(input.has("LogisticsID")){
            String sql = "update " + "logistics " + " set ";
            long logisticsID = input.getLong("LogisticsID");

            if(input.has("OutDate")){
                sql=sql+" OutDate='"+input.getString("OutDate")+"',";
            }
             /*
            if(input.has("OutCount")){
                sql=sql+" OutCount="+input.getLong("OutCount");
            }      */
            if(input.has("ProductStartID")){
                sql=sql+" ProductStartID="+input.getLong("ProductStartID")+",";
            }
            if(input.has("ProductEndID")){
                sql=sql+" ProductEndID="+input.getLong("ProductEndID")+",";
            }
            if(input.has("PassInspectID")){
                sql=sql+" PassInspectID='"+input.getString("PassInspectID")+"',";
            }
            if(input.has("PassGateID")){
                sql=sql+" PassGateID='"+input.getString("PassGateID")+"',";
            }

            if(input.has("ReceiveAddress")){
                sql=sql+" ReceiveAddress='"+input.getString("ReceiveAddress")+"',";
            }
            if(input.has("ReceiveCompany")){
                sql=sql+" ReceiveCompany='"+input.getString("ReceiveCompany")+"',";
            }
            if(input.has("ReceivePhone")){
                sql=sql+" ReceivePhone='"+input.getString("ReceivePhone")+"'";
            }
                sql += " where LogisticsID=" + logisticsID;
                String msg = LogUtil.MsgDBSQL;
                msg += sql;
                LogUtil.info(LogUtil.LogLogistics, LogUtil.LogLogistics, this.getClass()
                        .getName(), msg);

            //出区核销修改接口：所有字段均可修改，但是只是修改出区核销表，并未更新认证表和生产批次表。（所以出区数量，不允许修改）。
                if (!DBSessionUtil.update(sql)) {
                    output.put("Result", ActionResultInfo.Fail);
                    output.put("FailInfo", ActionResultInfo.ErrorDBModifyLogistics);
                    return;
                }
            output.put("Result", ActionResultInfo.Success);
        } else {
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorDBModifyLogisticsNoID);
            return;
        }

    }

    public void querybyinspect(JSONObject input, JSONObject output) {
        String sql2=" SELECT a.*,IFNULL(b.lgcnt,0) lgcnt,IFNULL(b.ountcnt,0) ountcnt " +
                " FROM inspect a LEFT JOIN (SELECT InspectID,InspectCode, VendorID,TypeID,Category,COUNT(LogisticsID) lgcnt,SUM(OutCount) ountcnt FROM logistics WHERE Category = 1 ";
        String sql= " GROUP BY InspectID,InspectCode,VendorID,TypeID,Category) b ON(a.InspectCode = b.InspectCode  AND a.VendorID = b.VendorID AND a.TypeID = b.TypeID AND a.InspectID=b.InspectID) where a.Category = 1  AND a.InspectStatus IN (1,3) ";
      /*
        String sql2="SELECT a.*,b.lgcnt,b.ountcnt " +
                " FROM inspect a,(SELECT InspectCode,VendorID,TypeID,Category,COUNT(LogisticsID) lgcnt,SUM(OutCount) ountcnt FROM logistics WHERE Category=1 " ;
        String sql=" GROUP BY  InspectCode,VendorID,TypeID,Category)b " +
                " WHERE a.InspectCode=b.InspectCode AND a.VendorID=b.VendorID AND a.TypeID=b.TypeID ";
                */
//        if (input.isEmpty()) {
//            output.put("Result", ActionResultInfo.Fail);
//            output.put("FailInfo",ActionResultInfo.ErrorDBNoParameter);
//            return;
//        } else {
            if(input.containsKey("InspectCode")){
                String InspectCode=input.getString("InspectCode") ;
                sql +=  " and a.InspectCode ='" + InspectCode+"' " ;
                sql2 +=  " and InspectCode ='" + InspectCode+"' " ;
            }
            if(input.containsKey("Category")){
                int Category=input.getInt("Category") ;
                sql +=  " and a.Category = " + Category ;
                sql2 +=  " and Category = " + Category ;
            }
            if(input.containsKey("VendorID")){
                long VendorID=input.getLong("VendorID") ;
                sql +=  " and a.VendorID = " + VendorID ;
                sql2 +=  " and VendorID = " + VendorID ;
            }
            if(input.containsKey("PassGateID")){
                String PassGateID=input.getString("PassGateID") ;
                sql2 +=  " and PassGateID = '" + PassGateID +"'";
            }
            if(input.containsKey("TypeID")){
                long TypeID=input.getLong("TypeID") ;
                sql +=  " and a.TypeID = " + TypeID;
                sql2 +=  " and TypeID = " + TypeID;
            }
       // }
        String msg0 = LogUtil.MsgDBSQL;
        msg0 += (sql2+sql +" order by a.InspectCode");
        /* FIXME */
        LogUtil.info(LogUtil.LogLogistics, LogUtil.LogLogistics, this
                .getClass().getName(), msg0);

        JSONArray ja = new JSONArray();
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        Session session=null;
        try {
            session = DBSessionUtil.getDBSession();
            pStmt=session.connection().prepareStatement(sql2+sql);
            rs = pStmt.executeQuery();
            ResultSetMetaData md = rs.getMetaData();
            while (rs.next()) {
                JSONObject jo = new JSONObject();
                jo.put("InspectID",rs.getLong("InspectID"));
                jo.put("InspectCode",rs.getString("InspectCode"));
                jo.put("ProductName",rs.getString("ProductName"));
                jo.put("TypeID",rs.getLong("TypeID"));
                jo.put("InspectDate",rs.getString("InspectDate"));
                jo.put("Category",rs.getInt("Category"));
                jo.put("HsCode",rs.getString("HsCode"));
                jo.put("ProductCount",rs.getLong("ProductCount"));
                jo.put("ProductCountUnit",rs.getString("ProductCountUnit"));
                jo.put("InspectRemain",rs.getLong("InspectRemain"));
                jo.put("VendorID",rs.getLong("VendorID"));
                jo.put("PassGateID"," ");
                jo.put("lgcnt",rs.getLong("lgcnt"));
                jo.put("ountcnt",rs.getLong("ountcnt"));

                ja.add(jo);
            }

        } catch (Exception e) {
            e.printStackTrace();
            output.put("Result", ActionResultInfo.Fail);
            /* FIXME */
            output.put("FailInfo", ActionResultInfo.ErrorDBQueryLogistics);
            return;
        } finally {
            try {
                rs.close();
                pStmt.close();
            } catch (Exception e)   {
                e.printStackTrace();
            }
            DBSessionUtil.closeDBSession(session);
            output.put("Result", ActionResultInfo.Success);
            output.put("insplogisticses", ja);
        }
    }
    public void vtypequerybyinspect(JSONObject input, JSONObject output) {
        String sql="SELECT  a.InspectCode,  b.* " +
                " FROM (SELECT  InspectCode,TypeID,COUNT(InspectID) inspectcnt   FROM inspect  WHERE Category=1 AND InspectCode='"+input.getString("InspectCode")+"' AND VendorID="+input.getLong("VendorID")+" GROUP BY InspectCode,TypeID) a," +
                "  (SELECT * FROM producttype  WHERE   VendorID="+input.getLong("VendorID")+") b " +
                " WHERE a.TypeID = b.TypeID ";
        String msg0 = LogUtil.MsgDBSQL;
        msg0 += (sql);
        /* FIXME */
        LogUtil.info(LogUtil.LogLogistics, LogUtil.LogLogistics, this
                .getClass().getName(), msg0);

        JSONArray ja = new JSONArray();
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        Session session=null;
        try {
            session = DBSessionUtil.getDBSession();
            pStmt=session.connection().prepareStatement(sql);
            rs = pStmt.executeQuery();
            ResultSetMetaData md = rs.getMetaData();
            while (rs.next()) {
                JSONObject jo = new JSONObject();
                jo.put("InspectCode", rs.getString("InspectCode"));
                jo.put("TypeID", rs.getLong("TypeID"));
                jo.put("ProductName", rs.getString("ProductName"));
                jo.put("ApplyStatus",rs.getString("ApplyStatus"));
                jo.put("Category", rs.getInt("Category"));
                jo.put("OriginCountry",rs.getString("OriginCountry"));
                jo.put("OriginRegion",rs.getString("OriginRegion"));
                jo.put("OutDistributors",rs.getString("OutDistributors"));
                jo.put("NetContent",rs.getString("NetContent"));
                jo.put("NetContentUnit", rs.getString("NetContentUnit"));
                jo.put("LabelProof", rs.getString("LabelProof"));
                jo.put("Brand", rs.getString("Brand"));
                jo.put("Remain",rs.getString("Remain"));

                if (rs.getString("CategoryID") == null)
                {
                    jo.put("CategoryID", "0");
                }
                else
                {
                    jo.put("CategoryID", rs.getString("CategoryID"));
                }
                jo.put("VendorID", rs.getLong("VendorID"));
                jo.put("AuthID", rs.getInt("AuthID"));
                jo.put("ProductCount", rs.getInt("ProductCount"));
                jo.put("TagsBought", rs.getLong("TagsBought"));
                jo.put("TagsApplied", rs.getLong("TagsApplied"));
                jo.put("TagsUsed", rs.getLong("TagsUsed"));
                jo.put("TagType", rs.getInt("TagType"));
                jo.put("BatchCount",rs.getInt("BatchCount"));
                jo.put("ShelfLife",rs.getInt("ShelfLife"));
                jo.put("ShelfLifeUnit", rs.getString("ShelfLifeUnit"));
                if (rs.getString("TypeInfo") == null)
                    jo.put("TypeInfo", "null");
                else
                    jo.put("TypeInfo", rs.getString("TypeInfo"));
                jo.put("HSCode", rs.getString("HSCode"));
                jo.put("CNProofID",rs.getString("CNProofID"));
                jo.put("ManageReport",rs.getString("ManageReport"));
                jo.put("CNProofSample", rs.getString("CNProofSample"));
                if (rs.getString("Importer") == null)
                    jo.put("Importer", "null");
                else
                    jo.put("Importer", rs.getString("Importer"));

                if (rs.getString("Remark") == null)
                    jo.put("Remark", "null");
                else
                    jo.put("Remark", rs.getString("Remark"));

                ja.add(jo);
            }
            rs.close();
            pStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            output.put("Result", ActionResultInfo.Fail);
            /* FIXME */
            output.put("FailInfo", ActionResultInfo.ErrorDBQueryLogistics);
            return;
        } finally {
            DBSessionUtil.closeDBSession(session);
            output.put("Result", ActionResultInfo.Success);
            output.put("VPType", ja);
        }
    }

}
