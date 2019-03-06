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
    <title>系统角色管理</title>

    <jsp:include page="js.jsp" flush="true"/>

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

    <SCRIPT type="text/javascript">
        $(document).ready(function () {
            var rid = "";
            var rname = "";
            var rtype = "";
            var rremark = "";
            var rstatus = "";
            //点击角色列表时
            $("#tRoles tr").click(function () {
                $("#tRoles tr").removeAttr("style"); //移除所有
                $(this).css("background", "#D2E4EE");//为当前点击的行添加样式
                $("[type=radio]").prop("checked",false);
                $(this).children('td').eq(1).find("input").prop("checked","checked");//为当前行的radio添加checked
                rid = $(this).children('td').eq(0).text();
                rname = $(this).children('td').eq(2).text();
                rtype = $(this).children('td').eq(3).text();
                rremark = $(this).children('td').eq(4).text();
                rstatus = $(this).children('td').eq(1).val();
            });

            //新建角色
            $("#insert").click(function () {
                if(checkUserPermission("createRole.action") != "true"){  //权限校验
                    bootbox.alert("您没有相应的权限操作，请联系管理员！");
                    return ;
                }
                bootbox.dialog({
                    title : "新建系统角色",
                    message : "<div class='well ' style='margin-top:25px;'>"+
                                  "<form class='form-horizontal' role='form'>"+
                                      "<div class='form-group'>"+
                                          "<label class='col-sm-3 control-label no-padding-right' for='txtOldPwd'>角色名称</label>"+
                                          "<div class='col-sm-9'>"+
                                          "<input type='text' id='role_name' name='role_name' placeholder='请输入角色名称' class='form-control' />"+
                                          "</div>"+
                                      "</div>"+
                                      "<div class='space-4'>"+
                                      "</div>"+
                                      "<div class='form-group'>"+
                                          "<label class='col-sm-3 control-label no-padding-right' for='txtNewPwd1'>角色类型</label>"+
                                          "<div class='col-sm-9'>" +
                                          "<select  name='is_admin' id='is_admin'  class='form-control'>"+
                                              "<option value=''>------请选择------</option>"+
                                              "<option value='2'>普通角色</option>"+
                                              "<option value='1'>管理员</option>"+
                                              "<option value='0'>超级管理员</option>"+
                                          "</select>"+
                                          "</div>"+
                                      "</div>"+
                                      "<div class='space-4'>"+
                                      "</div>"+
                                      "<div class='form-group'>"+
                                          "<label class='col-sm-3 control-label no-padding-right' for='txtNewPwd2'>备注</label>"+
                                          "<div class='col-sm-9'>" +
                                          "<input type='text' id='remark' placeholder='备注' class='form-control' />"+
                                          "</div>"+
                                      "</div>"+
                                  "</form>"+
                              "</div>",
                    buttons : {
                        "success" : {
                            "label" : "<i class='icon-ok'></i> 保存",
                            "className" : "btn-sm btn-success",
                            "callback" : function() {
                                var role_name = $("#role_name").val();
                                var is_admin = $("#is_admin").val();
                                if(role_name == ""){
                                    bootbox.alert("角色名称不能为空！");
                                    return false;
                                }
                                if(is_admin == ""){
                                    bootbox.alert("请选择角色类型！");
                                    return false;
                                }
                                var role = {};
                                role["role_name"] = role_name;
                                role["is_admin"] = is_admin;
                                role["remark"] = $("#remark").val();
                                $.ajax({
                                    url: "createRole.action",
                                    type: "POST",
                                    data: role,
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
                                                window.location.href = "getAllRoles.action";
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

            //修改角色信息
            $("#update").click(function () {
                if(checkUserPermission("updateRole.action") != "true"){  //权限校验
                    bootbox.alert("您没有相应的权限操作，请联系管理员！");
                    return ;
                }
                var selectStruts = $("[type=radio]:checked").val(); //选中的那一行
                if (selectStruts != undefined && rid != "" && rname != "") {
                    bootbox.dialog({
                        title : "修改 "+rname+" 角色的相关信息",
                        message : "<div class='well ' style='margin-top:25px;'>"+
                                    "<form class='form-horizontal' role='form'>"+
                                        "<div class='form-group'>"+
                                            "<label class='col-sm-3 control-label no-padding-right' for='txtOldPwd'>角色名称</label>"+
                                            "<div class='col-sm-9'>"+
                                            "<input type='text' id='role_name' name='role_name' placeholder='请输入角色名称' class='form-control' value='"+rname+"' />"+
                                            "</div>"+
                                        "</div>"+
                                        "<div class='space-4'>"+
                                        "</div>"+
                                        "<div class='form-group'>"+
                                            "<label class='col-sm-3 control-label no-padding-right' for='txtNewPwd1'>角色类型</label>"+
                                            "<div class='col-sm-9'>" +
                                                "<select  name='is_admin' id='is_admin'  class='form-control'>"+
                                                    "<option value=''>------请选择------</option>"+
                                                    "<option value='2'>普通角色</option>"+
                                                    "<option value='1'>管理员</option>"+
                                                    "<option value='0'>超级管理员</option>"+
                                                "</select>"+
                                            "</div>"+
                                        "</div>"+
                                        "<div class='space-4'>"+
                                        "</div>"+
                                        "<div class='form-group'>"+
                                            "<label class='col-sm-3 control-label no-padding-right' for='txtNewPwd2'>备注</label>"+
                                            "<div class='col-sm-9'>" +
                                                "<input type='text' id='remark' placeholder='备注' class='form-control' value='"+rremark+"' />"+
                                            "</div>"+
                                        "</div>"+
                                        "</form>"+
                                    "</div>",
                        buttons : {
                            "success" : {
                                "label" : "<i class='icon-ok'></i> 保存",
                                "className" : "btn-sm btn-success",
                                "callback" : function() {
                                    var role_name = $("#role_name").val();
                                    var is_admin = $("#is_admin").val();
                                    if(role_name == ""){
                                        bootbox.alert("角色名称不能为空！");
                                        return false;
                                    }
                                    if(is_admin == ""){
                                        bootbox.alert("请选择角色类型！");
                                        return false;
                                    }
                                    var role = {};
                                    role["id"] = rid;
                                    role["role_name"] = role_name;
                                    role["is_admin"] = is_admin;
                                    role["remark"] = $("#remark").val();

                                    $.ajax({
                                        url: "updateRole.action",
                                        type: "POST",
                                        data: role,
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
                                                    window.location.href = "getAllRoles.action";
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
                    bootbox.alert("请选择角色再进行操作！");
                    return;
                }
            })

            //禁用/启用
            $("#checkStatus").click(function () {
                if(checkUserPermission("updateRoleStatus.action") != "true"){  //权限校验
                    bootbox.alert("您没有相应的权限操作，请联系管理员！");
                    return ;
                }
                var selectStruts = $("[type=radio]:checked").val();
                if (selectStruts != undefined && rid != "") {
                    var msg = "";
                    if (selectStruts == "1") {
                        msg = "禁用";
                    } else {
                        msg = "启用";
                    }
                    bootbox.dialog({
                        message: "确定" + msg + "该角色吗?",
                        title: "提示",
                        buttons: {
                            确定: {
                                label: "确定",
                                className: "btn-success",
                                callback: function () {
                                    var role = {};
                                    role["id"] = rid;
                                    if (selectStruts == "1") {
                                        role["status"] = 0;
                                    } else {
                                        role["status"] = 1;
                                    }
                                    $.ajax({
                                        url: "updateRoleStatus.action",
                                        type: "POST",
                                        data: role,
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
                                                    window.location.href = "getAllRoles.action";
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
                    bootbox.alert("请选择角色再进行操作！");
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
        <li><a href="#">系统角色管理</a></li>
    </ul>
</div>

<div class="formbody">
    <div style="width:100%;margin-bottom: 8px;height:36px;">
        <input type="button" value="新增角色" id="insert" class="btn btn-info"/>
        <input type="button" value="修改角色" id="update" class="btn  btn-warning"/>
        <input type="button" value="禁用/启用" id="checkStatus" class="btn btn btn-danger"/>
    </div>
    <div style="width:100%;margin-top: 10px">
        <table class="imgtable"  border="1" cellpadding="0" cellspacing="0" align="center" >
            <thead>
            <tr>
                <th style="display:none;">id</th>
                <th width="5%">选择</th>
                <th width="8%">角色名称</th>
                <th width="8%">角色类型</th>
                <th width="15%">描述</th>
                <th width="15%">状态</th>
                <th width="8%">修改时间</th>
            </tr>
            </thead>
            <tbody id="tRoles">
                <c:forEach items="${roles}" var="role">
                    <tr>
                        <td class="center" style='display:none;'>${role.id}</td>
                        <td class="center"><input type='radio' name='choose' value = '${role.status}'/></td>
                        <td class="center">${role.role_name}</td>
                        <c:if test="${role.is_admin==0}">
                            <td class="center">超级管理员</td>
                        </c:if>
                        <c:if test="${role.is_admin==1}">
                            <td class="center">管理员</td>
                        </c:if>
                        <c:if test="${role.is_admin==2}">
                            <td class="center">普通角色</td>
                        </c:if>
                        <td class="center">${role.remark}</td>
                        <c:if test="${role.status==1}">
                            <td class="center" style="color: green">有效角色</td>
                        </c:if>
                        <c:if test="${role.status==0}">
                            <td class="center" style="color: red">无效角色(该角色下的权限无法使用)</td>
                        </c:if>
                        <td class="center">${role.update_time}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>