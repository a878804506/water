<%@page pageEncoding="UTF-8"%>

<form onsubmit="return false;" style="display: none;">
    <div>
        <h3>输出消息：</h3>
        <textarea id="responseText" style="width: 300px; height: 600px;" readonly></textarea>
        <input type="button" onclick="javascript:document.getElementById('responseText').value=''" value="清空">
    </div>
</form>

<div class="chatContainer">
    <!-- 最小化时 图标 开始 -->
    <div class="chatBtn">
        <i class="iconfont icon-xiaoxi1"></i>
    </div>
    <!-- 最小化时 图标 结束 -->
    <!-- 最小化时 未读消息条数 -->
    <div class="chat-message-num" id="allMessage"></div>

    <div class="chatBox" ref="chatBox" id="chatBox">
        <!-- 聊天主体展开时的头部 开始 -->
        <div class="chatBox-head">
            <div class="chatBox-head-one">
                联系人
                <div class="chat-close" style="margin: 10px 10px 0 0;font-size: 16px">关闭</div>
            </div>
            <div class="chatBox-head-two">
                <div class="chat-return" style="font-size: 16px;">返回</div>
                <div class="chat-people">
                    <div class="ChatInfoHead">
                        <img src="" alt="头像"/>
                    </div>
                    <div class="ChatInfoName" style="font-size: 16px;"></div>
                    <input type='hidden'  id='toUserId' value=''/>
                </div>
                <div class="chat-close" style="font-size: 16px;">关闭</div>
            </div>
        </div>
        <!-- 聊天主体展开时的头部 结束 -->

        <div class="chatBox-info">
            <!-- 聊天主体展开时的联系人列表 开始 -->
            <div class="chatBox-list" ref="chatBoxlist" id="contactsTable">

            </div>
            <!-- 聊天主体展开时的联系人列表 结束 -->

            <div class="chatBox-kuang" ref="chatBoxkuang">
                <!-- 聊天窗口展示框体  开始 -->
                <div class="chatBox-content">
                    <div class="chatBox-content-demo" id="chatBox-content-demo">

                    </div>
                    <!-- 聊天窗口展示框体  结束-->

                    <!-- 聊天窗口编辑框  开始 -->
                    <div class="chatBox-send">
                        <div class="div-textarea" contenteditable="true"></div>
                        <div>
                            <button id="chat-biaoqing" class="btn-default-styles">
                                <i class="iconfont icon-biaoqing"></i>
                            </button>
                            <label id="chat-tuxiang" title="发送图片" for="inputImage" class="btn-default-styles">
                                <input type="file" onchange="selectImg(this)" accept="image/jpg,image/jpeg,image/png"
                                       name="file" id="inputImage" class="hidden">
                                <i class="iconfont icon-tuxiang"></i>
                            </label>
                            <button id="chat-fasong" class="btn-default-styles"><i class="iconfont icon-fasong"></i>
                            </button>
                        </div>
                        <div class="biaoqing-photo">
                            <ul>
                                <li><span class="emoji-picker-image" style="background-position: -9px -18px;"></span></li>
                                <li><span class="emoji-picker-image" style="background-position: -40px -18px;"></span></li>
                                <li><span class="emoji-picker-image" style="background-position: -71px -18px;"></span></li>
                                <li><span class="emoji-picker-image" style="background-position: -102px -18px;"></span></li>
                                <li><span class="emoji-picker-image" style="background-position: -164px -18px;"></span></li>
                                <li><span class="emoji-picker-image" style="background-position: -133px -18px;"></span></li>
                                <li><span class="emoji-picker-image" style="background-position: -9px -52px;"></span></li>
                                <li><span class="emoji-picker-image" style="background-position: -40px -52px;"></span></li>
                                <li><span class="emoji-picker-image" style="background-position: -71px -52px;"></span></li>
                                <li><span class="emoji-picker-image" style="background-position: -102px -52px;"></span></li>
                                <li><span class="emoji-picker-image" style="background-position: -133px -52px;"></span></li>
                                <li><span class="emoji-picker-image" style="background-position: -164px -52px;"></span></li>
                                <li><span class="emoji-picker-image" style="background-position: -9px -86px;"></span></li>
                                <li><span class="emoji-picker-image" style="background-position: -40px -86px;"></span></li>
                                <li><span class="emoji-picker-image" style="background-position: -71px -86px;"></span></li>
                                <li><span class="emoji-picker-image" style="background-position: -102px -86px;"></span></li>
                                <li><span class="emoji-picker-image" style="background-position: -133px -86px;"></span></li>
                                <li><span class="emoji-picker-image" style="background-position: -164px -86px;"></span></li>
                                <li><span class="emoji-picker-image" style="background-position: -9px -120px;"></span></li>
                                <li><span class="emoji-picker-image" style="background-position: -40px -120px;"></span></li>
                                <li><span class="emoji-picker-image" style="background-position: -71px -120px;"></span></li>
                                <li><span class="emoji-picker-image" style="background-position: -102px -120px;"></span></li>
                                <li><span class="emoji-picker-image" style="background-position: -133px -120px;"></span></li>
                                <li><span class="emoji-picker-image" style="background-position: -164px -120px;"></span></li>
                                <li><span class="emoji-picker-image" style="background-position: -9px -154px;"></span></li>
                                <li><span class="emoji-picker-image" style="background-position: -40px -154px;"></span></li>
                                <li><span class="emoji-picker-image" style="background-position: -71px -154px;"></span></li>
                                <li><span class="emoji-picker-image" style="background-position: -102px -154px;"></span></li>
                                <li><span class="emoji-picker-image" style="background-position: -133px -154px;"></span></li>
                                <li><span class="emoji-picker-image" style="background-position: -164px -154px;"></span></li>
                            </ul>
                        </div>
                    </div>
                    <!-- 聊天窗口编辑框  结束 -->
                </div>
            </div>
        </div>
    </div>
</div>
