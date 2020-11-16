/*
 * Filename	: sidebar.js
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
  'text!templates/base/leftPanel.html',
  'i18n!templates/globals/lang/nls/language'
], function (Backbone, Mustache, template, i18n) {
  'use strict';
  var isShow = true;
  var isClose = false;
  var View = Backbone.View.extend({
    el: $('#leftPanel'),
    initialize: function () {
      this.template = template;
      this.listenTo(this.model, 'sync', this.render);
    },
    initShow: function (show) {
      isShow = show;
    },
    initClose: function (close) {
      isClose = close;
    },
    render: function (menuNavi) {
      var view = this;
      var data = (view.model != undefined ? view.model.toJSON() : {});
      data.i18n = i18n;
      data.menuList = view.loadMenuList();
      data.user = view.userStatus();
      
      $.each(data.menuList, function(_idx, _list){
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
      
      var menuCode = (menuNavi != undefined ? menuNavi.current : "");
      $.each(data.menuList, function(idx, menu) {
        if (menu.menuCd == '010') {
          menu.mainClass = 'active';
          menu.childClass = '';
        }
        else if (menu.menuCd == menuCode) {
          menu.mainClass = 'nav-parent nav-active';
          menu.childClass = 'style="display: block;"';
        }
        else {
          menu.mainClass = 'nav-parent';
          menu.childClass = 'style="display: none;"';
        }
        menu.hasChild = (menu.nodes != null);
      });
      
      var rendered = Mustache.to_html(view.template, data);
      $(view.el).html(rendered);

      if (isClose) {
        $("body").addClass("leftpanel-collapsed");
      } else {
        $("body").removeClass("leftpanel-collapsed");
      }
      if (isShow) {
        $(view.el).show();
      } else {
        $(view.el).hide();
      }

      return view;
    },
    loadMenuList : function(codeValue, defaultValue){
      var data = {};
      $.ajax({
        url : './model/BackOfficeMenu',
        method : 'GET',
        async: false,
        success : function(result){
          if(result.success){
            data = result.list;
          }
        }
      });
      return data;
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