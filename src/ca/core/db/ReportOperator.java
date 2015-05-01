package ca.core.db;

import ca.core.logic.ActionResultInfo;
import ca.core.logic.DBSessionUtil;
import ca.core.logic.LogUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.hibernate.Session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * Created by Administrator on 2014/7/2.
 */
public class ReportOperator {

    public void query_producttype_rpt(JSONObject input, JSONObject output) {
        //SELECT @rownum:=@rownum+1 rownum,tt.* FROM(
        // SELECT @rownum:=0, a.VendorID,c.VendorName,a.AuthID,a.TypeID,a.ProductName,a.ApplyStatus,a.Category,a.TagType,a.createDate,a.ProductCount,a.Remain,IFNULL(b.InspectCount,0) InspectCount,IFNULL(b.InspectRemain,0) InspectRemain FROM producttype a LEFT JOIN (SELECT VendorID,TypeID,SUM(ProductCount) InspectCount,SUM(Inspectremain) Inspectremain FROM inspect WHERE InspectStatus>=0 GROUP BY VendorID,TypeID) b ON( a.TypeID=b.TypeID AND a.VendorID=b.VendorID) LEFT JOIN vendors c ON (a.VendorID=c.VendorID AND a.AuthID=c.AuthID)WHERE a.ApplyStatus>=0 AND a.AuthID=0 AND a.Category=1 AND a.createDate>='2014-01-01' AND a.createDate<='2014-07-01' AND a.VendorID=1058552
        // )tt;

        //String sql= " SELECT a.VendorID,c.VendorName,a.AuthID,a.TypeID,a.ProductName,a.ApplyStatus,a.Category,a.TagType,a.createDate,a.ProductCount,a.Remain,IFNULL(b.InspectCount,0) InspectCount,IFNULL(b.InspectRemain,0) InspectRemain FROM producttype a LEFT JOIN (SELECT VendorID,TypeID,SUM(ProductCount) InspectCount,SUM(Inspectremain) Inspectremain FROM inspect WHERE InspectStatus>=0 GROUP BY VendorID,TypeID) b ON( a.TypeID=b.TypeID AND a.VendorID=b.VendorID) LEFT JOIN vendors c ON (a.VendorID=c.VendorID AND a.AuthID=c.AuthID)WHERE a.ApplyStatus>=0  ";
         String  sql="SELECT @rownum:=@rownum+1 rownum,tt.* FROM(";
         sql+=" SELECT @rownum:=0, a.VendorID,c.VendorName,a.AuthID,a.TypeID,a.ProductName,a.ApplyStatus,a.Category,a.TagType,a.createDate,a.ProductCount,a.Remain,IFNULL(b.InspectCount,0) InspectCount,IFNULL(b.InspectRemain,0) InspectRemain FROM producttype a LEFT JOIN (SELECT VendorID,TypeID,SUM(ProductCount) InspectCount,SUM(Inspectremain) Inspectremain FROM inspect WHERE InspectStatus>=0 GROUP BY VendorID,TypeID) b ON( a.TypeID=b.TypeID AND a.VendorID=b.VendorID) LEFT JOIN vendors c ON (a.VendorID=c.VendorID AND a.AuthID=c.AuthID)WHERE a.ApplyStatus>=0 ";
        if(input.containsKey("AuthID")){
            sql +=  " AND a.AuthID =" + input.getLong("AuthID");
        }
        if(input.containsKey("VendorID")){
            sql +=  " AND a.VendorID =" + input.getLong("VendorID");
        }
        if(input.containsKey("Category")){
            int Category=input.getInt("Category") ;
            sql +=  " AND a.Category = " + Category ;
        }
        if(input.containsKey("BeginDate")){
            sql +=  " AND a.createDate >= '" + input.getString("BeginDate") +"'";
        }
        if(input.containsKey("EndDate")){
            sql +=  " AND a.createDate <= '" + input.getString("EndDate") +"'";
        }
       sql +=" ORDER BY a.VendorID,a.TypeID)tt";
        // }
        String msg0 = LogUtil.MsgDBSQL;
        msg0 += (sql);
        /* FIXME */
        LogUtil.info(LogUtil.LogReportLog, LogUtil.LogReportLog, this
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
               // SELECT a.VendorID,c.VendorName,a.AuthID,a.TypeID,a.ProductName,a.ApplyStatus,a.Category,
               // a.TagType,a.createDate,a.ProductCount,a.Remain,IFNULL(b.InspectCount,0) InspectCount,IFNULL(b.InspectRemain,0) InspectRemain

                jo.put("rownum",rs.getLong("rownum"));
                jo.put("VendorID",rs.getLong("VendorID"));
                jo.put("VendorName",rs.getString("VendorName"));
                jo.put("AuthID",rs.getLong("AuthID"));
                jo.put("TypeID",rs.getLong("TypeID"));
                jo.put("ProductName",rs.getString("ProductName"));
                jo.put("Category",rs.getInt("Category"));
                jo.put("ApplyStatus",rs.getInt("ApplyStatus"));
                jo.put("TagType",rs.getInt("TagType"));
                jo.put("createDate",rs.getString("createDate"));
                jo.put("ProductCount",rs.getLong("ProductCount"));
                jo.put("Remain",rs.getLong("Remain"));
                jo.put("InspectCount",rs.getLong("InspectCount"));
                jo.put("InspectRemain",rs.getLong("InspectRemain"));
                ja.add(jo);
            }

        } catch (Exception e) {
            e.printStackTrace();
            output.put("Result", ActionResultInfo.Fail);
            /* FIXME */
            output.put("FailInfo", ActionResultInfo.ErrorQueryReport);
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
            output.put("reports", ja);
        }
    }
    public void query_inspect_rpt(JSONObject input, JSONObject output) {
        //SELECT @rownum:=@rownum+1 rownum,tt.* FROM(
        //SELECT @rownum:=0,a.InspectCode,a.VendorID,b.ProductName,c.VendorName,a.TypeID,b.AuthID,a.Category,a.InspectStatus,a.InspectDate,a.ProductValue,a.ProductValueUnit,CONVERT(a.ProductCount,DECIMAL) InspectCount,a.ProductCountUnit,a.InspectRemain FROM inspect a,producttype b,vendors c WHERE  a.TypeID=b.TypeID AND a.VendorID=b.VendorID AND a.VendorID=c.VendorID AND a.InspectStatus>=0 AND a.Category=1 AND a.VendorID=1058552 AND a.InspectDate>='2014-01-01' AND a.InspectDate<='2014-07-01';;
        // )tt;
       // String sql= " SELECT a.InspectCode,a.VendorID,b.ProductName,c.VendorName,a.TypeID,b.AuthID,a.Category,a.InspectStatus,a.InspectDate,a.ProductValue,a.ProductValueUnit,CONVERT(a.ProductCount,DECIMAL) InspectCount,a.ProductCountUnit,a.InspectRemain FROM inspect a,producttype b,vendors c WHERE  a.TypeID=b.TypeID AND a.VendorID=b.VendorID AND a.VendorID=c.VendorID AND a.InspectStatus>=0   ";
        String sql=" SELECT @rownum:=@rownum+1 rownum,tt.* FROM( ";
        sql+=" SELECT @rownum:=0,a.InspectCode,a.VendorID,b.ProductName,c.VendorName,a.TypeID,b.AuthID,a.Category,a.InspectStatus,a.InspectDate,a.ProductValue,a.ProductValueUnit,CONVERT(a.ProductCount,DECIMAL) InspectCount,a.ProductCountUnit,a.InspectRemain FROM inspect a,producttype b,vendors c WHERE  a.TypeID=b.TypeID AND a.VendorID=b.VendorID AND a.VendorID=c.VendorID AND a.InspectStatus>=0 ";
        if(input.containsKey("AuthID")){
            sql +=  " AND b.AuthID =" + input.getLong("AuthID");
        }
        if(input.containsKey("VendorID")){
            sql +=  " AND a.VendorID =" + input.getLong("VendorID");
        }
        if(input.containsKey("InspectCode")){
            sql +=  " AND a.InspectCode ='" + input.getString("InspectCode")+"'";
        }
        if(input.containsKey("Category")){
            int Category=input.getInt("Category") ;
            sql +=  " AND a.Category = " + Category ;
        }
        if(input.containsKey("BeginDate")){
            sql +=  " AND a.InspectDate >= '" + input.getString("BeginDate") +"'";
        }
        if(input.containsKey("EndDate")){
            sql +=  " AND a.InspectDate <= '" + input.getString("EndDate") +"'";
        }

        sql +=" ORDER BY a.VendorID,a.TypeID)tt";
        String msg0 = LogUtil.MsgDBSQL;
        msg0 += (sql);
        /* FIXME */
        LogUtil.info(LogUtil.LogReportLog, LogUtil.LogReportLog, this
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
                //a.InspectCode,a.VendorID,b.ProductName,c.VendorName,a.TypeID,a.Category,
                // a.InspectStatus,a.InspectDate,a.ProductValue,a.ProductValueUnit,CONVERT(a.ProductCount,DECIMAL) InspectCount,a.ProductCountUnit,a.InspectRemain
                jo.put("rownum",rs.getLong("rownum"));
                jo.put("InspectCode",rs.getString("InspectCode"));
                jo.put("VendorID",rs.getLong("VendorID"));
                jo.put("VendorName",rs.getString("VendorName"));
                jo.put("AuthID",rs.getLong("AuthID"));
                jo.put("TypeID",rs.getLong("TypeID"));
                jo.put("ProductName",rs.getString("ProductName"));
                jo.put("Category",rs.getInt("Category"));
                jo.put("InspectStatus",rs.getInt("InspectStatus"));
                jo.put("InspectDate",rs.getString("InspectDate"));
                jo.put("ProductValue",rs.getLong("ProductValue"));
                jo.put("ProductValueUnit",rs.getString("ProductValueUnit"));
                jo.put("InspectCount",rs.getLong("InspectCount"));
                jo.put("InspectRemain",rs.getLong("InspectRemain"));
                jo.put("ProductCountUnit",rs.getString("ProductCountUnit"));
                ja.add(jo);
            }

        } catch (Exception e) {
            e.printStackTrace();
            output.put("Result", ActionResultInfo.Fail);
            /* FIXME */
            output.put("FailInfo", ActionResultInfo.ErrorQueryReport);
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
            output.put("reports", ja);
        }
    }

    public void query_logisticcode_rpt(JSONObject input, JSONObject output) {
        //SELECT @rownum:=@rownum+1 rownum,tt.* FROM(
        //SELECT @rownum:=0,a.InspectCode,a.VendorID,b.ProductName,c.VendorName,a.TypeID,a.Category,a.InspectStatus,a.InspectDate,a.ProductValue,a.ProductValueUnit,CONVERT(a.ProductCount,DECIMAL) InspectCount,a.ProductCountUnit,a.InspectRemain FROM inspect a LEFT JOIN producttype b ON(a.TypeID=b.TypeID AND a.VendorID=b.VendorID) LEFT JOIN vendors c ON(a.VendorID=c.VendorID ) LEFT JOIN (SELECT VendorID,TypeID,InspectCode,SUM(OutCount) OutCount FROM logistics GROUP BY VendorID,TypeID,InspectCode)d ON(a.TypeID=d.TypeID AND a.VendorID=d.VendorID AND a.InspectCode=d.InspectCode) WHERE a.InspectStatus>=0 AND a.VendorID=1058552 AND a.InspectDate>='2014-01-01' AND a.InspectDate<='2014-07-01'
        // ORDER BY a.TypeID,a.InspectCode)tt;
        //String sql= " SELECT a.InspectCode,a.VendorID,b.ProductName,c.VendorName,a.TypeID,a.Category,a.InspectStatus,a.InspectDate,a.ProductValue,a.ProductValueUnit,CONVERT(a.ProductCount,DECIMAL) InspectCount,a.ProductCountUnit,a.InspectRemain FROM inspect a LEFT JOIN producttype b ON(a.TypeID=b.TypeID AND a.VendorID=b.VendorID) LEFT JOIN vendors c ON(a.VendorID=c.VendorID ) LEFT JOIN (SELECT VendorID,TypeID,InspectCode,SUM(OutCount) OutCount FROM logistics GROUP BY VendorID,TypeID,InspectCode)d ON(a.TypeID=d.TypeID AND a.VendorID=d.VendorID AND a.InspectCode=d.InspectCode) WHERE a.InspectStatus>=0 ";
        String sql=" SELECT @rownum:=@rownum+1 rownum,tt.* FROM( ";
        sql+=" SELECT @rownum:=0,a.InspectCode,a.VendorID,b.ProductName,c.VendorName,a.TypeID,a.Category,a.InspectStatus,a.InspectDate,a.ProductValue,a.ProductValueUnit,CONVERT(a.ProductCount,DECIMAL) InspectCount,a.ProductCountUnit,a.InspectRemain FROM inspect a LEFT JOIN producttype b ON(a.TypeID=b.TypeID AND a.VendorID=b.VendorID) LEFT JOIN vendors c ON(a.VendorID=c.VendorID ) LEFT JOIN (SELECT VendorID,TypeID,InspectCode,SUM(OutCount) OutCount FROM logistics GROUP BY VendorID,TypeID,InspectCode)d ON(a.TypeID=d.TypeID AND a.VendorID=d.VendorID AND a.InspectCode=d.InspectCode) WHERE a.InspectStatus>=0 ";
//        if(input.containsKey("AuthID")){
//            sql +=  " AND a.AuthID =" + input.getLong("AuthID");
//        }
        if(input.containsKey("VendorID")){
            sql +=  " AND a.VendorID =" + input.getLong("VendorID");
        }
        if(input.containsKey("InspectCode")){
            sql +=  " AND a.InspectCode ='" + input.getString("InspectCode")+"'";
        }
        if(input.containsKey("Category")){
            int Category=input.getInt("Category") ;
            sql +=  " AND a.Category = " + Category ;
        }
        if(input.containsKey("BeginDate")){
            sql +=  " AND a.InspectDate >= '" + input.getString("BeginDate") +"'";
        }
        if(input.containsKey("EndDate")){
            sql +=  " AND a.InspectDate <= '" + input.getString("EndDate") +"'";
        }

        sql +=  " ORDER BY a.InspectCode,a.TypeID)tt";
        String msg0 = LogUtil.MsgDBSQL;
        msg0 += (sql);
        /* FIXME */
        LogUtil.info(LogUtil.LogReportLog, LogUtil.LogReportLog, this
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
                // a.InspectCode,a.VendorID,b.ProductName,c.VendorName,a.TypeID,a.Category,a.InspectStatus,a.InspectDate,
                // a.ProductValue,a.ProductValueUnit,CONVERT(a.ProductCount,DECIMAL) InspectCount,a.ProductCountUnit,a.InspectRemain
                jo.put("rownum",rs.getLong("rownum"));
                jo.put("InspectCode",rs.getString("InspectCode"));
                jo.put("VendorID",rs.getLong("VendorID"));
                jo.put("VendorName",rs.getString("VendorName"));
                //jo.put("AuthID",rs.getLong("AuthID"));
                jo.put("TypeID",rs.getLong("TypeID"));
                jo.put("ProductName",rs.getString("ProductName"));
                jo.put("Category",rs.getInt("Category"));
                jo.put("InspectStatus",rs.getInt("InspectStatus"));
                jo.put("InspectDate",rs.getString("InspectDate"));
                jo.put("ProductValue",rs.getLong("ProductValue"));
                jo.put("ProductValueUnit",rs.getString("ProductValueUnit"));
                jo.put("InspectCount",rs.getLong("InspectCount"));
                jo.put("InspectRemain",rs.getLong("InspectRemain"));
                jo.put("ProductCountUnit",rs.getString("ProductCountUnit"));
                ja.add(jo);
            }

        } catch (Exception e) {
            e.printStackTrace();
            output.put("Result", ActionResultInfo.Fail);
            /* FIXME */
            output.put("FailInfo", ActionResultInfo.ErrorQueryReport);
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
            output.put("reports", ja);
        }
    }

    public void query_logisticid_rpt(JSONObject input, JSONObject output) {
        //SELECT @rownum:=@rownum+1 rownum,tt.* FROM(
        //SELECT @rownum:=0,a.InspectCode,a.VendorID,b.ProductName,c.VendorName,a.TypeID,a.Category,a.InspectStatus,a.InspectDate,a.ProductValue,a.ProductValueUnit,CONVERT(a.ProductCount,DECIMAL) InspectCount,a.ProductCountUnit,a.InspectRemain FROM inspect a LEFT JOIN producttype b ON(a.TypeID=b.TypeID AND a.VendorID=b.VendorID) LEFT JOIN vendors c ON(a.VendorID=c.VendorID ) LEFT JOIN (SELECT VendorID,TypeID,InspectCode,SUM(OutCount) OutCount FROM logistics GROUP BY VendorID,TypeID,InspectCode)d ON(a.TypeID=d.TypeID AND a.VendorID=d.VendorID AND a.InspectCode=d.InspectCode) WHERE a.InspectStatus>=0 AND a.VendorID=1058552 AND a.InspectDate>='2014-01-01' AND a.InspectDate<='2014-07-01'
        // ORDER BY a.TypeID,a.InspectCode)tt;
        //String sql= " SELECT a.InspectCode,a.VendorID,b.ProductName,c.VendorName,a.TypeID,a.Category,a.InspectStatus,a.InspectDate,a.ProductValue,a.ProductValueUnit,CONVERT(a.ProductCount,DECIMAL) InspectCount,a.ProductCountUnit,a.InspectRemain FROM inspect a LEFT JOIN producttype b ON(a.TypeID=b.TypeID AND a.VendorID=b.VendorID) LEFT JOIN vendors c ON(a.VendorID=c.VendorID ) LEFT JOIN (SELECT VendorID,TypeID,InspectCode,SUM(OutCount) OutCount FROM logistics GROUP BY VendorID,TypeID,InspectCode)d ON(a.TypeID=d.TypeID AND a.VendorID=d.VendorID AND a.InspectCode=d.InspectCode) WHERE a.InspectStatus>=0  ";
        String sql=" SELECT @rownum:=@rownum+1 rownum,tt.* FROM( ";
        sql+=" SELECT @rownum:=0,a.InspectCode,a.VendorID,b.ProductName,c.VendorName,a.TypeID,a.Category,a.InspectStatus,a.InspectDate,a.ProductValue,a.ProductValueUnit,CONVERT(a.ProductCount,DECIMAL) InspectCount,a.ProductCountUnit,a.InspectRemain FROM inspect a LEFT JOIN producttype b ON(a.TypeID=b.TypeID AND a.VendorID=b.VendorID) LEFT JOIN vendors c ON(a.VendorID=c.VendorID ) LEFT JOIN (SELECT VendorID,TypeID,InspectCode,SUM(OutCount) OutCount FROM logistics GROUP BY VendorID,TypeID,InspectCode)d ON(a.TypeID=d.TypeID AND a.VendorID=d.VendorID AND a.InspectCode=d.InspectCode) WHERE a.InspectStatus>=0 ";

//        if(input.containsKey("AuthID")){
//            sql +=  " AND a.AuthID =" + input.getLong("AuthID");
//        }
        if(input.containsKey("VendorID")){
            sql +=  " AND a.VendorID =" + input.getLong("VendorID");
        }
        if(input.containsKey("TypeID")){
            sql +=  " AND a.TypeID =" + input.getLong("TypeID");
        }
        if(input.containsKey("Category")){
            int Category=input.getInt("Category") ;
            sql +=  " AND a.Category = " + Category ;
        }
        if(input.containsKey("BeginDate")){
            sql +=  " AND a.InspectDate >= '" + input.getString("BeginDate") +"'";
        }
        if(input.containsKey("EndDate")){
            sql +=  " AND a.InspectDate <= '" + input.getString("EndDate") +"'";
        }
        sql +=  " ORDER BY a.TypeID,a.InspectCode)tt";

        String msg0 = LogUtil.MsgDBSQL;
        msg0 += (sql);
        /* FIXME */
        LogUtil.info(LogUtil.LogReportLog, LogUtil.LogReportLog, this
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
                // a.InspectCode,a.VendorID,b.ProductName,c.VendorName,a.TypeID,a.Category,a.InspectStatus,a.InspectDate,
                // a.ProductValue,a.ProductValueUnit,CONVERT(a.ProductCount,DECIMAL) InspectCount,a.ProductCountUnit,a.InspectRemain
                jo.put("rownum",rs.getLong("rownum"));
                jo.put("InspectCode",rs.getString("InspectCode"));
                jo.put("VendorID",rs.getLong("VendorID"));
                jo.put("VendorName",rs.getString("VendorName"));
                //jo.put("AuthID",rs.getLong("AuthID"));
                jo.put("TypeID",rs.getLong("TypeID"));
                jo.put("ProductName",rs.getString("ProductName"));
                jo.put("Category",rs.getInt("Category"));
                jo.put("InspectStatus",rs.getInt("InspectStatus"));
                jo.put("InspectDate",rs.getString("InspectDate"));
                jo.put("ProductValue",rs.getLong("ProductValue"));
                jo.put("ProductValueUnit",rs.getString("ProductValueUnit"));
                jo.put("InspectCount",rs.getLong("InspectCount"));
                jo.put("InspectRemain",rs.getLong("InspectRemain"));
                jo.put("ProductCountUnit",rs.getString("ProductCountUnit"));
                ja.add(jo);
            }

        } catch (Exception e) {
            e.printStackTrace();
            output.put("Result", ActionResultInfo.Fail);
            /* FIXME */
            output.put("FailInfo", ActionResultInfo.ErrorQueryReport);
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
            output.put("reports", ja);
        }
    }
}
