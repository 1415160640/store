package cn.store.dao;

import java.util.List;

import cn.store.domain.Product;
//商品管理dao接口
public interface ProductDao {

	List<Product> findHots()throws Exception;

	List<Product> findNews()throws Exception;

	Product findProductByPid(String pid)throws Exception;

	int findTotalRecords(String cid)throws Exception;

	List findProductsByCidWithPage(String cid, int startIndex, int pageSize)throws Exception;

	List<Product> findAllProductsWithPage(int startIndex, int pageSize)throws Exception;
	
	int findTotalRecords() throws Exception;

	void saveProduct(Product product) throws Exception;

	int findOldTotalRecords() throws Exception;

	List<Product> findOldProductsWithPage(int startIndex, int pageSize) throws Exception;

	void deleteProductDao(String pid) throws Exception;



}
