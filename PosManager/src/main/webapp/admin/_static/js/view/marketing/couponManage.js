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
  
  'text!templates/marketing/couponManage.html',
  'text!templates/marketing/modal/coupon_modal.html',
  'text!templates/common/modal_form_alert.html',

  'bootstrap-daterangepicker'

], function (Backbone, Mustache, Common, i18n, template, couponModal, modalAlert) {
  'use strict';

  var _data = {};

  var targetUrl = './model/marketing/Coupon';

  var main_tables;

  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events : {
    	'click #new-coupon' 						: 'modal_init'				// 쿠폰신규등록
    	,'click #coupon_table button.coupon-detail' : 'load_detail'				// 쿠폰상세
    	,'change #discountTp' 						: 'selectDiscountTp'	// 디스카운트 타입
    	,'change #expireTp' 						: 'selectExpiretp'			// 만료유형선택
    	,'change #hasUseLimit' 						: 'hasUseLimit'				// 사용제한여부
    	,'change #isAll' 						: 'isAll'				// 사용제한여부
    	,'click .add-on-datepicker' : 'trigger_calender'
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
      
      // select2 초기화
      view.selectedInit();

      // dataTable 초기화 및 조회
      view.datatablesInit();

      return view;
    },
    
    // select2 이벤트
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
			_data.data = JSON.stringify({brandId : $(this).val()});
			_data.brandId = $(this).val();
			main_tables.draw();
        });
    },
    // 쿠폰신규등록
    modal_init : function(){
    	var view = this;
    	if(_data.brandId != undefined && _data.brandId != ''){
    		var data = view.loadBaseCode("DiscountTp,ExpireTp");
    		data.franId = _data.franId;
    		data.brandId = _data.brandId;
    		data.i18n = i18n;
    		
    		data.discount = "0";
    		data.discountLimit = "0";
    		
    		data.discountLabel = i18n.BO2163;
    		data.discountLimitLabel = i18n.BO2162;
    		
    		data.discountReadonly = " readonly ";
    		data.discountLimitReadonly = " readonly ";
    		
    		//data.term = Common.JC_format.transTerm(data.term);
    		data.formatTerm = Common.JC_format.formatTerm();
    		
    		var _template = Mustache.to_html(couponModal, data);
    		
    		$('#coupon-modal').html(Mustache.to_html(_template, view.buildOptions(data)));
    		
    		view.form_init('POST', $('#coupon-modal'));

    	} else {
    		alert(i18n.BO4002);
    		$('#coupon-modal').html('');
    		$('#coupon-modal').modal('toggle');
    	}
    },
    
    // dataTable 초기화 및 조회
    datatablesInit : function(){
    	main_tables = $('#coupon_table').DataTable({
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
			  { "data" : "id", "name" : "ID", "visible": false},
			  { "data" : "couponNm", "name" : "COUPON_NM"},
			  { "data" : "discountTp", "name" : "DISCOUNT_TP"
				  ,render: function ( data, type, row ) {
					  var _tmpOutPut = "";
					  if(data == '403001'){
					  		_tmpOutPut = "Rate Discount";
					  	}else if(data == '403002'){
					  		_tmpOutPut = "Amount Discount";
					  	}else if(data == '403003'){
					  		_tmpOutPut = "1+1";
					  	}else if(data == '403004'){
					  		_tmpOutPut = "Free Menu";
					  	}
					  return _tmpOutPut;
				  }
			  },
			  
			  { "data" : "discount", "name" : "DISCOUNT"
				,"render": function (data, type, row) {
	                  return Common.JC_format.number(data);
	              }  
			  },
			  { "data" : "used", "name" : "USED", "className": "dt-body-center"},
			  { "data" : "term", "name" : "TERM", "visible": false },
			  { "data" : "id"}
			],
		    "columnDefs":  [ {
		        "render": function ( data, type, row ) {
		            return '<button class="btn btn-primary btn-sm coupon-detail" data-toggle="modal" data-target=".bs-coupon-modal-lg" target-code="'+data+'" >'+i18n.BO0022+'</button>';
		        },
		        "bSortable": false,
		        "targets": 7
		    }],
		    "pageLength": 10,
		    "lengthChange": false,
		    "processing": true,
		    "rowReorder": true,
		    "rowId" : 'id'
		});
    },
    
    // 쿠폰상세
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
    				var data = response.data;
    				if(data.discountTp == "403001"){
    				  data.discountLabel = i18n.BO2431;
    				  //data.discountLimitLabel = i18n.BO2162;
    				  
    				  data.discountReadonly = "";
    				  data.discountLimitReadonly = "";
    				  
    				}else if(data.discountTp == "403002"){
    				  data.discountLabel = i18n.BO2170;
    				  //data.discountLimitLabel = i18n.BO2162;
    				  
    				  data.discountReadonly = "";
    				  data.discountLimitReadonly = " readonly ";
    				  
    				}else{
    				  data.discountLabel = i18n.BO2170;
    				  //data.discountLimitLabel = i18n.BO2162;
    				  
    				  data.discountReadonly = " readonly ";
    				  data.discountLimitReadonly = " readonly ";
    				  
    				  data.discount = "0";
    				  data.discountLimit = "0";
    				}
    				
    				data.term = Common.JC_format.transTerm(data.term);
    				data.formatTerm = Common.JC_format.formatTerm();
    				
    				data = $.extend(data, view.loadBaseCode("DiscountTp,ExpireTp"));
    				
    				data.i18n = i18n;
    				
    				data = view.buildOptions(response.data);
    				
    				var _template = Mustache.to_html(couponModal, data);

    				$('#coupon-modal').html(Mustache.to_html(_template, data));
    				view.form_init('PUT', $('#coupon-modal'));
    			}
    		}
    	});
    },
    
    // 
    form_init : function(method, modal){
    	var view = this;

    	modal.find('select').select2();
        
    	$('#store_select').select2({
        	placeholder: "Select a state",
    	    allowClear: true,
        	ajax : {
	    		url : './model/Store',
	    		dataType: 'json',
	    		delay: 250,
	    		data: function (params) {
	    			_data.start = (params.page != undefined ? params.page : 0) * 10;
	    			_data.draw = 'select';
	    			_data['search[value]'] = params.term;
			      return _data;
			    },
			    processResults: function (data, params) {
			    	var default_array = [{id: '', storeNm : i18n.BO1033}];

			        params.page = params.page || 1;

			        default_array = default_array.concat(data.list);

			        return {
			          results: default_array,
			          pagination: {
			            more: (params.page * 10) < data.recordsTotal
			          }
			        };
		        },
		        cache: true
		    },
		    
		    escapeMarkup: function (markup) { return markup; }, // let our custom formatter work
		    minimumInputLength: 1,
		    templateResult : function(data){
		    	return data.storeNm;
		    },
		    templateSelection : function(data){
		    	return data.storeNm;
		    }
        });

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
        		var week =  '';
        		var openTm = params.openTm_h + params.openTm_m;
        		var closeTm = params.closeTm_h + params.closeTm_m
        		
        		week += params.week_mon == 'on' ? '1' : '0';
        		week += params.week_tue == 'on' ? '1' : '0';
        		week += params.week_wed == 'on' ? '1' : '0';
        		week += params.week_thu == 'on' ? '1' : '0';
        		week += params.week_fri == 'on' ? '1' : '0';
        		week += params.week_sat == 'on' ? '1' : '0';
        		week += params.week_sun == 'on' ? '1' : '0';

        		params.enableDays = week;
        		if(params.storelist != undefined && params.storelist.length > 0){
        			params.storeIds = JSON.stringify({ storeIds : params.storelist });
        		}

        		params.couponTp = "406002"	// 쿠폰 발행 유형 406002 이벤트 발행
        		
        		params.term = Common.JC_format.transTerm(params.term);
        		
        		params.openTm = openTm;
        		params.closeTm = closeTm;
        		
	        	$.ajax({
	        		url : targetUrl,
	        		type: 'POST',
	        		data : {
	        			"_method" : method,
	        			"data" : JSON.stringify(params)
	        		},
	        		success : function(response){
	        			if(response.success){
	    		    		$('#coupon-modal').modal('toggle');
	    		    		main_tables.draw();
	        			}
	        		}
	        	});

	        	return false;
	        }
		});

		$('#timepicker1').daterangepicker({
	        "alwaysShowCalendars": true,
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

					data = result.codes;
				} else {
		    		$('#coupon-modal').html('');
		    		$('#coupon-modal').modal('toggle');
				}
			}
		});
		
		return data;
    },

    // 디스카운트 타입 선택시 
    selectDiscountTp : function(event){
      var discType = $('#discountTp').val();
      if(discType == "403001"){
        $('#discountLabel').text(i18n.BO2431 + " *");
        //$('#discountLimitLabel').text(i18n.BO2432 + " *");
        
        $('#discount').val('');
        $('#discountLimit').val('');
        $('#discount').attr('readonly',false);
        $('#discountLimit').attr('readonly',false);
        
      }else if(discType == "403002"){
        $('#discountLabel').text(i18n.BO2170 + " *");
        //$('#discountLimitLabel').text(i18n.BO2162 + " *");
        
        $('#discount').val('');
        $('#discountLimit').val('0');
        $('#discount').attr('readonly',false);
        $('#discountLimit').attr('readonly',true);
        
      }else{
        $('#discountLabel').text(i18n.BO2170 + " *");
        //$('#discountLimitLabel').text(i18n.BO2162 + " *");
        
        $('#discount').val('0');
        $('#discountLimit').val('0');
        
        $('#discount').attr('readonly',true);
        $('#discountLimit').attr('readonly',true);
      }
    },
    
    // 유효기간타입 선택시 
    selectExpiretp : function(event){
    	if($('#expireTp').val() == "404001"){
    		$('#expire-tp-term').show();
    		$('#expire-tp-fix').hide();
    		$('#term').val('');
    	}else if($('#expireTp').val() == "404002"){
    		$('#expire-tp-fix').show();
    		$('#expire-tp-term').hide();
    		$('#timepicker1').val('');
    		$('#begin').val('');
    		$('#expire').val('');
    	}else{
    		$('#expire-tp-term').hide();
    		$('#expire-tp-fix').hide();
    		$('#term').val('');
    		
    		$('#timepicker1').val('');
    		$('#begin').val('');
    		$('#expire').val('');
    	}
    },
    
    // 쿠폰사용제한 선택시 
    hasUseLimit : function(event){   
    	
    	if($('#hasUseLimit').val() == "true"){    		
    		$('#available').show();
    	}else if($('#hasUseLimit').val() == "false"){
    		$('#available').hide();
    		
    		$('.sub_select_checked input[type=checkbox]').attr('checked',false);
    		
    		$("#openTm_h option:eq(0)").attr("selected","selected");
    		$("#openTm_m option:eq(0)").attr("selected","selected");
    		$("#closeTm_h option:eq(0)").attr("selected","selected");
    		$("#closeTm_m option:eq(0)").attr("selected","selected");
    	}
    },
    
    isAll : function(event){   
    	if($('#isAll').val() == "true"){    		
    		$('#storelist').hide();
    	}else if($('#isAll').val() == "false"){
    		$('#storelist').show();
    		
    	}
    },
    buildOptions : function(object) {
        for (var i in object) {
            object[i.toLowerCase() + '=' + object[i]] = true;
        }
        return object;
    },
    load_issue_detail: function (event) {
    	var view = this;
    	var _this = $(event.target);
    	$.ajax({
    		url : "./model/management/BaseCode",
    		data : {
    			baseCd : _this.attr("target-code")
    		},
    		success : function(response){
    			if(response.success){
    				var data = response.list[0];
    				var obj = { disable : true };
    				data.i18n = i18n;
    				data = $.extend(data,  obj);

    				$('#sub-modal').html(Mustache.to_html(sub_modal, view.buildOptions(data)));
    				view.form_init('PUT', $('#sub-modal'), sub_tables);
    			}
    		}
    	});
    },
    trigger_calender : function(event){
    	var _this = $(event.target);
    	var _cal = _this.closest('div').find('.date-picker');
    	_cal.trigger('click');
    }
  });

  return ContentView;
});