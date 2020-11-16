/*
 * Filename	: beaconManage.js
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
  
  'text!templates/store/beaconManage.html',
  'text!templates/store/modal/beacon_form.html',
  'text!templates/store/modal/beacon_csv.html',
  'text!templates/store/modal/modal_form_alert.html'

], function (Backbone, Mustache, Common, i18n, template, modalform, modalcsv, modalAlert) {
  'use strict';

  var targetUrl = './model/store/Beacon';

  var _data = {};

  var datatables;

  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events : {
    	'click button.beacon-detail' : 'load_detail',
    	'click button#new-beacon' : 'modal_init',
    	'click button#dsuse-beacon' : 'dsuse_beacon',
		'click button#del-beacon' : 'del_beacon',
		'click button#csv-beacon' : 'modal_csv',
		'click button#csvUp' : 'csvUp',
    	'click button#search' : 'search'
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
      data.fr = (view.model != undefined ? view.model.toJSON()[0] : {});
      data.i18n = i18n;

      var rendered = Mustache.to_html(view.template, data);
      $(view.el).append(rendered);

  	  $('[name=statusSearch]').select2();

      view.datatablesInit();

      return view;
    },
    datatablesInit : function(){
    	datatables = $('#beacon-table').DataTable({
	  		"serverSide": true,
	    	"language": {
	            "url": "//cdn.datatables.net/plug-ins/1.10.11/i18n/English.json"
	        },
	        "ajax" : {
	        	"url" : targetUrl,
	        	"type": 'POST',
		    	"data" : function(data){
		    		data._method = 'GET';
		    		return $.extend(data, _data);
		    	},
		        "dataSrc" : 'list'
	        },
		    "columns" : [
              { "data" : "id", "name" : "id"},
			  { "data" : "uuid", "name" : "uuid"},
			  { "data" : "major", "name" : "major"},
			  { "data" : "minor", "name" : "minor"},
			  { "data" : "statusNm", "name" : "status_nm"},
			  { "data" : "storeNm", "name" : "store_nm"},
			  { "data" : "id"}
			],
		    "columnDefs":  [ {
		        "render": function ( data, type, row ) {
		            return '<button class="btn btn-primary btn-sm beacon-detail" data-toggle="modal" data-target=".bs-beacon-modal-lg" target-code="'+data+'" >'+i18n.BO0022+'</button>';
		        },
		    	"bSortable": false,
		        "targets": 6
		    } ],
	        "order": [[ 1, 'asc' ]],
		    "pageLength": 10,
		    "lengthChange": false,
		    "processing": true,
		    "rowReorder": true,
		    "searching": false
		});

    	/*
    	 * 번호컬럼 값을 1번부터 세팅
    	 */
//    	datatables.on( 'order.dt search.dt', function () {
//    		datatables.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
//    			cell.innerHTML = i+1;
//    		} );
//    	} ).draw();

    },
    modal_init : function(){
		var data = this.loadBaseCode();
	    data.i18n = i18n;
    	$('#beacon-modal').html(Mustache.to_html(modalform, data));
    	this.form_init('POST', $('#beacon-modal'));
    },
    modal_csv : function(){
    	var _data = {};
    	_data.i18n = i18n;
    	$('#beacon-csv').html(Mustache.to_html(modalcsv, _data));
    },
    csvUp: function () {
    	var file = $('[name=csv]').val();
    	var fileExt = file.substring(file.lastIndexOf('.') + 1).toLowerCase();

    	if (file == '' || fileExt != 'csv') {
    		alert(i18n.BO4012);
    		//$('#beacon-csv').prepend(modalAlert);
    		return false;
    	}
        if (confirm(i18n.BO4013)) {
            $('#csvUpForm').attr('action', './model/store/BeaconCsv');
       		var options = {
       			success : function(response) {
        			if(response.success) {
        				alert('CSV Upload Success!');
    		    		$('#beacon-csv').html('');
    		    		$('#beacon-csv').modal('toggle');
        				datatables.draw();
        			}
        			else {
        				alert(i18n.BO4017+' : '+response.errMsg);
        			}
       			},
       			type : 'POST'
       		};
       		$('#csvUpForm').ajaxSubmit(options);
        }
    },
    load_detail: function (event) {
    	var view = this;
    	var _this = $(event.target);
    	$.ajax({
    		url : targetUrl,
    		type : "POST",
    		data : {
    			"_method" : 'GET',
    			data : JSON.stringify({
    				id : _this.attr("target-code"),
    			})
    		},
    		success : function(response){
    			if(response.success){
    				var data = response.list[0];
    			    data.i18n = i18n;
    			    data = Common.JC_format.handleData(data);
    			    
    				$('#beacon-modal').html(Mustache.to_html(modalform, view.buildOptions(data)));
    				view.form_init('PUT', $('#beacon-modal'));
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
	        			}
	        			else {
	        				alert(i18n.BO4016);
	        			}
	        		}
	        	});
	        	return false;
	        }
		});
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
    loadBaseCode : function(obj){
    	var data = {};
		$.ajax({
			url : './model/Codes',
			method : 'POST',
			async: false,
			data : {
				'codes' : '412'
			},
			success : function(result){
				if(result.success){

					$.each(Object.keys(result.codes), function(idx, code){
						$.each(result.codes[code], function(_idx, _code){
							for (var key in obj) {
								if (key.toLowerCase() == code) {
									if (obj[key] == _code.baseCd) {
										_code.selections = 'selected="selected"';
										break;
									}
								}
							}
						});
					});

					data = $.extend(result.codes, _data);
				}
				else {
		    		$('#beacon-modal').html('');
		    		$('#beacon-modal').modal('toggle');
				}
			}
		});

		return data;
    },
    datatablesReload : function(){
    	var view = this;
    	if(datatables != undefined){
    		datatables.draw();
    	}
    },
    dsuse_beacon : function() {
    	if (confirm(i18n.BO4014)) {
	    	$('[name=status]').val('412003');
	    	this.form_init('PUT', $('#beacon-modal'));
    	}
        else {
        	return false;
        }
    },
    del_beacon : function() {
        if (confirm(i18n.BO4015)) {
        	$('[name=status]').val('412004');
        	this.form_init('PUT', $('#beacon-modal'));
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
    }
  });

  return ContentView;
});