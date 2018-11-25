String.prototype.replaceAll = function (FindText, RepText) {
	regExp = new RegExp(FindText, "g");
	return this.replace(regExp, RepText);
}

String.prototype.startWith=function(str){
	var reg=new RegExp("^"+str);
	return reg.test(this);
}


//日志联动开关
var logScrollFlag = true;

//单行日志渲染
var lineStaticStr = "<span class='log_text' id='#TIMESTR#'><span class='log_text_lineNO'>#LINENO#:</span> <span class='log_text_time'>#TIME#</span> <span class='log_text_prossNo'>#PROSSNO#</span> <span class='log_text_level'>#LEVEL#</span> <span class='log_text_pre'>#PRE#</span> <span class='log_text_context'>#CONTEXT#</span> </span><br/>"; 
// 从后台获取显示模版
var url_tlp = 'dataFactory/getlogTmplate';
apiGet(url_tlp, function(json){
	if(json && json.length >0){
		console.log(json);
		lineStaticStr = json;
	}
});

//日志查询的基本过滤参数集合
var baseParas = {};

function initLogContext(data, url){
	console.log(new Date()+"****initLogContext");
	//单行日志渲染DIV
	var oneStaticDiv = '<div class="log_display"><p class="log_path">#LOGPATH#</p><pre id="log_display_#INDEX#" onscroll="funcsrcoll(this, #COUNT#)"></pre></div>'; 
	var preInitDIV = function(data, count){
		return oneStaticDiv.replace('#INDEX#', data.logIndex).replace('#COUNT#', count).replace('#LOGPATH#', data.logPath);
	};
	
	//单个日志数据读取后加载
	var startInitData = function(onedata){
		$("#log_display_"+(onedata.logIndex)).append(formatData(onedata.data));
	};
	
	function replaceData(arr, i){
	    var result = lineStaticStr;
	    
		var lineData = arr[i]; 
		lineData['lineNO'] = (i+1);
		for(var x in lineData){
			result = result.replaceAll('#'+ String(x).toUpperCase() +'#', lineData[x]);
		}
		return result;
	}
	
	//格式化单行日志数据
	var formatData = function(arr){
		var result = "";
		for(var i=0; i< arr.length; i++){
			result += replaceData(arr, i);
		}
		return result;
	};
	
	$("#logPriview").empty();
	for(var i=0; i<data.length; i++){
		var params = data[i];
		$("#logPriview").append(preInitDIV(params, data.length));
		
		apiPost(url, params, function(json){
			if('success' == json.result){
				startInitData(json);
			}else{
				console.error("error load data");
			}
		});
	}
	
	$('#onLodding').modal('hide');
}

//div 滚动监听
function funcsrcoll(obj, count)
{
	var oid = obj.id;
    var idx =  parseInt(obj.id.split('log_display_')[1]);
	if(logScrollFlag && idx<count){
		const ac = document.querySelector('#logPriview');
		const lcFirst = ac.querySelector('#log_display_1');
		const lc = ac.querySelector('#log_display_' + idx);
		const rc = ac.querySelector('#log_display_' + (idx+1));
		
		const rc0 = rc.querySelector(".log_text");
		
		var toped_elmt = getTopedElement(lc);
		if(toped_elmt && toped_elmt.id){
			var near_elmt = getNearElement(rc, toped_elmt.id);
			if(near_elmt){
				var right_st = near_elmt.offsetTop;
				if(near_elmt.id!=rc0.id){
					right_st -= rc0.offsetTop;
					rc.scrollTop =  right_st;
				}
			}
		}else{
			rc.scrollTop =  0;
		}
		
		if ((lcFirst.scrollHeight - lcFirst.scrollTop) == lcFirst.clientHeight) {
			currentData = queryFirst(rowCount);
			queryUrl = 'dataFactory/getLogInfosByScroll';
			initLogContext(currentData, queryUrl);
		}
	}
}

function getTopedElement(searchObj)
{
	var topedSP = null;
	
	var search_arr = searchObj.querySelectorAll(".log_text");
	for(var i = search_arr.length-1; i>-1; i--)
	{
		var ct = search_arr[i].offsetTop;
		if(search_arr[i].offsetTop >= searchObj.scrollTop){
			continue;
		}else{
			var vi = (i>-1)?(i+2):(0)
			topedSP = search_arr[Math.min(vi,search_arr.length-1)];
			break;
		}
	}
	
	return topedSP;
}

// 根据时间点获取最接近的元素(根据模糊匹配的时间字符串查找,在结果中根据校对值查找最接近的元素)
// rightObj      模糊匹配的父节点对象
// timeStr       时间字符串
function getNearElement(searchObj, timeStr)
{
	
    /* 根据级别获取要模糊查找的时间字符串 */
    function getSearchStr(timeStr, level){
	    var search_end_idx = 0;
		switch(level){
			case 0:
				search_end_idx = 20;
				break;
			case 1:
				search_end_idx = 17;
				break;
			case 2:
				search_end_idx = 14;
				break;
			case 3:
				search_end_idx = 11;
				break;
			case 4:
				search_end_idx = 8;
				break;
			case 5:
				search_end_idx = 5;
				break;
			default:
				search_end_idx = 0;
		}
		return timeStr.substring(0, search_end_idx);
	}
	/* 根据级别获取要比对接近查找的时间数字 */
	function getCompareNumber(timeStr, level){
	    var search_start_idx = 0;
	    var search_end_idx = timeStr.length-1;
		switch(level){
			case 0:
				search_start_idx = 20;
				search_end_idx = 23;
				break;
			case 1:
				search_start_idx = 17;
				search_end_idx = 19;
				break;
			case 2:
				search_start_idx = 14;
				search_end_idx = 16;
				break;
			case 3:
				search_start_idx = 11;
				search_end_idx = 13;
				break;
			case 4:
				search_start_idx = 8;
				search_end_idx = 10;
				break;
			case 5:
				search_start_idx = 5;
				search_end_idx = 7;
				break;
			default:
				search_start_idx = 0;
				search_end_idx = timeStr.length;
		}
		var sub_str = timeStr.substring(search_start_idx, search_end_idx);
		return Number(sub_str);
	}
	/* 模糊查找,返回对应的最接近的元素 */
	function searchByTimeStr(searchObj, timeStr, level){
	    var activeElmt = null;
		
	    var search_str = getSearchStr(timeStr, level);
	    var compare_int = getCompareNumber(timeStr, level);
		var queryResult_arr = searchObj.querySelectorAll("span[id^='"+ search_str +"']");
		for(var i = 0; i < queryResult_arr.length; i++)
		{
			var one_compare_int = getCompareNumber(queryResult_arr[i]["id"], level);
			if( one_compare_int > compare_int){
			    var _one_index = (i>0?(i-1):0);
				activeElmt = queryResult_arr[_one_index];
				break;
			}
		}
		return activeElmt;
	}
    
    var result = null;
	// 查找级别lvl(0:毫秒; 1:秒; 2:分钟; 3:小时; 4:条; 5:月;)
	for(var lvl=0; lvl < 6; lvl++){
		result = searchByTimeStr(searchObj, timeStr, lvl);
		if(result){
			break;
		}
	}
	return result;
}