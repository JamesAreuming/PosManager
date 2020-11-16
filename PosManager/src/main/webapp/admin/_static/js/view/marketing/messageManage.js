/*
 * Filename	: messageManage.js
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
  'i18n!templates/globals/lang/nls/language',
  
  'text!templates/marketing/messageManage.html',
  'text!templates/marketing/modal/message_modal.html',
  'text!templates/common/modal_form_alert.html',

  'bootstrap-daterangepicker'

], function (Backbone, Mustache, Common, i18n, template, messageModal, modalAlert) {
  'use strict';

  var _data = {};

  var _def_form_mustache =
	{
	  'id' : '{{id}}',
	  'brandId' : '{{brandId}}',
	  'messageNm': '{{messageNm}}',
	  'term' : '{{term}}',
	  'begin' : '{{begin}}',
	  'expire' : '{{expire}}',
	  'discount' : '{{discount}}',
	  'discountLimit' : '{{discountLimit}}',
	  'targetMenuId' : '{{targetMenuId}}',
	  'supplyMenuId' : '{{supplyMenuId}}',
	  'used_t' : '{{#used=true}} selected="selected"{{/used=true}}',
	  'used_f' : '{{#used=false}} selected="selected"{{/used=false}}',
	  'hasUseLimit_t' : '{{#hasuselimit=true}} selected="selected"{{/hasuselimit=true}}',
	  'hasUseLimit_f' : '{{#hasuselimit=false}} selected="selected"{{/hasuselimit=false}}'
	};

  var targetUrl = './model/marketing/Message';

  var main_tables;

  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events : {
    	'click #new-message' : 'modal_init',
    	'click #message_table button.message-detail' : 'load_detail'/*,
    	'click #search_oper' : 'search_oper'*/
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
    selectedInit : function (){
    	$('#brand_select').select2();

        $('#franchise_select').select2()
        	.on('change', function(){
        		$.ajax({
        			url : './model/store/Brand',
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
	  	  						main_tables.draw();
    				        });
        				}
        			}
        		})
        	});
    },
    modal_init : function(){
    	var view = this;
    	if(_data.brandId != undefined && _data.brandId != ''){
    		var data = view.loadBaseCode();

			$('#message-modal').html(Mustache.to_html(messageModal, data));
			view.form_init('POST', $('#message-modal'));

    	} else {
    		alert(i18n.BO4002);
    		$('#message-modal').html('');
    		$('#message-modal').modal('toggle');
    	}
    },
    datatablesInit : function(){
    	main_tables = $('#message_table').DataTable({
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
			  { "data" : "messageNm"},
			  { "data" : "discountTp"},
			  { "data" : "discount"},
			  {
				  "data" : "used",
	              "className": "dt-body-center"
	          },
			  { "data" : "term" },
			  { "data" : "id"}
			],
		    "columnDefs":  [ {
		        "render": function ( data, type, row ) {
		            return '<button class="btn btn-primary btn-sm message-detail" data-toggle="modal" data-target=".bs-message-modal-lg" target-code="'+data+'" >상세내역</button>';
		        },
		        "targets": 7
		    } ],
		    "pageLength": 10,
		    "lengthChange": false,
		    "processing": true,
		    "rowReorder": true,
		    "rowId" : 'id'
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
    				var data = view.loadBaseCode();
    				var _template = Mustache.to_html(messageModal, $.extend(data, _def_form_mustache));

    				$('#message-modal').html(Mustache.to_html(_template, view.buildOptions(response.data)));
    				view.form_init('PUT', $('#message-modal'));

    			}
    		}
    	});
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
	    		    		$('#message-modal').modal('toggle');
	    		    		main_tables.draw();
	        			}
	        		}
	        	});

	        	return false;
	        }
		});

		$('#timepicker1').daterangepicker({
			autoApply : true,

			locale: {
	              format: 'YYYY-MM-DD',
	              separator: ' - ',
	              applyLabel: i18n.BO0023,
	              cancelLabel: i18n.BO0026,
	              fromLabel: i18n.BO3032,
	              toLabel: i18n.BO3033,
	              daysOfWeek: [i18n.BO3031, i18n.BO3025, i18n.BO3026, i18n.BO3027, i18n.BO3028, i18n.BO3029, i18n.BO3030],
	              monthNames: [i18n.BO3013, i18n.BO3014, i18n.BO3015, i18n.BO3016, i18n.BO3017, i18n.BO3018, i18n.BO3019, i18n.BO3020, i18n.BO3021, i18n.BO3022, i18n.BO3023, i18n.BO3024],
	              firstDay: 1
	       }
		},
		function(start, end, label) {
		});

//		view.load_map();
    },
    loadBaseCode : function(){
    	var data = {};
		$.ajax({
			url : './model/management/Codes',
			method : 'POST',
			async: false,
			data : {
				'codes' : 'DiscountTp,ExpireTp'
			},
			success : function(result){
				if(result.success){

					$.each(Object.keys(result.codes), function(idx, code){
						$.each(result.codes[code], function(_idx, _code){
							_code.selections = '{{#'+code+'='+_code.baseCd+'}} selected="selected" {{/'+code+'='+_code.baseCd+'}}';
						});
					});

					data = $.extend(result.codes, _data);
				} else {
		    		$('#message-modal').html('');
		    		$('#message-modal').modal('toggle');
				}
			}
		});

		return data;
    },
    buildOptions : function(object) {
        for (var i in object) {
            object[i.toLowerCase() + '=' + object[i]] = true;
        }
        return object;
    }
  });

  return ContentView;
});