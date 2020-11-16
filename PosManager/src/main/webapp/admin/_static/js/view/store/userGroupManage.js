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
  'common',
  'i18n!templates/sysmanage/nls/sysmanage',
  
  'text!templates/sysmanage/userGroupManage.html',
  'text!templates/sysmanage/modal/userGroup_form.html',
  'text!templates/sysmanage/modal/modal_form_alert.html'
  
], function (Backbone, Mustache, Common, i18n, template, form, modalAlert) {
  'use strict';
  
  var userGroup_tables;
  
  var modelUrl = './model/UserGroup';
  
  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events: {
    	'click #userGroup button.corp-detail' : 'load_detail_main',
    	'click .user-group-new' : 'modal_init'
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

      
	    var _data =  {};
	    
	    userGroup_tables = $('#userGroup').DataTable({
	  		"serverSide": true,
	    	"language": {
	            "url": "//cdn.datatables.net/plug-ins/1.10.11/i18n/English.json"
	        },
	        "ajax" : {
	        	"url" : modelUrl,
	        	"type": 'GET',
		    	"data" : function(data){
		    		return $.extend(data, _data);
		    	},
		        "dataSrc" : 'list'
	        },
		    "columns" : [ 
			  { "data" : "groupCd"}, 
			  { "data" : "title"},
			  { "data" : "groupRole"}, 
			  {
				  "data" : "useYn",
	              "className": "dt-body-center"
	          },
			  { "data" : "dsc" },
			  { "data" : "groupCd"}
			],
		    "columnDefs":  [ {
				"render": function ( data, type, row ) {
					if ( type === 'display' ) {
	                	if(data == 1)
	                		return i18n.BO2031;
	                	else
	                		return i18n.BO2032;
	                }
	                return data;
	            },
		        "targets": 3
			},{
		        "render": function ( data, type, row ) {
		            return '<button class="btn btn-primary btn-sm corp-detail" data-toggle="modal" data-target=".bs-main-modal-lg" target-code="'+data+'" >상세내역</button>'; 
		        },
		        "targets": 5
		    } ],
		    "pageLength": 10,
		    "lengthChange": false,
		    "processing": true,
		    "rowReorder": true,
		    "rowId" : 'groupCd'
		});

      return view;
    },
    load_detail_main: function (event) {
    	var view = this;
    	var _this = $(event.target);
    	
    	$.ajax({
    		url : modelUrl,
    		data : {
    			gruopCd : _this.attr("target-code")
    		},
    		success : function(response){
    			if(response.success){
    				var data = response.list[0];
    				
    				data.disabled = true;
    				
    				$('#form-modal').html(Mustache.to_html(form, view.buildOptions(data)));
    				view.form_init('PUT', $('#form-modal'), userGroup_tables);
    			}
    		}
    	});
    },
    modal_init : function(event){
    	$('#form-modal').html(Mustache.to_html(form, {}));
    	this.form_init('POST', $('#form-modal'), userGroup_tables);
    },
    form_init : function(method, $modal, tables){
    	form_select.init();
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
	        		url : modelUrl,
	        		type: 'POST',
	        		data : {
	        			"_method" : method,
	        			"data" : JSON.stringify(params) 
	        		},
	        		success : function(response){
	        			if(response.success){
	        				$modal.modal('toggle');
	        				tables.draw();
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