var preUrl = WEB_ROOT+"/promAnaly/";

//继承查询方法
function queryPromList(params,pageNo){
	
	if(pageNo == undefined){
		pageNo = 1;
	}
	params.pageNo = pageNo
	
	var queryCallBack = function(data){
		$("#promTablePagerId").pager({callback:function(pageNo){
			queryPromList(params,pageNo);
		}});
	}
	
	ajaxLoadPage(params,preUrl + "getPromList",$("#promTableId"),queryCallBack);
}

//查询按钮点击时
function searchDatas(){
	var params = getPromQueryParams();
	queryPromList(getPromQueryParams());
}

function getPromQueryParams(){
	var promName = $('#promName').val();
	var promTypeCode = $('#promTypeCode').val();
	var promStatus = $('#promStatus').val();
	var siteId = $('#siteId').val();
	var promShopId = $('#promShopId').val();
	var dateFrom = $('#prom_dateFrom').datetimepicker("getFormattedDate");
	var dateTo = $('#prom_dateTo').datetimepicker("getFormattedDate");
	
	var params = {
		promName:promName,
		promTypeCode:promTypeCode,
		status:promStatus,
		siteId:siteId,
		paramShopId:promShopId,
		dateFrom:dateFrom,
		dateTo:dateTo
	};
	
	return params;
}

$(document).ready(function(){
    //查询
	queryPromList(getPromQueryParams());
});