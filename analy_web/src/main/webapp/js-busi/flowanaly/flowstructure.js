var eChartInstant = null;
var eChartTheme = null;
var pieChart1 = null;
var pieChart2 = null;
var pieChart3 = null;
var mapChart = null;

//对组件的引用
require.config({
	paths: { echarts: WEB_ROOT + '/js/echarts' }
});

//初始化饼图
function renderPieChart(data) {
	
	var ecConfig = require('echarts/config').EVENT;
	
	var grid = {x:0,y:0,x2:0,y2:0};
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
	
	$.fn.popover.Constructor.DEFAULTS.placement = "top";
	$.fn.popover.Constructor.DEFAULTS.html = true;
	$.fn.popover.Constructor.DEFAULTS.animation = false;
	$.fn.popover.Constructor.DEFAULTS.container="body";
	
	function setToolsTips($el,chart,getContext){
		//鼠标进入时
		chart.on(ecConfig.HOVER,function(params){
			var param = params;
			var res = getContext(params);
			$el.attr("data-content",res);
			$el.popover("show");
		});
		//鼠标离开时
		chart.on(ecConfig.MOUSEOUT,function(params){
			$el.popover('destroy');
		});
	}
	
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
				pieOption.series[0].data.push({rows:pieDate,row:d,value:d.uv,name:d.userTypeName,temStyle:{normal:{color:"#eadged"}}});
				pieOption.legend.data.push({name:d.userTypeName});
			});
			
			if(hasDate == true){
				if(pieOption.series[0].data.length>2){
					pieOption.series[0].itemStyle.normal.label.position = "";
					pieOption.series[0].itemStyle.normal.labelLine.show = true;
					pieOption.series[0].itemStyle.emphasis = {};
					//pieOption.legend.orient='vertical';
					//pieOption.legend.x='left';
				}else{
					pieOption.series[0].itemStyle.normal.label.position = "inner";
					pieOption.series[0].itemStyle.normal.labelLine.show = false;
					pieOption.series[0].itemStyle.emphasis = pieOption.series[0].itemStyle.normal;
				}
				pieChart.setOption(pieOption);
			}
		}
		
		if(hasDate == false){
			$(pieChart.dom).html("<div>暂无数据</div>");
		}
	}
	
	$(".popover").popover('destroy');
	
	//连锁渠道与非连锁渠道
	setPieOption(pieChart1,data.listChainScales);
	
	//设置提示消息
	setToolsTips($("#flowPie1"),pieChart1,function(param){
		var row = param.data.row; 
		var res =  "<div style='text-align:left;padding-left:15px;'>" + param.name + "</div>"
	      +"<ul>"
	      +   "<li style='text-align:left;'>访客数：（<font color='red'>"+row.uv+"</font>人）</li>"
	      +   "<li style='text-align:left;'>访客占比：（<font color='red'>"+getFixedNum(row.uvPercent*100,2)+"%</font>）</li>"
	      +   "<li style='text-align:left;'>下单转化率：（<font color='red'>"+getFixedNum(row.orderRate*100,2)+"%</font>）</li>"
	      +"</ul>";
		return res;
	});
	
	//名单制非名单制客户
	setPieOption(pieChart2,data.listNonScales);
	//设置提示消息
	setToolsTips($("#flowPie2"),pieChart2,function(param){
		var res =  "<div class='table-responsive'>"
            +"<table class='table table-bordered'>"
            +"<thead><tr><th>类型结构</th><th>访客数</th><th>占比</th><th>下单转化率</th></tr></thead>"
            +"<tbody>";
            
		var rows = param.data.rows;
		$(rows).each(function(i,d){
			res+="<tr><td>"+d.userTypeName+"</td><td>"+d.uv+"</td><td>"+getFixedNum(d.uvPercent*100,2)+"%</td><td>"+getFixedNum(d.orderRate*100,2)+"%</td></tr>"
		});
		
	    res+=   "</tbody>"
		     +"</table>"
	        +"</div>";
		return res;
	});
	
	//渠道购买力度分布
	setPieOption(pieChart3,data.listBuyLevelScales);
	
	//设置提示消息
	setToolsTips($("#flowPie3"),pieChart3,function(param){
		var row = param.data.row;
		var res =  "<div style='text-align:left;padding-left:15px;'>" + param.name + "</div>"
	      +"<ul>"
	      +   "<li style='text-align:left;'>访客数：（<font color='red'>"+row.uv+"</font>人）</li>"
	      +   "<li style='text-align:left;'>访客占比：（<font color='red'>"+getFixedNum(row.uvPercent*100,2)+"%</font>）</li>"
	      +   "<li style='text-align:left;'>订单数：（<font color='red'>"+row.orderUv+"</font>）</li>"
	      +   "<li style='text-align:left;'>订单占比：（<font color='red'>"+getFixedNum(row.orderUvPercent*100,2)+"%</font>）</li>"
	      +"</ul>";
		return res;
	});
}

function queryFlowDate(params,id,url){
	ajaxLoadPage(params,WEB_ROOT+"/flowAnaly/"+url,$(id));
}

function queryDate(params){
	
	queryFlowDate(params,"#sectionTableId","getFlowSectionDate");
	
	queryFlowDate(params,"#flowStructureDiv","getFlowStructureDate");
	
	queryFlowDate(params,"#flowTrendDiv","getFlowTrendDate");
	
	queryFlowDate(params,"#flowRateTableId","getFlowRateDate");
}

function getFixedNum(num,n){
	var numStr = num.toFixed(1);
	if(numStr.indexOf(".") > 0){ 
		var test = /0+?$/;
		numStr = numStr.replace(test, "");//去掉多余的0  
		test = /[.]$/
		numStr = numStr.replace(test, "");//如最后一位是.则去掉    
	}
    return numStr;
}

$(document).ready(function(){
	//初始化图标
	require(
			['echarts',
			 'echarts/chart/pie',
			 'echarts/chart/line'],
			 function (ec,theme){
				eChartInstant = ec;
				eChartTheme = theme;
				queryDate(getQueryParams());
			 }
	);
});