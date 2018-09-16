$(function() {
	var fileName1 = "";
	var fileName2 = "";
	var fileName3 = "";
	
	//导入文件
	$("#logConfim").click(function() {
		if ($("#file1")[0]) {
			fileName1 = $("#file1")[0].files[0].name;
		}
		if ($("#file2")[0]) {
			fileName2 = $("#file2")[0].files[0].name;
		}
		if ($("#file3")[0]) {
			fileName3 = $("#file3")[0].files[0].name;
		}
		
		$('#importLog').modal('hide');
	});
	
	$("#exeFilter").click(function() {
		$(this).button('loading').delay(1000).queue(function() {
			$(this).button('reset');
			$(this).dequeue();
		});
	});
	
});
