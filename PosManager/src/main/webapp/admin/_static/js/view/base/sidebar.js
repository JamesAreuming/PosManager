/*
 * Filename	: sidebar.js
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

define([
	'backbone',
	'mustache',
	'text!templates/base/sidebar.html',
  'i18n!templates/base/nls/sidebar'
], function (Backbone, Mustache, sidebarTemplate, i18n) {
	'use strict';
	var isShow = true;
	var isClose = false;
	var View = Backbone.View.extend({
		el: $('#sidebar'),
		initialize: function () {
			this.template = sidebarTemplate;
			this.listenTo(this.model, 'sync', this.render);
		},
		initShow: function(show) {
			isShow = show;
		},
		initClose: function(close) {
			isClose = close;
		},
		render: function () {
			var view = this;
			var data = (view.model != undefined ? view.model.toJSON() : {});
      data.i18n = i18n;
			var rendered = Mustache.to_html(view.template, data);
			$(view.el).html(rendered);

			if (isClose) {
				$("body").addClass("page-sidebar-closed");
			} else {
				$("body").removeClass("page-sidebar-closed");
			}
			if (isShow) {
				$(view.el).show();
			} else {
				$(view.el).hide();
			}

			return view;
		}
	});

	return View;
});