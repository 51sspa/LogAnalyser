var rowCount = 1000;

$(function() {
	//默认文件列表
	var currentData = null;
	var queryUrl = "";
	
	var getFileContent = function() {
		$('#onLodding').modal('show');
	}
	
	$('#onLodding').on('shown.bs.modal', function () {
		if(currentData){
			initLogContext(currentData, queryUrl);
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
		var data = [];
		
		if ($("#file1") && $("#file1").val()) {
			fileName1 = $("#file1").val();
			var file = {};
			file["logIndex"] = 1;
			file["logName"] = fileName1;
			file["logPath"] = fileName1;
			file["logType"] = "java";
			file["rowCount"] = rowCount;
			data.push(file);
		}
		if ($("#file2") && $("#file2").val()) {
			fileName2 = $("#file2").val();
			var file = {};
			file["logIndex"] = 2;
			file["logName"] = fileName2;
			file["logPath"] = fileName2;
			file["logType"] = "java";
			file["rowCount"] = rowCount;
			data.push(file);
		}
		if ($("#file3") && $("#file3").val()) {
			fileName3 = $("#file3").val();
			var file = {};
			file["logIndex"] = 3;
			file["logName"] = fileName3;
			file["logPath"] = fileName3;
			file["logType"] = "C++";
			file["rowCount"] = rowCount;
			data.push(file);
		}
		currentData = data;
		queryUrl = 'dataFactory/getLogInfosByScroll';
		$('#importFile').modal('hide');
	});
	
	$('#importFile').on('hidden.bs.modal', getFileContent);

	$("#exeFilter").click(function() {
		if(!fileName1) {
			alert("请先加载文件!");
		}
		var data = [];
		
		var param1 = getqueryParams();
		param1["logIndex"] = 1;
		param1["logPath"] = fileName1;
		data.push(param1);
		
		var param2 = getqueryParams();
		param2["logIndex"] = 2;
		param2["logPath"] = fileName2;
		data.push(param2);
		
		var param3 = getqueryParams();
		param3["logIndex"] = 3;
		param3["logPath"] = fileName3;
		data.push(param3);
		
		currentData = data;
		queryUrl = 'dataFactory/getLogInfosByCon';
		
		$('#onLodding').modal('show');
	});
	
	var getqueryParams = function() {
		var	baseParas = {};
		baseParas["startTime"] = $("#startTime").val();
		baseParas["endTime"] = $("#endTime").val();
		baseParas["prossNo"] = $("#processName").val();
		var level = $("#logLevel").val();
		if(level == "请选择...") {
			level = "";
		}
		baseParas["level"] = level;
		baseParas["context"] = $("#key").val();
		baseParas["rowCount"] = rowCount;
		
		return baseParas;
	}
});

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
