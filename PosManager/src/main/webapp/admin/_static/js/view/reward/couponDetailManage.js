/*
 * Filename	: couponDetailManage.js
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
  
  'text!templates/reward/couponDetailManage.html',
  'text!templates/reward/modal/couponDetail_list_modal.html',

  'bootstrap-daterangepicker'

], function (Backbone, Mustache, Common, i18n, template, modal) {
  'use strict';

  var targetUrl = './model/reward/CouponLog';

  var _data = {};

  var main_tables;

  var ContentView = Backbone.View.extend({
	    el: $('#main-wrapper'),
	    events : {
	    	'click button#couponList-search' : 'couponListSearch',
	    	'click #coupon-table button.coupon-lists' : 'load_detail',
	    	'click #cancle_issued' : 'cancle_coupon',
	    	'click #cancle_used' : 'cancle_coupon',
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

	      _data = view.model.toJSON()[0].session.userInfo;
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
	    
	    couponListSearch : function(){
	    	var view = this;
	    	var $form = $('#be-search');
	    	
	    	_data = $.extend(_data, $form.serializeObject());
	    	
	    	$.each(Object.keys(_data), function(idx, _key){
	    		if ( _data[_key] == "" || _data[_key] === null || (typeof _data[_key] == "object" && $.isEmptyObject(_data[_key])) ) {
	    			delete _data[_key];
	    		}
	    	});
	    	
	    	view.datatablesReload();
	    },
	    datatablesInit : function(){
	    	main_tables = $("#coupon-table").DataTable({
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
			      { "data" : "id", "name":"ID"},
				  { "data" : "couponCd", "name":"COUPON_CD"},
				  { "data" : "couponNm", "name":"COUPON_NM"},
				  { "data" : "couponTpNm", "name":"COUPON_TP_NM"},
				  { "data" : "created", "name":"CREATED"
					  , "render": function (data, type, row) {
		                  return Common.JC_format.day(data);
		              }  
				  },
				  { "data" : "expire", "name":"EXPIRE"},
				  { "data" : "barcode", "name":"BARCODE"},
				  { "data" : "name", "name":"NAME"},
				  { "data" : "mb", "name":"MB"},				  
				  { "data" : "storeNm", "name":"STORE_NM"},
				  { "data" : "used", "name":"USED"},
				  { "data" : "id"}
				  
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
			        "targets": 10
					}, 
					{
			        "render": function ( data, type, row ) {
					            return '<button class="btn btn-primary btn-sm coupon-lists" data-toggle="modal" data-target=".bs-coupon-list-modal-lg" target-code="'+data+'">'+i18n.BO0022+'</button>';
					        },
			        "bSortable": false,
			        "targets": 11
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
	    				view.buildOptions(data);
	    				
	    				data.i18n = i18n;
	    				data = Common.JC_format.handleData(data);
	    		    	$('#coupon-lists').html(Mustache.to_html(modal, data));
	    			}
	    		}	    		
	    	});
	    },
	    buildOptions : function(object) {
	        for (var i in object) {
	            object[i.toLowerCase() + '=' + object[i]] = true;
	        }
	        return object;
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
	      		view.couponListSearch();
	      	});  //END : franchise select change
	    	
	    	
	    	$('#brand_select').select2().on('change', function(){
	    		_data.data = JSON.stringify({brandId : $(this).val()});
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
				view.couponListSearch();
	        });  //END : brand select change
	    	
	    	$('#store_select').select2().on('change', function(){
				//브랜드와 해당 매장에 관련된 카테고리 분류정보 셋팅
				_data.storeId = $(this).val();
				view.couponListSearch();
	        });
	    },
	    cancle_coupon : function(event){
	    	var _target = $(event.target);
	    	var data = {};
	    	var $form = $('#couponModal');
	    	
	    	data = $.extend(data, $form.serializeObject());
	    	data.couponSt = _target.attr('status');
	    	
	    	if($('#couponTp').val() == "406001" ){
	    		alert("Stamp accumulation issue can not be canceled.");
	    		return false;
	    	}
	    	
	    	if(_target.attr('status') == "402004"){
	    		if($('#couponSt').val() != "402001"){
	    			alert("Coupon is not in publishing status.");
		    		return false;
	    		}
	    	}
	    	
	    	if(_target.attr('status') == "402002"){
	    		if($('#couponSt').val() != "402003"){
	    			alert("Coupon is not in use.");
		    		return false;
	    		}
	    	}
	    	
	    	if($('#couponSt').val() == "402005"){
	    		alert("It's Expired coupon.");
	    		return false;
	    	}	    	
	    	
	    	$.ajax({
	    		url : targetUrl,
	    		type: 'POST',
	            data : {
	              "_method" : 'PUT',
	              "data" : JSON.stringify(data)
	            },
	    		success : function(response){
	    			if(response.success){
	    				alert("Successful coupon issue canceled");
	    			}else{
	    				alert("Coupon cancellation failed");
	    			}
	    		}
	    	});
		},
	    loadBaseCode : function(){
	    	var data = {};
			$.ajax({
				url : './model/management/Codes',
				method : 'POST',
				async: false,
				data : {
					'codes' : 'CouponPubType,CouponLogType'
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