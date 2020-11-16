/*
 * Filename	: posLicenseManage.js
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
  
  'text!templates/nine/posLicenseManage.html',  
  'text!templates/nine/modal/posLicense_form.html',
  'text!templates/nine/modal/posLicense_csv.html',
  'text!templates/nine/modal/modal_form_alert.html'

], function (Backbone, Mustache, Common, i18n, template, modalform, modalcsv, modalAlert) {
  'use strict';

  var targetUrl = './model/nine/PosLicense';

  var _data = {};

  var datatables;

  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events : {
    	'click button.posLicense-detail' : 'load_detail',
    	'click button#new-posLicense' : 'modal_init',
		'click button#del-posLicense' : 'del_posLicense',
		'click button#csv-posLicense' : 'modal_csv',
		'click button#csvUp' : 'csvUp',
    	'click button#search' : 'search',
    	'click .add-on-datepicker' : 'trigger_calender'
    },
    initialize: function () {
      this.template = template;
      this.listenTo(this.model, 'sync', this.render);
    },
    render: function () {
      var view = this;
      var data = {};
      data.cd = view.loadBaseCode();
      data.fr = (view.model != undefined ? view.model.toJSON()[0] : {});
      data.i18n = i18n;

      var rendered = Mustache.to_html(view.template, data);
      $(view.el).append(rendered);

      view.selectDecoInit();

      view.datatablesInit();
      
      return view;
    },
    selectDecoInit : function (){
    	$('[name=deviceTpSearch]').select2();
    	$('[name=statusSearch]').select2();
    },
    datatablesInit : function(){
    	datatables = $('#posLicense-table').DataTable({
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
              { "data" : "id", "name" : "id"},
			  { "data" : "licenseKey", "name" : "license_key"},
			  { "data" : "deviceTpNm", "name" : "device_tp_nm"},
			  { "data" : "begin", "name" : "end"},
			  { "data" : "statusNm", "name" : "status_nm"},
			  { "data" : "storeNm", "name" : "store_nm"},
			  { "data" : "userNm", "name" : "user_nm"},
			  { "data" : "id"}
			],
		    "columnDefs":  [ {
		        "render": function ( data, type, row ) {
		            return '<button class="btn btn-primary btn-sm posLicense-detail" data-toggle="modal" data-target=".bs-posLicense-modal-lg" target-code="'+data+'" >'+i18n.BO0022+'</button>';
		        },
		        "bSortable": false,
		        "targets": 7
		    },{
		    	"render" : function (data, type, row) {
		    		return Common.JC_format.day(row.begin) + " ~ "+ Common.JC_format.day(row.end);
		    	},
		    	"targets" : 3
		    }],
	        "order": [[ 1, 'asc' ]],
		    "pageLength": 10,
		    "lengthChange": false,
		    "processing": true,
		    "rowReorder": true,
		    "searching": false
		});
    	
//    	datatables.on( 'order.dt search.dt', function () {
//    		datatables.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
//    			cell.innerHTML = i+1;
//    		} );
//    	} ).draw();
    },
    modal_init : function(){
		var data = this.loadBaseCode();
	    data.i18n = i18n;

    	$('#posLicense-modal').html(Mustache.to_html(modalform, data));
    	this.form_init('POST', $('#posLicense-modal'));
    },
    load_detail: function (event) {
    	var view = this;
    	var _this = $(event.target);
    	$.ajax({
    		url : targetUrl,
    		data : {
    			data : JSON.stringify({
    				id : _this.attr("target-code")
    			})
    		},
    		success : function(response){
    			if(response.success){
    				var data = response.list[0];
    			    data.i18n = i18n;
    			    data = Common.JC_format.handleData(data);
    			    
	        		var code = view.loadBaseCode(data);
    				var _template = Mustache.to_html(modalform, $.extend(code, data));

    				$('#posLicense-modal').html(Mustache.to_html(_template, view.buildOptions(data)));

    				view.form_init('PUT', $('#posLicense-modal'));
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
	        				$modal.modal('toggle');
	        				datatables.draw();
	        			} else {
	        				alert(response.errMsg);
	        			}
	        		}
	        	});

	        	return false;
	        }
		});

		view.setCalendarArea();
    },
    
    setCalendarArea : Common.JC_calendar.searchRange(i18n, true),
    
    modal_csv : function(){
    	$('#posLicense-csv').html(Mustache.to_html(modalcsv, {}));
    },
    csvUp: function () {
    	var file = $('[name=csv]').val();
    	var fileExt = file.substring(file.lastIndexOf('.') + 1).toLowerCase();

    	if (file == '' || fileExt != 'csv') {
    		alert(i18n.BO4012);
    		return false;
    	}
        if (confirm(i18n.BO4013)) {
            $('#csvUpForm').attr('action', './model/nine/PosLicenseCsv');
       		var options = {
       			success : function(response) {
        			if(response.success) {
        				alert('CSV Upload Success!');
    		    		$('#posLicense-csv').html('');
    		    		$('#posLicense-csv').modal('toggle');
        				datatables.draw();
        			}
        			else {
        				alert(i18n.BO4061 + response.errMsg);
        			}
       			},
       			type : 'POST'
       		};
       		$('#csvUpForm').ajaxSubmit(options);
        }
    },
    loadBaseCode : function(obj){
    	var data = {};
		$.ajax({
			url : './model/Codes',
			method : 'POST',
			async: false,
			data : {
				'codes' : '876,354'
			},
			success : function(result){
				if(result.success){

					$.each(Object.keys(result.codes), function(idx, code){
						$.each(result.codes[code], function(_idx, _code){
							for (var key in obj) {
								if (obj[key] == _code.baseCd) {
									_code.selections = 'selected="selected"';
									break;
								}
							}
						});
					});

					data = $.extend(result.codes, _data);
				}
				else {
		    		$('#posLicense-modal').html('');
		    		$('#posLicense-modal').modal('toggle');
				}
			}
		});

		return data;
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
    	if(datatables != undefined){
    		datatables.draw();
    	}
    },
    del_posLicense : function() {
        if (confirm(i18n.BO4015)) {
        	this.form_init('DELETE', $('#posLicense-modal'));
        }
        else {
        	return false;
        }
    },
    buildOptions : function(object) {
        for (var i in object) {
            object[i + '=' + object[i]] = true;
        }
        return object;
    },
    trigger_calender : function(event){
    	var _this = $(event.target);
    	var _cal = _this.closest('div').find('.date-picker');
    	_cal.trigger('click');
    }
  });

  return ContentView;
});