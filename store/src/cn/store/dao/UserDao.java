package cn.store.dao;

import java.sql.SQLException;

import cn.store.domain.Admin;
import cn.store.domain.User;
//用户管理dao接口
public interface UserDao {

	void userRegist(User user) throws SQLException;

	User userActive(String code) throws SQLException;

	void userUpdate(User user) throws SQLException;

	User userLogin(User user) throws SQLException;

	Admin AdminUserLogin(Admin user) throws SQLException;

	boolean checkUserName(String username) throws SQLException;

}
