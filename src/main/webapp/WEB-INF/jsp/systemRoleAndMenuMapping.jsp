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
    <title>角色菜单配置</title>

    <jsp:include page="js.jsp" flush="true"/>
    <link href="<%=path %>/css/bootstrapStyle.css" rel="stylesheet" >

    <style type="text/css">
        span{
            display: inline
        }
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
        var menus = ${menus};

        var setting = {
            view: {
                //addHoverDom: addHoverDom,
                //removeHoverDom: removeHoverDom,
                selectedMulti: false
            },
            check: {
                enable: true
            },
            data: {
                simpleData: {
                    enable: true
                }
            },
            edit: {
                enable: false
            }
        };
        //菜单
        var zNodes = getMenuTreeData(null);

        $(document).ready(function () {
            $.fn.zTree.init($("#treeDemo"), setting, zNodes);
            //点击角色列表时
            $("#tRoles tr").click(function(){
                var rid = $(this).children('td').eq(0).text();
                if(rid != 'false'){
                    $("#tRoles tr").removeAttr("style");
                    $(this).css("background","#D2E4EE");
                    $.ajax({
                        url: "getMenuPermissionByRoleId.action",
                        type: "post",
                        data: {rid: rid},
                        dataType: "json",
                        success: function (data) {
                            if(data != null){
                                var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");
                                var node = zTreeObj.getNodes();
                                var nodes = zTreeObj.transformToArray(node);
                                $.each(nodes,function(index,node){
                                    var isChecked = false ;
                                    $.each(data,function(index,d){
                                        if(node.id == d){
                                            isChecked = true;
                                            node.checked = true;
                                        }
                                    })
                                    if(isChecked){
                                        zTreeObj.updateNode(node);
                                    }else{
                                        node.checked = false;
                                        zTreeObj.updateNode(node);
                                    }
                                })
                            }

                        }
                    });
                }
            });
            //点击‘保存关联’按钮时
            $("#savePermission").click(function(){
                var trs = $("#tRoles tr");
                var isSelect = false;
                var rid = "";
                trs.each(function(){
                    if($(this)[0].style.background != ""){
                        isSelect =true;
                        rid = $(this).children('td').eq(0).text();
                    }
                })
                if(isSelect && rid != ""){
                    var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
                    var nodes = treeObj.getCheckedNodes(true);
                    var permissionsId = [];
                    for(var i= 0 ; i < nodes.length ; i++){
                        permissionsId[i] = nodes[i].id;
                    }
                    $.ajax({
                        url: "insertMenuPermissionByRoleId.action",
                        type: "POST",
                        data: {rid:rid,permissionsId:permissionsId},
                        dataType: "text",
                        traditional: true,//传数组进后台需要设置该属性
                        success: function (data) {
                            bootbox.alert(data);
                        }
                    });
                }else{
                    bootbox.alert("请选择角色户并为角色配置菜单权限！");
                    return;
                }
            })
        });
        // 初始化菜单，也可根据参数来初始化
        function getMenuTreeData(data){
            var zNodes = [];
            $.each(menus,function(index,menu){
                var menuObject=new Object();
                if(menu.parentId == 1){
                    menuObject.id=menu.id;
                    menuObject.pId=menu.parentId;
                    menuObject.name=menu.name;
                    menuObject.open=true;
                    zNodes.push(menuObject);
                    /*if(menu.id == 11 || menu.id == 21){
                        menuObject.id=menu.id;
                        menuObject.pId=menu.parentId;
                        menuObject.name=menu.name;
                        menuObject.open=true;
                        zNodes.push(menuObject);
                    }else{
                        menuObject.id=menu.id;
                        menuObject.pId=menu.parentId;
                        menuObject.name=menu.name;
                        menuObject.open=false;
                        zNodes.push(menuObject);
                    }*/
                }else{
                    menuObject.id=menu.id;
                    menuObject.pId=menu.parentId;
                    menuObject.name=menu.name;
                    zNodes.push(menuObject);
                }
                if(menu.childMenus != undefined ){
                    $.each(menu.childMenus,function(cIndex,childMenu){
                        var menuObject=new Object();
                        menuObject.id=childMenu.id;
                        menuObject.pId=childMenu.parentId;
                        menuObject.name=childMenu.name;
                        zNodes.push(menuObject);
                        if(childMenu.childMenus != undefined){
                            $.each(childMenu.childMenus,function(dIndex,childchildMenu){
                                var menuObject=new Object();
                                menuObject.id=childchildMenu.id;
                                menuObject.pId=childchildMenu.parentId;
                                menuObject.name=childchildMenu.name;
                                zNodes.push(menuObject);
                            })
                        }
                    })
                }

            });
            if(data != null){
                $.each(zNodes,function(index,node){
                    $.each(data,function(index,d){
                        if(node.id == d){
                            node.checked = true;
                        }
                    })
                })
            }
            return zNodes;
        }
    </SCRIPT>
</head>

<body>
    <div class="place">
        <span>位置：</span>
        <ul class="placeul">
            <li><a href="#">首页</a></li>
            <li><a href="#">系统管理</a></li>
            <li><a href="#">角色与菜单管理</a></li>
        </ul>
    </div>

    <div class="formbody">
        <div style="width:100%;margin-bottom: 8px;height:36px;">
            <input type="button" value="保存关联" id="savePermission" class="btn btn-primary"/>
        </div>
        <div style="width:60%;float: left;">
            <table class="imgtable"  border="1" cellpadding="0" cellspacing="0" align="center" >
                <thead>
                    <tr>
                        <th style="display:none;">id</th>
                        <th width="10%">角色名称</th>
                        <th width="10%">角色类型</th>
                        <th width="25%">描述</th>
                        <th width="20%">状态</th>
                    </tr>
                </thead>
                <tbody id="tRoles">
                    <c:forEach items="${roles}" var="role">
                        <tr>
                            <td class="center" style='display:none;'>${role.id}</td>
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
        <!-- border:2px solid #F00; -->
        <div style="width:18%;float: left;margin-left: 3%">
            <ul id="treeDemo" class="ztree"></ul>
        </div>
    </div>
</body>
</html>