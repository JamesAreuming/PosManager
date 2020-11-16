/*
 * Filename : reviewManage.js
 * Function :
 * Comment  :
 * History  : 
 *
 * Version  : 1.0
 * Author   : 
 */

define([
  'backbone',
  'mustache',
  'common',
  'i18n!templates/globals/lang/nls/language',
  
  'text!templates/management/reviewManage.html',
    
  'bootstrap-daterangepicker'
], function (Backbone, Mustache, Common, i18n, template) {
  'use strict';

  var targetUrl = "./model/management/Review";

  var datatables;

  var _data = {};

  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events: {
      'click button#search' : 'search'
    },
    initialize: function () {
      this.template = template;
      this.listenTo(this.model, 'sync', this.render);
      
      var view = this;
      view.initObject("begin");
      view.initObject("end");
      view.initObject("searchKeyword");
    },
    initObject : function(key) {
      if (_data[key] != null) {
        delete _data[key];
      }
    },
    render: function () {
      var view = this;
      var data = (view.model != undefined ? view.model.toJSON() : {});
      data.i18n = i18n;
      var rendered = Mustache.to_html(view.template, data);
      $(view.el).append(rendered);

      view.setCalendarArea();
      view.datatablesInit();


      return view;
    },
    
    setCalendarArea : Common.JC_calendar.searchRange(i18n, true),
    
    datatablesInit : function(){
      datatables = $('#review-table').DataTable({
        "serverSide": true,
        "language": {
            //Russian.json, English.json, Korean.json
            "url": "//cdn.datatables.net/plug-ins/1.10.11/i18n/English.json"
        },
        "ajax" : {
          "url" : targetUrl,
          "type": 'GET',
          "data" : function(data){
            return $.extend(data, _data);
          },
          "dataSrc" : 'list'
        },
        "columns" : [
          { "data" : "id", "name" : "id" },
          { "data" : "created", "name" : "A.created",
			  render: function (data, type, row) {
				  return Common.JC_format.day(data);
			  }
          },
          { "data" : "createdTime", "name" : "A.created", "bSortable": false, 
        	  render: function (data, type, row) {
				  return Common.JC_format.time(data);
			  }
          },
          { "data" : "content", "name" : "content" },
          { "data" : "rating", "name" : "rating" },
          { "data" : "name", "name" : "name" },
        ],
        "order": [[ 1, 'desc' ]],
        "pageLength": 10,
        "lengthChange": false,
        "processing": true,
        "rowReorder": true,
        "searching": false
      });

      datatables.on( 'order.dt search.dt', function () {
        datatables.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
          //cell.innerHTML = i+1;
        } );
      } ).draw();
    },
    search : function(){
      var view = this;
      var $form = $('form');
      
      _data= $.extend(_data, $form.serializeObject());
      
      $.each(Object.keys(_data), function(idx, _key){
        if ( _data[_key] == "" || _data[_key] === null || (typeof _data[_key] == "object" && $.isEmptyObject(_data[_key])) ) {
          delete _data[_key];
        }
      });

      view.datatablesReload();
    },
    datatablesReload : function(){
      var view = this;
      if(datatables != undefined){
        datatables.draw();
      }
    }
  });

  return ContentView;
});