package ca.core.db;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Random;

import ca.core.logic.ActionResultInfo;
import ca.core.logic.DBSessionUtil;

public class SessionOperator
{
	static private final int SessionValid    = 0;
	@SuppressWarnings("unused")
	static private final int SessionInvalid  = 1;
	// 20 minutes
	static private final int SeesionTimeout = 60 * 60;
	
	static private final String FailSessionNotExist = "Session 不存在";
	static private final String FailSessionTimeout = "Session 超时";
	
	private String failInfo = "未知错误";
	
	static public final String HeaderTokenName = "CA-Token";
	
	// Constructors
	/** default constructor */
	public SessionOperator() {}
	
	private String byteToHexString(byte[] bytes) {
		StringBuffer result = new StringBuffer();

        for (int i = 0; i < bytes.length; i++)
        {     								//转化为16进制字符串
            byte b1 = (byte) ((bytes[i] & 0xf0) >> 4);
            byte b2 = (byte) (bytes[i] & 0x0f);
            if (b1 < 10)
                result.append((char) ('0' + b1));
            else
                result.append((char) ('A' + (b1 - 10)));
            if (b2 < 10)
                result.append((char) ('0' + b2));
            else
                result.append((char) ('A' + (b2 - 10)));
        }
        return result.toString();
	}
	
	private String genSessionId(Integer userId)
	{
		// token : 1-16 random data, 17-20 userId, 21-32 dateNow
		// 
		Random random = new SecureRandom();
		byte randomBytes[] = new byte[8];
		random.nextBytes(randomBytes);
				
		Date dateNow = new Date(System.currentTimeMillis());
		String token = byteToHexString(randomBytes) + Integer.toHexString(userId | 0xF0000000).substring(4, 8) 
				+ Long.toHexString(dateNow.getTime() | 0xF000000000000000L).substring(4, 16);
		return token.toUpperCase();
	}
	
	public String sessionAdd(Integer userId)
	{
		String token = genSessionId(userId);
		Date dateNow = new Date(System.currentTimeMillis());

		Usersession usersession = new Usersession(dateNow, token, userId, SessionValid);
			
		if (!DBSessionUtil.save(usersession))
		{
				/* FIXME */
			failInfo = ActionResultInfo.ErrorDBQueryUser;
			return null;
		}
		return token;
	}
	
	public boolean sessionDelete(String token)
	{
		if (token == null)
			return false;
		String sql = "delete from usersession where ";
		sql += "SessionToken='" + token + "'";
		
		if (false == DBSessionUtil.update(sql))
		{
			failInfo = FailSessionNotExist;
			return false;
		}
		
		return true;
	}
	
	public boolean sessionUpdate(String token)
	{
		String sql = "select * from usersession where ";
		sql += "SessionToken='" + token + "'";
		Date dateNow = new Date(System.currentTimeMillis());
		
		@SuppressWarnings("unchecked")
		List<Usersession> mylist = DBSessionUtil.query(sql, Usersession.class);
		if (mylist == null)
		{
			/* FIXME */
			failInfo = ActionResultInfo.ErrorDBQueryUser;
			return false;
		}
		
		if (mylist.size() < 1)
		{
			failInfo = FailSessionNotExist;
			return false;
		}
		
		Usersession usersession = (Usersession)mylist.get(0);
		if ((dateNow.getTime() - usersession.getLastTime().getTime()) > 1000 * SeesionTimeout)
		{
			failInfo = FailSessionTimeout;
			//Delete Session
			sessionDelete(token);
			return false;
		}
		
		usersession.setLastTime(dateNow);
			
		if (!DBSessionUtil.update(usersession))
		{
			/* FIXME */
			failInfo = ActionResultInfo.ErrorDBQueryUser;
			return false;
		}			
		
		return true;
	}
	public Integer SessionUserId(String token) {
		String sql = "select * from usersession where ";
		sql += "SessionToken='" + token + "'";
		
		@SuppressWarnings("unchecked")
		List<Usersession> mylist = DBSessionUtil.query(sql, Usersession.class);
		if (mylist == null)
		{
			/* FIXME */
			failInfo = ActionResultInfo.ErrorDBQueryUser;
			return -1;
		}
		
		if (mylist.size() < 1)
		{
			failInfo = FailSessionNotExist;
			return -1;
		}		
		Usersession usersession = (Usersession)mylist.get(0);
		return usersession.getUserId();
	}
	public String getFailInfo()
	{
		return failInfo;
	}
}