<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<HTML>
	<HEAD>
		<meta http-equiv="Content-Language" content="zh-cn">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link href="${pageContext.request.contextPath}/css/Style1.css" rel="stylesheet" type="text/css" />
		<script language="javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js"></script>
	</HEAD>
	<body>
		<br>
		<form id="Form1" name="Form1" action="#" method="post">
			<table cellSpacing="1" cellPadding="0" width="100%" align="center" bgColor="#f5fafe" border="0">
				<TBODY>
					<tr>
						<td class="ta_01" align="center" bgColor="#F2F2F2">
							<table cellspacing="0" cellpadding="1" rules="all"
								bordercolor="gray" border="1" id="DataGrid1"
								style="BORDER-RIGHT: gray 1px solid; BORDER-TOP: gray 1px solid; BORDER-LEFT: gray 1px solid; WIDTH: 100%; WORD-BREAK: break-all; BORDER-BOTTOM: gray 1px solid; BORDER-COLLAPSE: collapse; BACKGROUND-COLOR: #F2F2F2; WORD-WRAP: break-word">
								<tr
									style="FONT-WEIGHT: bold; FONT-SIZE: 12pt; HEIGHT: 25px; BACKGROUND-COLOR: #afd1f3">

									<td align="center" width="5%">
										序号
									</td>
									<td align="center" width="20%">
										订单编号
									</td>
									<td align="center" width="5%">
										订单金额
									</td>
									<td align="center" width="5%">
										收货人
									</td>
									<td align="center" width="5%">
										订单状态
									</td>
									<td align="center" width="60%">
										订单详情
									</td>
								</tr>
								<c:forEach items="${page.list}" var="o" varStatus="status">
										<tr onmouseover="this.style.backgroundColor = 'white'"
											onmouseout="this.style.backgroundColor = '#F2F2F2';">
											<td style="CURSOR: hand; HEIGHT: 22px" align="center"
												width="5%">
												${status.count}
											</td>
											<td style="CURSOR: hand; HEIGHT: 22px" align="center"
												width="20%">
												${o.oid}
											</td>
											<td style="CURSOR: hand; HEIGHT: 22px" align="center"
												width="5%">
												${o.total}
											</td>
											<td style="CURSOR: hand; HEIGHT: 22px" align="center"
												width="5%">
												${o.name}
											</td>
											<td style="CURSOR: hand; HEIGHT: 22px" align="center"
												width="5%">
												<c:if test="${o.state==1}">未付款</c:if>
												<c:if test="${o.state==2}">
													<a href="/store/OrderServlet?method=updateOrderByOidAll&oid=${o.oid}">发货</a>
												</c:if>
												<c:if test="${o.state==3}">已发货</c:if>
												<c:if test="${o.state==4}">订单完成</c:if>
											</td>
											<td align="center" style="HEIGHT: 22px" width="60%"  >
											    <a type="button" id="${o.oid}" class="myClass"  style="font-size:1.3em;color:blue;">查看房屋详情</a>
												<table border="1"  width="100%" style="border-collapse:collapse;">
												</table>
											</td>
										</tr>
									</c:forEach>
							</table>
						</td>
					</tr>
					<tr align="center">
						<td colspan="7">
							
						</td>
					</tr>
				</TBODY>
			</table>
		</form>
		<%@include file="/jsp/pageFile2.jsp" %>
	</body>
<script>
$(function(){
	//页面加载完毕之后,获取样式名称为myClass一批元素,为期绑定点击事件
	$(".myClass").mouseover(function(){
		//获取当前订单id
		var id=this.id;
		//获取当前按钮文字
		var txt=this.value;
		//PS:获取到当前元素的下一个对象table
		var $tb=$(this).next('table');
			//向服务端发送Ajax请求,将当前的订单id传递到服务端
			var url="/store/OrderServlet";
			var obj={"method":"findOrderAjax","id":id};
			$.post(url,obj,function(data){
				//清除内容
				$tb.html("");
				var th="<tr><th>商品</th><th>名称</th><th>单价</th><th>数量</th></tr>";
				$tb.append(th);
				//利用JQUERY遍历响应到客户端的数据
				$.each(data,function(i,obj){
					var td="<tr><td><img src='/store/"+obj.product.pimage+"' width='50px'/></td><td>"+obj.product.pname+"</td><td>"+obj.product.shop_price+"</td><td>"+obj.quantity+"</td></tr>";
					$tb.append(td);				
				})
			},"json");
    });
	$(".myClass").mouseout(function(){
		var $tb=$(this).next('table');
		$tb.html("");
	});
});
</script>	
</HTML>

