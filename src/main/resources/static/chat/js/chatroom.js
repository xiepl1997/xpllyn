var ws = {
    singleSend : function (fromUserId, toUserId, content) {
        if (!window.WebSocket) {
            return;
        }
        if (socket.readyState == WebSocket.OPEN) {
            var data = {
                "fromUserId" : fromUserId,
                "toUserId" : toUserId,
                "content" : content,
                "type" : "SINGLE_SENDING"
            };
            socket.send(JSON.stringify(data));
        } else {
            alert("WebSocket连接没有开启！");
            return 'fail';
        }
        return 'success';
    },
    
    groupSend : function (fromUserId, toGroupId, content) {
        if (!window.WebSocket) {
            return;
        }
        if (socket.readyState == WebSocket.OPEN) {
            var data = {
                "fromUserId" : fromUserId,
                "toGroupId" : toGroupId,
                "content" : content,
                "type" : "GROUP_SENDING"
            };
            socket.send(JSON.stringify(data));
        } else {
            alert("WebSocket连接没有开启！");
            return 'fail';
        }
        return 'success';
    },

    // 全体用户群聊，发送
    groupSendAll : function (fromUserId, content) {
        if (!window.WebSocket) {
            return;
        }
        if (socket.readyState == WebSocket.OPEN) {
            var data = {
                "fromUserId" : fromUserId,
                "content" : content,
                "type" : "GROUP_SENDING_ALL"
            };
            socket.send(JSON.stringify(data));
        } else {
            alert("WebSocket连接没有开启！");
            return 'fail';
        }
        return 'success';
    },

    singleReceive : function (data) {
        var fromUserId = data.fromUserId;
        var content = data.content;
        var time = data.time;
        var param = {
            id : fromUserId,
            content : content,
            time : time
        };
        addMessage(param, 'you');
        updateOnePersonList(param);
    },

    groupReceive : function (data) {
        var groupId = data.groupId;
        var content = data.content;
        var time = data.time;
        var param = {
            id : groupId,
            content : content,
            time : time
        };
        addMessageForGroup(param, 'you');
        updateGroupList(param);
    },

    // 全体用户群聊，接收
    groupReceiveAll : function (data) {
        addMessageForGroupAll(data, 'you');
        updateGroupListAll(data);
    },

    // 好友下线处理
    // friendOffline : function (data) {
    //     alert(data.fromUserId + '下线');
    //     var html = document.querySelector(".person[data-chat='" + data.fromUserId + "'] .name").getAttribute('name');
    //     document.querySelector(".person[data-chat='" + data.fromUserId + "'] .name").innerHTML = html;
    // }
}

function addMessageForGroupAll(data, who) {
    var html = document.querySelector('.chat[data-chat=group]').innerHTML;
    var msg;
    if (who === 'you') {
        msg = "<div class='bubble you'>" + data.content + "</div>";
    } else {
        msg = "<div class='bubble me'>" + data.content + "</div>";
    }
    document.querySelector('.chat[data-chat=group]').innerHTML = html + msg;
    var chat = document.querySelector('.chat[data-chat=group]')
    gotoBottom(chat);
}

// 在聊天界面中追加消息
function addMessage(data, who) {
    var html = document.querySelector(".chat[data-chat='" + data.id + "']").innerHTML;
    var msg;
    if (who === 'you') {
        msg = "<div class='bubble you'>" + data.content + "</div>";
    } else {
        msg = "<div class='bubble me'>" + data.content + "</div>";
    }
    document.querySelector(".chat[data-chat='" + data.id + "']").innerHTML = html + msg;
    var chat = document.querySelector(".chat[data-chat='" + data.id + "']");
    gotoBottom(chat);
}

function addMessageForGroup(data, who) {
    var html = document.querySelector(".group_chat[data-chat='" + data.id + "']").innerHTML;
    var msg;
    if (who === 'you') {
        msg = "<div class='bubble you'>" + data.content + "</div>";
    } else {
        msg = "<div class='bubble me'>" + data.content + "</div>";
    }
    document.querySelector(".group_chat[data-chat='" + data.id + "']").innerHTML = html + msg;
    var chat = document.querySelector(".group_chat[data-chat='" + data.id + "']");
    gotoBottom(chat);
}

// 加载好友列表
function loadPersonList(data) {
    var person = "";
    for (var i = 0; i < data.length; i++) {
        person += "<div class='person' data-chat='" + data[i].id + "'></div>" +
            "<img src='" + data[i].user_icon + "' alt=''/>" +
            "<span class='name'>" + data[i].user_name + "</span>" +
            "<span class='time'>" + data[i].time + "</span>" +
            "<span class='preview'>" + data[i].preview + "</span>";
    }
    var html = $('.people').innerHTML;
    $('.people').innerHTML = html + person;
}

// 添加一名好友
function  addPerson(data) {
    var person = "<div class='person' data-chat='" + data.id + "'></div>" +
        "<img src='" + data.user_icon + "' alt=''/>" +
        "<span class='name'>" + data.user_name + "</span>" +
        "<span class='time'>" + data.time + "</span>" +
        "<span class='preview'>" + data.preview + "</span>";
    var html = $('.people').innerHTML;
    $('.people').innerHTML = html + person;

    var chat = "<div class='chat' data-chat='" + data.id + "'></div>";
    var html1 = document.querySelector('.chatbody').innerHTML;
    document.querySelector('.chatbody').innerHTML = html1 + chat;
}

// 更新好友列表中的群信息
function updateGroupList(data) {
    document.querySelector(".group_person[data-chat='" + data.id + "'] .time").innerHTML = data.time;
    if (data.content.length > 16) {
        data.content = data.content.substr(0, 16) + '...';
    }
    document.querySelector(".group_person[data-chat='" + data.id + "'] .preview").innerHTML = data.content;
}

// 更新好友列表中的信息
function updateOnePersonList(data) {
    document.querySelector(".person[data-chat='" + data.id + "'] .time").innerHTML = data.time;
    if (data.content.length > 16) {
        data.content = data.content.substr(0, 16) + '...';
    }
    document.querySelector(".person[data-chat='" + data.id + "'] .preview").innerHTML = data.content;
}

function updateGroupListAll(data) {
    document.querySelector('.group_person[data-chat=group] .time').innerHTML = data.time;
    if (data.content.length > 16) {
        data.content = data.content.substr(0, 16) + '...';
    }
    document.querySelector('.group_person[data-chat=group] .preview').innerHTML = data.content;
}

function gotoBottom(chat) {
    chat.scrollTop = chat.scrollHeight;
}

$(document).ready(function() {
    // 点击发送按钮
    $('.sendBtn').on('click', function () {
        sendMsg();
    })

    // 输入框点击回车监听
    $('#inputText').keydown(function (event) {
        if (event.keyCode == 13) {
            sendMsg();
        }
    })

    // 点击查找用户
    $('.search').on('click', function () {
        searchUser();
    })

    // 用户搜索框回车触发
    $('#idSearch').keydown(function (event) {
        if (event.keyCode == 13) {
            searchUser();
        }
    })

    // 加好友按钮点击事件
    $(document).on('click', '.addFriend', function () {
        var email = $(this).attr('name');

    })
})

// 查找用户，触发模态框
function searchUser() {
    if (socket.readyState != WebSocket.OPEN) {
        alert("WebSocket连接没有开启！");
        return;
    }
    var idOrEmail = $('#idSearch').val().trim();
    if (idOrEmail == '') {
        return;
    }
    $('#idSearch').val('');
    $('#modal-container').modal('show');
    var param = {
        idOrEmail : idOrEmail
    };
    $.ajax({
        url : '/chatroom/SearchUser',
        type : 'GET',
        contentType: 'application/x-www-form-urlencoded',
        data : param,
        beforeSend : function () {
            var html = "<div id='circle'></div>";
            $('#friendsSearchResult').html(html);
        },
        success : function (data) {
            var len = data.length;
            var html = '';
            for (var i = 0; i < len; i++) {
                html += "<div class='row' style='text-align: center'>" +
                    "<strong>" + data[i].user_name + "</strong>" + "(" + data[i].user_email + ")&nbsp;&nbsp;&nbsp;&nbsp;" +
                    "<button type='button' class='addFriend btn btn-primary' name='" + data[i].user_email + "'>加好友</button>" +
                    "</div>";
            }
            $('#friendsSearchResult').html(html);
        }
    })
}

// 发送信息
function sendMsg() {
    var fromUserId = $('.user').attr('name');
    var node = document.querySelector('.active-chat');
    var toUserId = node.getAttribute('data-chat');
    var content = $('#inputText').val().trim();
    if (content == '') {
        alert('消息不能为空！');
        return;
    }
    var data = {
        id: toUserId,
        content: content,
        time: (new Date().getMonth() + 1) + '/' + new Date().getDate()
    };
    if (node.classList.contains('group_chat')) {
        // 如果是全体群聊
        if (node.getAttribute('data-chat') === 'group') {
            if (ws.groupSendAll(fromUserId, content) === 'success') {
                addMessageForGroupAll(data, 'me');
                updateGroupListAll(data);
            }
        } else {
            if (ws.groupSend(fromUserId, toUserId, content) === 'success') {
                addMessageForGroup(data, 'me');
                updateGroupList(data);
            }
        }
    } else {
        if (ws.singleSend(fromUserId, toUserId, content) === 'success') {
            addMessage(data, 'me');
            updateOnePersonList(data);
        }
    }
    $('#inputText').val('');
}