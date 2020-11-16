/*
 * Filename : appManage.js
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
  
  'text!templates/management/appManage.html',
  'text!templates/management/modal/app_manage_modal.html',
  'text!templates/common/modal_form_alert.html',

], function (Backbone, Mustache, Common, i18n, template, modalform, modalAlert ) {
  'use strict';

  var targetUrl = "./model/management/AppVersion";
  var targetListUrl = "./model/management/AppVersionList";

  var main_tables;
  var _data = {};

  var modal_tables;
  var _data_modal = {};

  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events: {
      'click .app-manage-detail' : 'modal_detail',
      'click #new-app-manage' : 'modal_init',
      'click button#search' : 'search'
    },
    initialize: function () {
      this.template = template;
      this.listenTo(this.model, 'sync', this.render);
      
      var view = this;
      view.initObject("searchKeyword");
    },
    initObject : function(key) {
      if (_data[key] != null) {
        delete _data[key];
      }
    },
    render: function () {
      var view = this;
      var data = {};
      data.os = (view.model != undefined ? view.model.toJSON() : {});
      data.appTypeList = view.loadBaseCode('AppType', '0');
      data.osTypeList = view.loadBaseCode('MobileOSType', '0');
      data.i18n = i18n;
      var rendered = Mustache.to_html(view.template, data);
      $(view.el).append(rendered);

      view.datatablesInit();

      return view;
    },
    loadBaseCode : function(codeValue, defaultValue){
      var data = {};
      $.ajax({
        url : './model/Codes',
        method : 'POST',
        async: false,
        data : {
          'codes' : codeValue
        },
        success : function(result){
          if(result.success){
            $.each(Object.keys(result.codes), function(idx, code){
              $.each(result.codes[code], function(_idx, _code){
                if (defaultValue == _code.baseCd) {
                  _code.selections = ' selected="selected"';
                }
              });
            });
            data = $.extend(result.codes[codeValue.toLowerCase()], _data);
            //data = data.concat(result.codes[codeValue.toLowerCase()]);
          }
        }
      });
      return data;
    },
    datatablesInit : function(){
      main_tables = $('#app-manage-table').DataTable({
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
          { "data" : "appTpNm", "name" : "APP_TP" },
          { "data" : "version", "name" : "VERSION" },
          { "data" : "osTpNm", "name" : "OS_TP" },
          { "data" : "appInfo", "name" : "APP_INFO" },
          { "data" : "updated", "name" : "UPDATED" , "type":"date" },
          { "data" : "id" }
        ],
        "columnDefs":  [ {
          "render": function ( data, type, row ) {
            return Common.JC_format.day(data);
          },
          "targets": 5
        }, {
          "render": function ( data, type, row ) {
            return '<button class="btn btn-primary btn-sm app-manage-detail" data-toggle="modal" data-target=".bs-app-manage-modal-lg" target-code="'+data+'" >' + i18n.BO0022 + '</button>';
          },
          "bSortable": false,
          "targets": 6
        } ],
        "order": [[ 0, 'desc' ]],
        "pageLength": 10,
        "lengthChange": false,
        "processing": true,
        "rowReorder": true,
        "searching": false,
        "rowId" : 'id'
      });
    },
    modal_init : function(){
      var view = this;
      var data = {};
      data.i18n = i18n;
      data.appTypeList = view.loadBaseCode('AppType', '0');
      data.osTypeList = view.loadBaseCode('MobileOSType', '0');
      data.method = 'POST';
      
      $('#app-manage-modal').html(Mustache.to_html(modalform, data));
      this.form_init('POST', $('#app-manage-modal'));
      
      _data_modal.data = JSON.stringify({
        appTp : "",
        osTp : ""
      });
      view.showVersionList();
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
            data.appTypeList = view.loadBaseCode('AppType', data.appTp);
            data.osTypeList = view.loadBaseCode('MobileOSType', data.osTp);
            data.method = 'PUT';

            $('#app-manage-modal').html(Mustache.to_html(modalform, view.buildOptions(data)));
            view.form_init('PUT', $('#app-manage-modal'));
            
            _data_modal.data = JSON.stringify({
              appTp : data.appTp,
              osTp : data.osTp
            });
            view.showVersionList();
          }
        }
      });
    },
    showVersionList : function(){
      modal_tables = $('#app-version-table').DataTable({
        "serverSide": true,
        "language": {
          "url": "//cdn.datatables.net/plug-ins/1.10.11/i18n/English.json"
        },
        "ajax" : {
          "url" : targetListUrl,
          "type": 'GET',
          "data" : function(data){
            return $.extend(data, _data_modal);
          },
          "dataSrc" : 'list'
        },
        "columns" : [
          { "data" : "version" },
          { "data" : "isStrictUpdate" },
          { "data" : "updateInfo" },
          { "data" : "updated" , "type":"date" },
        ],
        "columnDefs":  [ {
          "render": function ( data, type, row ) {
            return data + ' (' + row.versionCode + ')';
          },
          "targets": 0
        } , {
          "render": function ( data, type, row ) {
            if (data) {
              return i18n.BO2061;
            } else {
              return i18n.BO2062;
            }
          },
          "targets": 1
        } , {
          "render": function ( data, type, row ) {
            return Common.JC_format.day(data);
          },
          "targets": 3
        } ],
        "order": [[ 0, 'desc' ]],
        "pageLength": 10,
        "lengthChange": false,
        "processing": true,
        "rowReorder": false,
        "searching": false,
        "ordering": false,
        "rowId" : 'id'
      });
    },
    redrawVersionList : function() {
      var appTp = $('#modal_app_select').val();
      var osTp = $('#modal_os_select').val();
      var objDiv = $('#btn_delete_group');
      if (appTp == '' || osTp == '') {
        this.showDeleteButton(false);
      }
      else {
        this.showDeleteButton(true);
      }
      _data_modal.data = JSON.stringify({
        appTp : appTp,
        osTp : osTp
      });
      modal_tables.draw();
    },
    showDeleteButton : function(value) {
      if (value) {
        $('#btn_delete_group').removeClass("hide");
        $('#btn_delete_group').addClass("show");
      }
      else {
        $('#btn_delete_group').removeClass("show");
        $('#btn_delete_group').addClass("hide");
      }
    },
    form_init : function(method, modal){
      var view = this;

      modal.find('select').select2();

      $('#modal_app_select').select2().on('change', function() {
        view.redrawVersionList();
      });
      $('#modal_os_select').select2().on('change', function() {
        view.redrawVersionList();
      });
      
      $('#btn_delete_update').on('click', function (e) {
        e.preventDefault();
        if (modal_tables != undefined && modal_tables.data() != undefined) {
          if (modal_tables.data().length > 0) {
            var latestId = modal_tables.data()[0].id;
            $.ajax({
              url : targetUrl,
              type: 'POST',
              data : {
                "_method" : 'DELETE',
                "data" : JSON.stringify({ id : latestId })
              },
              success : function(response){
                if(response.success){
                  $('#app-manage-modal').modal('toggle');
                  main_tables.draw();
                }
              }
            });
          }
        }
        return false;
      });
      
      if (method == "PUT") {
        this.showDeleteButton(true);
      }
      else {
        this.showDeleteButton(false);
      }
      
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
                $('#app-manage-modal').modal('toggle');
                main_tables.draw();
              }
            }
          });

          return false;
        }
      });
    },
    buildOptions : function(object) {
      for (var i in object) {
          object[i + '=' + object[i]] = true;
      }
      return object;
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
      if(main_tables != undefined){
        main_tables.draw();
      }
    }
  });

  return ContentView;
});