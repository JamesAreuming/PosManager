/*
 * Filename	: stampSumManage.js
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
    
    'text!templates/reward/stampSumManage.html',
    'text!templates/reward/stampSumChartAndData.html',
    
    
    'bootstrap-daterangepicker'
    ,'fusioncharts'
    ,'fusioncharts.theme.fint'
    ,'jquery.tabletoCSV'
], function (Backbone, Mustache, Common, i18n, template, chartAndData) {
    'use strict';

    var _data = {};

    var targetUrl = './model/reward/StampSum';

	var ContentView = Backbone.View.extend({
		el: $('#main-wrapper'),
	    initialize: function () {
	        this.trigger('remove-event');
	    	this.template = template;
	    	this.listenTo(this.model, 'sync', this.render);
	    },
	    events: {
	    	'click button#search' : 'search',
    		'click button#csv-exp' : 'csv_exp',
    		'click .add-on-datepicker' : 'trigger_calender'
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
	      		$("#search-table").empty();
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
				$("#search-table").empty();
	        });  //END : brand select change
	    	
	    	$('#store_select').select2().on('change', function(){
				//브랜드와 해당 매장에 관련된 카테고리 분류정보 셋팅
				_data.storeId =  $(this).val();
				$("#search-table").empty();
	        });
	    },
	    search : function() {
	    	var view = this;

	    	if(_data.brandId == ""){
	    		alert(i18n.BO4002);
	    		return false;
	    	}

	    	if ($('#daterangepicker').val() == 'Invalid date - Invalid date') {
	    		alert(i18n.BO4032);
	    		return false;
	    	}

	    	var data = {};
	    	data.franId = _data.franId;
	    	data.brandId = _data.brandId;
	    	data.storeId = _data.storeId;
	    	
			$.ajax({
				url : targetUrl,
				data : {
					data : JSON.stringify($.extend(data, $('form').serializeObject()))
				},
				async: false,
				success : function(result) {
					if (result.success) {
						var data = {};
						data.list = result.list;
				        data.option1 = $('[name=option1] option:selected').val();
				        data.summaryYn = data.option1 == 'summary' || data.option1 == null ? true : false;
				        data.searchYn = true;
				        view.summaryYn = data.summaryYn;
				        view.searchYn = true;

				        $('form').attr('id', 're-search');

				        data.i18n = i18n;
						$('#search-table').html(Mustache.to_html(chartAndData, data));
						view.tableHtml(result.list, data.option1);

						//$('#be-search').remove();
						view.convertChart(result);
					}
				}
			});
	    },
	    csv_exp: function () {
	    	if (this.searchYn) {
	    		$(this.summaryYn == true ? '#summary-table' : '#etc-table').tableToCSV();
	    	}
	    	else {
	    		alert(i18n.BO4030);
	    	}
	    },
	    tableHtml: function (data, option1) {
	    	var html = '';

	    	html += '<thead>';
	    	$.each(data, function(idx, val) {
	    		if (idx == 0) {
					html += '<tr class="dataTables_wrapper_th_min">';
					for (var key in val) {
						html += '<th>' + val[key] + '</th>';
					}
					html += '</tr>';
	    		}
	    		return false;
	    	});
	    	html += '</thead>';

	    	html += '<tbody>';
			$.each(data, function(idx, val) {
	    		if (idx > 0) {
	    			html += '<tr class="even gradeC">';

					for (var key in val) {
						html += '<td>' + val[key] + '</td>';
					}
					html += '</tr>';
	    		}
			});
			html += '</tbody>';

	    	$('#etc-table').html(html);
	    },
	    convertChart : function(data) {
        	var dayMonth = data.dayMonth;

        	var dayMonthChart = new FusionCharts({
        		type: 'line',
        		renderAt: 'dayMonthChart',
        		width: '100%',
        		height: '230',
        		dataFormat: 'json',
        		dataSource: {
        			"chart": {
        				"theme": "fint",
        				"caption": "",
        				"subCaption": dayMonth == 'day' ? "DAY" : "MONTH",
        				"xAxisName": dayMonth == 'day' ? "DAY" : "MONTH",
        				"yAxisName": "Count",
        				"showValues": "0",
				        "exportEnabled": "1"
        			},
        			"data": dayMonth == 'day' ? data.dayChart : data.monthChart,
        		}
        	});
        	dayMonthChart.render();

        	var dayofweekChart = new FusionCharts({
        		type: 'column2d',
        		renderAt: 'dayofweekChart',
        		width: '100%',
        		height: '230',
        		dataFormat: 'json',
        		dataSource: {
        			"chart": {
        				"theme": "fint",
        				"caption": "",
        				"subCaption": "DAY OF WEEK",
        				"xAxisName": "Day Of Week",
        				"yAxisName": "Count",
        				"showValues": "0",
				        "exportEnabled": "1"
        			},
        			"data": data.dayofweekChart
        		}
        	});
        	dayofweekChart.render();

        	var hourChart = new FusionCharts({
        		type: 'line',
        		renderAt: 'hourChart',
        		width: '100%',
        		height: '230',
        		dataFormat: 'json',
        		dataSource: {
        			"chart": {
        				"theme": "fint",
        				"caption": "",
        				"subCaption": "TIME DF DAY",
        				"xAxisName": "Hour",
        				"yAxisName": "Count",
        				"showValues": "0",
				        "exportEnabled": "1",
				        "labelDisplay": "rotate",
                       	"slantlabels": "1"
        			},
        			"data": data.hourChart
        		}
        	});
        	hourChart.render();
	    },
	    trigger_calender : function(event){
	    	var _this = $(event.target);
	    	var _cal = _this.closest('div').find('.date-picker');
	    	_cal.trigger('click');    	
	    }

    });

	return ContentView;
});