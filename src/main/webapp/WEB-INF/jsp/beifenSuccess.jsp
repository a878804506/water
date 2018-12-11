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

<jsp:include page="js.jsp" flush="true"/>

<script type="text/javascript">
		$(function(){
			sj();
			function sj(){
				var now=new Date();
		    	var h=now.getHours();
		    	if(h<9){
					$("#w").html("三哥，早上好！");
			    }else if (h<11){
			    	$("#w").html("三哥，上午好！");
			    }else if (h<13){
			    	$("#w").html("三哥，中午好！");
				}else if (h<18){
			    	$("#w").html("三哥，下午好！");
				}else{
					$("#w").html("三哥，晚上好！");
				}
			}
		})
</script>
<style  type="text/css">
		#s{font-size:26px;}
		#w{font-size:26px;}
		#n{font-size:20px;}
		#t{font-size:22px;color:red;}

</style>
</head>


<body>
	
	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="#">数据备份</a></li>
    </ul>
    </div>
    <div class="mainindex">
    <div class="welinfo">
    <span><img src="../../images/sun.png" alt="天气" /></span>
    <b id="s"><span id="w"></span>欢迎使用水务管理系统</b>
    </div>
    <div class="welinfo">
    <br/>
    <div>
    <span id="n"><span><img src="../../images/time.png" alt="时间" /></span>三哥，本次数据已经备份成功！</span>
    </div>
    <br/>
    <div>
			<span id="n">保存至${path }中，文件名为：</span><span id="t">${path1 }</span>
    </div>
    </div>
    </div>
</body>

</html>
