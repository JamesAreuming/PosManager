/*
 * Filename	: memberManage.js
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

define([
  'bracket',
  'backbone',
  'mustache',
  'common',
  'i18n!templates/globals/lang/nls/language',
  
  'text!templates/management/customerManage.html',
  'text!templates/management/modal/customer_detail.html',
  'text!templates/common/modal_form_alert.html',

  'summernote',
  'bootstrap-daterangepicker'  

], function (Brackets, Backbone, Mustache, Common, i18n, template, user_detail, modalAlert) {
  'use strict';

  var _data = {};

  var userType = {};
  
  var targetUrl = './model/management/Customer';

  var main_tables;
  jQuery.browser = {};
  (function () {
      jQuery.browser.msie = false;
      jQuery.browser.version = 0;
      if (navigator.userAgent.match(/MSIE ([0-9]+)\./)) {
          jQuery.browser.msie = true;
          jQuery.browser.version = RegExp.$1;
      }
  })();
  
  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events : {
    	'click .user-detail'		: 'user_detail',    	
    	'click .back-user-main' : 'back_user_main',
    	'click .save-user-info' : 'saveUserInfo',
    	'click #customerManage-search' : 'search',
	  	'click .add-on-datepicker'	: 'trigger_calender'
    },
    initialize: function () {
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
     _data.userInfo = view.model.toJSON()[0].session.userInfo;
      data.userInfo = view.model.toJSON()[0].session.userInfo;
      data.i18n = i18n;
      data.cd = view.loadBaseCode('Gender', true);
      var rendered = Mustache.to_html(view.template, data);
      $(view.el).append(rendered);
      
      view.setCalendarArea();
      view.selectRollInt();
      view.datatablesInit();
      $("#daterangepicker").val('');
      $("#from-date").val('');
      $("#to-date").val('');
      
      return view;
    },
    
    // setup calendar
    setCalendarArea : Common.JC_calendar.searchRange(i18n, true),
    
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
			if (_data.storeId != '') {
				view.datatablesReload();
			}
	    });
	}
    
    // fran 혹은 brand 선택 시 매장 정보 초기화
    ,resetSelectStore : function (result) {
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
    
    datatablesInit : function(){    	
    	main_tables = $('#customer-manage').DataTable({
    		"serverSide": true,
	  		"searching": false,	  		 
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
			  { "data" : "barcode", "name" : "BARCODE"}, 
			  { "data" : "mb", "name" : "MB"},
			  { "data" : "name", "name" : "NAME"}, 
			  { "data" : "birthday", "name" : "BIRTHDAY", render: function (data, type, row) {return Common.JC_format.day(data);}}, 
			  { "data" : "gender", "name" : "GENDER"}, 
			  { "data" : "email", "name" : "EMAIL" },
			  { "data" : "id"}
			],
		

		    "columnDefs":  [ 
		     {
		    	"render": function ( data, type, row ) {
		            return '<button type="button" class="btn btn-primary user-detail" target-data="'+data+'" >'+i18n.BO2155+'</button>';
		        },
		        "bSortable": false,
		        "targets": 6    
		    } ],
		    "order": [[ 1, 'desc' ]],
            "pageLength": 10,
            "lengthChange": false,
            "processing": true,
            "rowReorder": true,
            "rowId" : 'id'
		});
    },      
    datatablesReload : function(){
    	var view = this;
    	if(main_tables != undefined){
    		main_tables.draw();
    	}
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
    	var $form = $('#rollSearchFrom');

    	_data= $.extend(_data, $form.serializeObject());

    	$.each(Object.keys(_data), function(idx, _key){
    		if ( _data[_key] == "" || _data[_key] === null || (typeof _data[_key] == "object" && $.isEmptyObject(_data[_key])) ) {
    			delete _data[_key];
    		}
    	});

    	if(main_tables != undefined){
    		main_tables.draw();
    	}else{
    		view.datatablesReload();
    	}
    },
    
    user_detail : function(event){
    	var view = this;
    	var target = $(event.target);
    	var userId = target.attr("target-data");
    	// 멤버 디테일 버튼 누를 때 해당 멤버의 정보를 멤버 상세 페이지에 저장
    	$.extend(_data, main_tables.row($(target).closest('tr').get(0)).data());
    	
    	$.ajax({
    		url : targetUrl,
    		data : {
    				"userId" : userId
    				,"franId" :  _data.franId
    				,"brandId" : _data.brandId
    				,"storeId" : _data.storeId
    		},
    		success : function(result){
    			if(result.success){
    				var code = view.loadBaseCode('MemberLevel', true);
    				var data = {};
    				
    				data = $.extend(data, result.data);
    				data.i18n = i18n;
    				data.MemberLevel = data.level;
    				data.userInfo = JSON.stringify(_data);
    				data = Common.JC_format.handleData(data);
    				
    				var _template = Mustache.to_html(user_detail, $.extend(data, code));

    				$('#user_main').hide();
    		    	$('#user-detail').html(Mustache.to_html(_template,view.buildOptions(data))).show();
    		    	
    		    	$('#user-detail').find('select').select2();
    		    	
    		    	$('#lastest-stamp-log').DataTable({
    		    		"serverSide": true,
    			  		"ordering": false,
    			  		"searching": false,
    			    	"language": {
    			            "url": "//cdn.datatables.net/plug-ins/1.10.11/i18n/English.json"
    			        },
    			        "ajax" : {
    			        	"url" : './model/marketing/StampLog',
    			        	"type": 'POST',
    				    	"data" : function(data){
    				    		data._method = 'GET';
    				    		
    	    		    		// 사용자 ID
    	    		    		data.data =  {
    	    	    				"userId" : userId
    	    	    				,"franId" :  _data.franId
    	    	    				,"brandId" : _data.brandId
    	    	    				,"storeId" : _data.storeId
    	    		    		};

    	    		    		data.data = JSON.stringify(data.data);
    	    		    		data.userInfo = JSON.stringify(_data.userInfo);
    	    		    		
    				    		return data;
    				    	},
    				        "dataSrc" : 'list'
    			        },
    				    "columns" : [
			   		      { "data" : "id"},
						  { "data" : "insDate", "name":"INS_DATE", render: function (data, type, row) {return Common.JC_format.day(data);}}, 
						  { "data" : "insTime", "name":"INS_TIME", render: function (data, type, row) {return Common.JC_format.time(data);}}, 
						  { "data" : "storeNm", "name":"STORE_NM"}, 
						  { "data" : "stampSt", "name":"STAMP_ST", "visible": false},
						  { "data" : "stampStNm", "name":"STAMP_ST_NM"},
						  { "data" : "stampTp", "name":"STAMP_TP", "visible": false},
						  { "data" : "stampTpNm", "name":"STAMP_TP_NM"}
    					],
    					"order": [[ 1, 'desc' ]],
    				    "pageLength": 5,
    				    "lengthChange": false
    		    	});
    		    	
//    		    	$('#lastest-coupon-log').DataTable({
//    		    		"serverSide": true,
//    			  		"ordering": false,
//    			  		"searching": false,
//    			    	"language": {
//    			            "url": "//cdn.datatables.net/plug-ins/1.10.11/i18n/English.json"
//    			        },
//    			        "ajax" : {
//    			        	"url" : './model/marketing/CouponLog',
//    			        	"type": 'POST',
//    				    	"data" : function(data){
//    				    		
//    				    		data._method = 'GET';
//    				    		
//    				    		// 사용자 ID
//    	    		    		data.data = {userId : userId};
//    	    		    		
//    	    		    		data.data = JSON.stringify(data.data);
//    	    		    		data.userInfo = JSON.stringify(_data.userInfo);
//    	    		    		
//    				    		return data;
//    				    	},
//    				        "dataSrc" : 'list'
//    			        },
//    				    "columns" : [
//    				      { "data" : "id"},
//    					  { "data" : "couponCd"}, 
//    					  { "data" : "couponNm"}, 
//    					  { "data" : "couponTp"},
//    					  { "data" : "issueDt", render: function (data, type, row) {return Common.JC_format.day(data);}},
//    					  { "data" : "expireDt", render: function (data, type, row) {return Common.JC_format.day(data);}}
//    					],
//    					"order": [[ 4, 'desc' ]],
//    				    "pageLength": 5,
//    				    "lengthChange": false,
//    				    "processing": true,
//    				    "rowReorder": true
//    		    	});
    			} else {
    				alert(result.errMsg);
    			}
    		}
    	});
    },
    loadBaseCode : function(codeAlias,option){
    	var data = {};
		$.ajax({
			url : './model/Codes',
			method : 'POST',
			async: false,
			data : {
				'codes' : codeAlias
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
					
					data = $.extend(data, result.codes);
					
				} else {
		    		$('#store-modal').html('');
		    		$('#store-modal').modal('toggle');
				}
			}
		});
		
		return data;
    },
    buildOptions : function(object) {
        for (var i in object) {
            object[i.toLowerCase() + '=' + object[i]] = true;
        }
        return object;
    },
    trigger_calender : function(event){
    	var _this = $(event.target);
    	var _cal = _this.closest('div').find('.date-picker');
    	_cal.trigger('click');    	
    },
    back_user_main : function (){
    	$('#user_main').show();
    	$('#user-detail').hide();
    },
    saveUserInfo : function () {
    	var params = $("#userForm").serializeObject();
    	
    	$.ajax({
    		url : targetUrl,
    		type: 'POST',
    		data : {
    			"_method" : 'PUT',
    			"data" : JSON.stringify(params)
    		},
    		success : function(response){
    			if(response.success){
    				alert(i18n.BO4046);
    			}
    		}
    	});
    }
  });

  return ContentView;
});