<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>菜单</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" type="text/css" />
<script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script>
</head>
<body>
<ol class="tree">  
       <li>  
           <label for="folder1" class="folderOne">分类管理</label> <input type="checkbox" id="folder1" /> 
           <ol>  
                <li class="file folderTwo"><a href="${pageContext.request.contextPath}/CategoryServlet?method=findAllCats&num=1" target="mainFrame">分类管理</a></li>  
           </ol>  
   
       </li>  
       <li>  
           <label for="folder2" class="folderOne" >商品管理</label> <input type="checkbox" id="folder2" />   
           <ol>  
               <li class="file folderTwo"><a href="${pageContext.request.contextPath}/ProductServlet?method=findAllProductsWithPage&num=1" target="mainFrame">商品管理</a></li> 
               <li class="file folderTwo"><a href="${pageContext.request.contextPath}/ProductServlet?method=findDownProductsWithPage&num=1" target="mainFrame">已下架商品管理</a></li> 
           </ol>  
       </li> 
       <li>  
           <label for="folder3"  class="folderOne">订单管理</label> <input type="checkbox" id="folder3" />   
           <ol>  
               <li class="file folderTwo"><a href="${pageContext.request.contextPath}/OrderServlet?method=findOrders&num=1" target="mainFrame">订单管理</a></li> 
               <li class="file folderTwo"><a href="${pageContext.request.contextPath}/OrderServlet?method=findOrders&num=1&state=1" target="mainFrame">未付款的订单</a></li> 
               <li class="file folderTwo"><a href="${pageContext.request.contextPath}/OrderServlet?method=findOrders&num=1&state=2" target="mainFrame">已付款订单</a></li> 
               <li class="file folderTwo"><a href="${pageContext.request.contextPath}/OrderServlet?method=findOrders&num=1&state=3" target="mainFrame">已发货的订单</a></li> 
               <li class="file folderTwo"><a href="${pageContext.request.contextPath}/OrderServlet?method=findOrders&num=1&state=4" target="mainFrame">已完成的订单</a></li> 
           </ol>  
       </li> 
       <li>  
            <a id="lgout" href="${pageContext.request.contextPath}/UserServlet?method=AdminlogOut" target="_parent">退出</a>
       </li>
</ol>
 
<style type="text/css">
    body{background-color:#f2f2f2;}  
    .tree {margin: 0;padding: 0;background-color:#f2f2f2;overflow: hidden;}  
    .tree li input{position: absolute;left: 0;opacity: 0;z-index: 2;cursor: pointer;height: 1em;width:1em;top: 0;}  
    .tree li {position: relative;list-style: none;}   
    .tree>li{border-bottom: 1px solid #d9d9d9;}
    .tree li label {max-width:999px;cursor: pointer;display: block;margin:0 0 0 -40px;padding: 15px 10px 15px 70px;background: url(${pageContext.request.contextPath}/img/cp.png) no-repeat right center;background-position:95% 50%;white-space:nowrap;overflow:hidden;text-overflow: ellipsis; }  
    .tree li label:hover,li label:focus{background-color:#a7a7a7;color:#fff;}
    .tree li input + ol{display: none;}  
    .tree input:checked + ol {padding-left:14px;height: auto;display: block;}  
    .tree input:checked + ol > li { height: auto;}  
    .tree li.file a{margin:0 -10px 0 -50px;padding: 15px 20px 15px 70px;text-decoration:none;display: block;color:#333333;white-space:nowrap;overflow:hidden;text-overflow: ellipsis;} 
    .tree li.file a:hover,li.file a:focus{background-color:#a7a7a7;color:#fff;} 
    .tree .folderOne{font-size: 18px;}
    .tree .folderTwo{font-size:16px;}
     #lgout {font-size:1.3em;color:black;max-width:999px;cursor: pointer;display: block;margin:0 0 0 -40px;padding: 15px 10px 15px 70px;background-position:95% 50%;white-space:nowrap;overflow:hidden;text-overflow: ellipsis; }  
     #lgout:hover,#lgout:focus{background-color:#a7a7a7;color:#fff;}
</style>
</body>