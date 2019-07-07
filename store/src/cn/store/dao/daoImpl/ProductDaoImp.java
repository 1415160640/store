package cn.store.dao.daoImpl;

import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.hibernate.Query;
import org.hibernate.Session;

import cn.store.dao.ProductDao;
import cn.store.domain.Category;
import cn.store.domain.Product;
import cn.store.domain.User;
import cn.store.utils.HibernateUtils;
import cn.store.utils.JDBCUtils;
//商品管理dao实现
public class ProductDaoImp implements ProductDao {

	@Override
	//根据商品编码查询商品信息
	public Product findProductByPid(String pid) throws Exception {
		Session session = HibernateUtils.openSession();
	    String hql = "from Product where pid=?";
		Query query = session.createQuery(hql);
		query.setParameter(0, pid);
        return (Product) query.uniqueResult();
	}

	@Override
	//查询热门商品
	public List<Product> findHots() throws Exception {
		Session session = HibernateUtils.openSession();
		//HQL查询语句中表名要使用对象的名称
		Query query = session.createQuery("from Product WHERE pflag=0 AND is_hot=1 ORDER BY pdate DESC");
		query.setFirstResult(0);
		query.setMaxResults(9);
		List<Product> list = query.list();
		session.close();
		return list;
	}

	@Override
	//查询最新商品
	public List<Product> findNews() throws Exception {
		Session session = HibernateUtils.openSession();
		//HQL查询语句中表名要使用对象的名称
		Query query = session.createQuery("from Product WHERE pflag=0 ORDER BY pdate DESC");
		query.setFirstResult(0);
		query.setMaxResults(9);
		List<Product> list = query.list();
		session.close();
		return list;
		
	}

	@Override
	//根据分类查询商品数量
	public int findTotalRecords(String cid) throws Exception {
		Session session = HibernateUtils.openSession();
		String hql = "select count(*) from Product where cid =?";
		Query query = session.createQuery(hql);
		query.setParameter(0, cid);
	    int count = ((Long) query.iterate().next()).intValue();
	    session.close();
	    return count;  
	}

	@Override
	//根据分类分页查询商品
	public List findProductsByCidWithPage(String cid, int startIndex, int pageSize) throws Exception {
		Session session = HibernateUtils.openSession();
	    Query query = session.createQuery("from Product where pflag!=1 and cid=?");   
	    query.setParameter(0, cid);
	    query.setMaxResults(pageSize);
	    query.setFirstResult(startIndex);
	    List<Product> list = query.list();
	    session.close();
	    return list;
	}

	@Override
	//分页查询所有商品
	public List<Product> findAllProductsWithPage(int startIndex, int pageSize) throws Exception{
		Session session = HibernateUtils.openSession();
	    Query query = session.createQuery("from Product where pflag!=1 order by pdate desc");   
	    query.setMaxResults(pageSize);
	    query.setFirstResult(startIndex);
	    List<Product> list = query.list();
	    session.close();
	    return list;
	}

	@Override
    //	分页查询所有商品数量
	public int findTotalRecords() throws Exception {
		Session session = HibernateUtils.openSession();
		String hql = "select count(*) from Product where pflag!=1 ";
		Query query = session.createQuery(hql);
	    int count = ((Long) query.iterate().next()).intValue();
	    session.close();
	    return count;  
	}

	@Override
	//添加或更新商品
	public void saveProduct(Product product) throws Exception {
		Session session = HibernateUtils.getCurrentSession();
		session.saveOrUpdate(product);
	}

	@Override
	//查询下架商品数量
	public int findOldTotalRecords() throws Exception {
		Session session = HibernateUtils.openSession();
		String hql = "select count(*) from Product where pflag=1";
		Query query = session.createQuery(hql);
	    int count = ((Long) query.iterate().next()).intValue();
	    session.close();
	    return count;  
	}

	@Override
	//分页查询已下架商品
	public List<Product> findOldProductsWithPage(int startIndex, int pageSize) throws Exception {
		String sql="select * from product where pflag=1 order by pdate desc limit  ? , ?";
		QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
		return qr.query(sql, new BeanListHandler<Product>(Product.class),startIndex,pageSize);
	}

	@Override
	//删除商品
	public void deleteProductDao(String pid) throws Exception {
		Session session = HibernateUtils.getCurrentSession();
		Product product = session.get(Product.class,pid);
		session.delete(product);
		
	}


}
