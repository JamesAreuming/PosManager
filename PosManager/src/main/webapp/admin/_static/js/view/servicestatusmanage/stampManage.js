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

  'text!templates/servicestatusmanage/stampManage.html',
  'i18n!templates/globals/lang/nls/language',

  'moment',
  
  'text!templates/servicestatusmanage/modal/stamp_list_modal.html',

  'bootstrap-daterangepicker'
  
], function (Backbone, Mustache, template, i18n, moment, modal) {
  'use strict';
  
  var targetUrl = './model/StampLog'; 
	  
  var _data = {};
  
  var main_tables;
  
  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events : {
    	'click button#stampList-search' : 'stampListSearch',
    	'click #stamp-table button.stamp-lists' : 'load_detail'
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
      
      view.selectedInit();
      
      view.datatablesInit();
      
      $('#daterangepicker').daterangepicker({
    	  "alwaysShowCalendars": true,
    	  "startDate": new Date(),
    	  "endDate": new Date(),
    	  ranges :{
    		  'Today': [moment(), moment()],
              'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
              'Last 7 Days ': [moment().subtract(6, 'days'), moment()],
              'Last 30 Days ': [moment().subtract(29, 'days'), moment()],
              'This Month': [moment().startOf('month'), moment().endOf('month')],
              'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
    	  },
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
			$('#from-date').val(start.format('YYYY-MM-DD'));
			$('#to-date').val(end.format('YYYY-MM-DD') );
		});

      return view;
    },
    stampListSearch : function(){
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
    datatablesInit : function(){
    	main_tables = $("#stamp-table").DataTable({
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
			  { "data" : "insDate"}, 
			  { "data" : "insTime"}, 
			  { "data" : "storeNm"},
			  { "data" : "barcode"},
			  { "data" : "mb"},
			  { "data" : "stampSt"},
			  { "data" : "stampTp"},
			  { "data" : "id"}
			],
			"columnDefs":  [ {
		        "render": function ( data, type, row ) {
		            return '<button class="btn btn-primary btn-sm stamp-lists" data-toggle="modal" data-target=".bs-stamp-list-modal-lg" target-code="'+data+'">상세내역</button>'; 
		        },
		        "targets": 8
		    } ],
		    "pageLength": 10,
		    "lengthChange": false,
		    "processing": true,
		    "rowReorder": true
		});
    },
    datatablesReload : function(){
    	var view = this;
    	if(main_tables != undefined){
    		main_tables.draw();
    		
    	}
    	
    },
    load_detail: function(event){
    	var view = this;
    	var _this = $(event.target);
    	$.ajax({
    		url : targetUrl,
    		data : {
    			id : _this.attr("target-code")
    		},
    		success : function(response){
    			if(response.success){
    		    	$('#stamp-lists').html(Mustache.to_html(modal, response.data[0]));
    			}
    		}
    	});	
    },
    selectedInit : function (){
    	$('#brand_select').select2();
    	
    	$('.base-select').select2();
        
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
//	  	  						main_tables.draw();
    				        });
        				}
        			}
        		})
        	});
        
        $('#store_select').select2({
        	placeholder: "Select a state",
    	    allowClear: true,
        	ajax : {
	    		url : './model/Store',
	    		dataType: 'json',
	    		delay: 250,
	    		data: function (params) {
	    			_data.start = (params.page != undefined ? params.page : 0) * 10;
	    			_data.draw = 'select';
	    			_data['search[value]'] = params.term; 
			      return _data;
			    },
			    processResults: function (data, params) {
			    	var default_array = [{id: '', storeNm : i18n.BO1033}];
			    	
			        params.page = params.page || 1;
			        
			        default_array = default_array.concat(data.list);

			        return {
			          results: default_array,
			          pagination: {
			            more: (params.page * 10) < data.recordsTotal
			          }
			        };
		        },
		        cache: true
		    },
		    escapeMarkup: function (markup) { return markup; }, // let our custom formatter work
		    minimumInputLength: 1,
		    templateResult : function(data){
		    	return data.storeNm;
		    },
		    templateSelection : function(data){
		    	return data.storeNm;
		    }
        });
    },
    loadBaseCode : function(){
    	var data = {};
		$.ajax({
			url : './model/Codes',
			method : 'POST',
			async: false,
			data : {
				'codes' : 'StampLogType,StampType'
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
		    		$('#coupon-modal').html('');
		    		$('#coupon-modal').modal('toggle');
				}
			}
		});
		
		return data;
    }
  });

  return ContentView;
});	