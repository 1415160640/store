package cn.store.web.base;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 公共servlet 解耦
 */
public class baseServlet extends HttpServlet {
	@Override
	public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = req.getParameter("method");
		if (null == method || "".equals(method) || method.trim().equals("")) {
			method = "execute";
		}
		// 子类对象字节码对象
		Class clazz = this.getClass();
		try {
			// 查找子类对象对应的字节码中的名称为method的方法.
			Method md = clazz.getMethod(method, HttpServletRequest.class, HttpServletResponse.class);
			if(null!=md){
				String jspPath = (String) md.invoke(this, req, resp);
				if (null != jspPath) {
					req.getRequestDispatcher(jspPath).forward(req, resp);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	// 默认方法
	public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		return null;
	}

}
