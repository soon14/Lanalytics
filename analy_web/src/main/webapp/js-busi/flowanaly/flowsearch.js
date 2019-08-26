var preUrl = WEB_ROOT+"/flowAnaly/";

//获取平台热词TOP10
function getPlatformSearchDate(more,pageNo){
	//获取平台热词TOP10
	var preUrl = WEB_ROOT+"/flowAnaly/";
	var $table = null;
	var params = getQueryParams();
	params.showPage = false;
	var callBack = undefined;
	
	if(more == true){
		$table = $("#SearchTableId");
		$("#SearchDateModalLabel").html("平台搜索热词排行");
		$('#SearchDateModal').modal('show');
		params.pageNo = pageNo;
		params.pageSize = 10;
		params.showPage = true;
		
		callBack = function(){
			$('#platformPagerId').pager({callback:function(pageNo){
				getPlatformSearchDate(true,pageNo);
			}});
		};
		
	}else{
		$table = $("#platformSearchTableId");
		params.pageNo = 1;
		params.pageSize = 10;
	}
	
	ajaxLoadPage(params,preUrl+"getPlatformSearchDate",$table,callBack);
}

//继承查询方法
function queryDate(params){
	
	
	ajaxLoadPage(params,preUrl+"getSearchOverviewData",$("#searchOverviewDiv"));
	
	getPlatformSearchDate(false,1);
}

$(document).ready(function(){
	//查询数据
	queryDate(getQueryParams());
	
});

function exportPlatformSearch(){
	var params = getQueryParams();
	window.location = preUrl+'exportPlatformSearch.xls?dateFrom='+params.dateFrom+'&dateTo='+params.dateTo;
}