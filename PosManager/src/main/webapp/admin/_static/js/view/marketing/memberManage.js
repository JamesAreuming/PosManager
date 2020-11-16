/*
 * Filename	: memberManage.js
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

define([
  'bracket',
  'backbone',
  'mustache',
  'common',
  'i18n!templates/globals/lang/nls/language',
  
  'text!templates/marketing/memberManage.html',
  'text!templates/marketing/modal/member_detail.html',
  'text!templates/marketing/modal/email_modal.html',
  'text!templates/common/modal_form_alert.html',

  'summernote',
  'bootstrap-daterangepicker'  

], function (Brackets, Backbone, Mustache, Common, i18n, template, user_detail, modalform, modalAlert) {
  'use strict';

  var _data = {};

  var targetUrl = './model/marketing/Member';

  var main_tables;
  jQuery.browser = {};
  (function () {
      jQuery.browser.msie = false;
      jQuery.browser.version = 0;
      if (navigator.userAgent.match(/MSIE ([0-9]+)\./)) {
          jQuery.browser.msie = true;
          jQuery.browser.version = RegExp.$1;
      }
  })();
  
  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events : {
    	'click #usermanage-search'	: 'search',
    	'click #write_Email' 		: 'send',
    	'click #write_Sms'			: 'send',
    	'click #write_Push'			: 'send',
    	'click #detail_write_Email' : 'send',
    	'click #detail_write_Sms'	: 'send',
    	'click #detail_write_Push'	: 'send',
    	'click .user-detail'		: 'user_detail',    	
    	'click .all-checked'		: 'all_checked',   
    	'click .back-user-main'		: 'back_user_main',
    	'click .add-on-datepicker'	: 'trigger_calender',
    	'click .minimize'		: 'minimize'
    },
    initialize: function () {
      this.template = template;
      this.listenTo(this.model, 'sync', this.render);
    },
    render: function () {
      var view = this;
      var data = {};

      _data.userInfo = view.model.toJSON()[0].session.userInfo;
      data.i18n = i18n;
      data.cd = view.loadBaseCode('Gender');
      var rendered = Mustache.to_html(view.template, data);
      $(view.el).append(rendered);
      
      view.setCalendarArea();
      view.selectedInit(); 
      view.datatablesInit();
      $("#daterangepicker").val('');
      $("#from-date").val('');
      $("#to-date").val('');
      
      return view;
    },
 // setup calendar
    setCalendarArea : Common.JC_calendar.searchRange(i18n, true),
    
    selectedInit : function (){
    	$('select').select2({
    		width: '100%',
            minimumResultsForSearch: -1
    	});
    },
    datatablesInit : function(){    	
    	main_tables = $('#user-manage').DataTable({
    		"serverSide": true,
	  		"searching": false,	  		 
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
		      { "data" : "id"}, 
			  { "data" : "barcode", "name" : "BARCODE"}, 
			  { "data" : "mb", "name" : "MB"},
			  { "data" : "name", "name" : "NAME"}, 
			  { "data" : "birthday", "name" : "BIRTHDAY", render: function (data, type, row) {return Common.JC_format.day(data);}}, 
			  { "data" : "gender", "name" : "GENDER"}, 
			  { "data" : "email", "name" : "EMAIL" },
			  { "data" : "id"}
			],
		

		    "columnDefs":  [ {
		        "render": function ( data, type, row ) {
		        	return '<span class="checked"><input type="checkbox"  target-data="'+data+'" class="table-checkbox"></span>';
		        },
		        "bSortable": false,
		        "targets": 0    
		    }, {
		    	"render": function ( data, type, row ) {
		            return '<button type="button" class="btn btn-primary user-detail" target-data="'+data+'" >'+i18n.BO2155+'</button>';
		        },
		        "bSortable": false,
		        "targets": 7    
		    } ],
		    "order": [[ 1, 'desc' ]],
            "pageLength": 10,
            "lengthChange": false,
            "processing": true,
            "rowReorder": true,
            "rowId" : 'id'
		});
    },      
    send : function(event){
    	var _target = $(event.target);
    	var view = this;
    	var data = {};
    	var eventModal = '';
    	var id = _target.attr('id');
    	data.type = _target.attr('status');
		
		data = view.buildOptions(data);
		data.i18n = i18n;
		
		if (id.indexOf('detail') > -1) {	// 멤버 상세 페이지일 때
			eventModal = 'detail-event-modal';
//			$('#event-model').hide();
		} else { // 멤버 리스트 페이지일 때
			eventModal = 'event-modal';
		}
		
		$('#' + eventModal).html(Mustache.to_html(modalform, data));
		if ('email' == data.type) {	// 모달 타입이 email일 때 에디터 사용
		    $('#event-content').summernote({
		        lang: 'ko-KR',
		        height: 150,
		        dialogsInBody: true,
		        onChange: function(contents, $editable) {
		          var isEmpty = $('#event-content').summernote('isEmpty');
		          if(isEmpty){
		            $('#event-content').val('');
		          } else {
		            $('#event-content').val($('#event-content').summernote('code'));
		          }
		        },
		        onImageUpload: function(files, editor, welEditable) {
		          view.uploadImage(files[0], editor, welEditable);
		        }
		      });
		}
	    this.form_init('PUT', $('#' + eventModal), data.type);
	},
	validateContent : function(){
	      var _content = $('#event-content-layer');
	      var sHTML = $('#event-content').code();
	      if (sHTML.length == 0) {
	        _content.removeClass('has-success');
	        _content.addClass('has-error');
	        return false;
	      }
	      else {
	        _content.removeClass('has-error');
	        _content.addClass('has-success');
	        return true;
	      }
	},
	form_init : function(method, modal, type){
	      var view = this;
	      
	      $.validate({
	        validateOnBlur : false,
	        scrollToTopOnError : false,
	        showHelpOnFocus : false,
	        errorMessagePosition : $('.form-validatoin-alert-detail'),
	        addSuggestions : false,
	        onError : function($form) {
	          view.validateContent();
	          
	          var _target = $('#modal-form');
	          if(_target.find('.alert').length == 0){
	        	  _target.prepend(Mustache.to_html(modalAlert, {i18n:i18n}));
	          }
	        },
	        onSuccess : function($form) {
	         
	         if (!view.validateContent()) {
	            var _target = $('#modal-form');
	            if(_target.find('.alert').length == 0){
	            	_target.prepend(Mustache.to_html(modalAlert, {i18n:i18n}));
	            }
	            return false;
	          }
	          
	         var params = $form.serializeObject();
	         
	          params.status = type;
		      params.sendType = sendType;
		      
	          var sHTML = $('#event-content').code();
	          params.content = sHTML;	          
		      
	          var checkList = $('#user-manage').find('.table-checkbox:checked');
		      var checkArray =  [];		      
		      
		      var sendType = "";
		      if($("#id").val() == "" || $("#id").val() == undefined){
		    	  sendType = "multi"; 
		      }else{
		    	  sendType = "single";
		      }
		      
		      if(sendType == "multi"){
		    	  $.each(checkList, function(idx, data){
		    		  var _data = main_tables.row($(data).closest('tr').get(0)).data();
		    		  _data.title = $('#title').val();
		    		  _data.content = $('#content').val();
			      		checkArray.push(_data);
			      }); 
		    	  
		    	  if (modal.attr('id').indexOf('detail') > -1) {
		    		  // 멤버 상세 페이지에 저장된 멤버의 정보 추출
		    		 var userInfo = $('#user_info').val();
		    		 if (userInfo) {
		    			 checkArray.push(JSON.parse(userInfo));
		    		 }
		    	  } else { // 멤버 상세 페이지일 때
		    		  if(checkArray.length <= 0){
				    	  alert(i18n.BO4058);
				    	  return false;
				      }
		    	  }
		    	  
			      params.checkArray = checkArray;
		      }
		      
	          $.ajax({
	            url : targetUrl,
	            type: 'POST',
	            data : {
	              "_method" : method,
	              "data" : JSON.stringify(params)
	            },
	            success : function(response){
	              if(response.success){
//	                var _data = new FormData();
//	                 img 관련 로직이 없음.. 
//	                var _file = $form.find('#mainImage').get(0);
//
//	                if(_file.files[0]){
//	                  _data.append("imagefile", _file.files[0]);
//	                  _data.append("type", "main");
//	                  _data.append("id", response.id);
//	                  $.ajax({
//	                    data: _data,
//	                    type: "POST",
//	                    url: imgUpTargetUrl,
//	                    async: false,
//	                    cache: false,
//	                    contentType: false,
//	                    processData: false,
//	                    success: function(result) {
//	                      if(!result.success){
//	                        alert(i18n.BO4008);
//	                      }
//	                    }
//	                  });
//	                }
	                modal.modal('toggle');
	                main_tables.draw();
	              } 
	            }
	          });

	          return false;
	        }
	      });
	},
    sendMessage : function(event){
    	var _target = $(event.target);
    	var _target_status = _target.attr("status");
    	var checkList = $('#user-manage').find('.table-checkbox:checked');
    	var checkArray =  [];
    	var rows = null;
    	$.each(checkList, function(idx, data){
    		checkArray.push(main_tables.row($(data).closest('tr').get(0)).data());
    	});
    	
    	$.ajax({
    		url : targetUrl,
    		type: 'POST',
    		data : {
    			"_method" : 'PUT',
    			"status" : _target_status,
    			"datas" : JSON.stringify(checkArray) 
    		},
    		success : function(response){
    			if(response.success){
		    		//main_tables.draw();
    			} else {
    				alert(response.errors[0].message);
    			}
    		}
    	});
    },
    exportExcel : function(event){
    	_exportData.draw = 'excel';
    	_exportData.headers = 
    		JSON.stringify([{
    			idx : 0,
    			data : 'id',
    			title : '번호'
    		},  {
    			idx : 1,
    			data : 'barcode',
    			title : '고객코드'
    		}, {
    			idx : 2,
    			data : 'mb',
    			title : '휴대전화'
    		}, {
    			idx : 3,
    			data : 'username',
    			title : '고객명'
    		}, {
    			idx : 4,
    			data : 'birthday',
    			title : '생년월일'
    		}, {
    			idx : 5,
    			data : 'gender',
    			title : '성별'
    		}, {
    			idx : 6,
    			data : 'email',
    			title : '메일'
    		}]);    	

    	$.ajax({
    		url : targetUrl,
    		data : _exportData,
    		async : false,
    		success : function(result){
    			if(result.success){
    				location.href = result.url;
    			}
    		}
    	});    	
    },
    search : function(){
    	var view = this;
    	var $form = $('#rollSearchFrom');

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
    datatablesReload : function(){
    	var view = this;
    	if(main_tables != undefined){
    		main_tables.draw();
    	}
    },
    
    user_detail : function(event){
    	var target = $(event.target);
    	var userId = target.attr("target-data");
    	// 멤버 디테일 버튼 누를 때 해당 멤버의 정보를 멤버 상세 페이지에 저장
    	var _data = main_tables.row($(target).closest('tr').get(0)).data();
    	
//    	var tr = target.parent().parent();
//    	tr.find(':checkbox').attr('checked', true);
    	
    	$.ajax({
    		url : targetUrl,
    		data : {
    			userId : userId
    		},
    		success : function(result){
    			if(result.success){
    				var data = {};
    				data.i18n = i18n;
    				data = $.extend(data, result.data);
    				data.userInfo = JSON.stringify(_data);
    				
    				data = Common.JC_format.handleData(data);
    				var _template = Mustache.to_html(user_detail, data);
    				
    				$('#user_main').hide();
    		    	$('#user-detail').html(_template).show();
    		    	
    		    	$('#lastest-stamp-log').DataTable({
    		    		"serverSide": true,
    			  		"ordering": false,
    			  		"searching": false,
    			    	"language": {
    			            "url": "//cdn.datatables.net/plug-ins/1.10.11/i18n/English.json"
    			        },
    			        "ajax" : {
    			        	"url" : './model/marketing/StampLog',
    			        	"type": 'POST',
    				    	"data" : function(data){
    				    		data._method = 'GET';
    				    		
    	    		    		// 사용자 ID
    	    		    		data.data = {userId : userId};
    	    		    		
    	    		    		data.data = JSON.stringify(data.data);
    	    		    		data.userInfo = JSON.stringify(_data.userInfo);
    	    		    		
    				    		return data;
    				    	},
    				        "dataSrc" : 'list'
    			        },
    				    "columns" : [
			   		      { "data" : "id"},
						  { "data" : "insDate", "name":"INS_DATE", render: function (data, type, row) {return Common.JC_format.day(data);}}, 
						  { "data" : "insTime", "name":"INS_TIME", render: function (data, type, row) {return Common.JC_format.time(data);}}, 
						  { "data" : "storeNm", "name":"STORE_NM"}, 
						  { "data" : "stampSt", "name":"STAMP_ST", "visible": false},
						  { "data" : "stampStNm", "name":"STAMP_ST_NM"},
						  { "data" : "stampTp", "name":"STAMP_TP", "visible": false},
						  { "data" : "stampTpNm", "name":"STAMP_TP_NM"}
    					],
    					"order": [[ 1, 'desc' ]],
    				    "pageLength": 5,
    				    "lengthChange": false
    		    	});
    		    	
    		    	$('#lastest-coupon-log').DataTable({
    		    		"serverSide": true,
    			  		"ordering": false,
    			  		"searching": false,
    			    	"language": {
    			            "url": "//cdn.datatables.net/plug-ins/1.10.11/i18n/English.json"
    			        },
    			        "ajax" : {
    			        	"url" : './model/marketing/CouponLog',
    			        	"type": 'POST',
    				    	"data" : function(data){
    				    		
    				    		data._method = 'GET';
    				    		
    				    		// 사용자 ID
    	    		    		data.data = {userId : userId};
    	    		    		
    	    		    		data.data = JSON.stringify(data.data);
    	    		    		data.userInfo = JSON.stringify(_data.userInfo);
    	    		    		
    				    		return data;
    				    	},
    				        "dataSrc" : 'list'
    			        },
    				    "columns" : [
    				      { "data" : "id"},
    					  { "data" : "couponCd"}, 
    					  { "data" : "couponNm"}, 
    					  { "data" : "couponTp"},
    					  { "data" : "issueDt", render: function (data, type, row) {return Common.JC_format.day(data);}},
    					  { "data" : "expireDt", render: function (data, type, row) {return Common.JC_format.day(data);}}
    					],
    					"order": [[ 4, 'desc' ]],
    				    "pageLength": 5,
    				    "lengthChange": false,
    				    "processing": true,
    				    "rowReorder": true
    		    	});
    			} else {
    				alert(result.errMsg);
    			}
    		}
    	});
    },
    back_user_main : function (){
    	$('#user_main').show();
    	$('#user-detail').hide();
    },
    loadBaseCode : function(Type){
    	var data = {};
		$.ajax({
			url : './model/management/Codes',
			method : 'POST',
			async: false,
			data : {
				'codes' : Type
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
		    		$('#member-modal').html('');
		    		$('#member-modal').modal('toggle');
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
    trigger_calender : function(event){
    	var _this = $(event.target);
    	var _cal = _this.closest('div').find('.date-picker');
    	_cal.trigger('click');    	
    },
    all_checked : function(event){    	
    	if($(event.target).is(':checked')){
    		$('#user-manage').find('.table-checkbox').prop('checked', true);
    	} else {
    		$('#user-manage').find('.table-checkbox').prop('checked', false);
    	}
    }
  });

  return ContentView;
});