/*
 * Filename : tacManage.js
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
  
  'text!templates/management/tacManage.html',
  'text!templates/management/modal/terms_modal.html',
  'text!templates/common/modal_form_alert.html'
  
], function (Backbone, Mustache, Common, i18n, template, modalform, modalAlert) {
  'use strict';
  
  var targetUrl = './model/management/Terms';
  
  var _data = {};
  
  var main_tables;
  
  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events : {
      'click button.term-detail' : 'modal_detail',
      'click button#new-terms' : 'modal_init'
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
      main_tables = $('#terms-table').DataTable({
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
          { "data" : "termsTp", "name" : "TERMS_TP" },
          { "data" : "title", "name" : "TITLE" },
          { "data" : "isMandatory", "name" : "IS_MANDATORY" },
          { "data" : "version", "name" : "VERSION" },
          { "data" : "updated", "name" : "UPDATED" , "type":"date" },
          { "data" : "id" }
        ],
        "columnDefs":  [ {
          "render": function ( data, type, row ) {
            var titleLabel = data;
            if (data == "100001") {
              titleLabel = i18n.BO2365;
            } else if (data == "100002") {
              titleLabel = i18n.BO2366;
            } else if (data == "100003") {
              titleLabel = i18n.BO2367;
            } else if (data == "100004") {
              titleLabel = i18n.BO2368;
            } else if (data == "100005") {
              titleLabel = i18n.BO2369;
            }
            return titleLabel;
          },
          "targets": 1
        }, {
          "render": function ( data, type, row ) {
            var titleLabel = data;
            if (data) {
              titleLabel = i18n.BO2362;
            } else {
              titleLabel = i18n.BO2363;
            }
            return titleLabel;
          },
          "targets": 3
        }, {
          "render": function ( data, type, row ) {
            return Common.JC_format.day(row.updated);
          },
          "targets": 5
        }, {
          "render": function ( data, type, row ) {
            return '<button class="btn btn-primary term-detail" data-toggle="modal" data-target=".bs-terms-modal-lg" target-code="'+data+'" >' + i18n.BO0022 + '</button>'; 
          },
          "bSortable": false,
          "targets": 6
        } ],
        "order": [[ 0, 'desc' ]],
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
      
      var view = this;
      data.cd = view.loadBaseCode("0");
      
      $('#terms-modal').html(Mustache.to_html(modalform, data));
      this.form_init('POST', $('#terms-modal'));
    },
    modal_detail: function (event) {
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
            data.cd = view.loadBaseCode(data.termsTp);
            data = Common.JC_format.handleData(data);
            
            $('#terms-modal').html(Mustache.to_html(modalform, view.buildOptions(data)));
            view.form_init('PUT', $('#terms-modal'));
          }
        }
      });
    },
    form_init : function(method, $modal){
      var view = this;
      
      $modal.find('select').select2();
      
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
                $modal.modal('toggle');
                main_tables.draw();
              } else {
                alert(response.errMsg);
              }
            }
          });
          
          return false;
        }
      });
    },
    loadBaseCode : function(codeValue){
      var data = {};
      if(codeValue != undefined){
        $.ajax({
          url : './model/Codes',
          method : 'POST',
          async: false,
          data : {
            'codes' : 'Terms'
          },
          success : function(result){
            if(result.success){
              $.each(Object.keys(result.codes), function(idx, code){
                $.each(result.codes[code], function(_idx, _code){
                  if (_code.subCd == "001") {
                    _code.titleLabel = i18n.BO2365;
                  } else if (_code.subCd == "002") {
                    _code.titleLabel = i18n.BO2366;
                  } else if (_code.subCd == "003") {
                    _code.titleLabel = i18n.BO2367;
                  } else if (_code.subCd == "004") {
                    _code.titleLabel = i18n.BO2368;
                  } else if (_code.subCd == "005") {
                    _code.titleLabel = i18n.BO2369;
                  } else {
                    _code.titleLabel = _code.title;
                  }
                  if (codeValue == _code.baseCd) {
                    _code.selections = ' selected="selected"';
                  }
                });
              });
              data = $.extend(result.codes, _data);
            } else {
              //
            }
          }
        });
      }
      return data;
    },
    buildOptions : function(object) {
      for (var i in object) {
        object[i + '=' + object[i]] = true;
      }
      return object;
    }
  });

  return ContentView;
});