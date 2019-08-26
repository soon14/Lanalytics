var preUrl = WEB_ROOT+"/goodsAnaly/";
//继承查询方法
function queryDate(pageNo){
	
	if(pageNo == undefined){
		pageNo = 1;
	}
	var params = {};
	params.pageNo = pageNo
	params.pageSize = 10;
	params.abnormaType = $("#goodsAbnormalNav > li.active").attr("abnormaType");
	
	var queryCallBack = function(data){
		
		$("#goodsAbnormalPagerId").pager({callback:function(pageNo){
			queryDate(pageNo);
		}});
	}
	
	ajaxLoadPage(params,preUrl + "getGoodsAbnormalDate",$("#goodsAbnormalTableId"),queryCallBack);
}

$(document).ready(function(){
	
	$("#goodsAbnormalNav > li > a").click(function(){
		$("#goodsAbnormalNav > li.active").removeClass("active");
		$(this).parent().addClass("active");
		$(this).blur();
		
		queryDate(1);
	});
	
	queryDate(1);
	
	ajaxLoadPage({},preUrl + "getGoodsAbnormalSvDate",$("#goodsAbnormalSvData"));
});