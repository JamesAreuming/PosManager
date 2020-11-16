/*
 * Filename	: dashboard.js
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

  'text!templates/sysmanage/menuManage.html',
  'i18n!templates/globals/lang/nls/language',
  
  'text!templates/sysmanage/modal/menu_form.html',
  'text!templates/sysmanage/modal/modal_form_alert.html',
  
  'bootstrap-treeview',
  'form-select2'
  
  /*
  'text!templates/sysmanage/modal/basecode_modal_sub.html',
  
  */
], function (Backbone, Mustache, template, i18n, menu_form/*, sub_modal*/, modalAlert) {
  'use strict';
  
  var tree;
  
  var modelUrl = './model/BackOfficeMenu';
  
  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    /*events: {
    	'click button.basecode-main-new' : 'modal_init',
    	'click button.basecode-sub-new' : 'sub_init',
    	'click #main_code button.corp-detail' : 'load_detail_main',
    	'click #sub_code button.corp-detail' : 'load_detail_sub'
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
    	    
      view.renderTree();
      
      form_select.init();

      return view;
    },
    init_treeview : function(list){
    	var view = this;
    	$('#tree_view').treeview({
    	  data: list,         // data is not optional
    	  levels: 2,
    	  onNodeSelected: function(event, data) {
    		  $.ajax({
    			  url : modelUrl,
    			  method : 'get',
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
    setform : function(data){
    	var target = $('#tree_form');
    	var view = this;
    	
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
	        	var params = $form.serializeObject();

	        	$.ajax({
	        		url : modelUrl,
	        		type: 'POST',
	        		data : {
	        			"_method" : 'PUT',
	        			"data" : JSON.stringify(params) 
	        		},
	        		success : function(response){
	        			if(response.success){
	        				target.html('');
	        				view.renderTree();
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
       	 method: 'get',
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