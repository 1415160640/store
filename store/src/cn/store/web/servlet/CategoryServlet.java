package cn.store.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.store.domain.Category;
import cn.store.domain.PageModel;
import cn.store.service.CategoryService;
import cn.store.service.serviceImp.CategoryServiceImp;
import cn.store.utils.JedisUtils;
import cn.store.utils.UUIDUtils;
import cn.store.web.base.baseServlet;
import net.sf.json.JSONArray;
import redis.clients.jedis.Jedis;

/**
 *分类servlet
 */
public class CategoryServlet extends baseServlet {
	//客户端查询所有分了信息
	public void findAllCat(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//在redis中获取全部分类信息
		Jedis jedis = JedisUtils.getJedis();
		String jsonStr = jedis.get("allCats");
		if(null == jsonStr || "".equals(jsonStr)) {
		   CategoryService category = new CategoryServiceImp();
		   List<Category> list = category.getAllCats();
		   //将全部分类装化成json格式响应回界面
		   jsonStr = JSONArray.fromObject(list).toString();
		    //json格式数据缓存到redis中
		   jedis.set("allCats", jsonStr);
		    //告诉浏览器本次响应的数据是JSON格式的字符串
		   response.setContentType("application/json;Charset=utf-8");
		   response.getWriter().print(jsonStr);
		}else {
			response.setContentType("application/json;Charset=utf-8");
		    response.getWriter().print(jsonStr);
		}
		
		JedisUtils.closeJedis(jedis);
	}
	 //管理端查询所有分类
	public String findAllCats(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		//获取当前页
		int curNum=Integer.parseInt(req.getParameter("num"));
		CategoryService CategoryService=new CategoryServiceImp();
		PageModel pm=CategoryService.getAllCats(curNum);
		req.setAttribute("page", pm);
		return "/admin/category/list.jsp";
		
	}
	
	//跳转到添加分类界面
	public String addCategoryUI(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		return "/admin/category/add.jsp";
	}

	//添加分类方法
	public String addCategory(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		//获取分类名称
		String cname=req.getParameter("cname");
		String id=UUIDUtils.getId();
		Category c=new Category();
		c.setCid(id);
		c.setCname(cname);
		CategoryService CategoryService=new CategoryServiceImp();
		CategoryService.addCategory(c);
		resp.sendRedirect("/store/CategoryServlet?method=findAllCats&num=1");
		return null;
	}
	//删除分类
	public String deleteCategory(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String cid=req.getParameter("cid");
		CategoryService CategoryService=new CategoryServiceImp();
		try {
			CategoryService.deleteCategory(cid);
			req.setAttribute("msg", "删除成功!");
		} catch (Exception e) {
			req.setAttribute("msg", "删除失败，请重新选择分类!");
		}
		return "/admin/info.jsp";
	}
	//跳转到编辑界面
	public String editCategoryUI(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String cid=req.getParameter("cid");
		CategoryService CategoryService=new CategoryServiceImp();
		Category category=CategoryService.getCategoryByCid(cid);
		req.setAttribute("category", category);
		return "/admin/category/edit.jsp";
	}
	//编辑分类信息方法
	public String editCategory(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		//获取分类名称
		String cid=req.getParameter("cid");
		String cname=req.getParameter("cname");
		Category c=new Category();
		c.setCid(cid);
		c.setCname(cname);
		//调用业务层添加分类功能
		CategoryService CategoryService=new CategoryServiceImp();
		try {
			CategoryService.updateCategory(c);
			req.setAttribute("msg", "修改成功!");
		} catch (Exception e) {
			req.setAttribute("msg", "修改失败，请重新选择分类!");
		}
		return "/admin/info.jsp";
	}


}
