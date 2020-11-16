/*
 * Filename	: cardApprovalManage.js
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
  
  'text!templates/sales/cardApprovalManage.html',

  'bootstrap-daterangepicker'

], function (Backbone, Mustache, Common, i18n, template) {
  'use strict';

  var _data = {};

  var targetUrl = './model/sales/CardApproval';

  var datatables;

  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events : {
    	'click button#search' : 'search',
		'click button#csv-exp' : 'csv_exp',
		'change #franchise_select' : 'selectBrand',
		'change #brand_select' : 'selectStore',
		'click .add-on-datepicker' : 'trigger_calender'
    },
    initialize: function () {
      this.trigger('remove-compnents');
      this.template = template;
      this.listenTo(this.model, 'sync', this.render);
    },
    render: function () {
    	var view = this;
    	var data = {};
    	
    	data.cd = view.loadBaseCode('PathType,OrderStatus', false);
    	
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
    	
    	// 스토어 사용자가 로그인시 자동조회
        if(data.isSt == true || data.isBr == true){
      	  view.search();
        }

    	return view;
    },
    
	// setup calendar
    setCalendarArea : Common.JC_calendar.searchRange(i18n),

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
      					var defualt_array = [{id: '', storeNm : i18n.BO1033}];
      					$("#store_select").find('option').remove();
      					$('#store_select').select2({
      						data : defualt_array.concat(result.list),
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
        });  //END : brand select change
    	
    	$('#store_select').select2().on('change', function(){
    		_data.storeId = $(this).val();
        });
    },
    search : function() {
    	var view = this;
    	
    	if (_data.franId == "" || _data.franId == undefined) {
    		alert(i18n.BO4001);
    		return false;
    	}
    	
    	if (_data.brandId == "" || _data.brandId == undefined) {
    		alert(i18n.BO4002);
    		return false;
    	}
    	
    	if ($('#daterangepicker').val() == 'Invalid date - Invalid date') {
    		alert(i18n.BO4032);
    		return false;
    	}
    	
    	if(datatables != undefined){
    		datatables.draw();
    	}else{
	    	
	    	datatables = $("#approval_table").DataTable({
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
						    		if ( data[key] == "" || data[key] === null || (typeof data[key] == "object" &&  $.isEmptyObject(data[key])) ) {
						    			delete data[key];
						    		}
						    	});
						    	
					    		return data;
					    	},
			        "dataSrc" : 'list'
		        },
			    "columns" : [
			      { "data" : "id", "name" : "ID", "visible": false},
				  { "data" : "payDay", "name" : "PAY_DAY", render: function (data, type, row) {return Common.JC_format.day(data);}},
				  { "data" : "orderNo", "name" : "ORDER_NO"},
				  { "data" : "amount", "name" : "AMOUNT", render: function (data, type, row) {return Common.JC_format.number(data);}},
				  { "data" : "paySt", "name" : "PAY_ST", "visible": false},
				  { "data" : "payStNm", "name" : "PAY_ST_NM", "visible": false},
				  { "data" : "cardNo", "name" : "CARD_NO" },
				  { "data" : "tranNo", "name" : "TRAN_NO" },
				  { "data" : "monthlyPlain", "name" : "MONTHLY_PLAIN", "visible": false },
				  { "data" : "payMethod", "name" : "PAY_METHOD", "visible": false},
				  { "data" : "payMethodNm", "name" : "PAY_METHOD_NM", "visible": false },
				  { "data" : "approvalDay", "name" : "PAY_TM", render: function (data, type, row) {return Common.JC_format.day(data);}},
				  { "data" : "approvalTime", "name" : "PAY_TM", render: function (data, type, row) {return Common.JC_format.time(data);}}
				],
		        "order": [[ 2, 'DESC' ]],
			    "pageLength": 10,
			    "lengthChange": false,
			    "processing": true,
			    "rowReorder": true,
			    "searching": false
			});
	    	
			
			$('#approval').show();
    	}
    },
    csv_exp: function () {
    	
    	if(datatables != undefined){
    		if(datatables.page.info().recordsTotal > 0){
    			$('#approval_table').tableToCSV();
//    			_exportData.draw = 'excel';
//    	    	_exportData.headers = 
//    	    		JSON.stringify([{
//    	    			idx : 0,
//    	    			data : 'payDay',
//    	    			title : i18n.BO2323
//    	    		},  {
//    	    			idx : 1,
//    	    			data : 'orderNo',
//    	    			title : i18n.BO2322
//    	    		}, {
//    	    			idx : 2,
//    	    			data : 'amount',
//    	    			title : i18n.BO2327
//    	    		}, {
//    	    			idx : 3,
//    	    			data : 'cardNo',
//    	    			title : i18n.BO2325
//    	    		}, {
//    	    			idx : 4,
//    	    			data : 'tranNo',
//    	    			title : i18n.BO2325
//    	    		}, {
//    	    			idx : 5,
//    	    			data : 'approvalDay',
//    	    			title : i18n.BO2331
//    	    		}, {
//    	    			idx : 6,
//    	    			data : 'approvalTime',
//    	    			title : i18n.BO2332
//    	    		}]);    	
//
//    	    	$.ajax({
//    	    		url : targetUrl,
//    	    		data : _exportData,
//    	    		async : false,
//    	    		success : function(result){
//    	    			if(result.success){
//    	    				location.href = result.url;
//    	    			}
//    	    		}
//    	    	});    
    		}else{
    			alert(i18n.BO4030);
    		}
    		
    	}else{
    		alert(i18n.BO4030);
    	}
    },
    trigger_calender : function(event){
    	var _this = $(event.target);
    	var _cal = _this.closest('div').find('.date-picker');
    	_cal.trigger('click');    	
    },
    loadBaseCode : function(codeAlias, option){

    	var data = {};
    	
		$.ajax({
			url : './model/Codes',
			method : 'POST',
			async: false,
			data : {
				'codes' : codeAlias.trim()
			},
			success : function(result){
				if(result.success){

					if(option){
						$.each(Object.keys(result.codes), function(idx, code){
							$.each(result.codes[code], function(_idx, _code){
								_code.selections = '{{#'+code+'='+_code.baseCd+'}} selected="selected" {{/'+code+'='+_code.baseCd+'}}';
							});
						});
					}
					
					data = $.extend(result.codes, _data);

				} else {
		    		$('#items-modal').html('');
		    		$('#items-modal').modal('toggle');
				}
			}
		});
		
		return data;
    }
  });

  return ContentView;
});