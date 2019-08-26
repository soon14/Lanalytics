var preUrl = WEB_ROOT+"/system/";

function updateUrlName(btn,idx){
	
	$(btn).blur();
	
	if(idx == ""){
		return;
	}
	
	var name = $("#url_" + idx).val();
	name = $.trim(name);
	
	if(name == ""){
		$("#url_" + idx).focus();
		return;
	}
	
	$.doajax({
		url: preUrl+"saveUrlDates",
        data:{
        	idx:idx,
        	name:name
        },
        success:function(data){
        	$("#url_" + idx).val(name);
        	if(data.result == "true"){
        		$("#alertMsg").html("更新成功");
        	}else{
        		$("#alertMsg").html("更新失败");
        	}
        	
        	$("#alertModal").modal('show');
        }
	});
}

function queryUrlsData(pageNo){
	var params = {
					url:$.trim($("#inp_url").val()),
					name:$.trim($("#inp_name").val()),
					pageNo:pageNo,
					pageSize:20
				 };
	
	ajaxLoadPage(params,preUrl+"geturldates",$("#urlstbDiv"),function(){
		$("#urlPagerId").pager({callback:function(pageNo){
			queryUrlsData(pageNo);
		}});
	});
}

function refreshCache(btn){
	$(btn).attr("disabled",true);
	$.doajax({
		url: preUrl+"refreshUrlCache",
        success:function(data){
        	
        	if(data.result == "true"){
        		$("#alertMsg").html("刷新成功");
        	}else{
        		$("#alertMsg").html("刷新失败");
        	}
        	
        	$("#alertModal").modal('show');
        	$(btn).attr("disabled",false);
        },
        error:function(XMLHttpRequest, textStatus, errorThrown){
        	$("#alertMsg").html("刷新失败");
        	$("#alertModal").modal('show');
        	$(btn).attr("disabled",false);
        }
	});
}

$(document).ready(function(){
	queryUrlsData(1);
});