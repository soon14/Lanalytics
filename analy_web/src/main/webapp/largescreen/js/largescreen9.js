preUrl = WEB_ROOT+"/largeScreen/";
//查询
function queryDate(params){
	ajaxLoadPage(params,preUrl+"right",$("#datacontext"));
}

$(document).ready(function(){
	queryDate(getQueryParams());
});