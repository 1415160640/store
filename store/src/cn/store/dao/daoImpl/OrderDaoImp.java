package cn.store.dao.daoImpl;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.hibernate.Query;
import org.hibernate.Session;

import cn.store.dao.OrderDao;
import cn.store.domain.Order;
import cn.store.domain.OrderItem;
import cn.store.domain.Product;
import cn.store.domain.User;
import cn.store.utils.HibernateUtils;
import cn.store.utils.JDBCUtils;
//订单管理dao实现
public class OrderDaoImp implements OrderDao {

	@Override
	//更新订单信息
	public void updateOrder(Order order) throws Exception {
		String sql="UPDATE orders SET ordertime=? ,total=? ,state= ?, address=?,NAME=?, telephone =? WHERE oid=?";
		QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
		Object[] params={order.getOrdertime(),order.getTotal(),order.getState(),order.getAddress(),order.getName(),order.getTelephone(),order.getOid()};
		qr.update(sql,params);
		
	}

	@Override
	public List findMyOrdersWithPage(User user, int startIndex, int pageSize) throws Exception {
		String sql="select * from orders where uid=? limit ? , ?";
		QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
		List<Order> list=qr.query(sql, new BeanListHandler<Order>(Order.class),user.getUid(),startIndex,pageSize);
		
		//遍历所有订单
		for (Order order : list) {
			//获取到每笔订单oid   查询每笔订单下的订单项以及订单项对应的商品信息
			String oid=order.getOid();
			sql="select * from orderItem o ,product p where o.pid=p.pid and oid=?";
			List<Map<String, Object>> list02 = qr.query(sql, new MapListHandler(),oid);
			//遍历list
			for (Map<String, Object> map : list02) {
				OrderItem orderItem=new OrderItem();
				Product product=new Product();
				// 由于BeanUtils将字符串"1992-3-3"向user对象的setBithday();方法传递参数有问题,手动向BeanUtils注册一个时间类型转换器
				// 1_创建时间类型的转换器
				DateConverter dt = new DateConverter();
				// 2_设置转换的格式
				dt.setPattern("yyyy-MM-dd");
				// 3_注册转换器
				ConvertUtils.register(dt, java.util.Date.class);
				
				//将map中属于orderItem的数据自动填充到orderItem对象上
				BeanUtils.populate(orderItem, map);
				//将map中属于product的数据自动填充到product对象上
				BeanUtils.populate(product, map);
				
				//让每个订单项和商品发生关联关系
				orderItem.setProduct(product);
				//将每个订单项存入订单下的集合中
				order.getList().add(orderItem);	
			}
		}
		return list;
	}

	@Override
	public Order findOrderByOid(String oid) throws Exception {
		String sql="select * from orders where oid= ?";
		QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
		Order order=qr.query(sql, new BeanHandler<Order>(Order.class),oid);
		
		//根据订单id查询订单下所有的订单项以及订单项对应的商品信息
		sql="select * from orderitem o, product p where o.pid=p.pid and oid=?";
		List<Map<String, Object>> list02 = qr.query(sql, new MapListHandler(),oid);
		//遍历list
		for (Map<String, Object> map : list02) {
			OrderItem orderItem=new OrderItem();
			Product product=new Product();
			// 由于BeanUtils将字符串"1992-3-3"向user对象的setBithday();方法传递参数有问题,手动向BeanUtils注册一个时间类型转换器
			// 1_创建时间类型的转换器
			DateConverter dt = new DateConverter();
			// 2_设置转换的格式
			dt.setPattern("yyyy-MM-dd");
			// 3_注册转换器
			ConvertUtils.register(dt, java.util.Date.class);
			
			//将map中属于orderItem的数据自动填充到orderItem对象上
			BeanUtils.populate(orderItem, map);
			//将map中属于product的数据自动填充到product对象上
			BeanUtils.populate(product, map);
			
			//让每个订单项和商品发生关联关系
			orderItem.setProduct(product);
			//将每个订单项存入订单下的集合中
			order.getList().add(orderItem);
		}
		return order;
	}

	@Override
	public int getTotalRecords(User user) throws Exception {
		String sql="select count(*) from orders where uid=?";
		QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
		Long num=(Long)qr.query(sql, new ScalarHandler(),user.getUid());
		return num.intValue();
	}

	@Override
	public void saveOrder(Connection conn, Order order) throws Exception {
		String sql="INSERT INTO orders VALUES(?,?,?,?,?,?,?,?)";
		QueryRunner qr=new QueryRunner();
		Object[] params={order.getOid(),order.getOrdertime(),order.getTotal(),order.getState(),order.getAddress(),order.getName(),order.getTelephone(),order.getUser().getUid()};
		qr.update(conn,sql,params);
	}

	@Override
	public void saveOrderItem(Connection conn, OrderItem item) throws Exception {
		String sql="INSERT INTO orderitem VALUES(?,?,?,?,?)";
		QueryRunner qr=new QueryRunner();
		Object[] params={item.getItemid(),item.getQuantity(),item.getTotal(),item.getProduct().getPid(),item.getOrder().getOid()};
		qr.update(conn,sql,params);
	}

	@Override
	//查询所有订单数
	public int findTotalRecords() throws Exception {
		String sql="select count(*) from orders";
		QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
		Long num=(Long)qr.query(sql, new ScalarHandler());
		return num.intValue();
//		Session session = HibernateUtils.openSession();
//		String hql = "select count(*) from Orders";
//		Query query = session.createQuery(hql);
//	    int count = ((Long) query.iterate().next()).intValue();
//	    session.close();
//	    return count;  
	}

	@Override
	//根据时间分页查询所有订单
	public List<Order> findAllProductsWithPage(int startIndex, int pageSize) throws Exception {
		String sql="select * from orders order by ordertime desc limit  ? , ?";
		QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
		return qr.query(sql, new BeanListHandler<Order>(Order.class),startIndex,pageSize);
//		Session session = HibernateUtils.openSession();
//	    Query query = session.createQuery("from Orders where order by ordertime desc");   
//	    query.setMaxResults(pageSize);
//	    query.setFirstResult(startIndex);
//	    List<Order> list = query.list();
//	    session.close();
//	    return list;
	}

	@Override
	//根据状态查询订单数量
	public int findTotalRecords(String st) throws Exception {
		String sql="select count(*) from orders where state=?";
		QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
		Long num=(Long)qr.query(sql, new ScalarHandler(),st);
		return num.intValue();
//		Session session = HibernateUtils.openSession();
//		String hql = "select count(*) from Orders where state=?";
//		Query query = session.createQuery(hql);
//		query.setParameter(0, st);
//	    int count = ((Long) query.iterate().next()).intValue();
//	    session.close();
//	    return count;  
	}

	@Override
	//根据订单状态分页查询所有订单
	public List<Order> findAllProductsWithPage(int startIndex, int pageSize, String st) throws Exception {
		String sql="select * from orders where state=? order by ordertime desc limit  ? , ?";
		QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
		return qr.query(sql, new BeanListHandler<Order>(Order.class),st,startIndex,pageSize);
//		Session session = HibernateUtils.openSession();
//	    Query query = session.createQuery("from Orders where state=? order by ordertime desc");   
//	    query.setParameter(0, st);
//	    query.setMaxResults(pageSize);
//	    query.setFirstResult(startIndex);
//	    List<Order> list = query.list();
//	    session.close();
//	    return list;
	}

}
