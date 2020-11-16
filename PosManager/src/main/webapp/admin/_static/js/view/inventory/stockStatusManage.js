/*
 * Filename	: stockStatusManage.js
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
  
  'text!templates/inventory/stockStatusManage.html',
  'text!templates/inventory/modal/supplierModal.html',
  'text!templates/common/modal_form_alert.html',
  
  'bootstrap-daterangepicker'

  ], function (Backbone, Mustache, Common, i18n, template, modalform, modalAlert) {
	  'use strict';

	  var datatables;

	  var targetUrl = './model/inventory/StockStatus';

	  var _data = {};
	  
	  var userType = {};

	  var ContentView = Backbone.View.extend({
	    el: $('#main-wrapper'),
	    events: {
	    	'click button.franchise-detail' : 'load_detail',
	    	'click button#search' 			: 'search',
	    	'click .add-on-datepicker' 		: 'trigger_calender'
	    },
	    initialize: function (options) {
	      this.trigger('remove-event');
	      this.template = template;
	      this.listenTo(this.model, 'sync', this.render);
	    },
	    render: function () {
	      var view = this;
	      var data = {};
	            
	      data.frList = (view.model != undefined ? view.model.toJSON()[0].frList : {});
	      data.brList = (view.model != undefined ? view.model.toJSON()[0].brList : {});
	      data.stList = (view.model != undefined ? view.model.toJSON()[0].stList : {});
	      
	      // admin일 때는 프랜차이즈 list가 존재함
	      if(data.frList == undefined || data.frList == ""){	
	      	  data.isFr = true; 		// 프랜차이즈 관리자일 때
	        }else{
	        	if (data.frList.length > 0) {
	        		data.isAdmin = true;
	        	}
	      	  data.isFr = false;
	        }
	        
	        if(data.isFr == true && data.brList == undefined || data.brList == ""){
	      	  data.isBr = true;		// 브랜드 관리자일 떄
	        }else{
	      	  data.isBr = false;
	        }
	        
	        if((data.frList == '' || data.frList == undefined) && (data.brList == "" || data.brList == undefined)){
	          if (data.stList == '' || data.stList == undefined) {
	      	  	data.isSt = true; 		// 매장 관리자일 때
	        	} else {
	        		data.isSt = false;	
	        	}
	        }else{
	      	  data.isSt = false;
	        }
	      
	       $.extend(userType, data);
	      _data = view.model.toJSON()[0].session.userInfo;
	      data.userInfo = view.model.toJSON()[0].session.userInfo;
	      data.i18n = i18n;
	      
	      if(data.userInfo.type == "300004"){
	    	  data.isType = true;
	      }else{
	    	  data.isType = false;
	      }
	      
	      var rendered = Mustache.to_html(view.template, data);
	      
	      $(view.el).append(rendered);

	      view.setCalendarArea();
	      view.selectRollInt();
	      view.datatablesInit();

	      return view;
	    },
	    
	    // setup calendar
	    setCalendarArea : Common.JC_calendar.searchRange(i18n, true),
	    
	    datatablesInit : function(){
	    	datatables = $("#stockStatus-tables").DataTable({
		  		"serverSide": true,
		    	"language": {
		            "url": "//cdn.datatables.net/plug-ins/1.10.11/i18n/English.json"
		        },
		        "ajax" : {
		        	"url" : targetUrl,
		        	"type": 'GET',
			    	"data" : function(data){
			    		var form = $('form').serializeObject();
			    		$.extend(form, _data);
			    		return $.extend(data, form);
			    	},
			        "dataSrc" : 'list'
		        },
			    "columns" : [
			                 
			      { "data" : "ITEM_CD", "name" : "ITEM_CD", "className" : "item-cd" },
				  { "data" : "ITEM_NAME", "name" : "ITEM_NAME","className" : "dt-right item-name"},
				  { "data" : "BASE_CNT", "name" : "BASE_CNT", render: function (data, type, row) {return Common.JC_format.number(data);}, "className" : "dt-right base-cnt"},
				  { "data" : "IMPORT_CNT", "name" : "IMPORT_CNT", render: function (data, type, row) {return Common.JC_format.number(data);}, "className" : "dt-right import-cnt"},
				  { "data" : "EXPORT_CNT", "name" : "EXPORT_CNT", render: function (data, type, row) {return Common.JC_format.number(data);}, "className" : "dt-right export-cnt"},
				  { "data" : "ADJUST_CNT", "name" : "ADJUST_CNT", render: function (data, type, row) {return Common.JC_format.number(data);}, "className" : "dt-right adjust-cnt"},
				  { "data" : "STOCK_CNT", "name" : "STOCK_CNT", render: function (data, type, row) {return Common.JC_format.number(data);}, "className" : "dt-right stock-cnt"},
				  { "data" : "SAFE_STOCK_CNT", "name" : "SAFE_STOCK_CNT", render: function (data, type, row) {return Common.JC_format.number(data);}, "className" : "dt-right safe-stock-cnt"}
				],
				// columns에 의해 생성된 tr 데이터 재정의
				"fnRowCallback" : function (row /* tr  */, data /* all column data */, index) {
					// columns data
					// 특정 데이터의 수가 0미만일 때 빨간색으로 표시
					if (data.BASE_CNT < 0) {
						$(row).find(".base-cnt").css('color', 'red');
					} 
					if (data.ADJUST_CNT < 0) {
						$(row).find(".adjust-cnt").css('color', 'red');
					}
					if (data.STOCK_CNT < 0) {
						$(row).find(".stock-cnt").css('color', 'red');
					}
				},
		        "order": [[ 0, 'desc' ]],
			    "pageLength": 10,
			    "lengthChange": false,
			    "processing": true,
			    "rowReorder": true,
			    "searching": false
			});
	    },
	    load_detail: function (event) {
	    	var view = this;
	    	var _this = $(event.target);
	    	$.ajax({
	    		url : targetUrl,
	    		data : {
	    			data : JSON.stringify({ id : _this.attr("target-code")})
	    		},
	    		success : function(response){
	    			if(response.success){
	    				var data = response.list;
	    		        data.i18n = i18n;
	    		        data = Common.JC_format.handleData(data);
	    				var _template = Mustache.to_html(modalform, $.extend(code, data));

	    				$('#franchise-modal').html(Mustache.to_html(_template, view.buildOptions(data)));

	    				view.form_init('PUT', $('#franchise-modal'));
	    			} else {
	    				$('#franchise-modal').modal('toggle');
	    			}
	    		}
	    	});
	    },
	    datatablesReload : function(){
	    	var view = this;
	    	if(datatables != undefined){
	    		datatables.draw();
	    	}
	    },
	    
	    selectRollInt : function () {
	    	var view = this;
			$('select').select2({
		        minimumResultsForSearch: -1  //  -1 : select안에 옵션 검색 지원, 해당 속성이 없으면 지원 안함
			});
	    		
	    		// 특정 프랜차이즈가 선택이 될 때
			$("#selectFranchise").select2().on('change', function() {
				_data.franId = $(this).val();
				if (_data.franId == '' || _data.franId == undefined) {
					_data.brandId = '';
					_data.storeId = '';
				}
				$.ajax({
					 url 					: 		'./model/Brand'
					,data				: {
							data : JSON.stringify({ franId : _data.franId })
					}
				
					,success : function (result) {
						if (result.success) {
							var defaultOption		=		 { id : '', brandNm : i18n.BO1032};		// 기본 옵션
							var brandList 			= 		 [];
							brandList.push(defaultOption);
							$("#selectBrand").find('option').remove();
							$("#selectBrand").select2({
								 data 							:		 brandList.concat(result.list)		// concat 	 : 	 여러 배열 합침
								,templateResult 		: 		 function (data) {							// 옵션 텍스트 표시
									return data.brandNm;
								}
								,templateSelection 	:		 function (data, container) {		// 선택된 옵션 표시
									return data.brandNm;
								}
							});
							view.resetSelectStore(result);
						}
					}
				});
			});
	    		
			// 특정 브랜드가 선택이 될 때
			$("#selectBrand").select2().on('change', function() {
				_data.brandId = $(this).val();
				if (_data.brandId == '' || _data.brandId == undefined) {
					_data.storeId = '';
				}
				$.ajax({
					 url 					: 		'./model/Store'
					,data				: {
							data : JSON.stringify({ brandId : _data.brandId })
					}
				
					,success : function (result) {
						if (result.success) {
							view.resetSelectStore(result);
						}
					}
				});
			});
	    		
			$('#selectStore').select2().on('change', function(){
				//브랜드와 해당 매장에 관련된 카테고리 분류정보 셋팅
				_data.storeId =  $(this).val();
				view.selectedInit(_data.brandId, _data.storeId);
		    });
		},
	    
		 // fran 혹은 brand 선택 시 매장 정보 초기화
	    resetSelectStore : function (result) {
	    	var defaultOption		=		 { id : '', storeNm : i18n.BO1033};		// 기본 옵션
			var storeList 			= 		 [];
			storeList.push(defaultOption);
			$("#selectStore").find('option').remove();
			$("#selectStore").select2({
				 data 							:		 storeList.concat(result.list)		// concat 	 : 	 여러 배열 합침
				,templateResult 		: 		 function (data) {							// 옵션 텍스트 표시
					return data.storeNm;
				}
				,templateSelection 	:		 function (data, container) {		// 선택된 옵션 표시
					return data.storeNm;
				}
			});
	    },
		
	    selectedInit : function (vBrandId , vStoreId){
	    	/**
	    	 * 1.브랜드, 상점 id 별 카테고리 분류 (최소한 브랜드는 선택해야함)
	    	 * 2.판매여부 Y/N ==> 공통코드에 추가 사용 해야함.
	    	 * 3.등록구분 브랜드/매장 / pos plu 값 고정 셋팅 : 351001
	    	 */
	    	var view = this;

	    	$.ajax({
				url : './model/items/CateSelect',
				data : {
					data : JSON.stringify({ brandId : vBrandId, storeId : vStoreId })
				},
				success : function(result){
					if(result.success){
						var defualt_array = [{id: '', name : i18n.BO2248}];
						$("#itemCat").find('option').remove();
						$('#itemCat').select2({
							data : defualt_array.concat(result.list),
							templateResult : function(state) {
								return state.name;
							},
							templateSelection : function(data, container) {
							    return data.name;
							}
						});
					}
				}  //END : category select result
			});  //END : category select ajax

	    },
	    
	    search : function(){
	    	if (userType.isBr) {
				if (_data.storeId == "" || _data.storeId == undefined) {
//	        		BO4003 : 매장 선택해 주세요
	        		alert(i18n.BO4003);
	        		return false;
				}
	    	} else if (userType.isFr) {
	        	if (_data.brandId == "" || _data.brandId == undefined) {
//	        		BO4002 : 브랜드를 선택해 주세요
	        		alert(i18n.BO4002);
	        		return false;
	        	}
	        	
				if (_data.storeId == "" || _data.storeId == undefined) {
//	        		BO4003 : 매장 선택해 주세요
	        		alert(i18n.BO4003);
	        		return false;
				}
	    	} else if (userType.isAdmin) {
	        	if (_data.franId == "" || _data.franId == undefined) {
//	        		BO4001 : 법인을 선택해 주세요
	        		alert(i18n.BO4001);
	        		return false;
	        	}
	        	
	        	if (_data.brandId == "" || _data.brandId == undefined) {
//	        		BO4002 : 브랜드를 선택해 주세요
	        		alert(i18n.BO4002);
	        		return false;
	        	}
	    		
				if (_data.storeId == "" || _data.storeId == undefined) {
//	        		BO4003 : 매장 선택해 주세요
	        		alert(i18n.BO4003);
	        		return false;
				}
	    	}
	    	
	    	var view = this;
	    	var $form = $('form');

	    	// 기존 franId 저장 후 재 반납 
	    	var franId = _data.franId;
	    	var brandId = _data.brandId;
	    	var storeId = _data.storeId;
	    	_data= $.extend(_data, $form.serializeObject());
	    	_data.franId = franId;
	    	_data.brandId = brandId;
	    	_data.storeId = storeId;
	    	

	    	$.each(Object.keys(_data), function(idx, _key){
	    		if ( _data[_key] == "" || _data[_key] === null || (typeof _data[_key] == "object" && $.isEmptyObject(_data[_key])) ) {
	    			delete _data[_key];
	    		}
	    	});
	    	view.datatablesReload();
	    },
	    buildOptions : function(object) {
	        for (var i in object) {
	            object[i + '=' + object[i]] = true;
	        }
	        return object;
	    },
	    form_init : function(method, $modal){
	    	var view = this;

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
		        		url : targetUrl,
		        		type: 'POST',
		        		data : {
		        			"_method" : method,
		        			"data" : JSON.stringify(params)
		        		},
		        		success : function(response) {
		        			if (response.success) {
		        				$modal.modal('toggle');
		        				datatables.draw();
		        			} else {
		        				alert(i18n.BO4019+' : "'+response.errMsg+'"');
		        			}
		        		}
		        	});
		        	return false;
		        }
			});

	    },    
	    trigger_calender : function(event){
	    	var _this = $(event.target);
	    	var _cal = _this.closest('div').find('.date-picker');
	    	_cal.trigger('click');    	
	    }

	  });

	  return ContentView;
	});