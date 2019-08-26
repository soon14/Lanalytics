var basePath = WEB_ROOT;

var eChartInstant = null;
var eChartTheme = null;

var chnlRegDifferChart = null;
var tChnlPieChart = null;
var tChnlBarChart = null;
var dlNewChnlTypeChart = null;
var chainChnlChart = null;

var preUrl = WEB_ROOT + '/tradeAnaly/';

//对组件的引用
require.config({
	paths: { echarts: WEB_ROOT + '/js/echarts' }
});

function chnlRegDifferBar(barData) {
	
	if(barData.length == 0){
	    return;	
	}
	
	var chnlRegDifferBarData = barData;
	
	//横坐标值
	var xAxisData = [];
	$(chnlRegDifferBarData).each(function(i,d){
		xAxisData.push(d.provinceName);
	});
	
	var chnlRegDifferOption = {
		color:['#ff7f50','#87cefa','#da70d6','#6495ed','#ff69b4','#ba55d3','#cd5c5c','#ffa500','#40e0d0'],
		grid:{
			borderWidth:0,
			x:85,
			x2:35,
			y:10
		},
	    tooltip : {
	        trigger: 'item',
	        show:true
	    },
	    legend: {
	    	y:"bottom",
	        data:[],
	        selectedMode:false
	    },
	    xAxis : [
	        { 
	            type : 'category',
	            splitLine : {
					show : false
				},
				axisTick:{show:false},
				axisLabel:{
					rotate:45, //刻度旋转45度角
				},
	            data : xAxisData
	        }
	    ],
	    yAxis : [
	        {
	            type : 'value',
	            axisLine : {
					show : false
				}
	        }
	    ],
	    series : []
	};
	
	function setChnlRegDifferBarData(isChecked,value){
		
		var barName = $("input[name='chnlRegDifferCharCkb'][value='"+value+"']").parent().text();
		barName = $.trim(barName);
		
		var index = $.inArray(barName,chnlRegDifferOption.legend.data);
		
        if(isChecked == true){
			
			if(index == -1){
			
				var seriesData = [];
				$(chnlRegDifferBarData).each(function(i,d){
					var svalue = d[value];
					if(value == "tradeAmount" || value == "payPrice"){
						svalue = getFixedNum(parseFloat(svalue)/100,2);
					}
					if(value == "payUvRate"){
						svalue = getFixedNum(svalue,2);
					}
					
					seriesData.push(parseFloat(svalue));
				});
				
				var serie = {
			            name:barName,
			            type:'bar',
			            smooth:true,
			            barWidth:20,
			            data:seriesData,
			            itemStyle : { normal: {label : {show: false, position: 'top'}}}
			    };
				
				chnlRegDifferOption.legend.data.push(barName);
				chnlRegDifferOption.series.push(serie);
				chnlRegDifferChart.setOption(chnlRegDifferOption);
			}
		}else{
			if(index != -1){
				chnlRegDifferOption.series.splice(index,1);
				chnlRegDifferOption.legend.data.splice(index,1);
				chnlRegDifferChart.setOption(chnlRegDifferOption,true);
			}
		}
		
		return true;
	}
	
	$("input[name='chnlRegDifferCharCkb']").click(function(){
		
		var isChecked = $(this).is(":checked");
		var value = $(this).val();
		var type = $(this).attr("ckbType");
		
        var chkObjes = $("input[name='chnlRegDifferCharCkb']:checked");
		
		if(chkObjes.length>0){
			
			var chkResult = setChnlRegDifferBarData(isChecked,value);
			
			if(chkResult == true){
				$(chkObjes).each(function(i,obj){
					var chkValue = $(obj).val();
					var ckbType = $(this).attr("ckbType");
					
					if(ckbType != type){
						$("input[name='chnlRegDifferCharCkb'][value='"+chkValue+"']").attr("checked",false);
						setChnlRegDifferBarData(false,chkValue);
					}
				});
			}
		}else{
			return false;
		}
		
		return true;
	});
	
	setChnlRegDifferBarData(true,"uv");
}

function tChnlPie(data) {
	
	if(data.length == 0){
	    return;	
	}
	
	var chnlTypeData = data;
	
	var tChnlPieOption = {
		color:['#ff7f50','#87cefa','#da70d6','#6495ed','#ff69b4','#ba55d3','#cd5c5c','#ffa500','#40e0d0'],
	    tooltip: {
	    	trigger: 'item',
	        formatter: "{a} <br/>{b} : {c}"
	    },
	    legend: {
	    	y: "center",
	    	x: 'right' ,
	    	orient:'vertical',
	        data:[],
	        selectedMode:false
	    },
	    series: [
	        {
	            name:'',
	            type:'pie',
	            radius : '68%',
	            center: ['45%', '48%'],
	            data:[],
	            itemStyle:{
	         	   normal : {
	                    label : {
	                         show : true,
	                         formatter : function(param){
	                         	var percent = param.percent;
	                         	if(percent == 0){
	                         		return "0%";
	                         	}
	                         	return getFixedNum(parseFloat(percent),1)+"%";
	                         }
	                    },
	                    labelLine : {
	                        show : true,
	                        length : 5
	                    }
	                }
	            },
	        }
	    ]
	};
	
	var xAxisData = [];
	$(chnlTypeData).each(function(i,d){
		xAxisData.push(d.chnlType);
	});
	
	var tChnlBarOption = {
			grid:{
				borderWidth:0,
				x:65,
				x2:35,
				y:10
			},
		    tooltip : {
		        trigger: 'item',
		        show:true
		    },
		    legend: {
		    	y:"bottom",
		        data:[],
		        selectedMode:false
		    },
		    xAxis : [
		        { 
		            type : 'category',
		            splitLine : {
						show : false
					},
					axisTick:{show:false},
					axisLabel:{
						//rotate:45, //刻度旋转45度角
					},
		            data : xAxisData
		        }
		    ],
		    yAxis : [
		        {
		            type : 'value',
		            axisLine : {
						show : false
					}
		        }
		    ],
		    series : []
		};
	
	function setChnlTypeOption(isChecked,value){
		
		var barName = $("input[name='chnlTypeCharCkb'][value='"+value+"']").parent().text();
		barName = $.trim(barName);
		
    	var index = $.inArray(barName,tChnlBarOption.legend.data);
		
        if(isChecked == true){
			
			if(index == -1){
				var seriesData = [];
				$(chnlTypeData).each(function(i,d){
					var svalue = d[value];
					if(value == "tradeAmount" || value == "payPrice"){
						svalue = getFixedNum(parseFloat(svalue)/100,2);
					}
					if(value == "payUvRate"){
						svalue = getFixedNum(svalue,2);
					}
					
					seriesData.push(parseFloat(svalue));
				});
				
				var serie = {
			            name:barName,
			            type:'bar',
			            smooth:true,
			            barWidth:20,
			            data:seriesData,
			            itemStyle : { normal: {label : {show: false, position: 'top'}}}
			    };
				
				tChnlBarOption.legend.data.push(barName);
				tChnlBarOption.series.push(serie);
				tChnlBarChart.setOption(tChnlBarOption,true);
			}
		}else{
			if(index != -1){
				tChnlBarOption.series.splice(index,1);
				tChnlBarOption.legend.data.splice(index,1);
				tChnlBarChart.setOption(tChnlBarOption,true);
			}
		}
        
		return true;
	}
	
	function setChnlPieOption(value){
		
		tChnlPieOption.legend.data = [];
		tChnlPieOption.series[0].data = [];
		
		$(chnlTypeData).each(function(i,d){
			var svalue = d[value];
			if(value == "tradeAmount" || value == "payPrice"){
				svalue = getFixedNum(parseFloat(svalue)/100,2);
			}
			
			tChnlPieOption.legend.data.push(d.chnlType);
			tChnlPieOption.series[0].data.push({value:parseFloat(svalue),name:d.chnlType});
		});
		
		tChnlPieOption.series[0].name = $.trim($("input[name='chnlTypeCharCkb1'][value='"+value+"']").parent().text());
		
		tChnlPieChart.setOption(tChnlPieOption);
	}
	
    $("input[name='chnlTypeCharCkb1']").click(function(){
		
		var value = $(this).val();
		var type = $(this).attr("ckbType");
		
        var chkObjes = $("input[name='chnlTypeCharCkb1']:checked");
		
		if(chkObjes.length>0){
			
			$(chkObjes).each(function(i,obj){
				var chkValue = $(obj).val();
				var ckbType = $(this).attr("ckbType");
				
				if(ckbType != type){
					$("input[name='chnlTypeCharCkb1'][value='"+chkValue+"']").attr("checked",false);
				}
			});
			
		}else{
			return false;
		}
		
		setChnlPieOption(value);
		
		return true;
	});
	
    $("input[name='chnlTypeCharCkb']").click(function(){
		
		var isChecked = $(this).is(":checked");
		var value = $(this).val();
		var type = $(this).attr("ckbType");
		
        var chkObjes = $("input[name='chnlTypeCharCkb']:checked");
		
		if(chkObjes.length>0){
			
			var chkResult = setChnlTypeOption(isChecked,value);
			
			if(chkResult == true){
				$(chkObjes).each(function(i,obj){
					var chkValue = $(obj).val();
					var ckbType = $(this).attr("ckbType");
					
					if(ckbType != type){
						$("input[name='chnlTypeCharCkb'][value='"+chkValue+"']").attr("checked",false);
						setChnlTypeOption(false,chkValue);
					}
				});
			}
		}else{
			return false;
		}
		
		return true;
	});
	
    setChnlPieOption("uv");
    setChnlTypeOption(true,"payUvRate");
}

function setChnlTypeBar(id,data,chart) {

	var xAxisData = [];
	$(data).each(function(i,d){
		xAxisData.push(d.chnlType);
	});
	
	var chnlTypeOption = {
			color: [ "#3E98C5", "#f7a35c","#8085e9"],
			grid:{
				borderWidth:0,
				x:85,
				x2:35,
				y:30
			},
		    tooltip : {
		        trigger: 'item'
		    },
		    legend: {
		    	y:"bottom",
		        selectedMode:false,
		        data:xAxisData
		    },
		    xAxis : [
		        {
		            splitLine : {
						show : false
					},
		            type : 'category',
		            data : []
		        }
		    ],
		    yAxis : [
		        {
		        	 axisLine : {
							show : false
					 },
		            type : 'value'
		        }
		    ],
		    series : []
		};
	
	function setBarData(isChecked,value){
		var barName = $("input[name='"+id+"'][value='"+value+"']").parent().text();
		barName = $.trim(barName);
		
		var index = $.inArray(barName,chnlTypeOption.xAxis[0].data);
		
        if(isChecked == true){
			
			if(index == -1){
			
				chnlTypeOption.series = [];
				
				$(data).each(function(i,d){
					var svalue = d[value];
					if(value == "tradeAmount" || value == "payPrice"){
						svalue = getFixedNum(parseFloat(svalue)/100,2);
					}
					if(value == "payUvRate"){
						svalue = getFixedNum(svalue,2);
					}
					
					var seriesData = [];
					seriesData.push(parseFloat(svalue));
					var serie = {
				            name:d.chnlType,
				            type:'bar',
				            smooth:true,
				            barWidth:80,
				            data:seriesData,
				            itemStyle : { normal: {label : {show: false, position: 'top'}}}
				    };
					chnlTypeOption.series.push(serie);
				});
				
				chnlTypeOption.xAxis[0].data.push(barName);
				chart.setOption(chnlTypeOption);
			}
		}else{
			if(index != -1){
				
				//chnlTypeOption.series = [];
				chnlTypeOption.xAxis[0].data.splice(index,1);
				chart.setOption(chnlTypeOption,true);
			}
		}
		
		return true;
	}
	
    $("input[name='"+id+"']").click(function(){
		
		var isChecked = $(this).is(":checked");
		var value = $(this).val();
		var type = $(this).attr("ckbType");
		
        var chkObjes = $("input[name='"+id+"']:checked");
		
		if(chkObjes.length>0){
			
			var chkResult = setBarData(isChecked,value);
			
			if(chkResult == true){
				$(chkObjes).each(function(i,obj){
					var chkValue = $(obj).val();
					var ckbType = $(this).attr("ckbType");
					
					if(ckbType != type){
						$("input[name='"+id+"'][value='"+chkValue+"']").attr("checked",false);
						setBarData(false,chkValue);
					}
				});
			}
		}else{
			return false;
		}
		
		return true;
	});

    setBarData(true,"uv");
}

/** 加载数据 */
function queryDate(params){
	
	var baseUrl = basePath + "/tradeAnaly";
	
	var $chnlRegDiffer = $("#chnlRegDifferDiv");
	var $chnlTypeDiffer = $("#chnlTypeDifferDiv");
	
	ajaxLoadPage(params, baseUrl + "/chnlRegDiffer", $chnlRegDiffer);
	ajaxLoadPage(params, baseUrl + "/chnlTypeDiffer", $chnlTypeDiffer);
	loadChnlBehavior();
	
	loadGdsRelation(params,1);
}

function loadGdsRelation(params,pageNo){
	
	var baseUrl = basePath + "/tradeAnaly";
	var $gdsRelation = $("#gdsRelationDiv");
	params.pageNo = pageNo;
	params.pageSize = 5;
	
	var queryCallBack = function(data){
		$("#goodsRelationPagerId").pager({callback:function(pageNo){
			loadGdsRelation(params,pageNo);
		}});
	};
	
	ajaxLoadPage(params, baseUrl + "/gdsRelation", $gdsRelation,queryCallBack);
}

function loadChnlBehavior(){
	
	var params = getQueryParams();
	var baseUrl = basePath + "/tradeAnaly";
	
	var month = $("#tradeMonths > li.active").attr("option");
	params.month = month;
	
	var $chnlBehavior = $("#chnlBehaviorDiv");
	ajaxLoadPage(params, baseUrl + "/chnlBehavior", $chnlBehavior);
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

$(function () {
	require(['echarts',
	         'echarts/chart/bar',
	         'echarts/chart/pie'],
		function (ec,theme){
			eChartInstant = ec;
			eChartTheme = theme;
			queryDate(getQueryParams());
		}
	);
	
	$("#tradeMonths li  a").click(function(){
		$("#tradeMonths > li.active").removeClass("active");
		$(this).parent().addClass("active");
		$(this).blur();
		loadChnlBehavior();
	});
});

//导出客户行为分布数据
function exportChnlBehavior(type){
	var tradeMonth = $("#tradeMonths li[class='active']").attr("option");
	window.location = preUrl+'exportChnlBehaviorData.xls?month='+tradeMonth+'&type='+type;
}
//导出主营产品关联数据
function exportgdsRelation(){
	window.location = preUrl+'exportgdsRelationData.xls';
}
