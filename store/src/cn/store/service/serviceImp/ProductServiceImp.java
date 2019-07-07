package cn.store.service.serviceImp;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import cn.store.dao.ProductDao;
import cn.store.dao.daoImpl.ProductDaoImp;
import cn.store.domain.PageModel;
import cn.store.domain.Product;
import cn.store.service.ProductService;
import cn.store.utils.BeanFactory;
import cn.store.utils.HibernateUtils;
import cn.store.utils.JedisUtils;
import redis.clients.jedis.Jedis;
//商品管理service实现
public class ProductServiceImp implements ProductService {

	ProductDao ProductDao=(ProductDao)BeanFactory.createObject("ProductDao");
	//根据pid查询商品
	@Override
	public Product findProductByPid(String pid) throws Exception {
		return ProductDao.findProductByPid(pid);
		
	}
   //查询热门商品
	@Override
	public List<Product> findHots() throws Exception {
		return ProductDao.findHots();
	}
    //查询最新商品
	@Override
	public List<Product> findNews() throws Exception {
		return ProductDao.findNews();
	}
    //分类查询商品
	@Override
	public PageModel findProductsByCidWithPage(String cid, int curNum) throws Exception {
					//1_创建PageModel对象 目的:计算分页参数
					//统计当前分类下商品个数  select count(*) from product where cid=?
					int totalRecords=ProductDao.findTotalRecords(cid);
					PageModel pm=new PageModel(curNum,totalRecords,18);
					//2_关联集合 select * from product where cid =? limit ? ,?
					List list=ProductDao.findProductsByCidWithPage(cid,pm.getStartIndex(),pm.getPageSize());
					pm.setList(list);
					//3_关联url
					pm.setUrl("ProductServlet?method=findProductsByCidWithPage&cid="+cid);
					return pm;
	}
    //查询所有商品
	@Override
	public PageModel findAllProductsWithPage(int curNum) throws Exception {
		       //1_创建对象
				int totalRecords=ProductDao.findTotalRecords();
				PageModel pm=new PageModel(curNum,totalRecords,8);
				//2_关联集合 select * from product limit ? , ?
				List<Product> list=ProductDao.findAllProductsWithPage(pm.getStartIndex(),pm.getPageSize());
				pm.setList(list);
				//3_关联url
				pm.setUrl("ProductServlet?method=findAllProductsWithPage");
				return pm;
	}
	//查询下架商品
    @Override
	public PageModel findOldProductsWithPage(int curNum) throws Exception {
    	//1_创建对象
		int totalRecords=ProductDao.findOldTotalRecords();
		PageModel pm=new PageModel(curNum,totalRecords,8);
		//2_关联集合 select * from product limit ? , ?
		List<Product> list=ProductDao.findOldProductsWithPage(pm.getStartIndex(),pm.getPageSize());
		pm.setList(list);
		//3_关联url
		pm.setUrl("ProductServlet?method=findOldProductsWithPage");
		return pm;
	}

	@Override
	//添加商品
	public void saveProduct(Product product) throws Exception {
		 
		 Session session = HibernateUtils.getCurrentSession();
		 Transaction transaction = session.beginTransaction();
			        try {
					    ProductDao.saveProduct(product);  
						transaction.commit();
					} catch (Exception e) {
						transaction.rollback();
						e.printStackTrace();
					}
	}

	@Override
	//编辑商品
	public void editProduct(Product product) throws Exception {
		Session session = HibernateUtils.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		 try {
			 	ProductDao.saveProduct(product);	
				transaction.commit();
			} catch (Exception e) {
				transaction.rollback();
				e.printStackTrace();
			}	
		
	}

	@Override
	//删除商品
	public void deleteProduct(String pid) throws Exception {
		Session session = HibernateUtils.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		 try {
			 	ProductDao.deleteProductDao(pid);
				transaction.commit();
			} catch (Exception e) {
				transaction.rollback();
				e.printStackTrace();
			}	
		
	}


	
}
