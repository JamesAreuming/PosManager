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
  'i18n!templates/globals/lang/nls/language',
  
  'text!templates/management/baseCodeManage.html',
  'text!templates/management/modal/basecode_modal_main.html',
  'text!templates/management/modal/basecode_modal_sub.html',
  'text!templates/common/modal_form_alert.html'
  
], function (Backbone, Mustache, Common, i18n, template, main_modal, sub_modal, modalAlert) {
  'use strict';

  var main_tables;
  var sub_tables;

  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events: {
    	'click button.basecode-main-new'		: 'modal_init',
    	'click button.basecode-sub-new'			: 'sub_init',
    	'click #main_code button.corp-detail'	: 'load_detail_main',
    	'click #sub_code button.corp-detail'	: 'load_detail_sub'
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

      //Index.init(data);
      //Index.initCharts(); // init index page's custom scripts

	    var _data =  {};
	    var _data_sub =  {};

	    main_tables = $('#main_code').DataTable({
	  		"serverSide": true,
	    	"language": {
	            "url": "//cdn.datatables.net/plug-ins/1.10.11/i18n/English.json"
	        },
	        "ajax" : {
	        	"url" : './model/management/BaseCodeMain',
	        	"type": 'GET',
		    	"data" : function(data){
		    		return $.extend(data, _data);
		    	},
		        "dataSrc" : 'list'
	        },
		    "columns" : [
			  { "data" : "mainCd"},
			  { "data" : "title"},
			  { "data" : "mdlCd"},
			  { "data" : "grpNm"},
			  { "data" : "sortBc"},
			  {
				  "data" : "useYn",
	              "className": "dt-body-center"
	          },
			  { "data" : "dsc" },
			  { "data" : "mainCd"}
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
		        "targets": 5
			},{
		        "render": function ( data, type, row ) {
		            return '<button class="btn btn-primary btn-sm corp-detail" data-toggle="modal" data-target=".bs-main-modal-lg" target-code="'+data+'" >'+i18n.BO0022+'</button>';
		        },
		        "targets": 7		    } ],
		    "pageLength": 10,
		    "lengthChange": false,
		    "processing": true,
		    "rowReorder": true,
		    "rowId" : 'mainCd'
		});

	    sub_tables = $('#sub_code').DataTable({
	  		"serverSide": true,
	    	"language": {
	            "url": "//cdn.datatables.net/plug-ins/1.10.11/i18n/English.json"
	        },
	        "ajax" : {
	        	"url" : './model/management/BaseCode',
	        	"type": 'GET',
		    	"data" : function(data){
		    		return $.extend(data, _data_sub);
		    	},
		        "dataSrc" : 'list'
	        },
		    "columns" : [
		      { "data" : "baseCd"},
			  { "data" : "subCd"},
			  { "data" : "title"},
			  {
				  "data" : "useYn",
	              "className": "dt-body-center"
	          },
			  { "data" : "dsc" },
			  { "data" : "alias" },
			  { "data" : "baseCd"}
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
		            return '<button class="btn btn-primary btn-sm corp-detail" data-toggle="modal" data-target=".bs-sub-modal-lg" target-code="'+data+'" target-id="'+row.id+'">'+i18n.BO0022+'</button>';
		        },
		        "targets": 6
		    } ],
		    "pageLength": 10,
		    "lengthChange": false,
		    "processing": true,
		    "rowReorder": true
		});

	  	$('#main_code').on( 'click', 'tbody > tr', function () {
	        if ( $(this).hasClass('selected') ) {
	            $(this).removeClass('selected');
	        }
	        else {
	        	main_tables.$('tr.selected').removeClass('selected');
	            $(this).addClass('selected');

	            _data_sub.data =  JSON.stringify({mainCd :main_tables.row(this).data().mainCd});
	            sub_tables.draw();
	        }
	    } );


      return view;
    },
    load_detail_main: function (event) {
    	var view = this;
    	var _this = $(event.target);

    	$.ajax({
    		url : "./model/management/BaseCodeMain",
    		data : {
    			data : JSON.stringify({mainCd : _this.attr("target-code")})
    		},
    		success : function(response){
    			if(response.success){
    				var data = response.list[0];
    				var obj = { disable : true };
    				data.i18n = i18n;
    				data = $.extend(data,  obj);

    				$('#main-modal').html(Mustache.to_html(main_modal, view.buildOptions(data)));
    				view.form_init('PUT', $('#main-modal'), main_tables);
    			}
    		}
    	});
    },
    load_detail_sub: function (event) {
    	var view = this;
    	var _this = $(event.target);
    	$.ajax({
    		url : "./model/management/BaseCode",
    		data : {
    			baseCd : _this.attr("target-code")
    		},
    		success : function(response){
    			if(response.success){
    				var data = response.list[0];
    				var obj = { disable : true };
    				data.i18n = i18n;
    				data = $.extend(data,  obj);

    				$('#sub-modal').html(Mustache.to_html(sub_modal, view.buildOptions(data)));
    				view.form_init('PUT', $('#sub-modal'), sub_tables);
    			}
    		}
    	});
    },
    modal_init : function(event){
    	var data = {};
    	data.i18n = i18n;
    	
    	$('#main-modal').html(Mustache.to_html(main_modal, data));
    	this.form_init('POST', $('#main-modal'), main_tables);
    },
    sub_init : function(){
    	if(main_tables.$('tr.selected').length == 1){
    		
    		var data = {};
        	data.i18n = i18n;
        	data.mainCd = main_tables.row(main_tables.$('tr.selected')).data().mainCd;
        	data.mainId = main_tables.row(main_tables.$('tr.selected')).data().id;
        	
        	$('#sub-modal').html(Mustache.to_html(sub_modal, data));
        	this.form_init('POST', $('#sub-modal'),sub_tables);
    	} else {
    		alert(i18n.BO4057);
    		$('#sub-modal').modal('toggle');
    		$('#sub-modal').html('');
    	}
    },
    sub_modal : function(){
    	alert('shown');
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
	        		url : $form.attr('action'),
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