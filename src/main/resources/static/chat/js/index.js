document.querySelector('.chat[data-chat=group]').classList.add('active-chat');
document.querySelector('.person[data-chat=group]').classList.add('active');

var friends = {
  list: document.querySelector('ul.people'),
  all: document.querySelectorAll('.left .person'),
  name: '' },

chat = {
  container: document.querySelector('.container1 .right'),
  current: null,
  person: null,
  name: document.querySelector('.container1 .right .top .name') };


friends.all.forEach(function (f) {
  f.addEventListener('mousedown', function () {
    f.classList.contains('active') || setAciveChat(f);
  });
});

function setAciveChat(f) {
  friends.list.querySelector('.active').classList.remove('active');
  f.classList.add('active');
  chat.current = chat.container.querySelector('.active-chat');
  chat.person = f.getAttribute('data-chat');
  chat.current.classList.remove('active-chat');
  chat.container.querySelector('[data-chat="' + chat.person + '"]').classList.add('active-chat');
  friends.name = f.querySelector('.name').innerText;
  chat.name.innerHTML = friends.name;
  // 使聊天界面定位到最底部
  chat.current = chat.container.querySelector('.active-chat');
  chat.current.scrollTop = chat.current.scrollHeight;
}

// 添加一名好友
function addPerson(data) {
  var person = "<li class='person' data-chat='" + data.id + "'>" +
      "<img src='" + data.user_icon + "' alt=''/>" +
      "<span class='name'>" + data.user_name + "</span>" +
      "<span class='time'>" + data.time + "</span>" +
      "<span class='preview'>" + data.preview + "</span>" +
      "</li>";
  var html = $('.people').html();
  $('.people').html(html + person);

  var chat = "<div class='chat' data-chat='" + data.id + "'>" +
      "<div class='bubble you'>我们已经是好友啦，一起聊天吧！</div>" +
      "</div>";
  var html1 = $('.chatbody').html();
  $('.chatbody').html(html1 + chat);

  friends.list = document.querySelector('ul.people');
  friends.all = document.querySelectorAll('.left .person');
  chat.container = document.querySelector('.container1 .right');
  friends.all.forEach(function (f) {
    f.addEventListener('mousedown', function () {
      f.classList.contains('active') || setAciveChat(f);
    });
  });

}

$(document).ready(function () {
  // 在验证消息上点击同意，添加对方为好友
  $(document).on('click', '.agree', function () {
    var fromId = $(this).attr('name');
    var toId = $('.user').attr('name');
    var param = {
      'fromId' : fromId,
      'toId' : toId
    };
    $.ajax({
      url : '/chatroom/agreeAddRequest',
      type : 'POST',
      contentType: 'application/x-www-form-urlencoded',
      data: param,
      success : function (data) {
        alert('已同意。');
        $('#modal-addFriend-request').modal('hide');
        var d = {
          'id' : data[0].id,
          'user_icon' : data[0].user_icon,
          'user_name' : data[0].user_name,
          'time' : (new Date().getMonth() + 1) + '/' + new Date().getDate(),
          'preview' : '我们已经是好友了，快来聊天吧！'
        };
        addPerson(d);
        //给新好友发送接收消息，让新好友更新UI
        var d1 = {
          'fromUserId' : toId,
          'toUserId' : fromId,
          'user_icon' : data[1].user_icon,
          'user_name' : data[1].user_name,
          'time' : (new Date().getMonth() + 1) + '/' + new Date().getDate(),
          'type' : 'AGREE_FRIEND_REQUEST'
        };
        socket.send(JSON.stringify(d1));
      }
    })
  })
})

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
    if (socket.readyState != WebSocket.OPEN) {
      alert("WebSocket连接没有开启！");
      return;
    }
    var toId = $(this).attr('name');
    var fromId = $('.user').attr('name');
    var param = {
      'fromId' : fromId,
      'toId' : toId
    };
    $.ajax({
      url : '/chatroom/addFriendRequest',
      type: 'POST',
      contentType: 'application/x-www-form-urlencoded',
      data: param,
      success : function (data) {
        var msg = data.status;
        if (msg == 'repeat' || msg == 'success') {
          alert('已发送请求。');
        } else if (msg == 'isyourfriend') {
          alert('已是好友，无需添加。');
        }
        $('#modal-container').modal('hide');
      }
    })
  })

  // 点击消息按钮，查看添加好友请求
  $(document).on('click', '.addRequest', function () {
    if (socket.readyState != WebSocket.OPEN) {
      alert("WebSocket连接没有开启！");
      return;
    }
    var id = $('.user').attr('name');
    $.ajax({
      url : '/chatroom/getAddRequest',
      type : 'GET',
      contentType: 'application/x-www-form-urlencoded',
      data: {'id' : id},
      success : function (data) {
        var html = '';
        for (var i = 0; i < data.length; i++) {
          html += "<div class='row' style='text-align: center'>" +
              "<strong>" + data[i].user_name + "</strong>" + "(" + data[i].user_email + ")请求添加您为好友&nbsp;&nbsp;" +
              "<button type='button' class='agree btn btn-info' name='" + data[i].id + "'>同意</button>" +
              "<button type='button' class='disagree btn btn-danger' name='" + data[i].id + "'>拒绝</button>" +
              "</div>";
        }
        $('#requestList').html(html);
        $('#modal-addFriend-request').modal('show');
      }
    })
  })

  // 在验证消息上点击拒绝，不添加对方为好友
  $(document).on('click', '.disagree', function () {
    var fromId = $(this).attr('name');
    var toId = $('.user').attr('name');
    var param = {
      'fromId' : fromId,
      'toId' : toId
    };
    $.ajax({
      url : '/chatroom/disagreeAddRequest',
      type : 'POST',
      contentType: 'application/x-www-form-urlencoded',
      data: param,
      success : function (data) {
        alert('已拒绝。');
        $('#modal-addFriend-request').modal('hide');
      }
    })
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
            "<button type='button' class='addFriend btn btn-primary' name='" + data[i].id + "'>加好友</button>" +
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