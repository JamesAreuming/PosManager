/*
 * Filename	: categoryManage.js
 * Function	:
 * Comment 	: 카테고리는 브랜드 선택 후 부터 등록 가능 해야 함,
 *            브랜드에서 생성된 카테고리는 매장에서 변경 불가함,
 *            스토어(매장)가 있을경우 스토어기준으로 카테고리 생성.
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
  
  'text!templates/items/categoryManage.html',
  'text!templates/items/modal/category_tree.html',
  'text!templates/items/modal/category_form.html',
  'text!templates/common/modal_form_alert.html',

  'bootstrap-daterangepicker',
  'maxazan-jquery-treegrid'

], function (Backbone, Mustache, Common, i18n, template, categoryTree, categoryForm, modalAlert) {
  'use strict';

  var _Id = 0;
  var _parentId = 0;
  var _data = {};
  var modelUrl = './model/items/Category';

  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events : {
    	'click #addCate' 			: 'addCate',
    	'click button.saveCate' 	: 'saveCate',
    	'click button.removeCate' 	: 'removeCate',
    	'click button.edit' 		: 'editSubCate',
    	'click button.editUpt' 		: 'editSubCateUpt',
    	'click button.cancleUpt' 	: 'cancleSubCateUpt',
    	'click button.add' 			: 'addSubCate',
    	'click button.del' 			: 'delSubCate',
    	'click #cateCloseAll' 		: 'closeAll',
    	'click #cateOpenAll' 		: 'openAll'
    },
    initialize: function () {

      this.trigger('remove-compnents');
      this.template = template;
      this.listenTo(this.model, 'sync', this.render);
    },
    render: function () {
      var view = this;
      var data = {};

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

      view.selectedRollInit();

      view.selectedInit(_data.brandId, _data.storeId);
      
      //tree grid
      view.openAll();

      return view;
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
    	var view = this;
    	
		$.ajax({
			url : modelUrl,
			data : {
				data : JSON.stringify({ brandId : vBrandId, storeId : vStoreId })
			},
			success : function(result){
				if(result.success){
					var data = {};
					data.cate= result.list;
					data.i18n = i18n;
					$('.tree').find('tr').remove();
					$('.tree').find('tbody').append(Mustache.to_html(categoryTree, data));
					view.openAll();
				}
			}  //END : category select result
		});  //END : category select ajax
    },
    renderTreegrid : function(){
    	var view = this;
    	view.selectedInit(_data.brandId, _data.storeId);
    },
    addCate : function(){
    	var _brandId = "";
    	if(_data.franId == ""){
//    	if($("#franchise_select").val() == ""){
    		// BO4001 : 법인을 선택해 주세요
    		alert(i18n.BO4001);
    		$("#franchise_select").focus();
    		return false;
    	}

//    	_brandId = $("#brand_select").val();
    	if(_data.brandId == ""){
//    	if(_brandId == ""){
    		// BO4002 : 브랜드를 선택해 주세요
    		alert(i18n.BO4002);
    		$("#brand_select").focus();
    		return false;
    	}

    	if($('#treegrid-0').length > 0){
    		// BO4004 : 먼저 추가된 카테고리 정보를 저장해 주세요
    		alert(i18n.BO4004);
    		$("#tmpName").focus();
    		return false;
    	}
    	_data.nodeId = _Id;
    	_data.i18n = i18n;
    	$('.tree').find('tbody').prepend(Mustache.to_html(categoryForm, _data));
    	$('#tmpParentId').val(0);// 신규등록시 초기화

    },
    addNextCate : function(nodeId){
    	var _brandId = "";

    	if(_data.franId == ""){
//    	if($("#franchise_select").val() == ""){
    		// BO4001 : 법인을 선택해 주세요
    		alert(i18n.BO4001);
    		$("#franchise_select").focus();
    		return false;
    	}

//    	_brandId = $("#brand_select").val();
    	if(_data.brandId == ""){
    		// BO4002 : 브랜드를 선택해 주세요
    		alert(i18n.BO4002);
    		$("#brand_select").focus();
    		return false;
    	}

    	if($('#treegrid-0').length > 0){
    		// BO4004 : 먼저 추가된 카테고리 정보를 저장해 주세요
    		alert(i18n.BO4004);
    		$("#tmpName").focus();
    		return false;
    	}

    	_data.nodeId = nodeId;
    	_data.i18n = i18n;
    	$('#treegrid-'+nodeId).after(Mustache.to_html(categoryForm, _data));
    },
    saveCate : function(ev){
    	var view = this;
    	var _name = "";
    	var _brandId = "";
    	var _storeId = "";
    	var _cateCd = "";

    	if(_data.franId == ""){
//    	if($("#franchise_select").val() == ""){
    		// BO4001 : 법인을 선택해 주세요
    		alert(i18n.BO4001);
    		$("#franchise_select").focus();
    		return false;
    	}

    	
//    	_brandId = $("#brand_select").val();
    	if(_data.brandId == ""){
//    	if(_brandId == ""){
    		// BO4002 : 브랜드를 선택해 주세요
    		alert(i18n.BO4002);
    		$("#brand_select").focus();
    		return false;
    	}

    	//스토어선택
    	_storeId = $("#store_select").val();
    	if(_storeId != ""){
//    		alert("선택하신 매장 기준으로 카테고리 정보가 등록 됩니다.");
    	}

    	_name = $("#tmpName").val();
        if(_name == ''){
        	// BO4004 : 먼저 추가된 카테고리 정보를 저장해 주세요
    		alert(i18n.BO4004);
        	$("tmpName").focus();
      	  return false;
        }

        if($('#tmpParentId').val() != ''){
        	_parentId = $('#tmpParentId').val();
        }

        var _cateCd = $("#tmpCateCd").val();
        if(_cateCd == ''){
        	// BO4007 : 카테고리 코드를 입력해 주세요
        	alert(i18n.BO4007);
        	$("tmpCateCd").focus();
      	  return false;
        }

    	$.ajax({
    		url : modelUrl,
    		type: 'POST',
    		data : {
    			"_method" : 'POST',
    			"data" : JSON.stringify({
    									 name 	: _name,
    									 parent : _parentId,
    									 brandId : _data.brandId,
    									 storeId : _data.storeId,
    									 catCd 	: _cateCd
    									 })
    		},
    		success : function(response){
    			if(response.success){
    				view.renderTreegrid();
    			}else{
    				alert("error : " +response.errMsg);
    			}
    		}
    	});
    },
    removeCate : function(ev){
    	_Id = $(ev.target).attr('id').replace('removeCate-','')
    	$("#treegrid-"+_Id).remove();
    },
    cancleSubCateUpt : function(ev){
    	_Id = $(ev.target).attr('id').replace('cancleUpt-','')
    	$("#name-"+_Id).attr("type", "hidden");
    	$("#editUpt-"+_Id).remove();
    	$("#cancleUpt-"+_Id).remove();
    },
    editSubCate : function(ev){
    	_Id = $(ev.target).attr('id').replace('edit-','');
	   	if($("#name-"+_Id).attr("type") == "hidden"){
    		$("#name-"+_Id).attr("type", "text");
    		$("#name-"+_Id).after(" <button type='button' class='btn btn-xs btn-success editUpt' id='editUpt-"+_Id+"'>"+i18n.BO0014+"</button>" +
    				              " <button type='button' class='btn btn-xs btn-warning cancleUpt' id='cancleUpt-"+_Id+"'>"+i18n.BO0026+"</button>");
    		
		}
    },
    editSubCateUpt : function(ev){

    	_Id = $(ev.target).attr('id').replace('editUpt-','')
    	var view = this;

    	$.ajax({
    		url : modelUrl,
    		type: 'POST',
    		data : {
    			"_method" : 'PUT',
    			"data" : JSON.stringify({
    									 name 	 : $('#name-'+_Id).val(),
    									 id 	 : $('#id-'+_Id).val(),
    									 parent  : $('#parent-'+_Id).val(),
    									 brandId : $('#brandId-'+_Id).val(),
    									 storeId : $('#storeId-'+_Id).val(),
    									 catCd 	 : $('#catCd-'+_Id).val()
    									 })
    		},
    		success : function(response){
    			if(response.success){
    				view.renderTreegrid();
    			}
    		}
    	});

    },
    addSubCate : function(ev){
    	/*해당 노드 expanded 후 하위카테고리 추가*/
    	_Id = $(ev.target).attr('id').replace('add-','');
    	$('#treegrid-'+_Id).treegrid('expand');
    	this.addNextCate(_Id);

    },
    delSubCate : function(ev){

    	_Id = $(ev.target).attr('id').replace('del-','')
    	var view = this;

    	//1.하위카테고리 존재 체크
    	//2.해당 카테고리의 상품 존재 여부
    	//3.모든조건 만족시 삭제 실행
    	// BO4015 : 삭제 하시겠습니까?
    	if (confirm(i18n.BO4015)) {
	    	$.ajax({
	    		url : modelUrl,
	    		type: 'POST',
	    		data : {
	    			"_method" : 'DELETE',
	    			"data" : JSON.stringify({ id : $('#id-'+_Id).val() })
	    		},
	    		success : function(response){
	    			if(response.success){
	    				view.renderTreegrid();
	    			}else{
	    				alert("error : " +response.errMsg);
	    			}
	    		}
	    	});
    	}

    },
    closeAll : function(){  //모두닫기
        $('table.tree').treegrid({
        	'initialState': 'collapsed'
        });
    },
    openAll : function(){  //모두열기
        $('table.tree').treegrid({
        	'initialState': 'expanded'
        });
    }
  });

  return ContentView;
});