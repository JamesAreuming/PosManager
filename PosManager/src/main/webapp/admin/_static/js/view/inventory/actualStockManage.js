/*
 * Filename	: actualStockManage.js
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
  
  'text!templates/inventory/actualStockManage.html',
  'text!templates/inventory/modal/supplierModal.html',
  'text!templates/common/modal_form_alert.html',

  'bootstrap-daterangepicker'

], function (Backbone, Mustache, Common, i18n, template, modalform, modalAlert) {
  'use strict';

  var datatables;

  var targetUrl = './model/inventory/ActualStock';

  var _data = {};
  
  var userType = {};

  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events: {
    	'click button#search'				: 	 'search'
    	,'click button#atcualStock_save'	: 	 'actualStockSave'
		,'click .all-checked'				:	 'allChecked'
    	,'keyup #realCnt' 					:	 'changeActualStock'	
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
    }
    ,datatablesInit : function() {
    	datatables = $("#actualStock-tables").DataTable({
    		"ajax" : {
    			"url"        : 		targetUrl
    			,"type"   	 :		"POST"  // GET 방식일 때 request header data size가 특정 size를 넘어갈 때 to large error 발생
    			,"data"    	 :	    function (data) {
							    				var form = $('form').serializeObject();
									    		$.extend(form, _data);
									    		data._method = 'GET';
									    		return $.extend(data, form);
    										}
    			,"dataSrc"	 : 		"list"
    		}
    	
//		,"language" : {
//		"url" : "//cdn.datatables.net/plug-ins/1.10.11/i18n/English.json",
//	}
    	
    		,"columns" : [		// table data init
    		      /**
    		       *  columns.data 	:	 table 행의 열에 대한 데이터 소스를 설정
    		       *  columns.name   :	 data name 설정
    		       * */
    		      {"data" : "id"}
    		     ,{"data" : "itemCd", 						"name" : "ITEM_CD"}
    		     ,{"data" : "name", 						"name" : "NAME"}
    		     ,{"data" : "safeStockCnt", 			"name" : "SAFE_STOCK_CNT"}
    		     ,{"data" : "currentCnt",					"name" : "CURRENT_CNT", 			"className" : "current-cnt"}
    		     ,{"data" : "id"}
    		     ,{"data" : "id"}
    		 ]
    		
    		// columns에 의해 생성된 tr 데이터 재정의
			,"fnRowCallback" : function (row /* tr  */, data /* all column data */, index) {
				// columns data
				// 특정 데이터의 수가 0미만일 때 빨간색으로 표시
				if (data.currentCnt < 0) {
					$(row).find(".current-cnt").css('color', 'red');
				} 
			}
    		,"columnDefs" : [
	             	{
						   "render" : function (data, type, row) {
							   return '<span class="checked"><input type="checkbox" target-data="' + data + '" class="table-checkbox"></span>';
						   }
					   	  ,"bSortable": false
						  ,"targets" : 0
					}
					,{		//  안전 재고
						  "render": function ( data, type, row ) {
					          return Common.Common.JC_format.number(data);
					      }
					      ,"targets": 3
					}
					
					,{		// 현 재고
						  "render": function ( data, type, row ) {
					          return Common.Common.JC_format.number(data);
					      }
					      ,"targets": 4
					}
//    		                 
    		      ,{		// 실 재고
    		    	  "render": function ( data, type, row ) {
    		    		  return '<input type="text" class="form-control" id="realCnt" name="realCnt" value="' + row.currentCnt + '"/>';
//    			            return '<input type="text" class="form-control" id="realCnt" name="realCnt" value="' + Common.Common.JC_format.number(row.currentCnt) + '"/>';
    			        }
    		      		,"bSortable" : false
    			        ,"targets": 5
    		      }
    		      
    		      ,{	// 차이, 첫 화면 표시일 땐 실재고와 현재고의 수량은 같으므로 차이는 '0'으로 표시
    		    	  "render": function ( data, type, row ) {
    			            return '<p id="subCnt" name="subCnt">0</p>';
    			        }
    		      		,"bSortable" : false
    			        ,"targets": 6 
    		      }
    		]
    		    		
    	    ,"order": [[ 1, 'desc' ]]
    		,"serverSide"				: 	true			// 서버쪽에서 데이터 처리를 받음
    		,"pageLength" 			: 	10 			// 페이지 당 행의 수 설정
    		,"lengthChange" 		:	false		// 테이블 행 수 변경 (10, 25, 50, 100)
    		,"processing" 			: 	true 		// data return 전 까지 processing div 띄우기
    		,"rowReorder" 			: 	true			// 행 데이터 재 정렬??
    		,"searching"				:	false		// true : table search editor 지원
    	});
    }
    
    // 실재고 입력 값과 현재고의 값 비교 후 차이 표시
    ,changeActualStock : function (event) {
    	var target = $(event.target);
    	// 멤버 디테일 버튼 누를 때 해당 멤버의 정보를 멤버 상세 페이지에 저장
    	var data = datatables.row($(target).closest('tr').get(0)).data();
    	var subTxt = $(target).parent().parent().find("#subCnt");
    	var actualStock = target.val();
    	var currentStock = data.currentCnt;
    	var gapStock = actualStock - currentStock;
    	
    	if (target.val() == '' || target.val() == null || target.val() == undefined) {
    		target.val(0);
    	}
    	
    	if (gapStock < 0) {
    		subTxt.css("color", "red").text(Common.JC_format.number(gapStock));
    	} else {
    		subTxt.css("color", "black").text(Common.JC_format.number(gapStock));
    	}
    }
    
    // 실사 재고 저장
    ,actualStockSave : function () {
    	$.validate({
	   		 validateOnBlur 				: 	false		// element가 focus를 잃었을 때 유효검증 여부, 기본 값 true
	   		,scrollToTopOnError		: 	false		// 에러가 발생했을 때 에러 지점으로 스크롤링 여부?
	   		,showHelpOnFocus			: 	false		// ...
	   		,addSuggestions				: 	false		// ...
	   		,onError : function ($form) {
	   			alert(i18n.BO0010);
	   		}
	    	
	    	,onSuccess : function($form) {
	    		if (_data.storeId == '' || _data.storeId == undefined) {
	        		alert(i18n.BO4003);
	        		return false;
	        	}
	    		var list = $("#actualStock-tables").find('.table-checkbox:checked');
	        	var params = $form.serializeObject();
	        	var checkArray = [];
	        	
	        	if (list.length == 0) {
	        		alert(i18n.BO4058);
	        		return false;
	        	}
	        	
	        	$.each(list, function(idx, target) {
	        		var data = datatables.row($(target).closest('tr').get(0)).data();
	        		var gap = $(target).closest('tr').find("#subCnt").text();
	        		var realCnt = $(target).closest('tr').find("#realCnt").val();
	        		data.gap = gap;
	        		data.realCnt = realCnt;
	        		checkArray.push(data);
	        	});
	        	
	        	params.checkArray = checkArray;
	        	
	            $.ajax({
	                 url 		: 	targetUrl
	                ,type		:  	'POST'
	                ,data	: 	{
	                   "_method" : 'POST'
	                  ,"data" : JSON.stringify(params)
	                }
	                ,success : function(response){
	                  if(response.success){
	                	  alert(i18n.BO4046);
	                	  if (datatables != undefined) {
	    						datatables.draw();
	    					} else {
	    						view.datatablesReload();
	    					}
	                  }
	                }
	                ,error : function (response) {
	                	alert(i18n.BO0010);
	                }
	            });
	            
	            return false;
	    	}
    	});
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
			view.selectedInit(_data.brandId, 0);
		});
    		
		$('#selectStore').select2().on('change', function(){
			//브랜드와 해당 매장에 관련된 카테고리 분류정보 셋팅
			_data.storeId =  $(this).val();
			view.selectedInit(_data.brandId, _data.storeId);
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
    
    ,setCalendarArea : Common.JC_calendar.searchRange(i18n, true)
    
    ,selectedInit : function (vBrandId , vStoreId){
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

    }
    
    ,datatablesReload : function(){
    	var view = this;
    	if(datatables != undefined){
    		datatables.draw();
    	}
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
    }
    ,buildOptions : function(object) {
        for (var i in object) {
            object[i + '=' + object[i]] = true;
        }
        return object;
    }
   , loadBaseCode : function(obj){
    	var data = {};
		$.ajax({
			url : './model/Codes',
			method : 'POST',
			async: false,
			data : {
				'codes' : 'SvcSt,CountryCd'
			},
			success : function(result) {
				if (result.success) {

					$.each(Object.keys(result.codes), function(idx, code){
						$.each(result.codes[code], function(_idx, _code){
							for (var key in obj) {
								if (key.toLowerCase() == code) {
									if(code == "countrycd"){
										if (obj[key] == _code.title) {
											_code.selections = 'selected="selected"';
											break;
										}
									}else{
										if (obj[key] == _code.baseCd) {
											_code.selections = 'selected="selected"';
											break;
										}
									}									
								}
							}
						});
					});

					data = $.extend(result.codes, _data);
				}
				else {
		    		$('#brand-modal').html('');
		    		$('#brand-modal').modal('toggle');
				}
			}
		});

		return data;
    }
	,allChecked : function(event){    	
    	if($(event.target).is(':checked')){
    		$('#actualStock-tables').find('.table-checkbox').prop('checked', true);
    	} else {
    		$('#actualStock-tables').find('.table-checkbox').prop('checked', false);
    	}
    }
  });

  return ContentView;
});