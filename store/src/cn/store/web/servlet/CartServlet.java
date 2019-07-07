package cn.store.web.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.store.domain.Cart;
import cn.store.domain.CartItem;
import cn.store.domain.Product;
import cn.store.service.ProductService;
import cn.store.service.serviceImp.ProductServiceImp;
import cn.store.web.base.baseServlet;

/**
 * 购物车servlet
 */
public class CartServlet extends baseServlet {
	   //添加购物项到购物车
		public String addCartItemToCart(HttpServletRequest req, HttpServletResponse resp) throws Exception {
			//从session获取购物车
			Cart cart=(Cart)req.getSession().getAttribute("cart");
			if(null==cart){
				cart=new Cart();
				req.getSession().setAttribute("cart", cart);
			}
			//获取到商品id,数量
			String pid=req.getParameter("pid");
			int num=Integer.parseInt(req.getParameter("quantity"));
			//通过商品id查询都商品对象
			ProductService ProductService=new ProductServiceImp();
			Product product=ProductService.findProductByPid(pid);	
			//获取到待购买的购物项
			CartItem cartItem=new CartItem();
			cartItem.setNum(num);
			cartItem.setProduct(product);
			//调用购物车上的方法
			cart.addCartItemToCar(cartItem);
			resp.sendRedirect("/store/jsp/cart.jsp");
			return  null;
		}
		
		//移除购物项
		public String removeCartItem(HttpServletRequest req, HttpServletResponse resp) throws Exception {
			String pid=req.getParameter("id");
			//获取到购物车
			Cart cart=(Cart)req.getSession().getAttribute("cart");
			cart.removeCartItem(pid);
			resp.sendRedirect("/store/jsp/cart.jsp");
			return null;
		}
		//清空购物车
		public String clearCart(HttpServletRequest req, HttpServletResponse resp) throws Exception {
			//获取购物车
			Cart cart=(Cart)req.getSession().getAttribute("cart");
			cart.clearCart();
			resp.sendRedirect("/store/jsp/cart.jsp");
			return null;
		}

}
