var rowCount = 500;
var currentData = null;
var queryUrl = "";

$(function() {
	currentData = null;
	queryUrl = "";
	var isOK = false;
	
	var getFileContent = function() {
		if(isOK) {
			$('#onLodding').modal('show');
		}
	}
	
	$('#onLodding').on('shown.bs.modal', function () {
		if(currentData){
			initLogContext(currentData, queryUrl);
		}else{
			$('#onLodding').modal('hide');
			alert("请先加载文件!");
		}
	});
	
	$("#importBtn").click(function() {
		isOK = false;
		$('#importLog').modal('show');
	});
	
	// 确定
	$("#logConfim").click(function() {
		isOK = true;
		currentData = queryFirst(0);
		queryUrl = 'dataFactory/getLogInfosByScroll';
		$('#importLog').modal('hide');
	});
	
	$('#importLog').on('hidden.bs.modal', getFileContent);

	$("#exeFilter").click(function() {
		var fileName1 = $("#file1").val();
		var fileName2 = $("#file2").val();
		var fileName3 = $("#file3").val();
		if(!fileName1 && !fileName2 && !fileName3) {
			alert("请先加载文件!");
			return;
		}
		var data = [];
		var index = 0;
		if(fileName1) {
			index ++;
			var param1 = getqueryParams();
			param1["logIndex"] = index;
			param1["logPath"] = fileName1;
			data.push(param1);
		}
		if(fileName2) {
			index ++;
			var param2 = getqueryParams();
			param2["logIndex"] = index;
			param2["logPath"] = fileName2;
			data.push(param2);
		}
		if(fileName3) {
			index ++;
			var param3 = getqueryParams();
			param3["logIndex"] = index;
			param3["logPath"] = fileName3;
			data.push(param3);
		}
		
		currentData = data;
		queryUrl = 'dataFactory/getLogInfosByCon';
		
		$('#onLodding').modal('show');
	});
});

var queryFirst = function(num) {
	var data = [];
	var index = 0;
	
	if ($("#file1") && $("#file1").val()) {
		index++;
		fileName1 = $("#file1").val();
		var file = {};
		file["logIndex"] = index;
		file["startIndex"] = num;
		file["logName"] = fileName1;
		file["logPath"] = fileName1;
		file["logType"] = "java";
		file["rowCount"] = rowCount;
		data.push(file);
	}
	if ($("#file2") && $("#file2").val()) {
		index++;
		fileName2 = $("#file2").val();
		var file = {};
		file["logIndex"] = index;
		file["startIndex"] = num;
		file["logName"] = fileName2;
		file["logPath"] = fileName2;
		file["logType"] = "java";
		file["rowCount"] = rowCount;
		data.push(file);
	}
	if ($("#file3") && $("#file3").val()) {
		index++;
		fileName3 = $("#file3").val();
		var file = {};
		file["logIndex"] = index;
		file["startIndex"] = num;
		file["logName"] = fileName3;
		file["logPath"] = fileName3;
		file["logType"] = "C++";
		file["rowCount"] = rowCount;
		data.push(file);
	}
	
	return data;
}

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