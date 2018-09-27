function apiPost(url, params, successCallback, failCallback) {
	var data = {};
	if (params) {
		data = params;
	}
	$.ajax({
		url : url,
		type : "post",
		async : false,
		data : $.toJSON(data),
		contentType : 'application/json',
		success : function(data) {
			var json = data;
			if (typeof (json) == 'string') {
				json = JSON.parse(data);
			}
			if (successCallback) {
				successCallback(json);
			} else {
				console.log(json);
			}
		},
		error : function(e) {
			if (failCallback) {
				failCallback(json);
			} else {
				console.log(e);
			}
		}
	});
};

//定义文件列表
var data = [];

function loadFiles(data) {
	$('#onLodding').modal('show');
	
	$('#onLodding').on('shown.bs.modal', function () {
		initLogContext(data);
	})
	
	
	// apiPost('test/sayHello', null, loadLog);
//	var loadLog = function(data) {
		
//	};
	
	/*var testData = [ {
		'logIndex' : 1,
		'logName' : '/js/common/jquery/test1.log',
		'logType' : 'java',
	}, {
		'logIndex' : 2,
		'logName' : '/js/common/jquery/test2.log',
		'logType' : 'java',
	}, {
		'logIndex' : 3,
		'logName' : '/js/common/jquery/test3.log',
		'logType' : 'C++',
	} ];*/
	//loadLog(testData);
};

$(function() {
	var fileName1 = "";
	var fileName2 = "";
	var fileName3 = "";
	
	$("#importBtn").click(function() {
		$.ajax({
			url : "dataFactory/getlogFiles",
			type : "post",
			async : false,
			data : {},
			contentType : 'application/json',
			success : function(data) {
				var json = JSON.parse(data);
				console.log(json);
				if (json.result && json.result == "success") {
					$('#importFile').modal('show');
					var files = json.data;
					for (var i = 0;i < 3; i++) {
						if (files[i]) {
							$('#file' + (i + 1)).val(files[i].filename);
						}
					}
				} else {
					alert("error");
				}
			},
			error : function(e) {
				alert("获取文件失败");
			}
		});
		
	});
	
	// 确定
	$("#okBtn").click(function() {
		fileName1 = "";
		fileName2 = "";
		fileName3 = "";
		data = [];
		
		if ($("#file1") && $("#file1").val()) {
			fileName1 = $("#file1").val();
			var file = {};
			file["logIndex"] = 1;
			file["logName"] = fileName1;
			file["logType"] = "java";
			data.push(file);
		}
		if ($("#file2") && $("#file2").val()) {
			fileName2 = $("#file2").val();
			var file = {};
			file["logIndex"] = 2;
			file["logName"] = fileName2;
			file["logType"] = "java";
			data.push(file);
		}
		if ($("#file3") && $("#file3").val()) {
			fileName3 = $("#file3").val();
			var file = {};
			file["logIndex"] = 3;
			file["logName"] = fileName3;
			file["logType"] = "C++";
			data.push(file);
		}
		
		console.log(fileName1 + "  " + fileName2 + "  " +  fileName3);

		$('#importFile').modal('hide');
	});
	
	$('#importFile').on('hidden.bs.modal', function () {  
		loadFiles(data);
	});

	$("#exeFilter").click(function() {
		$(this).button('loading').delay(1000).queue(function() {

			var url = "test/sayHello";
			var params = {};
			params["fileName"] = "22222";
			params["processName"] = "aaa";

			$.ajax({
				url : url,
				type : "post",
				async : false,
				data : $.toJSON(params),
				contentType : 'application/json',
				success : function(data) {
					var json = JSON.parse(data);
					if (json.success) {
						// alert(json.msg);
					} else {
						alert("error");
					}
				},
				error : function(e) {
					alert("error2");
				}
			});

			$(this).button('reset');
			$(this).dequeue();

		});
	});
	
	//loadFiles();
});
