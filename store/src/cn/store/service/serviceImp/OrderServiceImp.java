package cn.store.service.serviceImp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import cn.store.dao.OrderDao;
import cn.store.dao.UserDao;
import cn.store.dao.daoImpl.OrderDaoImp;
import cn.store.domain.Category;
import cn.store.domain.Order;
import cn.store.domain.OrderItem;
import cn.store.domain.PageModel;
import cn.store.domain.User;
import cn.store.service.OrderService;
import cn.store.utils.BeanFactory;
import cn.store.utils.HibernateUtils;
import cn.store.utils.JDBCUtils;
//订单管理service实现
public class OrderServiceImp implements OrderService {
	
	OrderDao orderDao=(OrderDao)BeanFactory.createObject("OrderDao");
	//保存订单
	@Override
	public void saveOrder(Order order) throws SQLException {	
		Connection conn=null;
		try {
			//获取连接
			conn=JDBCUtils.getConnection();
			//开启事务
			conn.setAutoCommit(false);
			//保存订单
			
			orderDao.saveOrder(conn,order);
			//保存订单项
			for (OrderItem item : order.getList()) {
				orderDao.saveOrderItem(conn,item);	
			}
			//提交
			conn.commit();
		} catch (Exception e) {
			//回滚
			conn.rollback();
		}
	}
    //查询用户订单
	@Override
	public PageModel findMyOrdersWithPage(User user, int curNum) throws Exception {
		//1_创建PageModel对象,目的:计算并且携带分页参数
		//select count(*) from orders where uid=?
		int totalRecords=orderDao.getTotalRecords(user);
		PageModel pm=new PageModel(curNum, totalRecords, 5);
		//2_关联集合  select * from orders where uid=? limit ? ,?
		List list=orderDao.findMyOrdersWithPage(user,pm.getStartIndex(),pm.getPageSize());
		pm.setList(list);
		//3_关联url
		pm.setUrl("OrderServlet?method=findMyOrdersWithPage");
		return pm;
	}
   //根据oid查询订单
	@Override
	public Order findOrderByOid(String oid) throws Exception {
		return orderDao.findOrderByOid(oid);
		
	}

	@Override
	//更新订单
	public void updateOrder(Order order) throws Exception {
		 Session session = HibernateUtils.getCurrentSession();
		 Transaction transaction = session.beginTransaction();
			        try {
					   	orderDao.updateOrder(order);	
						transaction.commit();
					} catch (Exception e) {
						transaction.rollback();
						e.printStackTrace();
					}
	
	}
    //查询所有订单
	@Override
	public PageModel findAllOrders(int curNum) throws Exception {
		 //1_创建对象
		int totalRecords=orderDao.findTotalRecords();
		PageModel pm=new PageModel(curNum,totalRecords,8);
		//2_关联集合 select * from product limit ? , ?
		List<Order> list=orderDao.findAllProductsWithPage(pm.getStartIndex(),pm.getPageSize());
		pm.setList(list);
		//3_关联url
		pm.setUrl("OrderServlet?method=findOrders");
		return pm;
	
	}
    //根据状态查询订单
	@Override
	public PageModel findAllOrders(int curNum, String st) throws Exception {
		//1_创建对象
		int totalRecords=orderDao.findTotalRecords(st);
		PageModel pm=new PageModel(curNum,totalRecords,8);
		//2_关联集合 select * from product limit ? , ?
		List<Order> list=orderDao.findAllProductsWithPage(pm.getStartIndex(),pm.getPageSize(),st);
		pm.setList(list);
		//3_关联url
		pm.setUrl("OrderServlet?method=findOrders");
		return pm;
	}
}