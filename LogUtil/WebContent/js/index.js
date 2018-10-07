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
				console.error(json);
			}
		},
		error : function(e) {
			if (failCallback) {
				failCallback(json);
			} else {
				console.error(e);
			}
		}
	});
};

function apiGet(url, successCallback, failCallback) {

	$.ajax({
		url : url,
		type : "get",
		async : false,
		success : function(data) {
			var json = data;
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

$(function() {
	//默认文件列表
	var currentData = null;
	
	//加载
	var loadFiles = function() {
		$('#onLodding').modal('show');
	};
	
	$('#onLodding').on('shown.bs.modal', function () {
		if(currentData){
			console.log("***"+new Date());
			initLogContext(currentData);
		}else{
			$('#onLodding').modal('hide');
			alert("请先加载文件!");
		}
	});
	
	var fileName1 = "";
	var fileName2 = "";
	var fileName3 = "";
	
	$("#importBtn").click(function() {
		
		apiPost('dataFactory/getlogFiles', null, function(json){
			if('success'==json.result){
				$('#importFile').modal('show');
				var files = json.data;
				for (var i = 0;i < 3; i++) {
					if (files[i]) {
						$('#file' + (i + 1)).val(files[i].logPath);
					}
				}
			}else {
				console.error("load data error!");
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
			file["logPath"] = fileName1;
			file["logType"] = "java";
			data.push(file);
		}
		if ($("#file2") && $("#file2").val()) {
			fileName2 = $("#file2").val();
			var file = {};
			file["logIndex"] = 2;
			file["logName"] = fileName2;
			file["logPath"] = fileName2;
			file["logType"] = "java";
			data.push(file);
		}
		if ($("#file3") && $("#file3").val()) {
			fileName3 = $("#file3").val();
			var file = {};
			file["logIndex"] = 3;
			file["logName"] = fileName3;
			file["logPath"] = fileName3;
			file["logType"] = "C++";
			data.push(file);
		}
		currentData = data;
		console.dir(currentData);

		$('#importFile').modal('hide');
	});
	
	$('#importFile').on('hidden.bs.modal', loadFiles);

	$("#exeFilter").click(function() {
		console.log("exeFilter click" + new Date());
		var logLevel = $("#logLevel").val();
		var processName = $("#processName").val();
		var startTime = $("#startTime").val();
		var endTime = $("#endTime").val();
		var key = $("#key").val();
		if(!baseParas){
			baseParas = {};
		}
		if(logLevel){
			baseParas["logLevel"] = logLevel;
		}
		if(processName){
			baseParas["processName"] = processName;
		}
		if(startTime){
			baseParas["startTime"] = startTime;
		}
		if(endTime){
			baseParas["endTime"] = endTime;
		}
		if(key){
			baseParas["key"] = key;
		}
	
		$(this).button('loading').delay(1000).queue(function() {

			loadFiles();
			
			$(this).button('reset');
			$(this).dequeue();

		});
	});
});
