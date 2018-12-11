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
            var toDayCountList = ${toDayCountList};
            var selected = ${selected}-50;
            if(toDayCountList[0] == -1){
                $("#search").html("<option value='xxx'>第xxx-xxx户</option>");
                $("#search").attr("disabled","disabled");
                $("#input").attr("disabled","disabled");
			}else{
                var option = "";
                $.each(toDayCountList, function (index, num) {
                    if(selected == num){
                        option += "<option value='"+index*50+"'  selected>第"+(index*50+1)+"-"+(index*50+50)+"户</option>";
					}else{
                        option += "<option value='"+index*50+"'>第"+(index*50+1)+"-"+(index*50+50)+"户</option>";
					}
                })
                $("#search").html(option);
			}
            $("#input").click(function(){
                //填充月份到表格
                $.ajax({
                    url:"getToDayDatas.action",
                    type:"post",
                    data:{limit:$("#search").val()},
                    dataType:"json",
                    success:function(data){
                        if(data!=null){
                            $("#toDayCount").html(data.toDayCount);
                            $("#count").html(data.count);
                            $("#water").html(data.water);
                            $("#cost").html(data.cost);
                            $("#tital").html($("#search").find("option:selected").text()+"录入详情");
                            var myTab = "";
                            $.each(data.toDayDatasList, function (index, toDayData) {
                                myTab += "<tr><td>"+
                                    toDayData.customer_id +
                                    "</td><td >" +
                                    toDayData.customer_name +
                                    "</td><td >" +
                                    toDayData.create_time +
                                    "</td><td >" +
                                    toDayData.water_consumption +
                                    "</td><td >" +
                                    toDayData.month_bill_cost +
                                    "</td></tr>";
                            })
                            $("#myTab").html(myTab);
                            // scroll({id:"#myTable"});
                            $("#myTable").freezeHeader({ 'height': '100%'});
                        }
                    }
                });
            })
		})
        /*function scroll(obj){
            $(obj.id).niceScroll({
                cursorcolor: obj.color||"#59C7EF",   //滚动条颜色
                autohidemode:obj.autohidemode||false,
                zindex:200,
                cursorwidth:obj.cursorwidth||"7px",  //滚动条宽度
                cursorborderradius:obj.cursorborderradius||"10px",  // 圆角
                cursorborder:obj.cursorborder||"1px solid #59C7EF"  //滚动条边框颜色
            });
        };*/
	</script>
</head>
<body>
	<div class="place">
		<span>位置：</span>
		<ul class="placeul">
			<li><a href="#">首页</a></li>
			<li><a href="#">月水费管理</a></li>
			<li><a href="#">今日水费录入详情</a></li>
		</ul>
    </div>
    <div class="formbody">
	    <div class="formtitle"><span>查询今日水费录入</span></div>
        <div id="f">
            <a class="colorF">按今日录入水费用户查询：</a>
            <select id="search" style="width:120px; height:32px;" name="name">

            </select>
            <input name="" type="button" class="btn btn-primary  " value="查询" id="input"/>
        </div>
		<br />
		<div class="toDayDiv" id="toDayDiv">
			<table class="imgtable"  border="1" cellpadding="0" cellspacing="0" align="center"  >
				<tr>
					<th>今日录入总数</th>
					<th>本次查询用户数</th>
					<th>本次查询用户总用水量(m³)</th>
					<th>本次查询用户总水费(元)</th>
				</tr>
				<tr>
					<th id="toDayCount" class="red"></th>
					<th id="count" class="red"></th>
					<th id="water" class="red"></th>
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
						<th>录入时间</th>
						<th>用水量(m³)</th>
						<th>水费(元)</th>
					</tr>
				</thead>
				<tbody id="myTab" >

				</tbody>
			</table>
		</div>
	</div>
</body>
<!-- 因为代码的执行是从上到下依次执行，所以，把js代码块单独写在所有的body后面 -->
<script type="text/javascript">
    // 页面全部加载完成以后执行以下的js代码区
    window.onload = function(){
        var toDayCountList = ${toDayCountList};
        if(toDayCountList[0] != -1){
            $("#input").click();
		}else{
            bootbox.alert("今日还没有给任何用户录入水费!");
		}
    }
</script>
</html>
