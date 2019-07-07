package cn.store.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;

import cn.store.domain.Admin;
import cn.store.domain.User;
import cn.store.service.UserService;
import cn.store.service.serviceImp.UserServiceImp;
import cn.store.utils.MailUtils;
import cn.store.utils.MyBeanUtils;
import cn.store.utils.UUIDUtils;
import cn.store.web.base.baseServlet;

/**
 *用户servlet
 */
public class UserServlet extends baseServlet {
	   //跳转注册界面
		public String registUI(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			return "/jsp/register.jsp";
		}
		//跳转到登入界面
		public String loginUI(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			return "/jsp/login.jsp";
		}
		//实现注册功能
		public String userRegist(HttpServletRequest request, HttpServletResponse response) throws Exception {
		        	// 接收表单参数
					Map<String, String[]> map = request.getParameterMap();
					User user = new User();		
					MyBeanUtils.populate(user, map);
					// 为用户的其他属性赋值
					user.setUid(UUIDUtils.getId());
					user.setState(0);
					user.setCode(UUIDUtils.getCode());
					// 调用业务层注册功能
					UserService UserService = new UserServiceImp();
					try {
    					UserService.userRegist(user);
						// 发送邮件
						MailUtils.sendMail(user.getEmail(), user.getCode());
						request.setAttribute("msg", "用户注册成功,请激活!");
					} catch (Exception e) {
						request.setAttribute("msg", "用户注册失败,请重新注册!");

					}
					return "/jsp/info.jsp";
		  }
		   //获取激活码，调用业务层的激活功能,并且给出提示
			public String active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		         String code = request.getParameter("code");
		         UserService userservice = new UserServiceImp();
		         boolean flag = userservice.userActive(code);
		         if(flag==true) {
		        	 request.setAttribute("msg", "用户激活成功，请登入！");
		        	 return "/jsp/login.jsp";
		         }else {
		        	 request.setAttribute("msg", "用户激活失败，请重新激活！");
		        	 return "/jsp/info.jsp";
		         }
				
			}
			//登入
			public String userLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
				User user = new User();
				MyBeanUtils.populate(user, request.getParameterMap());
				UserService userservice = new UserServiceImp();
				User user02 = null;
				try {
					//返回用户的所有信息
					user02 = userservice.userLogin(user);
					//将用户信息放入session
					request.getSession().setAttribute("loginUser", user02);
					response.sendRedirect("/store/index.jsp");
					return null;
				} catch (Exception e) {
					//用户登入失败
					String msg = e.getMessage();
					request.setAttribute("msg", msg);
					return "/jsp/login.jsp";
				}
				
			}
			//退出用户登入
			public String logOut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
			  //清除session
			  request.getSession().invalidate();
			  response.sendRedirect("/store/index.jsp");
			  return null;
			}
			//检查用用户名是否重复
			public void CheckUserName(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		        try {
					request.setCharacterEncoding("UTF-8");
					String username = request.getParameter("username");
					UserService userservice = new UserServiceImp();
					boolean isExist = userservice.checkUserName(username);
					//2.  通知页面，到底有还是没有。
					if(isExist){
						response.getWriter().println(1); 
					}else{
						response.getWriter().println(2);
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			//管理员登入
			public String AdminLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		        //获取用户数据
				Admin user = new Admin();
				MyBeanUtils.populate(user, request.getParameterMap());
				UserService userservice = new UserServiceImp();
				Admin user02 = null;
				try {
					user02 = userservice.AdminUserLogin(user);
					//将用户信息放入session
					request.getSession().setAttribute("AdminloginUser", user02);
					response.sendRedirect("/store/admin/home.jsp");
					return null;
				} catch (Exception e) {
					//登入失败
					String msg = e.getMessage();
					request.setAttribute("msg", msg);
					return "/admin/index.jsp";
				}
				
			}
			//管理员退出登入
			public String AdminlogOut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
			  //清除session
			  request.getSession().invalidate();
			  return "/admin/index.jsp";
			} 
			
}
