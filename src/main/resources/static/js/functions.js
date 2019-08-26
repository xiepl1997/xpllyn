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
				message : str
			};

			$.ajax({
				url: './insertMessage',
				type: 'POST',
				contentType: 'application/x-www-form-urlencoded',
				// data: JSON.stringify(param),
				data: param,
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
							htmls += "<a class=\"list-group-item\" style=\"cursor: pointer;border: 1px solid gray\">";
							htmls += "<p class=\"list-group-item-text\" style=\"font-size: small;\">" + messageList[i].name + "</p>";
							htmls += "<p class=\"list-group-item-text\" style=\"font-weight: bold;margin-left: 25px;\">" + messageList[i].content + "</p>";
							htmls += "<p class=\"list-group-item-text\" style=\"font-size: x-small;\">" + messageList[i].time + "</p>";
							htmls += "</a>";
						}
						//htmls += "<a id=\"show_all_message\" class=\"list-group-item\" style=\"cursor: pointer;text-align: center;\" href=\"#modal-container-969735\" data-toggle=\"modal\"><h5>……More……</h5></a>";
						$("#message_group").html(htmls);
						$("#message").val("");
						alert("感谢您的留言！");
					}
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
                    if(i%2 == 0)
                        htmls += "<a class=\"list-group-item\" style=\"cursor: pointer;border-radius:15px;\">";
                    else
                        htmls += "<a class=\"list-group-item\" style=\"cursor: pointer;border-radius:15px;background-color: silver;\">";
                    htmls += "<p class=\"list-group-item-text\" style=\"font-size: small;\">" + mList[i].name + "</p>";
                    htmls += "<p class=\"list-group-item-text\" style=\"font-weight: bold;margin-left: 25px;\">" + mList[i].content + "</p>";
                    htmls += "<p class=\"list-group-item-text\" style=\"font-size: x-small;\">" + mList[i].time + "</p>";
                    htmls += "</a>";
                }
                $("#all_message").html(htmls);
            }
        })
    })
});




