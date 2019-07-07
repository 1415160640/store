package cn.store.dao.daoImpl;

import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import cn.store.dao.CategoryDao;
import cn.store.domain.Category;
import cn.store.domain.Product;
import cn.store.utils.HibernateUtils;
import cn.store.utils.JDBCUtils;

//商品分类管理dao实现
public class CategoryDaoImp implements CategoryDao {

	@Override
	//查询所有分类信息
	public List<Category> getAllCats() throws Exception {
		 Session session = HibernateUtils.openSession();
		 //HQL查询语句中表名要使用对象的名称
		 Query query = session.createQuery("from Category");
		 List<Category> list = query.list();
		 session.close();
		 return list;
		
	}

	@Override
	//添加分类信息
	public void addCategory(Category c) throws Exception {
		Session session = HibernateUtils.getCurrentSession();
		session.saveOrUpdate(c);
	}

	@Override
	//计算分类信息的总数
	public int findTotalRecords() throws Exception {
		Session session = HibernateUtils.openSession();
		String hql = "select count(*) from Category";
		Query query = session.createQuery(hql);
	    int count = ((Long) query.iterate().next()).intValue();
	    session.close();
	    return count;  
	}

	@Override
	//分页查询分类信息
	public List<Category> findAllProductsWithPage(int startIndex, int pageSize) throws Exception {
		Session session = HibernateUtils.openSession();
	    Query query = session.createQuery("from Category");   
	    query.setMaxResults(pageSize);
	    query.setFirstResult(startIndex);
	    List<Category> list = query.list();
	    session.close();
	    return list;
	}

	@Override
	//删除分类信息
	public void deleteCategory(String cid) throws Exception {
		Session session = HibernateUtils.getCurrentSession();
		Category cate = session.get(Category.class,cid);
		session.delete(cate); 
	}

	@Override
	//根据cid查询分类信息
	public Category getCategoryByCid(String cid) throws Exception {
		Session session = HibernateUtils.openSession();
		Category cate = session.get(Category.class,cid);
		session.close();
		return cate;
	}


}
