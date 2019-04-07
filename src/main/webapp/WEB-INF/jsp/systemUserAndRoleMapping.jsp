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
            var userId = ""; //用户id
            //点击用户列表时
            $("#userTab tr").click(function () {
                $("#userTab tr").css("background", ""); //移除所有
                $(this).css("background", "#D2E4EE");//为当前点击的行添加样式
                userId =  $(this).children('td').eq(0).text();
                //查询该用户的角色列表并自动勾选相应角色，供用户操作
                $.ajax({
                    url: "getRolesByUserId.action",
                    type: "post",
                    data: {userId: $(this).children('td').eq(0).text()},
                    dataType: "json",
                    success: function (data) {
                        //取消所有勾选
                        $('input[name=role]').prop('checked', false);
                        var a = $("#roleTab input:checkbox");
                        $.each(data,function(index,roleId){
                            $.each(a,function(){
                                if($(this).val() == roleId){
                                    //匹配勾选
                                    $(this).prop("checked",true);
                                }
                            })
                        })
                    }
                });
            });

            //点击角色列表时
            $("#roleTab tr").click(function (event) {
                var o=event.srcElement||event.target;
                if(o.type=='checkbox')///点击的是checkbox退出函数不执行下面的代码
                    return;

                if($(this).children('td').eq(1).find("input").prop("checked")){
                    $(this).children('td').eq(1).find("input").prop("checked",false);
                }else{
                    $(this).children('td').eq(1).find("input").prop("checked",true);
                }
            });

            //保存关联按钮
            $("#save").click(function (event) {
                if(userId == ""){
                    bootbox.alert("请为用户配置角色！");
                    return ;
                }
                var roleId =[];
                $('input[name=role]:checked').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
                    roleId.push($(this).val());//将选中的值添加到数组chk_value中
                });
                $.ajax({
                    url: "saveUserAndRolesMapping.action",
                    type: "post",
                    data: {userId: userId,roleIds: roleId},
                    dataType: "text",
                    traditional: true,//传数组进后台需要设置该属性
                    success: function (data) {
                        bootbox.alert(data);
                        console.log(data);
                    }
                });
            });
        })
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
        <li><a href="#">系统管理</a></li>
        <li><a href="#">用户与角色管理</a></li>
    </ul>
</div>
<div class="rightinfo">
    <div style="width:100%;margin-bottom: 8px;height:36px;">
        <input type="button" value="保存关联" id="save" class="btn btn-primary"/>
    </div>
    <div style="width: 49%;height: 700px;float: left">
        <table class="imgtable"  border="1" cellpadding="0" cellspacing="0" align="center" >
            <thead >
                <tr>
                    <th width="25%" >账号</th>
                    <th width="25%" >用户昵称</th>
                    <th width="25%" >用户状态</th>
                </tr>
            </thead>
            <tbody id="userTab">
                <c:forEach items="${users}" var="user">
                    <tr>
                        <td class="center" style='display:none;'>${user.id}</td>
                        <td class="center">${user.name}</td>
                        <td class="center">${user.nickName}</td>
                        <c:if test="${user.struts==1}">
                            <td class="center" style="color: green">已启用</td>
                        </c:if>
                        <c:if test="${user.struts==0}">
                            <td class="center" style="color: red">已冻结(该用户无法登陆系统)</td>
                        </c:if>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <div style="width: 49%;height: 700px;float: right">
        <table class="imgtable"  border="1" cellpadding="0" cellspacing="0" align="center" >
            <thead >
                <tr>
                    <th width="10%" >选择</th>
                    <th width="15%" >角色名称</th>
                    <th width="15%" >角色类型</th>
                    <th width="25%" >描述</th>
                    <th width="25%" >状态</th>
                </tr>
            </thead>
            <tbody id="roleTab">
                <c:forEach items="${roles}" var="role">
                    <tr>
                        <td class="center" style='display:none;'>${role.id}</td>
                        <td class="center"><input name="role" type="checkbox" value="${role.id}" /></td>
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
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
