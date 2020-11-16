/*
 * Filename	: stampSetManage.js
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
  
  'text!templates/nine/stampSetManage.html',
  'text!templates/nine/modal/stamp_form.html',
  'text!templates/nine/modal/modal_form_alert.html',

  'moment'
], function (Backbone, Mustache, Common, i18n, template, stampForm, modalAlert) {
  'use strict';

  var _data = {};

  var targetUrl = './model/nine/Stamp';

  var imgUpTargetUrl = "./model/nine/ImageUpload";

  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events : {
    	'click #search_oper' : 'search_oper',
    	'change #stampBg' : 'mainImagePreView',
		'change #stampIcon' : 'mainImagePreView',
		'click .rdio' : 'selectRdio'
    },
    initialize: function () {
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
      
      data.fr = (view.model != undefined ? view.model.toJSON()[0] : {});
      data.i18n = i18n;
      
      var rendered = Mustache.to_html(view.template, data);
      $(view.el).append(rendered);

      view.selectedInit();

      if(data.isSt == true){
    	  view.search_oper 
      }
      
      return view;
    },
    selectedInit : function (){
    	$('#store_select').select2();
    	$('#brand_select').select2();

    	$('#store_select').select2().on('change', function(){
			_data.data = JSON.stringify({storeId : $(this).val()});
			_data.storeId = $(this).val();
        });
    	
    	$('#brand_select').select2().on('change', function(){
    		_data.data = JSON.stringify({brandId : $(this).val()});
			_data.brandId = $(this).val();
			
    		$.ajax({
    			url : './model/nine',
    			data : {
    				data : JSON.stringify({ brandId : $(this).val() })
    			},
    			success : function(result){
    				if(result.success){
    					var defualt_array = [{id: '', storeNm : i18n.BO1033}];

    					$("#store_select").find('option').remove();

    					$('#store_select').select2({
    						data : defualt_array.concat(result.list),
    						templateResult : function(state) {
    							return state.storeNm;
    						},
    						templateSelection : function(data, container) {
    						    return data.storeNm;
    						}
    					})
    				}
    			}
    		})
    	});

        $('#franchise_select').select2().on('change', function(){
        	_data.data = JSON.stringify({franId : $(this).val()});
        	_data.franId =  $(this).val();
        	
        	$.ajax({
    			url : './model/Brand',
    			data : {
    				data : JSON.stringify({ franId : $(this).val() })
    			},
    			success : function(result){
    				if(result.success){
    					var defualt_array = [{id: '', brandNm : i18n.BO1032}];

    					$("#brand_select").find('option').remove();
    					$("#store_select").find('option').remove();

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
    			}
    		})
    	});
    },
    selectRdio : function (event){
    	if(event.target.value == "403001"){
        $('#discountLabel').text(i18n.BO2431);
        //$('#discountLimitLabel').text(i18n.BO2432);
        
        $('#discount').val('');
        $('#discountLimit').val('');
        $('#discountLimit').attr('readonly',false);
    		
    	}else if(event.target.value == "403002"){
        $('#discountLabel').text(i18n.BO2170);
        //$('#discountLimitLabel').text(i18n.BO2162);
        
        $('#discount').val('');
        $('#discountLimit').val('0');
        $('#discountLimit').attr('readonly',true);
    	}
    },
    search_oper : function (){
    	var view = this;

    	if (_data.brandId == '') {
    		alert(i18n.BO4003);
    		return false;
    	}

    	$.ajax({
    		url : targetUrl,
    		data : _data,
    		success : function(result) {
    	    	var rqMethod = result.rqMethod;
    	    	var gubun = result.gubun;

    	    	if (result.success) {
    	    		var __data = {};
    	    		__data = result.data;
    	    		__data.i18n = i18n;
    	    		__data = Common.JC_format.handleData(__data);
    	    		
    				if (rqMethod == 'POST') {
    					if (gubun == 'brandSet') {
    						$('#stamp-form-body').html(Mustache.to_html(stampForm, __data));
    					}
    					else {
    						__data.storeId = _data.storeId;
    						$('#stamp-form-body').html(Mustache.to_html(stampForm, __data));
    						$('[name=brandId]').val($('#brand_select').val());
    						$('[name=storeId]').val($('#store_select').val());
    					}
    				}
    				else {
    					__data.stampTerm = Common.JC_format.transTerm(__data.stampTerm);
    					__data.couponTerm = Common.JC_format.transTerm(__data.couponTerm);
    					__data.formatTerm = Common.JC_format.formatTerm();
    					$('#stamp-form-body').html(Mustache.to_html(stampForm, view.buildOptions(__data)));
    					if(result.data.stampBg != null) $('#main_img_container').show();
    					if(result.data.stampIcon != null) $('#main_img_container2').show();
    				}

    				view.form_init(rqMethod);
    				$('[name=gubun]').val(gubun);
    			}
    			else {
    				alert(i18n.BO4020);
    				$('#stamp-form-body').html('');
    				$('#stamp-form-body').hide();
    			}
    		}
    	})
    },
    form_init : function(method){
    	var view = this;

    	$('#stamp-form-body').find('select').select2();
    	$('#stamp-form-body').show();

		$.validate({
			validateOnBlur : false,
		    scrollToTopOnError : false,
		    showHelpOnFocus : false,
		    errorMessagePosition : $('.form-validatoin-alert-detail'),
		    addSuggestions : false,
		    onError : function($form) {
		    	var _target = $("modal-form");

		    	if(_target.find('.alert').length == 0){
		    		_target.prepend(Mustache.to_html(modalAlert, {i18n:i18n}));
		    	}
	        },
	        onSuccess : function($form) {
        		var params = $form.serializeObject();
        		var gubun = $('[name=gubun]').val();

        		params.stampTerm = Common.JC_format.transTerm(params.stampTerm);
        		params.couponTerm = Common.JC_format.transTerm(params.couponTerm);

	        	$.ajax({
	        		url : targetUrl,
	        		type: 'POST',
	        		data : {
	        			"_method" : method,
	        			"gubun" : gubun,
	        			"data" : JSON.stringify(params)
	        		},
	        		success : function(response) {
	        			if (response.success) {
							var _data = new FormData();
							var _bg_file = $form.find('#stampBg').get(0);
							var _icon_file = $form.find('#stampIcon').get(0);

							if (_bg_file.files[0]) {
								_data.append("gubun", gubun);
								_data.append("id", response.id);
								_data.append("_file_"+gubun+"_stampBg", _bg_file.files[0]);
								_data.append("type", "stampBg");
								$.ajax({
									data: _data,
									type: "POST",
									url: imgUpTargetUrl,
									async: false,
									cache: false,
									contentType: false,
									processData: false,
									success: function(result) {
										if (!result.success) {
											alert(i18n.BO4021);
										}
									}
								});
							}

							if (_icon_file.files[0]) {
								_data = new FormData();
								_data.append("gubun", gubun);
								_data.append("id", response.id);
								_data.append("_file_"+gubun+"_stampIcon", _icon_file.files[0]);
								_data.append("type", "stampIcon");
								$.ajax({
									data: _data,
									type: "POST",
									url: imgUpTargetUrl,
									async: false,
									cache: false,
									contentType: false,
									processData: false,
									success: function(result) {
										if (!result.success) {
											alert(i18n.BO4022);
										}
									}
								});
							}
							
							alert(i18n.BO4046);
	        				view.search_oper();
	        			}else{
	        				alert(i18n.BO4047);
	        				view.search_oper();
	        			}
	        		}
	        	});

	        	return false;
	        }
		});
    },
    buildOptions : function(object) {
        for (var i in object) {
            object[i + '=' + object[i]] = true;
        }
        return object;
    },
	mainImagePreView : function(event){
		var _this = $(event.target);
		var id = event.currentTarget.getAttribute('id');
		var ext = _this.val().split('.').pop().toLowerCase(); //확장자

        if($.inArray(ext, ['gif', 'png', 'jpg', 'jpeg']) == -1) {
        	if ($.browser.msie) {
        		// ie 일때 input[type=file] init.
        		_this.replaceWith( $("#filename").clone(true) );
        	} else {
        		// other browser 일때 input[type=file] init.
        		_this.val("");
        	}
            window.alert(i18n.BO4009+i18n.BO4010);
        } else {
            var file = _this.prop("files")[0];
            var blobURL = window.URL.createObjectURL(file);

            //var _image = $('<img />').attr('src', blobURL);

            if (id == 'stampBg') {
//            	$('#main_img_target').html(_image);
            	$('#main_img_target').find('img').attr('src', blobURL);
            	$('#main_img_container').show();
            }
            else {
//            	$('#main_img_target2').html(_image);
            	$('#main_img_target2').find('img').attr('src', blobURL);
            	$('#main_img_container2').show();
            }
        }
	}
  });

  return ContentView;
});