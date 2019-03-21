<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>水务管理系统</title>

<script language="JavaScript" src="<%=path %>/js/jquery-3.3.1.min.js"></script>
<script language="JavaScript"  src="<%=path %>/js/bootstrap.min.js"></script>
<script language="JavaScript" src="<%=path %>/js/bootbox.min.js"></script>
<script language="JavaScript" src="<%=path %>/js/jquery.ztree.core.js"></script>
<script language="JavaScript" src="<%=path %>/js/jquery.ztree.excheck.js"></script>
<script language="JavaScript" src="<%=path %>/js/jquery.ztree.exedit.js"></script>

<link href="<%=path %>/css/style.css" rel="stylesheet" type="text/css" />

<!-- 网站图标-->
<link rel="shortcut icon" href="${sessionScope.staticPictureHost}favicon.ico" type="image/x-icon" />

<script src="<%=path %>/js/cloud.js" type="text/javascript"></script>
<!-- 可选的集中颜色 #D3EDFE    #E8F3F7      #BDDADE    #A9D9FF -->
<style type="text/css">
	 .link{
	 	margin-top:70px;
	 	z-index:999;
	 	position:relative;
	 	float:right ;
	 	right:1%;
		border-radius: 20px;
		background-color:#A9D9FF;  
	 }
	#box{
		display: none;
		width: auto;
		height: auto;
		line-height: 175%;
	}
    #bigDiv{
        position: absolute;
        z-index: 9999;
        left: 0px;
        top: 0px;
        right: 0px;
        bottom: 0px;
        display: none;
        background: rgba(0, 0, 0, 0.6);
    }
    #message{
        width:249px;
        height:67px;
        background-color:white;
        margin:22% 0 0 40%;
    }
    #img{
        float:left;
        margin-top:16px;
        vertical-align:middle;
    }
    #msg {
        float:left;
        margin-top:25px;
    }
</style>

<!-- 此段代码主要用于盗链或者用户注销时跳出内联框架 -->
<script language="JavaScript">
    if (window != top)
    top.location.href = location.href;
</script>
<!-- 定位上网ip -->
<Script src="http://pv.sohu.com/cityjson"></Script>
<script language="javascript">
	$(function(){
	    $('.loginbox').css({'position':'absolute','left':($(window).width()-692)/2});
		$(window).resize(function(){  
	        $('.loginbox').css({'position':'absolute','left':($(window).width()-692)/2});
	    })

		var message = "${sessionScope.message}";
		if(message !=""){
		    $("#msg").html(message);
            document.querySelector("#bigDiv").style.display = "block";
            <% session.removeAttribute("message"); %>
            window.setTimeout(removeLi,3000);
        }

        $("#ip2").val(returnCitySN.cip);

        //跨域请求 获取用户上网IP和地址
        /*$.ajax({
            async: false,
            type: "GET",
            dataType: 'jsonp',
            jsonp: 'callback',
            jsonpCallback: 'callbackfunction',
            url: "http://ip.chinaz.com/getip.aspx",
            data: "",
            timeout: 3000,
            contentType: "application/json;utf-8",
            success: function(res) {
                $("#ip1").val(res.ip);
            }
        });*/

        $("#bigDiv").click(function(){
            document.querySelector("#bigDiv").style.display = "none";
        })
	});

	function removeLi(){
        document.querySelector("#bigDiv").style.display = "none";
	}

    //点击验证码变换图片
    function changeImg(){
        document.getElementById("checkCodeImg").src = "<%=path %>/login/checkCode?d="+Math.random();
    }
</script>
	<!-- 鼠标移动到“关于”显示更新内容 -->
<script language="JavaScript"> 		
	function display(){
		document.getElementById("box").style.display="block"; 
	}
	function disappear(){
		document.getElementById("box").style.display="none"; 
	}	
</script>
</head>

<body style="background-color:#1c77ac; background-image:url(<%=path %>/images/light.png); background-repeat:no-repeat; background-position:center top; overflow:hidden;">
    <div id="mainBody">
	    <div id="cloud1" class="cloud"></div>
	    <div id="cloud2" class="cloud"></div>
    </div>  
	<div class="logintop">    
	    <span>欢迎登录水务管理系统</span>
	    <ul>
		    <li><a href="#">帮助</a></li>
		    <li><a href="#" onmouseover="display()" onmouseout="disappear()">关于</a></li>
	    </ul>    
    </div>
    <div class="loginbody">
	    <span class="systemlogo"></span> 
	    <div class="loginbox loginbox3">
		    <form action="<%=path %>/CUser.action"  method="post" id="loginForm">
			    <ul>
				    <li><input id="name" name="name" type="text" class="loginuser" value="" onclick="JavaScript:this.value=''" required oninvalid="setCustomValidity('用户名不能为空!')" oninput="setCustomValidity('')"/></li>
				    <li><input id="password" name="password" type="password" class="loginpwd" value="" onclick="JavaScript:this.value=''" required oninvalid="setCustomValidity('密码不能为空!')" oninput="setCustomValidity('')"/></li>
                    <li class="yzm">
                        <span><input id="yzm" name="yzm" type="text" value="" onclick="JavaScript:this.value=''" required oninvalid="setCustomValidity('验证码不能为空!')" oninput="setCustomValidity('')"/></span>
                        <cite><img id="checkCodeImg" style="height: 46px; width: 120px;" src="<%=path %>/login/checkCode" onclick="changeImg()"></cite>
                    </li>
				    <li><input name="" type="submit" class="loginbtn" value="登录"   id="login" style="width:111px" />
                        <label>忘记密码了请联系管理员!</label>
                        <input type="hidden" name="ip1" value="" id="ip1"/>
                        <input type="hidden" name="ip2" value="" id="ip2"/>
				    </li>
			    </ul>
		    </form>
	    </div>
    </div>
    <div class="loginbm">版权所有(6.0)  2019.2  </div>
	<div class="link">
		<div id="box" onmouseover="display()" onmouseout="disappear()">
					<p class="p">1.新增天气预报功能；</p>
					<p class="p">2.添加角色权限的定义；</p>
					<p class="p">3.新增保存用户ip，上网地址；</p>
					<p class="p">4.当天开票统计以及各月开票统计！</p>
					<p class="p">5.新增操作日志记录；</p>
					<p class="p">6.新增开票页面数据校验；</p>
					<p class="p">7.数据备份功能完善；</p>
					<p class="p">8.前端界面风格调优；</p>
					<p class="p">9.修复用户当前登录IP地址；</p>
		</div>
	</div>
    <div id="bigDiv">
        <div id="message">
            <img src="<%=path %>/images/loginno.jpg" width="60px" height="35px" id="img"/>
            <div id="msg">

            </div>
        </div>
    </div>
</body>
</html>