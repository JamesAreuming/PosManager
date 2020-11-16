/*
 * Filename	: posTableSetManage.js
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
  
  'text!templates/items/posTableSetManage.html',
  'text!templates/items/modal/posTableSet_modal.html',
  'text!templates/common/modal_form_alert.html',

  'bootstrap-daterangepicker'

], function (Backbone, Mustache, Common, i18n, template, posTableSetModal, modalAlert) {
  'use strict';

  var _data = {};

  var targetUrl = './model/items/PosTableSet';

  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events : {
    	'click #floor-modal' : 'modal_init',
    	'click .table-select' : 'table_init',
    	//'dblclick .table-select' : 'load_detail',
    	'click #table-amt-modal' : 'modal_init',
    	'click #table-modal' : 'load_detail',
		'click #floor-del' : 'floor_del',
		'click .table-count_btn' : 'table_count_btn'
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
        view.selectedInit(_data.brandId, _data.storeId, 0);
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
			view.selectedInit(_data.brandId, _data.storeId, 0);
        });
    },
    selectedInit : function (vBrandId, vStoreId, sectionId) {
    	var view = this;

		$.ajax({
			url : targetUrl,
			data : {
				data : JSON.stringify({brandId : vBrandId, storeId : vStoreId, sectionId : sectionId})
			},
			async: false,
			success : function(result) {
				if (result.success) {
					var data = {};
					data.floor = result.floorList;
					data.table = result.tableList;
					data.search = vStoreId > 0 ? true : false;
					data.sectionId = result.sectionId;
					data.floorCnt = result.floorList.length > 0 ? true : false;
					data.tableCnt = result.tableCnt;
					data.i18n = i18n;
					view.tableCnt = result.tableCnt;

					$('.search-form').attr('id', 're-search-form');
					$('#posTableSet').html(Mustache.to_html(view.template, data));
		    		$('#be-search-form').remove();
		        	$('#'+result.sectionId).removeClass('btn-white').addClass('btn-primary');
				}
			}
		});
    },
    table_count_btn: function(event) {
    	var view = this;
    	var val = event.currentTarget.getAttribute('value');
    	var cnt = $('[name=tableCnt]').val();
    	cnt = Number(cnt);

    	if (val == '+') {
    		cnt = cnt + 1;
    	}
    	else {
    		if (cnt == 0) return false;
    		cnt = cnt - 1;
    	}
    	$('[name=tableCnt]').val(cnt);

    	return false;
    },
    table_init : function(event) {
    	var view = this;
    	var id = event.currentTarget.getAttribute('id');

    	view.selectedInit(_data.brandId, _data.storeId, id);
    },
    modal_init : function(event) {
    	var view = this;
    	var id = event.currentTarget.getAttribute('id');
    	var val = event.currentTarget.getAttribute('value');
    	var modalId = '';
    	var data = {};
    	data.mode = id;
    	data.tableCnt = view.tableCnt;
    	data.sectionId = val;
    	data.i18n = i18n;
    	
    	if (id == 'floor-modal') modalId = 'posFloorModal';
    	else if (id == 'table-modal') modalId = 'posTableModal';
    	else if (id == 'table-amt-modal') modalId = 'posTableAmtModal';

		$('#'+modalId).html(Mustache.to_html(posTableSetModal, view.buildOptions(data)));
		view.form_init('POST', $('#'+modalId));
    },
    load_detail: function(event) {
    	var view = this;
    	var id = event.currentTarget.getAttribute('id');
    	var val = event.currentTarget.getAttribute('value');
    	var modalId = '';

    	if (id == 'floor-modal') modalId = 'posFloorModal';
    	else if (id == 'table-modal') modalId = 'posTableModal';
    	else if (id == 'table-amt-modal') modalId = 'posTableAmtModal';

    	$.ajax({
    		url : targetUrl,
    		data : {
    			data : JSON.stringify({id : val, mode : id, detail : 'Y'})
    		},
    		success : function(response){
    			if(response.success){
    				var data = {};
    				data.list = response.list;
    				data.mode = id;
    				data.i18n = i18n;
    				
    				$('#'+modalId).html(Mustache.to_html(posTableSetModal, view.buildOptions(data)));

    				view.form_init('PUT', $('#'+modalId));
    			} else {
    				$('#'+modalId).modal('toggle');
    			}
    		}
    	});
    },
    form_init : function(method, modal){
    	var view = this;

		$('[name=brandId]').val(_data.brandId);
		$('[name=storeId]').val(_data.storeId);
		$('[name=mode]').val();

		$.validate({
			validateOnBlur : false,
		    scrollToTopOnError : false,
		    showHelpOnFocus : false,
		    errorMessagePosition : $('.form-validatoin-alert-detail'),
		    addSuggestions : false,
		    onError : function($form) {
		    	var _target = $("modal-body");

		    	if(_target.find('.alert').length == 0){
		    		_target.prepend(Mustache.to_html(modalAlert, {i18n:i18n}));
		    	}
	        },
	        onSuccess : function($form) {
        		var params = $form.serializeObject();

	        	$.ajax({
	        		url : targetUrl,
	        		type: 'POST',
	        		data : {
	        			"_method" : method,
	        			"data" : JSON.stringify(params)
	        		},
					async: false,
	        		success : function(response){
	        			if(response.success){
	        				$('#posTableModal').modal('toggle');

	        				view.selectedInit(_data.brandId, _data.storeId, response.sectionId);
	        				$('.modal-backdrop').remove();
        				    $('body').css({'padding-right':'0'});
	        			}
	        		}
	        	});
	        	return false;
	        }
		});
    },
    floor_del : function(event) {
    	// BO4015 : 삭제 하시겠습니까?
    	if (confirm(i18n.BO4015)) {
        	var view = this;
        	var val = event.currentTarget.getAttribute('value');

        	$.ajax({
        		url : targetUrl,
        		type : 'POST',
        		data : {
        			"_method" : 'DELETE',
        			"data" : JSON.stringify({id : val})
        		},
        		success : function(response){
        			if(response.success){
        				view.selectedInit(_data.brandId, _data.storeId, response.sectionId);
        			}
        		}
        	});
        }
        else {
        	return false;
        }
    },
    buildOptions : function(object) {
        for (var i in object) {
            object[i.toLowerCase() + '=' + object[i]] = true;
        }
        return object;
    }
  });

  return ContentView;
});