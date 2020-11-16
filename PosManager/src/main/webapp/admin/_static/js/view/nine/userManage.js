/*
 * Filename	: userManage.js
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
  'i18n!templates/nine/nls/store',
  
  'text!templates/nine/userManage.html',
  'text!templates/nine/modal/user_modal.html',
  'text!templates/nine/modal/modal_form_alert.html'

], function (Backbone, Mustache, Common, i18n, template, modalform, modalAlert) {
  'use strict';

  var targetUrl = './model/store/User';

  var main_tables;

  var _data = {};

  var _def_form_mustache =
  {
    'id' : '{{id}}',
    'brandId' : '{{brandId}}',
    'name': '{{name}}',
    'username' : '{{username}}',
    'userGroupId' : '{{userGroupId}}',
    'fax' : '{{fax}}',
    'mb' : '{{mb}}',
    'email' : '{{email}}',
    'used_t' : '{{#used=true}} selected="selected"{{/used=true}}',
    'used_f' : '{{#used=false}} selected="selected"{{/used=false}}'
  };

  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events : {
    	'click #new-user' : 'modal_init',
    	'click #user-tables button.user-detail' : 'load_detail'
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

      view.datatablesInit();

      return view;
    },
    datatablesInit : function(){
    	main_tables = $('#user-tables').DataTable({
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
			  { "data" : "id"},
			  { "data" : "username"},
			  { "data" : "name"},
			  { "data" : "tel"},
			  { "data" : "status" },
			  { "data" : "id"}
			],
		    "columnDefs":  [ {
		        "render": function ( data, type, row ) {
		            return '<button class="btn btn-primary btn-sm user-detail" data-toggle="modal" data-target=".bs-user-modal-lg" target-code="'+data+'" >상세내역</button>';
		        },
		        "targets":6
		    } ],
		    "pageLength": 10,
		    "lengthChange": false,
		    "processing": true,
		    "rowReorder": true,
		    "rowId" : 'id'
		});
    },
    selectedInit : function (){
    	$('#brand_select').select2();

        $('#franchise_select').select2()
        	.on('change', function(){
        		_data.franId = $(this).val();
        		$.ajax({
        			url : './model/nine/Brand',
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
//	  	  						main_tables.draw();
    				        });
        				}
        			}
        		})
        	});
    },
    modal_init : function(){
    	var view = this;
		$('#user-modal').html(Mustache.to_html(modalform, view.loadBaseCode(false)));
		view.form_init('POST', $('#user-modal'));
    },
    form_init : function(method, modal){
    	var view = this;

    	modal.find('select').select2();

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
	    		    		$('#coupon-modal').modal('toggle');
	    		    		main_tables.draw();
	        			}
	        		}
	        	});

	        	return false;
	        }
		});

    },
    load_detail : function(event){
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
    				var data = view.loadBaseCode(true);
    				var _template = Mustache.to_html(modalform, $.extend(data, _def_form_mustache));

    				$('#user-modal').html(Mustache.to_html(_template, view.buildOptions(response.list)));
    				view.form_init('PUT', $('#user-modal'));
    			}
    		}
    	});
    },
    buildOptions : function(object) {
        for (var i in object) {
            object[i.toLowerCase() + '=' + object[i]] = true;
        }
        return object;
    },
    loadBaseCode : function(option){
    	var data = {};

    	$.ajax({
			url : './model/nine/Brand',
			data : {
				data : JSON.stringify({ franId : _data.franId })
			},
			async : false,
			success : function(result){
				if(result.success){
					if(option){
						$.each(result.list, function(idx, code){
							code.selections = '{{#brandId='+code.id+'}} selected="selected" {{/brandId='+code.id+'}}';
						});
					}
					data.brand = result.list;
				}
			}
		});

    	$.ajax({
			url : './model/UserGroup',
			async : false,
			success : function(result){
				if(result.success){
					if(option){
						$.each(result.list, function(idx, code){
							code.selections = '{{#userGroupId='+code.id+'}} selected="selected" {{/userGroupId='+code.id+'}}';
						});
					}
					data.userGroup = result.list;
				}
			}
		});

    	return data;
    }
  });

  return ContentView;
});