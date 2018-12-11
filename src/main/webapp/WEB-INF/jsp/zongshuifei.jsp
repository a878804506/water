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
<title>无标题文档</title>

<jsp:include page="js.jsp" flush="true"/>
    <script language="JavaScript" src="<%=path %>/js/jquery.freezeheader.js"></script>

	<style type="text/css">
        body,html{
            width: 100%;
            height: 100%;
        }
        .formbody{
            height: 93%;
        }
        .colorF{font-weight:900;font-size:18px;}
        #f{margin-left:30px;}
        #input{margin-left:30px;}
        .toDayDiv{margin-left:30px;}
        .red{color:red;font-size:24px;}
        .imgtable th{
            text-indent:0px;
            text-align:center;
        }
        #hdScrollmyTable::-webkit-scrollbar {
            width: 8px;
            height: 8px;
        }
        /*正常情况下滑块的样式*/
        #hdScrollmyTable::-webkit-scrollbar-thumb {
            background-color: rgba(30, 144, 255,.5);
            border-radius: 10px;
            -webkit-box-shadow: inset 1px 1px 0 rgba(0,0,0,.1);
        }
        /*鼠标悬浮在该类指向的控件上时滑块的样式*/
        #hdScrollmyTable:hover::-webkit-scrollbar-thumb {
            background-color: rgba(30, 144, 255,.9);
            border-radius: 10px;
            -webkit-box-shadow: inset 1px 1px 0 rgba(0,0,0,.1);
        }
        /*鼠标悬浮在滑块上时滑块的样式*/
        #hdScrollmyTable::-webkit-scrollbar-thumb:hover {
            background-color: rgba(30, 144, 255,.9);
            -webkit-box-shadow: inset 1px 1px 0 rgba(0,0,0,.1);
        }
        /*正常时候的主干部分*/
        #hdScrollmyTable::-webkit-scrollbar-track {
            border-radius: 10px;
            -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0);
            background-color: white;
        }
        /*鼠标悬浮在滚动条上的主干部分*/
        #hdScrollmyTable::-webkit-scrollbar-track:hover {
            -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,.4);
            background-color: rgba(0,0,0,.01);
        }
	</style>
	<script type="text/javascript">
		$(function(){
			$("#input").click(function(){
				//填充月份到表格
				$.ajax({
					url:"zongshuifei.action",
					type:"post",
					data:{name:$("#month").val()},
					dataType:"json",
					success:function(data){
                        if(data!=null){
                            $("#total").html(data.total);
                            $("#baoting").html(data.baoting);
                            $("#delete").html(data.delete);
                            $("#yinggai").html(data.yinggai);
                            $("#shiji").html(data.shiji);
                            $("#no").html(data.no);
                            $("#cost").html(data.cost);
                            $("#tital").html($("#month").val()+"月未录入用户的起止码详情");
                            var myTab = "";
                            $.each(data.notImportCustomer, function (index, customer) {
                                myTab += "<tr><td>"+
                                    customer.id +
                                    "</td><td >" +
                                    customer.uname +
                                    "</td><td >" +
                                    customer.one +
                                    "</td><td >" +
                                    customer.two +
                                    "</td><td >" +
                                    customer.three +
                                    "</td><td >" +
                                    customer.four +
                                    "</td><td >" +
                                    customer.five +
                                    "</td><td >" +
                                    customer.six +
                                    "</td><td >" +
                                    customer.seven +
                                    "</td><td >" +
                                    customer.eight +
                                    "</td><td >" +
                                    customer.nine +
                                    "</td><td >" +
                                    customer.ten +
                                    "</td><td >" +
                                    customer.eleven +
                                    "</td><td >" +
                                    customer.twelve ;
                                    if(customer.cdelete == 0 && customer.cstatus == 1){
                                        myTab += "</td> <td  style='color:red;'>";
                                        myTab += "已删除";
                                    }else if(customer.cdelete == 1 && customer.cstatus == 0){
                                        myTab += "</td> <td  style='color:red;'>";
                                        myTab += "已报停";
                                    }else if(customer.cdelete == 1 && customer.cstatus == 1){
                                        myTab +="</td> <td  style='color:green;'>";
                                        myTab += "正常";
                                    }else{
                                        myTab += "</td> <td  style='color:red;'>";
                                        myTab += "已删除";
                                    }
                                myTab += "</td></tr>";
                            })
                            $("#myTab").html(myTab);
                            $("#myTable").freezeHeader({ 'height': '100%'});
                        }
					}
				});
			})
		})
	</script>
</head>
<body>
	<div class="place">
		<span>位置：</span>
		<ul class="placeul">
			<li><a href="#">首页</a></li>
			<li><a href="#">月水费管理</a></li>
			<li><a href="#">月度录入总水费详情</a></li>
		</ul>
    </div>
    <div class="formbody">
	    <div class="formtitle"><span>查询全镇当月总水费</span></div>
        <div id="f">
            <a class="colorF">月份：</a>
            <select id="month" style="width:70px; height:32px;" name="name">
                   <c:forEach begin="1" end="12" step="1" var="i">
                       <c:if test="${i==toMonth}">
                           <option value="${i}" selected >${i }</option>
                       </c:if>
                       <c:if test="${i!=toMonth}">
                           <option value="${i}"  >${i }</option>
                       </c:if>
                   </c:forEach>
            </select>
            <input name="" type="button" class="btn btn-primary  " value="查询" id="input"/>
        </div>
        <br />
        <div class="toDayDiv" id="toDayDiv">
            <table class="imgtable"  border="1" cellpadding="0" cellspacing="0" align="center"  >
                <tr>
                    <th>全镇总用户数</th>
                    <th>报停用户数</th>
                    <th>删除用户数</th>
                    <th>应录入用户数</th>
                    <th>实际录入用户数</th>
                    <th>未录入用户数</th>
                    <th>实际录入水费总额(元)</th>
                </tr>
                <tr>
                    <th id="total" class="red"></th>
                    <th id="baoting" class="red"></th>
                    <th id="delete" class="red"></th>
                    <th id="yinggai" class="red"></th>
                    <th id="shiji" class="red"></th>
                    <th id="no" class="red"></th>
                    <th id="cost" class="red"></th>
                </tr>
            </table>
        </div>
        <br />
        <div class="toDayDiv"  id="tableDown" style="height: 65%;">
            <table class="imgtable" border="1" cellpadding="0" cellspacing="0" align="center" id="myTable">
                <caption style="text-align:center;font-size:20px;color:red;" id="tital"></caption>
                <thead>
                <tr>
                    <th>编号</th>
                    <th>用户姓名</th>
                    <th>一月</th>
                    <th>二月</th>
                    <th>三月</th>
                    <th>四月</th>
                    <th>五月</th>
                    <th>六月</th>
                    <th>七月</th>
                    <th>八月</th>
                    <th>九月</th>
                    <th>十月</th>
                    <th>十一月</th>
                    <th>十二月</th>
                    <th>备注</th>
                </tr>
                </thead>
                <tbody id="myTab">

                </tbody>
            </table>
        </div>
	</div>
</body>
<!-- 因为代码的执行是从上到下依次执行，所以，把js代码块单独写在所有的body后面 -->
<script type="text/javascript">
    // 页面全部加载完成以后执行以下的js代码区
    window.onload = function(){
        $("#input").click();
    }
</script>
</html>
