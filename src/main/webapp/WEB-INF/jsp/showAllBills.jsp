<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>无标题文档</title>
    <jsp:include page="js.jsp" flush="true"/>
    <script type="text/javascript">
        var customerList = ${list};
        var tab = "";
        $.each(customerList, function (index, customer) {
            tab += "<tr><td>" +
                "<input type='radio' name='choose' value = '" +
                customer.id +
                "'/>"+
                "</td><td>" +
                customer.id +
                "</td> <td>" +
                customer.uname +
                "</td> <td>" +
                customer.one +
                "</td> <td>" +
                customer.two +
                "</td> <td>" +
                customer.three +
                "</td> <td>" +
                customer.four +
                "</td> <td>" +
                customer.five +
                "</td> <td>" +
                customer.six +
                "</td> <td>" +
                customer.seven +
                "</td> <td>" +
                customer.eight +
                "</td> <td>" +
                customer.nine +
                "</td> <td>" +
                customer.ten +
                "</td> <td>" +
                customer.eleven +
                "</td> <td>" +
                customer.twelve +
                "</td></tr>";
        });

        $(document).ready(function () {
            $("#myTab").html(tab);
            var uid = "";
            var bill = {};
            //点击用户列表时
            $("#myTab tr").click(function () {
                $("#myTab tr").removeAttr("style"); //移除所有
                $(this).css("background", "#D2E4EE");//为当前点击的行添加样式
                $("[type=radio]").prop("checked",false);
                $(this).children('td').eq(0).find("input").prop("checked","checked");//为当前行的radio添加checked
                uid = $(this).children('td').eq(0).find("input").val();
                bill["uid"] = uid;
                bill["uname"] = $(this).children('td').eq(2).text();
                bill["one"] = $(this).children('td').eq(3).text();
                bill["two"] = $(this).children('td').eq(4).text();
                bill["three"] = $(this).children('td').eq(5).text();
                bill["four"] = $(this).children('td').eq(6).text();
                bill["five"] = $(this).children('td').eq(7).text();
                bill["six"] = $(this).children('td').eq(8).text();
                bill["seven"] = $(this).children('td').eq(9).text();
                bill["eight"] = $(this).children('td').eq(10).text();
                bill["nine"] = $(this).children('td').eq(11).text();
                bill["ten"] = $(this).children('td').eq(12).text();
                bill["eleven"] = $(this).children('td').eq(13).text();
                bill["twelve"] = $(this).children('td').eq(14).text();
            });
            //点击修正按钮时
            $("#updateCustomerWater").click(function(){
                if(checkUserPermission("toupdateBill.action") != "true"){  //权限校验
                    bootbox.alert("您没有相应的权限操作，请联系管理员！");
                    return ;
                }
                if(uid != ""){
                    bootbox.dialog({
                        title : "请为 "+bill["uname"]+" 修正起止码",
                        message : "<div class='well ' style='margin-top:25px;'>"+
                        "<form class='form-horizontal' role='form'>"+
                        "<div class='form-group'>"+
                        "<label for='starttime' class='col-sm-2 control-label'>一月</label>"+
                        "<div class='col-md-4'>" +
                        "<input type='number' id='num1' class='form-control' value='"+bill["one"]+"' onblur='checkNum(this.value,\"num1\");' placeholder='一月' />"+
                        "</div>"+
                        "<label for='starttime' class='col-sm-2 control-label'>二月</label>"+
                        "<div class='col-md-4'>" +
                        "<input type='number' id='num2'  class='form-control' value='"+bill["two"]+"' onblur='checkNum(this.value,\"num2\");'  placeholder='二月'/>"+
                        "</div>"+
                        "</div>"+
                        "<div class='form-group'>"+
                        "<label for='starttime' class='col-sm-2 control-label'>三月</label>"+
                        "<div class='col-md-4'>" +
                        "<input type='number' id='num3' class='form-control' value='"+bill["three"]+"' onblur='checkNum(this.value,\"num3\");'  placeholder='三月' />"+
                        "</div>"+
                        "<label for='starttime' class='col-sm-2 control-label'>四月</label>"+
                        "<div class='col-md-4'>" +
                        "<input type='number' id='num4'  class='form-control' value='"+bill["four"]+"' onblur='checkNum(this.value,\"num4\");' placeholder='四月'/>"+
                        "</div>"+
                        "</div>"+
                        "<div class='form-group'>"+
                        "<label for='starttime' class='col-sm-2 control-label'>五月</label>"+
                        "<div class='col-md-4'>" +
                        "<input type='number' id='num5' class='form-control' value='"+bill["five"]+"' onblur='checkNum(this.value,\"num5\");' placeholder='五月' />"+
                        "</div>"+
                        "<label for='starttime' class='col-sm-2 control-label'>六月</label>"+
                        "<div class='col-md-4'>" +
                        "<input type='number' id='num6'  class='form-control' value='"+bill["six"]+"' onblur='checkNum(this.value,\"num6\");' placeholder='六月'/>"+
                        "</div>"+
                        "</div>"+
                        "<div class='form-group'>"+
                        "<label for='starttime' class='col-sm-2 control-label'>七月</label>"+
                        "<div class='col-md-4'>" +
                        "<input type='number' id='num7' class='form-control' value='"+bill["seven"]+"' onblur='checkNum(this.value,\"num7\");' placeholder='七月' />"+
                        "</div>"+
                        "<label for='starttime' class='col-sm-2 control-label'>八月</label>"+
                        "<div class='col-md-4'>" +
                        "<input type='number' id='num8'  class='form-control' value='"+bill["eight"]+"' onblur='checkNum(this.value,\"num8\");' placeholder='八月'/>"+
                        "</div>"+
                        "</div>"+
                        "<div class='form-group'>"+
                        "<label for='starttime' class='col-sm-2 control-label'>九月</label>"+
                        "<div class='col-md-4'>" +
                        "<input type='number' id='num9' class='form-control' value='"+bill["nine"]+"' onblur='checkNum(this.value,\"num9\");'  placeholder='九月' />"+
                        "</div>"+
                        "<label for='starttime' class='col-sm-2 control-label'>十月</label>"+
                        "<div class='col-md-4'>" +
                        "<input type='number' id='num10'  class='form-control' value='"+bill["ten"]+"' onblur='checkNum(this.value,\"num10\");' placeholder='十月'/>"+
                        "</div>"+
                        "</div>"+
                        "<div class='form-group'>"+
                        "<label for='starttime' class='col-sm-2 control-label'>十一月</label>"+
                        "<div class='col-md-4'>" +
                        "<input type='number' id='num11' class='form-control' value='"+bill["eleven"]+"' onblur='checkNum(this.value,\"num11\");'  placeholder='十一月' />"+
                        "</div>"+
                        "<label for='starttime' class='col-sm-2 control-label'>十二月</label>"+
                        "<div class='col-md-4'>" +
                        "<input type='number' id='num12'  class='form-control' value='"+bill["twelve"]+"' onblur='checkNum(this.value,\"num12\");' placeholder='十二月'/>"+
                        "</div>"+
                        "</div>"+
                        "</form>"+
                        "</div>",
                        buttons : {
                            "success" : {
                                "label" : "<i class='icon-ok'></i> 保存",
                                "className" : "btn-sm btn-success",
                                "callback" : function() {
                                    for(var i = 1 ; i <= 12 ; i++){
                                        var temp = $("#num"+i).val();
                                        if(temp == ""){
                                            bootbox.alert(bill["uname"]+"的"+i+"月份起止码为空，请重新填写！");
                                            return;
                                        }
                                    }
                                    bill["one"] = $("#num1").val();
                                    bill["two"] = $("#num2").val();
                                    bill["three"] = $("#num3").val();
                                    bill["four"] = $("#num4").val();
                                    bill["five"] = $("#num5").val();
                                    bill["six"] = $("#num6").val();
                                    bill["seven"] = $("#num7").val();
                                    bill["eight"] = $("#num8").val();
                                    bill["nine"] = $("#num9").val();
                                    bill["ten"] = $("#num10").val();
                                    bill["eleven"] = $("#num11").val();
                                    bill["twelve"] = $("#num12").val();
                                    $.ajax({
                                        url: "toupdateBill.action",
                                        type: "POST",
                                        data: bill,
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
                                                    window.location.href = "showAllBills.action?pageNow=${page.pageNow }&name=${page.name}";
                                                }
                                            });
                                        }
                                    });
                                }
                            },
                            "cancel" : {
                                "label" : "<i class='icon-info'></i> 取消",
                                "className" : "btn-sm btn-danger",
                                "callback" : function() { }
                            }
                        }
                    });
                }else{
                    bootbox.alert("请选择用户再进行操作！");
                    return;
                }
            })
        })

        function tiaozhuan() {
            window.location.href = "showAllBills.action?pageNow=" + $("#location").val() + "&name=${page.name}";
        }
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
        //校验系统用户输入的起止码正确性（应该为整数）
        function checkNum(value,inputId){
            value=value.replace(/\D/g,'');
            if(value ==""){
                value = 0;
            }
            $("#"+inputId).val(value);
        }
    </script>
</head>


<body>

<div class="place">
    <span>位置：</span>
    <ul class="placeul">
        <li><a href="#">首页</a></li>
        <li><a href="#">月水费管理</a></li>
        <li><a href="#">个人月度起止码</a></li>
    </ul>
</div>
<div class="rightinfo">
    <div class="tools">
        <ul style="float: left">
            <li ><input type="button" class="btn  btn-warning" id="updateCustomerWater" value="修正用户起止码"/></li>
        </ul>
        <form action="showAllBills.action" method="post">
            <ul class="toolbar1">
                <li ><input type="text" name="name" placeholder="输入用户编号或者用户名进行查询" value="${page.name }" id="a"/>&nbsp;&nbsp;
                    <input type="submit" class="btn btn-primary" value="搜索用户"/>
                </li>
            </ul>
        </form>
    </div>
    <table class="imgtable" border="1" cellpadding="0" cellspacing="0" align="center">
        <thead>
        <tr>
            <th>选择</th>
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
        </tr>
        </thead>
        <tbody id="myTab">

        </tbody>
    </table>

    <div class="pagin">
        <div class="message">共<i class="blue">${page.count }</i>条记录，共<i class="blue">${page.pageCount }</i>页码，当前显示第&nbsp;<i
                class="blue">${page.pageNow }&nbsp;</i>页
        </div>
        <div class="paginList_tiaozhuan">
            <input type="number" style="width:65px;height:28px;" id="location" value="${page.pageNow}"
                   onkeyup="if(this.value>${page.pageCount }){this.value=''}else{this.value=this.value.replace(/\D/g,'')}"/>
            <input type="button" value="跳转" style="height:28px;" class="btn btn-primary" onclick="tiaozhuan()"/>
        </div>
        <ul class="paginList">
            <c:if test="${page.pageNow>1}">
                <li class="paginItem"><a href="showAllBills.action?pageNow=1&name=${page.name}">首页</a></li>
                <li class="paginItem"><a href="showAllBills.action?pageNow=${page.pageNow-1 }&name=${page.name}">上一页</a>
                </li>
            </c:if>
            <c:if test="${page.pageNow==1}">
                <li class="paginItem"><a>首页</a></li>
                <li class="paginItem"><a>上一页</a></li>
            </c:if>
            <c:if test="${page.pageNow<page.pageCount}">
                <li class="paginItem"><a href="showAllBills.action?pageNow=${page.pageNow+1 }&name=${page.name}">下一页</a>
                </li>
                <li class="paginItem"><a href="showAllBills.action?pageNow=${page.pageCount }&name=${page.name}">尾页</a>
                </li>
            </c:if>
            <c:if test="${page.pageNow==page.pageCount}">
                <li class="paginItem"><a>下一页</a></li>
                <li class="paginItem"><a>尾页</a></li>
            </c:if>
        </ul>
    </div>
</div>
</body>

</html>
