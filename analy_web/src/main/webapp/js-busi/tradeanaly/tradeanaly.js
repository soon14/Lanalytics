var basePath = WEB_ROOT;

var eChartInstant = null;
var eChartTheme = null;

var lineChart = null;
var tradeMapChart = null;
var ranTrendChart = null;

var preUrl = WEB_ROOT + '/tradeAnaly/';

//对组件的引用
require.config({
	paths: { echarts: WEB_ROOT + '/js/echarts' }
});

//初始化线性图
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
	        		tip += "</br>" + param.seriesName+"："+value;
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
	
	$("input[name='tradeMapData']").click(function() {
		var value = $(this).val();
		if (value == "tradeCount") {
			$("#tradeAmount").show();
			$("#tradeMoney").hide();
			changeMap(value);
		} else if (value == "tradeAmount") {
			$("#tradeAmount").hide();
			$("#tradeMoney").show();
			changeMap(value);
		}
	});
	
	function changeMap(value) {
		var dataRangeText = "";
		var seriesName = "";
		var dataRangeMax = 0;
		
		if (value == "tradeCount") {
			dataRangeText = ["交易量	 高", "低"];
			seriesName = "交易量";
		} else if (value == "tradeAmount") {
			dataRangeText = ["交易额（元）	 高", "低"];
			seriesName = "交易额";
		}
		
		mapOption.series[0].data = [];
		$(data).each(function(i, d){
			if(dataRangeMax < d[value]) {
				dataRangeMax = d[value];
			}
			var dValue = d[value];
			if(value == "tradeAmount"){
				dValue = getFixedNum(dValue/100,2);
			}
			mapOption.series[0].data.push({name: d.provinceName, value: dValue});
			mapOption.series[0].name = seriesName;
		});
		
		if(value == "tradeAmount"){
			dataRangeMax = dataRangeMax/100;
		}
		
		mapOption.dataRange.max = dataRangeMax;
		mapOption.dataRange.text = dataRangeText;
		mapChart.setOption(mapOption);
	}
	
	changeMap("tradeCount");
}


function initRanTrend(data) {
	
	var legendData = [];
	var seriesData = [];
	var xAxisData = [];
	$(data).each(function(i,linedata){
		var legendName = "";
		var series = [];
		xAxisData = [];
		$(linedata).each(function(i,d){
			legendName = d.catName;
			series.push(d.amountRank*-1);
			xAxisData.push(d.date);
		});
		
		var serie = {
	            name:legendName,
	            type:'line',
	            symbol:'circle',
		        smooth:true,
	            stack: legendName,
	            data:series
	        };
		
		legendData.push(legendName);
		seriesData.push(serie);
	});
	
	ranTrendOption = {
			grid:{
				borderWidth:0,
				x:95,
				x2:35,
				y:35,
				y2:25
			},
		    legend: {
		    	y:"top",
		    	data:legendData,
		        selectedMode:false
		    },
		    tooltip : {
		        trigger: 'axis',
		        formatter:function(params){
		        	var tip = "";
		        	var xAxisName = "";
		        	var length = params.length;
		        	if(length>0){
		        		for (var int = length; int > 0; int--) {
		        			var param = params[int-1];
		        			xAxisName = param.name;
			        		var value = getFixedNum(param.value,2);
			        		if(value == 0){
			        			value = "-";
			        		}else{
			        			value = value*-1;
			        		}
			        		tip += "</br>" + param.seriesName+"排名："+value;
						}
		        	}
		        	
		        	return xAxisName + tip;
		        }
		    },

		    xAxis : [{
				type : 'category',
				boundaryGap : false,
				show:false,
				splitLine : {
					show : false
				},
				data : xAxisData,
				axisLabel : {
					interval : 0,
					formatter : function(value) {
						return value;
					}
				}
			}],
		    yAxis : [{
				type : 'value',
				axisLabel : {
					formatter :function(value){return value*-1;}
				},
				axisLine : {
					show : false
				},
			}],
		    series : seriesData
		};
		
	    ranTrendChart.setOption(ranTrendOption);
}

/** 加载数据 */
function queryDate(params){
	
	var baseUrl = basePath + "/tradeAnaly";
	
	var $tradeTrend = $("#tradeTrendDiv");
	var $tradeMap = $("#tradeMapDiv");
	
	ajaxLoadPage(params, baseUrl + "/tradeTrend", $tradeTrend);
	ajaxLoadPage(params, baseUrl + "/tradeMap", $tradeMap);
	
	loadtradeOverviewPage(provinceCode,"");
	
	//加载省份地市
    var provinceCode = $("#provice").attr("provinceCode");
    if(provinceCode == ""){
    	//$("#city").show();
    	$("#provice").show();
    	
    	if($("#provice").children().length == 0){
    	    provinceCode = "156";
    	    loadAreaInfo(provinceCode,"provice");
    	}else{
    		$("#provice option:first").attr("selected",true);
    	}
    	
    	provinceCode = "";
    	//$("#city").html("<option value=\"\">全部</option>");
    }else{
    	//$("#city").show();
    	//loadAreaInfo(provinceCode,"city");
    }
    
    loadRankCat();
}

function loadtradeOverviewPage(provinceCode,cityCode){
    var baseUrl = basePath + "/tradeAnaly";
	var $tradeOverview = $("#tradeOverviewDiv");
	var params = getQueryParams();
	params.provinceCode = provinceCode;
	params.cityCode = cityCode;
	ajaxLoadPage(params, baseUrl + "/tradeOverview", $tradeOverview);
}

//查询top 10 数据
function queryTop10Date(params,pageNo){
	var option = $("#goodsTopNav > li.active").attr("option");
	params.option = option;
	var preUrl = WEB_ROOT+"/goodsAnaly/";
	
	if(pageNo == undefined){
		pageNo = 1;
	}
	
	params.pageNo = pageNo;
	params.pageSize = 10;
	
	var queryCallBack = function(data){
		$("#goodsTop10PagerId").pager({callback:function(pageNo){
			queryTop10Date(params,pageNo);
		}});
	};
	
	ajaxLoadPage(params,preUrl + "getGoodsTopDate",$("#goodsTop10"),queryCallBack);
}

function getFixedNum(num,n){
	var numStr = num.toFixed(n);
	if(numStr.indexOf(".") > 0){ 
		var test = /0+?$/;
		numStr = numStr.replace(test, "");//去掉多余的0  
		test = /[.]$/;
		numStr = numStr.replace(test, "");//如最后一位是.则去掉    
	}
    return numStr;
}

function formaterxAxis(value){
	
	if(value){
		var str = value+"";
		var index = str.lastIndexOf("-");
		if(index > 0){
			return str.substring(index+1)+"日";
		}else{
			return value+"时";
		}
		
	}else{
		return value;
	}
}

function loadAreaInfo(parentAreaCode,id){
	
	$.doajax({
		url: WEB_ROOT+"/tradeAnaly/getAreaInfo",
        data:{parentAreaCode:parentAreaCode},
        //async:false,
        success: function (data){
        	
        	var optionHtml = "<option value=\"\">全部</option>";
        	if("provice" == id){
        		optionHtml = "<option value=\"\">全国</option>";
        	}
        	if(data.areaList && data.areaList.length>0){
        		
        		$(data.areaList).each(function(i,d){
        			optionHtml += "<option value=\""+d.areaCode+"\">"+ d.areaName +"</option>";
        		});
        		
        		$("#"+id).html(optionHtml);
        		
        		//添加事件
    			$("#"+id).unbind("change").bind("change",function(){
    				var value = $(this).children('option:selected').val();
    				
    				if("provice" == id){
    					if(value != ""){
    						loadAreaInfo(value,"city");
    					}
    					loadtradeOverviewPage(value,"");
    					$("#city").html("<option value=\"\">全部</option>");
    					$(this).blur();
    				}else{
    					var provinceCode = $("#provice").attr("provinceCode");
    				    if(provinceCode == "9A"){
    				    	provinceCode = $("#provice").children('option:selected').val();;
    				    }
    				    $(this).blur();
    					loadtradeOverviewPage(provinceCode,value);
    				}
    			});
            	
        	}else{
        		$("#"+id).html("");
        	}
        }
    });
}

function loadRankCat(){
	var params = getQueryParams();
	var catId = $("#tradeRankCats > li.active").attr("option");
	params.catId = catId;
	
	var baseUrl = basePath + "/tradeAnaly";
	var $tradeRank = $("#tradeRankDiv");
	ajaxLoadPage(params, baseUrl + "/tradeRank", $tradeRank);
}

$(function () {
	//初始化图标
	require(['echarts',
	         'echarts/chart/line',
	         'echarts/chart/map'],
		function (ec,theme){
			eChartInstant = ec;
			eChartTheme = theme;
			queryDate(getQueryParams());
		}
	);
	
	$("#tradeRankCats li  a").click(function(){
		$("#tradeRankCats > li.active").removeClass("active");
		$(this).parent().addClass("active");
		$(this).blur();
		//loadRankCat();
	});
});
