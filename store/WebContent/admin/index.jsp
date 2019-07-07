<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>网上商城管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" type="text/css" />
		<script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script>
			<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css"/>


<style type="text/css">
body {
   margin-top:20px;
   margin:0 auto;
 }
 .carousel-inner .item img{
	 width:100%;
	 height:300px;
 }
 .container .row div{ 
	 /* position:relative;
	 float:left; */
 }
 
font {
    color: #666;
    font-size: 22px;
    font-weight: normal;
    padding-right:17px;
}

</style>
</head>
<body style="background: #278296; background-image:url(${pageContext.request.contextPath}/img/ad/1.jpg);" >
<div class="container">
<center> <div style="color:red;"><h3>${msg}</h3></div></center>
<div class="row">
 
     <div class="col-md-4">
	</div>
	
	<div class="col-md-4">
	<div style="width:440px;border:1px solid #E7E7E7;padding:20px 0 20px 30px;border-radius:5px;margin-top:60px;color:black;background:#fff;">
	     <div class="form-group">
			<font>管理员登录</font>Admin LOGIN
		 </div>
    <form class="form-horizontal" action="${pageContext.request.contextPath }/UserServlet?method=AdminLogin" method="post">
       <div class="form-group">
          <label for="username" class="col-sm-2 control-label">用户名</label>
          <div class="col-sm-6">
             <input type="text" name="username" class="form-control" id="username" placeholder="请输入用户名">
          </div>
       </div>
       <div class="form-group">
         <label for="inputPassword3" class="col-sm-2 control-label">密码</label>
         <div class="col-sm-6">
             <input type="password" name="password" class="form-control" id="inputPassword3" placeholder="请输入密码">
         </div>
      </div>
      <div class="form-group">
          <div class="col-sm-offset-2 col-sm-10">
             <input type="submit"   value="登录" name="submit" 
             style="background: url('${pageContext.request.contextPath}/img/login.gif') no-repeat scroll 0 0 rgba(0, 0, 0, 0);
             height:35px;width:100px;color:white;">
        </div>
      </div>
     </form>
     </div>			
	</div>
	
	
</div>
</div>	
</body>