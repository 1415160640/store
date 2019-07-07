package cn.store.service.serviceImp;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import cn.store.dao.CategoryDao;
import cn.store.dao.daoImpl.CategoryDaoImp;
import cn.store.domain.Category;
import cn.store.domain.PageModel;
import cn.store.domain.Product;
import cn.store.service.CategoryService;
import cn.store.utils.BeanFactory;
import cn.store.utils.HibernateUtils;
import cn.store.utils.JedisUtils;
import redis.clients.jedis.Jedis;
//商品分类管理service实现
public class CategoryServiceImp implements CategoryService {
	
	CategoryDao CategoryDao=(CategoryDao)BeanFactory.createObject("CategoryDao");
	//获取所有分类
	@Override
	public List<Category> getAllCats() throws Exception {
		return CategoryDao.getAllCats();
	}
    //添加分类
	@Override
	public void addCategory(Category c){
		Session session = HibernateUtils.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		        try {
					//本质是向MYSQL插入一条数据
					CategoryDao.addCategory(c);
					//更新redis缓存
					Jedis jedis = JedisUtils.getJedis();
					jedis.del("allCats");
					JedisUtils.closeJedis(jedis);
					transaction.commit();
				} catch (Exception e) {
					transaction.rollback();
					e.printStackTrace();
				}
		
	}
    //分页查询所有分类
	@Override
	public PageModel getAllCats(int curNum) throws Exception {
		//1_创建对象
		int totalRecords=CategoryDao.findTotalRecords();
		PageModel pm=new PageModel(curNum,totalRecords,8);
		//2_关联集合 select * from product limit ? , ?
		List<Category> list=CategoryDao.findAllProductsWithPage(pm.getStartIndex(),pm.getPageSize());
		pm.setList(list);
		//3_关联url
		pm.setUrl("AdminCategoryServlet?method=findAllCats");
		return pm;
	}
    //删除分类
	@Override
	public void deleteCategory(String cid) {
		Session session = HibernateUtils.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		try {
			
			CategoryDao.deleteCategory(cid);
			//更新redis缓存
			Jedis jedis = JedisUtils.getJedis();
			jedis.del("allCats");
			JedisUtils.closeJedis(jedis);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
	}
    //根据cid查询分类
	@Override
	public Category getCategoryByCid(String cid) throws Exception {
		return CategoryDao.getCategoryByCid(cid);
	}
    //更新分类
	@Override
	public void updateCategory(Category c){
		Session session = HibernateUtils.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		
		try {
			CategoryDao.addCategory(c);
			//更新redis缓存
			Jedis jedis = JedisUtils.getJedis();
			jedis.del("allCats");
			JedisUtils.closeJedis(jedis);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		
	}

}
