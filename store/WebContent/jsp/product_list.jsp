<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="cn.store.utils.CookieUtil"%>
<!doctype html>
<html>

	<head>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>商品列表</title>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" type="text/css" />
		<script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script>
		<!-- 引入自定义css文件 style.css -->
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css"/>

		<style>
			body {
				margin-top: 20px;
				margin: 0 auto;
				width: 100%;
			}
			.carousel-inner .item img {
				width: 100%;
				height: 300px;
			}
		</style>
	</head>

	<body>
		 <%@include file="/jsp/header.jsp" %>	
        <c:if test="${empty page.list}">
        	<div class="row" style="width:1210px;margin:0 auto;">
        		<div class="col-md-12">
        			<img src="${pageContext.request.contextPath}/img/nono.jpg" width="270" height="250" style="display: inline-block;">
        		</div>
        	</div>	
        </c:if>
        
        
        <c:if test="${not empty page.list}">
	        <div class="row" style="width:1210px;margin:0 auto;">
				<div class="col-md-12">
					<ol class="breadcrumb">
						<li><a href="#">首页</a></li>
					</ol>
				</div>
	          <c:forEach items="${page.list}" var="p">
				<div class="col-md-2" style="height:300px;padding:10px 10px;">
					<a href="${pageContext.request.contextPath}/ProductServlet?method=findProductByPid&pid=${p.pid}">
						<img src="${pageContext.request.contextPath}/${p.pimage}" width="170" height="170" style="display: inline-block;">
					</a>
					<p><a href="${pageContext.request.contextPath}/ProductServlet?method=findProductByPid&pid=${p.pid}" style='color:green;' >${p.pname}</a></p>
					<p><font color="#FF0000" >商城价：&yen;${p.shop_price}</font></p>
				</div>
			  </c:forEach>
			</div>
			<%@ include file="/jsp/pageFile.jsp" %>
        </c:if>
		<div style="width:1210px;margin:0 auto; padding: 0 9px;border: 1px solid #ddd;border-top: 2px solid #999;height: 246px;">

			<h4 style="width: 50%;float: left;font: 14px/30px ;">浏览记录</h4>
			<div style="width: 50%;float: right;text-align: right;"><a href="">more</a></div>
			<div style="clear: both;"></div>

			<div style="overflow: hidden;">

				<ul style="list-style: none;">
					<%
				Cookie[] cookie = request.getCookies();
				Cookie cook = 	CookieUtil.findCookie(cookie, "history");
				if(cook == null){
					%>
					<h3>您还没浏览任何商品</h3>
					<%
				}else{
				String []  product1 =	cook.getValue().split("#");
				int j=0;
				for(String i : product1){
					String[] dd = i.split("@"); 
						%>
						<li style="width: 150px;height: 216;float: left;margin: 0 8px 0 0;padding: 0 18px 15px;text-align: center;">
						  <a href="${pageContext.request.contextPath}/ProductServlet?method=findProductByPid&pid=<%=dd[0] %>">
						    <img src="${pageContext.request.contextPath}/<%=dd[1] %>" width="130px" height="130px" />
						  </a>
						</li>
				        <%
				        j++;
					if(j>6){
						break;
					}
						
				  }
				}
				 
				%>
				</ul>
				<%
				  if(cook!=null){//清除记录
					   %>
					      <a href="/store/ProductServlet?method=clearHistory&num=${page.currentPageNum}&cid=${cid}" style=" display:inline-block;  width:100%;"><h3>清除浏览记录</h3></a>
					   <% 
				  }
				%>

			</div>
		</div>
		 <%@include file="/jsp/footer.jsp" %>
	</body>

</html>