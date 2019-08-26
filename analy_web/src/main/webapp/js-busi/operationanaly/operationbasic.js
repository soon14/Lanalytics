var eChartInstant = null;
var eChartTheme = null;
var saleCountChart = null;
var saleMoneyChart = null;

// 对组件的引用
var preUrl = WEB_ROOT + "/operationAnaly/";

require.config({
	paths : {
		echarts : WEB_ROOT + '/js/echarts'
	}
});

var flowQueryCallBack = function(data) {
	$("#flowMonthSumPagerId").pager({
				callback : function(pageNo) {
					var params = getQueryParams();
					params.pageNo = pageNo;
					ajaxLoadPage(params, preUrl + "getFlowMonthSumTable",$("#flowMonthSumTableDiv"), flowQueryCallBack);
				}
			});
}

var tradeQueryCallBack = function(data) {
	$("#tradeMonthSumPagerId").pager({
				callback : function(pageNo) {
					var params = getQueryParams();
					params.pageNo = pageNo
					ajaxLoadPage(params, preUrl + "getTradeMonthSum",$("#tradeMonthSumDiv"), tradeQueryCallBack);
				}
			});
}

// 继承方法
function queryDate(params, pageNo) {
	params.pageNo = 1

	ajaxLoadPage(params, preUrl + "getFlowMonthSum",$("#flowMonthSumDiv"));
	ajaxLoadPage(params, preUrl + "getFlowMonthSumTable",$("#flowMonthSumTableDiv"), flowQueryCallBack);
	ajaxLoadPage(params, preUrl + "getFlowMonthSumLine",$("#flowMonthSumLineDiv"));
	ajaxLoadPage(params, preUrl + "getTradeMonthSum", $("#tradeMonthSumDiv"),tradeQueryCallBack);
}

//设置数据
function setPieOption(pieChart, pieOption, title, axis, pieData) {
	var hasDate = false;
	if (pieData && pieData.length > 0) {
		pieOption.title.text = title;
		pieOption.series[0].name= title;
		pieOption.series[0].data = [];
		pieOption.legend.data = [];

		$(pieData).each(function(i, d) {
			pieOption.series[0].data.push({value:pieData[i],name:axis[i]});
			pieOption.legend.data.push(axis[i]);
		});

		pieChart.setOption(pieOption);
		
	} else {
		$(pieChart.dom).html("<div>暂无数据</div>");
	}
}

// 初始化销售分类饼图
function renderSalePieChart(data) {
	saleCountChart = eChartInstant.init(document.getElementById('saleCountPie'),eChartTheme);
	saleMoneyChart = eChartInstant.init(document.getElementById('saleMoneyPie'),eChartTheme);
	
	var pieOption = {
		title : {
		    x:'center'
		},
		tooltip : {
		    formatter: "系列\"{a}\" 点 \"{b}\"<br/>值:{c}({d}%)"
		},
		legend : {
	        show:false,
			data : []
		},
		series : [ {
			radius : '55%',
            center: ['50%', '60%'],
			type : 'pie',
			data : []
		} ]
	};

	setPieOption(saleCountChart, pieOption, '销售数量', data.axis, data.saleCounts);
	setPieOption(saleMoneyChart, pieOption, '销售额', data.axis, data.saleMoneys);
}

//初始化购买分布饼图
function renderOrdersTypePieChart(data) {
	staffNumChart = eChartInstant.init(document.getElementById('staffNumPie'),eChartTheme);
	payAmountChart = eChartInstant.init(document.getElementById('payAmountPie'),eChartTheme);
	
	var newAxis = [];
	for(i=0;i<data.axis.length;i++){
		newAxis.push("购买"+data.axis[i]+"次")
	}
	
	var pieOption = {
		title : {
		    x:'center'
		},
		tooltip : {
		    formatter: "{b}<br/>{d}%"
		},
		legend : {
	        show:false,
			data : []
		},
		series : [ {
			radius : '55%',
            center: ['50%', '60%'],
			type : 'pie',
			data : []
		}]
	};

	setPieOption(staffNumChart, pieOption, '客户数量', newAxis, data.staffNums);
	setPieOption(payAmountChart, pieOption, '购买金额', newAxis, data.payAmounts);
}

$(document).ready(function() {
	// 初始化图标
	require([ 'echarts', 'echarts/chart/pie', 'echarts/chart/line','echarts/chart/map' ], 
		function(ec, theme) {
			eChartInstant = ec;
			eChartTheme = theme;

			//统计月报总数
			sumDate(getQueryParams());
			//加载月报
			queryDate(getQueryParams());

			//加载历史报表
			ajaxLoadPage({}, preUrl + "getTradeCategorySum", $("#tradeCategorySumDiv"));
			ajaxLoadPage({}, preUrl + "getTradeOrdersTypeSum", $("#tradeOrdersTypeSumDiv"));
	});
});