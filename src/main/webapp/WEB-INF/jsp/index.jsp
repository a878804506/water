<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"   prefix="c"%>
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
			sj();
			//ajax获取天气数据
			
			function sj(){
				var now=new Date();
		    	var h=now.getHours();
		    	if(h<9){
					$("#w").html("早上好!");
			    }else if (h<11){
			    	$("#w").html("上午好!");
			    }else if (h<13){
			    	$("#w").html("中午好!");
				}else if (h<18){
			    	$("#w").html("下午好!");
				}else{
					$("#w").html("晚上好!");
				}
			}
		})
</script>
<style  type="text/css">
		#s{font-size:26px;}
		#w{font-size:26px;margin-left: 8px;}
		.n{font-size:20px;margin-left: 8px;}
		#t{font-size:20px;color:red;}
</style>
</head>


<body>

	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="#">首页</a></li>
    </ul>
    </div>
    <div class="mainindex">
    	<!-- 天气展示 -->
	    <div class="weatherInfo">
			    <!--<iframe src="//www.seniverse.com/weather/weather.aspx?uid=UEBF0109EB&cid=CHJL060100&l=zh-CHS&p=SMART&a=1&u=C&s=13&m=2&x=1&d=5&fc=&bgc=2E93D9&bc=&ti=1&in=1&li=" frameborder="0" scrolling="no" width="300" height="400" allowTransparency="true"></iframe>-->
				<iframe src="//www.seniverse.com/weather/weather.aspx?uid=UEBF0109EB&cid=CHBJ000000&l=zh-CHS&p=SMART&a=1&u=C&s=13&m=2&x=1&d=5&fc=&bgc=00AFDB&bc=&ti=1&in=1&li=" frameborder="0" scrolling="no" width="300" height="400" allowTransparency="true"></iframe>
		</div>
		
	    <div class="welinfo">
	    	<span><img src="<%=path %>/images/i09.png" height="26px" width="26px"/></span>
		    <b id="s"><span id="w"></span>欢迎使用水务管理系统</b>
	    </div>
		<p></p>
	    <div class="welinfo">
		    <span><img src="<%=path %>/images/time.png" height="26px" width="26px" /></span>
		    <i><span class="n">您上次登录的时间：</span><span id=t>${time }</span></i>
	    </div>
		<p></p>
	    <div class="welinfo">
		    <span><img src="<%=path %>/images/ewarn.png" height="30px" width="30px" /></span>
		    <i><span class="n">本次登陆：</span><span id='t'>${sessionScope.user.userRegion}${sessionScope.user.userCity}(${sessionScope.user.ip})</span></i>
	    </div>
    </div>
</body>

</html>
