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
<script type="text/javascript">
    var customerList = ${list};
    var customerTab = "";
    $.each(customerList, function (index, customer) {
        customerTab += "<tr><td>" +
            "<input type='radio' name='choose' value = '" +
            customer.cid +
            "'/>"+
            "</td><td>" +
            customer.cid +
            "</td> <td>" +
            customer.cname +
            "<input type='hidden' value = '"+customer.cname+"'/>" +
            "</td> <td>" +
            customer.cprice + " 元" +
            "<input type='hidden' value = '"+customer.cprice+"'/>" +
            "</td> <td>" ;
        if(customer.caddress == ""){
            customerTab += "(无填写)";
        }else{
            customerTab += customer.caddress;
        }
        customerTab += "<input type='hidden' value = '"+customer.caddress+"'/>";
        customerTab +="</td> <td>";
        if(customer.cstartime == ""){
            customerTab += "(无填写)";
        }else{
            customerTab += customer.cstartime;
        }
        customerTab += "<input type='hidden' value = '"+customer.cstartime+"'/>";
        if(customer.cstatus == 1){
            customerTab +="</td> <td  style='color:green;'>";
            customerTab += "启用中";
        }else{
            customerTab +="</td> <td  style='color:red;'>";
            customerTab += "已报停";
        }
        customerTab += "<input type='hidden' value = '"+customer.cstatus+"'/>";
        customerTab +="</td></tr>";
    });

	$(document).ready(function(){
        $("#myTab").html(customerTab);
        var cid = "";
        var cname = "";
        //点击用户列表时
        $("#myTab tr").click(function () {
            $("#myTab tr").removeAttr("style"); //移除所有
            $(this).css("background", "#D2E4EE");//为当前点击的行添加样式
            $("[type=radio]").prop("checked",false);
            $(this).children('td').eq(0).find("input").prop("checked","checked");//为当前行的radio添加checked
            cid = $(this).children('td').eq(0).find("input").val();
        });
		//启用用户
        $("#switchCustomer").click(function(){
            if(checkUserPermission("status.action") != "true"){  //权限校验
                bootbox.alert("您没有相应的权限操作，请联系管理员！");
                return ;
            }
            if(cid != "" ){
                bootbox.dialog({
                    message: "确定启用 "+cname+"?",
                    title: "提示",
                    buttons: {
                        确定: {
                            label: "确定",
                            className: "btn-sm btn-success",
                            callback: function () {
                                $.ajax({
                                    url: "status.action",
                                    type: "POST",
                                    data: {"cid":cid},
                                    dataType: "text",
                                    async:false, //使用同步
                                    success: function (data) {
                                        bootbox.alert({
                                            buttons: {
                                                ok: {
                                                    label: "确定",
                                                    className: 'btn-primary'
                                                }
                                            },
                                            message: data,
                                            callback: function () {
                                                window.location.href = "baotingCustomer.action?pageNow=${page.pageNow }&name=${page.name}";
                                            }
                                        });
                                    }
                                });
                            }
                        }
                        , 取消: {
                            label: "取消",
                            className: "btn-sm btn-danger",
                            callback: function () {

                            }
                        }
                    }
                });
            }else{
                bootbox.alert("请选择用户再进行操作！");
                return;
            }
        })
	});

    //系统用户权限检查
    function checkUserPermission(url) {
        var returnData = "";
        $.ajax({
            url: "checkUserPermission.action",
            type: "GET",
            data: {url: url},
            dataType: "text",
            async:false, //使用同步
            success: function (data) {
                returnData = data;
            }
        });
        return returnData;
    }

	function tiaozhuan(){
		window.location.href="baotingCustomer.action?pageNow="+$("#location").val();
	}
</script>
</head>


<body>

	<div class="place">
		<span>位置：</span>
		<ul class="placeul">
			<li><a href="#">首页</a></li>
			<li><a href="#">用户管理</a></li>
			<li><a href="#">查看报停用户</a></li>
		</ul>
    </div>
    <div class="rightinfo">
		<div class="tools">
			<ul style="float: left">
				<li style="display: inline-block;"><input type="button" class="btn  btn-warning" id="switchCustomer" value="启用用户"/></li>
			</ul>
		</div>
		<table class="imgtable"  border="1" cellpadding="0" cellspacing="0" align="center" >
			<thead>
			<tr>
				<th>选择</th>
				<th width="100px;">用户编号</th>
				<th>用户姓名</th>
				<th>水费定价</th>
				<th>用户住址</th>
				<th>开户时间</th>
				<th>用户状态</th>
			</tr>
			</thead>
			<tbody id="myTab">

			</tbody>
		</table>

		<div class="pagin">
			<div class="message">共<i class="blue">${page.count }</i>条记录，共<i class="blue">${page.pageCount }</i>页码，当前显示第&nbsp;<i class="blue">${page.pageNow }&nbsp;</i>页</div>
			<div class="paginList_tiaozhuan">
					<input  type="number" style="width:65px;height:28px;" id="location" value="${page.pageNow}"
					onkeyup="if(this.value>${page.pageCount }){this.value=''}else{this.value=this.value.replace(/\D/g,'')}" />
					<input type="button" value="跳转" style="height:28px;" class="btn btn-primary" onclick="tiaozhuan()"/>
			</div>
			<ul class="paginList">
			<c:if test="${page.pageNow>1}">
					<li class="paginItem"><a href="baotingCustomer.action?pageNow=1">首页</a></li>
					<li class="paginItem"><a href="baotingCustomer.action?pageNow=${page.pageNow-1 }">上一页</a></li>
			</c:if>
			<c:if test="${page.pageNow==1}">
					<li class="paginItem"><a >首页</a></li>
					<li class="paginItem"><a>上一页</a></li>
			</c:if>
			<c:if test="${page.pageNow<page.pageCount}">
				<li class="paginItem"><a href="baotingCustomer.action?pageNow=${page.pageNow+1 }">下一页</a></li>
				<li class="paginItem"><a href="baotingCustomer.action?pageNow=${page.pageCount }">尾页</a></li>
			</c:if>
			<c:if test="${page.pageNow==page.pageCount}">
				<li class="paginItem"><a >下一页</a></li>
				<li class="paginItem"><a >尾页</a></li>
			</c:if>
			</ul>
		</div>
    </div> 
</body>

</html>
