var preUrl = WEB_ROOT+"/goodsAnaly/";
//继承查询方法
function queryDate(params,pageNo){
	
	if(pageNo == undefined){
		pageNo = 1;
	}
	
	params.categoryType = $("#categoryTypeId > li.active").attr("categoryType");
	params.pageNo = pageNo
	params.pageSize = 10;
	params.categoryLevel = 1;
	
	
	var queryCallBack = function(data){
		/*
		$("#goodsClassifyPagerId").pager({callback:function(pageNo){
			queryDate(params,pageNo);
		}});
		*/
	}
	
	ajaxLoadPage(params,preUrl + "getGoodsClassifyeDate",$("#goodsClassifyTableId"),queryCallBack);
}

function queryGdsBrand(catId, brandId){
	var params = getQueryParams();
	params.categoryType = "2";
	params.catId = catId;
	params.categoryId = brandId;
	var queryCallBack = function(data){
	}
	ajaxLoadPage(params,preUrl + "getGoodsClassifyeDate",$("#goodsClassifyTableId"),queryCallBack);
	
	setCurrId(catId, brandId);
	$("#catgLevel").val("");
}

function setCurrId(currCatId, currBrandId){
	$("#currCatId").val(currCatId);
	$("#currBrandId").val(currBrandId);
}

$(document).ready(function(){
	$("#categoryTypeId > li > a").click(function(){
		$("#categoryTypeId > li.active").removeClass("active");
		$(this).parent().addClass("active");
		$(this).blur();
		
		queryDate(getQueryParams(),1);
		
		$("#catgLevel").val("1");
	});
	
	queryDate(getQueryParams(),1);
	
	$('#exportClassifyBtn').click(function(){
    	var params = getQueryParams();
    	var categoryType = $("#categoryTypeId > li.active").attr("categoryType");
    	var catId = $("#currCatId").val();
    	var brandId = $("#currBrandId").val();
    	var cateLevel = $("#catgLevel").val();
    	window.location = preUrl+'exportClassifyData.xls?dateFrom='+params.dateFrom+'&dateTo='+params.dateTo+"&source="+params.source
    		+"&categoryType="+categoryType+"&catId="+catId+"&categoryId="+brandId+"&categoryLevel="+cateLevel;
    });
});

