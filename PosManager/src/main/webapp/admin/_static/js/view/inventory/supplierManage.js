/*
 * Filename	: supplierManage.js
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
  
  'text!templates/inventory/supplierManage.html',
  'text!templates/inventory/modal/supplierModal.html',
  'text!templates/common/modal_form_alert.html',

  'bootstrap-daterangepicker'

], function (Backbone, Mustache, Common, i18n, template, modalForm, modalAlert) {
  'use strict';

  var datatables;

  var targetUrl = './model/inventory/Supplier';

  var _data = {};
  
  var userType = {};

  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events: {
    	 'click button.supplyCompany-new' 		:	'modal_add'
		,'click button#detail-supplyCompany' 	:	'load_detail'
		,'click button#search' 					: 	'search'
		,'click .all-checked'					:	'allChecked'
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
      
      if(data.userInfo.type == "300004"){	// 매장 관리자 권한
    	  data.isType = true;
      }else{
    	  data.isType = false;
      }
      
      var rendered = Mustache.to_html(view.template, data);
      
      $(view.el).append(rendered);

      view.selectRollInt();
      view.datatablesInit();

      return view;
    }
    
    ,datatablesInit : function () {
    	datatables = $("#supplyCompany-tables").DataTable({
    		"ajax" : {
    			"url"        		 : 		targetUrl
    			,"type"   		 :		"POST"  // GET 방식일 때 request header data size가 특정 size를 넘어갈 때 to large error 발생
    			,"data"    		 :	    function (data) {
    											data._method = 'GET';
    											return $.extend(data, _data);
    										}
    			,"dataSrc"	 : 		"list"
    		}
    	
//		,"language" : {
//		"url" : "//cdn.datatables.net/plug-ins/1.10.11/i18n/English.json",
//	}
    	
    		,"columns" : [		// table data init
    		      /**
    		       *  columns.data 	:	 table 행의 열에 대한 데이터 소스를 설정
    		       *  columns.name   :	 data name 설정, sorting query column name
    		       * */
    		     {"data" : "id", 						"name" : "ID"}
    		     ,{"data" : "companyCd", 		"name" : "COMPANY_CD"}
    		     ,{"data" : "companyNm", 	"name" : "COMPANY_NM"}
    		     ,{"data" : "bizNo",					"name" : "BIZ_NO"}
    		     ,{"data" : "tel", 						"name" : "TEL"}
    		     ,{"data" : "ceoNm", 				"name" : "CEO_NM"}
    		     ,{"data" : "id"}
    		 ]
    		
    		,"columnDefs" : [
    		      {
    		    	  "render": function ( data, type, row ) {
    			            return '<button type="button" class="btn btn-xs btn-primary" data-toggle="modal" data-target=".bs-example-modal-lg" id="detail-supplyCompany" target-code="'+data+'">' + i18n.BO0022 + '</button>';
    			        }
    			        ,"bSortable": false	// 해당 열에 의한 sorting 여부
    			        ,"targets": 6
    		      }
    		]
    		
    		,"order" : [	// [target, order sort], 특정 열 정렬
    		    [0, 'asc']
    		   ,[1, 'desc']
    		]

    		,"serverSide"				: 	true			// 서버쪽에서 데이터 처리를 받음
    		,"pageLength" 			: 	10 			// 페이지 당 행의 수 설정
    		,"lengthChange" 		:	false		// 테이블 행 수 변경 (10, 25, 50, 100)
    		,"processing" 			: 	true 		// data return 전 까지 processing div 띄우기
    		,"rowReorder" 			: 	true			// 행 데이터 재 정렬??
    		,"searching"				:	false		// true : table search editor 지원
    	});
    }
    
    ,search : function(){
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

    	_data.i18n = ''; 

    	$.each(Object.keys(_data), function(idx, _key){
    		if ( _data[_key] == "" || _data[_key] === null || (typeof _data[_key] == "object" && $.isEmptyObject(_data[_key])) ) {
    			delete _data[_key];
    		}
    	});
    	view.datatablesReload();
    }
    
    ,selectRollInt : function () {
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
    }
    
    ,load_detail : function (event) {
    	var view = this;
    	var targetSupplyCompany = $(event.target);
    	
		var data = datatables.row($(event.target).closest('tr').get(0)).data();
		// detail은 해당 매장 아이디 값을 가지고 있으므로 권한만 있다면 누구나 수정 가능
		_data.franId = data.franId;
		_data.brandId = data.brandId;
		_data.storeId = data.storeId;
		$.ajax({
    		url : targetUrl,
    		data : {
    			data : JSON.stringify({
    				id : targetSupplyCompany.attr("target-code"),
    			})
    		},
    		success : function(response){
    			if(response.success){
    				var result = response.data;
    				result.i18n = i18n;

    				$('#supplyCompany-modal').html(Mustache.to_html(modalForm, result));
    		    	view.form_init('PUT', $('#supplyCompany-modal'));
//        				var _template = Mustache.to_html(modalForm, data);
//        				$('#supplyCompany-modal').html(Mustache.to_html(_template, view.buildOptions(response.list)));
//        				view.form_init('PUT', $('#supplyCompany-modal'));
    			}
    		}
    	});
    }
    
    ,buildOptions : function(object) {
        for (var i in object) {
            object[i.toLowerCase() + '=' + object[i]] = true;
        }
        return object;
    }
    
    ,modal_add : function () {
    	// 매장이 선택되어 있지 않으면 등록 불가능
    	if (_data.storeId == '' || _data.storeId == undefined) {
    		alert(i18n.BO4003);
    		return false;
    	}
    	_data.i18n = i18n;
    	$('#supplyCompany-modal').html(Mustache.to_html(modalForm, _data));
    	this.form_init('POST', $('#supplyCompany-modal'));
    }
    
    ,form_init : function (method, $modal) {
    	var view = this;
    	
    	// form이 1개이기 때문에 특정 폼을 지정해주지 않아도 됨..
    	$.validate({
    		 validateOnBlur 				: 	false		// element가 focus를 잃었을 때 유효검증 여부, 기본 값 true
    		,scrollToTopOnError		: 	false		// 에러가 발생했을 때 에러 지점으로 스크롤링 여부?
    		,showHelpOnFocus			: 	false		// ...
    		,errorMessagePosition	: $('.form-validatoin-alert-detail')	// onError에 의해 표시됨, $('#modal-form') 밑에 alert 이 나타남
    		,addSuggestions				: 	false		// ...
    		,onError : function ($form) {
    			var target = $('#modal-form');
    			
    			if (target.find('.alert').length == 0) {
    				target.prepend(Mustache.to_html(modalAlert, {i18n:i18n}));
    			}
    		}
    		,onSuccess : function ($form) {
    			var params = $form.serializeObject();
    			params.franId 		= _data.franId
    			params.brandId 	= _data.brandId;
    			params.storeId 	= _data.storeId;
    			
    			$.ajax({
    				 url 		: 	targetUrl
    				,type	: 	'POST'
    				,data 	: 	{
	        			"_method" : method,
	        			"data" : JSON.stringify(params)
	        		}
    				,success : function (response) {
    					if (response.success) {
	    					$modal.modal('toggle');
	    					
	    					if (datatables != undefined) {
	    						datatables.draw();
	    					} else {
	    						view.datatablesReload();
	    					}
    					}
    				}
    			});
    			
    			return false; 	// false : 화면이 그대로, true : list 화면으로 
    		}
    	});
    }
    
	,datatablesReload : function(){
		var view = this;
		if(datatables != undefined){
			datatables.draw();
		}
	}
  });

  return ContentView;
});