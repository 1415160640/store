package cn.store.dao;

import java.util.List;

import cn.store.domain.Category;
import cn.store.domain.Product;
//商品管理dao接口
public interface CategoryDao {

	List<Category> getAllCats() throws Exception;
	void addCategory(Category c)throws Exception;
	int findTotalRecords() throws Exception;
	List<Category> findAllProductsWithPage(int startIndex, int pageSize) throws Exception ;
	void deleteCategory(String cid) throws Exception;
	Category getCategoryByCid(String cid) throws Exception;
	//void updateCategory(Category c) throws Exception;


}
