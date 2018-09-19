<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>日志分析工具</title>

<link rel="stylesheet" href="./css/base.css" type="text/css"></link>
<link rel="stylesheet" href="./css/bootstarp/admin-all-demo.css"
	type="text/css"></link>
<link rel="stylesheet" href="./css/file/fileinput.css" type="text/css"></link>
<link rel="stylesheet" href="./css/file/theme.css" type="text/css"></link>
<link rel="stylesheet" href="./css/bootstarp/bootstrap.css"
	type="text/css"></link>
 
<script type="text/javascript" src="./js/common/jquery/jquery-3.0.0.min.js"></script>
<script type="text/javascript" src="./js/common/jquery/jquery.json-2.4.min.js"></script>

<script type="text/javascript"
	src="./js/common/bootstarp/admin-all-demo.js"></script>
<script type="text/javascript" src="./js/common/file/fileinput.js"></script>
<script type="text/javascript" src="./js/common/bootstarp/bootstrap.js"></script>
<script type="text/javascript" src="./js/index.js"></script>

<style type="text/css">
	pre, xmp, plaintext, listing {
		margin: 0em 0px;
	}
	a{
		width: 100%;
	}
	.myleft {
	    width: 6%;
	}
	.mycenter {
	    width: 94%;
	}
	.myhead {
	    width: 93%;
	}
	.mycontent {
	    width: 93%;
	}
	.mycontent {
		display:flex;
		flex-direction:row;
		height:90%;
	}
	.log-display {
	    flex:1;
		word-break: break-all;
		word-wrap: break-word;
		color: #555;
		background-color: #f8f8f8;
		overflow:scroll;
		border: 1px solid #dedede;
		border-radius: 0;
		height:100%;
		margin-right: 2px;
	}
	.log-text-lineNO {
		color: gray;
	}
	.log-text-time {
		color: #5eb95e;
	}
	.log-text-level {
		color: #dd514c;
	}
	.log-text-pre {
		color: #dd51ff;
	}
	.log-text-context {
		padding-right: 20px;
	}
</style>

<script type="text/javascript" src="./js/log/showlog.js"></script>
</head>
<body style="position:absolute;width:100%;height:100%;background-color: green;">
	<div class="mycontainer">
		<div class="myleft">
			<div class="panel panel-info">
				<div class="panel-heading">
					<h3 class="panel-title">基础配置</h3>
				</div>
				<div>
					<div style="margin: 10px 0px; text-align: center;">
						<button class="btn btn-primary" data-toggle="modal"
							data-target="#importLog">导入日志</button>
					</div>
					<div style="margin: 10px 0px; text-align: center;">
						<button class="btn btn-primary" data-toggle="modal"
							data-target="#settings">基本设置</button>
					</div>
				</div>
			</div>
		</div>
		<div class="mycenter">
			<div class="myheader">
				<div class="form-group">
					<label for="logLevel" class="control-label"
						style="margin-top: 7px; float: left;">日志级别： </label>
					<div style="width: 120px; position: relative; float: left;">
						<select id="logLevel" class="form-control">
							<option>Info</option>
							<option>Debug</option>
							<option>Warn</option>
							<option>Error</option>
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
						style="width: 120px; position: relative; float: left; margin-top: -15px;">
						<input class="form-control" id="startTime" type="text"
							placeholder="输入起始时间">
					</div>
				</div>
				<div class="form-group">
					<label for="endTime"
						style="margin-top: -7px; float: left; margin-left: 10px;">结束时间：
					</label>
					<div
						style="width: 120px; position: relative; float: left; margin-top: -15px;">
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
			</div>
			<div class="mycontent" id="logPriview">content</div>
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
		<div class="modal fade" id="settings" tabindex="-1" role="dialog"
			aria-labelledby="logLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-hidden="true">×</button>
						<h4 class="content-box-wrapper" id="logLabel">基本设置</h4>
					</div>
					<div class="border-top border-red modal-body"
						style="padding-bottom: 150px;">内容预留，暂时不清楚内容</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary">确定</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">取消
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>