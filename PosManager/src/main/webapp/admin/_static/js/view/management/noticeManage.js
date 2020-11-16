/*
 * Filename : noticeManage.js
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
  
  'text!templates/management/noticeManage.html',
  'text!templates/management/modal/notice_modal.html',
  'text!templates/common/modal_form_alert.html',

  'bootstrap-daterangepicker'
], function (Backbone, Mustache, Common, i18n, template, modalform, modalAlert) {
  'use strict';

  var targetUrl = "./model/management/Notice";

  var main_tables;

  var _data = {};

  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events: {
      'click .notice-detail' : 'modal_detail',
      'click #new-notice' : 'modal_init',
      'click .add-on-datepicker' : 'trigger_calender'
    },
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

      view.datatablesInit();

      return view;
    },
    datatablesInit : function(){
      main_tables = $('#notice-table').DataTable({
        "serverSide": true,
        "language": {
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
          { "data" : "title", "name" : "TITLE" },
          { "data" : "open", "name" : "CLOSE" },
          { "data" : "created", "name" : "CREATED" , "type":"date" },
          { "data" : "id" }
        ],
        "columnDefs":  [ {
          "render": function ( data, type, row ) {
            return Common.JC_format.day(row.open) + " ~ "+ Common.JC_format.day(row.close);
          },
          "targets": 2
        }, {
          "render": function ( data, type, row ) {
            return Common.JC_format.day(row.created);
          },
          "targets": 3
        }, {
          "render": function ( data, type, row ) {
            return '<button class="btn btn-primary btn-sm notice-detail" data-toggle="modal" data-target=".bs-notice-modal-lg" target-code="'+data+'" >' + i18n.BO0022 + '</button>';
          },
          "bSortable": false,
          "targets": 4
        } ],
        "order": [[ 2, 'desc' ]],
        "pageLength": 10,
        "lengthChange": false,
        "processing": true,
        "rowReorder": true,
        "rowId" : 'id'
      });
    },
    modal_init : function(){
      var data = {};
      data.i18n = i18n;
      data.noticeDate = "";
      
      $('#notice-modal').html(Mustache.to_html(modalform, data));
      this.form_init('POST', $('#notice-modal'));
    },
    modal_detail : function(event){
      var view = this;
      var _this = $(event.target);
      $.ajax({
        url : targetUrl,
        data : {
          data : JSON.stringify({
            id : _this.attr("target-code"),
          })
        },
        success : function(response){
          if(response.success){
            var data = response.list;
            data.i18n = i18n;
            
            data = Common.JC_format.handleData(data);
            
            $('#notice-modal').html(Mustache.to_html(modalform, view.buildOptions(data)));
            view.form_init('PUT', $('#notice-modal'));
          }
        }
      });
    },
    form_init : function(method, modal){
      var view = this;

      modal.find('select').select2();

      view.setCalendarArea();
      
      $.validate({
        validateOnBlur : false,
        scrollToTopOnError : false,
        showHelpOnFocus : false,
        errorMessagePosition : $('.form-validatoin-alert-detail'),
        addSuggestions : false,
        onError : function($form) {
          var _target = $('#modal-form');

          if(_target.find('.alert').length == 0){
        	  _target.prepend(Mustache.to_html(modalAlert, {i18n:i18n}));
          }
        },
        onSuccess : function($form) {
          var params = $form.serializeObject();

          $.ajax({
            url : targetUrl,
            type: 'POST',
            data : {
              "_method" : method,
              "data" : JSON.stringify(params)
            },
            success : function(response){
              if(response.success){
                $('#notice-modal').modal('toggle');
                main_tables.draw();
              }
            }
          });

          return false;
        }
      });
    },
    
    setCalendarArea : Common.JC_calendar.searchRange(i18n,true),
    
    buildOptions : function(object) {
      for (var i in object) {
          object[i + '=' + object[i]] = true;
      }
      return object;
    },
    
    trigger_calender : function(event){
    	var _this = $(event.target);
    	var _cal = _this.closest('div').find('.date-picker');
    	_cal.trigger('click');    	
    }
  });

  return ContentView;
});