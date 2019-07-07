package cn.store.dao.daoImpl;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import cn.store.dao.UserDao;
import cn.store.domain.Admin;
import cn.store.domain.User;
import cn.store.utils.HibernateUtils;
import cn.store.utils.JDBCUtils;
//用户管理dao实现
public class UserDaoImp implements UserDao{

	@Override
	//用户注册功能
	public void userRegist(User user) throws SQLException {
		Session session = HibernateUtils.getCurrentSession();
		session.saveOrUpdate(user);
	}

	@Override
	//根据激活码查询是否存在该用户
	public User userActive(String code) throws SQLException {
		Session session = HibernateUtils.openSession();
	    String hql = " from User where code = ?";
		Query query = session.createQuery(hql);
		query.setParameter(0, code);
        return (User) query.uniqueResult();
	}

	@Override
	//更新用户信息
	public void userUpdate(User user) throws SQLException {
		Session session = HibernateUtils.getCurrentSession();
		session.saveOrUpdate(user);
	}
	
	@Override
	//用户登入
	public User userLogin(User user) throws SQLException {
		Session session = HibernateUtils.openSession();
	    String hql = " from User where username = :username and password= :password";
		Query query = session.createQuery(hql);
		query.setProperties(user);
        return (User) query.uniqueResult();
	}

	@Override
	//管理员登入
	public Admin AdminUserLogin(Admin user) throws SQLException {
		Session session = HibernateUtils.openSession();
	    String hql = " from Admin where username = :username and password= :password";
		Query query = session.createQuery(hql);
		query.setProperties(user);
        return (Admin) query.uniqueResult();
	}
    
	@Override
	//异步查询账户是否存在
	public boolean checkUserName(String username) throws SQLException {
		Session session = HibernateUtils.openSession();
		String hql = "select count(*) from User where username = ?";
		Query query = session.createQuery(hql);
		//hibernnate的占位符从0开始
		query.setParameter(0, username);
	    int count = ((Long) query.iterate().next()).intValue();
	    return count > 0;  
	}

}
