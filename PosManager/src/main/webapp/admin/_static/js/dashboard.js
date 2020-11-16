/*
 * Filename	: dashboard.js
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

// load default setting
require.config(config);

require(['jquery'], function(jQuery) {
	'use strict';
	if ($('html.lt-ie9').size()) {
		require(['respond', 'html5shiv'], function() {
			console.log('load internet explorer lib.');
		});
	}
	
	$.fn.serializeObject = function()
	{
	   var o = {};
	   var a = this.serializeArray();
	   $.each(a, function() {
	       if (o[this.name]) {
	           if (!o[this.name].push) {
	               o[this.name] = [o[this.name]];
	           }
	           o[this.name].push(this.value || '');
	       } else {
	           o[this.name] = this.value || '';
	       }
	   });
	   return o;
	};


	require([
		'bootstrap-editable'
		,'select2'
		,'jquery-validator'
		,'jquery-form'
		,'fusioncharts'
		,'modern'
	], function() {
		// TODO load each page
		require(['view/dashboard/router'], function(Dashboard) {
			Dashboard.init();
		});
	});
});