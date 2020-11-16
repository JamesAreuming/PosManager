/*
 * Filename	: footer.js
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
	'text!templates/base/footer.html',
	'i18n!templates/base/nls/header'
], function (Backbone, Mustache, footerTemplate, i18n) {
	'use strict';
	var FooterView = Backbone.View.extend({
		el: $('#footer'),
		initialize: function () {
			this.template = footerTemplate;
			this.listenTo(this.model, 'sync', this.render);
		},
		render: function () {
			var view = this;
			var data = (view.model != undefined ? view.model.toJSON() : {});
      data.i18n = i18n;
			var rendered = Mustache.to_html(view.template, data);
			$(view.el).html(rendered);

			return view;
		}
	});
	
	return FooterView;
});