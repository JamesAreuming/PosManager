/*
 * Filename	: stampDetailManage.js
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
  
  'text!templates/reward/stampDetailManage.html',
  'text!templates/reward/modal/stampDetail_list_modal.html',

  'bootstrap-daterangepicker'

], function (Backbone, Mustache, Common, i18n, template, modal) {
  'use strict';

  var targetUrl = './model/reward/StampLog';

  var _data = {};

  var main_tables;

  var ContentView = Backbone.View.extend({
	    el: $('#main-wrapper'),
	    events : {
	    	'click button#stampList-search' : 'stampListSearch',
	    	'click #stamp-table button.stamp-lists' : 'load_detail',
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
	      
	      data.frList = (view.model != undefined ? view.model.toJSON()[0].frList : {});
	        data.brList = (view.model != undefined ? view.model.toJSON()[0].brList : {});
	        data.stList = (view.model != undefined ? view.model.toJSON()[0].stList : {});

	        _data.userInfo = view.model.toJSON()[0].session.userInfo;
	        
	        _data.franId = _data.userInfo.franId;
	        _data.brandId = _data.userInfo.brandId;
	        _data.storeId = _data.userInfo.storeId;
	        
	        data.userInfo = view.model.toJSON()[0].session.userInfo;
	        data.i18n = i18n;

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
	        
	        if(data.frList == undefined && data.brList == undefined && data.stList == undefined){
	        	data.isSt = true;
	        }else{
	        	data.isSt = false;
	        }
	      
	      var rendered = Mustache.to_html(view.template, data);
	      $(view.el).append(rendered);

	      view.setCalendarArea();
	      
	      view.selectedRollInit();

	      view.datatablesInit();

	      return view;
	    },
	    
	    setCalendarArea : Common.JC_calendar.searchRange(i18n),
	    
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
		        	"type": 'POST',
			    	"data" : function(data){
			    		data._method = 'GET';
			    		
			    		// 검색폼 정보
			    		data.data = $('#be-search').serializeObject();
    		    		
			    		// 프랜차이즈 ID
    		    		data.data.franId = _data.franId;
    		    		// 브랜드 ID
    		    		data.data.brandId = _data.brandId;
    		    		// 스토어 ID
    		    		data.data.storeId = _data.storeId;
    		    		
    		    		data.data = JSON.stringify(data.data);
    		    		data.userInfo = JSON.stringify(_data.userInfo);

    			    	$.each(Object.keys(data), function(idx, key){
    			    		if ( data[key] == "" || data[key] === null || (typeof data[key] == "object" && $.isEmptyObject(data[key])) ) {
    			    			delete data[key];
    			    		}
    			    	});
    			    	
			    		return data;
			    	},
			        "dataSrc" : 'list'
		        },
			    "columns" : [
			      { "data" : "id", "name":"ID"},
				  { "data" : "insDate", "name":"INS_DATE"
					  , "render": function (data, type, row) {
		                  return Common.JC_format.day(data);
		              }
				  },
				  { "data" : "insTime", "name":"INS_TIME"},
				  { "data" : "storeNm", "name":"STORE_NM"},
				  { "data" : "barcode", "name":"BARCODE"},
				  { "data" : "mb", "name":"MB"},
				  { "data" : "stampSt", "name":"STAMP_ST", "visible": false},
				  { "data" : "stampStNm", "name":"STAMP_ST_NM"},
				  { "data" : "stampTp", "name":"STAMP_TP", "visible": false},
				  { "data" : "stampTpNm", "name":"STAMP_TP_NM"},
				  { "data" : "id"}
				],
				"columnDefs":  [ {
			        "render": function ( data, type, row ) {
			            return '<button class="btn btn-primary btn-sm stamp-lists" data-toggle="modal" data-target=".bs-stamp-list-modal-lg" target-code="'+data+'">'+i18n.BO0022+'</button>';
			        },
			        "bSortable": false,
			        "targets": 10
			    } ],
			    "order": [[ 0, 'asc' ]],
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
	    				var data = response.data[0];
	    				data.i18n = i18n;
	    				data = Common.JC_format.handleData(data);
	    		    	$('#stamp-lists').html(Mustache.to_html(modal, data));
	    			}
	    		}
	    	});
	    },
	    selectedRollInit : function (){
	    	var view = this;

	    	$('select').select2({
	    		width: '100%',
	            minimumResultsForSearch: -1
	    	});
	    	
	    	$('#franchise_select').select2().on('change', function(){
	    		_data.franId = $(this).val();
	      		$.ajax({
	      			url : './model/Brand',
	      			data : {
	      				data : JSON.stringify({ franId : $(this).val() })
	      			},
	      			success : function(result){
	      				if(result.success){
	      					// 브랜드ID, 스토어ID 전역변수 초기화 
	      					_data.brandId = '';
	      		      		_data.storeId = '';
	      		      		
	      					// 브랜드 콤보 초기화
	      					var brand_default = [{id: '', brandNm : i18n.BO1032}];
	      					$("#brand_select").find('option').remove();
	      					$('#brand_select').select2({
	      						data : brand_default.concat(result.list),
	      						templateResult : function(state) {
	      							return state.brandNm;
	      						},
	      						templateSelection : function(data, container) {
	      						    return data.brandNm;
	      						}
	      					});
	      					
	      					// 스토어 콤보 초기화
	      					var store_default = [{id: '', storeNm : i18n.BO1033}];
	      					$("#store_select").find('option').remove();
	      					$('#store_select').select2({
	      						data : store_default.concat(result.list),
	      						templateResult : function(state) {
	      							return state.storeNm;
	      						},
	      						templateSelection : function(data, container) {
	      						    return data.storeNm;
	      						}
	      					});
	      				}
	      			}  //END : brand select result
	      		});  //END : brand select ajax
	      		view.stampListSearch();
	      	});  //END : franchise select change
	    	
	    	
	    	$('#brand_select').select2().on('change', function(){
				_data.brandId = $(this).val();
				$.ajax({
	      			url : './model/Store',
	      			data : {
	      				data : JSON.stringify({ brandId : $(this).val() })
	      			},
	      			success : function(result){
	      				if(result.success){
	      					// 스토어ID 전역변수 초기화 
	      		      		_data.storeId = '';
	      		      		
	      					// 스토어 콤보 초기화
	      					var store_default = [{id: '', storeNm : i18n.BO1033}];
	      					$("#store_select").find('option').remove();
	      					$('#store_select').select2({
	      						data : store_default.concat(result.list),
	      						templateResult : function(state) {
	      							return state.storeNm;
	      						},
	      						templateSelection : function(data, container) {
	      						    return data.storeNm;
	      						}
	      					});
	      				}
	      			}  //END : store select result
	      		});  //END : store select ajax
				view.stampListSearch();
	        });  //END : brand select change
	    	
	    	$('#store_select').select2().on('change', function(){
				//브랜드와 해당 매장에 관련된 카테고리 분류정보 셋팅
	    		_data.data = JSON.stringify({storeId : $(this).val()});
				_data.storeId = $(this).val();
				view.stampListSearch();
	        });
	    },
	    loadBaseCode : function(){
	    	var data = {};
			$.ajax({
				url : './model/management/Codes',
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
	    },
	    trigger_calender : function(event){
	    	var _this = $(event.target);
	    	var _cal = _this.closest('div').find('.date-picker');
	    	_cal.trigger('click');    	
	    }
	  });

	  return ContentView;
	});