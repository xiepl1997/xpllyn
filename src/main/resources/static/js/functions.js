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

//提交留言按钮 by xiepl1997 at 2019-8-24
$(document).ready(function() {
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
						for(var i = 0; i < messageList.length; i++){
							htmls += "<a class=\"list-group-item\" style=\"cursor: pointer;border: 1px solid gray\">";
							htmls += "<p class=\"list-group-item-text\" style=\"font-size: small;\">" + messageList[i].name + "</p>";
							htmls += "<p class=\"list-group-item-text\" style=\"font-weight: bold;margin-left: 25px;\">" + messageList[i].content + "</p>";
							htmls += "<p class=\"list-group-item-text\" style=\"font-size: x-small;\">" + messageList[i].time + "</p>";
							htmls += "</a>";
						}
						htmls += "";
						$("#message_group").html(htmls);
					}
				}
			})

			

		}
	});
});