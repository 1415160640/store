package cn.store.web.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import cn.store.domain.Category;
import cn.store.domain.PageModel;
import cn.store.domain.Product;
import cn.store.service.CategoryService;
import cn.store.service.ProductService;
import cn.store.service.serviceImp.CategoryServiceImp;
import cn.store.service.serviceImp.ProductServiceImp;
import cn.store.utils.CookUtils;
import cn.store.utils.CookieUtil;
import cn.store.utils.UUIDUtils;
import cn.store.utils.UploadUtils;
import cn.store.web.base.baseServlet;


/**
 * 商品Servlet
 */
public class ProductServlet extends baseServlet {
	//跳转到首页
	public String indexUI(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return "/jsp/index.jsp";
	}
	//查询商品详情
	public String findProductByPid(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String pid = request.getParameter("pid");
		ProductService ProductService = new ProductServiceImp();
		Product product = ProductService.findProductByPid(pid);
		request.setAttribute("product", product);
		Cookie[] cookies = request.getCookies();
		Cookie cookie = CookieUtil.findCookie(cookies, "history");
		String ss = pid+"@"+product.getPimage();
		String id3 = ss;
		if (cookie==null) {
				Cookie cook = new Cookie("history", ss);
				//设置有效期
				cook.setMaxAge(60*60*24*7);
				response.addCookie(cook);
		}else{      
			        //记录的添加和重复记录的去除
					String id2 = cookie.getValue();
					String []  product1 = id2.split("#");
					for(String i : product1) {
						if(!i.equals(ss)) {
							id3=id3+"#"+i;
						}
					}
					cookie.setValue(id3);
					cookie.setMaxAge(60*60*24*7);
					response.addCookie(cookie);
		}
		return "/jsp/product_info.jsp";
	}
    //用户分类查询商品
	public String findProductsByCidWithPage(HttpServletRequest request, HttpServletResponse response) throws Exception {	
		String cid=request.getParameter("cid");
		int curNum=Integer.parseInt(request.getParameter("num"));
		//以分页形式查询当前类别下商品信息
		//返回PageModel对象
		ProductService ProductService=new ProductServiceImp();
		PageModel pm=ProductService.findProductsByCidWithPage(cid,curNum);
		request.setAttribute("page", pm);
		request.setAttribute("cid", cid);
		return  "/jsp/product_list.jsp";
	}
	//清除浏览记录
	public void clearHistory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cid=request.getParameter("cid");
		int curNum=Integer.parseInt(request.getParameter("num"));
		//演练清除cookie
		Cookie c = new Cookie("history", "");
		c.setMaxAge(0);
		response.addCookie(c);
		String src="/store/ProductServlet?method=findProductsByCidWithPage&num="+curNum+"&cid="+cid;
		response.sendRedirect(src);
	}
	//分页查询所有商品
	public String findAllProductsWithPage(HttpServletRequest req, HttpServletResponse resp) throws Exception {
			int curNum=Integer.parseInt(req.getParameter("num"));
			//返回PageModel
			ProductService ProductService=new ProductServiceImp();
			PageModel pm=ProductService.findAllProductsWithPage(curNum);
			req.setAttribute("page", pm);
			return "/admin/product/list.jsp";
	}
	//查询下架商品
	public String findDownProductsWithPage(HttpServletRequest req, HttpServletResponse resp) throws Exception {
			//获取当前页
		    int curNum=Integer.parseInt(req.getParameter("num"));
			//返回PageModel
		    ProductService ProductService=new ProductServiceImp();
		    PageModel pm=ProductService.findOldProductsWithPage(curNum);
		    //将PageModel放入request
		    req.setAttribute("page", pm);
		    return "/admin/product/pushDown_list.jsp";
	}
	//跳转到添加商品界面
	public String addProductUI(HttpServletRequest req, HttpServletResponse resp) throws Exception {
				CategoryService CategoryService=new CategoryServiceImp();
				//获取全部分类信息
				List<Category> list = CategoryService.getAllCats();
				req.setAttribute("allCats", list);
				return "/admin/product/add.jsp";
	}
	//添加商品
	public String addProduct(HttpServletRequest req, HttpServletResponse resp) throws Exception {
				//存储表单中数据
				Map<String,String> map=new HashMap<String,String>();
				Product product=new Product();
				try {
					//获取到请求体中全部数据,进行拆分和封装
					DiskFileItemFactory fac=new DiskFileItemFactory();
					ServletFileUpload upload=new ServletFileUpload(fac);
					List<FileItem> list=upload.parseRequest(req);
					//遍历集合
					for (FileItem item : list) {
						if(item.isFormField()){
							//将普通项上name属性的值作为键,将获取到的内容作为值,放入MAP中
							map.put(item.getFieldName(), item.getString("utf-8"));
						}else{
							//如果当前的FileItem对象是上传项
							//原始的文件名称
							String oldFileName=item.getName();
							//保存文件的名称
							String newFileName=UploadUtils.getUUIDName(oldFileName);
							//通过输入流可以获取到图片二进制数据
							InputStream is=item.getInputStream();
							//获取到当前项目下products/3下的真实路径
							String realPath=getServletContext().getRealPath("/products/3/");
							String dir=UploadUtils.getDir(newFileName); // /f/e/d/c/4/9/8/4
							String path=realPath+dir; 
							File newDir=new File(path);
							if(!newDir.exists()){
								newDir.mkdirs();
							}
							//在服务端创建一个空文件
							File finalFile=new File(newDir,newFileName);
							if(!finalFile.exists()){
								finalFile.createNewFile();
							}
							//建立和空文件对应的输出流
							OutputStream os=new FileOutputStream(finalFile);
							//将输入流中的数据刷到输出流中 
							IOUtils.copy(is, os);
							//释放资源
							IOUtils.closeQuietly(is);
							IOUtils.closeQuietly(os);
							//向map中存入一个键值对的数据 userhead<===> /image/11.bmp
							map.put("pimage", "/products/3/"+dir+"/"+newFileName);
						}
					}
					//利用BeanUtils将MAP中的数据填充到Product对象上
					BeanUtils.populate(product, map);
					product.setPid(UUIDUtils.getId());
					product.setPdate(new Date());
					product.setPflag(0);
					//调用servcie_dao将user上携带的数据存入数据库,重定向到查询全部商品信息路径
					ProductService ProductService=new ProductServiceImp();
					ProductService.saveProduct(product);
					resp.sendRedirect("/store/ProductServlet?method=findAllProductsWithPage&num=1");

				} catch (Exception e) {
					e.printStackTrace();
				}
				
				return null;
		}
		//下架商品
		public String pushDownProduct(HttpServletRequest req, HttpServletResponse resp) throws Exception {
				//获取订单ID
				String pid=req.getParameter("pid");
				//根据订单ID查询订单
				ProductService ProductService=new ProductServiceImp();
				Product product=ProductService.findProductByPid(pid);
				//设置订单状态
				product.setPflag(1);
				ProductService.saveProduct(product);
				resp.sendRedirect("/store/ProductServlet?method=findAllProductsWithPage&num=1");
				return null;
		}	
			
		//上架商品
		public String pushUpProduct(HttpServletRequest req, HttpServletResponse resp) throws Exception {
				//获取订单ID
				String pid=req.getParameter("pid");
				//根据订单ID查询订单
				ProductService ProductService=new ProductServiceImp();
				Product product=ProductService.findProductByPid(pid);
				//设置订单状态
				product.setPflag(0);
				ProductService.saveProduct(product);
				resp.sendRedirect("/store/ProductServlet?method=findOldProductsWithPage&num=1");
				return null;
		}	
		//删除商品
		public String deleteProduct(HttpServletRequest req, HttpServletResponse resp) throws Exception {
				//获取订单ID
				String pid=req.getParameter("pid");
				//根据订单ID查询订单
				ProductService ProductService=new ProductServiceImp();
				ProductService.deleteProduct(pid);
				resp.sendRedirect("/store/ProductServlet?method=findOldProductsWithPage&num=1");
				return null;
		}	
	    //更新商品
		public String updateProduct(HttpServletRequest req, HttpServletResponse resp) throws Exception {
				String pid=req.getParameter("pid");
				//根据订单ID查询订单
				ProductService ProductService=new ProductServiceImp();
				Product product=ProductService.findProductByPid(pid);
				//存数据
				req.setAttribute("product", product);
				CategoryService CategoryService=new CategoryServiceImp();
				//获取全部分类信息
				List<Category> list = CategoryService.getAllCats();
				req.setAttribute("allCats", list);
				//转发
				return "/admin/product/edit.jsp";
		}
		//编辑商品
		public String editProduct(HttpServletRequest req, HttpServletResponse resp) throws Exception {
				//存储表单中数据
				Map<String,String> map=new HashMap<String,String>();
				//携带表单中的数据向servcie,dao
				Product product=new Product();
				try {
					//利用req.getInputStream();获取到请求体中全部数据,进行拆分和封装
					DiskFileItemFactory fac=new DiskFileItemFactory();
					ServletFileUpload upload=new ServletFileUpload(fac);
					List<FileItem> list=upload.parseRequest(req);
					//遍历集合
					for (FileItem item : list) {
						if(item.isFormField()){
							//将普通项上name属性的值作为键,将获取到的内容作为值,放入MAP中
							map.put(item.getFieldName(), item.getString("utf-8"));
						}else{
							//如果当前的FileItem对象是上传项
							//获取到原始的文件名称
							String oldFileName=item.getName();
							if(!oldFileName.isEmpty()) {
							//获取到要保存文件的名称  
							String newFileName=UploadUtils.getUUIDName(oldFileName);	
							//,通过输入流可以获取到图片二进制数据
							InputStream is=item.getInputStream();
							//获取到当前项目下products/3下的真实路径
							String realPath=getServletContext().getRealPath("/products/3/");
							String dir=UploadUtils.getDir(newFileName);
							String path=realPath+dir; 
							File newDir=new File(path);
							if(!newDir.exists()){
								newDir.mkdirs();
							}
							File finalFile=new File(newDir,newFileName);
							if(!finalFile.exists()){
								finalFile.createNewFile();
							}
							//建立和空文件对应的输出流
							OutputStream os=new FileOutputStream(finalFile);
							//将输入流中的数据刷到输出流中 is-->os
							IOUtils.copy(is, os);
							//释放资源
							IOUtils.closeQuietly(is);
							IOUtils.closeQuietly(os);
							//向map中存入一个键值对的数据
							map.put("pimage", "/products/3/"+dir+"/"+newFileName);
							}
						}
					}
					//利用BeanUtils将MAP中的数据填充到Product对象上
					BeanUtils.populate(product, map);
					product.setPdate(new Date());
					product.setPflag(0);
					//调用servcie_dao将user上携带的数据存入数据库,重定向到查询全部商品信息路径
					ProductService ProductService=new ProductServiceImp();
					ProductService.editProduct(product);
					resp.sendRedirect("/store/ProductServlet?method=findAllProductsWithPage&num=1");
					} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
		}

}
