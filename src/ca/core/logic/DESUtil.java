package ca.core.logic;

public class DESUtil
{	
	public static String fadeInDES(String key, String desStr)
	{
		try
		{
			DESEncrypt des = new DESEncrypt(key);
			return des.encrypt(desStr);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "";
		}
	}
	
	public static String fadeOutDES(String key, String undesStr)
	{
		try
		{
			DESEncrypt des = new DESEncrypt(key);
			return des.decrypt(undesStr);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "";
		}
	}
}  



