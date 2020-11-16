/*
 * Filename	: franchiseManage.js
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
  
  'text!templates/nine/franchiseManage.html',
  'text!templates/nine/modal/franchisemodal.html',
  'text!templates/nine/modal/modal_form_alert.html',

  'bootstrap-daterangepicker'

], function (Backbone, Mustache, Common, i18n, template, modalform, modalAlert) {
  'use strict';

  var datatables;

  var targetUrl = './model/nine/Franchise';

  var _data = {};

  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events: {
    	'click button.franchise-detail' : 'load_detail',
    	'click button.franchise-new' : 'modal_init',
    	'click button#search' : 'search',
    	'click .add-on-datepicker' : 'trigger_calender'
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

      view.datatablesInit();

      return view;
    },
    datatablesInit : function(){
    	datatables = $("#franchise-tables").DataTable({
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
		      { "data" : "id", "name" : "id" },
			  { "data" : "franCd", "name" : "fran_cd"},
			  { "data" : "franNm", "name" : "fran_nm"},
			  { "data" : "companyNm", "name" : "company_nm"},
			  { "data" : "bizNo", "name" : "biz_no"},
			  { "data" : "ceoNm", "name" : "ceo_nm"},
			  { "data" : "id"}
			],
		    "columnDefs":  [ {
		    	"render": function ( data, type, row ) {
		            return '<button class="btn btn-primary btn-sm franchise-detail" data-toggle="modal" data-target=".bs-example-modal-lg" target-code="'+data+'" >'+i18n.BO0022+'</button>';
		        },
		    	"bSortable": false,
		        "targets": 6
		    } ],
	        "order": [[ 1, 'asc' ]],
		    "pageLength": 10,
		    "lengthChange": false,
		    "processing": true,
		    "rowReorder": true,
		    "searching": false
		});

    	/*
    	 * 번호컬럼 값을 1번부터 세팅
    	 */
//    	datatables.on( 'order.dt search.dt', function () {
//    		datatables.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
//                cell.innerHTML = i+1;
//            });
//        }).draw();
    },
    load_detail: function (event) {
    	var view = this;
    	var _this = $(event.target);
    	$.ajax({
    		url : targetUrl,
    		data : {
    			data : JSON.stringify({ id : _this.attr("target-code")})
    		},
    		success : function(response){
    			if(response.success){
    				var data = response.list;
    		        data.i18n = i18n;
    		        data = Common.JC_format.handleData(data);
	        		var code = view.loadBaseCode(data);
    				var _template = Mustache.to_html(modalform, $.extend(code, data));

    				$('#franchise-modal').html(Mustache.to_html(_template, view.buildOptions(data)));
    				
    				if($('[name=openDt]').val() != null){
    					$('[name=openDt]').val($('[name=openDt]').val().replace(' 00:00:00.000', ''))
    				}
    				
    				if($('[name=closeDt]').val() != null){
    					$('[name=closeDt]').val($('[name=closeDt]').val().replace(' 00:00:00.000', ''))
    				}   				 

    				view.form_init('PUT', $('#franchise-modal'));
    			} else {
    				$('#franchise-modal').modal('toggle');
    			}
    		}
    	});
    },
    datatablesReload : function(){
    	var view = this;
    	if(datatables != undefined){
    		datatables.draw();
    	}
    },
    search : function(){
    	var view = this;
    	var $form = $('form');

    	_data= $.extend(_data, $form.serializeObject());

    	$.each(Object.keys(_data), function(idx, _key){
    		if ( _data[_key] == "" || _data[_key] === null || (typeof _data[_key] == "object" && $.isEmptyObject(_data[_key])) ) {
    			delete _data[_key];
    		}
    	});
    	view.datatablesReload();
    },
    buildOptions : function(object) {
        for (var i in object) {
            object[i + '=' + object[i]] = true;
        }
        return object;
    },
    modal_init : function(){
		var data = this.loadBaseCode();
        data.i18n = i18n;

    	$('#franchise-modal').html(Mustache.to_html(modalform, data));
    	this.form_init('POST', $('#franchise-modal'));
    },
    form_init : function(method, $modal){
    	var view = this;

    	form_select.init();

		$.validate({
			validateOnBlur : false,
		    scrollToTopOnError : false,
		    showHelpOnFocus : false,
		    errorMessagePosition : $('.form-validatoin-alert-detail'),
		    addSuggestions : false,
		    onError : function($form) {
		    	var _target = $('#modal-form');

		    	if(_target.find('.alert').length == 0){
		    		_target.prepend(Mustache.to_html(modalAlert, {i18n:i18n}));
		    	}
	        },
	        onSuccess : function($form) {

	        	var openDt = $('[name=openDt]').val();
	        	if (openDt != '') $('[name=openDt]').val(openDt+' 00:00:00');

	        	var closeDt = $('[name=closeDt]').val();
	        	if (closeDt != '') $('[name=closeDt]').val(closeDt+' 00:00:00');

	        	var params = $form.serializeObject();

	        	$.ajax({
	        		url : targetUrl,
	        		type: 'POST',
	        		data : {
	        			"_method" : method,
	        			"data" : JSON.stringify(params)
	        		},
	        		success : function(response) {
	        			if (response.success) {
	        				$modal.modal('toggle');
	        				datatables.draw();
	        			} else {
	        				alert(i18n.BO4019+' : "'+response.errMsg+'"');
	        			}
	        		}
	        	});
	        	return false;
	        }
		});

		$('#timepicker1').daterangepicker({
		      	autoApply : true,
				singleDatePicker: true,
				autoUpdateInput:false,
	  		  locale: {
						format: Common._formatCalViewDay,
						separator: Common._formatCalSeparator,
						applyLabel: i18n.BO0023,
						cancelLabel: i18n.BO0026,
						fromLabel: i18n.BO3032,
						toLabel: i18n.BO3033,
						daysOfWeek: [i18n.BO3031, i18n.BO3025, i18n.BO3026, i18n.BO3027, i18n.BO3028, i18n.BO3029, i18n.BO3030],
						monthNames: [i18n.BO3013, i18n.BO3014, i18n.BO3015, i18n.BO3016, i18n.BO3017, i18n.BO3018, i18n.BO3019, i18n.BO3020, i18n.BO3021, i18n.BO3022, i18n.BO3023, i18n.BO3024],
						firstDay: 1
	  	      },
	  	      startDate: Common.JC_init_value.firstMonthDay("from-date"),
	  	      endDate: Common.JC_init_value.lastMonthDay("to-date")
	  		},
	  		function (start, end, label) {
	  			$('#timepicker1').val(start.format(Common._formatCalViewDay));
	  			$('#openDt').val(end.format(Common._formatCalViewDay));
	  	     }
	  	);
		
		$('#timepicker2').daterangepicker({
	      	autoApply : true,
			singleDatePicker: true,
			autoUpdateInput:false,
  		  locale: {
					format: Common._formatCalViewDay,
					separator: Common._formatCalSeparator,
					applyLabel: i18n.BO0023,
					cancelLabel: i18n.BO0026,
					fromLabel: i18n.BO3032,
					toLabel: i18n.BO3033,
					daysOfWeek: [i18n.BO3031, i18n.BO3025, i18n.BO3026, i18n.BO3027, i18n.BO3028, i18n.BO3029, i18n.BO3030],
					monthNames: [i18n.BO3013, i18n.BO3014, i18n.BO3015, i18n.BO3016, i18n.BO3017, i18n.BO3018, i18n.BO3019, i18n.BO3020, i18n.BO3021, i18n.BO3022, i18n.BO3023, i18n.BO3024],
					firstDay: 1
  	      },
  	      startDate: Common.JC_init_value.firstMonthDay("from-date"),
  	      endDate: Common.JC_init_value.lastMonthDay("to-date")
  		},
  		function (start, end, label) {
  			$('#timepicker2').val(start.format(Common._formatCalViewDay));
  			$('#closeDt').val(end.format(Common._formatCalViewDay));
  	     }
  	);
    },
    loadBaseCode : function(obj){
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
    },
    trigger_calender : function(event){
    	var _this = $(event.target);
    	var _cal = _this.closest('div').find('.date-picker');
    	_cal.trigger('click');    	
    }
    /*,isAddress : function(){
    	daum.postcode.load(function(){
			new daum.Postcode({
	            oncomplete: function(data) {
	                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

	                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
	                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
	                var fullAddr = ''; // 최종 주소 변수
	                var extraAddr = ''; // 조합형 주소 변수

	                // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
	                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
	                    fullAddr = data.roadAddress;

	                } else { // 사용자가 지번 주소를 선택했을 경우(J)
	                    fullAddr = data.jibunAddress;
	                }

	                // 사용자가 선택한 주소가 도로명 타입일때 조합한다.
	                if(data.userSelectedType === 'R'){
	                    //법정동명이 있을 경우 추가한다.
	                    if(data.bname !== ''){
	                        extraAddr += data.bname;
	                    }
	                    // 건물명이 있을 경우 추가한다.
	                    if(data.buildingName !== ''){
	                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
	                    }
	                    // 조합형주소의 유무에 따라 양쪽에 괄호를 추가하여 최종 주소를 만든다.
	                    fullAddr += (extraAddr !== '' ? ' ('+ extraAddr +')' : '');
	                }

	                // 우편번호와 주소 정보를 해당 필드에 넣는다.
	                $('#zipcode').val(data.zonecode); //5자리 새우편번호 사용
	                $('.addr1').val(fullAddr);
	                $('.addr2').focus();

	            }
	        }).open();
        });
	}*/

  });

  return ContentView;
});