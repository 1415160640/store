<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<%--分页显示的开始 --%>
    	<div style="text-align:center">
    		共${page.totalPageNum}页/第${page.currentPageNum}页
    		<c:if test="${page.currentPageNum !=1 }">
			  		<a href="${pageContext.request.contextPath}/${page.url}&num=1&state=${page.state}">首页</a>
    		        |<a href="${pageContext.request.contextPath}/${page.url}&num=${page.prePageNum}&state=${page.state}">上一页</a>
			</c:if>
    		<%--显示的页码，使用forEach遍历显示的页面 --%>
    		<c:forEach begin="${page.startPage}" end="${page.endPage}" var="pagenum">
    			<c:if test="${page.currentPageNum == pagenum }">
			  				${pagenum }
			  	</c:if>
			  	<c:if test="${page.currentPageNum != pagenum }">
			  				<a style="font-size: 1.25em; color:gray;" href="${pageContext.request.contextPath}/${page.url}&num=${pagenum}&state=${page.state}">${pagenum}</a>
			  	</c:if>
    		</c:forEach>
    		<c:if test="${page.currentPageNum !=page.totalPageNum }">
			  	     <a href="${pageContext.request.contextPath}/${page.url}&num=${page.nextPageNum}&state=${page.state}">下一页</a>
    		          |<a href="${pageContext.request.contextPath}/${page.url}&num=${page.totalPageNum}&state=${page.state}">末页</a>
			</c:if>
    	</div>
   
