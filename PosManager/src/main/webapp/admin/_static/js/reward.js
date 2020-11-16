/*
 * Filename	: reward.js
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

// load default setting
require.config(config);

require(['jquery',
  		'jquery.datatables'], function(jQuery) {
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
		,'modern'
		,'fusioncharts'
	], function() {
		// TODO load each page
		require(['view/reward/router'], function(Reward) {
			Reward.init();
		});
	});
});