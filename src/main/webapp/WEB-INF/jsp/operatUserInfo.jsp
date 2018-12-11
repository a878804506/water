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
            $("#submit").click(function () {
                if(checkUserPermission("updateUserInfoByUserId.action") != "true"){  //权限校验
                    bootbox.alert("您没有相应的权限操作，请联系管理员！");
                    return ;
                }
                if($("#password").val() != "" || $("#password").val() != null){
                    if($("#password").val() !=  $("#againPassword").val()){
                        bootbox.alert("两次输入的密码不一致！");
                        return ;
                    }
                }
                $.ajax({
                    url: "updateUserInfoByUserId.action",
                    type: "POST",
                    data: {nickName: $("#nickName").val(),password:$("#password").val(),img:$("#imgStr").val()},
                    dataType: "text",
                    async:false, //使用同步
                    success: function (data) {
                        var dataJson = JSON.parse(data);
                        bootbox.alert({
                            buttons: {
                                ok: {
                                    label: "确定",
                                    className: 'btn-primary'
                                }
                            },
                            message: dataJson.msg,
                            callback: function () {
                                // 清空
                                clear();
                                $("#nickName").val("");
                                $("#password").val("");
                                $("#againPassword").val("");
                                $("#imgStr").val("");
                                /*window.location.href = "getUserInfoByUserId.action";*/
                                /*window.parent.topFrame.location.reload();*/
                                if(dataJson.result == true){
                                    $("#userPicture",parent.topFrame.document).attr("src",dataJson.img);
                                    $("#nickName",parent.topFrame.document).text(dataJson.nickName);
                                }
                            }
                        });
                    }
                });
            })
        })

        // 选择图片后预览
        function preview(fileObject){
            var image = fileObject.files[0] ;
            var AllowImgFileSize = 2100000; //上传图片最大值(单位字节)（ 2 M = 2097152 B ）超过2M上传失败
            //名称
            var fileName = image.name;
            //大小 字节
            var fileSize = image.size;
            //类型
            var fileType = image.type;
            // 判断文件类型
            if(fileType != ".jpg" && fileType != ".JPG" && fileType != "image/jpeg" &&
                fileType != ".png" && fileType != ".PNG" && fileType != "image/png"  &&
                fileType != ".gif" && fileType != ".GIF" && fileType != "image/gif" &&
                fileType != ".bmp" && fileType != ".BMP" && fileType != "image/bmp"){
                // 清空
                clear();
                bootbox.alert("请选择图片组作为头像!");
                return;
            }
            // 大小 字节
            if(fileSize > AllowImgFileSize){
                // 清空
                clear();
                bootbox.alert("上传失败，请上传不大于2M的图片!");
                return;
            }

            var reader = new FileReader();
            var imgUrlBase64;
            var imgBase64 ;
            if (image) {
                //将文件以Data URL形式读入页面
                imgUrlBase64 = reader.readAsDataURL(image);
                reader.onload = function (e) {
                    //执行上传操作
                    imgBase64 = reader.result;
                    // base64 放入隐藏域
                    $("#imgStr").val(imgBase64);
                    // 预览
                    $("#previewImg").attr("src",imgBase64);
                    $("#previewImg").show();
                }
            }
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

        function clear(){
            // 清空文件域
            var file = $("#img") ;
            file.after(file.clone().val(""));
            file.remove();
            // 清空隐藏域
            $("#imgStr").val("");
            // 清空 预览图片
            $("#previewImg").hide();
        }
    </script>
    <style type="text/css">
        #previewImg{
            border-radius:110px;
        }
    </style>
</head>


<body>
    <div class="place">
        <span>位置：</span>
        <ul class="placeul">
            <li><a href="#">首页</a></li>
            <li><a href="#">个人中心</a></li>
            <li><a href="#">个人信息修改</a></li>
        </ul>
    </div>
    <div style="margin-left:6%;margin-top:5%">
        <div class="rightinfo">
            <div class="tools">
                <div class='well ' style='width:60%;'>
                    <form class='form-horizontal' role='form'>
                        <div class='form-group'>
                            <label class='col-sm-2 control-label no-padding-right' >新昵称</label>
                            <div class='col-sm-4'>
                                <input type='text' id='nickName' name='nickName' placeholder='新昵称' class='form-control' />
                            </div>
                        </div>
                        <div class='form-group'>
                            <label class='col-sm-2 control-label no-padding-right' >新密码</label>
                            <div class='col-sm-4'>
                                <input type='password' id='password' name='password' placeholder='请输入密码' class='form-control' />
                            </div>
                        </div>
                        <div class='space-4'>
                        </div>
                        <div class='form-group'>
                            <label class='col-sm-2 control-label no-padding-right' >确认密码</label>
                            <div class='col-sm-4'>
                                <input type='password' id='againPassword' placeholder='再次输入密码' class='form-control' />
                            </div>
                        </div>
                        <div class='space-4'>
                        </div>
                        <div class='form-group'>
                            <label class='col-sm-2 control-label no-padding-right' >新头像</label>
                            <div class='col-sm-4'>
                                <input id="img" name="img" type="file" data-show-caption="true" onchange="preview(this);">
                                <p class="help-block">支持jpg、jpeg、png、gif格式，大小不超过2.0M</p>
                                <input id="imgStr" name="img" type="hidden" value="">
                            </div>
                            <div class='col-sm-6'>
                                <img src="" alt="" id="previewImg" style="width:110px;height: 110px" />
                            </div>
                        </div>
                        <div class='space-4'>
                        </div>
                        <div class='form-group'>
                            <label class='col-sm-2 control-label no-padding-right' ></label>
                            <div class='col-sm-3'>
                                <input type='button' id='submit' value='提交' class='btn btn-primary form-control' />
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
