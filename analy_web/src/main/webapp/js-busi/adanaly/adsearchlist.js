var preUrl = WEB_ROOT+"/adAnaly/";

//继承查询方法
function queryAdList(params,pageNo){
	
	if(pageNo == undefined){
		pageNo = 1;
	}
	params.pageNo = pageNo
	
	var queryCallBack = function(data){
		$("#adTablePagerId").pager({callback:function(pageNo){
			queryAdList(params,pageNo);
		}});
	}
	
	ajaxLoadPage(params,preUrl + "getAdList",$("#adTableId"),queryCallBack);
}

//查询按钮点击时
function searchDatas(){
	var params = getAdQueryParams();
	queryAdList(getAdQueryParams());
}

function getAdQueryParams(){
	var siteId = $('#adSite').val();
	var templateId = $('#adTemplate').val();
	var placeId = $('#adPlace').val();
	var adTitle = $('#adTitle').val();
	var shopId = $('#adShopId').val();
	var status = $('#adStatus').val();
	var pubTimeFrom = $('#pubTimeFrom input').val();
	var pubTimeTo = $('#pubTimeTo input').val();
	var lostTimeFrom = $('#lostTimeFrom input').val();
	var lostTimeTo = $('#lostTimeTo input').val();
	
	var params = {
		siteId:siteId,
		templateId:templateId,
		placeId:placeId,
		adTitle:adTitle,
		paramShopId:shopId,
		status:status,
		firstDateFrom:pubTimeFrom,
		firstDateTo:pubTimeTo,
		secondDateFrom:lostTimeFrom,
		secondDateTo:lostTimeTo
	};
	
	return params;
}

$(document).ready(function(){
    //查询
	queryAdList(getAdQueryParams());
	
	$('#adSite').change(function(){
		$("#adTemplate").html('<option value="" selected >全部模板</option>')
		$("#adPlace").html('<option value="" selected >全部内容位置</option>');
		
		var params = {
			siteId:$('#adSite').val()
		}
		
		if(params.siteId !=''){
			ajaxLoadPage(params,preUrl + "getTemplateOptions",$("#adTemplate"));
		}
	});
	
	$('#adTemplate').change(function(){
		$("#adPlace").html('<option value="" selected >全部内容位置</option>');
		
		var params = {
			templateId:$('#adTemplate').val()
		}
		
		if(params.templateId !=''){
			ajaxLoadPage(params,preUrl + "getPlaceOptions",$("#adPlace"));
		}
	});
});