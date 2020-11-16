/*
 * Filename	: posPluSetManage.js
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
  
  'text!templates/items/posPluSetManage.html',
  'text!templates/items/posPluSetManage_itemRow.html',
  'text!templates/items/posPluSetManage_cateType.html',
  'text!templates/common/modal_form_alert.html',

  'bootstrap-daterangepicker',
  'jquery-gridly'

], function (Backbone, Mustache, Common, i18n, template, posPluSetItemRow, posPluCateType, modalAlert) {
  'use strict';

  var _Id = 0;
  var _parentId = 0;
  var _ordinal = 0;
  var _pluTp   = 351001;
  var _data = {};

  var modelUrl = './model/items/PosPluSet';
  var itemsSelect = './model/items/ItemsSelect';
  var pluTypeSelect = './model/items/PluTypeSelect';

  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events : {
    	'change #category_select': 'selectItems',
    	'change #cateType_select': 'selectPluCateList',
    	'click .add' : 'addGridly',
    	'click .del' : 'delGridly',
    	'click .pluType' : 'pluTypeSet',
    	'click #savePluItem' : 'savePluItem'
    },
    initialize: function () {
    	this.trigger('remove-compnents');
        this.template = template;
        this.listenTo(this.model, 'sync', this.render);
    },
    render: function () {
    	var view = this;
        var data = {};
        data.i18n = i18n;
        
        data.frList = (view.model != undefined ? view.model.toJSON()[0].frList : {});
        data.brList = (view.model != undefined ? view.model.toJSON()[0].brList : {});
        data.stList = (view.model != undefined ? view.model.toJSON()[0].stList : {});
        
        _data = view.model.toJSON()[0].session.userInfo;
        data.userInfo = view.model.toJSON()[0].session.userInfo;
        
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

        view.selectedRollInit();

        view.selectedInit(_data.brandId, _data.storeId);
		view.selectedPluCateType(_data.brandId, _data.storeId);
        
      $( function() {
    	    $( "#sortableBox" ).sortable();
    	    $( "#sortableBox" ).disableSelection();
    	  } );

      return view;
    },
    addGridly : function (event){

    	if($("#cateType_select").val() == "" ){
    		alert(i18n.BO4055);
    		$("#cateType_select").focus();
    		return false;
    	}

		var _Id = $(event.target).attr('id');
		var itemName = $('#itemName_'+ _Id).val();
		var itemPrice = $('#itemPrice_'+ _Id).val();
		var image = $('#image_'+ _Id).val();
		
		if($('#dp_'+_Id).val() !== undefined){
			// BO4011 : 이미 추가 되었습니다
			alert(i18n.BO4011);
			return false;
		}

		var addLi = "<li class='ui-state-default' id='li_"+_Id+"'>" +
						"<span class='ui-icon ui-icon-arrow-4-diag'></span>" +
						"<div class='image thmb' style='width: 100%; height: 50%;'>" +
							"<div class='thmb-prev' style='width: 100%; height: 100%;'>" + 
								"<img src='"+image+"' class='img-responsive' alt='' style='width: 100%; height: 100%;'/>" +
							"</div>" +
							"<h5 class='fm-title'>"+itemName+"</h5>" +
							"<small class='text-muted'>"+Common.JC_format.number(itemPrice, true)+"</small>"+
							"<p class='data-box-del-2'><button type='button' class='btn btn-xs btn-danger del' id='del_"+_Id+"'>"+i18n.BO0016+"</button></p>"+
							"<input type='hidden' name='ordinal' value='1' id='dpOd_"+_Id+"' />"+
							"<input type='hidden' name='itemId' value='"+_Id+"' id='dp_"+_Id+"' />"+
						"</div>" +
					"</li>";
		$('#sortableBox').append(addLi);

    },
    delGridly : function (event){
    	var _Id = $(event.target).attr('id').replace('del_','');
    	
    	$('#li_'+_Id).remove();
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
      		view.selectedInit(0, 0);
			view.selectedPluCateType(0, 0);
			view.resetGridlyArea();
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
      				view.resetGridlyArea();
      			}  //END : store select result
      		});  //END : store select ajax
			//브랜드 선택 변경시
			view.selectedInit(_data.brandId, 0);
			view.selectedPluCateType(_data.brandId, 0);
//					view.blankMessage();
        });  //END : brand select change
    	
    	$('#store_select').select2().on('change', function(){
			//브랜드와 해당 매장에 관련된 카테고리 분류정보 셋팅
			_data.storeId =  $(this).val();
			view.selectedInit(_data.brandId, _data.storeId);
			view.selectedPluCateType(_data.brandId, _data.storeId);
			view.resetGridlyArea();
//			view.blankMessage();
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
					});
				}
			}  //END : category select result
		});  //END : category select ajax
    	$('#table2').find('tbody').find('tr').remove();
    },
    selectItems : function(){

    	if(_data.franId == "" || _data.franId == undefined){
    		// BO4001 : 법인을 선택해 주세요
    		alert(i18n.BO4001);
    		$("#franchise_select").focus();
    		return false;
    	}
    	
    	if(_data.brandId == "" || _data.brandId == undefined){
    		// BO4002 : 브랜드를 선택해 주세요
    		alert(i18n.BO4002);
    		$("#brand_select").focus();
    		return false;
    	}

    	if(_data.storeId == "" || _data.storeId == undefined){
    		// BO4003 : 매장을 선택해 주세요
    		alert(i18n.BO4003);
    		$("#store_select").focus();
    		return false;
    	}

		$.ajax({
			url : itemsSelect,
			data : {
				data : JSON.stringify({ brandId : _data.brandId, storeId : _data.storeId, catId : $("#category_select").val() })
			},
			success : function(result){
				if(result.success){

					var data = {};
					data.cate= result.list;
					data.i18n = i18n;
					data = Common.JC_format.handleData(data);
					
					$('#table2').find('tbody').find('tr').remove();
					$('#table2').find('tbody').append(Mustache.to_html(posPluSetItemRow, data));

				}
			}  //END : category select result
		});  //END : category select ajax

    },
    selectedPluCateType : function (vBrandId , vStoreId){

    	var view = this;

    	$.ajax({
			url : pluTypeSelect,
			data : {
				data : JSON.stringify({ brandId : vBrandId, storeId : vStoreId , pluTp : _pluTp , parent : 0})
			},
			success : function(result){
				if(result.success){

					var defualt_array = [{id: '', name : 'Category Type'}];
					$("#cateType_select").find('option').remove();
					$('#cateType_select').select2({
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
    selectPluCateList : function(){

    	var view = this;

    	if(_data.franId == "" || _data.franId == undefined){
    		// BO4001 : 법인을 선택해 주세요
    		alert(i18n.BO4001);
    		$("#franchise_select").focus();
    		return false;
    	}

    	if(_data.brandId == "" || _data.brandId == undefined){
    		// BO4002 : 브랜드를 선택해 주세요
    		alert(i18n.BO4002);
    		$("#brand_select").focus();
    		return false;
    	}

    	if(_data.storeId == "" || _data.storeId == undefined){
    		// BO4003 : 매장을 선택해 주세요
    		alert(i18n.BO4003);
    		$("#store_select").focus();
    		return false;
    	}

    	if($("#cateType_select").val() == ""){
//    		view.blankPluTypeMessage();
    		return false;
    	}

    	//해당아이디로 데이터가 있을 경우 정보를 가져와서 셋팅 해준다, 정렬 순서대로...
    	$.ajax({
			url : modelUrl,
			type: 'GET',
			data : {
				data : JSON.stringify({ brandId : _data.brandId, storeId : _data.storeId , catId : $("#cateType_select").val()})
			},
			success : function(result){
				if(result.success){
					//조회완료 정보
					view.setPluItemGridly(result.list);
				}else{
					//요청결과 실패 정보
				}
			}
		});
    },

    setPluItemGridly : function (pluItemList){

    	var view = this;
    	view.resetGridlyArea(); // item list 초기화
    	
    	$.each(pluItemList, function(idx, data){

    		var addLi = "<li class='ui-state-default' id='li_"+data.itemId+"'>" +
						"<span class='ui-icon ui-icon-arrow-4-diag'></span>" +
						"<div class='image thmb' style='width: 100%; height: 50%;'>" +
			            "<div class='thmb-prev' style='width: 100%; height: 100%;'>" + 
			            "<img src='"+data.image+"' class='img-responsive' alt='' style='width: 100%; height: 100%;'/>" +
			            "</div>" +
			            "<h5 class='fm-title'>"+data.name+"</h5>" +
		                "<small class='text-muted'>"+Common.JC_format.number(data.price, true) +"</small>"+
						"<p class='data-box-del-2'><button type='button' class='btn btn-xs btn-danger del' id='del_"+data.itemId+"'>"+i18n.BO0016+"</button></p>"+
						"<input type='hidden' name='ordinal' value='1' id='dpOd_"+data.itemId+"' />"+
						"<input type='hidden' name='itemId' value='"+data.itemId+"' id='dp_"+data.itemId+"' />"+
			            "</div>" +
						"</li>";
    		$('#sortableBox').append(addLi);
    	});
    },
    savePluItem : function (event) {

    	var view = this;

    	if(_data.brandId == "" || _data.brandId == undefined){
    		// BO4002 : 브랜드를 선택해 주세요
    		alert(i18n.BO4002);
    		$("#brand_select").focus();
    		return false;
    	}

    	if(_data.storeId == "" || _data.storeId == undefined){
    		// BO4003 : 매장을 선택해 주세요
    		alert(i18n.BO4003);
    		$("#store_select").focus();
    		return false;
    	}
    	
    	if($("#cateType_select").val() == ""){
    		alert(i18n.BO4055);
    		$("#cateType_select").focus();
    		return false;
    	}

    	//pluItem 카드패널의 데이터
    	var _itemData  = [];
    	var params = $("#gdlForm").serializeArray();
    	var _ordinal = 0;

    	$.each(params, function(idx, param){

    		var obj = {};

    		obj.brandId = _data.brandId;
        	obj.storeId = _data.storeId;
        	obj.catId = $("#cateType_select").val();

    		if(param.name == 'itemId'){
    			_ordinal = _ordinal + 1;
    			obj.itemId = param.value;
    			obj.ordinal = _ordinal;
    			_itemData.push(obj);
    		}
    	});

    	if($("#cateType_select").val() != ""){
    		$.ajax({
    			url : modelUrl,
    			type: 'POST',
    			data : {
    				dataList : JSON.stringify(_itemData),
    				data : JSON.stringify({ brandId : _data.brandId, storeId : _data.storeId , catId : $("#cateType_select").val()})
    			},
    			success : function(result){
    				if(result.success){
    					alert(i18n.BO4046);
    					view.selectPluCateList();
    				}else{
    					alert(i18n.BO4047);
    					view.resetGridlyArea();
    				}
    			}
    		});
    	}else{
    		view.setPluItemGridly();
    	}
    },

    resetGridlyArea : function (){
    	$('#pluTypeId').val("");
    	$('#sortableBox').find('li').remove(); //item list 초기화
    }

  });
  return ContentView;
});