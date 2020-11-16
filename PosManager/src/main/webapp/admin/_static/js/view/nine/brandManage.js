/*
 * Filename	: brandManage.js
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
  
  'text!templates/nine/brandManage.html',
  'text!templates/nine/modal/brandmodal.html',
  'text!templates/common/modal_form_alert.html',

  'bootstrap-datepicker',
  'bootstrap-datepicker-lang-kr'

], function (Backbone, Mustache, Common, i18n, template, modalform, modalAlert) {
  'use strict';

  var _data = {};

  var franId = -1;

  var targetUrl = "./model/nine/Brand";

  var imgUpTargetUrl = "./model/store/ImageUpload";

  var datatables;

  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events: {
    	'click #brand_table button.brand-detail' : 'load_detail',
    	'click button#zipcode_bt' : 'isAddress',
    	'click button#search' : 'search',
    	'click button#new-brand' : 'modal_init',
    	'change #mainImage' : 'mainImagePreView',
    	'keydown input[type="number"]' : 'onlyNumber',
    	'keyup input[type="number"]' : 'removeChar'
    },
    initialize: function () {
      this.trigger('remove-event');
      this.template = template;
      this.listenTo(this.model, 'sync', this.render);
    },
    render: function () {
      var view = this;
      var data = {};
      data.cd = view.loadBaseCode();
      data.i18n = i18n;
      
      data.frList = (view.model != undefined ? view.model.toJSON()[0].frList : {});
      data.brList = (view.model != undefined ? view.model.toJSON()[0].brList : {});
      
      _data = view.model.toJSON()[0].session.userInfo;
      data.userInfo = view.model.toJSON()[0].session.userInfo;
      
      if(data.frList == undefined || data.frList == ""){
    	  data.isFr = true;
      }else{
    	  data.isFr = false;
      }
      
      if(data.isFr == true && data.brList == undefined || data.brList == ""){
    	  data.isBr = true;
      }else{
    	  data.isBr = false;
      }

      var rendered = Mustache.to_html(view.template, data);
      $(view.el).append(rendered);

      _data.data = JSON.stringify({ franId : _data.franId });
      
      view.datatablesInit();

  	  $('.base-select').select2();
      $('#franchise_select').select2().on('change', function(){

      		franId = $(this).val();

      		_data.data = JSON.stringify({ franId : franId });
      		_data.franId = franId;

      		view.datatablesReload();
      	});

      return view;
    },
    search : function(){
    	var view = this;
    	var $form = $('form');

    	_data = $.extend(_data, $form.serializeObject());

    	$.each(Object.keys(_data), function(idx, _key){
    		if ( _data[_key] == "" || _data[_key] === null || (typeof _data[_key] == "object" && $.isEmptyObject(_data[_key])) ) {
    			delete _data[_key];
    		}
    	});

    	view.datatablesReload();
    },
    loadBaseCode : function(obj){
    	var data = {};
		$.ajax({
			url : './model/Codes',
			method : 'POST',
			async: false,
			data : {
				'codes' : 'SvcSt,RewardTp,StampTp,Currency'
			},
			success : function(result){
				if(result.success){
					$.each(Object.keys(result.codes), function(idx, code){
						$.each(result.codes[code], function(_idx, _code){
							for (var key in obj) {
								if (key.toLowerCase() == code) {
									if(code == "currency"){
										if (obj[key] == _code.title) {
											_code.selections = 'selected="selected"';
											break;
										}
									}else{
										if (obj[key] == _code.baseCd) {
											_code.selections = 'selected="selected"';
											break;
										}
									}									
								}
							}
						});
					});

					data = $.extend(result.codes, data);

				}
				else {
		    		$('#brand-modal').html('');
		    		$('#brand-modal').modal('toggle');
				}
			}
		});
		
		
		return data;
    },
    datatablesInit : function(){
    	datatables = $('#brand_table').DataTable({
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
			  { "data" : "companyNm", "name" : "company_nm" }, 
			  { "data" : "ceoNm", "name" : "ceo_nm" },
			  { "data" : "tel", "name" : "tel" },  
			  { "data" : "svcStNm", "name" : "svc_st_nm", "className": "dt-body-center" },
			  { "data" : "id" }
			],
		    "columnDefs":  [ {
		    	"render": function ( data, type, row ) {
		    		return '<button class="btn btn-primary btn-sm brand-detail" data-toggle="modal" data-target=".bs-brand-modal-lg" target-code="'+data+'">'+i18n.BO0022+'</button>';
		    	},
		    	"bSortable": false,
		    	"targets": 5
		    } ],
	        "order": [[ 0, 'asc' ]],
		    "pageLength": 20,
		    "lengthChange": false,
		    "processing": true,
		    "rowReorder": true,
		    "searching": false
		});

    	/*
    	 * 번호컬럼 값을 1번부터 세팅
    	 */
//    	datatables.on( 'order.dt search.dt', function () {
//			datatables.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
//	            cell.innerHTML = i+1;
//	        });
//    	}).draw();
    },
    datatablesReload : function(){
    	var view = this;
    	if(datatables != undefined){
    		datatables.draw();
    	}
    },
    modal_init : function(){
    	if ($("#franchise_select option:selected").val() == '') {
    		alert(i18n.BO4001);
    		return false;
    	}
    	else {
    		var data = {};
    		data.code = this.loadBaseCode();
	        data.i18n = i18n;
	        data.franId = _data.franId;
    		$('#brand-modal').html(Mustache.to_html(modalform, data));
    		this.form_init('POST', $('#brand-modal'));
			this.selected();
    	}
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
	        		success : function(response) {
	        			if (response.success) {
							var _data = new FormData();
							var _file = $form.find('#mainImage').get(0);

	        				$modal.modal('toggle');
	        				datatables.draw();
	        			}
	        			else {
	        				alert(i18n.BO4018+' : "'+response.errMsg+'"');
	        				return false;
	        			}
	        		}
	        	});
	        	return false;
	        }
		});

		$('.date-picker').datepicker({
			format: "yyyy-mm-dd",
			language: "kr"
		});

		view.on('click button#zipcode_bt', view.isAddress);
    },
    load_detail: function (event) {
    	var view = this;
    	var _this = $(event.target);
    	$.ajax({
    		url : targetUrl,
    		data : {
    			data : JSON.stringify({
    				id : _this.attr("target-code"),
    				franId : _data.franId
    			})
    		},
    		success : function(response){
    			if(response.success){
    				
    				var data = response.list;
    				data.i18n = i18n;
    				data.code = view.loadBaseCode(response.list);
	        		
	        		var _template = Mustache.to_html(modalform, data);
	        		
    				$('#brand-modal').html(Mustache.to_html(_template, view.buildOptions(response.list)));

					if (data.logoImg != null) {
						$('#brand-modal').find('#main_img_container').show();
					}
					view.selected(data);

    				view.form_init('PUT', $('#brand-modal'));
    			} else {
    				$('#brand-modal').modal('toggle');
    			}
    		}
    	});
    },
    selected : function(data) {
    	if (data != null) {
    		var currency = data.currency;
    		$('[name=currency]').val(currency);
    	}

		for (var n = 0; n < 2; n++) {
			var date = new Date(2016,1,1,0,0,0);
			var order;
			var val;
			var str;
			var selected;
			var option = [];
			
			for (var i = 0; i < 48; i++) {
				val = date.format("HHmm");
				str = date.format("HH:mm");
				
				option.push($('<option>', {value: val, text: str}));
				date = new Date(date.getTime() + 30*60000);

		    	if (data != null) {
		    		if (n == 0) {
		    			order = data.orderBegin;
		    			if (order == val) selected = val;
		    		}
		    		else {
		    			order = data.orderEnd;
		    			if (order == val) selected = val;
		    		}
		    	}
			}
			if (n == 0) {
				$('[name=orderBegin]').append(option);
				if (order == selected) $("[name=orderBegin]").val(selected);
			}
			else {
				$('[name=orderEnd]').append(option);
				if (order == selected) $("[name=orderEnd]").val(selected);
			}

		}
    },
    buildOptions : function(object) {
        for (var i in object) {
            object[i.toLowerCase()+ '=' + object[i]] = true;
        }
        return object;
    },
	mainImagePreView : function(event) {
		var _this = $(event.target);
		var ext = _this.val().split('.').pop().toLowerCase(); //확장자

        if($.inArray(ext, ['gif', 'png', 'jpg', 'jpeg']) == -1) {
        	if ($.browser.msie) {
        		// ie 일때 input[type=file] init.
        		_this.replaceWith( $("#filename").clone(true) );
        	} else {
        		// other browser 일때 input[type=file] init.
        		_this.val("");
        	}
            window.alert(i18n.BO4009+'('+i18n.BO4010+')');
        } else {
            var file = _this.prop("files")[0];
            var blobURL = window.URL.createObjectURL(file);

            var _image = $('<img />').attr('src', blobURL);
            $('#brand-modal').find('#main_img_target').html(_image);
			$('#brand-modal').find('#main_img_container').show();
        }
	},
	onlyNumber : function (event){
		var event = event || window.event;
		var keyID = (event.which) ? event.which : event.keyCode;
		if ( (keyID >= 48 && keyID <= 57) || (keyID >= 96 && keyID <= 105) || keyID == 8 || keyID == 46 || keyID == 37 || keyID == 39 ){
			if($(event.target).attr("max") <= event.target.value){
				return false;
			}
			return;
		}else{			
			return false;
		}	
	},
	
	removeChar : function (event) {
		var event = event || window.event;
		var keyID = (event.which) ? event.which : event.keyCode;
		if ( keyID == 8 || keyID == 46 || keyID == 37 || keyID == 39 ){
			return;
		}else{
			event.target.value = event.target.value.replace(/[^0-9]/g, "");			
		}	
	},
  });

  return ContentView;
});