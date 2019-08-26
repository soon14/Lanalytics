var promIndexTrendChart = null;
//初始化线性图
var allLineChartData;
//对比数据
var dateCompData;
//
var dateCompXaxis;

var lineOption =  {
	grid:{
		borderWidth:0,
		x:25,
		x2:25,
		y:10
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
        		if(dateCompData && param.seriesName.indexOf('（对比）') >0){
					var index = $.inArray(param.name,lineOption.xAxis[0].data);
					xAxisName = formaterxAxis(dateCompXaxis[index],true);
				}
        		
        		var value = getFixedNum(param.value,2);
        		tip += param.seriesName.replace("（对比）","")+"（"+xAxisName+"）："+value+"</br>"
        	});
        	
        	return tip;
        }
    },
    xAxis : [
        {
            type : 'category',
            boundaryGap : false,
            splitLine:{show:false},
            //data : xAxis,
            axisLabel : {
            	//interval:0,
                formatter:function(value){
                	return formaterxAxis(value);
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

//渲染趋势图
function renderPromIndexTrendChart(data){	
	//数据
	allLineChartData = {
		uvs:{
	    	name:'商品访客数',
	    	data:data.uvs
	    },
	    pvs:{
	    	name:'商品浏览量',
	    	data:data.pvs
	    },
	    exitRates:{
	    	name:'详情页跳失率 ',
	    	data:data.exitRates
	    },
	    orderUvRates:{
	    	name:'下单转化率 ',
	    	data:data.orderUvRates
	    },
	    orderCountAvgs:{
	    	name:'平均下单件数',
	    	data:data.orderCountAvgs
	    },
	    payCounts:{
	    	name:'交易量 ',
	    	data:data.payCounts
	    },
	    payMoneys:{
	    	name:'交易额',
	    	data:data.payMoneys
	    }
	};
	
	lineOption.xAxis[0].data = data.xAxis;
	lineOption.legend.data = [];
	lineOption.series = [];
	checkedLine(true,"uvs");
	
	//事件
	$("input[name='promIndexTrendCkb']").click(function(){
		
		var isChecked = $(this).is(":checked");
		var value = $(this).val();
		var type = $(this).attr("ckbType");
		var chkObjes = $("input[name='promIndexTrendCkb']:checked");
		
		if(chkObjes.length>0){
			var chkResult = checkedLine(isChecked,value);
			if(chkResult == true){
				$(chkObjes).each(function(i,obj){
					var chkValue = $(obj).val();
					var ckbType = $(this).attr("ckbType");
					
					if(ckbType != type){
						$("input[name='promIndexTrendCkb'][value='"+chkValue+"']").attr("checked",false);
						checkedLine(false,chkValue);
					}
				});
			}
		}else{
			return false;
		}
		
		return true;
	});
}

//渲染时间对比
function renderDateCompData(data){
	//数据
	dateCompData = {
		uvs:{
	    	name:'商品访客数（对比）',
	    	data:data.uvs
	    },
	    pvs:{
	    	name:'商品浏览量（对比）',
	    	data:data.pvs
	    },
	    exitRates:{
	    	name:'详情页跳失率（对比） ',
	    	data:data.exitRates
	    },
	    orderUvRates:{
	    	name:'下单转化率（对比） ',
	    	data:data.orderUvRates
	    },
	    orderCountAvgs:{
	    	name:'平均下单件数（对比）',
	    	data:data.orderCountAvgs
	    },
	    payCounts:{
	    	name:'交易量（对比） ',
	    	data:data.payCounts
	    },
	    payMoneys:{
	    	name:'交易额（对比）',
	    	data:data.payMoneys
	    }
	};
	
	dateCompXaxis = data.xAxis;
	
	resetDateCompOption();
}

function resetDateCompOption(){
	for(var index=0;index<lineOption.legend.data.length; index++){
		if(lineOption.legend.data[index].indexOf('（对比）') !== -1){
			lineOption.series.splice(index,1);
		    lineOption.legend.data.splice(index,1);
		    promIndexTrendChart.setOption(lineOption,true);
		    
		    index--;
		}
	}
	
	var chkObjes = $("input[name='promIndexTrendCkb']:checked");
	$(chkObjes).each(function(i,obj){
		var chkValue = $(obj).val();
		
		var compSeriesData;
		if(dateCompData){
			var compLineChartData = dateCompData[chkValue];
			compSeriesData = {
		      name:compLineChartData.name,
		      type:'line',
		      data:compLineChartData.data,
		      symbol:'circle',
		      smooth:true
		    };
			
			lineOption.series.push(compSeriesData);
			lineOption.legend.data.push(compLineChartData.name);
			promIndexTrendChart.setOption(lineOption,true);
		}
	});
}

function checkedLine(isChecked,value){
	var lineChartData = allLineChartData[value];
	
	if(lineChartData==undefined){
		return false;
	}
	
	var seriesData = {
      name:lineChartData.name,
      type:'line',
      data:lineChartData.data,
      symbol:'circle',
      smooth:true
    };
	
	var compSeriesData
	if(dateCompData){
		compLineChartData = dateCompData[value];
		compSeriesData = {
	      name:compLineChartData.name,
	      type:'line',
	      data:compLineChartData.data,
	      symbol:'circle',
	      smooth:true
	    };
	}
	
	var index = $.inArray(lineChartData.name,lineOption.legend.data);
	if(isChecked == true){
		if(index == -1){
			lineOption.series.push(seriesData);
			lineOption.legend.data.push(lineChartData.name);
			
			if(dateCompData){
				lineOption.series.push(compSeriesData);
				lineOption.legend.data.push(compLineChartData.name);
			}
			
			promIndexTrendChart.setOption(lineOption,false);
		}
	}else{
		if(index != -1){
		    lineOption.series.splice(index,1);
		    lineOption.legend.data.splice(index,1);
		    promIndexTrendChart.setOption(lineOption,true);
		    
		    if(dateCompData){
		    	var compIndex =  $.inArray(compLineChartData.name,lineOption.legend.data);
		    	lineOption.series.splice(compIndex,1);
			    lineOption.legend.data.splice(compIndex,1);
			    promIndexTrendChart.setOption(lineOption,true);
		    }
		}
	}
	
	return true;
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
				if(dateCompData){
					var index = $.inArray(value,lineOption.xAxis[0].data);
					return "第"+(index+1)+"日";
					
				}else{
					return str.substring(index+1)+"日";
				}
			}
		}else{
			return value+"时";
		}
		
	}else{
		return value;
	}
}