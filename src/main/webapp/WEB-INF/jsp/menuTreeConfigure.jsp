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
    <title>用户菜单配置</title>

    <jsp:include page="js.jsp" flush="true"/>
    <link href="<%=path %>/css/bootstrapStyle.css" rel="stylesheet" >

    <style type="text/css">
        span{display: inline}
    </style>

    <SCRIPT type="text/javascript">
        var menus = ${menus};
        var users = ${users}

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

        //用户列表
        var tUser = "";
        $.each(users,function(index,user){
            tUser += "<tr><td style='display:none;'>" +
                    user.id+
                    "</td><td>"+
                    user.name+
                    "</td> <td>";
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
            tUser += "</td></tr>";
        })
        tUser += "<tr align='center' style='border-style:none;'>" +
            "<td style='display:none;'>false</td><td style='border-style:none;'></td>" +
            "<td colspan='2' style='margin-top: 10px; margin-bottom: 10px;border-style:none;'>" +
            "<input type='button' value='保存所选用户关联的权限' id='savePermission'  class='btn btn-primary'  />" +
            "</td><td style='border-style:none;'></td><td style='border-style:none;'></td></tr>";

        $(document).ready(function () {
            $("#tUsers").html(tUser);
            $.fn.zTree.init($("#treeDemo"), setting, zNodes);
            //点击用户列表时
            $("#tUsers tr").click(function(){
                var uid = $(this).children('td').eq(0).text();
                if(uid != 'false'){
                    $("#tUsers tr").removeAttr("style");
                    $(this).css("background","#D2E4EE");
                    $.ajax({
                        url: "getUserMenuPermissionByUserId.action",
                        type: "get",
                        data: {uid: uid},
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
            //点击‘保存所选用户关联的权限’按钮时
            $("#savePermission").click(function(){
                var trs = $("#tUsers tr");
                var isSelect = false;
                var uid = "";
                trs.each(function(){
                    if($(this)[0].style.background != ""){
                        isSelect =true;
                        uid = $(this).children('td').eq(0).text();
                    }
                })
                if(isSelect && uid != ""){
                    var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
                    var nodes = treeObj.getCheckedNodes(true);
                    var permissionsId = [];
                    for(var i= 0 ; i < nodes.length ; i++){
                        permissionsId[i] = nodes[i].id;
                    }
                    $.ajax({
                        url: "insertUserMenuPermissionByUserId.action",
                        type: "POST",
                        data: {uid:uid,permissionsId:permissionsId},
                        dataType: "text",
                        traditional: true,//传数组进后台需要设置该属性
                        success: function (data) {
                            bootbox.alert(data);
                        }
                    });
                }else{
                    bootbox.alert("请选择用户并为用户配置菜单权限！");
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
            <li><a href="#">菜单&权限管理</a></li>
            <li><a href="#">用户&菜单&权限配置</a></li>
        </ul>
    </div>

    <div class="formbody">
        <div style="width:70%;float: left;">
            <table class="table table-bordered table-hover">
                <thead>
                    <tr>
                        <th style="display:none;">id</th>
                        <th>系统账号</th>
                        <th>用户昵称</th>
                        <th>上次登录时间</th>
                        <th>ip</th>
                        <th>上次登录地</th>
                    </tr>
                </thead>
                <tbody id="tUsers">

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