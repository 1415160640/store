package cn.store.service.serviceImp;

import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.Transaction;

import cn.store.dao.UserDao;
import cn.store.dao.daoImpl.UserDaoImp;
import cn.store.domain.Admin;
import cn.store.domain.User;
import cn.store.service.UserService;
import cn.store.utils.BeanFactory;
import cn.store.utils.HibernateUtils;
//用户管理service实现
public class UserServiceImp implements UserService{
	
	UserDao userdao=(UserDao)BeanFactory.createObject("UserDao");
	//用户注册
	@Override
	public void userRegist(User user) throws SQLException {
		//实现注册功能
		Session session = HibernateUtils.getCurrentSession();
		Transaction transaction = session.beginTransaction();
	    userdao.userRegist(user);
	    transaction.commit();
		
	}
    //用户激活
	@Override
	public boolean userActive(String code) throws SQLException {	
	    User user=userdao.userActive(code);
	    if(null!=user){
	    	//存在该用户，修改该用户的状态，清除激活码
	    	Session session = HibernateUtils.getCurrentSession();
	    	Transaction transaction = session.beginTransaction();
	    	user.setState(1);
	    	user.setCode(null);
	    	userdao.userUpdate(user);
	    	transaction.commit();
	    	return true;
	    }else {
	    	return false;
	    }
		
	}
    //用户登入
	@Override
	public User userLogin(User user) throws SQLException {
		User uu = userdao.userLogin(user);
		if(uu == null) {
			throw new RuntimeException("密码有误！");
		}else if(uu.getState() == 0) {
			throw new RuntimeException("用户未激活！");
		}else {
			return uu;
		}
	}
    //管理员登入
	@Override
	public Admin AdminUserLogin(Admin user) throws SQLException {
		Admin uu = userdao.AdminUserLogin(user);
		if(uu == null) {
			throw new RuntimeException("登入失败！");
		}else {
			return uu;
		}
	}
    //查询用户名是否存在
	@Override
	public boolean checkUserName(String username) throws SQLException {
		return userdao.checkUserName(username);
	}

}
