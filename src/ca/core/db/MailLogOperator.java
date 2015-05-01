package ca.core.db;

import ca.core.logic.ActionResultInfo;
import ca.core.logic.DBSessionUtil;
import ca.core.logic.DateUtil;
import ca.core.logic.LogUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.hibernate.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

/**
 * Created by Administrator on 2014/6/18.
 */
public class MailLogOperator {

   // -- 提供接口 更新接口
   // 输入参数：{"IDS":[{"ID":1,"STATUS":0},{"ID":2,"STATUS":0},{"ID":3,"STATUS":0},{"ID":4,"STATUS":1}]}
   // -- STATUS ：0邮件发送成功，1邮件发送失败
   // 输出参数：{"Result":0} 或者 {"Result":1,"FailInfo":"***"}
    public void modify(JSONObject input, JSONObject output) {
        String sql="UPDATE T_BUSI_MAILLOG SET FLAG=1 WHERE ID=?";
        String msg0 = LogUtil.MsgDBSQL;
        msg0 += sql;
        /* FIXME */
        LogUtil.info(LogUtil.LogMailLog, LogUtil.LogMailLog, this
                .getClass().getName(), msg0);
        Session session=null;
        Connection con=null;
        PreparedStatement pStmt = null;
        try {
            session=  DBSessionUtil.getDBSession();
            con=session .connection();
            con.setAutoCommit(false);
            pStmt=con.prepareStatement(sql);
            for(int ii=0;ii<input.getJSONArray("IDS").size();ii++){
                pStmt.setLong(1, input.getJSONArray("IDS").getJSONObject(ii).getLong("ID"));
                pStmt.addBatch();
            }
            pStmt.executeBatch();

            con.commit();
            pStmt.close();
            con.close();
            DBSessionUtil.closeDBSession(session);
            output.put("Result", ActionResultInfo.Success);
        } catch (Exception e) {
            e.printStackTrace();
            output.put("Result", ActionResultInfo.Fail);
            /* FIXME */
            output.put("FailInfo", ActionResultInfo.ErrorModifyMailLog);
            return;
        }
    }
    public void query(JSONObject input, JSONObject output) {
        String sql=" SELECT a.ID,a.RECORDFLAG,a.FLAG,a.AUTHEMAIL,a.VENDOREMAIL,a.AUTHID, a.VENDORID,DATE_FORMAT(a.SAVETIME,'%Y-%m-%d %H:%i:%s') SAVETIME, a.TYPEID, a.PID,a.PCODE,a.OPSTATUS,a.USERID,a.USERNAME FROM T_BUSI_MAILLOG a,(SELECT TYPEID,RECORDFLAG,MAX(ID) ID FROM T_BUSI_MAILLOG WHERE FLAG=0 AND SAVETIME>=DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY) ";
        String sql1=" GROUP BY TYPEID,RECORDFLAG)b WHERE a.ID=b.ID AND a.TYPEID=b.TYPEID AND a.FLAG=0 AND a.SAVETIME>=DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY) ";

            if(input.containsKey("AUTHID")){
               sql+=" AND AUTHID="+input.getLong("AUTHID");
               sql1+=" AND a.AUTHID="+input.getLong("AUTHID");
            }
            if(input.containsKey("RECORDFLAG")){
                sql+=" AND RECORDFLAG="+input.getInt("RECORDFLAG");
                sql1+=" AND a.RECORDFLAG="+input.getInt("RECORDFLAG");
            }
            if(input.containsKey("VENDORID")){
                sql+=" AND VENDORID="+input.getLong("VENDORID");
                sql1+=" AND a.VENDORID="+input.getLong("VENDORID");
            }


        String msg0 = LogUtil.MsgDBSQL;
        msg0 += sql+sql1;
        /* FIXME */
        LogUtil.info(LogUtil.LogMailLog, LogUtil.LogMailLog, this
                .getClass().getName(), msg0);

        JSONArray ja = new JSONArray();
        @SuppressWarnings("unchecked")
        List<MailLognew> mylist = DBSessionUtil.query(sql+sql1, MailLognew.class);
        if (mylist != null) {
            output.put("Result", ActionResultInfo.Success);
            for (MailLognew mailLognew : mylist) {
                JSONObject jo = new JSONObject();
                // ID,RECORDFLAG,FLAG,AUTHEMAIL,VENDOREMAIL,AUTHID, VENDORID,SAVETIME, TYPEID, PID,PCODE,OPSTATUS
                jo.put("ID", mailLognew.getId());
                jo.put("RECORDFLAG",mailLognew.getRecordflag());
                jo.put("AUTHEMAIL",mailLognew.getAuthemail());
                jo.put("VENDOREMAIL",mailLognew.getVendoremail());
                jo.put("AUTHID",mailLognew.getAuthid());
                jo.put("VENDORID",mailLognew.getVendorid());
                jo.put("SAVETIME",mailLognew.getSavetime());
                jo.put("TYPEID",mailLognew.getTypeid());
                jo.put("PID",mailLognew.getPid());
                jo.put("PCODE",mailLognew.getPcode());
                jo.put("OPSTATUS",mailLognew.getOpstatus());
                ja.add(jo);
            }
        }
        output.put("maillogs", ja);
    }
    public void query_group(JSONObject input, JSONObject output) {

    }
}
