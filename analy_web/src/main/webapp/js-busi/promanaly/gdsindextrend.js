var gdsLineChart = null;

//初始化线性图
var gdsAllLineChartData;
function renderGdsLineChart(data){	
	//数据
	gdsAllLineChartData = {
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
	
	var xAxis = data.xAxis;
	
	var gdsLineOption =  {
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
	        		xAxisName = formatGdsLinexAxis(param.name,true);
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
	                	return formatGdsLinexAxis(value);
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
	$("input[name='gdsIndexTrendCkb']").click(function(){
		
		var isChecked = $(this).is(":checked");
		var value = $(this).val();
		var type = $(this).attr("ckbType");
		
		var chkObjes = $("input[name='gdsIndexTrendCkb']:checked");
		
		if(chkObjes.length>0){
			
			var chkResult = checkedGdsLine(isChecked,value);
			if(chkResult == true){
				$(chkObjes).each(function(i,obj){
					var chkValue = $(obj).val();
					var ckbType = $(this).attr("ckbType");
					
					if(ckbType != type){
						$("input[name='gdsIndexTrendCkb'][value='"+chkValue+"']").attr("checked",false);
						checkedGdsLine(false,chkValue);
					}
				});
			}
		}else{
			return false;
		}
		
		return true;
	});
	
	function checkedGdsLine(isChecked,value){
		var gdsLineChartData = gdsAllLineChartData[value];
		
		if(gdsLineChartData==undefined){
			return false;
		}
		
		var seriesData = {
          name:gdsLineChartData.name,
          type:'line',
          data:gdsLineChartData.data,
          symbol:'circle',
          smooth:true
	    };
		
		var index = $.inArray(gdsLineChartData.name,gdsLineOption.legend.data);
		
		if(isChecked == true){
			if(index == -1){
				gdsLineOption.series.push(seriesData);
				gdsLineOption.legend.data.push(gdsLineChartData.name);
				gdsLineChart.setOption(gdsLineOption,false);
			}
		}else{
			if(index != -1){
			    gdsLineOption.series.splice(index,1);
			    gdsLineOption.legend.data.splice(index,1);
			    gdsLineChart.setOption(gdsLineOption,true);
			}
		}
		
		return true;
	}
	
	checkedGdsLine(true,"uvs");
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

function formatGdsLinexAxis(value,flag){
	
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