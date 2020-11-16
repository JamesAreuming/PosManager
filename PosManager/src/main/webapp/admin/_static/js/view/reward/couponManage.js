/*
 * Filename	: couponManage.js
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
  'common',
  'i18n!templates/globals/lang/nls/language',
  
  'text!templates/reward/couponManage.html',
  
], function (Backbone, Mustache, Coommon, i18n, template) {
  'use strict';
  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    initialize: function () {
      this.template = template;
      this.listenTo(this.model, 'sync', this.render);
    },
    render: function () {
      var view = this;
      var data = (view.model != undefined ? view.model.toJSON() : {});
      data.i18n = i18n;
      var rendered = Mustache.to_html(view.template, data);
      $(view.el).append(rendered);

      //Index.init(data);
      //Index.initCharts(); // init index page's custom scripts

      return view;
    }
  });

  return ContentView;
});	