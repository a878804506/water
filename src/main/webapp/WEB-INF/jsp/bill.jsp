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

    <jsp:include page="js.jsp" flush="true"/>

    <style type="text/css">
        .colorFont {
            color: red;
            font-weight: 900;
            font-size: 20px;
        }

        tr td {
            font-size: 20px;
        }

        .colorF {
            font-weight: 900;
            font-size: 18px;
        }

        #select {
            float: right;
            margin-top: -36px;
            margin-right: 5%;
        }

        .myDiv {
            border: 1px solid black;
            overflow: scroll;
            overflow-x: hidden;
            font-size: 12px;
            float: right;
            margin-right: 5%;
        }

        .myDiv a {
            text-decoration: none;
            display: block;
            height: 18px;
            line-height: 18px;
            text-indent: 4px;
            text-align: left;
        }

        .myDiv a:link, .myDiv a:visited {
            color: #2A1B00;
            background-color: #FFF0D9;
        }

        .myDiv a:hover, .myDiv a:active {
            background-color: #C2C660;
        }

        .inputType {
            height: 32px;
            border: solid 1px green;
            width: 100px;
        }
    </style>
    <script type="text/javascript">
        $(function () {
            var operate_cutomer_count_date = ${operate_cutomer_count_date };
            //导出Excel
            $("#excel").click(function () {
                var symbol = $("#symbol").val();
                if (symbol == 1) {
                    //iframe 下载xecel
                    $("#downloadExcelForm").submit();
                    //清空数据
                    emptyForm();
                    //如果今天开票100户了  给个休息的提示
                    if (operate_cutomer_count_date == 100) {
                        bootbox.alert("三哥！今天开票100户了，休息一下吧！");
                    }
                } else {
                    bootbox.alert("请先生成了收据！");
                    return;
                }
            })
            //生成收据 按钮
            $("#createB").click(function () {
                var count = ${countSize};		//最大用户编号
                var reg = new RegExp("^[0-9]*$");
                var cid = $("#id").val();
                if (cid > count) {
                    bootbox.alert("输入的用户编号不能大过最大用户编号(" + count + ")！");
                    return;
                }
                if (!reg.test(cid)) {		//校验输入的用户编号是不是非负数
                    bootbox.alert("用户编号请输入整数！");
                    return;
                }
                if (cid == 0 || cid == "" || cid == null) {
                    bootbox.alert("用户编号非法！");
                    return;
                }
                var zm = $("#zm").val();
                reg = /^[0-9]*$/;//用来验证数字，包括小数的正则
                if (!reg.test(zm)) {
                    bootbox.alert("水表止码请输入正整数！");
                    return;
                }
                if (zm == 0 || zm == "" || zm == null) {
                    bootbox.alert("水表止码非法！");
                    return;
                }
                //表单提交前，根据输入的月份和用户编号--->校验输入止码的正确性
                // month:月份    id:用户编号
                $.ajax({
                    url: "checkWaterMeterLastNumber.action",
                    type: "get",
                    data: {month: $("#month").val(), customerNum: $("#id").val()},
                    dataType: "json",
                    success: function (data) {
                        if (Number(data.WaterMeterNumber) > Number($("#zm").val())) {
                            bootbox.alert("该用户上月止码为：" + data.WaterMeterNumber + ",请输入比该数字大的数字作为本月止码或者修改该用户的起止码！");
                            return;
                        }
                        if (Number($("#zm").val()) - Number(data.WaterMeterNumber) > 50) {
                            bootbox.dialog({
                                message: "该用户上月止码：" + data.WaterMeterNumber + ",用水量已经超过50吨水，请确认是否继续？",
                                title: "提示",
                                buttons: {
                                    确定: {
                                        label: "确定",
                                        className: "btn-success",
                                        callback: function () {
                                            $("#createBill").submit();
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
                            $("#createBill").submit();
                        }
                    }
                });
            })
            //ajax输入框
            $("#txt1").keyup(function () {
                showDiv();
                $.ajax({
                    url: "ajax.action",
                    type: "post",
                    data: {name: $("#txt1").val()},
                    dataType: "json",
                    success: function (data) {
                        if ($("#txt1").val() == "") {
                            hideDiv();
                            return;
                        }
                        $("#sel").html("");
                        jQuery.each(data, function (key, value) {
                            var y = '<div><a href="javascript:;" onclick="getValue(' + value.cid + ');">' + value.cid + ':' + value.cname + '</a></div>'
                            $("#sel").append(y);
                        })
                    }
                });
            })
            //月份下拉框改变事件
            $("#month").change(function () {
                $.ajax({
                    url: "getTotalMonthCount.action",
                    type: "post",
                    data: {month: $("#month").val()},
                    dataType: "json",
                    success: function (data) {
                        $("#totalMonthCount").text($("#month").val() + "月份已经开票总数：" + data.totalMonthCount);
                    }
                });
            });
        })

        function showDiv() {
            var div = document.getElementById('sel');
            div.style.visibility = "visible";
        }

        function hideDiv() {
            var div = document.getElementById('sel');
            div.style.visibility = "hidden";
        }

        function getValue(a) {
            $("#id").val(a);
            hideDiv();
        }

        //清空数据
        function emptyForm() {
            $("#id").val("");
            $("#zm").val("");
            $(".colorFont").text("");
            $("#symbol").val(0);
        }
    </script>
</head>

<div>
    <div class="place">
        <span>位置：</span>
        <ul class="placeul">
            <li><a href="#">首页</a></li>
            <li><a href="#">月水费管理</a></li>
            <li><a href="#">个人水费账单生成</a></li>
        </ul>
    </div>

    <div class="formbody">

        <div class="formtitle">
            <span>用户基本信息</span>
            <c:choose>
                <c:when test="${operate_cutomer_count_date<100}">
                    <a class="formtitle_count">三哥！今天已经开票：${operate_cutomer_count_date }&nbsp;户</a>
                </c:when>
                <c:when test="${operate_cutomer_count_date >=100}">
                    <a class="formtitle_count1">三哥！今天已经开票：${operate_cutomer_count_date }&nbsp;户</a>
                </c:when>
            </c:choose>
            <a class="formtitle_count2" id="totalMonthCount">${mm}月份已经开票总数：${totalMonthCount }</a>
        </div>
        <table>
            <tr>
                <td>
                    <form action="addBill.action?password=${pwd }" method="post" id="createBill">
                        <a class="colorF">月份：</a>
                        <c:if test="${pwd==1 }"> <!-- 用户点击 "个人水费账单生成"时，展示的页面"-->
                            <select id="month" style="width:70px; height:32px;" name="month">
                                <c:forEach begin="1" end="12" step="1" var="i">
                                    <c:if test="${mm==i }">
                                        <option value="${i}" selected>${i }</option>
                                    </c:if>
                                    <c:if test="${mm !=i }">
                                        <option value="${i}">${i }</option>
                                    </c:if>
                                </c:forEach>
                            </select>
                        </c:if>
                        <c:if test="${pwd==2 }"> <!-- 用户点击 "1-2月份个人水费账单"时，展示的页面"-->
                            <select id="month" style="width:70px; height:32px;" name="month">
                                <option value="13" selected>1-2</option>
                            </select>
                        </c:if>
                        <c:if test="${pwd==4 }"> <!-- 用户点击 "3-4月份个人水费账单"时，展示的页面"-->
                            <select id="month" style="width:70px; height:32px;" name="month">
                                <option value="14" selected>3-4</option>
                            </select>
                        </c:if>
                        &nbsp;&nbsp;
                        <a class="colorF">用户编号：</a><input name="id" type="text" class="inputType" placeholder="编号"
                                                          required id="id" value="${mi.uid }"/></li>&nbsp;&nbsp;
                        <a class="colorF">水表止码：</a><input name="lastNumber" type="text" class="inputType"
                                                          placeholder="月度止码" required value="${mi.lastNumber }"
                                                          id="zm"/>&nbsp;&nbsp;
                        <input name="" type="button" class="btn btn-primary" value="生成收据" id="createB"/>
                        &nbsp;&nbsp;
                    </form>
                </td>

                <td>
                    <form action="downloadExcel.action?password=${pwd }" method="post" id="downloadExcelForm">
                        <input name="" type="button" class="btn btn-primary" value="下载Excel" id="excel"/>
                    </form>
                </td>
            </tr>
        </table>


        <div style="Float:left; position:absolute ;  margin-left:730px;">
            <div id="select">
                <input type='text' id="txt1" class="myTxt" placeholder="按姓名搜索用户" style="width:150px; height:32px;"
                       onclick="javascript:this.value='' "/>
            </div>
            <div id='sel' class='myDiv' style="left:10px; top:40px; width:150px;max-height:350px;visibility:hidden;">
            </div>
        </div>

        <br/>
        <br/>
        <table align="center" valgin="center" width="60%" border="1" cellpadding="0" cellspacing="0">
            <tr>
                <c:if test="${pwd==1}"> <!-- 预览票据时判断是从哪个入口进的页面 -->
                    <td colspan="13" align="center">${mi.year }年<a class="colorFont">${mi.month }</a>月</td>
                </c:if>
                <c:if test="${pwd==2}"> <!-- 预览票据时判断是从哪个入口进的页面    ${mi.lianyue } 这是定死的数据-->
                    <td colspan="13" align="center">${mi.year }年<a class="colorFont">${mi.lianyue }</a>月</td>
                </c:if>
                <c:if test="${pwd==4}"> <!-- 预览票据时判断是从哪个入口进的页面    ${mi.lianyue } 这是定死的数据-->
                    <td colspan="13" align="center">${mi.year }年<a class="colorFont">${mi.lianyue }</a>月</td>
                </c:if>
            </tr>
            <tr align="center">
                <td width="100" height="35">用户</td>
                <td width="120" colspan="2" class="colorFont">${mi.uname }</td>

                <td width="81">住址</td>
                <td width="63" colspan="8" class="colorFont">${mi.address }</td>
                <td width="63">备注</td>
            </tr>
            <tr>
                <td rowspan="2">项目及类别</td>
                <td colspan="2" align="center" valign="middle" height="40">水表动态</td>
                <td rowspan="2"><p style="font-size:20px;">&nbsp;用水量</p>
                    <p style="font-size:20px;">(立方米)</p></td>
                <td rowspan="2" align="center">单价</td>
                <td align="center" colspan="7">金额</td>
                <td rowspan="5"></td>
            </tr>
            <tr>
                <td align="center">起码</td>
                <td align="center">止码</td>
                <td>万</td>
                <td>千</td>
                <td>百</td>
                <td>十</td>
                <td>元</td>
                <td>角</td>
                <td>分</td>
            </tr>
            <tr align="center">
                <td>计量水费</td>
                <td class="colorFont">${mi.firstNumber }</td>
                <td class="colorFont">${mi.lastNumber }</td>
                <c:if test="${mi.lastNumber-mi.firstNumber !=0}">
                    <td class="colorFont">${mi.lastNumber-mi.firstNumber }</td>
                </c:if>
                <c:if test="${mi.lastNumber-mi.firstNumber ==0}">
                    <td class="colorFont">${mi.ling }</td>
                </c:if>
                <td class="colorFont">${mi.price }</td>
                <td class="colorFont">${mi.wan }</td>
                <td class="colorFont">${mi.qian }</td>
                <td class="colorFont">${mi.bai }</td>
                <td class="colorFont">${mi.shi }</td>
                <td class="colorFont">${mi.yuan }</td>
                <td class="colorFont">${mi.jiao }</td>
                <td class="colorFont">${mi.ling }</td>
            </tr>
            <tr align="center">
                <td>容量水费</td>
                <td colspan="4"></td>
                <td class="colorFont"></td>
                <td class="colorFont"></td>
                <td class="colorFont"></td>
                <c:if test="${pwd==1}">
                    <td class="colorFont"></td>
                </c:if>
                <c:if test="${pwd==2}">
                    <td class="colorFont">${mi.twoandtwo }</td>
                </c:if>
                <c:if test="${pwd==4}">
                    <td class="colorFont">${mi.twoandtwo }</td>
                </c:if>

                <c:if test="${pwd==1}">
                    <td class="colorFont">${mi.wu }</td>
                </c:if>
                <c:if test="${pwd==2}">
                    <td class="colorFont">${mi.ling }</td>
                </c:if>
                <c:if test="${pwd==4}">
                    <td class="colorFont">${mi.ling }</td>
                </c:if>

                <td class="colorFont">${mi.ling }</td>
                <td class="colorFont">${mi.ling }</td>
            </tr>
            <tr align="center">
                <td>合计大写</td>
                <td colspan="4" align="center" class="colorFont" id="da">${mi.daxie }</td>
                <td class="colorFont">${mi.wan1 }</td>
                <td class="colorFont">${mi.qian1 }</td>
                <td class="colorFont">${mi.bai1 }</td>
                <td class="colorFont">${mi.shi1 }</td>
                <td class="colorFont">${mi.yuan1 }</td>
                <td class="colorFont">${mi.jiao }</td>
                <td class="colorFont">${mi.ling }</td>
            </tr>
            <tr>
                <td border="0" align="center">合计：</td>
                <td>&nbsp;</td>
                <td colspan="2">复核：</td>
                <td colspan="6">收款：</td>
                <td colspan="3">开票：</td>
            </tr>
        </table>

        <div id="bu">

        </div>
        <!-- 隐藏域 -->
        <input type="hidden" value="${mi.firstTotal}" id="total"/>

        <!-- 标识符号  用于判断点击‘导出Excel’按钮时，是否生成了收据 -->
        <input type="hidden" value="${symbol}" id="symbol"/>
    </div>
</div>

</body>

</html>
