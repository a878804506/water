<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>

	<jsp:include page="/WEB-INF/jsp/js.jsp" flush="true"/>
	<script type="text/javascript">
	$(function(){
		$(".top").removeAttr("href");

		//顶部导航切换
		$(".nav li a").click(function(){
			$(".nav li a.selected").removeClass("selected")
			$(this).addClass("selected");
		})
	})
	</script>

	<style type="text/css">
		#userPicture{
			border-radius:110px;
			width: 70px;
			height: 70px;
		}
	</style>
</head>

<body style="background:url(<%=path %>/images/topbg.gif) repeat-x;">

    <div class="topleft">
    	<a href="index" target="rightFrame"><img src="<%=path %>/images/logo.png" title="系统首页" /></a>
    </div>
        
    <ul class="nav">
	    <li><a href="#" target="rightFrame"  class="top"><img src="<%=path %>/images/icon01.png" title="今日头条" /><h2>今日头条</h2></a></li>
	    <%--<li><a href="#"  class="top" target="rightFrame" ><img src="<%=path %>/images/icon02.png" title="" /><h2>三哥好！</h2></a></li>
	    <li><a href="#"  class="top" target="rightFrame" ><img src="<%=path %>/images/icon03.png" title="" /><h2>三哥好！</h2></a></li>
	    <li><a href="#"  id="jisuanqi" target="rightFrame" class="top"><img src="<%=path %>/images/icon04.png" title="" /><h2>三哥好！</h2></a></li>
	    <li><a href="#"  id="bianqian"  target="rightFrame" class="top"><img src="<%=path %>/images/icon05.png" title="" /><h2>三哥好！</h2></a></li>
	    <li><a href="#"  target="rightFrame"  class="top"><img src="<%=path %>/images/icon06.png" title="系统设置" /><h2>系统设置</h2></a></li>--%>
    </ul>

     <!-- 人型时钟 -->
	<div style="position:absolute;margin-left:60%;margin-top:0px;margin-top:-15px;"  >
		<script type="text/javascript" src="<%=path %>/js/clock.js"></script>
	</div>

    <div class="topright">
	    <ul>
			<li><a href="#"  target="rightFrame" class="top"><img src="${sessionScope.staticPictureHost}${sessionScope.user.img}" id="userPicture"/></a></li>
		    <li>
		    	<a style="font-size:16px;cursor:default">欢迎您_</a>
		    	<a href="#" style="font-weight:800;font-size:16px" id="nickName">${sessionScope.user.nickName}</a>
		    </li>
	    	<li><a href="out.action"  target="_top" style="font-size:16px">注销</a></li>
	    </ul>
    </div>
</body>
</html>
