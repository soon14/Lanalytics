var eChartInstant = null;
var eChartTheme = null;

var preUrl = WEB_ROOT+"/adAnaly/";

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

//广告列表点击
function loadAdIndex(aadId, aadName, aStartTime, aEndTime){
	$('#adIndexDiv').show();
	
	adId = aadId;
	adStartTime = aStartTime;
	adEndTime = aEndTime;
	
	$('#adIndexHead').text('广告指标明细-'+aadName);
	//设置时间
	$("#adIndexParamsDate > li.active").removeClass("active");
	$("#adIndexEffectiveDateLi").addClass("active");
	var dateFromObj = $('#adIndexDateFrom').datetimepicker();
	var dateToObj = $('#adIndexDateTo').datetimepicker();
	dateFromObj.datetimepicker("setDate",new Date(adStartTime));
	dateToObj.datetimepicker("setDate",new Date(adEndTime));
	
	queryAdIndex(getAdIndexQueryParams());
	
	$('html,body').animate({scrollTop: $("#adIndexAnchor").offset().top-50},'slow');
}

//加载广告指标明细
function queryAdIndex(params){
	dateCompData = null;
	
	//加载概览
	ajaxLoadPage(params,preUrl+"getAdIndexOverview",$("#adIndexOverviewDiv"));
	
	if(params.dateFrom != params.dateTo){//不支持24小时趋势查询
		//指标趋势
		ajaxLoadPage(params,preUrl+"getAdIndexTrend",$("#adIndexTrendDiv"));
	}else{
		$("#adIndexTrendDiv").html('');
	}
}