package ca.core.logic;

public class ActionResultInfo {
	public static final Integer Fail = 1;
	public static final Integer Success = 0;
	
	public static final String  ErrorLogin          = "用户名或密码错误！";
	public static final String  ErrorLogout         = "用户退出错误！";
	public static final String  ErrorRegister       = "用户信息不完整！";
	public static final String  ErrorQueryUser      = "用户信息不完整！";
	public static final String  ErrorModifyUser     = "用户信息不完整！";
	public static final String  ErrorUserNameExist  = "用户名已经存在！";
	public static final String  ErrorUserOffline    = "用户未登录！";
	public static final String  ErrorAddVendor      = "企业信息不完整！";
	public static final String  ErrorQueryVendor    = "企业信息不完整！";
	public static final String  ErrorModifyVendor   = "企业信息不完整！";
	public static final String  ErrorAddAuth        = "授权方信息不完整！";
	public static final String  ErrorQueryAuth      = "授权方信息不完整！";
	public static final String  ErrorModifyAuth     = "授权方信息不完整！";
	public static final String  ErrorAddVPType      = "企业商品类型信息不完整！";
	public static final String  ErrorQueryVPType    = "企业商品类型信息不完整！";
	public static final String  ErrorModifyVPType   = "企业商品类型信息不完整！";
	public static final String  ErrorNoVendor       = "厂商不存在";
	public static final String  ErrorBuyTag         = "CA防伪标签授权失败！";
	public static final String  ErrorLackofBalance  = "余额不足";
	public static final String ErrorApplyZeroTag = "申请0个标签";
	public static final String  ErrorApplyTag       = "CA防伪标签申请失败，信息不完整！";
	public static final String  ErrorConfirmTag     = "CA防伪标签使用确认失败！";
	public static final String  ErrorConfirmTagId   = "CA防伪标签使用确认：起始标签存在问题！";
	public static final String  ErrorQueryBatch     = "商品批次查询失败！";
	public static final String  ErrorModifyBatch    = "商品批次修改失败！";
	public static final String  ErrorQueryTagDeal   = "CA防伪标签交易记录查询失败！";
	public static final String  ErrorCheckTag       = "商品未经授权！";
	public static final String  ErrorMatchTag       = "认证码不匹配！";
	public static final String  ErrorNoTag          = "商品未经授权！";
	public static final String  ErrorAddInspect      = "添加报批号失败";
	public static final String  ErrorQueryInspect    = "企业商品类型信息不完整！";
	public static final String  ErrorModifyInspect   = "企业商品类型信息不完整！";
	public static final String  ErrorDBNewAuth      = "创建新的授权方信息时，数据库操作失败";
	public static final String ErrorDBQueryAuth = "查找授权方信息时，数据库操作失败";
	public static final String ErrorDBModifyAuth = "修改授权方信息时，数据库操作失败";
	public static final String ErrorDBQueryTagAuth = "查找商品认证信息时，数据库操作失败";
	public static final String ErrorDBQueryVPBatch = "查找商品批次信息时，数据库操作失败";
	public static final String ErrorDBQueryVendor = "查找企业信息时，数据库操作失败";
	public static final String ErrorDBModifyTagAuth = "修改商品认证计数时，数据库操作失败";
	public static final String ErrorDBQueryVPType = "查找企业商品类新信息时，数据库操作失败";
	public static final String ErrorDBNewTagDeal = "新建商品标签交易信息时，数据库操作失败";
	public static final String ErrorDBQueryTagDEal = "查找商品标签交易信息时，数据库操作失败";
	public static final String ErrorDBCreateTmpPA = "临时新建商品认证时，数据库操作失败";
	public static final String ErrorDBNewBatch = "新建商品批次信息时，数据库操作失败";
	public static final String ErrorDBModifyVPType = "修改企业商品类新信息时，数据库操作失败";
	public static final String ErrorDBDeleteTmpPA = "删除临时认证表时，数据库操作失败";
	public static final String ErrorDBOperation = "数据库操作失败";
	public static final String ErrorDBModifyUser = "更新用户状态时，数据库操作失败";
	public static final String ErrorDBNewUser = "新建用户时，数据库操作失败";
	public static final String ErrorDBQueryUser = "查找用户信息时，数据库操作失败";
	public static final String ErrorDBModifyVendor = "修改企业信息时，数据库操作失败";
	public static final String ErrorDBNewVendor = "新增企业时，数据库操作失败";
	public static final String ErrorDBModifyVPBatch = "修改商品批次信息时，数据库操作失败";
	public static final String ErrorDBCreateVPBatch = "修改商品批次信息表时，数据库操作失败";
	public static final String ErrorDBCreatePA = "新建商品认证时，数据库操作失败";
	public static final String ErrorDBNewVPType = "新建企业商品类型时，数据库操作失败";
	public static final String ErrorDBTmpPA2PA = "拷贝临时认证信息入库时，数据库操作失败";
	public static final String ErrorDBNewTmpPA = "在临时认证表中新增认证信息时，数据库操作失败";
	public static final String ErrorQueryTag = "标签查询失败";
	public static final String ErrorLogQuery = "日志查询失败";
	public static final String ErrorVPType = "错误的TypeID";
	public static final String ErrorNoAppliedTag = "请先下载认证码";
	public static final String ErrorWriteLog = "修改商品类型时，日志写入失败";
	public static final String ErrorDBNewInspect = "新建商品报批表时，数据库操作失败";
	public static final String ErrorDBQueryInspect = "查找商品报批表时，数据库操作失败";
	public static final String ErrorDBModifyInspect = "修改商品报批表时，数据库操作失败";
	public static final String ErrorLogisticsAdd = "出区核销出错，数据库操作失败";
	public static final String ErrorDBQueryLogistics = "查找出区核销表时，数据库操作失败";
	public static final String ErrorDBModifyLogistics = "修改出区核销表时，数据库操作失败";
    public static final String ErrorDBModifyLogisticsNoID = "修改出区核销表时，缺少参数LogisticsID";
    public static final String ErrorDBNoParameter = "无传入参数";
    public static final String  ErrorAddAPPFormat    = "添加APP定制信息失败！";
    public static final String  ErrorModifyAPPFormat    = "修改APP定制信息失败！";
    public static final String  ErrorDelAPPFormat    = "删除APP定制信息失败！";
    public static final String  ErrorQueryAPPFormat    = "查询APP定制信息失败！";
    public static final String  ErrorAPPFormatNoID    = "缺少参数！";
    public static final String  ErrorAddMailLog    = "添加Email信息失败！";
    public static final String  ErrorModifyMailLog    = "修改Email信息失败！";
    public static final String  ErrorDelMailLog    = "删除Email信息失败！";
    public static final String  ErrorQueryMailLog    = "查询Email信息失败！";
    public static final String  ErrorMailLogNoID    = "缺少参数！";
    public static final String  ErrorQueryReport    = "报表查询失败！";
    public static final String  ErrorQueryReportNoParameter    = "报表查询失败，缺少参数！";
    public static final String  ErrorQueryReportErrorParameter    = "报表查询失败，错误参数！";
}
