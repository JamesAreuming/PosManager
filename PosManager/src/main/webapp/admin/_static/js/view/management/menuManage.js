/*
 * Filename : dashboard.js
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
  
  'text!templates/management/menuManage.html',
  'text!templates/management/modal/menu_form.html',
  'text!templates/common/modal_form_alert.html',

  'bootstrap-treeview',
  'form-select2'

  /*
  'text!templates/sysmanage/modal/basecode_modal_sub.html', 
  , sub_modal
  */
], function (Backbone, Mustache, Common, i18n, template,  menu_form, modalAlert) {
  'use strict';

  var tree;

  var modelUrl = './model/management/BackOfficeMenu';

  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events: {
      'click button.register-main-menu' : 'registMainmenu',
      'click #tree_form button.register-sub-menu' : 'registSubmenu'
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

      view.renderTree();

      form_select.init();

      return view;
    },
    init_treeview : function(list){
      var view = this;
      
      $.each(list, function(_idx, _list){
        if (_list.titleCode != undefined && _list.titleCode != "") {
          _list.title = i18n[_list.titleCode];
        }
        if (_list.nodes != null && _list.nodes != undefined) {
          $.each(_list.nodes, function(_idx, _listNode){
            if (_listNode.titleCode != undefined && _listNode.titleCode != "") {
              _listNode.title = i18n[_listNode.titleCode];
            }
          });
        }
      });
      
      $('#tree_view').treeview({
        data: list,         // data is not optional
        levels: 2,
        onNodeSelected: function(event, data) {
          $.ajax({
            url : modelUrl,
            method : 'GET',
            data : {
              data : JSON.stringify({menuCd : data.menuCd, id : data.id})
            },
            success : function(result){
              if(result.success){
                view.setform(result.menu);
              }
            }
          });
        }
      });
    },
    registMainmenu : function() {
      var view = this;
      
      var data = {};
      view.setform(data);
      
      var target = $('#tree_form');
      target.find('.register-sub-menu').addClass('hide');
      target.find('.menu-manage-update').text(i18n.BO0025);
      target.find('#methodType').val('POST');
    },
    registSubmenu : function() {
      var target = $('#tree_form');
      var menuCode = target.find('#menuCd').val();
      if (menuCode.length == 0) {
        return false;
      }
      target.find('.register-sub-menu').addClass('hide');
      target.find('.menu-manage-update').text(i18n.BO0025);
      target.find('#methodType').val('POST');
      target.find('#id').val('');
      target.find('#menuCd').val('');
      target.find('#upCd').val(menuCode);
      target.find('#upCode').val(menuCode);
      target.find('#subCd').val('');
      target.find('#title').val('');
      target.find('#titleCode').val('');
      target.find('#url').val('');
      target.find('#dsc').val('');
      $("select#useYn option").get(0).selected = true;
    },
    setform : function(data){
      var target = $('#tree_form');
      var view = this;
      data.i18n = i18n;
      target.html(Mustache.to_html(menu_form, view.buildOptions(data)));

      $.validate({
        validateOnBlur : false,
        scrollToTopOnError : false,
        showHelpOnFocus : false,
        errorMessagePosition : $('.form-validatoin-alert-detail'),
        addSuggestions : false,
        onError : function($form) {
          var _target = $('form');

          if(_target.find('.alert').length == 0){
        	  _target.prepend(Mustache.to_html(modalAlert, {i18n:i18n}));
          }
        },
        onSuccess : function($form) {
          var methodType = $form.find('#methodType').val();
          var params = $form.serializeObject();
          
          if(!params.hasOwnProperty("adminMenuYn")){
          	params.adminMenuYn = false;
          }
          if(!params.hasOwnProperty("storeMenuYn")){
          	params.storeMenuYn = false;
          }
          
          $.ajax({
            url : modelUrl,
            type: 'POST',
            data : {
              "_method" : methodType,
              "data" : JSON.stringify(params)
            },
            success : function(response){
              if(response.success){
                target.html('');
                view.renderTree();
              }
              else{
                alert("error : " + response.errMsg);
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
    renderTree : function(){
      var view = this;

        $.ajax({
          url : modelUrl,
          method: 'GET',
          success : function(result){
            if(result.success){
              view.init_treeview(result.list);
            }
          }
         });
    }

  });

  return ContentView;
});