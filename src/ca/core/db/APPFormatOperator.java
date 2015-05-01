package ca.core.db;

import ca.core.logic.ActionResultInfo;
import ca.core.logic.DBSessionUtil;
import ca.core.logic.DateUtil;
import ca.core.logic.LogUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2014/6/14.
 */
public class APPFormatOperator {

    public void add(JSONObject input, JSONObject output) {
        APPFormat appFormat=new APPFormat();
        //appFormat.setId();
        if (input.has("Authid")) {
            appFormat.setAuthid(input.getLong("Authid"));
        }
        if (input.has("Vendorid")) {
            appFormat.setVendorid(input.getLong("Vendorid"));
        }
        if (input.has("Typeid")) {
            appFormat.setTypeid(input.getLong("Typeid"));
        }
        if (input.has("Flag")) {
            appFormat.setFlag(input.getInt("Flag"));
        }
        if (input.has("Formatinfo")) {
            appFormat.setFormatinfo(input.getString("Formatinfo"));
        }

        appFormat.setSavetime(DateUtil.convertDate2String(DateUtil.getCurrentDate(),DateUtil.DATE_FORMAT));
        String msg1 = LogUtil.MsgDBInsert;
        msg1 += appFormat.getClass().getName();
        LogUtil.info(LogUtil.LogAPPFormat, LogUtil.LogAPPFormat, this
                .getClass().getName(), msg1);

        if (!DBSessionUtil.save(appFormat)) {
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorAddAPPFormat);
            return;
        }
        output.put("Result", ActionResultInfo.Success);
    }
    public void delete(JSONObject input, JSONObject output) {
        if (input.has("Id")){
            String sql = "DELETE FROM T_BUSI_APPFORMAT WHERE ID="+input.getLong("Id");

            String msg = LogUtil.MsgDBSQL;
            msg += sql;
            LogUtil.info(LogUtil.LogAPPFormat, LogUtil.LogAPPFormat, this.getClass().getName(), msg);
            if (!DBSessionUtil.update(sql))
            {
                output.put("Result", ActionResultInfo.Fail);
                output.put("FailInfo", ActionResultInfo.ErrorDelAPPFormat);
                return;
            }
            output.put("Result", ActionResultInfo.Success);
            return;
        }else {
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorAPPFormatNoID);
            return;
        }
    }
    public void query(JSONObject input, JSONObject output) {
        String sql="SELECT * FROM T_BUSI_APPFORMAT WHERE 1=1 ";
        if(input.has("Id")){
            sql +=  " and ID = " + input.getLong("Id") ;
        }
        if(input.has("Authid")){
            sql +=  " and AUTHID = " + input.getLong("Authid") ;
        }
        if(input.has("Vendorid")){
            sql +=  " and VENDORID = " + input.getLong("Vendorid") ;
        }
        if(input.has("Typeid")){
            sql +=  " and TYPEID = " + input.getLong("Typeid") ;
        }
        if(input.has("Flag")){
            sql +=  " AND FLAG = " + input.getLong("Flag") ;
        }
        if(input.has("Begintime")){
            sql +=  " AND SAVETIME >= '" + input.getString("Begintime")+"'" ;
        }
        if(input.has("Endtime")){
            sql +=  " AND SAVETIME <= '" + input.getString("Endtime")+"'" ;
        }

        String msg0 = LogUtil.MsgDBSQL;
        msg0 += sql;
        /* FIXME */
        LogUtil.info(LogUtil.LogAPPFormat, LogUtil.LogAPPFormat, this
                .getClass().getName(), msg0);

        @SuppressWarnings("unchecked")
        List<APPFormat> mylist = DBSessionUtil.query(sql, APPFormat.class);

        output.put("Result", ActionResultInfo.Success);
        JSONArray ja = new JSONArray();
        if(mylist!=null) {
            for (APPFormat appFormat : mylist) {
                JSONObject jo = new JSONObject();
                jo.put("Id", appFormat.getId());
                jo.put("Authid", appFormat.getAuthid());
                jo.put("Vendorid", appFormat.getVendorid());
                jo.put("Typeid", appFormat.getTypeid());
                jo.put("Flag", appFormat.getFlag());
                jo.put("Savetime",appFormat.getSavetime());
                jo.put("Formatinfo", appFormat.getFormatinfo());
                ja.add(jo);
            }
        }
        output.put("appformats", ja);

    }
    public void modify(JSONObject input, JSONObject output) {
        if (input.has("Id")&&input.has("Formatinfo")){
            String sql = "UPDATE T_BUSI_APPFORMAT SET FORMATINFO = '" + input.getString("Formatinfo") +"' WHERE ID="+input.getLong("Id");

            String msg = LogUtil.MsgDBSQL;
            msg += sql;
            LogUtil.info(LogUtil.LogAPPFormat, LogUtil.LogAPPFormat, this.getClass().getName(), msg);
            if (!DBSessionUtil.update(sql))
            {
                output.put("Result", ActionResultInfo.Fail);
                output.put("FailInfo", ActionResultInfo.ErrorModifyAPPFormat);
                return;
            }
            output.put("Result", ActionResultInfo.Success);
            return;
        }else {
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorAPPFormatNoID);
            return;
        }
    }

    public void merge(JSONObject input, JSONObject output) {
        if (input.has("Authid")&&input.has("Vendorid")&&input.has("Typeid")&&input.has("Flag")&&input.has("Formatinfo")){
            APPFormat appFormat=new APPFormat();
            appFormat.setAuthid(input.getLong("Authid"));
            appFormat.setVendorid(input.getLong("Vendorid"));
            appFormat.setTypeid(input.getLong("Typeid"));
            appFormat.setFlag(input.getInt("Flag"));
            appFormat.setFormatinfo(input.getString("Formatinfo"));
            appFormat.setSavetime(DateUtil.convertDate2String(DateUtil.getCurrentDate(),DateUtil.DATE_FORMAT));

            if (DBSessionUtil.save(appFormat)) {
                output.put("Result", ActionResultInfo.Success);
                return;
            }else{
                String sql = "UPDATE T_BUSI_APPFORMAT SET FORMATINFO = '" + input.getString("Formatinfo") +"' WHERE AUTHID="+input.getLong("Authid")+" AND VENDORID="+input.getLong("Vendorid")+" AND TYPEID="+input.getLong("Typeid")+" AND FLAG="+input.getLong("Flag");
                String msg = LogUtil.MsgDBSQL;
                msg += sql;
                LogUtil.info(LogUtil.LogAPPFormat, LogUtil.LogAPPFormat, this.getClass().getName(), msg);
                if (!DBSessionUtil.update(sql))
                {
                    output.put("Result", ActionResultInfo.Fail);
                    output.put("FailInfo", ActionResultInfo.ErrorModifyAPPFormat);
                    return;
                }
                output.put("Result", ActionResultInfo.Success);
                return;
            }
        }else {
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", ActionResultInfo.ErrorAPPFormatNoID);
            return;
        }
    }
}
