var adIndexLineChart = null;

//初始化线性图
var adIndexAllLineChartData;
function renderAdIndexLineChart(data){	
	//数据
	adIndexAllLineChartData = {
		uvs:{
	    	name:'访客数',
	    	data:data.uvs
	    },
	    pvs:{
	    	name:'访问量',
	    	data:data.pvs
	    },
	    orderUvRates:{
	    	name:'下单率 ',
	    	data:data.orderUvRates
	    },
	    payUvRates:{
	    	name:'支付成功率 ',
	    	data:data.payUvRates
	    }
	};
	
	var xAxis = data.xAxis;
	
	var adIndexLineChartOption =  {
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
	        		xAxisName = formatLinexAxis(param.name,true);
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
	                	return formatLinexAxis(value);
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
	$("input[name='adIndexTrendCkb']").click(function(){
		
		var isChecked = $(this).is(":checked");
		var value = $(this).val();
		var type = $(this).attr("ckbType");
		
		var chkObjes = $("input[name='adIndexTrendCkb']:checked");
		
		if(chkObjes.length>0){
			
			var chkResult = checkedAdIndexLine(isChecked,value);
			if(chkResult == true){
				$(chkObjes).each(function(i,obj){
					var chkValue = $(obj).val();
					var ckbType = $(this).attr("ckbType");
					
					if(ckbType != type){
						$("input[name='adIndexTrendCkb'][value='"+chkValue+"']").attr("checked",false);
						checkedAdIndexLine(false,chkValue);
					}
				});
			}
		}else{
			return false;
		}
		
		return true;
	});
	
	function checkedAdIndexLine(isChecked,value){
		var lineChartData = adIndexAllLineChartData[value];
		
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
		
		var index = $.inArray(lineChartData.name,adIndexLineChartOption.legend.data);
		
		if(isChecked == true){
			if(index == -1){
				adIndexLineChartOption.series.push(seriesData);
				adIndexLineChartOption.legend.data.push(lineChartData.name);
				adIndexLineChart.setOption(adIndexLineChartOption,false);
			}
		}else{
			if(index != -1){
				adIndexLineChartOption.series.splice(index,1);
				adIndexLineChartOption.legend.data.splice(index,1);
				adIndexLineChart.setOption(adIndexLineChartOption,true);
			}
		}
		
		return true;
	}
	
	checkedAdIndexLine(true,"uvs");
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

function formatLinexAxis(value,flag){
	
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