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
  
  'text!templates/marketing/promotionManage.html',
  'text!templates/marketing/modal/promotion_modal.html',
  'text!templates/marketing/modal/promotion_detail_form.html',
  'text!templates/common/modal_form_alert.html',

  'bootstrap-daterangepicker'

], function (Backbone, Mustache, Common, i18n, template, promotionModal, issueDetail, modalAlert) {
  'use strict';

  var _data = {};

  var targetUrl = './model/marketing/Promotion';

  var main_tables;

  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events : {
    	'click #new-promotion' 									: 'modal_init'				// 프로모션 신규등록
    	,'click #promotion_table button.promotion-detail' 		: 'load_detail'				// 프로모션 상세
    	,'click .issue-detail' 									: 'issue_detail'			// 프로모션 발급내역
    	,'click .back-user-main'								: 'back_user_main'			// 프로모션 목록
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
      
      view.selectedInit();

      view.datatablesInit();

      return view;
    },
    selectedInit : function (){
    	$(".select2").select2({
      	  width: '100%'
        });

        $('#franchise_select').select2().on('change', function(){
        	_data.data = JSON.stringify({franId : $(this).val()});
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
    					main_tables.draw();
    				}
    			}
    		})
    	});
        
        $('#brand_select').select2().on('change', function(){
			_data.data = JSON.stringify({franId : _data.franId , brandId : $(this).val()});
			_data.brandId = $(this).val();
			main_tables.draw();
        });
    },
    
    // 프로모션 신규등록
    modal_init : function(){
    	var view = this;
    	if(_data.brandId != undefined && _data.brandId != ''){
    		var data = view.loadBaseCode("PromotionIssuedType, PromotionIssueStatus");
    		
    		$.ajax({
    			url : './model/marketing/Coupon',
    			data: {
    				data : JSON.stringify({ franId : _data.franId, brandId : _data.brandId }),
	    			draw : 'select'
			    },
    			async : false,
    			success : function(result){
    				if(result.success){
    					data.couponList = result.list;
    				}
    			}
    		});
    		
    		$.ajax({
    			url : './model/Store',
    			data : {
    				data : JSON.stringify({ franId : _data.franId, brandId : _data.brandId })
    				
    			},
    			async : false,
    			success : function(result){
    				if(result.success){
    					data.storeList = result.list;
    				}
    			}
    		});
			
			data.i18n = i18n;
    		
			$('#promotion-modal').html(Mustache.to_html(promotionModal, $.extend(data, _data)));
			
			view.form_init('POST', $('#promotion-modal'));

    	} else {
    		alert(i18n.BO4002);
    		$('#promotion-modal').modal('toggle');
    		$('#promotion-modal').html('');
    	}
    },
    // 프로모션 목록 조회
    datatablesInit : function(){
    	main_tables = $('#promotion_table').DataTable({
	  		"serverSide": true,
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
			  { "data" : "id", "name" : "ID"},
			  { "data" : "promotionNm", "name" : "PROMOTION_NM"},
			  { "data" : "franId", "name" : "FRAN_ID" , "visible": false},
			  { "data" : "brandId", "name" : "BRAND_ID", "visible": false},
			  { "data" : "brandNm", "name" : "BRAND_NM"},
			  { "data" : "startDt", "name" : "START_DT"
				  , "render": function (data, type, row) {
	                  return Common.JC_format.day(data);
	              }
			  },
			  
			  { "data" : "endDt", "name" : "END_DT"
				  , "render": function (data, type, row) {
	                  return Common.JC_format.day(data);
	              }
			  },
			  
//			  { "data" : "issueTpNm", "name" : "ISSUE_TP_NM", "className": "dt-body-center"},
//			  { "data" : "issueStNm", "name" : "ISSUE_ST_NM", "className": "dt-body-center"},
//			  { "data" : "issuedDt", "name" : "ISSUED_DT" },
//			  { "data" : "issuedTm", "name" : "ISSUED_TM" },
			  { "data" : "issuedDt", "name" : "ISSUED_DT"
				  , "render": function (data, type, row) {
	                  return Common.JC_format.day(data);
	              }
			  },
			  
			  { "data" : "id"},
			  { "data" : "id"}
			],
		    "columnDefs":  [ {
		        "render": function ( data, type, row ) {
		            return '<button class="btn btn-primary btn-sm promotion-detail" data-toggle="modal" data-target=".bs-promotion-modal-lg" target-code="'+data+'" >'+i18n.BO0022+'</button>';
		        },
		        "bSortable": false,
		        "targets": 8
		    },  {
		        "render": function ( data, type, row ) {
		            return '<button class="btn btn-primary btn-sm issue-detail" target-code="'+data+'" >'+i18n.BO2179+'</button>';
		        },
		        "bSortable": false,
		        "targets": 9
		    } ],
		    "order": [[ 1, 'desc' ]],
		    "pageLength": 10,
		    "lengthChange": false,
		    "processing": true,
		    "rowReorder": true,
		    "rowId" : 'id'
		});
    },
    
    // 프로모션 상세조회
    load_detail : function(event){
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
    				var data = view.loadBaseCode("PromotionIssuedType,PromotionIssueStatus");
    				
    				$.ajax({
    	    			url : './model/marketing/Coupon',
    	    			data: {
    	    				data : JSON.stringify({ franId : response.data.franId, brandId : response.data.brandId }),
    		    			draw : 'select'
    				    },
    	    			async : false,
    	    			success : function(result){
    	    				if(result.success){
    	    					
    	    					data.couponList = result.list;
    	    					
    	    					if(response.data.couponId != ""){
    	    						var temp = response.data.couponId.split(",");
        	    					
        	    					for(var i=0; i<temp.length; i++){
            	    					$.each(Object.keys(result.list), function(idx, code){            	    						
            	    						if(temp[i] == result.list[code].id){
                	    						//code.selections = '{{#'+i+'='+code.id+'}} selected="selected" {{/'+i+'='+code.id+'}}';
            	    							result.list[code].selections = 'selected="selected"';
            	    						}
            	    					});
        	    					}  
    	    					}    	    					  	    					
    	    				}
    	    			}
    	    		});
    	    		
    	    		$.ajax({
    	    			url : './model/Store',
    	    			data : {
    	    				data : JSON.stringify({ franId : response.data.franId, brandId : response.data.brandId })
    	    				
    	    			},
    	    			async : false,
    	    			success : function(result){
    	    				if(result.success){
    	    					data.storeList = result.list;
    	    				}
    	    			}
    	    		});
    	    		
    				data.i18n = i18n;
    				
    				var _template = Mustache.to_html(promotionModal, $.extend(data,response.data));

    				$('#promotion-modal').html(Mustache.to_html(_template, view.buildOptions(response.data)));
    				view.form_init('PUT', $('#promotion-modal'));
    			}
    		}
    	});
    },
    
    // 프로모션 신규, 상세 validate
    form_init : function(method, modal){
    	var view = this;

    	modal.find('select').select2();

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
	        		success : function(response){
	        			if(response.success){
	        				alert(i18n.BO4046);
	    		    		$('#promotion-modal').modal('toggle');
	    		    		main_tables.draw();
	        			}
	        		}
	        	});

	        	return false;
	        }
		});

		$('#timepicker1').daterangepicker({
	        "alwaysShowCalendars": true,
	      	autoUpdateInput: false,
			locale: {
	              format: 'YYYY-MM-DD',
	              separator: ' - ',
	              applyLabel: i18n.BO0023,
	              cancelLabel: i18n.BO0026,
	              fromLabel: i18n.BO3032,
	              toLabel: i18n.BO3033,
	              daysOfWeek: [i18n.BO3031, i18n.BO3025, i18n.BO3026, i18n.BO3027, i18n.BO3028, i18n.BO3029, i18n.BO3030],
	              monthNames: [i18n.BO3013, i18n.BO3014, i18n.BO3015, i18n.BO3016, i18n.BO3017, i18n.BO3018, i18n.BO3019, i18n.BO3020, i18n.BO3021, i18n.BO3022, i18n.BO3023, i18n.BO3024],
	              firstDay: 1
	       }
		});

		  $('#timepicker1').on('apply.daterangepicker', function(ev, picker) {
		      $(this).val(picker.startDate.format('YYYY-MM-DD') + ' - ' + picker.endDate.format('YYYY-MM-DD'));
		      $('#from-date').val(picker.startDate.format('YYYY-MM-DD 00:00:00'));
		      $('#to-date').val(picker.endDate.format('YYYY-MM-DD 00:00:00') );
		  });

		  $('#timepicker1').on('cancel.daterangepicker', function(ev, picker) {
		      $(this).val('');
	  		  $('#from-date').val('');
	  		  $('#to-date').val('');
		  });
//		view.load_map();
    },
    
    // 프로모션 발급내역
    issue_detail : function(event){
    	
    	var target = $(event.target);
    	var _id = target.attr("target-code");

    	$.ajax({
    		url : targetUrl,
    		data : {
    			data : JSON.stringify({ id : _id})
    		},
    		success : function(result){
    			if(result.success){
    				var data = {};
    				data.i18n = i18n;
    				
    				var _template = Mustache.to_html(issueDetail, $.extend(data,result.data));
    				
    				var issued_main = {};
    				var issued_user = {};
    				var _data_sub = {};
    				
    				$('#promotion-main').hide();
    		    	$('#issue-detail').html(_template).show();

    		    	issued_main = $('#coupon-list').DataTable({
    		    		"serverSide": true,
    			  		"ordering": false,
    			  		"searching": false,
    			    	"language": {
    			            "url": "//cdn.datatables.net/plug-ins/1.10.11/i18n/English.json"
    			        },
    			        "ajax" : {
    			        	"url" : './model/marketing/PromotionCoupon',
    			        	"type": 'GET',
    				    	"data" : function(data){
    				    		return $.extend(data, { id : _id });
    				    	},
    				        "dataSrc" : 'list'
    			        },
    				    "columns" : [
			   		      { "data" : "id", "name" : "ID"},
						  { "data" : "couponNm", "name" : "COUPON_NM"},
						  { "data" : "promotionId", "name" : "PROMOTION_ID", "visible": false},
						  { "data" : "couponId", "name" : "COUPON_ID", "visible": false},
						  { "data" : "discountTpNm", "name" : "DISCOUNT_TP_NM"},
						  { "data" : "exipreTpNm", "name" : "EXPIRE_TP_NM"},
						  { "data" : "iseeueCnt", "name" : "ISSUE_CNT"},
						  { "data" : "issueTp", "name" : "ISSUE_TP", "visible": false},
						  { "data" : "issueTpNm", "name" : "ISSUE_TP_NM"},
						  { "data" : "issueSt", "name" : "ISSUE_ST" , "visible": false},
						  { "data" : "issueStNm", "name" : "ISSUE_ST_NM"},
						  { "data" : "issuedDt" , "name" : "ISSUED_DT"
							  , "render": function (data, type, row) {
				                  return Common.JC_format.day(data);
				              }
						  }
						  
    					],
    				    "pageLength": 5,
    				    "lengthChange": false
    		    	});
    		    	
    		    	issued_user = $('#issue-list').DataTable({
    		    		"serverSide": true,
    			  		"ordering": false,
    			  		"searching": false,
    			    	"language": {
    			            "url": "//cdn.datatables.net/plug-ins/1.10.11/i18n/English.json"
    			        },
    			        "ajax" : {
    			        	"url" : './model/marketing/IssueDetail',
    			        	"type": 'GET',
    				    	"data" : function(data){
    				    		return $.extend(data, _data_sub);
    				    	},
    				        "dataSrc" : 'list'
    			        },
    				    "columns" : [
			   		      { "data" : "id", "name" : "ID"},
						  { "data" : "couponCd", "name" : "COUPON_CD"},
						  { "data" : "barcode", "name" : "BARCODE"},
						  { "data" : "name", "name" : "NAME"},
						  { "data" : "mb", "name" : "MB"},
						  { "data" : "used", "name" : "USED"},
						  { "data" : "storeNm", "name" : "STORE_NM"},
						  { "data" : "useStoreNm", "name" : "USE_STORE_NM"}
    					],
    				    
    				    "pageLength": 5,
    				    "lengthChange": false
    		    	});
    		    	
    		    	$('#coupon-list').on( 'click', 'tbody > tr', function () {    			        
    		    		if ( $(this).hasClass('selected') ) {
    			            $(this).removeClass('selected');
    			        }
    			        else {
    			        	$('#coupon-list').DataTable.$('tr.selected').removeClass('selected');
    			            $(this).addClass('selected');

//    			            _data_sub.data =  JSON.stringify({promotionId : issued_main.row(this).data().id});
    			            _data_sub.data =  JSON.stringify(issued_main.row(this).data());
    			            
    			            issued_user.draw();
    			        }
    			    } );
    			}
    		}
    	});
    },
    
    // 공통코드 조회
    loadBaseCode : function(codeAlias){
    	var data = {};
    	
		$.ajax({
			url : './model/management/Codes',
			method : 'POST',
			async: false,
			data : {
				'codes' : codeAlias
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
		    		$('#coupon-modal').html('');
		    		$('#coupon-modal').modal('toggle');
				}
			}
		});

		return data;
    },
    
    // 조회된 데이토를 Mustache 적용을 위해 {Key=Value : true} 형태로 변환  
    buildOptions : function(object) {
        for (var i in object) {
            object[i.toLowerCase() + '=' + object[i]] = true;
        }
        return object;
    },
    
    // 메인화면으로 전환
    back_user_main : function (){
    	$('#promotion-main').show();
    	$('#issue-detail').hide();
    	
    	$('#issue-detail').empty();
    }
  });

  return ContentView;
});