<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
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

	<script language="javascript">
		$(function(){
	        $("#vrsn").hide();		//进入页面先隐藏
	        $("#vrsn").css("z-index",-1);
	    })
		function showMessage(message,event){
			$("#vrsn").show();		//显示
	        $("#vrsn").css("z-index",999);
	        x=event.clientX  
	        y=event.clientY  
	        $("#vrsn").css("margin-left",x-520);
	        $("#vrsn").css("top",y);
	        $("#html").html(message);
		}
		function hide(){
			$("#vrsn").hide();		//隐藏
	        $("#vrsn").css("z-index",-1);
	        $("#html").html("");
		}
	</script>
	<style type="text/css">
		.imgtable th{
			height:34px; 
			line-height:34px;
			text-indent:0px;
			text-align:center;
			font-size:16px;
		}
		.imgtable td{
			line-height:30px; 
			text-indent:10px; 
			font-size:13px;
		}
		tr .center{ 
			text-align:center;
		}
	</style>
</head>

<body>
	<div class="place">
		<span>位置：</span>
		<ul class="placeul">
			<li><a href="#">首页</a></li>
			<li><a href="#">操作记录管理</a></li>
			<li><a href="#">操作记录查看</a></li>
		</ul>
    </div>
    <div class="rightinfo">
    <div class="tools">
        <form method="post"  action="showOperatot_log.action">
	        <ul class="toolbar2">
	        	<li>
	        			<input type="hidden"  name="password"  value="${password }"  />		<!-- password 标示符       历史记录/当天记录 -->
	        			<input type="text"  name="user_id" placeholder="用户编号" value="${page.user_id }"  class="a"/>
	        			<input type="text"  name="user_name" placeholder="用户姓名" value="${page.user_name }"  class="a" />
	        			<input type="date"  name="create_date" placeholder="操作时间" value="${page.create_date }"   class="a"/>
	        			
	        			<select  name="operate_type"  class="a">
	        				<c:choose>  
							   <c:when test="${empty page.operate_type }">
							   			 <option value=""  >------请选择------</option>
			                            <option value="1"  >1、添加用户操作记录</option>
			                            <option value="2"  >2、修改用户信息操作记录</option>
			                            <option value="3"  >3、报停/恢复用户操作记录</option>
			                            <option value="4"  >4、删除/恢复用户操作记录</option>
			                            <option value="5"  >5、录入账单操作记录</option>
			                            <option value="6"  >6、修改用户用水量操作记录</option>
							   </c:when>  
							   <c:otherwise>  
							   			<option value=""  >------请选择------</option>
									   	<option value="1"  <c:if test="${page.operate_type== 1 }">selected</c:if>>1、添加用户操作记录</option>
			                            <option value="2"  <c:if test="${page.operate_type== 2 }">selected</c:if>>2、修改用户信息操作记录</option>
			                            <option value="3"  <c:if test="${page.operate_type== 3 }">selected</c:if>>3、报停/恢复用户操作记录</option>
			                            <option value="4"  <c:if test="${page.operate_type== 4 }">selected</c:if>>4、删除/恢复用户操作记录</option>
			                            <option value="5"  <c:if test="${page.operate_type== 5 }">selected</c:if>>5、录入账单操作记录</option>
			                            <option value="6"  <c:if test="${page.operate_type== 6 }">selected</c:if>>6、修改用户用水量操作记录</option>
							   </c:otherwise>  
							</c:choose>  
                        </select>
                        &nbsp;
	        			<input type="submit" class="btn  btn-primary" value="搜索"/>
	        	</li>
	        </ul>
	    </form> 
    </div>
    <!--  隐藏div  -->
    <div id="vrsn" style="width: 500px; height: 75px;border:1px solid #000000;float :left; position: absolute; background : #E7E9D1;">
        <span id="html"></span>      
  	</div>
    <table class="imgtable"  border="1" cellpadding="0" cellspacing="0" align="center" >
		    <thead >
			    <tr>
					    <th width="8%" >记录编号</th>
					    <th width="9%" >操作员姓名</th>
					    <th width="8%" >用户编号</th>
					    <th width="8%" >用户姓名</th>
					    <th width="14%" >操作时间</th>
					    <th width="10%" >操作类型</th>
					    <th width="44%" >具体操作信息</th>
			    </tr>
		    </thead>
		    <tbody>
		    <c:forEach items="${list}" var="op">
				    <tr>
						    <td class="center">${op.id } </td>
						    <td class="center">${op.operator_name }</td>
						    <td class="center">${op.user_id }</td>
						    <td class="center">${op.user_name }</td>
						    <td class="center">${op.create_time }</td>
						    <td class="center">
						    		<c:if test="${op.operate_type==1}">
								    		添加用户
								    </c:if>
						    		<c:if test="${op.operate_type==2}">
								    		修改用户信息
								    </c:if>
								    <c:if test="${op.operate_type==3 }">
								    		报停/恢复用户
								    </c:if>
								    <c:if test="${op.operate_type==4}">
								    		删除/恢复用户
								    </c:if>
								    <c:if test="${op.operate_type==5}">
								    		录入账单记录
								    </c:if>
								    <c:if test="${op.operate_type==6}">
								    		修改用户用水量
								    </c:if>
						    </td>
						    <c:if test="${op.operate_type=='6' || op.operate_type=='2'}">
						    	<td><a onMouseOver="showMessage('${op.update_message }',event);" onMouseOut="hide();">详细信息</a></td>
						    </c:if>
						    <c:if test="${op.operate_type != '6' && op.operate_type !='2'}">
						    	<td>${op.update_message }</td>
						    </c:if>
				    </tr>
		    </c:forEach>
		    </tbody>
    </table>

    <div class="pagin">
    	<div class="message">共<i class="blue">${page.count }</i>条记录，共<i class="blue">${page.pageCount }</i>页码，当前显示第&nbsp;<i class="blue">${page.pageNow }&nbsp;</i>页</div>
        <ul class="paginList">
        <c:if test="${page.pageNow>1}">
        	<li class="paginItem"><a href="showOperatot_log.action?password=1&pageNow=1&user_id=${page.user_id}&user_name=${page.user_name}&create_date=${page.create_date}&operate_type=${page.operate_type}">首页</a></li>
        	<li class="paginItem"><a href="showOperatot_log.action?password=1&pageNow=${page.pageNow-1 }&user_id=${page.user_id}&user_name=${page.user_name}&create_date=${page.create_date}&operate_type=${page.operate_type}">上一页</a></li>
        </c:if>
        <c:if test="${page.pageNow==1}">
        	<li class="paginItem"><a >首页</a></li>
        	<li class="paginItem"><a>上一页</a></li>
        </c:if>
        <c:if test="${page.pageNow<page.pageCount}">
        	<li class="paginItem"><a href="showOperatot_log.action?password=1&pageNow=${page.pageNow+1 }&user_id=${page.user_id}&user_name=${page.user_name}&create_date=${page.create_date}&operate_type=${page.operate_type}">下一页</a></li>
        	<li class="paginItem"><a href="showOperatot_log.action?password=1&pageNow=${page.pageCount }&user_id=${page.user_id}&user_name=${page.user_name}&create_date=${page.create_date}&operate_type=${page.operate_type}">尾页</a></li>
        </c:if>
        <c:if test="${page.pageNow==page.pageCount}">
        	<li class="paginItem"><a >下一页</a></li>
        	<li class="paginItem"><a>尾页</a></li>
        </c:if>
        </ul>
    </div>
</div> 
</body>
</html>
