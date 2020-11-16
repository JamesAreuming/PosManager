/*
 * Filename	: salesDetailManage.js
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

define([
  'backbone'
  ,'mustache'
  ,'common'
  ,'i18n!templates/globals/lang/nls/language'
  
  ,'text!templates/sales/salesDetailManage.html'
  ,'text!templates/sales/salesDetailChartAndData.html'
  ,'text!templates/sales/modal/salesDetail_modal.html'

  ,'bootstrap-daterangepicker'

], function (Backbone, Mustache, Common, i18n, template, chartAndData, salesDetailModal) {
  'use strict';

  var _data = {};

  var targetUrl = './model/sales/SalesDetail';

  var main_tables;

  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events : {
    	'click button#search' : 'search',
		'click button#csv-exp' : 'csv_exp',
		'change #franchise_select' : 'selectBrand',
		'change #brand_select' : 'selectStore',
    	'click .sales-detail' : 'load_detail',
		'click #cancel-btn' : 'cancel_btn',
		'click .add-on-datepicker' : 'trigger_calender'
    },
    initialize: function () {
      this.trigger('remove-compnents');
      this.template = template;
      this.listenTo(this.model, 'sync', this.render);
    },
    render: function () {
    	var view = this;
    	var data = {};
    	data.cd = view.loadBaseCode('PathType,OrderStatus', false);
    	
        data.frList = (view.model != undefined ? view.model.toJSON()[0].frList : {});
        data.brList = (view.model != undefined ? view.model.toJSON()[0].brList : {});
        data.stList = (view.model != undefined ? view.model.toJSON()[0].stList : {});
        
        _data.userInfo = view.model.toJSON()[0].session.userInfo;
        
        _data.franId = _data.userInfo.franId;
        _data.brandId = _data.userInfo.brandId;
        _data.storeId = _data.userInfo.storeId;
        
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
        view.setCalendarArea();

        // 스토어 사용자가 로그인시 자동조회
        if(data.isSt == true || data.isBr == true){
      	  view.search();
        }
        
    	return view;
    },
    
    // setup calendar
    setCalendarArea : Common.JC_calendar.searchRange(i18n),
    
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
      					});
      				}
      			}  //END : store select result
      		});  //END : store select ajax
        });  //END : brand select change
    	
    	$('#store_select').select2().on('change', function(){
    		_data.storeId = $(this).val();
        });
    },
    search : function() {
    	var view = this;

    	if (_data.franId == "" || _data.franId == undefined) {
    		alert(i18n.BO4001);
    		return false;
    	}
    	
    	if (_data.brandId == "" || _data.brandId == undefined) {
    		alert(i18n.BO4002);
    		return false;
    	}
    	
    	if ($('#daterangepicker').val() == 'Invalid date - Invalid date') {
    		alert(i18n.BO4032);
    		return false;
    	}

    	// 검색데이터 세팅
    	var data = {};
    	
    	data._method = 'GET';
		
    	// 검색폼 정보
		data.data = $('#be-search').serializeObject();
		
		// 프랜차이즈 ID
		data.data.franId = _data.franId;
		// 브랜드 ID
		data.data.brandId = _data.brandId;
		// 스토어 ID
		data.data.storeId = _data.storeId;
		
		data.data = JSON.stringify(data.data);
		
		// 사용자 정보
		data.userInfo = JSON.stringify(_data.userInfo);
		
    	$.each(Object.keys(data), function(idx, key){
    		if ( data[key] == "" || data[key] === null || (typeof data[key] == "object" &&  $.isEmptyObject(data[key])) ) {
    			delete data[key];
    		}
    	});
    	
		$.ajax({
			"url" : targetUrl,
			"type" : 'POST',
			"data" : data,
			"async": false,
			success : function(result) {
				if (result.success) {
					var data = {};
					data.list = result.list;
			        data.searchYn = true;
			        view.searchYn = true;
			        
			        data.i18n = i18n;
			        data = Common.JC_format.handleData(data);

			        $('#search-table').html(Mustache.to_html(chartAndData, data));
					view.tableHtml(result.list);
				}
			}
		});
    },
    tableHtml: function (data) {
    	var html = '';    	

    	if (data.length > 0) {
    		$.each(data, function(idx, val) {
    			var tag = 'even gradeC';

    			if (val.orderSt == '607003') {
    				tag = 'odd gradeX back_red';
    			}
    			else if (val.id == '0') {
    				tag = 'odd gradeX back_blue';
    			}

    			html += '<tr class="'+tag+'">';

    			for (var key in val) {
    				if (key != 'id' && key != 'orderSt') {
    					if (key == 'orderNo') {
    						if (val.id == '0') html += '<td></td>';
    						else html += '<td><button class="btn btn-primary btn-sm sales-detail" data-toggle="modal" data-target=".bs-example-modal-lg" target-code="'+ val.id +'">'+ val[key] +'</button></td>';
    					} else if (key == 'payDay') {
    						html += '<td>' + Common.JC_format.day(val[key]) + '</td>';
    					} else if (key == 'discount' || key == 'netSales' || key == 'realSales' || key == 'sales' || key == 'tax') {
    						html += '<td>' + Common.JC_format.number(val[key], true) + '</td>';
    					} else {
    						html += '<td>' + val[key] + '</td>';
    					}
    				}
    			}

    			html += '</tr>';
    		});
    	}
    	else {
			html += '<tr class="even gradeC"><td colspan="8">'+i18n.BO0006+'</td></tr>';
    	}

    	$('#tbody-id').html(html);
    },
    csv_exp: function () {
    	if (this.searchYn) {
    		$('#summary-table').tableToCSV();
    	}
    	else {
    		alert(i18n.BO4030);
    	}
    },
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
    				var data = {};
    				data.itemList = response.itemList;
    				data.list = response.list;
    				data.orderCancel = response.orderCancel;
    				data = view.buildOptions(data);
    				data.i18n = i18n;
    				data = Common.JC_format.handleData(data);
    				
    				$('#salesDetail-modal').html(Mustache.to_html(salesDetailModal, data));
    				//view.form_init('GET', $('#salesDetail-modal'));
    			}
    		}
    	});
    },
    cancel_btn : function(event) {
    	var view = this;
    	var _this = $(event.target);

        if (confirm(i18n.BO4060)) {
	    	$.ajax({
	    		url : targetUrl,
        		type: 'POST',
        		async: false,
	    		data : {
	    			'_method' : 'PUT',
	    			'data' : JSON.stringify({id : _this.attr("target-code")	})
	    		},
	    		success : function(response){
	    			if(response.success){
	    				$('#salesDetail-modal').modal('toggle');
	    			}
	    		}
	    	});
        }
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
    loadBaseCode : function(codeAlias, option){

    	var data = {};
    	
		$.ajax({
			url : './model/Codes',
			method : 'POST',
			async: false,
			data : {
				'codes' : codeAlias.trim()
			},
			success : function(result){
				if(result.success){

					if(option){
						$.each(Object.keys(result.codes), function(idx, code){
							$.each(result.codes[code], function(_idx, _code){
								_code.selections = '{{#'+code+'='+_code.baseCd+'}} selected="selected" {{/'+code+'='+_code.baseCd+'}}';
							});
						});
					}
					
					data = $.extend(result.codes, _data);

				} else {
		    		$('#items-modal').html('');
		    		$('#items-modal').modal('toggle');
				}
			}
		});
		
		return data;
    }
  });

  return ContentView;
});