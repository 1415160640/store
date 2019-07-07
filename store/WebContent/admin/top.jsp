<%@ page language="java" pageEncoding="UTF-8"%>
<html>
	<head>
		<meta http-equiv="Content-Language" content="zh-cn">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js" type="text/javascript"></script>
		<style type="text/css">
BODY {
	MARGIN: 0px;
	BACKGROUND-COLOR: #F2F2F2;
}

BODY {
	FONT-SIZE: 30px;
	COLOR: #000000
}

TD {
	FONT-SIZE: 30px;
	COLOR: #000000
}

TH {
	FONT-SIZE: 30px;
	COLOR: #000000
}
</style>
		<link href="${pageContext.request.contextPath}/css/Style1.css" rel="stylesheet" type="text/css">
	</HEAD>
	<body>
		<table width="100%" height="70%"  border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="100%" background="${pageContext.request.contextPath}/img/admin/top.jpg">
				</td>
			</tr>
		</table>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="30" valign="bottom" background="${pageContext.request.contextPath}/img/admin/mis_01.jpg" >
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
						    <td width="8%">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td width="155" height="30" valign="middle">
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;欢迎您：${AdminloginUser.username}&nbsp;
										</td>
										
									</tr>
								</table>
							</td>
							<td width="85%" align="left">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<font color="#000000" id="showDate"></font>
							</td>
							
							<td align="right" width="5%">
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</body>
<script type="text/javascript">
	$(function(){
		setInterval("getTime();",1000); //每隔一秒运行一次
	})
	//取得系统当前时间
	function getTime(){
		var myDate = new Date();
		var date = myDate.toLocaleDateString();
		var hours = myDate.getHours();
		var minutes = myDate.getMinutes();
		var seconds = myDate.getSeconds();
		$("#showDate").html(date+" "+hours+":"+minutes+":"+seconds); //将值赋给div
	}
</script>
</HTML>
