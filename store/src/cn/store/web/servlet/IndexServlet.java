package cn.store.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.store.domain.Category;
import cn.store.domain.Product;
import cn.store.service.CategoryService;
import cn.store.service.ProductService;
import cn.store.service.serviceImp.CategoryServiceImp;
import cn.store.service.serviceImp.ProductServiceImp;
import cn.store.web.base.baseServlet;

//首页定位
public class IndexServlet extends baseServlet {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {	
		//查询最新商品,查询最热商品
		ProductService ProductService=new ProductServiceImp();
		List<Product> list01=ProductService.findHots();
		List<Product> list02=ProductService.findNews();
		request.setAttribute("hots", list01);
		request.setAttribute("news", list02);
		return "/jsp/index.jsp";
		
	}


}
