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

  'text!templates/servicemanage/tacManage.html',
  'i18n!templates/globals/lang/nls/language',
  
  'text!templates/servicemanage/modal/terms_modal.html'
  
], function (Backbone, Mustache, template, i18n, modalform) {
  'use strict';
  
  var targetUrl = './model/Terms';
  
  var _data = {};
  
  var main_tables;
  
  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events : {
    	'click button.term-detail' : 'load_detail',
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
			  { "data" : "id"}, 
			  { "data" : "title"}, 
			  { "data" : "isMandatory"}, 
			  { "data" : "version"}, 
			  { "data" : "updated", "type":"date" },
			  { "data" : "id"}
			],
		    "columnDefs":  [ {
		        "render": function ( data, type, row ) {
		            return '<button class="btn btn-primary btn-sm term-detail" data-toggle="modal" data-target=".bs-terms-modal-lg" target-code="'+data+'" >상세내역</button>'; 
		        },
		        "targets": 5	    
		    } ],
		    "pageLength": 10,
		    "lengthChange": false,
		    "processing": true,
		    "rowReorder": true,
		    "rowId" : 'id'
		});
    },
    modal_init : function(){
    	$('#terms-modal').html(Mustache.to_html(modalform, {}));
    	this.form_init('POST', $('#terms-modal'));
    },
    load_detail: function (event) {
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
		    	var _target = $('#brand-modal-form');

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
    buildOptions : function(object) {
        for (var i in object) {
            object[i + '=' + object[i]] = true;
        }
        return object;
    }
  });

  return ContentView;
});	