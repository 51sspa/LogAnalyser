$(function() {
	var fileName1 = "";
	var fileName2 = "";
	var fileName3 = "";
	
	//导入文件
	$("#logConfim").click(function() {
		if ($("#file1")[0] && $("#file1")[0].files[0]) {
			fileName1 = $("#file1")[0].files[0].name;
		}
		if ($("#file2")[0] && $("#file2")[0].files[0]) {
			fileName2 = $("#file2")[0].files[0].name;
		}
		if ($("#file3")[0] && $("#file3")[0].files[0]) {
			fileName3 = $("#file3")[0].files[0].name;
		}
		
		$('#importLog').modal('hide');
	});
	
	$("#exeFilter").click(function() {
		$(this).button('loading').delay(1000).queue(function() {
			
			var url = "http://www.baidu.com";
			var params = {};
			params["fileName"] = "22222";
			params["processName"] = "aaa";
			var result = sendPost(url, false, params);
			
			$(this).button('reset');
			$(this).dequeue();
		});
	});
	
	
	
});

/**
 * 发送请求
 * @returns
 */
function sendPost(url, async, params) {
	alert($.toJSON(params));
	$.ajax({
		url : url,
		type : "post",
		async : async,
		data : $.toJSON(params),
		contentType : 'application/json',
		success : function(data) {
			return data;
		},
		error : function(e) {
			return null;
		}
	});
}
