<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <title>水务管理系统</title>
  <script language="JavaScript" src="<%=path %>/js/JQuery.js"></script>
  <script language="JavaScript" src="<%=path %>/js/bootstrap.min.js"></script>
  <script language="JavaScript" src="<%=path %>/js/bootbox.min.js"></script>
  <script language="JavaScript" src="<%=path %>/js/jquery.ztree.core.js"></script>
  <script language="JavaScript" src="<%=path %>/js/jquery.ztree.excheck.js"></script>
  <script language="JavaScript" src="<%=path %>/js/jquery.ztree.exedit.js"></script>

  <link href="<%=path%>/fonts/iconfont.css" rel="stylesheet" type="text/css" ><!-- 聊天服务css-->
  <link href="<%=path %>/css/chat.css" rel="stylesheet" type="text/css" ><!-- 聊天服务css-->
  <link href="<%=path %>/css/bootstrap.css" rel="stylesheet"/>
  <link href="<%=path %>/css/style.css" rel="stylesheet" type="text/css" />
  <!-- 网站图标-->
  <link rel="shortcut icon" href="${sessionScope.staticPictureHost}favicon.ico" type="image/x-icon" />

  <style>
    html, body{
      width:100%;
      height:100%;
      margin:0;
      padding:0;
      overflow: hidden;
    }
    #top{float: left;height:88px;overflow : hidden;}
    #left{float: left;width:187px;overflow : hidden;}
    #index{float: right;overflow : hidden;}
    #webSocketChat{
      position:absolute;
      right:30px;
      bottom:30px;
      width:50px;
      height: 50px;
      border:1px solid #aaa;
      background-color: #D2E4EE;
      border-radius:50px;
    }
    #hiddenDiv{
      display:none;
    }
  </style>

  <script>
      $(function(){
          resize();
          $(window).resize(resize);
      })
      function resize(){
          $("#top").css("width", $("body").width());
          $("#left").css("height", ($("body").height()-88)/$("body").height()*100+'%');
          $("#index").css("height", ($("body").height()-88)/$("body").height()*100+'%');
          $("#index").css("width", ($("body").width()-188)/$("body").width()*100+'%');
      }
  </script>

</head>
<%--<frameset rows="88,*" cols="*" frameborder="no" border="0" framespacing="0">
  <frame src="<%=path %>/getJSPForFrame.action?flag=top" name="topFrame" noresize="noresize" id="topFrame" title="topFrame" scrolling="no"   />
  <frameset cols="187,*" frameborder="no" border="0" framespacing="0">
    <frame src="<%=path %>/getJSPForFrame.action?flag=left" name="leftFrame" noresize="noresize" id="leftFrame" title="leftFrame" scrolling="no"    />
    <frame src="<%=path %>/getJSPForFrame.action?flag=index" name="rightFrame" id="rightFrame" title="rightFrame" scrolling="auto"   />
  </frameset>
</frameset>--%>
<body>
  <div id="top">
    <iframe src="<%=path %>/getJSPForFrame.action?flag=top" name="topFrame" width="100%" height="100%" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no" allowtransparency="yes"></iframe>
  </div>
  <div id="left">
    <iframe src="<%=path %>/getJSPForFrame.action?flag=left" name="leftFrame" width="100%" height="100%" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no" allowtransparency="yes"></iframe>
  </div>
  <div id="index">
    <!-- 聊天服务-->
    <div id="webSocketChat">
      <jsp:include page="/WEB-INF/jsp/webSocketChat.jsp" flush="true"/>
    </div>
    <iframe src="<%=path %>/getJSPForFrame.action?flag=index" name="rightFrame" width="100%" height="100%" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no" allowtransparency="yes"></iframe>
  </div>
  <div id="hiddenDiv">
    <!-- 登陆用户的id-->
    <input type="hidden" value="${sessionScope.user.id}" id="thisUserId"/>
    <!-- session的Id-->
    <input type="hidden" value="${pageContext.session.id}" id="thisUserSessionId"/>
    <!-- 聊天架构是否开启了-->
    <input type="hidden" value="${sessionScope.webSocketChatSwitch}" id="webSocketChatSwitch"/>
    <!-- 聊天架构服务所在ip-->
    <input type="hidden" value="${sessionScope.webSocketChatAddress}" id="webSocketChatAddress"/>
    <!-- 有联系人发消息时发出的声音-->
    <audio id="didi" src="<%=path %>/js/didi.mp3" preload="auto"></audio>
    <!-- 有联系人上线时发出的声音-->
    <audio id="onLine" src="<%=path %>/js/onLine.mp3" preload="auto"></audio>
    <!-- 聊天架构服务所在ip-->
    <input type="hidden" value="<%=path %>" id="projectPath"/>
  </div>
</body>
<!-- 聊天服务js-->
<script language="JavaScript" src="<%=path %>/js/webSocketChat.js"></script>
</html>
