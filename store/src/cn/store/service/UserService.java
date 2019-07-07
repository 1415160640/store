package cn.store.service;

import java.sql.SQLException;

import cn.store.domain.Admin;
import cn.store.domain.User;
//用户管理service接口
public interface UserService {

	void userRegist(User user) throws SQLException;

	boolean userActive(String code) throws SQLException;

	User userLogin(User user) throws SQLException;

	Admin AdminUserLogin(Admin user) throws SQLException;

	boolean checkUserName(String username) throws SQLException;

}
