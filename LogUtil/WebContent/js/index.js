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

function loadFiles() {
	$('#onLodding').modal('show');
	
	// apiPost('test/sayHello', null, loadLog);
	var loadLog = function(data) {
		initLogContext(data);
	};
	
	var testData = [ {
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
	} ];
	loadLog(testData);
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
				if (json.result && json.result == "success") {
					$('#importFile').modal('show');
					var files = json.data;
					for (var i = 0;i < 3; i++) {
						if (files[i]) {
							$('#file' + i).val(files[i].filename);
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
		if ($("#file1") && $("#file1").value) {
			fileName1 = $("#file1").value;
		}
		if ($("#file2") && $("#file2").value) {
			fileName2 = $("#file2").value;
		}
		if ($("#file3") && $("#file3").value) {
			fileName3 = $("#file3").value;
		}

		$('#importFile').modal('hide');
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
	
	loadFiles();
});
