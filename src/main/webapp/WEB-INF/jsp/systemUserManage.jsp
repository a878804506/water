<%@ page language="java" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>系统用户管理</title>

    <jsp:include page="js.jsp" flush="true"/>

    <style type="text/css">

    </style>

    <SCRIPT type="text/javascript">
        var users = ${users};
        //用户列表
        var tUser = "";
        $.each(users, function (index, user) {
            tUser += "<tr><td style='display:none;'>" +
                user.id +
                "</td><td >" +
                "<input type='radio' name='choose' value = '" +
                user.struts +
                "'/>"+
                "</td><td>" +
                user.name +
                "</td> <td>" ;
            if(user.nickName != null){
                tUser += user.nickName;
            }else{
                tUser += "暂无记录";
            }
            tUser += "</td> <td>";
            if(user.time != null){
                tUser += user.time;
            }else{
                tUser += "暂无记录";
            }
            tUser += "</td> <td>";
            if(user.ip != null){
                tUser += user.ip;
            }else{
                tUser += "暂无记录";
            }
            tUser += "</td> <td>";
            if(user.userRegion != null || user.userCity !=null){
                tUser += user.userRegion + user.userCity;
            }else{
                tUser += "暂无记录";
            }
            tUser += "</td>";
            if (user.struts == 1) {
                tUser += "<td style='color:green' >已启用";
            } else {
                tUser += "<td style='color:red'>已禁用";
            }
            tUser += "</td></tr>";
        });

        $(document).ready(function () {
            $("#tUsers").html(tUser);
            var uid = "";
            var uname = "";
            //点击用户列表时
            $("#tUsers tr").click(function () {
                $("#tUsers tr").removeAttr("style"); //移除所有
                $(this).css("background", "#D2E4EE");//为当前点击的行添加样式
                $("[type=radio]").prop("checked",false);
                $(this).children('td').eq(1).find("input").prop("checked","checked");//为当前行的radio添加checked
                uid = $(this).children('td').eq(0).text();
                uname = $(this).children('td').eq(2).text();
            });

            //新建用户
            $("#insert").click(function () {
                if(checkUserPermission("createManageUser.action") != "true"){  //权限校验
                    bootbox.alert("您没有相应的权限操作，请联系管理员！");
                    return ;
                }
                bootbox.dialog({
                    title : "新建系统用户",
                    message : "<div class='well ' style='margin-top:25px;'>"+
                                  "<form class='form-horizontal' role='form'>"+
                                      "<div class='form-group'>"+
                                          "<label class='col-sm-3 control-label no-padding-right' for='txtOldPwd'>系统账号</label>"+
                                          "<div class='col-sm-9'>"+
                                          "<input type='text' id='name' name='name' placeholder='请输入账号' class='form-control' />"+
                                          "</div>"+
                                      "</div>"+
                                      "<div class='space-4'>"+
                                      "</div>"+
                                      "<div class='form-group'>"+
                                          "<label class='col-sm-3 control-label no-padding-right' for='txtNewPwd1'>密码</label>"+
                                          "<div class='col-sm-9'>" +
                                          "<input type='text' id='password' name='password' placeholder='请输入密码' class='form-control' />"+
                                          "</div>"+
                                      "</div>"+
                                      "<div class='space-4'>"+
                                      "</div>"+
                                      "<div class='form-group'>"+
                                          "<label class='col-sm-3 control-label no-padding-right' for='txtNewPwd2'>确认密码</label>"+
                                          "<div class='col-sm-9'>" +
                                          "<input type='text' id='againPassword' placeholder='再次输入密码' class='form-control' />"+
                                          "</div>"+
                                      "</div>"+
                                  "</form>"+
                              "</div>",
                    buttons : {
                        "success" : {
                            "label" : "<i class='icon-ok'></i> 保存",
                            "className" : "btn-sm btn-success",
                            "callback" : function() {
                                var name = $("#name").val();
                                var password = $("#password").val();
                                var againPassword = $("#againPassword").val();
                                if(name == ""){
                                    bootbox.alert("用户名不能为空！");
                                    return false;
                                }
                                if(password == ""){
                                    bootbox.alert("密码不能为空！");
                                    return false;
                                }
                                if(password != againPassword){
                                    bootbox.alert("两次密码不一致！");
                                    return false;
                                }
                                var user = {};
                                user["name"] = name;
                                user["password"] = password;
                                $.ajax({
                                    url: "createManageUser.action",
                                    type: "POST",
                                    data: user,
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
                                                window.location.href = "getAllUsers.action";
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
            })

            //为用户重置密码
            $("#update").click(function () {
                if(checkUserPermission("resettingPasswordForUserByUserId.action") != "true"){  //权限校验
                    bootbox.alert("您没有相应的权限操作，请联系管理员！");
                    return ;
                }
                var selectStruts = $("[type=radio]:checked").val(); //选中的那一行
                if (selectStruts != undefined && uid != "" && uname != "") {
                    bootbox.dialog({
                        title : "请为 "+uname+" 重置密码",
                        message : "<div class='well ' style='margin-top:25px;'>" +
                                      "<form class='form-horizontal' role='form'>" +
                                           "<div class='form-group'>" +
                                               "<label class='col-sm-4 control-label no-padding-right' for='txtNewPwd2'>请输入重置后的新密码:</label>" +
                                                "<div class='col-sm-8'>" +
                                                    "<input type='text' id='newPwd' placeholder='请输入重置后的新密码' class='form-control' />" +
                                                "</div>" +
                                          "</div>" +
                                      "</form>" +
                                   "</div>",
                        buttons : {
                            "success" : {
                                "label" : "<i class='icon-ok'></i> 保存",
                                "className" : "btn-sm btn-success",
                                "callback" : function() {
                                    var newPwd = $("#newPwd").val();
                                    if(newPwd == ""){
                                        bootbox.alert("密码不能为空");
                                        return false;
                                    }
                                    $.ajax({
                                        url: "resettingPasswordForUserByUserId.action",
                                        type: "POST",
                                        data: {uid: uid,newPwd:newPwd},
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
                                                    window.location.href = "getAllUsers.action";
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
                } else {
                    bootbox.alert("请选择用户再进行操作！");
                    return;
                }
            })

            //禁用/启用
            $("#checkStatus").click(function () {
                if(checkUserPermission("updateUserStrutsByUid.action") != "true"){  //权限校验
                    bootbox.alert("您没有相应的权限操作，请联系管理员！");
                    return ;
                }
                var selectStruts = $("[type=radio]:checked").val();
                if (selectStruts != undefined && uid != "") {
                    var msg = "";
                    if (selectStruts == "1") {
                        msg = "禁用";
                    } else {
                        msg = "启用";
                    }
                    bootbox.dialog({
                        message: "确定" + msg + "该用户吗?",
                        title: "提示",
                        buttons: {
                            确定: {
                                label: "确定",
                                className: "btn-success",
                                callback: function () {
                                    $.ajax({
                                        url: "updateUserStrutsByUid.action",
                                        type: "POST",
                                        data: {uid: uid },
                                        dataType: "text",
                                        async:false,
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
                                                    window.location.href = "getAllUsers.action";
                                                }
                                                //,title: "bootbox alert也可以添加标题哦",
                                            });
                                        }
                                    });
                                }
                            }
                            , 取消: {
                                label: "取消",
                                className: "btn-warning",
                                callback: function () {
                                }
                            }
                        }
                    });
                } else {
                    bootbox.alert("请选择用户再进行操作！");
                    return;
                }
            })

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
        });

    </SCRIPT>

</head>

<body>
<div class="place">
    <span>位置：</span>
    <ul class="placeul">
        <li><a href="#">首页</a></li>
        <li><a href="#">系统管理</a></li>
        <li><a href="#">系统用户管理</a></li>
    </ul>
</div>

<div class="formbody">
    <div style="width:100%;">
        <input type="button" value="新建系统用户" id="insert" class="btn btn-info"/>
        <input type="button" value="重置用户密码" id="update" class="btn  btn-warning"/>
        <input type="button" value="禁用/启用" id="checkStatus" class="btn btn btn-danger"/>
    </div>
    <div style="width:100%;margin-top: 10px">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th style="display:none;">id</th>
                <th>选择</th>
                <th>账号</th>
                <th>用户昵称</th>
                <th>上次登录时间</th>
                <th>ip</th>
                <th>上次登录地</th>
                <th>用户状态</th>
            </tr>
            </thead>
            <tbody id="tUsers">

            </tbody>
        </table>
    </div>
</div>
</body>
</html>