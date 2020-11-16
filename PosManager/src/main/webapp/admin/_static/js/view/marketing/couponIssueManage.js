/*
 * Filename	: dashboard.js
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
  
  'text!templates/marketing/couponIssueManage.html',
  'text!templates/marketing/modal/couponIssue_modal.html',
  'text!templates/common/modal_form_alert.html',

  'bootstrap-daterangepicker'

], function (Backbone, Mustache, Common, i18n, template, couponModal, modalAlert) {
  'use strict';

  var _data = {};

  var targetUrl = './model/marketing/IssueCoupon2';

  var main_tables;	// 데이터 테이블을 담기위한 객체
  var target_tables;	// 전체고객 테이블을 담기위한 객체
  var groupUser_tables;	// 그룸선택 고객 테이블을 담기위한 객체
  var custom_tables;	// 직접선택 고객 테이블을 담기위한 객체
  var tabIndex;			// 탭인덱스를 저장
  var issuingTabIndex;	// 발행구분 저장
  var _data_sub =  {};	// 프로모션 쿠폰 리스트에서 선택된 데이터를 저장하기 위한 객체
  
  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events : {
    	'click #new-couponIssue' : 'modal_init',
    	'click #couponIssue_table button.couponIssue-detail' : 'load_detail'/*,
    	'click #search_oper' : 'search_oper'*/
    	,'click #colOwn' 	: 'getRowCount'
    	,'click #colTwo' 	: 'getRowCount'
    	,'click #colThree'  : 'getRowCount'
    	,'click #groupUser_Search'  : 'selectGroupUser'
    	,'click #nextButton'  : 'nextCollapse'
    	,'click #targetUser li'  : 'getTabIndex'
    	,'click #issuingTab li'  : 'getIssuingTabIndex'
    	,'click #issuing_coupon'  : 'issuing_coupon'
    	,'click #upload-target-button' : 'target_upload'
    	,'click #promotionSearch' : 'promotionSearch'
    },
    initialize: function () {
      this.template = template;
      this.listenTo(this.model, 'sync', this.render);
    },
    render: function () {
    	var view = this;
        var data = {};
        data.i18n = i18n;
        
        data.frList = (view.model != undefined ? view.model.toJSON()[0].frList : {});
        data.brList = (view.model != undefined ? view.model.toJSON()[0].brList : {});
        
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

      var rendered = Mustache.to_html(view.template, data);
      $(view.el).append(rendered);

      view.selectedRollInit();

      view.datatablesInit();
      
      $('.date-picker').daterangepicker({
			autoApply : true,
			singleDatePicker: true,
			autoUpdateInput:false,
			locale: {
	            format: 'YYYY-MM-DD',
			}
		});
		
		$('.date-picker').on('apply.daterangepicker', function(ev, picker) {
		      $(this).val(picker.startDate.format('YYYY-MM-DD'));
		});
		
      return view;
    },
    selectedRollInit : function (){
    	var view = this;

    	$('#franchise_select').select2();    	
    	$('#brand_select').select2();
    	
    	$('#franchise_select').select2().on('change', function(){
      		_data.franId = $(this).val();
      		$.ajax({
      			url : './model/Brand',
      			data : {
      				data : JSON.stringify({ franId : $(this).val() })
      			},
      			success : function(result){
      				if(result.success){
      					var defualt_array = [{id: '', brandNm : i18n.BO1032}];
      					$("#brand_select").find('option').remove();
      					$('#brand_select').select2({
      						data : defualt_array.concat(result.list),
      						templateResult : function(state) {
      							return state.brandNm;
      						},
      						templateSelection : function(data, container) {
      						    return data.brandNm;
      						}
      					})
      				}
//      				$('#colOwn').trigger('click');
      				main_tables.draw();
      			}  //END : brand select result
      		});  //END : brand select ajax
      	});  //END : franchise select change
    	
    	$('#brand_select').select2().on('change', function(){
			//브랜드와 해당 매장에 관련된 카테고리 분류정보 셋팅
			_data.brandId =  $(this).val();
//			$('#colOwn').trigger('click');
			main_tables.draw();
        });
    },    
    
    getRowCount : function(){    	
 	
    },
    getTabIndex : function(event){
    	tabIndex = event.target.id;

    },
    getIssuingTabIndex : function(event){
    	issuingTabIndex = event.target.id;    	
    },
    
    promotionSearch : function(){
    	var view = this;
    	var $form = $('#sub-search');

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
    
    datatablesInit : function(){
    	main_tables = $('#coupon_table').DataTable({
	  		"serverSide": true,
	  		"searching": false,
	    	"language": {
	            "url": "//cdn.datatables.net/plug-ins/1.10.11/i18n/English.json"
	        },
	        "ajax" : {
	        	"url" : targetUrl,
	        	"type": 'POST',
		    	"data" : function(data){
		    		data._method = "GET";
		    		return $.extend(data, _data);
		    	},
		        "dataSrc" : 'list'
	        },
		    "columns" : [
			  { "data" : "promotionId", "name" : "PROMOTION_ID", "visible": false},
			  { "data" : "couponId", "name" : "COUPON_ID", "visible": false},
			  { "data" : "promotionNm", "name" : "PROMOTION_NM"},
			  { "data" : "couponNm", "name" : "COUPON_NM"},
			  { "data" : "discountTpNm", "name" : "DISCOUNT_TP_NM"},
			  { "data" : "exipreTpNm", "name" : "EXPIRE_TP_NM"},
			  { "data" : "discountTp", "name" : "DISCOUNT_TP", "visible": false},
			  { "data" : "exipreTp", "name" : "EXPIRE_TP", "visible": false},
			  { "data" : "brandCd", "name" : "BRAND_CD", "visible": false},
			  { "data" : "brandId", "name" : "BRAND_ID", "visible": false},
			  { "data" : "storeId", "name" : "STORE_ID", "visible": false},
			  { "data" : "issueSt", "name" : "ISSUE_ST", "visible": false},
			  { "data" : "issueStNm", "name" : "ISSUE_ST_NM"},
			  { "data" : "issueTp", "name" : "ISSUE_TP", "visible": false},
			  { "data" : "issueTpNm", "name" : "ISSUE_TP_NM"},
			  { "data" : "issuedDt", "name" : "ISSUED_DT"
				  ,"render": function (data, type, row) {
					  			return Common.JC_format.date(data);
					  		}
			  },
			  { "data" : "term", "name" : "TERM", "visible": false},
			  { "data" : "expire", "name" : "EXPIRE", "visible": false},
			],
			
	        "order": [[ 0, 'asc' ]],
		    "pageLength": 10,
		    "lengthChange": false,
		    "processing": true,
		    "rowReorder": true,
		    "rowId" : 'id'
		});
    	
    	$('#coupon_table').on( 'click', 'tbody > tr', function () {    			        
    		
    		if ( $(this).hasClass('selected') ) {
	            $(this).removeClass('selected');
	        }
	        else {
	        	main_tables.$('tr.selected').removeClass('selected');
	            $(this).addClass('selected');
	            
	            if(typeof(target_tables) != "undefined"){
		    		target_tables.destroy();
	    		}
	            
	            _data_sub.data =  main_tables.row(this).data();
	            
	            target_tables = $('#allUser_table').DataTable({
	    	  		"serverSide": true,
	    	  		"searching": false,
	    	    	"language": {
	    	            "url": "//cdn.datatables.net/plug-ins/1.10.11/i18n/English.json"
	    	        },
	    	        "ajax" : {
	    	        	"url" : './model/marketing/IssueCouponUser',
	    	        	"type": 'POST',
	    		    	"data" : function(data){
	    		    		data._method = "GET";
	    		    		return $.extend(data, _data_sub.data);
	    		    	},
	    		        "dataSrc" : 'list'
	    	        },
	    		    "columns" : [
	    			  { "data" : "userId", "name" : "USER_ID"},
	    			  { "data" : "brandId", "name" : "BRAND_ID", "visible": false},
	    			  { "data" : "storeId", "name" : "STORE_ID", "visible": false},
	    			  { "data" : "name", "name" : "NAME"},
	    			  { "data" : "barcode", "name" : "BARCODE"},
	    			  { "data" : "mb", "name" : "MB"},
	    			  { "data" : "email", "name" : "EMAIL"}
	    			],
	    			
	    	        "order": [[ 0, 'asc' ]],
	    		    "pageLength": 10,
	    		    "lengthChange": false,
	    		    "processing": true,
	    		    "rowReorder": true,
	    		    "rowId" : 'id'
	    		});
	            
	            $('#colTwo').trigger('click');
	            $('#all').trigger('click');
	            
	            $("#promotionName").text(_data_sub.data.promotionNm + " (" + _data_sub.data.couponNm +")" );
	            
	        }    		
	    } );
    },
    issuing_coupon : function(){
    	
    	if($('#promotionName').text() == ""){
    		alert("쿠폰을 선택하세요.");
    		return false;
    	}
    	
    	if($('#totalCnt').text() == "" || $('#totalCnt').text() == "0"){
    		alert("쿠폰 발행 대상 고객이 없습니다.");
    		return false;
    	}
    	var params = _data_sub.data;
    	
    	params.issueTp = '409001';
    		
    	if(issuingTabIndex == "issueTab1"){	// 즉시발행인 경우 
    		params.issueTp = '409001';
    	}else if(issuingTabIndex == "issueTab2"){	// 예약발행인 경우
    		params.issueTp = '409002';
    	}
    	
    	if(params.issueTp == "" || params.issueTp == undefined){
    		alert("발행구분이 없습니다.\n" + params.issueTp+"\n"+issuingTabIndex);
    		return false;
    	}
    	
    	if(tabIndex == "group"){
    		var searchData = $('#groupUser').serializeObject();
    		params = $.extend(searchData, params);
    	}else if(tabIndex == ""){
    		
    	}
    	
    	$.ajax({
    		url : targetUrl,
    		type: 'POST',
    		data : {
    			"_method" : 'POST',
    			"data" : JSON.stringify(params)
    		},
    		success : function(response) {
    			if (response.success) {
    				alert("success");
    				
    			}else{
    				alert("fail");
    			}
    		}
    	});
    },
    
    // 다음버튼 클릭시
    nextCollapse : function(){
    	
    	if(tabIndex == "all"){
    		if(target_tables != undefined){
    			$("#totalCnt").text(target_tables.page.info().recordsTotal);
    		}    		
    	}else if(tabIndex == "group"){
    		if(groupUser_tables != undefined){
    			$("#totalCnt").text(groupUser_tables.page.info().recordsTotal);
    		}    		
    	}else if(tabIndex == "coutom"){
    		if(custom_tables != undefined){
    			$("#totalCnt").text(custom_tables.page.info().recordsTotal);
    		}
    	}
    	
    	$('#colThree').trigger('click');
    	
    	$('#issueTab1').trigger('click');
    	
    	var temp = i18n.BO4065;
    	
    	temp = temp.replace("{0}", $('#promotionName').text());
    	temp = temp.replace("{1}", Common.JC_format.number($('#totalCnt').text()));
    	
    	$('#test1').text(temp);
    	$('#test2').text(temp);
    	
    },
    selectGroupUser : function(){
    	
    	if(typeof(groupUser_tables) != "undefined"){
    		groupUser_tables.destroy();
		}
    	
    	var searchData = $('#groupUser').serializeObject();
    	
    	searchData = $.extend(searchData, _data_sub.data);
    	
    	groupUser_tables = $('#groupUser_table').DataTable({
	  		"serverSide": true,
	  		"searching": false,
	    	"language": {
	            "url": "//cdn.datatables.net/plug-ins/1.10.11/i18n/English.json"
	        },
	        "ajax" : {
	        	"url" : './model/marketing/IssueCouponUser',
	        	"type": 'POST',
		    	"data" : function(data){
		    		data._method = "GET";
		    		return $.extend(data, searchData);
		    	},
		        "dataSrc" : 'list'
	        },
		    "columns" : [
			  { "data" : "userId", "name" : "USER_ID"},
			  { "data" : "brandId", "name" : "BRAND_ID", "visible": false},
			  { "data" : "storeId", "name" : "STORE_ID", "visible": false},
			  { "data" : "name", "name" : "NAME"},
			  { "data" : "barcode", "name" : "BARCODE"},
			  { "data" : "mb", "name" : "MB"},
			  { "data" : "email", "name" : "EMAIL"}
			],
			
	        "order": [[ 0, 'asc' ]],
		    "pageLength": 10,
		    "lengthChange": false,
		    "processing": true,
		    "rowReorder": true,
		    "rowId" : 'id'
		});
    },
    loadBaseCode : function(){
    	var data = {};
		$.ajax({
			url : './model/management/Codes',
			method : 'POST',
			async: false,
			data : {
				'codes' : 'DiscountTp,ExpireTp'
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
		    		$('#couponIssue-modal').html('');
		    		$('#couponIssue-modal').modal('toggle');
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
    target_upload : function(){
    	
    	var _data = new FormData();
    	var file = $('#upload-target');
    	if(file.get(0).files[0] != undefined){
			_data.append("targetUser", $('#upload-target').get(0).files[0]);
			$.ajax({
				data: _data,
				type: "POST",
				url: "./model/marketing/PromotionTarget/Upload",
				cache: false,
				contentType: false,
				processData: false,
				async: false,
				success: function(result) {
					if(result.success){
						custom_tables.clear();
						custom_tables.rows.add(result.list);
						custom_tables.draw();
						if(result.alert){
							alert(result.alert);
						}
					} else {
						alert(result.errMsg);
					}
				}
			});
    	} else {
    		alert(i18n.BO4063);
    	}
    },
  });

  return ContentView;
});