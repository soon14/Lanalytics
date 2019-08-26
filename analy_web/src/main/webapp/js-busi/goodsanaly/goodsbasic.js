var eChartInstant = null;
var eChartTheme = null;
var lineChart = null;

//对组件的引用
require.config({
	paths: { echarts: WEB_ROOT + '/js/echarts' }
});

//初始化线性图
var allLineChartData;
function renderLineChart(data){
	allLineChartData = data;
	var xAxis = [];
	$(data).each(function(i,d){
		if(d.xtime != undefined)
		    xAxis.push(d.xtime);
	});
	
	var lineOption =  {
		grid:{
			borderWidth:0,
			x:70,
			x2:15,
			y:12
		},
	    legend: {
	    	y:"bottom",
	        data:[],
	        selectedMode:false
	    },
	    tooltip : {
	    	trigger: 'axis',
	        formatter:function(params){
	        	var tip = "";
	        	var xAxisName = "";
	        	$(params).each(function(i,param){
	        		xAxisName = formaterxAxis(param.name,true);
	        		var value = getFixedNum(param.value,2);
	        		tip += "</br>" + param.seriesName+"："+value
	        	});
	        	
	        	return xAxisName + tip;
	        }
	    },
	    xAxis : [
	        {
	            type : 'category',
	            boundaryGap : false,
	            splitLine:{show:false},
	            data : xAxis,
	            axisLabel : {
	            	//interval:0,
	                formatter:function(value){
	                	return formaterxAxis(value,false);
	                }
	            }
	        }
	    ],
	    yAxis : [
	        {
	            type : 'value',
	            axisLabel : {
	                formatter: '{value}'
	            },
	            axisLine:{show:false},
	        }
	    ],
	    series : []
	};
	
	
	//事件
	$("input[name='flowTrendCkb']").click(function(){
		
		var isChecked = $(this).is(":checked");
		var value = $(this).val();
		var type = $(this).attr("ckbType");
		
		var chkObjes = $("input[name='flowTrendCkb']:checked");
		
		if(chkObjes.length>0){
			
			var chkResult = checkedLine(isChecked,value);
			
			if(chkResult == true){
				$(chkObjes).each(function(i,obj){
					var chkValue = $(obj).val();
					var ckbType = $(this).attr("ckbType");
					
					if(ckbType != type){
						$("input[name='flowTrendCkb'][value='"+chkValue+"']").attr("checked",false);
						checkedLine(false,chkValue);
					}
				});
			}
		}else{
			return false;
		}
		
		return true;
	});
	
	function checkedLine(isChecked,value){
		
		if(allLineChartData==undefined){
			return false;
		}
		
		var lineName = $("input[name='flowTrendCkb'][value='"+value+"']").parent().text();
		
		var index = $.inArray(lineName,lineOption.legend.data);
		
		if(isChecked == true){
			
			if(index == -1){
			
				var lineData = [];
				
				$(allLineChartData).each(function(i,d){
					var lineValue = d[value];
					
					if(lineValue == undefined){
						lineValue = 0;
					}
					
					if("orderMoney" == value || "payMoney" == value){
						lineValue = lineValue/100;
					}
					
					if(d.xtime != undefined)
					    lineData.push(lineValue);
				});
				
				var seriesData = {
		          name:lineName,
		          type:'line',
		          data:lineData,
		          symbol:'circle',
		          smooth:true
			    };
				
				lineOption.series.push(seriesData);
				lineOption.legend.data.push(lineName);
				lineChart.setOption(lineOption,false);
			}
		}else{
			if(index != -1){
				lineOption.series.splice(index,1);
				lineOption.legend.data.splice(index,1);
				lineChart.setOption(lineOption,true);
			}
		}
		
		return true;
	}
	
	checkedLine(true,"pv");
}

//继承查询方法
function queryDate(params){
	
	var preUrl = WEB_ROOT+"/goodsAnaly/";
	
	ajaxLoadPage(params,preUrl + "getOverviewDate",$("#flowOverviewDiv"));
	
	ajaxLoadPage(params,preUrl + "getFlowTrendDate",$("#flowTrendDiv"));
	
	getTop10Page(params);
}

//获取商品排行概览页面
function getTop10Page(params,pageNo){
	var option = $("#goodsTopNav > li.active").attr("option");
	params.option = option;
	var preUrl = WEB_ROOT+"/goodsAnaly/";
	
	if(pageNo == undefined){
		pageNo = 1;
	}
	
	params.pageNo = pageNo
	params.pageSize = 10;
	
	var queryCallBack = function(data){
		$("img.imgBgSmall").error(function(){
			$(this).attr("src",WEB_ROOT + "/images/blank.png");
		});
		
		getGoodsTopRateTable(false,1);
	}
	
	ajaxLoadPage(params,preUrl + "getGoodsTop10Page", $("#goodsTop10"),queryCallBack);
}

//查询商品排行列表
function getGoodsTopRateTable(more,pageNo){
	var params = getQueryParams();
	var option = $("#goodsTopNav > li.active").attr("option");
	params.option = option;
	$("#catgCondDiv a.btn-primary").each(function(index){
		var catLevel = $(this).attr("catLevel")
		params["gdscatg"+catLevel] = $(this).attr("catId");
	});
	params.showPage = false;
	
	var callBack = undefined;
	var $table = null;
	if(more == true){
		$table = $("#TopRateModalTableId");
		//$("#TopRateModalLabel").html("平台搜索热词排行");
		$('#TopRateModal').modal('show');
		params.pageNo = pageNo;
		params.pageSize = 10;
		params.showPage = true;
		
		callBack = function(){
			$('#TopRateModalPagerId').pager({callback:function(pageNo){
				getGoodsTopRateTable(true,pageNo);
			}});
		};
		
	}else{
		$table = $("#TopRateTableId");
		params.pageNo = 1;
		params.pageSize = 10;
	}
	
	var preUrl = WEB_ROOT+"/goodsAnaly/";
	ajaxLoadPage(params,preUrl+"getGoodsTopRateTable",$table,callBack);
}

//导出商品排行列表
function exportGoodsTopRateTable(){
	var params = getQueryParams();
	var option = $("#goodsTopNav > li.active").attr("option");
	params.option = option;
	$("#catgCondDiv a.btn-primary").each(function(index){
		var catLevel = $(this).attr("catLevel")
		params["gdscatg"+catLevel] = $(this).attr("catId");
	});
	
	var preUrl = WEB_ROOT+"/goodsAnaly/";
	var paramStr = "";
	for (var key in params) {
	    if (paramStr != "") {
	    	paramStr += "&";
	    }
	    paramStr += key + "=" + params[key];
	}
	window.location = preUrl+'exportGoodsTopRateTable.xls?'+paramStr;
}

function getFixedNum(num,n){
	var numStr = num.toFixed(n);
	if(numStr.indexOf(".") > 0){ 
		var test = /0+?$/;
		numStr = numStr.replace(test, "");//去掉多余的0  
		test = /[.]$/
		numStr = numStr.replace(test, "");//如最后一位是.则去掉    
	}
    return numStr;
}

function formaterxAxis(value,flag){
	
	if(value){
		var str = value+"";
		var index = str.lastIndexOf("-");
		if(index > 0){
			if(flag == true){
				return value;
			}else{
				return str.substring(index+1)+"日";
			}
		}else{
			return value+"时";
		}
		
	}else{
		return value;
	}
}

$(document).ready(function(){
	//初始化图标
	require(['echarts','echarts/chart/line'],
		function (ec,theme){
			eChartInstant = ec;
			eChartTheme = theme;
			queryDate(getQueryParams());
		}
	);
	
	$("#goodsTopNav > li > a").click(function(){
		$("#goodsTopNav > li.active").removeClass("active");
		$(this).parent().addClass("active");
		$(this).blur();
		
		getTop10Page(getQueryParams());
	});
	
	queryDate(getQueryParams());
});