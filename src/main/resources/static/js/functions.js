// // 图标元素淡入
// function load_animation(){
// 	$(document).ready(function() {
// 		$("#titleimg").show(function() {
// 			$("#titleimg").hide();
// 			$("#titleimg").fadeIn(2000);
// 		});
// 		$("#icon_xpl").show(function() {
// 			$("#icon_xpl").hide();
// 			$("#icon_xpl").fadeIn(3000);
// 		});
// 	});
// }

// //xpllyn图标动画
// function title_animation(){
// 	$(document).ready(function() {
// 		$("#titleimg").mouseenter(function(event) {
// 			$("#titleimg").animate({
// 				height: '90px',
// 				width: '180px'
// 			},"fast");
// 		});
// 		$("#titleimg").mouseleave(function(event) {
// 			$("#titleimg").animate({
// 				height: '70px', 
// 				width: '150px'
// 			},"fast")
// 		});
// 	});
// }

$(document).ready(function() {
    //提交留言按钮 by xiepl1997 at 2019-8-24
	$("#submit_message").click(function(event) {
		if($("#message").val().trim() == ''){
			alert("未填写留言哦~");
			return;
		}
		else{

			var str = $("#message").val().trim();
			var param = {
				message : str,
				type : 'insertMessage'
			};

			$.ajax({
				url: './insertMessage',
				type: 'POST',
				contentType: 'application/x-www-form-urlencoded',
				// data: JSON.stringify(param),
				data: param,
                beforeSend : function(){
                	$("#loading").modal('show');
                },
				success : function(messageList){
					if(messageList == null){
						alert("提交失败");
						return;
					}
					else{
						// $("#message").val("").focus();
						// var htmls = "<span th:each=\"i:${#numbers.sequence(0,messageCount-1)}\">\n" +
						// 	"<a class=\"list-group-item\" style=\"cursor: pointer;border: 1px solid gray\">\n" +
						// 	"<p class=\"list-group-item-text\" style=\"font-size: small;\" th:text=\"${messageList[i]?.name}\"></p>\n" +
						// 	"<p class=\"list-group-item-text\" style=\"font-weight: bold;margin-left: 25px;\" th:text=\"${messageList[i]?.content}\"></p>\n" +
						// 	"<p class=\"list-group-item-text\" style=\"font-size: x-small;\" th:text=\"${messageList[i]?.time}\"></p>\n" +
						// 	"</a>\n" +
						// 	"</span>";
						// $("#message_group").innerHTML(htmls);
						var htmls = "";
						var count = messageList.length;
						if(count > 8){
						    count = 8;
                        }
						for(var i = 0; i < count; i++){
							// var span = Date.parse(messageList[i].create_time);
							var dt = new Date(messageList[i].create_time);
							var timeStr = dt.getFullYear() + '-' + (dt.getMonth() + 1) + '-' + dt.getDate() + ' ' + dt.getHours() + ':' + dt.getMinutes() + ':' + dt.getSeconds();

							htmls += "<a class=\"list-group-item\" style=\"cursor: pointer;border: 1px solid gray\">";
							htmls += "<p class=\"list-group-item-text\" style=\"font-size: small;\">" + messageList[i].name + "</p>";
							htmls += "<p class=\"list-group-item-text\" style=\"font-weight: bold;margin-left: 25px;\">" + escapeHTML(messageList[i].content) + "</p>";
							htmls += "<p class=\"list-group-item-text\" style=\"font-size: x-small;\">" + timeStr + "</p>";
							htmls += "</a>";
						}
						//htmls += "<a id=\"show_all_message\" class=\"list-group-item\" style=\"cursor: pointer;text-align: center;\" href=\"#modal-container-969735\" data-toggle=\"modal\"><h5>……More……</h5></a>";
						$("#message_group").html(htmls);
						$("#message").val("");
						alert("感谢您的留言！");
					}
				},
				error : function (data) {
                    alert("提交失败！");
                },
                complete : function(){
                	$("#loading").modal('hide');
                }
			})

			

		}
	});

	//显示全部留言 by xiepl1997 at 2019-8-25
    $("#show_all_message").click(function () {
        $.ajax({
            url: './getAllMessages',
            type: 'GET',
            dataType: 'json',
            success: function (mList) {
                var htmls = "";
                for(var i = 0; i < mList.length; i++){
					// var span = new Date(messageList[i].create_time);
					var dt = new Date(mList[i].create_time);
					var timeStr = dt.getFullYear() + '-' + (dt.getMonth() + 1) + '-' + dt.getDate() + ' ' + dt.getHours() + ':' + dt.getMinutes() + ':' + dt.getSeconds();

					if(i%2 == 0)
                        htmls += "<a class=\"list-group-item\" style=\"cursor: pointer;border-radius:15px;\">";
                    else
                        htmls += "<a class=\"list-group-item\" style=\"cursor: pointer;border-radius:15px;background-color: silver;\">";
                    htmls += "<p class=\"list-group-item-text\" style=\"font-size: small;\">" + mList[i].name + "</p>";
                    htmls += "<p class=\"list-group-item-text\" style=\"font-weight: bold;margin-left: 25px;\">" + escapeHTML(mList[i].content) + "</p>";
                    htmls += "<p class=\"list-group-item-text\" style=\"font-size: x-small;\">" + timeStr + "</p>";
                    htmls += "</a>";
                }
                $("#all_message").html(htmls);
            },
            error : function (data) {
                alert("获取失败！");
            }
        })
    })

	//用户点击电子书，插入一条日志
	$(".booklist").click(function (e) {
		var str = $(this).attr("download"); //获取书名
		var param = {
			name : str,
			type : 'book_download'
		};
		$.ajax({
			url: './ebook_download',
			type: 'post',
			contentType: 'application/x-www-form-urlencoded',
			data: param,
			success: function (log) {

			}
		})
	})
	
	//用户点击文章时，插入一条日志
	$(".blog").click(function (e) {
		var str = $(this).children("h4").text();
		var param = {
			name : str,
			type : 'readBlog'
		};
		$.ajax({
			url: './read_blog',
			type: 'post',
			contentType: 'application/x-www-form-urlencoded',
			data: param,
			success: function (log) {

			}
		})
	})

	//用户点击“更多文章”时，插入一条日志
	$(".blog-more").click(function (e) {
		var param = {
			name : '....more....',
			type : 'readMoreBlog'
		};
		$.ajax({
			url: './read_blog_more',
			type: 'post',
			contentType: 'application/x-www-form-urlencoded',
			data: param,
			success: function (log) {

			}
		})
	})
});

/**
 * function:显示注册界面
 * date:2020/2/4
 * @constructor
 */
function ShowRegister() {
	$("#c1").removeAttr("hidden");
}

/**
 *. 转义html(防XSS攻击)
 *. @param str 字符串
 */
function escapeHTML (str) {
	return     str.replace(
		/[&<>'"]/g,
		tag =>
			({
				'&': '&amp;',
				'<': '&lt;',
				'>': '&gt;',
				"'": '&#39;',
				'"': '&quot;'
			}[tag] || tag)
	);
}



