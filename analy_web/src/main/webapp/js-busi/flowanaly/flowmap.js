var preUrl = WEB_ROOT+"/flowAnaly/";

//店内路径-流量来源  获取数据
function searchSourceData(pageNo){
	var params = getQueryParams();
	params.target = $("#targetId > li.active").attr("target");
	params.pageNo = pageNo;
	params.pageSize = 10;
	
	ajaxLoadPage(params,preUrl+"getFlowDirectionSourceDate",$("#FlowMapTableId"),function(){
		$("#FlowMapDateModalLabel").html("流量来源");
		$('#FlowMapDateModal').modal('show');
		var $sourcePvPagerId = $("#sourcePvPagerId");
		if($sourcePvPagerId && $sourcePvPagerId.length == 1){
			$sourcePvPagerId.pager({showSkip:true,showPnpage:true,callback:function(pageNo){
				searchSourceData(pageNo);
			}});
		}
	});
}

//店内路径-流量去向  获取数据
function searchDestData(pageNo){
	var params = getQueryParams();
	params.target = $("#targetId > li.active").attr("target");
	params.pageNo = pageNo;
	params.pageSize = 10;
	
	ajaxLoadPage(params,preUrl+"getFlowDirectionDestDate",$("#FlowMapTableId"),function(){
		$("#FlowMapDateModalLabel").html("流量去向");
		$('#FlowMapDateModal').modal('show');
		var $destPvPagerId = $("#destPvPagerId");
		if($destPvPagerId && $destPvPagerId.length == 1){
			$destPvPagerId.pager({showSkip:true,showPnpage:true,callback:function(pageNo){
				searchDestData(pageNo);
			}});
		}
	});
}

//流量来源  获取数据
function searchMainSourceData(pageNo){
	var params = getQueryParams();
	params.target = $("#targetId > li.active").attr("target");
	params.pageNo = pageNo;
	
	ajaxLoadPage(params,preUrl+"getFlowMapSourceDate",$("#sourceTableId"),function(){
		$("#sourceTableId #mapSourcePagerId").pager({showSkip:true,callback:function(pageNo){
			searchMainSourceData(pageNo);
		}});
	});
}

//全平台-流量来源  获取数据
function searchRefDomainData(pageNo){
	var params = getQueryParams();
	params.pageNo = pageNo;
	
	ajaxLoadPage(params,preUrl+"getFlowRefDomainData",$("#refDomainTableId"),function(){
		$("#mapRefDomainPagerId").pager({showSkip:true,callback:function(pageNo){
			searchRefDomainData(pageNo);
		}});
	});
}

//流量去向  获取数据
function searchMainDestData(pageNo){
	var params = getQueryParams();
	params.target = $("#targetId > li.active").attr("target");
	params.pageNo = pageNo;
	
	ajaxLoadPage(params,preUrl+"getFlowMapDestDate",$("#destTableId"),function(){
		$("#destTableId #mapdestPagerId").pager({showSkip:true,callback:function(pageNo){
			searchMainDestData(pageNo);
		}});
	});
}

//继承查询方法
function queryDate(params){
	params.target = $("#targetId > li.active").attr("target");
	
	//概览
	if($("#flowMapOverviewId").length){
		ajaxLoadPage(params,preUrl+"getFlowMapOwDate",$("#flowMapOverviewId"));
	}
	
	//店内路径
	if($("#flowDirectionDiv").length){
		ajaxLoadPage($.extend({pageSize:5,showPage:"more"},params),preUrl+"getFlowDirectionDate",$("#flowDirectionDiv"));
	}
	
	//浏览来源
	if($("#sourceTableId").length){
		//流量来源
		searchMainSourceData(1);
	}
	
	//全平台-外站来源
	if($("#refDomainTableId").length){
		searchRefDomainData(1);
	}
	
	//流量去向
	searchMainDestData(1);
}

function getFlowDirectionDate(target){
	$("#targetId > li.active").removeClass("active");
	$("#targetId li[target='"+target+"']").addClass("active");
	queryDate(getQueryParams());
}



$(document).ready(function(){
	$("#targetId > li > a").click(function(){
		$("#targetId > li.active").removeClass("active");
		$(this).parent().addClass("active");
		$(this).blur();
		
		queryDate(getQueryParams());
	});
	
	queryDate(getQueryParams());
	
	$('#exportFlowSourceBtn').click(function(){
    	var params = getQueryParams();
    	params.target = $("#targetId > li.active").attr("target");
    	window.location = preUrl+'exportFlowSourceData.xls?dateFrom='+params.dateFrom+'&dateTo='+params.dateTo
    		+"&target="+params.target;
    	
    });
	
	$('#exportPageFlowBtn').click(function(){
    	var params = getQueryParams();
    	window.location = preUrl+'exportPageFlowData.xls?dateFrom='+params.dateFrom+'&dateTo='+params.dateTo;
    });
	
	$("#url_select").change(function(){
		$("#url_input").val($(this).val());
	});
	
	$("#pageUrlSearchButtonId").click(function(){
		var params = getQueryParams();
		params.url = $("#url_input").val();
		
		ajaxLoadPage(params,preUrl+"getFlowMapDestDate",$("#pageUrlQueryTableId"),function(){
//完全匹配查询，不分页
//			$("#pageUrlQueryTableId #mapdestPagerId").pager({showSkip:true,callback:function(pageNo){
//				searchMainDestData(pageNo);
//			}});
		});
	})
});