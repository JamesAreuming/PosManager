/*
 * Filename	: pageHeader.js
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
  'text!templates/base/pageHeader.html',
  'i18n!templates/base/nls/pageHeader'
], function (Backbone, Mustache, headerTemplate, i18n) {
  'use strict';
  var HeaderView = Backbone.View.extend({
    el: $('#pageHeader'),
    initialize: function (options) {
      this.template = headerTemplate;
      this.listenTo(this.model, 'sync', this.render);
    },
    render: function (pageNavi) {
        var view = this;
        var data = (pageNavi != undefined ? pageNavi : {});
        var nowPage = pageNavi.nowPage;
        data.i18n = i18n;
        var rendered = Mustache.to_html(view.template, data);

        if(nowPage != undefined){
      	  rendered = Mustache.to_html(rendered, nowPage);
        }
        
        // window title
        if ('windowTitle' in data) {
          $('title').text(Mustache.to_html(data.windowTitle, nowPage));
        }
        $(view.el).html(rendered);

        return view;
    }
  });

  return HeaderView;
});