var eChartInstant = null;
var eChartTheme = null;
var pieChart1 = null;
var pieChart2 = null;
var pieChart3 = null;
var pieChart4 = null;
var mapChart = null;

//对组件的引用
var preUrl = WEB_ROOT+"/flowAnaly/";

require.config({
	paths: { echarts: WEB_ROOT + '/js/echarts' }
});

//初始化饼图
function renderPieChart(data) {
		
	var pieOption = {
			legend: {
				y:0,
				padding: 0,
		        data:[],
		        selectedMode:false,
		    },
			series : [
	           {
	           itemStyle:{
	        	   normal : {
	                   label : {
	                        show : true,
	                        position : 'inner',
	                        formatter : function(param){
	                        	var percent = param.percent;
	                        	if(percent == 0){
	                        		return "0%";
	                        	}
	                        	
	                        	return getFixedNum(parseFloat(percent),1)+"%";
	                        }
	                   },
	                   labelLine : {
	                       show : false,
	                       length : 5
	                   }
	               }
	           },
	           name:'访问来源',
	           type:'pie',
	           radius:[0, '70%'],
	           center: ['50%', '65%'],
	           data:[]
	           }
			]
		};
	
	//设置数据
	function setPieOption(pieChart,pieDate){
		
		var hasDate = false;
		
		if(pieDate && pieDate.length>0){
			
			pieOption.series[0].data = [];
			pieOption.legend.data=[];
			
			$(pieDate).each(function(i,d){
				if(d.uv>0){
					hasDate = true;
				}
				pieOption.series[0].data.push({value:d.uv,name:d.userTypeName,temStyle:{normal:{color:"#eadged"}}});
				pieOption.legend.data.push({name:d.userTypeName});
			});
			
			if(hasDate == true){
				
				if(pieOption.series[0].data.length>2){
					pieOption.series[0].itemStyle.normal.label.position = "";
					pieOption.series[0].itemStyle.normal.labelLine.show = true;
					pieOption.series[0].itemStyle.emphasis = {};
				}else{
					pieOption.series[0].itemStyle.normal.label.position = "inner";
					pieOption.series[0].itemStyle.normal.labelLine.show = false;
					pieOption.series[0].itemStyle.emphasis = pieOption.series[0].itemStyle.normal;
				}
				
			    pieChart.setOption(pieOption);
			}
		}else{
			hasDate = false;
		}
		
		if(hasDate == false){
			$(pieChart.dom).html("<div>暂无数据</div>");
		}
	}
	
	//新老访客比
	var newOldScales = data.newOldScales;
	setPieOption(pieChart1,newOldScales);
	
	//名单制非名单制客户
	//var listNonScales = data.listNonScales;
	//setPieOption(pieChart2,listNonScales);
	
	//PC端与无线端
	var pcWlScales = data.pcWlScales;
	setPieOption(pieChart3,pcWlScales);
	
	//外链入口与自主访问
	//var inOutScales = data.inOutScales;
	//setPieOption(pieChart4,inOutScales);
	
	if(pieChart4){
		var listBuyLevelScales = data.listBuyLevelScales;
		setPieOption(pieChart4,listBuyLevelScales);
	}
}

//初始化地图
function renderMapChart(data){
	
	var mapOption =   {
		    tooltip : {
		    	show:true,
		        trigger: 'item'
		    },
		    dataRange: {
		        min: 0,
		        max: 1000,
		        splitNumber:7,
		        orient:'horizontal',
		        x: 'left',
		        y: 'top',
		        itemGap:0,
		        text:['访客数（UV）高','低'],
		        calculable : false
		    },
		    series : [
		        {
		            name: '访客数（UV）',
		            type: 'map',
		            mapType: 'china',
		            roam: false,
		            itemStyle:{
		                normal:{label:{show:false}},
		                emphasis:{label:{show:false}}
		            },
		            data:[]
		        }
		    ]
		};
	
	mapOption.series[0].data = [];
	var maxUv = 0;
	$(data).each(function(i,d){
		if(maxUv<d.uv) maxUv = d.uv;
		mapOption.series[0].data.push({name: d.provinceName,value: d.uv});
	});
	
	mapOption.dataRange.max = maxUv;
	mapChart.setOption(mapOption);
}

//继承方法
function queryDate(params){
	
	ajaxLoadPage(params,preUrl+"getOverviewDate",$("#flowOverviewDiv"));
	
	ajaxLoadPage(params,preUrl+"getFlowTrendDate",$("#flowTrendDiv"));
	
	ajaxLoadPage(params,preUrl+"getFlowMapDate",$("#flowMapDiv"));
	
	if($("#flowDirectionDiv").length){
		getFlowDirectionDate(g_target);
	}
}

function getFlowDirectionDate(target){
	var params = getQueryParams();
	params.pageSize=5;
	params.target=target;
	ajaxLoadPage(params,preUrl+"getFlowDirectionDate",$("#flowDirectionDiv"));
}

$(document).ready(function(){
	//初始化图标
	require(
			['echarts',
			 'echarts/chart/pie',
			 'echarts/chart/line',
			 'echarts/chart/map'],
			 function (ec,theme){
				eChartInstant = ec;
				eChartTheme = theme;
				queryDate(getQueryParams());
				
				ajaxLoadPage({},WEB_ROOT+"/flowAnaly/getFlowRateDate",$("#flowRateTableId"));
				
			 }
	);
	
	$('#exportFlowRateDataBtn').click(function(){
    	var params = getQueryParams();
    	window.location = preUrl+'exportFlowRateData.xls';
    });
});