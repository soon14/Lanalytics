var eChartInstant = null;
var eChartTheme = null;
var mapChart = null;
var pieChart1 = null;
var pieChart2 = null;
var lineChart = null;

var preUrl = WEB_ROOT+"/largeScreen/";


require.config({
	paths: { echarts: WEB_ROOT + '/js/echarts' }
});

//地图
function renderMapChart(data){
	var mapColorArr = ['#d80000','#e44d4d','#ec8080','#f2abab','#fcd4d4'];
	var mapTextColor = '#12131b';
	var mapOption =   {
		    tooltip : {
		    	show:true,
		        trigger: 'item'
		    },
		    dataRange: {
		        min: 0,
		        max: 1000,
		        splitNumber:5,
		        orient:'horizontal',
		        x: '70%',
		        y: 165,
		        itemGap:0,
		        text:['高','低'],
		        calculable : false,
		        color:mapColorArr,
		        textStyle:{color:"#fff"}
		    },
		    series : [
		        {
		            name: '访客数（UV）',
		            type: 'map',
		            mapType: 'china',
		            roam: false,
		            itemStyle:{
		            	normal:{areaStyle: {color: mapColorArr[4]},label:{textStyle: {color: mapTextColor,fontSize:8},show:false},borderWidth:1,borderColor:'#fff'},
     	                emphasis:{label:{show:false},borderWidth:1,borderColor:'#fff'}
		            },
		            mapLocation:{
		            	x:5,
		            	y:5
		            },
		            data:[]
		        }
		    ]
		};
	
	mapOption.series[0].data = [];
	var maxUv = 0;
	$(data).each(function(i,d){
		if(maxUv<d.uv) maxUv = d.uv;
		var itemStyle={normal:{areaStyle: {color: mapColorArr[4]},label:{textStyle: {color: mapTextColor,fontSize:8},show:true},borderWidth:1,borderColor:'#fff'}};
		mapOption.series[0].data.push({name: d.provinceName,value: d.uv,itemStyle:itemStyle});
	});
	
	mapOption.dataRange.max = maxUv;
	mapChart.setOption(mapOption);
}

//饼图
function renderPieChart(params){

	pieChart1 = eChartInstant.init(document.getElementById('flowRegionDiv'));//区域分布
	pieChart2 = eChartInstant.init(document.getElementById('listBuyLevelDiv'));//购买力度
	
	//设置数据
	function setPieOption(pieChart,pieDate,colorArr){
		
		var pieOption = {
				legend: {
					//x:"left",
					y:205,
					padding: 3,
			        data:[],
			        selectedMode:false,
			        textStyle:{color:"#fff"}
			    },
				series : [
		           {
			           name:'访问来源',
			           type:'pie',
			           radius:['20%', '45%'],
			           center: ['50%', '28%'],
			           data:[]
		           }
				]
			};
		
		var hasDate = false;
		
		if(pieDate && pieDate.length>0){
			
			pieOption.series[0].data = [];
			pieOption.legend.data=[];
			
			$(pieDate).each(function(i,d){
			    pieOption.legend.data.push({name:d.name});
			});
			
			pieDate.sort(function (a, b) { return a.value-b.value;});
			
			$(pieDate).each(function(i,d){
				if(d.value>0){
					hasDate = true;
				}
				var subLength = colorArr.length-i-1 - (colorArr.length - pieDate.length);
				var itemStyle = {
		     	   normal : {
		     		  color:colorArr[subLength],
		               label : {
		                    textStyle:{
		                    	color:"#fff"//colorArr[colorArr.length-i-1]
		                    },
		                    formatter : function(param){
	                        	var percent = param.percent;
	                        	if(percent == 0){
	                        		return "0%";
	                        	}
	                        	
	                        	return getFixedNum(parseFloat(percent),3)+"%";
	                        }
		               },
		               labelLine : {
		                   show : true,
		                   length : 3,
		                   lineStyle:{
		                	   color:colorArr[subLength],
		                	   width :1
		                   }
		               }
		           }
				};
				
				pieOption.series[0].data.push({value:d.value,name:d.name,itemStyle:itemStyle});
			});
			
			if(hasDate == true){
				pieChart.setOption(pieOption,true);
			}
		}
		
		if(hasDate == false){
			//$(pieChart.dom).html("<div>暂无数据</div>");
		}
	}
	
	//查询数据
	$.doajax({
		url: preUrl + "flowPie",
        data:params,
        success:function(data){
        	//区域分布
        	var flowRegionData = [];
        	if(data.regionList && data.regionList.length>0){
        		$(data.regionList).each(function(i,d){
        			if(d.pv>0)
        			    flowRegionData.push({name:d.chnlType,value:d.pv});
        		});
        		
        		//var colorArr = ['#d80000','#e44d4d','#ec8080','#f2abab','#fcd4d4','#fcd4d4'];
        		var colorArr = ['#63ba63','#337ab7','#f0ad4e','#d9534f','#ecf0f1','#5bc1e0'];
        		setPieOption(pieChart1,flowRegionData,colorArr);
        	}else{
        		//$(pieChart1.dom).html("<div>暂无数据</div>");
        	}
        	
        	
        	//购买力度
        	var listBuyLeveData = [];
        	if(data.listBuyLevelScales && data.listBuyLevelScales.length>0){
        		$(data.listBuyLevelScales).each(function(i,d){
        			if(d.uv>0)
        			    listBuyLeveData.push({name:d.userTypeName,value:d.uv});
        		});
        		
        		//var colorArr = ['#d80000','#e44d4d','#ec8080','#f2abab','#fcd4d4','#fcd4d4'];
        		var colorArr = ['#63ba63','#337ab7','#f0ad4e','#d9534f','#ecf0f1','#5bc1e0'];
        		listBuyLeveData.sort(function (a, b) { return b.value-a.value;});
        		setPieOption(pieChart2,listBuyLeveData,colorArr);
        	}else{
        		//$(pieChart2.dom).html("<div>暂无数据</div>");
        	}
        	
        }
	});
}

//初始化线性图

function renderLineChart(params){
	
	lineChart = eChartInstant.init(document.getElementById('flowLineDiv'));//访客趋势
	
	//查询数据
	$.doajax({
		url: preUrl + "flowLine",
        data:params,
        success:function(data){
        	
        	var lineData = data.flowline;
        	
        	var lineOption =  {
    			grid:{
    				borderWidth:0,
    				x:35,
    				x2:35,
    				y:65
    			},
    		    legend: {
    		    	y:"bottom",
    		        data:[],
    		        selectedMode:false,
    		        textStyle:{color:"#fff"}
    		    },
    		    xAxis : [
    		        {
    		            type : 'category',
    		            boundaryGap : false,
    		            splitLine:{show:false},
    		            data : lineData.xAxis,
    		            axisLabel : {
    		            	//interval:4,
    		            	show:true,
    		                formatter:function(value){
    		                	return formaterxAxis(value);
    		                },
    		                textStyle:{
    		                    color:"#fff"
    		                }
    		            }
    		        }
    		    ],
    		    yAxis : [
    		        {
    		            type : 'value',
    		            axisLabel : {
    		            	show:false,
    		                formatter: '{value}',
    		                textStyle:{
    		                    color:"#fff"
    		                }
    		            },
    		            axisLine:{show:false},
    		            splitLine:{show:false}
    		        }
    		    ],
    		    series : []
    		};

        	//数据
        	var allLineChartData = {
        	    uvs:{
        	    	name:'访客数（UV）',
        	    	data:lineData.uvs
        	    },
        	    pvs:{
        	    	name:'浏览量（PV）',
        	    	data:lineData.pvs
        	    },
        	    bounceRates:{
        	    	name:'跳失率 ',
        	    	data:lineData.bounceRates
        	    },
        	    avgPvs:{
        	    	name:'平均浏览页面 ',
        	    	data:lineData.avgPvs
        	    },
        	    avgStays:{
        	    	name:'平均停留时间 ',
        	    	data:lineData.avgStays
        	    }
        	};
        	
        	function setLineData(lineData,flag){
        		var position = "top";
        		if(flag == 1){
        			position = "bottom";
        		}
        		
        		var newLineDate = [];
        		$(lineData.data).each(function(i,d){
        			if(d == 0){
        				newLineDate.push(d);
        			}else{
        				newLineDate.push(d+150);
        			}
        		});
        		
        		var seriesData = {
        	          name:lineData.name,
        	          type:'line',
        	          data:newLineDate,
        	          symbol:'rectangle',
        	          smooth:true,
        	          itemStyle:{
        	              normal:{
    	            	    label:{
        	        		  show:true,
        	        		  position:position,
        	        		  formatter:function(data){
        	        			return data.value== 0?"":data.value-150;  
        	        		  }
        	        	    },
        	                lineStyle: {
        	                  width: 1
        	                }
        	              }
        	          }
        		};
        		
        		lineOption.legend.data.push(lineData.name);
        		lineOption.series.push(seriesData);
        	}
        	
        	setLineData(allLineChartData.uvs,1);
        	setLineData(allLineChartData.pvs,2);
        	
        	lineChart.setOption(lineOption,true);
        }
	});
}

//查询
function queryDate(params){
	
	ajaxLoadPage(params,preUrl+"flowMap",$("#flowMapDiv"));
	
	renderPieChart(params);
	
	renderLineChart(params);
	
	ajaxLoadPage(params,preUrl+"flowSource",$("#flowSourceDiv"));
}


function getFixedNum(num,n){
	var numStr = num.toFixed(1);
	if(numStr.indexOf(".") > 0){ 
		var test = /0+?$/;
		numStr = numStr.replace(test, "");//去掉多余的0  
		test = /[.]$/;
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
	
	//初始化图表
	require(
		['echarts',
		 'echarts/chart/pie',
		 'echarts/chart/line',
		 'echarts/chart/map'],
		 function (ec,theme){
			
			eChartInstant = ec;
			
			queryDate(getQueryParams());
		 }
	);
	
});