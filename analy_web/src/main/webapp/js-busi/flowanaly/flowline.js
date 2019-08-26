var lineChart = null;

//初始化线性图
var allLineChartData;
function renderLineChart(data){	
	//数据
	allLineChartData = {
	    uvs:{
	    	name:'访客数（UV）',
	    	data:data.uvs
	    },
	    pvs:{
	    	name:'浏览量（PV）',
	    	data:data.pvs
	    },
	    bounceRates:{
	    	name:'跳失率 ',
	    	data:data.bounceRates
	    },
	    avgPvs:{
	    	name:'平均浏览页面 ',
	    	data:data.avgPvs
	    },
	    avgStays:{
	    	name:'平均停留时间 ',
	    	data:data.avgStays
	    }
	};
	
	var xAxis = data.xAxis;
	
	var lineOption =  {
		grid:{
			borderWidth:0,
			x:55,
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
		
		var index = $.inArray(lineChartData.name,lineOption.legend.data);
		
		if(isChecked == true){
			if(index == -1){
				lineOption.series.push(seriesData);
				lineOption.legend.data.push(lineChartData.name);
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
	
	checkedLine(true,"uvs");
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