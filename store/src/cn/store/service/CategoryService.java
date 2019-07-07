package cn.store.service;

import java.util.List;

import cn.store.domain.Category;
import cn.store.domain.PageModel;
//分类管理service接口
public interface CategoryService {

	List<Category> getAllCats () throws Exception;

	void addCategory(Category c) throws Exception;

	PageModel getAllCats(int curNum) throws Exception;

	void deleteCategory(String cid) throws Exception;

	Category getCategoryByCid(String cid) throws Exception;

	void updateCategory(Category c) throws Exception;

}
