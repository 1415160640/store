package cn.store.web.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.store.domain.Cart;
import cn.store.domain.CartItem;
import cn.store.domain.Order;
import cn.store.domain.OrderItem;
import cn.store.domain.PageModel;
import cn.store.domain.User;
import cn.store.service.OrderService;
import cn.store.service.serviceImp.OrderServiceImp;
import cn.store.utils.UUIDUtils;
import cn.store.web.base.baseServlet;
import net.sf.json.JSONArray;

/**
 * 订单Servlet
 */
public class OrderServlet extends baseServlet {
	   // saveOrder  将购物车中的信息以订单的形式保存
		public String saveOrder(HttpServletRequest req, HttpServletResponse resp) throws Exception {
			//确认用户登录状态
			User user=(User)req.getSession().getAttribute("loginUser");
			if(null==user){
				req.setAttribute("msg", "请登录之后再下单");
				return "/jsp/login.jsp";
			}
			//获取购物车
			Cart cart=(Cart)req.getSession().getAttribute("cart");
			//创建订单对象,为订单对象赋值
			Order order=new Order();
			order.setOid(UUIDUtils.getCode());
			order.setOrdertime(new Date());
			order.setTotal(cart.getTotal());
			order.setState(1);
			order.setUser(user);
			//遍历购物项的同时,创建订单项,为订单项赋值
			for (CartItem item : cart.getCartItems()) {
				OrderItem orderItem=new OrderItem();
				orderItem.setItemid(UUIDUtils.getCode());
				orderItem.setQuantity(item.getNum());
				orderItem.setTotal(item.getSubTotal());
				orderItem.setProduct(item.getProduct());
				//设置当前的订单项属于哪个订单:程序的角度体检订单项和订单对应关系
				orderItem.setOrder(order);
				order.getList().add(orderItem);
			}
			//调用业务层功能:保存订单
			OrderService OrderService=new OrderServiceImp();
			//将订单数据,用户的数据,订单下所有的订单项都传递到了service层
			OrderService.saveOrder(order);
			//清空购物车
			cart.clearCart();
			//将订单放入request
			req.setAttribute("order", order);
			return "/jsp/order_info.jsp";
		}
		
		//用户查询个人订单
		public String findMyOrdersWithPage(HttpServletRequest req, HttpServletResponse resp) throws Exception {
			//获取用户信息
			User user=(User)req.getSession().getAttribute("loginUser");
			if(user == null){
				req.setAttribute("msg", "请先登入再执行操作");
				return "/jsp/login.jsp";
			}
			//获取当前页
			int curNum=Integer.parseInt(req.getParameter("num"));
			//调用业务层功能:查询当前用户订单信息,返回PageModel
			OrderService OrderService=new OrderServiceImp();
			//当前用户的当前页的订单(集合) ,每笔订单上对应的订单项,以及订单项对应的商品信息
			PageModel pm=OrderService.findMyOrdersWithPage(user,curNum);
			//将PageModel放入request
			req.setAttribute("page", pm);
			return "/jsp/order_list.jsp";
			
		}
		
		//根据订单编号查询商品
		public String findOrderByOid(HttpServletRequest req, HttpServletResponse resp) throws Exception {
			//获取到订单oid
			String oid=req.getParameter("oid");
			//调用业务层功能:根据订单编号查询订单信息
			OrderService OrderService=new OrderServiceImp();
			Order order=OrderService.findOrderByOid(oid);
			// 将订单放入request
			req.setAttribute("order", order);
			return "/jsp/order_info.jsp";
		}	
		//用户支付
		public String payOrder(HttpServletRequest req, HttpServletResponse resp) throws Exception {
			//获取订单oid,收货人地址,姓名,电话,银行
			String oid=req.getParameter("oid");
			String address=req.getParameter("address");
			String name=req.getParameter("name");
			String telephone=req.getParameter("telephone");
			//更新订单上收货人的地址,姓名,电话
			OrderService OrderService=new OrderServiceImp();
			Order order=OrderService.findOrderByOid(oid);
			order.setName(name);
			order.setTelephone(telephone);
			order.setAddress(address);
			order.setState(2);
			OrderService.updateOrder(order);
			req.setAttribute("msg", "支付成功");
			return "/jsp/info.jsp";
		}
		//更新订单状态
		public String updateOrderByOid(HttpServletRequest req, HttpServletResponse resp) throws Exception {
			//获取订单ID
			String oid=req.getParameter("oid");
			//根据订单ID查询订单
			OrderService OrderService=new OrderServiceImp();
			Order order=OrderService.findOrderByOid(oid);
			//设置订单状态
			order.setState(4);
			//修改订单信息
			OrderService.updateOrder(order);
			//重新定向到查询已发货订单
			resp.sendRedirect("/store/OrderServlet?method=findMyOrdersWithPage&num=1");
			return null;
		}	
		//根据状态查询订单
		public String findOrders(HttpServletRequest req, HttpServletResponse resp) throws Exception {
			//获取当前页
			int curNum=Integer.parseInt(req.getParameter("num"));
			OrderService OrderService=new OrderServiceImp();
			PageModel pm=null;
			String st=req.getParameter("state");
			if(null==st||"".equals(st)){
				//获取到全部订单
				pm=OrderService.findAllOrders(curNum);			
			}else{
				pm=OrderService.findAllOrders(curNum,st);
			}
			pm.setState(st);
			req.setAttribute("page", pm);
			return "/admin/order/list.jsp";
		}
		//异步查询订单详情
		public String findOrderAjax(HttpServletRequest req, HttpServletResponse resp) throws Exception {
			//服务端获取到订单ID,
			String oid=req.getParameter("id");
			//查询这个订单下所有的订单项以及订单项对应的商品信息,返回集合
			OrderService OrderService=new OrderServiceImp();
			Order order=OrderService.findOrderByOid(oid);
			//将返回的集合转换为JSON格式字符串,响应到客户端
			String jsonStr=JSONArray.fromObject(order.getList()).toString();
			//响应到客户端
			resp.setContentType("application/json;charset=utf-8");
			resp.getWriter().println(jsonStr);
			return null;
		}
		
		//更新订单状态
		public String updateOrderByOidAll(HttpServletRequest req, HttpServletResponse resp) throws Exception {
			//获取订单ID
			String oid=req.getParameter("oid");
			//根据订单ID查询订单
			OrderService OrderService=new OrderServiceImp();
			Order order=OrderService.findOrderByOid(oid);
			//设置订单状态
			order.setState(3);
			//修改订单信息
			OrderService.updateOrder(order);
			//重新定向到查询已发货订单
			resp.sendRedirect("/store/OrderServlet?method=findOrders&state=3&num=1");
			return null;
		}	

}
