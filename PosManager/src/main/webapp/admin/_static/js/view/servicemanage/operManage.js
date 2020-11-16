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

  'text!templates/servicemanage/operManage.html',
  'i18n!templates/globals/lang/nls/language',
  
  'text!templates/servicemanage/modal/stamp_form.html'
  
], function (Backbone, Mustache, template, i18n, stampForm) {
  'use strict';
  
  var _data = {};
  
  var targetUrl = './model/Stamp';
  
  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events : {
    	'click #search_oper' : 'search_oper'
    },
    initialize: function () {
      this.template = template;
      this.listenTo(this.model, 'sync', this.render);
    },
    render: function () {
      var view = this;
      var data = {};
      data.fr = (view.model != undefined ? view.model.toJSON()[0] : {});
      data.i18n = i18n;
      var rendered = Mustache.to_html(view.template, data);
      $(view.el).append(rendered);

      view.selectedInit();
      
      return view;
    },
    selectedInit : function (){
    	$('#brand_select').select2();
        
        $('#franchise_select').select2()
        	.on('change', function(){
        		$.ajax({
        			url : './model/Brand',
        			data : {
        				data : JSON.stringify({ franId : $(this).val() })
        			},
        			success : function(result){
        				if(result.success){
        					var defualt_array = [{id: '', brandNm : i18n.BO1032}];
        					
        					$("#brand_select").find('option').remove();
        					
        					$('#brand_select').select2({
        						data : defualt_array.concat(result.list),
        						templateResult : function(state) {
        							return state.brandNm;
        						},
        						templateSelection : function(data, container) {
        						    return data.brandNm;
        						}
        					}).on('change', function(){
	  	  						_data.data = JSON.stringify({brandId : $(this).val()});
	  	  						_data.brandId = $(this).val();
    				        });
        				}
        			}
        		})
        	});
    },
    search_oper : function (){
    	var view = this;
    	$.ajax({
    		url : targetUrl,
    		data : _data,
    		success : function(result){
    			if(result.success){
    				$('#stamp-form-body').html(Mustache.to_html(stampForm, view.buildOptions(result.data)));
    				view.form_init('PUT');
    			} else {
    				$('#stamp-form-body').html(Mustache.to_html(stampForm, {brandId : _data.brandId}));
    				view.form_init('POST');
    			}
    		}
    	})
    },
    form_init : function(method){
    	var view = this;
    	
    	$('#stamp-form-body').find('select').select2();
    	
		$.validate({
			validateOnBlur : false, 
		    scrollToTopOnError : false,
		    showHelpOnFocus : false,
		    errorMessagePosition : $('.form-validatoin-alert-detail'),
		    addSuggestions : false,
		    onError : function($form) {
		    	var _target = $("modal-body");

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
	        				view.search_oper();
	        			}
	        		}
	        	});
        		
	        	return false;
	        }
		});

//		view.load_map();
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