package ca.core.logic;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.struts2.ServletActionContext;

import ca.core.db.SessionOperator;

public class LogUtil {
	public static final int TypeIDNone    = 0;
	public static final int InspectNone   = 0;
	public static final int LogTypeNone   = 0;
	public static final int LogTypeUser   = 1;
	public static final int LogTypeAuth   = 2;
	public static final int LogTypeVendor = 3;
	public static final int LogTypeVPType = 4;
	public static final int LogTypeTag    = 5;
	public static final int LogInspect    = 6;
	public static final int LogLogistics  = 7;
    public static final int LogAPPFormat  = 8;
    public static final int LogMailLog  = 9;
    public static final int LogReportLog  = 10;
	
	public static final String MsgInput    = "接收的接口信息: ";
	public static final String MsgOutput   = "返回的接口信息: ";
	public static final String MsgDBSQL    = "数据库操作: SQL = ";
	public static final String MsgDBInsert = "数据库插入: Object = ";
	public static final String MsgDBUpdate = "数据库跟新: Object = ";
	
	private static Logger logger = Logger.getLogger("SYSTEM");
	
	private static int getUserId()
	{
		int userId = 0;
		
		try
		{
//			HttpSession httpsession = ServletActionContext.getRequest().getSession();
//			userId = (Integer) httpsession.getAttribute("UserID");
			String token = ServletActionContext.getRequest().getHeader(SessionOperator.HeaderTokenName);
			if (token != null)
			{
				SessionOperator sOperator = new SessionOperator();
				if (sOperator.sessionUpdate(token))
				{
					userId = sOperator.SessionUserId(token);
				}
			}
						
		}
		catch (Exception e)
		{
			userId = 0;
		}
		return userId;
	}
		
	 /**
	  * 崩溃级别
	  * 
	  * @param username
	  * @param message
	  */
	public static void fatal(int logType, long typeId, String className, String message)
	{
		message = message.replaceAll("'", "\\\\'");
		MDC.put("UserID", getUserId());
		MDC.put("LogType", logType);
		MDC.put("TypeID", typeId);
		MDC.put("ClassName", className);
		logger.fatal(message);
	}
	
	 /**
	  * 错误级别
	  * 
	  * @param username
	  * @param message
	  */
	public static void error(int logType, long typeId, String className, String message)
	{
		message = message.replaceAll("'", "\\\\'");
		MDC.put("UserID", getUserId());
		MDC.put("LogType", logType);
		MDC.put("TypeID", typeId);
		MDC.put("ClassName", className);
		logger.error(message);
	}
	
	 /**
	  * 消息级别
	  * 
	  * @param username
	  * @param message
	  */
	public static void info(int logType, long typeId, String className, String message)
	{
		message = message.replaceAll("'", "\\\\'");
		MDC.put("UserID", getUserId());
		MDC.put("LogType", logType);
		MDC.put("TypeID", typeId);
		MDC.put("ClassName", className);
		logger.info(message);
	}
	
	 /**
	  * 警告级别
	  * 
	  * @param username
	  * @param message
	  */
	public static void warn(int logType, long typeId, String className, String message)
	{
		message = message.replaceAll("'", "\\\\'");
		MDC.put("UserID", getUserId());
		MDC.put("LogType", logType);
		MDC.put("TypeID", typeId);
		MDC.put("ClassName", className);
		logger.warn(message);
	}
	
	 /**
	  * 调试级别
	  * 
	  * @param username
	  * @param message
	  */
	public static void debug(int logType, long typeId, String className, String message)
	{
		message = message.replaceAll("'", "\\\\'");
		MDC.put("UserID", getUserId());
		MDC.put("LogType", logType);
		MDC.put("TypeID", typeId);
		MDC.put("ClassName", className);
		logger.debug(message);
	}
}
