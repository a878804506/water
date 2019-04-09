var socket;
var sessionId = $("#thisUserSessionId").val();
var userId = $("#thisUserId").val();
var webSocketChatSwitch = $("#webSocketChatSwitch").val();
var webSocketChatAddress = $("#webSocketChatAddress").val();
var projectPath = $("#projectPath").val();

var me ; //自己
var struts ="-1"; //当前窗体所在位置      -1界面最小化状态/0：联系人列表界面       其他(联系人id)：当前处在与该联系人的聊天窗口的界面
var contactsList; //最新联系人列表
var contactsListForPicture = new Map(); // key : userId    value : img/grayImg

//联系人上线 文字颜色切换效果
/* 红：255，0，0    #FF0000
橙:  255,125,0     #FF7D00
黄：255，255，0   #FFFF00
绿：0，255，0    #00FF00
蓝：0，0，255    #0000FF
靛: 0,255,255    #00FFFF
紫: 255,0,255    #FF00FF */
var colors = ["#FF0000","#FF7D00","#FFFF00","#00FF00","#0000FF","#00FFFF","#FF00FF"];
var colorIndex = 0;
var interval =null;

$(function(){
    if("true" == webSocketChatSwitch){

        //建立WebSocket连接
        createWebSocketClient(sessionId,userId);

        screenFuc();

        (window.onresize = function () {
            screenFuc();
        })();
        //未读信息数量为空时
        var totalNum = $(".chat-message-num").html();
        if (totalNum == "") {
            $(".chat-message-num").css("padding", 0);
        }
        $(".message-num").each(function () {
            var wdNum = $(this).html();
            if (wdNum == "") {
                $(this).css("padding", 0);
            }
        });

        //打开/关闭聊天框
        $(".chatBtn").click(function () {
            $(".chatBox").toggle(10);
            if("-1" == struts){
                $("#allMessage").empty();
                struts = "0";
            }else if("0" == struts){
                struts = "-1";
            }else{
                $(".chatBox-head-one").toggle(1);
                $(".chatBox-head-two").toggle(1);
                $(".chatBox-list").fadeToggle(1);
                $(".chatBox-kuang").fadeToggle(1);
                struts = "-1";
            }
            console.log("此时窗口状态为："+struts);
        })
        $(".chat-close").click(function () {
            $(".chatBox").toggle(10);

            if("0" != struts){
                $(".chatBox-head-one").toggle(1);
                $(".chatBox-head-two").toggle(1);
                $(".chatBox-list").fadeToggle(1);
                $(".chatBox-kuang").fadeToggle(1);
            }
            struts = "-1";
            console.log("此时窗口状态为："+struts);
        })

        //返回列表
        $(".chat-return").click(function () {
            struts = "0";
            $(".chatBox-head-one").toggle(1);
            $(".chatBox-head-two").toggle(1);
            $(".chatBox-list").fadeToggle(1);
            $(".chatBox-kuang").fadeToggle(1);
            console.log("此时窗口状态为："+struts);
        });

        //      发送信息
        $("#chat-fasong").click(function () {
            var textContent = $(".div-textarea").html().replace(/[\n\r]/g, '<br>')
            if (textContent != "") {
                $(".chatBox-content-demo").append("<div class=\"clearfloat\">" +
                    "<div class=\"author-name\"><small class=\"chat-date\">"+CurentTime()+"</small> </div> " +
                    "<div class=\"right\"> <div class=\"chat-message\"> " + textContent + " </div> " +
                    "<div class=\"chat-avatars\"><img src=\""+me.img+"\" alt=\"头像\" /></div> </div> </div>");
                //发送后清空输入框
                $(".div-textarea").html("");

                //webSocket 发送消息
                var sendMsg = {};
                sendMsg.id = "1";
                sendMsg.msgType = "0";
                sendMsg.from = userId;
                sendMsg.to = $("#toUserId").val();
                sendMsg.data = textContent;
                sendMsg.type = "2";
                send(sendMsg);

                //聊天框默认最底部
                $(document).ready(function () {
                    $("#chatBox-content-demo").scrollTop($("#chatBox-content-demo")[0].scrollHeight);
                });
            }
        });

        //      发送表情
        $("#chat-biaoqing").click(function () {
            $(".biaoqing-photo").toggle();
        });
        $(document).click(function () {
            $(".biaoqing-photo").css("display", "none");
        });
        $("#chat-biaoqing").click(function (event) {
            event.stopPropagation();//阻止事件
        });
        $(".emoji-picker-image").each(function () {
            $(this).click(function () {
                var bq = $(this).parent().html();
                $(".chatBox-content-demo").append("<div class=\"clearfloat\">" +
                    "<div class=\"author-name\"><small class=\"chat-date\">"+CurentTime()+"</small> </div> " +
                    "<div class=\"right\"> <div class=\"chat-message\"> " + bq + " </div> " +
                    "<div class=\"chat-avatars\"><img src=\""+me.img+"\" alt=\"头像\" /></div> </div> </div>");
                //发送后关闭表情框
                $(".biaoqing-photo").toggle();

                //webSocket 发送消息
                var sendMsg = {};
                sendMsg.id = "1";
                sendMsg.msgType = "1";
                sendMsg.from = userId;
                sendMsg.to = $("#toUserId").val();
                sendMsg.data = bq;
                sendMsg.type = "2";
                send(sendMsg);

                //聊天框默认最底部
                $(document).ready(function () {
                    $("#chatBox-content-demo").scrollTop($("#chatBox-content-demo")[0].scrollHeight);
                });
            })
        });
    }else{
        $('#webSocketChat').hide();
    }
})

// 选择图片后先校验是否为图片然后在调用selectImg 方法发送
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
        bootbox.alert("请选择图片!");
        return;
    }
    // 大小 字节   暂时解除 2M大小限制
    /*if(fileSize > AllowImgFileSize){
        bootbox.alert("不支持大于2M的图片!");
        return;
    }*/
    //发送图片
    selectImg(fileObject);
}
// 发送图片  pic是一个类型为file的input框
function selectImg(pic) {
    if (!pic.files || !pic.files[0]) {
        return;
    }
    var time = (new Date()).getTime();
    var radom = Math.floor(Math.random()*8999+1000);
    // 正则匹配文件后缀
    var fileExt=(/[.]/.exec(pic.files[0].name)) ? /[^.]+$/.exec(pic.files[0].name.toLowerCase()) : '';
    // 生成图片id
    var uuidForPic = userId+"_"+$("#toUserId").val()+"_"+time+"_"+radom+"."+fileExt[0];
    //文件上传的时候展示base64图片
    var readerInfo = new FileReader();
    readerInfo.readAsDataURL(pic.files[0]);
    readerInfo.onload = function (evt) {
        var base64Image = evt.target.result;
        $(".chatBox-content-demo").append("<div class=\"clearfloat\">" +
            "<div class=\"author-name\"><small class=\"chat-date\">"+CurentTime()+"</small> </div> " +
            "<div class=\"right\"> <div class=\"chat-message\">" +
            "<img src=" + base64Image + " id="+uuidForPic+" onclick=clickThisPic(\""+base64Image+"\");></div> " +
            "<div class=\"sending\" id=\""+radom+"\"></div>" +
            "<div class=\"chat-avatars\"><img src=\""+me.img+"\" alt=\"头像\" /></div> </div> </div>");
        //聊天框默认最底部
        $(document).ready(function () {
            $("#chatBox-content-demo").scrollTop($("#chatBox-content-demo")[0].scrollHeight);
        });
    }
    //组装报文头
    var sendInfoData = new Array(); // 发送信息
    sendInfoData.push(userId); // 0 发送者id
    sendInfoData.push($("#toUserId").val()); //1 接收者id
    sendInfoData.push("2"); // 2类型 发送的是图片
    sendInfoData.push(time); // 3时间戳
    sendInfoData.push(radom); //4随机数
    sendInfoData.push(fileExt[0]); //5文件后缀

    var blob = new Blob([sendInfoData],{type:'application/json'});
    var readHead = new FileReader();
    var head ;
    readHead.readAsArrayBuffer(blob);
    readHead.onload = function (evt){
        head = evt.target.result;
    }

    var reader = new FileReader();
    reader.readAsArrayBuffer(pic.files[0]);
    reader.onload = function (evt) {
        var image = evt.target.result;
        var temp = new ArrayBuffer(1+head.byteLength+image.byteLength);
        var view = new Int8Array(temp);
        var viewA = new Int8Array(head);
        var viewI = new Int8Array(image);
        view[0] = viewA.length;
        for (var i = 0 ; i < viewA.length ; i++){
            view[i+1] = viewA[i];
        }
        for (var i = 0 ; i < viewI.length ; i++){
            view[1+i+viewA.length] = viewI[i];
        }
        var baba = view.buffer;
        console.log(head);
        console.log(image);
        console.log(baba);
        if (!window.WebSocket) {
            return;
        }
        if (socket.readyState == WebSocket.OPEN) {
            socket.send(baba);//发送图片的二进制数据
        } else {
            $("#responseText").val("连接没有开启.");
        }
    }

}

// 创建连接、重连
function createWebSocketClient(sessionId,userId){
    var webSocketLogin  = "{\"id\":\""+sessionId+"\",\"userId\":\""+userId+"\"}";
    if (!window.WebSocket) {
        window.WebSocket = window.MozWebSocket;
    }
    if (window.WebSocket) {
        socket = new WebSocket("ws://"+webSocketChatAddress+":7379?webSocketLogin="+webSocketLogin);

        socket.onmessage = function(event) {
            var dataJson = JSON.parse(event.data);
            var ta = $("#responseText");
            if(dataJson.type == -1){ //是服务器消息
                ta.val(ta.val() + '\n' + dataJson.data);
            }else if(dataJson.type == 0){ //用户登陆   服务器推送的联系人列表消息
                //刷新成最新的联系人列表
                createNewContactsList(dataJson.data,true);
            }else if(dataJson.type == 1){//服务器推送过来最新联系人列表，有用户上线时的最新联系人列表
                //联系人上线声音提示
                document.getElementById("onLine").play();
                //刷新成最新的联系人列表
                createNewContactsList(dataJson.data,false);
                if(interval == null){
                    //上线渐变色
                    interval = setInterval("changeColor("+colorIndex+","+interval+")",800);
                }
            }else if(dataJson.type == 2){ //一对一聊天 有人发送消息给你
                //有人发送消息给你  声音提示
                document.getElementById("didi").play();
                ta.val(ta.val() + '\n有人发送消息给你：' + JSON.stringify(dataJson));

                if(dataJson.from == struts){ //当前窗体刚好处于与该联系人聊天的界面  则展示消息
                    var textContent = $(".div-textarea").html().replace(/[\n\r]/g, '<br>')
                    var img ;
                    for(var i = 0 ; i < contactsList.length ; i++){
                        if(dataJson.from == contactsList[i].id){
                            img = contactsList[i].img;
                            break;
                        }
                    }
                    var msg = dataJson.data;
                    if(dataJson.msgType == "2"){
                        msg = "<img src=\"" + dataJson.data + "\" onclick=clickThisPic(\""+dataJson.data+"\");>";
                    }
                    $(".chatBox-content-demo").append(
                        "<div class='clearfloat'>"+
                        "<div class='author-name'>"+
                        "<small class='chat-date'>"+dataJson.date+"</small>"+
                        "</div>"+
                        "<div class='left'>"+
                        "<div class='chat-avatars'><img src='"+img+"' alt='头像'/></div>"+
                        "<div class='chat-message'>"+
                        msg+
                        "</div>"+
                        "</div>"+
                        "</div>");
                    //聊天框默认最底部
                    $(document).ready(function () {
                        $("#chatBox-content-demo").scrollTop($("#chatBox-content-demo")[0].scrollHeight);
                    });
                    //告知服务器要移出未读消息记录数
                    var sendMsg = {};
                    sendMsg.id = "";
                    sendMsg.from = $("#toUserId").val();
                    sendMsg.to = userId;
                    sendMsg.type = "4";
                    send(sendMsg);

                }else{ //当前窗体处于与其他联系人聊天的界面或者是联系人列表界面  则添加红点
                    var unReadCount = $("#userId_"+dataJson.from).children(".i_").text();
                    if(unReadCount ==""){
                        unReadCount = 1;
                    }else{
                        unReadCount = Number(unReadCount) +Number(1);
                    }
                    $("#userId_"+dataJson.from).children(".i_").text(unReadCount);
                    $("#userId_"+dataJson.from).children(".i_").addClass("message-num");

                    if("-1" == struts) { //界面最小化状态  消息球则也要添加小红点
                        $("#allMessage").empty();
                        $("#allMessage").html("<img  src=\""+projectPath+"/images/message.gif\"/>");
                    }
                }
            }else if(dataJson.type == 3){ //拉取到一对一聊天记录后 展示出来
                ta.val(ta.val() + '\n一对一聊天记录：' + JSON.stringify(dataJson.data));
                var oneToOneHistoryMessage = "";
                $(".chatBox-content-demo").empty();
                var temp = JSON.parse(dataJson.data);
                for(var i = 0 ; i < temp.length ; i ++){
                    var msg = temp[i].data;
                    if(temp[i].msgType == "2"){
                        msg = "<img src=\"" + msg + "\" onclick=clickThisPic(\""+temp[i].data+"\");>";
                    }
                    if(temp[i].from == userId){  //说明是自己发送的历史消息  应该右边靠齐显示
                        oneToOneHistoryMessage +="<div class=\"clearfloat\">" +
                            "<div class=\"author-name\"><small class=\"chat-date\">"+temp[i].date+"</small> </div> " +
                            "<div class=\"right\"> <div class=\"chat-message\"> " + msg + " </div> " +
                            "<div class=\"chat-avatars\"><img src=\""+contactsListForPicture.get(temp[i].from)+"\" alt=\"头像\" /></div> </div> </div>";
                    }else{
                        oneToOneHistoryMessage +="<div class='clearfloat'>"+
                            "<div class='author-name'><small class='chat-date'>"+temp[i].date+"</small></div>"+
                            "<div class='left'><div class='chat-avatars'>" +
                            "<img src='"+contactsListForPicture.get(temp[i].from)+"' alt='头像'/></div>"+
                            "<div class='chat-message'>"+msg+"</div></div></div>";
                    }
                }
                $(".chatBox-content-demo").append(oneToOneHistoryMessage);
                //聊天框默认最底部
                $(document).ready(function () {
                    $("#chatBox-content-demo").scrollTop($("#chatBox-content-demo")[0].scrollHeight);
                });
            }else if(dataJson.type == 5){  //最新联系人列表  有用户下线时的最新联系人列表
                //刷新成最新的联系人列表
                createNewContactsList(dataJson.data,false);
            }else if(dataJson.type == 6){  // 对于客户端发出的心跳包 服务端给予了回应  type=6
                if(dataJson.stauts){
                    heartCheck.reset().start(); //拿到任何消息都说明当前连接是正常的  //如果后端有下发消息，那么就会重置初始化心跳检测，除非超时没下发，那么就会触发onclose
                }
            }else if(dataJson.type == 7){ // 服务器反馈图片发送的状态
                if(dataJson.sendPicStruts == true){
                    console.log("图片发送成功");
                    $("#"+dataJson.id).removeClass("sending");
                    $("#"+dataJson.id).addClass("sendSuccess");
                }else{
                    console.log("图片发送失败");
                }
            }else if(dataJson.type == 8){ //暂时还没有8

            }
        };

        socket.onopen = function(event) {
            $("#responseText").val("连接开启!");
            console.log("连接开启!");
            $("#contactsTable").removeClass("OverLoad");
            $("#chatBox_Ex").hide();
            $("#chatBox_Conning").hide();
            heartCheck.reset().start(); //心跳检测重置   在open的时候触发心跳检测
        };

        socket.onclose = function(event) {
            $("#responseText").val("连接被关闭!");
            console.log("连接关闭!");

            if(struts != "-1" && struts != "0"){
                $(".chat-return").click();
            }
            $("#contactsTable").empty();
            $("#chatBox_Conning").hide();
            $("#chatBox_Ex").show();
            $("#contactsTable").addClass("OverLoad");
        };

        socket.onerror = function(event) {
            console.log("出现异常！");
        };

    } else {
        $("#responseText").val("你的浏览器不支持！");
    }
}

function send(message) {
    if (!window.WebSocket) {
        return;
    }
    if (socket.readyState == WebSocket.OPEN) {
        socket.send(JSON.stringify(message));
    } else {
        $("#responseText").val("连接没有开启.");
    }
}

function screenFuc() {
    var topHeight = $(".chatBox-head").innerHeight();//聊天头部高度
    //屏幕小于768px时候,布局change
    var winWidth = $(window).innerWidth();
    if (winWidth <= 768) {
        var totalHeight = $(window).height(); //页面整体高度
        $(".chatBox-info").css("height", totalHeight - topHeight);
        var infoHeight = $(".chatBox-info").innerHeight();//聊天头部以下高度
        //中间内容高度
        $(".chatBox-content").css("height", infoHeight - 46);
        $(".chatBox-content-demo").css("height", infoHeight - 46);

        $(".chatBox-list").css("height", totalHeight - topHeight);
        $(".chatBox-kuang").css("height", totalHeight - topHeight);
        $(".div-textarea").css("width", winWidth - 106);
    } else {
        $(".chatBox-info").css("height", 495);
        $(".chatBox-content").css("height", 448);
        $(".chatBox-content-demo").css("height", 448);
        $(".chatBox-list").css("height", 495);
        $(".chatBox-kuang").css("height", 495);
        $(".div-textarea").css("width", 260);
    }
}

//进入与某人的聊天页面
function addClickForContacts(){
    $(".chat-list-people").each(function () {
        $(this).click(function () {
            var n = $(this).index();
            $(".chatBox-head-one").toggle();
            $(".chatBox-head-two").toggle();
            $(".chatBox-list").fadeToggle();
            $(".chatBox-kuang").fadeToggle();
            //修改当前窗体状态
            struts = $(this).children(".chat-name").children("input").val();
            //传id
            $("#toUserId").val($(this).children(".chat-name").children("input").val());
            //传名字
            $(".ChatInfoName").text($(this).children(".chat-name").children("p").eq(0).text().split("  ")[0]);
            //传头像
            $(".ChatInfoHead>img").attr("src", $(this).children().eq(0).children("img").attr("src"));

            //聊天框默认最底部
            $(document).ready(function () {
                $("#chatBox-content-demo").scrollTop($("#chatBox-content-demo")[0].scrollHeight);
            });

            //移出该联系人的小红点
            $(this).children(".message-num").text("");
            $(this).children(".message-num").removeClass("message-num");

            //发送到后台拉取最近三天的聊天记录
            var sendMsg = {};
            sendMsg.id = "1";
            sendMsg.from = userId;
            sendMsg.to = $("#toUserId").val();
            sendMsg.type = "3";
            sendMsg.msgDate = "0";
            send(sendMsg);
        })
    });
}

//刷新成最新的联系人列表
function createNewContactsList(newContactsList,isShow){
    var contactsTable = "";
    var isShowAllMessageImg = false;
    for(var i = 0 ; i < newContactsList.length ; i ++){
        if(newContactsList[i].id  != userId){
            contactsTable += "<div class='chat-list-people' id='userId_"+newContactsList[i].id+"'>";
            if(newContactsList[i].isOnline ==  true){
                contactsTable += "<div><img src='"+newContactsList[i].img+"' alt='头像'/></div>";
                contactsListForPicture.set(newContactsList[i].id,newContactsList[i].img);
            }else{
                contactsTable += "<div><img src='"+newContactsList[i].grayImg+"' alt='头像'/></div>";
                contactsListForPicture.set(newContactsList[i].id,newContactsList[i].grayImg);
            }
            contactsTable += "<div class='chat-name'>";
            contactsTable += "<input type='hidden' value='"+newContactsList[i].id+"'/>";
            contactsTable += "<p class='contactsUserName'>"+newContactsList[i].nickName+"  ("+newContactsList[i].name+")</p>";
            contactsTable += "</div>";
            if(newContactsList[i].unread != 0){
                contactsTable += "<div class='message-num i_'>"+newContactsList[i].unread+"</div>";
                isShowAllMessageImg = true;
            }else{
                contactsTable += "<div class= 'i_'></div>";
            }
            contactsTable += "</div>";
        }else{
            me = newContactsList[i];
            contactsListForPicture.set(newContactsList[i].id,newContactsList[i].img);
        }
    }
    // isShow
    if(isShowAllMessageImg && isShow){
        $("#allMessage").empty();
        $("#allMessage").html("<img src='"+projectPath+"/images/message.gif'/>");
    }
    $("#contactsTable").html(contactsTable);
    //联系人列表添加点击事件
    addClickForContacts();
    //js 保存最新联系人列表
    contactsList = newContactsList;
}

function changeColor() {
    if(colorIndex == colors.length){
        colorIndex = 0;
        $(".chat-list-people").children(".chat-name").children("p").eq(0).css("color","black");
        clearInterval(interval);
        interval = null;
    }else{
        $(".chat-list-people").children(".chat-name").children("p").eq(0).css("color",colors[colorIndex]);
        colorIndex++;
    }
}

function CurentTime(){
    var now = new Date();
    var year = now.getFullYear();       //年
    var month = now.getMonth() + 1;     //月
    var day = now.getDate();            //日
    var hh = now.getHours();            //时
    var mm = now.getMinutes();          //分
    var ss = now.getSeconds();           //秒
    var clock = year + "-";
    if(month < 10)
        clock += "0";
    clock += month + "-";
    if(day < 10)
        clock += "0";
    clock += day + " ";
    if(hh < 10)
        clock += "0";
    clock += hh + ":";
    if (mm < 10)
        clock += '0';
    clock += mm + ":";
    if (ss < 10) clock += '0';
    clock += ss;
    return clock;
}

//webSocket 重连
function reConnect(){
    $("#chatBox_Ex").hide(); // 隐藏连接异常提示，
    $("#chatBox_Conning").show() // 提示正在连接。。。
    //重新建立WebSocket连接
    createWebSocketClient(sessionId,userId);
}

//心跳检测 type = 6
var heartCheck = {
    timeout: 10000,
    timeoutObj: null,
    serverTimeoutObj: null,
    reset: function() {
        clearTimeout(this.timeoutObj);
        clearTimeout(this.serverTimeoutObj);
        return this;
    },
    start: function() {
        var self = this;
        this.timeoutObj = setTimeout(function() {
            //这里发送一个心跳，后端收到后，返回一个心跳消息，
            //onmessage拿到返回的心跳就说明连接正常
            var sendMsg = {};
            sendMsg.id = "6";
            sendMsg.userId = userId;
            sendMsg.sessionId = sessionId;
            sendMsg.type = "6";
            send(sendMsg);

            self.serverTimeoutObj = setTimeout(function() { //如果超过一定时间还没重置，说明后端主动断开了
                console.log("超时");
                socket.onclose();
                //这里为什么要在send检测消息后，倒计时执行这个代码呢，因为这个代码的目的时为了触发onclose方法，这样才能实现onclose里面的重连方法
                //所以这个代码也很重要，没有这个方法，有些时候发了定时检测消息给后端，后端超时（我们自己设定的时间）后，不会自动触发onclose方法。我们只有执行ws.close()代码，让ws触发onclose方法
                //的执行。如果没有这个代码，连接没有断线的情况下而后端没有正常检测响应，那么浏览器时不会自动超时关闭的（比如谷歌浏览器）,谷歌浏览器会自动触发onclose
                //是在断网的情况下，在没有断线的情况下，也就是后端响应不正常的情况下，浏览器不会自动触发onclose，所以需要我们自己设定超时自动触发onclose，这也是这个代码的
                //的作用。
            }, self.timeout)
        }, this.timeout)
    }
}

// 点击图片事件
function clickThisPic(thisPicAddress){
    console.log(thisPicAddress);
}