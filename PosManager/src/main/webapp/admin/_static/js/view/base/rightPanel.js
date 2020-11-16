/*
 * Filename	: rightPanel.js
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
  'text!templates/base/rightPanel.html',
  'i18n!templates/base/nls/rightPanel'
], function (Backbone, Mustache, template, i18n) {
  'use strict';
  var isClose = true;
  var View = Backbone.View.extend({
    el: $('#rightPanel'),
    initialize: function () {
      this.template = template;
      this.listenTo(this.model, 'sync', this.render);
    },
    initClose: function (close) {
      isClose = close;
    },
    render: function () {
      var view = this;
      var data = (view.model != undefined ? view.model.toJSON() : {});
      data.i18n = i18n;
      var rendered = Mustache.to_html(view.template, data);
      $(view.el).html(rendered);

      if (isClose) {
        $("body").removeClass("chat-view");
      } else {
        $("body").addClass("chat-view");
      }
      return view;
    }
  });

  return View;
});