package ca.core.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import ca.core.logic.ActionResultInfo;
import ca.core.logic.DBSessionUtil;
import ca.core.logic.DateUtil;
import ca.core.logic.IDUtil;
import ca.core.logic.LogUtil;

public class TagAuthorize
{
	public static final String TagStatusDesc[] = 
		{"未激活", "检验中", "未定义保留" ,"检验合格", "不合格", "紧急召回", "正规商品(过期)", "未知商品", "该二次认证码无效！", "认证通过", "该商品已经被购买,购买时间："};
	public static final int TagInvalid = 0; //"未激活";
	public static final int TagInProof = 1; //"检验中";
	public static final int TagValid = 2; //待修改;
	public static final int TagOK = 3;      //"检验合格";
	public static final int TagNotOK = 4;   //"不合格";
	public static final int TagRecall = 5;  //"紧急召回";
	public static final int TagExpire = 6;  //"正规商品(过期)";
	public static final int TagUnknown = 7; //"未知商品";
    public static final int CoatUnKnow = 8;//未知商品验证
    public static final int CoatSuccess = 9;
    public static final int CoatFail = 10;
	public static Properties prop = new Properties();
	
	static {
		InputStream  is = null;
		try {
			is = TagAuthorize.class.getClassLoader().getResourceAsStream("config.properties");
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public TagAuthorize() {}

	public static void returnTagUnknown(JSONObject output)
	{
//		int tagStatus;
//   	JSONObject jo = new JSONObject();
//		tagStatus = TagAuthorize.TagUnknown;
//		jo.put("Status", tagStatus);
//		jo.put("StatusDesc", TagAuthorize.TagStatusDesc[tagStatus]);
		output.put("Result", ActionResultInfo.Fail);
		output.put("FailInfo", TagStatusDesc[TagUnknown]);
//		output.put("ProductInfo", jo);
	}
	
	/*
	 * MF1 标签认证
	 * 1. 将authCode分段解释，如厂家商品类型ID，即TypeID， 商品编号，认证码
	 * 2. 根据获取的TypeID，获得相应的商品认证表
	 * 3. 根据商品编号，在商品认证表中搜索相应的条目
	 * 4. 对认证码进行认证
	 * 5. 如果认证通过，则找到相应的商品批次信息表，找到相应的条目
	 * 
	 * @param authCode
	 *   标签编号和认证码
	 * @param output
	 *   认证结果和商品信息。
	 */
	public void MF1TagAuthorize(String authCode, JSONObject output)
	{
		CTagAuthorize_part(authCode, output);
	}

    /**
     * 涂层二次验证
     * @param authCode
     * @param coatCode
     * @param output
     * 第一次验证：返回首次验证信息，回写出售时间，已被出售
     * 之后的验证：返回已被出售，出售时间
     */
    public void CoatCodeAuthorize(String authCode,String coatCode, JSONObject output){
        authCode = authCode.toLowerCase();
        // 1. 将authCode分段解释，如厂家商品类型ID，即TypeID， 商品编号，认证码
        String strAuthCode = IDUtil.getAuthCode(authCode);
        // 2. 根据获取的TypeID，获得相应的商品认证表
        long typeID = IDUtil.getTypeId(authCode);
        String authTable = "pa" + Long.toHexString(typeID);
        // 3. 根据商品编号，在商品认证表中搜索相应的条目
        long productId = IDUtil.getProductId(authCode);
        String sql = "select * from " + authTable + " where ProductID=" + productId;

        String msg0 = LogUtil.MsgDBSQL;
        msg0 += sql;
        LogUtil.info(LogUtil.LogTypeTag, LogUtil.TypeIDNone, this.getClass().getName(), msg0);

        @SuppressWarnings("unchecked")
        List<Productauth> authlist = DBSessionUtil.query(sql, Productauth.class);
        if (authlist == null || authlist.size() != 1)
        {
            TagAuthorize.returnTagUnknown(output);
            return;
        }
        // 4. 对认证码进行认证
        Productauth auth = authlist.get(0);
        if (!(auth.getCoatCode().substring(4).equals(coatCode)))
        {
            output.put("Result", ActionResultInfo.Fail);
            output.put("FailInfo", TagStatusDesc[CoatUnKnow]);
            return;
        }
        // 5. 如果认证通过，根据认证标志返回不同信息
        JSONArray infoArray = new JSONArray();
        if(auth.getSaleFlag() == 0){
            //没有认证过，第一次出售
            sql = "update " + authTable + " set saleflag = " + 1 + ",saledate = now() where productid=" + auth.getProductId() ;
            if (!DBSessionUtil.update(sql))
            {
                output.put("Result", ActionResultInfo.Fail);
                output.put("FailInfo", TagStatusDesc[CoatUnKnow]);
                return;
            }
            //infoArray.add(GenDisplayInfo("SaleDate", "购买时间", "", "1"));
            //infoArray.add(GenDisplayInfo("SaleFlag", "是否出售", "0", "1"));
            //infoArray.add(GenDisplayInfo("SaleDesc", "购买信息", "验证通过，该商品首次销售!", "1"));
            infoArray.add(GenDisplayInfo("SaleDesc", "认证结果", "认证成功，该商品为首次销售！", "1"));
        } else {
            //已经认证过
            //infoArray.add(GenDisplayInfo("SaleDate", "购买时间", DateUtil.convertDate2String(auth.getSaleDate(),DateUtil.DATE_FORMAT), "1"));
            //infoArray.add(GenDisplayInfo("SaleFlag", "是否出售", "1", "1"));
            //infoArray.add(GenDisplayInfo("SaleDesc", "购买信息", "该商品非首次销售!", "1"));
            infoArray.add(GenDisplayInfo("SaleDesc", "认证结果", "二次认证码已被使用，该商品非首次销售！", "1"));
        }
        String jsonStr = infoArray.toString();
        output.put("Result", ActionResultInfo.Success);
        output.put("SaleInfo", jsonStr);
    }
	
	/*
	 * C码标签认证
	 * 1. 将authCode分段解释，如厂家商品类型ID，即TypeID， 商品编号，认证码
	 * 2. 根据获取的TypeID，获得相应的商品认证表
	 * 3. 根据商品编号，在商品认证表中搜索相应的条目
	 * 4. 对认证码进行认证
	 * 5. 如果认证通过，则找到相应的商品批次信息表，找到相应的条目
	 * 6. 认证此数加 1
	 * 7. 输出商品信息
	 * 
	 * @param authCode
	 *   标签编号和认证码
	 * @param output
	 *   认证结果和商品信息。
	 */
	public void CTagAuthorize(String authCode, JSONObject output)
	{
		int tagStatus = 0;
		authCode = authCode.toLowerCase();
		// 1. 将authCode分段解释，如厂家商品类型ID，即TypeID， 商品编号，认证码
        String strAuthCode = IDUtil.getAuthCode(authCode);
        // 2. 根据获取的TypeID，获得相应的商品认证表
        long typeID = IDUtil.getTypeId(authCode);
        String authTable = "pa" + Long.toHexString(typeID);
        String batchTable = "batch" + Long.toHexString(typeID);
        // 3. 根据商品编号，在商品认证表中搜索相应的条目
        long productId = IDUtil.getProductId(authCode);
        String sql = "select * from " + authTable + " where ProductID=" + productId;

        String msg0 = LogUtil.MsgDBSQL;
        msg0 += sql;
        LogUtil.info(LogUtil.LogTypeTag, LogUtil.TypeIDNone, this.getClass().getName(), msg0);

        @SuppressWarnings("unchecked")
        List<Productauth> authlist = DBSessionUtil.query(sql, Productauth.class);
        if (authlist == null || authlist.size() != 1)
        {
            TagAuthorize.returnTagUnknown(output);
            return;
        }
		// 4. 对认证码进行认证
		Productauth auth = authlist.get(0);
		if (!auth.getAuthCode().contains(strAuthCode))
		{
			TagAuthorize.returnTagUnknown(output);
			return;
		}
		// 5. 如果认证通过，则找到相应的商品批次信息表，找到相应的条目
		Productbatch batch = null;
		if (auth.getBatchId() != 0) 
		{
			sql = "select * from " + batchTable + " where BatchID=" + auth.getBatchId();

			@SuppressWarnings("unchecked")
			List<Productbatch> batchlist = DBSessionUtil.query(sql, Productbatch.class);
			if (batchlist == null || batchlist.size() != 1)
			{
				TagAuthorize.returnTagUnknown(output);
				return;
			}
			batch = batchlist.get(0);
			
		}
		
		long vendorId = batch.getVendorID();

		sql = "select * from vendors where VendorID=" + vendorId;

		@SuppressWarnings("unchecked")
		List<Vendors> vendorlist = DBSessionUtil.query(sql, Vendors.class);
		if (vendorlist == null || vendorlist.size() != 1)
		{
			TagAuthorize.returnTagUnknown(output);
			return;
		}
		Vendors vendor = vendorlist.get(0);

        long inspectID = batch.getInspectID();

        sql = "select * from inspect where InspectID=" + inspectID;

        @SuppressWarnings("unchecked")
        List<Inspect> inspectlist = DBSessionUtil.query(sql, Inspect.class);
        if (inspectlist == null || inspectlist.size() != 1)
        {
            TagAuthorize.returnTagUnknown(output);
            return;
        }
        Inspect inspect = inspectlist.get(0);
		
		sql = "select * from producttype where TypeId=" + typeID;

		@SuppressWarnings("unchecked")
		List<Producttype> producttypelist = DBSessionUtil.query(sql, Producttype.class);
		if (producttypelist == null || producttypelist.size() != 1)
		{
			TagAuthorize.returnTagUnknown(output);
			return;
		}
		Producttype producttype = producttypelist.get(0);
		
		// 6. 认证此数加 1
		sql = "update " + authTable + " set Counter=" + (auth.getCounter() + 1) + " where ProductID=" + auth.getProductId();
		String msg = LogUtil.MsgDBSQL;
		msg += sql;
		LogUtil.info(LogUtil.LogTypeTag, LogUtil.TypeIDNone, this.getClass().getName(), msg);
		if (!DBSessionUtil.update(sql))
		{
			TagAuthorize.returnTagUnknown(output);
			return;
		}
		// 7. 输出商品信息
		output.put("Result", ActionResultInfo.Success);
		
		tagStatus = auth.getStatus();
		
		JSONArray infoArray = new JSONArray();
		infoArray.add(GenDisplayInfo("ProductName", "商品名称", producttype.getProductName(), "1"));
        infoArray.add(GenDisplayInfo("OriginCountry", "原产国(地区)", producttype.getOriginCountry(), "1"));
        if(null != producttype.getOriginRegion() && !"".equals(producttype.getOriginRegion()))
            infoArray.add(GenDisplayInfo("OriginRegion", "产地(产区)", producttype.getOriginRegion(), "1"));
        infoArray.add(GenDisplayInfo("ProductDate", "生产日期",DateUtil.convertDate2String(batch.getProductDate(), DateUtil.DEFAULT_DATE_FORMAT), "1"));
        if(!"0".equals(producttype.getShelfLife()))
            infoArray.add(GenDisplayInfo("ShelfLife", "保质期", producttype.getShelfLife() + prop.getProperty("casystem.shelfLifeUnit." + producttype.getShelfLifeUnit()), "1"));
        infoArray.add(GenDisplayInfo("OutDistributors", "生产商", producttype.getOutDistributors(), "1"));
        if(producttype.getCategory() == 1){
            infoArray.add(GenDisplayInfo("InDistributors", "境内经销商",   inspect.getInDistributors(), "1"));
        }
        infoArray.add(GenDisplayInfo("NetContent", "净含量", producttype.getNetContent() + prop.getProperty("casystem.netcontentunit." + producttype.getNetContentUnit()), "1"));
        infoArray.add(GenDisplayInfo("Count", "查询次数", auth.getCounter().toString(), "1"));
        infoArray.add(GenDisplayInfo("Vendor", "企业名称",   vendor.getVendorName(), "1"));
        if(producttype.getCategory() == 1){
            //进口商品
            if(inspect.getImportGate() != null && !"".equals(inspect.getImportGate())){
                infoArray.add(GenDisplayInfo("ImportGate", "进口口岸",   inspect.getImportGate(), "1",prop.getProperty("casystem.SubBureau")));
                //infoArray.add(GenDisplayInfo("ImportGateURL", "进口口岸URL",   prop.getProperty("casystem.SubBureau"), "1"));
            }
            infoArray.add(GenDisplayInfo("InspectCode", "商品报检单号",   inspect.getInspectCode(), "1"));
            if(inspect.getInspectInCode() != null && !"".equals(inspect.getInspectInCode())){
                infoArray.add(GenDisplayInfo("InspectInCode", "海关进境备案清单号",   inspect.getInspectInCode(), "1"));
            }
            infoArray.add(GenDisplayInfo("IntoDate", "货物入区日期",DateUtil.convertDate2String(inspect.getIntoDate(), DateUtil.DEFAULT_DATE_FORMAT), "1"));
        }else {
            //出口商品
            infoArray.add(GenDisplayInfo("InspectCode", "商品报检单号",   inspect.getInspectCode(), "1"));
        }
        if(inspect.getInspectStatus() == 5){
            infoArray.add(GenDisplayInfo("Urgent", "召回原因",   inspect.getUrgent(), "1"));
        }
		infoArray.add(GenDisplayInfo("LabelProof", "商品图片", prop.getProperty("casystem.url.prefix") + "productimg.action?tid=" + Long.toString(typeID) + "&prefix=", "2"));
		//先判断图片是否存在
        File f = new File(prop.getProperty("casystem.imageuploadpath") + "/" + producttype.getTypeId() + "/sample" + producttype.getTypeId() + ".jpg");
        if(f.exists())
            infoArray.add(GenDisplayInfo("CNProofSample", "中文标签样张", prop.getProperty("casystem.url.prefix") + "productimg.action?tid=" + Long.toString(typeID) + "&prefix=sample", "2"));
        //先判断图片是否存在
        File f1 = new File(prop.getProperty("casystem.applyuploadpath") + "/" + inspect.getInspectId() + "/" + inspect.getInspectId() + ".jpg");
        if(f1.exists() && inspect.getInspectStatus() != 5)
            infoArray.add(GenDisplayInfo("InspectReport", "局方检验合格证", prop.getProperty("casystem.url.prefix") + "applyimg.action?inspectID=" + Long.toString(inspect.getInspectId()), "2"));
        String jsonStr = infoArray.toString();
		output.put("ProductInfo", jsonStr);
		output.put("Status", tagStatus);
		output.put("TagType", producttype.getTagType());
		output.put("StatusDesc", TagAuthorize.TagStatusDesc[tagStatus]);

	}
    /*
	 * C码标签认证 分区块显示       f
	 * 1. 将authCode分段解释，如厂家商品类型ID，即TypeID， 商品编号，认证码
	 * 2. 根据获取的TypeID，获得相应的商品认证表
	 * 3. 根据商品编号，在商品认证表中搜索相应的条目
	 * 4. 对认证码进行认证
	 * 5. 如果认证通过，则找到相应的商品批次信息表，找到相应的条目
	 * 6. 认证此数加 1
	 * 7. 输出商品信息
	 *
	 * @param authCode
	 *   标签编号和认证码
	 * @param output
	 *   认证结果和商品信息。
	 */
    public void CTagAuthorize_part(String authCode, JSONObject output)
    {
        int tagStatus = 0;
        authCode = authCode.toLowerCase();
        // 1. 将authCode分段解释，如厂家商品类型ID，即TypeID， 商品编号，认证码
        String strAuthCode = IDUtil.getAuthCode(authCode);
        // 2. 根据获取的TypeID，获得相应的商品认证表
        long typeID = IDUtil.getTypeId(authCode);
        String authTable = "pa" + Long.toHexString(typeID);
        String batchTable = "batch" + Long.toHexString(typeID);
        // 3. 根据商品编号，在商品认证表中搜索相应的条目
        long productId = IDUtil.getProductId(authCode);
        String sql = "select * from " + authTable + " where ProductID=" + productId;

        String msg0 = LogUtil.MsgDBSQL;
        msg0 += sql;
        LogUtil.info(LogUtil.LogTypeTag, LogUtil.TypeIDNone, this.getClass().getName(), msg0);

        @SuppressWarnings("unchecked")
        List<Productauth> authlist = DBSessionUtil.query(sql, Productauth.class);
        if (authlist == null || authlist.size() != 1)
        {
            TagAuthorize.returnTagUnknown(output);
            return;
        }
        // 4. 对认证码进行认证
        Productauth auth = authlist.get(0);
        if (!auth.getAuthCode().contains(strAuthCode))
        {
            TagAuthorize.returnTagUnknown(output);
            return;
        }
        // 5. 如果认证通过，则找到相应的商品批次信息表，找到相应的条目
        Productbatch batch = null;
        if (auth.getBatchId() != 0)
        {
            sql = "select * from " + batchTable + " where BatchID=" + auth.getBatchId();

            @SuppressWarnings("unchecked")
            List<Productbatch> batchlist = DBSessionUtil.query(sql, Productbatch.class);
            if (batchlist == null || batchlist.size() != 1)
            {
                TagAuthorize.returnTagUnknown(output);
                return;
            }
            batch = batchlist.get(0);

        }

        long vendorId = batch.getVendorID();

        sql = "select * from vendors where VendorID=" + vendorId;

        @SuppressWarnings("unchecked")
        List<Vendors> vendorlist = DBSessionUtil.query(sql, Vendors.class);
        if (vendorlist == null || vendorlist.size() != 1)
        {
            TagAuthorize.returnTagUnknown(output);
            return;
        }
        Vendors vendor = vendorlist.get(0);

        long inspectID = batch.getInspectID();

        sql = "select * from inspect where InspectID=" + inspectID;

        @SuppressWarnings("unchecked")
        List<Inspect> inspectlist = DBSessionUtil.query(sql, Inspect.class);
        if (inspectlist == null || inspectlist.size() != 1)
        {
            TagAuthorize.returnTagUnknown(output);
            return;
        }
        Inspect inspect = inspectlist.get(0);

        sql = "select * from producttype where TypeId=" + typeID;

        @SuppressWarnings("unchecked")
        List<Producttype> producttypelist = DBSessionUtil.query(sql, Producttype.class);
        if (producttypelist == null || producttypelist.size() != 1)
        {
            TagAuthorize.returnTagUnknown(output);
            return;
        }
        Producttype producttype = producttypelist.get(0);

        // 6. 认证此数加 1
        sql = "update " + authTable + " set Counter=" + (auth.getCounter() + 1) + " where ProductID=" + auth.getProductId();
        String msg = LogUtil.MsgDBSQL;
        msg += sql;
        LogUtil.info(LogUtil.LogTypeTag, LogUtil.TypeIDNone, this.getClass().getName(), msg);
        if (!DBSessionUtil.update(sql))
        {
            TagAuthorize.returnTagUnknown(output);
            return;
        }
        // 7. 输出商品信息
        output.put("Result", ActionResultInfo.Success);

        tagStatus = auth.getStatus();

        //JSONArray infoArray = new JSONArray();
        JSONArray BaseInfo=new JSONArray();
        JSONArray ProductInfo=new JSONArray();
        JSONObject subPinfo=new JSONObject();
        JSONArray subPdata=new JSONArray();

        //商品基本信息
        BaseInfo.add(GenDisplayInfo("ProductName", "商品名称", producttype.getProductName(), "1"));
        BaseInfo.add(GenDisplayInfo("StatusDesc", "商品状态", TagAuthorize.TagStatusDesc[tagStatus], "1"));
        BaseInfo.add(GenDisplayInfo("Count", "查询次数", auth.getCounter().toString(), "1"));
        BaseInfo.add(GenDisplayInfo("LabelProof", "商品图片", prop.getProperty("casystem.url.prefix") + "productimg.action?tid=" + Long.toString(typeID) + "&prefix=", "2"));

        //生产信息
        subPdata.add(GenDisplayInfo("OriginCountry", "原产国(地区)", producttype.getOriginCountry(), "1"));
        if(null != producttype.getOriginRegion() && !"".equals(producttype.getOriginRegion()))
            subPdata.add(GenDisplayInfo("OriginRegion", "产地(产区)", producttype.getOriginRegion(), "1"));
        subPdata.add(GenDisplayInfo("ProductDate", "生产日期",DateUtil.convertDate2String(batch.getProductDate(), DateUtil.DEFAULT_DATE_FORMAT), "1"));
        if(!"0".equals(producttype.getShelfLife()))
            subPdata.add(GenDisplayInfo("ShelfLife", "保质期", producttype.getShelfLife() + prop.getProperty("casystem.shelfLifeUnit." + producttype.getShelfLifeUnit()), "1"));
        subPdata.add(GenDisplayInfo("OutDistributors", "生产商", producttype.getOutDistributors(), "1"));
        subPdata.add(GenDisplayInfo("NetContent", "净含量", producttype.getNetContent() + prop.getProperty("casystem.netcontentunit." + producttype.getNetContentUnit()), "1"));
        subPinfo.put("text", "基本信息");
        subPinfo.put("data", subPdata);
        ProductInfo.add(subPinfo);

        subPinfo.clear();
        subPdata.clear();
        //报检信息
        if(producttype.getCategory() == 1){
            subPdata.add(GenDisplayInfo("InDistributors", "境内经销商",   inspect.getInDistributors(), "1"));
            // InDistributors 境内经销商
            // InAgentor 境内代理商
            // InspectInfo 补充信息  确定不提供显示
            // importer 进口商
            // ImportGate 进出口口岸
            if(inspect.getInAgentor() != null && !"".equals(inspect.getInAgentor())){
                subPdata.add(GenDisplayInfo("InAgentor", "境内代理商",   inspect.getInAgentor(), "1"));
            }
            if(inspect.getImporter() != null && !"".equals(inspect.getImporter())){
                subPdata.add(GenDisplayInfo("importer", "进口商",   inspect.getImporter(), "1"));
            }
        }
        subPdata.add(GenDisplayInfo("Vendor", "企业名称",   vendor.getVendorName(), "1"));
        if(producttype.getCategory() == 1){
            //进口商品
            if(inspect.getImportGate() != null && !"".equals(inspect.getImportGate())){
                subPdata.add(GenDisplayInfo("ImportGate", "进口口岸",   inspect.getImportGate(), "1",prop.getProperty("casystem.SubBureau")));
                //infoArray.add(GenDisplayInfo("ImportGateURL", "进口口岸URL",   prop.getProperty("casystem.SubBureau"), "1"));
            }
            subPdata.add(GenDisplayInfo("InspectCode", "商品报检单号",   inspect.getInspectCode(), "1"));
            if(inspect.getInspectInCode() != null && !"".equals(inspect.getInspectInCode())){
                subPdata.add(GenDisplayInfo("InspectInCode", "海关进境备案号",   inspect.getInspectInCode(), "1"));
            }
            subPdata.add(GenDisplayInfo("IntoDate", "货物入区日期",DateUtil.convertDate2String(inspect.getIntoDate(), DateUtil.DEFAULT_DATE_FORMAT), "1"));

        }else {
            //出口商品
            subPdata.add(GenDisplayInfo("InspectCode", "商品报检单号",   inspect.getInspectCode(), "1"));
        }
        if(inspect.getInspectStatus() == 5){
            subPdata.add(GenDisplayInfo("Urgent", "召回原因",   inspect.getUrgent(), "1"));
        }
        //先判断图片是否存在
        File f = new File(prop.getProperty("casystem.imageuploadpath") + "/" + producttype.getTypeId() + "/sample" + producttype.getTypeId() + ".jpg");
        if(f.exists())
            subPdata.add(GenDisplayInfo("CNProofSample", "中文标签样张", prop.getProperty("casystem.url.prefix") + "productimg.action?tid=" + Long.toString(typeID) + "&prefix=sample", "2"));
        //先判断图片是否存在
        File f1 = new File(prop.getProperty("casystem.applyuploadpath") + "/" + inspect.getInspectId() + "/" + inspect.getInspectId() + ".jpg");
        if(f1.exists() && inspect.getInspectStatus() != 5)
            subPdata.add(GenDisplayInfo("InspectReport", "局方检验合格证", prop.getProperty("casystem.url.prefix") + "applyimg.action?inspectID=" + Long.toString(inspect.getInspectId()), "2"));
        subPinfo.put("text", "报检信息");
        subPinfo.put("data", subPdata);
        ProductInfo.add( subPinfo);

        output.put("BaseInfo", BaseInfo);
        output.put("ProductInfo", ProductInfo);
        output.put("Status", tagStatus);
        output.put("TagType", producttype.getTagType());

//        String jsonStr = infoArray.toString();
//        output.put("ProductInfo", jsonStr);
//        output.put("Status", tagStatus);
//        output.put("TagType", producttype.getTagType());
//        output.put("StatusDesc", TagAuthorize.TagStatusDesc[tagStatus]);

    }
	
	static JSONObject GenDisplayInfo(String key, String text, String value, String type)
	{
		JSONObject jo = new JSONObject();
		jo.put("key", key);
		jo.put("text", text);
		jo.put("value", value);
		jo.put("type", type);
		return jo;
	}
    static JSONObject GenDisplayInfo(String key, String text, String value, String type,String url)
    {
        JSONObject jo = new JSONObject();
        jo.put("key", key);
        jo.put("text", text);
        jo.put("value", value);
        jo.put("type", type);
        jo.put("url", url);
        return jo;
    }
	/*
	 * CPU卡标签认证
	 */
	public void CPUTagAuthorize(JSONObject output)
	{
		//TODO:
	}
}
