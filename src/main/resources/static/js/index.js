$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	$("#publishModal").modal("hide");

	// // 在发送AJAX之前，将csrf令牌设置到请求的消息头中
	// var token = $("meta[name='_csrf']").attr("content");
	// var header = $("meta[name='_csrf_header']").attr("content");
	// $(document).ajaxSend(function(e, xhr, options) {
	// 	xhr.setRequestHeader(header, token);
	// });

	// 获取标题和内容
	var title = $("#recipient-name").val();
	var content = $("#message-text").val();
	// 发送异步请求 post
	$.post(
		CONTEXT_PATH + "/discuss/add",
		{"title": title, "content": content},
		function (data) {
			data = $.parseJSON(data);
			// 在提示框中显示返回的消息
			$("#hintBody").text(data.msg)
			// 显示提示框
			$("#hintModal").modal("show");
			// 2s后自动隐藏
			setTimeout(function(){
				$("#hintModal").modal("hide");
				// 刷新页面
				if (data.code == 0) {
					window.location.reload();
				}
			}, 2000);
		}
	);


}