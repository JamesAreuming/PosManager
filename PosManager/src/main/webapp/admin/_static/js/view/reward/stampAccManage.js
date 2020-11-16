/*
 * Filename	: stampAccManage.js
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
  
  'text!templates/reward/stampAccManage.html',
  
  'bootstrap-daterangepicker'
], function (Backbone, Mustache, Common, i18n, template) {
  'use strict';
  
  var _data = {};

  var targetUrl = './model/reward/StampAcc';
  
  var datatables;
	  
  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    initialize: function () {
    	this.trigger('remove-event');
    	this.template = template;
    	this.listenTo(this.model, 'sync', this.render);
    },
    events: {
    	'click .add-on-datepicker' : 'trigger_calender',
    	'click button#settlt' : 'getSettlement'    		
    },
    render: function () {
      var view = this;
      var data = {};
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
      
      if(data.frList == undefined && data.brList == undefined){
    	  data.isSt = true;
      }else{
    	  data.isSt = false;
      }
      data.i18n = i18n;
      
      var rendered = Mustache.to_html(view.template, data);
      $(view.el).append(rendered);

      view.setCalendarArea();
  	  view.selectedRollInit();
  	
      return view;
    },
    setCalendarArea : Common.JC_calendar.searchRange(i18n),
    
    selectedRollInit : function (){
    	var view = this;

    	$('select').select2({
    		width: '100%',
            minimumResultsForSearch: -1
    	});
    	
    	$('#franchise_select').select2().on('change', function(){
      		_data.franId = $(this).val();
      		$('#settlement').hide();
      		//$("#settlement_table").DataTable().clear();
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
      				}
      			}  //END : brand select result
      		});  //END : brand select ajax
      	});  //END : franchise select change
    	
    	$('#brand_select').select2().on('change', function(){
			//브랜드와 해당 매장에 관련된 카테고리 분류정보 셋팅
			_data.brandId =  $(this).val();
			$('#settlement').hide();
			//$("#settlement_table").DataTable().clear();
        });
    },
    getSettlement : function(){    	
    	if(_data.franId == "" || _data.franId == undefined){
			alert(i18n.BO4001);
			return false;
		}
		
		if(_data.brandId == "" || _data.brandId == undefined){
			alert(i18n.BO4002);
			return false;
		}
		
		if($('#daterangepicker').val() == "" ){
			alert(i18n.BO4032);
			$('#daterangepicker').focus();
			return false;
		}
		
		if($('#settltRate').val() == ""){
			alert(i18n.BO4064);
			$('#settltRate').focus();
			return false;
		}
		
    	if(datatables != undefined){
    		datatables.draw();
    		$('#settlement').show();
    	}else{
    		
    		datatables = $("#settlement_table").DataTable({
    	  		"serverSide": true,
    	    	"language": {
    	            "url": "//cdn.datatables.net/plug-ins/1.10.11/i18n/English.json"
    	        },
    	        "ajax" : {
    	        	"url" : targetUrl,
    	        	"type": 'POST',
    		    	"data" : function(data){
    		    		data._method = "GET";
    		    		
    		    		data.data = $('#be-search').serializeObject();
    		    		
    		    		data.data.franId = _data.franId;
    		    		data.data.brandId = _data.brandId;
    		    		
    		    		data.data = JSON.stringify(data.data);
    		    		
    		    		return $.extend(data, _data);
    		    	},
    		        "dataSrc" : 'list'
    	        },
    		    "columns" : [
    			  { "data" : "brandId", "name" : "BRAND_ID", "visible": false},
    			  { "data" : "brandNm", "name" : "BRAND_NM", "visible": false},
    			  { "data" : "storeId", "name" : "STORE_ID", "visible": false},
    			  { "data" : "storeNm", "name" : "STORE_NM"},
    			  { "data" : "stampAmount", "name" : "STAMP_AMOUNT", render: function (data, type, row) {return Common.JC_format.number(data);}},
    			  { "data" : "issueStampCnt", "name" : "IISUE_STAMP_CNT", render: function (data, type, row) {return Common.JC_format.number(data);}},
    			  { "data" : "useCouponCnt", "name" : "USE_COUPON_CNT", render: function (data, type, row) {return Common.JC_format.number(data);}},
    			  { "data" : "couponAmount", "name" : "COUPON_AMOUNT", render: function (data, type, row) {return Common.JC_format.number(data);}},
    			  { "data" : "settlementAmount", "name" : "SETTLEMENT_AMOUNT", render: function (data, type, row) {return Common.JC_format.number(data);}}
    			],
    	        "order": [[ 2, 'asc' ]],
    		    "pageLength": 5,
    		    "lengthChange": false,
    		    "processing": true,
    		    "rowReorder": true,
    		    "searching": false
    		});

        	$('#settlement').show();
    	}
    },
    trigger_calender : function(event){
    	var _this = $(event.target);
    	var _cal = _this.closest('div').find('.date-picker');
    	_cal.trigger('click');    	
    }
  });

  return ContentView;
});