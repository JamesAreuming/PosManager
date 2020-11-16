/*
 * Filename	: anlsGoalsManage.js
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
  
  ,'text!templates/sales/anlsGoalsManage.html'
  ,'text!templates/sales/modal/modal_form_alert.html'

  ,'bootstrap-daterangepicker'
  ,'fusioncharts'
  ,'fusioncharts.theme.fint'
  ,'jquery.tabletoCSV'

], function (Backbone, Mustache, Common, i18n, template, modalAlert) {
  'use strict';

  var _data = {};

  var targetUrl = './model/sales/anlsGoals';
  
  var main_tables;

  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events : {
    	'click button#search' : 'searchAnlsGoals',
    	'click #saveBtn'	  : 'saveanlsGoals',
    	'keypress :input': 'inputNumValidate'	

    },
    initialize: function () {
      this.trigger('remove-compnents');
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
        
      var rendered = Mustache.to_html(view.template, data);
      $(view.el).append(rendered);

      view.selectedRollInit();      

      return view;
    },
    
    selectedRollInit : function (){
    	var view = this;

    	$('#franchise_select').select2();
    	$('#brand_select').select2();
    	$('#store_select').select2();
    	$('#year_select').select2();
    	
    	
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
      			}  //END : store select result
      		});  //END : store select ajax
        });  //END : brand select change
    	
    	$('#store_select').select2().on('change', function(){
    		_data.storeId = $(this).val();
        });
    },

    //Search Btn
    searchAnlsGoals : function() {
    	var view = this;

    	if(jQuery("#franchise_select option:selected").val() == "") {
    		//BO4001 : 법인을 선택해 주세요
    		alert(i18n.BO4001);
    		$("#franchise_select").focus();
    		return false;
    	}

    	if(jQuery("#brand_select  option:selected").val() == "") {
    		//BO4002 : Please select the brand.
    		alert(i18n.BO4002);
    		jQuery("#brand_select").focus();
    		return false;
    	}

    	if(jQuery("#store_select  option:selected").val() == "") {
        	//BO4003 : Please select stores.
    		alert(i18n.BO4003);
    		jQuery("#store_select").focus();
      	  return false;
        }
    	
    	if(jQuery("#year_select  option:selected").val() == "") {
        	//BO0009':"Please input the required fields."
    		alert(i18n.BO0009);
    		jQuery("#year_select").focus();
      	  return false;
        }
    	
  
    	$.ajax({
			url : targetUrl,
			data : {
    			"data" : JSON.stringify({
    									 brandId : jQuery("#brand_select  option:selected").val(),
    									 storeId : jQuery("#store_select  option:selected").val(),
    									 year  	 : jQuery("#year_select   option:selected").val()
    									})
			},
			async: false,
			success : function(result) {
				if (result.success) {
					var data = {};
					var apGoalList = [result.apGoalList[0].MONTH1_GOAL_AMT,
					                    result.apGoalList[0].MONTH2_GOAL_AMT,
					                    result.apGoalList[0].MONTH3_GOAL_AMT,
					                    result.apGoalList[0].MONTH4_GOAL_AMT,
					                    result.apGoalList[0].MONTH5_GOAL_AMT,
					                    result.apGoalList[0].MONTH6_GOAL_AMT,
					                    result.apGoalList[0].MONTH7_GOAL_AMT,
					                    result.apGoalList[0].MONTH8_GOAL_AMT,
					                    result.apGoalList[0].MONTH9_GOAL_AMT,
					                    result.apGoalList[0].MONTH10_GOAL_AMT,
					                    result.apGoalList[0].MONTH11_GOAL_AMT,
					                    result.apGoalList[0].MONTH12_GOAL_AMT
					                    ];
					var apSalesList = result.apSalesList
					
					//data.apGoalList = result.apGoalList;
			        //alert(apGoalList);
			        data.i18n = i18n;
					//$('#search-table').html(Mustache.to_html(chartAndData, data));
			        // Create Table
					view.tableHtml(apGoalList,apSalesList);
					jQuery("#saveBtn").show();

				}
			}
		});
    },
    
    inputNumValidate: function (event) {
        if (event.which && (event.which <= 45 || event.which >= 58) && event.which != 8 && event.which != 46 ) {
          event.preventDefault();
        }

    },
    tableHtml: function (apGoalList,apSalesList) {
    	var html = '';

    	html += '<thead>';
    	    	
    	// 1.Create Table-Header
    	html += "<tr class='dataTables_wrapper_th_min'>";
    	html += "<th style='width: 50%;'>" + i18n.BO2385 + "</th>";  //구분
    	html += "<th style='width: 50%;'>" + i18n.BO2433 + "</th>";  //목표매출
    	html += "</tr>";    	
    	
    	html += "</thead>";

    	
    	// 2.Create Table-ListData
    	html += "<tbody>";   	
    	
    	var monthList = [i18n.BO3013,i18n.BO3014,i18n.BO3015,i18n.BO3016,i18n.BO3017,i18n.BO3018,
    	                 i18n.BO3019,i18n.BO3020,i18n.BO3021,i18n.BO3022,i18n.BO3023,i18n.BO3024];
    	
    	for( var i = 0; i < 12 ; i++ ) {

        	html += "<tr class='even gradeC'>";
        		html += 	" <td class='title' style='text-align: center;'>" + monthList[i] + "</td>";
        		html += 	" <td>" + Common.JC_format.number(apGoalList[i],true) +	"</td>";  //
	        	
        		
            html += "</tr>";	
    	}
    	
    	html += "<tr class='even gradeC'>";
		html += 	"<td class='title' style='text-align: center;'>" + i18n.BO2278 + "</td>";
		html += 	"<td class='title' style='text-align: center;'>" + "" + "</td>";
		html += "</tr>";
        
    	html += '</tbody>';
    	
    	$('#etc-table').html(html);
    	
    	
		/*$.each(apGoalList, function(idx, val) {
			
			//좌측 월(Month) 셋팅
			html += "<td class='title'>" + i18n.i +  + "</td>";
			//First header at Left
			if(idx == 0){
				
			} else {
				html += "<td class='price'>" +  Common.JC_format.number(val[key]) + "</td>";
			}					
			
			
			html += "</tr>";
			
		});*/
		
		// END OF 2.Create Table-ListData
		
    },
    
    /**
     * 매출목표 저장
     */
    saveanlsGoals : function() {
    	var view = this;
    	var _name = "";
    	var _brandId = "";
    	var _storeId = "";

    	
    	if(jQuery("#franchise_select option:selected").val() == "") {
    		//BO4001 : 법인을 선택해 주세요
    		alert(i18n.BO4001);
    		$("#franchise_select").focus();
    		return false;
    	}

    	if(jQuery("#brand_select  option:selected").val() == "") {
    		//BO4002 : Please select the brand.
    		alert(i18n.BO4002);
    		jQuery("#brand_select").focus();
    		return false;
    	}

    	if(jQuery("#store_select  option:selected").val() == "") {
        	//BO4003 : Please select stores.
    		alert(i18n.BO4003);
    		jQuery("#store_select").focus();
      	  return false;
        }
    	
    	if(jQuery("#year_select  option:selected").val() == "") {
        	//BO0009':"Please input the required fields."
    		alert(i18n.BO0009);
    		jQuery("#year_select").focus();
      	  return false;
        }

    	if( jQuery("#month0").val() == ""	|| jQuery("#month1").val() == ""   || jQuery("#month2").val() == ""  || 
    		jQuery("#month3").val() == ""   || jQuery("#month4").val() == ""   || jQuery("#month5").val() == ""  || 
    		jQuery("#month6").val() == ""   || jQuery("#month7").val() == ""   || jQuery("#month8").val() == ""  || 
    		jQuery("#month9").val() == ""   || jQuery("#month10").val() == ""  || jQuery("#month11").val() == "" ) {
	        	//BO4003 : Please select stores.
	    		alert(i18n.BO0009);
	      	  return false;
        }
    	
    	//Ajax
    	$.ajax({
    		url : targetUrl,
    		type: 'POST',
    		data : {
    			"_method" : 'POST',
    			"data" : JSON.stringify({
    									 brandId : jQuery("#brand_select  option:selected").val(),
    									 storeId : jQuery("#store_select  option:selected").val(),
    									 year  	 : jQuery("#year_select   option:selected").val(),
    									 month1GoalAmt  : jQuery("#month0").val(),
    									 month2GoalAmt  : jQuery("#month1").val(),
    									 month3GoalAmt  : jQuery("#month2").val(),
    									 month4GoalAmt  : jQuery("#month3").val(),
    									 month5GoalAmt  : jQuery("#month4").val(),
    									 month6GoalAmt  : jQuery("#month5").val(),
    									 month7GoalAmt  : jQuery("#month6").val(),
    									 month8GoalAmt  : jQuery("#month7").val(),
    									 month9GoalAmt  : jQuery("#month8").val(),
    									 month10GoalAmt : jQuery("#month9").val(),
    									 month11GoalAmt : jQuery("#month10").val(),
    									 month12GoalAmt : jQuery("#month11").val()
    									})
    		},
    		success : function(response){
    			if(response.success){
    				//'BO4046':'저장 완료 하였습니다.'
    				alert(i18n.BO4046);
    				//view.renderTreegrid();
    			}else{
    				alert("error : " +response.errMsg);
    			}
    		}
    	});
    },
    
    modal_init : function(){
    	var view = this;
    	if(_data.brandId != undefined && _data.brandId != ''){
    		var data = view.loadBaseCode();

			$('#anlsGoals-modal').html(Mustache.to_html(anlsGoalsModal, data));
			view.form_init('POST', $('#anlsGoals-modal'));

    	} else {
    		alert(i18n.BO4002);
    		$('#anlsGoals-modal').html('');
    		$('#anlsGoals-modal').modal('toggle');
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
    				var data = view.loadBaseCode();
    				var _template = Mustache.to_html(anlsGoalsModal, $.extend(data, _def_form_mustache));

    				$('#anlsGoals-modal').html(Mustache.to_html(_template, view.buildOptions(response.data)));
    				view.form_init('PUT', $('#anlsGoals-modal'));

    			}
    		}
    	});
    },
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
	    		    		$('#anlsGoals-modal').modal('toggle');
	    		    		main_tables.draw();
	        			}
	        		}
	        	});

	        	return false;
	        }
		});
		

//		view.load_map();
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
		    		$('#anlsGoals-modal').html('');
		    		$('#anlsGoals-modal').modal('toggle');
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
    }
  });

  return ContentView;
});