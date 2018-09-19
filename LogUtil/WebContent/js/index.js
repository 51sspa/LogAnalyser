function apiPost(url, params, successCallback, failCallback){
	var data = {};
	if(params){
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
			if(typeof(json) == 'string')
			{
				json = JSON.parse(data);
			}
			if(successCallback){
				successCallback(json);
			}else{
				console.log(json);
			}
		},
		error : function(e) {
			if(failCallback){
				failCallback(json);
			}else{
				console.log(e);
			}
		}
	});
};

function loadFiles(){
	//apiPost('test/sayHello', null, loadLog);
	var loadLog = function(data){
		initLogContext(data);
	};
	
	var testData = [
		{
		'logIndex' : 1,
		'logName' : '/js/common/jquery/test1.log',
		'logType' : 'java',
		},
		{
		'logIndex' : 2,
		'logName' : '/js/common/jquery/test2.log',
		'logType' : 'java',
		},
		{
		'logIndex' : 3,
		'logName' : '/js/common/jquery/test3.log',
		'logType' : 'C++',
		}
	];
	loadLog(testData);
};

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
						alert(json.msg);
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

