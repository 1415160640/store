package cn.store.service;

import java.util.List;

import cn.store.domain.PageModel;
import cn.store.domain.Product;
//商品管理service接口
public interface ProductService {

	List<Product> findHots()throws Exception;

	List<Product> findNews()throws Exception;

	Product findProductByPid(String pid)throws Exception;

	PageModel findProductsByCidWithPage(String cid, int curNum)throws Exception;

	PageModel findAllProductsWithPage(int curNum) throws Exception;

	void saveProduct(Product product)  throws Exception;

	PageModel findOldProductsWithPage(int curNum) throws Exception;

//	void updateProduct(Product product) throws Exception;

	void editProduct(Product product) throws Exception;

	void deleteProduct(String pid) throws Exception;

}
