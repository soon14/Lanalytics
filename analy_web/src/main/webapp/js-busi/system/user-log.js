/**
 * Created by amax on 7/31/
 */
var _maq = _maq || [];
_maq.push(["_setAccount", "WOEGOUAT"]);
_maq.push(["_sanme", "WOEGOUAT"]);


//a.gif
var _externalParams = _externalParams || {};

function setv(name) {
    if (document.cookie.length > 0) {
        var c_start = document.cookie.indexOf(name + "=");
        if (c_start != -1) {
            c_start = c_start + name.length + 1;
            var c_end = document.cookie.indexOf(";", c_start);
            if (c_end == -1) c_end = document.cookie.length;
            return decodeURI(document.cookie.substring(c_start, c_end));
        }
    }
}
(function () {

    var params = {};

    //Document
    if (document) {
        params.domain = document.domain || '';
        params.url = document.URL || '';
        params.title = document.title || '';
        params.referrer = document.referrer || '';
    }

    //Window
    if (window && window.screen) {
        params.sh = window.screen.height || 0;
        params.sw = window.screen.width || 0;
        params.cd = window.screen.colorDepth || 0;
    }

    //navigator
    if (navigator) {
        params.lang = navigator.language || '';
    }

    //_maq
    if (_maq) {
        for (var i in _maq) {
            switch (_maq[i][0]) {
                case '_setAccount':
                    params.account = _maq[i][1];
                    break;
                case '_sanme':
                    params.sanme = setv(_maq[i][1]);
                    break;
                default:
                    break;
            }
        }
    }

    //
    var args = '';
    for (i in params) {
        if (args != '') {
            args += '&';
        }
        args += i + '=' + encodeURIComponent(params[i]);
    }
	
	for (i in _externalParams) {
        if (args != '') {
            args += '&';
        }
        args += i + '=' + encodeURIComponent(_externalParams[i]);
    }

    //
    var img = new Image(1, 1);
    img.src = '/a.gif?' + args;

})();

function clickReport(event){
	var params = {};

    //Document
    if (document) {
        params.domain = document.domain || '';
        params.url = document.URL || '';
        params.title = document.title || '';
        params.referrer = document.referrer || '';
		
		params.docwidth = $(document).width();
		params.docheight = $(document).height();
    }

    //Window
    if (window && window.screen) {
        params.sh = window.screen.height || 0;
        params.sw = window.screen.width || 0;
        params.cd = window.screen.colorDepth || 0;
    }

    //navigator
    if (navigator) {
        params.lang = navigator.language || '';
    }
	
	//event
	if(event){
		params.pagex = event.pageX || 0;
        params.pagey = event.pageY || 0;
		var e = event.target || event.srcElement;
		if("a" == e.tagName.toLowerCase()){
			params.ahref=encodeURIComponent(e.href);
		}
    }

    //
    var args = '';
    for (i in params) {
        if (args != '') {
            args += '&';
        }
        args += i + '=' + encodeURIComponent(params[i]);
    }

    //
    var img = new Image(1, 1);
    img.src = '/b.gif?' + args;
}

(function(){
	document.body.addEventListener('click',clickReport, false);
})();