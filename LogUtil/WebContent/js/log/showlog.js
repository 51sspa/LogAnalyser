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

var fileCount = -1;
function initLogContext(data){
	//单行日志渲染DIV
	var oneStaticDiv = '<div id="log_display_#INDEX#" class="log-display" onscroll="funcsrcoll(this, #COUNT#)"><pre></pre></div>'; 
	var preInitDIV = function(count, index){
		return oneStaticDiv.replace('#INDEX#', index).replace('#COUNT#', count);
	};
	
	//单个日志数据读取后加载
	var startInitData = function(data){
		console.dir(data);
		$("#log_display_"+(data.index)+" pre").append(formatData(data.data));
	};
	
	//单行日志渲染
	var lineStaticStr = '<a><span class="lineSP" id="#TIME#"><span class="log-text-lineNO">#LINENO#:</span> <span class="log-text-time">#TIME#</span> <span class="log-text-level">#LEVEL#</span> <span class="log-text-pre">#PRE#</span> <span class="log-text-context">#CONTEXT#</span></span><br/></a>'; 
	function replaceData(arr, i){
		var lineData = arr[i]; 
		var timeStr = lineData.time.substring(0,16);
		
	    var result = lineStaticStr;
		
		result = result.replaceAll('#TIMESTR#', timeStr);
	    result = result.replaceAll('#LINENO#', (i+1));
	    result = result.replaceAll('#TIME#', lineData.time);
	    result = result.replaceAll('#LEVEL#', lineData.level);
	    result = result.replaceAll('#PRE#', lineData.pre);
	    result = result.replaceAll('#CONTEXT#', lineData.context);
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
	
	
	fileCount = data.length;

	var url = 'test/sayHello';
	$("#logPriview").empty();
	for(var i=0; i<fileCount; i++){
		var oneData = data[i];
		$("#logPriview").append(preInitDIV(fileCount,oneData.logIndex));
		
		var params = oneData;
		url = './data/log'+(i+1)+'.json';
		apiPost(url, params, startInitData);
	}

}


//div 滚动监听
function funcsrcoll(obj, count)
{
    var idx =  parseInt(obj.id.split('log_display_')[1]);
	if(logScrollFlag && idx<count){
		const ac = document.querySelector('#logPriview');
		const lc = ac.querySelector('#log_display_' + idx);
		const rc = ac.querySelector('#log_display_' + (idx+1))
		
		const rc0 = rc.querySelector(".lineSP");
		
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
		}
	}
}

function getTopedElement(searchObj)
{
	var search_arr = searchObj.querySelectorAll(".lineSP");
	
	var ssTop = searchObj.scrollTop;
	var topedSP = null;
	for(var i = 0; i<search_arr.length; i++)
	{
		if(ssTop < search_arr[i].offsetTop){
			ssTop = search_arr[i].offsetTop;
			//topedSP = search_arr[i];
			continue;
		}else{
			topedSP = search_arr[i+1];
			//break;
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