/*
 * Filename	: posMessageSetManage.js
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
  
  'text!templates/items/posMessageSetManage.html',
  'text!templates/items/modal/posMessageSet_modal.html',
  'text!templates/common/modal_form_alert.html',

  'bootstrap-daterangepicker'

], function (Backbone, Mustache, Common, i18n, template, posMessageSetModal, modalAlert) {
  'use strict';

  var _data = {};

  var targetUrl = './model/items/PosMessageSet';
  var pluTypeSelect = './model/items/PluTypeSelect';

  var rowCnt = 1;

  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events : {
    	'change #category_select': 'selectPluCateList',
    	'click #add-row' : 'add_row',
    	'click a.mod-row' : 'mod_row',
		'click a.del-row' : 'del_row'
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

        if(data.frList == undefined && data.brList == undefined && data.stList == undefined){
        	view.selectedInit(_data.brandId, _data.storeId, $("#category_select").val());
        }
        
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
      			}  //END : brand select result
      		});  //END : brand select ajax
      		
      		view.selectedInit(0, 0, 0);
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
			view.selectedInit(_data.brandId, 0, 0);
        });  //END : brand select change
    	
    	$('#store_select').select2().on('change', function(){
			//브랜드와 해당 매장에 관련된 카테고리 분류정보 셋팅
			_data.storeId =  $(this).val();
			view.selectedPluCateType(_data.brandId, _data.storeId);
        });
    },
    
    selectedPluCateType : function (vBrandId , vStoreId){

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

    	if($("#category_select").val() == ""){
    		$("#posMessageSet").empty();
    		return false;
    	}

    	view.selectedInit(_data.brandId, _data.storeId, $("#category_select").val());
    },
    selectedInit : function (vBrandId, vStoreId, vCate) {
    	var view = this;
    	
		$.ajax({
			url : targetUrl,
			data : {
				data : JSON.stringify({brandId : vBrandId, storeId : vStoreId, itemCatId: vCate})
			},
			async: false,
			success : function(result) {
				if (result.success) {
					var data = {};
					data.i18n = i18n;
					data.list = result.list;
					data.totCnt = result.totCnt;
					data.search = vStoreId > 0 ? true : false;

					$('.search-form').attr('id', 're-search-form');
					$('#posMessageSet').html(Mustache.to_html(view.template, data));
		    		$('#be-search-form').remove();
		        	rowCnt = 1;
				}
			}
		});
    },
    add_row : function () {
    	if (rowCnt < 2) {
	    	var html = '';
		    	html += '<tr class="odd gradeX txt-center">';
		    	html += '<td class="title"></td>';
		    	html += '<td class="txt-left">';
		    	html += '<input type="text" class="form-control" placeholder="Kitchen Message" name="message" id="message" value="" />';
		    	html += '</td>';
		    	html += '<td class="pos_msg_txt_modify"><a class="mod-row">'+i18n.BO0014+'</a> | <a class="del-row" id="del_'+rowCnt+'">'+i18n.BO0016+'</a></td></tr>';
	    	$('#table-tbody').append(html);

	        rowCnt++;
    	}
    },
    mod_row : function (event) {
    	var view = this;
//    	var brandId = $('#brand_select option:selected').val();
//		var storeId = $('#store_select option:selected').val();
    	var brandId = _data.brandId;
		var storeId = _data.storeId;
		var itemCatId = $("#category_select").val();
		
    	var id = event.currentTarget.getAttribute('id');
    	var method = 'POST';
    	var message = $('#message').val();
    	var ordinal = $('[name=totCnt]').val();
    	var flag = true;

    	if ($('#message').val() == '') flag = false;

    	if (id != null) {
    		method = 'PUT';
    		message = $('#message'+id).val();
    		ordinal = $('#ordinal'+id).val();

        	if ($('#message'+id).val() == '') flag = false;
    	}

    	if (!flag) {
    		alert(i18n.BO4056);
    		return false;
    	}

    	$.ajax({
    		url : targetUrl,
    		type: 'POST',
    		data : {
    			"_method" : method,
    			"data" : JSON.stringify({
    				id : id,
    				message : message,
    				ordinal : ordinal,
    				brandId : brandId,
    				storeId : storeId,
    				itemCatId : itemCatId
    			})
    		},
			async: false,
    		success : function(response) {
    			if (response.success) {
    				alert(i18n.BO4046);
    				view.selectedInit(brandId, storeId, $("#category_select").val());
    			}
    		}
    	});
    },
    del_row : function (event) {
    	var view = this;
    	var flag = true;
    	var id = event.currentTarget.getAttribute('id');

    	// BO4015 : 삭제 하시겠습니까
        if (confirm(i18n.BO4015)) {
        	if (id.indexOf('del_') == -1) {
	        	$.ajax({
	        		url : targetUrl,
	        		type : 'POST',
	        		data : {
	        			"_method" : 'DELETE',
	        			"data" : JSON.stringify({id : id})
	        		},
	        		success : function(response){
	        			if(response.success){
	        				view.selectedInit(_data.brandId, _data.storeId, $("#category_select").val());
	        			}
	        			else {
	        				flag = false;
	        			}
	        		}
	        	});
	        }
        	if (flag) {
        		$('#'+id).closest('tr').remove();
        		rowCnt = 1;
        	}
    	}
    }
  });

  return ContentView;
});