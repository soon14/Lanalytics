var preUrl = WEB_ROOT+"/goodsAnaly/";
//继承查询方法
function queryDate(params,pageNo){
	
	if(pageNo == undefined){
		pageNo = 1;
	}
	
	params.gdsName = $.trim($("#gdsName").val());
	params.skuIsbn = $.trim($("#skuIsbn").val());
	params.catId = $.trim($("#catId").val());
	params.pageNo = pageNo
	params.pageSize = 10;
	if(params.option == undefined){
		params.option = "uv";
	}
	if(params.sort == undefined){
		params.sort = "desc";
	}
	
	var queryCallBack = function(data){
		goodsDetailSort(params.option,params.sort);
		$("#goodsDetailPagerId").pager({callback:function(pageNo){
			queryDate(params,pageNo);
		}});
		
		$("img.imgBgSmall").error(function(){
			$(this).attr("src",WEB_ROOT + "/images/blank.png");
		});
	}
	
	ajaxLoadPage(params,preUrl + "getGoodsDetailDate",$("#goodsDetailTableId"),queryCallBack);
}

//启用排序
function goodsDetailSort(name,sort){
	var index = $("#goodsDetailTableId table thead th").index($("#goodsDetailTableId table thead").find("th[col_name='"+name+"']"));
	$("#goodsDetailTableId table").tablesorter({
		sortList : [[index,sort]],
		selectorHeaders:"> thead th:gt(2)",
		serverSideSorting:true,
		sortMultiSortKey : '', 
		sortResetKey     : '',
		//headerTemplate : '{content}{icon}'
	}).unbind("sortBegin").bind("sortBegin",function(e, table) {
		/*后台排序  serverSideSorting:true,*/
		var sortList = $(table).data("tablesorter").sortList;
		if(sortList.length == 1){
			var cell = $(this).find("thead th:eq("+sortList[0][0]+")");
			var colName = cell.attr("col_name");
			var sort = sortList[0][1] ===1?"desc":"asc";
			
			var params = getQueryParams();
			params.option = colName;
			params.sort = sort;
			queryDate(params);
		}
	});		
}
//查询按钮点击时
function searchDatas(){
	var params = getQueryParams();
	queryDate(getQueryParams());
}

$(document).ready(function(){
    //查询
	queryDate(getQueryParams());
});