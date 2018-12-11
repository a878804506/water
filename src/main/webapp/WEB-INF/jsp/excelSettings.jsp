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
        $(function () {
            $("#save").click(function () {
                if(checkUserPermission("excelSrcSetting.action") != "true"){  //权限校验
                    bootbox.alert("您没有相应的权限操作，请联系管理员！");
                    return ;
                }
                if ($("#a").val().length != 0) {
                    $.ajax({
                        url: "excelSrcSetting.action",
                        type: "POST",
                        data: {excelSrc: $("#a").val()},
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
                                    window.location.href = "getExcelPath.action";
                                }
                            });
                        }
                    });
                } else {
                    bootbox.alert("请填写文件路径！");
                    return;
                }
            })
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
    </script>
</head>


<body>
    <div class="place">
        <span>位置：</span>
        <ul class="placeul">
            <li><a href="#">首页</a></li>
            <li><a href="#">数据管理</a></li>
            <li><a href="#">Excel导出路径配置</a></li>
        </ul>
    </div>
    <div style="margin-left:2%;margin-top:2%">
        <div class="rightinfo">
            <div class="tools">
                <ul>
                    <li style="display: inline-block;"><img src="<%=path %>/images/path.png" alt="时间"/></span>请填入Excel导出的路径</span></li>
                </ul>
                <p></p>
                <ul style="float: left" class="toolbar1">
                    <li style="display: inline-block;">
                        <input type="text"  name="name" placeholder="请输入Excel配置路径" value="${excelSettings.path }" id="a" style="width:470px"/>
                    </li>
                    <li style="display: inline-block;"><input name="" type="button" class="btn  btn-info" value="保存" id="save"/></li>
                </ul>
            </div>
        </div>
    </div>
</body>

</html>
