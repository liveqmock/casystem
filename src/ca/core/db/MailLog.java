package ca.core.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
/**
 * Created by Administrator on 2014/6/18.
 */

/**
 * Producttype entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "T_BUSI_MAILLOG", catalog = "cadb")
public class MailLog  implements java.io.Serializable{

    long id;
    int recordflag;
    int flag;
    String authemail;
    String vendoremail;
    long authid;
    long vendorid;
    String vendorname;
    long typeid;
    String productname;
    long inspectid;
    String inspectcode;
    int category;
    int tagtype;
    int applystatus;
    int inspectstatus;
    String netcontent;
    String netcontentunit;
    String productcount;
    String productcountunit;
    int inspectremain;
    Timestamp inspectdate;
    Timestamp applydate ;
    Timestamp savetime  ;
    int  userid ;
    String username ;

    /*
            <!--
         `AUTHEMAIL`  VARCHAR(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '局方email' ,
`VENDOREMAIL`  VARCHAR(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '企业email' ,
`AUTHID`  BIGINT NOT NULL COMMENT '监管方编号' ,
`VENDORID`  BIGINT NOT NULL COMMENT '企业编号' ,
`VENDORNAME`  VARCHAR(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '企业名称' ,
`TYPEID`  BIGINT NOT NULL COMMENT '商品类型编号' ,
`PRODUCTNAME`  VARCHAR(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品名称' ,
`INSPECTID`  BIGINT NOT NULL COMMENT '报检流水号' ,
`INSPECTCODE`  VARCHAR(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品报检号，由企业用户填入' ,
`CATEGORY`  INT NOT NULL COMMENT '商品类别（1进口/2出口/3普通）' ,
`TAGTYPE`  INT NOT NULL COMMENT '认证类型 1 - C码，2 - 涂层认证，3 - 芯片认证' ,
`APPLYSTATUS`  INT NOT NULL DEFAULT -100 COMMENT '备案审批状态(默认值-100) -1草稿 0 待备案 1备案通过 2备案不通过' ,
`INSPECTSTATUS`  INT NOT NULL DEFAULT -100 COMMENT '报检状态(默认值-100) -1草稿 0待审批  1检验中  2待修改  3合格  4不合格  5紧急召回' ,
`NETCONTENT`  VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '净含量' ,
`NETCONTENTUNIT`  VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '净含量单位（1-g，2-kg，3-mL，4-L）' ,
`PRODUCTCOUNT`  VARCHAR(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '该批次的商品数量' ,
`PRODUCTCOUNTUNIT`  VARCHAR(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '报检数量的单位（1-瓶、2-盒、3-件、4-个）' ,
`INSPECTREMAIN`  INT(20) NOT NULL DEFAULT 0 COMMENT '未出区商品的剩余数量' ,
`INSPECTDATE`  TIMESTAMP NOT NULL  COMMENT '报检申报日期' ,
`APPLYDATE`  TIMESTAMP NOT NULL  COMMENT '备案申请时间' ,
`SAVETIME`  TIMESTAMP NOT NULL  COMMENT '入库时间' ,
`USERID`  INT NOT NULL DEFAULT -1 COMMENT '操作用户ID' ,
`USERNAME`  VARCHAR(756) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '-1' COMMENT '操作用户' ,
        -->
    */


    // Property accessors
    @Id
    @Column(name = "ID")
     public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    @Column(name = "RECORDFLAG")
    public int getRecordflag() {
        return recordflag;
    }

    public void setRecordflag(int recordflag) {
        this.recordflag = recordflag;
    }
    @Column(name = "FLAG")
    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
    @Column(name = "AUTHEMAIL")
    public String getAuthemail() {
        return authemail;
    }

    public void setAuthemail(String authemail) {
        this.authemail = authemail;
    }
    @Column(name = "VENDOREMAIL")
    public String getVendoremail() {
        return vendoremail;
    }

    public void setVendoremail(String vendoremail) {
        this.vendoremail = vendoremail;
    }
    @Column(name = "AUTHID")
    public long getAuthid() {
        return authid;
    }

    public void setAuthid(long authid) {
        this.authid = authid;
    }
    @Column(name = "VENDORID")
    public long getVendorid() {
        return vendorid;
    }

    public void setVendorid(long vendorid) {
        this.vendorid = vendorid;
    }
    @Column(name = "VENDORNAME")
    public String getVendorname() {
        return vendorname;
    }

    public void setVendorname(String vendorname) {
        this.vendorname = vendorname;
    }
    @Column(name = "TYPEID")
    public long getTypeid() {
        return typeid;
    }

    public void setTypeid(long typeid) {
        this.typeid = typeid;
    }
    @Column(name = "PRODUCTNAME")
    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }
    @Column(name = "INSPECTID")
    public long getInspectid() {
        return inspectid;
    }

    public void setInspectid(long inspectid) {
        this.inspectid = inspectid;
    }
    @Column(name = "INSPECTCODE")
    public String getInspectcode() {
        return inspectcode;
    }

    public void setInspectcode(String inspectcode) {
        this.inspectcode = inspectcode;
    }
    @Column(name = "CATEGORY")
    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
    @Column(name = "TAGTYPE")
    public int getTagtype() {
        return tagtype;
    }

    public void setTagtype(int tagtype) {
        this.tagtype = tagtype;
    }
    @Column(name = "APPLYSTATUS")
    public int getApplystatus() {
        return applystatus;
    }

    public void setApplystatus(int applystatus) {
        this.applystatus = applystatus;
    }
    @Column(name = "INSPECTSTATUS")
    public int getInspectstatus() {
        return inspectstatus;
    }

    public void setInspectstatus(int inspectstatus) {
        this.inspectstatus = inspectstatus;
    }
    @Column(name = "NETCONTENT")
    public String getNetcontent() {
        return netcontent;
    }

    public void setNetcontent(String netcontent) {
        this.netcontent = netcontent;
    }
    @Column(name = "NETCONTENTUNIT")
    public String getNetcontentunit() {
        return netcontentunit;
    }

    public void setNetcontentunit(String netcontentunit) {
        this.netcontentunit = netcontentunit;
    }
    @Column(name = "PRODUCTCOUNT")
    public String getProductcount() {
        return productcount;
    }

    public void setProductcount(String productcount) {
        this.productcount = productcount;
    }
    @Column(name = "PRODUCTCOUNTUNIT")
    public String getProductcountunit() {
        return productcountunit;
    }

    public void setProductcountunit(String productcountunit) {
        this.productcountunit = productcountunit;
    }
    @Column(name = "INSPECTREMAIN")
    public int getInspectremain() {
        return inspectremain;
    }

    public void setInspectremain(int inspectremain) {
        this.inspectremain = inspectremain;
    }
    @Column(name = "INSPECTDATE")
    public Timestamp getInspectdate() {
        return inspectdate;
    }

    public void setInspectdate(Timestamp inspectdate) {
        this.inspectdate = inspectdate;
    }
    @Column(name = "APPLYDATE")
    public Timestamp getApplydate() {
        return applydate;
    }

    public void setApplydate(Timestamp applydate) {
        this.applydate = applydate;
    }
    @Column(name = "SAVETIME")
    public Timestamp getSavetime() {
        return savetime;
    }

    public void setSavetime(Timestamp savetime) {
        this.savetime = savetime;
    }
    @Column(name = "USERID")
    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
    @Column(name = "USERNAME")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
     public MailLog(){

     }
    public MailLog(    long id,
            int recordflag,
            int flag,
            String authemail,
            String vendoremail,
            long authid,
            long vendorid,
            String vendorname,
            long typeid,
            String productname,
            long inspectid,
            String inspectcode,
            int category,
            int tagtype,
            int applystatus,
            int inspectstatus,
            String netcontent,
            String netcontentunit,
            String productcount,
            String productcountunit,
            int inspectremain,
            Timestamp inspectdate,
            Timestamp applydate ,
            Timestamp savetime ,
            int  userid ,
            String username ){
        this.id= id;
        this.recordflag= recordflag;
        this.flag= flag;
        this.authemail= authemail;
        this.vendoremail= vendoremail;
        this.authid= authid;
        this.vendorid= vendorid;
        this.vendorname= vendorname;
        this.typeid= typeid;
        this.productname= productname;
        this.inspectid= inspectid;
        this.inspectcode= inspectcode;
        this.category= category;
        this.tagtype= tagtype;
        this.applystatus= applystatus;
        this.inspectstatus= inspectstatus;
        this.netcontent= netcontent;
        this.netcontentunit= netcontentunit;
        this.productcount= productcount;
        this.productcountunit =productcountunit;
        this.inspectremain= inspectremain;
        this.inspectdate= inspectdate;
        this.applydate= applydate ;
        this.savetime= savetime  ;
        this.userid=  userid ;
        this.username= username ;
    }
}
