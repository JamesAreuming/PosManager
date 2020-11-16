/*
 * Filename	: itemsManage.js
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
  
  'text!templates/items/itemsManage.html',
  'text!templates/items/modal/items_modal.html',
  'text!templates/items/modal/itemsOptDtl_modal.html',
  'text!templates/items/modal/items_modal_multiTable.html',
  'text!templates/common/modal_form_alert.html',
  
  'bootstrap-daterangepicker'

], function (Backbone, Mustache, Common, i18n, template, modalform, modalOptDtlform, modalMultiTable, modalAlert) {
  'use strict';

  var _data = {};
  var targetUrl = './model/items/Items';
  var imgUpTargetUrl = "./model/items/Items/Image";
  var itemOptTargetUrl = "./model/items/ItemOption";
  var getItemOptAndDtlUrl = "./model/items/GetItemOptAndDtl";
  var main_tables;

  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events : {
    	'click #new-items' : 'modal_init',
    	'click #dtl-items' : 'modal_detail',
    	'click button#search' : 'search',
    	'click input[name="isOptUsed"]' : 'isOptUsed',
    	'change #mainImage' : 'mainImagePreView',
    	'click button#saveOptListUpt' : 'saveOptListUpt',
    	'click button#addItemOptDtl' : 'addItemOptDtl',
    	'click button.optDel' : 'optDel',
    	'click button.optEdtInit' : 'optEdtInit',
    	'change #optCount_select' : 'optCountSelect',
    	'click button.optListDel' : 'optListDel',
    	'click button#addOpt' : 'addOpt',
    	'click button#getItemOptData' : 'getItemOptDataInit',
    	'click button.getItemOptDataDtl' : 'getItemOptDataDtlInit',
    	'click button#getOtherItemOptDataSave' : 'getOtherItemOptDataSave',
    	'click .add-on-datepicker' : 'trigger_calender',
    	'change #itemTp' : 'isOptUsed',
    	'click #imgFile' : 'triggerFile',
    	'click #isStockUse' : 'showStockCnt',
    	'keydown input[type="number"]' : 'onlyNumber',
    	'keyup input[type="number"]' : 'removeChar'
    		
    },
    initialize: function () {
    	this.trigger('remove-compnents');
    	this.template = template;
    	this.listenTo(this.model, 'sync', this.render);
    },
    render: function () {
    	var view = this;
    	var data = {};
    	
    	data.statusCd = view.loadBaseCode('ItemStatus', true);
    	
    	data.frList = (view.model != undefined ? view.model.toJSON()[0].frList : {});
        data.brList = (view.model != undefined ? view.model.toJSON()[0].brList : {});
        data.stList = (view.model != undefined ? view.model.toJSON()[0].stList : {});
        
        _data.userInfo = view.model.toJSON()[0].session.userInfo;
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
        
        if(_data.franId == "" || _data.franId == undefined){
//    		BO4001 : 법인을 선택해 주세요 
    		_data.franId = _data.userInfo.frandId;
    	}
    	
    	if(_data.brandId == "" || _data.brandId == undefined){
//    		BO4002 : 브랜드를 선택해 주세요
    		_data.brandId = _data.userInfo.brandId; 
    	} 
    	
    	if(_data.storeId == "" || _data.storeId == undefined){
//    		BO4003 : 매장을 선택해 주세요
    		console.log(_data.userInfo);
    		if(_data.userInfo.storeId != "" && _data.userInfo.storeId != undefined){
    			_data.storeId = _data.userInfo.storeId;
    		} 
    	}         
        
    	var rendered = Mustache.to_html(view.template, data);
    	$(view.el).append(rendered);
        
    	view.selectedRollInit();	// 법인, 브랜드, 스토어 정보 로딩

    	view.selectedInit(_data.brandId, _data.storeId);
    	
    	view.datatablesInit();	// 데이터 테이블 로딩

    	$(document).ready(function(){
    	    $("input[name=searchKeyword]").keydown(function (key) {
    	        if(key.keyCode == 13){//키가 13이면 실행 (엔터는 13)
    	        	view.search();
    	        	return false;
    	        }
    	    });
    	});

      	return view;
    },
    isOptUsed : function() {
    	var view = this;
    	if($('#itemTp').val() == "818007"){
    		$('#optDiv').show();
    		$('#isOptUsed').attr("value",true);
    		view.itemOptionList();
    	}else{
    		$('#optDiv').hide();
    		$('#isOptUsed').attr("value",false);
    	}
//    	if($('input[name="isOptUsed"]:checked').val() == "true"){
//            $('#optDiv').show();
//            view.itemOptionList();
//        } else {
//            $('#optDiv').hide();
//        }
    },
    
    selectedRollInit : function (){
    	var view = this;

    	$('.select').select2({
    		width: '100%',
            minimumResultsForSearch: -1
    	});
    	
    	$('.select2').select2({
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
      				view.selectedInit(_data.brandId, _data.storeId);
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
			//브랜드 선택 변경시
			view.selectedInit(_data.brandId, _data.storeId);
        });  //END : brand select change
    	
    	$('#store_select').select2().on('change', function(){
			//브랜드와 해당 매장에 관련된 카테고리 분류정보 셋팅
			_data.storeId =  $(this).val();
			view.selectedInit(_data.brandId, _data.storeId);
        });
    },
    selectedInit : function (vBrandId , vStoreId){
    	/**
    	 * 1.브랜드, 상점 id 별 카테고리 분류 (최소한 브랜드는 선택해야함)
    	 * 2.판매여부 Y/N ==> 공통코드에 추가 사용 해야함.
    	 * 3.등록구분 브랜드/매장
    	 */

		$.ajax({
			url : './model/items/CateSelect',
			data : {
				data : JSON.stringify({ brandId : vBrandId, storeId : vStoreId })
			},
			success : function(result){
				if(result.success){
					var defualt_array = [{id: '', name : 'AllCategory'}];
					$("#category_select").find('option').remove();
					$('#category_select').select2({
						data : defualt_array.concat(result.list),
						templateResult : function(state) {
							return state.name;
						},
						templateSelection : function(data, container) {
						    return data.name;
						}
					})
				}
			}  //END : category select result
		});  //END : category select ajax
    },
    datatablesInit : function(){

    	var _tmpOutPut = "";

    	main_tables = $('#items_table').DataTable({
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
		    		data.data = $('#rollSearchFrom').serializeObject();
		    		
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
			  { "data" : "id", "name" : "id" },
			  { "data" : "name", "name" : "name"},
			  { "data" : "price", "name" : "price", render: function (data, type, row) {return Common.JC_format.number(data);}},
			  { "data" : "status", "name" : "status" , 
				  render: function ( data, type, row ) {
					  	if(data == '602001'){
					  		_tmpOutPut = "On sale";
					  	}else if(data == '602002'){
					  		_tmpOutPut = "Suspension of sale";
					  	}else{
					  		_tmpOutPut = "N/A";
					  	}
					  return _tmpOutPut;
				  }
			  },
			  { "data" : "storeId", "name" : "store_id",
				  render: function ( data, type, row ) {
					  	if(data == null){
					  		_tmpOutPut = i18n.BO1032;
					  	}else{
					  		_tmpOutPut = i18n.BO1033;
					  	}
					  return _tmpOutPut;
				  }
			  },
			  { "data" : "id"}
			],
		    "columnDefs":  [ {
		        "render": function ( data, type, row ) {
		            return '<button type="button" class="btn btn-xs btn-primary" data-toggle="modal" data-target=".bs-item-modal-lg" id="dtl-items" target-code="'+data+'">' + i18n.BO0022 + '</button>';
		        },
		        "bSortable": false,
		        "targets": 5
		    } ],
		    "order": [[ 1, 'asc' ]],
		    "pageLength": 10,
		    "lengthChange": false,
		    "processing": true,
		    "rowReorder": true,
		    "searching": false,
		    "rowId" : 'id'
		});
    },
    keyAction: function(e) {

    	var view = this;
        var code = e.keyCode || e.which;
        if(code == 13) {
        	view.search();
        }
    },
    search : function(){ 
    	
    	if(_data.franId == "" || _data.franId == undefined){
//    		BO4001 : 법인을 선택해 주세요
    		//alert(i18n.BO4001);
    		//$("#franchise_select").focus();
    		//return false;
    		_data.franId = _data.userInfo.frandId;
    	}
    	
    	if(_data.brandId == "" || _data.brandId == undefined){
//    		BO4002 : 브랜드를 선택해 주세요
    		_data.brandId = _data.userInfo.brandId;
    		//alert(i18n.BO4002);
    		//$("#brand_select").focus();
    		//return false;
    	} 
    	
    	if(_data.storeId == "" || _data.storeId == undefined){
//    		BO4003 : 매장을 선택해 주세요
    		console.log(_data.userInfo);
    		if(_data.userInfo.storeId != "" && _data.userInfo.storeId != undefined){
    			_data.storeId = _data.userInfo.storeId;
    		}else{
        		alert(i18n.BO4003);
        		$("#store_select").focus();
        		return false;
    		}
    	} 
 
//    	var view = this;    	
//    	var $form = $('#rollSearchFrom');
//    	_data= $.extend(_data, $form.serializeObject());
//    	
//    	$.each(Object.keys(_data), function(idx, _key){
//    		if ( _data[_key] == "" || _data[_key] === null || (typeof _data[_key] == "object" &&  $.isEmptyObject(_data[_key])) ) {
//    			delete _data[_key];
//    		}
//    	});

    	if(main_tables != undefined){
    		main_tables.draw();
    	}else{
    		view.datatablesInit();
    	}
    },
    modal_init : function(){
    	var view = this;
    	var $form = $('#rollSearchFrom');
    	_data= $.extend(_data, $form.serializeObject());

    	if(_data.brandId != undefined && _data.brandId != ''){

			if($("#category_select option:selected").val() == ""){
				// BO4006 : 카테고리 정보를 입력해 주세요
				alert(i18n.BO4006);
	    		return false;
			}

    		var data = view.loadBaseCode('ItemTp,ItemStatus,MbIcon,TaxTp' , false);
    		data.i18n = i18n;

			$('#items-modal').html(Mustache.to_html(modalform, data));

			view.form_init('POST', $('#items-modal'));
    	} else {
    		// BO4002 : 브랜드를 선택해 주세요
    		alert(i18n.BO4002);
    		return false;
    	}
    },
    
    setCalendarArea : Common.JC_calendar.searchRange(i18n, true),
    
    onlyNumber : function (event){
		event = event || window.event;
		var keyID = (event.which) ? event.which : event.keyCode;
		if ( (keyID >= 48 && keyID <= 57) || (keyID >= 96 && keyID <= 105) || keyID == 8 || keyID == 46 || keyID == 37 || keyID == 39 ){
			return;
		}else{
			return false;
		}	
	},
	
	removeChar : function (event) {
		event = event || window.event;
		var keyID = (event.which) ? event.which : event.keyCode;
		if ( keyID == 8 || keyID == 46 || keyID == 37 || keyID == 39 ){
			return;
		}else{
			event.target.value = event.target.value.replace(/[^0-9]/g, "");
		}	
	},
    
    triggerFile : function() {
    	var file = $("#mainImage").click();
    },
    
    modal_detail : function(event){
    	var view = this;
    	var _this = $(event.target);
    	$.ajax({
    		url : targetUrl,
    		data : {
    			data : JSON.stringify({
    				id : _this.attr("target-code"),
    			})
    		},
    		success : function(response){
    			if(response.success){
    				var data = response.list;
    				var code = view.loadBaseCode('ItemTp,ItemStatus,MbIcon,TaxTp' , true);

    				//데이터 컬럼을 공통코드 alias 이름으로 매핑
    				data.ItemStatus =  data.status;
    				data.ItemTp =  data.itemTp;
    				data.MbIcon =  data.mbIconTp;
    				data.imgInfo = response.itemImg;
    				data.taxTp = data.taxTp;
    				data.i18n = i18n;
    				
    				data = Common.JC_format.handleData(data);
    				
    				var _template = Mustache.to_html(modalform, $.extend(code, data ));
    				
    				$('#items-modal').html(Mustache.to_html(_template, view.buildOptions(response.list)));
    				view.form_init('PUT', $('#items-modal'));
    			}
    		}
    	});
    },
    form_init : function(method, modal){

    	var view = this;
    	modal.find('select').select2({
    		width: '100%',
            minimumResultsForSearch: -1
    	});

		$.validate({
			validateOnBlur : false,
		    scrollToTopOnError : false,
		    showHelpOnFocus : false,
		    errorMessagePosition : $('.form-validatoin-alert-detail'),
		    addSuggestions : false,
		    onError : function($form) {
		    	var _target = $(".modal-body");
		    	
		    	if(_target.find('.alert').length == 0){
		    		_target.prepend(Mustache.to_html(modalAlert, {i18n:i18n}));
		    	}
	        },
	        onSuccess : function($form) {
        		var params = $form.serializeObject();
        		$.each(params, function(key, value) {
        	        if (key == 'itemOptId') {
        	        	delete params[key];
        	        }
        	    });
	        	$.ajax({
	        		url : targetUrl,
	        		type: 'POST',
	        		data : {
	        			"_method" : method,
	        			"data" : JSON.stringify(params)
	        		},
	        		success : function(response){
	        			if(response.success){
	        				var _data = new FormData();
							var _file = $form.find('#mainImage').get(0);

							if(_file.files[0]){
								_data.append("imagefile", _file.files[0]);
								_data.append("itemId", response.itemId);
								$.ajax({
									data: _data,
									type: "POST",
									url: imgUpTargetUrl,
									async: false,
									cache: false,
									contentType: false,
									processData: false,
									success: function(result) {
										if(!result.success){
											//  BO4008 : 이미지 등록에 실패 하였습니다
											alert(i18n.BO4008);
										}
									}
								});
							}
							alert(i18n.BO4046);
	    		    		$('#items-modal').modal('toggle');
	    		    		main_tables.draw();
	        			}
	        		}
	        	});
	        	return false;
	        }
		});
		
		view.setCalendarArea();
		view.isOptUsed(); //옵션 정보
    },
    loadBaseCode : function(codeAlias, option){

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
					
					data = $.extend(result.codes, _data);

				} else {
		    		$('#items-modal').html('');
		    		$('#items-modal').modal('toggle');
				}
			}
		});
		return data;
    },
    mainImagePreView : function(event){
		var _this = $(event.target);
		var ext = _this.val().split('.').pop().toLowerCase(); //확장자
        if($.inArray(ext, ['gif', 'png', 'jpg', 'jpeg']) == -1) {
        	if ($.browser.msie) {
        		// ie 일때 input[type=file] init.
        		_this.replaceWith( $("#filename").clone(true) );
        	} else {
        		// other browser 일때 input[type=file] init.
        		_this.val("");
        	}
        	// BO4009 : 이미지 파일이 아닙니다
        	// BO4010 : gif, png, jpg, jpeg 만 업로드 가능
            window.alert(i18n.BO4009 + "(" + i18n.BO4010 + ")");
        } else {
            var file = _this.prop("files")[0];
            var blobURL = window.URL.createObjectURL(file);

            var _image = $('<img />').attr('src', blobURL).attr('class', 'img_item');
            $('#fileName').html(file.name);
            $('#items-modal').find('#main_img_target').find('.img_item').attr('src', blobURL);
            $('#items-modal').find('#main_img_container').show();
        }
	},
    buildOptions : function(object) {
        for (var i in object) {
            object[i.toLowerCase() + '=' + object[i]] = true;
        }
        return object;
    },
 
    itemOptionList : function(optEvent){

    	var view = this;
    	var _itemId = "";
    	_itemId = $('#id').val();

    	$.ajax({
			url : itemOptTargetUrl,
			type: 'GET',
			data : {
				data : JSON.stringify({ itemId : _itemId})
			},
			success : function(result){
				if(result.success){
					//조회완료 정보
					view.setItemOptListGrid(result.list);
					view.saveOptListUpt('reflash');
				}else{
					//요청결과 실패 정보
				}
			}
		});
    },
    setItemOptListGrid : function(itemOptList){

	    $( "#sortable" ).sortable();
	    $( "#sortable" ).disableSelection();

	    var view = this;
	    view.resetGridlyArea(); // item list 초기화

    	$.each(itemOptList, function(idx, data){

    		if(data.isMandatory == true){ 
    			data.isMandatory = i18n.BO2241;
    		}else{ 
    			data.isMandatory = i18n.BO0024; 
    		}
    		
    		if(data.isUsed == true){ 
    			data.isUsed = 'Y';
    		}else{ 
    			data.isUsed = 'N'; 
    		}

    		var addLi = "<li class='ui-state-default' id='li_"+data.id+"'>"+
						"<span class='ui-icon ui-icon-arrowthick-2-n-s'></span>"+
						"<p class='data-box-a-1'>"+data.name+"</p>"+
						"<p class='data-box-b-1'>"+data.isMandatory+"</p>"+
						"<p class='data-box-c-1'>"+data.isUsed+"</p>"+
						"<p class='data-box-mng'>"+
						"<button type='button' class='btn btn-xs btn-primary optEdtInit' id='optEdtInit_"+data.id+"'>"+i18n.BO1037+"</button> "+
						"<button type='button' class='btn btn-xs btn-danger optDel' id='optDel_"+data.id+"'>"+i18n.BO0016+"</button>"+
						"</p>"+
						"<input type='hidden' name='itemOptId' value='"+data.id+"' id='dp_"+data.id+"' />"+
						"</li>";

    		$('#sortable').append(addLi);

    	});

    },
    saveOptListUpt : function(optEvent){

    	var view = this;
    	// 데이터
    	var _itemData  = [];
    	var params = $("#gdlForm").serializeArray();
    	var _ordinal = 0;

    	$.each(params, function(idx, param){

    		var obj = {};

    		if(param.name == 'itemOptId'){
    			_ordinal = _ordinal + 1;
    			obj.id = param.value;
    			obj.ordinal = _ordinal;
    			_itemData.push(obj);
    		}
    	});

    	$.ajax({
			url : itemOptTargetUrl,
			type: 'POST',
			data : {
				"_method" : "PUT",
				mode : "optListUpt",	//옵션리스트 업데이트
				dataList : JSON.stringify(_itemData)
			},
			success : function(result){
				if(result.success){
					if(optEvent != 'reflash'){
						alert(i18n.BO4046);
					}
				}else{
					alert(i18n.BO4047);
				}
			}
		});

    },
    optDel : function(event){

    	//데이터
    	var _Id = $(event.target).attr('id').replace('optDel_','');

    	// BO4015 : 삭제 하시겠습니까?
    	if (confirm(i18n.BO4015)) {

    		$.ajax({
    			url : itemOptTargetUrl,
    			type: 'POST',
    			data : {
    				"_method" : "DELETE",
    				mode : "optListDel",
    				data : JSON.stringify({id : _Id})
    			},
    			success : function(result){
    				if(result.success){
    					$('#li_'+_Id).remove();
    					alert(i18n.BO4049);
    				}else{
    					alert(i18n.BO4050);
    				}
    			}
    		});
    	}
    },
    resetGridlyArea : function (){
    	$('#sortable').find('li').remove(); //item list 초기화
    },
    addItemOptDtl : function(){
    	var view = this;
    	_data.itemId = $('#id').val();

    	var _test = {};
    	_test.i18n = i18n;
    	
    	if(_data.brandId != undefined && _data.brandId != ''){
			$('#items-option-modal').html(Mustache.to_html(modalOptDtlform, _test));
			view.optDtlform_init('POST', $('#items-option-modal'));
    	} else {
    		// BO4002 : 브랜드를 선택해 주세요
    		alert(i18n.BO4002);
    		return false;
    	}
    },
    optDtlform_init : function(method, modal){
    	var view = this;
    	modal.find('select').select2();
    	$('#items-option-modal').modal('toggle');
    },
    optEdtInit : function(event){

    	var view = this;
    	//수정하기 위한 데이터 호출
    	var _Id = $(event.target).attr('id').replace('optEdtInit_','');
    	_data.itemId = $('#id').val();

    	$.ajax({
			url : itemOptTargetUrl,
			type: 'GET',
			data : {
				data : JSON.stringify({ id : _Id})
			},
			success : function(result){
				if(result.success){
					//조회완료 정보
					//1. 옵션 마스터 정보
					var optData = result.optInfo;
					//2.옵션상세정보
					optData.optDtl =result.optDtlList;
					optData.i18n = i18n;
					
					$('#items-option-modal').html(Mustache.to_html(modalOptDtlform, optData));
					view.optDtlform_init('POST', $('#items-option-modal'));

				}else{
					//요청결과 실패 정보
				}
			}
		});

    },
    optCountSelect : function(){

    	var view = this;
    	//view.resetOptDtlList();
    	var trCnt = $('#optDtlListTbl >tbody >tr').length;

    	//옵션선택 수량
    	var _optCnt = 0;
    		_optCnt = $('#optCount_select option:selected').val();

    	if(_optCnt >= trCnt ){
    		_optCnt = _optCnt - trCnt;
    	}else{
    		_optCnt = 0;
    	}

    	var _optId = $('#optId').val();
    	var tmpNum = 900; //임의 수자배정

    	if(_optCnt !=''){
	    	for(var i=0; i<_optCnt; i++){

		    	var addTr = "<tr id='optDtlTr_"+(tmpNum+i)+"'>"+
							"<td><input type='hidden' name='optId' value='"+_optId+"' class='form-control'>"+
							"<input type='text' name='optCd' value='' class='form-control'></td>"+
							"<td><input type='text' name='name' value='' class='form-control'></td>"+
							"<td><input type='text' name='price' value='' class='form-control'></td>"+
							"<td><button type='button' class='btn btn-xs btn-danger optListDel' id='optListDel_"+(tmpNum+i)+"'>- "+i18n.BO0016+"</button></td>"+
							"</tr>";
				$('#optDtlTbl').append(addTr);
	    	}
	    }
    },
    optListDel : function(event){
    	var _Id = $(event.target).attr('id').replace('optListDel_','');
    	$('#optDtlTr_'+_Id).remove();
    },
    resetOptDtlList : function (){
    	$('#optDtlTbl').find('tr').remove(); // list 초기화
    },
    addOpt : function(){

    	var view = this;
    	var _optId = $('#optId').val();
    	var _method = "POST";

    	if(_data.itemmId == ''){
    		alert(i18nBO4051);
    		return false;
    	}

    	//validation
    	// 옵션명, 최대선택수 확인, 옵션코드, 항목명,추가요금 - 항목삭제기능
    	if($('#optNm').val() == ''){
    		alert(i18n.BO4052);
    		return false;
    	}

    	if(_optId != '' && _optId != undefined){
    		_method = "PUT";
    		
    		if($('#optCount_select option:selected').val() == ''){
				alert(i18n.BO4053);
	    		return false;
			}
    	}

    	//1. 옵션 마스터 정보 데이터
    	var params = $("#itemOptDtlModalForm").serializeObject();

    	//2. 디테일 table tr별 정보 데이터
    	var _itemData = [];
		var _ordinal = 0;

		$('#itemOptDtlDataForm tr').each(function(i) {
			var obj = {}
			$(this).find("input").each(function(index, el){
		     	obj[el.name] = el.value;
			});
			if( i > 0){  //tr title 항목 다음부터
				_ordinal = _ordinal + 1;
				obj.ordinal = _ordinal;
				_itemData.push(obj);
			}
		 });

		_data= $.extend(_data, params);

    	$.ajax({
			url : itemOptTargetUrl,
			type: 'POST',
			data : {
				"_method" : _method,
				mode : "optAndDtlInst",	// 마스터, 디테일 테이블 동시 저장시
				data : JSON.stringify(_data),
				dataList : JSON.stringify(_itemData)  //table .tr : row data
			},
			success : function(result){
				if(result.success){
					alert(i18n.BO4046);
					view.itemOptionList();
					$('#items-option-modal').modal('toggle');
				}else{
					alert(i18n.BO4047);
				}
			}
		});
    },
    getItemOptDataInit : function(){

    	$.ajax({
			url : getItemOptAndDtlUrl,
			type: 'GET',
			data : {
				mode : "getOptData"
			},
			success : function(result){
				if(result.success){
					var optData = {};
					//조회완료 정보
					optData.list = result.list;
					optData.i18n = i18n;
					
					$('#items-getItemOpt-modal').html(Mustache.to_html(modalMultiTable, optData));
					$('#items-getItemOpt-modal').modal('toggle');

				}else{
					//요청결과 실패 정보
				}
			}
		});

    },
    getItemOptDataDtlInit : function(event){

    	var view = this;
    	$('#getDtlDataTr').find('tr').remove(); // list 초기화

    	var _optId = $(event.target).attr('id').replace('getItemOpt_','');

    	$.ajax({
			url : getItemOptAndDtlUrl,
			type: 'GET',
			data : {
				mode : "getOptDtlData",
				data : JSON.stringify({ id : _optId})
			},
			success : function(result){
				if(result.success){
					//옵션상세정보
					$.each(result.optDtlList, function(idx, data){
						var addTr = "<tr>"+
									"<td>"+data.name+"</td>"+
									"<td>"+data.price+"</td>"+
									"</tr>";
						$('#getDtlDataTr').append(addTr);
					});
				}else{
					//요청결과 실패 정보
				}
			}
		});

    },
    getOtherItemOptDataSave : function(){

    	var view = this;
    	var _optId = $("input:radio[name='rOptId']:checked").val();

    	if(_optId == ''){
    		alert(i18n.BO4054);
    		return false;
    	}

    	_data.itemId = $('#id').val();
    	_data.id = _optId;

    	$.ajax({
			url : getItemOptAndDtlUrl,
			type: 'POST',
			data : {
				"_method" : "PUT",
				mode : "otherItemOptDataSave",
				data : JSON.stringify(_data)
			},
			success : function(result){
				if(result.success){
					//옵션상세정보
					alert(i18n.BO4046);
					view.itemOptionList();
					$('#items-getItemOpt-modal').modal('toggle');
				}else{
					//요청결과 실패 정보
					alert(i18n.BO4047);
				}
			}
		});
    },
    trigger_calender : function(event){
    	var _this = $(event.target);
    	var _cal = _this.closest('div').find('.date-picker');
    	_cal.trigger('click');    	
    },
    
    // Available Stock Flag 선택에 따라 Available Stock Cnt 에디트 박스 readonly toggle
    showStockCnt : function (event){
    	if(event.target.value == "true"){
    		$('#safeStockCnt').attr('readonly',false);
    		
    	}else if(event.target.value == "false"){
    		$('#safeStockCnt').val('0');
    		$('#safeStockCnt').attr('readonly',true);
    	}
    }
  });

  return ContentView;
});