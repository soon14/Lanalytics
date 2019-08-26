/**
 * chency6 简单分页 ， 基于jquery
 */

(function($) {
	$.fn.pager = function(option) {
		var $this = $(this);
		if ($this.data("size") === ""
		|| $this.data("count") === ""
		|| $this.data("count") === 0){
			
			//$this.data("size", 10);
			return;
		}
			
		$this.pager.options = $.extend({}, $.fn.pager.defaults, $this.data(),
				typeof option == 'object' && option);
		$.pager.init($this, $this.pager.options);
	}

	$.fn.pager.defaults = {
		count : 0,
		size : 10,
		currentindex : 1,
		maxButtons : 3,
		callback : function(index) {
		},
		initstatus: true,
		showSkip:true,
		showPnpage:true
	}

	$.fn.pager.options = {}

	$.pager = {
		init : function($this, opts) {
            $skip = $('<span style="float:right;margin-left:5px;">到第<input type="text" id="skipPage'+$this.attr('id')+'" name="skipPage'+$this.attr('id')+'" style="width:40px;height:27px;border:1px solid #E5E5E5;" value="'+opts.currentindex+'"/>页&nbsp;<button id="skip'+$this.attr('id')+'" style="height:27px;border:1px solid #E5E5E5;background-color:#fff">确定</button></span>');
            opts.currentindex = parseInt(opts.currentindex);
			opts.count = parseInt(opts.count);
			opts.size = parseInt(opts.size);
			opts.maxButtons = parseInt(opts.maxButtons);
			var midNum = parseInt(opts.maxButtons / 2), _pagerCount = Math
					.ceil( parseFloat(opts.count)  / opts.size), _start = opts.maxButtons < opts.currentindex
					|| (opts.currentindex > midNum && opts.currentindex + midNum <= _pagerCount) ? opts.currentindex- midNum
					: 1, $ul = $('<ul class="pages"> </ul>'), lisString = '';
			lisString += opts.currentindex > 1 ? '<li class="pgNext" pagerIndex="'
					+ (opts.currentindex - 1) + '" >上一页</li>'
					: '<li class="pgNext" pagerIndex="1">上一页</li>'

			if (_pagerCount > opts.maxButtons && _start > _pagerCount - opts.maxButtons) {
				_start = _pagerCount - opts.maxButtons + 1;
			}
		    for(var i=1;i <_start ;i++){
		    
		    	if(i===3){
		    		lisString += '<li>...</li>';
		    		break ; 
		    	}
		    	lisString += '<li class="page-number" pagerIndex="'
					+ i + '" >' + i + '</li>';
		    }
		    var tempIndex = _start ; 
			for (var i = 0 ; i < opts.maxButtons ; i++) {
			    tempIndex = _start + i;
			    if(tempIndex>_pagerCount){break ; }
				if (tempIndex != opts.currentindex) {
					lisString += '<li class="page-number" pagerIndex="'
							+ tempIndex + '" >' + tempIndex + '</li>';
				} else {
					lisString += '<li class="page-number pgCurrent" pagerIndex="'
							+ tempIndex + '" >' + tempIndex + '</li>';
				}
			}
			var tempb = _start+opts.maxButtons ; 
			
			if( _pagerCount>=tempb && _pagerCount<=tempb+1 ){
			
				while(_pagerCount>=tempb){
					lisString += '<li class="page-number" pagerIndex="'
						+ tempb + '" >' + (tempb) + '</li>';
					tempb++ ; 
				}

			}else if(_pagerCount>tempb+1){
				
				lisString += '<li>...</li><li class="page-number" pagerIndex="'
					+ (_pagerCount-1) + '" >' + (_pagerCount-1) + '</li>';
		
				lisString += '<li class="page-number" pagerIndex="'
					+ (_pagerCount) + '" >' + (_pagerCount) + '</li>';

			}
			lisString += opts.currentindex >= _pagerCount ? '<li class="pgNext" pagerIndex="'
					+ opts.currentindex + '" >下一页</li>'
					: '<li class="pgNext" pagerIndex="'
							+ (opts.currentindex + 1) + '" >下一页</li>';

			if(opts.showSkip == false){
				$skip.hide();
			}
			
			$ul.append(lisString);
			$this.append($skip);
			$this.append($ul);
			
			if(opts.showPnpage == false){
				$ul.find(".pgNext").hide();
			}
			
			$('#skip'+$this.attr('id')).on('click',function(){
				var pIndex = parseInt($('#skipPage'+$this.attr('id')).val());
				var r = /^\+?[1-9][0-9]*$/;
				if(!r.test(pIndex)){
			       return false;
				}
				$this.empty();
				opts.callback(pIndex);
			});

			$("li.page-number,li.pgNext", $ul).each(function() {
				var $li = $(this);
				$li.click(function() {
					var pIndex = parseInt($(this).attr("pagerIndex"));
					if (parseInt(opts.currentindex) !== pIndex) {
						opts.callback(pIndex);
					}

				});
			});

		}
	}
})(jQuery)
