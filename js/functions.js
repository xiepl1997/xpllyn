// 图标元素淡入
function load_animation(){
	$(document).ready(function() {
		$("#titleimg").show(function() {
			$("#titleimg").hide();
			$("#titleimg").fadeIn(2000);
		});
		$("#icon_xpl").show(function() {
			$("#icon_xpl").hide();
			$("#icon_xpl").fadeIn(3000);
		});
	});
}

//xpllyn图标动画
function title_animation(){
	$(document).ready(function() {
		$("#titleimg").mouseenter(function(event) {
			$("#titleimg").animate({
				height: '90px',
				width: '180px'
			},"fast");
		});
		$("#titleimg").mouseleave(function(event) {
			$("#titleimg").animate({
				height: '70px', 
				width: '150px'
			},"fast")
		});
	});
}