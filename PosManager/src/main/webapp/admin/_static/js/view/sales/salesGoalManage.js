/*
 * Filename	: salesGoalManage.js
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
  
  ,'text!templates/sales/salesGoalManage.html'
  ,'text!templates/sales/modal/modal_form_alert.html'
  
  ,'bootstrap-daterangepicker'
  ,'fusioncharts'
  ,'fusioncharts.theme.fint'
  ,'jquery.tabletoCSV'

], function (Backbone, Mustache, Common, i18n, template, modalAlert) {
  'use strict';

  var _data = {};

  var targetUrl = './model/sales/salesGoal';
  
  var main_tables;

  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events : {
    	'click button#search' : 'searchSalesGoal',
    	'click #saveBtn'	  : 'saveSalesGoal',
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
        
        if(data.userInfo.type == "300004"){
      	  data.isType = true;
        }else{
      	  data.isType = false;
        }
        data.isType = false; // TODO
        
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
      
      if (_data.storeId != "" && _data.storeId != undefined) {
    	  view.searchSalesGoal();
      }
      
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
      		$("#etc-table").empty();
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
			$("#etc-table").empty();
        });  //END : brand select change
    	
    	$('#store_select').select2().on('change', function(){
    		_data.storeId = $(this).val();
    		$("#etc-table").empty();
        });
    },

    //Search Btn
    searchSalesGoal : function() {
    	var view = this;

    	if (_data.franId == "" || _data.franId == undefined) {
    		alert(i18n.BO4001);
    		return false;
    	}

    	if (_data.brandId == "" || _data.brandId == undefined) {
    		alert(i18n.BO4002);
    		return false;
    	}
    	
    	if (_data.storeId == "" || _data.storeId == undefined) {
    		alert(i18n.BO4003);
    		return false;
    	}
    	
    	
    	if ($('#daterangepicker').val() == 'Invalid date - Invalid date') {
    		alert(i18n.BO4032);
    		return false;
    	}
  
    	$.ajax({
			url : targetUrl,
			data : {
    			"data" : JSON.stringify({
    									 brandId : _data.brandId ,
    									 storeId : _data.storeId ,
    									 year  	 : jQuery("#year_select   option:selected").val()
    									})
			},
			async: false,
			success : function(result) {
				if (result.success) {
					var data = {};
					
					var salesGoalList = [];
										
					if ( result.salesGoalList.length > 0 ) {
						salesGoalList =    [result.salesGoalList[0].MONTH1_GOAL_AMT,
						                    result.salesGoalList[0].MONTH2_GOAL_AMT,
						                    result.salesGoalList[0].MONTH3_GOAL_AMT,
						                    result.salesGoalList[0].MONTH4_GOAL_AMT,
						                    result.salesGoalList[0].MONTH5_GOAL_AMT,
						                    result.salesGoalList[0].MONTH6_GOAL_AMT,
						                    result.salesGoalList[0].MONTH7_GOAL_AMT,
						                    result.salesGoalList[0].MONTH8_GOAL_AMT,
						                    result.salesGoalList[0].MONTH9_GOAL_AMT,
						                    result.salesGoalList[0].MONTH10_GOAL_AMT,
						                    result.salesGoalList[0].MONTH11_GOAL_AMT,
						                    result.salesGoalList[0].MONTH12_GOAL_AMT
						                    ];
					}						
					
					//data.salesGoalList = result.salesGoalList;
			        //alert(salesGoalList);
			        data.i18n = i18n;
					//$('#search-table').html(Mustache.to_html(chartAndData, data));
			        // Create Table
					view.tableHtml(salesGoalList);
					jQuery("#saveBtn").show();

				}
			}
		});
    },
    
    inputNumValidate: function (event) {
        if (event.which && (event.which <= 47 || event.which >= 58) && event.which != 8 ) {
          event.preventDefault();
        }

    },
    tableHtml: function (salesGoalList) {
    	var html = '';

    	html += '<thead>';
    	    	
    	// 1.Create Table-Header
    	html += "<tr class='dataTables_wrapper_th_min'>";
    	html += "<th style='width: 50%;'>" + i18n.BO2441 + "</th>";  //월
    	html += "<th style='width: 50%;'>" + i18n.BO2442 + "</th>";  //목표매출액
    	html += "</tr>";    	
    	
    	html += "</thead>";

    	
    	// 2.Create Table-ListData
    	html += "<tbody>";   	
    	
    	var monthList = [i18n.BO3013,i18n.BO3014,i18n.BO3015,i18n.BO3016,i18n.BO3017,i18n.BO3018,
    	                 i18n.BO3019,i18n.BO3020,i18n.BO3021,i18n.BO3022,i18n.BO3023,i18n.BO3024];
    	
    	for( var i = 0; i < 12 ; i++ ) {

        	html += "<tr class='even gradeC'>";
        		html += 	" <td class='title' style='text-align: center;'>" + monthList[i] + "</td>";
        		html += 	" <td>" + "<input type='text' name='monthInput' id='" + "month" + i + "' class='form-control' style='width: 90%; ime-mode:disabled; text-align:right; float:right;'  maxlength=13 ";
	
	        	if ( salesGoalList.length > 0 ) {
	        		html += "value= '" + salesGoalList[i] + "'";
	        		//html += "value= '" + salesGoalList[i] + "'";
	        	}
        		
        		html += 	"/>";
        		html += 	"</td>";
            html += "</tr>";	
    	}
    	
    	/*html += "<tr class='even gradeC'>";
		html += 	"<td class='title' style='text-align: center;'>" + i18n.BO2278 + "</td>";
		html += 	"<td class='title' style='text-align: center;'>" + "" + "</td>";
		html += "</tr>";*/
        
    	html += '</tbody>';
    	
    	$('#etc-table').html(html);
    	
    	
		/*$.each(salesGoalList, function(idx, val) {
			
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
    saveSalesGoal : function() {
    	var view = this;
    	var _name = "";
    	var _brandId = "";
    	var _storeId = "";

    	
    	if (_data.franId == "" || _data.franId == undefined) {
    		alert(i18n.BO4001);
    		return false;
    	}

    	if (_data.brandId == "" || _data.brandId == undefined) {
    		alert(i18n.BO4002);
    		return false;
    	}
    	
    	if (_data.storeId == "" || _data.storeId == undefined) {
    		alert(i18n.BO4003);
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
					    				 brandId : _data.brandId ,
										 storeId : _data.storeId ,
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

			$('#salesGoal-modal').html(Mustache.to_html(salesGoalModal, data));
			view.form_init('POST', $('#salesGoal-modal'));

    	} else {
    		alert(i18n.BO4002);
    		$('#salesGoal-modal').html('');
    		$('#salesGoal-modal').modal('toggle');
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
    				var _template = Mustache.to_html(salesGoalModal, $.extend(data, _def_form_mustache));

    				$('#salesGoal-modal').html(Mustache.to_html(_template, view.buildOptions(response.data)));
    				view.form_init('PUT', $('#salesGoal-modal'));

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
	    		    		$('#salesGoal-modal').modal('toggle');
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
		    		$('#salesGoal-modal').html('');
		    		$('#salesGoal-modal').modal('toggle');
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