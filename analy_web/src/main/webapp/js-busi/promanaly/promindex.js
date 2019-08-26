var eChartInstant = null;
var eChartTheme = null;

var preUrl = WEB_ROOT+"/promAnaly/";

require.config({
	paths: { echarts: WEB_ROOT + '/js/echarts' }
});

$(document).ready(function(){
	//初始化图标
	require(
			['echarts',
			 'echarts/chart/line'],
			 function (ec,theme){
				eChartInstant = ec;
				eChartTheme = theme;
			 }
	);
});

//促销列表点击
function loadPromDetail(aPromId, aPromName, aStartTime, aEndTime){
	$('#promIndexDiv').show();
	
	promId = aPromId;
	promStartTime = aStartTime;
	promEndTime = aEndTime;
	
	$('#promIndexHead').text('促销指标明细-'+aPromName);
	//设置时间
	$("#promindex_params_date > li.active").removeClass("active");
	$("#promIndexPromDateLi").addClass("active");
	var dateFromObj = $('#promIndexDateFrom').datetimepicker();
	var dateToObj = $('#promIndexDateTo').datetimepicker();
	dateFromObj.datetimepicker("setDate",new Date(promStartTime));
	dateToObj.datetimepicker("setDate",new Date(promEndTime));
	
	queryPromIndex(getIndexQueryParams());
	
	$('html,body').animate({scrollTop: $("#promIndexAnchor").offset().top-50},'slow');
}

//加载促销指标明细
function queryPromIndex(params){
	dateCompData = null;
	
	//加载概览
	ajaxLoadPage(params,preUrl+"getPromIndexOverview",$("#promIndexOverviewDiv"));
	
	if(params.dateFrom != params.dateTo){//不支持24小时趋势查询
		//指标趋势
		ajaxLoadPage(params,preUrl+"getPromIndexTrend",$("#promIndexTrendDiv"));
	}else{
		$("#promIndexTrendDiv").html('');
	}
	
	//商品列表
	queryPromGdsTable(params);
}

//查询促销商品列表
function queryPromGdsTable(params,pageNo){
	if(pageNo == undefined){
		pageNo = 1;
	}
	params.pageNo = pageNo
	params.pageSize = 10;
	
	if(params.option == undefined){
		params.option = "uv";
	}
	if(params.sort == undefined){
		params.sort = "desc";
	}
	
	var gdsQueryCallBack = function(data){
		goodsTableSort(params.option,params.sort);
		$("#gdsTablePagerId").pager({callback:function(pageNo){
			queryPromGdsTable(params,pageNo);
		}});
		
		$("img.imgBgSmall").error(function(){
			$(this).attr("src",WEB_ROOT + "/images/blank.png");
		});
	}
	
	//加载商品列表
	ajaxLoadPage(params,preUrl+"getPromGdsTable",$("#promGdsTableDiv"),gdsQueryCallBack);
}

//启用排序
function goodsTableSort(name,sort){
	var index = $("#promGdsTableDiv table thead th").index($("#promGdsTableDiv table thead").find("th[col_name='"+name+"']"));
	$("#promGdsTableDiv table").tablesorter({
		sortList : [[index,sort]],
		selectorHeaders:"> thead th:gt(2)",
		serverSideSorting:true,
		sortMultiSortKey : '', 
		sortResetKey     : '',
		//headerTemplate : '{content}{icon}'
	}).unbind("sortBegin").bind("sortBegin",function(e, table) {
		/*后台排序  serverSideSorting:true,*/
		var sortList = $(table).data("tablesorter").sortList;
		if(sortList.length == 1){
			var cell = $(this).find("thead th:eq("+sortList[0][0]+")");
			var colName = cell.attr("col_name");
			var sort = sortList[0][1] ===1?"desc":"asc";
			
			var params = getIndexQueryParams();
			params.option = colName;
			params.sort = sort;
			
			queryPromGdsTable(params);
		}
	});		
}

function popGdsIndexDetailTable(skuId, skuName){
	var params =  getIndexQueryParams();
	params.skuId = skuId;
	//pageno
	$('#gdsindex-modal').modal('show');
	ajaxLoadPage(params,preUrl+"getGdsIndexTrend",$("#gdsindex-detaildiv"));
	
	
	$("#gdsindex-gdsname").text("商品："+skuName);
	$("#gdsindex-daterange").text("时间："+params.dateFrom+"至"+params.dateTo);
}