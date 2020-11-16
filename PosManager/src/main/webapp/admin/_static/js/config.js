/*
 * Filename	: config.js
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

'use strict';

var regex = /[?&]([^=#]+)=([^&#]*)/g,
	url = window.location.href,
	params = {},
	match;
	while(match = regex.exec(url)) {
	params[match[1]] = match[2];
}

/* Utility */
var JC_util = {
	trimLeft: function(str) {
		return str.indexOf(' ') == 0 ? str.replace(/\s+/, '') : str;
	},
	trimRight: function(str) {
		return str.replace(/\s+$/, '');
	},
	trim: function(str) {
		if (typeof(str) == "number") {str += "";}
		if (!str || str.length == 0) {return "";}
		str = JC_util.trimLeft(str);
		return JC_util.trimRight(str);
	}
};

/* Cookie */
var JC_cookies = {
	set: function(key, value) {
		var validDays = 1;
		var date = new Date();
		date.setDate(date.getDate() + validDays);
		var expires = "expires=" + date.toUTCString() + ";";
		var path = "path=/;"
		var domain = "domain=" + document.domain + ";";
		//document.cookie = key + "=" + escape(value) + ";" + expires + domain + path;
		document.cookie = key + "=" + escape(value) + ";" + expires + path;
	},
	get: function(key) {
		var cookies = document.cookie.split(';');
		for(var i = 0; i < cookies.length; i++) {
			var pair = cookies[i].split("=");
			if (JC_util.trim(pair[0]) == key) {
				return unescape(pair[1]);
			}
		}
		return "";
	},
	remove: function(key) {
		JC_cookies.set(key, "", -1);
	},
	reload: function(key, value) {
		JC_cookies.set(key,value);
		location.reload();
	}
};

/* Google Map */
var _googleKey = "AIzaSyAAnApqHjMrIWnk_2ezU_nHDWKuwUJuiYE";
var JC_google_util = {
	setupMapKey: function(divId) {
		var mapJsId = "gm_js_div";
		var mapJsObj = $("#" + mapJsId);
		if (!mapJsObj || !mapJsObj.length) {
			var param = "?key=" + _googleKey + "&language=" + _lang;
			$("<script src='https://maps.google.com/maps/api/js" + param + "' />").appendTo($("<div id='" + mapJsId + "' />").appendTo($("#" + divId)));
		}
	}
};

var _lang = JC_cookies.get("BAK_ADMIN_LANG");
if (_lang == undefined || _lang == '') {
	_lang = 'ko'
}

var config = {
  baseUrl: './_static/js',
  waitSeconds: 5,
  locale: _lang,
  pageNaviCount: 10,
  paths: {
    // template
    'templates'				: '../templates',

    // for ie
    'respond'				: '../assets/plugins/respond/dest/respond.min',
    'html5shiv'				: '../assets/plugins/html5shiv/dist/html5shiv.min',
    // core
    'angular'				: '../assets/plugins/angularjs/angular.min',
    'underscore'			: '../assets/plugins/underscore-min',
    'backbone'				: '../assets/plugins/backbone-min',
    'mustache'				: '../assets/plugins/mustache.min',
    'jquery'				: '../assets/js/jquery-1.11.1.min',
    'jquery-validator'      : '//cdnjs.cloudflare.com/ajax/libs/jquery-form-validator/2.2.8/jquery.form-validator.min',
    'jquery-form'			: '../assets/plugins/jquery-form/jquery.form',
    'jquery-ui'				: '../assets/plugins/jquery-ui/jquery-ui.min',
    'bootstrap'				: '../assets/plugins/bootstrap/js/bootstrap.min',

    'fusioncharts'				: '../assets/plugins/fusioncharts-suite-n-xt-developer/js/fusioncharts',
    'fusioncharts.charts' : '../assets/plugins/fusioncharts-suite-n-xt-developer/js/fusioncharts.charts',
    'fusioncharts.theme.fint'	: '//static.fusioncharts.com/code/latest/themes/fusioncharts.theme.fint.js?cacheBust=56',
    		
    'toastr'				: '../assets/plugins/toastr/toastr.min',
    'bootbox'				: '../assets/plugins/bootbox.min',
    'pace'					: '../assets/plugins/pace-master/pace.min',
    'moment'				: '../assets/plugins/moment/moment',	// 날짜(Date)형식의 데이터 파싱, 검증, 조작 그리고 화면에 추력하는 작업을 위해 필요함
    'moment-locale'			: '../assets/plugins/moment/moment-with-locales',


    'jquery-sparkline'		: '../assets/plugins/jquery.sparkline.min',
    'toggles'				: '../assets/plugins/toggles.min',
    'retina'				: '../assets/plugins/retina.min',
    'jquery-cookies'		: '../assets/plugins/jquery.cookies',
    'jquery-chosen'			: '../assets/plugins/chosen.jquery.min',


    'jquery-flot'			: '../assets/plugins/flot/flotjquery.flot.min',
    'jquery-flot.resize'	: '../assets/plugins/flot/flotjquery.flot.resize.min',
    'jquery-flot.spline'	: '../assets/plugins/flot/flotjquery.flot.spline.min',
    'morris'				: '../assets/plugins/morris/morris.min',
    //'raphael'				: '../assets/plugins/raphael-2.1.0.min',

    'select2'				: '../assets/plugins/select2/js/select2.min',
    'form-select2'			: '../assets/js/pages/form-select2',
    'jquery-migrate'		: '../assets/js/jquery-migrate-1.2.1.min',
    'jquery-prettyPhoto'	: '../assets/js/jquery.prettyPhoto',

    'bracket'				: '../assets/js/bracket',
//    'custom'				: '../assets/js/custom',
    'dashboard'				: '../assets/js/dashboard',
    'off-canvas-classie'	: '../assets/plugins/offcanvasmenueffects/js/classie',
    'off-canvas-main'		: '../assets/plugins/offcanvasmenueffects/js/main',
    //'waves'					: '../assets/plugins/waves/waves.min',
    '3d-bold-navi-main'		: '../assets/plugins/3d-bold-navigation/js/main',
    '3d-bold-navi-modernizr': '../assets/plugins/3d-bold-navigation/js/modernizr',

    'jquery.mockjax'		: '../assets/plugins/jquery-mockjax-master/jquery.mockjax',
    'jquery.datatables'		: '../assets/plugins/datatables/js/jquery.dataTables',
    'bootstrap-popover'		: '../assets/plugins/bootstrap-popover/bootstrap-popover',
    'bootstrap-editable'	: '../assets/plugins/x-editable/bootstrap3-editable/js/bootstrap-editable',
    'bootstrap-validator'	: '../assets/plugins/bootstrap-validator/dist/validator',
    'bootstrap-datepicker'	: '../assets/plugins/bootstrap-datepicker/js/bootstrap-datepicker',
    'bootstrap-datepicker-lang-kr'	: '../assets/plugins/bootstrap-datepicker/js/locales/bootstrap-datepicker.kr',
    'bootstrap-daterangepicker' : '../assets/plugins/bootstrap-daterangepicker/daterangepicker',

    'snap.svg'				: '../assets/plugins/offcanvasmenueffects/js/snap.svg-min',
    'jquery.blockui'		: '../assets/plugins/jquery-blockui/jquery.blockui',
    'jquery.slimscroll'		: '../assets/plugins/jquery-slimscroll/jquery.slimscroll.min',
    'jquery.uniform'		: '../assets/plugins/uniform/jquery.uniform.min',

    'bootstrap-treeview'	: '../assets/plugins/bootstrap-treeview/dist/bootstrap-treeview',
    'tree-grid-directive'   : '../assets/plugins/tree-grid-directive/tree-grid-directive',

    'modern'				: '../assets/js/modern-require',

    'summernote'			: '../assets/plugins/summernote-master/summernote.min',

    'google-map'			: '//maps.googleapis.com/maps/api/js?sensor=false',

    'maxazan-jquery-treegrid' : '../assets/plugins/maxazan-jquery-treegrid/js/jquery.treegrid.min',

    'jquery-gridly' : '../assets/plugins/jquery-gridly/jquery.gridly.test',

    'morris-chart' : '../assets/plugins/morris/morris.min',
    'raphael' : '../assets/plugins/morris/raphael.min',

	
	//,'fusioncharts.theme.fint'			: '../assets/plugins/fusioncharts-suite-n-xt-developer/js/themes/fusioncharts.theme.fint.js?cacheBust=56'
	//,'fusioncharts-jquery-plugin'	  : '../assets/plugins/fusioncharts-jquery-plugin-master/package/fusioncharts-jquery-plugin'
	//,'fusioncharts-jquery-plugin.min' : '../assets/plugins/fusioncharts-jquery-plugin-master/package/fusioncharts-jquery-plugin.min'

	'jquery.tabletoCSV' : '../assets/plugins/jquery.tabletoCSV'

  },
  urlArgs : function(id, url) {
	  var args = '';
	  if (url.indexOf('maps.googleapis.com') !== -1) {
	      //args = 'key=AIzaSyAvBnBp0JP0MQAev1e_py91b1vMgFjZakI';
	  }
	  return (url.indexOf('?') === -1 ? '?' : '&') + args;
  },
  shim: {
    'jquery': {
      exports: '$'
    },
    'underscore': {
      exports: '_'
    },
    'backbone': {
      deps: ['underscore'],
      exports: 'Backbone'
    },
    'moment': {
      exports: 'moment'
    },
    'mustache': {
      exports: 'Mustache'
    },
    'toastr': {
      deps: ['jquery', 'bootstrap'],
      exports: 'toastr'
    },
    'bootbox': {
      deps: ['jquery', 'bootstrap'],
      exports: 'bootbox'
    },
    'validator': {
      exports: 'validator'
    },
    'pace': {
      exports: 'pace'
    },
    'tree-grid-directive' : ['angular'],
    'jquery.datatables' : ['jquery'],
    'bootstrap-editable' : ['bootstrap','jquery.datatables'],
    'bootstrap-treeview' : ['bootstrap'],
    'jquery-validator' : ['jquery'],
    'jquery-form' : ['jquery'],
    'jquery-ui': ['jquery'],
    'jquery-gridly' : ['jquery'],
    'bootstrap': ['jquery'],
    'jquery.mockjax': ['jquery'],
    'bootstrap-datepicker' : ['jquery'],
    'bootstrap-datepicker-lang-kr' : ['bootstrap-datepicker'],
    'bootstrap-daterangepicker': ['jquery', 'bootstrap', 'moment'],
    'morris-chart' : ['jquery', 'raphael'],

    'fusioncharts' : ['jquery'],
    //'fusioncharts-jquery-plugin' : ['jquery', 'fusioncharts-jquery-plugin.min'],

    'jquery.tabletoCSV' : ['jquery'],

    'off-canvas-main': {
      deps: ['off-canvas-classie']
    },

    '3d-bold-navi-modernizr': {
      deps: ['3d-bold-navi-main']
    }
  }
};