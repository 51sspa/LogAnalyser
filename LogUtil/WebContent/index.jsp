<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>日志分析工具</title>

<link rel="stylesheet" href="./css/base.css" type="text/css"></link>
 
<link rel="stylesheet" href="./css/bootstarp/admin-all-demo.css" type="text/css"></link>

<link rel="stylesheet" href="./css/file/fileinput.css" type="text/css"></link>
<link rel="stylesheet" href="./css/file/theme.css" type="text/css"></link>
<link rel="stylesheet" href="./css/bootstarp/bootstrap.css"
	type="text/css"></link>

<script type="text/javascript"
	src="./js/common/jquery/jquery-3.0.0.min.js"></script>
<script type="text/javascript"
	src="./js/common/jquery/jquery.json-2.4.min.js"></script>

<script type="text/javascript"
	src="./js/common/bootstarp/admin-all-demo.js"></script>
<script type="text/javascript" src="./js/common/file/fileinput.js"></script>
<script type="text/javascript" src="./js/common/bootstarp/bootstrap.js"></script>
<script type="text/javascript" src="./js/index.js"></script>
<script type="text/javascript" src="./js/log/showlog.js"></script>

</head>
<body style="position: absolute; width: 100%; height: 100%;">
	<div class="mycontainer">
		<div class="mycenter">
			<div class="myheader">
				<div class="form-group">
					<label for="logLevel" class="control-label"
						style="margin-top: 7px; float: left;">日志级别： </label>
					<div style="width: 120px; position: relative; float: left;">
						<select id="logLevel" class="form-control">
							<option>请选择...</option>
							<option>INFO</option>
							<option>DEBUG</option>
							<option>WARN</option>
							<option>ERROR</option>
						</select>
					</div>
				</div>
				<div class="form-group">
					<label for="processName" class="control-label"
						style="margin-top: -7px; float: left; margin-left: 10px;">进程名称：
					</label>
					<div
						style="width: 120px; position: relative; float: left; margin-top: -15px;">
						<input id="processName" type="text" class="form-control"
							placeholder="输入进程名称">
					</div>
				</div>
				<div class="form-group">
					<label for="startTime" class="control-label"
						style="margin-top: -7px; float: left; margin-left: 10px;">起始时间：
					</label>
					<div
						style="width: 150px; position: relative; float: left; margin-top: -15px;">
						<input class="form-control" id="startTime" type="text"
							placeholder="输入起始时间">
					</div>
				</div>
				<div class="form-group">
					<label for="endTime"
						style="margin-top: -7px; float: left; margin-left: 10px;">结束时间：
					</label>
					<div
						style="width: 150px; position: relative; float: left; margin-top: -15px;">
						<input class="form-control" id="endTime" type="text"
							placeholder="输入结束时间">
					</div>
				</div>
				<div class="form-group">
					<label for="key"
						style="margin-top: -7px; float: left; margin-left: 10px;">关键字：
					</label>
					<div
						style="width: 120px; position: relative; float: left; margin-top: -15px;">
						<input class="form-control" id="key" type="text"
							placeholder="输入关键字">
					</div>
				</div>
				<button id="exeFilter" class="btn btn-primary"
					style="margin-left: 10px; margin-top: -15px;">执行过滤</button>
				<button id="importBtn" class="btn btn-primary"
					style="float: right; margin-top: -15px;">加载文件</button>
			</div>
			<div class="mycontent" id="logPriview"></div>
		</div>
	</div>
	
	<!-- 导入日志 -->
	<div class="modal fade" id="importLog" tabindex="-1" role="dialog"
		aria-labelledby="logLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="content-box-wrapper" id="logLabel">选择文件</h4>
				</div>
				<div class="border-top border-red modal-body"
					style="padding-bottom: 150px;">
					<div class="form-group" style="float: left;">
						<label class="control-label"
							style="margin-top: 7px; float: left;">文件1</label>
						<div class="col-sm-11">
							<input id="file1" name="file1" type="file" class="file"
								data-show-preview="false" data-show-upload="false"
								data-show-cancel="false">
						</div>
					</div>
					<div class="form-group" style="float: left;">
						<label class="control-label"
							style="margin-top: 7px; float: left;">文件2</label>
						<div class="col-sm-11">
							<input id="file2" name="file2" type="file" class="file"
								data-show-preview="false" data-show-upload="false"
								data-show-cancel="false">
						</div>
					</div>
					<div class="form-group" style="float: left;">
						<label class="control-label"
							style="margin-top: 7px; float: left;">文件3</label>
						<div class="col-sm-11">
							<input id="file3" name="file3" type="file" class="file"
								data-show-preview="false" data-show-upload="false"
								data-show-cancel="false">
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button id="logConfim" type="button" class="btn btn-primary">确定</button>
					<button id="logCancel" type="button" class="btn btn-default"
						data-dismiss="modal">取消</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 导入日志 -->
	<div class="modal fade" id="importFile" tabindex="-1" role="dialog"
		aria-labelledby="logLabel" aria-hidden="true">
		<div class="modal-dialog" style="width: 400px;">
			<div class="modal-content" style="width: 400px; height: 300px;">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="content-box-wrapper" id="logLabel">文件列表</h4>
				</div>
				<div class="border-top border-red modal-body"
					style="padding-bottom: 130px;">
					<div class="form-group">
						<label for="file1" class="control-label"
							style="margin-top: 8px; float: left; margin-left: 10px;">文件1：
						</label>
						<div style="width: 300px; position: relative; float: left;">
							<input id="file1" type="text" class="form-control">
						</div>
					</div>
					<div class="form-group">
						<label for="file2" class="control-label"
							style="margin-top: 18px; float: left; margin-left: 10px;">文件2：
						</label>
						<div
							style="width: 300px; position: relative; float: left; margin-top: 10px;">
							<input id="file2" type="text" class="form-control">
						</div>
					</div>
					<div class="form-group">
						<label for="file3" class="control-label"
							style="margin-top: 18px; float: left; margin-left: 10px;">文件3：
						</label>
						<div
							style="width: 300px; position: relative; float: left; margin-top: 10px;">
							<input id="file3" type="text" class="form-control">
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button id="okBtn" type="button" class="btn btn-primary">确定</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">取消
					</button>
				</div>
			</div>
		</div>
	</div>

	<!-- loadding -->
	<div id="onLodding" class="modal fade " tabindex="-1" role="dialog"
		aria-labelledby="logLabel" aria-hidden="true">
		<div class="modal-dialog clearfix" style="top: 50%">
			<div class="loading-spinner" style="width: 0px; height: 0px;">
				<i class="bg-primary"></i> <i class="bg-primary"></i> <i
					class="bg-primary"></i> <i class="bg-primary"></i> <i
					class="bg-primary"></i> <i class="bg-primary"></i>
			</div>
		</div>
	</div>
</body>
</html>