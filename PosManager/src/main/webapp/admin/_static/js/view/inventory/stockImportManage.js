/*
 * Filename	: stockImportManage.js
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
  
  'text!templates/inventory/stockImportManage.html',
  'text!templates/inventory/modal/stockImportCSV.html',
  'text!templates/common/modal_form_alert.html',
  
  'bootstrap-daterangepicker'

], function (Backbone, Mustache, Common, i18n, template, modalcsv, modalAlert) {
  'use strict';

  var datatables;

  var targetUrl = './model/inventory/StockImport';

  var _data = {};
  
  var userType = {};

  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events: {
    	'click button#search' 		: 'search',
    	'click .add-on-datepicker'	: 'trigger_calender',
    	'click button#del-stock'	: 'del_stock',
		'click button#csv-stock'	: 'modal_csv',
		'click button#csvUp'		: 'csvUp'
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
      data.isType = false; // TODO
      
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
		var form = $('form').serializeObject();
		$.extend(form, {"getTotalPrice" : 1});
    	$.ajax({
    	 	 url : targetUrl
    		,type : 'GET'
    		,data : form
			,error : function (result) {
				alert(i18n.BO0010);
			}
    		,success : function (result) {
    			var totalPrice = 0;
    			for (var i = 0; i < result.list.length; i++) {
    				var stockImport = result.list[i];
    				totalPrice += stockImport.totalPrice;
    			}
    			if (totalPrice > 0) {
    				$("#totalPriceUl").show();
    				$('#totalPrice').text(Common.JC_format.number(totalPrice));
    			} else {
    				$("#totalPriceUl").hide();
    			}
    			
    			datatables = $("#stockImport-tables").DataTable({
    		  		"serverSide": true,
    		    	"language": {
    		            "url": "//cdn.datatables.net/plug-ins/1.10.11/i18n/English.json"
    		        },
    		        "ajax" : {
    		        	"url" : targetUrl,
    		        	"type": 'POST',
    			    	"data" : function(data){
    			    		var form = $('form').serializeObject();
    			    		$.extend(form, _data);
    			    		data._method = 'GET';
    			    		return $.extend(data, form);
    			    	},
    			        "dataSrc" : 'list'
    		        },
    			    "columns" : [
    			      { "data" : "stockDt", "name" : "stockDt", render: function (data, type, row) {return Common.JC_format.day(data);} },
    				  { "data" : "itemCd", "name" : "itemCd"},
    				  { "data" : "itemNm", "name" : "itemNm"},
    				  { "data" : "supplyNm", "name" : "supplyNm"},
    				  { "data" : "unitPrice", "name" : "unitPrice", render: function (data, type, row) {return Common.JC_format.number(data);} },
    				  { "data" : "stockCnt", "name" : "stockCnt", render: function (data, type, row) {return Common.JC_format.number(data);} },
    				  { "data" : "totalPrice", "name" : "totalPrice", render: function (data, type, row) {return Common.JC_format.number(data);} }
    				  /* ,{ "data" : "id"} */
    				],
    				/*
    			    "columnDefs":  [ {
    			    	"render": function ( data, type, row ) {
    			            return '<button class="btn btn-primary btn-sm franchise-detail" data-toggle="modal" data-target=".bs-example-modal-lg" target-code="'+data+'" >'+i18n.BO0016+'</button>';
    			        },
    			    	"bSortable": false,
    			        "targets": 7
    			    } ],
    			    */
    		        "order": [[ 0, 'desc' ]],
    			    "pageLength": 10,
    			    "lengthChange": false,
    			    "processing": true,
    			    "rowReorder": true,
    			    "searching": false
    			});
    		}
    	});
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
	
    search : function(){
    	if (userType.isBr) {
			if (_data.storeId == "" || _data.storeId == undefined) {
//        		BO4003 : 매장 선택해 주세요
        		alert(i18n.BO4003);
        		return false;
			}
    	} else if (userType.isFr) {
        	if (_data.brandId == "" || _data.brandId == undefined) {
//        		BO4002 : 브랜드를 선택해 주세요
        		alert(i18n.BO4002);
        		return false;
        	}
        	
			if (_data.storeId == "" || _data.storeId == undefined) {
//        		BO4003 : 매장 선택해 주세요
        		alert(i18n.BO4003);
        		return false;
			}
    	} else if (userType.isAdmin) {
        	if (_data.franId == "" || _data.franId == undefined) {
//        		BO4001 : 법인을 선택해 주세요
        		alert(i18n.BO4001);
        		return false;
        	}
        	
        	if (_data.brandId == "" || _data.brandId == undefined) {
//        		BO4002 : 브랜드를 선택해 주세요
        		alert(i18n.BO4002);
        		return false;
        	}
    		
			if (_data.storeId == "" || _data.storeId == undefined) {
//        		BO4003 : 매장 선택해 주세요
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
    	datatables.destroy();	// destroy dataTable context
    	view.datatablesInit();
    },
    buildOptions : function(object) {
        for (var i in object) {
            object[i + '=' + object[i]] = true;
        }
        return object;
    },
    modal_csv : function(){
    	if (_data.storeId == '' || _data.storeId == undefined) {
    		alert(i18n.BO4003);
    		return false;
    	}
    	_data.i18n = i18n;
    	$('#stock-csv').html(Mustache.to_html(modalcsv, _data));
    },
    csvUp: function () {
    	var file = $('[name=csv]').val();
    	var fileExt = file.substring(file.lastIndexOf('.') + 1).toLowerCase();

    	if (file == '' || fileExt != 'csv') {
    		alert(i18n.BO4012);
    		//$('#stock-csv').prepend(modalAlert);
    		return false;
    	}
        if (confirm(i18n.BO4013)) {
            $('#csvUpForm').attr('action', './model/inventory/StockImportCSV');
       		var options = {
       			success : function(response) {
        			if(response.success) {
        				alert('CSV Upload Success!');
    		    		$('#stock-csv').html('');
    		    		$('#stock-csv').modal('toggle');
    		    		datatables.destroy();   //  destroy dataTable context
    		    		view.datatablesInit();
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
    trigger_calender : function(event){
    	var _this = $(event.target);
    	var _cal = _this.closest('div').find('.date-picker');
    	_cal.trigger('click');    	
    }

  });

  return ContentView;
});