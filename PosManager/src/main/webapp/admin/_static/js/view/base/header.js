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
  'text!templates/base/header.html',
  'i18n!templates/base/nls/header'
], function (Backbone, Mustache, headerTemplate, i18n) {
  'use strict';
  var View = Backbone.View.extend({
    el: $('#header'),
    initialize: function () {
      this.template = headerTemplate;
      this.listenTo(this.model, 'sync', this.render);
    },
    render: function () {
      var view = this;
      var data = (view.model != undefined ? view.model.toJSON() : {});
      data.i18n = i18n;
      data.user = view.userStatus();
      var rendered = Mustache.to_html(view.template, data);
      $(view.el).html(rendered);

      return view;
    },
    userStatus : function(){
    	var view = this;
    	var user = {};
    	$.ajax({
			url : './model/UserStatus',
			async: false,
			success : function(response){
				if(response.success){
					user = response.session;
				}
			}
		});
    	return user;
    }
  });

  return View;
});