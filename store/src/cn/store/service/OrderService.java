package cn.store.service;

import cn.store.domain.Order;
import cn.store.domain.PageModel;
import cn.store.domain.User;
//订单管理service管理
public interface OrderService {

	void saveOrder(Order order)throws Exception;

	PageModel findMyOrdersWithPage(User user, int curNum)throws Exception;

	Order findOrderByOid(String oid)throws Exception;

	void updateOrder(Order order) throws Exception;

	PageModel findAllOrders(int curNum) throws Exception;

	PageModel findAllOrders(int curNum, String st) throws Exception;

}
