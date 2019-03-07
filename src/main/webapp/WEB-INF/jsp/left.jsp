<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <title>无标题文档</title>

        <jsp:include page="/WEB-INF/jsp/js.jsp" flush="true"/>

        <style type="">
            body {
                font-family: '微软雅黑';
                margin: 0 auto;
                min-width: 70px;
                height: 500px;
            }
        </style>

        <script type="text/javascript">
            $(function () {
                var menuList = ${sessionScope.menuList};
                var sbStr = "";
                for (var i = 0; i < menuList.length; i++) {
                    sbStr += "<dd>" +
                        "<div class=\"title\">" +
                        " <span><img src=\"" +
                        menuList[i].iconCls +
                        " \" /></span>" +
                        menuList[i].name +
                        "</div>\n" +
                        "<ul class=\"menuson\">";
                    if (menuList[i].childMenus != null) {
                        for (var j = 0; j < menuList[i].childMenus.length; j++) {
                            sbStr += "<li><cite></cite><a href=\"" +
                                menuList[i].childMenus[j].url +
                                "\"  target=\"rightFrame\">" +
                                menuList[i].childMenus[j].name +
                                "</a><i></i></li>";
                        }
                    }
                    sbStr += "</ul></dd>";
                }
                if (sbStr.length != 0) {
                    $("#menus").append(sbStr);
                }
                //导航切换
                $(".menuson li").click(function () {
                    $(".menuson li.active").removeClass("active")
                    $(this).addClass("active");
                });

                $('.title').click(function () {
                    var $ul = $(this).next('ul');
                    $('dd').find('ul').slideUp();
                    if ($ul.is(':visible')) {
                        $(this).next('ul').slideUp();
                    } else {
                        $(this).next('ul').slideDown();
                    }
                });
            })
        </script>
    </head>

    <body style="background: #f0f9fd;" id="le">
        <div class="lefttop">
            <span></span>功能菜单
        </div>
        <dl class="leftmenu" id="menus">

        </dl>
    </body>
</html>
