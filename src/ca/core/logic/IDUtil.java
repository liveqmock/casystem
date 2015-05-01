package ca.core.logic;

public class IDUtil {
	/*
	 * Old:
	 * TagID:        10字节，即20个16进制数。从左到右如下：
	 *    IDVersion:  0.5字节，即    1 Hex数
	 *    AuthID:     1字节，       即    2 Hex数
	 *    VendorID:   2.5字节，即    5 Hex数
	 *    VPTypeID:   1.5字节，即    3 Hex数
	 *    ProductID:  4.5字节，即    9 Hex数，其中高5位DeviceID
	 *    
	 *    AuthCode: 4字节
	 * New:
	 * TagID:        10字节，即20个16进制数。从左到右如下：
	 *    IDVersion:  0.5字节，即    1 Hex数
	 *    AuthID:     1      字节， 即    2 Hex数    省局
	 *    VendorID:   2.5字节，即    5 Hex数    子局+企业
	 *    VPTypeID:   2      字节，即    4 Hex数      商品类别
	 *    ProductID:  4       字节，即    8 Hex数，其中高5位DeviceID
	 *    
	 *    AuthCode:   4       字节
	 * New 2014.4.3:
	 * TagID:         10字节，即20个16进制数。从左到右如下：
	 *    IDVersion:  0.5字节，即    1 Hex数
	 *    ProAuthID:  1      字节， 即    2 Hex数    省局
	 *    AuthID   :  6  bits 子局
	 *    VendorID:   14 bits，企业
	 *    VPTypeID:   2      字节，即    4 Hex数      商品类别
	 *    ProductID:  4       字节，即    8 Hex数，其中高5位DeviceID
	 *    
	 *    AuthCode:   4       字节
	 */
	// Version域
	private static final int IDVersion     = 0;
	@SuppressWarnings("unused")
	private static final int IDVersionBits = 4;
	
	// ProviceAuthID
	private static final int ProvinceAuthIDBits    = 8;
	private static final int ProvinceAuthId = 1;
	
	private static final int AuthIDBits    = 6;

	
	// VendorID域
	private static final int VendorStartID = 0x3a6f8;
	private static final int VendorIDMask  = 0x00003fff;
	private static final int VendorIDBits  = 14;         // 单独的VendorID，数据库中包含AuthID
	
	// TypeID域
	private static final int TypeStartID = 0xa9d;
	private static final int TypeIDMask  = 0x0000ffff;
	private static final int TypeIDBits  = 16;           // 单独的TypeID，数据库中TypeID包含AuthID和VendorID

	// ProductID, 含DeviceID
	public static final int ProductIDBits = 32;          // 产品ID位数，含DeviceID
	public static final int ProductIDMask = 0x07ffffff;  // 不含DeviceID时的掩码
	
	// DeviceID
	public static final int DeviceMaxId   = 31;      // 喷码设备的ID为：0-31
	public static final int DeviceIDBits  = 5;       // 喷码设备的ID为：0-31，占据商品ID的最高5位

	// 16进制字符串中标签字段定义
	public static final int TagIDVersionLength = 1;   //标签版本长度
	public static final int TagTypeIDStart     = 1;   //左边开始，标签中TypeID起始字符
	public static final int TagTypeIDLength    = 11;  //左边开始，标签中TypeID结束字符
	public static final int TagProductIDStart  = 12;  //左边开始，标签中ProductID起始字符
	public static final int TagProductIDLength = 8;   //左边开始，标签中ProductID结束字符
	public static final int TagAuthCodeStart   = 20;  //左边开始，标签中AuthCode起始字符
	public static final int TagAuthCodeLength  = 8;   //左边开始，标签中AuthCode结束字符
	
	/*
	 * 获得最新VendorID，由版本号，省局ID，厂家编号（子局和企业一起编码）组成
	 * 
	 * @param authId
	 *    授权方ID，不含IDVersion
	 * @param vendorCount
	 *    authId授权方当前所包含的厂家数量
	 */
	public static long getVendorId(int authId, int vendorCount)
	{
		// id version
		long tmp1 =  (long)(IDUtil.IDVersion) << (IDUtil.VendorIDBits 
				+ IDUtil.AuthIDBits + IDUtil.ProvinceAuthIDBits);
		// province auth
		long tmp2 = (long)(IDUtil.ProvinceAuthId) << (IDUtil.VendorIDBits + IDUtil.AuthIDBits);
		// auth
		long tmp3 = (long)(authId) << IDUtil.VendorIDBits;
		// vendor
		long tmp4 = (vendorCount + IDUtil.VendorStartID) & IDUtil.VendorIDMask;
		
		return (tmp1 + tmp2 + tmp3 + tmp4);
//		long tmp1 = (vendorCount + IDUtil.VendorStartID) & IDUtil.VendorIDMask;
//		long tmp2 =  (long)(authId) << IDUtil.VendorIDBits;
//		//long tmp2 =  (long)(ProviceAuthId) << IDUtil.VendorIDBits;
//		long tmp3 =  (long)(IDUtil.IDVersion) << (IDUtil.VendorIDBits + IDUtil.AuthIDBits + IDUtil.ProviceAuthIDBits);
//		return (tmp1 + tmp2 + tmp3);
	}

	/*
	 * 获得最新厂家商品类型ID，由VendorID和商品类型编号组成。
	 * 
	 * @param vendorId
	 *    厂家ID，已经包含IDVersion
	 * @param typeCount
	 *    authId授权方当前所包含的厂家数量
	 */
	public static long getTypeId(long vendorId, int typeCount)
	{
		long tmp1 = (typeCount + IDUtil.TypeStartID) & IDUtil.TypeIDMask;
		long tmp2 =  (long)(vendorId) << IDUtil.TypeIDBits;
		return (tmp1 + tmp2);
	}
	
	/*
	 * 根据设备ID和当前该设备所包含的产品数量，生成产品ID
	 */
	public static long getProductId(int deviceId, long currentCount)
	{
		long productId = (((long)deviceId) << (ProductIDBits - DeviceIDBits)) + (currentCount & ProductIDMask) + 1;
		return productId;
	}

	/*
	 * 根据版本号，类型ID，产品ID，生成认证ID
	 * 
	 * @param typeId
	 *    已经包含IDVersion
	 * @param productId
	 *    商品类型内部的产品编号
	 */
	public static String getProductId(long typeId, long productId)
	{
		String fmtTypeId = "%0" + (IDUtil.TagIDVersionLength + IDUtil.TagTypeIDLength) + "x";
		String fmtProductId = "%0" + IDUtil.TagProductIDLength + "x";
		String strTotalId = String.format(fmtTypeId, typeId) +
				            String.format(fmtProductId, productId);
		
		return strTotalId;
	}
	
	public static String getBatchTable(long typeId)
	{
		String strTypeId = Long.toHexString(typeId);
		String batchTable = "batch" + strTypeId;
		return batchTable;
	}	

	public static String getProductAuthTable(long typeId)
	{
		String strTypeId = Long.toHexString(typeId);
		String paTable = "pa" + strTypeId;
		return paTable;
	}
	
	/*
	 * 临时认证表表名: typeId的十六进制字符串 + 用户ID + 喷码器设备ID或标签烧写软件ID
	 * 
	 * @param typeId
	 *    已经包含IDVersion
	 * @param userId
	 *    当前登录会话的用户ID
	 * @param deviceID
	 *    喷码器设备ID或标签烧写软件ID
	 */
	public static String getTempPaTable(long typeId, int userId, int deviceID)
	{
		String strTypeId = Long.toHexString(typeId);
		String strUserId = Integer.toHexString(userId);
		String strDeviceId = Integer.toHexString(deviceID);
		String paTable = "tmppa" + strTypeId + strUserId + strDeviceId;
		return paTable;
	}
	
	/*
	 * 根据标签ID的16进制字符串获取类型ID
	 */
	public static long getTypeId(String code)
	{
		String strTypeId = String.copyValueOf(code.toCharArray(), TagTypeIDStart, TagTypeIDLength);
		long typeId = Long.valueOf(strTypeId, 16);
		return typeId;
	}
	
	/*
	 * 根据标签ID的16进制字符串获取产品ID
	 */
	public static long getProductId(String code)
	{
		String strProductId = String.copyValueOf(code.toCharArray(), TagProductIDStart, TagProductIDLength);
		long productId = Long.valueOf(strProductId, 16);
		return productId;
	}

	/*
	 * 根据标签ID和认证码的16进制字符串获取认证码部分字符串
	 */
	public static String getAuthCode(String code)
	{
		String strAuthCode = String.copyValueOf(code.toCharArray(), TagAuthCodeStart, TagAuthCodeLength);
		
		return strAuthCode;
	}
    public static int getOrgid(String code){
        long typeid = IDUtil.getTypeId(code);
        //System.out.println("typeid=="+typeid);
        long t = typeid;
        t = t << 4;
        t = t & 0xff0000000000l;
        t = t >> 40;
        return   (int)t;
    }

    public static int getSubOrgid(String code){
        long typeid = IDUtil.getTypeId(code);
        //System.out.println("typeid=="+typeid);
        long t = typeid;
        t = t << 4;
        t = t & 0xff0000000000l;
        t = t >> 40;
        int org = (int)t;

        t = typeid;
        t = t << 12;
        t = t & 0xff0000000000l;
        t = t >> 42;
        return   (int)t;
    }

    public static Long getEnigma(String code){
        return Long.valueOf(IDUtil.getAuthCode(code),16);
    }
    public static void parse(String code){
        //setCode(code);
        long typeid = IDUtil.getTypeId(code);
        System.out.println("typeid=="+typeid);
        long t = typeid;
        t = t << 4;
        t = t & 0xff0000000000l;
        t = t >> 40;
        int org = (int)t;
        System.out.println("org=="+org);

        t = typeid;
        t = t << 12;
        t = t & 0xff0000000000l;
        t = t >> 42;
        int suborg = (int)t;
        System.out.println("suborg=="+suborg);

        long productid = IDUtil.getProductId(code);
        System.out.println("productid=="+productid);
        Long enigma = Long.valueOf(IDUtil.getAuthCode(code),16);
        System.out.println("enigma=="+enigma);

    }
}
