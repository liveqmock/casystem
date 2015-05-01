package ca.core.logic;

import java.sql.*;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;

public class DBSessionUtil {
	private static AnnotationConfiguration config;
	private static SessionFactory sessionFactory;
	private static Object lockSession = new Object();     //初始化时用到
	
	public static Session getDBSession()
	{
		if (config == null)
		{
			synchronized(lockSession)
			{
				if (config == null) {
					config = new AnnotationConfiguration().configure();
					sessionFactory = config.buildSessionFactory();
				}
			}
		}
		Session session = sessionFactory.openSession();
		return session;
	}
	
	public static void closeDBSession(Session session)
	{
		if (session != null)
		{
			session.close();
		}
	}
	
	@SuppressWarnings("deprecation")
	public static boolean isExistTable(String strTable)
	{
		Session session = null;
		
		try {
			session = DBSessionUtil.getDBSession();
			Connection conn = session.connection();
			DatabaseMetaData metaData = conn.getMetaData();
			ResultSet rs = metaData.getTables(null, "cadb", strTable, new String[]{"TABLE"});
			if (rs.next())
			{
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
            return false;
		} finally {
            DBSessionUtil.closeDBSession(session);
        }
    }


	public static boolean update(String sql)
	{
		Session session = null;
		
		try
		{
			session = DBSessionUtil.getDBSession();
			Transaction tx = session.beginTransaction();
			SQLQuery query = session.createSQLQuery(sql);
			query.executeUpdate();
			tx.commit();
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		} finally {
            DBSessionUtil.closeDBSession(session);
        }
    }

	public static boolean update(Object object)
	{
		Session session = null;
		
		try
		{
			session = DBSessionUtil.getDBSession();
			Transaction tx = session.beginTransaction();
			session.update(object);
			tx.commit();
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		} finally {
            DBSessionUtil.closeDBSession(session);
        }
    }
	
	@SuppressWarnings("rawtypes")
	public static List query(String sql, Class clazz)
	{
		Session session = null;
		
		try
		{
			session = DBSessionUtil.getDBSession();
			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(clazz);
			List list = query.list();
			return list;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		} finally {
            DBSessionUtil.closeDBSession(session);
        }
    }

	public static boolean save(Object object)
	{
		Session session = null;
		
		try
		{
			session = DBSessionUtil.getDBSession();
			Transaction tx = session.beginTransaction();
			session.save(object);
			tx.commit();
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		} finally {
            DBSessionUtil.closeDBSession(session);
        }
    }
	
	public static Object getUniqueResult(String sql)
	{
		Session session = null;
		
		try
		{
			session = DBSessionUtil.getDBSession();
			SQLQuery query = session.createSQLQuery(sql);
			Object tmp = query.uniqueResult();
			return tmp;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		} finally {
            DBSessionUtil.closeDBSession(session);
        }
    }
    /**
     * 执行普通SQL查询,返回count/sum/min/max
     *  eg:select count(*) from table_name;
     */
    public static Long queryfun(final String sql) {
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        Session session=null;
        Long fun=null;
        try {
            session = DBSessionUtil.getDBSession();
            pStmt=session.connection().prepareStatement(sql);
            rs = pStmt.executeQuery();
            rs.next();
            fun=rs.getLong(1) ;
            rs.close();
            pStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            fun=null;
        } finally {
            DBSessionUtil.closeDBSession(session);
        }
        return fun;
    }
}
