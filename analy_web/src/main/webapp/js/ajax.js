(function($){

	$.doajax=function(opt){
        var defpram = "defpram" + Math.floor(Math.random() * 10000);
		var _opt = $.extend({
		    type:'post',
		    dataType:'json',
		    data:"{'"+defpram+"':'aa'}",
		    async:true,
		    cache:true,
			error:function(XMLHttpRequest, textStatus, errorThrown){
				var responseText = XMLHttpRequest.responseText;
				try{
				    var resData = eval("("+responseText+")");
				    alert("程序报错啦！错误信息：" + resData.exception);
				}catch (e) {
					alert("程序报错啦！错误信息：" + responseText);
				}
			},
			success:function(data){}
		},opt);
		
		$.ajax({
			url: _opt.url,
	        data:_opt.data,
	        type:_opt.type,
	        dataType:_opt.dataType,
	        async:_opt.async,
	        cache:_opt.cache,
	        success: function (data){
	        	var resData = data;
	            if(resData.exception){
	                alert(resData.exception.message);
	            }else{
	           	    _opt.success(resData);
	            }
	        },
	        error:function(XMLHttpRequest, textStatus, errorThrown){
	        	var status = XMLHttpRequest.status;
	        	if(status == 401){
	        		window.location.href = WEB_ROOT+"/auth/index";
	        	}else{
	        	    _opt.error(XMLHttpRequest, textStatus, errorThrown);
	        	}
	        }
		});
	};
	
})(jQuery);

/**
 * 通过ajax 加载界面
 * @param params json格式参数
 * @param url 请求url
 * @param $el 界面加载到的容器
 */
function ajaxLoadPage(params,url,$el,callBack){
	$.doajax({
		url: url,
        data:params,
        dataType:"html",
        cache:false,
        success: function (data){
        	$el.html(data);
        	if(callBack != undefined){
        		callBack(data);
        	}
        },
        error:function(XMLHttpRequest, textStatus, errorThrown){
        	var responseText = XMLHttpRequest.responseText;
        	$el.html(responseText);
        }
	});
}