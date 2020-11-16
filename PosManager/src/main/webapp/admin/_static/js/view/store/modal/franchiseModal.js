/*
 * Filename	: franchiseModal.js
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

  'text!templates/store/modal/franchisemodal.html',
  'i18n!templates/store/nls/store'
], function (Backbone, Mustache, template, i18n) {
  'use strict';
  var FranchiseModalView = Backbone.View.extend({
    el: $('#franchise-modal'),/*
    events: {
    	'click button.franchise-detail' : 'load_detail_franchise'
    },*/
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


      return view;
    }/*,
    load_detail_franchise: function () {
    	$.ajax({

    	});
    }*/
  });

  return FranchiseModalView;
});