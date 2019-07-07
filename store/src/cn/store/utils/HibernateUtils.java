package cn.store.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
//创建hibernate连接对象
public class HibernateUtils {
	public static final Configuration cfg;
	public static final SessionFactory sf;
	static {
		cfg = new Configuration().configure();
		sf = cfg.buildSessionFactory();
	} 
	public static Session openSession() {
		return sf.openSession();
	}
	public static Session getCurrentSession() {
		  return sf.getCurrentSession();
	}
	

}
